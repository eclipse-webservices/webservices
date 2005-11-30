/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jst.ws.internal.context;

public class TransientScenarioContext implements ScenarioContext
{
  private String[] webServiceTestTypes;
  private String[] nonJavaTestService;
  private String   webServiceType_;
  private boolean  installWebService_;
  private boolean  installClient_;
  private boolean  startWebService_;
  private boolean  launchWebServiceExplorer_;
  private boolean  generateProxy_;
  private String   clientWebServiceType_;
  private boolean  testWebService_;
  private boolean  monitorWebService;
  private boolean  launchSample_;
  
  private ScenarioDefaults defaults = new ScenarioDefaults();
 
  public TransientScenarioContext () {}

  public String[] getNonJavaTestService()
  {
    return nonJavaTestService;
  }
 
  public void setNonJavaTestService(String[] nonJavaTestService)
  {
    this.nonJavaTestService = nonJavaTestService;	
  }
 
  public void setWebServiceTestTypes(String[] webServiceTestTypes)
  {
    this.webServiceTestTypes = webServiceTestTypes;
  }
  
  public String[] getWebServiceTestTypes()
  {
    return webServiceTestTypes;	
  }
 
  public ScenarioContext copy() {
  	TransientScenarioContext context = new TransientScenarioContext();
	context.setWebServiceTestTypes(getWebServiceTestTypes());
	context.setNonJavaTestService(getNonJavaTestService());
	
	context.setWebServiceType( getWebServiceType() );
	context.setClientWebServiceType( getClientWebServiceType() );
	context.setGenerateProxy( getGenerateProxy() );
	context.setLaunchWebServiceExplorer( getLaunchWebServiceExplorer() );
	context.setInstallWebService( getInstallWebService() );
	context.setInstallClient( getInstallClient() );
	context.setStartWebService( getStartWebService() );
	context.setTestWebService( getTestWebService() );
	context.setMonitorWebService(getMonitorWebService());
	context.setLaunchSampleEnabled( isLaunchSampleEnabled() );
	return context;
  }
 
  /* (non-Javadoc)
   * @see org.eclipse.jst.ws.internal.context.ScenarioContext#getClientWebServiceType()
   */
  public String getClientWebServiceType()
  {
    return clientWebServiceType_;
  }
  
  public String getClientWebServiceTypeDefault(){ return defaults.webserviceClientTypeDefault(); }
  
  /* (non-Javadoc)
   * @see org.eclipse.jst.ws.internal.context.ScenarioContext#getGenerateProxy()
   */
  public boolean getGenerateProxy()
  {
    return generateProxy_;
  }
  
  public boolean getGenerateProxyDefault(){ return defaults.generateProxyDefault(); }
  
  /* (non-Javadoc)
   * @see org.eclipse.jst.ws.internal.context.ScenarioContext#getLaunchWebServiceExplorer()
   */
  public boolean getLaunchWebServiceExplorer()
  {
    return launchWebServiceExplorer_;
  }
  
  public boolean getLaunchWebServiceExplorerDefault(){ return defaults.launchWebserviceExplorerDefault(); }
  
  /* (non-Javadoc)
   * @see org.eclipse.jst.ws.internal.context.ScenarioContext#getInstallWebService()
   */
  public boolean getInstallWebService()
  {
    return installWebService_;
  }
  
  public boolean getInstallWebServiceDefault(){ return defaults.installWebserviceDefault(); }
  
  /* (non-Javadoc)
   * @see org.eclipse.jst.ws.internal.context.ScenarioContext#getInstallWebService()
   */
  public boolean getInstallClient()
  {
    return installClient_;
  }
  
  public boolean getInstallClientDefault(){ return defaults.installClientDefault(); }
  
  
  
  /* (non-Javadoc)
   * @see org.eclipse.jst.ws.internal.context.ScenarioContext#getStartWebService()
   */
  public boolean getStartWebService()
  {
    return startWebService_;
  }
  
  public boolean getStartWebServiceDefault(){ return defaults.startWebserviceDefault(); }
  
  /* (non-Javadoc)
   * @see org.eclipse.jst.ws.internal.context.ScenarioContext#getTestWebService()
   */
  public boolean getTestWebService()
  {
    return testWebService_;
  }
  
  public boolean getTestWebServiceDefault(){ return defaults.testWebserviceDefault(); }
  
  /* (non-Javadoc)
   * @see org.eclipse.jst.ws.internal.context.ScenarioContext#getWebServiceType()
   */
  public String getWebServiceType()
  {
    return webServiceType_;
  }
  
  public String getWebServiceTypeDefault(){ return defaults.webserviceTypeIdDefault(); }
  
  /* (non-Javadoc)
   * @see org.eclipse.jst.ws.internal.context.ScenarioContext#setClientWebServiceType(java.lang.String)
   */
  public void setClientWebServiceType(String value)
  {
    clientWebServiceType_ = value;
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.jst.ws.internal.context.ScenarioContext#setGenerateProxy(boolean)
   */
  public void setGenerateProxy(boolean value)
  {
    generateProxy_ = value;
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.jst.ws.internal.context.ScenarioContext#setLaunchWebServiceExplorer(boolean)
   */
  public void setLaunchWebServiceExplorer(boolean value)
  {
    launchWebServiceExplorer_ = value;
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.jst.ws.internal.context.ScenarioContext#setInstallWebService(boolean)
   */
  public void setInstallWebService(boolean value)
  {
    installWebService_ = value;
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.jst.ws.internal.context.ScenarioContext#setInstallWebService(boolean)
   */
  public void setInstallClient(boolean value)
  {
    installClient_ = value;
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.jst.ws.internal.context.ScenarioContext#setStartWebService(boolean)
   */
  public void setStartWebService(boolean value)
  {
    startWebService_ = value;
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.jst.ws.internal.context.ScenarioContext#setTestWebService(boolean)
   */
  public void setTestWebService(boolean value)
  {
    testWebService_ = value;
  }
  /* (non-Javadoc)
   * @see org.eclipse.jst.ws.internal.context.ScenarioContext#setWebServiceType(java.lang.String)
   */
  public void setWebServiceType(String value)
  {
    webServiceType_ = value;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jst.ws.internal.context.ScenarioContext#getMonitorWebService()
   */
  public boolean getMonitorWebService()
  {
    return monitorWebService;
  }
  
  public boolean getMonitorWebServiceDefault()
  {
    return defaults.getMonitorWebServiceDefault();
  }

  /* (non-Javadoc)
   * @see org.eclipse.jst.ws.internal.context.ScenarioContext#setMonitorWebService(boolean)
   */
  public void setMonitorWebService(boolean value)
  {
    monitorWebService = value;
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.jst.ws.internal.context.ScenarioContext#getLaunchSampleEnabledDefault()
   */
  public boolean getLaunchSampleEnabledDefault()
  {
    return defaults.launchSample();
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.jst.ws.internal.context.ScenarioContext#isLaunchSampleEnabled()
   */
  public boolean isLaunchSampleEnabled()
  {
    return launchSample_;
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.jst.ws.internal.context.ScenarioContext#setLaunchSampleEnabled(boolean)
   */
  public void setLaunchSampleEnabled(boolean value)
  {
    launchSample_ = value;
  }
}
