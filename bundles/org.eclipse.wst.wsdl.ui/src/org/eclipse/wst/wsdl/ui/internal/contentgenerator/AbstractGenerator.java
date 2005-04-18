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
package org.eclipse.wst.wsdl.ui.internal.contentgenerator;

import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Fault;
import org.eclipse.wst.wsdl.Input;
import org.eclipse.wst.wsdl.Operation;
import org.eclipse.wst.wsdl.Output;
import org.eclipse.wst.wsdl.Port;
import org.eclipse.wst.wsdl.PortType;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.actions.AddNamespaceDeclarationsAction;
import org.eclipse.wst.wsdl.ui.internal.util.WSDLEditorUtil;
import org.eclipse.wst.wsdl.util.WSDLConstants;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.core.internal.provisional.format.FormatProcessorXML;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


public abstract class AbstractGenerator
{
  //public static final String UNSPECIFIED = "(unspecified)"; //WSDLEditorPlugin.getInstance().getWSDLString("_UI_LABEL_NONE");
  public static final int BINDING_GENERATOR = 1;
  public static final int PORT_GENERATOR = 2;  

  protected ContentGenerator bindingContentGenerator;
  protected String name;
  protected String protocol;
  protected boolean overwrite;
  protected Object[] options;
  protected WSDLElement newComponent;

  public abstract int getType();

  public abstract Definition getDefinition();

  public abstract Node getParentNode();

  public abstract void generateContent();

  protected abstract String getUndoDescription();
  
  public WSDLElement getNewComponent()
  {
  	return newComponent;
  }
  
  public void setName(String name)
  {
  	this.name = name;
  }
  
  public String getName()
  {
  	return name;
  }
  
  public abstract void setRefName(String refName);
  public abstract String getRefName();


  public void generate()
  {
    try
    {
      beginRecording();

      ContentGeneratorExtension extension = WSDLEditorPlugin.getInstance().getContentGeneratorExtensionRegistry().getContentGeneratorExtension(protocol);
      if (extension != null)
      {
        bindingContentGenerator = extension.createBindingContentGenerator();
      }

      if (bindingContentGenerator == null)
      {
        bindingContentGenerator = new EmptyBindingContentGenerator();
      }

      bindingContentGenerator.init(getDefinition(), this, options);

      Element definitionElement = WSDLEditorUtil.getInstance().getElementForObject(getDefinition());
      if (definitionElement != null)
      {
        AddNamespaceDeclarationsAction action =
          new AddNamespaceDeclarationsAction(definitionElement, bindingContentGenerator.getRequiredNamespaces(), bindingContentGenerator.getPreferredNamespacePrefixes());
        action.run();
      }

      generateContent();
      format();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      endRecording();
    }
  }

  public void setProtocol(String protocol)
  {
    this.protocol = protocol;
  }

  public void setOptions(Object[] options)
  {
    this.options = options;
  }

  public String getProtocol()
  {
    return protocol;
  }

  public void setOverwrite(boolean overwrite)
  {
    this.overwrite = overwrite;
  }

  public boolean getOverwrite()
  {
    return overwrite;
  }

  class EmptyBindingContentGenerator implements ContentGenerator
  {
    public void init(Definition definition, Object generator, Object[] options)
    {
    }

    public String[] getRequiredNamespaces()
    {
      return new String[0];
    }

    public String[] getPreferredNamespacePrefixes()
    {
      return new String[0];
    }

    public void generatePortContent(Element portElement, Port port)
    {
    }

    public void generateBindingContent(Element bindingElement, PortType portType)
    {
    }

    public void generateBindingOperationContent(Element bindingOperationElement, Operation operation)
    {
    }

    public void generateBindingInputContent(Element bindingInputElement, Input input)
    {
    }

    public void generateBindingOutputContent(Element bindingOutputElement, Output output)
    {
    }

    public void generateBindingFaultContent(Element bindingFaultElement, Fault fault)
    {
    }
  }

  public void beginRecording()
  {
    Node node = getParentNode();
    if (node instanceof IDOMNode)
    {
      ((IDOMNode) node).getModel().beginRecording(this, getUndoDescription());
    }
  }

  public void endRecording()
  {
    Node node = getParentNode();
    if (node instanceof IDOMNode)
    {
      ((IDOMNode) node).getModel().endRecording(this);
    }
  }

  protected void format()
  {
    Node node = getParentNode();
    if (node instanceof IDOMNode)
    {
      // tell the model that we are about to make a big model change
      //model.aboutToChangeModel();

      // format selected node                                                    
      FormatProcessorXML formatProcessorXML = new FormatProcessorXML();
      formatProcessorXML.formatNode((IDOMNode)node);
      
      // tell the model that we are done with the big model change
      //model.changedModel();
    }
  }

  protected Element createWSDLElement(Element parentElement, String elementName)
  {
    String prefix = parentElement.getPrefix();
    String name = prefix != null ? (prefix + ":" + elementName) : elementName;
    Element result = parentElement.getOwnerDocument().createElementNS(WSDLConstants.WSDL_NAMESPACE_URI ,name);
    parentElement.appendChild(result);
    return result;
  }

  protected Element createElement(Element parentElement, String prefix, String elementName)
  {
    String name = prefix != null ? (prefix + ":" + elementName) : elementName;
    // TODO... consider createElementNS for these elements too
    //
    Element result = parentElement.getOwnerDocument().createElement(name);
    parentElement.appendChild(result);
    return result;
  }
}
