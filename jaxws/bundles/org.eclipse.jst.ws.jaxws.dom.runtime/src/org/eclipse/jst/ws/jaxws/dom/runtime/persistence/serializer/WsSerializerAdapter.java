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

import static org.eclipse.jst.ws.jaxws.utils.ContractChecker.nullCheckParam;

/**
 * Adapter that on {@link IWebService} object change saves the annotation to 
 * the underlying compilation unit. Uses {@link JaxWsWorkspaceResource} to define 
 * if save operation is enabled.
 * 
 * @author Georgi Vachkov
 */
public class WsSerializerAdapter extends AbstractSerializerAdapter
{
	/**
	 * Constructor 
	 * @param resource
	 */
	public WsSerializerAdapter(JaxWsWorkspaceResource resource) 
	{
		super(resource);
	}
	
	@Override
	protected boolean isAnnotationRequired() {
		return true;
	}		

	@Override
	protected IAnnotation<? extends IJavaElement> getAnnotation() throws JavaModelException 
	{
		assert getTarget() instanceof IWebService;
		
		return createIAnnotation((IWebService)getTarget(), resource().javaModel());
	}
	
	protected IAnnotation<? extends IJavaElement> createIAnnotation(final IWebService ws, final IJavaModel javaModel) throws JavaModelException
	{
		nullCheckParam(ws, "ws");//$NON-NLS-1$
		nullCheckParam(javaModel, "javaModel");//$NON-NLS-1$
		
		final IType type = findType(ws, ws.getImplementation());
		if (type==null) {
			return null;
		}
		
		final Set<IParamValuePair> paramValuePairs = createParamValueSortedTreeSet();
		
		final IServiceEndpointInterface sei = ws.getServiceEndpoint();
		if (sei!=null) 
		{			
			if (sei.isImplicit()) 
			{
				String seiName = sei.getName();
				if (!JaxWsUtils.getDefaultPorttypeName(sei.getImplementation()).equals(seiName)) {
					paramValuePairs.add(createParamValue(WSAnnotationFeatures.NAME_ATTRIBUTE, ws.getServiceEndpoint().getName()));
				}
			} else {
				paramValuePairs.add(createParamValue(WSAnnotationFeatures.ENDPOINT_INTERFACE_ATTRIBUTE, ws.getServiceEndpoint().getImplementation()));
			}
		}
		
		if (ws.getName()!=null && 
			!ws.getName().equals(JaxWsUtils.getDefaultServiceName(ws.getImplementation()))) 
		{
			paramValuePairs.add(createParamValue(WSAnnotationFeatures.SERVICE_NAME_ATTRIBUTE, ws.getName()));
		}				
		if (ws.getPortName()!=null && 
			!ws.getPortName().equals(JaxWsUtils.getDefaultPortName(ws.getImplementation()))) 
		{
			paramValuePairs.add(createParamValue(WSAnnotationFeatures.PORT_NAME_ATTRIBUTE, ws.getPortName()));
		}
		if (ws.getTargetNamespace()!=null && 
			!ws.getTargetNamespace().equals(JaxWsUtils.composeJaxWsTargetNamespaceByPackage(type.getPackageFragment().getElementName()))) 
		{
			paramValuePairs.add(createParamValue(WSAnnotationFeatures.TARGET_NAMESPACE_ATTRIBUTE, ws.getTargetNamespace()));
		}
		if (ws.getWsdlLocation()!=null) {
			paramValuePairs.add(createParamValue(WSAnnotationFeatures.WSDL_LOCATION_ATTRIBUTE, ws.getWsdlLocation()));
		}
		
		return AnnotationFactory.createAnnotation(WSAnnotationFeatures.WS_ANNOTATION, type, paramValuePairs);
	}
}