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

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditor;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.extension.IMenuActionContributor;
import org.eclipse.wst.wsdl.ui.internal.extension.WSDLEditorExtension;
import org.eclipse.wst.wsdl.ui.internal.extension.WSDLEditorExtensionRegistry;
import org.w3c.dom.Node;


public class ExtensibleMenuActionContributor implements IMenuActionContributor
{                                                                   
  protected WSDLEditor wsdlEditor;
  protected IMenuActionContributor[] menuActionContributors;  

  public ExtensibleMenuActionContributor(WSDLEditor wsdlEditor)
  {
    this.wsdlEditor = wsdlEditor; 

    WSDLEditorExtensionRegistry registry = WSDLEditorPlugin.getInstance().getWSDLEditorExtensionRegistry(); 

    WSDLEditorExtension[] extensions = registry.getRegisteredExtensions(WSDLEditorExtension.MENU_ACTION_CONTRIBUTOR);
    menuActionContributors = new IMenuActionContributor[extensions.length];

    for (int i = 0; i < menuActionContributors.length; i++)
    {
      menuActionContributors[i] = (IMenuActionContributor)extensions[i].createExtensionObject(WSDLEditorExtension.MENU_ACTION_CONTRIBUTOR, wsdlEditor);
    }
  }
                           
  public void contributeMenuActions(IMenuManager manager, Node node, Object object)
  {  
    for (int i = 0; i < menuActionContributors.length; i++)
    {                          
      if (menuActionContributors[i] != null)
      {
        menuActionContributors[i].contributeMenuActions(manager, node, object);
      }
    }
  }
}