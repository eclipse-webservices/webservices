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
 * 20060607   145604 cbrealey@ca.ibm.com - Chris Brealey          
 *******************************************************************************/
package org.eclipse.jst.ws.internal.conformance;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.ws.internal.WSPluginMessages;
import org.eclipse.jst.ws.internal.conformance.rules.JAXRPCRuleSetFactory;
import org.eclipse.osgi.util.NLS;

/**
 * This class provides convenience methods for analyzing
 * compliance of Java service classes and value types to
 * the JAX-RPC 1.1 specification. Typical usage of this
 * class:
 * <code>
 * IType type = JAXRPCWebServiceAnalyzer.asType(myJavaFileResource);
 * JavaWebServiceRuleSet rules = JAXRPCWebServiceAnalyzer.getRuleSet();
 * JAXRPCWebServiceAnalyzer.getRuleEngine().analyze(type,rules,myProgressMonitor);
 * </code> 
 * @author cbrealey
 */
public class JAXRPCWebServiceAnalyzer
{
	private JavaWebServiceRuleSet ruleSet_;
	
	/**
	 * Constructs a new JAX-RPC Web service analyzer,
	 * a convenience class wrapped around a
	 * JAXRPCWebServiceAnalyzer.
	 */
	public JAXRPCWebServiceAnalyzer ()
	{
		ruleSet_ = JAXRPCRuleSetFactory.newRuleSet();
	}

	/**
	 * Returns the set of rules in use, the contents of
	 * which can be manipulated and will be respected by
	 * this analyzer. In other words, this method returns a
	 * reference to, not a copy of, the set of rules in use.
	 * @return The set of rules in use by the analyzer.
	 */
	public JavaWebServiceRuleSet getRuleSet ()
	{
		return ruleSet_;
	}
	
	/**
	 * Analyzes the Java service class represented by the given
	 * JDT type for compliance to the JAX-RPC for compliance to
	 * the JAX-RPC specification.
	 * Progress monitoring is provided by the given monitor. 
	 * @param project The project context in which the analysis
	 * should take place.
	 * @param rootClass The Java service class to analyze.
	 * @param monitor The progress monitor to use,
	 * or null if monitoring is not desired.
	 * @return A status object whose severity, message and
	 * child status objects, if any, describe any JAX-RPC
	 * rules that may be compromised by the service class.
	 * Never returns null.
	 */
	public IStatus analyze ( IProject project, IType rootClass, IProgressMonitor monitor )
	{
		IJavaWebServiceRuleEngine engine = new JAXRPCWebServiceRuleEngine(monitor);
		return engine.analyze(project,rootClass,ruleSet_);
	}

	/**
	 * Analyzes the Java service class represented by the given
	 * JDT type for compliance to the JAX-RPC for compliance to
	 * the JAX-RPC specification.
	 * Progress monitoring is provided by the given monitor. 
	 * @param project The project context in which the analysis
	 * should take place.
	 * @param rootClass The fully qualified name of the
	 * Java service class to analyze.
	 * @param monitor The progress monitor to use,
	 * or null if monitoring is not desired.
	 * @return A status object whose severity, message and
	 * child status objects, if any, describe any JAX-RPC
	 * rules that may be compromised by the service class.
	 * Never returns null.
	 */
	public IStatus analyze ( IProject project, String rootClass, IProgressMonitor monitor )
	{
		try
		{
			IType rootType = asType(rootClass,project);
			if (rootType == null)
			{
				String message = NLS.bind(WSPluginMessages.MSG_JAXRPC11_UNRESOLVED_CLASS,rootClass,project.getName());
				return new Status(IStatus.ERROR,"org.eclipse.jst.ws",0,message,null);
			}
			return analyze(project,rootType,monitor);
		}
		catch (JavaModelException e)
		{
			// An exception may be thrown from the call to asType(...),
			// not from the call to analyze(...). Exceptions within the
			// analyze(...) method are captured in the returned IStatus.
			return new Status(IStatus.ERROR,"org.eclipse.jst.ws",0,"Internal error",e);
		}
	}

	/**
	 * Analyzes the Java service class represented by the given
	 * ".java" compilation unit or ".class" file for compliance
	 * to the JAX-RPC specification.
	 * Progress monitoring is provided by the given monitor. 
	 * @param project The project context in which the analysis
	 * should take place.
	 * @param rootClass The Java service class to analyze.
	 * @param monitor The progress monitor to use,
	 * or null if monitoring is not desired.
	 * @return A status object whose severity, message and
	 * child status objects, if any, describe any JAX-RPC
	 * rules that may be compromised by the service class.
	 * Never returns null.
	 */
	public IStatus analyze ( IProject project, IFile rootClass, IProgressMonitor monitor )
	{
		try
		{
			IType rootType = asType(rootClass);
			if (rootType == null)
			{
				String message = NLS.bind(WSPluginMessages.MSG_JAXRPC11_UNRESOLVED_CLASS,rootClass.getFullPath().toString(),project.getName());
				return new Status(IStatus.ERROR,"org.eclipse.jst.ws",0,message,null);
			}
			return analyze(project,rootType,monitor);
		}
		catch (JavaModelException e)
		{
			// An exception may be thrown from the call to asType(...),
			// not from the call to analyze(...). Exceptions within the
			// analyze(...) method are captured in the returned IStatus.
			return new Status(IStatus.ERROR,"org.eclipse.jst.ws",0,"Internal error",e);
		}
	}

	/**
	 * Returns the Java type represented by the given file,
	 * or null if no such type can be clearly determined.
	 * @param file The file whose type to infer, usually a
	 * ".java" or ".class" file. For ".java" files, this
	 * method will return the primary type.
	 * @return The inferred JDT Java type.
	 * @throws JavaModelException If the given file could not
	 * mapped to a JDT IType.
	 */
	private static IType asType ( String qname, IProject project )
	throws JavaModelException
	{
		IType type = null;
		IJavaElement javaElement = JavaCore.create(project);
		if (javaElement instanceof IJavaProject)
		{
			IJavaProject javaProject = (IJavaProject)javaElement;
			type = javaProject.findType(qname);
		}
		return type;
	}
	/**
	 * Returns the Java type represented by the given file,
	 * or null if no such type can be clearly determined.
	 * @param file The file whose type to infer, usually a
	 * ".java" or ".class" file. For ".java" files, this
	 * method will return the primary type.
	 * @return The inferred JDT Java type.
	 * @throws JavaModelException If the given file could not
	 * mapped to a JDT IType.
	 */
	private static IType asType ( IFile file )
	throws JavaModelException
	{
		IType type = null;
		IJavaElement javaElement = JavaCore.create(file);
		if (javaElement instanceof ICompilationUnit)
		{
			ICompilationUnit compilationUnit = (ICompilationUnit)javaElement;
			type = compilationUnit.findPrimaryType();
		}
		else if (javaElement instanceof IClassFile)
		{
			IClassFile classFile = (IClassFile)javaElement;
			type = classFile.getType();
		}
		return type;
	}
}
