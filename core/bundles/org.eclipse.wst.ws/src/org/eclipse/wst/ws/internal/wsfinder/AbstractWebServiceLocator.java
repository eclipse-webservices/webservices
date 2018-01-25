/*******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060317   127456 cbrealey@ca.ibm.com - Chris Brealey
 * 20060620   147862 cbrealey@ca.ibm.com - Chris Brealey
 *******************************************************************************/

package org.eclipse.wst.ws.internal.wsfinder;

import java.util.Collections;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * @author joan
 * 
 * Implementation of IWebServiceLocator.  Extenders must implement the abstract method getWebServices().  
 * This class will be used to provide methods useful to locator implementers.  Extenders should register their
 * locator implementation with their plugin using the org.eclipse.wst.ws.locator extension point. 
 *
 */
public abstract class AbstractWebServiceLocator implements IWebServiceLocator {

	/**
	 * Creates a new instance of this class.
	 */
	public AbstractWebServiceLocator ()
	{
		super();
	}

	/**
	 * Returns a list of WebServiceClientInfo objects.
	 * Subclasses should override this method's
	 * default behaviour of returning an empty list.
	 * @param monitor A progress monitor,
	 * or null if progress monitoring is not desired.
	 * @return A non-null but possibly empty list of WebServiceClientInfo objects
	 */
	public List getWebServiceClients ( IProgressMonitor monitor )
	{
		return Collections.EMPTY_LIST;
	}

	/**
	 * Returns a list of WebServiceClientInfo objects.
	 * Subclasses should override this method's
	 * default behaviour of returning an empty list.
	 * @param projects One or more projects to which
	 * the search should be limited.
	 * @param monitor A progress monitor,
	 * or null if progress monitoring is not desired.
	 * @return A non-null but possibly empty list of WebServiceClientInfo objects
	 */
	public List getWebServiceClients ( IProject[] projects, IProgressMonitor monitor )
	{
		return Collections.EMPTY_LIST;
	}

	/**
	 * Returns a list of WebServiceInfo objects. 
	 * Subclasses should override this method's
	 * default behaviour of returning an empty list.
	 * @param monitor A progress monitor,
	 * or null if progress monitoring is not desired.
	 * @return A non-null but possibly empty list of WebServiceInfo objects
	 */
	public List getWebServices ( IProgressMonitor monitor )
	{
		return Collections.EMPTY_LIST; 
	}

	/**
	 * Returns a list of WebServiceInfo objects. 
	 * Subclasses should override this method's
	 * default behaviour of returning an empty list.
	 * @param projects One or more projects to which
	 * the search should be limited.
	 * @param monitor A progress monitor,
	 * or null if progress monitoring is not desired.
	 * @return A non-null but possibly empty list of WebServiceInfo objects
	 */
	public List getWebServices ( IProject[] projects, IProgressMonitor monitor )
	{
		return Collections.EMPTY_LIST; 
	}
}
