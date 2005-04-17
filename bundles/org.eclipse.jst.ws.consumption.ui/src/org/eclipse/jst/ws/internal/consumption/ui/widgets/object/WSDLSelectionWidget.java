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

import java.io.File;
import java.net.MalformedURLException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jst.j2ee.webservice.wsclient.ServiceRef;
import org.eclipse.jst.ws.internal.common.J2EEActionAdapterFactory;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.WSDLSelectionConditionCommand;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.WSDLSelectionTreeWidget;
import org.eclipse.jst.ws.internal.ui.common.DialogResourceBrowser;
import org.eclipse.jst.ws.internal.ui.common.FileExtensionFilter;
import org.eclipse.jst.ws.internal.ui.common.UIUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.command.env.core.common.MessageUtils;
import org.eclipse.wst.command.env.core.common.SimpleStatus;
import org.eclipse.wst.command.env.core.common.Status;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetDataEvents;
import org.eclipse.wst.ws.internal.parser.discovery.WebServicesParserExt;
import org.eclipse.wst.ws.internal.parser.wsil.WebServiceEntity;
import org.eclipse.wst.ws.internal.parser.wsil.WebServicesParser;
import org.eclipse.wst.wsdl.internal.impl.ServiceImpl;
import org.eclipse.wst.wsdl.util.WSDLResourceImpl;

public class WSDLSelectionWidget extends AbstractObjectSelectionWidget implements IObjectSelectionWidget, Runnable
{
  private String              pluginId_;
  private MessageUtils        msgUtils_;
  private FileExtensionFilter wsFilter_;
  private WebServicesParser webServicesParser;

  private Composite parent_;
  private Listener  statusListener_;
  private WSDLSelectionTreeWidget tree;
  
  /*CONTEXT_ID PCON0001 for the WSDL Selection Page*/
  private final String INFOPOP_PCON_PAGE = "PCON0001";

  /*CONTEXT_ID PCON0002 for the WSDL Document text field of the WSDL Selection Page*/
  private final String INFOPOP_PCON_TEXT_WSDL = "PCON0002";
  private Text webServiceURI;

  /*CONTEXT_ID PCON0003 for the WSDL Resource Browse button of the WSDL Selection Page*/
  private final String INFOPOP_PCON_BUTTON_BROWSE_WSDL = "PCON0003";
  private Button wsBrowseButton_;
  
  public WSDLSelectionWidget()
  {
    pluginId_ = "org.eclipse.jst.ws.consumption.ui";
    msgUtils_ = new MessageUtils( pluginId_ + ".plugin", this );
    wsFilter_ = new FileExtensionFilter(new String[] {"wsdl", "wsil", "html"});
    webServicesParser = new WebServicesParserExt();
  }
  
  public WidgetDataEvents addControls( Composite parent, Listener statusListener )
  {
    UIUtils uiUtils  = new UIUtils( msgUtils_, pluginId_ );
    parent_          = parent;
    statusListener_  = statusListener;

	  parent.setToolTipText( msgUtils_.getMessage( "TOOLTIP_PCON_PAGE" ) );
	  PlatformUI.getWorkbench().getHelpSystem().setHelp( parent, pluginId_ + "." + INFOPOP_PCON_PAGE );
    
    Composite wsdlGroup = uiUtils.createComposite( parent, 2, 5, 0 );
    
    Label wsLabel = new Label( wsdlGroup, SWT.WRAP);
    wsLabel.setText( msgUtils_.getMessage("LABEL_WS_SELECTION"));
    GridData gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
    gd.horizontalSpan = 2;
    wsLabel.setLayoutData(gd);
    wsLabel.setToolTipText( msgUtils_.getMessage("TOOLTIP_PCON_TEXT_WS") );
    
    webServiceURI = uiUtils.createText( wsdlGroup, null, "TOOLTIP_PCON_TEXT_WS", INFOPOP_PCON_TEXT_WSDL, SWT.SINGLE | SWT.BORDER );
    webServiceURI.addModifyListener(
      new ModifyListener()
      {
        public void modifyText(ModifyEvent event)
        {
          handleWebServiceURIModifyEvent();
        }
      });
    webServiceURI.addListener( SWT.Modify, statusListener );

    wsBrowseButton_ = uiUtils.createPushButton( wsdlGroup, "BUTTON_BROWSE", "TOOLTIP_PCON_BUTTON_BROWSE_WS", INFOPOP_PCON_BUTTON_BROWSE_WSDL );
    wsBrowseButton_.addSelectionListener(
      new SelectionListener()
      {
        public void widgetDefaultSelected(SelectionEvent event)
        {
          handleWSDLButton();
        }
        
        public void widgetSelected(SelectionEvent event)
        {
          handleWSDLButton();
        } 
      });

    tree = new WSDLSelectionTreeWidget();
    tree.addControls(parent, statusListener);
    tree.setWebServicesParser(webServicesParser);
    return this;
  }
  
