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
package org.eclipse.wst.wsdl.ui.internal.actions; 
                      
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.wst.wsdl.Binding;
import org.eclipse.wst.wsdl.BindingFault;
import org.eclipse.wst.wsdl.BindingInput;
import org.eclipse.wst.wsdl.BindingOperation;
import org.eclipse.wst.wsdl.BindingOutput;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Port;
import org.eclipse.wst.wsdl.UnknownExtensibilityElement;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.util.WSDLConstants;
import org.eclipse.wst.wsdl.util.WSDLSwitch;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.extension.IMenuActionContributor;
import org.eclipse.wst.wsdl.ui.internal.model.WSDLGroupObject;
import org.eclipse.wst.wsdl.ui.internal.util.WSDLEditorUtil;
import org.eclipse.wst.xml.core.document.DOMNode;
import org.eclipse.wst.xml.core.format.FormatProcessorXML;
import org.eclipse.wst.xml.core.internal.contentmodel.CMDocument;
import org.eclipse.wst.xml.core.internal.contentmodel.CMElementDeclaration;
import org.eclipse.wst.xml.core.internal.contentmodel.CMNode;
import org.eclipse.wst.xml.core.internal.contentmodel.modelquery.ModelQuery;
import org.eclipse.wst.xml.core.internal.contentmodel.modelquery.ModelQueryAction;
import org.eclipse.wst.xml.core.internal.contentmodel.util.DOMContentBuilder;
import org.eclipse.wst.xml.core.internal.contentmodel.util.DOMContentBuilderImpl;
import org.eclipse.wst.xml.core.internal.contentmodel.util.DOMNamespaceHelper;
import org.eclipse.wst.xml.core.internal.modelquery.ModelQueryUtil;
import org.eclipse.wst.xml.ui.actions.MenuBuilder;
import org.eclipse.wst.xml.ui.util.XMLCommonResources;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

                             
/**
 * TODO... there are likely several places where we can refactor code from  AbstractNodeActionManager
 */
public class AddEEMenuActionContributor implements IMenuActionContributor
{                   
  protected MenuBuilder menuBuilder = new MenuBuilder();

  public void contributeMenuActions(final IMenuManager menu, Node node, Object object)
  {
    boolean isSubmenuRequired = false;
    Element element = null;

    if (object instanceof WSDLElement)
    {     
      WSDLSwitch wsdlSwitch = new WSDLSwitch()
      {                   
      	public Object caseBinding(Binding binding)
        {                                          
          return Boolean.TRUE;
	      } 

      	public Object caseBindingOperation(BindingOperation bindingOperation)
        {                                          
          return Boolean.TRUE;
	      } 

      	public Object caseBindingInput(BindingInput bindingInput)
        {                                          
          return Boolean.TRUE;
	      } 

      	public Object caseBindingOutput(BindingOutput bindingOutput)
        {                                          
          return Boolean.TRUE;
	      }

      	public Object caseBindingFault(BindingFault bindingFault)
        {                                          
          return Boolean.TRUE;
	      } 
 
      	public Object caseDefinition(Definition definition)
        {      
          return Boolean.TRUE;
	      }           
     
      	public Object casePort(Port port)
        {                                         
          return Boolean.TRUE;
        } 
      	public Object caseUnknownExtensibilityElement(UnknownExtensibilityElement unknownExtensibilityElement)
      	{
      		if (getExtensibilityElementActions(unknownExtensibilityElement.getElement()).size() > 0)
      		{      		
      			return Boolean.TRUE;
      		}
      		else
      		{
      			return Boolean.FALSE;
      		}
      	}
      };        
      isSubmenuRequired = wsdlSwitch.doSwitch((WSDLElement)object) != null;
      element = WSDLEditorUtil.getInstance().getElementForObject(object);      
    }
    else if (object instanceof WSDLGroupObject)
    {
      WSDLGroupObject groupObject = (WSDLGroupObject)object;
     
      switch (groupObject.getType())
      {   
        case WSDLGroupObject.EXTENSIBILITY_ELEMENTS_GROUP : 
        {
          isSubmenuRequired = true;
          element = WSDLEditorUtil.getInstance().getElementForObject(groupObject.getDefinition());    
          break;
        }
      }   
    }

    if (isSubmenuRequired)
    {
      MenuManager submenu = new MenuManager(WSDLEditorPlugin.getWSDLString("_UI_ADD_EXTENSIBILITY_ELEMENT"));  //$NON-NLS-1$
      // here I'm attempting to add the menu item in a 'good' place in the exisiting menu
      // todo add menu groups to avoid the need for this test
      //
//      if (menu.find("addchild") != null)
//      {
//      	menu.insertAfter("addchild", submenu);
//      }
//      else
//      {
//      	menu.add(submenu);
//      }	 
    	menu.add(submenu);
      if (element != null && element instanceof DOMNode)
      {
        addExtensibilityElementActions(submenu, element);
      }
    }
  }

  public void addExtensibilityElementActions(IMenuManager menu, Element element)
  {                 
    ModelQuery modelQuery = ModelQueryUtil.getModelQuery(element.getOwnerDocument());
    CMElementDeclaration ed = modelQuery.getCMElementDeclaration(element);
    
    if (ed != null)
    {
      addActionHelper(menu, getExtensibilityElementActions(element));
    }
  }
  
