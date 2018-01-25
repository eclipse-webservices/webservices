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
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.ws.internal.WSPluginMessages;
import org.eclipse.jst.ws.internal.conformance.IJavaBeanProperty;
import org.eclipse.jst.ws.internal.conformance.IJavaWebServiceRuleEngine;
import org.eclipse.jst.ws.internal.conformance.JDTResolver;
import org.eclipse.jst.ws.internal.conformance.JavaWebServiceRule;
import org.eclipse.osgi.util.NLS;

/**
 * This rule verifies that the fields and properties of a
 * value type class uses only JAX-RPC supported types.
 */
public class JAXRPCRule0006 extends JavaWebServiceRule
{
	/**
	 * Creates a new instance of this rule.
	 */
	public JAXRPCRule0006 ()
	{
		id_ = 6;
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
	public void visitField ( IField jdtField, Stack peanutTrail )
	{
		//
		// A peanut trail of length greater than one implies this is a
		// field belonging to a class other than the root (aka. JAX-RPC
		// service) class, namely a presumed value type class.
		//
		if (peanutTrail.size() > 1)
		{
			try
			{
				//
				// Any Java primitive or standard ("java." or "javax.") type
				// that is not supported by JAX-RPC will get flagged.
				// Any non-primitive or non-standard type is presumed to be a
				// potential value type and is bypassed since the rule engine
				// will navigate into the potential value type's class.
				//
				JDTResolver resolver = engine_.getJDTResolver();
				String fieldTypeName = resolver.getFieldTypeName(jdtField);
				if ((resolver.isPrimitiveType(fieldTypeName) || resolver.isStandardType(fieldTypeName)) && !resolver.isJAXRPCStandardType(fieldTypeName))
				{
					//
					// The field pretty much MUST be declared in a type or
					// we would not have gotten here. Still, best to be safe.
					//
					IType valueType = jdtField.getDeclaringType();
					if (valueType != null)
					{
						String valueTypeClassName = valueType.getFullyQualifiedName();
						String fieldName = jdtField.getElementName();
						String message = NLS.bind(WSPluginMessages.MSG_JAXRPC11_RULE_0006,new String[] {fieldName,valueTypeClassName,serviceClassName_,fieldTypeName});
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
	
	/* (non-Javadoc)
	 * @see org.eclipse.jst.ws.internal.conformance.JavaWebServiceRule#visitProperty(org.eclipse.jst.ws.internal.conformance.IJavaBeanProperty, java.util.Stack)
	 */
	public void visitProperty ( IJavaBeanProperty property, Stack peanutTrail )
	{
		//
		// A peanut trail of length greater than one implies this is a
		// property belonging to a class other than the root (aka. JAX-RPC
		// service) class, namely a presumed value type class.
		//
		if (peanutTrail.size() > 1)
		{
			try
			{
				//
				// Any Java primitive or standard ("java." or "javax.") type
				// that is not supported by JAX-RPC will get flagged.
				// Any non-primitive or non-standard type is presumed to be a
				// potential value type and is bypassed since the rule engine
				// will navigate into the potential value type's class.
				//
				JDTResolver resolver = engine_.getJDTResolver();
				String propertyTypeName = resolver.getPropertyTypeName(property);
				if ((resolver.isPrimitiveType(propertyTypeName) || resolver.isStandardType(propertyTypeName)) && !resolver.isJAXRPCStandardType(propertyTypeName))
				{
					//
					// The field pretty much MUST be declared in a type or
					// we would not have gotten here. Still, best to be safe.
					//
					IType valueType = property.getDeclaringType();
					if (valueType != null)
					{
						String valueTypeClassName = valueType.getFullyQualifiedName();
						String propertyName = property.getName();
						String message = NLS.bind(WSPluginMessages.MSG_JAXRPC11_RULE_0006,new String[] {propertyName,valueTypeClassName,serviceClassName_,propertyTypeName});
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
