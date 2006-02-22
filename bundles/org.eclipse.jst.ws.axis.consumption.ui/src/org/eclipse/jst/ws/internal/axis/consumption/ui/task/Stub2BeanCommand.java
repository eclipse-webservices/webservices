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
 * 20060216   115144 pmoogk@ca.ibm.com - Peter Moogk
 *******************************************************************************/

package org.eclipse.jst.ws.internal.axis.consumption.ui.task;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.wsdl.Definition;
import javax.wsdl.Port;
import javax.wsdl.PortType;
import javax.wsdl.Service;
import javax.wsdl.extensions.soap.SOAPAddress;
import javax.xml.namespace.QName;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jst.ws.internal.axis.consumption.core.common.JavaWSDLParameter;
import org.eclipse.jst.ws.internal.axis.consumption.core.common.NameMappingUtils;
import org.eclipse.jst.ws.internal.axis.consumption.ui.AxisConsumptionUIMessages;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.common.componentcore.ModuleCoreNature;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.ws.internal.parser.wsil.WebServicesParser;


public class Stub2BeanCommand extends AbstractDataModelOperation
{
  private WebServicesParser webServicesParser;
  private JavaWSDLParameter javaWSDLParam_;
  private String discoveredWsdlPortElementName;
  private Vector portTypes_;
  private String proxyBean_;
  private String outputFolder_;
  
  private IProject clientProject_;
  
  public Stub2BeanCommand(){
	  super();
	  portTypes_ = new Vector();	  
  }

  /**
  * Execute
  */
	public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable ) 
	{
		IEnvironment environment = getEnvironment();       
    String inputWsdlLocation = javaWSDLParam_.getInputWsdlLocation();
    Definition def = webServicesParser.getWSDLDefinition(inputWsdlLocation);
    /*
    * Hack: Axis is not using a proper java.net.URL as its inputWsdlLocation.
    * We need to convert it to a proper file URL.
    */
    if (def == null)
    {
      File file = new File(inputWsdlLocation);
      if (file.exists())
      {
        try
        {
          def = webServicesParser.getWSDLDefinition(file.toURL().toString());
        }
        catch (MalformedURLException murle)
        {
        }
      }
    }
    
    //Ensure the client project is either a flexible project or a Java project
    ModuleCoreNature mn = ModuleCoreNature.getModuleCoreNature(clientProject_);
    if (mn==null)
    {
        // Check if it's a plain old Java project
    	IJavaProject javaProject = null;    	  
		javaProject = JavaCore.create(clientProject_);    
		if (javaProject == null)
		{
	 		   IStatus status = StatusUtils.errorStatus( AxisConsumptionUIMessages.MSG_WARN_NO_JAVA_NATURE);	
	 		   environment.getStatusHandler().reportError(status);
	 		   return status;
		}    	
    }
    
    Map pkg2nsMapping = javaWSDLParam_.getMappings();
    Map services = def.getServices();
    for (Iterator it = services.values().iterator(); it.hasNext();)
    {
      Service service = (Service)it.next();
      String servicePkgName = NameMappingUtils.getPackageName(service.getQName().getNamespaceURI(), pkg2nsMapping);
      String serviceClassName = computeClassName(service.getQName().getLocalPart());
      String jndiName = serviceClassName;
      Map ports = service.getPorts();
      for (Iterator it2 = ports.values().iterator(); it2.hasNext();)
      {
        if (serviceClassName.equals(computeClassName(((Port)it2.next()).getBinding().getPortType().getQName().getLocalPart())))
        {
          serviceClassName = serviceClassName + "_Service";
          break;
        }
      }
      for (Iterator it2 = ports.values().iterator(); it2.hasNext();)
      {
        Port port = (Port)it2.next();
        if (discoveredWsdlPortElementName != null && !discoveredWsdlPortElementName.equals(port.getName()))
          continue;
        SOAPAddress soapAddress = null;
        List extensibilityElements = port.getExtensibilityElements();
        if (extensibilityElements != null)
        {
          for (Iterator it3 = extensibilityElements.iterator(); it3.hasNext();)
          {
            Object object = it3.next();
            if (object instanceof SOAPAddress)
            {
              soapAddress = (SOAPAddress)object;
              break;
            }
          }
        }
        if (soapAddress != null)
        {
          PortType portType = port.getBinding().getPortType();
          QName portTypeQName = portType.getQName();
          StringBuffer portTypeID = new StringBuffer();
          portTypeID.append(portTypeQName.getNamespaceURI());
          portTypeID.append("#");
          portTypeID.append(portTypeQName.getLocalPart());
          if (!portTypes_.contains(portTypeID.toString()))
          {
            portTypes_.add(portTypeID.toString());
            Stub2BeanInfo stub2BeanInfo = new Stub2BeanInfo();
            stub2BeanInfo.setClientProject(clientProject_);
            stub2BeanInfo.setOutputFolder( outputFolder_ );
            String portTypePkgName = NameMappingUtils.getPackageName(portType.getQName().getNamespaceURI(), pkg2nsMapping);
            String portTypeClassName = computeClassName(portTypeQName.getLocalPart());
            stub2BeanInfo.setPackage(portTypePkgName);
            stub2BeanInfo.setClass(portTypeClassName + "Proxy");
            proxyBean_ = portTypePkgName+"."+portTypeClassName+"Proxy";
            if (jndiName.equals(portTypeClassName))
              portTypeClassName = portTypeClassName + "_PortType";
            stub2BeanInfo.addSEI(portTypePkgName, portTypeClassName, servicePkgName, serviceClassName, jndiName, NameMappingUtils.getPortName(port.getName()));
            try
            {              
              stub2BeanInfo.write( monitor, environment.getStatusHandler() );
              if (discoveredWsdlPortElementName != null)
              {
                // The discovered port was processed. Ignore all other ports and services.
                return Status.OK_STATUS;
              }
            }
            catch (CoreException ce)
            {
            }
            catch (IOException ioe)
            {
            }
          }
        }
      }
    }
    return Status.OK_STATUS;
  }

  private String computeClassName(String className)
  {
	  return NameMappingUtils.xmlNameToJavaClass(className);
  }

  /**
  * Returns the javaWSDLParam.
  * @return JavaWSDLParameter
  */
  public JavaWSDLParameter getJavaWSDLParam()
  {
    return javaWSDLParam_;
  }

  /**
  * Sets the javaWSDLParam.
  * @param javaWSDLParam The javaWSDLParam to set
  */
  public void setJavaWSDLParam(JavaWSDLParameter javaWSDLParam)
  {
    javaWSDLParam_ = javaWSDLParam;
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


  /**
   * @param discoveredWsdlPortElementName The discoveredWsdlPortElementName to set.
   */
  public void setDiscoveredWsdlPortElementName(String discoveredWsdlPortElementName) {
    this.discoveredWsdlPortElementName = discoveredWsdlPortElementName;
  }

  /**
	* @param clientProject The clientProject to set.
	*/
  public void setClientProject(IProject clientProject) {
		this.clientProject_ = clientProject;
  }
	
	/**
	 * @return Returns the proxyBean.
	 */
	public String getProxyBean() {
		return proxyBean_;
	}	
	
	public void setOutputFolder( String outputFolder )
	{
		outputFolder_ = outputFolder;
	}
}
