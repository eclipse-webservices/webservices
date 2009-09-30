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
package org.eclipse.jst.ws.jaxws.dom.runtime.persistence.annotation;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.annotation.impl.AnnotationAdapter;

/**
 * Factory that adapts DOM objects to {@link IAnnotationAdapter} interface.
 * 
 * @author Georgi Vachkov
 */
public class AnnotationAdapterFactory extends AdapterFactoryImpl
{
	/**
	 * Singleton - use this static instance to adapt objects
	 */
	public static final AnnotationAdapterFactory INSTANCE = new AnnotationAdapterFactory();
	
	private AnnotationAdapterFactory() {
		// singleton
	}
	
	@Override
	public boolean isFactoryForType(Object type) {
		return type == IAnnotationAdapter.class;
	}

	@Override
	protected Adapter createAdapter(Notifier target) 
	{
		return new AnnotationAdapter();
	}
}
