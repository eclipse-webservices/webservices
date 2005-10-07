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

package org.eclipse.wst.ws.internal.common;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.wsdl.Definition;
import javax.wsdl.Port;
import javax.wsdl.Service;
import javax.wsdl.WSDLException;
import javax.wsdl.extensions.ExtensibilityElement;
import javax.wsdl.extensions.http.HTTPAddress;
import javax.wsdl.extensions.soap.SOAPAddress;
import org.eclipse.wst.ws.internal.parser.discovery.WebServicesParserExt;
import org.eclipse.wst.ws.internal.parser.wsil.WWWAuthenticationException;

public class LiveWSDLFilter extends Thread
{
  private String wsdlURL_;
  private boolean finish_;
  private boolean live_;

  public LiveWSDLFilter(String wsdlURL)
  {
    wsdlURL_ = wsdlURL;
    finish_ = false;
    live_ = false;
  }

  public String getWSDLURL()
  {
    return wsdlURL_;
  }

  public boolean isFinish()
  {
    return finish_;
  }

  public boolean isWSDLLive()
  {
    return live_;
  }

  public void run()
  {
    try
    {
      live_ = validateWSDL();
    }
    catch (Throwable t)
    {
    	System.out.println("Throwing exception " + t.getMessage());
      live_ = false;
    }
    finally
    {
      finish_ = true;
    }
  }

  private boolean validateWSDL() throws WSDLException, MalformedURLException, IOException, WWWAuthenticationException
  {
  	WebServicesParserExt parser = new WebServicesParserExt();
  	Definition definition = parser.getWSDLDefinitionVerbose(wsdlURL_);
  	System.out.println("wsdlURL_ " + wsdlURL_.toString());
    Map services = definition.getServices();
    Iterator serviceIterator = services.values().iterator();
    System.out.println("serviceIterator " + serviceIterator.toString());
    while (serviceIterator.hasNext())
    {
      Service service = (Service)serviceIterator.next();
      Map ports = service.getPorts();
      Iterator portIterator = ports.values().iterator();
      while (portIterator.hasNext())
      {
        Port port = (Port)portIterator.next();
        List extensibilityElements = port.getExtensibilityElements();
        Iterator extensibilityElementsIterator = extensibilityElements.iterator();
        while (extensibilityElementsIterator.hasNext())
        {
          ExtensibilityElement extensibilityElement = (ExtensibilityElement)extensibilityElementsIterator.next();
          if ((extensibilityElement instanceof SOAPAddress) || (extensibilityElement instanceof HTTPAddress))
            return true;
        }
      }
    }
    return false;
  }
}
