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



public class XSDDetailsViewerProvider // implements IDetailsViewerProvider
{
//  public Object getViewerKey(Object modelObject)
//  {                              
//    Object result = null;   
//
//    if (modelObject instanceof XSDConcreteComponent)
//    {
//      Element input = ((XSDConcreteComponent)modelObject).getElement();
//
//      if (inputEquals(input, XSDConstants.ELEMENT_ELEMENT_TAG, false))
//      {
//        result = "element";
//      }
//      else if (inputEquals(input, XSDConstants.ELEMENT_ELEMENT_TAG, true))
//      {
//        result = "elementRef";
//      }
//      else if (inputEquals(input, XSDConstants.SEQUENCE_ELEMENT_TAG, false) ||
//                inputEquals(input, XSDConstants.CHOICE_ELEMENT_TAG, false) ||
//                inputEquals(input, XSDConstants.ALL_ELEMENT_TAG, false))
//      {
//        result = "sequenceChoiceAll";
//      }
//      else if (inputEquals(input, XSDConstants.ATTRIBUTE_ELEMENT_TAG, false))
//      {
//        result = "attribute";
//      }
//      else if (inputEquals(input, XSDConstants.ATTRIBUTE_ELEMENT_TAG, true))
//      {
//        result = "attributeRef";
//      }
//      else if (inputEquals(input, XSDConstants.ATTRIBUTEGROUP_ELEMENT_TAG, false))
//      {
//        result = "attributeGroup";
//      }
//      else if (inputEquals(input, XSDConstants.ATTRIBUTEGROUP_ELEMENT_TAG, true))
//      {
//        result = "attributeGroupRef";
//      }
//      else if (inputEquals(input, XSDConstants.NOTATION_ELEMENT_TAG, false))
//      {
//        result = "notation";
//      }
//      else if (inputEquals(input, XSDConstants.GROUP_ELEMENT_TAG, false))
//      {
//        result = "group";
//      }
//      else if (inputEquals(input, XSDConstants.GROUP_ELEMENT_TAG, true))
//      {
//        result = "groupRef";
//      }
//      else if (inputEquals(input, XSDConstants.SCHEMA_ELEMENT_TAG, false))
//      {
//        result = "schema";
//      }
//      else if (inputEquals(input, XSDConstants.COMPLEXTYPE_ELEMENT_TAG, false))
//      {
//        result = "complexType";
//      }
//      else if (inputEquals(input, XSDConstants.DOCUMENTATION_ELEMENT_TAG, false))
//      {
//        result = "documentation";
//      }
//      else if (inputEquals(input, XSDConstants.APPINFO_ELEMENT_TAG, false))
//      {
//        result = "appInfo";
//      }
//      else if (inputEquals(input, XSDConstants.SIMPLECONTENT_ELEMENT_TAG, false))
//      {
//        if (input != null && input instanceof Element)
//        {
//          Element parentElement = (Element)input;
//          XSDDOMHelper xsdDOMHelper = new XSDDOMHelper();
//          Element derivedByNode = xsdDOMHelper.getDerivedByElement(parentElement);
//          if (derivedByNode != null)
//          {
//            if (inputEquals(derivedByNode, XSDConstants.RESTRICTION_ELEMENT_TAG, false))
//            {
//              result = "simpleContent";
//            }
//            else if (inputEquals(derivedByNode, XSDConstants.EXTENSION_ELEMENT_TAG, false))
//            {
//              result = "simpleContent";
//            }
//          }
//          else
//          {
//            result = "xsd";
//          }
//        }
//      }
//      else if (inputEquals(input, XSDConstants.COMPLEXCONTENT_ELEMENT_TAG, false))
//      {
//        if (input != null && input instanceof Element)
//        {
//          Element parentElement = (Element)input;
//          XSDDOMHelper xsdDOMHelper = new XSDDOMHelper();
//          Element derivedByNode = xsdDOMHelper.getDerivedByElement(parentElement);
//          if (derivedByNode != null)
//          {
//            result = "simpleContent";
//          }
//          else
//          {
//            result = "xsd";
//          }
//        }
//      }
//      else if (inputEquals(input, XSDConstants.INCLUDE_ELEMENT_TAG, false))
//      {
//        result = "includeXSD";
//      }
//      else if (inputEquals(input, XSDConstants.IMPORT_ELEMENT_TAG, false))
//      {
//        result = "importXSD";
//      }
//      else if (inputEquals(input, XSDConstants.REDEFINE_ELEMENT_TAG, false))
//      {
//        result = "redefine";
//      }
//      else if (inputEquals(input, XSDConstants.LIST_ELEMENT_TAG, false))
//      {
//        result = "simpleBase";
//      }
//      else if (inputEquals(input, XSDConstants.UNION_ELEMENT_TAG, false))
//      {
//        result = "simpleBase";
//      }
//      else if (inputEquals(input, XSDConstants.RESTRICTION_ELEMENT_TAG, false))
//      {
//        result = getKeyForRestrictWindow(input);
//      }
//      else if (XSDDOMHelper.isFacet(input))
//      {
//        if (input != null && input instanceof Element)
//        {
//          Node parentElement = ((Element)input).getParentNode();
//          if (inputEquals(parentElement, XSDConstants.RESTRICTION_ELEMENT_TAG, false))
//          {
//            result = getKeyForRestrictWindow(input);
//          } 
//        }
//      }
//      else if (inputEquals(input, XSDConstants.EXTENSION_ELEMENT_TAG, false))
//      {
//        if (input != null && input instanceof Element)
//        {
//          Node parentElement = ((Element)input).getParentNode();
//          if (inputEquals(parentElement, XSDConstants.COMPLEXCONTENT_ELEMENT_TAG, false))
//          {
//            result = "simpleContent";
//          }
//          else if (inputEquals(parentElement, XSDConstants.SIMPLECONTENT_ELEMENT_TAG, false))
//          {
//            result = "simpleContent";
//          }
//          else
//          {
//            result = "xsd";            
//          }
//        }
//      }
//      else if (inputEquals(input, XSDConstants.PATTERN_ELEMENT_TAG, false))
//      {
//        result = "pattern";
//      }
//      else if (inputEquals(input, XSDConstants.ENUMERATION_ELEMENT_TAG, false))
//      {
//        result = "enum";
//      }
//      else if (inputEquals(input, XSDConstants.ANY_ELEMENT_TAG, false))
//      {
//        result = "anyElement";
//      }
//      else if (inputEquals(input, XSDConstants.ANYATTRIBUTE_ELEMENT_TAG, false))
//      {
//        result = "anyAttribute";
//      }
//      else if (inputEquals(input, XSDConstants.UNIQUE_ELEMENT_TAG, false))
//      {
//        result = "unique";
//      }
//      else if (inputEquals(input, XSDConstants.KEYREF_ELEMENT_TAG, false))
//      {
//        result = "keyRef";
//      }
//      else if (inputEquals(input, XSDConstants.SELECTOR_ELEMENT_TAG, false))
//      {
//        result = "selector";
//      }
//      else if (inputEquals(input, XSDConstants.FIELD_ELEMENT_TAG, false))
//      {
//        result = "field";
//      }
//      else if (inputEquals(input, XSDConstants.KEY_ELEMENT_TAG, false))
//      {
//        result = "key";
//      }
//      else
//      {
//        result = "xsd";
//      }      
//    }  
//    else if (modelObject instanceof org.eclipse.wst.wsdl.XSDSchemaExtensibilityElement)
//    {
//      result = "schema";
//    }
//
//    return result;
//  }                                                                
//
//
//  public Viewer createViewer(Object modelObject, Composite parent, IEditorPart editorPart)
//  {                                       
//    Viewer viewer = null;
//
//    
//    DesignViewer designViewer = new DesignViewer(editorPart);
//    designViewer.setControl(parent);    
//    if (modelObject instanceof XSDConcreteComponent)
//    {
//      Element elem = ((XSDConcreteComponent)modelObject).getElement();
//      viewer = designViewer.createViewer(elem);
////TODO port
////      ((BaseWindow)viewer).setModelObject(modelObject);
////      viewer.setInput(elem);
//    }
//    else if (modelObject instanceof org.eclipse.wst.wsdl.XSDSchemaExtensibilityElement)
//    {
//      XSDSchemaExtensibilityElement schema = (XSDSchemaExtensibilityElement)modelObject;
//      XSDSchema xsdSchema = schema.getSchema();
//      Element elem = xsdSchema.getElement();
//      viewer = designViewer.createViewer(elem);
////TODO port
////      ((BaseWindow)viewer).setModelObject(modelObject);
////      viewer.setInput(elem);      
//    }
//    return viewer;
//  }
//
//  protected boolean inputEquals(Object input, String tagname, boolean isRef)
//  {
//    return XSDDOMHelper.inputEquals(input, tagname, isRef);
//  }
//
//  protected String getKeyForRestrictWindow(Object input)
//  {
//    // special case where SimpleType restriction is different than SimpleContent restriction
//
//    if (input != null && input instanceof Element)
//    {
//      Node parentElement = ((Element)input).getParentNode();
//      if (inputEquals(parentElement, XSDConstants.SIMPLETYPE_ELEMENT_TAG, false))
//      {
//        return "simpleRestrict";
//      }
//      else if (inputEquals(parentElement, XSDConstants.SIMPLECONTENT_ELEMENT_TAG, false))
//      {
//        return "simpleRestrict";
//      }
//      else if (inputEquals(parentElement, XSDConstants.COMPLEXCONTENT_ELEMENT_TAG, false))
//      {
//        return "simpleContent";
//      }
//    }
//    return "xsd";
//  }

}