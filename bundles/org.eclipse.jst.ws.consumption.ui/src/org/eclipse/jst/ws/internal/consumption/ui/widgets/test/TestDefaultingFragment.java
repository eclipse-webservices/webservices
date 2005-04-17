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
/*
 * Created on May 4, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.eclipse.jst.ws.internal.consumption.ui.widgets.test;

import org.eclipse.jst.ws.internal.context.ScenarioContext;
import org.eclipse.jst.ws.internal.ext.test.WebServiceTestRegistry;
import org.eclipse.jst.ws.internal.plugin.WebServicePlugin;
import org.eclipse.wst.command.internal.provisional.env.core.SimpleCommand;
import org.eclipse.wst.command.internal.provisional.env.core.common.Environment;
import org.eclipse.wst.command.internal.provisional.env.core.common.SimpleStatus;
import org.eclipse.wst.command.internal.provisional.env.core.common.Status;
import org.eclipse.wst.command.internal.provisional.env.core.selection.SelectionList;


/**
 * @author gilberta
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TestDefaultingFragment extends SimpleCommand
{
  
  private WebServiceTestRegistry testRegistry;
  private SelectionList testFacilities;
  private String launchedServiceTestName;
  private ScenarioContext scenarioContext;	
  
  
  public Status execute(Environment env)
  {
  	SimpleStatus status = new SimpleStatus("");
  	
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
	  	
    testFacilities = new SelectionList(newTestTypes,0);	
  }

  public void setLaunchedServiceTestName(String launchedServiceTestName)
  {
  	this.launchedServiceTestName = launchedServiceTestName;
  }
  
}
