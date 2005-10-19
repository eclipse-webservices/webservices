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

import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.EditorActionBarContributor;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.ITextEditorActionConstants;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditor;

public class WSDLActionBarContributor extends SourceEditorActionBarContributor
{
  protected ITextEditor textEditor;

  /**
   * Constructor for WSDLActionBarContributor.
   */
  public WSDLActionBarContributor()
  {
    super();
  }

  public void setActivePage(IEditorPart activeEditor)
  {
    super.setActivePage(activeEditor);

    // always enable undo/redo regardless of which page we're on.  The undo/redo comes from the editor
    //    
    updateAction(ActionFactory.UNDO.getId(), ITextEditorActionConstants.UNDO, true);
    updateAction(ActionFactory.REDO.getId(), ITextEditorActionConstants.REDO, true);

    // turn these off so that the actionhandler will handle it for us
    //
    //updateAction(IWorkbenchActionConstants.CUT, ITextEditorActionConstants.CUT, false);
    //updateAction(IWorkbenchActionConstants.COPY, ITextEditorActionConstants.COPY, false);
    //updateAction(IWorkbenchActionConstants.PASTE, ITextEditorActionConstants.PASTE, false);

    getActionBars().updateActionBars();
  }

  protected void updateAction(String globalActionId, String textEditorActionId, boolean enable)
  {
    getActionBars().setGlobalActionHandler(globalActionId, enable ? getAction(textEditor, textEditorActionId) : null);
  }

  /**
   * Returns the action registed with the given text editor.
   * @return IAction or null if editor is null.
   */
  protected IAction getAction(ITextEditor editor, String actionID)
  {
    try
    {
      return (editor == null ? null : editor.getAction(actionID));
    }
    catch (Exception e)
    {
      return null;
    }
  }

  /**
   * @see EditorActionBarContributor#contributeToToolBar(IToolBarManager)
   */
  public void addToToolBar(IToolBarManager toolBarManager)
  {
    super.addToToolBar(toolBarManager);
    toolBarManager.add(new GroupMarker("WSDLEditor"));
  }


  public void setActiveEditor(IEditorPart activeEditor)
  {
    super.setActiveEditor(activeEditor);
    textEditor = null;
    if (activeEditor instanceof WSDLEditor)
    {
      textEditor = ((WSDLEditor) activeEditor).getTextEditor();
    }
    
    updateAction(ActionFactory.UNDO.getId(), ITextEditorActionConstants.UNDO, true);
    updateAction(ActionFactory.REDO.getId(), ITextEditorActionConstants.REDO, true);
  }
}
