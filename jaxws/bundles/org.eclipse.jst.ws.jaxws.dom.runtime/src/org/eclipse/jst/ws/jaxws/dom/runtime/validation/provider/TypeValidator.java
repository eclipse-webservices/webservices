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

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.ws.jaxws.dom.runtime.GeneralTypesNames;
import org.eclipse.jst.ws.jaxws.dom.runtime.internal.plugin.JaxWsDomRuntimePlugin;
import org.eclipse.jst.ws.jaxws.dom.runtime.internal.validation.provider.TypeAdapter;
import org.eclipse.jst.ws.jaxws.dom.runtime.internal.validation.provider.TypeKey;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.provider.exceptions.HasInadmisableInnerTypesException;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.provider.exceptions.InadmissableTypeException;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.provider.exceptions.InheritanceAndImplementationExecption;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.provider.exceptions.MultipleImplementationException;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.provider.exceptions.RemoteObjectException;

/**
 * Provides set of general validation methods which can be reused by subclasses.
 * 
 * @i036509
 */
public abstract class TypeValidator
{
	// there are types from the java.* packages that are supported by the runtime
	// and are known to it without analysis of there structure, therefore they are
	// stored as elementaries.
	private static final Set<String> JAVA_TYPES = new HashSet<String>(); // $JL-COLLECTION$

	// for these java.* types attributes are resolved, i.e they are stored as
	// structure if they contain fields.
	private static final Set<String> ANALYSED_JAVA_TYPES = new HashSet<String>(); // $JL-COLLECTION$

	// these types are allowed to be used in case of multy inheritance
	private static final Set<String> ALLOWED_IN_MULTYINHERITANCE = new HashSet<String>(); // $JL-COLLECTION$

	private Map<TypeKey, IStatus> statusMap = new HashMap<TypeKey, IStatus>();

	// cache of already analyzed types
	private Set<TypeKey> analyzed = new HashSet<TypeKey>();

	static
	{
		// There are types from the java.* packages that are supported by the runtime
		// and are known to it without analysis of there structure, therefore they are
		// stored as elementaries.

		// Date/Time Specific Types
		JAVA_TYPES.add("java.util.Calendar"); //$NON-NLS-1$
		JAVA_TYPES.add("java.util.Date"); //$NON-NLS-1$
		JAVA_TYPES.add("java.util.GregorianCalendar"); //$NON-NLS-1$
		JAVA_TYPES.add("javax.xml.datatype.XMLGregorianCalendar"); //$NON-NLS-1$	

		// Array Based Types
		JAVA_TYPES.add("java.util.ArrayList"); //$NON-NLS-1$
		JAVA_TYPES.add("java.util.HashSet"); //$NON-NLS-1$
		JAVA_TYPES.add("java.util.LinkedList"); //$NON-NLS-1$
		JAVA_TYPES.add("java.util.List"); //$NON-NLS-1$
		JAVA_TYPES.add("java.util.Stack"); //$NON-NLS-1$
		JAVA_TYPES.add("java.util.Vector"); //$NON-NLS-1$

		// Others
		JAVA_TYPES.add("java.math.BigDecimal"); //$NON-NLS-1$
		JAVA_TYPES.add("java.math.BigInteger"); //$NON-NLS-1$
		JAVA_TYPES.add("java.lang.Object"); //$NON-NLS-1$
		JAVA_TYPES.add("javax.xml.namespace.QName"); //$NON-NLS-1$
		JAVA_TYPES.add("java.util.UUID"); //$NON-NLS-1$
		JAVA_TYPES.add("java.net.URI"); //$NON-NLS-1$
		JAVA_TYPES.add("java.awt.Image"); //$NON-NLS-1$
		JAVA_TYPES.add("javax.xml.datatype.Duration"); //$NON-NLS-1$
		JAVA_TYPES.add("javax.xml.transform.Source"); //$NON-NLS-1$
		JAVA_TYPES.add("javax.activation.DataHandler"); //$NON-NLS-1$

		// Java primitive type wrapper classes
		JAVA_TYPES.add("java.lang.String"); //$NON-NLS-1$
		JAVA_TYPES.add("java.lang.Boolean"); //$NON-NLS-1$
		JAVA_TYPES.add("java.lang.Integer"); //$NON-NLS-1$
		JAVA_TYPES.add("java.lang.Character"); //$NON-NLS-1$
		JAVA_TYPES.add("java.lang.Float"); //$NON-NLS-1$
		JAVA_TYPES.add("java.lang.Byte"); //$NON-NLS-1$
		JAVA_TYPES.add("java.lang.Double"); //$NON-NLS-1$
		JAVA_TYPES.add("java.lang.Long"); //$NON-NLS-1$
		JAVA_TYPES.add("java.lang.Short"); //$NON-NLS-1$
		JAVA_TYPES.add("java.lang.Void"); //$NON-NLS-1$

		JAVA_TYPES.add("java.rmi.RemoteException"); //$NON-NLS-1$

		JAVA_TYPES.add("java.io.Serializable"); //$NON-NLS-1$
		JAVA_TYPES.add("java.lang.Cloneable"); //$NON-NLS-1$
		JAVA_TYPES.add("java.lang.Comparable"); //$NON-NLS-1$

		JAVA_TYPES.add("java.util.Map"); //$NON-NLS-1$
		JAVA_TYPES.add("java.util.HashMap"); //$NON-NLS-1$
		JAVA_TYPES.add("java.util.Hashtable"); //$NON-NLS-1$
		JAVA_TYPES.add("java.util.Collection"); //$NON-NLS-1$

		// For these java.* types attributes are resolved, i.e they are stored as
		// structure if they contain fields.
		ANALYSED_JAVA_TYPES.add("java.lang.Exception"); //$NON-NLS-1$
		ANALYSED_JAVA_TYPES.add("java.lang.Throwable"); //$NON-NLS-1$
		ANALYSED_JAVA_TYPES.add("java.lang.StackTraceElement"); //$NON-NLS-1$

		ALLOWED_IN_MULTYINHERITANCE.add(GeneralTypesNames.JAVA_LANG_CLONEABLE); // $NON-NLS$
		ALLOWED_IN_MULTYINHERITANCE.add(GeneralTypesNames.JAVA_IO_SERIALIZABLE); // $NON-NLS$
		ALLOWED_IN_MULTYINHERITANCE.add(GeneralTypesNames.JAVA_LANG_COMPARABLE); // $NON-NLS$
	}

