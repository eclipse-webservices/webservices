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
                                      
import java.util.ArrayList;

import org.eclipse.core.resources.IFile;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.wst.wsdl.Types;
import org.eclipse.wst.wsdl.XSDSchemaExtensibilityElement;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditor;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.extension.IMenuActionContributor;
import org.eclipse.wst.wsdl.ui.internal.graph.WSDLComponentViewer;
import org.eclipse.wst.wsdl.ui.internal.model.WSDLGroupObject;
import org.eclipse.wst.wsdl.ui.internal.xsd.actions.AddSchemaAction;
import org.eclipse.wst.wsdl.ui.internal.xsd.actions.BackAction;
import org.eclipse.wst.xml.core.document.DOMModel;
import org.eclipse.wst.xml.core.internal.document.DocumentImpl;
import org.eclipse.wst.xsd.ui.internal.XSDEditorPlugin;
import org.eclipse.wst.xsd.ui.internal.XSDMenuListener;
import org.eclipse.wst.xsd.ui.internal.actions.CreateElementAction;
import org.eclipse.wst.xsd.ui.internal.actions.CreateGroupAction;
import org.eclipse.wst.xsd.ui.internal.actions.DOMAttribute;
import org.eclipse.wst.xsd.ui.internal.graph.editparts.TopLevelComponentEditPart;
import org.eclipse.wst.xsd.ui.internal.graph.model.Category;
import org.eclipse.wst.xsd.ui.internal.provider.CategoryAdapter;
import org.eclipse.wst.xsd.ui.internal.util.XSDDOMHelper;
import org.eclipse.xsd.XSDConcreteComponent;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.util.XSDConstants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.eclipse.wst.xsd.ui.internal.gef.util.editparts.AbstractComponentViewerRootEditPart;

public class XSDMenuActionContributor implements IMenuActionContributor
{
  Element currElement;
  Document currDocument;
  XSDSchema xsdSchema;
//  protected ISelectionProvider iSelectionProvider;
  WSDLEditor wsdlEditor;

  public XSDMenuActionContributor(WSDLEditor wsdlEditor)
  {
    this.wsdlEditor = wsdlEditor;
  }

  public void contributeMenuActions(IMenuManager manager, Node node, Object object)
  {
    XSDMenuExtensionListener xsdMenuListener = new XSDMenuExtensionListener(wsdlEditor.getSelectionManager());
    xsdMenuListener.contributeMenuActions(manager, node, object);
  }

  class XSDMenuExtensionListener extends XSDMenuListener
  {
    public XSDMenuExtensionListener(ISelectionProvider selectionProvider)
    {
      super(selectionProvider);
    }

