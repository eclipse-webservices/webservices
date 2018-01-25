/*******************************************************************************
 * Copyright (c) 2006, 2007 IBM Corporation and others.
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
 * 20060403 128827   kathy@ca.ibm.com - Kathy Chan
 * 20060524 127343   mahutch@ca.ibm.com - Mark Hutchinson
 * 20070116 169138   mahutch@ca.ibm.com - Mark Hutchinson	
 * 20070815   188999 pmoogk@ca.ibm.com - Peter Moogk
 * 20071113   209075 kathy@ca.ibm.com - Kathy Chan
 *******************************************************************************/
package org.eclipse.jst.ws.internal.axis.creation.ui.task;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

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
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.ws.internal.parser.wsil.WebServicesParser;
import org.eclipse.wst.ws.internal.wsrt.WebServiceInfo;

public class BackupSkelImplCommand extends AbstractDataModelOperation
{
  private final String IMPL = "Impl";	//$NON-NLS-1$
  private final String DOT = ".";	//$NON-NLS-1$
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
		  
		  //first check if there is a mapping for this namespace to a package name
		  //if not then compute package name from namespace
		  String beanPackageName = null;
		  Map mappings = javaWSDLParam.getMappings();
		  if (mappings != null)
		  {
			  String targetNamespace = definition.getTargetNamespace();
			  beanPackageName = (String)mappings.get(targetNamespace);
		  }
		  if (beanPackageName == null)
		  {
			  beanPackageName = WSDLUtils.getPackageName(definition);
		  }		  
		  
		  javaWSDLParam.setBeanPackage(beanPackageName);		  
		  beanName.append(beanPackageName);
		  beanName.append(DOT);
		  
		  service = (Service) definition.getServices().values().iterator().next();
		  port = (Port) service.getPorts().values().iterator().next();
		  Binding binding = port.getBinding();
		  
		  String qName = binding.getQName().getLocalPart();		  
		  beanName.append(qName.substring(0,1).toUpperCase());//Fix for bug 169138, need to ensure first char is uppercase
		  beanName.append(qName.substring(1));

		  beanName.append(IMPL);
		  String beanNameString = beanName.toString();
		  javaWSDLParam.setBeanName(beanNameString);
		  
		  // Defect 209075 - Do not back up anymore since skeleton merge is defaulted to on.
		  
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
		  
		  String[] implURLArray = new String[implURLList.size()];
		  webServiceInfo.setImplURLs( (String[]) (implURLList.toArray(implURLArray)));

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
