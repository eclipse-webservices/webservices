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

import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditor;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.extension.WSDLEditorExtension;
import org.eclipse.wst.wsdl.ui.internal.extension.WSDLEditorExtensionRegistry;
import org.eclipse.xsd.XSDConcreteComponent;
import org.eclipse.xsd.XSDParticle;
import org.eclipse.xsd.XSDSchema;
import org.w3c.dom.Node;


/**
 * This class handles delete with the WSDL Editor's graph view.  the object for
 * deletion could be a WSDL 'object' or an XSD 'object'.  We need to make this
 * distinction because we have two different Delete classes.  One to handle
 * WSDL and another to handle XSD.
 */
public class DeleteWSDLAndXSDAction extends BaseNodeAction {
	List list;
	Node recordingNode;
	WSDLEditor wsdlEditor;

	public DeleteWSDLAndXSDAction(List deleteList, Node recordingNode, WSDLEditor wsdlEditor) {
		setText(WSDLEditorPlugin.getWSDLString("_UI_ACTION_DELETE"));
		list = deleteList;
		this.recordingNode = recordingNode;
		this.wsdlEditor = wsdlEditor;
	}
	
	public void run() {
		Iterator it = list.iterator();
		List wsdlDeleteList = new ArrayList();
		List xsdDeleteList = new ArrayList();

		while (it.hasNext()) {
			Object object = it.next();
			
			if (object instanceof WSDLElement || object instanceof XSDSchema) {
				wsdlDeleteList.add(object);
			}
			else {
				xsdDeleteList.add(object);
			}
		}
		
		Object newSelection = null;
		Object topLevelObject = null;
		if (wsdlDeleteList.size() > 0 || xsdDeleteList.size() > 0)
		{	
			try
			{
			beginRecording();	
				
		
		// Delete WSDL objects
		if (wsdlDeleteList.size() > 0) {
			Object wsdlObject = wsdlDeleteList.get(0);
			if (wsdlObject instanceof WSDLElement) {
				newSelection = ((WSDLElement) wsdlObject).eContainer();
				topLevelObject = getTopLevelWSDLObject((WSDLElement) wsdlObject);
			}
			
			DeleteAction wsdlDelete = new DeleteAction(wsdlDeleteList);
			wsdlDelete.run();
		}
		
		// Delete XSD objects
		if (xsdDeleteList.size() > 0) {
			Object xsdObject = xsdDeleteList.get(0);
			if (xsdObject instanceof XSDConcreteComponent) {
				newSelection = ((XSDConcreteComponent) xsdObject).getContainer();
				topLevelObject = getTopLevelXSDObject((XSDConcreteComponent) xsdObject);
			}
			
			DeleteInterfaceAction xsdDelete = (DeleteInterfaceAction) getXSDDelete();
			xsdDelete.setDeleteList(xsdDeleteList);
			xsdDelete.run();
			// Hack to force a refresh of all components of the Graph View.  This is needed
			// for the case where we delete a XSD Element (View input == Definition).  The XSD
			// Element EditPart visual would not be removed from the view.
			wsdlEditor.getDefinition().setQName(wsdlEditor.getDefinition().getQName());
		}
			}
			finally
			{
			  endRecording();
			}
			// Make our selection after our Deletions
			ISelectionProvider selectionProvider = (ISelectionProvider)wsdlEditor.getSelectionManager();
		    if (selectionProvider != null)
		    {
		    	if (newSelection instanceof XSDParticle)
	            {
	              newSelection = ((XSDParticle) newSelection).getContainer();
	            }
	    		
		    	if (newSelection == null) {
		    		newSelection = getNewSelection();
		    	}
		    	if (newSelection != null) {
		    		// We have a newSelection, but it may also be deleted by the user.  In this case, select the top level object
		    		if (list.contains(newSelection) && topLevelObject != null) {
		    			newSelection = topLevelObject;
		    		}

		    		selectionProvider.setSelection(new StructuredSelection(newSelection));
		    	}
		    }
		}
	}
	
	public Node getNode()
	{
	  return recordingNode;
	}

	public String getUndoDescription()
	{
	  return WSDLEditorPlugin.getWSDLString("_UI_ACTION_DELETE"); //$NON-NLS-1$
	}

	private Object getXSDDelete() {
		WSDLEditorExtensionRegistry registry = WSDLEditorPlugin.getInstance().getWSDLEditorExtensionRegistry(); 
	    WSDLEditorExtension[] extensions = registry.getRegisteredExtensions(WSDLEditorExtension.MENU_ACTION_CONTRIBUTOR);
	    Object xsdDelete = null;
	    
	    for (int i = 0; i < extensions.length; i++)
	    {
//	    	xsdDelete = extensions[i].createExtensionObject(WSDLEditorExtension.XSD_DELETE_ACTION, wsdlEditor);
	    	xsdDelete = extensions[i].createExtensionObject(WSDLEditorExtension.XSD_DELETE_ACTION, null);
	    	if (xsdDelete != null) {
	    		break;
	    	}
	    }	    
	    
	    return xsdDelete;
	}
	
	private Object getNewSelection() {
		// Hack using wsdleditor....  This situation occurs when we delete the Schema from the Types group....  We
		// usually get the container of the object being deleted... however, in this situation, the container would
		// be null because the object being deleted is a Schema......
		Object selection;
		try {
			selection = wsdlEditor.getGraphViewer().getComponentViewer().getInput();
			if (selection == null) {
				selection = wsdlEditor.getDefinition();
			}
		}
		catch (Exception e) {
			selection = wsdlEditor.getDefinition();
		}
		
		return selection;
	}

	private WSDLElement getTopLevelWSDLObject(WSDLElement element) {
		WSDLElement topObject = (WSDLElement) element.eContainer();
		
		while (topObject != null && !(topObject instanceof Definition)) {
			topObject = (WSDLElement) topObject.eContainer();
		}

		return topObject;
	}
	
	private XSDConcreteComponent getTopLevelXSDObject(XSDConcreteComponent component) {
		XSDConcreteComponent topObject = component.getContainer();
		
		while (topObject != null && !(topObject instanceof XSDSchema)) {
			topObject = (XSDConcreteComponent) topObject.eContainer();
		}

		return topObject;
	}
}