    public void contributeMenuActions(IMenuManager manager, Node node, Object object)
    {
      updateXSDSchema();
      AbstractComponentViewerRootEditPart editPart = (AbstractComponentViewerRootEditPart)wsdlEditor.getGraphViewer().getComponentViewer().getRootEditPart().getContents();
//    iSelectionProvider = ((WSDLContentOutlinePage)(wsdlEditor.getWSDLTextEditor().getContentOutlinePage())).getTreeViewer();
      EditPart focusEditPart = wsdlEditor.getGraphViewer().getComponentViewer().getFocusEditPart();

      if (object instanceof WSDLGroupObject)
      {
        WSDLGroupObject group = (WSDLGroupObject)object;
        if (group.getType() == WSDLGroupObject.TYPES_GROUP)
        {
          boolean typesExist = group.getParent().getTypes() != null;
          
          Element typesElement = null;
          if (typesExist)
          {
            Types types = group.getParent().getETypes();
            typesElement = types.getElement();
          }
          if (manager != null)
          {
            Document document = null;
            if (editPart.getViewer() instanceof WSDLComponentViewer) {
              WSDLComponentViewer wsdlComponentViewer = (WSDLComponentViewer) editPart.getViewer();
              document = wsdlComponentViewer.getWSDLEditor().getXMLDocument();
            }

            manager.add(new AddSchemaAction(group.getDefinition(), (Element)node, typesElement, document));
          }
        }
      } 
      else if (object instanceof Types)
      {
        // IMenuManager menu = manager.findMenuUsingPath("addchild");

        if (manager != null)
        {
          Types types = (Types)object;
          manager.add(new AddSchemaAction(types.getEnclosingDefinition(), (Element)node));
        }
      }
      
      if (xsdSchema == null)
      {
        return;
      }
      
      ArrayList attributes = null;
      Node relativeNode = null;
      
      BackAction backAction;
      
      if (object instanceof XSDSchema || focusEditPart instanceof TopLevelComponentEditPart || object instanceof Category)
      {
        backAction = new BackAction(WSDLEditorPlugin.getWSDLString("_UI_BACK_TO", "Definition"));
        backAction.setDefinition(wsdlEditor.getDefinition());
        backAction.setSelectionProvider(selectionProvider);
        backAction.setRootEditPart(editPart);
        backAction.setGraphViewer(wsdlEditor.getGraphViewer());
        manager.add(backAction);
        manager.add(new Separator());
      }
      else if (object instanceof XSDConcreteComponent)
      {
        backAction = new BackAction(XSDEditorPlugin.getXSDString("_UI_ACTION_BACK_TO_SCHEMA_VIEW"));
        // backAction.setXSDSchema(((XSDConcreteComponent)object).getSchema());
        backAction.setXSDSchema(xsdSchema);
        backAction.setSelectionProvider(selectionProvider);
        backAction.setRootEditPart(editPart);
        backAction.setGraphViewer(wsdlEditor.getGraphViewer());
        manager.add(backAction);
        manager.add(new Separator());
      }

      // CS: I'm removing the 'object instanceof Element' case from this test
      // for now the menu actions are driven from 'model' objects
      if (object instanceof XSDConcreteComponent || (object instanceof org.eclipse.wst.wsdl.XSDSchemaExtensibilityElement))
      {
        Element parent;
        if (object instanceof XSDConcreteComponent)
        {
          parent = ((XSDConcreteComponent)object).getElement();
          xsdSchema = ((XSDConcreteComponent)object).getSchema();
        }
        else if (object instanceof Element)
        {
          manager.add(new Separator());
          parent = (Element)object; 
        }
        else if (object instanceof org.eclipse.wst.wsdl.XSDSchemaExtensibilityElement)
        {
          XSDSchemaExtensibilityElement schema = (XSDSchemaExtensibilityElement)object;
          xsdSchema = schema.getSchema();
          manager.add(new Separator());
          parent = xsdSchema.getElement();
        }
        else
        {
          return;
        }
        
        currElement = parent;
        currDocument = parent.getOwnerDocument();
        
//        if (!(currDocument instanceof DocumentImpl))
//        {
//          return;
//        }
        
        addContextItems(manager, currElement, null);

        if (!(object instanceof XSDSchema) ||
           	(object instanceof XSDSchema && !(wsdlEditor.getGraphViewer().getComponentViewer().getInput() instanceof XSDSchema))) {
           	IStructuredSelection selections = (IStructuredSelection) wsdlEditor.getSelectionManager().getSelection();
           	manager.add(new Separator());
           	org.eclipse.wst.wsdl.ui.internal.actions.DeleteWSDLAndXSDAction deleteWSDLAndXSDAction = new org.eclipse.wst.wsdl.ui.internal.actions.DeleteWSDLAndXSDAction(selections.toList(), currElement, wsdlEditor);
           	deleteWSDLAndXSDAction.setEnabled(!isReadOnly);
           	manager.add(deleteWSDLAndXSDAction);
        }
      }
      else if (object instanceof Category
            || object instanceof CategoryAdapter)
      {
        int groupType = -1;

        if (object instanceof Category)
        {
          Category cg = (Category)object;
          xsdSchema = cg.getXSDSchema();
          groupType = cg.getGroupType();
        }
        else  // CategoryAdapter
        {
          CategoryAdapter category = (CategoryAdapter)object;
          groupType = category.getGroupType();
          xsdSchema = category.getXSDSchema();
        }
        Element parent = xsdSchema.getElement();
        currElement = parent;
        currDocument = parent.getOwnerDocument();
        switch (groupType)
        {
          case Category.TYPES:
          {
            attributes = new ArrayList();
            attributes.add(new DOMAttribute(XSDConstants.NAME_ATTRIBUTE, getNewGlobalTypeName("ComplexType")));
            Action action = addCreateElementAction(manager, XSDConstants.COMPLEXTYPE_ELEMENT_TAG, XSDEditorPlugin.getXSDString("_UI_ACTION_ADD_COMPLEX_TYPE"), attributes, parent, relativeNode);
            ((CreateElementAction)action).setIsGlobal(true);
            attributes = new ArrayList();
            attributes.add(new DOMAttribute(XSDConstants.NAME_ATTRIBUTE, getNewGlobalTypeName("SimpleType")));
            Action action2 = addCreateSimpleTypeAction(manager, XSDConstants.SIMPLETYPE_ELEMENT_TAG, XSDEditorPlugin.getXSDString("_UI_ACTION_ADD_SIMPLE_TYPE"), attributes, parent, relativeNode);
            ((CreateElementAction)action2).setIsGlobal(true);
            break;
          }
          case Category.ELEMENTS:
          {
            attributes = new ArrayList();
            attributes.add(new DOMAttribute(XSDConstants.NAME_ATTRIBUTE,
                                            getNewGlobalName(XSDConstants.ELEMENT_ELEMENT_TAG, "GlobalElement")));
            attributes.add(new DOMAttribute(XSDConstants.TYPE_ATTRIBUTE, getBuiltInStringQName()));
            Action action = addCreateElementAction(manager, XSDConstants.ELEMENT_ELEMENT_TAG, XSDEditorPlugin.getXSDString("_UI_ACTION_ADD_ELEMENT"), attributes, parent, relativeNode);
            ((CreateElementAction)action).setIsGlobal(true);
            break;
          }
          case Category.GROUPS:
          {
            attributes = new ArrayList();
            attributes.add(new DOMAttribute(XSDConstants.NAME_ATTRIBUTE,
                                            getNewGlobalName(XSDConstants.GROUP_ELEMENT_TAG, "Group")));
            CreateGroupAction groupAction = addCreateGroupAction(manager, XSDConstants.GROUP_ELEMENT_TAG, XSDEditorPlugin.getXSDString("_UI_ACTION_ADD_GROUP"), attributes, parent, relativeNode);
            groupAction.setIsGlobal(true);
            break;
          }
          case Category.ATTRIBUTES:
          {
            attributes = new ArrayList();
            attributes.add(new DOMAttribute(XSDConstants.NAME_ATTRIBUTE,
                                            getNewGlobalName(XSDConstants.ATTRIBUTE_ELEMENT_TAG, "GlobalAttribute")));
            attributes.add(new DOMAttribute(XSDConstants.TYPE_ATTRIBUTE, getBuiltInStringQName()));
            Action action = addCreateElementAction(manager, XSDConstants.ATTRIBUTE_ELEMENT_TAG, XSDEditorPlugin.getXSDString("_UI_ACTION_ADD_GLOBAL_ATTRIBUTE"), attributes, parent, relativeNode);
            ((CreateElementAction)action).setIsGlobal(true);
            break;
          }
          case Category.ATTRIBUTE_GROUPS:
          {
            attributes = new ArrayList();
            attributes.add(new DOMAttribute(XSDConstants.NAME_ATTRIBUTE,
                                            getNewGlobalName(XSDConstants.ATTRIBUTEGROUP_ELEMENT_TAG, "AttributeGroup")));
            Action action = addCreateElementAction(manager, XSDConstants.ATTRIBUTEGROUP_ELEMENT_TAG, XSDEditorPlugin.getXSDString("_UI_ACTION_ADD_ATTRIBUTE_GROUP"), attributes, parent, relativeNode);
            ((CreateElementAction)action).setIsGlobal(true);
            break; 
          }
          case Category.NOTATIONS:
          {
            attributes = new ArrayList();
            attributes.add(new DOMAttribute(XSDConstants.NAME_ATTRIBUTE, 
                           getNewGlobalName(XSDConstants.NOTATION_ELEMENT_TAG, "Notation")));
            attributes.add(new DOMAttribute(XSDConstants.PUBLIC_ATTRIBUTE, ""));
            Action action = addCreateElementAction(manager, XSDConstants.NOTATION_ELEMENT_TAG, XSDEditorPlugin.getXSDString("_UI_ACTION_ADD_NOTATION"), attributes, parent, relativeNode);
            ((CreateElementAction)action).setIsGlobal(true);
            break;
          }
          case Category.DIRECTIVES:
          {
            boolean b = true;
            NodeList children = parent.getChildNodes();
            Node effectiveRelativeNode = parent.getFirstChild();
            for (int i=0; i < children.getLength() && b; i++)
            {
              Node child = children.item(i);
              if (child != null && child instanceof Element)
              {
                if (XSDDOMHelper.inputEquals((Element)child, XSDConstants.INCLUDE_ELEMENT_TAG, false) ||
                    XSDDOMHelper.inputEquals((Element)child, XSDConstants.IMPORT_ELEMENT_TAG, false) ||
                    XSDDOMHelper.inputEquals((Element)child, XSDConstants.REDEFINE_ELEMENT_TAG, false) ||
                    XSDDOMHelper.inputEquals((Element)child, XSDConstants.ANNOTATION_ELEMENT_TAG, false))
                {
                  effectiveRelativeNode = child;
                }
                else
                {
                  b = false;
                }
              }
            }
            relativeNode = effectiveRelativeNode != null ? effectiveRelativeNode.getNextSibling() : null;
            attributes = new ArrayList();
            attributes.add(new DOMAttribute(XSDConstants.SCHEMALOCATION_ATTRIBUTE, ""));

            addCreateElementAction(manager, XSDConstants.INCLUDE_ELEMENT_TAG,XSDEditorPlugin.getXSDString("_UI_ACTION_ADD_INCLUDE"), attributes, parent, relativeNode);
            addCreateElementAction(manager, XSDConstants.IMPORT_ELEMENT_TAG,XSDEditorPlugin.getXSDString("_UI_ACTION_ADD_IMPORT"), null, parent, relativeNode);
            addCreateElementAction(manager, XSDConstants.REDEFINE_ELEMENT_TAG,XSDEditorPlugin.getXSDString("_UI_ACTION_ADD_REDEFINE"), attributes, parent, relativeNode);
            
            
            break;
          }
        }
      }
    }    

    protected IFile getFileResource()
    {
  ///// WSDL TODO
  //    if (getEditor() != null)
  //      return getEditor().getFileResource();
      return null;
    }

//    protected Object getSelectedElement()
//    {
//      return currElement;
//    }

    protected XSDSchema getXSDSchema()
    {
      return xsdSchema;
    }
  
    protected DOMModel getXMLModel()
    {
      if (currElement != null)
      {
        Object obj = currElement.getOwnerDocument();
        if (obj instanceof DocumentImpl)
        {
          DocumentImpl xmlDoc = (DocumentImpl) currElement.getOwnerDocument();
          return xmlDoc.getModel();
        }
      }
      return null;
    }
  }

}