/**********************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *
 * Contributors:
 *    IBM - Initial API and implementation
 **********************************************************************/
package org.eclipse.wst.wsdl.tests.performance;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.widgets.Display;
import org.eclipse.test.performance.Dimension;
import org.eclipse.test.performance.PerformanceTestCase;
import org.eclipse.wst.validation.internal.operations.OneValidatorOperation;
import org.eclipse.wst.ws.internal.plugin.WSPlugin;
import org.eclipse.wst.ws.internal.preferences.PersistentWSIContext;

/**
 * Test for validation of 50 WSDL files in the sample workspace.
 * 
 * @author Kihup Boo, IBM
 */
public class ValidateWSDLProjectTestCase extends PerformanceTestCase {
	
	public static Test suite() {
		return new TestSuite(ValidateWSDLProjectTestCase.class, "Test");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
	    super.setUp();
		// Set the WS-I preference to ignore so WS-I validation will not be run.
		WSPlugin wsui = WSPlugin.getInstance();
		PersistentWSIContext wsicontext = wsui.getWSISSBPContext();
		wsicontext.updateWSICompliances(PersistentWSIContext.IGNORE_NON_WSI);
	}

	public void testWSDLProjectValidation() throws Exception {
		String projectDir = System.getProperty("projectDir");
		Assert.assertNotNull(projectDir);
		if (!projectDir.endsWith("/") && !projectDir.endsWith("\\"))
			projectDir = projectDir + "/";
		File dir = new File(projectDir);
		if (dir.exists() && dir.isDirectory()) {
			IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject("WSDLProject");
			project.create(null);
			project.open(null);
			copy(dir, project);
			joinBackgroundJobs();

			IWorkspaceRunnable myRunnable = new OneValidatorOperation(
					project,
					"org.eclipse.wst.wsdl.ui.internal.validation.Validator",
					true, 
					false);
			IWorkspace workspace = ResourcesPlugin.getWorkspace();

			tagAsSummary("Validate WSDL Project", new Dimension[] {Dimension.ELAPSED_PROCESS, Dimension.WORKING_SET });
			startMeasuring();
			workspace.run(myRunnable, null);
			// project.build(IncrementalProjectBuilder.CLEAN_BUILD,null);
			stopMeasuring();
			commitMeasurements();
			assertPerformance();
		} else
			fail(dir.toString());
	}

	private void copy(File src, IContainer dest) throws CoreException,
			FileNotFoundException {
		File[] children = src.listFiles();
		for (int i = 0; i < children.length; i++) {
			String name = children[i].getName();
			if (children[i].isDirectory()) {
				IFolder folder = dest.getFolder(new Path(name));
				folder.create(true, true, null);
				copy(children[i], folder);
			} else {
				IFile file = dest.getFile(new Path(name));
				file.create(new FileInputStream(children[i]), true, null);
			}
		}
	}

	private void joinBackgroundJobs() {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				try {
					Platform.getJobManager().join(
							ResourcesPlugin.FAMILY_AUTO_BUILD, null);
				} catch (InterruptedException e) {
				}
				long start = System.currentTimeMillis();
				Display display = Display.getDefault();
				while (System.currentTimeMillis() - start < 5000) {
					if (!display.readAndDispatch()) {
						display.sleep();
					}
				}
			}
		});
	}
}
