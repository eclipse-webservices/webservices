/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060525   142281 joan@ca.ibm.com - Joan Haggarty
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.ui.widgets;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wst.command.internal.env.ui.widgets.AbstractSelectionDialog;
import org.eclipse.wst.command.internal.env.ui.widgets.PageInfo;

public class WSDLSelectionDialog extends AbstractSelectionDialog {
	
	private String titleText_;
	private String wsUri_;
	private String componentName_;
	private IProject project_;
	
	public WSDLSelectionDialog(Shell parent, PageInfo pageInfo) {
		super(parent, pageInfo);
		titleText_ = pageInfo.getPageName();
	}

	protected void callSetters()
	{		
		((WSDLSelectionWidgetWrapper)getWidget()).setComponentName(componentName_);
		((WSDLSelectionWidgetWrapper)getWidget()).setProject(project_);
		((WSDLSelectionWidgetWrapper)getWidget()).setWebServiceURI(wsUri_);
	}
	
	public void setComponentName(String componentName)
	{
         componentName_ = componentName;      	   
	}
	
	public String getComponentName()
	{
         return componentName_ ;      	   
	}
	
	public void setProject(IProject project)
	{
		project_ = project;
	}
	
	public void setWebServiceURI(String wsUri)
	{
		wsUri_ = wsUri;
	}	
	 
	  protected void setShellStyle(int newShellStyle)
	  {
	    super.setShellStyle( newShellStyle | SWT.RESIZE );  
	  }

	  protected void configureShell(Shell newShell)
	  {
	    newShell.setText(titleText_);   
	    super.configureShell(newShell);
	  }

	  public String getDisplayableSelectionString() {
		  
		 return ((WSDLSelectionWidgetWrapper)getWidget()).getObjectSelectionDisplayableString();
	}
	  
	  public IStructuredSelection getObjectSelection() {
		  return ((WSDLSelectionWidgetWrapper)getWidget()).getObjectSelection();
	}
	 
	  public String getWebServiceURI()
	  {
		  return wsUri_;
	  }
	  
	  public IProject getProject(){
		  return project_;
	  }

      public boolean close() {
        setComponentName(((WSDLSelectionWidgetWrapper)getWidget()).getComponentName());
        setProject(((WSDLSelectionWidgetWrapper)getWidget()).getProject());
        setWebServiceURI(((WSDLSelectionWidgetWrapper)getWidget()).getWebServiceURI());
    	return super.close();
    }
}
