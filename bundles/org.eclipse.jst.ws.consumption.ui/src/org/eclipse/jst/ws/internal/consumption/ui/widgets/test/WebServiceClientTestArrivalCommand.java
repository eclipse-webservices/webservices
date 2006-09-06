/*******************************************************************************
 * Copyright (c) 2003, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060608   144500 mahutch@ca.ibm.com - Mark Hutchinson
 * 20060818   153903 makandre@ca.ibm.com - Andrew Mak, Browse does not work in generate client test page
 * 20060906   154548 gilberta@ca.ibm.com - Gilbert Andrews, This fixes name collisions when creating a sample project
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.ui.widgets.test;

import java.util.List;
import java.util.ListIterator;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jem.java.JavaClass;
import org.eclipse.jem.java.JavaHelpers;
import org.eclipse.jem.java.Method;
import org.eclipse.jem.util.emf.workbench.ProjectUtilities;
import org.eclipse.jst.j2ee.internal.project.J2EEProjectUtilities;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.jst.ws.internal.consumption.command.common.JavaMofReflectionCommand;
import org.eclipse.jst.ws.internal.consumption.ui.ConsumptionUIMessages;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.command.internal.env.core.selection.BooleanSelection;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.common.environment.IStatusHandler;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;


/**
* This task sets up all the defaults for the webservice test
* client page
*
*/
public class WebServiceClientTestArrivalCommand extends AbstractDataModelOperation
{
  public static final String DEFAULT_WEB_MODULE_ROOT = "WebContent";
  public static final String DEFAULT_SAMPLE_WEB_PROJECT_EXT = "Sample";

  public static String SAMPLE_DIR = "sample";
  
  private String clientProject;
  private String clientProjectEAR;
  private String clientP;
  private String clientC;
  private IProject clientIProject;
  private String folder;
  private String jspFolder;
  private BooleanSelection[] methods;
  private String proxyBean;
  private String sampleProject;
  private String sampleP;
  private String sampleC;
  private String sampleProjectEAR;

  
  
  /**
  * Constructs a new WebServiceClientTestArrivalTask object with the given label and description.
  */
  public WebServiceClientTestArrivalCommand ()
  {

  }

  public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable )
  {
    IEnvironment env = getEnvironment();
  	
	IStatus status = Status.OK_STATUS;
  	
	sampleProjectAndEarSetup(env);
	IPath webcontentPath = J2EEUtils.getWebContentPath(clientIProject);
	
    //Get the sample Folder ready
    StringBuffer sb = new StringBuffer();

    // *need* to double-check that clientIProject is a web project
    if (webcontentPath != null && J2EEUtils.isWebComponent(clientIProject))
    {	
    	String path = webcontentPath.toString();
    	sb.append(path).append("/");
    }
    else
    {	//then just use the default
    	sb.append("/").append(sampleC).append("/").append(DEFAULT_WEB_MODULE_ROOT).append("/");
    }
    folder = SAMPLE_DIR + getBean(); 
        
    sb.append(folder);
    jspFolder = sb.toString();
    
    //get the method names ready
    //find the full path of the file in the project
    /*
    * Getting the method names using javamof introspection
    */
    if(proxyBean == null){
      IStatusHandler sHandler = env.getStatusHandler();
      IStatus errorStatus = StatusUtils.errorStatus( ConsumptionUIMessages.MSG_ERROR_JTS_PROXY_NOT_COMPILED );
      sHandler.reportError(errorStatus);
      return errorStatus;
      
    } 	
    
    
    JavaMofReflectionCommand javamofcommand = new JavaMofReflectionCommand(); 
    javamofcommand.setProxyBean(proxyBean);
    javamofcommand.setClientProject(clientP);
    javamofcommand.setEnvironment( env );
    
    
    
    try{ 
      IStatus mofStatus = javamofcommand.execute( monitor, null);
      if(mofStatus.getSeverity() == Status.ERROR)
      	return mofStatus;
    }catch(Exception exc){
    	IStatusHandler sHandler = env.getStatusHandler();
        IStatus errorStatus = StatusUtils.errorStatus( ConsumptionUIMessages.MSG_ERROR_JTS_PROXY_NOT_COMPILED );
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
      
      tempMethods[j] = new BooleanSelection( method.getMethodElementSignature(), true);      
      j++;
    } 
    methods = tempMethods;
    return status;
  }
  public static final String DEFAULT_SAMPLE_EAR_PROJECT_EXT = "EAR";
  
  private void sampleProjectAndEarSetup(IEnvironment env)
  {
	if(clientProject == null) return;
  	else{
	  int index = clientProject.indexOf("/");
	  clientP = clientProject.substring(0,index);
	  clientC = clientProject.substring(index + 1);
  	}
	  
	clientIProject = (IProject)ProjectUtilities.getProject(clientP);
  	
  	if(J2EEUtils.isWebComponent(clientIProject)){
      sampleProject = clientProject;
	  sampleP = clientP;
	  sampleC = clientC;
 	}  	
  	else{ 
  	  sampleP = clientP + DEFAULT_SAMPLE_WEB_PROJECT_EXT;
	  sampleC = clientC + DEFAULT_SAMPLE_WEB_PROJECT_EXT;
	  sampleProject = sampleP + "/" + sampleC;
	  
	  String sampleTemp = sampleP;
	  
	  boolean nameFound = false;
	  int i = 1;
	  while(!nameFound){
		  IProject sampleIProject = (IProject)ProjectUtilities.getProject(sampleTemp);
		  if(sampleIProject.exists() && !J2EEProjectUtilities.isDynamicWebProject(sampleIProject)){
			  sampleTemp = sampleP + Integer.toString(i);
			  sampleProject = sampleTemp + "/" + sampleC;
		      
		  }
		  else
			  nameFound = true; 
		  
		  i++;
	  }
	  sampleP = sampleTemp;
    
  	}  
	
  	
  	
  	
  	sampleProjectEAR = clientProjectEAR;
	if (sampleProjectEAR == null || sampleProjectEAR.length()==0){
	  sampleProjectEAR = sampleP + DEFAULT_SAMPLE_EAR_PROJECT_EXT + "/" + sampleC + DEFAULT_SAMPLE_EAR_PROJECT_EXT;	
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
    
  public String getSampleProject()
  {
  	return sampleProject;
  }
       
  public void setClientProject(String clientProject)
  {
    this.clientProject = clientProject;
  }
  
    
  public void setClientProjectEAR(String clientProjectEAR)
  {
    this.clientProjectEAR = clientProjectEAR;
  }
  
  
  public void setProxyBean(String proxyBean)
  {
  	this.proxyBean = proxyBean;
  }
  
  public String getSampleProjectEAR()
  {
    return sampleProjectEAR;
  }
  

}
