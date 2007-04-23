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


package org.eclipse.jst.ws.internal.consumption.datamodel.validate;

import java.util.HashSet;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceDescription;
import org.eclipse.core.resources.ResourcesPlugin;

/**
  * This class manages the validation state of projects that some tasks may want
  * to disable.  The manager can then be used to restore the validation state.
**/
public class ValidationManager
{

  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";

  /**
    * This set contains a set of IProjects that need to be validated.
  **/
  private HashSet projects = null;              

  /**
    * This object contains a reference to the j2ee validator.
  **/
  //pgm private ValidatorManager j2eeManager = null;

  /**
    * This boolean contains the auto build setting under preferrence
  **/
  private boolean isAutoBuild_;
  private boolean autoBuildDisabled_;
                                             
  public ValidationManager()
  {
    projects    = new HashSet();
    //pgm j2eeManager = ValidatorManager.getManager();
    setAutoBuildPreference();
    autoBuildDisabled_ = false;
  }
  
  /**
    * Disables the validation for a project if required.
  **/
  public void disableValidationForProject( IProject project )
  {    
    // We need to remember that this project needs have validation turned back on.
    projects.add( project );
    
    //pgm j2eeManager.suspendAllValidation( true );
  }

  /**
    * Restores the validation state for all needed projects.
  **/
  public void restoreValidationForProjects( boolean runValidation )
  {
  }

  public void modifyAutoBuild(boolean isAutoBuild) {
    try {
      IWorkspace workspace = ResourcesPlugin.getWorkspace();
      IWorkspaceDescription workspaceDesc = workspace.getDescription();
      workspaceDesc.setAutoBuilding(isAutoBuild);
      workspace.setDescription(workspaceDesc);
    }
    catch (Exception e) {}
  }

  public void disableAutoBuild() {
    if (!autoBuildDisabled_) {
      setAutoBuildPreference();
      modifyAutoBuild(false);
      autoBuildDisabled_ = true;
    }
  }

  public void restoreAutoBuild() {
    if (autoBuildDisabled_) {
      modifyAutoBuild(getAutoBuildPreference());
      autoBuildDisabled_ = false;
    }
  }

  public boolean setAutoBuildPreference() {
    isAutoBuild_ = getWorkspaceAutoBuildPreference();
    return isAutoBuild_;
  }

  public boolean getAutoBuildPreference() {
    return isAutoBuild_;
  }

  public boolean getWorkspaceAutoBuildPreference() {
    IWorkspaceDescription workspaceDesc = ResourcesPlugin.getWorkspace().getDescription();
    return workspaceDesc.isAutoBuilding();
  }

}
