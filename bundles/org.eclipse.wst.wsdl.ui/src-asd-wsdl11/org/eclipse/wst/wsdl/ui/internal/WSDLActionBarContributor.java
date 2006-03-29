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
package org.eclipse.wst.wsdl.ui.internal;

import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.MultiPageEditorActionBarContributor;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.ITextEditorActionConstants;
import org.eclipse.wst.wsdl.ui.internal.actions.IWSDLToolbarAction;

public class WSDLActionBarContributor extends MultiPageEditorActionBarContributor
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
  
  public void contributeToToolBar(IToolBarManager manager)
  {
    manager.add(new GroupMarker("WSDLEditor"));
    List list = WSDLEditorPlugin.getInstance().getWSDLEditorConfiguration().getToolbarActions();
    for (Iterator i = list.iterator(); i.hasNext(); )
    {
      manager.add((IWSDLToolbarAction)i.next());
    }

//    manager.add(new Separator());
//    String[] zoomStrings = new String[] { ZoomManager.FIT_ALL, ZoomManager.FIT_HEIGHT, ZoomManager.FIT_WIDTH };
//    manager.add(new ZoomComboContributionItem(getPage(), zoomStrings));
  }

  public void setActiveEditor(IEditorPart activeEditor)
  {
    super.setActiveEditor(activeEditor);
    textEditor = null;
    if (activeEditor instanceof InternalWSDLMultiPageEditor)
    {
      textEditor = ((InternalWSDLMultiPageEditor) activeEditor).getTextEditor();
    }
    
    updateAction(ActionFactory.UNDO.getId(), ITextEditorActionConstants.UNDO, true);
    updateAction(ActionFactory.REDO.getId(), ITextEditorActionConstants.REDO, true);
    
    List list = WSDLEditorPlugin.getInstance().getWSDLEditorConfiguration().getToolbarActions();
    for (Iterator i = list.iterator(); i.hasNext(); )
    {
      ((IWSDLToolbarAction)i.next()).setEditorPart(activeEditor);
    }
  }
}
