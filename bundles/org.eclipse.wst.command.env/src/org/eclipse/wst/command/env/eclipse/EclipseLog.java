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
package org.eclipse.wst.command.env.eclipse;

import org.eclipse.core.runtime.Platform;
import org.eclipse.wst.command.env.core.common.Log;
import org.eclipse.wst.command.env.core.common.Status;

public class EclipseLog implements Log 
{
//	private Logger logger;
	public EclipseLog() {
		/*
		logger = Logger.getLogger("org.eclipse.wst.env");
		// logger.setLogFileName("env.log");
		logger.setLevel(Level.INFO); // log all levels for now
		*/
	}
	
	/**
	 * @see org.eclipse.wst.command.env.core.common.Log#isEnabled()
	 */
	public boolean isEnabled() {
		return Platform.getPlugin("org.eclipse.wst.env").isDebugging();
	}
	
	/**
	 * @see org.eclipse.wst.command.env.core.common.Log#isEnabled(java.lang.String)
	 */
	public boolean isEnabled(String option) {
		return "true".equals(Platform.getDebugOption("org.eclipse.wst.env/trace/"
				+ option));
	}
	
	/**
	 * @see org.eclipse.wst.command.env.core.common.Log#log(int, int, java.lang.Object, java.lang.String, java.lang.Object)
	 */
	public void log(int severity, int messageNum, Object caller,
			String method, Object object) {
		/*
		if (isEnabled()) {
			switch (severity) {
				case Log.ERROR :
					if (isEnabled("error"))
						logger
								.logError(getMessageNumString(messageNum) + "E "
										+ caller + "::" + method + ": object="
										+ object);
					break;
				case Log.WARNING :
					if (isEnabled("warning"))
						logger
								.logWarning(getMessageNumString(messageNum)
										+ "W " + caller + "::" + method
										+ ": object=" + object);
					break;
				case Log.INFO :
					if (isEnabled("info"))
						logger
								.logInfo(getMessageNumString(messageNum) + "I "
										+ caller + "::" + method + ": object="
										+ object);
					break;
			}
		}
		*/
	}
	
	/**
	 * @see org.eclipse.wst.command.env.core.common.Log#log(int, int, java.lang.Object, java.lang.String, org.eclipse.wst.command.env.core.common.Status)
	 */
	public void log(int severity, int messageNum, Object caller,
			String method, Status status) {
		log(severity, messageNum, caller, method, (Object)status);
	}
	
	/**
	 * @see org.eclipse.wst.command.env.core.common.Log#log(int, int, java.lang.Object, java.lang.String, java.lang.Throwable)
	 */
	public void log(int severity, int messageNum, Object caller,
			String method, Throwable throwable) {
		// log( severity, caller, method, (Object)null );
		// throwable.printStackTrace();
		/*
		if (isEnabled()) {
			switch (severity) {
				case Log.ERROR :
					if (isEnabled("error"))
						logger.logError(getMessageNumString(messageNum) + "E "
								+ caller + "::" + method);
					logger.logError(throwable);
					break;
				case Log.WARNING :
					if (isEnabled("warning"))
						logger.logWarning(getMessageNumString(messageNum) + "W "
								+ caller + "::" + method);
					logger.logWarning(throwable);
					break;
				case Log.INFO :
					if (isEnabled("info"))
						logger.logInfo(getMessageNumString(messageNum) + "I "
								+ caller + "::" + method);
					logger.logInfo(throwable);
					break;
			}
		}
		*/
	}
	
	/**
	 * @see org.eclipse.wst.command.env.core.common.Log#log(int, java.lang.String, int, java.lang.Object, java.lang.String, java.lang.Object)
	 */
	public void log(int severity, String option, int messageNum,
			Object caller, String method, Object object) {
		/*
		if (isEnabled(option))
			logger.logInfo(getMessageNumString(messageNum) + "I " + caller
					+ "::" + method + ": object=" + object);
		*/
	}
	
	/**
	 * @see org.eclipse.wst.command.env.core.common.Log#log(int, java.lang.String, int, java.lang.Object, java.lang.String, java.lang.Throwable)
	 */
	public void log(int severity, String option, int messageNum,
			Object caller, String method, Throwable throwable) {
		/*
		if (isEnabled(option)) {
			logger.logInfo(getMessageNumString(messageNum) + "I " + caller
					+ "::" + method);
			logger.logInfo(throwable);
		}
		*/
	}
	
	/**
	 * @see org.eclipse.wst.command.env.core.common.Log#log(int, java.lang.String, int, java.lang.Object, java.lang.String, org.eclipse.wst.command.env.core.common.Status)
	 */
	public void log(int severity, String option, int messageNum,
			Object caller, String method, Status status) {
		log(severity, option, messageNum, caller, method, (Object)status);
	}
	
	private String getMessageNumString(int messageNum) {
		String messageNumString = "IWAB";
		if (messageNum > 9999 || messageNum < 0)
			messageNum = 9999; //default message number
		messageNumString += (new Integer(messageNum)).toString();
		return messageNumString;
	}
}
