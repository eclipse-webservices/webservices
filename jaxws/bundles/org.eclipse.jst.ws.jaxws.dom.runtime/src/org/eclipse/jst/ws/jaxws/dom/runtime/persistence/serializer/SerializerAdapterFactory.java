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
package org.eclipse.jst.ws.jaxws.dom.runtime.persistence.serializer;

import static org.eclipse.jst.ws.jaxws.utils.ContractChecker.nullCheckParam;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.IAnnotationSerializer;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.JaxWsWorkspaceResource;
import org.eclipse.jst.ws.jaxws.dom.runtime.util.DomAdapterFactory;
/**
 * Factory for serialization adapters. This factory is instantiated on construction by 
 * {@link JaxWsWorkspaceResource} and it is recommended that you use the same instance
 * in case you need this factory.
 * 
 * @author Georgi Vachkov
 */
public class SerializerAdapterFactory extends DomAdapterFactory
{
	private final JaxWsWorkspaceResource resource;
	
	/**
	 * Constructor 
	 * @param resource
	 * @throws NullPointerException in case <code>resource</code> is <code>null</code>
	 */
	public SerializerAdapterFactory(final JaxWsWorkspaceResource resource) 
	{
		nullCheckParam(resource, "resource");//$NON-NLS-1$
		this.resource = resource;
	}
	
	@Override
	public Adapter createIWebServiceAdapter() {
		return new WsSerializerAdapter(resource);
	}
	
	@Override
	public Adapter createIServiceEndpointInterfaceAdapter() {
		return new SeiSerializerAdapter(resource);
	}
	
	@Override
	public Adapter createIWebMethodAdapter() {
		return new MethodSerializerAdapter(resource);
	}
	
	@Override
	public Adapter createIWebParamAdapter() {
		return new ParameterSerializerAdapter(resource);
	}
	
	@Override
	public boolean isFactoryForType(Object type)
	{
		return IAnnotationSerializer.class == type;
	}
}
