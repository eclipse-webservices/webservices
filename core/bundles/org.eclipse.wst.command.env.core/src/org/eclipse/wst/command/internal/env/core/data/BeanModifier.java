/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.command.internal.env.core.data;

public interface BeanModifier {

	/**
	 * Performs modification on bean properties using data provided
	 * @param bean  The bean to be modified
	 * @param propertyHolder The data to use to make the modification
	 */
	public void modify(Object bean, Object propertyHolder);
	
}
