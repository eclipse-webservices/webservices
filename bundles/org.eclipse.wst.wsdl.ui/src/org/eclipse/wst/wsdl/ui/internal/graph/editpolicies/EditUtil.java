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
package org.eclipse.wst.wsdl.ui.internal.graph.editpolicies;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.wst.wsdl.Binding;
import org.eclipse.wst.wsdl.Fault;
import org.eclipse.wst.wsdl.Input;
import org.eclipse.wst.wsdl.Output;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.Port;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.ui.internal.util.ComponentReferenceUtil;
import org.w3c.dom.Element;

//import org.eclipse.wst.wsdl.ui.internal.reconciler.WSDLToDOMElementUtil;


public class EditUtil
{
  public static List getPossiblePropertyValues(Object model, String propertyName)
  {
    List list = null;
    if (model instanceof Port)
    {
      Port port = (Port)model;   
      ComponentReferenceUtil util = new ComponentReferenceUtil(port.getEnclosingDefinition());
      list = util.getBindingNames();
    }                                                   
    else if (model instanceof Binding)
    {
      Binding binding = (Binding)model;   
      ComponentReferenceUtil util = new ComponentReferenceUtil(binding.getEnclosingDefinition());
      list = util.getPortTypeNames();
    }    
    else if (model instanceof Part)
    {
      Part part = (Part)model;    
      boolean isType = ComponentReferenceUtil.isType(part);
      list = ComponentReferenceUtil.getComponentNameList(part, isType);
    } 
    else if (model instanceof Input ||
             model instanceof Output ||
             model instanceof Fault)
    { 
      WSDLElement wsdlElement = (WSDLElement)model;   
      ComponentReferenceUtil util = new ComponentReferenceUtil(wsdlElement.getEnclosingDefinition());
      list = util.getMessageNames();
    } 

    if (list == null)
    {                
      list = new ArrayList();                            
      list.add("A");
      list.add("B");
      list.add("C");
    }
    return list;
  }

  public static void setPropertyValue(Object model, String propertyName, String value)
  { 
    if (model instanceof Part)
    {                     
      Part part = (Part)model; 
      boolean isType = ComponentReferenceUtil.isType(part);
      ComponentReferenceUtil.setComponentReference(part, isType, value);
    } 
    else if (model instanceof Port)
    {
      Element element = ((WSDLElement)model).getElement();
      element.setAttribute("binding", value);
    } 
    else if (model instanceof Binding)
    { 
      Element element = ((WSDLElement)model).getElement();
      element.setAttribute("type", value);
    } 
    else if (model instanceof Input ||
             model instanceof Output ||
             model instanceof Fault)
    { 
      Element element = ((WSDLElement)model).getElement();
      element.setAttribute("message", value);
    } 
  }

  public static String getPropertyValue(Object model, String propertyName)
  { 
    String result = null;

    if (model instanceof Part)
    {
      Element element = ((WSDLElement)model).getElement();
      result = element.getAttribute(propertyName);
    } 
    else if (model instanceof Port)
    {
      Element element = ((WSDLElement)model).getElement();
      result = element.getAttribute("binding");
    }   
    else if (model instanceof Binding)
    { 
      Element element = ((WSDLElement)model).getElement();
      result = element.getAttribute("type");
    }   
    else if (model instanceof Input ||
             model instanceof Output ||
             model instanceof Fault)
    { 
      Element element = ((WSDLElement)model).getElement();
      result = element.getAttribute("message");
    }              
    return result;
  }

  public static void setName(Object model, String value)
  { 
    if (model instanceof WSDLElement)
    {
      Element element = ((WSDLElement)model).getElement();
      element.setAttribute("name", value);
    }  
  }
}