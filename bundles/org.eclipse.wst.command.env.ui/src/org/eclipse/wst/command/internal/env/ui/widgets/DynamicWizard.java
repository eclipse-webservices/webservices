/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.command.internal.env.ui.widgets;

import java.net.MalformedURLException;
import java.net.URL;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.wst.command.internal.env.context.PersistentResourceContext;
import org.eclipse.wst.command.internal.env.core.common.Condition;
import org.eclipse.wst.command.internal.env.core.common.MessageUtils;
import org.eclipse.wst.command.internal.env.core.data.DataFlowManager;
import org.eclipse.wst.command.internal.env.core.data.DataMappingRegistryImpl;
import org.eclipse.wst.command.internal.env.core.fragment.CommandFragment;
import org.eclipse.wst.command.internal.env.core.fragment.SequenceFragment;
import org.eclipse.wst.command.internal.env.core.fragment.SimpleFragment;
import org.eclipse.wst.command.internal.env.ui.eclipse.EclipseEnvironment;
import org.eclipse.wst.command.internal.env.ui.eclipse.EclipseStatusHandler;
import org.osgi.framework.Bundle;


/**
 * This class is used to add dynamic wizards into Eclipse.  When adding
 * a dynamic wizard into eclipse two entries need to be made in the plugin.xml
 * file.  This first entry tells Eclipse that a new wizard should be added
 * and it references this class.  The second entry specifies detailed information
 * that is specific to the wizard to be added.  These two plugin.xml entries
 * are linked together via their id attributes.  For example here is an
 * entry that registers a new wizard with eclipse.  Note the class attribute
 * references this class.
 * <pre>
 *   &lt;extension
 *        point="org.eclipse.ui.newWizards"&gt;
 *     &lt;wizard
 *           name="%PLUGIN_NEW_WIZARD_NAME_WS_CLIENT"
 *           icon="icons/full/ctool16/newclient_webserv_wiz.gif"
 *           category="org.eclipse.jst.ws.ui.new"
 *           class="org.eclipse.wst.command.env.ui.widgets.DynamicWizard"
 *           id="org.eclipse.jst.ws.consumption.ui.wizard.client.clientwizard"&gt;
 *        &lt;description&gt;
 *           %PLUGIN_NEW_WIZARD_DESC_WS_CLIENT
 *        &lt;/description&gt;
 *        &lt;selection
 *              class="org.eclipse.core.resources.IResource"&gt;
 *        &lt;/selection&gt;
 *     &lt;/wizard&gt;
 *   &lt;/extension&gt;
 *
 * </pre>
 * Here is the dynamicWizard entry for this particular wizard.  This id attributes
 * link these two entries together.  The class attribute must specify a class that
 * implements the org.eclipse.wst.command.env.ui.widgets.CommandWidgetBinding interface.  This
 * interface specifies what the UI should look like and the commands the should
 * be executed in the wizard.
 * 
 * <pre>
 *   &lt;extension
 *        point="org.eclipse.wst.command.env.dynamicWizard"&gt;
 *     &lt;dynamicWizard
 *           iconbanner="icons/full/wizban/webservicesclient_wiz.gif"
 *           class="org.eclipse.jst.ws.consumption.ui.widgets.binding.ClientWidgetBinding"
 *           title="%WIZARD_TITLE_WSC"
 *           id="org.eclipse.jst.ws.consumption.ui.wizard.client.clientwizard"&gt;
 *     &lt;/dynamicWizard&gt;
 *   &lt;/extension&gt;
 * </pre>
 *
 */
public class DynamicWizard extends Wizard implements INewWizard, IExecutableExtension
{
  private   String                  iconBannerName_;
  private   Bundle                  bundle_;
  private   SimpleCanFinishRegistry canFinishRegistry_; 
  private   WizardPageManager       pageManager_;
  private   String                  wizardTitle_;
  private   DataObjectCommand       dataObjectCommand_ = null;

  protected IWizardPage             startPage_ = null;
  protected IConfigurationElement   wizardElement_;
  protected IConfigurationElement   originalElement_;
  protected CommandWidgetBinding    commandWidgetBinding_;
  
  public DynamicWizard()
  {
	setNeedsProgressMonitor(true);
  }
  
