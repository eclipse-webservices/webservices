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
package org.eclipse.jst.ws.jaxws.dom.runtime.validation;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.model.IModelConstraint;
import org.eclipse.emf.validation.service.IModelConstraintProvider;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebMethod;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebParam;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebService;
import org.eclipse.jst.ws.jaxws.dom.runtime.util.DomSwitch;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.sei.SeiConstraintProvider;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.webmethod.WmConstraintProvider;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.webparam.WpConstraintProvider;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.webservice.WsConstraintProvider;

public class WsDynamicModelConstraintProvider implements IModelConstraintProvider 
{
	private final DomSwitch<Collection<IModelConstraint>> domSwitch;
	private final WsConstraintProvider wsProvider;
	private final SeiConstraintProvider seiProvider;
	private final WmConstraintProvider wmProvider;
	private final WpConstraintProvider wpProvider;
	
	
	public WsDynamicModelConstraintProvider()
	{
		domSwitch = createDomSwitch();
		wsProvider = new WsConstraintProvider();
		seiProvider = new SeiConstraintProvider();
		wmProvider = new WmConstraintProvider();
		wpProvider = new WpConstraintProvider();
	}
	
	@SuppressWarnings("unchecked")
	public Collection getBatchConstraints(EObject eObject, Collection constraints) 
	{
		final Collection<IModelConstraint> modelConstraints = domSwitch.doSwitch(eObject);
		if (modelConstraints!=null) {
			constraints.addAll(modelConstraints);
		}
		
		return constraints;
	}

	@SuppressWarnings("unchecked")
	public Collection getLiveConstraints(Notification arg0, Collection constraints) {
		return constraints;
	}

	private DomSwitch<Collection<IModelConstraint>> createDomSwitch()
	{
		return new DomSwitch<Collection<IModelConstraint>>() 
		{
			public Collection<IModelConstraint> caseIWebService(IWebService object) {				
				return wsProvider.getConstraints();
			}
			
			@Override
			public Collection<IModelConstraint> caseIServiceEndpointInterface(IServiceEndpointInterface object) {
				return seiProvider.getConstraints();
			}	
			
			@Override
			public Collection<IModelConstraint> caseIWebMethod(IWebMethod object) {
				return wmProvider.getConstraints();
			}
			
			@Override
			public Collection<IModelConstraint> caseIWebParam(IWebParam object) {
				return wpProvider.getConstraints();
			}
		};
	}
}
