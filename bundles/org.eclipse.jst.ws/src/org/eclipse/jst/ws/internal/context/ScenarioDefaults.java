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
package org.eclipse.jst.ws.internal.context;


import org.eclipse.jst.ws.internal.data.LabelsAndIds;
import org.eclipse.jst.ws.internal.ext.test.WebServiceTestRegistry;



public class ScenarioDefaults
{  
  
	
  public String[] getWebServiceTestTypes()
  {
  	//we will set the home grown sample as the first choice
  	WebServiceTestRegistry testRegistry = WebServiceTestRegistry.getInstance();
  	
  	LabelsAndIds labelsandids = testRegistry.getLabelsAndIDs();
  	String[] labels = labelsandids.getLabels_();
  	String[] ids = labelsandids.getIds_();
  	
  	String[] newNames = new String[labels.length];
  	int index = -1;
  	for(int i = 0;i<ids.length;i++){
  	  if(ids[i].equals("org.eclipse.jst.ws.internal.consumption.ui.widgets.test.WebServiceSampleTest"))  	
  	    index = i;   
  	}
  	if(index != -1){
  	  newNames[0] = labels[index];
  	  int j = 1;
  	  for(int i = 0;i<labels.length;i++){
  	    if(i != index){
  	  	  newNames[j] = labels[i]; 
  	      j++;
  	    }
  	  }
  	}
  	else return labels;
  	
  	return newNames;
  }
  
  public String getNonJavaTestServiceDefault()
  {
    return "Web Services Explorer";	
  } 
  
  public String webserviceTypeIdDefault()
  {
    return "org.eclipse.jst.ws.type.java";
  }
  
  public boolean startWebserviceDefault()
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
