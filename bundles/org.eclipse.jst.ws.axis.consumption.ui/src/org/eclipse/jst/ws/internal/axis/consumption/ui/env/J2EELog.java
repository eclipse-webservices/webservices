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
package org.eclipse.jst.ws.internal.axis.consumption.ui.env;

import org.eclipse.wst.command.internal.provisional.env.core.common.Log;
import org.eclipse.wst.command.internal.provisional.env.core.common.Status;

/**
*
*/
public class J2EELog implements Log
{
	/* (non-Javadoc)
	 * @see org.eclipse.wst.command.internal.provisional.env.core.common.Log#isEnabled()
	 */
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.wst.command.internal.provisional.env.core.common.Log#isEnabled(java.lang.String)
	 */
	public boolean isEnabled(String option) {
		// TODO Auto-generated method stub
		return false;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.wst.command.internal.provisional.env.core.common.Log#log(int, int, java.lang.Object, java.lang.String, java.lang.Object)
	 */
	public void log(int severity, int messageNum, Object caller, String method,
			Object object) {
		// TODO Auto-generated method stub
	}
	/* (non-Javadoc)
	 * @see org.eclipse.wst.command.internal.provisional.env.core.common.Log#log(int, int, java.lang.Object, java.lang.String, org.eclipse.wst.command.internal.provisional.env.core.common.Status)
	 */
	public void log(int severity, int messageNum, Object caller, String method,
			Status status) {
		// TODO Auto-generated method stub
	}
	/* (non-Javadoc)
	 * @see org.eclipse.wst.command.internal.provisional.env.core.common.Log#log(int, int, java.lang.Object, java.lang.String, java.lang.Throwable)
	 */
	public void log(int severity, int messageNum, Object caller, String method,
			Throwable throwable) {
		// TODO Auto-generated method stub
	}
	/* (non-Javadoc)
	 * @see org.eclipse.wst.command.internal.provisional.env.core.common.Log#log(int, java.lang.String, int, java.lang.Object, java.lang.String, java.lang.Object)
	 */
	public void log(int severity, String option, int messageNum, Object caller,
			String method, Object object) {
		// TODO Auto-generated method stub
	}
	/* (non-Javadoc)
	 * @see org.eclipse.wst.command.internal.provisional.env.core.common.Log#log(int, java.lang.String, int, java.lang.Object, java.lang.String, org.eclipse.wst.command.internal.provisional.env.core.common.Status)
	 */
	public void log(int severity, String option, int messageNum, Object caller,
			String method, Status status) {
		// TODO Auto-generated method stub
	}
	/* (non-Javadoc)
	 * @see org.eclipse.wst.command.internal.provisional.env.core.common.Log#log(int, java.lang.String, int, java.lang.Object, java.lang.String, java.lang.Throwable)
	 */
	public void log(int severity, String option, int messageNum, Object caller,
			String method, Throwable throwable) {
		// TODO Auto-generated method stub
	}
}