  private void handleWebServiceURIModifyEvent()
  {
    if (webServiceURI.getText().indexOf(':') > 0)
      Timer.newInstance(Display.getCurrent(), this).startTimer();
    else
      handleWebServiceURI();
    statusListener_.handleEvent(null);
  }
  
  private void handleWebServiceURI()
  {
    String wsURI = webServiceURI.getText();
    if (wsURI.indexOf(':') < 0)
    {
      IFile file = uri2IFile(wsURI);
      if (file != null)
        wsURI = iFile2URI(file);
    }
    if (wsURI != null && wsURI.indexOf(':') >= 0 && webServicesParser.getWebServiceEntityByURI(wsURI) == null)
    {
      WSDLSelectionConditionCommand cmd = new WSDLSelectionConditionCommand();
      cmd.setWebServicesParser(webServicesParser);
      cmd.setWebServiceURI(wsURI);
      cmd.execute(null);
    }
    WebServiceEntity entity = webServicesParser.getWebServiceEntityByURI(wsURI);
    if (entity != null && entity.getType() == WebServiceEntity.TYPE_WSDL)
      tree.setEnabled(false);
    else
      tree.setEnabled(true);
    tree.setWebServiceURI(wsURI);
    tree.refreshTreeViewer();
  }
  
  public void run()
  {
    handleWebServiceURI();
    statusListener_.handleEvent(null);
  }

  private void handleWSDLButton()
  {
    DialogResourceBrowser dialog = new DialogResourceBrowser( parent_.getShell(), null, wsFilter_);
    dialog.open();
    IResource res = dialog.getFirstSelection();
    if( res != null ) webServiceURI.setText( res.getFullPath().toString() );
    statusListener_.handleEvent(null);
  }
  
  public Status getStatus()
  {
    // Timer validation
    /*
     * Commenting out because we don't want to block fast typers from hitting Next/Finish 
    if (Timer.isRunning())
      return new SimpleStatus("", msgUtils_.getMessage("PAGE_MSG_LOADING_WEB_SERVICE_URI"), Status.ERROR);
    */

    // Validate the String representation of the Web service URI
    // For example, is it pointing to an existing resource in the workspace?
    String wsPath  = webServiceURI.getText();
    if( wsPath == null || wsPath.length() <= 0 )
      return new SimpleStatus("", msgUtils_.getMessage("PAGE_MSG_INVALID_WEB_SERVICE_URI"), Status.ERROR);
    else if( wsPath.indexOf(':') < 0 )
    {
      IResource res = ResourceUtils.findResource(wsPath);
      if( res == null )
        return new SimpleStatus("", msgUtils_.getMessage("PAGE_MSG_NO_SUCH_FILE", new Object[] {wsPath}), Status.ERROR);
      else if( res.getType() != IResource.FILE )
        return new SimpleStatus("", msgUtils_.getMessage("PAGE_MSG_INVALID_WEB_SERVICE_URI"), Status.ERROR);
    }

    // Validate the content of the Web service URI
    // For example, is selection a WSDL URI?
    if (!Timer.isRunning() && tree.isEnabled())
    {
      Status status = tree.getStatus();
      if (status != null)
      {
        int severity = status.getSeverity();
        if (severity == Status.ERROR || severity == Status.WARNING)
          return status;
      }
    }
    else
    {
      if( wsPath.indexOf(':') < 0 )
      {
        String wsdlURI = iFile2URI((IFile)ResourceUtils.findResource(wsPath));
        if (webServicesParser.getWSDLDefinition(wsdlURI) == null)
          return new SimpleStatus("", msgUtils_.getMessage("PAGE_MSG_SELECTION_MUST_BE_WSDL"), Status.ERROR);
      }
    }

    // OK status
    return new SimpleStatus( "" );
  }
  
