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

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jst.ws.jaxws.dom.runtime.TypeResolver;
import org.eclipse.jst.ws.jaxws.dom.runtime.internal.plugin.JaxWsDomRuntimeMessages;
import org.eclipse.jst.ws.jaxws.dom.runtime.internal.plugin.JaxWsDomRuntimePlugin;
import org.eclipse.jst.ws.jaxws.utils.ContractChecker;

/**
 * Validates SEI against implementation bean.
 * 
 * @author Georgi Vachkov
 */

public class SeiImplementationValidator
{
	private final IType seiType;

	/**
	 * Constructor.
	 * 
	 * @param seiType
	 * @throws NullPointerException in case <code>seiType</code> is <code>null</code>
	 * @throws IllegalArgumentException in case <code>seiType</code> is not a interface
	 * @throws JavaModelException
	 */
	public SeiImplementationValidator(final IType seiType) throws JavaModelException
	{
		ContractChecker.nullCheckParam(seiType);
		
		if (!seiType.isInterface()) {
			throw new IllegalArgumentException("seiType should be interface"); //$NON-NLS-1$
		}
 		
		this.seiType = seiType;
	}

	/**
	 * Validates provided in constructor SEI against <tt>beanType</tt>. The <tt>beanType</tt> should implement all
	 * 
	 * @param beanType
	 * @return status object with set severity and message
	 * @throws JavaModelException
	 */
	public IStatus validate(final IType beanType) throws JavaModelException
	{
		final String beanFQName = beanType.getFullyQualifiedName();
		if (implementsSEI(beanType))
		{
			return prepareStatus(null, null);
		}

		for (IMethod seiMethod : collectInterfaceMethods(seiType))
		{
			IMethod beanMethod = findMethodInBean(seiMethod, beanType);
			if (beanMethod == null)
			{
				return prepareStatus(MessageFormat.format(JaxWsDomRuntimeMessages.SeiImplementationValidator_METHOD_NOT_FOUND, formatMethodSignature(seiMethod), beanFQName),
						new IllegalArgumentException(
								MessageFormat.format(
												"Method {0} cannot be found in the implementation bean {1}. It is required that the bean implements all the methods of the specified service endpoint interface", //$NON-NLS-1$
												beanFQName)));
			}

			if (!Flags.isPublic(beanMethod.getFlags()))
			{
				return prepareStatus(MessageFormat.format(JaxWsDomRuntimeMessages.SeiImplementationValidator_METHOD_NOT_PUBLIC, formatMethodSignature(seiMethod), beanFQName),
						new IllegalArgumentException(
								MessageFormat.format(
												" Method {0} in the implementation bean {1} is not public and therefore cannot be exposed as a web service operation", //$NON-NLS-1$
												beanFQName)));
			}

			if (!hasSameExceptions(seiMethod, beanMethod))
			{
				return prepareStatus(MessageFormat.format(JaxWsDomRuntimeMessages.SeiImplementationValidator_METHOD_HASDIFFERENT_EXCEPTIONS, formatMethodSignature(seiMethod), beanFQName),
						new IllegalArgumentException(
								MessageFormat.format(
												"Method {0} in the implementation bean {1} is declared to throw exceptions that differ from exceptions thrown by the same method in the service endpoint interface", //$NON-NLS-1$
												beanFQName)));
			}
		}

		return prepareStatus(null, null);
	}

	private boolean implementsSEI(final IType beanType) throws JavaModelException
	{
		for (String interfaceName : beanType.getSuperInterfaceNames())
		{
			String interfaceFQName = TypeResolver.resolveType(interfaceName, beanType);
			if (interfaceFQName.equals(seiType.getFullyQualifiedName()))
			{
				return true;
			}
		}

		return false;
	}

	private IMethod findMethodInBean(final IMethod seiMethod, final IType beanType) throws JavaModelException
	{
		for (IMethod beanMethod : beanType.getMethods())
		{
			if (!seiMethod.getElementName().equals(beanMethod.getElementName()))
			{
				continue;
			}

			if (hasSameReturnType(seiMethod, beanMethod) && hasSameParams(seiMethod, beanMethod))
			{
				return beanMethod;
			}
		}

		if (beanType.getSuperclassName() == null)
		{
			return null;
		}

		final String superTypeName = TypeResolver.resolveType(beanType.getSuperclassName(), beanType);

		return findMethodInBean(seiMethod, beanType.getJavaProject().findType(superTypeName));
	}

