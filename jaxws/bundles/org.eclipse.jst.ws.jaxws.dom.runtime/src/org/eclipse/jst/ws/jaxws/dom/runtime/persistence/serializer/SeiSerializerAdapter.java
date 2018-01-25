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

import java.util.Set;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebService;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.JaxWsWorkspaceResource;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.WSAnnotationFeatures;
import org.eclipse.jst.ws.jaxws.utils.JaxWsUtils;
import org.eclipse.jst.ws.jaxws.utils.annotations.AnnotationFactory;
import org.eclipse.jst.ws.jaxws.utils.annotations.IAnnotation;
import org.eclipse.jst.ws.jaxws.utils.annotations.IParamValuePair;

/**
 * Adapter that on {@link IWebService} object change saves the annotation to 
 * the underlying compilation unit. Uses {@link JaxWsWorkspaceResource} to define 
 * if save operation is enabled.
 * 
 * @author Georgi Vachkov
 */
public class SeiSerializerAdapter extends AbstractSerializerAdapter
{
	/**
	 * Constructor
	 * @param resource
	 */
	public SeiSerializerAdapter(JaxWsWorkspaceResource resource) {
		super(resource);
	}
	
	@Override
	protected boolean isAnnotationRequired() {
		return true;
	}	

	@Override
	protected IAnnotation<? extends IJavaElement> getAnnotation() throws JavaModelException 	
	{
		assert getTarget() instanceof IServiceEndpointInterface;

		return createIAnnotation((IServiceEndpointInterface)getTarget(), resource().javaModel());
	}

	protected IAnnotation<? extends IJavaElement> createIAnnotation(final IServiceEndpointInterface sei, final IJavaModel javaModel) throws JavaModelException 
	{
		nullCheckParam(sei, "sei");//$NON-NLS-1$
		nullCheckParam(javaModel, "javaModel");//$NON-NLS-1$
		
		if (sei.isImplicit()) 
		{
			if (sei.getImplementingWebServices().size() == 0) {
				// the implicit SEI has already been removed from the project
				return null;
			}
			
			return new WsSerializerAdapter(resource()).createIAnnotation(sei.getImplementingWebServices().get(0), javaModel);
		}
		
		final IType type = findType(sei, sei.getImplementation());
		if (type == null) {
			// the explicit SEI has already been removed from the project
			return null;
		}

		final Set<IParamValuePair> paramValuePairs = createParamValueSortedTreeSet();

		if (sei.getName()!=null && !sei.getName().equals(JaxWsUtils.getDefaultPorttypeName(sei.getImplementation()))) {
			paramValuePairs.add(createParamValue(WSAnnotationFeatures.NAME_ATTRIBUTE, sei.getName()));
		}
		if (sei.getTargetNamespace()!=null && !sei.getTargetNamespace().equals(JaxWsUtils.composeJaxWsTargetNamespaceByPackage(type.getPackageFragment().getElementName()))) {
			paramValuePairs.add(createParamValue(WSAnnotationFeatures.TARGET_NAMESPACE_ATTRIBUTE, sei.getTargetNamespace()));
		}
		
		return AnnotationFactory.createAnnotation(WSAnnotationFeatures.WS_ANNOTATION, type, paramValuePairs);	
	}
}