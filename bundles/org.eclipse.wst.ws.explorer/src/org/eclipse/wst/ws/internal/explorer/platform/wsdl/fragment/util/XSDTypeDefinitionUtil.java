/*******************************************************************************
 * Copyright (c) 2002, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.util;

import org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants.FragmentConstants;
import org.eclipse.xsd.XSDComplexTypeContent;
import org.eclipse.xsd.XSDComplexTypeDefinition;
import org.eclipse.xsd.XSDTypeDefinition;

public class XSDTypeDefinitionUtil {
  private XSDTypeDefinitionUtil() {
  }

  public static boolean isXSDBuiltInTypeDefinition(XSDTypeDefinition type) {
    if (type == null)
      return false;
    String ns = type.getTargetNamespace();
    return (ns != null && ns.equals(FragmentConstants.URI_XSD));
  }
  
  public static boolean isRootTypeDefinition(XSDTypeDefinition type) {
    if (type == null)
      return true;
    XSDTypeDefinition baseType = type.getBaseType();
    if (baseType == null)
      return true;
    String typeNS = type.getTargetNamespace();
    String baseTypeNS = baseType.getTargetNamespace();
    String typeName = type.getName();
    String baseTypeName = baseType.getName();
    boolean sameNS;
    boolean sameName;
    if (typeNS == null && baseTypeNS == null)
      sameNS = true;
    else if (typeNS != null && baseTypeNS != null && typeNS.equals(baseTypeNS))
      sameNS = true;
    else
      sameNS = false;
    if (typeName == null && baseTypeName == null)
      sameName = true;
    else if (typeName != null && baseTypeName != null && typeName.equals(baseTypeName))
      sameName = true;
    else
      sameName = false;
    return (sameNS && sameName);
  }
  
  public static XSDTypeDefinition resolveToXSDBuiltInTypeDefinition(XSDTypeDefinition type) {
    if (type == null)
      return null;
    XSDTypeDefinition currType = type;
    while (!isXSDBuiltInTypeDefinition(currType)) {
      if (isRootTypeDefinition(currType))
        return null;
      currType = currType.getBaseType();
    }
    return currType;
  }

  public static boolean isSoapEncArray(XSDTypeDefinition type) {
    XSDTypeDefinition currType = type;
    if (currType != null)
    while(currType != null) {
      String ns = currType.getTargetNamespace();
      String name = currType.getName();
      if (FragmentConstants.URI_SOAP.equals(ns) && FragmentConstants.QNAME_LOCAL_NAME_ARRAY.equals(name))
        return true;
      else if (isRootTypeDefinition(currType))
        return false;
      else
        currType = currType.getBaseType();
    }
    return false;
  }

  /**
   * Returns the complex type content given a complex type.
   * If complexType is a user defined type:
   *   This method will return the user defined complex content of this type or the content of the extended
   *   or derived type iff the extended or derived type is not a built-in XSD type (for example, xsd:anyType).
   * If complexType is a built-in XSD type:
   *   It will return the complex content of this built-in XSD complex type.  It will not return the
   *   complex content of its extended or derived type.
   * @param complexType XSDComplexTypeDefinition complex type.
   * @return XSDComplexTypeContent XSDComplexTypeContent the resolved complex type content.
  **/
  public static XSDComplexTypeContent getXSDComplexTypeContent(XSDComplexTypeDefinition complexType)
  {
    if (!isXSDBuiltInTypeDefinition(complexType))
    {
      XSDTypeDefinition type = complexType;
      while (!isXSDBuiltInTypeDefinition(type) && (type instanceof XSDComplexTypeDefinition))
      {
        XSDComplexTypeContent complexTypeContent = ((XSDComplexTypeDefinition)type).getContent();
        if (complexTypeContent != null)
          return complexTypeContent;
        if (isRootTypeDefinition(type))
          return null;
        type = type.getBaseType();
      }
      return null;
    }
    else
      return complexType.getContent();
  }
}
