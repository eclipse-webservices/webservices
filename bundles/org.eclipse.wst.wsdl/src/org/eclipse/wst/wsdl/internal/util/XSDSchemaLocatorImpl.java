package org.eclipse.wst.wsdl.internal.util;

import java.util.Iterator;

import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.XSDSchemaExtensibilityElement;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.util.XSDSchemaLocator;

public class XSDSchemaLocatorImpl extends AdapterImpl implements XSDSchemaLocator
{
    /**
     * @see org.eclipse.xsd.util.XSDSchemaLocator#locateSchema(org.eclipse.xsd.XSDSchema,
     *      java.lang.String, java.lang.String, java.lang.String)
     */
    public XSDSchema locateSchema(XSDSchema xsdSchema, String namespaceURI, String rawSchemaLocationURI, String resolvedSchemaLocationURI)
    {
      XSDSchema resolvedSchema = null;

      if (namespaceURI != null)
      {
        Definition definition = null;

        for (EObject parent = xsdSchema.eContainer(); parent != null; parent = parent.eContainer())
        {
          if (parent instanceof Definition)
          {
            definition = (Definition) parent;
            break;
          }
        }

        if (definition != null && definition.getETypes() != null)
        {
          for (Iterator i = definition.getETypes().getEExtensibilityElements().iterator(); i.hasNext();)
          {
            Object o = i.next();
            if (o instanceof XSDSchemaExtensibilityElement)
            {
              XSDSchema schema = ((XSDSchemaExtensibilityElement) o).getSchema();
              if (schema != null && namespaceURI.equals(schema.getTargetNamespace()))
              {
                resolvedSchema = schema;
                break;
              }
            }
          }
        }
      }
      return resolvedSchema;      
    }

    public boolean isAdatperForType(Object type)
    {
      return type == XSDSchemaLocator.class;
    }  
}
