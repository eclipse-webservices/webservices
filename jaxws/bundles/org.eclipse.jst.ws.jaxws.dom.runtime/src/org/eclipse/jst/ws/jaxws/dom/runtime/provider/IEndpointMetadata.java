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
 * Represents metadata for an endpoint. Contains information about the name and namespace of the endpoint. Contains exposed operations. This interface
 * is intended to be used as input data for implementors of {@link IEndpointGenerator}.
 * 
 * @author Plamen Pavlov
 */
public interface IEndpointMetadata
{
	/**
	 * @return The name of enpoint.
	 */
	public String getName();

	/**
	 * @return The package/namespace of the endpoint.
	 */
	public String getPackage();

	/**
	 * @return the name of the service representing this endpoint
	 */
	public String getServiceName();

	/**
	 * Sets the name of the wsdl:service representing this endpoint.
	 * 
	 * @param serviceName
	 */
	public void setServiceName(String serviceName);

	/**
	 * @return the name of the wsdl:port referencing this endpoint which this
	 */
	public String getPortName();

	/**
	 * Sets the name of the wsdl:port referencing this endpoint which this
	 * 
	 * @param portName
	 */
	public void setPortName(String portName);

	/**
	 * @return the wsdl location in case this endpoint is generated from wsdl otherwise <tt>null</tt>
	 */
	public String getWsdlLocation();

	/**
	 * Sets the wsdl location in case this endpoint is generated from wsdl
	 * 
	 * @param wsdlLocation
	 *            location
	 */
	public void setWsdlLocation(String wsdlLocation);

	/**
	 * @return the namespace URI
	 */
	public String getNamespaceURI();

	/**
	 * sets the namespace URI
	 * 
	 * @param namespaceURI
	 */
	public void setNamespaceURI(String namespaceURI);

	/**
	 * @return the name of the Service Endpoint Interface containing WebService methods that will be implemented by this endpoint implementation. This
	 *         method can return <tt>null</tt>
	 */
	public String getSEIName();

	/**
	 * Sets the Service Endpoint Interface referenced by this endpoint.
	 * 
	 * @param seiName
	 * @throws NullPointerException
	 *             in case <tt>seiName</tt> is null
	 * @throws IllegalArgumentException
	 *             in case <tt>seiName</tt> is empty string
	 */
	public void setSEIName(String seiName);

	/**
	 * @return The fully qualified name of endpoint.
	 */
	public String getFullyQualifiedName();

	/**
	 * @return list of added methods
	 */
	public List<IEndpointMethod> getMethods();

	/**
	 * Creates an empty instance of {@link IEndpointMethod} with <tt>name</tt> and <tt>returnTypeFQName</tt> and adds it to the set of available
	 * methods.
	 * 
	 * @param name
	 * @param returnTypeFQName
	 * @return created IEndpointMethod for added method
	 */
	public IEndpointMethod addMethod(String name, String returnTypeFQName);
	
	/**
	 * Adds {@link IAnnotationPropertyContainer} to the {@link IEndpointMetadata} descriptor.
	 * 
	 * @param annotationsContainer - Container, which will be added.
	 * 
	 */
	public void addAnnotationsContainer(final IAnnotationPropertyContainer annotationsContainer);

	/**
	 * Returns Set with {@link IAnnotationPropertyContainer} related to the {@link IEndpointMetadata} descriptor.
	 *
	 * @return Set with {@link IAnnotationPropertyContainer}.
	 */
	public Set<IAnnotationPropertyContainer> getAnnotationsContainer();
}
