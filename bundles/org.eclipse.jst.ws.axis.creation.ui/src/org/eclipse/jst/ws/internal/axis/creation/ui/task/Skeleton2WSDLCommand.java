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
package org.eclipse.jst.ws.internal.axis.creation.ui.task;


import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.wsdl.Binding;
import javax.wsdl.Definition;
import javax.wsdl.Port;
import javax.wsdl.Service;
import javax.wsdl.extensions.soap.SOAPAddress;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jst.j2ee.internal.ejb.project.EJBNatureRuntime;
import org.eclipse.jst.j2ee.internal.project.IEJBNatureConstants;
import org.eclipse.jst.j2ee.internal.project.IWebNatureConstants;
import org.eclipse.jst.j2ee.internal.web.operations.J2EEWebNatureRuntime;
import org.eclipse.jst.ws.internal.axis.consumption.core.common.JavaWSDLParameter;
import org.eclipse.jst.ws.internal.axis.consumption.util.FileUtil;
import org.eclipse.jst.ws.internal.axis.consumption.util.PlatformUtils;
import org.eclipse.jst.ws.internal.axis.creation.ui.plugin.WebServiceAxisCreationUIPlugin;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.internal.consumption.command.common.CopyWSDLCommand;
import org.eclipse.jst.ws.internal.consumption.util.WSDLUtils;
import org.eclipse.wst.command.env.core.SimpleCommand;
import org.eclipse.wst.command.env.core.common.Environment;
import org.eclipse.wst.command.env.core.common.MessageUtils;
import org.eclipse.wst.command.env.core.common.SimpleStatus;
import org.eclipse.wst.command.env.core.common.Status;
import org.eclipse.wst.command.env.ui.eclipse.EclipseEnvironment;
import org.eclipse.wst.ws.parser.wsil.WebServicesParser;

public class Skeleton2WSDLCommand extends SimpleCommand
{
  private static final String LABEL = "TASK_LABEL_SKELETON_WSDL";
  private static final String DESCRIPTION = "TASK_DESC_SKELETON_WSDL";
  private static final String IMPL = "Impl";	//$NON-NLS-1$
  private static final String SERVICE_EXT = "/services/";	//$NON-NLS-1$
  private static final String WSDL_EXT = "wsdl";	//$NON-NLS-1$
  private static final String DOT = ".";	//$NON-NLS-1$
  private static final String SLASH = "/";	//$NON-NLS-1$
  private static final String PROTOCOL_SUFFIX = "://";	//$NON-NLS-1$
  private final String WSDL_FOLDER = "wsdl"; //$NON-NLS-1$
  private WebServicesParser webServicesParser;
  private JavaWSDLParameter javaWSDLParam;
  private IProject serverProject;
  private HashMap visitedLinks;
  private MessageUtils msgUtils_;

  public Skeleton2WSDLCommand() {
    super(WebServiceAxisCreationUIPlugin.getMessage(LABEL), WebServiceAxisCreationUIPlugin.getMessage(DESCRIPTION));
	msgUtils_ = new MessageUtils( "org.eclipse.jst.ws.axis.creation.ui.plugin", this );
    setName( LABEL );
    setDescription( DESCRIPTION );
  }

