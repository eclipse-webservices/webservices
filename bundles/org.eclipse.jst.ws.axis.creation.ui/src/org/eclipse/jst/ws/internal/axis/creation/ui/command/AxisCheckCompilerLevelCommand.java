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
package org.eclipse.jst.ws.internal.axis.creation.ui.command;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jem.util.emf.workbench.ProjectUtilities;
import org.eclipse.jst.ws.internal.axis.creation.ui.AxisCreationUIMessages;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;

/**
 * Check to make sure that the project containing the service bean is not at a compiler compliance setting
 * that's higher than the Java VM the workbench is launched with. 
 */
public class AxisCheckCompilerLevelCommand extends AbstractDataModelOperation {

	private String serverProject_;

	/**
	 * Default CTOR
	 */
	public AxisCheckCompilerLevelCommand() {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.commands.operations.IUndoableOperation#execute(org.eclipse.core.runtime.IProgressMonitor, org.eclipse.core.runtime.IAdaptable)
	 */
	public IStatus execute(IProgressMonitor monitor, IAdaptable adaptable) {
		
		IStatus status = Status.OK_STATUS;
		String javaSpecVersion = System.getProperty("java.specification.version");
		if (javaSpecVersion != null)
		{
			IProject project = ProjectUtilities.getProject(serverProject_);
			IJavaProject javaProject = JavaCore.create(project);    
			if (javaProject != null)
			{
				String projectCompilerLevel = javaProject.getOption("org.eclipse.jdt.core.compiler.compliance", false);
				if (projectCompilerLevel == null)
				{
					projectCompilerLevel = (String)JavaCore.getDefaultOptions().get("org.eclipse.jdt.core.compiler.compliance");
				}
				if (projectCompilerLevel != null)
				{
					if (!compilerLevelsCompatible(javaSpecVersion, projectCompilerLevel)) {
						status = StatusUtils.errorStatus( NLS.bind(AxisCreationUIMessages.MSG_ERROR_COMPILER_LEVEL_NOT_COMPATIBLE, new String[] {javaSpecVersion, serverProject_, projectCompilerLevel}));
						getEnvironment().getStatusHandler().reportError(status);
					}			
				} 	
			}
		}
		return status;
	}

	/**
	 * Returns true if and only if the Java specification level
	 * of the Eclipse JRE (<code>javaSpecVersion</code> is not
	 * lower than the Java specification level of the compiler
	 * (<code>projectCompilerLevel</code>. 
	 * @param javaSpecVersion The Java Specification Version of the JRE.
	 * @param projectCompilerLevel The Java project's compiler level.
	 * @return True if the Java specification level of the project's
	 * compiler does not exceed the Java specification level of the
	 * JRE that Eclipse is running on, and false otherwise.
	 */
	private boolean compilerLevelsCompatible(String javaSpecVersion, String projectCompilerLevel) {
		return (javaSpecVersion.compareTo(projectCompilerLevel) >= 0);
	}

	/**
	 * Sets the project whose compiler level should be checked.
	 * @param serverProject The project whose compiler level should be checked.
	 */
	public void setServerProject(String serverProject) {
		serverProject_ = serverProject;
	}
}
