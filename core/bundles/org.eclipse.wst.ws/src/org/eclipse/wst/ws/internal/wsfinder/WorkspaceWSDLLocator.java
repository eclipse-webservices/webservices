/*******************************************************************************
 * Copyright (c) 2005, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060317   127456 cbrealey@ca.ibm.com - Chris Brealey
 * 20060620   147862 cbrealey@ca.ibm.com - Chris Brealey
 * 20090310   242440 yenlu@ca.ibm.com - Yen Lu, Pluggable IFile to URI Converter
 *******************************************************************************/

package org.eclipse.wst.ws.internal.wsfinder;

import java.util.List;
import java.util.Vector;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.wst.ws.internal.converter.IIFile2UriConverter;
import org.eclipse.wst.ws.internal.plugin.WSPlugin;
import org.eclipse.wst.ws.internal.wsrt.WebServiceInfo;

/**
 * Extends the Web Services Finder Framework.
 * Finds all .wsdl files in the workspace.
 * Not intended to be used directly.  WorkspaceWSDLLocator registers with WebServiceLocatorRegistry 
 * and is accessed by the {@link WebServiceFinder}. 
 */

public class WorkspaceWSDLLocator extends AbstractWebServiceLocator
{

	protected List wsdlServices = null;
	private static final String PLATFORM_RES = "platform:/resource";  //$NON-NLS-1$
	private static final String WSDL_EXT = "wsdl";  //$NON-NLS-1$

	public WorkspaceWSDLLocator()
	{
		super();	
	}

	/**
	 * Returns the collection of all .wsdl files in the workspace.  Currently does not eliminate multiple 
	 * occurences of the same web service. 
	 * 
	 * TODO: add a listener to the workspace resource tree so that getWebServices doesn't always
	 * use the WSDLVisitor to walk the entire resource tree.  That should only happen once.  After
	 * that the resource tree can be monitored for modifications to .wsdl files and changes made to a cache. 
	 * 
	 * @param monitor A progress monitor, or null if progress monitoring is not desired.
	 * @return list of WebServiceInfo objects, possibly null.
	 */
	public List getWebServices (IProgressMonitor monitor)
	{	
		if (wsdlServices == null)
		{
			try
			{
				IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
				WSDLVisitor visitor = new WSDLVisitor();
				root.accept(visitor);		
				wsdlServices = visitor.getWSDL();
			}
			catch (Exception ex)
			{
				// Do nothing.
			}
		}
		return wsdlServices;		
	}

	/**
	 * Returns the collection of all .wsdl files in the given project.
	 * Currently does not eliminate multiple occurences of the same web service. 
	 * 
	 * TODO: add a listener to the workspace resource tree so that getWebServices doesn't always
	 * use the WSDLVisitor to walk the entire resource tree.  That should only happen once.  After
	 * that the resource tree can be monitored for modifications to .wsdl files and changes made to a cache. 
	 * 
	 * @param monitor A progress monitor, or null if progress monitoring is not desired.
	 * @return list of WebServiceInfo objects
	 */
	public List getWebServices (IProject[] projects, IProgressMonitor monitor)
	{
		WSDLVisitor visitor = new WSDLVisitor();
		if (projects != null)
		{
			for (int p=0; p<projects.length; p++)
			{
				try
				{
					projects[p].accept(visitor);
				}
				catch (CoreException ex)
				{
					// Do nothing.
				}
			}
		}
		return visitor.getWSDL();
	}

	/**
	 *  Uses the Visitor pattern to walk the workspace resource tree
	 */
	private class WSDLVisitor implements IResourceVisitor
	{

		private Vector wsdl = new Vector();

		/**
		 * visits every node on the resource tree stopping a file resources
		 * if file resource has extension .wsdl a WebServiceInfo object is created and added to a vector 
		 *  TODO: look at caching to eliminate duplicate web service definitions in the vector returned
		 *  TODO: add more information to the WebServiceInfo object.  Currently only the qualified filename is added.
		 */
		public boolean visit(IResource resource)
		{
			if (resource.getType() == IResource.FILE)
			{
				String wsdlURL = null;
				IIFile2UriConverter converter = WSPlugin.getInstance().getIFile2UriConverter();
				boolean allowBaseConversionOnFailure = true;
				if (converter != null)
				{
				  wsdlURL = converter.convert((IFile)resource);
				  if (wsdlURL == null)
					allowBaseConversionOnFailure = converter.allowBaseConversionOnFailure();
				}
				if (wsdlURL == null && allowBaseConversionOnFailure)
				{
				  String ext = resource.getFileExtension();
				  if (ext != null && ext.equalsIgnoreCase(WSDL_EXT))
					wsdlURL = PLATFORM_RES + resource.getFullPath().toString();
				}
				if (wsdlURL != null)
				{
				  WebServiceInfo wsInfo = new WebServiceInfo();
				  wsInfo.setWsdlURL(wsdlURL);           
				  wsdl.add(wsInfo);
				}
			}
			return true;
		}

		/**
		 * Returns a vector of WebServiceInfo objects.
		 * @return vector of WebServiceInfo objects
		 */
		public Vector getWSDL()
		{
			return wsdl;
		}
	}
}
