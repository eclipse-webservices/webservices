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
                      
import java.util.HashMap;
import java.util.List;


import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Preferences;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.wst.sse.core.preferences.CommonModelPreferenceNames;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.util.WSDLEditorUtil;
import org.eclipse.wst.wsdl.ui.internal.widgets.NewComponentDialog;
import org.eclipse.wst.wsdl.internal.util.WSDLConstants;
import org.eclipse.wst.xml.core.XMLModelPlugin;
import org.eclipse.wst.xml.core.document.XMLNode;
import org.eclipse.wst.xml.core.format.FormatProcessorXML;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


public class AddElementAction extends BaseNodeAction
{ 
  protected Node parentNode;
  protected String prefix;
  protected String nodeName; 
  protected Element newElement;
  protected Node relativeNode = null;    
  protected IEditorPart editorPart;
  protected Definition definition;
  protected Document document;
  protected boolean computeTopLevelRefChild;

  public AddElementAction(String text, String imageDescriptorKey, Node parentNode, String nodeName)
  {
    setText(text);
    setImageDescriptor(WSDLEditorPlugin.getImageDescriptor(imageDescriptorKey));    
    this.parentNode = parentNode;
    this.nodeName = nodeName;
  } 
                                  
  public AddElementAction(String text, String imageDescriptorKey, Node parentNode, String prefix, String localName)
  {
    setText(text);
    setImageDescriptor(WSDLEditorPlugin.getImageDescriptor(imageDescriptorKey));    
    this.parentNode = parentNode;
    this.prefix = prefix;
    this.nodeName = localName;
  } 

  public AddElementAction(String text, Node parentNode, String prefix, String localName)
  {
    setText(text);
    setImageDescriptor(null);
    this.parentNode = parentNode;
    this.prefix = prefix;
    this.nodeName = localName;
  } 

  public AddElementAction(Node parentNode, String prefix, String localName, Node relativeNode)
  {
    this.parentNode = parentNode;
    this.prefix = prefix;
    this.nodeName = localName;
    this.relativeNode = relativeNode; 
  }
  
  public void setComputeTopLevelRefChild(boolean isEnabled)
  {
    computeTopLevelRefChild = isEnabled; 
  }

  protected void setEditorPart(IEditorPart editorPart)
  {
    this.editorPart = editorPart;
  }

  protected boolean showDialog()
  {
    return true;
  }

  public void run()
  {  	
    boolean ok = showDialog();    
    if (ok)
    {
      beginRecording();
      performAddElement();
      endRecording();
    }
	}

  protected void performAddElement()
  {
    if (parentNode != null)
    {                                        
      newElement = createElement(nodeName);
      addAttributes(newElement);
      if (relativeNode == null && computeTopLevelRefChild)
      {
		relativeNode = computeTopLevelRefChild(newElement);
      }
      
      if (relativeNode == null)
      {      
        parentNode.appendChild(newElement);   
      }
      else
      {
        parentNode.insertBefore(newElement, relativeNode);
      }    
      //format(parentNode);
      format(newElement); 
      selectObjectForNewElement();
    }  
  }


  public Node getNode()
  {
  	if (parentNode != null) {
  		return parentNode;
  	}
  	else {
  		return document;
  	}
  }                    
 

  public String getUndoDescription()
  {
    return WSDLEditorPlugin.getWSDLString("_UI_ACTION_ADD");
  }


  protected Element createElement(String nodeName)
  {
    Document document = parentNode.getOwnerDocument();

    Element element = (prefix != null && prefix.length() > 0) ? 
                        document.createElement(prefix + ":" + nodeName) : 
                        document.createElement(nodeName);

    return element;   
  }   

  protected void addAttributes(Element newElement)
  {                                              
  }
  

  protected void format(Node parentNode)
  {
    if (parentNode instanceof XMLNode) 
    {
		  // format selected node                                                    
      FormatProcessorXML formatProcessorXML = new FormatProcessorXML();
      formatProcessorXML.formatNode((XMLNode)parentNode);
      
    }
  }   
     
  protected Element getDefinitionElement(Element parentElement)
  {
    Element definitionElement = null;          

    for (Node node = parentElement.getOwnerDocument().getFirstChild(); node != null; node = node.getNextSibling())
    {
      if (node.getNodeType() == Node.ELEMENT_NODE)
      {
        Element element = (Element)node;
        if (WSDLEditorUtil.getInstance().getWSDLType(element) == WSDLConstants.DEFINITION)
        {
          definitionElement = element;
          break;
        }
      }
    }  
    return definitionElement;
  }   

  public Element getNewElement()
  {
    return newElement;
  }      

  public void setDefinition(Definition definition)
  {
    this.definition = definition; 
  }

