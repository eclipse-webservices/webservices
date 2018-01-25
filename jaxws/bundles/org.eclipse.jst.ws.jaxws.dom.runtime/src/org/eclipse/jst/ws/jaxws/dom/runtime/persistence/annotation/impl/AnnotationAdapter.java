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
package org.eclipse.jst.ws.jaxws.dom.runtime.persistence.annotation.impl;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.annotation.IAnnotationAdapter;
import org.eclipse.jst.ws.jaxws.utils.annotations.IAnnotation;

import static org.eclipse.jst.ws.jaxws.utils.ContractChecker.nullCheckParam;

/**
 * Implementation of {@link ILocatorAdapter}.
 * 
 * @author Georgi Vachkov
 */
public class AnnotationAdapter extends AdapterImpl implements IAnnotationAdapter
{
	private final Map<String, IAnnotation<? extends IJavaElement>> annotationLocators;
	
	/**
	 * Constructor
	 */
	public AnnotationAdapter() {
		annotationLocators = new HashMap<String, IAnnotation<? extends IJavaElement>>();
	}
	
	public IAnnotation<? extends IJavaElement> getAnnotation(final String annotationFQName) 
	{
		nullCheckParam(annotationFQName, "annotationFQName");//$NON-NLS-1$
		return annotationLocators.get(annotationFQName);
	}

	public void addAnnotation(final String annFQName, final IAnnotation<? extends IJavaElement> annotation) 
	{
		nullCheckParam(annFQName, "annFQName");//$NON-NLS-1$
		annotationLocators.put(annFQName, annotation);
	}

	@Override
	public boolean isAdapterForType(Object type) {
		return IAnnotationAdapter.class == type;
	}
}