	private boolean hasSameReturnType(final IMethod seiMethod, final IMethod beanMethod) throws JavaModelException
	{
		final List<String> seiRetType = TypeResolver.resolveTypes(seiMethod.getReturnType(), seiMethod.getDeclaringType());
		final List<String> beanRetType = TypeResolver.resolveTypes(beanMethod.getReturnType(), beanMethod.getDeclaringType());

		if (!areSameTypes(seiRetType, beanRetType))
		{
			return false;
		}

		return true;
	}

	private boolean hasSameParams(final IMethod seiMethod, final IMethod beanMethod) throws JavaModelException
	{
		final String[] seiParamTypes = seiMethod.getParameterTypes();
		final String[] beanParamTypes = beanMethod.getParameterTypes();
		if (seiParamTypes.length != beanParamTypes.length)
		{
			return false;
		}

		List<String> seiParamType = null;
		List<String> beanParamType = null;
		for (int i = 0; i < seiParamTypes.length; i++)
		{
			seiParamType = TypeResolver.resolveTypes(seiParamTypes[i], seiMethod.getDeclaringType());
			beanParamType = TypeResolver.resolveTypes(beanParamTypes[i], beanMethod.getDeclaringType());

			if (!areSameTypes(seiParamType, beanParamType))
			{
				return false;
			}
		}

		return true;
	}

	private boolean areSameTypes(final List<String> seiTypeNames, final List<String> beanTypeNames)
	{
		if (seiTypeNames.size() != beanTypeNames.size())
		{
			return false;
		}

		for (int i = 0; i < seiTypeNames.size(); i++)
		{
			if (!seiTypeNames.get(i).equals(beanTypeNames.get(i)))
			{
				return false;
			}
		}

		return true;
	}

	private List<IMethod> collectInterfaceMethods(final IType type) throws JavaModelException
	{
		final List<IMethod> allMethods = new ArrayList<IMethod>();
		Collections.addAll(allMethods, type.getMethods());

		for (String superInterfaceName : type.getSuperInterfaceNames())
		{
			String superFQName = TypeResolver.resolveType(superInterfaceName, type);
			IType superType = type.getJavaProject().findType(superFQName, (IProgressMonitor) null);

			assert (superType != null);

			allMethods.addAll(collectInterfaceMethods(superType));
		}

		return allMethods;
	}

	private boolean hasSameExceptions(IMethod seiMethod, IMethod beanMethod) throws JavaModelException
	{
		if (seiMethod.getExceptionTypes().length != beanMethod.getExceptionTypes().length)
		{
			return false;
		}

		final Set<String> seiExceptions = getResolvedExceptions(seiMethod);
		final IType beanType = beanMethod.getDeclaringType();
		for (String beanEx : beanMethod.getExceptionTypes())
		{
			String resolvedName = TypeResolver.resolveType(Signature.toString(beanEx), beanType);
			if (!seiExceptions.contains(resolvedName))
			{
				return false;
			}
		}

		return true;
	}

	private Set<String> getResolvedExceptions(IMethod method) throws JavaModelException
	{
		final Set<String> exceptionsFQNs = new HashSet<String>(method.getExceptionTypes().length);
		final IType declaringType = method.getDeclaringType();

		for (String exception : method.getExceptionTypes())
		{
			exceptionsFQNs.add(TypeResolver.resolveType(Signature.toString(exception), declaringType));
		}

		return exceptionsFQNs;
	}

	private String formatMethodSignature(final IMethod method) throws JavaModelException
	{
		final StringBuffer name = new StringBuffer(Signature.toString(method.getSignature(), method.getElementName(), method.getParameterNames(),
										true, false));
		name.append(" : "); //$NON-NLS-1$
		name.append(Signature.toString(method.getReturnType()));

		return name.toString();
	}

	private IStatus prepareStatus(String msg, Throwable cause)
	{
		return new Status((msg == null) ? IStatus.OK : IStatus.ERROR, JaxWsDomRuntimePlugin.PLUGIN_ID, IStatus.OK, msg == null ? "" : msg, cause); //$NON-NLS-1$
	}
}