  public void selectObjectForNewElement()
  {
  	if (editorPart == null) {
        editorPart = WSDLEditorPlugin.getInstance().getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
  	}
    if (editorPart != null && definition != null)
    {
      Object object = WSDLEditorUtil.getInstance().findModelObjectForElement(definition, newElement);
      if (object != null)
      {
        ISelectionProvider selectionProvider = (ISelectionProvider)editorPart.getAdapter(ISelectionProvider.class);
        if (selectionProvider != null)
        {
          selectionProvider.setSelection(new StructuredSelection(object));
        }
      }  
    }   
  }   
  
  public void selectObject(WSDLElement object)
  {
  	if (editorPart == null) {
        editorPart = WSDLEditorPlugin.getInstance().getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
  	}
    if (editorPart != null && definition != null)
    {
      if (object != null)
      {
        ISelectionProvider selectionProvider = (ISelectionProvider)editorPart.getAdapter(ISelectionProvider.class);
        if (selectionProvider != null)
        {
          selectionProvider.setSelection(new StructuredSelection(object));
        }
      }  
    }    
  }   

  public String showDialogHelper(String title, String defaultName, List usedNames)
  {   
    String result = defaultName;                                                                                             
    NewComponentDialog dialog = new NewComponentDialog(WSDLEditorPlugin.getShell(), title, defaultName, usedNames);
    int rc = dialog.createAndOpen();
    if (rc == IDialogConstants.OK_ID)
    {
      result = dialog.getName();  
    }
    else
    {
      result = null;
    }               
    return result;
  }
  
  protected Node computeTopLevelRefChild(Node nodeToAdd)
  {
  	Node result = null;
    int a = getPrecedence(nodeToAdd);
      	
    for (Node node = parentNode.getFirstChild(); node != null; node = node.getNextSibling())
    {
      if (node.getNodeType() == Node.ELEMENT_NODE)
      {
	    int b = getPrecedence(node);
	    if (b > a)
	    {
	      result = node;	
          break;	    
	    }        
      } 
    }
    return result;
  }
  
  protected void createDefinitionStub() {
  	if (document != null) {
  		// Create the Definitions element with proper namespace
  	    Preferences preference = XMLModelPlugin.getDefault().getPluginPreferences();
  		String charSet = preference.getString(CommonModelPreferenceNames.OUTPUT_CODESET);
  	     if (charSet == null || charSet.trim().equals(""))
  	    {
  	    	charSet = "UTF-8";
  	    }  	     
  		document.appendChild(document.createProcessingInstruction("xml", "version=\"1.0\" encoding=\"" + charSet + "\""));
  		Element root = document.createElement("wsdl:definitions");  		
  		document.appendChild(root);

  		// Add various namespace attributes here. 
  		root.setAttribute("xmlns:soap", "http://schemas.xmlsoap.org/wsdl/soap/");
  		root.setAttribute("xmlns:tns", getDefaultNamespace());
  		root.setAttribute("xmlns:wsdl", "http://schemas.xmlsoap.org/wsdl/");
  		root.setAttribute("xmlns:xsd", "http://www.w3.org/2001/XMLSchema");
  		root.setAttribute("name", getFileName());
  		root.setAttribute("targetNamespace", getDefaultNamespace());

  		definition.setElement(root);	
  		parentNode = root;
  		prefix = definition.getPrefix(WSDLConstants.WSDL_NAMESPACE_URI);
  	}
  }

  private String getDefaultNamespace()
  {
    String namespace = WSDLEditorPlugin.getInstance().getPreferenceStore().getString(WSDLEditorPlugin.getWSDLString("_UI_PREF_PAGE_DEFAULT_TARGET_NAMESPACE"));
    if (!namespace.endsWith("/")) {
    	namespace = namespace.concat("/");
    }
    
    namespace += getFileName() + "/";

    return namespace;
  }
  
  private String getFileName() {
    String fileLocation = definition.getLocation();
  	IPath filePath = new Path(fileLocation);
  	return filePath.removeFileExtension().lastSegment().toString();
  }

  protected static HashMap precedenceMap = createPrecedenceMap();

  protected static int getPrecedence(Node node)
  {
  	int result = 2;
  	String localName = node.getLocalName();
  	if (localName != null)
  	{  	
      Integer integer = (Integer)precedenceMap.get(localName);
      if (integer != null)
      {      
        result = integer.intValue();
      }
  	}
  	return result;
  }
    
  protected static HashMap createPrecedenceMap()
	{
		HashMap hashMap = new HashMap();
		hashMap.put(WSDLConstants.DOCUMENTATION_ELEMENT_TAG, new Integer(1));
		hashMap.put(WSDLConstants.IMPORT_ELEMENT_TAG, new Integer(3));
		hashMap.put(WSDLConstants.TYPES_ELEMENT_TAG, new Integer(4));
		hashMap.put(WSDLConstants.MESSAGE_ELEMENT_TAG, new Integer(5));
		hashMap.put(WSDLConstants.PORT_TYPE_ELEMENT_TAG, new Integer(6));
		hashMap.put(WSDLConstants.BINDING_ELEMENT_TAG, new Integer(7));
		hashMap.put(WSDLConstants.SERVICE_ELEMENT_TAG, new Integer(8));
		return hashMap;
	}
}