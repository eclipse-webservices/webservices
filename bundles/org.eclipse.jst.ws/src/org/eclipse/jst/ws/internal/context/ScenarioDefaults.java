/*******************************************************************************
 * Copyright (c) 2004, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20070314   154543 makandre@ca.ibm.com - Andrew Mak, WebServiceTestRegistry is tracking extensions using label attribute instead of ID
 *******************************************************************************/
package org.eclipse.jst.ws.internal.context;


import org.eclipse.jst.ws.internal.data.LabelsAndIds;
import org.eclipse.jst.ws.internal.ext.test.WebServiceTestRegistry;



public class ScenarioDefaults
{  
  
	
  public String[] getWebServiceTestIds()
  {
  	//we will set the home grown sample as the first choice
  	WebServiceTestRegistry testRegistry = WebServiceTestRegistry.getInstance();
  	
  	LabelsAndIds labelsandids = testRegistry.getLabelsAndIDs();
  	String[] ids = labelsandids.getIds_();
  	
  	String[] newIds = new String[ids.length];
  	int index = -1;
  	for(int i = 0;i<ids.length;i++){
  	  if(ids[i].equals("org.eclipse.jst.ws.internal.consumption.ui.widgets.test.WebServiceSampleTest"))  	
  	    index = i;   
  	}
  	if(index != -1){
  	  newIds[0] = ids[index];
  	  int j = 1;
  	  for(int i = 0;i<ids.length;i++){
  	    if(i != index){
  	  	  newIds[j] = ids[i]; 
  	      j++;
  	    }
  	  }
  	}
  	else return ids;
  	
  	return newIds;
  }
  
  public String getNonJavaTestServiceDefault()
  {
    return "Web Services Explorer";	
  } 
  
  public String webserviceTypeIdDefault()
  {
    return "0/org.eclipse.jst.ws.wsImpl.java";
  }
  
  public int serviceGenerationDefault()
  {
	  return ScenarioContext.WS_START;  
  }
  
  public int clientGenerationDefault()
  {
	  return ScenarioContext.WS_NONE;  
  }
  
  public boolean startWebserviceDefault()
  {
    return true;
  }
  
  public boolean installWebserviceDefault()
  {
    return true;
  }
  
  public boolean installClientDefault()
  {
    return true;
  }
  
  public boolean launchWebserviceExplorerDefault()
  {
    return false;
  }
  
  public boolean generateProxyDefault()
  {
    return false;
  }
  
  public String webserviceClientTypeDefault()
  {
    return "org.eclipse.jst.ws.client.type.java";
  }
  
  public boolean testWebserviceDefault()
  {
    return false;
  }

  public boolean getMonitorWebServiceDefault()
  {
    return false;
  }
  
  public boolean launchSample()
  {
    return true;
  }
}
