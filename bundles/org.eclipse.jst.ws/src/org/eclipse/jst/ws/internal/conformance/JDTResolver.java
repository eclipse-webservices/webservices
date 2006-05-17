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

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;

/**
 * @author cbrealey
 * A JDTResolver provides several convenience methods for
 * navigating references between Java classes, methods,
 * fields and properties. A JDTResolver is constructed
 * for a Java project whose effective classpath will
 * govern the scope of search operations performed by the
 * JDTResolver whenever it needs to convert a signature
 * into a JDT IType object.
 */
public class JDTResolver
{
	/**
	 * Creates a new JDTResolver for the given project.
	 * @param project The project providing the context
	 * @param monitor The progress monitor for this resolver
	 * to use, or null if progress monitoring is not desired.
	 * within which this resolver will search for classes.
	 */
	public JDTResolver ( IProject project, IProgressMonitor monitor )
	{
		javaProject_ = JavaCore.create(project);
		monitor_ = monitor;
	}
	
	/**
	 * Returns true if and only if the given JDT IType is an interface.
	 * @param jdtType The type to analyze.
	 * @return True if and only if the given JDT IType is an interface.
	 */
	public boolean isInterface ( IType jdtType )
	throws JavaModelException
	{
		return jdtType.isInterface();
	}
	
	/**
	 * Returns true if and only if the given JDT IType is an abstract class.
	 * @param jdtType The type to analyze.
	 * @return True if and only if the given JDT IType is an abstract class.
	 */
	public boolean isAbstract ( IType jdtType )
	throws JavaModelException
	{
		return jdtType.isInterface();
		//TODO: There's more to it than this.
	}
	
	/**
	 * Returns true if and only if instances of the given JDT IType
	 * can be instantiated via a public default constructor. The class
	 * must have an explicit public default constructor, or have no
	 * explicit constructors at all to meet this criteria.
	 * @param jdtType The type to analyze.
	 * @return True if and only if instances of the type are
	 * public default constructable.
	 */
	public boolean isConstructable ( IType jdtType )
	throws JavaModelException
	{
		IMethod[] methods = jdtType.getMethods();
		int numberOfConstructors = 0;
		for (int m=0; m<methods.length; m++)
		{
			if (methods[m].isConstructor())
			{
				numberOfConstructors++;
				if (methods[m].getNumberOfParameters() == 0 && Flags.isPublic(methods[m].getFlags()))
				{
					return true;
				}
			}
		}
		return numberOfConstructors == 0;
	}
	
	/**
	 * Returns true if and only if the given JDT IType
	 * is one of the Java primitive types supported by JAX-RPC.
	 * @param jdtType The type to analyze.
	 * @return True if and only if the type is a Java primitive
	 * type supported by JAX-RPC.
	 */
	public boolean isPrimitiveType ( IType jdtType )
	throws JavaModelException
	{
		if (primitiveTypeSet_ == null)
		{
			primitiveTypeSet_ = new HashSet();
			primitiveTypeSet_.addAll(java.util.Arrays.asList(primitiveTypes_));
		}
		return primitiveTypeSet_.contains(jdtType.getFullyQualifiedName());
	}
	
	/**
	 * Returns true if and only if the given JDT IType
	 * is one of the Java wrapper types supported by JAX-RPC. 
	 * @param jdtType The type to analyze.
	 * @return True if and only if the type is a Java wrapper
	 * type supported by JAX-RPC.
	 */
	public boolean isWrapperType ( IType jdtType )
	throws JavaModelException
	{
		if (wrapperTypeSet_ == null)
		{
			wrapperTypeSet_ = new HashSet();
			wrapperTypeSet_.addAll(java.util.Arrays.asList(wrapperTypes_));
		}
		return wrapperTypeSet_.contains(jdtType.getFullyQualifiedName());
	}
	
	/**
	 * Returns true if and only if the given JDT IType
	 * is one of the Java standard types supported by JAX-RPC. 
	 * @param jdtType The type to analyze.
	 * @return True if and only if the type is a Java standard
	 * type supported by JAX-RPC.
	 */
	public boolean isSupportedType ( IType jdtType )
	throws JavaModelException
	{
		if (jaxrpcTypeSet_ == null)
		{
			jaxrpcTypeSet_ = new HashSet();
			jaxrpcTypeSet_.addAll(java.util.Arrays.asList(jaxrpcTypes_));
		}
		return jaxrpcTypeSet_.contains(jdtType.getFullyQualifiedName());
	}
	
