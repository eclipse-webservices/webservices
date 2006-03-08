/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060112   121199 jesper@selskabet.org - Jesper M�ller
 *******************************************************************************/
package org.eclipse.wst.command.internal.env.common;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.command.internal.env.EnvironmentMessages;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.command.internal.env.core.context.ResourceContext;
import org.eclipse.wst.common.environment.Choice;
import org.eclipse.wst.common.environment.IStatusHandler;
import org.eclipse.wst.common.environment.StatusException;



/**
* This class contains useful methods for working with Eclipse resources.
*/
public final class FileResourceUtils
{
  //
  // Keeps the IWorkspace hanging around. See getWorkspace().
  //
  private static IWorkspace workspace_ = null;
  //
  // Keeps the IWorkspaceRoot hanging around. See getWorkspaceRoot().
  //
  private static IWorkspaceRoot root_ = null;
  
//  private static MessageUtils msg_ = new MessageUtils( "org.eclipse.wst.command.internal.env.common.environment", new FileResourceUtils() );


 /**
  * Returns the IWorkspaceRoot object.
  * @return The IWorkspaceRoot object.
  */
  public static IWorkspaceRoot getWorkspaceRoot ()
  {
    if (root_ == null)
    {
      root_ = ResourcesPlugin.getWorkspace().getRoot();
    }
    return root_;
  }

  /**
	 * Returns the IWorkspace object.
	 * 
	 * @return The IWorkspace object.
	 */
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
  
  /**
   * 
   * @return returns an array of three common choices. (ie. Yes, yes all, and cancel )
   */
  public static Choice[] getThreeStateFileOptions() 
  {
    Vector choices = new Vector();
    
    choices.add(new Choice( 'Y', EnvironmentMessages.LABEL_YES));
    choices.add(new Choice( 'A', EnvironmentMessages.LABEL_YES_TO_ALL));
    choices.add(new Choice( 'C', EnvironmentMessages.LABEL_CANCEL));
    
    return (Choice[])choices.toArray(new Choice[choices.size()]);
  }
  
  /**
  * Returns an
  * {@link org.eclipse.core.resources.IResource IResource}
  * of the given absolute pathname or null if no such resource exists.
  * @param absolutePathname The absolute path of the resource.
  * @return The <code>IResource</code>.
  */
  public static IResource findResource ( String absolutePathname )
  {
    if (absolutePathname == null)
    {
      return null;
    }
    
    return findResource(new Path(absolutePathname));
  }

  /**
  * Returns an
  * {@link org.eclipse.core.resources.IResource IResource}
  * of the given absolute path or null if no such resource exists.
  * @param absolutePath The absolute <code>IPath</code> of the resource.
  * @return The <code>IResource</code>.
  */
  public static IResource findResource ( IPath absolutePath )
  {
    if (absolutePath == null)
    {
      return null;
    }
    
    return FileResourceUtils.getWorkspaceRoot().findMember(absolutePath);
  }
  
