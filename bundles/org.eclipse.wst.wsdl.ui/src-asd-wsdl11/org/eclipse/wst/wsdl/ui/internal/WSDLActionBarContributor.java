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
package org.eclipse.wst.wsdl.ui.internal;

import java.util.Iterator;
import java.util.List;

import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorActionBarContributor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.MultiPageEditorActionBarContributor;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.ITextEditorActionConstants;
import org.eclipse.wst.sse.ui.internal.ISourceViewerActionBarContributor;
import org.eclipse.wst.wsdl.ui.internal.actions.IWSDLToolbarAction;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.ASDDeleteAction;
import org.eclipse.wst.xsd.ui.internal.adt.actions.CaptureScreenAction;

public class WSDLActionBarContributor extends MultiPageEditorActionBarContributor
{
  private IEditorPart activeEditorPart;
  private InternalWSDLMultiPageEditor wsdlEditor;
  protected ITextEditor textEditor;
  protected IEditorActionBarContributor sourceViewerActionContributor = null;
  Action captureScreenAction;

  /**
   * Constructor for WSDLActionBarContributor.
   */
  public WSDLActionBarContributor()
  {
    super();
    sourceViewerActionContributor = new SourcePageActionContributor();
    captureScreenAction = new CaptureScreenAction();
  }

  public void setActivePage(IEditorPart part)
  {
    if (activeEditorPart == part)
      return;

    activeEditorPart = part;

    IActionBars actionBars = getActionBars();
    
    if (activeEditorPart != null && activeEditorPart instanceof ITextEditor)
    {
      activateSourcePage(activeEditorPart, true);
      captureScreenAction.setEnabled(false);
    }
    else
    {
      activateSourcePage(wsdlEditor, false);
      if (part instanceof InternalWSDLMultiPageEditor)
      {
        wsdlEditor = (InternalWSDLMultiPageEditor) part;
      }
      if (wsdlEditor != null)
      {
        // cs: here's we ensure the UNDO and REDO actions are available when 
        // the design view is active
        IWorkbenchPartSite site = wsdlEditor.getSite();
        if (site instanceof IEditorSite) 
        {
          ITextEditor textEditor = wsdlEditor.getTextEditor();
          IActionBars siteActionBars = ((IEditorSite) site).getActionBars();
          siteActionBars.setGlobalActionHandler(ITextEditorActionConstants.UNDO, getAction(textEditor, ITextEditorActionConstants.UNDO));
          siteActionBars.setGlobalActionHandler(ITextEditorActionConstants.REDO, getAction(textEditor, ITextEditorActionConstants.REDO));
          siteActionBars.updateActionBars();              
        }        

        Object adapter = wsdlEditor.getAdapter(ActionRegistry.class);
        if (adapter instanceof ActionRegistry)
        {
          ActionRegistry registry = (ActionRegistry) adapter;
          actionBars.setGlobalActionHandler(ActionFactory.DELETE.getId(), registry.getAction(ASDDeleteAction.ID));
          captureScreenAction.setEnabled(true);
        }
      }
    }

    if (actionBars != null) {
      // update menu bar and tool bar
      actionBars.updateActionBars();
    }
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
    manager.add(new GroupMarker("WSDLEditor")); //$NON-NLS-1$
    List list = WSDLEditorPlugin.getInstance().getWSDLEditorConfiguration().getToolbarActions();
    for (Iterator i = list.iterator(); i.hasNext(); )
    {
      manager.add((IWSDLToolbarAction)i.next());
    }

//    manager.add(new Separator());
//    String[] zoomStrings = new String[] { ZoomManager.FIT_ALL, ZoomManager.FIT_HEIGHT, ZoomManager.FIT_WIDTH };
//    manager.add(new ZoomComboContributionItem(getPage(), zoomStrings));
    manager.add(captureScreenAction);
  }

  public void setActiveEditor(IEditorPart part)
  {
    IEditorPart activeNestedEditor = null;
    if (part instanceof MultiPageEditorPart)
    {
      activeNestedEditor = part;
    }
    setActivePage(activeNestedEditor);
    
    if (part instanceof InternalWSDLMultiPageEditor)
    {
      wsdlEditor = (InternalWSDLMultiPageEditor) part;

      textEditor = wsdlEditor.getTextEditor();
      if (textEditor != null)
      {      
        getActionBars().updateActionBars();
      }
    }
    
    List list = WSDLEditorPlugin.getInstance().getWSDLEditorConfiguration().getToolbarActions();
    for (Iterator i = list.iterator(); i.hasNext(); )
    {
      ((IWSDLToolbarAction)i.next()).setEditorPart(part);
    }
    super.setActiveEditor(part);
  }
  
  protected void activateSourcePage(IEditorPart activeEditor, boolean state)
  {
    if (sourceViewerActionContributor != null && sourceViewerActionContributor instanceof ISourceViewerActionBarContributor)
    {
      sourceViewerActionContributor.setActiveEditor(activeEditor);
      ((ISourceViewerActionBarContributor) sourceViewerActionContributor).setViewerSpecificContributionsEnabled(state);
    }
  }

  public void init(IActionBars bars, IWorkbenchPage page)
  {
    initSourceViewerActionContributor(bars);
    super.init(bars, page);
  }

  
  protected void initSourceViewerActionContributor(IActionBars actionBars) {
    if (sourceViewerActionContributor != null)
      sourceViewerActionContributor.init(actionBars, getPage());
  }
  
  public void dispose()
  {
    if (sourceViewerActionContributor != null)
      sourceViewerActionContributor.dispose();
    super.dispose();
  }

public void contributeToMenu(IMenuManager manager) {
	super.contributeToMenu(manager);
	
	IMenuManager menu = new MenuManager(Messages._UI_EDITOR_NAME, "WSDLEditor");

	manager.appendToGroup(IWorkbenchActionConstants.MB_ADDITIONS, menu);
	menu.add(captureScreenAction);

    menu.updateAll(true);
	
}

}
