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
package org.eclipse.jst.ws.jaxws.dom.runtime.internal.validation.provider;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.ws.jaxws.dom.runtime.GeneralTypesNames;
import org.eclipse.jst.ws.jaxws.dom.runtime.TypeResolver;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.provider.exceptions.TypeNotFoundException;
import org.eclipse.jst.ws.jaxws.utils.ContractChecker;

/**
 * TypeProxy represents a type in a way convienient for type checking.
 * 
 * @author Georgi Vachkov
 */
public class TypeAdapter
{
	private final IType type;

	private String superClassName;

	/**
	 * Constructor.
	 * 
	 * @param type
	 * @throws NullPointerException -
	 *             in case <code>type</code> is null.
	 */
	public TypeAdapter(IType type)
	{
		ContractChecker.nullCheckParam(type, "type");
		this.type = type;
	}

	/**
	 * Checks for default constructor in analysed class.
	 * 
	 * @return true if public constructor without paramethers exists or class does not have any constructor.
	 * @throws JavaModelException
	 */
	public boolean hasDefaultConstructor() throws JavaModelException
	{
		boolean hasConstructor = false;
		boolean isPublic = false;

		for (IMethod method : type.getMethods())
		{
			isPublic = Flags.isPublic(method.getFlags());
			if (method.isConstructor())
			{
				hasConstructor = true;
			}

			if (method.isConstructor() && isPublic && method.getParameterNames().length == 0)
			{
				return true;
			}
		}

		return !hasConstructor;
	}

	/**
	 * Retrieves the direct super interfaces fully qualified names.
	 * 
	 * @return list of interface names - in case no interfaces are implemented an empty array is returned
	 * @throws JavaModelException
	 */
	public final List<String> getInterfaceNames() throws JavaModelException
	{
		String[] iNames = type.getSuperInterfaceNames();
		List<String> interfaceNames = new ArrayList<String>(iNames.length);
		for (String name : iNames)
		{
			String resolvedName = TypeResolver.resolveType(name, type);
			interfaceNames.add(resolvedName);
		}

		return interfaceNames;
	}

	/**
	 * Checks if the class contains non static and non public inner classes.
	 * 
	 * @return true if contains such
	 * @throws JavaModelException
	 */
	public boolean hasNonStaticOrNonPublicInnerTypes() throws JavaModelException
	{
		for (IType t : type.getTypes())
		{
			int typeFlags = t.getFlags();
			if (!Flags.isStatic(typeFlags) || !Flags.isPublic(typeFlags))
			{
				return true;
			}
		}

		return false;
	}

	/**
	 * Retrieves the name of it's superclass
	 * 
	 * @return returnes resolved super class name or null if not present
	 * @throws JavaModelException
	 */
	public final String getSuperClassName() throws JavaModelException
	{
		if (type.getSuperclassName() != null && superClassName == null)
		{
			superClassName = TypeResolver.resolveType(type.getSuperclassName(), type);
		}

		return superClassName;
	}

	/**
	 * Resolves and creates an instance of TypeProxy for super class.
	 * 
	 * @return returns instance of {@link TypeAdapter} for super class or null if not present
	 * @throws JavaModelException
	 * @throws TypeNotFoundException
	 */
	public final TypeAdapter getSuperClassType() throws TypeNotFoundException, JavaModelException
	{
		if (getSuperClassName() == null)
			return null;

		return new TypeAdapter(TypeFactory.create(getSuperClassName(), getType()));
	}

	/**
	 * Defines if this class extends <tt>baseClassType</tt>
	 * 
	 * @param baseClassType
	 * @return check if this class type extends <tt>baseClassType</tt>
	 * @throws TypeNotFoundException
	 * @throws JavaModelException
	 */
	public boolean isExtending(String baseClassType) throws TypeNotFoundException, JavaModelException
	{
		String typeName = this.getQualifiedName();

		// if we have arrived at object in the hierarchy, the type cannot be an exception
		if (typeName.equals(GeneralTypesNames.JAVA_LANG_OBJECT))
		{
			return false;
		}
		// All exceptions inherit from Throwable
		if (typeName.equals(baseClassType))
		{
			return true;
		}

		TypeAdapter parentType = getSuperClassType();
		if (parentType != null)
		{
			return parentType.isExtending(baseClassType);
		}

		return false;
	}

	/**
	 * Checks if <code>type</code> implements interface <code>checkedInterfaceName</code>. Recursive check over the implemented interfaces is
	 * done which defines if some of the implemented in <code>type</code> interface is extending the specified interface.
	 * 
	 * @param interfaceName
	 * @return true is this class type implements <tt>interfaceName</tt>
	 * @throws TypeNotFoundException
	 * @throws JavaModelException
	 */
	public boolean isImplementing(final String interfaceName) throws TypeNotFoundException, JavaModelException
	{
		return isImplementing(this, interfaceName);
	}

	private boolean isImplementing(final TypeAdapter checkedType, final String checkedInterfaceName) throws TypeNotFoundException, JavaModelException

	{
		if (checkedType.getQualifiedName().equals(checkedInterfaceName))
			return true;

		for (String interfaceName : checkedType.getInterfaceNames())
		{
			TypeAdapter ta = new TypeAdapter(TypeFactory.create(interfaceName, checkedType.getType()));
			if (isImplementing(ta, checkedInterfaceName))
			{
				return true;
			}
		}

		// here use ep checker as seriablizable is not required in supertype
		if (checkedType.getSuperClassName() != null)
		{
			TypeAdapter superClass = new TypeAdapter(TypeFactory.create(checkedType.getSuperClassName(), checkedType.getType()));
			return isImplementing(superClass, checkedInterfaceName);
		}

		return false;
	}

	/**
	 * Returns the type fully qualified name
	 * 
	 * @return not <tt>null</tt> string
	 */
	public final String getQualifiedName()
	{
		return type.getFullyQualifiedName();
	}

	/**
	 * Returns the project name in which the type resides.
	 * 
	 * @return not <tt>null</tt> string
	 */
	public final String getProjectName()
	{
		return type.getJavaProject().getProject().getName();
	}

	/**
	 * Returns wrapped IType.
	 * 
	 * @return IType instance
	 */
	public final IType getType()
	{
		return type;
	}

	@Override
	public int hashCode()
	{
		return type.getFullyQualifiedName().hashCode();
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == null)
		{
			return false;
		}

		if (obj.getClass() == TypeAdapter.class)
		{
			return (getQualifiedName().equals(((TypeAdapter) obj).getQualifiedName()));
		}

		return false;
	}

	/**
	 * Checks if the type is public.
	 * 
	 * @return <tt>true</tt> if the class is public
	 * @throws JavaModelException
	 */
	public boolean isPublic() throws JavaModelException
	{
		return Flags.isPublic(type.getFlags());
	}

	/**
	 * Checks if the type is final.
	 * 
	 * @return <tt>true</tt> if the class is final
	 * @throws JavaModelException
	 */
	public boolean isFinal() throws JavaModelException
	{
		return Flags.isFinal(type.getFlags());
	}

	/**
	 * Checks if the type is abstract.
	 * 
	 * @return <tt>true</tt> if the class is abstract
	 * @throws JavaModelException
	 */
	public boolean isAbstract() throws JavaModelException
	{
		return Flags.isAbstract(type.getFlags());
	}

	/**
	 * Checks if the type is an interface.
	 * 
	 * @return <tt>true</tt> if the class is interface
	 * @throws JavaModelException
	 */
	public boolean isInterface() throws JavaModelException
	{
		return type.isInterface();
	}
}
