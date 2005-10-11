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
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.internal.axis.consumption.core.common.JavaWSDLParameter;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.ws.internal.parser.wsil.WebServicesParser;


public class ComputeAxisSkeletonBeanCommand extends AbstractDataModelOperation
{
  private List classNames;
  //private String wsdlURI;
  //private WebServicesParser webServicesParser;
  private JavaWSDLParameter javaWSDLParameter;

  public ComputeAxisSkeletonBeanCommand()
  {
  }

	public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable ) 
	{
    classNames = new Vector();
    if (javaWSDLParameter != null)
    {
      String beanName = javaWSDLParameter.getBeanName();
      if (beanName != null)
        classNames.add(beanName);
    }
    return Status.OK_STATUS;
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
