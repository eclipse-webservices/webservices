/*******************************************************************************
 * Copyright (c) 2004, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20070321   176886 pmoogk@ca.ibm.com - Peter Moogk
 *******************************************************************************/
package org.eclipse.jst.ws.tests.performance.util;

import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jst.ws.tests.util.DynamicPopupJUnitWizard;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.command.internal.env.eclipse.BaseStatusHandler;


public class PerformanceJUnitUtils {
  
	private static void launchWizard(String pluginNS,String wizardId,String objectClassId,IStructuredSelection initialSelection) throws Exception
	{
		BaseStatusHandler statusHandler = new BaseStatusHandler();

        DynamicPopupJUnitWizard wizard = new DynamicPopupJUnitWizard(statusHandler);
        wizard.setInitialData(wizardId);
        ProgressMonitorDialog monitor = new ProgressMonitorDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
        try {
        wizard.runHeadLess(initialSelection, monitor);
        } 
        catch (Exception e){
        	e.printStackTrace();
        }

	}
	
	public static void launchCreationWizard(String wizardId,String objectClassId,IStructuredSelection initialSelection) throws Exception
	{
		launchWizard("org.eclipse.jst.ws.creation.ui",wizardId,objectClassId,initialSelection);
	}
	
	public static void launchConsumptionWizard(String wizardId,String objectClassId,IStructuredSelection initialSelection) throws Exception
	{
		launchWizard("org.eclipse.jst.ws.internal.consumption.ui",wizardId,objectClassId,initialSelection);
	}
	
}