  /**
   * Copies a file from a plugin's installation location
   * to an Eclipse folder.
   * @param plugin The plugin containing the files to copy.
   * Must not be null.
   * @param sourcePath The path, relative to the <code>plugin</code>
   * install location, containing the files to copy.
   * If null, then the plugin install location is the source path
   * (ie. null is equivalent to ".").
   * @param pathname The pathname of the file to copy.
   * The pathname is relative to the <code>plugin sourcePath</code>.
   * Must not be null.
   * @param targetPath The absolute Eclipse path of the folder to
   * which the file will be copied. The relative pathname of the
   * file is preserved.
   * Must not be null.
   * @param createFolders The intermediate folder creation policy, one of
   * {@link #CREATE CREATE} or {@link #DONT_CREATE DONT_CREATE}.
   * <ul>
   * <li><code>CREATE</code> -
   * If any intermediate folders in the given <code>absolutePath</code>
   * do not exist, they will be created.
   * <li><code>DONT_CREATE</code> -
   * If any intermediate folders in the given <code>absolutePath</code>
   * do not exist, the method will throw a <code>CoreException</code>.
   * </ul>
   * @param overwriteFile The policy for existing files, one of
   * {@link #OVERWRITE OVERWRITE} or {@link #DONT_OVERWRITE DONT_OVERWRITE}.
   * <ul>
   * <li><code>OVERWRITE</code> -
   * If a resource of the same name as the given
   * <code>absolutePath</code> already exists and is a file,
   * it will be replaced.
   * If the resource already exists and it is not a file,
   * then no file will be created and
   * a <code>CoreException</code> will be thrown.
   * <li><code>DONT_OVERWRITE</code> -
   * If any resource of the same name as the given
   * <code>absolutePath</code> already exists,
   * then no file will be created and
   * a <code>CoreException</code> will be thrown.
   * </ul>
   * @param progressMonitor The progress monitor for the operation, or null.
   * @throws CoreException An exception containing an
   * {@link org.eclipse.core.runtime.IStatus IStatus}
   * with a severity of <code>IStatus.ERROR</code> and a
   * locale-specific description of the cause.
   */
   static public void copyFile( ResourceContext resourceContext,
                                Plugin plugin,
                                IPath sourcePath,
                                IPath pathname,
                                IPath targetPath,
                                IProgressMonitor progressMonitor,
                                IStatusHandler statusMonitor )
     throws CoreException
   {
     try
     {
       IPath target = targetPath.append(pathname);
       IPath source = sourcePath == null ? pathname : sourcePath.append(pathname);
       InputStream input = plugin.openStream(source);
       createFile(resourceContext, target, input, progressMonitor, statusMonitor);
     }
     catch (IOException e)
     {
       throw new CoreException(new Status(IStatus.ERROR,
                               plugin.getBundle().getSymbolicName(),
                               0,
                               EnvironmentMessages.MSG_ERROR_IO,e));
     }
   }
   
  /**
   * Deletes a file under a container.
   * The container must already exist.
   * @param file - the IFile to be deleted
   * @param progressMonitor
   * @param statusMonitor
   * @return True if the file does not exist or if it exists and is successfully deleted. False otherwise.
   */
  public static boolean deleteFile( ResourceContext resourceContext, 
                                    IFile file,
                                    IProgressMonitor progressMonitor, 
                                    IStatusHandler statusMonitor)
    throws CoreException
  {
    if (file.exists())
    {
      if (!resourceContext.isOverwriteFilesEnabled())
      {
        IStatus status 
          = StatusUtils.warningStatus( NLS.bind(EnvironmentMessages.MSG_ERROR_FILE_OVERWRITE_DISABLED,
                                                        new Object[]{ file.getParent().getFullPath().toString(),
                                                                      file.getName()}) );
                            
        
        Choice choice = statusMonitor.report( status, getThreeStateFileOptions() );
        
        if( choice.getShortcut() == 'C' ) return false;
        
        if( choice.getShortcut() == 'A' ) resourceContext.setOverwriteFilesEnabled(true);
      }
      
      //We have permission to overwrite so check if file is read-only
      if (file.isReadOnly())
      {
        if (!resourceContext.isCheckoutFilesEnabled())
        {
          IStatus status 
            = StatusUtils.warningStatus( NLS.bind(EnvironmentMessages.MSG_ERROR_FILE_CHECKOUT_DISABLED,
                                                          new Object[]{ file.getParent().getFullPath().toString(),
                                                                        file.getName()}) );
          
          Choice choice = statusMonitor.report( status, getThreeStateFileOptions() );
          
          if( choice.getShortcut() == 'C' ) return false;
          
          if( choice.getShortcut() == 'A' ) resourceContext.setCheckoutFilesEnabled(true);          
        }
        
        IFile[] files = new IFile[1];
        files[0] = file;
        IStatus status = getWorkspace().validateEdit(files, null);
        
        if( status.getSeverity() == IStatus.ERROR )
        {
          statusMonitor.reportError( status );
          return false;
        }
      }
      
      file.delete(true, null);
    }
    //At this point, either the file did not exist or we successfully deleted
    // it. Return success.
    return true;
  }

