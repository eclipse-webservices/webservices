/*******************************************************************************
 * Copyright (c) 2004, 2007 IBM Corporation and others.
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
 * 20060418   136712 joan@ca.ibm.com - Joan Haggarty
 * 20070131   168786 makandre@ca.ibm.com - Andrew Mak, wsdl url on web service wizard page 1 is not reflected in browse dialog
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.ui.widgets;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jst.ws.internal.consumption.ui.ConsumptionUIMessages;
import org.eclipse.jst.ws.internal.consumption.ui.plugin.WebServiceConsumptionUIPlugin;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.command.internal.env.ui.widgets.SimpleWidgetDataContributor;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetDataEvents;
import org.eclipse.wst.ws.internal.parser.wsil.WebServiceEntity;
import org.eclipse.wst.ws.internal.parser.wsil.WebServicesParser;


/**
* This wizard page allows the user to select the WSDL URI for consumption
*/
public class WSDLSelectionTreeWidget extends SimpleWidgetDataContributor
{
  private String pluginId_ = "org.eclipse.jst.ws.consumption.ui";

  /*CONTEXT_ID PWWS0001 for the WSDL Selection Page*/
  private final String INFOPOP_PWWS_PAGE = ".PWWS0001";

  private WebServicesParser webServicesParser;
  private String webServiceURI;
  
  private Listener statusListener;
  private TreeViewer treeViewer_;
  private TreeContentProvider treeContentProvider;
  private Tree tree_;
  private int pageWidth_;
  private WebServiceEntity entity_;

  /**
  * Default constructor.
  */
  public WSDLSelectionTreeWidget()
  {
  }

  public WidgetDataEvents addControls( Composite parent, Listener statusListener )
  {
  	this.statusListener = statusListener;
    parent.setToolTipText(ConsumptionUIMessages.TOOLTIP_PWWS_PAGE);
    PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, pluginId_ + INFOPOP_PWWS_PAGE);

