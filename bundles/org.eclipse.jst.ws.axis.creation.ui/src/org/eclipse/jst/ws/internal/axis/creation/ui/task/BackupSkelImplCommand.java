/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060330 128827   kathy@ca.ibm.com - Kathy Chan
 *******************************************************************************/
package org.eclipse.jst.ws.internal.axis.creation.ui.task;


import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.wsdl.Binding;
import javax.wsdl.Definition;
import javax.wsdl.Port;
import javax.wsdl.Service;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.internal.axis.consumption.core.AxisConsumptionCoreMessages;
import org.eclipse.jst.ws.internal.axis.consumption.core.common.JavaWSDLParameter;
import org.eclipse.jst.ws.internal.axis.consumption.ui.AxisConsumptionUIMessages;
import org.eclipse.jst.ws.internal.axis.consumption.ui.util.PlatformUtils;
import org.eclipse.jst.ws.internal.axis.consumption.ui.util.WSDLUtils;
import org.eclipse.jst.ws.internal.plugin.WebServicePlugin;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.command.internal.env.common.FileResourceUtils;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.command.internal.env.core.context.ResourceContext;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.common.environment.IStatusHandler;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.ws.internal.parser.wsil.WebServicesParser;
import org.eclipse.wst.ws.internal.wsrt.WebServiceInfo;

public class BackupSkelImplCommand extends AbstractDataModelOperation
{
  private final String IMPL = "Impl";	//$NON-NLS-1$
  private final String DOT = ".";	//$NON-NLS-1$
  private final String BAK_EXT = "bak";	//$NON-NLS-1$
  private final String JAVA = "java";	//$NON-NLS-1$
  private WebServicesParser webServicesParser;
  private JavaWSDLParameter javaWSDLParam; 
  
  private WebServiceInfo webServiceInfo;

  public BackupSkelImplCommand( ) {
  }
  
  
  public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable ) 
  {
	  IEnvironment environment = getEnvironment();
	  if (javaWSDLParam == null)
	  {
		  IStatus status = StatusUtils.errorStatus(AxisConsumptionCoreMessages.MSG_ERROR_JAVA_WSDL_PARAM_NOT_SET);
		  environment.getStatusHandler().reportError(status);
		  return status;
	  }
	  
	  IStatus status = Status.OK_STATUS;
	  // Read WSDL
	  Definition definition = null;
	  String wsdlURL = javaWSDLParam.getInputWsdlLocation();
	  try
	  {
		  URL url = new URL(wsdlURL);
		  definition = webServicesParser.getWSDLDefinition(url.toString());
	  }
	  catch(MalformedURLException e)
	  {
		  wsdlURL = PlatformUtils.getFileURLFromPath(new Path(wsdlURL));
		  definition = webServicesParser.getWSDLDefinition(wsdlURL);
	  }
	  
	  // Compute the qualified name of the Java bean skeleton
	  Service service = null;
	  Port port = null;
	  ArrayList implURLList = new ArrayList();
	  if (definition != null) {
		  StringBuffer beanName = new StringBuffer();
		  String beanPackageName = WSDLUtils.getPackageName(definition);
		  javaWSDLParam.setBeanPackage(beanPackageName);
		  beanName.append(beanPackageName);
		  beanName.append(DOT);
		  
		  service = (Service) definition.getServices().values().iterator().next();
		  port = (Port) service.getPorts().values().iterator().next();
		  Binding binding = port.getBinding();
		  beanName.append(binding.getQName().getLocalPart());
		  beanName.append(IMPL);
		  String beanNameString = beanName.toString();
		  javaWSDLParam.setBeanName(beanNameString);
		  
		  // Check if the skeleton implementation bean already exist or not.  
		  // If it does, then back it up as xxx.java.bak before proceeding to call the Axis emitter
		  
		  FileInputStream finStream = null;
		  
		  ResourceContext context = WebServicePlugin.getInstance().getResourceContext();
		  IStatusHandler statusHandler = environment.getStatusHandler();
		  
		  String beanNamePathString = beanNameString.replace('.',IPath.SEPARATOR);
		  IPath skelImplPath = new Path (javaWSDLParam.getJavaOutput()).append(new Path (beanNamePathString)).addFileExtension(JAVA);
		  
		  // store the name of the implURL
		  String implURLString;
		  try {
			  implURLString = skelImplPath.toFile().toURL().toString();
		  } catch (MalformedURLException e1) {
			  implURLString = PlatformUtils.getFileURLFromPath(skelImplPath);
		  }
		  implURLList.add( implURLString );
		  
		  if (skelImplPath.toFile().exists())  {
			  
			  IPath targetPath = skelImplPath.addFileExtension(BAK_EXT);
			  try {
				  finStream = new FileInputStream(skelImplPath.toString());
				  if (finStream != null) {
					  FileResourceUtils.createFileAtLocation(context, targetPath.makeAbsolute(), finStream,
							  monitor, statusHandler);
					  finStream.close();
				  }
			  } catch (Exception e) {
				  status = StatusUtils.errorStatus(NLS.bind(AxisConsumptionCoreMessages.MSG_ERROR_MOVE_RESOURCE,new String[]{e.getLocalizedMessage()}), e);
				  environment.getStatusHandler().reportError(status);
			  } finally {
				  try {
					  if (finStream != null) {
						  finStream.close();
					  }
				  } catch (IOException e) {
				  }
			  }
		  }
		  
		  if (context.isSkeletonMergeEnabled()) {
			  String[] implURLArray = new String[implURLList.size()];
			  webServiceInfo.setImplURLs( (String[]) (implURLList.toArray(implURLArray)));
		  }
	  } 
	  else {
		  status = StatusUtils.errorStatus( NLS.bind(AxisConsumptionUIMessages.MSG_ERROR_WSDL_NO_DEFINITION, new String[] {wsdlURL}));
		  environment.getStatusHandler().reportError(status);
	  }  
	  
	  return status;
  }
  

  /**
  * Sets the javaWSDLParam.
  * @param javaWSDLParam The javaWSDLParam to set
  */
  public void setJavaWSDLParam(JavaWSDLParameter javaWSDLParam) {
    this.javaWSDLParam = javaWSDLParam;
  }

  /**
   * @param webServicesParser The webServicesParser to set.
   */
  public void setWebServicesParser(WebServicesParser webServicesParser) {
    this.webServicesParser = webServicesParser;
  }


public void setWebServiceInfo(WebServiceInfo webServiceInfo) {
	this.webServiceInfo = webServiceInfo;
}
  

}
