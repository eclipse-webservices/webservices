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
import org.eclipse.jst.ws.internal.ext.test.WebServiceTestExtension;
import org.eclipse.jst.ws.internal.ext.test.WebServiceTestRegistry;
import org.eclipse.jst.ws.internal.plugin.WebServicePlugin;
import org.eclipse.wst.command.env.core.common.Condition;
import org.eclipse.wst.command.env.core.fragment.BooleanFragment;


/**
 * @author gilberta
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FinishTestFragment extends BooleanFragment{

  private boolean isTestWidget;	
  private boolean launchedTest;
  private boolean generateProxy;
  private String launchedServiceTestName;
  private boolean testService;
  private String testID;
  public FinishTestFragment()
  {
    Condition condition = new Condition()
	{
	  public boolean evaluate()
	  {
	    if(!testService) return false;
	  	if(launchedServiceTestName == null) return true;
	    if(launchedServiceTestName != null)
	      if((!launchedServiceTestName.equals("") &&  (!isPreferedAWSDLTest() || (isTestWidget && isClientTestJava())) && generateProxy) || launchedServiceTestName.equals("")) return true;	
	    
	  	return false; 
	  }
	};
	   
	setTrueFragment(new WebServiceTestClientDepartureFragment());
    setCondition(condition);
  }
	    
  public void setLaunchedServiceTestName(String launchedServiceTestName)
  {
	this.launchedServiceTestName = launchedServiceTestName;
  }
	  
  public boolean isPreferedAWSDLTest()
  {
    ScenarioContext scenarioContext = WebServicePlugin.getInstance().getScenarioContext().copy();
	String[] testTypes = scenarioContext.getWebServiceTestTypes();
	WebServiceTestRegistry wsttRegistry = WebServiceTestRegistry.getInstance();
	WebServiceTestExtension wscte = (WebServiceTestExtension)wsttRegistry.getWebServiceExtensionsByName(testTypes[0]);  	
	if(!wscte.isJava())	
	  return true;
	return false;
  }

  public boolean isClientTestJava()
  {
  	if(testID == null) return false;
  	WebServiceTestRegistry wsttRegistry = WebServiceTestRegistry.getInstance();
	WebServiceTestExtension wscte = (WebServiceTestExtension)wsttRegistry.getWebServiceExtensionsByName(testID);  	
	if(wscte.isJava())	
      return true;
    return false;   	
  }
  
  
  public void setTestService(boolean testService)
  {
    this.testService = testService;	 
  }

  public void setGenerateProxy(boolean generateProxy)
  {
    this.generateProxy = generateProxy;	 
  }

  public void setTestID(String testID)
  {
    this.testID = testID;
  }

  public void setIsTestWidget(boolean isTestWidget)
  {
  	this.isTestWidget = isTestWidget;
  }
}