  private IFile uri2IFile(String uri)
  {
    IResource res = ResourcesPlugin.getWorkspace().getRoot().findMember(uri);
    if (res instanceof IFile)
      return (IFile)res;
    else
      return null;
  }
  
  private String iFile2URI(IFile file)
  {
  	File f = file.getLocation().toFile();
    try
    {
      return f.toURL().toString();
    }
    catch (MalformedURLException murle)
    {
    }
    return f.toString();
  }

  public void setInitialSelection(IStructuredSelection initialSelection)
  {
    if (initialSelection != null && !initialSelection.isEmpty())
    {
      Object object = initialSelection.getFirstElement();
      String wsdlURI = toWsdlURI(object);
      if (wsdlURI != null)
      {
        webServiceURI.setText(wsdlURI);
        handleWebServiceURI();
      }
    }
  }
  
  private String toWsdlURI(Object object)
  {
    if (object instanceof ServiceImpl)
      return J2EEActionAdapterFactory.getWSDLURI((ServiceImpl)object);
    else if (object instanceof WSDLResourceImpl)
      return J2EEActionAdapterFactory.getWSDLURI((WSDLResourceImpl)object);
    else if (object instanceof ServiceRef)
      return J2EEActionAdapterFactory.getWSDLURI((ServiceRef)object);
    else if (object instanceof IFile)
      return ((IFile)object).getFullPath().toString();
    else if (object instanceof String)
      return (String)object;
    else
      return null;
  }
  
  private String toAbsoluteWsdlURI(Object object)
  {
    if (object instanceof IFile)
    {
      try
      {
        return ((IFile)object).getLocation().toFile().toURL().toString();
      }
      catch (MalformedURLException murle)
      {
      }
    }
    return toWsdlURI(object);
  }

  public IStructuredSelection getObjectSelection()
  {
    return new StructuredSelection( 
               new WSDLSelectionWrapper( webServicesParser,
                                         new StructuredSelection(tree.getWsdlURI()) ) );
  }
  
  public WebServicesParser getWebServicesParser()
  {
    return webServicesParser;
  }
  
  public Status validateSelection(IStructuredSelection objectSelection)
  {
    return new SimpleStatus("");
  }
  
  public IProject getProject()
  {
    String wsdlURI = tree.getWsdlURI();
    if (wsdlURI != null)
    {
      IProject p = getProjectFromURI(wsdlURI);
      if (p!=null && p.exists())
        return p;
      
      String wsRelPath = webServiceURI.getText();
      IResource wsRes = ResourceUtils.findResource(wsRelPath);
      if (wsRes!=null && wsRes instanceof IFile)
      {
        IProject p2 = ((IFile)wsRes).getProject();
        return p2;
      }
      
    }
    return null;
  }
  
  private IProject getProjectFromURI(String uri)
  {
    IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
    String wkspcRootLoc = root.getLocation().toString();
    int idx = uri.indexOf(wkspcRootLoc);
    if (idx != -1) 
    {
      String relPath = uri.substring(wkspcRootLoc.length()+idx);
      IResource res = root.findMember(new Path(relPath));
      if (res instanceof IFile)
      {
        IProject p = ((IFile)res).getProject();
        return p;
      }
    }
    return null;
  }
}