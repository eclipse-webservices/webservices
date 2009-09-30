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

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.ws.jaxws.dom.runtime.TypeResolver;
import org.eclipse.jst.ws.jaxws.dom.runtime.internal.plugin.JaxWsDomRuntimeMessages;
import org.eclipse.jst.ws.jaxws.dom.runtime.internal.plugin.JaxWsDomRuntimePlugin;

/**
 * Interface validator - checks if supplied type is applicable for Service Endpoint Interface (SEI).
 * 
 * @author Georgi Vachkov
 */

public class SeiValidator
{
	
	private final MethodValidator methodValidator;

	/**
	 * Constructor.
	 */
	public SeiValidator()
	{
		methodValidator = new MethodValidator();
	}

	/**
	 * <pre>
	 * Validates <tt>type</tt> if it is applicable for Service Entdpoint Interface.
	 *  
	 *  1. <tt>type</tt> must be interface
	 *  2. <tt>type</tt> must be outer
	 *  3. <tt>type</tt> must be public
	 *  4. all <tt>type</tt> methods must be exposable according to the rules in {@link MethodValidator}
	 * </pre>
	 * 
	 * @param type validated type
	 * @return IStatus object with status {@link IStatus#OK} in case <tt>type</tt> is applicable for SEI or {@link IStatus#ERROR} in case type is
	 *         not applicable with some human readble explanation of the problem.
	 * @throws NullPointerException in case <tt>type</tt> is null.
	 * @throws JavaModelException
	 */
	public IStatus validate(IType type) throws JavaModelException
	{
		String seiName = type.getFullyQualifiedName();
		if (!type.isInterface())
		{
			return prepareStatus(MessageFormat.format(JaxWsDomRuntimeMessages.SeiValidator_NOT_INTERFACE_MSG, seiName),
					new IllegalArgumentException(
							MessageFormat.format(
											"{0} is not a interface and cannot be used as a service endpoint interface", //$NON-NLS-1$
											seiName)));
		}

		if (type.isBinary())
		{
			return prepareStatus(MessageFormat.format(JaxWsDomRuntimeMessages.SeiValidator_NOT_COMPILATIONUNIT_MSG, seiName),
					new IllegalArgumentException(
							MessageFormat.format(
											"{0} is a binary interface and cannot annotated as a service endpoint interface", //$NON-NLS-1$
											seiName)));
		}

		if (type.isMember())
		{
			return prepareStatus(MessageFormat.format(JaxWsDomRuntimeMessages.SeiValidator_NOT_OUTER_MSG, seiName),
					new IllegalArgumentException(
							MessageFormat.format(
											"{0} is not a outer interface and cannot be used as a service endpoint interface", //$NON-NLS-1$
											seiName)));
		}

		if (!Flags.isPublic(type.getFlags()))
		{
			return prepareStatus(MessageFormat.format(JaxWsDomRuntimeMessages.SeiValidator_NOT_PUBLIC_MSG, seiName),
					new IllegalArgumentException(
							MessageFormat.format(
											"{0} is not a public interface and cannot be used as a service endpoint interface", //$NON-NLS-1$
											seiName)));
		}

		return validateMethods(type);
	}

	private IStatus validateMethods(IType type) throws JavaModelException
	{
		IStatus status;
		for (IMethod method : type.getMethods())
		{
			status = methodValidator.check(method);
			if (status.getSeverity() != IStatus.OK)
			{
				return status;
			}
		}

		for (String superInterfaceName : type.getSuperInterfaceNames())
		{
			String superFQName = TypeResolver.resolveType(superInterfaceName, type);
			IType superType = type.getJavaProject().findType(superFQName);

			assert (superType != null);

			status = validateMethods(superType);
			if (status.getSeverity() != IStatus.OK)
			{
				return status;
			}
		}

		return prepareStatus(null, null);
	}

	private IStatus prepareStatus(String msg, Throwable cause)
	{
		return new Status((msg == null) ? IStatus.OK : IStatus.ERROR, JaxWsDomRuntimePlugin.PLUGIN_ID, IStatus.OK, msg == null ? "" : msg, cause); //$NON-NLS-1$
	}
}
