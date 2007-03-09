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
package org.eclipse.wst.wsdl.ui.internal.text;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.ui.internal.util.OpenOnSelectionHelper;
import org.eclipse.wst.wsdl.util.WSDLConstants;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMDocument;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xsd.ui.internal.editor.XSDHyperlinkDetector;
/**
 * Detects hyperlinks for WSDL files. Used by the WSDL text editor to provide a
 * "Go to declaration" functionality similar with the one provided by the Java
 * editor.
 */
public class WSDLHyperlinkDetector extends XSDHyperlinkDetector
{
  /*
   * (non-Javadoc)
   */
  protected IHyperlink createHyperlink(IDocument document, IDOMNode node, IRegion region)
  {
    // Here we're trying to find the target component's resource and spec.

    Definition definition = getDefinition(document);
    OpenOnSelectionHelper helper = new OpenOnSelectionHelper(definition);
    String[] targetData = helper.computeSpecification(node);

    if (targetData != null)
    {
      String resource = targetData[0];
      String spec = targetData[1];
      return new WSDLHyperlink(region, resource, spec);
    }

    return null;
  }

  /**
   * Gets the definition from document
   * 
   * @param document
   * @return Definition
   */
  private Definition getDefinition(IDocument document)
  {
    Definition definition = null;
    IStructuredModel model = StructuredModelManager.getModelManager().getExistingModelForRead(document);
    if (model != null)
    {
      try
      {
        if (model instanceof IDOMModel)
        {
          IDOMDocument domDoc = ((IDOMModel) model).getDocument();
          if (domDoc != null)
          {
            WSDLModelAdapter modelAdapter = (WSDLModelAdapter) domDoc.getAdapterFor(WSDLModelAdapter.class);
            // ISSUE: if adapter does not already exist for domDoc getAdapterFor
            // will create one. So why is this null check/creation needed?
            if (modelAdapter == null)
            {
              modelAdapter = new WSDLModelAdapter();
              domDoc.addAdapter(modelAdapter);
              modelAdapter.createDefinition(domDoc);
            }
            definition = modelAdapter.getDefinition();
          }
        }
      }
      finally
      {
        model.releaseFromRead();
      }
    }
    return definition;
  }

  /*
   * (non-Javadoc)
   */
  protected boolean isLinkableAttribute(String name)
  {
    boolean isLinkable = super.isLinkableAttribute(name)||
    name.equals(WSDLConstants.BINDING_ATTRIBUTE) || 
    name.equals(WSDLConstants.ELEMENT_ATTRIBUTE) || 
    name.equals(WSDLConstants.TYPE_ATTRIBUTE) || 
    name.equals(WSDLConstants.MESSAGE_ATTRIBUTE); 
    return isLinkable;
  }
}
