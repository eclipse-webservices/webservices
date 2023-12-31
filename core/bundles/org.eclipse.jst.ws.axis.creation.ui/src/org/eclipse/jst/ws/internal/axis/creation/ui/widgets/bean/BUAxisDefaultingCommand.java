/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.axis.creation.ui.widgets.bean;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.internal.axis.consumption.core.common.JavaWSDLParameter;
import org.eclipse.jst.ws.internal.axis.creation.ui.AxisCreationUIMessages;
import org.eclipse.jst.ws.internal.consumption.datamodel.validate.ValidationManager;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.ws.internal.parser.discovery.WebServicesParserExt;
import org.eclipse.wst.ws.internal.parser.wsil.WebServicesParser;

/**
 * 
 */
public class BUAxisDefaultingCommand extends AbstractDataModelOperation
{
  
  //Provide
  private IServer serviceExistingServer_;
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
  
 
  public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable ) 
  {
	IEnvironment environment = getEnvironment();
    
	//javaWSDLParam
	javaWSDLParam_ = new JavaWSDLParameter();
    
	if (javaBeanName_ == null) {
		IStatus status = StatusUtils.errorStatus(
				AxisCreationUIMessages.MSG_ERROR_CANNOT_NO_JAVA_BEAN);
		environment.getStatusHandler().reportError(status);
		return status;
	}
    
    //parser
    parser_ = new WebServicesParserExt();
    
    //validationManager
    validationManager_ = new ValidationManager();
    
    return Status.OK_STATUS;
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
  
  /**
   * @return Returns the serviceExistingServer_.
   */
  public IServer getServiceExistingServer()
  {
    return serviceExistingServer_;
  }
  
 
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