  /**
   * Deletes a folder under a container.
   * @param folder - the IFolder to be deleted
   * @param progressMonitor
   * @param statusMonitor
   * @return True if the folder does not exist or if it exists and is successfully deleted along with its members. False otherwise.
   */
   public static boolean deleteFolder( ResourceContext resourceContext,
                                       IFolder folder,
                                       IProgressMonitor progressMonitor,
                                       IStatusHandler statusMonitor )
     throws CoreException
   {
     if (!folder.exists()) return true;

     boolean     deleted   = true;
     IResource[] resources = folder.members();
     
     for (int i=0; i<resources.length; i++)
     {
       IResource resource = resources[i];
       if (resource instanceof IFile)
       {
         deleted = deleteFile(resourceContext, (IFile)resource, progressMonitor, statusMonitor);
       }
       if (resource instanceof IFolder)
       {
         deleted = deleteFolder( resourceContext, (IFolder)resource, progressMonitor, statusMonitor);
       }

       if( !deleted ) break;
     }
     
     if( deleted )
     {
       folder.delete(true, true, null);
       return true;
     }
     else
       return false;
   }

  /**
  * Creates a file of the given <code>absolutePath</code>
  * and returns its handle as an <code>IFile</code>.
  * If the file cannot be created, a
  * <code>CoreException</code> containing an
  * <code>IStatus</code> object is thrown.
  * @param absolutePath The absolute path of the file to create.
  * The project at the beginning of the path must already exist,
  * that is, this method cannot be used to create projects.
  * @param progressMonitor The progress monitor for the operation, or null.
  * @return The {@link org.eclipse.core.resources.IFile IFile}
  * handle of the file.
  * @throws CoreException An exception containing an
  * {@link org.eclipse.core.runtime.IStatus IStatus}
  * with a severity of <code>IStatus.ERROR</code> and a
  * locale-specific description of the cause.
  */
  public static IFile createFile (
    ResourceContext resourceContext,
    IPath           absolutePath,
    InputStream     inputStream,
    IProgressMonitor progressMonitor,
    IStatusHandler   statusHandler )

    throws CoreException 
  {    
    if (!absolutePath.isAbsolute())
    {
      throw new CoreException(new Status(IStatus.ERROR, "ResourceUtils",0,NLS.bind(EnvironmentMessages.MSG_ERROR_PATH_NOT_ABSOLUTE,new Object[] {absolutePath.toString()}),null));
    }
    if (absolutePath.segmentCount() < 1)
    {
      throw new CoreException(new Status(IStatus.ERROR,"ResourceUtils",0,NLS.bind(EnvironmentMessages.MSG_ERROR_PATH_EMPTY,new Object[] {absolutePath.toString()}),null));
    }
    if (absolutePath.segmentCount() < 2)
    {
      throw new CoreException(new Status(IStatus.ERROR,"ResourceUtils",0,NLS.bind(EnvironmentMessages.MSG_ERROR_PATH_NOT_FOLDER,new Object[] {absolutePath.toString()}),null));
    }
    IContainer parent   = makeFolderPath(resourceContext, absolutePath.removeLastSegments(1), progressMonitor, statusHandler);
    String     fileName = absolutePath.lastSegment();
    
    return makeFile(resourceContext, parent, fileName, inputStream, progressMonitor, statusHandler);
  }

