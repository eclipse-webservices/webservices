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
package org.eclipse.wst.wsdl.ui.internal.text;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.wst.sse.core.internal.provisional.INodeNotifier;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.filter.ExtensiblityElementFilter;
import org.eclipse.wst.wsdl.ui.internal.util.ComponentReferenceUtil;
import org.eclipse.wst.wsdl.ui.internal.util.WSDLEditorUtil;
import org.eclipse.wst.wsdl.util.WSDLConstants;
import org.eclipse.wst.xsd.ui.internal.text.XSDModelQueryExtension;
import org.eclipse.wst.xsd.ui.internal.util.TypesHelper;
import org.eclipse.xsd.XSDConcreteComponent;
import org.eclipse.xsd.XSDSchema;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class WSDLModelQueryExtension extends XSDModelQueryExtension
{
  public WSDLModelQueryExtension()
  {
  }

  protected boolean isParentElementMessageReference(String parentElementName)
  {
    return parentElementName.equals("input") || parentElementName.equals("output") || parentElementName.equals("fault"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
  }

  protected boolean isMessageReference(String elementName)
  {
    return elementName.equals("body") || elementName.equals("header") || elementName.equals("fault") || elementName.equals("urlReplacement") || elementName.equals("urlEncoded"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
  }
  
  
  public boolean isApplicableChildElement(Node parentNode, String namespace, String name)
  {
    boolean result = true;    
    
    if (parentNode.getNodeType() == Node.ELEMENT_NODE)
    {
      Element element = (Element) parentNode;
      String parentElementNamespaceURI = parentNode.getNamespaceURI();
      String parentElementName = parentNode.getLocalName();
      // only filter children for 'non-schema' elements
      //   
      if (!WSDLConstants.XSD_NAMESPACE_URI.equals(parentElementNamespaceURI))
      {
        if (parentElementName != null && name != null)
        {
          if (namespace != null)
          {
            // the following namespace are one that always should be filtered out            
            // for now this is hardcoded
            //
            if (namespace.equals("http://schemas.xmlsoap.org/soap/encoding/")) //$NON-NLS-1$
            {
              // exclude soap-enc elements
              //
              result = false;
            }
            else if (namespace.equals(WSDLConstants.XSD_NAMESPACE_URI))
            {
              // eclipse all schema elements, except for 'schema' withing wsdl types elements 
              result = parentElementName.equals("types") && name.equals("schema"); //$NON-NLS-1$ //$NON-NLS-2$
            }
            else if (namespace.equals(WSDLConstants.WSDL_NAMESPACE_URI))
            {
              // cs : the 'required' attribute is burned into WSDL schema as the 'base' extensibility element
              // the WTP WSDL validator complains when these are present so this line filters them out
              // of the content assist and extension details view.  As far as I can tell no one actually uses 'em
              // in practise ... so filtering them out should be ok.  
              //
              // TODO (cs) how come the schema says their ok but the validator doesn't like them?
              //
              result = !name.equals("required");
            }  
            else
            {
              // TODO.. we should investigate removing the  ExtensiblityElementFilter extension point
              // shouldn't this be a ModelQueryExtension defined on the extension languages?
              //
              ExtensiblityElementFilter filter = (ExtensiblityElementFilter) WSDLEditorPlugin.getInstance().getExtensiblityElementFilterRegistry().getProperty(namespace, ""); //$NON-NLS-1$
              if (filter != null)
              {
                result = filter.isValidContext(element, name);
              }
            }
          }
        }
      }
      else
      {
        return super.isApplicableChildElement(parentNode, namespace, name);
      }
    }
    return result;
  }

  public String[] getAttributeValues(Element element, String namespace, String name)
  {
    if (WSDLConstants.WSDL_NAMESPACE_URI.equals(namespace))
    {
      List list = new ArrayList();
      ComponentReferenceUtil util = new ComponentReferenceUtil(lookupOrCreateDefinition(element.getOwnerDocument()));
      String currentElementName = element.getLocalName();
      if (checkName(name, "message")) //$NON-NLS-1$
      {
        list.addAll(util.getMessageNames());
      }
      else if (checkName(name, "binding")) //$NON-NLS-1$
      {
        list.addAll(util.getBindingNames());
      }
      else if (checkName(name, "type")) //$NON-NLS-1$
      {
        if (checkName(currentElementName, "binding")) //$NON-NLS-1$
        {
          list.addAll(util.getPortTypeNames());
        }
        else if (checkName(currentElementName, "part")) //$NON-NLS-1$
        {
          list.addAll(util.getComponentNameList(true));
        }
      }
      else if (checkName(name, "element")) //$NON-NLS-1$
      {
        if (checkName(currentElementName, "part")) //$NON-NLS-1$
        {
          list.addAll(util.getComponentNameList(false));
        }
      }
      String[] result = new String[list.size()];
      list.toArray(result);
      return result;
    }
    else
    {
      return super.getAttributeValues(element, namespace, name);
    }
  }

  /**
   * @deprecated
   */
  protected XSDSchema lookupOrCreateSchemaForElement(Element element)
  {       
    return lookupOrCreateSchema(element);
  }
  
  private XSDSchema lookupOrCreateSchema(Element element)
  {   
    XSDSchema schema = null;
    Definition definition = lookupOrCreateDefinition(element.getOwnerDocument());
    Object o = WSDLEditorUtil.getInstance().findModelObjectForElement(definition, element);
    if (o instanceof XSDConcreteComponent)
    {
      schema = ((XSDConcreteComponent) o).getSchema();
    } 
    return schema;    
  }
  
  protected Definition lookupOrCreateDefinition(Document document)
  {
    Definition definition = null;
    if (document instanceof INodeNotifier)
    {
      INodeNotifier notifier = (INodeNotifier) document;
      WSDLModelAdapter adapter = (WSDLModelAdapter) notifier.getAdapterFor(WSDLModelAdapter.class);
      if (adapter == null)
      {
        adapter = new WSDLModelAdapter();
        notifier.addAdapter(adapter);
        adapter.createDefinition(document);
      }
      definition = adapter.getDefinition();
    }
    return definition;
  }
  
  
  protected TypesHelper getTypesHelper(final Element element)
  {
    XSDSchema schema = lookupOrCreateSchema(element);
    return new TypesHelper(schema)
    {
      // TODO... it seems as though the model is not correctly
      // mainting the list of prefixes for a given namespace
      // must be a bug!
      //
      protected List getPrefixesForNamespace(String namespace)
      {
        List list = super.getPrefixesForNamespace(namespace);
        Definition definition = lookupOrCreateDefinition(element.getOwnerDocument());
        if (definition != null)
        {  
          Map map = definition.getNamespaces();
          for (Iterator i = map.keySet().iterator(); i.hasNext();)
          {
            String prefix = (String) i.next();
            String ns = (String) map.get(prefix);
            if (ns != null && ns.equals(namespace))
            {
              if (!list.contains(prefix))
              {
                list.add(prefix);
              }
            }
          }
        }
        return list;
      }       
    };    
  } 
  
  /**   
   * @deprecated
   */
  protected Definition lookupOrCreateDefinition(Element element)
  {
    return lookupOrCreateDefinition(element.getOwnerDocument());
  } 
}
