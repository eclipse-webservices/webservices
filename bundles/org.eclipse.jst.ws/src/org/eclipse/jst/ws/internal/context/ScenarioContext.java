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

public interface ScenarioContext 
{
  public final String PREFERENCE_WEBSERVICE_TEST_TYPES = "webServiceTestTypes";
    
  public final String PREFERENCE_NON_JAVA_TEST_SERVICE= "nonJavaTestService";
  
  public final String PREFERENCE_WEBSERVICE_TYPE = "webServiceType";
  
  public final String PREFERENCE_START_WEBSERVICE = "startWebService";
  
  public final String PREFERENCE_LAUNCH_WEBSERVICE_EXPLORER = "launchWebServiceExplorer";
  
  public final String PREFERENCE_GENERATE_PROXY = "generateProxy";
  
  public final String PREFERENCE_CLIENT_WEBSERVICE_TYPE = "clientWebServiceType";
  
  public final String PREFERENCE_TEST_WEBSERVICE = "testWebService";
  
  public final String PREFERENCE_MONITOR_WEBSERVICE = "monitorWebService";
  
  public final String PREFERENCE_LAUNCH_SAMPLE = "launchSample";
  
  public String[] getWebServiceTestTypes();
  public void setWebServiceTestTypes(String[] testTypes);
  
  public String[] getNonJavaTestService();
  
  public String getWebServiceType();
  public String getWebServiceTypeDefault();
  public void   setWebServiceType( String value );
  
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
  
  public ScenarioContext copy();
}