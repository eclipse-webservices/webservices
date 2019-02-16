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
package org.eclipse.jst.ws.jaxws.dom.ui;

import org.eclipse.emf.edit.provider.IItemPropertySource;

/**
 * Interface representing property value. This interface is used to identify property values for WS DOM objects.
 * 
 * @author Georgi Vachkov
 */
public interface IDOMPropertyValue extends IItemPropertySource
{
	public Object getEditableValue();
}