  public List getExtensibilityElementActions(Element element) {
    List modelQueryActionList = new ArrayList();
  	ModelQuery modelQuery = ModelQueryUtil.getModelQuery(element.getOwnerDocument());
    CMElementDeclaration ed = modelQuery.getCMElementDeclaration(element);                                                                                                                            

    if (ed != null)
    {                                                                                    
      // add insert child node actions
      //
      int ic = ModelQuery.INCLUDE_CHILD_NODES;
      int vc = ModelQuery.VALIDITY_STRICT;

      modelQuery.getInsertActions(element, ed, -1, ic, vc, modelQueryActionList);
    }
    
    return modelQueryActionList;
  }
  

  protected void addActionHelper(IMenuManager menu, List modelQueryActionList)
  {                          
    List actionList = new Vector();

    for (Iterator i = modelQueryActionList.iterator(); i.hasNext(); )
    {                                                   
      ModelQueryAction action = (ModelQueryAction)i.next();
      CMNode cmnode = action.getCMNode();
      if (cmnode != null)
      {                                       
        boolean isOtherNamespace = false;
        CMDocument cmDocument = (CMDocument)cmnode.getProperty("CMDocument");
        if (cmDocument != null)          
        {             
          String namespaceURI = (String)cmDocument.getProperty("http://org.eclipse.wst/cm/properties/targetNamespaceURI"); 
          isOtherNamespace = namespaceURI != null && !namespaceURI.equals(WSDLConstants.WSDL_NAMESPACE_URI);
        }

        if (isOtherNamespace)
        {
          int cmNodeType = cmnode.getNodeType();
          if (action.getKind() == ModelQueryAction.INSERT)
          {                                                 
            switch (cmNodeType)                             
            {
              case CMNode.ELEMENT_DECLARATION :
              {
                actionList.add(new AddNodeAction((CMElementDeclaration)cmnode, action.getParent(), action.getStartIndex()));
                break;
              }
            }
          }           
        }
      }   
    }  
    menuBuilder.populateMenu(menu, actionList, false);
  }   

  /**
   * AddNodeAction
   */
  public class AddNodeAction extends BaseNodeAction
  {
    protected String description;
    protected String undoDescription; 
    protected CMNode cmnode;
    protected int index;
    protected Node parent;


    public AddNodeAction(CMNode cmnode, Node parent,  int index)
    {                                 
      this.cmnode = cmnode;
      this.parent = parent;
      this.index = index;                                   
                                                                                                
      String text = getLabel(parent, cmnode);
      setText(text);     
      description = text;
      undoDescription = XMLCommonResources.getInstance().getString("_UI_MENU_ADD") + " " + text;
      //setImageDescriptor(imageDescriptorCache.getImageDescriptor(cmnode)); 
    }

    public String getLabel(Node parent, CMNode cmnode)
    {                                                
      String result = "?" + cmnode + "?";
      if (cmnode != null)
      {                
        result = DOMNamespaceHelper.computeName(cmnode, parent, null);         
      }
      return result;   
    }  

    public Node getNode()
    {
      return parent;
    }

    public String getUndoDescription()
    {
      return undoDescription;
    }

    public void run()
    {
      beginRecording();

      if (cmnode != null && parent != null)
      {
        Document document = parent.getNodeType() == Node.DOCUMENT_NODE ? (Document)parent : parent.getOwnerDocument();        

        DOMContentBuilder builder = new DOMContentBuilderImpl(document); 
        builder.setProperty(DOMContentBuilder.PROPERTY_BUILD_BLANK_TEXT_NODES, Boolean.TRUE);
        builder.setBuildPolicy(DOMContentBuilder.BUILD_ONLY_REQUIRED_CONTENT);
        builder.build(parent, cmnode);
        insertNodesAtIndex(parent, builder.getResult(), index, true);
      }

      endRecording();
    } 

    public void insertNodesAtIndex(Node parent, List list, int index, boolean format)
    {                   
      NodeList nodeList = parent.getChildNodes();
      if (index == -1)
      {
        index = nodeList.getLength();
      }
      Node refChild = (index < nodeList.getLength()) ? nodeList.item(index) : null;
    
      // here we consider the case where the previous node is a 'white space' Text node
      // we should really do the insert before this node
      //
      int prevIndex = index - 1;
      Node prevChild = (prevIndex < nodeList.getLength()) ? nodeList.item(prevIndex) : null;
	    if (isWhitespaceTextNode(prevChild)) 
      {
	  	  refChild = prevChild;
	    }
    
      for (Iterator i = list.iterator(); i.hasNext(); )
      {
        Node newNode = (Node)i.next();
    
        if (newNode.getNodeType() == Node.ATTRIBUTE_NODE)
        {
          Element parentElement = (Element)parent;
          parentElement.setAttributeNode((Attr)newNode);
        }
        else
        {
          parent.insertBefore(newNode, refChild);
        }  
      }
    
      for (Iterator i = list.iterator(); i.hasNext(); )
      {
        Node newNode = (Node)i.next();
        if (format)
        {                                                             
		      FormatProcessorXML formatProcessorXML = new FormatProcessorXML();
		      formatProcessorXML.formatNode((DOMNode)newNode);
        }  
      }                      
	    //setViewerSelection(list);
    }   

    protected boolean isWhitespaceTextNode(Node node) 
    {
	    return (node != null) && (node.getNodeType() == Node.TEXT_NODE) && (node.getNodeValue().trim().length() == 0);
    } 
  }                                       
}    