  /**
  * Creates under the given <code>project</code>
  * a file of the given <code>relativePath</code>
  * and returns its handle as an <code>IFile</code>.
  * If the file cannot be created, a
  * <code>CoreException</code> containing an
  * <code>IStatus</code> object is thrown.
  * @param absolutePath The absolute path of the file to create.
  * The project at the beginning of the path must already exist,
  * that is, this method cannot be used to create projects.
  * @param createFolders The intermediate folder creation policy, one of
  * {@link #CREATE CREATE} or {@link #DONT_CREATE DONT_CREATE}.
  * <ul>
  * <li><code>CREATE</code> -
  * If any intermediate folders in the given <code>absolutePath</code>
  * do not exist, they will be created.
  * <li><code>DONT_CREATE</code> -
  * If any intermediate folders in the given <code>absolutePath</code>
  * do not exist, the method will throw a <code>CoreException</code>.
  * </ul>
  * @param overwriteFile The policy for existing files, one of
  * {@link #OVERWRITE OVERWRITE} or {@link #DONT_OVERWRITE DONT_OVERWRITE}.
  * <ul>
  * <li><code>OVERWRITE</code> -
  * If a resource of the same name as the given
  * <code>absolutePath</code> already exists and is a file,
  * it will be replaced.
  * If the resource already exists and it is not a file,
  * then no file will be created and
  * a <code>CoreException</code> will be thrown.
  * <li><code>DONT_OVERWRITE</code> -
  * If any resource of the same name as the given
  * <code>absolutePath</code> already exists,
  * then no file will be created and
  * a <code>CoreException</code> will be thrown.
  * </ul>
  * @param progressMonitor The progress monitor for the operation, or null.
  * @return The {@link org.eclipse.core.resources.IFile IFile}
  * handle of the file.
  * @throws CoreException An exception containing an
  * {@link org.eclipse.core.runtime.IStatus IStatus}
  * with a severity of <code>IStatus.ERROR</code> and a
  * locale-specific description of the cause.
  */
  public static IFile createFile (
    ResourceContext resourceContext,
    IProject        project,
    IPath           relativePath,
    InputStream     inputStream,
    IProgressMonitor progressMonitor,
    IStatusHandler   statusMonitor )
 
    throws CoreException 
  {
    IPath absolutePath = project.getFullPath().append(relativePath);
    return createFile(resourceContext, absolutePath, inputStream, progressMonitor, statusMonitor);
  }

  /**
  * Creates an output stream that can be used to write to the
  * given <code>file</code>. Actual changes to the workspace
  * may occur during creation of the stream, while writing to
  * the stream, or when the stream is closed.
  * A <code>CoreException</code> containing
  * an <code>IStatus</code> will be thrown
  * at some point in the lifecycle of the stream
  * if the file resource cannot be created.
  * @param file The {@link org.eclipse.core.resources.IFile IFile}
  * handle of the file resource to create. The project implied by the
  * pathname of the file must already exist,
  * that is, this method cannot be used to create projects.
  * @param progressMonitor The progress monitor for the operation, or null.
  * @return An <code>OutputStream</code> tied to the file resource.
  * Actual checks of or changes to the workspace may occur as early during
  * stream creation, closure, or any time in between.
  * @throws CoreException An exception containing an
  * {@link org.eclipse.core.runtime.IStatus IStatus}
  * with a severity of <code>IStatus.ERROR</code> and a
  * locale-specific description of the cause.
  * Reasons include:
  * <ol>
  * <li>The project of the given file's path does not exist.
  * <li>A non-file resource of the same name of the given file
  * already exists.
  * <li>A file resource of the same name of the given file
  * already exists, and <code>overwriteFile</code> is false.
  * <li>One or more intermediate folders to the given file
  * do not exist, and <code>createFolders</code> is false.
  * </ol>
  */

  public static OutputStream newFileOutputStream (
       ResourceContext  context,
       IPath            file,
       IProgressMonitor progressMonitor,
       IStatusHandler    statusHandler )
 
  {
    return new FileResourceOutputStream(context, file, progressMonitor, statusHandler);
  }

  //----------------------------------------------------------------------
  // Naughty bits...
  //----------------------------------------------------------------------

