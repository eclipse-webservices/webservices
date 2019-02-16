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
package org.eclipse.jst.ws.jaxws.dom.runtime;

import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * Interface for checking the state of some property for concrete DOM object.
 * This interface should be used by DOM clients that need to know if some property 
 * can be changed - for example UI editor that needs to know if the property should
 * be enabled for edit or disabled.
 * 
 * @author Georgi Vachkov
 */
public interface IPropertyState 
{
	/**
	 * Defines if property is changeable.
	 * @return <code>true</code> in case property can be changed
	 */
	public boolean isChangeable(EStructuralFeature feature);
}
