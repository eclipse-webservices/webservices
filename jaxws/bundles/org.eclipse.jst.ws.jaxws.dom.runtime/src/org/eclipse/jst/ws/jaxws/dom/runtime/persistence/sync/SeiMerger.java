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

import static org.eclipse.jst.ws.jaxws.dom.runtime.persistence.SBAnnotationFeatures.SB_ANNOTATION;
import static org.eclipse.jst.ws.jaxws.dom.runtime.persistence.WSAnnotationFeatures.NAME_ATTRIBUTE;
import static org.eclipse.jst.ws.jaxws.dom.runtime.persistence.WSAnnotationFeatures.TARGET_NAMESPACE_ATTRIBUTE;
import static org.eclipse.jst.ws.jaxws.dom.runtime.persistence.WSAnnotationFeatures.WS_ANNOTATION;
import static org.eclipse.jst.ws.jaxws.utils.ContractChecker.nullCheckParam;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.DomPackage;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.SOAPBindingParameterStyle;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.SOAPBindingStyle;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.SOAPBindingUse;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.IModelElementSynchronizer;
import org.eclipse.jst.ws.jaxws.utils.JaxWsUtils;
import org.eclipse.jst.ws.jaxws.utils.annotations.IAnnotation;
import org.eclipse.jst.ws.jaxws.utils.annotations.IAnnotationInspector;

/**
 * Synchronizer that merges content of {@link IServiceEndpointInterface} with the current content of 
 * the class implementing it.  
 * 
 * @author Georgi Vachkov
 */
public class SeiMerger extends ElementSynchronizerImpl
{
	private AbstractMethodSynchronizer methodSynchronizer;
	
	/**
	 * Constructor
	 * @param parent
	 * @param methodSync the synchronizer that takes care for the SEI methods
	 */
	public SeiMerger(IModelElementSynchronizer parent, AbstractMethodSynchronizer methodSync) 
	{
		super(parent);
		nullCheckParam(methodSync);
		this.methodSynchronizer = methodSync;
	}
		
	protected void merge(IServiceEndpointInterface sei, IAnnotation<IType> wsAnnotation, IAnnotationInspector inspector) throws JavaModelException
	{
		final String seiName = wsAnnotation.getPropertyValue(NAME_ATTRIBUTE) == null ? 
				JaxWsUtils.getDefaultPorttypeName(wsAnnotation.getAppliedElement().getFullyQualifiedName()) : wsAnnotation.getPropertyValue(NAME_ATTRIBUTE);
				
		if (!seiName.equals(sei.getName())) {
			util().setFeatureValue(sei, DomPackage.ISERVICE_ENDPOINT_INTERFACE__NAME, seiName);
		}
				
		// add targetNamespace
		final String targetNs = extractTargetNamespace(wsAnnotation);
		if (!targetNs.equals(sei.getTargetNamespace())) {
			util().setFeatureValue(sei, DomPackage.ISERVICE_ENDPOINT_INTERFACE__TARGET_NAMESPACE, targetNs);
		}
		
		mergeSoapBinding(sei, inspector);
		methodSynchronizer.synchronizeMethods(sei, wsAnnotation.getAppliedElement(), inspector);
		
		adaptToLocationInterface(sei, WS_ANNOTATION, wsAnnotation);
	}
	
	private String extractTargetNamespace(final IAnnotation<IType> wsAnnotation)
	{
		// add targetNamespace
		final String targetNs = wsAnnotation.getPropertyValue(TARGET_NAMESPACE_ATTRIBUTE);
		if (targetNs==null) {
			return JaxWsUtils.composeJaxWsTargetNamespaceByPackage(wsAnnotation.getAppliedElement().getPackageFragment().getElementName());
		}	
		
		return targetNs;
	}
	
	private void mergeSoapBinding(final IServiceEndpointInterface sei, final IAnnotationInspector inspector) throws JavaModelException
	{
		final IAnnotation<IType> sbAnnotation = inspector.inspectType(SB_ANNOTATION);
		
		final SOAPBindingStyle style = defCalc().defineSBStyle(sbAnnotation);
		if (sei.getSoapBindingStyle() != style) {
			sei.setSoapBindingStyle(style);
		}
		
		final SOAPBindingUse use = defCalc().defineSBUse(sbAnnotation);
		if (sei.getSoapBindingUse() != use) {
			sei.setSoapBindingUse(use);
		}
		
		final SOAPBindingParameterStyle paramStyle = defCalc().defineSBParameterStyle(sbAnnotation);
		if (sei.getSoapBindingParameterStyle() != paramStyle) {
			sei.setSoapBindingParameterStyle(paramStyle);
		}
		
		adaptToLocationInterface(sei, SB_ANNOTATION, sbAnnotation);
	}
}
