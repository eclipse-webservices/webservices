/*******************************************************************************
 * Copyright (c) 2009 by SAP AG, Walldorf. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     SAP AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.jaxws.utils.logging;

import org.eclipse.core.runtime.ILog;
import org.eclipse.jst.ws.jaxws.utils.StatusUtils;
import org.eclipse.jst.ws.jaxws.utils.internal.plugin.JaxwsUtilsPlugin;

public class Logger implements ILogger
{
	public void logDebug(String message)
	{
//		log().log(StatusUtils.statusInfo(message));
	}

	public void logDebug(String message, Throwable cause)
	{
//		log().log(StatusUtils.statusInfo(message, cause));
	}

	public void logError(String message)
	{
		log().log(StatusUtils.statusError(message));
	}

	public void logError(String message, Throwable cause)
	{
		log().log(StatusUtils.statusError(message, cause));
	}

	public void logWarn(String message)
	{
		log().log(StatusUtils.statusWarning(message));
	}

	public void logWarn(String message, Throwable cause)
	{
		log().log(StatusUtils.statusWarning(message, cause));
	}

	private ILog log()
	{
		return JaxwsUtilsPlugin.getDefault().getLog();
	}

	public boolean isDebug()
	{
		// TODO Auto-generated method stub
		return false;
	}
}
