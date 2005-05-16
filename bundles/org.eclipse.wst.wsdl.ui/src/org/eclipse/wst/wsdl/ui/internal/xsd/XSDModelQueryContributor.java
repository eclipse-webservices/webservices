/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************
package org.eclipse.wst.wsdl.ui.internal.xsd;      

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.wst.wsdl.ui.internal.WSDLEditor;
import org.eclipse.wst.wsdl.ui.internal.extension.IModelQueryContributor;
import org.eclipse.wst.wsdl.ui.internal.util.WSDLEditorUtil;
import org.eclipse.wst.xml.core.internal.contentmodel.modelquery.ModelQuery;
import org.eclipse.wst.xsd.ui.internal.AbstractXSDDataTypeValueExtension;
import org.eclipse.wst.xsd.ui.internal.AbstractXSDModelQueryContributor;
import org.eclipse.wst.xsd.ui.internal.util.TypesHelper;
import org.eclipse.xsd.XSDConcreteComponent;
import org.eclipse.xsd.XSDSchema;
import org.w3c.dom.Element;

public class XSDModelQueryContributor extends AbstractXSDModelQueryContributor implements IModelQueryContributor
{ 
  protected WSDLEditor wsdlEditor;

  public XSDModelQueryContributor(WSDLEditor wsdlEditor)
  {
    this.wsdlEditor = wsdlEditor;
  }
  
  public AbstractXSDDataTypeValueExtension createXSDDataTypeValueExtension(ModelQuery modelQuery)
  {
    return new XSDDataTypeValueExtension(modelQuery); 
  }

  class XSDDataTypeValueExtension extends AbstractXSDDataTypeValueExtension
  {                             
    public XSDDataTypeValueExtension(ModelQuery modelQuery)
    {
      super(modelQuery);
    }

    public String getId()
    {
      return "WSDL-XSDDataTypeValueExtension";
    }
     
    protected XSDSchema getEnclosingXSDSchema(Element element)
    {        
      XSDSchema result = null;
      Object o = WSDLEditorUtil.getInstance().findModelObjectForElement(wsdlEditor.getDefinition(), element);
      if (o instanceof XSDConcreteComponent)
      {                  
        result = ((XSDConcreteComponent)o).getSchema();
      }
      return result;
    }   

	  protected TypesHelper createTypesHelper(XSDSchema schema)
	  { 
		  return new TypesHelper(schema)
		  {
			  protected List getPrefixesForNamespace(String namespace)
			  {
			  	List list = super.getPrefixesForNamespace(namespace);
			  	Map map = wsdlEditor.getDefinition().getNamespaces();
			  	for (Iterator i = map.keySet().iterator(); i.hasNext();)
			  	{
			  		String prefix = (String)i.next();
			  		String ns = (String)map.get(prefix);
			   		if (ns != null && ns.equals(namespace))
				  	{					
				  	  if (!list.contains(prefix))
				  	  {
				  		list.add(prefix);
				  	  }
				  	}  
	  			}	
	  			return list;
	  		}				
	  	};		
  	}  
  }
}*/