/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.axis.consumption.ui.task;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.internal.axis.consumption.core.common.JavaWSDLParameter;
import org.eclipse.jst.ws.internal.consumption.ui.wsil.DialogWWWAuthentication;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.ws.internal.parser.discovery.WebServicesParserExt;
import org.eclipse.wst.ws.internal.parser.wsil.WWWAuthenticationException;
import org.eclipse.wst.ws.internal.parser.wsil.WebServiceEntity;
import org.eclipse.wst.ws.internal.parser.wsil.WebServicesParser;



public class DefaultsForHTTPBasicAuthCommand extends AbstractDataModelOperation
{
  private WebServicesParser webServicesParser;
  private String wsdlServiceURL;
  private JavaWSDLParameter javaWSDLParam;

  public DefaultsForHTTPBasicAuthCommand()
  {
    super();
  }
  
	public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable ) 
	{   
    if (wsdlServiceURL != null && wsdlServiceURL.length() > 0)
    {
      if (webServicesParser == null)
      {
        webServicesParser = new WebServicesParserExt();
        //wse.setWSParser(webServicesParser);
      }
      WebServiceEntity wsEntity = webServicesParser.getWebServiceEntityByURI(wsdlServiceURL);
      if (wsEntity == null || !wsEntity.isEntityResolved())
      {
        webServicesParser.setURI(wsdlServiceURL);
        try
        {
          webServicesParser.parse(WebServicesParser.PARSE_NONE);
        }
        catch (WWWAuthenticationException wwwae)
        {
          DialogWWWAuthentication dialog = new DialogWWWAuthentication(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
          dialog.handleWWWAuthentication(wwwae);
          String username = dialog.getUsername();
          String password = dialog.getPassword();
          if (username != null && password != null)
          {
            webServicesParser.setHTTPBasicAuthUsername(username);
            webServicesParser.setHTTPBasicAuthPassword(password);
            try
            {
              webServicesParser.parse(WebServicesParser.PARSE_NONE);
            }
            catch (Throwable t)
            {
            }
            webServicesParser.setHTTPBasicAuthUsername(null);
            webServicesParser.setHTTPBasicAuthPassword(null);
          }
        }
        catch (Throwable t)
        {
        }
        wsEntity = webServicesParser.getWebServiceEntityByURI(wsdlServiceURL);
      }
      if (wsEntity != null && wsEntity.getType() == WebServiceEntity.TYPE_WSDL)
      {
        String httpUsername = wsEntity.getHTTPUsername();
        String httpPassword = wsEntity.getHTTPPassword();
        if (httpUsername != null && httpPassword != null)
        {
          javaWSDLParam.setHTTPUsername(httpUsername);
          javaWSDLParam.setHTTPPassword(httpPassword);
        }
      }
    }
    return Status.OK_STATUS;
  }
  /**
   * @param javaWSDLParam The javaWSDLParam to set.
   */
  public void setJavaWSDLParam(JavaWSDLParameter javaWSDLParam) {
    this.javaWSDLParam = javaWSDLParam;
  }

  /**
   * @param wsdlServiceURL The wsdlServiceURL to set.
   */
  public void setWsdlServiceURL(String wsdlServiceURL) {
    this.wsdlServiceURL = wsdlServiceURL;
  }

  /**
   * @return Returns the javaWSDLParam.
   */
  public JavaWSDLParameter getJavaWSDLParam() {
    return javaWSDLParam;
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

}
