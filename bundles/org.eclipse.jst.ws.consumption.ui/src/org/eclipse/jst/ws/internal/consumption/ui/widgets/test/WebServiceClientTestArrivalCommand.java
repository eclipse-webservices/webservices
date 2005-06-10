/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.ui.widgets.test;

import java.util.List;
import java.util.ListIterator;

import org.eclipse.core.resources.IProject;
import org.eclipse.jem.java.JavaClass;
import org.eclipse.jem.java.JavaHelpers;
import org.eclipse.jem.java.Method;
import org.eclipse.jem.util.emf.workbench.ProjectUtilities;
import org.eclipse.jst.ws.internal.common.ServerUtils;
import org.eclipse.jst.ws.internal.consumption.command.common.JavaMofReflectionCommand;
import org.eclipse.jst.ws.internal.context.ScenarioContext;
import org.eclipse.jst.ws.internal.data.TypeRuntimeServer;
import org.eclipse.jst.ws.internal.ext.test.WebServiceTestRegistry;
import org.eclipse.jst.ws.internal.plugin.WebServicePlugin;
import org.eclipse.wst.command.internal.provisional.env.core.SimpleCommand;
import org.eclipse.wst.command.internal.provisional.env.core.common.Environment;
import org.eclipse.wst.command.internal.provisional.env.core.common.MessageUtils;
import org.eclipse.wst.command.internal.provisional.env.core.common.SimpleStatus;
import org.eclipse.wst.command.internal.provisional.env.core.common.Status;
import org.eclipse.wst.command.internal.provisional.env.core.common.StatusHandler;
import org.eclipse.wst.command.internal.provisional.env.core.selection.BooleanSelection;
import org.eclipse.wst.command.internal.provisional.env.core.selection.SelectionList;
import org.eclipse.wst.command.internal.provisional.env.core.selection.SelectionListChoices;
import org.eclipse.wst.common.componentcore.internal.util.IModuleConstants;
import org.eclipse.wst.server.core.IServer;


/**
* This task sets up all the defaults for the webservice test
* client page
*
*/
public class WebServiceClientTestArrivalCommand extends SimpleCommand
{
  public static final String DEFAULT_WEB_MODULE_ROOT = "WebContent";
  public static final String DEFAULT_SAMPLE_WEB_PROJECT_EXT = "Sample";
  public static final String DEFAULT_SAMPLE_EAR_PROJECT_EXT = "EAR";

  private String LABEL = "WebServiceClientTestArrivalTask";
  private String DESCRIPTION = "default actions";
  private MessageUtils msgUtils;
  public static String SAMPLE_DIR = "sample";
  public String SET_ENDPOINT = "setEndPoint(java.net.URL)";
  public String GET_ENDPOINT = "getEndPoint()";

  private String PROXY = "Proxy";

  private ScenarioContext scenarioContext;
 
  
  private String clientProject;
  private String clientP;
  private String clientComponent;
  private IProject clientIProject;
  private String clientProjectEAR;
  private String sampleServerTypeID;
  private IServer sampleExistingServer;
  private WebServiceTestRegistry testRegistry;
  private SelectionList testFacilities;
  private String folder;
  private String jspFolder;
  private BooleanSelection[] methods;
  private String proxyBean;
  private SelectionListChoices runtime2ClientTypes;
  private String sampleProject;
  private String sampleEAR;
  private TypeRuntimeServer clientIds;
  private String j2eeVersion;

  private String launchedServiceTestName;
  private boolean runClientTest=true;
  
  /**
  * Constructs a new WebServiceClientTestArrivalTask object with the given label and description.
  */
  public WebServiceClientTestArrivalCommand ()
  {
  	setDescription(DESCRIPTION);
  	setName(LABEL);
  	String pluginId = "org.eclipse.jst.ws.consumption.ui";
	msgUtils = new MessageUtils(pluginId + ".plugin", this);  	
    
    scenarioContext = WebServicePlugin.getInstance().getScenarioContext().copy();
  	testRegistry = WebServiceTestRegistry.getInstance();
  }

