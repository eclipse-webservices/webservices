/*******************************************************************************
 * Copyright (c) 2009 by SAP AG, Walldorf. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     SAP AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.jaxws.dom.runtime.validation.provider;

import java.util.Collection;

import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jst.ws.jaxws.dom.runtime.GeneralTypesNames;
import org.eclipse.jst.ws.jaxws.dom.runtime.PrimitiveTypeHandler;
import org.eclipse.jst.ws.jaxws.dom.runtime.TypeResolver;
import org.eclipse.jst.ws.jaxws.dom.runtime.internal.validation.provider.TypeAdapter;
import org.eclipse.jst.ws.jaxws.dom.runtime.internal.validation.provider.TypeFactory;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.provider.exceptions.AbstractClassNotImplementedException;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.provider.exceptions.InadmissableTypeException;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.provider.exceptions.InterfacesNotSupportedException;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.provider.exceptions.NoDefaultConstructorException;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.provider.exceptions.TypeNotFoundException;
import org.eclipse.jst.ws.jaxws.utils.annotations.AnnotationFactory;
import org.eclipse.jst.ws.jaxws.utils.annotations.IAnnotation;
import org.eclipse.jst.ws.jaxws.utils.annotations.IAnnotationInspector;

/**
 * Provides type validation for class intedent to be used as an runtime classes in WebService.Runtime classes should fulfil following requirements:<br>
 * <ul>
 * 1. Should provide default constructor<br>
 * 2. Should not implement java.rmi.Remote<br>
 * 3. If it is a java.* type should be one of allowed java types<br>
 * 4. Class should not contain inner classes that are not public static.<br>
 * 5. Should not have 'multy inheritance'. This means if class extends class other than java.lang.Object it should not implement interface. In oposite
 * if the class extends java.lang.Object it should implement only one interface. Following interfaces are not checked:<br>
 * <br>
 * 
 * <ul>
 * java.lang.Comparable<br>
 * java.lang.Clonable<br>
 * java.io.Serializable
 * </ul>
 * </ul>
 * 
 * @author Georgi Vachkov
 */
public class RuntimeTypeValidator extends TypeValidator
{
	private static final String XML_TRANSIENT_ANNOTATION = "javax.xml.bind.annotation.XmlTransient"; //$NON-NLS-1$
	private static final String XML_TRANSIENT_ANNOTATION_SHORT = "XmlTransient"; //$NON-NLS-1$

	private static final String XML_TYPE_ANNOTATION = "javax.xml.bind.annotation.XmlType"; //$NON-NLS-1$
	private static final String WEB_FAULT_ANNOTATION = "javax.xml.ws.WebFault"; //$NON-NLS-1$

	private static final String GET = "get"; //$NON-NLS-1$
	private static final String OBJECT_FACTORY = "ObjectFactory"; //$NON-NLS-1$
	

	// private static final String SET = "set";

	
	private IAnnotationInspector annotationInspector = null;

	/**
	 * Provides type validation for class intedent to be used as an runtime classes in WebService.Runtime classes should fulfil following
	 * requirements:<br>
	 * <ul>
	 * 1. Should provide default constructor<br>
	 * 2. Should not implement java.rmi.Remote<br>
	 * 3. If it is a java.* type should be one of allowed java types<br>
	 * 4. Class should not contain inner classes that are not public static.<br>
	 * 5. Should not have 'multy inheritance'. This means if class extends class other than java.lang.Object it should not implement interface. In
	 * oposite if the class extends java.lang.Object it should implement only one interface. Following interfaces are not checked:<br>
	 * <br>
	 * 
	 * <ul>
	 * java.lang.Comparable<br>
	 * java.lang.Clonable<br>
	 * java.io.Serializable
	 * </ul>
	 * </ul>
	 * 
	 * @param type -
	 *            checked type
	 * @throws JavaModelException
	 * @throws InadmissableTypeException
	 * @see TypeValidator for list of allowed java types.
	 */
	@Override
	protected void doValidate(TypeAdapter type) throws InadmissableTypeException, JavaModelException
	{
		final String typeName = type.getQualifiedName();

		// check for types from java.* packages
		if (isJavaType(typeName))
		{
			if (isAllowedJavaType(typeName))
			{
				return;
			}
		}

		if (Flags.isEnum(type.getType().getFlags()))
		{
			return;
		}

		if (isXmlAnnotated(type.getType()))
		{
			return;
		}
		
		if (hasObjectFactoryMethod(type.getType())) {
			return;
		}

		if (type.isInterface())
		{
			throw new InterfacesNotSupportedException(typeName);
		}

		if (isExtendingCollectionOrMap(type))
		{
			return;
		}

		if (!type.hasDefaultConstructor())
		{
			throw new NoDefaultConstructorException(typeName);
		}

		checkRemoteNotImplemented(type);
		checkMultipleInheritance(type);
		checkForInadmissableInnerClasses(type);
		checkAbstractNotImplemented(type);
		checkPublicAttributes(type);
		checkTypesUsedInProperties(type);
	}
	
