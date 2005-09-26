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
package org.eclipse.wst.command.internal.env.common;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.wst.command.internal.provisional.env.core.common.SimpleStatus;
import org.eclipse.wst.command.internal.provisional.env.core.common.Status;

/**
 * @author cbrealey@ca.ibm.com
 * 
 * This class contains utility methods for converting between generic
 * Environment clases and Eclipse classes.
 */
public final class EnvironmentUtils {
	/**
	 * Converts an Eclipse <code>IStatus</code> to an Environment
	 * <code>Status</code>.
	 * 
	 * @param istatus
	 *            The Eclipse status object.
	 * @return An equivalent Environment status object.
	 */
	public static Status convertIStatusToStatus(IStatus istatus) {
		Status status;
		String message = istatus.getMessage();
		IStatus[] children = istatus.getChildren();
		int noOfChildren = children.length;
		if (noOfChildren > 0) {
			Status[] statusChildren = new Status[noOfChildren];
			for (int i = 0; i < noOfChildren; i++) {
				statusChildren[i] = convertIStatusToStatus(children[i]);
			}

			status = new SimpleStatus("", message, statusChildren);
		} else {
			int severity = istatus.getSeverity();
			int statusSeverity = Status.OK;
			switch (severity) {
			case IStatus.ERROR:
				statusSeverity = Status.ERROR;
				break;
			case IStatus.WARNING:
				statusSeverity = Status.WARNING;
				break;
			case IStatus.INFO:
				statusSeverity = Status.INFO;
				break;
			case IStatus.OK:
				statusSeverity = Status.OK;
				break;
			default:
			}
			Throwable e = istatus.getException();
			status = new SimpleStatus("", message, statusSeverity, e);
		}

		return status;
	}

	/**
	 * Converts an Environment <code>Status</code> to an Eclipse
	 * <code>IStatus</code>.
	 * 
	 * @param istatus
	 *            The Environment status object.
	 * @return An equivalent Eclipse status object.
	 */
	public static IStatus convertStatusToIStatus(Status status, String pluginId) {
		IStatus istatus;
		String message = status.getMessage();
		Throwable throwable = status.getThrowable();
		if (status.hasChildren()) {
			IStatus[] children = status.getChildren();
			int noOfChildren = children.length;
			IStatus[] istatusChildren = new IStatus[noOfChildren];
			for (int i = 0; i < noOfChildren; i++) {
				istatusChildren[i] = children[i];
			}

			istatus = new MultiStatus(pluginId, 0, istatusChildren, message,
					throwable);
		} else {
			int severity = status.getSeverity();
			int istatusSeverity = IStatus.OK;
			switch (severity) {
			case Status.ERROR:
				istatusSeverity = IStatus.ERROR;
				break;
			case Status.WARNING:
				istatusSeverity = IStatus.WARNING;
				break;
			case Status.INFO:
				istatusSeverity = IStatus.INFO;
				break;
			case Status.OK:
				istatusSeverity = IStatus.OK;
				break;
			default:
			}
			istatus = new org.eclipse.core.runtime.Status(istatusSeverity,
					pluginId, 0, message, throwable);
		}

		return istatus;
	}

}