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
package org.eclipse.jst.ws.internal.axis.creation.ui.widgets.bean;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.wizard.IWizardContainer;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.internal.consumption.common.JavaResourceFilter;
import org.eclipse.jst.ws.internal.ui.common.UIUtils;
import org.eclipse.jst.ws.internal.ui.dialog.DialogUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.help.WorkbenchHelp;
import org.eclipse.wst.command.env.core.common.MessageUtils;
import org.eclipse.wst.command.env.ui.widgets.SimpleWidgetDataContributor;
import org.eclipse.wst.command.env.ui.widgets.WidgetDataEvents;


public class BeanClassWidget extends SimpleWidgetDataContributor
{
  private String             pluginId_  = "org.eclipse.jst.ws.consumption.ui";
  private String             thisPlugin = "org.eclipse.jst.ws.axis.creation.ui";
  private IProject           serverProject_ = null;
  private Composite          parent_ = null;
  private IWizardContainer   context_ = null;
  private JavaResourceFilter filter_ = new JavaResourceFilter();
  private IResource          initialResource_ = null;
  
  /*CONTEXT_ID PBCL0001 for the Bean Selection Page*/
  private String INFOPOP_PBCL_PAGE = "PBCL0001";
  
  private Text beanClassText_;
  /*CONTEXT_ID PBCL0002 for the Bean Selection field of the Bean Selection Page*/
  private String INFOPOP_PBCL_TEXT_BEAN_CLASS = "PBCL0002";

  private Button beanClassBrowseButton_;
  /*CONTEXT_ID PBCL0003 for the Bean Class Browse button of the Bean Selection Page*/
  private String INFOPOP_PBCL_BUTTON_BEAN_CLASS_BROWSE = "PBCL0003";

  private Button beanResourceBrowseButton_;
  /*CONTEXT_ID PBCL0004 for the Bean Resource Browse button of the Bean Selection Page*/
  private String INFOPOP_PBCL_BUTTON_BEAN_RESOURCE_BROWSE = "PBCL0004";
   
  public WidgetDataEvents addControls( Composite parent, Listener statusListener )
  {
    MessageUtils msgUtils = new MessageUtils( pluginId_ + ".plugin", this );
    UIUtils      uiUtils  = new UIUtils(msgUtils, thisPlugin ); 
    
    parent_ = parent;
    
    Composite group = uiUtils.createComposite( parent, 4 );
    
    group.setToolTipText( msgUtils.getMessage("TOOLTIP_PBCL_PAGE") );
    WorkbenchHelp.setHelp( group, pluginId_ + "." + INFOPOP_PBCL_PAGE );
    
    beanClassText_ = uiUtils.createText( group, "LABEL_BEAN_CLASS_NAME",
                                         "TOOLTIP_PBCL_TEXT_BEAN_CLASS",
                                         INFOPOP_PBCL_TEXT_BEAN_CLASS,
                                         SWT.SINGLE | SWT.BORDER);
    
    beanClassBrowseButton_ = uiUtils.createPushButton( group, "BUTTON_BROWSE_CLASSES",
                                                       "TOOLTIP_PBCL_BUTTON_BEAN_CLASS_BROWSE",
                                                       INFOPOP_PBCL_BUTTON_BEAN_CLASS_BROWSE );
    beanClassBrowseButton_.addSelectionListener( new SelectionAdapter()
                                                 {
                                                   public void widgetSelected( SelectionEvent evt )
                                                   {
                                                     handleBrowseClasses();  
                                                   }
                                                 } );
    
    beanResourceBrowseButton_ = uiUtils.createPushButton( group, "BUTTON_BROWSE_FILES",
                                                          "TOOLTIP_PBCL_BUTTON_BEAN_RESOURCE_BROWSE",
                                                          INFOPOP_PBCL_BUTTON_BEAN_RESOURCE_BROWSE );
    beanResourceBrowseButton_.addSelectionListener( new SelectionAdapter()
                                                    {
                                                      public void widgetSelected( SelectionEvent evt )
                                                      {
                                                        handleBrowseFiles();  
                                                      }
                                                    } );
    
    return this;
  }
  
  private void handleBrowseClasses()
  {
    String className = DialogUtils.browseClasses( parent_.getShell(), 
                                                  serverProject_, context_ );

    if (className!=null) beanClassText_.setText(className);
    
  }
  
  private void handleBrowseFiles()
  {
    IResource topResource = serverProject_;
    
    if (topResource == null)
    {
      topResource = ResourceUtils.getWorkspaceRoot();
    }
    
    IResource resource 
      = DialogUtils.browseResources( parent_.getShell(),
                                     topResource,
				                     initialResource_,
				                     filter_ );
    
    if( resource != null )
    {
      IPath  path     = resource.getFullPath();
      String basename = path.lastSegment();
      
      if( basename != null && basename.length() > 0 )
      {
        String beanPackage = org.eclipse.jst.ws.internal.common.ResourceUtils.getJavaResourcePackageName(path);
        String beanClass = (beanPackage == null ? basename : (beanPackage + "." + basename));
        
        if( beanClass.toLowerCase().endsWith(".java") || beanClass.toLowerCase().endsWith(".class" ) )
        {
          beanClass = beanClass.substring( 0, beanClass.lastIndexOf('.') );
        }
        
        beanClassText_.setText(beanClass);
      }
    }   
  }
  
  public void setCurrentPage( IWizardPage page )
  {
    context_ = page.getWizard().getContainer();
  }

  public void setBeanClassName(String beanClassName)
  {
    beanClassText_.setText(beanClassName);
  }
  
  public String getBeanClassName()
  {
    return beanClassText_.getText().trim();  
  }
  
  public void setServerProjectName( String name )
  {
    serverProject_ = (IProject)ResourceUtils.findResource( new Path( name ).makeAbsolute() );  
  }
    
  public void setInitialResource( IResource resource )
  {
    initialResource_ = resource;  
  }  
  
}