	/**
	 * Returns an array of zero or more JDT IType objects
	 * for the public fields belonging to the given type
	 * @param jdtType The type to analyze.
	 * @param jdtSuperTypes Any supertypes to analyze,
	 * or null to analyze only the <code>jdtType</code>.
	 * @return An array of zero or more public field types.
	 * @throws JavaModelException If the JDT engine fails to
	 * analyze the given types to satisfy this request.
	 */
	public IField[] getPublicFields ( IType jdtType, IType[] jdtSuperClasses )
	throws JavaModelException
	{
		List fieldsList = new LinkedList();
		harvestPublicFields(jdtType,fieldsList);
		if (jdtSuperClasses != null)
		{
			for (int t=0; t<jdtSuperClasses.length; t++)
			{
				harvestPublicFields(jdtSuperClasses[t],fieldsList);
			}
		}
		return (IField[])fieldsList.toArray(new IField[0]);
	}
	
	/**
	 * Returns an array of zero or more JDT IMethod objects
	 * for the public methods belonging to the given type.
	 * @param jdtType The type to analyze.
	 * @param jdtSuperTypes Any supertypes to analyze,
	 * or null to analyze only the <code>jdtType</code>.
	 * @return An array of zero or more public methods.
	 * @throws JavaModelException If the JDT engine fails to
	 * analyze the given types to satisfy this request.
	 */
	public IMethod[] getPublicMethods ( IType jdtType, IType[] jdtSuperClasses )
	throws JavaModelException
	{
		List methodsList = new LinkedList();
		harvestPublicMethods(jdtType,methodsList);
		if (jdtSuperClasses != null)
		{
			for (int t=0; t<jdtSuperClasses.length; t++)
			{
				harvestPublicMethods(jdtSuperClasses[t],methodsList);
			}
		}
		return (IMethod[])methodsList.toArray(new IMethod[0]);
	}
	
	/**
	 * Returns an array of zero or more JDT IType objects
	 * for the public properties (getters and/or setters)
	 * belonging to the given type.
	 * @param jdtType The type to analyze.
	 * @param jdtSuperTypes Any supertypes to analyze,
	 * or null to analyze only the <code>jdtType</code>.
	 * @return An array of zero or more public property types.
	 * @throws JavaModelException If the JDT engine fails to
	 * analyze the given types to satisfy this request.
	 */
	public IJavaBeanProperty[] getPublicProperties ( IType jdtType, IType[] jdtSuperClasses )
	throws JavaModelException
	{
		IMethod[] methods = getPublicMethods(jdtType,jdtSuperClasses);
		Map properties = new HashMap();
		for (int m=0; m<methods.length; m++)
		{
			String name = getGetterName(methods[m]);
			if (name != null)
			{
				JavaBeanProperty property = (JavaBeanProperty)properties.get(name);
				if (property == null)
				{
					property = new JavaBeanProperty();
					property.setName(name);
					properties.put(name,property);
				}
				property.setGetter(methods[m]);
			}
			else
			{
				name = getSetterName(methods[m]);
				if (name != null)
				{
					JavaBeanProperty property = (JavaBeanProperty)properties.get(name);
					if (property == null)
					{
						property = new JavaBeanProperty();
						property.setName(name);
						properties.put(name,property);
					}
					property.setSetter(methods[m]);
				}
			}
		}
		return (IJavaBeanProperty[])properties.values().toArray(new JavaBeanProperty[0]);
	}
	
	/**
	 * Returns the JDT IType object for the given field.
	 * @param jdtField The field to analyze.
	 * @return The field type.
	 * @throws JavaModelException If the JDT engine fails to
	 * analyze the given field to satisfy this request.
	 */
	public IType getFieldType ( IField jdtField )
	throws JavaModelException
	{
		String signature = jdtField.getTypeSignature();
		String typeName = getTypeNameFromSignature(signature);
		return findType(typeName);
	}
	
	/**
	 * Returns the JDT IType object for the return type
	 * of the given method, or null if the method is void.
	 * @param jdtMethod The method to analyze.
	 * @return The method return type, or null if none.
	 * @throws JavaModelException If the JDT engine fails to
	 * analyze the given method to satisfy this request.
	 */
	public IType getReturnType ( IMethod jdtMethod )
	throws JavaModelException
	{
		String signature = jdtMethod.getReturnType();
		String typeName = getTypeNameFromSignature(signature);
		return findType(typeName);
	}
	
