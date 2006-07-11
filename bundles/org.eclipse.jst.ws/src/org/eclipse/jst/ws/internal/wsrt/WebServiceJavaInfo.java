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
 * 20060620   147864 cbrealey@ca.ibm.com - Chris Brealey
 *******************************************************************************/

package org.eclipse.jst.ws.internal.wsrt;

import org.eclipse.jdt.core.IType;
import org.eclipse.wst.ws.internal.wsrt.WebServiceInfo;

/**
 * This subclass of WebServiceInfo adds properties
 * relevant to descriptors of Java Web services.
 */
public class WebServiceJavaInfo extends WebServiceInfo
{
	private IType jdtType_;
	
	/**
	 * Returns the JDT IType of the primary Java implementation
	 * class of a Web service, or null if no such class has
	 * been located.
	 * @return The JDT IType of the Java implementation class.
	 */
	public IType getType ()
	{
		return jdtType_;
	}
	
	/**
	 * Sets the JDT IType of the primary Java implementation
	 * class of a Web service.
	 * @param jdtType The JDT IType of the Java implementation class.
	 */
	public void setType ( IType jdtType )
	{
		jdtType_ = jdtType;
	}
}
