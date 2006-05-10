/*******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060504   119296 pmoogk@ca.ibm.com - Peter Moogk
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.ui.widgets;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jst.ws.internal.consumption.ui.ConsumptionUIMessages;
import org.eclipse.jst.ws.internal.consumption.ui.plugin.WebServiceConsumptionUIPlugin;
import org.eclipse.wst.command.internal.env.core.common.Condition;
import org.eclipse.wst.command.internal.env.ui.common.TimedOperation;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.ws.internal.parser.wsil.WebServicesParser;


public class TimedWSDLSelectionConditionCommand extends AbstractDataModelOperation implements Condition
{
  private final String                  TIMEOUT_PREFERENCE = "wsdlTimeOut";
  private WSDLSelectionConditionCommand selectionCommand;
  private int                           timeOutValue;
  
  public TimedWSDLSelectionConditionCommand()
  {
    selectionCommand = new WSDLSelectionConditionCommand();
    
    IPreferenceStore prefStore = WebServiceConsumptionUIPlugin.getInstance().getPreferenceStore();
    
    timeOutValue = prefStore.getDefaultString(TIMEOUT_PREFERENCE).equals("") ? 10000 : prefStore.getInt( TIMEOUT_PREFERENCE );
  }

  public void setWebServicesParser(WebServicesParser webServicesParser)
  {
    selectionCommand.setWebServicesParser( webServicesParser );
  }

  public WebServicesParser getWebServicesParser()
  {
    return selectionCommand.getWebServicesParser();
  }
  
  public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable )
  {
    String         timeOutMessage = ConsumptionUIMessages.bind( ConsumptionUIMessages.MSG_INFO_WSDL_OPERATION_TIMED_OUT, getWebServiceURI() );
    
    TimedOperation timedOperation = new TimedOperation( selectionCommand, timeOutValue, timeOutMessage );
    
    return timedOperation.execute(monitor, adaptable);
  }
  
  public boolean evaluate()
  {
    return selectionCommand.evaluate();
  }

  public String getWebServiceURI()
  {
    return selectionCommand.getWebServiceURI();
  }

  public void setWebServiceURI(String webServiceURI)
  {
    selectionCommand.setWebServiceURI(webServiceURI);
  }
  
  public String getWsdlURI()
  {
    return getWebServiceURI();
  }
/**
 * @return Returns the httpBasicAuthPassword.
 */
public String getHttpBasicAuthPassword() {
	return selectionCommand.getHttpBasicAuthPassword();
}
/**
 * @return Returns the httpBasicAuthUsername.
 */
public String getHttpBasicAuthUsername() {
	return selectionCommand.getHttpBasicAuthUsername();
}
}
