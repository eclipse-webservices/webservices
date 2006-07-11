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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
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
	 * @throws JavaModelException If the JDT model fails to
	 * analyze the given type.
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
	 * @throws JavaModelException If the JDT model fails to
	 * analyze the given type.
	 */
	public boolean isAbstract ( IType jdtType )
	throws JavaModelException
	{
		return Flags.isAbstract(jdtType.getFlags());
	}
	
	/**
	 * Returns true if and only if instances of the given JDT IType
	 * can be instantiated via a public default constructor. The class
	 * must have an explicit public default constructor, or have no
	 * explicit constructors at all to meet this criteria.
	 * @param jdtType The type to analyze.
	 * @return True if and only if instances of the type are
	 * public default constructable.
	 * @throws JavaModelException If the JDT model fails to
	 * analyze the given type.
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
	 * Returns true if and only if the given fully qualified
	 * type name is a type from the standard JDK, that is,
	 * if the given type name belongs under the "java" or
	 * "javax" packages.
	 * @param typeName The name of the type to check.
	 * @return True if and only if the type is a non-primitive,
	 * Java standard type.
	 */
	public boolean isStandardType ( String typeName )
	{
		return typeName.startsWith("java.") || typeName.startsWith("javax.");
	}
	
	/**
	 * Returns true if and only if the given type name is a
	 * Java primitive type.
	 * @param typeName The name of the type to check.
	 * @return True if and only if the type is a Java primitive type.
	 */
	public boolean isPrimitiveType ( String typeName )
	{
		for (int i=0; i<primitiveTypes_.length; i++)
		{
			if (primitiveTypes_[i].equals(typeName))
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns true if and only if the given fully qualified
	 * type name is a Java wrapper type.
	 * @param typeName The name of the type to check.
	 * @return True if and only if the type is a Java wrapper type.
	 */
	public boolean isWrapperType ( String typeName )
	{
		for (int i=0; i<wrapperTypes_.length; i++)
		{
			if (wrapperTypes_[i].equals(typeName))
			{
				return true;
			}
		}
		return false;
	}
		
	/**
	 * Returns true if and only if the given fully qualified
	 * type name is a Java primitive or standard type supported
	 * by the JAX-RPC specification.
	 * @param typeName The name of the type to check.
	 * @return True if and only if the type is a Java wrapper type.
	 */
	public boolean isJAXRPCStandardType ( String typeName )
	{
		for (int i=0; i<jaxrpcTypes_.length; i++)
		{
			if (jaxrpcTypes_[i].equals(typeName))
			{
				return true;
			}
		}
		if (isWrapperType(typeName) && !"java.lang.Character".equals(typeName))
		{
			return true;
		}
		if (isPrimitiveType(typeName) && !"char".equals(typeName))
		{
			return true;
		}
		return false;
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
		IJavaElement elem = jdtField.getAncestor(IJavaElement.TYPE);
		IType ancestor = elem instanceof IType ? (IType)elem : null;
		String signature = jdtField.getTypeSignature();
		String typeName = getTypeNameFromSignature(signature);
		return findType(typeName,ancestor);
	}
	
	/**
	 * Returns the type name for the given field.
	 * @param jdtField The field to analyze.
	 * @return The field type name.
	 * @throws JavaModelException If the JDT engine fails to
	 * analyze the given field to satisfy this request.
	 */
	public String getFieldTypeName ( IField jdtField )
	throws JavaModelException
	{
		IJavaElement elem = jdtField.getAncestor(IJavaElement.TYPE);
		IType ancestor = elem instanceof IType ? (IType)elem : null;
		String signature = jdtField.getTypeSignature();
		String typeName = getTypeNameFromSignature(signature);
		return resolveType(typeName,ancestor);
	}
	
	/**
	 * Returns the JDT IType object for the given bean property.
	 * @param javaBeanProperty The bean property to analyze.
	 * @return The property type.
	 * @throws JavaModelException If the JDT engine fails to
	 * analyze the given bean property to satisfy this request.
	 */
	public IType getPropertyType ( IJavaBeanProperty javaBeanProperty )
	throws JavaModelException
	{
		IMethod method = javaBeanProperty.getGetter();
		if (method != null)
		{
			return getReturnType(method);
		}
		else
		{
			method = javaBeanProperty.getSetter();
			if (method != null)
			{
				IType[] parameters = getParameterTypes(method);
				if (parameters.length > 0)
				{
					//
					// It's the last parameter's type we want,
					// for indexed or non-indexed setters.
					//
					return parameters[parameters.length-1];
				}
			}
		}
		return null;
	}
	
	/**
	 * Returns the type name for the given bean property.
	 * @param javaBeanProperty The bean property to analyze.
	 * @return The property type name.
	 * @throws JavaModelException If the JDT engine fails to
	 * analyze the given bean property to satisfy this request.
	 */
	public String getPropertyTypeName ( IJavaBeanProperty javaBeanProperty )
	throws JavaModelException
	{
		IMethod method = javaBeanProperty.getGetter();
		if (method != null)
		{
			return getReturnTypeName(method);
		}
		else
		{
			method = javaBeanProperty.getSetter();
			if (method != null)
			{
				String[] parameterTypeNames = getParameterTypeNames(method);
				if (parameterTypeNames.length > 0)
				{
					return parameterTypeNames[0];
				}
			}
		}
		return null;
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
		IJavaElement elem = jdtMethod.getAncestor(IJavaElement.TYPE);
		IType ancestor = elem instanceof IType ? (IType)elem : null;
		String signature = jdtMethod.getReturnType();
		String typeName = getTypeNameFromSignature(signature);
		return findType(typeName,ancestor);
	}
	
	/**
	 * Returns the type name for the return type
	 * of the given method, or null if the method is void.
	 * @param jdtMethod The method to analyze.
	 * @return The method return type name, or null if none.
	 * @throws JavaModelException If the JDT engine fails to
	 * analyze the given method to satisfy this request.
	 */
	public String getReturnTypeName ( IMethod jdtMethod )
	throws JavaModelException
	{
		IJavaElement elem = jdtMethod.getAncestor(IJavaElement.TYPE);
		IType ancestor = elem instanceof IType ? (IType)elem : null;
		String signature = jdtMethod.getReturnType();
		String typeName = getTypeNameFromSignature(signature);
		return resolveType(typeName,ancestor);
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
		IJavaElement elem = jdtMethod.getAncestor(IJavaElement.TYPE);
		IType ancestor = elem instanceof IType ? (IType)elem : null;
		String[] signatures = jdtMethod.getParameterTypes();
		IType[] types = new IType[signatures.length];
		for (int s=0; s<signatures.length; s++)
		{
			String typeName = getTypeNameFromSignature(signatures[s]);
			types[s] = findType(typeName,ancestor);
		}
		return types;
	}
	
	/**
	 * Returns an array of zero or more type names
	 * for the parameters of the given method.
	 * @param jdtMethod The method to analyze.
	 * @return An array of zero or more parameter type names.
	 * @throws JavaModelException If the JDT engine fails to
	 * analyze the given method to satisfy this request.
	 */
	public String[] getParameterTypeNames ( IMethod jdtMethod )
	throws JavaModelException
	{
		IJavaElement elem = jdtMethod.getAncestor(IJavaElement.TYPE);
		IType ancestor = elem instanceof IType ? (IType)elem : null;
		String[] signatures = jdtMethod.getParameterTypes();
		String[] typeNames = new String[signatures.length];
		for (int s=0; s<signatures.length; s++)
		{
			String typeName = getTypeNameFromSignature(signatures[s]);
			typeNames[s] = resolveType(typeName,ancestor);
		}
		return typeNames;
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
		IJavaElement elem = jdtMethod.getAncestor(IJavaElement.TYPE);
		IType ancestor = elem instanceof IType ? (IType)elem : null;
		String[] signatures = jdtMethod.getExceptionTypes();
		IType[] types = new IType[signatures.length];
		for (int s=0; s<signatures.length; s++)
		{
			String typeName = getTypeNameFromSignature(signatures[s]);
			types[s] = findType(typeName,ancestor);
		}
		return types;
	}
	
	/**
	 * Returns an array of zero or more type names
	 * for the exceptions thrown by the given method.
	 * @param jdtMethod The method to analyze.
	 * @return An array of zero or more exception type names.
	 * @throws JavaModelException If the JDT engine fails to
	 * analyze the given method to satisfy this request.
	 */
	public String[] getExceptionTypeNames ( IMethod jdtMethod )
	throws JavaModelException
	{
		IJavaElement elem = jdtMethod.getAncestor(IJavaElement.TYPE);
		IType ancestor = elem instanceof IType ? (IType)elem : null;
		String[] signatures = jdtMethod.getExceptionTypes();
		String[] typeNames = new String[signatures.length];
		for (int s=0; s<signatures.length; s++)
		{
			String typeName = getTypeNameFromSignature(signatures[s]);
			typeNames[s] = resolveType(typeName,ancestor);
		}
		return typeNames;
	}
	
	/**
	 * Returns an array of zero or more types representing
	 * the superclasses, if any, of the given IType in bottom-up
	 * order excluding java.lang.Object.
	 * @param jdtType The type to analyze.
	 * @return An array of zero or more superclass types.
	 * @throws JavaModelException If the JDT engine fails to
	 * analyze the given type to satisfy this request.
	 */
	public IType[] getSuperClasses ( IType jdtType )
	throws JavaModelException
	{
		return getSuperClasses(jdtType,"java.lang.Object");
	}
	
	/**
	 * Returns an array of zero or more types representing
	 * the superclasses, if any, of the given IType.
	 * Under normal circumstances, java.lang.Object is included.
	 * @param jdtType The type to analyze.
	 * @param stopClassName The name of a stop class used to limit
	 * the superclasses returned to the caller. If the stop class
	 * is null or names a class not found in the hierarchy, all
	 * superclasses are returned. Otherwise, only superclasses up
	 * to but excluding the stop class are returned.
	 * @return An array of zero or more superclass types.
	 * @throws JavaModelException If the JDT engine fails to
	 * analyze the given type to satisfy this request.
	 */
	public IType[] getSuperClasses ( IType jdtType, String stopClassName )
	throws JavaModelException
	{
		ITypeHierarchy hierarchy = jdtType.newSupertypeHierarchy(monitor_);
		IType[] allSuperClasses = hierarchy.getAllSuperclasses(jdtType);
		List superClasses = new LinkedList();
		for (int i=0; i<allSuperClasses.length; i++)
		{
			if (allSuperClasses[i].getFullyQualifiedName().equals(stopClassName))
				break;
			superClasses.add(allSuperClasses[i]);
		}
		return (IType[])superClasses.toArray(new IType[0]);
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
	 * Attempts to resolve an unresolved type name in the
	 * context of the given ancestor type (ie. the type
	 * acting as the context within which the type name
	 * should be resolved).
	 * @param typeName The qualified name of the type to find.
	 * @param ancestor The type providing the context within
	 * which to look up the type.
	 * @return The qualified name, or the original type name
	 * if it could not be resolved.
	 */
	public String resolveType ( String typeName, IType ancestor )
	throws JavaModelException
	{
		if (ancestor != null)
		{
			String[][] matches = ancestor.resolveType(typeName);
			if (matches != null && matches.length > 0)
			{
				StringBuffer qname = new StringBuffer();
				int n = matches[0].length;
				for (int j=0; j<n-1; j++)
				{
					qname.append(matches[0][j]).append(".");
				}
				if (n >= 0)
				{
					qname.append(matches[0][n-1]);
				}
				return qname.toString();
			}
		}
		return typeName;
	}

	/**
	 * Attempts to find a JDT IType object for the given
	 * resolved or unresolved type name in the context of
	 * the given ancestor type (ie. the type acting as the
	 * context within which the type name should be resolved).
	 * @param typeName The qualified name of the type to find.
	 * @param ancestor The type providing the context within
	 * which to look up the type.
	 * @return The IType object of the given qualified name,
	 * or null if no type could be found in the workspace.
	 */
	public IType findType ( String typeName, IType ancestor )
	throws JavaModelException
	{
		IType type = javaProject_.findType(typeName);
		if (type == null && ancestor != null)
		{
			String[][] matches = ancestor.resolveType(typeName);
			if (matches != null)
			{
				int i=0;
				while (type == null && i<matches.length)
				{
					StringBuffer qname = new StringBuffer();
					int n = matches[i].length;
					for (int j=0; j<n-1; j++)
					{
						qname.append(matches[i][j]).append(".");
					}
					if (n >= 0)
					{
						qname.append(matches[i][n-1]);
					}
					type = javaProject_.findType(qname.toString());
					i++;
				}
			}
		}
		return type;
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
		try
		{
			String methodName = method.getElementName();
			if (methodName.startsWith("get") && methodName.length() > 3)
			{
				String signature = method.getSignature();
				String returnTypeName = Signature.getReturnType(signature);
				//
				// Getters must not be void.
				//
				if (returnTypeName != null && returnTypeName != Signature.SIG_VOID)
				{
					String[] parameterSignatures = method.getParameterTypes();
					//
					// Non-indexed getters must have zero parameters.
					//
					if (parameterSignatures.length == 0)
					{
						return methodName.substring(3,4).toLowerCase() + methodName.substring(4);
					}
					//
					// Indexed getters must have exactly one parameter of type "int".
					//
					else if (parameterSignatures.length == 1)
					{
						String indexTypeName = getTypeNameFromSignature(parameterSignatures[0]);
						if (indexTypeName.equals("int"))
						{
							return methodName.substring(3,4).toLowerCase() + methodName.substring(4);
						}
					}
				}
			}
			else if (methodName.startsWith("is") && methodName.length() > 2)
			{
				String signature = method.getSignature();
				String returnTypeName = Signature.getReturnType(signature);
				//
				// "is" getters must be boolean.
				//
				if (Signature.SIG_BOOLEAN.equals(returnTypeName))
				{
					//
					// "is" getters must have no parameters.
					//
					if (method.getParameterTypes().length == 0)
					{
						return methodName.substring(2,3).toLowerCase() + methodName.substring(3);
					}
				}
			}
		}
		catch (JavaModelException e)
		{
			return null;
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
		try
		{
			String methodName = method.getElementName();
			if (methodName.startsWith("set") && methodName.length() > 3)
			{
				String signature = method.getSignature();
				String returnTypeName = Signature.getReturnType(signature);
				//
				// Setters must be void.
				//
				if (Signature.SIG_VOID.equals(returnTypeName))
				{
					String[] parameterSignatures = method.getParameterTypes();
					//
					// Non-indexed setters must have exactly one parameter.
					//
					if (parameterSignatures.length == 1)
					{
						return methodName.substring(3,4).toLowerCase() + methodName.substring(4);
					}
					//
					// Indexed setters must have exactly two parameters,
					// the first of which must be "int".
					//
					else if (parameterSignatures.length == 2)
					{
						if (Signature.SIG_INT.equals(parameterSignatures[0]))
						{
							return methodName.substring(3,4).toLowerCase() + methodName.substring(4);
						}
					}
				}
			}
		}
		catch (JavaModelException e)
		{
			return null;
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
	 * The set of JAX-RPC supported Java standard types,
	 * excluding primitives and wrapper types.
	 */
	private static String[] jaxrpcTypes_ = {
		"boolean",
		"byte",
		"short",
		"int",
		"long",
		"float",
		"double",
		"java.lang.String",
		"java.lang.Boolean",
		"java.lang.Byte",
		"java.lang.Short",
		"java.lang.Integer",
		"java.lang.Long",
		"java.lang.Float",
		"java.lang.Double",
		"java.util.Date",
		"java.util.Calendar",
		"java.math.BigInteger",
		"java.math.BigDecimal",
		"java.net.URI",
		"javax.xml.namespace.QName"
	};
}
