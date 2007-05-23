/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20070314   176886 pmoogk@ca.ibm.com - Peter Moogk
 * 20070522   176943 pmoogk@ca.ibm.com - Peter Moogk
 *******************************************************************************/
package org.eclipse.wst.command.internal.env.ant;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.wst.command.internal.env.eclipse.BaseStatusHandler;
import org.eclipse.wst.common.environment.Choice;
import org.eclipse.wst.common.environment.StatusException;

public class AntStatusHandler extends BaseStatusHandler
{

	public Choice report(IStatus status, Choice[] choices) {
		// TODO 
	  checkStatus(status);
		return null;
	}

	public void report(IStatus status) throws StatusException {
		if (status.getSeverity() == IStatus.ERROR)
			reportError(status);
		else
			reportInfo(status);

	}

	public void reportError(IStatus status) {
	  checkStatus(status);
		System.err.println(status.getMessage());
	}

	public void reportInfo(IStatus status) {	
	  checkStatus(status);
		System.out.println(status.getMessage());		
	}

}
