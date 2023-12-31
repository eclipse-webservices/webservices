/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
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
 * 20060410   135441 joan@ca.ibm.com - Joan Haggarty
 * 20060418   136335 joan@ca.ibm.com - Joan Haggarty
 *******************************************************************************/
package org.eclipse.jst.ws.internal.creation.ui.widgets;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jst.ws.internal.consumption.ui.ConsumptionUIMessages;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.object.ObjectSelectionWidget;
import org.eclipse.jst.ws.internal.consumption.ui.wsrt.WebServiceRuntimeExtensionUtils2;
import org.eclipse.jst.ws.internal.data.TypeRuntimeServer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wst.command.internal.env.ui.widgets.AbstractSelectionDialog;
import org.eclipse.wst.command.internal.env.ui.widgets.PageInfo;
import org.eclipse.wst.ws.internal.wsrt.WebServiceScenario;

public class ServiceImplSelectionDialog extends AbstractSelectionDialog {

	TypeRuntimeServer trs_;
	String titleText_;
	IStructuredSelection selection_;
	
	public ServiceImplSelectionDialog(Shell parent, PageInfo pageInfo) {
		super(parent, pageInfo);
		titleText_ = pageInfo.getPageName();
	}
	
	protected void callSetters()
	{		
		((ObjectSelectionWidget)getWidget()).setTypeRuntimeServer(trs_);
		((ObjectSelectionWidget)getWidget()).setInitialSelection(selection_);
	}
	
	public void setTypeRuntimeServer(TypeRuntimeServer trs)
	{
		trs_ = trs; 
		
		// set dialog title text based on the web service scenario
		if (trs != null)
		{
		   String typeId = trs.getTypeId();
		   int scenario = WebServiceRuntimeExtensionUtils2.getScenarioFromTypeId(typeId);
		   
		   if (scenario == WebServiceScenario.BOTTOMUP)
		   {
			  titleText_ = ConsumptionUIMessages.DIALOG_TITILE_SERVICE_IMPL_SELECTION;
		   }
		   else if (scenario == WebServiceScenario.TOPDOWN)
		   {
			   titleText_ = ConsumptionUIMessages.DIALOG_TITILE_SERVICE_DEF_SELECTION;
		   }
		}             	   
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
		  
		 return ((ObjectSelectionWidget)getWidget()).getObjectSelectionDisplayableString();
	}
	  
	  public IStructuredSelection getObjectSelection() {
		  return ((ObjectSelectionWidget)getWidget()).getObjectSelection();
	}
	  
	  public IProject getProject(){
		  return ((ObjectSelectionWidget)getWidget()).getProject();
	  }
	  
	  public String getComponentName(){
		  return ((ObjectSelectionWidget)getWidget()).getComponentName();  
	  }
	  
	  public void setInitialSelection(IStructuredSelection selection)
	  {
		  selection_ = selection; 
	  }
	  
	  protected Point getInitialSize()
	  {	
		  return ((ObjectSelectionWidget)getWidget()).getWidgetSize();	    
	  }
}
