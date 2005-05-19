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
package org.eclipse.jst.ws.internal.consumption.ui.command;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.eclipse.core.resources.IProject;
import org.eclipse.jem.util.emf.workbench.ProjectUtilities;
import org.eclipse.jst.ws.internal.consumption.ui.common.ValidationUtils;
import org.eclipse.wst.command.internal.provisional.env.core.SimpleCommand;
import org.eclipse.wst.command.internal.provisional.env.core.common.Environment;
import org.eclipse.wst.command.internal.provisional.env.core.common.MessageUtils;
import org.eclipse.wst.command.internal.provisional.env.core.common.SimpleStatus;
import org.eclipse.wst.command.internal.provisional.env.core.common.Status;
import org.eclipse.wst.command.internal.provisional.env.core.common.StatusException;
import org.eclipse.wst.command.internal.provisional.env.core.selection.SelectionListChoices;
import org.eclipse.wst.ws.internal.parser.wsil.WebServicesParser;


/**
 * This command checks to see if the selected client project is the
 * service project. If so, the user is warned.
 */
public class CheckForServiceProjectCommand extends SimpleCommand
{
  MessageUtils msgUtils;
  SelectionListChoices runtime2ClientTypes;
  String wsdlURI;
  WebServicesParser webServicesParser;
    
  public CheckForServiceProjectCommand()
  {
    String pluginId = "org.eclipse.jst.ws.consumption.ui";
    msgUtils = new MessageUtils(pluginId + ".plugin", this);  
  }
  
  public void setRuntime2ClientTypes( SelectionListChoices runtime2ClientTypes )
  {
    this.runtime2ClientTypes = runtime2ClientTypes;
  }
  
  public void setWsdlURI(String wsdlURI)
  {
    this.wsdlURI = wsdlURI;
  }

  public void setWebServicesParser(WebServicesParser webServicesParser)
  {
    this.webServicesParser = webServicesParser;
  }
  
  
  /* (non-Javadoc)
   * @see org.eclipse.wst.command.env.core.Command#execute(org.eclipse.wst.command.internal.provisional.env.core.common.Environment)
   */
  public Status execute(Environment environment)
  {
    Status status = new SimpleStatus("");
    if (runtime2ClientTypes==null || wsdlURI==null || wsdlURI.length()==0 || webServicesParser==null)
      return status;
    
    String clientProjectName = runtime2ClientTypes.getChoice().getChoice().getList().getSelection();
    if (clientProjectName==null || clientProjectName.length()==0)
      return status;
    
    IProject clientProject = ProjectUtilities.getProject(clientProjectName);
    ValidationUtils vu = new ValidationUtils();
    Calendar cal = new GregorianCalendar();
    boolean isServiceProject = vu.isProjectServiceProject(clientProject, wsdlURI, webServicesParser);
    if (isServiceProject)
    {
      Status wStatus = new SimpleStatus("", msgUtils.getMessage("MSG_WARN_IS_SERVICE_PROJECT", new String[]{clientProjectName}), Status.WARNING);
      try
      {
        environment.getStatusHandler().report(wStatus);
      }
      catch (StatusException se)
      {
        //User decided to abort. Return an error status
        Status eStatus = new SimpleStatus("", msgUtils.getMessage("MSG_USER_ABORTED"), Status.ERROR);
        return eStatus;
      }
    }
    
    return status;
  }
}
