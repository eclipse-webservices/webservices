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
package org.eclipse.jst.ws.internal.axis.creation.ui.widgets.bean;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jst.ws.internal.axis.consumption.core.common.JavaWSDLParameter;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.internal.consumption.common.JavaResourceFilter;
import org.eclipse.jst.ws.internal.consumption.datamodel.validate.ValidationManager;
import org.eclipse.jst.ws.internal.data.TypeRuntimeServer;
import org.eclipse.wst.command.internal.provisional.env.core.SimpleCommand;
import org.eclipse.wst.command.internal.provisional.env.core.common.Environment;
import org.eclipse.wst.command.internal.provisional.env.core.common.MessageUtils;
import org.eclipse.wst.command.internal.provisional.env.core.common.SimpleStatus;
import org.eclipse.wst.command.internal.provisional.env.core.common.Status;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.ws.internal.parser.discovery.WebServicesParserExt;
import org.eclipse.wst.ws.internal.parser.wsil.WebServicesParser;

/**
 * 
 */
public class BUAxisDefaultingCommand extends SimpleCommand
{
  //Need
  private IStructuredSelection initialSelection_;
  private TypeRuntimeServer serviceIds_;
  
  //Provide
  private IServer serviceExistingServer_;
  private String serviceServerTypeID_;  // rsk revisit
  private JavaWSDLParameter javaWSDLParam_;
  private String javaBeanName_;
  private WebServicesParser parser_;
  private boolean forceBuild_ = true;
  private Boolean IsWebProjectStartupRequested = Boolean.TRUE; // rsk revisit
  private ValidationManager validationManager_;
  private boolean customizeServiceMappings_ = false;
  
  //This is proxy and client related information
  //which can be nulled for commands dealing
  //with service creation.
  //rsk revisit
  private Boolean isProxyProject_ = Boolean.FALSE;
  private String clientRuntimeID_ = null;
  private IProject proxyProject_ = null;
  private IProject sampleProject_ = null;
  private String sampleServerTypeID_ = null;
  private IServer sampleExistingServer_ = null;
  
  private MessageUtils msgUtils_;
  
  public Status execute(Environment env)
  {
    
  	String       pluginId = "org.eclipse.jst.ws.axis.creation.ui";
    msgUtils_ = new MessageUtils( pluginId + ".plugin", this );
    
//    if (serviceIds_.getServerInstanceId() != null) { // server exists
//    	serviceExistingServer_ = ServerCore.findServer(serviceIds_.getServerInstanceId());
//    }
//    if (serviceExistingServer_ != null)
//    {
//      serviceServerTypeID_ = serviceExistingServer_.getServerType().getId();
//    }
//    else
//    {
//    	serviceServerTypeID_ = serviceIds_.getServerId();
//      // server will be created in ServerDeployableConfigurationCommand
//    }
    
    //javaWSDLParam
    javaWSDLParam_ = new JavaWSDLParameter();
     
//    if (initialSelection_ == null) {
//    	Status status = new SimpleStatus("BUAxisDefaultingCommand", //$NON-NLS-1$
//				msgUtils_.getMessage("MSG_ERROR_INITIAL_SELECTION"), Status.ERROR);
//		env.getStatusHandler().reportError(status);
//		return status;
//    }
    
	if (javaBeanName_ == null) {
		Status status = new SimpleStatus("BUAxisDefaultingCommand", //$NON-NLS-1$
				msgUtils_.getMessage("MSG_ERROR_CANNOT_NO_JAVA_BEAN"), //$NON-NLS-1$
				Status.ERROR);
		env.getStatusHandler().reportError(status);
		return status;
	}
//    try {
//		
//		javaBeanName_ = getJavaBeanFromInitialSelection(env);
//		
//	} catch (CoreException e) {
//		Status status = new SimpleStatus("BUAxisDefaultingCommand", //$NON-NLS-1$
//				msgUtils_.getMessage("MSG_ERROR_INITIAL_SELECTION") + " " //$NON-NLS-1$
//				+e.getCause().toString(), Status.ERROR);
//		env.getStatusHandler().reportError(status);
//		return status;
//	}
    
    //parser
    parser_ = new WebServicesParserExt();
    
    
    
    //validationManager
    validationManager_ = new ValidationManager();
    
    return new SimpleStatus("");
  }
  
