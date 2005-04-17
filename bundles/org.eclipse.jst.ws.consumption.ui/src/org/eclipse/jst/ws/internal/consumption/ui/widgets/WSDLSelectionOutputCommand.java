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

import org.eclipse.core.resources.IProject;
import org.eclipse.wst.command.internal.provisional.env.core.SimpleCommand;
import org.eclipse.wst.command.internal.provisional.env.core.common.Environment;
import org.eclipse.wst.command.internal.provisional.env.core.common.MessageUtils;
import org.eclipse.wst.command.internal.provisional.env.core.common.SimpleStatus;
import org.eclipse.wst.command.internal.provisional.env.core.common.Status;
import org.eclipse.wst.ws.internal.parser.discovery.WebServicesParserExt;
import org.eclipse.wst.ws.internal.parser.wsil.WebServicesParser;


public class WSDLSelectionOutputCommand extends SimpleCommand
{
  private WebServicesParser webServicesParser;
  private String wsdlURI;
  private IProject project;
  
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

  public Status execute(Environment env)
  {
    if (wsdlURI != null && getWebServicesParser().getWSDLDefinition(wsdlURI) != null)
      return new SimpleStatus("");
    else
    {
      MessageUtils msgUtils = new MessageUtils("org.eclipse.jst.ws.consumption.ui.plugin", this);
      Status status = new SimpleStatus("", msgUtils.getMessage("PAGE_MSG_SELECTION_MUST_BE_WSDL"), Status.ERROR);
      env.getStatusHandler().reportError(status);
      return status;
    }
  }
}