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
 * 20060711   147862 cbrealey@ca.ibm.com - Chris Brealey
 * 20060711   147864 cbrealey@ca.ibm.com - Chris Brealey
 *******************************************************************************/

package org.eclipse.jst.ws.axis.consumption.core.tests.util;

public class JavaFilter implements Filter {

	public boolean accept ( Object object )
	{
		return object.toString().endsWith(".java");
	}

}
