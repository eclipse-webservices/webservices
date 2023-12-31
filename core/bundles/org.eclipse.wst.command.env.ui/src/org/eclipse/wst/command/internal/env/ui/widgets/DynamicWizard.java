/*******************************************************************************
 * Copyright (c) 2004, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060223   129232 pmoogk@ca.ibm.com - Peter Moogk
 * 20060822   154750 pmoogk@ca.ibm.com - Peter Moogk
 * 20070529   187780 pmoogk@ca.ibm.com - Peter Moogk
 * 20080613   236523 makandre@ca.ibm.com - Andrew Mak, Overwrite setting on Web service wizard is coupled with preference
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
import org.eclipse.osgi.util.NLS;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.wst.command.internal.env.context.PersistentResourceContext;
import org.eclipse.wst.command.internal.env.core.common.Condition;
import org.eclipse.wst.command.internal.env.core.context.ResourceContext;
import org.eclipse.wst.command.internal.env.core.data.DataFlowManager;
import org.eclipse.wst.command.internal.env.core.data.DataMappingRegistryImpl;
import org.eclipse.wst.command.internal.env.core.fragment.CommandFragment;
import org.eclipse.wst.command.internal.env.core.fragment.SequenceFragment;
import org.eclipse.wst.command.internal.env.core.fragment.SimpleFragment;
import org.eclipse.wst.command.internal.env.eclipse.EclipseEnvironment;
import org.eclipse.wst.command.internal.env.ui.EnvironmentUIMessages;
import org.eclipse.wst.command.internal.env.ui.eclipse.EclipseStatusHandler;
import org.eclipse.wst.command.internal.env.ui.registry.WidgetRegistry;
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
  private   String                  wizardTitle_;
  private   DataObjectCommand       dataObjectCommand_ = null;
  private   WizardPageManager       pageManager_;
  
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
    String wizardId   = config.getAttribute( "id" ); //$NON-NLS-1$
    
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
   * 
   * @deprecated Use either setInitialData(wizardId) or
   *                        setInitialData(wizardId, bundle, iconBannerPath, wizardTitle )
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
    setInitialData(wizardId, null, null, null );
  }
  
  /**
   * Sets the initial data based on the dynamic wizard id.
   * 
   * @param wizardId the ID of the wizard.
   * @param bundle the bundle used to find an icon banner.  Can be null if
   * no icon banner is specified.
   * @param iconBannerPath the path relative to the bundle specified.
   * @param wizardTitle the title of the wizard.
   * 
   * Note: normally the wizard associated with a wizardId will have an
   * icon banner and a title.  The parameters on this method allow the caller
   * to override these values.
   */
  public void setInitialData( String wizardId, Bundle bundle, String iconBannerPath, String wizardTitle ) throws CoreException
  {
    IExtensionRegistry    registry      = Platform.getExtensionRegistry();
    IExtensionPoint       point         = registry.getExtensionPoint("org.eclipse.wst.command.env.dynamicWizard"); //$NON-NLS-1$
    IExtension[]          extensions    = point.getExtensions();
    
    for( int index = 0; index < extensions.length; index++) 
    {
      IConfigurationElement[] elements = extensions[index].getConfigurationElements();
      
      if( elements.length == 1 && elements[0].getName().equals( "dynamicWizard") )  //$NON-NLS-1$
      {
        String id = elements[0].getAttribute( "id" ); //$NON-NLS-1$
        
        if( id != null && id.equals( wizardId ) )
        {
          wizardElement_ = elements[0];
          break;
        }
      }
    }
       
    bundle_         = bundle;
    iconBannerName_ = iconBannerPath;
    wizardTitle_    = wizardTitle;
    
    if( wizardElement_ == null )
    {
      Status status = new Status( Status.ERROR, "id", 0, NLS.bind(EnvironmentUIMessages.MSG_ERROR_WIZARD_ID_NOT_FOUND, new String[]{ wizardId}) , null ); //$NON-NLS-1$
      throw new CoreException( status );
    }
  }
    
  /**
   * This method is called by the eclipse framework to initialize the
   * wizard. 
   * 
   * Note: if this method is called programatically(ie. not by the eclipse
   * framework.) one of the setInitalData methods needs to be called before this
   * method is invoked.
   * 
   * @param workbench the eclipse workbench
   * @param selection the initial selection that the user has made within
   *                  eclipse.
   */
  public void init ( IWorkbench workbench, IStructuredSelection selection )
  {    
    ResourceContext           context        = PersistentResourceContext.getInstance().copy();
    EclipseStatusHandler      handler        = new EclipseStatusHandler();
    EclipseEnvironment        environment    = new EclipseEnvironment( null, context, handler );
    
    DataMappingRegistryImpl dataRegistry_   = new DataMappingRegistryImpl();
    SimpleWidgetRegistry    widgetRegistry_ = new SimpleWidgetRegistry();  
    DataFlowManager         dataManager     = new DataFlowManager( dataRegistry_, environment );
    
    try
    {
      if( wizardElement_ != null )
      {
        if( commandWidgetBinding_ == null )
        {
          commandWidgetBinding_ = (CommandWidgetBinding)wizardElement_.createExecutableExtension( "class" ); //$NON-NLS-1$
        }
        
        if( bundle_ == null )
        {
          bundle_ = Platform.getBundle( wizardElement_.getNamespace() );
        }
        
        if( iconBannerName_ == null )
        {
          iconBannerName_ = wizardElement_.getAttribute( "iconbanner" ); //$NON-NLS-1$
        }
        
        if( wizardTitle_ == null )
        {
          wizardTitle_ = wizardElement_.getAttribute( "title" ); //$NON-NLS-1$
        }
      }
    }
    catch( CoreException exc )
    {
      exc.printStackTrace();
    }
    
	dataObjectCommand_ = new DataObjectCommand();
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
        URL installURL        = bundle_.getEntry("/"); //$NON-NLS-1$
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
    return pageManager_.getStartingPage();
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
    
    root.add( new SimpleFragment( new SelectionCommand( selection ), "" ) ); //$NON-NLS-1$
    root.add( new SimpleFragment( new CurrentPageCommand( pageManager ), "" ) ); //$NON-NLS-1$
    root.add( commandWidgetBinding_.create().create() );
	root.add( new SimpleFragment( dataObjectCommand_, "" )); //$NON-NLS-1$
    
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
	  iconBannerName_       = null;
	  bundle_               = null;
	  canFinishRegistry_    = null; 
	  pageManager_          = null;
	  wizardTitle_          = null;
	  commandWidgetBinding_ = null;
	  WidgetRegistry.initialize();
  }
}
