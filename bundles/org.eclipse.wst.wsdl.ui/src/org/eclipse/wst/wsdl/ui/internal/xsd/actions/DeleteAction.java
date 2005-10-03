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
package org.eclipse.wst.wsdl.ui.internal.xsd.actions;

import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.ui.internal.actions.DeleteInterfaceAction;
import org.eclipse.wst.wsdl.ui.internal.util.WSDLEditorUtil;
import org.eclipse.wst.xml.core.internal.document.DocumentImpl;
import org.eclipse.wst.xsd.ui.internal.XSDEditorPlugin;
import org.eclipse.wst.xsd.ui.internal.util.TypesHelper;
import org.eclipse.wst.xsd.ui.internal.util.XSDDOMHelper;
import org.eclipse.xsd.XSDComponent;
import org.eclipse.xsd.XSDConcreteComponent;
import org.eclipse.xsd.XSDImport;
import org.eclipse.xsd.XSDInclude;
import org.eclipse.xsd.XSDRedefine;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.util.XSDConstants;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


// Should try to use xsdeditor's delete action....

public class DeleteAction extends Action implements DeleteInterfaceAction
{
  XSDSchema xsdSchema;
  Definition definition;
  List deleteList;
  
  /**
   * Constructor for DeleteAction.
   * @param text
   */
  public DeleteAction(String text)
  {
    super(XSDEditorPlugin.getXSDString("_UI_ACTION_DELETE"));
  }

  public XSDSchema getSchema()
  {
    return xsdSchema;
  }

  public void setSchema(XSDSchema xsdSchema)
  {
    this.xsdSchema = xsdSchema;
  }

  public void setDeleteList(List list) {
  	deleteList = list;
  }
  
  public List getDeleteList() {
  	return deleteList;
  }

  /*
   * @see IAction#run()
   */
  public void run()
  {
    List selections = getDeleteList();
    
    if (selections.isEmpty())
    {
      return;
    }
    
    Iterator iter = selections.iterator();
    DocumentImpl doc = null;
    while (iter.hasNext())
    {
      Object obj = iter.next();
      Element node;
      if (obj instanceof Element)
      {
        node = (Element)obj;
        Object elem = WSDLEditorUtil.getInstance().findModelObjectForElement(definition, node);
        if (elem instanceof XSDComponent)
        {
          xsdSchema = ((XSDComponent)elem).getSchema();
        }        
      }
      else if (obj instanceof XSDComponent)
      {
        node = ((XSDComponent)obj).getElement();
        xsdSchema = ((XSDComponent)obj).getSchema();
      }
      else if (obj instanceof XSDConcreteComponent) {
      	node = ((XSDConcreteComponent)obj).getElement();
      	xsdSchema = ((XSDConcreteComponent)obj).getSchema();
      }
      else
      {
        return; 
      }
        
      if (!XSDDOMHelper.inputEquals(node, XSDConstants.SCHEMA_ELEMENT_TAG, false))
      {
        
        if (doc == null)
        {
          doc = (DocumentImpl) node.getOwnerDocument();
          doc.getModel().beginRecording(this, XSDEditorPlugin.getXSDString("_UI_ACTION_DELETE_NODES"));
        }
  
        boolean refresh = cleanupReferences(node);
        XSDDOMHelper.removeNodeAndWhitespace(node);
        
        // Workaround to reset included elements in XSD model
        if (refresh)
        {
// TODO
//          getEditor().reparseSchema();
//          getEditor().getGraphViewer().setSchema(getEditor().getXSDSchema());
        }
      }
    }
    if (doc != null)
    {
      doc.getModel().endRecording(this);
    }
  }

  protected boolean cleanupReferences(Node deletedNode)
  {
    boolean refresh = false;
    XSDConcreteComponent comp = getSchema().getCorrespondingComponent(deletedNode);
    
    if (comp instanceof XSDInclude ||
        comp instanceof XSDImport ||
        comp instanceof XSDRedefine)
    {
//      XSDSchema resolvedSchema = ((XSDSchemaDirective)comp).getResolvedSchema();
      XSDSchema referencedSchema = null;
      if (comp instanceof XSDInclude)
      {
        referencedSchema = ((XSDInclude)comp).getIncorporatedSchema();
        refresh = true;
      }
      else if (comp instanceof XSDRedefine)
      {
        referencedSchema = ((XSDRedefine)comp).getIncorporatedSchema();
        refresh = true;
      }
      else if (comp instanceof XSDImport)
      {
         referencedSchema = ((XSDImport)comp).getResolvedSchema();
      }

      if (referencedSchema != null)
      {
//        XSDExternalFileCleanup cleanHelper = new XSDExternalFileCleanup(referencedSchema);
//        cleanHelper.visitSchema(getSchema());
//        // populate messages
//// TODO
////        getEditor().createTasksInTaskList(cleanHelper.getMessages());
      }
      if (comp instanceof XSDImport)
      {
        TypesHelper typesHelper = new TypesHelper(getSchema());
        typesHelper.updateMapAfterDelete((XSDImport)comp);
      }
    }
    else if (getSchema().equals(comp.getContainer()))
    {
//      BaseGlobalCleanup cleanHelper = null;
   //   Only need to clean up references if the component being deleted is global scoped
//      if (comp instanceof XSDElementDeclaration)
//      {
//        cleanHelper = new GlobalElementCleanup(comp);
//      }
//      else if (comp instanceof XSDModelGroupDefinition)
//      {
//        cleanHelper = new GlobalGroupCleanup(comp);
//      }
//      else if (comp instanceof XSDTypeDefinition)
//      {
//        cleanHelper = new GlobalSimpleOrComplexTypeCleanup(comp);
//      }
//      else if (comp instanceof XSDAttributeDeclaration)
//      {
//        cleanHelper = new GlobalAttributeCleanup(comp);
//      }
//      else if (comp instanceof XSDAttributeGroupDefinition)
//      {
//        cleanHelper = new GlobalAttributeGroupCleanup(comp);
//      }
//      
//      
//      if (cleanHelper != null)
//      {
//        cleanHelper.visitSchema(getSchema());
//        // populate messages
//// TODO
////        getEditor().createTasksInTaskList(cleanHelper.getMessages());
//      }
    }
    return refresh;
  }
}