  public Status execute(Environment env)
  {
  	
	Status status = new SimpleStatus( "" );
  	
	sampleProjectAndEarSetup();
  	
    //Get the sample Folder ready
    StringBuffer sb = new StringBuffer();
    sb.append("/").append(sampleProject).append("/").append(DEFAULT_WEB_MODULE_ROOT).append("/");
    folder = SAMPLE_DIR + getBean(); 
        
    sb.append(folder);
    jspFolder = sb.toString();
    
    //get the method names ready
    //find the full path of the file in the project
    /*
    * Getting the method names using javamof introspection
    */
    if(proxyBean == null){
      StatusHandler sHandler = env.getStatusHandler();
      Status errorStatus = new SimpleStatus("", msgUtils.getMessage("MSG_ERROR_JTS_PROXY_NOT_COMPILED"), Status.ERROR);
      sHandler.reportError(errorStatus);
      return errorStatus;
      
    } 	
    
    
    JavaMofReflectionCommand javamofcommand = new JavaMofReflectionCommand(); 
    javamofcommand.setProxyBean(proxyBean);
    javamofcommand.setClientProject(clientP);
    
    
    
    try{ 
      Status mofStatus = javamofcommand.execute(env);
      if(mofStatus.getSeverity() == Status.ERROR)
      	return mofStatus;
    }catch(Exception exc){
    	StatusHandler sHandler = env.getStatusHandler();
        Status errorStatus = new SimpleStatus("", msgUtils.getMessage("MSG_ERROR_JTS_PROXY_NOT_COMPILED"), Status.ERROR);
        sHandler.reportError(errorStatus);
        return errorStatus;
    }  
    JavaHelpers javaHelpers = javamofcommand.getJavaClass();
    if (javaHelpers == null) return status;
      
    JavaClass javaClass = null; 
    if(javaHelpers instanceof JavaClass) 
     javaClass = (JavaClass)javaHelpers; 
    else return status; 
    List methodList = javaClass.getPublicMethods();
 	BooleanSelection[] tempMethods = new BooleanSelection[methodList.size()];
    ListIterator listIterator = methodList.listIterator();
    
    int j = 0;
 	while (listIterator.hasNext())
    {
      Method method = (Method)listIterator.next();
      if(method.isConstructor()) continue;
      String signature = method.getMethodElementSignature();
      
      tempMethods[j] = new BooleanSelection( method.getMethodElementSignature(), true);      
      j++;
    } 
    methods = tempMethods;
    return status;
  }
 
  
  public static String WEBID = "org.eclipse.jst.ws.consumption.ui.clientProjectType.Web"; 
  public static String EJBID = "org.eclipse.jst.ws.consumption.ui.clientProjectType.EJB";
  public static String APPCLIENT = "org.eclipse.jst.ws.consumption.ui.clientProjectType.AppClient";
  
  
  private void sampleProjectAndEarSetup()
  {
  	if(clientProject == null) return;
  	else{
	  int index = clientProject.indexOf("/");
	  clientP = clientProject.substring(0,index);
	  clientComponent = clientProject.substring(index + 1);
  	}
	  
	clientIProject = (IProject)ProjectUtilities.getProject(clientP);
  	//move to the web level
  	SelectionListChoices slc = runtime2ClientTypes.getChoice();
  	String projectType = slc.getList().getSelection();
  	if(projectType.equals(IModuleConstants.JST_WEB_MODULE)){
      sampleProject = clientProject;
  	}  	
  	else{ 
  	  sampleProject = (new StringBuffer(clientIProject.getName())).append(DEFAULT_SAMPLE_WEB_PROJECT_EXT).toString();	
  	}
  	sampleEAR = slc.getChoice().getChoice().getList().getSelection();
  	if (sampleEAR == null || sampleEAR.length()==0)
  	{
  	  //Create a default name if an EAR is needed.
		//Use the server type
		String targetId = ServerUtils.getRuntimeTargetIdFromFactoryId(clientIds.getServerId());
		if (targetId!=null && targetId.length()>0)
		{
		  if (ServerUtils.isTargetValidForEAR(targetId,j2eeVersion))
	  	  {
		    sampleEAR = (new StringBuffer(sampleProject)).append(DEFAULT_SAMPLE_EAR_PROJECT_EXT).toString();
	  	  }
		}  	  
  	}
  }
    
  //getters and setters
  
  private String getBean()
  {
  	int index = proxyBean.lastIndexOf(".");
  	String end = "";
  	if(index != -1){
  	  end = proxyBean.substring(index + 1);  	
  	}
  	String bean = end.substring(0,end.length());
    
  	return bean;
  }
  
  
  public boolean getRunClientTest()
  {
  	return runClientTest;
  }
  
  public String getFolder()
  {
  	return folder;
  }
  
  public String getJspFolder()
  {
  	return jspFolder;
  }
  
  public BooleanSelection[] getMethods()
  {
    return methods;  
  }
    
  public String getSampleProjectEAR()
  {
  	return sampleEAR;
  }
  
  public String getSampleProject()
  {
  	return sampleProject;
  }
       
  public void setClientProject(String clientProject)
  {
    this.clientProject = clientProject;
  }
  
  public void setProxyBean(String proxyBean)
  {
  	this.proxyBean = proxyBean;
  }
  
  public String getProxyBean()
  {
    return proxyBean;
  }

  public void setClientProjectEAR(String clientProjectEAR)
  {
    this.clientProjectEAR = clientProjectEAR;
  }	
  
  public void setRuntime2ClientTypes( SelectionListChoices selectionList )
  {
    runtime2ClientTypes = selectionList;  
  }

  public void setLaunchedServiceTestName(String launchedServiceTestName)
  {
  	this.launchedServiceTestName = launchedServiceTestName;
  }
  /**
   * @param clientIds The clientIds to set.
   */
  public void setClientIds(TypeRuntimeServer clientIds)
  {
    this.clientIds = clientIds;
  }
  
  /**
   * @param version The j2eeVersion to set.
   */
  public void setJ2eeVersion(String version)
  {
    j2eeVersion = version;
  }  
}
