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

import static org.eclipse.jst.ws.jaxws.dom.runtime.persistence.WPAnnotationFeatures.HEADER_ATTRIBUTE;
import static org.eclipse.jst.ws.jaxws.dom.runtime.persistence.WPAnnotationFeatures.MODE_ATTRIBUTE;
import static org.eclipse.jst.ws.jaxws.dom.runtime.persistence.WPAnnotationFeatures.NAME_ATTRIBUTE;
import static org.eclipse.jst.ws.jaxws.dom.runtime.persistence.WPAnnotationFeatures.PART_NAME_ATTRIBUTE;
import static org.eclipse.jst.ws.jaxws.dom.runtime.persistence.WPAnnotationFeatures.TARGET_NAMESPACE_ATTRIBUTE;
import static org.eclipse.jst.ws.jaxws.dom.runtime.persistence.WPAnnotationFeatures.WP_ANNOTATION;

import java.util.Collection;
import java.util.LinkedList;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.ITypeParameter;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.DomPackage;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebMethod;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebParam;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.WebParamKind;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.IAnnotationSerializer;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.IModelElementSynchronizer;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.WPAnnotationFeatures;
import org.eclipse.jst.ws.jaxws.utils.annotations.IAnnotation;
import org.eclipse.jst.ws.jaxws.utils.annotations.IAnnotationInspector;

/**
 * Class that synchronizes the parameters for {@link IWebMethod} object;
 * 
 * @author 
 */
public class ParameterSynchronizer extends ElementSynchronizerImpl
{
	private static final String RETURN_PARAM_NAME	= "return";//$NON-NLS-1$
	private static final String HOLDER_CLASS_NAME = "Holder"; //$NON-NLS-1$
	private static final String HOLDER_CLASS_FQNAME = "javax.xml.ws.Holder";//$NON-NLS-1$
	
	public ParameterSynchronizer(IModelElementSynchronizer parent)
	{
		super(parent);
	}

	public void synchronizeParameters(IWebMethod wm, IMethod method, IAnnotationInspector inspector) throws JavaModelException
	{
		final Collection<IWebParam> mergedParams = new LinkedList<IWebParam>();
		if (!Signature.SIG_VOID.equals(method.getReturnType()))
		{
			final IWebParam retParam = obtainInstance(wm, RETURN_PARAM_NAME);
			mergeParam(retParam, 0, wm, method, method.getReturnType(), inspector);
			mergedParams.add(retParam);
		}
		final String[] names = method.getParameterNames();
		final String[] typeSignatures = method.getParameterTypes(); 
		for (int i = 0; i < names.length; i++)
		{
			final IWebParam param = obtainInstance(wm, names[i]);
			mergeParam(param, i, wm, method, typeSignatures[i], inspector);
			mergedParams.add(param);
		}
		wm.getParameters().retainAll(mergedParams);
	}

	private void mergeParam(IWebParam webParam, int pos, IWebMethod webMethod, IMethod method, String typeSign, IAnnotationInspector inspector) throws JavaModelException
	{
		final ITypeParameter typeParam = method.getTypeParameter(webParam.getImplementation());
		assert typeParam!= null;
		
		final IAnnotation<ITypeParameter> wpAnnotation = isWebResult(webParam) ? null : inspector.inspectParam(typeParam, WP_ANNOTATION);
				
		if (! typeSign.equals(webParam.getTypeName())) {
			util().setFeatureValue(webParam, DomPackage.IWEB_PARAM__TYPE_NAME, typeSign);
		}
		
		final WebParamKind kind = isWebResult(webParam) ? WebParamKind.OUT : calcKind(wpAnnotation, method, typeSign);
		if (!kind.equals(webParam.getKind())) {
			webParam.setKind(kind);
		}
		
		final boolean isHeader = calcHeader(wpAnnotation);
		if (webParam.isHeader() ^ isHeader) {
			webParam.setHeader(isHeader);
		}
		
		final String name = isWebResult(webParam) ? defCalc().calcWebResultDefaultName(webMethod) : calcName(wpAnnotation, webMethod, pos);
		if (!name.equals(webParam.getName())) {
			webParam.setName(name);
		}
		
		final String partName = calcPartName(wpAnnotation, name);
		if (!partName.equals(webParam.getPartName())) {
			webParam.setPartName(partName);
		}
		
		final String targetNamespace = calcTargetNamespace(wpAnnotation, webMethod, webParam);
		if (!targetNamespace.equals(webParam.getTargetNamespace())) {
			webParam.setTargetNamespace(targetNamespace);
		}		
		
		resource().getSerializerFactory().adapt(webParam, IAnnotationSerializer.class);		
		adaptToLocationInterface(webParam, WP_ANNOTATION, wpAnnotation);
	}
	
	private boolean isWebResult(final IWebParam param)
	{
		return param.getImplementation().equals(RETURN_PARAM_NAME);
	}
	
