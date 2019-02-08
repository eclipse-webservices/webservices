/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
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
 * 20060419   132905 cbrealey@ca.ibm.com - Chris Brealey          
 *******************************************************************************/
package org.eclipse.jst.ws.internal.conformance;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.IType;

/**
 * @author cbrealey
 * This is an engine that walks a JDT model and runs rules.
 */
public interface IJavaWebServiceRuleEngine
{
	/**
	 * Analyzes the given class in the context of the given 
	 * project for compliance to a set of JAX-RPC rules.
	 * @param project The project context of the analysis.
	 * @param rootClass The IType of the class to analyze.
	 * @param rules The rules to use for the analysis.
	 * @return An IStatus summarizing the results of the analysis.
	 */
	public IStatus analyze ( IProject project, IType rootClass, JavaWebServiceRuleSet rules );
	
	/**
	 * Returns the JDTResolver in use by this analyzer.
	 * The JDTResolver is guaranteed not to be null only
	 * after the analyze method has been invoked and before
	 * the first rule is visited. This method is intended
	 * for use by implementations of IJavaWebServiceRule.
	 * @return The JDTResolver in use by this analyzer.
	 */
	public JDTResolver getJDTResolver ();
	
	/**
	 * Returns the progress monitor in use by this analyzer,
	 * or null if no progress monitor is in use.
	 * This method is intended for use by implementations
	 * of IJavaWebServiceRule.
	 * @return The progress monitor in use by this analyzer,
	 * or null for none.
	 */
	public IProgressMonitor getProgressMonitor ();
}
