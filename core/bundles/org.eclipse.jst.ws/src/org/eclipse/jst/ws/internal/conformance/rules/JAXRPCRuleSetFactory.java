/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060419   132905 cbrealey@ca.ibm.com - Chris Brealey          
 *******************************************************************************/
package org.eclipse.jst.ws.internal.conformance.rules;

import org.eclipse.jst.ws.internal.conformance.JavaWebServiceRuleSet;

/**
 * A factory that produces a JavaWebServiceRuleSet
 * for the JAX-RPC 1.1 specification.
 */
public class JAXRPCRuleSetFactory
{
	private JAXRPCRuleSetFactory ()
	{
		// Not publicly constructable.
	}
	
	/**
	 * Returns a set of rules for analyzing Java classes
	 * for compliance to the rules of JAX-RPC 1.1.  
	 * @return A set of JAX-RPC 1.1 analysis rules.
	 */
	public static JavaWebServiceRuleSet newRuleSet ()
	{
		JavaWebServiceRuleSet set = new JavaWebServiceRuleSet();
		set.addRule(new JAXRPCRule0001());
		set.addRule(new JAXRPCRule0002());
		set.addRule(new JAXRPCRule0003());
		set.addRule(new JAXRPCRule0005());
		set.addRule(new JAXRPCRule0006());
		return set;
	}
}
