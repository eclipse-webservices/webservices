/*******************************************************************************
 * Copyright (c) 2007 WSO2 Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * WSO2 Inc. - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20070125  168762 sandakith@wso2.com - Lahiru Sandakith, Initial code to introduse the Axis2 
 * 										  runtime to the framework for 168762
 *******************************************************************************/
package org.eclipse.jst.ws.axis2.creation.core.command;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;

public class Axis2ChengeBuildPathCommand extends AbstractDataModelOperation {
	
	private IProject project;
	private String srcDir;
	private String outDir;
	
	public Axis2ChengeBuildPathCommand(IProject project, String srcDir, String outDir) {
		this.project = project;
		this.srcDir = srcDir;
		this.outDir = outDir;
	}

	public IStatus execute(IProgressMonitor monitor, IAdaptable info)
				   throws ExecutionException {
		
		IPath srcPath = new Path(srcDir);
		IPath outPath = new Path(outDir);
		
		IStatus status = Status.OK_STATUS;
		final IJavaProject jproj = JavaCore.create(project);
        final IClasspathEntry[] cp = {JavaCore.newSourceEntry(srcPath)};

        try {
			jproj.setRawClasspath(cp, outPath, null);
			jproj.save(null, true);
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		return status;
	}

}
