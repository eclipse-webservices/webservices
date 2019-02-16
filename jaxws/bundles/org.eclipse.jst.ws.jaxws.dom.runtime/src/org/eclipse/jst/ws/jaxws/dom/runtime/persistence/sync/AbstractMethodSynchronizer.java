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
import static org.eclipse.jst.ws.jaxws.dom.runtime.persistence.WMAnnotationFeatures.WM_ANNOTATION;
import static org.eclipse.jst.ws.jaxws.dom.runtime.persistence.WMAnnotationFeatures.WM_EXCLUDED_ATTRIBUTE;
import static org.eclipse.jst.ws.jaxws.dom.runtime.persistence.WMAnnotationFeatures.WM_NAME_ATTRIBUTE;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.DomPackage;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebMethod;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.SOAPBindingParameterStyle;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.SOAPBindingStyle;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.SOAPBindingUse;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.IAnnotationSerializer;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.IModelElementSynchronizer;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.SBAnnotationFeatures;
import org.eclipse.jst.ws.jaxws.utils.annotations.IAnnotation;
import org.eclipse.jst.ws.jaxws.utils.annotations.IAnnotationInspector;


public abstract class AbstractMethodSynchronizer extends ElementSynchronizerImpl
{
	private ParameterSynchronizer parameterSynchronizer = new ParameterSynchronizer(this);
	
	protected interface IMethodVisitor
	{
		void visit(IMethod method, IAnnotationInspector inspector) throws JavaModelException;
	}

	public AbstractMethodSynchronizer(IModelElementSynchronizer parent)
	{
		super(parent);
	}

	protected abstract void visitExposableMethods(IMethodVisitor visitor, ITypeHierarchy hierarchy, IAnnotationInspector inspector) throws JavaModelException;

	public void synchronizeMethods(final IServiceEndpointInterface sei, final IType seiType, final IAnnotationInspector inspector) throws JavaModelException
	{
		final Collection<IWebMethod> mergedMethods = new LinkedList<IWebMethod>();
		final ITypeHierarchy hierarchy = seiType.newSupertypeHierarchy(null);
		final IMethodVisitor visitor = new IMethodVisitor()
		{
			public void visit(IMethod method, IAnnotationInspector inspector) throws JavaModelException
			{
				final IWebMethod wm = obtainInstance(sei, method);
				if (mergedMethods.contains(wm))
				{
					/**
					 * check whether this method has been processed in some of the child classes. If so
					 * skip it and continue. Only the method in the last child declaring this method needs
					 * to be processed because each child is overriding the properties for the every overriden
					 * method.
					 */
					return;
				}
				mergeMethod(wm, method, inspector);
				mergedMethods.add(wm);
			}
		};
		visitExposableMethods(visitor, hierarchy, inspector);
		removeLeftMethods(sei, mergedMethods);

	}

	private IWebMethod obtainInstance(IServiceEndpointInterface sei, IMethod method) throws JavaModelException
	{
		for (IWebMethod wm : sei.getWebMethods())
		{
			if (util().calcImplementation(method).equals(wm.getImplementation()))
			{
				return wm;
			}
		}
		final IWebMethod newWebMethod = domFactory().createIWebMethod();
		sei.getWebMethods().add(newWebMethod);
		util().setFeatureValue(newWebMethod, DomPackage.IWEB_METHOD__IMPLEMENTATION, util().calcImplementation(method));
		return newWebMethod;
	}

	private void mergeMethod(IWebMethod wm, IMethod m, IAnnotationInspector inspector) throws JavaModelException
	{
		final IAnnotation<IMethod> wmAnnotation = inspector.inspectMethod(m, WM_ANNOTATION);
		final boolean excluded;
		final String name;
		if (wmAnnotation == null)
		{
			excluded = false;
			name = m.getElementName();
		} else
		{
			excluded = wmAnnotation.getPropertyValue(WM_EXCLUDED_ATTRIBUTE) == null ? false : Boolean.parseBoolean(wmAnnotation
											.getPropertyValue(WM_EXCLUDED_ATTRIBUTE));
			name = wmAnnotation.getPropertyValue(WM_NAME_ATTRIBUTE) == null ? m.getElementName() : wmAnnotation.getPropertyValue(WM_NAME_ATTRIBUTE);
		}
		if (!name.equals(wm.getName()))
		{
			util().setFeatureValue(wm, DomPackage.IWEB_METHOD__NAME, name);
		}
		if (excluded != wm.isExcluded())
		{
			util().setFeatureValue(wm, DomPackage.IWEB_METHOD__EXCLUDED, excluded);
		}
		mergeSoapBinding(wm, m, inspector);
		parameterSynchronizer.synchronizeParameters(wm, m, inspector);
		
		resource().getSerializerFactory().adapt(wm, IAnnotationSerializer.class);
		adaptToLocationInterface(wm, WM_ANNOTATION, wmAnnotation);
	}
	
