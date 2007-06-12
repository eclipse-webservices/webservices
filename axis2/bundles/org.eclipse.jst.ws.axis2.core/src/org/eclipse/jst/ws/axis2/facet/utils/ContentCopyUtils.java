/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation, WSO2 Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * WSO2 Inc. - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * IBM Corporation,  WSO2 Inc. - initial API and implementation
 * WSO2 Inc. - initial extended API and implementation
 * 20070213  168766 sandakith@wso2.com - Lahiru Sandakith, Initial code to introduse the Axis2 
 * 										  facet to the framework for 168766
 * 20070426   183046 sandakith@wso2.com - Lahiru Sandakith
 * 20070606   177421 sandakith@wso2.com - fix web.xml wiped out when Axis2 facet
 *******************************************************************************/
package org.eclipse.jst.ws.axis2.facet.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.axis2.core.plugin.messages.Axis2CoreUIMessages;
import org.eclipse.jst.ws.internal.plugin.WebServicePlugin;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.command.internal.env.core.context.ResourceContext;

public class ContentCopyUtils {

	private static IWorkspaceRoot root_ = null;
	private static IWorkspace workspace_ = null;
	private List fileAndDirectoriesList = new ArrayList();
	FileInputStream finStream = null;		

	/**
	 * This method will copy the source directory into the eclipse workspace 
	 * according to the Eclipse Framework API
	 * @param sourceDir
	 * @param destinationDir
	 * @param monitor
	 * @param statusHandler
	 * @return
	 */
	public IStatus copyDirectoryRecursivelyIntoWorkspace(String sourceDir, 
									String destinationDir,IProgressMonitor monitor) {
		IStatus status = Status.OK_STATUS;
		fileAndDirectoriesList.clear();
		File axis2WebappFolder = new File(sourceDir);
		visitAllDirsAndFiles(axis2WebappFolder);
		List deployFiles = new ArrayList();
		deployFiles= fileAndDirectoriesList;

		try {

			//Import the axis2 dependency plugin according to the Resources API of eclipse 
			ResourceContext context = WebServicePlugin.getInstance().getResourceContext();	

			IPath outputPath = new Path (destinationDir);

			String fileName;
			IPath targetPath=null;
			boolean isDirectory = false;

			String deployFile;
			Iterator iterator;

			String tempOutputDir = axis2WebappFolder.toString();
			iterator = deployFiles.iterator();
			while (iterator.hasNext()) {
				deployFile = (String) iterator.next();
				File source = new File(deployFile);
				if (source.isDirectory()) {
					isDirectory =true;
				}else{
					isDirectory=false;
					finStream = new FileInputStream(source);
				}

				if (deployFile.startsWith(tempOutputDir)) {
					fileName = deployFile.substring(tempOutputDir.length());
					targetPath = outputPath.append(fileName).makeAbsolute();
					if (isDirectory){
						makeFolderPathAtLocation(context,  
								targetPath,
								monitor);
					}else{
						if (finStream != null) {
							createFileAtLocation(context,  
									targetPath,
									finStream,
									monitor);
						}
						finStream.close();
					}
				}
			}
		} catch (IOException e) {
			status = StatusUtils.errorStatus(
					NLS.bind(Axis2CoreUIMessages.ERROR_INVALID_FILE_READ_WRITEL,
							 new String[]{e.getLocalizedMessage()}), e);
		} catch (CoreException e) {
			status = StatusUtils.errorStatus(
					NLS.bind(Axis2CoreUIMessages.ERROR_INVALID_FILE_READ_WRITEL,
							 new String[]{e.getLocalizedMessage()}), e);
		}

		return status;

	}

