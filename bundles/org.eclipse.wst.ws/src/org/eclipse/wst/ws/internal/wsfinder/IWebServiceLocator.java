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
 *******************************************************************************/

package org.eclipse.wst.ws.internal.wsfinder;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;

/**
 * @author joan
 *
 * Interface for Web service locators that will be retrieved
 * by the WebServiceFinder. This interface should not be
 * implemented directly. Instead, subclasses should extend
 * AbstractWebServiceLocator and implement the
 * getWebServices() and getWebServiceClients() methods. 
 */
public interface IWebServiceLocator
{
	/**
	 * Returns a list of WebServiceClientInfo objects.
	 * This operation may be long running.
	 * @param monitor A progress monitor,
	 * or null if progress monitoring is not desired.
	 * @return A list of WebServiceClientInfo objects.
	 * possibly empty, possibly null.
	 */
	public List getWebServiceClients ( IProgressMonitor monitor );
	
	/**
	 * Returns a list of WebServiceInfo objects.
	 * This operation may be long running.
	 * @param monitor A progress monitor,
	 * or null if progress monitoring is not desired.
	 * @return A list of WebServiceInfo objects,
	 * possibly empty, possibly null.
	 */
	public List getWebServices ( IProgressMonitor monitor );
}
