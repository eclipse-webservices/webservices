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
public class JAXRPCRule0002 extends JavaWebServiceRule
{
	/**
	 * Creates a new instance of this rule.
	 */
	public JAXRPCRule0002 ()
	{
		id_ = 2;
		namespace_ = "http://www.eclipse.org/webtools/org.eclipse.jst.ws/jaxrpc/1.1";
		name_ = null;
		description_ = null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jst.ws.internal.conformance.JavaWebServiceRule#visitClass(org.eclipse.jdt.core.IType, java.util.Stack)
	 */
	public void visitClass ( IType jdtClass, Stack peanutTrail )
	{
		// An empty peanut trail implies this is the root class
		// root class or JAX-RPC service class, whereas a non-empty
		// peanut trail implies this is a class from a method, field
		// or property signature. In the former case, remember the
		// qualified name of the type just in case we need it for the
		// latter case.
		if (peanutTrail.size() == 0)
		{
			serviceClassName_ = jdtClass.getFullyQualifiedName();
		}
		else
		{
			try
			{
				JDTResolver resolver = engine_.getJDTResolver();
				String qname = jdtClass.getFullyQualifiedName();
				if (!resolver.isPrimitiveType(jdtClass) && !qname.startsWith("java.") && !qname.startsWith("javax."))
				{ 
					if (!resolver.isConstructable(jdtClass) || resolver.isInterface(jdtClass) || resolver.isAbstract(jdtClass))
					{
						String message = NLS.bind(WSPluginMessages.MSG_JAXRPC11_RULE_0002,qname,serviceClassName_);
						statusList_.add(new Status(IStatus.WARNING,"org.eclipse.jst.ws",0,message,null));
					}
				}
			}
			catch (JavaModelException e)
			{
				statusList_.add(new Status(IStatus.ERROR,"org.eclipse.jst.ws",0,"Internal error",e));
			}
		}
	}
	
	private String serviceClassName_;
}
