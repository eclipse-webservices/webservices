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
package org.eclipse.wst.wsdl.ui.internal.search.actions;

import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.wst.xsd.ui.internal.search.actions.BaseGroupActionDelegate;
import org.eclipse.wst.xsd.ui.internal.search.actions.XSDSearchGroupSubMenu;

public class WSDLSearchReferencesGroupActionDelegate extends BaseGroupActionDelegate
{
    protected void fillMenu(Menu menu) {
      try
      {
        if (fSelection == null) {
            return;
        }
        if (workbenchPart != null)
        {
		  IWorkbenchPartSite site = workbenchPart.getSite();
			if (site == null)
			  return;
	
		  IEditorPart editor = site.getPage().getActiveEditor();
          if ( editor != null ){
            WSDLReferencesSearchGroup referencesGroup = new WSDLReferencesSearchGroup(editor);
            XSDSearchGroupSubMenu subMenu = new XSDSearchGroupSubMenu(referencesGroup);
            subMenu.fill(menu, -1);
          }
        }  
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }  
}
