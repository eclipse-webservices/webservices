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

import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.Vector;

import org.eclipse.jst.ws.internal.ext.test.WebServiceTestExtension;
import org.eclipse.jst.ws.internal.ext.test.WebServiceTestRegistry;
import org.eclipse.jst.ws.internal.plugin.WebServicePlugin;
import org.eclipse.wst.command.internal.env.context.PersistentContext;


public class PersistentScenarioContext extends PersistentContext implements ScenarioContext
{
  public PersistentScenarioContext()
  {
    super(WebServicePlugin.getInstance()); 
  }

  public void load()
  {
    ScenarioDefaults defaults = new ScenarioDefaults();
    String[] ids = defaults.getWebServiceTestTypes();
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < ids.length; i++)
    {
      if (i != 0) sb.append(",");
      sb.append(ids[i]);
    }
    String nonJavaTestService = defaults.getNonJavaTestServiceDefault();
    setDefaultStringIfNoDefault(PREFERENCE_WEBSERVICE_TEST_TYPES, sb.toString());
    setDefaultStringIfNoDefault(PREFERENCE_NON_JAVA_TEST_SERVICE, nonJavaTestService);
    
    setDefaultStringIfNoDefault(PREFERENCE_CLIENT_WEBSERVICE_TYPE, defaults.webserviceClientTypeDefault() );
    setDefaultStringIfNoDefault(PREFERENCE_WEBSERVICE_TYPE, defaults.webserviceTypeIdDefault() );
    setDefaultBooleanIfNoDefault(PREFERENCE_GENERATE_PROXY, defaults.generateProxyDefault() );
    setDefaultBooleanIfNoDefault(PREFERENCE_LAUNCH_WEBSERVICE_EXPLORER, defaults.launchWebserviceExplorerDefault() );
    setDefaultBooleanIfNoDefault(PREFERENCE_INSTALL_WEBSERVICE, defaults.installWebserviceDefault() );
    setDefaultBooleanIfNoDefault(PREFERENCE_INSTALL_CLIENT, defaults.installClientDefault() );
    setDefaultBooleanIfNoDefault(PREFERENCE_START_WEBSERVICE, defaults.startWebserviceDefault() );
    setDefaultBooleanIfNoDefault(PREFERENCE_TEST_WEBSERVICE, defaults.testWebserviceDefault() );
    setDefaultBooleanIfNoDefault(PREFERENCE_MONITOR_WEBSERVICE, defaults.getMonitorWebServiceDefault());
    setDefaultBooleanIfNoDefault(PREFERENCE_LAUNCH_SAMPLE, defaults.launchSample() );
  }

  public String[] getNonJavaTestService()
  {
    WebServiceTestRegistry registry = WebServiceTestRegistry.getInstance();
    String[] testTypes = getWebServiceTestTypes();
    Vector newTestCases = new Vector();
	for (int i = 0; i < testTypes.length; i++)
    {
      WebServiceTestExtension wse = (WebServiceTestExtension) registry
          .getWebServiceExtensionsByName(testTypes[i]);
      if (wse.testWSDL()) 
		  newTestCases.addElement(testTypes[i]);
    }
	String[] wsdlTestArray = new String[newTestCases.size()];
	Enumeration e = newTestCases.elements();
	int i = 0;
	while(e.hasMoreElements()){
	  wsdlTestArray[i] = (String)e.nextElement();
	  i++;
	}
	
	return wsdlTestArray;
  }
  
  public void setWebServiceTestTypes(String[] ids)
  {
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < ids.length; i++)
    {
      if (i != 0) sb.append(",");
      sb.append(ids[i]);
    }
    setValue(PREFERENCE_WEBSERVICE_TEST_TYPES, sb.toString());
  }

  public String[] getWebServiceTestTypes()
  {
    StringTokenizer st = new StringTokenizer(
        getValueAsString(PREFERENCE_WEBSERVICE_TEST_TYPES), ",");
    String[] s = new String[st.countTokens()];
    for (int i = 0; i < s.length; i++)
      s[i] = st.nextToken();
    return s;
  }

  public ScenarioContext copy()
  {
    TransientScenarioContext context = new TransientScenarioContext();
    context.setWebServiceTestTypes(getWebServiceTestTypes());
    context.setNonJavaTestService(getNonJavaTestService());
    
    context.setClientWebServiceType( getClientWebServiceType() );
    context.setWebServiceType( getWebServiceType() );
    context.setLaunchWebServiceExplorer( getLaunchWebServiceExplorer() );
    context.setStartWebService( getStartWebService() );
    context.setGenerateProxy( getGenerateProxy() );
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
    return getValueAsString(PREFERENCE_CLIENT_WEBSERVICE_TYPE);
  }
  
  public String getClientWebServiceTypeDefault()
  {
    return getDefaultString(PREFERENCE_CLIENT_WEBSERVICE_TYPE);
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.jst.ws.internal.context.ScenarioContext#getGenerateProxy()
   */
  public boolean getGenerateProxy()
  {
    return getValueAsBoolean(PREFERENCE_GENERATE_PROXY);
  }
  
  public boolean getGenerateProxyDefault()
  {
    return getDefaultBoolean(PREFERENCE_GENERATE_PROXY);
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.jst.ws.internal.context.ScenarioContext#getLaunchWebServiceExplorer()
   */
  public boolean getLaunchWebServiceExplorer()
  {
    return getValueAsBoolean(PREFERENCE_LAUNCH_WEBSERVICE_EXPLORER);
  }
  
  public boolean getLaunchWebServiceExplorerDefault()
  {
    return getDefaultBoolean(PREFERENCE_LAUNCH_WEBSERVICE_EXPLORER);
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.jst.ws.internal.context.ScenarioContext#getStartWebService()
   */
  public boolean getStartWebService()
  {
    return getValueAsBoolean(PREFERENCE_START_WEBSERVICE);
  }
  
  public boolean getStartWebServiceDefault()
  {
    return getDefaultBoolean(PREFERENCE_START_WEBSERVICE);
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.jst.ws.internal.context.ScenarioContext#getInstallWebService()
   */
  public boolean getInstallWebService()
  {
    return getValueAsBoolean(PREFERENCE_INSTALL_WEBSERVICE);
  }
  
  public boolean getInstallWebServiceDefault()
  {
    return getDefaultBoolean(PREFERENCE_INSTALL_WEBSERVICE);
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.jst.ws.internal.context.ScenarioContext#getInstallWebService()
   */
  public boolean getInstallClient()
  {
    return getValueAsBoolean(PREFERENCE_INSTALL_CLIENT);
  }
  
  public boolean getInstallClientDefault()
  {
    return getDefaultBoolean(PREFERENCE_INSTALL_CLIENT);
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.jst.ws.internal.context.ScenarioContext#getTestWebService()
   */
  public boolean getTestWebService()
  {
    return getValueAsBoolean(PREFERENCE_TEST_WEBSERVICE);
  }
  
  public boolean getTestWebServiceDefault()
  {
    return getDefaultBoolean(PREFERENCE_TEST_WEBSERVICE);
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.jst.ws.internal.context.ScenarioContext#getWebServiceType()
   */
  public String getWebServiceType()
  {
    return getValueAsString(PREFERENCE_WEBSERVICE_TYPE);
  }
  
  public String getWebServiceTypeDefault()
  {
    return getDefaultString(PREFERENCE_WEBSERVICE_TYPE);
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.jst.ws.internal.context.ScenarioContext#setClientWebServiceType(java.lang.String)
   */
  public void setClientWebServiceType(String value)
  {
    setValue( PREFERENCE_CLIENT_WEBSERVICE_TYPE, value);
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.jst.ws.internal.context.ScenarioContext#setGenerateProxy(boolean)
   */
  public void setGenerateProxy(boolean value)
  {
    setValue( PREFERENCE_GENERATE_PROXY, value);
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.jst.ws.internal.context.ScenarioContext#setLaunchWebServiceExplorer(boolean)
   */
  public void setLaunchWebServiceExplorer(boolean value)
  {
    setValue( PREFERENCE_LAUNCH_WEBSERVICE_EXPLORER, value);
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.jst.ws.internal.context.ScenarioContext#setInstallWebService(boolean)
   */
  public void setInstallWebService(boolean value)
  {
    setValue( PREFERENCE_INSTALL_WEBSERVICE, value);
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.jst.ws.internal.context.ScenarioContext#setInstallWebService(boolean)
   */
  public void setInstallClient(boolean value)
  {
    setValue( PREFERENCE_INSTALL_CLIENT, value);
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.jst.ws.internal.context.ScenarioContext#setStartWebService(boolean)
   */
  public void setStartWebService(boolean value)
  {
    setValue( PREFERENCE_START_WEBSERVICE, value);
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.jst.ws.internal.context.ScenarioContext#setTestWebService(boolean)
   */
  public void setTestWebService(boolean value)
  {
    setValue( PREFERENCE_TEST_WEBSERVICE, value);
  }
  /* (non-Javadoc)
   * @see org.eclipse.jst.ws.internal.context.ScenarioContext#setWebServiceType(java.lang.String)
   */
  public void setWebServiceType(String value)
  {
    setValue( PREFERENCE_WEBSERVICE_TYPE, value);
  }

  /* (non-Javadoc)
   * @see org.eclipse.jst.ws.internal.context.ScenarioContext#getMonitorWebService()
   */
  public boolean getMonitorWebService()
  {
    return getValueAsBoolean(PREFERENCE_MONITOR_WEBSERVICE);
  }
  
  public boolean getMonitorWebServiceDefault()
  {
    return getDefaultBoolean(PREFERENCE_MONITOR_WEBSERVICE);
  }

  /* (non-Javadoc)
   * @see org.eclipse.jst.ws.internal.context.ScenarioContext#setMonitorWebService(boolean)
   */
  public void setMonitorWebService(boolean value)
  {
    setValue(PREFERENCE_MONITOR_WEBSERVICE, value);
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.jst.ws.internal.context.ScenarioContext#getLaunchSampleEnabledDefault()
   */
  public boolean getLaunchSampleEnabledDefault()
  {
    return getDefaultBoolean(PREFERENCE_LAUNCH_SAMPLE);
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.jst.ws.internal.context.ScenarioContext#isLaunchSampleEnabled()
   */
  public boolean isLaunchSampleEnabled()
  {
    return getValueAsBoolean(PREFERENCE_LAUNCH_SAMPLE);
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.jst.ws.internal.context.ScenarioContext#setLaunchSampleEnabled(boolean)
   */
  public void setLaunchSampleEnabled(boolean value)
  {
    setValue( PREFERENCE_LAUNCH_SAMPLE, value);
  }
}
