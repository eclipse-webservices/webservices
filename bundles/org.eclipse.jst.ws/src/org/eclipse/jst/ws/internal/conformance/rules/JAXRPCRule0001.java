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
 * 20060419   132905 cbrealey@ca.ibm.com - Chris Brealey          
 *******************************************************************************/
package org.eclipse.jst.ws.internal.conformance.rules;

import java.util.Stack;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.ws.internal.WSPluginMessages;
import org.eclipse.jst.ws.internal.conformance.JDTResolver;
import org.eclipse.jst.ws.internal.conformance.JavaWebServiceRule;
import org.eclipse.osgi.util.NLS;

/**
 * This rule checks if a service class
 * is public default constructable.
 */
public class JAXRPCRule0001 extends JavaWebServiceRule
{
	/**
	 * Creates a new instance of this rule.
	 */
	public JAXRPCRule0001 ()
	{
		id_ = 1;
		namespace_ = "http://www.eclipse.org/webtools/org.eclipse.jst.ws/jaxrpc/1.1";
		name_ = null;
		description_ = null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jst.ws.internal.conformance.JavaWebServiceRule#visitClass(org.eclipse.jdt.core.IType, java.util.Stack)
	 */
	public void visitClass ( IType jdtClass, Stack peanutTrail )
	{
		try
		{
			JDTResolver resolver = engine_.getJDTResolver();
			if (!resolver.isConstructable(jdtClass) || resolver.isInterface(jdtClass) || resolver.isAbstract(jdtClass))
			{
				String message = NLS.bind(WSPluginMessages.MSG_JAXRPC11_RULE_0001,jdtClass.getFullyQualifiedName());
				statusList_.add(new Status(IStatus.WARNING,"org.eclipse.jst.ws",0,message,null));
			}
		}
		catch (JavaModelException e)
		{
			statusList_.add(new Status(IStatus.ERROR,"org.eclipse.jst.ws",0,"Internal error",e));
		}
	}
}
