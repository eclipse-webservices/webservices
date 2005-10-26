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

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jem.util.emf.workbench.ProjectUtilities;
import org.eclipse.jst.ws.internal.consumption.ui.common.ValidationUtils;
import org.eclipse.wst.command.internal.env.core.common.MessageUtils;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.command.internal.env.core.selection.SelectionListChoices;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.common.environment.StatusException;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.ws.internal.parser.wsil.WebServicesParser;


/**
 * This command checks to see if the selected client project is the
 * service project. If so, the user is warned.
 */
public class CheckForServiceProjectCommand extends AbstractDataModelOperation
{
  MessageUtils msgUtils;
  //SelectionListChoices runtime2ClientTypes;
  String clientProjectName;
  String wsdlURI;
  WebServicesParser webServicesParser;
    
  public CheckForServiceProjectCommand()
  {
    String pluginId = "org.eclipse.jst.ws.consumption.ui";
    msgUtils = new MessageUtils(pluginId + ".plugin", this);  
  }
  

  public void setClientProjectName(String name)
  {
    clientProjectName = name;
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
   * @see org.eclipse.wst.command.env.core.Command#execute(org.eclipse.wst.command.internal.env.core.common.Environment)
   */
  public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable )
  {
    IEnvironment environment = getEnvironment();
    IStatus status = Status.OK_STATUS;
    if (clientProjectName==null || wsdlURI==null || wsdlURI.length()==0 || webServicesParser==null)
      return status;
    
    if (clientProjectName==null || clientProjectName.length()==0)
      return status;
    
    IProject clientProject = ProjectUtilities.getProject(clientProjectName);
    ValidationUtils vu = new ValidationUtils();
    boolean isServiceProject = vu.isProjectServiceProject(clientProject, wsdlURI, webServicesParser);
    if (isServiceProject)
    {
      IStatus wStatus = StatusUtils.warningStatus( msgUtils.getMessage("MSG_WARN_IS_SERVICE_PROJECT", new String[]{clientProjectName}) );
      try
      {
        environment.getStatusHandler().report(wStatus);
      }
      catch (StatusException se)
      {
        //User decided to abort. Return an error status
        IStatus eStatus = StatusUtils.errorStatus( msgUtils.getMessage("MSG_USER_ABORTED") );
        return eStatus;
      }
    }
    
    return status;
  }
}
