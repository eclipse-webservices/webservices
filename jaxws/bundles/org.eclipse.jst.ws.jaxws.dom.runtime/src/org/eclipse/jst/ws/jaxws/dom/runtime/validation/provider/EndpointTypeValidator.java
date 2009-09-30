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
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.ws.jaxws.dom.runtime.internal.plugin.JaxWsDomRuntimeMessages;
import org.eclipse.jst.ws.jaxws.dom.runtime.internal.plugin.JaxWsDomRuntimePlugin;
import org.eclipse.jst.ws.jaxws.dom.runtime.internal.validation.provider.TypeAdapter;

/**
 * Provides type validation for class intedent to be used as an endpoint in
 * WebService.
 * 
 * @author Georgi Vachkov
 */

public class EndpointTypeValidator {
	private static final String EMPTY = ""; //$NON-NLS-1$

	private static final String FINALYZE_METHOD_NAME = "finalize"; //$NON-NLS-1$

	/**
	 * Validates <tt>type</tt> if is cappable to be used as endpoint. Type used
	 * as Endpoint should fulfil following requirements:
	 * 
	 * <pre>
	 *  1. Must be a source type
	 *  2. Must be public
	 *  3. Should not be an interface
	 *  4. Should not be an abstract class
	 *  5. Should not be final
	 *  6. Should provide default constructor
	 *  7. Should not declare finalyze() method
	 * </pre>
	 * 
	 * @param type
	 * @return IStatus object - in case <tt>type</tt> can be used as endpoint
	 *         the {@link IStatus#getSeverity()} will return {@link IStatus#OK},
	 *         else {@link IStatus#ERROR} is returned and
	 *         {@link IStatus#getMessage()} will return readable message
	 *         describing the condition that is not fulfiled.
	 * @throws JavaModelException
	 */
	public IStatus validate(IType type) throws JavaModelException {
		TypeAdapter typeAdapter = new TypeAdapter(type);
		String typeName = typeAdapter.getQualifiedName();

		if (type.isBinary()) {
			return prepareStatus(
					MessageFormat
							.format(
									JaxWsDomRuntimeMessages.EndpointTypeValidator_ERR_MSG_BINARYCLASS,
									typeName),
					new IllegalArgumentException(
							MessageFormat
									.format(
											"Class {0} is binary class and cannot be annotated", //$NON-NLS-1$
											typeName)));
		}

		if (!typeAdapter.isPublic()) {
			return prepareStatus(MessageFormat
					.format(
							JaxWsDomRuntimeMessages.EndpointTypeValidator_ERR_MSG_NOTPUBLIC,
							typeName),
					new IllegalArgumentException(
							MessageFormat
									.format(
											"Class {0} is not public and cannot be used as web service endpoint", //$NON-NLS-1$
											typeName)));
		}

		if (!typeAdapter.getType().isClass()) {
			return prepareStatus(MessageFormat
					.format(
							JaxWsDomRuntimeMessages.EndpointTypeValidator_ERR_MSG_NOTCLASS,
							typeName),
							new IllegalArgumentException(
									MessageFormat
											.format(
													"{0} is not a class", //$NON-NLS-1$
													typeName)));
		}

		if (typeAdapter.isAbstract()) {
			return prepareStatus(MessageFormat
					.format(
							JaxWsDomRuntimeMessages.EndpointTypeValidator_ERR_MSG_ABSTRACT,
							typeName),
							new IllegalArgumentException(
									MessageFormat
											.format(
													"Class {0} is abstract", //$NON-NLS-1$
													typeName)));
		}

		if (typeAdapter.isFinal()) {
			return prepareStatus(MessageFormat
					.format(
							JaxWsDomRuntimeMessages.EndpointTypeValidator_ERR_MSG_FINAL,
							typeName),
							new IllegalArgumentException(
									MessageFormat
											.format(
													"Class {0} is final and cannot be used as web service endpoint", //$NON-NLS-1$
													typeName)));
		}

		if (!typeAdapter.hasDefaultConstructor()) {
			return prepareStatus(MessageFormat
					.format(
							JaxWsDomRuntimeMessages.EndpointTypeValidator_ERR_MSG_CONSTRUCTOR,
							typeName),
							new IllegalArgumentException(
									MessageFormat
											.format(
													"Class {0} does not have a default constructor", //$NON-NLS-1$
													typeName)));
		}

		if (hasFinalyzeMethod(type)) {
			return prepareStatus(MessageFormat
					.format(
							JaxWsDomRuntimeMessages.EndpointTypeValidator_ERR_MSG_HASFINALYZE,
							typeName),
							new IllegalArgumentException(
									MessageFormat
											.format(
													"Class {0} contains finalize() method and cannot be used as web service endpoint", //$NON-NLS-1$
													typeName)));
		}

		return prepareStatus(null, null);
	}

	/**
	 * Checks if type contains finalyze() method
	 * 
	 * @param type
	 * @return <tt>true</tt> if has finalyze method
	 */
	private boolean hasFinalyzeMethod(IType type) {
		IMethod method = type.getMethod(FINALYZE_METHOD_NAME, new String[0]);
		if (method == null) {
			return false;
		}

		if (method.exists()) {
			return true;
		}

		return false;
	}

	/**
	 * Prepares IStatus instance. If <code>errorMessage</code> is
	 * <code>null</code> IStatus.OK is set to the IStatus object else the status
	 * is IStatus.ERROR.
	 * 
	 * @param errorMessage
	 *            the message to be used
	 * @return always returns an valid IMethodAudit instance.
	 */
	private IStatus prepareStatus(String localizedMessage, Throwable excepion) {
		IStatus status = new Status((localizedMessage == null) ? IStatus.OK
				: IStatus.ERROR, JaxWsDomRuntimePlugin.PLUGIN_ID, IStatus.OK,
				(localizedMessage == null) ? EMPTY : localizedMessage, excepion);

		return status;
	}
}
