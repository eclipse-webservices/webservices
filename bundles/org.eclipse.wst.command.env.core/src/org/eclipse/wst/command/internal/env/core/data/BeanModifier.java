/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
