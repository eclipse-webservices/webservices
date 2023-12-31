/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
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
package org.eclipse.jst.ws.internal.consumption.ui.widgets;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.internal.consumption.ui.ConsumptionUIMessages;
import org.eclipse.jst.ws.internal.consumption.ui.wsil.DialogWWWAuthentication;
import org.eclipse.osgi.util.NLS;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.command.internal.env.core.common.Condition;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.ws.internal.parser.discovery.WebServicesParserExt;
import org.eclipse.wst.ws.internal.parser.wsil.WWWAuthenticationException;
import org.eclipse.wst.ws.internal.parser.wsil.WebServiceEntity;
import org.eclipse.wst.ws.internal.parser.wsil.WebServicesParser;


public class WSDLSelectionConditionCommand extends AbstractDataModelOperation implements Condition
{
  private WebServicesParser webServicesParser;
  private String webServiceURI;
  private String httpBasicAuthUsername;
  private String httpBasicAuthPassword;
  private boolean needWSDLSelectionTreeWidget;
  
  public WSDLSelectionConditionCommand()
  {
  }

  public void setWebServicesParser(WebServicesParser webServicesParser)
  {
    this.webServicesParser = webServicesParser;
  }

  public WebServicesParser getWebServicesParser()
  {
  	if (webServicesParser == null)
  	  webServicesParser = new WebServicesParserExt();
  	return webServicesParser;
  }
  
  public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable )
  {
  	needWSDLSelectionTreeWidget = false;
  	WebServicesParser parser = getWebServicesParser();
  	parser.setURI(webServiceURI);
    try
    {
      parser.parse(WebServicesParser.PARSE_WSIL | WebServicesParser.PARSE_DISCO | WebServicesParser.PARSE_LINKS);
    }
    catch (WWWAuthenticationException wwwae)
    {
      DialogWWWAuthentication dialog = new DialogWWWAuthentication(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
      dialog.handleWWWAuthentication(wwwae);
      String username = dialog.getUsername();
      String password = dialog.getPassword();
      httpBasicAuthUsername = username;
      httpBasicAuthPassword = password;
      if (username != null && password != null)
      {
        parser.setHTTPBasicAuthUsername(username);
        parser.setHTTPBasicAuthPassword(password);
        try
        {
          parser.parse(WebServicesParser.PARSE_WSIL | WebServicesParser.PARSE_DISCO | WebServicesParser.PARSE_LINKS);
        }
        catch (Throwable t)
        {
          return StatusUtils.errorStatus( NLS.bind(ConsumptionUIMessages.MSG_ERROR_URI_NOT_RESOLVABLE, new Object[] {webServiceURI}), t);
        }
        finally
        {
          parser.setHTTPBasicAuthUsername(null);
          parser.setHTTPBasicAuthPassword(null);
        }
      }
    }
    catch (Throwable t)
    {
      return StatusUtils.errorStatus( NLS.bind(ConsumptionUIMessages.MSG_ERROR_URI_NOT_RESOLVABLE, new Object[] {webServiceURI}), t);
    }
    WebServiceEntity wsEntity = parser.getWebServiceEntityByURI(webServiceURI);
    if (wsEntity != null)
    {
      int type = wsEntity.getType();
      if (type != WebServiceEntity.TYPE_WSDL)
      	needWSDLSelectionTreeWidget = true;
    }
    return Status.OK_STATUS;
  }
  
  public boolean evaluate()
  {
    return needWSDLSelectionTreeWidget;
  }

  public String getWebServiceURI()
  {
    return webServiceURI;
  }

  public void setWebServiceURI(String webServiceURI)
  {
    this.webServiceURI = webServiceURI;
  }
  
  public String getWsdlURI()
  {
    return getWebServiceURI();
  }
/**
 * @return Returns the httpBasicAuthPassword.
 */
public String getHttpBasicAuthPassword() {
	return httpBasicAuthPassword;
}
/**
 * @return Returns the httpBasicAuthUsername.
 */
public String getHttpBasicAuthUsername() {
	return httpBasicAuthUsername;
}
}
