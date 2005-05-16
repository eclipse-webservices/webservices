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
    return parentElementName.equals("input") || parentElementName.equals("output") || parentElementName.equals("fault");
  }

  protected boolean isMessageReference(String elementName)
  {
    return elementName.equals("body") || elementName.equals("header") || elementName.equals("fault") || elementName.equals("urlReplacement") || elementName.equals("urlEncoded");
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
            if (namespace.equals("http://schemas.xmlsoap.org/soap/encoding/") || namespace.equals(WSDLConstants.XSD_NAMESPACE_URI))
            {
              // exclude soap-enc elements
              //
              result = false;
            }
            else
            {
              // TODO.. we should investigate removing the  ExtensiblityElementFilter extension point
              // shouldn't this be a ModelQueryExtension defined on the extension languages?
              //
              ExtensiblityElementFilter filter = (ExtensiblityElementFilter) WSDLEditorPlugin.getInstance().getExtensiblityElementFilterRegistry().get(namespace);
              if (filter != null)
              {
                result = filter.isValidContext(element, name);
              }
            }
          }
        }
      }
    }
    return result;
  }

  public String[] getAttributeValues(Element element, String namespace, String name)
  {
    if (WSDLConstants.WSDL_NAMESPACE_URI.equals(namespace))
    {
      List list = new ArrayList();
      ComponentReferenceUtil util = new ComponentReferenceUtil(lookupOrCreateDefinition(element));
      String currentElementName = element.getLocalName();
      Node parentNode = element.getParentNode();
      String parentName = parentNode != null ? parentNode.getLocalName() : "";
      if (checkName(name, "message"))
      {
        list.addAll(util.getMessageNames());
      }
      else if (checkName(name, "binding"))
      {
        list.addAll(util.getBindingNames());
      }
      else if (checkName(name, "type"))
      {
        if (checkName(currentElementName, "binding"))
        {
          list.addAll(util.getPortTypeNames());
        }
        else if (checkName(currentElementName, "part"))
        {
          list.addAll(util.getComponentNameList(true));
        }
      }
      else if (checkName(name, "element"))
      {
        if (checkName(currentElementName, "part"))
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

  
  protected XSDSchema lookupOrCreateSchemaForElement(Element element)
  {       
    XSDSchema schema = null;
    Definition definition = lookupOrCreateDefinition(element);
    Object o = WSDLEditorUtil.getInstance().findModelObjectForElement(definition, element);
    if (o instanceof XSDConcreteComponent)
    {
      schema = ((XSDConcreteComponent) o).getSchema();
    } 
    return schema;
  }
  
  
  protected Definition lookupOrCreateDefinition(Element element)
  {
    Definition definition = null;
    Document document = element.getOwnerDocument();
    if (document instanceof INodeNotifier)
    {
      INodeNotifier notifier = (INodeNotifier) document;
      WSDLModelAdapter adapter = (WSDLModelAdapter) notifier.getAdapterFor(WSDLModelAdapter.class);
      if (adapter == null)
      {
        adapter = new WSDLModelAdapter();
        notifier.addAdapter(adapter);
        adapter.createDefinition(document.getDocumentElement());
      }
      definition = adapter.getDefinition();
    }
    return definition;
  }
  
  
  protected TypesHelper getTypesHelper(final Element element)
  {
    XSDSchema schema = lookupOrCreateSchemaForElement(element);
    return new TypesHelper(schema)
    {
      // TODO... it seems as though the model is not correctly
      // mainting the list of prefixes for a given namespace
      // must be a bug!
      //
      protected List getPrefixesForNamespace(String namespace)
      {
        List list = super.getPrefixesForNamespace(namespace);
        Definition definition = lookupOrCreateDefinition(element);
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
}