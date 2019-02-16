/*******************************************************************************
 * Copyright (c) 2009 by SAP AG, Walldorf. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     SAP AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.jaxws.dom.runtime.persistence.sync;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jst.ws.jaxws.dom.runtime.DomUtil;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.DomFactory;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IDOM;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.IModelElementSynchronizer;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.JaxWsDefaultsCalculator;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.JaxWsWorkspaceResource;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.JaxWsWorkspaceResource.ServiceModelData;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.annotation.AnnotationAdapterFactory;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.annotation.IAnnotationAdapter;
import org.eclipse.jst.ws.jaxws.utils.annotations.IAnnotation;
import org.eclipse.jst.ws.jaxws.utils.logging.ILogger;

class ElementSynchronizerImpl implements IModelElementSynchronizer
{

	private final IModelElementSynchronizer parent;
	private final DomUtil util;
	private final JaxWsDefaultsCalculator defaultsCalculator;
	
	ElementSynchronizerImpl(IModelElementSynchronizer parent)
	{
		this.parent = parent;
		util = new DomUtil();
		defaultsCalculator = new JaxWsDefaultsCalculator();
	}
	
	public DomUtil util() {
		return util;
	}
	
	public JaxWsDefaultsCalculator defCalc() {
		return defaultsCalculator;
	}
	
	public ServiceModelData serviceData()
	{
		return parent.serviceData();
	}
	
	public ILogger logger()
	{
		return parent.logger();
	}
	
	public IDOM getDomBeingLoaded()
	{
		return parent.getDomBeingLoaded();
	}
	
	public DomFactory domFactory()
	{
		return parent.domFactory();
	}
	
	public IJavaModel javaModel()
	{
		return parent.javaModel();
	}
	
	public JaxWsWorkspaceResource resource()
	{
		return parent.resource();
	}
	
	public void adaptToLocationInterface(final EObject object, final String annFQName, final IAnnotation<? extends IJavaElement> annotation)
	{
		final IAnnotationAdapter locator = (IAnnotationAdapter)AnnotationAdapterFactory.INSTANCE.adapt(object, IAnnotationAdapter.class);
		locator.addAnnotation(annFQName, annotation);
	}
}
