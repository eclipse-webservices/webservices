/*******************************************************************************
 * Copyright (c) 2009 by SAP AG, Walldorf. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     SAP AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.jaxws.utils;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;


/**
 * Convenience methods to manage status objects.
 * 
 * @author Joerg Dehmel
 */
public final class StatusUtils
{
	private StatusUtils()
	{
		// no instantiation
	}
	
	/**
	 * Finds the most severe status from a array of stati.
	 * An error is more severe than a warning, and a warning is more severe
	 * than ok.
	 */
	public static IStatus getMostSevere(IStatus[] status) {
		IStatus max= null;
		for (int i= 0; i < status.length; i++) {
			IStatus curr= status[i];
			if (curr.matches(IStatus.ERROR)) {
				return curr;
			}
			if (max == null || curr.getSeverity() > max.getSeverity()) {
				max= curr;
			}
		}
		return max;
	}
	
	/**
	 * Provides a new status object with severity IStatus.CANCEL.
	 * 
	 * @param message
	 *            status message
	 * @return the cancel status
	 */
	public static IStatus statusCancel(final String message)
	{
		return createStatus(IStatus.CANCEL, message);
	}

	/**
	 * Provides a new status object with severity IStatus.ERROR.
	 * 
	 * @param message
	 *            status message
	 * @return the error status
	 */
	public static IStatus statusError(final String message)
	{
		return createStatus(IStatus.ERROR, message);
	}

	/**
	 * Provides a new status object with severity IStatus.ERROR.
	 * 
	 * @param message
	 *            status message
	 * @param ex
	 *            exception that corresponds to the status
	 * @return the error status
	 */
	public static IStatus statusError(final String message, final Throwable ex)
	{
		return createStatus(IStatus.ERROR, message, ex);
	}

	/**
	 * Provides a new status object with severity IStatus.WARNING.
	 * 
	 * @param message
	 *            status message
	 * @return the warning status
	 */
	public static IStatus statusWarning(final String message)
	{
		return createStatus(IStatus.WARNING, message);
	}

	/**
	 * Provides a new status object with severity IStatus.WARNING.
	 * 
	 * @param message
	 *            status message
	 * @param ex
	 *            exception that corresponds to the status
	 * @return the warning status
	 */
	public static IStatus statusWarning(final String message, final Throwable ex)
	{
		return createStatus(IStatus.WARNING, message, ex);
	}

	/**
	 * Provides a new status object with severity IStatus.INFO.
	 * 
	 * @param message
	 *            status message
	 * @return the info status
	 */
	public static IStatus statusInfo(final String message)
	{
		return createStatus(IStatus.INFO, message);
	}
	
	/**
	 * Provides a new status object with severity IStatus.INFO.
	 * 
	 * @param message
	 *            status message
	 * @return the info status
	 */
	public static IStatus statusInfo(final String message, final Throwable cause)
	{
		return createStatus(IStatus.INFO, message, cause);
	}
	
	/**
	 * Provides a new status object with severity IStatus.OK.
	 * 
	 * @param message
	 *            status message
	 * @return the ok status
	 */
	public static IStatus statusOk(final String message)
	{
		return createStatus(IStatus.OK, message);
	}

	private static IStatus createStatus(final int severity, final String message)
	{
		return new Status(severity, "id", 0, message, null); //$NON-NLS-1$
	}

	private static IStatus createStatus(final int severity, final String message, final Throwable ex)
	{
		return new Status(severity, "id", 0, message, ex); //$NON-NLS-1$
	}
	
	/**
	 * Provides a new multistatus object
	 * 
	 * @param message
	 *            status message
	 * @return multistatus
	 */
	public static MultiStatus createMultiStatus(String message)
	{
		return new MultiStatus("id", 0, message, null); //$NON-NLS-1$
	}
}
