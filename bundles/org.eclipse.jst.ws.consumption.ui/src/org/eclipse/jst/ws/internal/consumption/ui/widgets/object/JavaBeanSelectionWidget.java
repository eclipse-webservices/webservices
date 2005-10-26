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
package org.eclipse.jst.ws.internal.consumption.ui.widgets.object;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jst.j2ee.webservice.wsdd.BeanLink;
import org.eclipse.jst.j2ee.webservice.wsdd.internal.impl.PortComponentImpl;
import org.eclipse.jst.j2ee.webservice.wsdd.internal.impl.ServiceImplBeanImpl;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.internal.consumption.common.JavaResourceFilter;
import org.eclipse.jst.ws.internal.ui.common.UIUtils;
import org.eclipse.jst.ws.internal.ui.dialog.DialogUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.command.internal.env.core.common.MessageUtils;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetDataEvents;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;

public class JavaBeanSelectionWidget extends AbstractObjectSelectionWidget implements IObjectSelectionWidget
{
  private String             pluginId_ = "org.eclipse.jst.ws.consumption.ui";
  private IProject           serverProject_ = null;
  private String             serverComponentName_ = null;
  private Composite          parent_ = null;
  private JavaResourceFilter filter_ = new JavaResourceFilter();
  private IResource          initialResource_ = null;
  private Listener statusListener;
  
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
    this.statusListener = statusListener;
    MessageUtils msgUtils = new MessageUtils( pluginId_ + ".plugin", this );
    UIUtils      uiUtils  = new UIUtils(msgUtils, pluginId_ ); 
    
    parent_ = parent;
    
    Composite group = uiUtils.createComposite( parent, 4 );
    
    group.setToolTipText( msgUtils.getMessage("TOOLTIP_PBCL_PAGE") );
    PlatformUI.getWorkbench().getHelpSystem().setHelp( group, pluginId_ + "." + INFOPOP_PBCL_PAGE );
    
    beanClassText_ = uiUtils.createText( group, "LABEL_BEAN_CLASS_NAME",
                                         "TOOLTIP_PBCL_TEXT_BEAN_CLASS",
                                         INFOPOP_PBCL_TEXT_BEAN_CLASS,
                                         SWT.SINGLE | SWT.BORDER);
    beanClassText_.addModifyListener(
      new ModifyListener()
      {
        public void modifyText(ModifyEvent e)
        {
          handleModifyBeanClassText();
        }
      }
    );
    
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
  
  private void handleModifyBeanClassText()
  {
    statusListener.handleEvent(null);
    serverProject_ = null;
    serverComponentName_ = null;
  }

  private void handleBrowseClasses()
  {
    Shell shell = parent_.getShell();
    IType itype = DialogUtils.browseClassesAsIType(shell, ResourcesPlugin.getWorkspace().getRoot().getProjects(), new ProgressMonitorDialog(shell));
    if (itype != null)
    {
      beanClassText_.setText(itype.getFullyQualifiedName());
      try
      {
        IResource res = itype.getCorrespondingResource();
        if (res != null)
        {
          serverProject_ = res.getProject();
          IVirtualComponent comp = ResourceUtils.getComponentOf(res);
          if (comp!=null)
          {
            serverComponentName_ = comp.getName();
          }
        }
        else
        {
          serverProject_ = null;
          serverComponentName_ = null;
        }
      }
      catch (JavaModelException jme)
      {
        serverProject_ = null;
        serverComponentName_ = null;
      }
    }
  }
  
  private void handleBrowseFiles()
  {
    IResource topResource = ResourceUtils.getWorkspaceRoot();
    IResource resource = DialogUtils.browseResources( parent_.getShell(), topResource, initialResource_, filter_ );
    setBeanClass(resource);
  }
  
  private void setBeanClass(IResource resource)
  {
    if( resource != null && resource instanceof IFile )
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
        serverProject_ =  ResourceUtils.getProjectOf(path);
        IVirtualComponent comp = ResourceUtils.getComponentOf(resource);
        
        if (comp!=null)
        {	
          serverComponentName_ = comp.getName();
        }        
        
      }
    }
  }

  private void setBeanClass(ServiceImplBeanImpl serviceImpl)
  {
    if( serviceImpl != null )
    {
      EObject eObject = serviceImpl.eContainer();
      if(eObject instanceof PortComponentImpl){
      	PortComponentImpl pci = (PortComponentImpl)eObject;
      	String beanClass = pci.getServiceEndpointInterface();
      	beanClassText_.setText(beanClass);
      }
    }
  }
  
  private void setBeanClass(BeanLink serviceImpl)
  {
    if( serviceImpl != null )
    {
      EObject eObject = serviceImpl.eContainer();
      if(eObject instanceof ServiceImplBeanImpl){
      	setBeanClass((ServiceImplBeanImpl)eObject);
      }
    }
  }
  
  public void setInitialSelection(IStructuredSelection initialSelection)
  {
    if (initialSelection != null && !initialSelection.isEmpty())
    {
      Object object = initialSelection.getFirstElement();
      if (object instanceof IFile)
      {
      	IFile iFile = (IFile)object;
      	String fileExt = iFile.getFileExtension().toLowerCase();
      	if (fileExt.equals("java") || fileExt.equals("class"))
          setBeanClass(iFile);
      }
      else if (object instanceof ICompilationUnit)
      	setBeanClass(((ICompilationUnit)object).getResource());
      else if (object instanceof ServiceImplBeanImpl)
      	setBeanClass((ServiceImplBeanImpl)object);
      else if(object instanceof BeanLink)
      	setBeanClass((BeanLink)object);
    }
  }
  
  public IStructuredSelection getObjectSelection()
  {
    return new StructuredSelection(beanClassText_.getText());
  }
  
  public IProject getProject()
  {
    return serverProject_;
  }

  public String getComponentName()
  {
    return serverComponentName_;
  }
  
  public IStatus getStatus()
  {
    String beanClassName = beanClassText_.getText().trim();
    if (beanClassName == null || beanClassName.length() <= 0)
    {
      MessageUtils msgUtils = new MessageUtils(pluginId_ + ".plugin", this);
      return StatusUtils.errorStatus( msgUtils.getMessage("PAGE_MSG_BEAN_CANNOT_BE_EMPTY") );
    }
    return Status.OK_STATUS;
  }
  
  public IStatus validateSelection(IStructuredSelection objectSelection)
  {
    return Status.OK_STATUS;
  }
}