	private boolean hasObjectFactoryMethod(final IType type) throws JavaModelException 
	{
		if (type.getJavaProject()==null) {
			return false;
		}
		
		final String pack = type.getPackageFragment().getElementName();
		final String objectFactoryFQName = pack.length()==0 ? OBJECT_FACTORY : pack + '.' + OBJECT_FACTORY;
		
		final IType objectFactory = type.getJavaProject().findType(objectFactoryFQName);
		if (objectFactory == null) {
			return false;
		}

		if (containsCreateMethod(objectFactory, type)) {
			return true;
		}

		return false;
	}
	
	private boolean containsCreateMethod(final IType objectFactory, final IType type) throws JavaModelException
	{		
		for (IMethod method : objectFactory.getMethods()) 
		{
			if (method.isConstructor()) {
				continue;
			}
			
			for (String name : TypeResolver.resolveTypes(method.getReturnType(), type)) {
				if (type.getFullyQualifiedName().equals(name)) {
					return true;
				}
			}
		}
		
		return false;
	}

	/**
	 * Checks if <code>type</code> is annotated with annotation affecting mapping to XML. 
	 * Always returns false in case <code>type</code> is binary class.
	 * 
	 * @param type
	 * @return
	 * @throws JavaModelException 
	 */
	private boolean isXmlAnnotated(IType type) throws JavaModelException
	{
		if (type.isBinary()) {
			return false;
		}

		annotationInspector = AnnotationFactory.createAnnotationInspector(type);
		IAnnotation<IType> typeAnnotation = annotationInspector.inspectType(XML_TYPE_ANNOTATION);
		if (typeAnnotation!=null) {
			return true;
		}
		
		typeAnnotation = annotationInspector.inspectType(WEB_FAULT_ANNOTATION);
		if (typeAnnotation!=null) {
			return true;
		}

		return false;
	}

	/**
	 * Performs check on public member variables of type. 1. Checks if the type is a java API class whether it is supported. 2. If is other type
	 * perform rutime class check using validate(TypeProxy type) method.
	 * 
	 * @see RuntimeTypeValidator - checkType(TypeProxy type) method.
	 * @param type
	 * @throws InadmissableTypeException
	 * @throws JavaModelException
	 */
	private void checkPublicAttributes(TypeAdapter type) throws InadmissableTypeException, JavaModelException
	{
		IType iType = type.getType();
		for (IField field : iType.getFields())
		{
			if (Flags.isPublic(field.getFlags()))
			{
				resolveAndCheckType(field.getTypeSignature(), iType);
			}
		}
	}

	/**
	 * Check if the <code>type</code> implements {@link java.util.Collection} or {@link java.util.Map}
	 * 
	 * @param type
	 * @return true - if implements {@link java.util.Collection} or {@link java.util.Map}
	 * @throws TypeNotFoundException
	 * @throws JavaModelException
	 */
	private boolean isExtendingCollectionOrMap(TypeAdapter type) throws TypeNotFoundException, JavaModelException
	{
		if (type.isImplementing(GeneralTypesNames.JAVA_UTIL_COLLECTION))
		{
			return true;
		}

		if (type.isImplementing(GeneralTypesNames.JAVA_UTIL_MAP))
		{
			return true;
		}

		return false;
	}

	/**
	 * Checks types used as return value, parameter types.
	 * 
	 * @param type
	 * @throws InadmissableTypeException
	 * @throws JavaModelException
	 */
	private void checkTypesUsedInProperties(TypeAdapter type) throws InadmissableTypeException, JavaModelException
	{
		IType iType = type.getType();
		for (IMethod method : iType.getMethods())
		{
			// Constructors are not validated
			if (method.isConstructor() || isXmlTransient(method))
			{
				continue;
			}

			// only getters are validated
			if (!isGetter(method))
			{// || !findSetter(method)) {
				return;
			}

			resolveAndCheckType(method.getReturnType(), method.getDeclaringType());
		}
	}

