/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.wsdl.ui.internal.adapters.commands;

import javax.xml.namespace.QName;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Preferences;
import org.eclipse.gef.commands.Command;
import org.eclipse.wst.sse.core.internal.encoding.CommonEncodingPreferenceNames;
import org.eclipse.wst.sse.core.internal.format.IStructuredFormatProcessor;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.ui.internal.Messages;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.adapters.visitor.W11FindInnerElementVisitor;
import org.eclipse.wst.wsdl.util.WSDLConstants;
import org.eclipse.wst.xml.core.internal.XMLCorePlugin;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.core.internal.provisional.format.FormatProcessorXML;
import org.eclipse.xsd.XSDElementDeclaration;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;

public class W11TopLevelElementCommand extends Command
{
  private static final String XML = "xml"; //$NON-NLS-1$
  protected Definition definition;

  public W11TopLevelElementCommand(String label, Definition definition)
  {
    super(label);
    this.definition = definition;
  }
  
  protected void beginRecording(Object element) {
	  if (element instanceof IDOMNode) {
		  ((IDOMNode) element).getModel().beginRecording(this, getUndoDescription());
	  }
  }
  
  protected void endRecording(Object element) {
	  if (element instanceof IDOMNode) {
		  ((IDOMNode) element).getModel().endRecording(this);
	  }
  }
  
  protected String getUndoDescription() {
	  return getLabel();
  }
  
  protected void formatChild(Element child)
  {
    if (child instanceof IDOMNode)
    {
      IDOMModel model = ((IDOMNode)child).getModel();
      try
      {
        // tell the model that we are about to make a big model change
        model.aboutToChangeModel();
        
        IStructuredFormatProcessor formatProcessor = new FormatProcessorXML();
        formatProcessor.formatNode(child);
      }
      finally
      {
        // tell the model that we are done with the big model change
        model.changedModel(); 
      }
    }
  }

  public void execute()
  {
    ensureDefinition(definition);
  }
  
  protected XSDElementDeclaration getNewXSDElement(XSDElementDeclaration xsdElement) { 
	  W11FindInnerElementVisitor visitor = new W11FindInnerElementVisitor();
	  return visitor.getInnerXSDElement(xsdElement);
  }

  public static void ensureDefinition(Definition definition)
  {
    Document document = definition.getDocument();

    Element definitionsElement = document.getDocumentElement();

    if (definitionsElement == null)
    {
      String targetNamespace = getDefaultNamespace(definition);
      definition.setQName(new QName(null, getFileName(definition)));
      definition.setTargetNamespace(targetNamespace);
      definition.addNamespace("wsdl", WSDLConstants.WSDL_NAMESPACE_URI); //$NON-NLS-1$
      definition.updateElement();
      // Moving these above updateElement() seems to cause grief with the model.
      definition.addNamespace("tns", targetNamespace); //$NON-NLS-1$
      definition.addNamespace("xsd", WSDLConstants.XSD_NAMESPACE_URI); //$NON-NLS-1$
      definitionsElement = definition.getElement();
    }

    ensureXMLDirective(document);
  }

  private static void ensureXMLDirective(Document document)
  {
    if (hasXMLDirective(document))
    {
      return;
    }
    
    Node firstChild = document.getFirstChild();
    ProcessingInstruction xmlDeclaration = getXMLDeclaration(document);
    document.insertBefore(xmlDeclaration, firstChild);
  }
  
  private static boolean hasXMLDirective(Document document)
  {
    Node firstChild = document.getFirstChild();
   
    if (firstChild == null)
    {
      return false;
    }
    
    if (firstChild.getNodeType() != Node.PROCESSING_INSTRUCTION_NODE)
    {
      return false;
    }
    
    ProcessingInstruction processingInstruction  = (ProcessingInstruction)firstChild;
    
    if (!processingInstruction.getTarget().equals(XML)) 
    {
      return false;
    }
    
    return true;
  }

  private static ProcessingInstruction getXMLDeclaration(Document document)
  {
    Preferences preference = XMLCorePlugin.getDefault().getPluginPreferences();
    String charSet = preference.getString(CommonEncodingPreferenceNames.OUTPUT_CODESET);
    if (charSet == null || charSet.trim().equals("")) //$NON-NLS-1$
    {
      charSet = "UTF-8"; //$NON-NLS-1$
    }
    ProcessingInstruction xmlDeclaration = document.createProcessingInstruction(XML, "version=\"1.0\" encoding=\"" + charSet + "\""); //$NON-NLS-1$ //$NON-NLS-2$
    return xmlDeclaration;
    
  }
  private static String getDefaultNamespace(Definition definition)
  {
    String namespace = WSDLEditorPlugin.getInstance().getPreferenceStore().getString(Messages._UI_PREF_PAGE_DEFAULT_TARGET_NAMESPACE);

    if (!namespace.endsWith("/")) //$NON-NLS-1$
    {
      namespace = namespace.concat("/"); //$NON-NLS-1$
    }

    namespace += getFileName(definition) + "/"; //$NON-NLS-1$

    return namespace;

  }

  private static String getFileName(Definition definition)
  {
    String fileLocation = definition.getLocation();
    IPath filePath = new Path(fileLocation);
    return filePath.removeFileExtension().lastSegment().toString();
  }
}