	protected String resolveFullyQualifiedName(final IMethod method, final String typeSignature) throws JavaModelException, IllegalArgumentException
	{
		if (Signature.getTypeSignatureKind(typeSignature) == Signature.BASE_TYPE_SIGNATURE)
		{
			return Signature.toString(typeSignature);
		}
		
		final String[][] resolvedTypes = method.getDeclaringType().resolveType(Signature.toString(typeSignature));
		if (resolvedTypes == null)
		{
			return "";//$NON-NLS-1$
		}
		final String pack = resolvedTypes[0][0];
		final String className = resolvedTypes[0][1];
		if (pack != null && pack.length() != 0)
		{
			return pack + "." + className;//$NON-NLS-1$
		} else
		{
			return className;
		}
	}
	
	protected WebParamKind calcKind(final IAnnotation<ITypeParameter> parmAnnotation, final IMethod method, final String typeSignature) throws JavaModelException, IllegalArgumentException
	{
		if (parmAnnotation != null && parmAnnotation.getPropertyValue(MODE_ATTRIBUTE) != null)
		{
			final String mode = parmAnnotation.getPropertyValue(MODE_ATTRIBUTE);
			if (WPAnnotationFeatures.WEB_PARAM_MODE_INOUT.endsWith(mode))
			{
				return WebParamKind.INOUT;
			}
			if (WPAnnotationFeatures.WEB_PARAM_MODE_IN.endsWith(mode))
			{
				return WebParamKind.IN;
			}
			if (WPAnnotationFeatures.WEB_PARAM_MODE_OUT.endsWith(mode))
			{
				return WebParamKind.OUT;
			}
		} 
		
		if (isPossiblyHolderClass(typeSignature)) {
			final String typeFQName = resolveFullyQualifiedName(method, typeSignature);
			if (typeFQName.startsWith(HOLDER_CLASS_FQNAME)) {
				return WebParamKind.INOUT;
			}
		}
		
		return WebParamKind.IN;
	}
	
	/**
	 * Added due to performance issues CSN 0120061532 0002256925 2009.
	 * The idea is that before calling {@link #resolveFullyQualifiedName(IMethod, String)}
	 * we check if the class is potentially Holder.
	 */
	private boolean isPossiblyHolderClass(final String typeSignature) {	
		final String erasuteTypeName = Signature.getSignatureSimpleName(Signature.getTypeErasure(typeSignature));
		if (HOLDER_CLASS_NAME.equals(erasuteTypeName)) {
			return true;
		}
		
		return false;
	}
	
	private String calcName(IAnnotation<ITypeParameter> parmAnnotation, IWebMethod webMethod, int paramPosition)
	{
		if (parmAnnotation != null && parmAnnotation.getPropertyValue(NAME_ATTRIBUTE) != null) {
			return parmAnnotation.getPropertyValue(NAME_ATTRIBUTE);
		}
		
		return defCalc().calcWebParamDefaultName(webMethod, paramPosition);
	}
	
	private String calcPartName(final IAnnotation<ITypeParameter> wpAnnotation, final String name) 
	{
		if (wpAnnotation!=null && wpAnnotation.getPropertyValue(PART_NAME_ATTRIBUTE)!=null) {
			return wpAnnotation.getPropertyValue(PART_NAME_ATTRIBUTE);
		}
		
		return name;
	}
	
	private boolean calcHeader(final IAnnotation<ITypeParameter> wpAnnotation)
	{
		if (wpAnnotation!=null && wpAnnotation.getPropertyValue(HEADER_ATTRIBUTE)!=null) {
			return Boolean.parseBoolean(wpAnnotation.getPropertyValue(HEADER_ATTRIBUTE));
		}
		
		return false;
	}
	
	private String calcTargetNamespace(final IAnnotation<ITypeParameter> wpAnnotation, final IWebMethod webMethod, final IWebParam webParam)
	{
		if (wpAnnotation!=null && wpAnnotation.getPropertyValue(TARGET_NAMESPACE_ATTRIBUTE)!=null) {
			return wpAnnotation.getPropertyValue(TARGET_NAMESPACE_ATTRIBUTE);
		}
		
		return defCalc().calcWebParamDefaultTargetNS(webMethod, webParam);
	}
	
	private IWebParam obtainInstance(IWebMethod wm, String impl)
	{
		final IWebParam existing = findWebParamByImplName(wm, impl);
		if (existing != null)
		{
			return existing;
		}
		final IWebParam newParam = domFactory().createIWebParam();
		util().setFeatureValue(newParam, DomPackage.IWEB_PARAM__IMPLEMENTATION, impl);
		wm.getParameters().add(newParam);
		return newParam;
	}
	
	private IWebParam findWebParamByImplName(final IWebMethod wm, final String impl)
	{
		for (IWebParam param : wm.getParameters())
		{
			if (impl.equals(param.getImplementation()))
			{
				return param;
			}
		}
		return null;
	}
}