  //
  // Creates a path of folders.
  // Do not call with an absolutePath of less than one segment.
  //
  /**
   * Creates a path of folders.
   * Do not call with an absolutePath of less than one segment.
   * @param resourceContext the resource context for making folders.
   * @param absolutePath the path of folders that will be created.
   * @param progressMonitor the progress monitor to be used.
   * @param statusHandler the status handler.
   * @return returns the IContainer of the created folder.
   */
  public static IContainer makeFolderPath (
    ResourceContext  resourceContext,
    IPath            absolutePath,
    IProgressMonitor progressMonitor,
    IStatusHandler    statusHandler )
  
    throws CoreException
  {
    if (absolutePath.segmentCount() <= 1)
    {
      return getWorkspaceRoot().getProject(absolutePath.segment(0));
    }
    else
    {
      IContainer parent = makeFolderPath(resourceContext, absolutePath.removeLastSegments(1), progressMonitor, statusHandler );
      String folderName = absolutePath.lastSegment();
      
      return makeFolder(resourceContext, parent,folderName, progressMonitor , statusHandler );
    }
  }
  //
  // Creates a folder under a container.
  // The container must already exist.
  //
 private static IFolder makeFolder (
    ResourceContext  resourceContext,
    IContainer       parent,
    String           folderName,
    IProgressMonitor progressMonitor,
    IStatusHandler    statusHandler )
  
