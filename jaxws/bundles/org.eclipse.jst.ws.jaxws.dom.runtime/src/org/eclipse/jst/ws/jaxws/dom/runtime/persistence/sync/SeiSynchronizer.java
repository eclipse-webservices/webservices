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
package org.eclipse.jst.ws.jaxws.dom.runtime.persistence.sync;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.DomPackage;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebService;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebServiceProject;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.IAnnotationSerializer;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.IModelElementSynchronizer;
import org.eclipse.jst.ws.jaxws.utils.annotations.IAnnotation;
import org.eclipse.jst.ws.jaxws.utils.annotations.IAnnotationInspector;

/**
 * Synchronizer for explicit {@link IServiceEndpointInterface} instance. Updates the DOM instance to the current
 * SEI implementation class by reading relevant annotations and 
 * 
 * @author 
 */
public class SeiSynchronizer extends ElementSynchronizerImpl
{
	private final SeiMerger seiMerger; 

	public SeiSynchronizer(IModelElementSynchronizer parent)
	{
		super(parent);
		seiMerger = new SeiMerger(this, new SeiMethodSynchronizer(this));
	}

	/* IServiceEndpointInterface */
	public void synchronizeInterface(IWebServiceProject wsProject, IAnnotation<IType> wsAnnotation, IAnnotationInspector inspector) throws JavaModelException
	{
		if(wsAnnotation.getAppliedElement().getFullyQualifiedName()==null) {
			return;
		}
		
		final IServiceEndpointInterface sei = obtainInstance(wsProject, wsAnnotation.getAppliedElement().getFullyQualifiedName());
		seiMerger.merge(sei, wsAnnotation, inspector);
		resolveWsRefsToThisSei(sei);
		resource().getSerializerFactory().adapt(sei, IAnnotationSerializer.class);		
	}

	private IServiceEndpointInterface obtainInstance(IWebServiceProject wsProject, String implName)
	{
		IServiceEndpointInterface sei = null;
		for (IServiceEndpointInterface existing : wsProject.getServiceEndpointInterfaces())
		{
			if (existing.getImplementation().equals(implName)) {
				sei = existing;
			}
		}
		
		if (sei==null)
		{
			sei = domFactory().createIServiceEndpointInterface();
			util().setFeatureValue(sei, DomPackage.IJAVA_WEB_SERVICE_ELEMENT__IMPLEMENTATION, implName);
			wsProject.getServiceEndpointInterfaces().add(sei);
		}
		
		util().setFeatureValue(sei, DomPackage.ISERVICE_ENDPOINT_INTERFACE__IMPLICIT, false);
		
		return sei;
	}

	private void resolveWsRefsToThisSei(IServiceEndpointInterface sei)
	{
		for (IWebService ws : serviceData().getImplementingWs(sei.getImplementation()))
		{
			if (ws.getServiceEndpoint() != sei)
			{
				util().setFeatureValue(ws, DomPackage.IWEB_SERVICE__SERVICE_ENDPOINT, sei);
			}
		}
	}
}
