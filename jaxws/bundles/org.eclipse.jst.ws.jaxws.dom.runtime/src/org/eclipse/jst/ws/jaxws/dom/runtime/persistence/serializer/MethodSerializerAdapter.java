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
package org.eclipse.jst.ws.jaxws.dom.runtime.persistence.serializer;

import java.util.Set;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.DomPackage;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebMethod;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.JaxWsWorkspaceResource;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.WMAnnotationFeatures;
import org.eclipse.jst.ws.jaxws.utils.annotations.AnnotationFactory;
import org.eclipse.jst.ws.jaxws.utils.annotations.IAnnotation;
import org.eclipse.jst.ws.jaxws.utils.annotations.IParamValuePair;

/**
 * Adapter for {@link IWebMethod} serialization. Listens for property change notifications and 
 * serializes the new object content to the underlying java class.
 * 
 * @author Georgi Vachkov
 */
public class MethodSerializerAdapter extends AbstractSerializerAdapter
{
	/**
	 * Constructor
	 * @param resource the DOM resource 
	 */
	public MethodSerializerAdapter(JaxWsWorkspaceResource resource) {
		super(resource);
	}
	
	@Override
	protected boolean isAnnotationRequired() {
		return false;
	}

	@Override
	protected IAnnotation<? extends IJavaElement> getAnnotation() throws JavaModelException 
	{		
		assert getTarget() instanceof IWebMethod;

		final IWebMethod webMethod = (IWebMethod)getTarget();
		final IServiceEndpointInterface sei = (IServiceEndpointInterface)webMethod.eContainer();
		if (sei == null) {
			return null;
		}
		final IType seiType = findType(sei, sei.getImplementation());
		final IMethod method = util().findMethod(seiType, webMethod);		
		if (method==null) {
			throw new IllegalStateException("IMethod for DOM method was not found");//$NON-NLS-1$
		}

		return AnnotationFactory.createAnnotation(WMAnnotationFeatures.WM_ANNOTATION, method, defineParamValues(webMethod, method));	
	}
	
	private Set<IParamValuePair> defineParamValues(final IWebMethod webMethod, final IMethod method)
	{
		final Set<IParamValuePair> paramValuePairs = createParamValueSortedTreeSet();
		if (webMethod.getName()!=null && !webMethod.getName().equals(method.getElementName())) {
			paramValuePairs.add(createParamValue(WMAnnotationFeatures.WM_NAME_ATTRIBUTE, webMethod.getName()));
		}
		
		if (webMethod.isExcluded()) {
			paramValuePairs.add(createParamValue(WMAnnotationFeatures.WM_EXCLUDED_ATTRIBUTE, true));
		}
		
		return paramValuePairs;
	}
	
	@Override
	protected boolean checkValue(final Notification msg)
	{
		final EStructuralFeature feature = (EStructuralFeature)msg.getFeature();
		if (feature.getFeatureID()!=DomPackage.ISERVICE_ENDPOINT_INTERFACE__NAME) {
			return false;
		}

		if (!super.checkValue(msg)) {
			return false;
		}
		
		return true;
	}
}