  throws CoreException
  {
    IResource child  = parent.findMember(folderName);
    Choice    result = null;
    
    if( child == null )
    {
      if (!resourceContext.isCreateFoldersEnabled())  
      {
        result = statusHandler.report(
                StatusUtils.warningStatus(
                		NLS.bind(EnvironmentMessages.MSG_ERROR_FOLDER_CREATION_DISABLED,
        	                      new Object[]{ parent.getFullPath().toString(), folderName} ) ), 
                getThreeStateFileOptions() );
        
        if( result == null || result.getShortcut() == 'C' )
        {
          return null;
        }
        else if( result.getShortcut() == 'A' )
        {
          resourceContext.setCreateFoldersEnabled(true);
        }
      }
      
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
                    NLS.bind(EnvironmentMessages.MSG_ERROR_RESOURCE_NOT_FOLDER,
					                 new Object[]{ parent.getFullPath().append(folderName).toString() }),
			null ) );
    }
  }

  //
  // Creates a file under a container.
  // The container must already exist.
  //
 private static IFile makeFile (
    ResourceContext  resourceContext,
    IContainer       parent,
    String           fileName,
    InputStream      inputStream,
    IProgressMonitor progressMonitor,
    IStatusHandler    statusHandler )
 
    throws CoreException
  {
    IResource child  = parent.findMember(fileName);
    Choice    result = null;
    
    if( child != null )
    {
      if( child.getType() == IResource.FILE )
      {
        if( !resourceContext.isOverwriteFilesEnabled() )   
        {
          result = statusHandler.report( 
                StatusUtils.warningStatus( NLS.bind(EnvironmentMessages.MSG_ERROR_FILE_OVERWRITE_DISABLED,
                                           new Object[] {parent.getFullPath().toString(),fileName}) ),					  		    
                getThreeStateFileOptions() );
          
          if( result == null || result.getShortcut() == 'C' )
          {
            return null;
          }
          else if( result.getShortcut() == 'A' )
          {
            resourceContext.setOverwriteFilesEnabled(true);
          }
        }	
        
        //We have permission to overwrite so check if file is read-only
        if( child.getResourceAttributes().isReadOnly() )
        {
          if( !resourceContext.isCheckoutFilesEnabled() ) 
          {            
            result = statusHandler.report( 
                         StatusUtils.errorStatus( NLS.bind(EnvironmentMessages.MSG_ERROR_FILE_CHECKOUT_DISABLED,
                                                  new Object[]{ parent.getFullPath().toString(),fileName} ) ), 
                         getThreeStateFileOptions() );
            
		    if( result == null || result.getShortcut() == 'C' )
		    {
              return null;
		    }
            else if( result.getShortcut() == 'A' )
            {
              resourceContext.setCheckoutFilesEnabled(true);
            }
          }

          IFile[] files = new IFile[1];
          files[0] = (IFile)child;
          
          IStatus status = getWorkspace().validateEdit(files,null);
          
          try
          {
            statusHandler.report( status );
          }
          catch( StatusException exc )
          {
            return null;
          }
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
                      NLS.bind(EnvironmentMessages.MSG_ERROR_RESOURCE_NOT_FILE,
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

 
 /**
   * Creates a file of the given <code>absolutePath</code>
   * and returns its handle as an <code>IFile</code>.
   * If the file cannot be created, a
   * <code>CoreException</code> containing an
   * <code>IStatus</code> object is thrown.
   * @param absolutePath The absolute path of the file to create.
   * The project at the beginning of the path must already exist,
   * that is, this method cannot be used to create projects.
   * @param progressMonitor The progress monitor for the operation, or null.
   * @return The {@link org.eclipse.core.resources.IFile IFile}
   * handle of the file.
   * @throws CoreException An exception containing an
   * {@link org.eclipse.core.runtime.IStatus IStatus}
   * with a severity of <code>IStatus.ERROR</code> and a
   * locale-specific description of the cause.
   */
   public static IFile createFileAtLocation (
     ResourceContext resourceContext,
     IPath           absolutePath,
     InputStream     inputStream,
     IProgressMonitor progressMonitor,
     IStatusHandler   statusHandler )

     throws CoreException 
   {    
     if (!absolutePath.isAbsolute())
     {
       throw new CoreException(new Status(IStatus.ERROR, "ResourceUtils",0,NLS.bind(EnvironmentMessages.MSG_ERROR_PATH_NOT_ABSOLUTE,new Object[] {absolutePath.toString()}),null));
     }
     if (absolutePath.segmentCount() < 1)
     {
       throw new CoreException(new Status(IStatus.ERROR,"ResourceUtils",0,NLS.bind(EnvironmentMessages.MSG_ERROR_PATH_EMPTY,new Object[] {absolutePath.toString()}),null));
     }
     if (absolutePath.segmentCount() < 2)
     {
       throw new CoreException(new Status(IStatus.ERROR,"ResourceUtils",0,NLS.bind(EnvironmentMessages.MSG_ERROR_PATH_NOT_FOLDER,new Object[] {absolutePath.toString()}),null));
     }
     IContainer parent   = makeFolderPathAtLocation(resourceContext, absolutePath.removeLastSegments(1), progressMonitor, statusHandler);
     String     fileName = absolutePath.lastSegment();
     
     return makeFile(resourceContext, parent, fileName, inputStream, progressMonitor, statusHandler);
   }

/**
  * Creates a path of folders using absolute filenames.
  * Do not call with an absolutePath of less than one segment.
  * @param resourceContext the resource context for making folders.
  * @param resource the resource that will be created.
  * @param progressMonitor the progress monitor to be used.
  * @param statusHandler the status handler.
  * @return returns the IContainer of the created folder.
  */
 public static IContainer makeFolderPathAtLocation (
   ResourceContext  resourceContext,
   IContainer       resource,
   IProgressMonitor progressMonitor,
   IStatusHandler   statusHandler )
 
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
							 NLS.bind(EnvironmentMessages.MSG_ERROR_RESOURCE_NOT_FOLDER,
									 new Object[]{ resource.getFullPath().toString() }),
									 null ) );
		 }
	 }
	 else
	 {
		 IContainer parent = makeFolderPathAtLocation(resourceContext, resource.getParent(), progressMonitor, statusHandler );
		 String folderName = resource.getName();
		 
		 return makeFolder(resourceContext, parent, folderName, progressMonitor , statusHandler );
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
   IProgressMonitor progressMonitor,
   IStatusHandler    statusHandler )
 
   throws CoreException
 {
 	return makeFolderPathAtLocation(resourceContext,
 		getWorkspaceRoot().getContainerForLocation(absolutePath),
 		progressMonitor, statusHandler);
 }
 
 

}
