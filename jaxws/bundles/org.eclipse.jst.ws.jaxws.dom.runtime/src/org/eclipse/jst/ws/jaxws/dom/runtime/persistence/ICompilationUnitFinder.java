/*******************************************************************************
 * Copyright (c) 2009 by SAP AG, Walldorf. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     SAP AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.jaxws.dom.runtime.persistence;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.WsDOMLoadCanceledException;

/**
 * Implements a visitor pattern for traversing compilation unit. This interface does not define what set of compilation
 * units will be traveresed. This could be a single java project, the whole workspace or just a given package. The set of
 * traversed compilation units is left up to the implementation. Nevertheless as these compilation units are always contained
 * inside a java project, the </c>ICompilationUnitHandler.handleProject(IJavaProject)</c> has to be called exactly once to
 * notify the handler for each project being processed. The compilation unit handler will be first notified that the processing
 * of given project has began and immediately after this it will be notified for all annotations in this project discovered by
 * the given implementation. 
 * @author Hristo Sabev
 *
 */
public interface ICompilationUnitFinder
{
	/**
	 * Starts the traversal of compilation units
	 * @param monitor - the progress monitor to be used for the traversal.
	 * @param cuHandler - compilation unit handler to be notified for a given project
	 * @throws CoreException - thrown if unexpected problem occurred while traversing annotations
	 */
	public void find(IProgressMonitor monitor, ICompilationUnitHandler cuHandler) throws CoreException, WsDOMLoadCanceledException;
}
