/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jst.ws.internal.ui.dialog;

import java.util.Vector;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.ui.IJavaElementSearchConstants;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.jface.wizard.IWizardContainer;
import org.eclipse.jst.ws.internal.common.Filter;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.internal.ui.WSUIPluginMessages;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;
import org.eclipse.ui.dialogs.SelectionDialog;


/**
* Contains utility methods for launching various common dialogs.
*/
public final class DialogUtils
{

  /**
  * Raises a dialog for browsing containers.
  * @param parentShell The parent shell, optionally null.
  * @param initialPathname The initial selection.
  * @return The selected container or null if the dialog
  * is cancelled, or there is no selection, or the selection
  * is not a project or a folder.
  */
  public static String browseContainers ( Shell parentShell, String initialPathname )
  {
    String selectedPathname = null;
    IPath initialPath = new Path(initialPathname);
    IResource initialResource = ResourceUtils.getWorkspaceRoot().findMember(initialPath);
    if (initialResource == null)
    {
      if (initialPath.segmentCount() == 1)
      {
        initialResource = ResourceUtils.getWorkspaceRoot().getProject(initialPath.segment(0));
      }
      else if (initialPath.segmentCount() > 1)
      {
        initialResource = ResourceUtils.getWorkspaceRoot().getFolder(initialPath);
      }
    }
    else
    {
      if (initialResource.getType() == IResource.FILE)
      {
        initialResource = initialResource.getParent();
      }
    }
    ContainerSelectionDialog dialog = new ContainerSelectionDialog(parentShell,(IContainer)initialResource,true,null);
    dialog.open();
    Object[] result = dialog.getResult();
    if (result != null && result.length > 0)
    {
      if (result[0] instanceof IPath)
      {
        IPath path = (IPath)result[0];
        selectedPathname = path.toString();
      }
    }
    return selectedPathname;
  }

  /**
  * Raises a dialog for browsing resources.
  * @param parentShell The parent shell, optionally null.
  * @param topResource The resource under which to browse.
  * If null, all resources in the workspace are browsed.
  * @param initialSelection The initial selection, or null for none.
  * If the initial selection is not null and exists in the set of
  * resources found by the browser, that resource is selected by default.
  * @param filter The resource filter object.
  * @return The selected resource.
  */
  public static IResource browseResources ( Shell parentShell, IResource topResource, IResource initialSelection, Filter filter)
  {
    ResourceSelectionDialog rds = new ResourceSelectionDialog(parentShell,topResource,initialSelection,filter);
    rds.setMultipleSelectionEnabled(false);
    if (rds.open() == Dialog.OK)
    {
      IResource[] resources = rds.getResult();
      if (resources != null && resources.length > 0)
      {
        return resources[0];
      }
    }
    return null;
  }

  /**
  * <b>Implementation remains TBD.</b><br>
  * Raises a dialog for browsing classes on a project's class path.
  * @param parentShell The parent shell, optionally null.
  * @param project The project.
  * @return The qualified name of the selected java class.
  */
  public static String browseClasses ( Shell parentShell, IProject project )
  {
    return browseClasses(parentShell, project, (IRunnableContext)null);
  }

  public static String browseClasses ( Shell parentShell, IProject project, IWizardContainer container)
  {
    return browseClasses(parentShell, project, (IRunnableContext)container);
  }

  /**
  * Raises a dialog for browsing classes on a project's class path.
  * @param parentShell The parent shell, optionally null.
  * @param project The project.
  * @param container The container of the launching wizard.
  * @return The qualified name of the selected java class.
  */
  public static String browseClasses ( Shell parentShell, 
                                       IProject project, 
                                       IRunnableContext ctxt) 
  {
    return browseClasses(parentShell, new IResource[] {project}, ctxt);
  }
  
  public static String browseClasses(Shell parentShell, IResource[] resources, IRunnableContext ctxt)
  {
    IType itype = browseClassesAsIType(parentShell, resources, ctxt);
    if (itype != null)
      return itype.getFullyQualifiedName();
    else
      return null;
  }
  
  public static IType browseClassesAsIType(Shell parentShell, IResource[] resources, IRunnableContext ctxt)
  {
	Vector javaElements = new Vector();
	for (int i = 0; i < resources.length; i++)
	{
	  IJavaElement javaElement = JavaCore.create(resources[i]);
	  if (javaElement != null)
	    javaElements.add(javaElement);
	}
	IJavaElement[] elements = (IJavaElement[])javaElements.toArray(new IJavaElement[0]);
	
    IJavaSearchScope scope = SearchEngine.createJavaSearchScope(elements);
    scope.setIncludesClasspaths(true);
    scope.setIncludesBinaries(true);

    //Creating and opening a TypeSelectionDialog
    try
	{
      SelectionDialog dialog = JavaUI.createTypeDialog(parentShell, ctxt, scope, IJavaElementSearchConstants.CONSIDER_CLASSES, false);
      dialog.setTitle(WSUIPluginMessages.DIALOG_TITLE_CLASS_BROWSE);
      dialog.setMessage(WSUIPluginMessages.DIALOG_TITLE_CLASS_BROWSE);    
      dialog.open();

      //Getting the result and returning it
      Object[] results = dialog.getResult();
      if (results != null && results.length > 0)
      {
        if (results[0] instanceof IType)
        {
          return (IType)results[0];
        }
      }
    }
    catch (JavaModelException jme)
	{
    }
    return null;
  }

  /**
  * Raises a dialog for browsing interfaces on a project's class path.
  * @param parentShell The parent shell, optionally null.
  * @param project The project.
  * @param container The container of the launching wizard.
  * @return The qualified name of the selected java interface.
  */
  public static String browseInterfaces( Shell parentShell, 
                                         IProject project, 
                                         IWizardContainer container) 
  									    
  {
    String interfaceName = null;

    //Limiting search scope to the project
    IResource[] resources = new IResource[1];
    resources[0]=project;
	IJavaElement[] elements = new IJavaElement[1];
	elements[0] = JavaCore.create(resources[0]);
    IJavaSearchScope scope = SearchEngine.createJavaSearchScope(elements);
    scope.setIncludesClasspaths(true);
    scope.setIncludesBinaries(true);

    //Creating and opening a TypeSelectionDialog
    try
	{
    	SelectionDialog dialog = JavaUI.createTypeDialog(parentShell, container, scope, IJavaElementSearchConstants.CONSIDER_TYPES, false);
    	dialog.setTitle(WSUIPluginMessages.DIALOG_TITLE_INTERFACE_BROWSE);
    	dialog.setMessage(WSUIPluginMessages.DIALOG_TITLE_INTERFACE_BROWSE);    
    	dialog.open();

    	//Getting the result and returning it
    	Object[] results = dialog.getResult();
    	if (results != null && results.length > 0)
    	{
    		if (results[0] instanceof IType)
    		{
    			IType resultInterface = (IType)results[0];
    			interfaceName = resultInterface.getFullyQualifiedName();
    		}
    	}
    }
    catch (JavaModelException jme)
	{
    }
    
    return interfaceName;
  } 
}