  /**
   * This method is called when the Dynamic wizard is created by eclipse.
   * We need to find a dynamicWizard extension point that matches the id
   * for this wizard.  Once the extension point is found it will contain
   * a reference to a CommandWidgetBinding class which defines the widgets 
   * and commands for this wizard.
   * 
   * @see org.eclipse.core.runtime.IExecutableExtension#setInitializationData(org.eclipse.core.runtime.IConfigurationElement, java.lang.String, java.lang.Object)
   */
  public void setInitializationData( IConfigurationElement config,
			                         String                propertyName,
			                         Object                data )
    throws CoreException
  {
    String wizardId   = config.getAttribute( "id" );
    
    setInitialData( wizardId );
    originalElement_ = config;
  }
  
  /**
   * This method should only be called if the Dynamic Wizard is being
   * launched through some other mechanise other than the Eclipse New Wizard
   * mechanism.
   * 
   * @param binding        the command widget binding for this wizard.
   * @param descriptor     the plugin descriptor where the icon banner is
   *                       located.
   * @param iconBannerPath the icon banner path for this wizard.  The path
   *                       is relative to the plugin specified by the 
   *                       descriptor parameter.
   * @param wizardTitle    the wizard title
   */
  public void setInitialData( CommandWidgetBinding binding,
                              Bundle               bundle,
                              String               iconBannerPath,
                              String               wizardTitle )
  {
    commandWidgetBinding_ = binding;
    bundle_               = bundle;
    iconBannerName_       = iconBannerPath;
    wizardTitle_          = wizardTitle;
  }
  
  /**
   * Sets the initial data based on the dynamic wizard id.
   * 
   * @param wizardId
   */
  public void setInitialData( String wizardId ) throws CoreException
  {
    IExtensionRegistry    registry      = Platform.getExtensionRegistry();
    IExtensionPoint       point         = registry.getExtensionPoint("org.eclipse.wst.command.env.dynamicWizard");
    IExtension[]          extensions    = point.getExtensions();
    
    for( int index = 0; index < extensions.length; index++) 
    {
      IConfigurationElement[] elements = extensions[index].getConfigurationElements();
      
      if( elements.length == 1 && elements[0].getName().equals( "dynamicWizard") ) 
      {
        String id = elements[0].getAttribute( "id" );
        
        if( id != null && id.equals( wizardId ) )
        {
          wizardElement_ = elements[0];
          break;
        }
      }
    }
        
    if( wizardElement_ == null )
    {
      MessageUtils msg = new MessageUtils( "org.eclipse.wst.command.env.ui.widgets.environment", this );
      Status status = new Status( Status.ERROR, "id", 0, msg.getMessage( "MSG_ERROR_WIZARD_ID_NOT_FOUND", new String[]{ wizardId}) , null );
      throw new CoreException( status );
    }
  }
    
