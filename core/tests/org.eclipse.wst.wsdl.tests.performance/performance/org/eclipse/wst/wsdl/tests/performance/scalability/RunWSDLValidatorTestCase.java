/*******************************************************************************
* Copyright (c) 2006, 2008 IBM Corporation and others.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
* IBM Corporation - initial API and implementation
*******************************************************************************/ 

package org.eclipse.wst.wsdl.tests.performance.scalability;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.wst.common.tests.performance.internal.scalability.RunValidatorTestCase;
import org.eclipse.wst.common.tests.performance.internal.util.EditorTestHelper;
import org.eclipse.wst.validation.ValidationFramework;
import org.eclipse.wst.validation.Validator;
import org.eclipse.wst.validation.internal.ValManager;
import org.eclipse.wst.validation.internal.core.ValidationException;
import org.eclipse.wst.wsdl.tests.performance.PerformancePlugin;

public abstract class RunWSDLValidatorTestCase extends RunValidatorTestCase
{

  protected String getValidatorId()
  {
      return PerformancePlugin.WSDL_VALIDATOR_ID;
  }

  protected String getBundleId()
  {
      return PerformancePlugin.BUNDLE_ID;
  }


	protected void runValidator(IFile file) throws CoreException,
			ValidationException {

		waitForJobs();
		turnoffOtherValidators(getValidatorId());

		ValidationFramework vf = ValidationFramework.getDefault();
		IProject[] projects = { file.getProject() };
		try {
			// just run validation
			vf.validate(projects, true, false, new NullProgressMonitor());
		} catch (CoreException e) {
			fail(e.getMessage());
		}
	}

	private void waitForJobs() {
		try {
			EditorTestHelper.joinBackgroundActivities();
		} catch (CoreException e) {
			fail(e.getMessage());
		}
	}

	/**
	 * Since other plug-ins can add and remove validators, turn off all the ones
	 * that are not part of these tests.
	 */
	private void turnoffOtherValidators(String id) {
		Validator[] vals = ValManager.getDefault().getValidators();
		int length = vals.length;
		for (int i = 0; i < length; i++) {
			Validator v = vals[i];
			if (!v.getValidatorClassname().startsWith(id)) {
				v.setBuildValidation(false);
				v.setManualValidation(false);
			}
		}
	}
}
