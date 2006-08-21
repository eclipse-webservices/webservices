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
package org.eclipse.wst.wsdl.ui.internal.actions;


import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.ui.internal.Messages;
import org.eclipse.wst.wsdl.ui.internal.util.WSDLEditorUtil;
import org.eclipse.wst.wsdl.util.WSDLConstants;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.core.internal.provisional.format.FormatProcessorXML;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class AddWSISchemaImportAction extends BaseNodeAction
{
    protected Definition definition;
    protected Element definitionElement;
    protected String namespace;
    protected String location;
    protected String elementDeclarationNamespacePrefix;

    public AddWSISchemaImportAction(Definition definition, String namespace, String location)
    {
        this.definition = definition;
        this.namespace = namespace;
        this.location = location;
        definitionElement = WSDLEditorUtil.getInstance().getElementForObject(definition);
    }

    public Node getNode()
    {
        return definitionElement;
    }
    
    public String getUndoDescription()
    {
    	return Messages._UI_ACTION_ADD_IMPORT; //$NON-NLS-1$
    }

    protected Element getOrCreateTypesElement()
    {
        Element typesElement = null;
        if (definition.getTypes() == null)
        {
            if (definitionElement != null)
            {
                AddElementAction addTypesAction = new AddElementAction("", "icons/xsd_obj.gif", definitionElement, definitionElement.getPrefix(), "types"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                addTypesAction.setComputeTopLevelRefChild(true);
                addTypesAction.run();
                typesElement = addTypesAction.getNewElement();
                format(typesElement);
            }
        }
        else
        {
            typesElement = WSDLEditorUtil.getInstance().getElementForObject(definition.getTypes());
        }
        return typesElement;
    }
    
    protected Element getImportHolderElement(Element typesElement) {
    	Element importHolderElement = null;
        NodeList nodeList = typesElement.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++)
        {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE)
            {
                Element element = (Element) node;
                if ("schema".equals(element.getLocalName()) && element.getAttribute("targetNamespace") == null) //$NON-NLS-1$ //$NON-NLS-2$
                {
                    importHolderElement = element;
                    break;
                }
            }
        }
        
        return importHolderElement;
    }

    public Element getOrCreateImportHolderElement(Element typesElement)
    {
        Element importHolderElement = getImportHolderElement(typesElement);
        if (importHolderElement == null)
        {
            AddElementAction addImportHolderAction = new AddElementAction("", "icons/xsd_obj.gif", typesElement, "xsd", "schema") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
            {
                protected void addAttributes(Element newElement)
                {
                    newElement.setAttribute("xmlns:xsd", WSDLConstants.XSD_NAMESPACE_URI); //$NON-NLS-1$
                }
            };
            addImportHolderAction.run();
            importHolderElement = addImportHolderAction.getNewElement();
            format(importHolderElement);
        }
        return importHolderElement;
    }
    
    // We don't want to add the import if it's already there
    protected boolean importNotAlreadyExists() {
    	boolean notExists = true;
    	
    	if (definition.getTypes() != null) {
    		Element typesElement = WSDLEditorUtil.getInstance().getElementForObject(definition.getTypes());
    		Element schemaElement = getImportHolderElement(typesElement);

    		if (schemaElement != null) {
    			// Collect children
    			Node node = schemaElement.getFirstChild();
    			java.util.Vector schemaChildren = new java.util.Vector();
    			if (node != null) {
    				Node child = node;
					
    				if (!(child instanceof org.eclipse.wst.xml.core.internal.document.TextImpl)) {
    					schemaChildren.addElement(child);
    				}
					
    				while (child.getNextSibling() != null) {
    					child = child.getNextSibling();
    					if (!(child instanceof org.eclipse.wst.xml.core.internal.document.TextImpl)) {
    						schemaChildren.addElement(child);
    					}
    				}

    				for (int index = 0; index < schemaChildren.size(); index++) {
    					Element schemaChild = (Element) schemaChildren.elementAt(index);
    					String schemaLocation = schemaChild.getAttribute("schemaLocation"); //$NON-NLS-1$
    					String schemaNamespace = schemaChild.getAttribute("namespace"); //$NON-NLS-1$
				
    					if (schemaLocation != null && namespace != null &&
    							schemaLocation.equals(location) && schemaNamespace.equals(namespace)) {
    						notExists = false;
    						break;
    					}
    				}
    			}
    		}
    	}
    	
    	return notExists;
    }

    public void run()
    {
        if (definitionElement != null && importNotAlreadyExists())
        {
            beginRecording();
            performAddElement();
            endRecording();
        }
    }
    public void performAddElement()
    {
        try
        {
            Element typesElement = getOrCreateTypesElement();
            Element importHolderElement = getOrCreateImportHolderElement(typesElement);

            AddElementAction addImportAction = new AddElementAction("", "icons/xsd_obj.gif", importHolderElement, importHolderElement.getPrefix(), "import") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            {
                protected void addAttributes(Element newElement)
                {
                    newElement.setAttribute("namespace", namespace); //$NON-NLS-1$
                    newElement.setAttribute("schemaLocation", location); //$NON-NLS-1$
                }
            };
            addImportAction.run();
            Element newElement = addImportAction.getNewElement();
            format(newElement);
        }
        catch (Exception e)
        {
        }
    }

    public void setElementDeclarationNamespacePrefix(String nsPrefix)
    {
        this.elementDeclarationNamespacePrefix = nsPrefix;
    }

    protected static void format(Node parentNode)
    {
        if (parentNode instanceof IDOMNode)
        {
            // format selected node                                                    
            FormatProcessorXML formatProcessorXML = new FormatProcessorXML();
            formatProcessorXML.formatNode((IDOMNode)parentNode);
        }
    }
}
