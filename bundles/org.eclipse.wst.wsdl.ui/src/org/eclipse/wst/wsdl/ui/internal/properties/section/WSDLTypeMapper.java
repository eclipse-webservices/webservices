/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.properties.section;

import org.eclipse.wst.common.ui.properties.internal.provisional.ITypeMapper;
import org.eclipse.wst.wsdl.ui.internal.model.WSDLGroupObject;

public class WSDLTypeMapper implements ITypeMapper
{
  /* (non-Javadoc)
   * @see org.eclipse.wst.common.ui.properties.internal.provisional.ITypeMapper#remapType(java.lang.Object, java.lang.Class)
   */
  public Class remapType(Object object, Class effectiveType)
  {
		Class type = effectiveType;
		
		if (object instanceof WSDLGroupObject) {
			type = ((WSDLGroupObject) object).getDefinition().getClass();
		}

		return type;
  }
  
  public Object remapObject(Object object)
  {
    Object type = object;
    
    if (object instanceof WSDLGroupObject)
    {
      type = ((WSDLGroupObject) object).getDefinition();
    }
    return type;
  }
}