	private boolean isGetter(final IMethod method)
	{
		return method.getElementName().startsWith(GET);
	}

	private boolean isXmlTransient(IMethod method) throws JavaModelException
	{
		if (method.getDeclaringType().isBinary())
		{
			return false;
		}

		annotationInspector = AnnotationFactory.createAnnotationInspector(method.getDeclaringType());
		Collection<IAnnotation<IMethod>> methodAnnotations = annotationInspector.inspectMethod(method);
		boolean result = false;
		for (IAnnotation<IMethod> methodAnnotation : methodAnnotations)
		{
			if(methodAnnotation.getAnnotationName().equals(XML_TRANSIENT_ANNOTATION) ||
					methodAnnotation.getAnnotationName().equals(XML_TRANSIENT_ANNOTATION_SHORT))
			{
				result = true;
			}

		}

		return result;
	}

	/**
	 * Provides type check with type resolving.
	 * 
	 * @param typeName
	 * @param type
	 * @throws JavaModelException
	 * @throws TypeNotFoundException
	 * @throws InadmissableTypeException
	 */
	private void resolveAndCheckType(String typeName, IType type) throws JavaModelException, TypeNotFoundException, InadmissableTypeException
	{
		if(isVariableType(typeName, type))
		{
			return;
		}
		
		for (String resolvedType : TypeResolver.resolveTypes(typeName, type))
		{
			if (PrimitiveTypeHandler.isVoidType(resolvedType))
			{
				continue;
			}

			if (isJavaType(resolvedType) && isAllowedJavaType(resolvedType))
			{
				continue;
			}

			validateInternal(new TypeAdapter(TypeFactory.create(resolvedType, type)));
		}
	}

	/**
	 * Validates if <code>type</code> is abstract class whether it is implemented by some class capable for runtime class.
	 * 
	 * @param type
	 * @throws InadmissableTypeException
	 * @throws JavaModelException
	 */
	private void checkAbstractNotImplemented(TypeAdapter type) throws InadmissableTypeException, JavaModelException
	{
		// check for implementor for abstract class
		if (type.isAbstract() && !isImplemented(type.getType()))
		{
			throw new AbstractClassNotImplementedException(type.getQualifiedName());
		}
	}

	/**
	 * Checks whether <code>base</code> class has some non abstract implementor which is capable for runtime class.
	 * 
	 * @param base
	 *            checked type
	 * @throws JavaModelException
	 */
	private boolean isImplemented(IType base) throws JavaModelException
	{
		if (base.getFullyQualifiedName().equals(GeneralTypesNames.JAVA_LANG_OBJECT)
										|| base.getFullyQualifiedName().equals(GeneralTypesNames.JAVA_IO_SERIALIZABLE))
		{
			return true;
		}

		ITypeHierarchy typeHierarchy = base.newTypeHierarchy(null);
		IType[] classes = typeHierarchy.getAllSubtypes(base);

		IType classCandidate = null;
		for (int i = 0; i < classes.length; i++)
		{
			classCandidate = classes[i];

			if (canBeUsedAsRTClass(classCandidate))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Check if given type is suitable for class/interface implementor. Class should not be abstract, interface and should comply to the rules defined
	 * in checkType documentation.
	 * 
	 * @see RuntimeTypeValidator - checkType(TypeProxy type) method.
	 * @param type -
	 *            checked type
	 * @throws JavaModelException
	 */
	private boolean canBeUsedAsRTClass(IType type) throws JavaModelException
	{
		int flags = type.getFlags();

		if (!type.isClass() || Flags.isAbstract(flags))
		{
			return false;
		}

		if (Flags.isInterface(flags) || type.isAnonymous())
		{
			return false;
		}

		// check sub type if it is allowed as rt classes
		try
		{
			TypeAdapter tp = new TypeAdapter(type);
			validateInternal(tp);
		} catch (InadmissableTypeException e)
		{
			// $JL-EXC$
			return false;
		}

		return true;
	}
	
	/**
	 * Checks if the type is a variable type (like e.g. "T" in Holder&ltT&gt)
	 */
	private boolean isVariableType(final String typeName, final IType type) throws JavaModelException
	{
		return Signature.getTypeSignatureKind(TypeResolver.resolveType(typeName, type)) == Signature.TYPE_VARIABLE_SIGNATURE;
	}
}
