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
package org.eclipse.wst.wsdl.ui.internal.viewers.widgets;

import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Event;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.ui.internal.util.ComponentReferenceUtil;
import org.w3c.dom.Element;


// A convenience class
//
public abstract class ComponentNameComboHelper
{                      
  protected CCombo componentNameCombo;

  public ComponentNameComboHelper(CCombo componentNameCombo)
  {
    this.componentNameCombo = componentNameCombo;
  }       

  public void update(Object input)
  {                                   
    Element element = ((WSDLElement)input).getElement();
    componentNameCombo.removeAll();
    String value = element.getAttribute(getAttributeName());
    componentNameCombo.setText(value != null ? value : "");

    if (input instanceof WSDLElement)
    {
      Definition definition = ((WSDLElement)input).getEnclosingDefinition();
      ComponentReferenceUtil componentReferenceUtil = new ComponentReferenceUtil(definition);
      java.util.List list = getComponentNameList(componentReferenceUtil);
      for (Iterator iter = list.iterator(); iter.hasNext();)
      {
        componentNameCombo.add((String)iter.next());
      }
    }
  }  

  public void handleEventHelper(Element element, Event event)
  {                        
    if (event.type == SWT.Modify)
    {  
      if (event.widget == componentNameCombo)
      {
        element.setAttribute(getAttributeName(), componentNameCombo.getText());
      }
    }
  }

  protected abstract List getComponentNameList(ComponentReferenceUtil util);

  protected abstract String getAttributeName();
}