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

import java.util.Stack;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.ws.internal.WSPluginMessages;
import org.eclipse.jst.ws.internal.conformance.IJavaWebServiceRuleEngine;
import org.eclipse.jst.ws.internal.conformance.JDTResolver;
import org.eclipse.jst.ws.internal.conformance.JavaWebServiceRule;
import org.eclipse.osgi.util.NLS;

/**
 * This rule verifies that the methods of a service class
 * uses only JAX-RPC supported types.
 */
public class JAXRPCRule0005 extends JavaWebServiceRule
{
	/**
	 * Creates a new instance of this rule.
	 */
	public JAXRPCRule0005 ()
	{
		id_ = 5;
		namespace_ = "http://www.eclipse.org/webtools/org.eclipse.jst.ws/jaxrpc/1.1";
		name_ = null;
		description_ = null;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jst.ws.internal.conformance.JavaWebServiceRule#init(org.eclipse.jst.ws.internal.conformance.IJavaWebServiceRuleEngine)
	 */
	public void init ( IJavaWebServiceRuleEngine engine )
	{
		super.init(engine);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jst.ws.internal.conformance.JavaWebServiceRule#visitClass(org.eclipse.jdt.core.IType, java.util.Stack)
	 */
	public void visitClass ( IType jdtClass, Stack peanutTrail )
	{
		//
		// An empty peanut trail implies this is the root
		// class, aka. JAX-RPC service class. Put aside
		// the class name in case visitMethod() needs it.
		//
		if (peanutTrail.size() == 0)
		{
			serviceClassName_ = jdtClass.getFullyQualifiedName();
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jst.ws.internal.conformance.JavaWebServiceRule#visitMethod(org.eclipse.jdt.core.IMethod, java.util.Stack)
	 */
	public void visitMethod ( IMethod jdtMethod, Stack peanutTrail )
	{
		//
		// A peanut trail of length one implies this is a method
		// belonging to the root class, aka. JAX-RPC service class.
		//
		if (peanutTrail.size() == 1)
		{
			try
			{
				String methodName = jdtMethod.getElementName();
				//
				// Check the method return type for compliance.
				// Any Java primitive or standard ("java." or "javax.") type
				// that is not supported by JAX-RPC will get flagged.
				// Any non-primitive or non-standard type is presumed to be a
				// potential value type and is bypassed since the rule engine
				// will navigate into the potential value type's class.
				//
				JDTResolver resolver = engine_.getJDTResolver();
				String returnTypeName = resolver.getReturnTypeName(jdtMethod);
				if ((resolver.isPrimitiveType(returnTypeName) || resolver.isStandardType(returnTypeName)) && !resolver.isJAXRPCStandardType(returnTypeName))
				{
					String message = NLS.bind(WSPluginMessages.MSG_JAXRPC11_RULE_0005,new String[] {methodName,serviceClassName_,returnTypeName});
					statusList_.add(new Status(IStatus.WARNING,"org.eclipse.jst.ws",0,message,null));
				}
				//
				// Check the method parameter types for compliance.
				// Same comment as above, only applied to the method's parameters.
				//
				String[] parameterTypeNames = resolver.getParameterTypeNames(jdtMethod);
				for (int p=0; p<parameterTypeNames.length; p++)
				{
					if ((resolver.isPrimitiveType(parameterTypeNames[p]) || resolver.isStandardType(parameterTypeNames[p])) && !resolver.isJAXRPCStandardType(parameterTypeNames[p]))
					{
						String message = NLS.bind(WSPluginMessages.MSG_JAXRPC11_RULE_0005,new String[] {methodName,serviceClassName_,parameterTypeNames[p]});
						statusList_.add(new Status(IStatus.WARNING,"org.eclipse.jst.ws",0,message,null));
					}
				}
			}
			catch (JavaModelException e)
			{
				// Do nothing.
			}
		}
	}
	
	private String serviceClassName_;
}
