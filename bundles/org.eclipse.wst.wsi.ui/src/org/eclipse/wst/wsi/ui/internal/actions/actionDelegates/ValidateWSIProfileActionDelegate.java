/*******************************************************************************
 * Copyright (c) 2002-2005 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   IBM - Initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsi.ui.internal.actions.actionDelegates;

import java.util.List;
import java.util.Vector;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.wst.internet.monitor.core.Request;
import org.eclipse.wst.wsi.ui.internal.LogBuilder;
import org.eclipse.wst.wsi.ui.internal.wizards.ValidationWizard;
import org.eclipse.wst.wsi.ui.internal.WSIValidator;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.dialogs.ContainerGenerator;
import org.eclipse.wst.wsi.internal.core.log.Log;

/**
 * Action delegate for the WS-I validator.
 * 
 * @author David Lauzon, IBM
 * @author Lawrence Mandel, IBM
 */
public class ValidateWSIProfileActionDelegate implements IViewActionDelegate
{
  /**
   * The default WS-I Message Log filename.
   */
  public static final String DEFAULT_LOG_FILENAME = "log.wsimsg";

  /**
   * The HTTP protocol.
   */
  private final String HTTP = "http://";
  
  
  /**
   * The default tag for WSDL locations.
   */
  private final String WSDL = "?WSDL";
  
  /**
   * The current selection, or null if there is no selection.
   */
  ISelection selection;

  /**
   * The list of messages in the form of request-response pairs.
   */
  Request[] requestResponses;

  /**
   * The view that provides the context for this delegate.
   */
  IViewPart view;

  /**
   * Constructor.
   */
  public ValidateWSIProfileActionDelegate()
  {
  }

  /**
   * @see org.eclipse.ui.IViewActionDelegate#init(org.eclipse.ui.IViewPart)
   */
  public void init(IViewPart view)
  {
    this.view = view;
  }

  /** 
   * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
   */
  public void run(IAction action)
  {
    WorkspaceModifyOperation operation = new WorkspaceModifyOperation()
    {
      protected void execute(IProgressMonitor progressMonitor)
      throws CoreException
      {
        validate(progressMonitor);
      }
    };
    
    try
    {
      operation.run(null);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }

  /**
   * The validate action.
   * 
   * @param progressMonitor
   */
  public void validate(IProgressMonitor progressMonitor)
  {
    try
    {
      ValidationWizard validateWizard = new ValidationWizard(DEFAULT_LOG_FILENAME);

      //TODO requestResponses = MonitorCore.getRequests();
      List wsdllocs = new Vector();
      if(requestResponses != null)
      {
       	for (int i=0; i<requestResponses.length; i++)
      	{
      	  Request reqresp = requestResponses[i];
      	  String remotehost = reqresp.getRemoteHost();
      	  int remoteport = reqresp.getRemotePort();
      	  String remotelabel = reqresp.getName();
      	  String location = HTTP + remotehost + ":" + remoteport + remotelabel + WSDL;
      	  if(!wsdllocs.contains(location))
      	  {
      	    wsdllocs.add(location);
      	  }
      	}
      }
      validateWizard.setWSDLLocations((String[])wsdllocs.toArray(new String[wsdllocs.size()]));
      Shell shell = Display.getCurrent().getActiveShell();
      WizardDialog wizardDialog = new WizardDialog(shell, validateWizard);
      wizardDialog.create();

      int result = wizardDialog.open();

      if (validateWizard.isValid() && (result != org.eclipse.jface.window.Window.CANCEL))
      {
        // If the container doesn't exist, create it now
        checkAndCreateContainer(validateWizard.getContainerFullPath());

        IFile file = validateWizard.getFile();
        //String filename = file.getLocation().toOSString();
        //IMonitor monitor = MonitorCore.getRequests();
        //monitor.getElements();
        LogBuilder builder = new LogBuilder(file);
        Log log = builder.buildLog(requestResponses);

        builder.writeLog(log);
        file.refreshLocal(1, progressMonitor);
        
        WSIValidator messageValidator = new WSIValidator();
        if(validateWizard.includeWSDLFile())
        {	
        	String wsdlfile = validateWizard.getWSDLFile();
        	String name = validateWizard.getElementName();
        	String namespace = validateWizard.getNamespace();
        	String parentname = validateWizard.getParentName();
        	String type = validateWizard.getType();
        	messageValidator.validate(file, wsdlfile, name, namespace, parentname, type);
        }
        else
        {	
        	messageValidator.validate(file);
        }
        //WSIValidateAction validateAction = new WSIValidateAction(file, true);
//        validateAction.setValidator(messageValidator);
        //validateAction.run();
//        WSIValidator wsiValidator = new WSIValidator();
//        wsiValidator.validate(file);
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  /**
   * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction, org.eclipse.jface.viewers.ISelection)
   */
  public void selectionChanged(IAction action, ISelection selection)
  {
    this.selection = selection;
  }
 
  /**
   * If the container doesn't exist for the selected IFile, create it now.
   * 
   * @param containerPath The container for the selected IFile.
   */
  public static void checkAndCreateContainer(IPath containerPath)
  {
    IResource resource = ResourcesPlugin.getWorkspace().getRoot().findMember(containerPath);
    
    // If the container doesn't exist in the workspace, create it.
    if (resource == null) 
    {
      try
      {
        ContainerGenerator generator = new ContainerGenerator(containerPath);
        generator.generateContainer(null);
      }
      catch (CoreException e)
      {
      }
    }
  }
}
