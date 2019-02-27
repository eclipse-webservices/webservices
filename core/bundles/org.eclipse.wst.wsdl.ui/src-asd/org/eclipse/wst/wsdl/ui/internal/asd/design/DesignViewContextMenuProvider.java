/*******************************************************************************
 * Copyright (c) 2001, 2010 IBM Corporation and others.
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
package org.eclipse.wst.wsdl.ui.internal.asd.design;

import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.wsdl.ui.internal.asd.ASDEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.asd.Messages;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.BaseSelectionAction;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.ShowPropertiesViewAction;
import org.eclipse.wst.wsdl.ui.internal.asd.design.editparts.ColumnEditPart;
import org.eclipse.wst.wsdl.ui.internal.asd.design.editparts.model.IActionProvider;
import org.eclipse.wst.wsdl.ui.internal.refactor.IWSDLRefactorConstants;
import org.eclipse.wst.xsd.ui.internal.adt.editor.ContextMenuParticipant;
import org.eclipse.wst.xsd.ui.internal.adt.editor.EditorModeManager;


public class DesignViewContextMenuProvider extends ContextMenuProvider
{
  ISelectionProvider selectionProvider;

  /**
   * Constructor for GraphContextMenuProvider.
   * 
   * @param selectionProvider
   * @param editor
   */
  public DesignViewContextMenuProvider(EditPartViewer viewer, ISelectionProvider selectionProvider)
  {
    super(viewer);
    this.selectionProvider = selectionProvider;
  }
  
  protected Object getAppropriateSelection(Object selection) {
	  if (selection instanceof ColumnEditPart) {
		  return ((EditPart) selection).getParent();
	  }
	  
	  return selection;
  }

  /**
   * @see org.eclipse.gef.ui.parts.ContextMenuProvider#buildContextMenu(org.eclipse.jface.action.IMenuManager,
   *      org.eclipse.gef.EditPartViewer)
   */
  public void buildContextMenu(IMenuManager menu)
  {
	IMenuManager currentMenu = menu;
    
    IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
	EditorModeManager manager = (EditorModeManager)editor.getAdapter(EditorModeManager.class);
	ContextMenuParticipant contextMenuParticipant = manager != null ? manager.getCurrentMode().getContextMenuParticipant() : null;

        
    currentMenu.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
    ActionRegistry registry = getEditorActionRegistry();
    ISelection selection = selectionProvider.getSelection();
    
    Object activePart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActivePart();
    
    if (selection != null)
    {
      Object selectedObject = ((StructuredSelection) selection).getFirstElement();
      selectedObject = getAppropriateSelection(selectedObject);

      if (contextMenuParticipant != null)
      {
        // Convert editparts to model objects as selections
        Object o = selectedObject;
        if (o instanceof EditPart)
        {
          o = ((EditPart)selectedObject).getModel();
        }                   
        contextMenuParticipant.contributeActions(o, menu);
      }  
      
      if (selectedObject instanceof IActionProvider)
      {
        IActionProvider actionProvider = (IActionProvider) selectedObject;
        String[] actions = actionProvider.getActions(activePart);
        for (int i = 0; i < actions.length; i++)
        {
          String id = actions[i];          
          if (contextMenuParticipant == null || contextMenuParticipant.isApplicable(selectedObject, id))
          {          
            if (id.startsWith(BaseSelectionAction.SUBMENU_START_ID)) {
              String text = id.substring(BaseSelectionAction.SUBMENU_START_ID.length());
              IMenuManager subMenu = new MenuManager(text);
              currentMenu.add(subMenu);
              currentMenu = subMenu;
            }
            else if (id.startsWith(BaseSelectionAction.SUBMENU_END_ID)) {
              currentMenu = getParentMenu(menu, currentMenu);
            }
            else 
            {
              IAction action = registry.getAction(id);
              if (action != null) {
                action.isEnabled();
                currentMenu.add(action);
              }
            }
          }  
        }     
        
        menu.add(registry.getAction(ShowPropertiesViewAction.ID));
        menu.add(new Separator());       
        IMenuManager subMenu = new MenuManager(Messages._UI_REFACTOR_CONTEXT_MENU, IWSDLRefactorConstants.REFACTOR_CONTEXT_MENU_ID);
		menu.add(subMenu);
        menu.add(new Separator());       
        menu.add(new Separator("search_slot_temp"));        //$NON-NLS-1$
        menu.add(new Separator());
      }
    }    
    menu.add(new Separator());
  }

  protected IMenuManager getParentMenu(IMenuManager root, IMenuManager child) {
	  IMenuManager parent = null;
	  
	  IContributionItem[] kids = root.getItems();
	  int index = 0;
	  while (index < kids.length && parent == null) {
		  IContributionItem item = kids[index];
		  if (item.equals(child)) {
			  parent = root;
		  }
		  else {
			  if (item instanceof IMenuManager) {
				  parent = getParentMenu((IMenuManager) item, child);
			  }
		  }
		  index++;
	  }
	  
	  return parent;
  }
  
  protected ActionRegistry getEditorActionRegistry()
  {
    return (ActionRegistry) ASDEditorPlugin.getActiveEditor().getAdapter(ActionRegistry.class);
  }
  protected CommandStack commandStack;

  protected CommandStack getCommandStack()
  {
    if (commandStack == null)
      commandStack = getViewer().getEditDomain().getCommandStack();
    return commandStack;
  }
}