    Label wsdlLabel = new Label(parent, SWT.WRAP);
    wsdlLabel.setText(ConsumptionUIMessages.LABEL_SELECT_WSDL);
    wsdlLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));

    tree_ = new Tree(parent, SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
    GridData gd = new GridData(GridData.FILL_BOTH);
    gd.heightHint = 200;
    tree_.setLayoutData(gd);
    pageWidth_ = tree_.getShell().getSize().x;
    treeViewer_ = new TreeViewer(tree_);
    treeContentProvider = new TreeContentProvider();
    treeViewer_.setContentProvider(treeContentProvider);
    treeViewer_.setLabelProvider(new TreeLabelProvider());
    treeViewer_.addSelectionChangedListener(
      new ISelectionChangedListener()
      {
        public void selectionChanged(SelectionChangedEvent event)
        {
        	handleTreeSelectionChangedEvent(event);
        }
      }
    );
    return this;
  }
  
  private void handleTreeSelectionChangedEvent(SelectionChangedEvent event)
  {
  	statusListener.handleEvent(new Event());
  }
  
  /**
   * @return Returns the webServicesParser.
   */
  public WebServicesParser getWebServicesParser()
  {
  	return webServicesParser;
  }
  /**
   * @param webServicesParser The webServicesParser to set.
   */
  public void setWebServicesParser(WebServicesParser webServicesParser)
  {
  	this.webServicesParser = webServicesParser;
  	treeViewer_.setInput(this.webServicesParser);
  	refreshTreeViewer();
  }

  public void setWebServiceURI(String wsURI)
  {
  	webServiceURI = wsURI;
  	if (!tree_.isDisposed()) {
  		treeContentProvider.setWebServiceURI(webServiceURI);
  		refreshTreeViewer();
  	}
  }

  public void refreshTreeViewer()
  {
  	if (webServicesParser != null && webServiceURI != null)
  	{
  	  treeViewer_.refresh();
  	  Control shell = tree_.getShell();
  	  if (tree_.getSize().x > pageWidth_ || shell.getSize().x > pageWidth_)
  	  {
  	    tree_.setSize(pageWidth_, tree_.getSize().y);
  	    shell.setSize(pageWidth_, shell.getSize().y);
  	    shell.computeSize(SWT.DEFAULT, SWT.DEFAULT);
  	    shell.redraw();
  	  }
  	}
  }
  
  public void setEnabled(boolean enabled)
  {
    tree_.setEnabled(enabled);
  }
  
  public boolean isEnabled()
  {
    return tree_.isEnabled();
  }
  
  public String getWsdlURI()
  {	  
	  	if (entity_ != null)
	  	  return entity_.getURI();
	  	else
	  	  return webServiceURI;
  }
  
  private WebServiceEntity getSelectionAsWebServiceEntity()
  {
  	if (treeViewer_ != null)
  	{
      ISelection sel = treeViewer_.getSelection();
      if (!sel.isEmpty() && (sel instanceof IStructuredSelection))
      {
        IStructuredSelection ssel = (IStructuredSelection)sel;
        Object object = ssel.getFirstElement();
        if (object instanceof WebServiceEntity)
          return (WebServiceEntity)object;
      }
  	}
    return null;  	
  }
    
  public IStatus getStatus()
  {    
	entity_ = getSelectionAsWebServiceEntity();
    if (entity_ == null || entity_.getType() != WebServiceEntity.TYPE_WSDL)
      return StatusUtils.errorStatus( ConsumptionUIMessages.PAGE_MSG_SELECTION_MUST_BE_WSDL );
    else
      return Status.OK_STATUS;
  }

  private class TreeContentProvider implements ITreeContentProvider
  {
  	private String webServiceURI;

    public void dispose()
    {
    }

    public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
    {
    }

    public Object[] getElements(Object inputElement)
    {
      if (inputElement instanceof WebServicesParser)
      {
      	WebServicesParser parser = (WebServicesParser)inputElement;
        Object wsEntity = parser.getWebServiceEntityByURI(webServiceURI);
        if (wsEntity != null)
          return new Object[] {wsEntity};
      }
      return new Object[0];
    }

    public Object[] getChildren(Object parentElement)
    {
      if (parentElement instanceof WebServiceEntity)
      {
        List list = ((WebServiceEntity)parentElement).getChildren();
        Object[] objects = new Object[list.size()];
        for (int i = 0; i < objects.length; i++)
          objects[i] = list.get(i);
        return objects;
      }
      return new Object[0];
    }

    public Object getParent(Object element)
    {
      if (element instanceof WebServiceEntity)
        return ((WebServiceEntity)element).getParent();
      return new Object[0];
    }

    public boolean hasChildren(Object element)
    {
      return (getChildren(element).length > 0);
    }
    
    public void setWebServiceURI(String wsURI)
    {
      webServiceURI = wsURI;
    }
  }

  private class TreeLabelProvider implements ILabelProvider
  {
    public Image getImage(Object element)
    {
      switch (((WebServiceEntity)element).getType())
      {
        case WebServiceEntity.TYPE_HTML:
          return WebServiceConsumptionUIPlugin.getImageDescriptor("icons/wsil/html.gif").createImage();
        case WebServiceEntity.TYPE_WSIL:
          return WebServiceConsumptionUIPlugin.getImageDescriptor("icons/wsil/wsil.gif").createImage();
        case WebServiceEntity.TYPE_WSDL:
          return WebServiceConsumptionUIPlugin.getImageDescriptor("icons/wsil/wsdl.gif").createImage();
        default:
          return null;
      }
    }

    public String getText(Object element)
    {
      return ((WebServiceEntity)element).getURI();
    }

    public void addListener(ILabelProviderListener listener)
    {
    }

    public void removeListener(ILabelProviderListener listener)
    {
    }

    public boolean isLabelProperty(Object element, String property)
    {
      return true;
    }

    public void dispose()
    {
    }
  }
}
