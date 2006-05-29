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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.ws.internal.WSPluginMessages;
import org.eclipse.jst.ws.internal.conformance.IJavaWebServiceRuleEngine;
import org.eclipse.jst.ws.internal.conformance.JavaWebServiceRule;
import org.eclipse.osgi.util.NLS;

/**
 * This rule verifies that a service class has no overloaded methods.
 */
public class JAXRPCRule0003 extends JavaWebServiceRule
{
	/**
	 * Creates a new instance of this rule.
	 */
	public JAXRPCRule0003 ()
	{
		id_ = 3;
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
		methodNames_ = new HashMap();
		overloadedMethodNames_ = new HashSet();
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
				//
				// If we have encountered this method already but haven't
				// reported the overload condition, report it.
				//
				String methodName = jdtMethod.getElementName();
				String firstParameterSignature = (String)methodNames_.get(methodName);
				String thisParameterSignature = flattenedSignatureOf(jdtMethod);
				if (thisParameterSignature != null)
				{
					if (firstParameterSignature != null)
					{
						//
						// If we have encountered this method's name already,
						// but have not yet determined if it is overloaded,
						// then compare this method's parameter signature with
						// that of the "first signature" of the method name.
						// The moment we find a second signature for a given
						// method name, we have an overload condition.
						//
						if (!overloadedMethodNames_.contains(methodName) && !thisParameterSignature.equals(firstParameterSignature))
						{
							overloadedMethodNames_.add(methodName);
							String message = NLS.bind(WSPluginMessages.MSG_JAXRPC11_RULE_0003,methodName,serviceClassName_);
							statusList_.add(new Status(IStatus.WARNING,"org.eclipse.jst.ws",0,message,null));
						}
					}
					else
					{
						//
						// If we have recorded no signature for the given method
						// name (ie. we have not encountered the method name yet),
						// then record this one. Any "first signature" will do.
						//
						methodNames_.put(methodName,thisParameterSignature);
					}
				}
			}
			catch (JavaModelException e)
			{
				// Do nothing.
			}
		}
	}
	
	/*
	 * Grabs the types of the parameters of the given method
	 * and flattens then into a single string sufficient to
	 * distinguish between overloaded vs. overridden methods.
	 */
	private String flattenedSignatureOf ( IMethod jdtMethod )
	throws JavaModelException
	{
		String[] signatures = engine_.getJDTResolver().getParameterTypeNames(jdtMethod);
		StringBuffer flattenedSignature = new StringBuffer();
		for (int s=0; s<signatures.length; s++)
		{
			flattenedSignature.append(signatures[s]).append(",");
		}
		return flattenedSignature.toString();
	}
	
	private Map methodNames_;
	private Set overloadedMethodNames_;
	private String serviceClassName_;
}
