/*******************************************************************************
 * Copyright (c) 2009 by SAP AG, Walldorf. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     SAP AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.jaxws.dom.runtime.persistence;

import org.eclipse.jdt.core.IJavaProject;

/**
 * Specifies what java projects will be traversed by a given <c>ICompilationUnitFinder</c> instance. If any of the registered
 * selectors approved the project i.e. <c>IProjectSelector.approve(IJavaProject)</c> returned true, then this project will
 * be traversed. I.e. the project needs to be rejected by all the selectors in order not to be traversed.
 * 
 * An implementation of this interface should reject a project that it has no knowledge about.
 * @author Hristo Sabev
 *
 */
public interface IProjectSelector
{
	/**
	 * Approves the specified project for traversal
	 * @param prj - the project to approve or reject
	 * @return - true if the project should be traversed according to this selector, false otherwise.
	 */
	public boolean approve(IJavaProject prj);
}
