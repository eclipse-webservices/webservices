/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060317   127456 cbrealey@ca.ibm.com - Chris Brealey
 *******************************************************************************/

package org.eclipse.jst.ws.axis.consumption.core.tests.util;

import org.eclipse.core.runtime.NullProgressMonitor;

public class StdoutProgressMonitor extends NullProgressMonitor
{

	public void beginTask(String name, int totalWork)
	{
		System.out.println("[Progress] "+name+", "+totalWork+" unit(s) remaining).");
	}

	public void done()
	{
		System.out.println("[Progress] Done.");
	}

	public void setTaskName(String name)
	{
		System.out.println("[Progress] "+name);
	}

	public void subTask(String name)
	{
		System.out.println("[Progress] "+name);
	}

	public void worked(int work)
	{
		System.out.println("[Progress] "+work+" unit(s) completed.");
	}
	
}