	/**
	 * Returns an array of zero or more JDT IType objects
	 * for the parameters of the given method.
	 * @param jdtMethod The method to analyze.
	 * @return An array of zero or more parameter types.
	 * @throws JavaModelException If the JDT engine fails to
	 * analyze the given method to satisfy this request.
	 */
	public IType[] getParameterTypes ( IMethod jdtMethod )
	throws JavaModelException
	{
		String[] signatures = jdtMethod.getParameterTypes();
		IType[] types = new IType[signatures.length];
		for (int s=0; s<signatures.length; s++)
		{
			String typeName = getTypeNameFromSignature(signatures[s]);
			types[s] = findType(typeName);
		}
		return types;
	}
	
	/**
	 * Returns an array of zero or more JDT IType objects
	 * for the exceptions thrown by the given method.
	 * @param jdtMethod The method to analyze.
	 * @return An array of zero or more exception types.
	 * @throws JavaModelException If the JDT engine fails to
	 * analyze the given method to satisfy this request.
	 */
	public IType[] getExceptionTypes ( IMethod jdtMethod )
	throws JavaModelException
	{
		String[] signatures = jdtMethod.getExceptionTypes();
		IType[] types = new IType[signatures.length];
		for (int s=0; s<signatures.length; s++)
		{
			String typeName = getTypeNameFromSignature(signatures[s]);
			types[s] = findType(typeName);
		}
		return types;
	}
	
	/**
	 * Returns an array of zero or more types representing
	 * the superclasses, if any, of the given IType.
	 * @param jdtType The type to analyze.
	 * @param jdtSuperTypes Any supertypes to analyze,
	 * or null to analyze only the <code>jdtType</code>.
	 * @return An array of zero or more superclass types.
	 * @throws JavaModelException If the JDT engine fails to
	 * analyze the given type to satisfy this request.
	 */
	public IType[] getSuperClasses ( IType jdtType )
	throws JavaModelException
	{
		ITypeHierarchy hierarchy = jdtType.newSupertypeHierarchy(monitor_);
		IType[] superClasses = hierarchy.getAllSuperclasses(jdtType);
		int n = superClasses.length - 1;
		if (n >= 0 && superClasses[n].getFullyQualifiedName().equals("java.lang.Object"))
		{
			IType[] superClassesExcludingObject = new IType[n];
			for (int i=0; i<n; i++)
			{
				superClassesExcludingObject[i] = superClasses[i];
			}
			return superClassesExcludingObject;
		}
		return superClasses;
	}
	
	/**
	 * Extracts the qualified type name from a JDT signature.
	 * @param signature The signature to examine.
	 * @return The qualified type name from the signature.
	 */
	public String getTypeNameFromSignature ( String signature )
	{
		String packageName = Signature.getSignatureQualifier(signature);
		String baseName = Signature.getSignatureSimpleName(signature);
		String typeName = (packageName.trim().equals("")?"":packageName+".") + baseName;
		return typeName;
	}
	
	/**
	 * Attempts to find a JDT IType object for the given
	 * fully qualified type name.
	 * @param typeName The qualified name of the type to find.
	 * @return The IType object of the given qualified name,
	 * or null if no type could be found in the workspace.
	 */
	public IType findType ( String typeName )
	throws JavaModelException
	{
		IType type = javaProject_.findType(typeName);
		return type;
		/*
		SearchEngine engine = new SearchEngine();
		IJavaSearchScope scope = SearchEngine.createJavaSearchScope(new IJavaElement[] {javaProject_},true);
		SearchPattern pattern = SearchPattern.createPattern(
				"*",
				IJavaSearchConstants.CLASS_AND_INTERFACE,
				IJavaSearchConstants.DECLARATIONS,
				SearchPattern.R_PATTERN_MATCH
				);
		SearchParticipant[] participants = new SearchParticipant[] {SearchEngine.getDefaultSearchParticipant()};
		final IType[] type = new IType[1];
		SearchRequestor requestor = new SearchRequestor()
		{
			public void acceptSearchMatch ( SearchMatch match )
			{
				Object element = match.getElement();
				if (element != null && element instanceof IType)
				{
					type[0] = (IType)element;
				}
			}
		};
		try
		{
			engine.search(pattern,participants,scope,requestor,monitor_);
		}
		catch (CoreException e)
		{
			throw new JavaModelException(e);
		}
		return type[0];
		*/
	}

