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
package org.eclipse.jst.ws.jaxws.utils.internal.annotations.impl;

public enum AttributeTypeEnum 
{
	BOOLEAN("Boolean"), //$NON-NLS-1$
	CLASS("Class"), //$NON-NLS-1$
	INTEGER("Integer"), //$NON-NLS-1$
	QUALIFIED_NAME("Qualified Name"), //$NON-NLS-1$
	STRING("String"); //$NON-NLS-1$
	
    private final String value;
    
    AttributeTypeEnum(final String value)
    {
        this.value = value;
    }

    final String getValue()
    {
        return this.value;
    }
}
