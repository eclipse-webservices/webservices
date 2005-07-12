/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.axis.creation.ui.command;


import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.wst.command.internal.provisional.env.core.SimpleCommand;
import org.eclipse.wst.command.internal.provisional.env.core.common.Environment;
import org.eclipse.wst.command.internal.provisional.env.core.common.SimpleStatus;
import org.eclipse.wst.command.internal.provisional.env.core.common.Status;
import org.eclipse.wst.common.componentcore.internal.StructureEdit;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.componentcore.resources.IVirtualFile;

/**
 * 
 * This command copies the server-config.wsdd file to it's proper location in the module.
 * 
 */

public class CopyDeploymentFileCommand extends SimpleCommand
{
  private String projectName_;
  private String componentName_;
  
  /**
   * Constructor for CopyDeploymentFileCommand.
   * @param String description
   * @param String name
   * 
   */
  public CopyDeploymentFileCommand( String projectName, String componentName )
  {
    super("org.eclipse.jst.ws.internal.axis.consumption.core.command.CopyDeploymentFileCommand", "org.eclipse.jst.ws.internal.axis.consumption.core.command.CopyDeploymentFileCommand");
    
    projectName_   = projectName;
    componentName_ = componentName;
  }

  public Status execute(Environment environment)
  {
    Status            status         = new SimpleStatus("");
    IVirtualComponent component      = J2EEUtils.getVirtualComponent( projectName_, componentName_ );
    IFolder           root           = StructureEdit.getOutputContainerRoot( component );
    IPath             path           = new Path( "WEB-INF" ).append( "server-config.wsdd" );
    IFile             descriptorFile = root.getFile( path );
    IVirtualFile      newLocation    = component.getRootFolder().getFile( path );
    IPath             newPath        = newLocation.getWorkspaceRelativePath();
    
    try
    {
      descriptorFile.refreshLocal( 0, null );
      descriptorFile.copy( newPath, true, null );
    }
    catch( CoreException exc )
    {
      exc.printStackTrace();	
    }
    
    return status;
  }
}
