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
package org.eclipse.jst.ws.jaxws.dom.runtime;

import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * Interface that provides default values for DOM object's properties.
 * Usually this interface should be used by clients that need to revert some
 * property value to the default one.
 * 
 * @author Georgi Vachkov
 */
public interface IPropertyDefaults 
{
	/**
	 * Defines the default value for this <code>feature</code>.
	 * 
	 * @param feature the feature representing the objects property type
	 * @return the default value. In case <code>null</code> is possible property value
	 * <code>null</code> might be returned
	 */
	public Object getDefault(EStructuralFeature feature);
}
