/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.actions;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.ui.internal.Messages;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.dialogs.EditNamespacesDialog;
import org.eclipse.wst.wsdl.ui.internal.util.WSDLEditorUtil;
import org.eclipse.wst.xml.core.internal.contentmodel.util.DOMNamespaceInfoManager;
import org.eclipse.wst.xml.core.internal.contentmodel.util.NamespaceInfo;
import org.w3c.dom.Element;


public class EditNamespacesAction extends Action
{
  protected Definition definition;

  public EditNamespacesAction(Definition definition)
  {
    setText(Messages._UI_EDIT_NAMESPACES); //$NON-NLS-1$
    this.definition = definition;
  }

  public void run()
  {
    DOMNamespaceInfoManager namespaceInfoManager = new DOMNamespaceInfoManager();
    Element element = WSDLEditorUtil.getInstance().getElementForObject(definition);
    if (element != null)
    {
      List namespaceInfoList = namespaceInfoManager.getNamespaceInfoList(element);
//      List oldNamespaceInfoList = NamespaceInfo.cloneNamespaceInfoList(namespaceInfoList);

      // here we store a copy of the old info for each NamespaceInfo
      // this info will be used in createPrefixMapping() to figure out how to update the document 
      // in response to these changes
      for (Iterator i = namespaceInfoList.iterator(); i.hasNext();)
      {
        NamespaceInfo info = (NamespaceInfo) i.next();
        NamespaceInfo oldCopy = new NamespaceInfo(info);
        info.setProperty("oldCopy", oldCopy); //$NON-NLS-1$
      }

	  IPath path = new Path(definition.getDocumentBaseURI());
      EditNamespacesDialog dialog = new EditNamespacesDialog(WSDLEditorPlugin.getShell(),  path, Messages._UI_EDIT_NAMESPACES_DIALOG_TITLE, definition.getTargetNamespace(), namespaceInfoList); //$NON-NLS-1$
      int rc = dialog.createAndOpen();
      if (rc == IDialogConstants.OK_ID)
      {      	
		element.setAttribute("targetNamespace", dialog.getTargetNamespace()); //$NON-NLS-1$
		
        List newInfoList = dialog.getNamespaceInfoList();
        namespaceInfoManager.removeNamespaceInfo(element);
        namespaceInfoManager.addNamespaceInfo(element, newInfoList, false);

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
    }
  }

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
}
