/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal;

import java.lang.reflect.Method;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.FileEditorInput;

/**
 * WSDL Hyperlink that knows how to open links from wsdl files
 */
public class WSDLHyperlink implements IHyperlink {
	private IRegion fRegion;
	private String fResource;
	private String fSpec;

	public WSDLHyperlink(IRegion region, String resource, String spec) {
		fRegion = region;
		fResource = resource;
		fSpec = spec;
	}

	public IRegion getHyperlinkRegion() {
		return fRegion;
	}

	public String getTypeLabel() {
		return null;
	}

	public String getHyperlinkText() {
		return null;
	}

	public void open() {
		/*
		 * ISSUE: There are cleaner ways to find the right file based on a URI
		 * string and cleaner ways to find which editor to open for the file.
		 * See other IHyperlink and IHyperlinkDetector implementors for
		 * examples.
		 */
		String pattern = "platform:/resource";
		if (fResource != null && fResource.startsWith(pattern)) {
			try {
				Path path = new Path(fResource.substring(pattern.length()));
				IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);

				IWorkbenchPage workbenchPage = WSDLEditorPlugin.getInstance().getWorkbench().getActiveWorkbenchWindow().getActivePage();
				IEditorPart editorPart = workbenchPage.getActiveEditor();

				if (editorPart.getEditorInput() instanceof IFileEditorInput && ((IFileEditorInput) editorPart.getEditorInput()).getFile().equals(file)) {
					workbenchPage.getNavigationHistory().markLocation(editorPart);
				}
				else {
					try {
						if (fResource.endsWith("xsd")) {
							editorPart = workbenchPage.openEditor(new FileEditorInput(file), WSDLEditorPlugin.XSD_EDITOR_ID);
						}
						else {
							// Since we are already in the wsdleditor
							editorPart = workbenchPage.openEditor(new FileEditorInput(file), editorPart.getEditorSite().getId());
						}
					}
					catch (PartInitException initEx) {
					}
				}

				/*
				 * ISSUE: This just does not look like a safe thing to do. One
				 * simple solution would be to have an interface for
				 * openOnSelection that your editors can implement. Or, java
				 * editor has something like a utility that is able to find
				 * the offset/location for a given element in a file. Once you
				 * have offset/location, you can just call setSelection.
				 */
				Class theClass = editorPart.getClass();
				Class[] methodArgs = {String.class};
				Method method = theClass.getMethod("openOnSelection", methodArgs);
				Object args[] = {fSpec};
				method.invoke(editorPart, args);
				workbenchPage.getNavigationHistory().markLocation(editorPart);
			}
			catch (Exception e) {
			}
		}
	}
}
