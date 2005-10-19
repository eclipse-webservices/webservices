/*
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *   IBM - Initial API and implementation
 *   Jens Lukowski/Innoopract - initial renaming/restructuring
 * 
 */
package org.eclipse.wst.wsdl.ui.internal;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.jface.text.hyperlink.IHyperlinkDetector;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;
import org.eclipse.wst.sse.core.internal.provisional.StructuredModelManager;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.ui.internal.text.WSDLModelAdapter;
import org.eclipse.wst.wsdl.ui.internal.util.OpenOnSelectionHelper;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMDocument;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.w3c.dom.Node;

/**
 * Detects hyperlinks for WSDL files
 */
public class WSDLHyperlinkDetector implements IHyperlinkDetector {
	/**
	 * Gets the definition from document
	 * 
	 * @param document
	 * @return Definition
	 */
	private Definition getDefinition(IDocument document) {
		Definition definition = null;
		IStructuredModel model = StructuredModelManager.getModelManager().getExistingModelForRead(document);
		if (model != null) {
			try {
				if (model instanceof IDOMModel) {
					IDOMDocument domDoc = ((IDOMModel) model).getDocument();
					if (domDoc != null) {
						WSDLModelAdapter modelAdapter = (WSDLModelAdapter) domDoc.getAdapterFor(WSDLModelAdapter.class);

						/*
						 * ISSUE: if adapter does not already exist for domDoc
						 * getAdapterFor will create one. So why is this null
						 * check/creation needed?
						 */
						if (modelAdapter == null) {
							modelAdapter = new WSDLModelAdapter();
							domDoc.addAdapter(modelAdapter);
							modelAdapter.createDefinition(domDoc.getDocumentElement());
						}

						definition = modelAdapter.getDefinition();
					}
				}
			}
			finally {
				model.releaseFromRead();
			}
		}
		return definition;
	}

	public IHyperlink[] detectHyperlinks(ITextViewer textViewer, IRegion region, boolean canShowMultipleHyperlinks) {
		// for now, only capable of creating 1 hyperlink
		List hyperlinks = new ArrayList(0);

		if (region != null && textViewer != null) {
			IDocument document = textViewer.getDocument();
			Node node = getCurrentNode(document, region.getOffset());

			if (node != null) {
				Definition definition = getDefinition(textViewer.getDocument());
				OpenOnSelectionHelper helper = new OpenOnSelectionHelper(definition);
				String[] array = helper.computeSpecification(node);
				if (array != null) {
					IRegion nodeRegion = region;
					if (node instanceof IndexedRegion) {
						IndexedRegion indexed = (IndexedRegion) node;
						nodeRegion = new Region(indexed.getStartOffset(), indexed.getLength());
					}
					hyperlinks.add(new WSDLHyperlink(nodeRegion, array[0], array[1]));
				}
			}
		}
		if (hyperlinks.size() == 0)
			return null;
		return (IHyperlink[]) hyperlinks.toArray(new IHyperlink[0]);
	}

	/**
	 * Returns the node the cursor is currently on in the document. null if no
	 * node is selected
	 * 
	 * @param offset
	 * @return Node either element, doctype, text, or null
	 */
	private Node getCurrentNode(IDocument document, int offset) {
		// get the current node at the offset (returns either: element,
		// doctype, text)
		IndexedRegion inode = null;
		IStructuredModel sModel = null;
		try {
			sModel = StructuredModelManager.getModelManager().getExistingModelForRead(document);
			inode = sModel.getIndexedRegion(offset);
			if (inode == null)
				inode = sModel.getIndexedRegion(offset - 1);
		}
		finally {
			if (sModel != null)
				sModel.releaseFromRead();
		}

		if (inode instanceof Node) {
			return (Node) inode;
		}
		return null;
	}
}
