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
package org.eclipse.jst.ws.internal.axis.creation.ui.command;

import java.util.List;
import java.util.Vector;

import org.eclipse.jst.ws.internal.axis.consumption.core.common.JavaWSDLParameter;
import org.eclipse.wst.command.env.core.SimpleCommand;
import org.eclipse.wst.command.env.core.common.Environment;
import org.eclipse.wst.command.env.core.common.SimpleStatus;
import org.eclipse.wst.command.env.core.common.Status;
import org.eclipse.wst.ws.parser.wsil.WebServicesParser;


public class ComputeAxisSkeletonBeanCommand extends SimpleCommand
{
  private List classNames;
  //private String wsdlURI;
  //private WebServicesParser webServicesParser;
  private JavaWSDLParameter javaWSDLParameter;

  public ComputeAxisSkeletonBeanCommand()
  {
  }

  public Status execute(Environment environment)
  {
    classNames = new Vector();
    if (javaWSDLParameter != null)
    {
      String beanName = javaWSDLParameter.getBeanName();
      if (beanName != null)
        classNames.add(beanName);
    }
    return new SimpleStatus("");
  }

  public List getClassNames()
  {
    return classNames;
  }

  /**
   * @param webServicesParser The webServicesParser to set.
   */
  public void setWebServicesParser(WebServicesParser webServicesParser)
  {
    //this.webServicesParser = webServicesParser;
  }

  /**
   * @param wsdlURI The wsdlURI to set.
   */
  public void setWsdlURI(String wsdlURI)
  {
    //this.wsdlURI = wsdlURI;
  }
  
  public void setJavaWSDLParam(JavaWSDLParameter javaWSDLParameter)
  {
    this.javaWSDLParameter = javaWSDLParameter;
  }
}
