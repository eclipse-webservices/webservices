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
package org.eclipse.wst.wsdl.ui.internal.properties.section;

import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.util.Assert;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IEditorPart;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditor;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.extension.WSDLEditorExtension;
import org.eclipse.wst.wsdl.ui.internal.extension.WSDLEditorExtensionRegistry;
import org.eclipse.wst.wsdl.ui.internal.graph.model.WSDLGraphModelAdapterFactory;
import org.eclipse.wst.wsdl.ui.internal.model.ModelAdapter;
import org.eclipse.wst.wsdl.ui.internal.model.ModelAdapterFactory;
import org.eclipse.wst.wsdl.ui.internal.model.WSDLModelAdapterFactory;
import org.eclipse.wst.xml.core.document.IDOMNode;
import org.eclipse.xsd.XSDConcreteComponent;
import org.w3c.dom.Element;

public class WSDLLabelProvider extends LabelProvider
{
  protected ModelAdapterFactory adapterFactory = new WSDLModelAdapterFactory();
	private WSDLTypeMapper typeMapper;
	
  protected WSDLEditorExtension[] labelProviderExtensions;
  protected LabelProvider[] labelProviders;
  IEditorPart editorPart;

  /**
   * 
   */
  public WSDLLabelProvider()
  {
    super();
    typeMapper = new WSDLTypeMapper();
    editorPart = WSDLEditorPlugin.getInstance().getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
    WSDLEditorExtensionRegistry registry = WSDLEditorPlugin.getInstance().getWSDLEditorExtensionRegistry(); 
    labelProviderExtensions = registry.getRegisteredExtensions(WSDLEditorExtension.OUTLINE_LABEL_PROVIDER); 
    labelProviders = new LabelProvider[labelProviderExtensions.length]; 
    for (int i = 0; i < labelProviderExtensions.length; i++)
    {
      labelProviders[i] = (LabelProvider)labelProviderExtensions[i].createExtensionObject(WSDLEditorExtension.OUTLINE_LABEL_PROVIDER, (WSDLEditor)editorPart);
    }
  }

	/**
	 * @see org.eclipse.jface.viewers.ILabelProvider#getImage(java.lang.Object)
	 */
	public Image getImage(Object object)
	{
		if (object == null || object.equals(StructuredSelection.EMPTY)) {
			return null;
		}
    Image result = null;           
    if (object instanceof StructuredSelection)
    {
      Object selected = ((StructuredSelection)object).getFirstElement();
      selected  = typeMapper.remapObject(selected);
      for (int i = 0; i < labelProviders.length; i++)
      {
        result = labelProviders[i].getImage(selected);
        if (result!=null)
        break;
      }
//      if (result != null)
//      {
//        ModelAdapter modelAdapter = adapterFactory.getAdapter(selected);
//        if (modelAdapter != null)
//        {
//          result = (Image)modelAdapter.getProperty(selected, ModelAdapter.IMAGE_PROPERTY);     
//        }
//      }
    }
    return result;
	}

	/**
	 * @see org.eclipse.jface.viewers.ILabelProvider#getText(java.lang.Object)
	 */
	public String getText(Object object)
	{
		if (object == null || object.equals(StructuredSelection.EMPTY)) {
			return "No items selected";//$NON-NLS-1$
		}
    String result = null;
    Object selected = null;
    if (object instanceof StructuredSelection)
    {
      selected = ((StructuredSelection)object).getFirstElement();
      selected  = typeMapper.remapObject(selected);
      
      // Override outline provider's getText for WSDLElements
      if (selected instanceof WSDLElement)
      {
      	if (((WSDLElement)selected).getElement() == null) {
      		return "";
      	}
        boolean isReadOnly = false;
        ModelAdapter adapter = WSDLGraphModelAdapterFactory.getWSDLGraphModelAdapterFactory().getAdapter(selected);
        if (adapter != null)
        {
          isReadOnly = Boolean.TRUE.equals(adapter.getProperty(selected, "isReadOnly"));
        }
        if (isReadOnly)
        {
          result = ((WSDLElement)selected).getElement().getLocalName() + " (" + WSDLEditorPlugin.getWSDLString("_UI_LABEL_READ_ONLY") + ")";   //$NON-NLS-1$
        }
        else
        {
          result = ((WSDLElement)selected).getElement().getLocalName();
        }
        return result;
      }
      else if (selected instanceof XSDConcreteComponent)
      {
        // Override for XSD Components
       
        Element element = ((XSDConcreteComponent)selected).getElement();
        if (element != null)
        {
          if (element instanceof IDOMNode)
          {
            return ((XSDConcreteComponent)selected).getElement().getLocalName();
          }
          else
          {
            return ((XSDConcreteComponent)selected).getElement().getLocalName() + " (" + WSDLEditorPlugin.getWSDLString("_UI_LABEL_READ_ONLY") + ")";   //$NON-NLS-1$
          }
        }
        else
        {
          return "(" + WSDLEditorPlugin.getWSDLString("_UI_LABEL_READ_ONLY") + ")";  //$NON-NLS-1$
        }

      }
      // otherwise get it from the extensions
      for (int i = 0; i < labelProviders.length; i++)
      {
        result = labelProviders[i].getText(selected);
        if (result!=null)
        break;
      }
    }
    else if (object instanceof TextSelection)
    {
    }
    

    return result;
	}

	/**
	 * Determine if a multiple object selection has been passed to the 
	 * label provider. If the objects is a IStructuredSelection, see if 
	 * all the objects in the selection are the same and if so, we want
	 * to provide labels for the common selected element.
	 * @param objects a single object or a IStructuredSelection.
	 * @param multiple first element in the array is true if there is multiple
	 * unequal selected elements in a IStructuredSelection.
	 * @return the object to get labels for.
	 */
	private Object getObject(Object objects, boolean multiple[]) {
		Assert.isNotNull(objects);
		Object object = null;
		return object;
	}

}
