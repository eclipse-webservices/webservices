/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.ui.widgets;

import java.util.Map;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.wst.command.internal.provisional.env.core.EnvironmentalOperation;
import org.eclipse.wst.command.internal.provisional.env.core.common.Environment;
import org.eclipse.wst.command.internal.provisional.env.core.common.MessageUtils;
import org.eclipse.wst.command.internal.provisional.env.core.common.SimpleStatus;
import org.eclipse.wst.command.internal.provisional.env.core.common.Status;
import org.eclipse.wst.ws.internal.parser.discovery.WebServicesParserExt;
import org.eclipse.wst.ws.internal.parser.wsil.WebServicesParser;


public class WSDLSelectionOutputCommand extends EnvironmentalOperation
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
    Environment env = getEnvironment();
    
    MessageUtils msgUtils = new MessageUtils("org.eclipse.jst.ws.consumption.ui.plugin", this);    
    if (wsdlURI != null && getWebServicesParser().getWSDLDefinition(wsdlURI) != null) {
      Status status = new SimpleStatus("");     
      Map services = getWebServicesParser().getWSDLDefinition(wsdlURI).getServices();
      if (services.isEmpty()){
        if (testService==true){
            testService = false;
            status = new SimpleStatus("", msgUtils.getMessage("MSG_WARNING_NO_SERVICE_ELEMENT"), Status.WARNING);
            try{
              env.getStatusHandler().report(status);
            }catch(Exception e){
              status = new SimpleStatus("", msgUtils.getMessage("MSG_WARNING_NO_SERVICE_ELEMENT"), Status.ERROR);
            }
        }
      }    
    return status;
  }
    else
    {
      Status status = new SimpleStatus("", msgUtils.getMessage("PAGE_MSG_SELECTION_MUST_BE_WSDL"), Status.ERROR);
      env.getStatusHandler().reportError(status);
      return status;
    }
  }
}