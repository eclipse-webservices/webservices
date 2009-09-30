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

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jst.ws.jaxws.dom.runtime.PrimitiveTypeHandler;
import org.eclipse.jst.ws.jaxws.dom.runtime.TypeResolver;
import org.eclipse.jst.ws.jaxws.dom.runtime.internal.plugin.JaxWsDomRuntimePlugin;
import org.eclipse.jst.ws.jaxws.dom.runtime.internal.validation.provider.TypeFactory;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.provider.exceptions.ConstructorNotExposableException;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.provider.exceptions.InadmissableTypeException;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.provider.exceptions.MethodNotPublicException;

/**
 * Performes method validation by validating types used. Types are cached between different invocations of check(IMethod method) for better
 * performance.
 * 
 * @author Georgi Vachkov
 */
public class MethodValidator
{
	private final static String EMPTY = ""; //$NON-NLS-1$

	private final RuntimeTypeValidator validator;

	/**
	 * Constructor.
	 */
	public MethodValidator()
	{
		validator = new RuntimeTypeValidator();
	}

	/**
	 * Performs check on types used in method. Checks return types, paramether types and exception types. Stops validation on the first error. An
	 * IStatus object containing information on the result of the check is returned.
	 * 
	 * @param method
	 * @return IStatus with severity IStatus.OK if the method can be exposed, or IStatus.ERROR including error message decribing the problem.
	 * @throws NullPointerException -
	 *             in case <code>method</code> is null
	 * @throws JavaModelException
	 */
	public IStatus check(IMethod method) throws JavaModelException
	{
		if (method.isConstructor())	{
			return prepareStatus(new ConstructorNotExposableException(method.getElementName()));
		}
		
		final IType declaringType = method.getDeclaringType();

		if (!Flags.isPublic(method.getFlags()) && !declaringType.isInterface())	{
			return prepareStatus(new MethodNotPublicException(method.getElementName()));
		}

		try
		{
			// Handle return type
			validateRuntimeClass(method.getReturnType(), declaringType);

			// Handle input parameters and there types
			for (String paramTypeName : method.getParameterTypes())
			{
				validateRuntimeClass(paramTypeName, declaringType);
			}

			// handle exception types
			for (String exceptionTypeName : method.getExceptionTypes())
			{
				validateException(exceptionTypeName, declaringType);
			}

			return prepareStatus(null);
		} catch (InadmissableTypeException e)
		{
			return prepareStatus(e);
		}
	}

	/**
	 * Validates <code>typeName</code>. On first steps type is resolved and all resolved types are validated one by one. The resolved types can be
	 * more than one in case the type declaration uses generics. Validation propagates the exceptions thrown by RuntimeTypeValidator.
	 * 
	 * @param typeName
	 * @param declaringType
	 * @throws JavaModelException
	 * @throws InadmissableTypeException
	 */
	private void validateRuntimeClass(String typeName, IType declaringType) throws JavaModelException, InadmissableTypeException
	{
		Collection<String> names = TypeResolver.resolveTypes(typeName, declaringType);
		for (String name : names)
		{
			if (PrimitiveTypeHandler.isVoidType(name))
			{
				continue;
			}

			validator.validate(TypeFactory.create(name, declaringType));
		}
	}

	/**
	 * Validates if <tt>exceptionType</tt> is a valid exception type for WS operation
	 * 
	 * @param exceptionType -
	 *            exception type name
	 * @param declaringType -
	 *            type in which it is used
	 * @throws JavaModelException
	 * @throws InadmissableTypeException
	 * @throws JavaModelException
	 */
	private void validateException(final String exceptionType, final IType declaringType) throws InadmissableTypeException, JavaModelException
	{
		String exFQName = TypeResolver.resolveType(Signature.toString(exceptionType), declaringType);

		validator.validate(TypeFactory.create(exFQName, declaringType));
	}

	/**
	 * Prepares IMethodAudit instance for <code>method</code>. If <code>throwable</code> is <code>null</code> IStatus.OK is set to the IStatus
	 * object contained in returned IMethodAudit instance else IStatus.ERROR is set.
	 * 
	 * @param throwable
	 * @return always returns an valid IMethodAudit instance.
	 */
	private IStatus prepareStatus(Throwable throwable)
	{
		IStatus status = new Status((throwable == null) ? IStatus.OK : IStatus.ERROR, JaxWsDomRuntimePlugin.PLUGIN_ID, IStatus.OK,
										(throwable == null) ? EMPTY : throwable.getLocalizedMessage(), throwable);

		return status;
	}
}
