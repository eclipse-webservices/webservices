/*******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060727   144354 kathy@ca.ibm.com - Kathy Chan
 *******************************************************************************/
/*
 * Created on May 4, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.eclipse.jst.ws.internal.consumption.ui.widgets.test;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.internal.context.ScenarioContext;
import org.eclipse.jst.ws.internal.plugin.WebServicePlugin;
import org.eclipse.wst.command.internal.env.core.selection.SelectionList;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;


/**
 * @author gilberta
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TestDefaultingFragment extends AbstractDataModelOperation
{
  
  private SelectionList testFacilities;
  private String launchedServiceTestName;
  private ScenarioContext scenarioContext;	
  private boolean generateProxy;  // This actually represent whether client test is requested
  
  
  public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable )
  {
  	IStatus status = Status.OK_STATUS;
  	
  	scenarioContext = WebServicePlugin.getInstance().getScenarioContext().copy();
  	//  test facilities
    selectTestFacility();
    return status;
  }
	
  // The test facilities retrieved from the extension
  // plus the default
  public SelectionList getTestFacility()
  {
    return testFacilities;
  } 
	  
  
  
  //	*********************** 
  // This method uses the preference to 
  // select the correct test facility 
  // 
  //***********************
  private void selectTestFacility()
  {
    //if we find something we launched earlier 
    //put it on the bottom
    String[] testTypes = scenarioContext.getWebServiceTestTypes();	  
    String[] newTestTypes = new String[testTypes.length];
	String[] wsdlCases = scenarioContext.getNonJavaTestService();
	boolean launched = false;
    
	int j = 0;
	
    for(int i = 0;i<testTypes.length;i++){
	  if(testTypes[i].equals(launchedServiceTestName))
	    launched = true;
	  else{
	     newTestTypes[j] = testTypes[i];
	     j++;
	  }
	}
	   	
    if(launched)
	  newTestTypes[testTypes.length - 1] = launchedServiceTestName;
    
	
	if(!generateProxy)
      testFacilities = new SelectionList(wsdlCases,0);	
	else
	  testFacilities = new SelectionList(newTestTypes,0);	
  }

  public void setLaunchedServiceTestName(String launchedServiceTestName)
  {
  	this.launchedServiceTestName = launchedServiceTestName;
  }
  
  public void setGenerateProxy(boolean generateProxy)
  {
  	this.generateProxy = generateProxy;
  }
  
}
