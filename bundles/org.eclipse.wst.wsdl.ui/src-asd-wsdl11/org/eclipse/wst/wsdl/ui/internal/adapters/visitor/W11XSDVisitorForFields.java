/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.adapters.visitor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.eclipse.xsd.XSDAttributeDeclaration;
import org.eclipse.xsd.XSDAttributeGroupContent;
import org.eclipse.xsd.XSDAttributeGroupDefinition;
import org.eclipse.xsd.XSDAttributeUse;
import org.eclipse.xsd.XSDComplexTypeDefinition;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDModelGroup;

public class W11XSDVisitorForFields extends W11XSDVisitor
{
  public W11XSDVisitorForFields()
  {
  }

  List concreteComponentList = new ArrayList();
  List thingsWeNeedToListenTo = new ArrayList();
  
  public void visitComplexTypeDefinition(XSDComplexTypeDefinition type)
  {
    super.visitComplexTypeDefinition(type);
    if (type.getAttributeContents() != null)
    {
      for (Iterator iter = type.getAttributeContents().iterator(); iter.hasNext(); )
      {
        XSDAttributeGroupContent attrGroupContent = (XSDAttributeGroupContent)iter.next();
        if (attrGroupContent instanceof XSDAttributeUse)
        {
          XSDAttributeUse attrUse = (XSDAttributeUse)attrGroupContent;
          concreteComponentList.add(attrUse);
        }
        else if (attrGroupContent instanceof XSDAttributeGroupDefinition)
        {
          XSDAttributeGroupDefinition attrGroup = (XSDAttributeGroupDefinition)attrGroupContent;
          thingsWeNeedToListenTo.add(attrGroup);
          visitAttributeGroupDefinition(attrGroup);          
        }
      }
    }
  }
  
  
  public void visitModelGroup(XSDModelGroup modelGroup)
  {
    super.visitModelGroup(modelGroup);
    thingsWeNeedToListenTo.add(modelGroup); 
  }
  
  public void visitAttributeGroupDefinition(XSDAttributeGroupDefinition attributeGroup)
  {
    for (Iterator it = attributeGroup.getContents().iterator(); it.hasNext(); )
    {
      Object o = it.next();
      if (o instanceof XSDAttributeUse)
      {
        XSDAttributeUse attributeUse = (XSDAttributeUse)o;
        concreteComponentList.add(attributeUse);
      }
      else if (o instanceof XSDAttributeGroupDefinition)
      {
        XSDAttributeGroupDefinition attrGroup = (XSDAttributeGroupDefinition)o;
        thingsWeNeedToListenTo.add(attrGroup);
        if (attrGroup.isAttributeGroupDefinitionReference())
        {
          attrGroup = attrGroup.getResolvedAttributeGroupDefinition();
          visitAttributeGroupDefinition(attrGroup);
        }
      }
    }
  }

  public void visitElementDeclaration(XSDElementDeclaration element)
  {
    //super.visitElementDeclaration(element);
    concreteComponentList.add(element);
  }
  
  public void visitAttributeDeclaration(XSDAttributeDeclaration attr)
  {
    super.visitAttributeDeclaration(attr);
    concreteComponentList.add(attr);
  }


  public List getConcreteComponentList()
  {
    return concreteComponentList;
  }


  public List getThingsWeNeedToListenTo()
  {
    return thingsWeNeedToListenTo;
  }
}