	private void mergeSoapBinding(final IWebMethod webMethod, final IMethod method, final IAnnotationInspector inspector) throws JavaModelException
	{
		final IAnnotation<IMethod> sbAnnotation = inspector.inspectMethod(method, SB_ANNOTATION);
		final IAnnotation<IType> seiSbAnnotation = inspector.inspectType(SB_ANNOTATION);
		
		final SOAPBindingStyle style = defineSBStyle(sbAnnotation, seiSbAnnotation);
		if (webMethod.getSoapBindingStyle() != style) {
			webMethod.setSoapBindingStyle(style);
		}
		
		final SOAPBindingUse use = defineSBUse(sbAnnotation, seiSbAnnotation);
		if (webMethod.getSoapBindingUse() != use) {
			webMethod.setSoapBindingUse(use);
		}
		
		final SOAPBindingParameterStyle paramStyle =defineSBParameterStyle(sbAnnotation, seiSbAnnotation);
		if (webMethod.getSoapBindingParameterStyle() != paramStyle) {
			webMethod.setSoapBindingParameterStyle(paramStyle);
		}
		
		adaptToLocationInterface(webMethod, SB_ANNOTATION, sbAnnotation);
	}
	
	private SOAPBindingStyle defineSBStyle(final IAnnotation<IMethod> methodSBAnnotation, final IAnnotation<IType> seiSbAnnotation)
	{
		final String value = methodSBAnnotation!=null ? methodSBAnnotation.getPropertyValue(SBAnnotationFeatures.STYLE_ATTRIBUTE) : null;
		if(value==null) {
			return defCalc().defineSBStyle(seiSbAnnotation);
		}
		
		if (SBAnnotationFeatures.SB_STYLE_RPC.endsWith(value)) {
			return SOAPBindingStyle.RPC;
		} 
		
		if(SBAnnotationFeatures.SB_STYLE_DOCUMENT.endsWith(value)) {
			return SOAPBindingStyle.DOCUMENT;
		}
			
		return defCalc().defineSBStyle(seiSbAnnotation);
	}
	
	private SOAPBindingUse defineSBUse(final IAnnotation<IMethod> methodSBAnnotation, final IAnnotation<IType> seiSbAnnotation) 
	{
		final String value = methodSBAnnotation!=null ? methodSBAnnotation.getPropertyValue(SBAnnotationFeatures.USE_ATTRIBUTE) : null;
		if (value==null) {		
			return defCalc().defineSBUse(seiSbAnnotation);
		}
		
		if (SBAnnotationFeatures.SB_USE_ENCODED.endsWith(value)) {
			return SOAPBindingUse.ENCODED;
		}
		
		if (SBAnnotationFeatures.SB_USE_LITERAL.endsWith(value)) {
			return SOAPBindingUse.LITERAL;
		}
		
		return defCalc().defineSBUse(seiSbAnnotation);
	}
	
	private SOAPBindingParameterStyle defineSBParameterStyle(final IAnnotation<IMethod> methodSBAnnotation, final IAnnotation<IType> seiSbAnnotation)
	{
		final String value = methodSBAnnotation!=null ? methodSBAnnotation.getPropertyValue(SBAnnotationFeatures.PARAMETER_STYLE_ATTRIBUTE) : null;
		if (value==null) {
			return defCalc().defineSBParameterStyle(seiSbAnnotation);
		}
		
		if (SBAnnotationFeatures.SB_PARAMETER_STYLE_BARE.endsWith(value)) {
			return SOAPBindingParameterStyle.BARE;
		}
		
		if (SBAnnotationFeatures.SB_PARAMETER_STYLE_WRAPPED.endsWith(value)) {
			return SOAPBindingParameterStyle.WRAPPED;
		}		
		
		return defCalc().defineSBParameterStyle(seiSbAnnotation);
	}

	private void removeLeftMethods(IServiceEndpointInterface sei, Collection<IWebMethod> mergedMethods)
	{
		final Iterator<IWebMethod> iter = sei.getWebMethods().iterator();
		while (iter.hasNext())
		{
			if (!mergedMethods.contains(iter.next()))
			{
				iter.remove();
			}
		}
	}


}
