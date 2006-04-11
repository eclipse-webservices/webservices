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
package org.eclipse.wst.wsdl.ui.internal.edit;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.wst.common.ui.internal.search.dialogs.ComponentSpecification;
import org.eclipse.wst.common.ui.internal.search.dialogs.IComponentDescriptionProvider;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.MessageReference;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11Description;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11Message;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11MessageReference;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IMessage;
import org.eclipse.wst.wsdl.ui.internal.dialogs.W11BrowseComponentDialog;
import org.eclipse.wst.wsdl.ui.internal.dialogs.W11NewComponentDialog;
import org.eclipse.wst.wsdl.ui.internal.search.IWSDLSearchConstants;
import org.eclipse.wst.wsdl.ui.internal.util.WSDLSetComponentHelper;
import org.eclipse.wst.xsd.ui.internal.adt.edit.ComponentReferenceEditManager;
import org.eclipse.wst.xsd.ui.internal.adt.edit.IComponentDialog;

public class W11MessageReferenceEditManager implements ComponentReferenceEditManager {
	protected W11Description description;
	protected IFile iFile;
	
	public W11MessageReferenceEditManager(W11Description description, IFile iFile) {
		this.description = description;
		this.iFile = iFile;
	}

	public IComponentDialog getBrowseDialog() {
		return new W11BrowseComponentDialog(IWSDLSearchConstants.MESSAGE_META_NAME, iFile, description);
	}

	public IComponentDialog getNewDialog() {
		return new W11NewComponentDialog(IWSDLSearchConstants.MESSAGE_META_NAME, iFile, description);
	}

	private Definition getDefinition() {
		return (Definition) description.getTarget();
	}
	
	public void modifyComponentReference(Object referencingObject, ComponentSpecification referencedComponent) {
		W11MessageReference w11MessageRef = (W11MessageReference) referencingObject;
		Object messageObject = referencedComponent.getObject();
		if (messageObject == null) {
			// Need to figure out the IMessage based on the information contained in the ComponentSpecification

		}
		
		if (messageObject instanceof ComponentSpecification) {
			MessageReference messageRef= (MessageReference) w11MessageRef.getTarget();
			WSDLSetComponentHelper helper = new WSDLSetComponentHelper(iFile, getDefinition());
			helper.setWSDLComponent(messageRef, "message", (ComponentSpecification) messageObject);
		}		
		else if (messageObject instanceof W11Message){
			// Below is a little complex.... The alternative was to have IMessage contain a
			// getSetMessageCommand() which would handle all of this but does a getSetMessageCommand()
			// make sense at the generic level.....
			W11Message iMessage = (W11Message) messageObject;
			ComponentSpecification specObject = new ComponentSpecification();
			specObject.setName(iMessage.getName());
			specObject.setMetaName(IWSDLSearchConstants.MESSAGE_META_NAME);
			specObject.setQualifier(description.getTargetNamespace());
			
			String location = ((Definition) description.getTarget()).getLocation();
			String platformResource = "platform:/resource";
	        if (location != null && location.startsWith(platformResource))
	        {
	          Path path = new Path(location.substring(platformResource.length()));
	          specObject.setFile(ResourcesPlugin.getWorkspace().getRoot().getFile(path));
	        }  
			
			MessageReference messageRef= (MessageReference) w11MessageRef.getTarget();
			WSDLSetComponentHelper helper = new WSDLSetComponentHelper(iFile, getDefinition());
			helper.setWSDLComponent(messageRef, "message", (ComponentSpecification) specObject);
		}
	}

	public IComponentDescriptionProvider getComponentDescriptionProvider() {

		return null;
	}

	public ComponentSpecification[] getQuickPicks() {
		List specList = new ArrayList();
		
		Iterator messages = description.getMessages().iterator();
		while (messages.hasNext()) {
			IMessage message = (IMessage) messages.next();
			String qualifier = "";
			String name = message.getName();
			IFile file = null;
			
			ComponentSpecification spec = new ComponentSpecification(qualifier, name, file);
			spec.setObject(message);
			specList.add(spec);
		}

		ComponentSpecification[] specArray = new ComponentSpecification[specList.size()];
		specList.toArray(specArray);
		
		return specArray;
	}

	public ComponentSpecification[] getHistory() {

		return null;
	}

	public void addToHistory(ComponentSpecification component) {


	}
}