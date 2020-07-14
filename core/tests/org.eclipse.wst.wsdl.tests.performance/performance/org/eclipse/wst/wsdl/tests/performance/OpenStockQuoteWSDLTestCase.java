/*******************************************************************************
 * Copyright (c) 2004, 2020 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.wsdl.tests.performance;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.test.performance.PerformanceTestCase;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

public class OpenStockQuoteWSDLTestCase extends PerformanceTestCase
{
  public static Test suite()
  {
    return new TestSuite(OpenStockQuoteWSDLTestCase.class, "OpenStockQuoteWSDLTestCase");
  }

  protected IProject getProject(String projectName) throws CoreException
  {
    IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
    assertTrue(project.exists());
    project.open(null);
    project.refreshLocal(IProject.DEPTH_INFINITE, null);
    joinAutoBuild();
    return project;
  }

  protected void joinAutoBuild() throws CoreException
  {
    boolean interrupted = true;
    while (interrupted)
    {
      try
      {
        Job.getJobManager().join(ResourcesPlugin.FAMILY_AUTO_BUILD, null);
        interrupted = false;
      }
      catch (InterruptedException e)
      {
        interrupted = true;
      }
    }
  }

  protected IEditorPart openEditor(IEditorInput editorInput, String editorid) throws PartInitException
  {
    IWorkbenchWindow workbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
    return workbenchWindow.getActivePage().openEditor(editorInput, editorid, true);
  }

  protected boolean closeEditor(IEditorPart editor)
  {
    IWorkbenchWindow workbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
    return workbenchWindow.getActivePage().closeEditor(editor, false);
  }

  public void testOpenStockQuoteWSDL() throws Exception
  {
    IProject project = getProject(OpenStockQuoteWSDLSetup.PROJECT_NAME);
    IEditorInput editorInput = new FileEditorInput((IFile)project.findMember("StockQuote.wsdl"));
    startMeasuring();
    IEditorPart editorPart = openEditor(editorInput, PerformancePlugin.WSDL_EDITOR_ID);
    stopMeasuring();
    commitMeasurements();
    assertPerformance();
    closeEditor(editorPart);
  }
}
