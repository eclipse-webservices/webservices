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

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.ui.provider.PropertySource;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.IPropertySourceProvider;
import org.eclipse.wst.wsdl.XSDSchemaExtensibilityElement;
import org.eclipse.wst.xsd.ui.internal.properties.AnyAttributePropertySource;
import org.eclipse.wst.xsd.ui.internal.properties.AnyElementPropertySource;
import org.eclipse.wst.xsd.ui.internal.properties.AppInfoPropertySource;
import org.eclipse.wst.xsd.ui.internal.properties.AttributeGroupRefPropertySource;
import org.eclipse.wst.xsd.ui.internal.properties.AttributePropertySource;
import org.eclipse.wst.xsd.ui.internal.properties.BasePropertySource;
import org.eclipse.wst.xsd.ui.internal.properties.ComplexTypePropertySource;
import org.eclipse.wst.xsd.ui.internal.properties.DocumentationPropertySource;
import org.eclipse.wst.xsd.ui.internal.properties.ElementPropertySource;
import org.eclipse.wst.xsd.ui.internal.properties.EnumerationPropertySource;
import org.eclipse.wst.xsd.ui.internal.properties.GroupRefPropertySource;
import org.eclipse.wst.xsd.ui.internal.properties.ImportPropertySource;
import org.eclipse.wst.xsd.ui.internal.properties.IncludePropertySource;
import org.eclipse.wst.xsd.ui.internal.properties.KeyrefPropertySource;
import org.eclipse.wst.xsd.ui.internal.properties.ModelGroupPropertySource;
import org.eclipse.wst.xsd.ui.internal.properties.NamePropertySource;
import org.eclipse.wst.xsd.ui.internal.properties.NotationPropertySource;
import org.eclipse.wst.xsd.ui.internal.properties.PatternPropertySource;
import org.eclipse.wst.xsd.ui.internal.properties.SchemaPropertySource;
import org.eclipse.wst.xsd.ui.internal.properties.SimpleContentPropertySource;
import org.eclipse.wst.xsd.ui.internal.properties.SimpleRestrictPropertySource;
import org.eclipse.wst.xsd.ui.internal.properties.SimpleTypeListPropertySource;
import org.eclipse.wst.xsd.ui.internal.properties.SimpleTypePropertySource;
import org.eclipse.wst.xsd.ui.internal.properties.SimpleTypeUnionPropertySource;
import org.eclipse.wst.xsd.ui.internal.properties.XPathPropertySource;
import org.eclipse.wst.xsd.ui.internal.util.XSDDOMHelper;
import org.eclipse.xsd.XSDConcreteComponent;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.util.XSDConstants;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class XSDPropertySourceProvider implements IPropertySourceProvider
{
  /* (non-Javadoc)
   * @see org.eclipse.ui.views.properties.IPropertySourceProvider#getPropertySource(java.lang.Object)
   */
  public IPropertySource getPropertySource(Object object)
  {
// Using XSD's item providers
//    XSDItemProviderAdapterFactory adapterFactory = XSDExtensionPlugin.getXSDItemProviderAdapterFactory();
//   
//    IItemPropertySource itemPropertySource =
//      (IItemPropertySource)
//        (object instanceof EObject && ((EObject)object).eClass() == null ?
//          null :
//          adapterFactory.adapt(object, IItemPropertySource.class));
//    return 
//    itemPropertySource != null ?  createPropertySource(object, itemPropertySource) : null;
    
    if (object == null) return null;
    
    if (object instanceof XSDSchemaExtensibilityElement)
    {
      object = ((XSDSchemaExtensibilityElement)object).getSchema();
    }
    if (object instanceof XSDConcreteComponent)
    {
      BasePropertySource bps = (BasePropertySource)getXSDPropertySource(object);
      Element input = ((XSDConcreteComponent)object).getElement();
      bps.setInput(input);
      return bps;
    }
    return null;
  }

  protected IPropertySource createPropertySource(Object object, IItemPropertySource itemPropertySource)
  {
    return new PropertySource(object, itemPropertySource);
  }
  
  protected boolean inputEquals(Object input, String tagname, boolean isRef)
  {
    return XSDDOMHelper.inputEquals(input, tagname, isRef);
  }

  boolean showParent = false;
  
  // TODO: We should use adapters to do this.  Do as we complete 'model'-based port
  public IPropertySource getXSDPropertySource(Object object)
  {
    XSDSchema xsdSchema = ((XSDConcreteComponent)object).getSchema();
    Element input = ((XSDConcreteComponent)object).getElement();

    if (inputEquals(input, XSDConstants.ELEMENT_ELEMENT_TAG, false))
    {
      return new ElementPropertySource(xsdSchema);
    }
    else if (inputEquals(input, XSDConstants.ELEMENT_ELEMENT_TAG, true))
    {
      return new GroupRefPropertySource(xsdSchema);
    }
    else if (inputEquals(input, XSDConstants.SEQUENCE_ELEMENT_TAG, false) ||
              inputEquals(input, XSDConstants.CHOICE_ELEMENT_TAG, false) ||
              inputEquals(input, XSDConstants.ALL_ELEMENT_TAG, false))
    {
      return new ModelGroupPropertySource(xsdSchema);
    }
    else if (inputEquals(input, XSDConstants.ATTRIBUTE_ELEMENT_TAG, false))
    {
      return new AttributePropertySource(xsdSchema);
    }
    else if (inputEquals(input, XSDConstants.ATTRIBUTE_ELEMENT_TAG, true))
    {
      return new AttributeGroupRefPropertySource(xsdSchema);
    }
    else if (inputEquals(input, XSDConstants.ATTRIBUTEGROUP_ELEMENT_TAG, false))
    {
      return new NamePropertySource(xsdSchema);
    }
    else if (inputEquals(input, XSDConstants.ATTRIBUTEGROUP_ELEMENT_TAG, true))
    {
      return new AttributeGroupRefPropertySource(xsdSchema);
    }
    else if (inputEquals(input, XSDConstants.NOTATION_ELEMENT_TAG, false))
    {
      return new NotationPropertySource(xsdSchema);
    }
    else if (inputEquals(input, XSDConstants.SIMPLETYPE_ELEMENT_TAG, false))
    {
      return new SimpleTypePropertySource(xsdSchema);
    }
    else if (inputEquals(input, XSDConstants.GROUP_ELEMENT_TAG, false))
    {
      return new NamePropertySource(xsdSchema);
    }
    else if (inputEquals(input, XSDConstants.GROUP_ELEMENT_TAG, true))
    {
      return new GroupRefPropertySource(xsdSchema);
    }
    else if (inputEquals(input, XSDConstants.SCHEMA_ELEMENT_TAG, false))
    {
      return new SchemaPropertySource(xsdSchema);
    }
    else if (inputEquals(input, XSDConstants.COMPLEXTYPE_ELEMENT_TAG, false))
    {
      return new ComplexTypePropertySource(xsdSchema);
    }
    else if (inputEquals(input, XSDConstants.DOCUMENTATION_ELEMENT_TAG, false))
    {
      return new DocumentationPropertySource(xsdSchema);
    }
    else if (inputEquals(input, XSDConstants.APPINFO_ELEMENT_TAG, false))
    {
      return new AppInfoPropertySource(xsdSchema);
    }
    else if (inputEquals(input, XSDConstants.SIMPLECONTENT_ELEMENT_TAG, false))
    {
      if (input != null && input instanceof Element)
      {
        Element parent = (Element)input;
        XSDDOMHelper xsdDOMHelper = new XSDDOMHelper();
        Element derivedByNode = xsdDOMHelper.getDerivedByElement(parent);
        if (derivedByNode != null)
        {
          if (inputEquals(derivedByNode, XSDConstants.RESTRICTION_ELEMENT_TAG, false) || 
              inputEquals(derivedByNode, XSDConstants.EXTENSION_ELEMENT_TAG, false))
          {
            return new SimpleContentPropertySource(xsdSchema);
          }
        }
        else
        {
          return null;
        }
      }
    }
    else if (inputEquals(input, XSDConstants.COMPLEXCONTENT_ELEMENT_TAG, false))
    {
      if (input != null && input instanceof Element)
      {
        Element parent = (Element)input;
        XSDDOMHelper xsdDOMHelper = new XSDDOMHelper();
        Element derivedByNode = xsdDOMHelper.getDerivedByElement(parent);
        if (derivedByNode != null)
        {
          return new SimpleContentPropertySource(xsdSchema);
        }
        else
        {
          return null;
        }
      }
    }
    else if (inputEquals(input, XSDConstants.INCLUDE_ELEMENT_TAG, false))
    {
//      if (editor instanceof XSDEditor)
//      {
//        IFile inputFile = ((IFileEditorInput)editor.getEditorInput()).getFile();
        IFile inputFile = null;
        return new IncludePropertySource(xsdSchema, inputFile);
//      }
//      return null;
    }
    else if (inputEquals(input, XSDConstants.IMPORT_ELEMENT_TAG, false))
    {
//      if (editor instanceof XSDEditor)
//      {
//        IFile inputFile = ((IFileEditorInput)editor.getEditorInput()).getFile();
      IFile inputFile = null;
        return new ImportPropertySource(xsdSchema, inputFile);
//      }
//      return null;
    }
    else if (inputEquals(input, XSDConstants.REDEFINE_ELEMENT_TAG, false))
    {
//      if (editor instanceof XSDEditor)
//       {
//        IFile inputFile = ((IFileEditorInput)editor.getEditorInput()).getFile();
      IFile inputFile = null;
        return new IncludePropertySource(xsdSchema, inputFile);
//      }
//      return null;
    }
    else if (inputEquals(input, XSDConstants.LIST_ELEMENT_TAG, false))
    {
      return new SimpleTypeListPropertySource(xsdSchema);
    }
    else if (inputEquals(input, XSDConstants.UNION_ELEMENT_TAG, false))
    {
      return new SimpleTypeUnionPropertySource(xsdSchema);
    }
    else if (inputEquals(input, XSDConstants.RESTRICTION_ELEMENT_TAG, false))
    {
      return createRestrictWindow(input, xsdSchema);
    }
    else if (XSDDOMHelper.isFacet(input))
    {
      if (input != null && input instanceof Element)
      {
        Node parent = ((Element)input).getParentNode();
        if (inputEquals(parent, XSDConstants.RESTRICTION_ELEMENT_TAG, false))
        {
          return createRestrictWindow(input, xsdSchema);
        } 
      }
    }
    else if (inputEquals(input, XSDConstants.EXTENSION_ELEMENT_TAG, false))
    {
      if (input != null && input instanceof Element)
      {
        Node parent = ((Element)input).getParentNode();
        if (inputEquals(parent, XSDConstants.COMPLEXCONTENT_ELEMENT_TAG, false)
            || inputEquals(parent, XSDConstants.SIMPLECONTENT_ELEMENT_TAG, false))
        {
          showParent = true;
          return new SimpleContentPropertySource(xsdSchema);
        }
      }
    }
    else if (inputEquals(input, XSDConstants.PATTERN_ELEMENT_TAG, false))
    {
      return new PatternPropertySource(xsdSchema);
    }
    else if (inputEquals(input, XSDConstants.ENUMERATION_ELEMENT_TAG, false))
    {
      return new EnumerationPropertySource(xsdSchema);
    }
    else if (inputEquals(input, XSDConstants.ANY_ELEMENT_TAG, false))
    {
      return new AnyElementPropertySource(xsdSchema);
    }
    else if (inputEquals(input, XSDConstants.ANYATTRIBUTE_ELEMENT_TAG, false))
    {
      return new AnyAttributePropertySource(xsdSchema);
    }
    else if (inputEquals(input, XSDConstants.UNIQUE_ELEMENT_TAG, false))
    {
      return new NamePropertySource(xsdSchema);
    }
    else if (inputEquals(input, XSDConstants.KEYREF_ELEMENT_TAG, false))
    {
      return new KeyrefPropertySource(xsdSchema);
    }
    else if (inputEquals(input, XSDConstants.SELECTOR_ELEMENT_TAG, false))
    {
      return new XPathPropertySource(xsdSchema);
    }
    else if (inputEquals(input, XSDConstants.FIELD_ELEMENT_TAG, false))
    {
      return new XPathPropertySource(xsdSchema);
    }
    else if (inputEquals(input, XSDConstants.KEY_ELEMENT_TAG, false))
    {
      return new NamePropertySource(xsdSchema);
    }
    else
    {
      return null;
    }
    return null;
  }
  
  protected IPropertySource createRestrictWindow(Object input, XSDSchema xsdSchema)
  {
    // special case where SimpleType restriction is different than SimpleContent restriction

    if (input != null && input instanceof Element)
    {
      Node parent = ((Element)input).getParentNode();
      if (inputEquals(parent, XSDConstants.SIMPLETYPE_ELEMENT_TAG, false))
      {
        return new SimpleRestrictPropertySource(xsdSchema);
      }
      else if (inputEquals(parent, XSDConstants.SIMPLECONTENT_ELEMENT_TAG, false))
      {
        return new SimpleRestrictPropertySource(xsdSchema);
      }
      else if (inputEquals(parent, XSDConstants.COMPLEXCONTENT_ELEMENT_TAG, false))
      {
        showParent = true;
        return new SimpleContentPropertySource(xsdSchema);
      }
    }
    return null;
  }

}