	/**
	 * Finds all public, non-constructor IMethods belonging
	 * to the given IType and adds them to the given list.
	 * @param jdtType The type whose methods are to be harvested.
	 * @param list The list to which the harvested methods will be added.
	 * @throws JavaModelException If the JDT engine fails to
	 * analyze the given type to satisfy this request.
	 */
	public void harvestPublicMethods ( IType jdtType, List list )
	throws JavaModelException
	{
		IMethod[] methods = jdtType.getMethods();
		for (int m=0; m<methods.length; m++)
		{
			if (!methods[m].isConstructor() && Flags.isPublic(methods[m].getFlags()))
			{
				list.add(methods[m]);
			}
		}
	}
	
	/**
	 * Finds all public IFields belonging
	 * to the given IType and adds them to the given list.
	 * @param jdtType The type whose fields are to be harvested.
	 * @param list The list to which the harvested fields will be added.
	 * @throws JavaModelException If the JDT engine fails to
	 * analyze the given type to satisfy this request.
	 */
	public void harvestPublicFields ( IType jdtType, List list )
	throws JavaModelException
	{
		IField[] fields = jdtType.getFields();
		for (int f=0; f<fields.length; f++)
		{
			if (Flags.isPublic(fields[f].getFlags()))
			{
				list.add(fields[f]);
			}
		}
	}
	
	/**
	 * If the given method is a getter according to the Bean spec
	 * (eg. "MyType getMyProperty ()" or "boolean isMyProperty ()")
	 * return the corresponding bean property name (eg. "myProperty"),
	 * otherwise, return null.
	 * @param method The method to check.
	 * @return The property name or none if the method is not a getter.
	 */
	public String getGetterName ( IMethod method )
	{
		String methodName = method.getElementName();
		if (methodName.startsWith("get") && methodName.length() > 3)
		{
			//TODO Check signature. Must be "T getP()" or "T[] getP()" or "T getP(int)".
			return methodName.substring(3,4).toLowerCase() + methodName.substring(4);
		}
		else if (methodName.startsWith("is") && methodName.length() > 3)
		{
			//TODO Check signature. Must be "boolean isP()".
			return methodName.substring(2,3).toLowerCase() + methodName.substring(3);
		}
		return null;
	}
	
	/**
	 * If the given method is a setter according to the Bean spec
	 * (eg. "void setMyProperty (MyType)" return the corresponding
	 * bean property name (eg. "myProperty"), otherwise, return null.
	 * @param method The method to check.
	 * @return The property name or none if the method is not a setter.
	 */
	public String getSetterName ( IMethod method )
	{
		String methodName = method.getElementName();
		if (methodName.startsWith("get") && methodName.length() > 3)
		{
			//TODO Check signature. Must be "T getP()" or "T[] getP()" or "T getP(int)".
			return methodName.substring(3,4).toLowerCase() + methodName.substring(4);
		}
		else if (methodName.startsWith("is") && methodName.length() > 3)
		{
			//TODO Check signature. Must be "boolean isP()".
			return methodName.substring(2,3).toLowerCase() + methodName.substring(3);
		}
		return null;
	}
	
	/*
	 * The Java project that governs the search scope for this resolver.
	 */
	private IJavaProject javaProject_;
	
	/*
	 * The progress monitor to use, if any. 
	 */
	private IProgressMonitor monitor_;
	
	/*
	 * The set of Java wrapper types.
	 */
	private static HashSet wrapperTypeSet_ = null;
	private static String[] wrapperTypes_ = {
		"java.lang.Character",
		"java.lang.Boolean",
		"java.lang.Byte",
		"java.lang.Short",
		"java.lang.Integer",
		"java.lang.Long",
		"java.lang.Float",
		"java.lang.Double"
	};

	/*
	 * The set of Java primitive types.
	 */
	private static HashSet primitiveTypeSet_ = null;
	private static String[] primitiveTypes_ = {
		"char",
		"boolean",
		"byte",
		"short",
		"int",
		"long",
		"float",
		"double"
	};

	/*
	 * The set of JAX-RPC supported Java standard types.
	 */
	private static HashSet jaxrpcTypeSet_ = null;
	private static String[] jaxrpcTypes_ = {
		"boolean",
		"byte",
		"short",
		"int",
		"long",
		"float",
		"double",
		"java.lang.Boolean",
		"java.lang.Byte",
		"java.lang.Short",
		"java.lang.Integer",
		"java.lang.Long",
		"java.lang.Float",
		"java.lang.Double",
		"java.lang.String",
		"java.util.Date",
		"java.util.Calendar",
		"java.math.BigInteger",
		"java.math.BigDecimal",
		"java.net.URI",
		"javax.xml.namespace.QName"
	};
}
