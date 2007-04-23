/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 * Created on Apr 15, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.eclipse.jst.ws.internal.creation.ui.widgets.test;

import java.util.Enumeration;
import java.util.Vector;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.internal.context.ScenarioContext;
import org.eclipse.jst.ws.internal.ext.test.WebServiceTestExtension;
import org.eclipse.jst.ws.internal.ext.test.WebServiceTestRegistry;
import org.eclipse.jst.ws.internal.plugin.WebServicePlugin;
import org.eclipse.wst.command.internal.env.core.selection.SelectionList;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;


/**
 * @author gilberta
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class WebServiceTestDefaultingCommand extends AbstractDataModelOperation 
{
  
  private SelectionList serviceTestFacilities;	
  private String[] testID;
  
  public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable )
  {    
  	ScenarioContext scenarioContext = WebServicePlugin.getInstance().getScenarioContext().copy();
  	testID = scenarioContext.getNonJavaTestService();
  	String[] testTypes = scenarioContext.getWebServiceTestTypes();
  	  	
  	IStatus status = Status.OK_STATUS;
  	WebServiceTestRegistry wsttRegistry = WebServiceTestRegistry.getInstance();
  	
    
  	Vector wsdlNames = new Vector();
  	for(int i =0 ;i<testTypes.length;i++){
  	  WebServiceTestExtension wscte = (WebServiceTestExtension)wsttRegistry.getWebServiceExtensionsByName(testTypes[i]);  	
  	  if(wscte.testWSDL()){
  	    wsdlNames.addElement(testTypes[i]);
  	  }
  	}
    
  	String javaNamesArray[] = new String[wsdlNames.size()];
  	Enumeration e = wsdlNames.elements();
  	int i = 0;
  	while(e.hasMoreElements()){
      javaNamesArray[i] = (String)e.nextElement();
  	  i++;
  	}
  	
  	
  	serviceTestFacilities = new SelectionList(javaNamesArray,0);  	
  	
 	return status;
  }

  public SelectionList getServiceTestFacilities()
  {
  	return serviceTestFacilities;
  }

  public String[] getTestID()
  {
  	return testID;
  }
  
  public boolean getExternalBrowser()
  {
  	return false;
  }


}
