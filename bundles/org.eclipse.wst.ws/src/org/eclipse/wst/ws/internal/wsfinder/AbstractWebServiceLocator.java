/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.ws.internal.wsfinder;

import java.util.List;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;

/**
 * @author joan
 * 
 * Implementation of IWebServiceLocator.  Extenders must implement the abstract method getWebServices().  
 * This class will be used to provide methods useful to locator implementers.  Extenders should register their
 * locator implementation with their plugin using the org.eclipse.wst.ws.locator extension point. 
 *
 */


public abstract class AbstractWebServiceLocator implements IWebServiceLocator {

	public AbstractWebServiceLocator()
	{
		super();
	}

	/**
	 * Must return a list of WebServiceInfo objects.  Method will be called by WebServiceFinder.getWebServices()
	 * 
	 * @return list of WebServiceInfo objects
	 */
	public abstract List getWebServices();
	
	/**
	 * 
	 * @return array of all projects in the workspace resource tree
	 */
	public IProject[] getWorkspaceProjects()
	{
	  IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
      return root.getProjects();
	}
	
}
