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
package org.eclipse.jst.ws.internal.consumption.ui.widgets;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jst.j2ee.webservice.wsclient.ServiceRef;
import org.eclipse.jst.ws.internal.common.J2EEActionAdapterFactory;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.internal.consumption.ui.plugin.WebServiceConsumptionUIPlugin;
import org.eclipse.jst.ws.internal.consumption.ui.wsil.Arguments;
import org.eclipse.jst.ws.internal.consumption.ui.wsil.TableViewerEditor;
import org.eclipse.wst.ws.internal.wsil.AddWSDLToWSILCommand;
import org.eclipse.jst.ws.internal.ui.common.DialogResourceBrowser;
import org.eclipse.jst.ws.internal.ui.common.FileExtensionFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.help.IWorkbenchHelpSystem;
import org.eclipse.wst.command.internal.env.ui.widgets.SimpleWidgetDataContributor;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetDataEvents;
import org.eclipse.wst.command.internal.provisional.env.core.common.MessageUtils;
import org.eclipse.wst.command.internal.provisional.env.core.common.SimpleStatus;
import org.eclipse.wst.command.internal.provisional.env.core.common.Status;
import org.eclipse.wst.wsdl.internal.impl.ServiceImpl;
import org.eclipse.wst.wsdl.util.WSDLResourceImpl;

/**
* Page for importing WSDL references into a WSIL in the workbench.
*/
public class ImportWSILWidget extends SimpleWidgetDataContributor
{
  /*CONTEXT_ID WSIL0001 for the WSIL Import Page*/
  private final String INFOPOP_WSIL_IMPORT_PAGE = WebServiceConsumptionUIPlugin.ID + ".WSIL0001";

  /*CONTEXT_ID WSIL0002 for the WSIL text field*/
  private final String INFOPOP_WSIL_TEXT_WSIL = WebServiceConsumptionUIPlugin.ID + ".WSIL0002";

  /*CONTEXT_ID WSIL0003 for the WSIL browse button*/
  private final String INFOPOP_WSIL_BUTTON_BROWSE_WSIL = WebServiceConsumptionUIPlugin.ID + ".WSIL0003";

  /*CONTEXT_ID WSIL0004 for the WSDL table*/
  private final String INFOPOP_WSIL_TABLE_WSDL = WebServiceConsumptionUIPlugin.ID + ".WSIL0004";

  private final String INSPECTION_WSIL = "inspection.wsil";

  private Text wsil_;
  private Button browse_;
  private TableViewerEditor wsdls_;
  
  private MessageUtils msgUtils;
  private IStructuredSelection selection;

  /**
  * Constructs a new page.
  */
  public ImportWSILWidget()
  {
    msgUtils = new MessageUtils(WebServiceConsumptionUIPlugin.ID + ".plugin", this);
    //super(msgUtils.getMessage("PAGE_WSIL_IMPORT"), msgUtils.getMessage("TITLE_WSIL_IMPORT"), msgUtils.getMessage("DESC_WSIL_IMPORT"));
  }