  /**
   * This method is called by the eclipse framework to initialize the
   * wizard. 
   * 
   * @param workbench the eclipse workbench
   * @param selection the initial selection that the user has made within
   *                  eclipse.
   */
  public void init ( IWorkbench workbench, IStructuredSelection selection )
  {    
    PersistentResourceContext context        = PersistentResourceContext.getInstance();
    EclipseStatusHandler      handler        = new EclipseStatusHandler();
    EclipseEnvironment        environment    = new EclipseEnvironment( null, context, handler );
    
    DataMappingRegistryImpl dataRegistry_   = new DataMappingRegistryImpl();
    SimpleWidgetRegistry    widgetRegistry_ = new SimpleWidgetRegistry();  
    DataFlowManager         dataManager     = new DataFlowManager( dataRegistry_, environment );
    
    try
    {
      commandWidgetBinding_ = (CommandWidgetBinding)wizardElement_.createExecutableExtension( "class" );
      bundle_               = Platform.getBundle( wizardElement_.getNamespace() );
      iconBannerName_       = wizardElement_.getAttribute( "iconbanner" );
      wizardTitle_          = wizardElement_.getAttribute( "title" );
    }
    catch( CoreException exc )
    {
      exc.printStackTrace();
    }
    
	dataObjectCommand_ = new DataObjectCommand();
    startPage_         = null;
    canFinishRegistry_ = new SimpleCanFinishRegistry(); 
    pageManager_       = new WizardPageManager( widgetRegistry_, 
                                                getWizardPageFactory(), 
				                                this,
				                                dataManager,
				                                environment );  
    
    pageManager_.setRootFragment( getRootFragment( selection, pageManager_ ) );
    
    commandWidgetBinding_.registerWidgetMappings( widgetRegistry_ );
    commandWidgetBinding_.registerDataMappings( dataRegistry_ );
    commandWidgetBinding_.registerCanFinish( canFinishRegistry_ );
        
    // If a page is not complete ensure that canFinish is false.
    Condition canFinish = new Condition()
                          {
                            public boolean evaluate()
                            {
                              IWizardPage currentPage = pageManager_.getCurrentPage();
                              
                              return currentPage != null ? currentPage.isPageComplete() : true;
                            }
                          }; 
    
    canFinishRegistry_.add( canFinish );
    
    // Set that we need a progress monitor.
    setNeedsProgressMonitor( true );
    
    // Ensure that the next/back buttons are enabled.
	setForcePreviousAndNextButtons( true );
    
    // Set the icon banner if specified.
    if( iconBannerName_ != null && iconBannerName_.length() > 0 )
    {
      try
      {
        URL installURL        = bundle_.getEntry("/");
        URL imageURL          = new URL(installURL, iconBannerName_ );
        ImageDescriptor image = ImageDescriptor.createFromURL(imageURL);
        
        if( image != null )
        {
          setDefaultPageImageDescriptor( image );
        }
      }
      catch (MalformedURLException e)
      {
      }
    }
    
    if( wizardTitle_ != null && wizardTitle_.length() > 0 )
    {
      setWindowTitle( wizardTitle_ );
    }
  }
  
  // Subclasses can override this.
  protected WizardPageFactory getWizardPageFactory()
  {
    return new SimplePageFactory();
  }
  
  /**
  * Returns true if all pages are complete.
  * @return True if all pages are complete.
  */
  public boolean canFinish ()
  {
  	int     length = canFinishRegistry_.size();
  	boolean result = true;
  	
    for( int index = 0; index < length; index++ )
    {
      Condition condition = (Condition)canFinishRegistry_.elementAt( index );
    
      result = condition.evaluate();
      
      if( !result ) break;
    }
    
    return result;
  }

  /**
    * Returns the first page for this wizard.
    *
    * @return the first page for this wizard.
  **/
  public IWizardPage getStartingPage()
  {
  	if( startPage_ == null )
  	{
  		startPage_ = pageManager_.getNextPage();
  	}
  	
    return startPage_;
  }

  /**
    * Returns the next page after page and
    * runs the appropriate tasks.
    *
    * @return the next page after page.
  **/
  public IWizardPage getNextPage( IWizardPage page )
  {   
    return pageManager_.getNextPage();
  }
  
  /**
  */
  public boolean performFinish ()
  { 
    boolean result = pageManager_.performFinish();
    
    if( result ) cleanup();
    
    return result;
  }

  /**
   * Undo fragments
  */
  public boolean performCancel ()
  {
  	boolean result = pageManager_.performCancel();
  	
  	if( result ) cleanup();
  	
  	return result;
  }  
      
  public Object getDataObject()
  {
    return dataObjectCommand_.getDataObject();  
  }
  
  protected CommandFragment getRootFragment( IStructuredSelection selection, WizardPageManager pageManager )
  {
    SequenceFragment root = new SequenceFragment();
    
    root.add( new SimpleFragment( new SelectionCommand( selection ), "" ) );
    root.add( new SimpleFragment( new CurrentPageCommand( pageManager ), "" ) );
    root.add( commandWidgetBinding_.create().create() );
	root.add( new SimpleFragment( dataObjectCommand_, "" ));
    
    return root;
  }
  
  protected IConfigurationElement getConfigElement()
  {
    return wizardElement_;
  }
  
  /**
   * This method frees up the memory held by this object.
   *
   */
  private void cleanup()
  {
	iconBannerName_       = null;;
	bundle_               = null;;
	canFinishRegistry_    = null; 
	pageManager_          = null;
	wizardTitle_          = null;
	dataObjectCommand_    = null;
	startPage_            = null;
	commandWidgetBinding_ = null;  
  }
}