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
package org.eclipse.wst.wsdl.ui.internal.adapters.commands;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.wst.wsdl.ExtensibilityElement;
import org.eclipse.wst.wsdl.Port;
import org.eclipse.wst.wsdl.binding.http.HTTPAddress;
import org.eclipse.wst.wsdl.binding.soap.SOAPAddress;
import org.eclipse.wst.wsdl.util.WSDLConstants;
import org.eclipse.wst.xml.core.internal.contentmodel.CMDocument;
import org.eclipse.wst.xml.core.internal.contentmodel.CMElementDeclaration;
import org.eclipse.wst.xml.core.internal.contentmodel.CMNode;
import org.eclipse.wst.xml.core.internal.contentmodel.modelquery.ModelQuery;
import org.eclipse.wst.xml.core.internal.contentmodel.modelquery.ModelQueryAction;
import org.eclipse.wst.xml.core.internal.contentmodel.util.DOMContentBuilder;
import org.eclipse.wst.xml.core.internal.contentmodel.util.DOMContentBuilderImpl;
import org.eclipse.wst.xml.core.internal.modelquery.ModelQueryUtil;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.core.internal.provisional.format.FormatProcessorXML;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class W11SetAddressCommand extends Command {
	private Port port;
	private String newAddress;
	
	public W11SetAddressCommand(Port port, String newAddress) {
        super("");  // TODO: Need to add String here...
		this.port = port;
		this.newAddress = newAddress;
	}
	
	public void execute() {
		// Should the actual set address code live in it's 'own separate' command??
		if (!setAddress()) {
			createNewExtensibilityElement();
			setAddress();
		}
	}
	
	private boolean setAddress() {
		boolean success = false;
		
		if (port.getEExtensibilityElements().size() > 0) {
			Iterator eeIt = port.getEExtensibilityElements().iterator();
			while (eeIt.hasNext()) {
				ExtensibilityElement ee = ((ExtensibilityElement) eeIt.next());
				if (ee instanceof SOAPAddress) {
					((SOAPAddress) ee).setLocationURI(newAddress);
					success = true;
				}
				else if (ee instanceof HTTPAddress) {
					((HTTPAddress) ee).setLocationURI(newAddress);
					success = true;
				}
	//			ee.getElement().setAttribute("location", newAddress);
			}
		}
		
		return success;
	}

	/*
	 * TODO: rmah: We need to clean the code below.....
	 * Things should be wrapped up and placed in a common location.  Currently, this is
	 * from the AddEEMenuActionContriubor.java class
	 */
	private void createNewExtensibilityElement() {
		List modelQueryActionList = new ArrayList();
		ModelQuery modelQuery = ModelQueryUtil.getModelQuery(port.getElement().getOwnerDocument());
	    CMElementDeclaration ed = modelQuery.getCMElementDeclaration(port.getElement());
	    
	    if (ed != null)
	    {
	          // add insert child node actions
	          //
	          int ic = ModelQuery.INCLUDE_CHILD_NODES;
	          int vc = ModelQuery.VALIDITY_STRICT;

	          modelQuery.getInsertActions(port.getElement(), ed, -1, ic, vc, modelQueryActionList);
	     }
	    
	    createNewExtensibilityElementHelper1(modelQueryActionList);
	}
	
	private void createNewExtensibilityElementHelper1(List modelQueryActionList)
	  {                          
	    for (Iterator i = modelQueryActionList.iterator(); i.hasNext(); )
	    {                                                   
	      ModelQueryAction action = (ModelQueryAction)i.next();
	      CMNode cmnode = action.getCMNode();
	      if (cmnode != null)
	      {                                       
	        boolean isOtherNamespace = false;
	        CMDocument cmDocument = (CMDocument)cmnode.getProperty("CMDocument"); //$NON-NLS-1$
	        if (cmDocument != null)          
	        {             
	          String namespaceURI = (String)cmDocument.getProperty("http://org.eclipse.wst/cm/properties/targetNamespaceURI");  //$NON-NLS-1$
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
	            	  createNewExtensibilityElementHelper2((CMElementDeclaration)cmnode, action.getParent(), action.getStartIndex());
	                return;
	              }
	            }
	          }           
	        }
	      }   
	    }
	  }

    public void createNewExtensibilityElementHelper2(CMNode cmnode, Node parent, int index)
    {
      if (cmnode != null && parent != null)
      {
        Document document = parent.getNodeType() == Node.DOCUMENT_NODE ? (Document)parent : parent.getOwnerDocument();        

        DOMContentBuilder builder = new DOMContentBuilderImpl(document); 
        builder.setProperty(DOMContentBuilder.PROPERTY_BUILD_BLANK_TEXT_NODES, Boolean.TRUE);
        builder.setBuildPolicy(DOMContentBuilder.BUILD_ONLY_REQUIRED_CONTENT);
        builder.build(parent, cmnode);
        insertNodesAtIndex(parent, builder.getResult(), index, true);
      }
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
		      formatProcessorXML.formatNode((IDOMNode)newNode);
        }  
      }                      
	    //setViewerSelection(list);
    }   
	
    protected boolean isWhitespaceTextNode(Node node) 
    {
	    return (node != null) && (node.getNodeType() == Node.TEXT_NODE) && (node.getNodeValue().trim().length() == 0);
    }
}
