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
package org.eclipse.wst.wsdl.ui.internal.properties.sections;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.wsdl.ExtensibilityElement;
import org.eclipse.wst.wsdl.ExtensibleElement;
import org.eclipse.wst.wsdl.ui.internal.Messages;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.commands.AddExtensionElementCommand;
import org.eclipse.wst.xsd.ui.internal.common.commands.AddExtensionCommand;
import org.eclipse.wst.xsd.ui.internal.common.properties.sections.AbstractExtensionsSection;
import org.eclipse.wst.xsd.ui.internal.common.properties.sections.appinfo.DOMExtensionTreeContentProvider;
import org.eclipse.wst.xsd.ui.internal.common.properties.sections.appinfo.DOMExtensionTreeLabelProvider;
import org.eclipse.wst.xsd.ui.internal.common.properties.sections.appinfo.ExtensionsSchemasRegistry;
import org.eclipse.xsd.XSDAttributeDeclaration;
import org.eclipse.xsd.XSDElementDeclaration;
import org.w3c.dom.Element;

public class W11ExtensionsSection extends AbstractExtensionsSection
{
  public W11ExtensionsSection()
  {
    super();
    setExtensionTreeLabelProvider(new WSDLExtensionTreeLabelProvider());
    setExtensionTreeContentProvider(new WSDLExtensionTreeContentProvider());
  }

  protected AddExtensionCommand getAddExtensionCommand(Object o)
  {
    AddExtensionCommand addExtensionCommand = null;
    if (input instanceof ExtensibleElement)
    {  
      if (o instanceof XSDElementDeclaration)
      {
        XSDElementDeclaration element = (XSDElementDeclaration) o;
        addExtensionCommand = new AddExtensionElementCommand(Messages.getString("_UI_LABEL_ADD_EXTENSION_ELEMENT"), (ExtensibleElement)input, element); //$NON-NLS-1$
      }
      else if (o instanceof XSDAttributeDeclaration)
      {
        // TODO (cs) need to implement this
      }
    }
    return addExtensionCommand;
  }

  protected Command getRemoveExtensionCommand(Object o)
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  protected ExtensionsSchemasRegistry getExtensionsSchemasRegistry()
  {
    return WSDLEditorPlugin.getInstance().getExtensionsSchemasRegistry();
  }
 
  static class WSDLExtensionTreeContentProvider extends DOMExtensionTreeContentProvider
  {
    public java.lang.Object[] getElements(java.lang.Object inputElement)
    {         
      if (inputElement instanceof Adapter)
      {
        inputElement = ((Adapter)inputElement).getTarget();
      }  
      if (inputElement instanceof ExtensibleElement)
      {        
        List domElementList = new ArrayList();
        ExtensibleElement extensibleElement = (ExtensibleElement) inputElement;        
        for (Iterator i = extensibleElement.getExtensibilityElements().iterator(); i.hasNext(); )
        {
          ExtensibilityElement element = (ExtensibilityElement)i.next();
          // add the DOM element
          domElementList.add(element.getElement());
        }
        return domElementList.toArray();
      }
      return Collections.EMPTY_LIST.toArray();
    }
  }
  
  static class WSDLExtensionTreeLabelProvider extends DOMExtensionTreeLabelProvider
  {
    public Image getImage(Object object)
    {
      if (object instanceof Element)
      {
        Element element = (Element)object;
        ExtensionsSchemasRegistry registry = WSDLEditorPlugin.getInstance().getExtensionsSchemasRegistry();      
        ILabelProvider provider = registry.getLabelProvider(element);
        if (provider != null)
        {
          return provider.getImage(element);
        }
      }
      return super.getImage(object);
    }

    public String Text(Object object)
    {
      if (object instanceof Element)
      {
        Element element = (Element)object;
        ExtensionsSchemasRegistry registry = WSDLEditorPlugin.getInstance().getExtensionsSchemasRegistry();      
        ILabelProvider provider = registry.getLabelProvider(element);
        if (provider != null)
        {
          return provider.getText(element);
        }
      }
      return super.getText(object);
    }        
  }
}
