/*******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.wsdl.tests.performance;

import java.io.IOException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.intro.IIntroManager;
import org.eclipse.ui.intro.IIntroPart;

public class OpenStockQuoteWSDLSetup extends TestCase
{
  public static String PROJECT_NAME = "Project";

  public static Test suite()
  {
    return new TestSuite(OpenStockQuoteWSDLSetup.class, "OpenStockQuoteWSDLSetup");
  }

  protected void closeIntro()
  {
    IIntroManager introManager = PlatformUI.getWorkbench().getIntroManager();
    IIntroPart introPart = introManager.getIntro();
    if (introPart != null)
      introManager.closeIntro(introPart);
  }

  protected IProject createSimpleProject(String name) throws CoreException
  {
    IProject simpleProject = ResourcesPlugin.getWorkspace().getRoot().getProject(name);
    simpleProject.create(null);
    simpleProject.open(null);
    return simpleProject;
  }

  protected void copyFile(IProject project, String source, String dest) throws IOException, CoreException
  {
    IFile file = project.getFile(dest);
    file.create(PerformancePlugin.getDefault().getBundle().getEntry(source).openStream(), true, null);
  }

  protected void joinAutoBuild() throws CoreException
  {
    boolean interrupted = true;
    while (interrupted)
    {
      try
      {
        Platform.getJobManager().join(ResourcesPlugin.FAMILY_AUTO_BUILD, null);
        interrupted = false;
      }
      catch (InterruptedException e)
      {
        interrupted = true;
      }
    }
  }

  public void testSetup() throws Exception
  {
    closeIntro();
    IProject project = createSimpleProject(PROJECT_NAME);
    copyFile(project, "data/StockQuote/StockQuote.wsdl", "StockQuote.wsdl");
    joinAutoBuild();
  }
}