  private String getJavaBeanFromInitialSelection(Environment env) throws CoreException
  {
    String beanClass = "";
    JavaResourceFilter filter = new JavaResourceFilter(); 
    IStructuredSelection selection = initialSelection_;
    //
    if (selection != null)
    {
      Object obj = selection.getFirstElement();
      if (obj != null)
      {
        // get the IResource represented by the selection
        IResource res = null;
        
        res = ResourceUtils.getResourceFromSelection(obj);
        
        if (filter.accepts(res))
        {
          // get the package-qualified class name without file extensions
          String beanPackage = ResourceUtils.getJavaResourcePackageName(res.getFullPath());
          if (beanPackage==null)
            beanPackage = "";
          else
            beanPackage = beanPackage + ".";

          beanClass = beanPackage + res.getName();

          if (beanClass.toLowerCase().endsWith(".java") || beanClass.toLowerCase().endsWith(".class")) {
            beanClass = beanClass.substring(0,beanClass.lastIndexOf('.'));
          }
        }
      }
    }
    
    //
    return beanClass;
  }
  /**
   * @return Returns the clientRuntimeID_.
   */
  public String getClientRuntimeID()
  {
    return clientRuntimeID_;
  }
  /**
   * @return Returns the forceBuild_.
   */
  public boolean getForceBuild()
  {
    return forceBuild_;
  }
  /**
   * @return Returns the isProxyProject_.
   */
  public Boolean getIsProxyProject()
  {
    return isProxyProject_;
  }
  /**
   * @return Returns the javaBeanName_.
   */
  public String getJavaBeanName()
  {
    return javaBeanName_;
  }
  /**
   * @return Returns the javaWSDLParam_.
   */
  public JavaWSDLParameter getJavaWSDLParam()
  {
    return javaWSDLParam_;
  }
  /**
   * @return Returns the parser_.
   */
  public WebServicesParser getParser()
  {
    return parser_;
  }
  /**
   * @return Returns the proxyProject_.
   */
  public IProject getProxyProject()
  {
    return proxyProject_;
  }
  /**
   * @return Returns the sampleExistingServer_.
   */
  public IServer getSampleExistingServer()
  {
    return sampleExistingServer_;
  }
  /**
   * @return Returns the sampleProject_.
   */
  public IProject getSampleProject()
  {
    return sampleProject_;
  }
  /**
   * @return Returns the sampleServerTypeID_.
   */
  public String getSampleServerTypeID()
  {
    return sampleServerTypeID_;
  }
  /**
   * @return Returns the validationManager_.
   */
  public ValidationManager getValidationManager()
  {
    return validationManager_;
  }
  
  public void setInitialSelection(IStructuredSelection initialSelection)
  {
    initialSelection_ = initialSelection;
  }
  /**
   * @return Returns the serviceExistingServer_.
   */
  public IServer getServiceExistingServer()
  {
    return serviceExistingServer_;
  }
  
//  public String getServiceServerTypeID()
//  {
//    return serviceServerTypeID_;
//  }
  
  public boolean getCustomizeServiceMappings()
  {
    return customizeServiceMappings_;
  }
  /**
   * @return Returns the isWebProjectStartupRequested.
   */
  public Boolean getIsWebProjectStartupRequested()
  {
    return IsWebProjectStartupRequested;
  }

public void setJavaBeanName(String javaBeanName) {
	this.javaBeanName_ = javaBeanName;
}
  
//  public void setServiceTypeRuntimeServer(TypeRuntimeServer ids)
//  {
//    serviceIds_ = ids;
//  }
}
