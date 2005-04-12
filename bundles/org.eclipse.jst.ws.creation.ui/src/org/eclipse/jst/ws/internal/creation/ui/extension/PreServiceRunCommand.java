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

package org.eclipse.jst.ws.internal.creation.ui.extension;

import org.eclipse.jst.ws.internal.consumption.command.common.StartServerCommand;
import org.eclipse.wst.command.env.core.SimpleCommand;
import org.eclipse.wst.command.env.core.common.Environment;
import org.eclipse.wst.command.env.core.common.Status;
import org.eclipse.wst.ws.internal.provisional.wsrt.IWebService;

public class PreServiceRunCommand extends SimpleCommand 
{
	private IWebService				webService_;
	
	  public Status execute(Environment environment) 
	  {
		  System.out.println( "In Pre service run command." );
			
			StartServerCommand command = new StartServerCommand();
			command.setServerInstanceId(webService_.getWebServiceInfo().getServerInstanceId());
			Status status = command.execute(environment);
			if (status.getSeverity()==Status.ERROR)
			{
				environment.getStatusHandler().reportError(status);			  
			}			
			return status;
			
	  }
		
	  public void setWebService( IWebService webService )
	  {
		  webService_ = webService;  
	  }

}
