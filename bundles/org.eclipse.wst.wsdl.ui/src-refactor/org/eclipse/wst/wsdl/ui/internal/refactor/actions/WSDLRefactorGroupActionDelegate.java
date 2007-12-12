/*******************************************************************************
 * Copyright (c) 2001, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.refactor.actions;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.ide.FileStoreEditorInput;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.ui.internal.ISelectionMapper;
import org.eclipse.wst.xsd.ui.internal.refactor.wizard.RefactorActionGroup;
import org.eclipse.wst.xsd.ui.internal.refactor.wizard.RefactorGroupActionDelegate;
import org.eclipse.wst.xsd.ui.internal.refactor.wizard.RefactorGroupSubMenu;

public class WSDLRefactorGroupActionDelegate extends RefactorGroupActionDelegate
{
  public WSDLRefactorGroupActionDelegate()
  {
    super();
  }
  /**
   * Fills the menu with applicable refactor sub-menues
   * 
   * @param menu
   *          The menu to fill
   */
  protected void fillMenu(Menu menu)
  {
    if (fSelection == null)
    {
      return;
    }
    if (workbenchPart != null)
    {
      IWorkbenchPartSite site = workbenchPart.getSite();
      if (site == null)
        return;
      IEditorPart editor = site.getPage().getActiveEditor();
      IEditorInput editorInput = editor.getEditorInput();
      boolean shouldFillMenu = editor != null && (editorInput instanceof IFileEditorInput || editorInput instanceof FileStoreEditorInput);
      if (shouldFillMenu)
      {
        ISelectionMapper mapper = (ISelectionMapper) editor.getAdapter(ISelectionMapper.class);
        ISelection selection = mapper != null ? mapper.mapSelection(fSelection) : fSelection;
        Definition definition = (Definition) editor.getAdapter(Definition.class);
        if (definition != null)
        {  
          RefactorActionGroup refactorMenuGroup = new WSDLRefactorActionGroup(selection, definition);
          RefactorGroupSubMenu subMenu = new RefactorGroupSubMenu(refactorMenuGroup);
          subMenu.fill(menu, -1);
        }  
      }
    }
  }
}