/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jst.ws.internal.ui.action;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jst.ws.internal.common.ServerUtils;

public class WebServiceDeployAction extends WindowActionDelegate {
public void run(org.eclipse.jface.action.IAction action) {

   IProject project = getSelectedResourceProject();

   String [] typeIds = ServerUtils.getServerTypeIdsByModule(project);
   Object theAction = null;

   IExtensionRegistry reg = Platform.getExtensionRegistry();
   IConfigurationElement[] elements = reg.getConfigurationElementsFor("org.eclipse.jst.ws.ui", "webservicedeploy");
   try {
   	
   	if (typeIds == null || typeIds.length <= 0)
   	{
   	 	// that means the module is not attached to a server then pick the first extension found
   	    theAction = elements[0].createExecutableExtension("class");
   	}
   	else if ( typeIds.length == 1)
   	{
   		// that means the module is attached to one server then finds it id and a matchin extension or give a message
   		for (int i = 0; i < elements.length; i++)
      	{
		   	String id = elements[i].getAttribute("factoryId");
    	   	if ( typeIds[0].equals(id))
		   	{
		   		theAction = elements[i].createExecutableExtension("class");
		   		break;
		   	}
   		}
   		if (theAction == null )
   			{
   				// give a message
   			}
   	}
   	else
   	{
		// The module is attached to more than one server find the first server-extension couple
		// TBD: add a dialog to choose the server for deployment
		for (int i = 0; i < elements.length; i++)
      	{
		   	String id = elements[i].getAttribute("factoryId");
		   	for ( int k=0; k< typeIds.length; k++)
		   	{
    	   		if ( typeIds[k].equals(id))
		   		{
		   			theAction = elements[i].createExecutableExtension("class");
		   			break;
		   		}
		   	}
		   if (theAction != null) break;
   		 }
		if (theAction == null) 
			{
				// give a Message
			}
   	}

    if (theAction instanceof WebServiceDeploy)
          ((WebServiceDeploy)theAction).run(project);   
  }

  catch (Exception e) {}
} 
}
