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

public interface ScenarioContext 
{
  public final String PREFERENCE_WEBSERVICE_TEST_TYPES = "webServiceTestTypes";
    
  public final String PREFERENCE_NON_JAVA_TEST_SERVICE= "nonJavaTestService";
  
  public final String PREFERENCE_WEBSERVICE_TYPE = "webServiceType";
  
  public final String PREFERENCE_START_WEBSERVICE = "startWebService";
  
  public final String PREFERENCE_INSTALL_WEBSERVICE = "installWebService";
  
  public final String PREFERENCE_INSTALL_CLIENT = "installClient";
  
  public final String PREFERENCE_LAUNCH_WEBSERVICE_EXPLORER = "launchWebServiceExplorer";
  
  public final String PREFERENCE_GENERATE_PROXY = "generateProxy";
  
  public final String PREFERENCE_CLIENT_WEBSERVICE_TYPE = "clientWebServiceType";
  
  public final String PREFERENCE_TEST_WEBSERVICE = "testWebService";
  
  public final String PREFERENCE_MONITOR_WEBSERVICE = "monitorWebService";
  
  public final String PREFERENCE_LAUNCH_SAMPLE = "launchSample";
  
  public final String PREFERENCE_GENERATE_WEBSERVICE = "generateWebService"; //jvh
  public final String PREFERENCE_GENERATE_CLIENT = "generateClient"; //jvh  
  
  public final static int WS_TEST=0;
  public final static int WS_START=1;
  public final static int WS_INSTALL=2;
  public final static int WS_DEPLOY=3;  
  public final static int WS_ASSEMBLE=4;
  public final static int WS_DEVELOP=5;  
  public final static int WS_NONE=6;
  
  public String[] getWebServiceTestIds();
  public void setWebServiceTestIds(String[] ids);
  
  public String[] getWebServiceTestTypes();
  public void setWebServiceTestTypes(String[] testTypes);
  
  public String[] getNonJavaTestService();
  
  public String getWebServiceType();
  public String getWebServiceTypeDefault();
  public void   setWebServiceType( String value );
  
  public boolean getInstallWebService();
  public boolean getInstallWebServiceDefault();
  public void    setInstallWebService( boolean value );
  
  public boolean getInstallClient();
  public boolean getInstallClientDefault();
  public void    setInstallClient( boolean value );
    
  public boolean getStartWebService();
  public boolean getStartWebServiceDefault();
  public void    setStartWebService( boolean value );
  
  public boolean getLaunchWebServiceExplorer();
  public boolean getLaunchWebServiceExplorerDefault();
  public void    setLaunchWebServiceExplorer( boolean value );
  
  public boolean getGenerateProxy();
  public boolean getGenerateProxyDefault();
  public void    setGenerateProxy( boolean value );
  
  public String getClientWebServiceType();
  public String getClientWebServiceTypeDefault();
  public void   setClientWebServiceType( String value );
  
  public boolean getTestWebService();
  public boolean getTestWebServiceDefault();
  public void    setTestWebService( boolean value );
  
  public boolean getMonitorWebService();
  public boolean getMonitorWebServiceDefault();
  public void setMonitorWebService(boolean value);
  
  public boolean isLaunchSampleEnabled();
  public boolean getLaunchSampleEnabledDefault();
  public void    setLaunchSampleEnabled( boolean value );
  
  public int getGenerateWebServiceDefault();
  public int getGenerateWebService();
  public void setGenerateWebService(int value);
  
  public int getGenerateClientDefault();
  public int getGenerateClient();
  public void setGenerateClient(int value);
  
  public ScenarioContext copy();
}
