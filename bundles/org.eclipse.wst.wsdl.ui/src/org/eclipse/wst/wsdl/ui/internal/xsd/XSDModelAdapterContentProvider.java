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
package org.eclipse.wst.wsdl.ui.internal.xsd;

import org.eclipse.wst.wsdl.XSDSchemaExtensibilityElement;
import org.eclipse.wst.xsd.ui.internal.provider.XSDContentProvider;
import org.eclipse.wst.xsd.ui.internal.provider.XSDModelAdapterFactoryImpl;
import org.eclipse.xsd.XSDConcreteComponent;


public class XSDModelAdapterContentProvider extends XSDContentProvider
{  
  public XSDModelAdapterContentProvider(XSDModelAdapterFactoryImpl xsdModelAdapterFactory)
  {
    super(xsdModelAdapterFactory);
  }

  public Object[] getChildren(Object parentObject)
  {                                     
    XSDConcreteComponent xsdComp = null;
    if (parentObject instanceof XSDSchemaExtensibilityElement)
    {
      XSDSchemaExtensibilityElement xsdSchemaExtensibilityElement = (XSDSchemaExtensibilityElement)parentObject;
      xsdComp = xsdSchemaExtensibilityElement.getSchema();
      return super.getChildren(xsdComp);
    }
    return super.getChildren(parentObject);    
  }

}
