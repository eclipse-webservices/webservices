/*******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060413   135581 rsinha@ca.ibm.com - Rupam Kuehner
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.ui.widgets;

import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.internal.consumption.ui.ConsumptionUIMessages;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.ws.internal.parser.discovery.WebServicesParserExt;
import org.eclipse.wst.ws.internal.parser.wsil.WebServicesParser;


public class WSDLSelectionOutputCommand extends AbstractDataModelOperation
{
  private WebServicesParser webServicesParser;
  private String wsdlURI;
  private IProject project;
  private String componentName;
  
  private boolean testService;
  
  
  public boolean getTestService() {
    return testService;
  }

  
  public void setTestService(boolean testService) {
    this.testService = testService;
  }

  /**
   * @return Returns the project.
   */
  public IProject getProject()
  {
    return project;
  }

  /**
   * @param project
   *          The project to set.
   */
  public void setProject(IProject project)
  {
    this.project = project;
  }
  
  
  
  public String getComponentName()
  {
    return componentName;
  }

  public void setComponentName(String componentName)
  {
    this.componentName = componentName;
  }

  public String getWsdlURI()
  {
    return wsdlURI;
  }

  public void setWsdlURI(String wsdlURI)
  {
    this.wsdlURI = wsdlURI;
  }

  /**
   * @return Returns the webServicesParser.
   */
  public WebServicesParser getWebServicesParser()
  {
    if (webServicesParser != null)
      return webServicesParser;
    else
      return new WebServicesParserExt();
  }

  /**
   * @param webServicesParser to set.
   */
  public void setWebServicesParser(WebServicesParser webServicesParser)
  {
    this.webServicesParser = webServicesParser;
  }

  public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable )
  {
    IEnvironment env = getEnvironment();
       
    if (wsdlURI != null && getWebServicesParser().getWSDLDefinition(wsdlURI) != null) {
      IStatus status = Status.OK_STATUS;     
      Map services = getWebServicesParser().getWSDLDefinition(wsdlURI).getServices();
      if (services.isEmpty()){
        if (testService==true){
            testService = false;
            status = StatusUtils.warningStatus( ConsumptionUIMessages.MSG_WARNING_NO_SERVICE_ELEMENT );
            try{
              env.getStatusHandler().report(status);
            }catch(Exception e){
              status = StatusUtils.errorStatus( ConsumptionUIMessages.MSG_WARNING_NO_SERVICE_ELEMENT );
            }
        }
      }    
    return status;
  }
    else
    {
      IStatus status = StatusUtils.errorStatus( ConsumptionUIMessages.PAGE_MSG_SELECTION_MUST_BE_WSDL );
      if (env != null)
      {
        env.getStatusHandler().reportError(status);
      }
      return status;
    }
  }
}
