/*******************************************************************************
 * Copyright (c) 2002, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jst.ws.internal.axis.consumption.ui.task;

import javax.wsdl.Definition;

import org.eclipse.jst.ws.internal.consumption.ui.plugin.WebServiceConsumptionUIPlugin;
import org.eclipse.wst.command.env.core.SimpleCommand;
import org.eclipse.wst.command.env.core.common.Environment;
import org.eclipse.wst.command.env.core.common.MessageUtils;
import org.eclipse.wst.command.env.core.common.SimpleStatus;
import org.eclipse.wst.command.env.core.common.Status;
import org.eclipse.wst.ws.parser.wsil.WebServicesParser;



public class ValidateWSDLCommand extends SimpleCommand
{
  private WebServicesParser webServicesParser;
  private String wsdlURI;

  public ValidateWSDLCommand()
  {
    super("org.eclipse.jst.ws.internal.axis.consumption.ui.task.ValidateWSDLCommand", "org.eclipse.jst.ws.internal.axis.consumption.ui.task.ValidateWSDLCommand");
  }

  public Status execute(Environment env)
  {
	MessageUtils msgUtils_ = new MessageUtils( "org.eclipse.jst.ws.axis.consumption.ui.plugin", this );
	
    if (wsdlURI != null && wsdlURI.length() > 0)
    {
      Definition definition = webServicesParser.getWSDLDefinition(wsdlURI);
      if (definition != null)
      {
        int numServices = definition.getServices().size();
        if (numServices < 1)
        {
          Status status = new SimpleStatus(WebServiceConsumptionUIPlugin.ID, msgUtils_.getMessage("MSG_ERROR_WSDL_HAS_NO_SERVICE_ELEMENT", new Object[] {wsdlURI}), Status.ERROR, null);
          env.getStatusHandler().reportError(status);
          return status;
        }
      }
    }
    return new SimpleStatus("");
  }

  /**
   * @param wsdlURI The wsdlURI to set.
   */
  public void setWsdlURI(String wsdlURI)
  {
    this.wsdlURI = wsdlURI;
  }

  /**
   * @param webServicesParser The webServicesParser to set.
   */
  public void setWebServicesParser(WebServicesParser webServicesParser)
  {
    this.webServicesParser = webServicesParser;
  }

}
