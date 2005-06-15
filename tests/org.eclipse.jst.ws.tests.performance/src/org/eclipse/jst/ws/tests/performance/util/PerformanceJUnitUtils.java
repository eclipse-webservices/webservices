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
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jst.ws.tests.util.AccumulateStatusHandler;
import org.eclipse.jst.ws.tests.util.DynamicPopupJUnitWizard;
import org.eclipse.wst.command.internal.provisional.env.core.common.SimpleStatus;
import org.eclipse.wst.command.internal.provisional.env.core.common.Status;


public class PerformanceJUnitUtils {
  
	private static Status launchWizard(String pluginNS,String wizardId,String objectClassId,IStructuredSelection initialSelection) throws Exception
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
                        EclipsePerformanceLog log = new EclipsePerformanceLog();
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
		return new SimpleStatus("","No wizard found for: ",Status.ERROR);
	}
	
	public static Status launchCreationWizard(String wizardId,String objectClassId,IStructuredSelection initialSelection) throws Exception
	{
		return launchWizard("org.eclipse.jst.ws.creation.ui",wizardId,objectClassId,initialSelection);
	}
	
	public static Status launchConsumptionWizard(String wizardId,String objectClassId,IStructuredSelection initialSelection) throws Exception
	{
		return launchWizard("org.eclipse.jst.ws.internal.consumption.ui",wizardId,objectClassId,initialSelection);
	}
	
}
