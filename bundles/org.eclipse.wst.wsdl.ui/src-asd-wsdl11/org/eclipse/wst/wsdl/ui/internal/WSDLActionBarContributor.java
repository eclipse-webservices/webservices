/*******************************************************************************
 * Copyright (c) 2001, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.actions.ZoomComboContributionItem;
import org.eclipse.gef.ui.actions.ZoomInRetargetAction;
import org.eclipse.gef.ui.actions.ZoomOutRetargetAction;
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
import org.eclipse.ui.actions.RetargetAction;
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
  protected List fPartListeners= new ArrayList();
  ZoomInRetargetAction zoomInRetargetAction;
  ZoomOutRetargetAction zoomOutRetargetAction;
  ZoomComboContributionItem zoomComboContributionItem;
  Action captureScreenAction;

  /**
   * Constructor for WSDLActionBarContributor.
   */
  public WSDLActionBarContributor()
  {
    super();
    sourceViewerActionContributor = new SourcePageActionContributor();
    zoomInRetargetAction = new ZoomInRetargetAction();
    zoomInRetargetAction.setImageDescriptor(WSDLEditorPlugin.getImageDescriptorFromPlugin("icons/etool16/zoomplus.gif"));
    zoomOutRetargetAction = new ZoomOutRetargetAction();
    zoomOutRetargetAction.setImageDescriptor(WSDLEditorPlugin.getImageDescriptorFromPlugin("icons/etool16/zoomminus.gif"));
    captureScreenAction = new CaptureScreenAction();
    fPartListeners.add(zoomInRetargetAction);
    fPartListeners.add(zoomOutRetargetAction);
  }

  public void setActivePage(IEditorPart part)
  {
    if (activeEditorPart == part)
      return;

    activeEditorPart = part;

    IActionBars actionBars = getActionBars();
    boolean isSource = false;
    
    if (activeEditorPart != null && activeEditorPart instanceof ITextEditor)
    {
    	isSource = true;
        zoomInRetargetAction.setEnabled(false);
        zoomOutRetargetAction.setEnabled(false);
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
          actionBars.setGlobalActionHandler(GEFActionConstants.ZOOM_IN, registry.getAction(GEFActionConstants.ZOOM_IN));
          actionBars.setGlobalActionHandler(GEFActionConstants.ZOOM_OUT, registry.getAction(GEFActionConstants.ZOOM_OUT));
          actionBars.setGlobalActionHandler(ActionFactory.PRINT.getId(), registry.getAction(ActionFactory.PRINT.getId()));
          zoomInRetargetAction.setEnabled(true);
          zoomOutRetargetAction.setEnabled(true);
          captureScreenAction.setEnabled(true);
        }
      }
    }

    if (actionBars != null) {
      // update menu bar and tool bar
      actionBars.updateActionBars();
    }
    
    if (zoomComboContributionItem != null)
    {
      zoomComboContributionItem.setVisible(!isSource);
      zoomComboContributionItem.update();
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
    String[] zoomStrings = new String[] { ZoomManager.FIT_ALL, ZoomManager.FIT_HEIGHT, ZoomManager.FIT_WIDTH };
    zoomComboContributionItem = new ZoomComboContributionItem(getPage(), zoomStrings);
    manager.add(zoomComboContributionItem);
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
	  Iterator e = fPartListeners.iterator();
	  while (e.hasNext())
	  {
		  page.addPartListener((RetargetAction) e.next());
	  }

	  initSourceViewerActionContributor(bars);
	  super.init(bars, page);
  }

  
  protected void initSourceViewerActionContributor(IActionBars actionBars) {
    if (sourceViewerActionContributor != null)
      sourceViewerActionContributor.init(actionBars, getPage());
  }
  
  public void dispose()
  {
	  fPartListeners = null;
	  if (sourceViewerActionContributor != null)
		  sourceViewerActionContributor.dispose();
	  super.dispose();
  }

public void contributeToMenu(IMenuManager manager) {
	super.contributeToMenu(manager);
	
	IMenuManager menu = new MenuManager(Messages._UI_EDITOR_NAME, "WSDLEditor");

    menu.add(zoomInRetargetAction);
    menu.add(zoomOutRetargetAction);
	manager.appendToGroup(IWorkbenchActionConstants.MB_ADDITIONS, menu);
	menu.add(captureScreenAction);

    menu.updateAll(true);
	
}

}
