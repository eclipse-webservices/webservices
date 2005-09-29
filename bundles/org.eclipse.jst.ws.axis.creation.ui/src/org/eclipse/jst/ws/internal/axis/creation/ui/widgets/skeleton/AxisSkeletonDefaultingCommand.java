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
package org.eclipse.jst.ws.internal.axis.creation.ui.widgets.skeleton;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jst.ws.internal.axis.consumption.core.common.JavaWSDLParameter;
import org.eclipse.jst.ws.internal.consumption.common.WSDLParserFactory;
import org.eclipse.wst.command.internal.provisional.env.core.EnvironmentalOperation;
import org.eclipse.wst.ws.internal.parser.wsil.WebServicesParser;


public class AxisSkeletonDefaultingCommand extends EnvironmentalOperation
{
  private IStructuredSelection initialSelection;
  private WebServicesParser webServicesParser;
  private String wsdlURI_;
  private JavaWSDLParameter javaWSDLParam;
  
	public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable ) 
	{
    javaWSDLParam = new JavaWSDLParameter();
    javaWSDLParam.setServerSide(JavaWSDLParameter.SERVER_SIDE_BEAN);
    javaWSDLParam.setSkeletonDeploy(true);
    javaWSDLParam.setMetaInfOnly(false);
    return Status.OK_STATUS;
    
  }
  
  public void setInitialSelection(IStructuredSelection initialSelection)
  {
    this.initialSelection = initialSelection;
  }

  public void setObjectSelection(IStructuredSelection objectSelection)
  {
  }
  
  public void setWebServicesParser(WebServicesParser webServicesParser)
  {
    this.webServicesParser = webServicesParser;
  }
  
  public WebServicesParser getWebServicesParser()
  {
    if (webServicesParser == null)
		webServicesParser = WSDLParserFactory.getWSDLParser();
    return webServicesParser;
  }

  public String getWebServiceURI()
  {
    if (initialSelection != null && !initialSelection.isEmpty())
    {
      Object firstElement = initialSelection.getFirstElement();
      if (firstElement instanceof IFile)
      {
        IFile ifile = (IFile)firstElement;
        String fileExtension = ifile.getFileExtension();
        if (fileExtension.equals("wsdl") ||
            fileExtension.equals("wsil") ||
            fileExtension.equals("html"))
        {
          return ifile.getFullPath().toString();  
        }
      }
    }
    return "";
  }
  
  public String getWsdlURI()
  {
	  return wsdlURI_;
  }
  
  public void setWsdlURI(String wsdlURI)
  {
	  wsdlURI_ = wsdlURI;
	  
  }
  
  public boolean getGenWSIL()
  {
    return false;
  }
  
  public String getWsilURI()
  {
    String wsURI = getWsdlURI();
    int index = wsURI.lastIndexOf('.');
    if (index != -1)
    {
      StringBuffer sb = new StringBuffer(wsURI.substring(0, index));
      sb.append(".wsil");
      return sb.toString();
    }
    return "";
  }
  
  public JavaWSDLParameter getJavaWSDLParam()
  {
    return javaWSDLParam;
  }

  public String getHttpBasicAuthPassword()
  {
    return getWebServicesParser().getHTTPBasicAuthPassword();
  }

  public String getHttpBasicAuthUsername()
  {
    return getWebServicesParser().getHTTPBasicAuthUsername();
  }
  
}