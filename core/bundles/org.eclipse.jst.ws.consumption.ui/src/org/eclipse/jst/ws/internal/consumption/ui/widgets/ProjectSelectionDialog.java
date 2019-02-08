/*******************************************************************************
 * Copyright (c) 2006, 2008 IBM Corporation and others.
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
 * 20060413   135581 rsinha@ca.ibm.com - Rupam Kuehner
 * 20060802   148731 mahutch@ca.ibm.com - Mark Hutchinson
 * 20080603   234251 pmoogk@ca.ibm.com - Peter Moogk, Updated the size of project selection dialog
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.ui.widgets;

import org.eclipse.jst.ws.internal.consumption.ui.widgets.runtime.ProjectSelectionWidget;
import org.eclipse.jst.ws.internal.data.TypeRuntimeServer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wst.command.internal.env.ui.widgets.PageInfo;
import org.eclipse.wst.command.internal.env.ui.widgets.SimpleDialog;

public class ProjectSelectionDialog extends SimpleDialog {

	String titleText_="";
	String projectName_="";
	String earProjectName_="";
	String componentType_="";
	TypeRuntimeServer trs_;
	boolean needEAR_;
	
	public ProjectSelectionDialog(Shell shell, PageInfo pageInfo)
	{
		super(shell, pageInfo);
		titleText_ = pageInfo.getPageName();
	}	
	
	protected void callSetters() {
		// TODO Auto-generated method stub
		ProjectSelectionWidget projWidget = (ProjectSelectionWidget)getWidget();		
		projWidget.setEarProjectName(earProjectName_);
		projWidget.setProjectName(projectName_);
		projWidget.setComponentType(componentType_);
		projWidget.setNeedEAR(needEAR_);
		projWidget.setTypeRuntimeServer(trs_);
		
		projWidget.refreshProjectItems();
	}
	
	  protected Point getInitialSize()
	  {
	    return this.getShell().computeSize(325, SWT.DEFAULT, true); 
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
		
		public void setProjectName(String name)
		  {
				projectName_ = name;
		  }
		
		public String getProjectName()
		  {
				return projectName_;
		  }
		
		 public void setEarProjectName(String name)
		  {			
				earProjectName_ = name;
		  } 
		 
		 public String getEarProjectName()
		 {
			 return earProjectName_;
		 }
		 
		 public void setProjectComponentType( String type )
		  {
			    componentType_ = type;
		  }

		 public String getProjectComponentType()
		  {
			 return componentType_;
		  }
		 
		 public void setNeedEAR(boolean b)
		  {
			    needEAR_ = b;			    
		  }
		 
		 public boolean getNeedEAR()
		  {
			    return needEAR_;				
		  }
		 
		 public void setTypeRuntimeServer(TypeRuntimeServer trs)
		 {
			    trs_ = trs; 
		 }
		 
		 public boolean close() {

			projectName_ = ((ProjectSelectionWidget)getWidget()).getProjectName();
			earProjectName_ = ((ProjectSelectionWidget)getWidget()).getEarProjectName(); 
			needEAR_ = ((ProjectSelectionWidget)getWidget()).getNeedEAR();
			componentType_ = ((ProjectSelectionWidget)getWidget()).getComponentType();
			return super.close();
		}
}
