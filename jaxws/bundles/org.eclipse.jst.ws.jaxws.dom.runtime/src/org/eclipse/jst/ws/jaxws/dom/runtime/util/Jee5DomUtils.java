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
package org.eclipse.jst.ws.jaxws.dom.runtime.util;

import static org.eclipse.jst.ws.jaxws.dom.runtime.persistence.WPAnnotationFeatures.WP_ANNOTATION;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebMethod;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebParam;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.SOAPBindingParameterStyle;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.SOAPBindingStyle;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.WebParamKind;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.WPAnnotationFeatures;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.annotation.AnnotationAdapterFactory;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.annotation.IAnnotationAdapter;
import org.eclipse.jst.ws.jaxws.utils.annotations.IAnnotation;

/**
 * Utility for some specification specific logics that are frequently used
 * while dealing with DOM objects.
 * 
 * @author Georgi Vachkov
 */
public class Jee5DomUtils 
{
	/** the singleton instance */
	private static Jee5DomUtils instance;
	
	private Jee5DomUtils() {
		// singleton
	}
	
	/**
	 * Method that decides if the attribute 'partName' is used at all according 
	 * to JSR-181 section 4.4.1 - attribute 'partName' column 'meaning'.
	 * @param webParam the webParam to be checked
	 * @return <code>true</code> in case partName is used
	 */
	public boolean isPartNameUsed(final IWebParam webParam)
	{
		final IWebMethod webMethod = (IWebMethod)webParam.eContainer();
		
		return webMethod.getSoapBindingStyle()==SOAPBindingStyle.RPC || 
			webMethod.getSoapBindingParameterStyle()==SOAPBindingParameterStyle.BARE;
	}
	
	/**
	 * Method that decides is the attribute 'name' is used at all
	 * according to JSR-181 section 4.4.1 - attribute 'name' column 'meaning'
	 * @param webParam
	 * @return return <code>true</code> in case name is used
	 */
	public boolean isNameUsed(final IWebParam webParam)
	{
		final IWebMethod webMethod = (IWebMethod)webParam.eContainer();
		if( webMethod.getSoapBindingStyle()==SOAPBindingStyle.DOCUMENT || webParam.isHeader() ) {
			return true;
		}
		
		final IAnnotation<? extends IJavaElement> annotation =  findAnnotation(webParam, WP_ANNOTATION);
		if (annotation==null) {
			return false;
		}
		
		return annotation.getPropertyValue(WPAnnotationFeatures.PART_NAME_ATTRIBUTE)==null;
	}
	
	/**
	 * Helper method to extract annotation with fully qualified name <code>annFQName</code>
	 * @param eObject the object to search in
	 * @param annFQName the FQ name of searched annotation 
	 * @return found annotation or <code>null</code>
	 */
	public IAnnotation<? extends IJavaElement> findAnnotation(final EObject eObject, final String annFQName) 
	{
		final IAnnotationAdapter adapter = (IAnnotationAdapter)AnnotationAdapterFactory.INSTANCE.adapt(eObject, IAnnotationAdapter.class);
		if (adapter!=null) {
			return adapter.getAnnotation(annFQName);
		}
		
		return null;
	}
	
	/**
	 * Method that decides if the param name is required to be put in the annotation according 
	 * to JSR-181 section 4.4.1 - attribute 'name' column 'meaning'.
	 * @param webParam
	 * @return <code>true</code> if the name attribute should be put on annotation
	 */
	public boolean isNameRequired(final IWebParam webParam)
	{
		final IWebMethod webMethod = (IWebMethod)webParam.eContainer();
		
		if (webMethod.getSoapBindingStyle()==SOAPBindingStyle.DOCUMENT &&
			webMethod.getSoapBindingParameterStyle()==SOAPBindingParameterStyle.BARE &&
			(webParam.getKind()==WebParamKind.INOUT || webParam.getKind()==WebParamKind.OUT)) {
			return true;
		}
		
		return false;
	}	
	
	/**
	 * @return the singleton instance
	 */
	public static Jee5DomUtils getInstance()
	{
		if (instance == null) {
			instance = new Jee5DomUtils();
		}
		
		return instance;
	}
}
