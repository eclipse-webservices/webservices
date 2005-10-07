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
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.wst.command.internal.provisional.env.core.AbstractDataModelOperation;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.componentcore.resources.IVirtualFile;

/**
 * 
 * This command copies the server-config.wsdd file to it's proper location in the module.
 * 
 */

public class CopyDeploymentFileCommand extends AbstractDataModelOperation
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
    projectName_   = projectName;
    componentName_ = componentName;
  }

	public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable ) 
	{
    IStatus            status         = Status.OK_STATUS;
    
    try
    {
      IVirtualComponent component      = J2EEUtils.getVirtualComponent( projectName_, componentName_ );
      IFolder           root           = J2EEUtils.getOutputContainerRoot( component );
      IPath             path           = new Path( "WEB-INF" ).append( "server-config.wsdd" );
      IFile             descriptorFile = root.getFile( path );
      IVirtualFile      newLocation    = component.getRootFolder().getFile( path );
      IPath             targetPath     = newLocation.getWorkspaceRelativePath();
      IFile             targetFile     = (IFile)ResourceUtils.findResource( targetPath );
        
      descriptorFile.refreshLocal( 0, null );
      
      if( targetFile != null && targetFile.exists() )
      {
        // The target file already exists so we will just overwrite its contents.
    	targetFile.setContents( descriptorFile.getContents(), true, false, null );
      }
      else
      {
    	// The target file doesn't exist so we will copy it.
        descriptorFile.copy( targetPath, true, null );
      }
    }
    catch( Throwable exc )
    {
    }
    
    return status;
  }
}
