/*******************************************************************************
 * Copyright (c) 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20120124   369472 yenlu@ca.ibm.com - Yen Lu, Intermittent publishing issues
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.command.common;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.wst.server.core.IServer.IOperationListener;

public class ServerPublishOperationListener implements IOperationListener {

	private volatile IStatus publishStatus;
	
	public ServerPublishOperationListener()
	{
		publishStatus = null;
	}
	
	public void done(IStatus result) {
		publishStatus = result;
	}
	
	public IStatus getPublishStatus()
	{
		while (publishStatus == null)
		{
			try
			{
				Thread.sleep(300);
			}
			catch (InterruptedException e)
			{					
			}
		}
			return publishStatus;
	}
}
