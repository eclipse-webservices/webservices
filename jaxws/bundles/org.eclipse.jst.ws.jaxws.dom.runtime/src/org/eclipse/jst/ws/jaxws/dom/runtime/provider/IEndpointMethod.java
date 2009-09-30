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
package org.eclipse.jst.ws.jaxws.dom.runtime.provider;

import java.util.List;
import java.util.Set;

import org.eclipse.jst.ws.jaxws.utils.annotations.IAnnotationPropertyContainer;

/**
 * An interface representing method in endpoint.
 * 
 * @author Georgi Vachkov
 */
public interface IEndpointMethod
{
	/**
	 * @return the method name
	 */
	public String getName();

	/**
	 * @return the returnFQName
	 */
	public String getReturnType();

	/**
	 * @param returnTypeFQName
	 *            sets the return type
	 */
	public void setReturnType(String returnTypeFQName);

	/**
	 * @return human readable method representation
	 */
	public String getSignature();

	/**
	 * @return set of thrown exceptions
	 */
	public Set<String> getExceptions();

	/**
	 * @param exceptionFQName
	 *            add thrown exception to the method
	 */
	public void addException(String exceptionFQName);

	/**
	 * @return list of method params on the order they appear in method
	 */
	public List<IMethodParameter> getParameters();

	/**
	 * creates new param ans adds it to the end of parameters list.
	 * 
	 * @param type fully qualified type name
	 * @param name parameter name
	 * @return {@link IMethodParameter} creates
	 */
	public IMethodParameter addParameter(String type, String name);

	/**
	 * Adds {@link IAnnotationPropertyContainer} to the {@link IEndpointMethod} descriptor.
	 * 
	 * @param annotationsContainer - Container, which will be added.
	 * 
	 */
	public void addAnnotationsContainer(final IAnnotationPropertyContainer annotationsContainer);

	/**
	 * Returns Set with {@link IAnnotationPropertyContainer} related to the {@link IEndpointMethod} descriptor.
	 *
	 * @return Set with {@link IAnnotationPropertyContainer}.
	 */
	public Set<IAnnotationPropertyContainer> getAnnotationsContainer();
}