  public WidgetDataEvents addControls(Composite parent, Listener statusListener)
  {
  	IWorkbenchHelpSystem helpSystem = PlatformUI.getWorkbench().getHelpSystem();
  	
    parent.setToolTipText(msgUtils.getMessage("TOOLTIP_WSIL_IMPORT_PAGE"));
    helpSystem.setHelp(parent, INFOPOP_WSIL_IMPORT_PAGE);

    Composite composite = new Composite(parent, SWT.NONE);
    GridLayout gl = new GridLayout();
    gl.numColumns = 3;
    composite.setLayout(gl);
    GridData gd = new GridData(GridData.GRAB_VERTICAL | GridData.VERTICAL_ALIGN_BEGINNING | GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL);
    gd.grabExcessVerticalSpace = true;
    gd.grabExcessHorizontalSpace = true;
    composite.setLayoutData(gd);

    Label label = new Label(composite, SWT.WRAP);
    label.setText(msgUtils.getMessage("LABEL_WSIL_URI"));
    label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));

    wsil_ = new Text(composite, SWT.WRAP | SWT.SINGLE | SWT.BORDER);
    gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
    gd.widthHint = 256;
    wsil_.setLayoutData(gd);
    wsil_.addListener(SWT.Modify, statusListener);
    wsil_.setToolTipText(msgUtils.getMessage("TOOLTIP_WSIL_TEXT_WSIL"));
    helpSystem.setHelp(wsil_, INFOPOP_WSIL_TEXT_WSIL);

    browse_ = new Button(composite, SWT.PUSH);
    browse_.setText(msgUtils.getMessage("LABEL_BROWSE"));
    browse_.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
    browse_.addListener(SWT.Selection,
      new Listener()
      {
        public void handleEvent(Event event)
        {
          handleBrowseEvent(event);
        }
      }
    );
    browse_.setToolTipText(msgUtils.getMessage("TOOLTIP_WSIL_BUTTON_BROWSE_WSIL"));
    helpSystem.setHelp(browse_, INFOPOP_WSIL_BUTTON_BROWSE_WSIL);

    Composite wsdlComposite = new Composite(composite, SWT.NONE);
    wsdlComposite.setLayout(new GridLayout());
    gd = new GridData(GridData.FILL_BOTH);
    gd.horizontalSpan = 3;
    wsdlComposite.setLayoutData(gd);
    label = new Label(wsdlComposite, SWT.WRAP);
    label.setText(msgUtils.getMessage("LABEL_WSDL"));
    label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
    String[] columns = {msgUtils.getMessage("LABEL_WSDL_URI")};
    wsdls_ = new TableViewerEditor(wsdlComposite, columns, new ArrayList(), "http://");
    wsdls_.setToolTipText(msgUtils.getMessage("TOOLTIP_WSIL_TABLE_WSDL"));
    wsdls_.setInfopop(INFOPOP_WSIL_TABLE_WSDL);
    return this;
  }

  private IResource[] getWSDLResources()
  {
    ArrayList list = new ArrayList();
    if (selection != null && !selection.isEmpty())
    {
      for (Iterator it = selection.iterator(); it.hasNext();)
      {
        Object object = it.next();
        if ((object instanceof IFile) && ((IFile)object).getFileExtension() != null && ((IFile)object).getFileExtension().equals("wsdl"))
          list.add(object);
      }
    }
    IResource[] res = new IResource[list.size()];
    for (int i = 0; i < res.length; i++)
      res[i] = (IResource)list.get(i);
    return res;
  }

  public void handleBrowseEvent(Event event)
  {
    DialogResourceBrowser dialog = new DialogResourceBrowser(browse_.getShell(), null, new FileExtensionFilter(new String[] {"wsil"}));
    dialog.open();
    IResource res = dialog.getFirstSelection();
    if (res != null)
    {
      if (res instanceof IFile)
        wsil_.setText(res.getFullPath().toString());
      else
        wsil_.setText(res.getFullPath().addTrailingSeparator().append(INSPECTION_WSIL).toString());
    }
  }

  public Status getStatus()
  {
    if (!wsil_.getText().endsWith(".wsil"))
      return new SimpleStatus("", msgUtils.getMessage("PAGE_MSG_INVALID_WSIL_FILE_NAME"), Status.ERROR);
    else
      return new SimpleStatus("");
  }
  
  public void setInitialSelection(IStructuredSelection selection)
  {
    this.selection = selection;
    ArrayList list = new ArrayList();
    if (selection != null && !selection.isEmpty())
    {
      for (Iterator it = selection.iterator(); it.hasNext();)
      {
        Object object = it.next();
        if ((object instanceof IFile) && ((IFile)object).getFileExtension() != null && ((IFile)object).getFileExtension().equals("wsdl"))
          list.add(((IFile)object).getFullPath().toString());
        if (object instanceof ServiceImpl)
          list.add(J2EEActionAdapterFactory.getWSILPath(((ServiceImpl)(object))));	
        if (object instanceof ServiceRef)
          list.add(J2EEActionAdapterFactory.getWSILPath(((ServiceRef)(object))));		
        if (object instanceof WSDLResourceImpl)
          list.add(J2EEActionAdapterFactory.getWSILPath(((WSDLResourceImpl)(object))));	
      }
    }
    wsdls_.setInput(list);
    wsdls_.refresh();
    if (list.size() > 0)
    {
      StringBuffer path = new StringBuffer(list.get(0).toString());
      // change extension from .wsdl to .wsil
      int length = path.length();
      path = path.replace(length-4, length, "wsil");
      wsil_.setText(path.toString());
    }
  }
  
  public Arguments getGenWSILArguments()
  {
    Arguments args = new Arguments();
    String wsilPath = wsil_.getText();
    if (wsilPath.indexOf(':') < 0)
      args.add(AddWSDLToWSILCommand.ARG_WSIL, URI.createPlatformResourceURI(wsil_.getText()).toString());
    else
      args.add(AddWSDLToWSILCommand.ARG_WSIL, wsilPath);
    TableItem[] items = wsdls_.getItems();
    for (int i = 0; i < items.length; i++)
    {
      String uri = items[i].getText(0);
      if (uri.indexOf(':') < 0)
      {
        IResource res = ResourceUtils.findResource(uri);
        if (res != null)
        {
          try
          {
            uri = res.getLocation().toFile().toURL().toString();
          }
          catch (MalformedURLException murle)
          {
          	murle.getMessage();
          }
        }
      }
      args.add(AddWSDLToWSILCommand.ARG_WSDL, uri);
    }
    args.add(AddWSDLToWSILCommand.ARG_RESOLVE_WSDL, null);
    return args;
  }
}