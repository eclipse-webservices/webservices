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
package org.eclipse.jst.ws.internal.conformance;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.ws.internal.WSPluginMessages;
import org.eclipse.osgi.util.NLS;

/**
 * @author cbrealey
 * This engine navigated JDT classes according to a JAX-RPC
 * interpretation. The root class is considered to be a
 * candidate JAX-RPC service class, and any non-standard
 * or non-primitive types used by the methods of the service
 * class are considered to be candidates for JAX-RPC value
 * type classes. 
 */
public class JAXRPCWebServiceRuleEngine implements IJavaWebServiceRuleEngine
{
	// This set keeps track of the fully qualified names of
	// classes we have visited so that we don't visit the same
	// service class, value type class or exception class more
	// than once.
	//
	private Set visited_;
	
	// This stack keeps a trail of JDT objects back to the
	// service class we began with. This can be useful to
	// visitors that need to inspect the JDT elements that
	// enclose the currently visited element.
	private Stack peanutTrail_;
	
	/* (non-Javadoc)
	 * @see org.eclipse.jst.ws.internal.conformance.IJavaWebServiceRuleEngine#analyze(org.eclipse.core.resources.IProject, org.eclipse.jdt.core.IType, org.eclipse.jst.ws.internal.conformance.JavaWebServiceRuleSet, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public IStatus analyze ( IProject project, IType rootClass, JavaWebServiceRuleSet rules, IProgressMonitor monitor )
	{
		visited_ = new HashSet();
		peanutTrail_ = new Stack();		
		resolver_ = new JDTResolver(project,monitor);
		IStatus status = null;
		try
		{
			rules.init(this);
			rules.visitClass(rootClass,peanutTrail_);
			visited_.add(rootClass.getFullyQualifiedName());
			peanutTrail_.push(rootClass);
			IType[] superClasses = resolver_.getSuperClasses(rootClass);
			IMethod[] methods = resolver_.getPublicMethods(rootClass,superClasses);
			for (int m=0; m<methods.length; m++)
			{
				rules.visitMethod(methods[m],peanutTrail_);
				
				IType returnType = resolver_.getReturnType(methods[m]);
				//TODO: Visit the return type.

				IType[] parameterTypes = resolver_.getParameterTypes(methods[m]);
				for (int p=0; p<parameterTypes.length; p++)
				{
					//TODO: Visit each parameter type.
				}
				
				IType[] exceptionTypes = resolver_.getExceptionTypes(methods[m]);
				for (int e=0; e<exceptionTypes.length; e++)
				{
					//TODO: Visit each exception type.
				}
			}
			String inCaseOfFailureMessage = NLS.bind(WSPluginMessages.MSG_JAXRPC11_NOT_COMPLIANT,rootClass.getFullyQualifiedName());
			status = rules.getResults(inCaseOfFailureMessage);

		}
		catch (JavaModelException e)
		{
			status = new Status(IStatus.ERROR,"org.eclipse.jst.ws",0,e.getMessage(),e);
		}
		return status;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jst.ws.internal.conformance.IJavaWebServiceRuleEngine#getJDTResolver()
	 */
	public JDTResolver getJDTResolver ()
	{
		return resolver_;
	}
	
	/*
	 * The JDTResolver. 
	 */
	private JDTResolver resolver_;
}