	protected abstract void doValidate(TypeAdapter type) throws InadmissableTypeException, JavaModelException;

	/**
	 * Validates <code>type</code>. The first step is tho check the cache if this type has been checked already. If yes the previuos validation
	 * result is returned if no a check is performed and the result is chached. Internally calls doValidate(type) which should be implemented by
	 * extending classes.
	 * 
	 * @param type -
	 *            checked type
	 * @throws InadmissableTypeException
	 * @throws JavaModelException
	 */
	public void validate(IType type) throws InadmissableTypeException, JavaModelException
	{
		analyzed.clear();
		validateInternal(new TypeAdapter(type));
	}

	/**
	 * Validates <code>type</code>. The first step is tho check the cache if this type has been checked already. If yes the previuos validation
	 * result is returned if no a check is performed and the result is chached. Internally calls doValidate(type) which should be implemented by
	 * extending classes.
	 * 
	 * @param type -
	 *            checked type
	 * @throws InadmissableTypeException
	 * @throws JavaModelException
	 */
	protected void validateInternal(TypeAdapter type) throws InadmissableTypeException, JavaModelException
	{
		final TypeKey key = new TypeKey(type.getProjectName(), type.getQualifiedName());

		// check for circular type check
		if (analyzed.contains(key))
		{
			return;
		}

		analyzed.add(key);

		final IStatus status = statusMap.get(key);
		if (status != null)
		{
			if (status.getSeverity() == IStatus.OK)
			{
				return;
			}

			throw (InadmissableTypeException) status.getException();
		}

		try
		{
			doValidate(type);
			statusMap.put(key, prepareStatus(null));
		} catch (InadmissableTypeException e)
		{
			statusMap.put(key, prepareStatus(e));
			throw e;
		}
	}

	/**
	 * if <code>throwable</code> is null prepares status with severity IStatus.OK and empty message. If <code>throwable</code> is not null
	 * prepares status with severity IStatus.ERROR and message the contained in <code>throwable</code> error message.
	 * 
	 * @param throwable
	 * @return IStatus with severity IStatus.OK if <tt>throwable</tt> is null , or IStatus.ERROR including error message decribing the problem.
	 */
	private IStatus prepareStatus(Throwable throwable)
	{
		final IStatus status = new Status((throwable == null) ? IStatus.OK : IStatus.ERROR, JaxWsDomRuntimePlugin.PLUGIN_ID, IStatus.OK,
										(throwable == null) ? "" : throwable.getMessage(), //$NON-NLS-1$
										throwable);

		return status;
	}

