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
 * 20060711   149411 cbrealey@ca.ibm.com - Chris Brealey
 *******************************************************************************/
package org.eclipse.jst.ws.internal.conformance;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IField;
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
	/**
	 * Creates a new JAX-RPC analysis engine with the given
	 * progress monitor.
	 * @param progressMonitor The progress monitor for the
	 * engine to use, or null if monitoring is not desired.
	 */
	public JAXRPCWebServiceRuleEngine ( IProgressMonitor progressMonitor )
	{
		progressMonitor_ = progressMonitor;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jst.ws.internal.conformance.IJavaWebServiceRuleEngine#analyze(org.eclipse.core.resources.IProject, org.eclipse.jdt.core.IType, org.eclipse.jst.ws.internal.conformance.JavaWebServiceRuleSet, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public IStatus analyze ( IProject project, IType rootClass, JavaWebServiceRuleSet rules )
	{
		visited_ = new HashSet();
		peanutTrail_ = new Stack();		
		resolver_ = new JDTResolver(project,progressMonitor_);
		IStatus status = null;
		rules.init(this);
		try
		{
			analyzeServiceClass(rootClass,rules);
			String inCaseOfFailureMessage = NLS.bind(WSPluginMessages.MSG_JAXRPC11_NOT_COMPLIANT,rootClass.getFullyQualifiedName());
			status = rules.getResults(inCaseOfFailureMessage);
		}
		catch (JavaModelException e)
		{
			status = new Status(IStatus.ERROR,"org.eclipse.jst.ws",0,"Internal Error",e);
		}
		return status;
	}
	
	private void analyzeServiceClass ( IType type, JavaWebServiceRuleSet rules )
	throws JavaModelException
	{
		rules.visitClass(type,peanutTrail_);
		visited_.add(type.getFullyQualifiedName());
		push(type);
		try
		{
			IType[] superClasses = resolver_.getSuperClasses(type);
			IMethod[] methods = resolver_.getPublicMethods(type,superClasses);
			for (int m=0; m<methods.length; m++)
			{
				analyzeMethod(methods[m],rules);
			}
		}
		finally
		{
			pop();
		}
	}
	
	private void analyzeMethod ( IMethod method, JavaWebServiceRuleSet rules )
	throws JavaModelException
	{
		rules.visitMethod(method,peanutTrail_);
		push(method);
		try
		{
			IType returnType = resolver_.getReturnType(method);
			analyzeDataType(returnType,rules);
			IType[] parameterTypes = resolver_.getParameterTypes(method);
			for (int p=0; p<parameterTypes.length; p++)
			{
				analyzeDataType(parameterTypes[p],rules);
			}
			IType[] exceptionTypes = resolver_.getExceptionTypes(method);
			for (int e=0; e<exceptionTypes.length; e++)
			{
				analyzeExceptionType(exceptionTypes[e],rules);
			}
		}
		finally
		{
			pop();
		}
	}
	
	private void analyzeDataType ( IType type, JavaWebServiceRuleSet rules )
	throws JavaModelException
	{
		if (type != null && !resolver_.isStandardType(type.getFullyQualifiedName()) && !visited_.contains(type.getFullyQualifiedName()))
		{
			rules.visitClass(type,peanutTrail_);			
			visited_.add(type.getFullyQualifiedName());
			push(type);
			try
			{	
				IType[] superClasses = resolver_.getSuperClasses(type);
				IField[] fields = resolver_.getPublicFields(type,superClasses);
				for (int f=0; f<fields.length; f++)
				{
					analyzeField(fields[f],rules);
				}
				IJavaBeanProperty[] properties = resolver_.getPublicProperties(type,superClasses);
				for (int p=0; p<properties.length; p++)
				{
					analyzeProperty(properties[p],rules);
				}
			}
			finally
			{
				pop();
			}
		}
	}
	
	private void analyzeField ( IField field, JavaWebServiceRuleSet rules )
	throws JavaModelException
	{
		if (field != null)
		{
			rules.visitField(field,peanutTrail_);
			push(field);
			try
			{
				//TODO: Get qualified name of the field type
				//and determine if we need to build an IType
				//for it and analyze it.
				IType type = resolver_.getFieldType(field);
				analyzeDataType(type,rules);
			}
			finally
			{
				pop();
			}
		}
	}
	
	private void analyzeProperty ( IJavaBeanProperty property, JavaWebServiceRuleSet rules )
	throws JavaModelException
	{
		if (property != null)
		{
			rules.visitProperty(property,peanutTrail_);
			push(property);
			try
			{
				IType type = resolver_.getPropertyType(property);
				analyzeDataType(type,rules);
			}
			finally
			{
				pop();
			}
		}
	}

	private void analyzeExceptionType ( IType type, JavaWebServiceRuleSet rules )
	throws JavaModelException
	{
		if (type != null && !resolver_.isStandardType(type.getFullyQualifiedName()) && !visited_.contains(type.getFullyQualifiedName()))
		{
			rules.visitException(type,peanutTrail_);
			visited_.add(type.getFullyQualifiedName());
			push(type);
			try
			{
				// We trust java.lang.Exception/Throwable as superclasses
				// for service specific exception classes, therefore we do
				// not try to analyze their properties (some of which would
				// otherwise violate JAX-RPC's rules anyways).
				IType[] superClasses = resolver_.getSuperClasses(type,"java.lang.Exception");
				IJavaBeanProperty[] properties = resolver_.getPublicProperties(type,superClasses);
				for (int p=0; p<properties.length; p++)
				{
					analyzeProperty(properties[p],rules);
				}
			}
			finally
			{
				pop();
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jst.ws.internal.conformance.IJavaWebServiceRuleEngine#getJDTResolver()
	 */
	public JDTResolver getJDTResolver ()
	{
		return resolver_;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jst.ws.internal.conformance.IJavaWebServiceRuleEngine#getProgressMonitor()
	 */
	public IProgressMonitor getProgressMonitor ()
	{
		return progressMonitor_;
	}
	
	/*
	 * Pushes a peanut onto the peanut trail.
	 */
	private void push ( Object peanut )
	{
		peanutTrail_.push(peanut);
	}
	
	/*
	 * Pops a peanut off the peanut trail. 
	 */
	private void pop ()
	{
		peanutTrail_.pop();
	}
	
	/*
	 * The JDTResolver used by this analyzer.
	 */
	private JDTResolver resolver_;
	
	/*
	 * The progress monitor used by this analyzer. 
	 */
	private IProgressMonitor progressMonitor_;
	
	/*
	 * This set keeps track of the fully qualified names of
	 * classes we have visited so that we don't visit the same
	 * service class, value type or exception class more than once.
	 */
	private Set visited_;

	/*
	 * This stack keeps a trail of JDT objects back to the
	 * service class we began with. This can be useful to
	 * visitors that need to inspect the JDT elements that
	 * enclose the currently visited element.
	 */
	private Stack peanutTrail_;
}