	//	Process all files and directories under dir
	private void visitAllDirsAndFiles(File dir) {

		fileAndDirectoriesList.add(dir.getAbsolutePath());

		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i=0; i<children.length; i++) {
				visitAllDirsAndFiles(new File(dir, children[i]));
			}
		}
	}
	
	
	
	private static IFolder makeFolder (
		    ResourceContext  resourceContext,
		    IContainer       parent,
		    String           folderName,
		    IProgressMonitor progressMonitor )
		  
		  throws CoreException
		  {
		    IResource child  = parent.findMember(folderName);
		    if( child == null )
		    {
		      IFolder folder = parent.getFolder(new Path(folderName));
		      folder.create(true,true,null);
		      return folder;
		    }
		    else if( child.getType() == IResource.FOLDER )
		    {
		      return (IFolder)child;
		    }
		    else
		    {
		      throw new CoreException(
		        new Status( IStatus.ERROR, 
		                    "ResourceUtils",
		                    0, 
		                    NLS.bind("ERROR",//EnvironmentMessages.MSG_ERROR_RESOURCE_NOT_FOLDER,
							                 new Object[]{ parent.getFullPath().append(folderName).toString() }),
					null ) );
		    }
		  }

	
	public static IContainer makeFolderPathAtLocation (
			   ResourceContext  resourceContext,
			   IContainer       resource,
			   IProgressMonitor progressMonitor )
			 
			   throws CoreException
			   {
				 if (resource.exists())
				 {
					 if (resource instanceof IContainer)
					 {
						 return (IContainer) resource;
					 }
					 else
					 {
						 throw new CoreException(
								 new Status( IStatus.ERROR, 
										 "ResourceUtils",
										 0, 
										 NLS.bind("ERROR",
												 new Object[]{ resource.getFullPath().toString() }),
												 null ) );
					 }
				 }
				 else
				 {
					 IContainer parent = makeFolderPathAtLocation(resourceContext, 
							 			resource.getParent(), 
							 			progressMonitor );
					 String folderName = resource.getName();
					 
					 return makeFolder(resourceContext, parent, folderName, progressMonitor  );
				 }
			 }
	
	
	 /**
	  * Creates a path of folders.
	  * Do not call with an absolutePath of less than one segment.
	  * @param resourceContext the resource context for making folders.
	  * @param absolutePath the path of folders that will be created.
	  * @param progressMonitor the progress monitor to be used.
	  * @param statusHandler the status handler.
	  * @return returns the IContainer of the created folder.
	  */
	 public static IContainer makeFolderPathAtLocation (
	   ResourceContext  resourceContext,
	   IPath            absolutePath,
	   IProgressMonitor progressMonitor )
	 
	   throws CoreException
	 {
	 	return makeFolderPathAtLocation(resourceContext,
	 		getWorkspaceRoot().getContainerForLocation(absolutePath),
	 		progressMonitor);
	 }
	 
	 
	 public static IWorkspaceRoot getWorkspaceRoot ()
	  {
	    if (root_ == null)
	    {
	      root_ = ResourcesPlugin.getWorkspace().getRoot();
	    }
	    return root_;
	  }
	
	  public static IFile createFileAtLocation (
			     ResourceContext resourceContext,
			     IPath           absolutePath,
			     InputStream     inputStream,
			     IProgressMonitor progressMonitor )

			     throws CoreException 
			   {    
			     if (!absolutePath.isAbsolute())
			     {
			       throw new CoreException(new Status(IStatus.ERROR, "ResourceUtils",0,
			    		   NLS.bind("ERROR",//EnvironmentMessages.MSG_ERROR_PATH_NOT_ABSOLUTE,
			    		   new Object[] {absolutePath.toString()}),null));
			     }
			     if (absolutePath.segmentCount() < 1)
			     {
			       throw new CoreException(new Status(IStatus.ERROR,"ResourceUtils",0,
			    		   NLS.bind("ERROR",//EnvironmentMessages.MSG_ERROR_PATH_EMPTY,
			    		   new Object[] {absolutePath.toString()}),null));
			     }
			     if (absolutePath.segmentCount() < 2)
			     {
			       throw new CoreException(new Status(IStatus.ERROR,"ResourceUtils",0,
			    		   NLS.bind("ERROR",//EnvironmentMessages.MSG_ERROR_PATH_NOT_FOLDER,
			    		   new Object[] {absolutePath.toString()}),null));
			     }
			     IContainer parent   = makeFolderPathAtLocation(resourceContext, 
			    		 				absolutePath.removeLastSegments(1), progressMonitor);
			     String     fileName = absolutePath.lastSegment();
			     
			     return makeFile(resourceContext, parent, fileName, inputStream, progressMonitor);
			   }
	
	
	  private static IFile makeFile (
			    ResourceContext  resourceContext,
			    IContainer       parent,
			    String           fileName,
			    InputStream      inputStream,
			    IProgressMonitor progressMonitor )
			 
			    throws CoreException
			  {
			    IResource child  = parent.findMember(fileName);
			    
			    if( child != null )
			    {
			      if( child.getType() == IResource.FILE )
			      {
			    
			        //We have permission to overwrite so check if file is read-only
			        if(child.getResourceAttributes()!=null && 
			        						child.getResourceAttributes().isReadOnly() )
			        {
			          IFile[] files = new IFile[1];
			          files[0] = (IFile)child;
			        }

			        //Change the contents of the existing file.
			        IFile file = parent.getFile( new Path(fileName) );
			        file.setContents( inputStream, true, true, null );
			        
			        return file;
			      
			      }
			      else
			      {
			        throw new CoreException( 
			          new Status( IStatus.ERROR,
			                      "ResourceUtils",
			                      0, 
			                      NLS.bind("ERROR",
							                       new Object[] {parent.getFullPath().append(fileName)}),
								  null ) );
			      }
			    }
			    else
			    {
			      //Create a new file.
			      IFile file = parent.getFile( new Path(fileName) );
			      file.create( inputStream, true, null);
			      
			      return file;
			    }
			  }
	  
	  public static IWorkspace getWorkspace ()
	  {
	    if (workspace_ == null)
	    {
	      if (root_ == null)
	      {
	        root_ = ResourcesPlugin.getWorkspace().getRoot();
	      }
	      
	      workspace_ = root_.getWorkspace();
	    }
	    
	    return workspace_;
	  }
	  
}
