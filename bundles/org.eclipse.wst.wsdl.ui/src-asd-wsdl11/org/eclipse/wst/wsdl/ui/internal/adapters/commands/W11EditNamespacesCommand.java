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

import java.util.List;

import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.ui.internal.Messages;
import org.eclipse.wst.xml.core.internal.contentmodel.util.DOMNamespaceInfoManager;
import org.w3c.dom.Element;

public class W11EditNamespacesCommand extends W11TopLevelElementCommand {
	private List namespacesInfoList;
	private String targetNamespace;
	private String targetNamespacePrefix;
	
	public W11EditNamespacesCommand(Definition definition) {
	  super(Messages._UI_EDIT_NAMESPACES, definition);
	}
	
	public void setNamespacesInfo(List namespaces) {
		namespacesInfoList = namespaces;
	}
	
	public void setTargetNamespace(String tns) {
		targetNamespace = tns;
	}
	
	public void setTargetNamespacePrefix(String prefix) {
		targetNamespacePrefix = prefix;
	}
	
	public void execute() {
		try {
			beginRecording(definition.getElement());
			super.execute();
			if (targetNamespacePrefix != null) {
				Element element = definition.getElement();

				// Remove the old prefix
				String oldPrefix = definition.getPrefix(definition.getTargetNamespace());
				element.removeAttribute("xmlns:"+oldPrefix);  //$NON-NLS-1$

				// Set the new prefix
				element.setAttribute("xmlns:" + targetNamespacePrefix, definition.getTargetNamespace()); //$NON-NLS-1$
			}
			if (targetNamespace != null) {
				String newValue = targetNamespace;
				String prefix = definition.getPrefix(definition.getTargetNamespace());
				definition.setTargetNamespace(newValue);
				definition.getElement().setAttribute("xmlns:" + prefix, newValue); //$NON-NLS-1$
			}

			if (namespacesInfoList != null) {
				DOMNamespaceInfoManager namespaceInfoManager = new DOMNamespaceInfoManager();

				/*
		      List namespaceInfoList = namespaceInfoManager.getNamespaceInfoList(element);
//		      List oldNamespaceInfoList = NamespaceInfo.cloneNamespaceInfoList(namespaceInfoList);

		      // here we store a copy of the old info for each NamespaceInfo
		      // this info will be used in createPrefixMapping() to figure out how to update the document 
		      // in response to these changes
		      for (Iterator i = namespaceInfoList.iterator(); i.hasNext();)
		      {
		        NamespaceInfo info = (NamespaceInfo) i.next();
		        NamespaceInfo oldCopy = new NamespaceInfo(info);
		        info.setProperty("oldCopy", oldCopy); //$NON-NLS-1$
		      }
				 */

				namespaceInfoManager.removeNamespaceInfo(definition.getElement());
				namespaceInfoManager.addNamespaceInfo(definition.getElement(), namespacesInfoList, false);

				/*
	        // see if we need to rename any prefixes
	        Map prefixMapping = createPrefixMapping(oldNamespaceInfoList, namespaceInfoList);
	        if (prefixMapping.size() > 0)
	        {
	          //manager.getModel().aboutToChangeModel();
	          ReplacePrefixAction replacePrefixAction = new ReplacePrefixAction(element, prefixMapping);
	          replacePrefixAction.run();
	          //manager.getModel().changedModel();
	        }
				 */   
			}

			/*
		 // Could not modify (more specifically remove) namespaces via model.....
		 // should revisit this
		if (targetNamespace != null || targetNamespacePrefix != null) {
			Map map = definition.getNamespaces();
			String oldTargetNamespace = definition.getTargetNamespace();
			String oldPrefix = null;

			Iterator it = map.keySet().iterator();
			while (it.hasNext()) {
				String key = (String) it.next();
				Object value = map.get(key);
				if (value != null && oldTargetNamespace != null && value.equals(oldTargetNamespace)) {
					oldPrefix = key;
					break;
				}
			}

			if (oldPrefix != null && !oldPrefix.equals(targetNamespacePrefix)) {
				map.remove(oldPrefix);
				definition.updateElement();
			}

			String tns = "";
			String prefix = "";
			if (targetNamespace != null) {
				tns = targetNamespace;
			}
			if (targetNamespacePrefix != null) {
				prefix = targetNamespacePrefix;
			}
			definition.addNamespace(prefix, tns);
			definition.setTargetNamespace(tns);
//			map.put(prefix, tns);
		}
			 */
		}
		finally {
			endRecording(definition.getElement());
		}
	}
	
	/*
	protected Map createPrefixMapping(List oldList, List newList)
	{
		Map map = new Hashtable();

		Hashtable oldURIToPrefixTable = new Hashtable();
		for (Iterator i = oldList.iterator(); i.hasNext();)
		{
			NamespaceInfo oldInfo = (NamespaceInfo) i.next();
			oldURIToPrefixTable.put(oldInfo.uri, oldInfo);
		}

		for (Iterator i = newList.iterator(); i.hasNext();)
		{
			NamespaceInfo newInfo = (NamespaceInfo) i.next();
			NamespaceInfo oldInfo = (NamespaceInfo) oldURIToPrefixTable.get(newInfo.uri != null ? newInfo.uri : ""); //$NON-NLS-1$

			// if oldInfo is non null ... there's a matching URI in the old set
			// we can use its prefix to detemine out mapping
			//
			// if oldInfo is null ...  we use the 'oldCopy' we stashed away 
			// assuming that the user changed the URI and the prefix
			if (oldInfo == null)
			{
				oldInfo = (NamespaceInfo) newInfo.getProperty("oldCopy"); //$NON-NLS-1$
			}

			if (oldInfo != null)
			{
				String newPrefix = newInfo.prefix != null ? newInfo.prefix : ""; //$NON-NLS-1$
				String oldPrefix = oldInfo.prefix != null ? oldInfo.prefix : ""; //$NON-NLS-1$
				if (!oldPrefix.equals(newPrefix))
				{
					map.put(oldPrefix, newPrefix);
				}
			}
		}
		return map;
	}
	*/
}
