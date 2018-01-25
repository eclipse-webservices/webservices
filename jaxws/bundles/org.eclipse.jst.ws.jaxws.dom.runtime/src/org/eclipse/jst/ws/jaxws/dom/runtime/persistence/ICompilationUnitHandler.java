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

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;

/**
 * A listener for events isseud by an <c>ICompilationUnitFinder</c> instance during compilation unit traversals
 * 
 * @author Hristo Sabev
 * 
 */
public interface ICompilationUnitHandler
{
	/**
	 * called by the <c>{@link ICompilationUnitFinder}</c> instance when it has started to traverse the annotation set. All calls to <c>handle(...)</c>
	 * methods is inside a started-finished block.
	 */
	public void started();

	/**
	 * called by the <c>{@link ICompilationUnitFinder}</c> instance when it has finished to traverse the annotation set.
	 */
	public void finished();

	/**
	 * Called when the <c>{@link ICompilationUnitFinder}</c> instance found a compilation unit. This method is awlays called after a call to <c>{@link #handle(JavaProject) handle(JavaProject)}</c>.
	 * The reported compilation unit is guaranteed to be found inside the <c>JavaProject</c> reported by the last call to <c>{@link#handle(JavaProject) handle(JavaProject)}</c>.
	 * This method will be called exactly once for each compilation unit
	 * 
	 * @param cu -
	 *            the compilation unit found by the finder
	 */
	public void handle(ICompilationUnit cu);

	/**
	 * Called when the <c>{@link ICompilationUnitFinder}</c> instance found a java project. This method is always called before the <c>{@link #handle(ICompilationUnit)}</c>
	 * for any compilation unit found in this project. This method will be called exactly once for each found project.
	 * 
	 * @param jprj -
	 *            the java project found by the finder
	 */
	public void handle(IJavaProject jprj);
}
