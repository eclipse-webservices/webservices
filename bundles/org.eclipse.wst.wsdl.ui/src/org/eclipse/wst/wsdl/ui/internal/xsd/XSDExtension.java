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
package org.eclipse.wst.wsdl.ui.internal.xsd;
                                           

// import org.eclipse.emf.edit.provider.ItemProvider;
// import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
// import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.wst.wsdl.XSDSchemaExtensibilityElement;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditor;
import org.eclipse.wst.wsdl.ui.internal.extension.WSDLEditorExtension;
import org.eclipse.wst.wsdl.ui.internal.xsd.actions.DeleteAction;
import org.eclipse.wst.xsd.ui.internal.XSDEditorPlugin;
import org.eclipse.wst.xsd.ui.internal.graph.model.Category;
import org.eclipse.wst.xsd.ui.internal.provider.CategoryAdapter;
import org.eclipse.wst.xsd.ui.internal.provider.XSDAdapterFactoryLabelProvider;
import org.eclipse.wst.xsd.ui.internal.provider.XSDModelAdapterFactoryImpl;
import org.eclipse.xsd.XSDConcreteComponent;


//
//
public class XSDExtension implements WSDLEditorExtension
{
  XSDModelAdapterFactoryImpl xsdModelAdapterFactory;
  XSDAdapterFactoryLabelProvider adapterFactoryLabelProvider;
  
  public XSDExtension()
  {
    xsdModelAdapterFactory = new XSDModelAdapterFactoryImpl();
    adapterFactoryLabelProvider = new XSDAdapterFactoryLabelProvider(xsdModelAdapterFactory);
  }
  
  public boolean isExtensionTypeSupported(int type)
  {
    return 
           //CS comment these out until the port is complete
           type == OUTLINE_TREE_CONTENT_PROVIDER || 
           type == OUTLINE_LABEL_PROVIDER ||
           type == PROPERTY_SOURCE_PROVIDER ||
           type == PROPERTY_SECTION_DESCRIPTOR_PROVIDER ||
           type == MENU_ACTION_CONTRIBUTOR ||
           type == DETAILS_VIEWER_PROVIDER ||
           type == EDIT_PART_FACTORY || 
           type == TYPE_SYSTEM_PROVIDER ||
           type == NODE_RECONCILER ||
           type == NODE_ASSOCIATION_PROVIDER ||
           type == MODEL_QUERY_CONTRIBUTOR;
  }                                       

  public boolean isApplicable(Object object)
  {                     
    return (object instanceof XSDSchemaExtensibilityElement || 
            object instanceof XSDConcreteComponent ||
            object instanceof CategoryAdapter ||
            object instanceof Category);
            // || object instanceof ItemProvider);
  } 

  public Object createExtensionObject(int type, WSDLEditor wsdlEditor)
  {
    Object result = null;
    switch (type)
    {
      case OUTLINE_TREE_CONTENT_PROVIDER :
      {
        result = new XSDModelAdapterContentProvider(xsdModelAdapterFactory); 
//        result = new XSDModelAdapterContentProvider();
        // result = new AdapterFactoryContentProvider(XSDExtensionPlugin.getXSDSemanticItemProviderAdapterFactory());
        break;
      }
      case OUTLINE_LABEL_PROVIDER :
      {
        result = new XSDLabelProvider(adapterFactoryLabelProvider);
        // result = new AdapterFactoryLabelProvider(XSDExtensionPlugin.getXSDSemanticItemProviderAdapterFactory());
        break;
      }
      case DETAILS_VIEWER_PROVIDER :
      {
        // result = new XSDDetailsViewerProvider();
        result = null;
        break;
      }
      case MENU_ACTION_CONTRIBUTOR :
      {
        result = new XSDMenuActionContributor(wsdlEditor);
        break;
      }
      case TYPE_SYSTEM_PROVIDER :
      {
        result = new XSDTypeSystemProvider();
        break;
      }    
      case EDIT_PART_FACTORY :
      {
        result = new XSDExtensionEditPartFactory();
        break;
      }
      case NODE_RECONCILER :
      {
        result = new XSDNodeReconciler();
        break;
      }   
      case NODE_ASSOCIATION_PROVIDER :
      {
        result = new XSDNodeAssociationProvider();
        break;
      }  
      case MODEL_QUERY_CONTRIBUTOR :
      {
        //result = new XSDModelQueryContributor(wsdlEditor);
        break;
      }
      case PROPERTY_SOURCE_PROVIDER:
      {
        result = new XSDPropertySourceProvider();
        break;
      }
      case PROPERTY_SECTION_DESCRIPTOR_PROVIDER:
      {
        result = new XSDSectionDescriptorProvider();
        break;
      }
      case XSD_DELETE_ACTION:
      {
        result = new DeleteAction(XSDEditorPlugin.getXSDString("_UI_ACTION_DELETE"));
        break;
      }
    }
    return result;
  }
} 