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
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.wst.wsdl.ExtensibilityElement;
import org.eclipse.wst.wsdl.ExtensibleElement;
import org.eclipse.wst.wsdl.ui.internal.Messages;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.asd.design.editparts.model.AbstractModelCollection;
import org.eclipse.wst.wsdl.ui.internal.commands.AddExtensionElementCommand;
import org.eclipse.wst.wsdl.ui.internal.filter.ExtensiblityElementFilter;
import org.eclipse.wst.wsdl.ui.internal.text.WSDLModelAdapter;
import org.eclipse.wst.xsd.ui.internal.common.commands.AddExtensionCommand;
import org.eclipse.wst.xsd.ui.internal.common.properties.sections.AbstractExtensionsSection;
import org.eclipse.wst.xsd.ui.internal.common.properties.sections.appinfo.AddExtensionsComponentDialog;
import org.eclipse.wst.xsd.ui.internal.common.properties.sections.appinfo.DOMExtensionTreeContentProvider;
import org.eclipse.wst.xsd.ui.internal.common.properties.sections.appinfo.DOMExtensionTreeLabelProvider;
import org.eclipse.wst.xsd.ui.internal.common.properties.sections.appinfo.ExtensionsSchemasRegistry;
import org.eclipse.xsd.XSDAttributeDeclaration;
import org.eclipse.xsd.XSDElementDeclaration;
import org.w3c.dom.Element;

public class W11ExtensionsSection extends AbstractExtensionsSection
{
  WSDLModelAdapter modelAdapter;
  
  public W11ExtensionsSection()
  {
    super();
    setExtensionTreeLabelProvider(new WSDLExtensionTreeLabelProvider());
    setExtensionTreeContentProvider(new WSDLExtensionTreeContentProvider());
  }

  protected AddExtensionCommand getAddExtensionCommand(Object o)
  {
    AddExtensionCommand addExtensionCommand = null;
    ExtensibleElement extensibleElement = getExtensibleElement(input);
    if (extensibleElement != null)
    {  
      if (o instanceof XSDElementDeclaration)
      {
        XSDElementDeclaration element = (XSDElementDeclaration) o;
        addExtensionCommand = new AddExtensionElementCommand(Messages.getString("_UI_LABEL_ADD_EXTENSION_ELEMENT"), extensibleElement, element); //$NON-NLS-1$
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
  
  
  // TODO (cs) the AbstractExtensionsSection is polluted with XSD specic stuff
  // need to clean that up!!
  // TODO (cs) we should avoid referencing WSDL model objects ... go thru facade instead
  public void setInput(IWorkbenchPart part, ISelection selection)
  {
    super.setInput(part, selection);
    isReadOnly = true;    
    ExtensibleElement extensibleElement = getExtensibleElement(input);
    if (extensibleElement != null)
    {    
      Element element = extensibleElement.getElement();
      if (element != null)
      {  
        isReadOnly = false;
        modelAdapter = WSDLModelAdapter.lookupOrCreateModelAdapter(element.getOwnerDocument());
        modelAdapter.getModelReconcileAdapter().addListener(internalNodeAdapter);
      }         
    }       
    addButton.setEnabled(!isReadOnly);
    removeButton.setEnabled(!isReadOnly);         
  }
  
  public void dispose()
  {
    super.dispose();
    if (modelAdapter != null)
    {
      modelAdapter.getModelReconcileAdapter().removeListener(internalNodeAdapter);
    }  
  }
  
  // TODO (cs) in the future we need to 'fix' things so that we can rid of this method
  // we need a way to do all of this via the facade so that we don't have any direct 
  // dependency on the WSDL1.1 model./ Similarly this class shouldn't need to 'know'
  // about AbstractModelCollection this is an underlying detail that needs to be hidden
  // 
  private static ExtensibleElement getExtensibleElement(Object o)
  {
    if (o instanceof AbstractModelCollection)
    {         
      o = ((AbstractModelCollection)o).getModel();
    }  
    if (o instanceof Adapter)
    {
      // TODO (cs) we need a way to do all of this via the facade
      // so that we don't have any direct dependency on the WSDL1.1 model
      // of course at the moment we call this class the W11ExtensionSections
      // so that's not a problem.  In the future though we'll want to reuse this 
      // class for WSDL 2.0.
      //       
      o = ((Adapter)o).getTarget();
    }      
    if (o instanceof ExtensibleElement)
    {
      return (ExtensibleElement)o;
    }
    return null;
  }

  protected AddExtensionsComponentDialog createAddExtensionsComponentDialog()
  {    
    ExtensibleElement extensibleElement = getExtensibleElement(input);
    if (extensibleElement != null)
    {  
      AddExtensionsComponentDialog dialog = new AddExtensionsComponentDialog(composite.getShell(), getExtensionsSchemasRegistry());   
      dialog.addElementsTableFilter(new AddExtensionsComponentDialogFilter(extensibleElement.getElement()));
      return dialog;
    }      
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
      ExtensibleElement extensibleElement = getExtensibleElement(inputElement);
      if (extensibleElement != null)
      {  
        List domElementList = new ArrayList();     
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
  /**
   * This filter is to be used by the dialog invoked when addButton is pressed
   */
  private class AddExtensionsComponentDialogFilter extends ViewerFilter
  {
    private Element hostElement;

    public AddExtensionsComponentDialogFilter(Element hostElement)
    {
      this.hostElement = hostElement;
    }

    public boolean select(Viewer viewer, Object parentElement, Object element)
    {
      if (element instanceof XSDElementDeclaration)
      {
        String namespace = ((XSDElementDeclaration) element).getTargetNamespace();
        String name = ((XSDElementDeclaration) element).getName();
        ExtensiblityElementFilter filter = (ExtensiblityElementFilter) WSDLEditorPlugin.getInstance().getExtensiblityElementFilterRegistry().getProperty(namespace, "");
        if (filter != null)
        {
          return filter.isValidContext(hostElement, name);
        }
        return true;
      }
      return true;
    }
  }
}
