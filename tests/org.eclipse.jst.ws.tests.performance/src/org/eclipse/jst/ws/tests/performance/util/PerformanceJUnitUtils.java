/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.tests.performance.util;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jst.ws.tests.util.AccumulateStatusHandler;
import org.eclipse.jst.ws.tests.util.DynamicPopupJUnitWizard;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;


public class PerformanceJUnitUtils {
  
	private static IStatus launchWizard(String pluginNS,String wizardId,String objectClassId,IStructuredSelection initialSelection) throws Exception
	{
		IExtension[] extensions = Platform.getExtensionRegistry().getExtensionPoint("org.eclipse.ui.popupMenus").getExtensions();
		for (int i=0;i<extensions.length;i++)
		{
			if (extensions[i].getNamespace().equals(pluginNS));
			{
				IConfigurationElement[] configElements = extensions[i].getConfigurationElements();
				for (int j=0;j<configElements.length;j++)
				{
					if (configElements[j].getAttribute("id").equals(wizardId) && configElements[j].getAttribute("objectClass").equals(objectClassId))
					{
						IConfigurationElement actionElement = configElements[j].getChildren()[0];
						AccumulateStatusHandler statusHandler = new AccumulateStatusHandler();
                        // Use this to enable per command performance measurements
                        //DynamicPopupJUnitWizard wizard = new DynamicPopupJUnitWizard(statusHandler, log);
						DynamicPopupJUnitWizard wizard = new DynamicPopupJUnitWizard(statusHandler);
						wizard.setInitializationData(actionElement,null,null);
						wizard.selectionChanged(null,initialSelection);
						wizard.run(null);
						return statusHandler.getStatus();
					}
				}
			}
		}
		return StatusUtils.errorStatus( "No wizard found for: " );
	}
	
	public static IStatus launchCreationWizard(String wizardId,String objectClassId,IStructuredSelection initialSelection) throws Exception
	{
		return launchWizard("org.eclipse.jst.ws.creation.ui",wizardId,objectClassId,initialSelection);
	}
	
	public static IStatus launchConsumptionWizard(String wizardId,String objectClassId,IStructuredSelection initialSelection) throws Exception
	{
		return launchWizard("org.eclipse.jst.ws.internal.consumption.ui",wizardId,objectClassId,initialSelection);
	}
	
}
