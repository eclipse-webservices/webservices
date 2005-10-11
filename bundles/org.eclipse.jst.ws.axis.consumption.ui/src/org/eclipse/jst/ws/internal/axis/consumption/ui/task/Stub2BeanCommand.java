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
import org.eclipse.jst.ws.internal.axis.consumption.ui.util.WSDLUtils;
import org.eclipse.wst.command.internal.provisional.env.core.common.MessageUtils;
import org.eclipse.wst.command.internal.provisional.env.core.common.StatusUtils;
import org.eclipse.wst.common.componentcore.ModuleCoreNature;
import org.eclipse.wst.common.environment.Environment;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.ws.internal.parser.wsil.WebServicesParser;


public class Stub2BeanCommand extends AbstractDataModelOperation
{
  private WebServicesParser webServicesParser;
  private JavaWSDLParameter javaWSDLParam_;
  private String discoveredWsdlPortElementName;
  private Vector portTypes_;
  private String proxyBean_;
  
  private String module_ = "";
  
  private IProject clientProject_;
  
  private MessageUtils msgUtils_;

  public Stub2BeanCommand()
  {
    super();
    portTypes_ = new Vector();
    String pluginId = "org.eclipse.jst.ws.axis.consumption.ui";
    msgUtils_ = new MessageUtils(pluginId + ".plugin", this);    
    //setRunInWorkspaceModifyOperation(false);
  }
  
  public Stub2BeanCommand(String moduleName){
	  super();
	  portTypes_ = new Vector();	  
	  module_ = moduleName;
	  String pluginId = "org.eclipse.jst.ws.axis.consumption.ui";
	  msgUtils_ = new MessageUtils(pluginId + ".plugin", this);	  
  }

  /**
  * Execute
  */
	public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable ) 
	{
		Environment environment = getEnvironment();       
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
	 		   IStatus status = StatusUtils.errorStatus( msgUtils_.getMessage("MSG_WARN_NO_JAVA_NATURE"));	
	 		   environment.getStatusHandler().reportError(status);
	 		   return status;
		}    	
    }
    
    Map pkg2nsMapping = javaWSDLParam_.getMappings();
    Map services = def.getServices();
    for (Iterator it = services.values().iterator(); it.hasNext();)
    {
      Service service = (Service)it.next();
      String servicePkgName = WSDLUtils.getPackageName(service, pkg2nsMapping);
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
            stub2BeanInfo.setClientModuleName(module_);
            String portTypePkgName = WSDLUtils.getPackageName(portType, pkg2nsMapping);
            String portTypeClassName = computeClassName(portTypeQName.getLocalPart());
            stub2BeanInfo.setPackage(portTypePkgName);
            stub2BeanInfo.setClass(portTypeClassName + "Proxy");
            proxyBean_ = portTypePkgName+"."+portTypeClassName+"Proxy";
            if (jndiName.equals(portTypeClassName))
              portTypeClassName = portTypeClassName + "_PortType";
            stub2BeanInfo.addSEI(portTypePkgName, portTypeClassName, servicePkgName, serviceClassName, jndiName, port.getName());
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
  
  private final char UNDERSCORE = '_';

  private String computeClassName(String className)
  {
    String classNameCopy = className;
    int i = classNameCopy.indexOf(UNDERSCORE);
    while (i != -1)
    {
      char c = classNameCopy.charAt(i+1);
      if (Character.isLowerCase(c))
      {
        StringBuffer sb = new StringBuffer();
        sb.append(classNameCopy.substring(0, i+1));
        sb.append(Character.toUpperCase(c));
        sb.append(classNameCopy.substring(i+2, classNameCopy.length()));
        classNameCopy = sb.toString();
      }
      i = classNameCopy.indexOf(UNDERSCORE, i+1);
    }
    char[] cArray = new char[classNameCopy.length()];
    boolean foundDigit = false;
    for (int j = 0; j < cArray.length; j++)
    {
      char c = classNameCopy.charAt(j);
      if (Character.isDigit(c))
      {
        cArray[j] = c;
        foundDigit = true;
      }
      else
      {
        if (foundDigit)
          cArray[j] = Character.toUpperCase(c);
        else
          cArray[j] = c;
        foundDigit = false;
      }
    }
    return new String(cArray);
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
}
