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
 * 20060821   153833 makandre@ca.ibm.com - Andrew Mak, Allow the Web Service Test extension point to specify the supported client runtime
 *******************************************************************************/
/*
 * Created on May 4, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.eclipse.jst.ws.internal.consumption.ui.widgets.test;

import java.util.ArrayList;
import java.util.List;

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
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TestDefaultingFragment extends AbstractDataModelOperation
{
  
  private SelectionList testFacilities;
  private String launchedServiceTestName;
  private ScenarioContext scenarioContext;	
  private boolean generateProxy;  // This actually represent whether client test is requested
  
  private String clientRuntimeId_;
  
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
    List newTestTypes = new ArrayList();
	String[] wsdlCases = scenarioContext.getNonJavaTestService();
	boolean launched = false;
	
    for(int i = 0;i<testTypes.length;i++){
    	
      WebServiceTestExtension extension =
    	  (WebServiceTestExtension) WebServiceTestRegistry.getInstance()
    	  .getWebServiceExtensionsByName(testTypes[i]);
		  
      if (!extension.supportsRuntime(clientRuntimeId_))
    	  continue;
    	
	  if(testTypes[i].equals(launchedServiceTestName))
	    launched = true;
	  else{
	     newTestTypes.add(testTypes[i]); 
	  }
	}
	   	
    if(launched)
	  newTestTypes.add(launchedServiceTestName);
    
	
	if(!generateProxy)
      testFacilities = new SelectionList(wsdlCases,0);	
	else {
	  String[] tempArray = new String[newTestTypes.size()];
	  testFacilities = new SelectionList((String[]) newTestTypes.toArray(tempArray), 0);
	}
  }

  public void setLaunchedServiceTestName(String launchedServiceTestName)
  {
  	this.launchedServiceTestName = launchedServiceTestName;
  }
  
  public void setGenerateProxy(boolean generateProxy)
  {
  	this.generateProxy = generateProxy;
  }
    
  /**
   * Sets the client runtime ID.
   * 
   * @param clientRuntimeId The client runtime ID.
   */
  public void setClientRuntimeId(String clientRuntimeId) {
	  clientRuntimeId_ = clientRuntimeId;
  }

}
