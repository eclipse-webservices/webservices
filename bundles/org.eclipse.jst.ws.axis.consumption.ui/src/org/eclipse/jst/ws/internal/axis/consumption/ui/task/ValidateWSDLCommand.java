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

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.internal.consumption.ui.ConsumptionUIMessages;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.ws.internal.parser.wsil.WebServicesParser;



public class ValidateWSDLCommand extends AbstractDataModelOperation
{
  private WebServicesParser webServicesParser;
  private String wsdlURI;

  public ValidateWSDLCommand()
  {
  }

	public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable ) 
	{
		IEnvironment environment = getEnvironment();
		if (wsdlURI != null && wsdlURI.length() > 0)
		{
			Definition definition = webServicesParser.getWSDLDefinition(wsdlURI);
			if (definition != null)
			{
				int numServices = definition.getServices().size();
				if (numServices < 1)
				{
					IStatus status = StatusUtils.errorStatus( NLS.bind(ConsumptionUIMessages.MSG_ERROR_WSDL_HAS_NO_SERVICE_ELEMENT, new Object[] {wsdlURI}), null);
					environment.getStatusHandler().reportError(status);
					return status;
				}
			}
		}
		return Status.OK_STATUS;
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
