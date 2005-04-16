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

import org.eclipse.jst.ws.internal.consumption.ui.wsil.DialogWWWAuthentication;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.command.env.core.SimpleCommand;
import org.eclipse.wst.command.env.core.common.Condition;
import org.eclipse.wst.command.env.core.common.Environment;
import org.eclipse.wst.command.env.core.common.MessageUtils;
import org.eclipse.wst.command.env.core.common.SimpleStatus;
import org.eclipse.wst.command.env.core.common.Status;
import org.eclipse.wst.ws.internal.parser.discovery.WebServicesParserExt;
import org.eclipse.wst.ws.internal.parser.wsil.WWWAuthenticationException;
import org.eclipse.wst.ws.internal.parser.wsil.WebServiceEntity;
import org.eclipse.wst.ws.internal.parser.wsil.WebServicesParser;


public class WSDLSelectionConditionCommand extends SimpleCommand implements Condition
{
  private String pluginId_;
  private MessageUtils msgUtils_;
  private WebServicesParser webServicesParser;
  private String webServiceURI;
  private String httpBasicAuthUsername;
  private String httpBasicAuthPassword;
  private boolean needWSDLSelectionTreeWidget;
  
  public WSDLSelectionConditionCommand()
  {
    pluginId_ = "org.eclipse.jst.ws.consumption.ui";
    msgUtils_ = new MessageUtils( pluginId_ + ".plugin", this );
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
  
  public Status execute(Environment env)
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
          return new SimpleStatus("", msgUtils_.getMessage("MSG_ERROR_URI_NOT_RESOLVABLE", new Object[] {webServiceURI}), Status.ERROR, t);
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
      return new SimpleStatus("", msgUtils_.getMessage("MSG_ERROR_URI_NOT_RESOLVABLE", new Object[] {webServiceURI}), Status.ERROR, t);
    }
    WebServiceEntity wsEntity = parser.getWebServiceEntityByURI(webServiceURI);
    if (wsEntity != null)
    {
      int type = wsEntity.getType();
      if (type != WebServiceEntity.TYPE_WSDL)
      	needWSDLSelectionTreeWidget = true;
    }
    return new SimpleStatus("");
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