  /**
  * Execute Skeleton2WSDLCommand
  */
  public Status execute(Environment env)
  {
    if (!(env instanceof EclipseEnvironment))
    {
      Status status = new SimpleStatus(WebServiceAxisCreationUIPlugin.ID, msgUtils_.getMessage("MSG_ERROR_NOT_IN_ECLIPSE_ENVIRONMENT", new String[] {"Skeleton2WSDLCommand"}), Status.ERROR, null);
      env.getStatusHandler().reportError(status);
      return status;
    }
    if (javaWSDLParam == null)
    {
      Status status = new SimpleStatus(WebServiceAxisCreationUIPlugin.ID, msgUtils_.getMessage("MSG_ERROR_JAVA_WSDL_PARAM_NOT_SET"), Status.ERROR, null);
      env.getStatusHandler().reportError(status);
      return status;
    }

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
    if (definition != null) {
      StringBuffer beanName = new StringBuffer();
      beanName.append(WSDLUtils.getPackageName(definition));
      beanName.append(DOT);
      try{
      service = (Service) definition.getServices().values().iterator().next();
      port = (Port) service.getPorts().values().iterator().next();
      Binding binding = port.getBinding();
      beanName.append(binding.getQName().getLocalPart());
      beanName.append(IMPL);
      javaWSDLParam.setBeanName(beanName.toString());

      javaWSDLParam.setPortTypeName(WSDLUtils.getPortTypeName(definition));
      javaWSDLParam.setServiceName(
        WSDLUtils.getServiceElementName(definition));
      javaWSDLParam.setBeanPackage(WSDLUtils.getPackageName(definition));
      }
      catch(Throwable e){
      	e.printStackTrace();
      }
    } 
    else {
      Status status = new SimpleStatus(WebServiceAxisCreationUIPlugin.ID, msgUtils_.getMessage("MSG_ERROR_WSDL_NO_DEFINITION", new String[] {wsdlURL}), Status.ERROR, null);
      env.getStatusHandler().reportError(status);
      return status;
    }

    // Modify WSDL endpoint
    if (port == null) {
      Status status = new SimpleStatus(WebServiceAxisCreationUIPlugin.ID, msgUtils_.getMessage("MSG_ERROR_WSDL_NO_PORT", new String[] {wsdlURL}), Status.ERROR, null);
      env.getStatusHandler().reportError(status);
      return status;
    }
    Map services = definition.getServices();
    for (Iterator servicesIt = services.values().iterator(); servicesIt.hasNext();)
    {
      Service s = (Service)servicesIt.next();
      Map ports = s.getPorts();
      for (Iterator portsIt = ports.values().iterator(); portsIt.hasNext();)
      {
        Port p = (Port)portsIt.next();
        modifyEndpoint(p);
      }
    }

    visitedLinks = new HashMap();

    // Set WSDL file name: javaWSDLParam.setOutputWsdlLocation();
    IWorkspaceRoot workspace = ResourcesPlugin.getWorkspace().getRoot();

    String wsdlLocation = javaWSDLParam.getOutputWsdlLocation();
    IPath wsdlPath = null;
    IFile outputFile = null;
    try {
     if(wsdlLocation != null){
		outputFile = workspace.getFileForLocation( new Path(wsdlLocation));
     }
     else{
		wsdlPath = serverProject.getFullPath();
		if (serverProject.hasNature(IWebNatureConstants.J2EE_NATURE_ID)) {
			   J2EEWebNatureRuntime webProject =
				 (J2EEWebNatureRuntime) serverProject.getNature(
				   IWebNatureConstants.J2EE_NATURE_ID);
			   wsdlPath = webProject.getWebModulePath();
			   //wsdlPath = wsdlPath.append(webProject.getWEBINFPath());
			 }
		else if (serverProject.hasNature(IEJBNatureConstants.NATURE_ID))
		{
			EJBNatureRuntime ejbProject = EJBNatureRuntime.getRuntime(serverProject);
			wsdlPath = wsdlPath.append(ejbProject.getMetaFolder().getProjectRelativePath().addTrailingSeparator());
		}		

			 IPath wsdlFilePath = wsdlPath.append(WSDL_FOLDER).append(port.getName()).addFileExtension(WSDL_EXT);
		     IFolder folder = ResourceUtils
								.getWorkspaceRoot()
								.getFolder(wsdlPath.append(WSDL_FOLDER));
			 FileUtil.createFolder(folder, true, true);	
		     outputFile = workspace.getFile(wsdlFilePath);
			 wsdlLocation = outputFile.getLocation().toString();
			 javaWSDLParam.setOutputWsdlLocation(wsdlLocation);
			 
     }

     // copy WSDL
     CopyWSDLCommand copyWSDLCommand = new CopyWSDLCommand();
     copyWSDLCommand.setWebServicesParser(webServicesParser);
     copyWSDLCommand.setWsdlURI(wsdlURL);
     copyWSDLCommand.setDestinationURI(outputFile.getLocation().toFile().toURL().toString());
     copyWSDLCommand.setDefinition(definition);
     Status status = copyWSDLCommand.execute(env);
     if(status!=null && status.getSeverity()==Status.ERROR) {
       return status;
     }
    } 
    catch (Exception e) {
      Status status = new SimpleStatus(WebServiceAxisCreationUIPlugin.ID, msgUtils_.getMessage("MSG_ERROR_WRITE_WSDL", new String[] { wsdlLocation }), Status.ERROR, e);
      env.getStatusHandler().reportError(status);
      return status;
    }
    return new SimpleStatus("");
  }

  private Status modifyEndpoint(Port port)
  {
    Iterator it = port.getExtensibilityElements().iterator();
    SOAPAddress soapAddress = null;
    while (it.hasNext())
    {
      Object obj = it.next();
      if (obj instanceof SOAPAddress)
      {
        soapAddress = (SOAPAddress)obj;
        break;
      }
    }
    if (soapAddress != null)
    {
      String projectURL = ResourceUtils.getEncodedWebProjectURL(serverProject);
      if (projectURL == null)
        return new SimpleStatus(WebServiceAxisCreationUIPlugin.ID, msgUtils_.getMessage("MSG_ERROR_PROJECT_URL", new String[] {serverProject.toString()}), Status.ERROR, null);
      StringBuffer serviceURL = new StringBuffer(projectURL);
      serviceURL.append(SERVICE_EXT).append(port.getName());
      javaWSDLParam.setUrlLocation(serviceURL.toString());
      soapAddress.setLocationURI(serviceURL.toString());
    }
    return new SimpleStatus("");
  }
  
  /**
  * Returns the javaWSDLParam.
  * @return JavaWSDLParameter
  */
  public JavaWSDLParameter getJavaWSDLParam() {
    return javaWSDLParam;
  }

  /**
  * Sets the javaWSDLParam.
  * @param javaWSDLParam The javaWSDLParam to set
  */
  public void setJavaWSDLParam(JavaWSDLParameter javaWSDLParam) {
    this.javaWSDLParam = javaWSDLParam;
  }

  /**
   * @param serverProject The serverProject to set.
   */
  public void setServerProject(IProject serverProject) {
    this.serverProject = serverProject;
  }

  /**
   * @return Returns the webServicesParser.
   */
  public WebServicesParser getWebServicesParser() {
    return webServicesParser;
  }

  /**
   * @param webServicesParser The webServicesParser to set.
   */
  public void setWebServicesParser(WebServicesParser webServicesParser) {
    this.webServicesParser = webServicesParser;
  }
  
  public String getWsdlURI()
  {
  	File file = new File(getJavaWSDLParam().getOutputWsdlLocation()); 
    String url = "";
	  try {
    	url = file.toURL().toString();
    }
	  catch(MalformedURLException mue){}
	return url;
  }

}