	/**
	 * Defines if <code>typeName</code> one form java.* or javax.* packages.
	 * 
	 * @param typeName
	 * @return <tt>true</tt> if <tt>typeName</tt> is from java standard classes.
	 */
	protected boolean isJavaType(String typeName)
	{
		return typeName.startsWith("java.") //$NON-NLS-1$
										|| typeName.startsWith("javax."); //$NON-NLS-1$
	}

	/**
	 * Defines if <code>typeName</code> is allowed java type. The list of allowed java types can be found in static section of this class.
	 * 
	 * @param typeName
	 * @return <tt>true</tt> if <tt>typeName</tt> is allowed to be used in Web Service.
	 */
	protected boolean isAllowedJavaType(String typeName)
	{
		return JAVA_TYPES.contains(typeName) || ANALYSED_JAVA_TYPES.contains(typeName);
	}

	/**
	 * Checks if <code>type</code> implements java.rmi.Remote interface.
	 * 
	 * @param type
	 * @throws InadmissableTypeException
	 * @throws JavaModelException
	 */
	protected void checkRemoteNotImplemented(TypeAdapter type) throws InadmissableTypeException, JavaModelException
	{
		if (type.isImplementing(GeneralTypesNames.JAVA_RMI_REMOTE))
		{
			throw new RemoteObjectException(type.getQualifiedName());
		}
	}

	/**
	 * Checks if <code>type</code> implements multiple interfaces. Classes that extend class other than java.lang.Object should not implement any
	 * interface. Classes that extend java.lang.Object can implement only one interface.<br>
	 * <br>
	 * Note: The following interfaces are skipped from check:<br>
	 * <ul>
	 * java.io.Serializble<br>
	 * java.lang.Clonable<br>
	 * java.lang.Comparable<br>
	 * </ul>
	 * 
	 * @param type
	 * @throws InadmissableTypeException
	 * @throws JavaModelException
	 */
	protected void checkMultipleInheritance(TypeAdapter type) throws InadmissableTypeException, JavaModelException
	{
		List<String> interfaceNames = type.getInterfaceNames();
		final String superClassName = type.getSuperClassName();
		if (superClassName == null || superClassName.equals(GeneralTypesNames.JAVA_LANG_OBJECT)
										|| superClassName.equals(GeneralTypesNames.JAVA_LANG_EXCEPTION))
		{
			if (!checkInterfaceImplementationCount(interfaceNames, 1))
			{
				throw new MultipleImplementationException(type.getQualifiedName(), interfaceNames.get(0), interfaceNames.get(1));
			}
		} else
		{
			if (!checkInterfaceImplementationCount(interfaceNames, 0))
			{
				throw new InheritanceAndImplementationExecption(type.getQualifiedName(), superClassName);
			}

			checkMultipleInheritance(type.getSuperClassType());
		}
	}

	/**
	 * Checks if <code>interfaceNames</code> are more than <code>maxAllowed</code> interfaces. Note: The following interfaces are skipped from
	 * check:<br>
	 * <ul>
	 * java.io.Serializble<br>
	 * java.lang.Clonable<br>
	 * java.lang.Comparable<br>
	 * </ul>
	 * 
	 * @param interfaceNames
	 * @param maxAllowed -
	 *            number of maximum allowed implementations
	 */
	protected boolean checkInterfaceImplementationCount(List<String> interfaceNames, int maxAllowed)
	{
		if (interfaceNames == null || interfaceNames.size() == 0)
		{
			return true;
		}

		int noneAllowedInterfaceCount = 0;
		for (String interfaceName : interfaceNames)
		{
			if (ALLOWED_IN_MULTYINHERITANCE.contains(interfaceName))
			{
				continue;
			}

			noneAllowedInterfaceCount++;
			if (noneAllowedInterfaceCount > maxAllowed)
			{
				return false;
			}
		}

		return true;
	}

	/**
	 * Inadmissable are member classes that are not "public static".
	 * 
	 * @param type
	 * @throws HasInadmisableInnerTypesException
	 * @throws JavaModelException
	 */
	protected void checkForInadmissableInnerClasses(TypeAdapter type) throws HasInadmisableInnerTypesException, JavaModelException
	{
		if (type.hasNonStaticOrNonPublicInnerTypes())
		{
			throw new HasInadmisableInnerTypesException(type.getQualifiedName());
		}
	}
}
