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

import static org.eclipse.jst.ws.jaxws.dom.runtime.persistence.WPAnnotationFeatures.HEADER_ATTRIBUTE;
import static org.eclipse.jst.ws.jaxws.dom.runtime.persistence.WPAnnotationFeatures.NAME_ATTRIBUTE;
import static org.eclipse.jst.ws.jaxws.dom.runtime.persistence.WPAnnotationFeatures.PART_NAME_ATTRIBUTE;
import static org.eclipse.jst.ws.jaxws.dom.runtime.persistence.WPAnnotationFeatures.TARGET_NAMESPACE_ATTRIBUTE;
import static org.eclipse.jst.ws.jaxws.dom.runtime.persistence.WPAnnotationFeatures.WP_ANNOTATION;

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
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebParam;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.WebParamKind;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.JaxWsWorkspaceResource;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.WPAnnotationFeatures;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.annotation.AnnotationAdapterFactory;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.annotation.IAnnotationAdapter;
import org.eclipse.jst.ws.jaxws.dom.runtime.util.Jee5DomUtils;
import org.eclipse.jst.ws.jaxws.utils.annotations.AnnotationFactory;
import org.eclipse.jst.ws.jaxws.utils.annotations.IAnnotation;
import org.eclipse.jst.ws.jaxws.utils.annotations.IParamValuePair;
import org.eclipse.jst.ws.jaxws.utils.annotations.IValue;
import org.eclipse.jst.ws.jaxws.utils.logging.Logger;

/**
 * Class that serializes {@link IWebParam} objects to WebParam annotation in underlying java
 * class. Checks the attribute values and omits the defaults.
 *  
 * @author Georgi Vachkov
 */
public class ParameterSerializerAdapter extends AbstractSerializerAdapter
{
	private final Jee5DomUtils jee5Utils = Jee5DomUtils.getInstance();
	
	/**
	 * Constructor
	 * @param resource
	 */
	public ParameterSerializerAdapter(JaxWsWorkspaceResource resource) {
		super(resource);
	}
	
	@Override
	public void notifyChanged(final Notification msg) 
	{
		if (!resource().isSaveEnabled()) {
			return;
		}
		
		final EStructuralFeature feature = (EStructuralFeature)msg.getFeature();
		if (feature.getFeatureID()==DomPackage.ISERVICE_ENDPOINT_INTERFACE__NAME) 
		{
			try {
				changePartName(msg);
			} 
			catch (JavaModelException e) {
				(new Logger()).logError(e.getMessage(), e);
			}
		}	
		
		super.notifyChanged(msg);
	}
	
	private void changePartName(final Notification msg) throws JavaModelException
	{
		final IWebParam webParam = (IWebParam)getTarget();
		if (jee5Utils.isPartNameUsed(webParam)) 
		{
			final IAnnotationAdapter annAdapter = (IAnnotationAdapter )AnnotationAdapterFactory.INSTANCE.adapt(webParam, IAnnotationAdapter.class);
			final IAnnotation<? extends IJavaElement> ann = annAdapter.getAnnotation(WPAnnotationFeatures.WP_ANNOTATION);
			if (ann!=null && ann.getPropertyValue(WPAnnotationFeatures.PART_NAME_ATTRIBUTE)!=null) {
				return;
			}
		}
		
		if (checkValue(msg)) {
			putValue(webParam, DomPackage.Literals.IWEB_PARAM__PART_NAME, msg.getNewValue());
		}
	}
	
	@Override
	protected boolean isAnnotationRequired() {
		return false;
	}
	
	@Override
	protected IAnnotation<? extends IJavaElement> getAnnotation() throws JavaModelException 
	{
		final IWebParam webParam = (IWebParam)getTarget();
		final IWebMethod webMethod = (IWebMethod)webParam.eContainer();
		if (webMethod==null || webMethod.eContainer()==null) {
			return null;
		}
		
		final IType seiType = findType(webMethod, ((IServiceEndpointInterface)webMethod.eContainer()).getImplementation());
		final IMethod method = util().findMethod(seiType, webMethod);
		
		return AnnotationFactory.createAnnotation(WP_ANNOTATION, 
				method.getTypeParameter(webParam.getImplementation()),
				defineParamValues(webParam, webMethod));	
	}
	
	protected Set<IParamValuePair> defineParamValues(final IWebParam webParam, final IWebMethod webMethod)
	{
		final Set<IParamValuePair> valuePairs = createParamValueSortedTreeSet();
		
		if (webParam.getKind()!=WebParamKind.IN) {
			valuePairs.add(createModeValuePair(webParam));
		}
		
		if (webParam.isHeader()) {
			valuePairs.add(createParamValue(HEADER_ATTRIBUTE, true));
		}

		final String defName = defCalc().calcWebParamDefaultName(webParam);
		if (jee5Utils.isNameRequired(webParam) || 
			(!webParam.getName().equals(defName) && jee5Utils.isNameUsed(webParam))) 
		{
			valuePairs.add(createParamValue(NAME_ATTRIBUTE, webParam.getName()));
		}
		
		if (jee5Utils.isPartNameUsed(webParam) && !webParam.getPartName().equals(defName)) {
			valuePairs.add(createParamValue(PART_NAME_ATTRIBUTE, webParam.getPartName()));
		}

		if (webParam.getTargetNamespace()!=null) 
		{
			final String defNs = defCalc().calcWebParamDefaultTargetNS(webMethod, webParam);
			if (webParam.getTargetNamespace().trim().length() != 0 && !webParam.getTargetNamespace().equals(defNs)) {
				valuePairs.add(createParamValue(TARGET_NAMESPACE_ATTRIBUTE, webParam.getTargetNamespace()));
			}
		}
		
		return valuePairs;
	}	
	
	protected IParamValuePair createModeValuePair(final IWebParam webParam) 
	{
		IValue value = null;
		switch (webParam.getKind()) 
		{
		case OUT:
			value = AnnotationFactory.createQualifiedNameValue(WPAnnotationFeatures.WEB_PARAM_MODE_OUT);
			break;
		case INOUT:
			value = AnnotationFactory.createQualifiedNameValue(WPAnnotationFeatures.WEB_PARAM_MODE_INOUT);
			break;
		default:
			return null;
		}
		
		return AnnotationFactory.createParamValuePairValue(WPAnnotationFeatures.MODE_ATTRIBUTE, value);
	}
	
	protected boolean hasParamWithSameName(final IWebParam param, final String newName)
	{
		final IWebMethod webMethod = (IWebMethod)param.eContainer();
		for (IWebParam webParam : webMethod.getParameters()) 
		{
			if(webParam==param) {
				continue;
			}
			
			if(webParam.getName().equals(newName)) {
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	protected boolean checkValue(final Notification msg) 
	{
		if(((EStructuralFeature)msg.getFeature()).getFeatureID()==DomPackage.IWEB_PARAM__TARGET_NAMESPACE) {
			return true;
		}
		
		return super.checkValue(msg);
	}
}