package org.eclipse.wst.wsdl.internal.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.xsd.util.XSDSchemaLocator;

public class XSDSchemaLocatorAdapterFactory extends AdapterFactoryImpl
{
    protected XSDSchemaLocatorImpl schemaLocator = new XSDSchemaLocatorImpl();

    public boolean isFactoryForType(Object type)
    {
      return type == XSDSchemaLocator.class;
    }

    public Adapter adaptNew(Notifier target, Object type)
    {
      return schemaLocator;
    }
}
