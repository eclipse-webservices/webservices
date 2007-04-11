/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060221   119111 rsinha@ca.ibm.com - Rupam Kuehner
 * 20060330   124667 kathy@ca.ibm.com - Kathy Chan
 * 20061004   159356 kathy@ca.ibm.com - Kathy Chan, Get correct module root URL based on server chosen
 * 20070112   165721 makandre@ca.ibm.com - Andrew Mak, WSDL import cannot use relative import with to parent directories
 * 20070125   171071 makandre@ca.ibm.com - Andrew Mak, Create public utility method for copying WSDL files
 * 20070409   181635 makandre@ca.ibm.com - Andrew Mak, WSDLCopier utility should create target folder
 *******************************************************************************/
package org.eclipse.jst.ws.internal.axis.creation.ui.task;


import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

import javax.wsdl.Definition;
import javax.wsdl.Port;
import javax.wsdl.Service;
import javax.wsdl.extensions.soap.SOAPAddress;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.internal.axis.consumption.core.AxisConsumptionCoreMessages;
import org.eclipse.jst.ws.internal.axis.consumption.core.common.JavaWSDLParameter;
import org.eclipse.jst.ws.internal.axis.consumption.ui.AxisConsumptionUIMessages;
import org.eclipse.jst.ws.internal.axis.consumption.ui.util.FileUtil;
import org.eclipse.jst.ws.internal.axis.consumption.ui.util.PlatformUtils;
import org.eclipse.jst.ws.internal.axis.consumption.ui.util.WSDLUtils;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.internal.common.ServerUtils;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.ws.internal.parser.wsil.WebServicesParser;
import org.eclipse.wst.ws.internal.util.WSDLCopier;

public class Skeleton2WSDLCommand extends AbstractDataModelOperation
{

  private static final String SERVICE_EXT = "/services/";	//$NON-NLS-1$
  private static final String WSDL_EXT = "wsdl";	//$NON-NLS-1$
  private final String WSDL_FOLDER = "wsdl"; //$NON-NLS-1$
  private WebServicesParser webServicesParser;
  private JavaWSDLParameter javaWSDLParam;
  private IProject serverProject;
  private String serviceServerTypeID_;  
  private IServer serviceExistingServer;  
  private final String TEMP_URI = "http://tempuri.org/"; //$NON-NLS-1$

  public Skeleton2WSDLCommand( ) {
  }

  /**
  * Execute Skeleton2WSDLCommand
  */
	public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable ) 
	{
		IEnvironment environment = getEnvironment();
		if (javaWSDLParam == null)
		{
			IStatus status = StatusUtils.errorStatus(AxisConsumptionCoreMessages.MSG_ERROR_JAVA_WSDL_PARAM_NOT_SET);
			environment.getStatusHandler().reportError(status);
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
    	
    	// beanName and beanPackageName are now set in BackupSkelImplCommand
    	
    	service = (Service) definition.getServices().values().iterator().next();
    	port = (Port) service.getPorts().values().iterator().next();
    	
    	javaWSDLParam.setPortTypeName(WSDLUtils.getPortTypeName(definition));
    	javaWSDLParam.setServiceName(WSDLUtils.getServiceElementName(definition));

    } 
    else {
      IStatus status = StatusUtils.errorStatus( NLS.bind(AxisConsumptionUIMessages.MSG_ERROR_WSDL_NO_DEFINITION, new String[] {wsdlURL}));
      environment.getStatusHandler().reportError(status);
      return status;
    }

    // Modify WSDL endpoint
    if (port == null) {
      IStatus status = StatusUtils.errorStatus( NLS.bind(AxisConsumptionUIMessages.MSG_ERROR_WSDL_NO_PORT, new String[] {wsdlURL}));
      environment.getStatusHandler().reportError(status);
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
//		wsdlPath = serverProject.getFullPath();
//		
//		if (serverProject.hasNature(IWebNatureConstants.J2EE_NATURE_ID)) {
//			   J2EEWebNatureRuntime webProject =
//				 (J2EEWebNatureRuntime) serverProject.getNature(
//				   IWebNatureConstants.J2EE_NATURE_ID);
//			   wsdlPath = J2EEUtils.getWebContentPath( serverProject, moduleName_ );
//			   //wsdlPath = wsdlPath.append(webProject.getWEBINFPath());
//			 }
//		else if (serverProject.hasNature(IEJBNatureConstants.NATURE_ID))
//		{
//			EJBNatureRuntime ejbProject = EJBNatureRuntime.getRuntime(serverProject);
//			wsdlPath = wsdlPath.append(ejbProject.getMetaFolder().getProjectRelativePath().addTrailingSeparator());
//		}		
// TODO:  handle EJB case
		     wsdlPath = J2EEUtils.getWebContentPath( serverProject );
			 IPath wsdlFilePath = wsdlPath.append(WSDL_FOLDER).append(port.getName()).addFileExtension(WSDL_EXT);
		     IFolder folder = ResourceUtils
								.getWorkspaceRoot()
								.getFolder(wsdlPath.append(WSDL_FOLDER));
			 FileUtil.createFolder(folder, true, true);	
		     outputFile = workspace.getFile(wsdlFilePath);
			 wsdlLocation = outputFile.getLocation().toString();
			 javaWSDLParam.setOutputWsdlLocation(wsdlLocation);
			 
     }

     String folder   = outputFile.getParent().getLocation().toFile().toURL().toString();
     String filename = outputFile.getName();
	
     WSDLCopier copier = new WSDLCopier(webServicesParser);
     copier.setSourceURI(wsdlURL, definition);
     copier.setTargetFolderURI(folder);
     copier.setTargetFilename(filename);
	
     ResourceUtils.getWorkspace().run(copier, monitor);
	
     String newPath = outputFile.getParent().getLocation().append(copier.getRelativePath()).toString();
     javaWSDLParam.setOutputWsdlLocation(newPath);     
    } 
    catch (Exception e) {
      IStatus status = StatusUtils.errorStatus( NLS.bind(AxisConsumptionUIMessages.MSG_ERROR_WRITE_WSDL, new String[] { wsdlLocation }), e);
      environment.getStatusHandler().reportError(status);
      return status;
    }
    return Status.OK_STATUS;
  }

  private IStatus modifyEndpoint(Port port)
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
      
      String projectURL = null;
      if (serviceServerTypeID_ != null && serviceServerTypeID_.length()>0)
      {
	    projectURL = ServerUtils.getEncodedWebComponentURL(serverProject, serviceServerTypeID_, serviceExistingServer);
      }
      
      if (projectURL == null) // either no server type defined or server not able to get project URL
      {
        projectURL = TEMP_URI + serverProject.getName(); 
        javaWSDLParam.setGuessProjectURL(true);
      }
      javaWSDLParam.setProjectURL(projectURL);
      StringBuffer serviceURL = new StringBuffer(projectURL);
      serviceURL.append(SERVICE_EXT).append(port.getName());
      javaWSDLParam.setUrlLocation(serviceURL.toString());
      soapAddress.setLocationURI(serviceURL.toString());
    }
    return Status.OK_STATUS;
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
  
  public void setServiceServerTypeID(String id)
  {
    serviceServerTypeID_ = id;
  }

public void setServiceExistingServer(IServer existingServer) {
	serviceExistingServer = existingServer;
}  

}
