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

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.WsDOMLoadCanceledException;
import org.eclipse.jst.ws.jaxws.dom.runtime.internal.plugin.JaxWsDomRuntimeMessages;

public class WorkspaceCUFinder implements ICompilationUnitFinder
{
	private final IJavaModel javaModel;
	private final IProjectSelector[] selectors;
	
	public WorkspaceCUFinder(IJavaModel javaModel, IProjectSelector[] selectors) {
		if (javaModel == null) {
			throw new NullPointerException("javaModel");//$NON-NLS-1$
		}
		this.javaModel = javaModel;
		if (selectors == null) {
			throw new NullPointerException("selector");//$NON-NLS-1$
		}
		this.selectors = selectors;
	}
	
	public void find(final IProgressMonitor monitor, final ICompilationUnitHandler cuHandler) throws CoreException, WsDOMLoadCanceledException
	{
		cuHandler.started();
		if (monitor != null) {
			monitor.beginTask(JaxWsDomRuntimeMessages.JAXWS_DOM_LOADING, assumeWork());
		}		
		try {
			for (IJavaProject prj : javaModel.getJavaProjects())
			{
				if (monitor!=null && monitor.isCanceled()) {
					throw new WsDOMLoadCanceledException("JAX-WS DOM loading canceled", JaxWsDomRuntimeMessages.WorkspaceCUFinder_LOADING_CANCELED); //$NON-NLS-1$
				}
				
				if (approve(prj))
				{
					cuHandler.handle(prj);
					parseProject(prj, monitor, cuHandler);
				}
			}
			cuHandler.finished();
		} finally {
			if (monitor != null) {
				monitor.done();
			}
		}
	}
	
	protected int assumeWork() throws CoreException, WsDOMLoadCanceledException
	{
		final CountingCUHandler countingHandler = new CountingCUHandler();
		for (IJavaProject prj : javaModel.getJavaProjects()) {		
			if (approve(prj)) {
				parseProject(prj, new NullProgressMonitor(), countingHandler);
			}
		}
		
		return countingHandler.count;
	}
	
	private void parseProject(final IJavaProject prj, final IProgressMonitor monitor, final ICompilationUnitHandler cuHandler) throws JavaModelException, WsDOMLoadCanceledException 
	{
		for (IPackageFragment packageFragment : prj.getPackageFragments()) 
		{
			if (packageFragment.getKind() != IPackageFragmentRoot.K_SOURCE) {
				continue;
			}
			
			for (ICompilationUnit cu : packageFragment.getCompilationUnits()) 
			{
				if (monitor!=null && monitor.isCanceled()) {
					throw new WsDOMLoadCanceledException("JAX-WS DOM loading canceled",JaxWsDomRuntimeMessages.WorkspaceCUFinder_LOADING_CANCELED); //$NON-NLS-1$
				}
				
				cuHandler.handle(cu);
				
				if (monitor != null) {
					monitor.worked(1);
				}
			}
		}
	}
	
	private boolean approve(IJavaProject prj) {
		for (IProjectSelector selector : selectors) {
			if (selector.approve(prj)) {
				return true;
			}
		}

		return false;
	}
	
	/**
	 * This handler counts the compilation units 
	 *  
	 * @author Georgi Vachkov
	 */
	protected class CountingCUHandler implements ICompilationUnitHandler 
	{
		private int count;
		
		public void started() {
			count = 0;
		}
		
		public void handle(ICompilationUnit cu) {
			count++;
		}
		
		public void finished() { /* no processing needed */ }
		public void handle(IJavaProject jprj) { /* no processing needed */ }
	}	
}
