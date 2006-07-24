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
package org.eclipse.wst.wsdl.ui.internal.asd;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.ASDAddBindingAction;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.ASDAddEndPointAction;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.ASDAddFaultAction;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.ASDAddImportAction;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.ASDAddInputAction;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.ASDAddInterfaceAction;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.ASDAddOperationAction;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.ASDAddOutputAction;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.ASDAddParameterAction;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.ASDAddSchemaAction;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.ASDAddServiceAction;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.ASDDeleteAction;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.ASDGenerateBindingAction;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.ASDOpenSchemaAction;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.ASDSetExistingBindingAction;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.ASDSetExistingInterfaceAction;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.ASDSetNewBindingAction;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.ASDSetNewInterfaceAction;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.BaseSelectionAction;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.ShowPropertiesViewAction;
import org.eclipse.wst.wsdl.ui.internal.asd.design.DesignViewContextMenuProvider;
import org.eclipse.wst.wsdl.ui.internal.asd.design.DesignViewGraphicalViewer;
import org.eclipse.wst.wsdl.ui.internal.asd.design.directedit.DirectEditSelectionTool;
import org.eclipse.wst.wsdl.ui.internal.asd.design.editparts.ASDEditPartFactory;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IDescription;
import org.eclipse.wst.wsdl.ui.internal.asd.outline.ASDContentOutlinePage;
import org.eclipse.wst.wsdl.ui.internal.asd.outline.ASDContentOutlineProvider;
import org.eclipse.wst.wsdl.ui.internal.asd.properties.sections.ASDTabbedPropertySheetPage;
import org.eclipse.wst.wsdl.ui.internal.asd.util.IOpenExternalEditorHelper;
import org.eclipse.wst.xsd.ui.internal.adt.editor.CommonMultiPageEditor;

public abstract class ASDMultiPageEditor extends CommonMultiPageEditor
{
  // TODO: move to design viewer
  protected DesignViewContextMenuProvider menuProvider;
  protected IDescription model;
  private int currentPage = -1;
  
  /**
   * Creates a multi-page editor example.
   */
  public ASDMultiPageEditor()
  {
    super();
   
    getEditDomain().setActiveTool(new DirectEditSelectionTool());
    getEditDomain().setDefaultTool(new DirectEditSelectionTool());
  }

  public String getContributorId()
  {
    return "org.eclipse.wst.wsdl.ui.internal.WSDLEditor"; //$NON-NLS-1$
  }
  
  public IContentOutlinePage getContentOutlinePage() {
	  if ((fOutlinePage == null) || fOutlinePage.getControl() == null || (fOutlinePage.getControl().isDisposed())) {
		  ASDContentOutlineProvider provider = new ASDContentOutlineProvider(this, model);
      
      ASDContentOutlinePage outlinePage = new ASDContentOutlinePage(this, menuProvider);
		  outlinePage.setContentProvider(provider);
		  outlinePage.setLabelProvider(provider);
		  outlinePage.setModel(getModel());
		  outlinePage.addSelectionChangedListener(getSelectionManager());
      getSelectionManager().addSelectionChangedListener(outlinePage);
      
      fOutlinePage = outlinePage;
	  }
	  return fOutlinePage;
  }
  
  /**
   * Creates the pages of the multi-page editor.
   */
  protected void createPages()
  {
    selectionProvider = getSelectionManager();
    getEditorSite().setSelectionProvider(selectionProvider);

    createGraphPage();
    createSourcePage();

    buildAndSetModel();
    initializeGraphicalViewer();
    setActivePage(getDefaultPageTypeIndex());
    
    getSelectionManager().setSelection(new StructuredSelection(getModel()));
  }
  
  protected int getDefaultPageTypeIndex() {
	  int pageIndex = SOURCE_PAGE_INDEX;
	  if (WSDLEditorPlugin.getInstance().getDefaultPage().equals(WSDLEditorPlugin.DESIGN_PAGE)) {
		  pageIndex = DESIGN_PAGE_INDEX;
	  }

	  return pageIndex;
	  }
  
  protected ScrollingGraphicalViewer getGraphicalViewer()
  {
    return new DesignViewGraphicalViewer(this, getSelectionManager());
  }
  
  public void buildAndSetModel() {
	  model = buildModel((IFileEditorInput)getEditorInput());
  }

  abstract public IDescription buildModel(IFileEditorInput editorInput);
  
  protected void createActions()
  {
    ActionRegistry registry = getActionRegistry();

    BaseSelectionAction action = new ASDAddServiceAction(this);
    action.setSelectionProvider(getSelectionManager());
    registry.registerAction(action);
    
    action = new ASDAddBindingAction(this);
    action.setSelectionProvider(getSelectionManager());
    registry.registerAction(action);
    
    action = new ASDAddInterfaceAction(this);
    action.setSelectionProvider(getSelectionManager());
    registry.registerAction(action);
    
    action = new ASDAddEndPointAction(this);
    action.setSelectionProvider(getSelectionManager());
    registry.registerAction(action);
    
    action = new ASDAddOperationAction(this);
    action.setSelectionProvider(getSelectionManager());
    registry.registerAction(action);
    
    action = new ASDAddInputAction(this);
    action.setSelectionProvider(getSelectionManager());
    registry.registerAction(action);
    
    action = new ASDAddOutputAction(this);
    action.setSelectionProvider(getSelectionManager());
    registry.registerAction(action);
    
    action = new ASDAddFaultAction(this);
    action.setSelectionProvider(getSelectionManager());
    registry.registerAction(action);

    action = new ASDDeleteAction(this);
    action.setSelectionProvider(getSelectionManager());
    registry.registerAction(action);
    
    action = new ASDSetNewBindingAction(this);
    action.setSelectionProvider(getSelectionManager());
    registry.registerAction(action);
    
    action = new ASDSetExistingBindingAction(this);
    action.setSelectionProvider(getSelectionManager());
    registry.registerAction(action);

    action = new ASDSetNewInterfaceAction(this);
    action.setSelectionProvider(getSelectionManager());
    registry.registerAction(action);
    
    action = new ASDSetExistingInterfaceAction(this);
    action.setSelectionProvider(getSelectionManager());
    registry.registerAction(action);
    
    action = new ASDGenerateBindingAction(this);
    action.setSelectionProvider(getSelectionManager());
    registry.registerAction(action);
    
    action = new ASDAddImportAction(this);
    action.setSelectionProvider(getSelectionManager());
    registry.registerAction(action);
    
    action = new ASDAddParameterAction(this);
    action.setSelectionProvider(getSelectionManager());
    registry.registerAction(action);    
    
    action = new ASDAddSchemaAction(this);
    action.setSelectionProvider(getSelectionManager());
    registry.registerAction(action);
    
    action = new ASDOpenSchemaAction(this);
    action.setSelectionProvider(getSelectionManager());
    registry.registerAction(action);
    
    action = new ShowPropertiesViewAction(this);
    action.setSelectionProvider(getSelectionManager());
    registry.registerAction(action);
  }

  public IDescription getModel()
  {
    return model;
  }

  public Object getAdapter(Class type)
  {
    if (type == ZoomManager.class)
      return graphicalViewer.getProperty(ZoomManager.class.toString());

    if (type == org.eclipse.ui.views.properties.IPropertySheetPage.class)
    {
    	ASDTabbedPropertySheetPage page = new ASDTabbedPropertySheetPage(this);
    	return page;
    }
    if (type == GraphicalViewer.class)
      return graphicalViewer;
    if (type == EditPart.class && graphicalViewer != null)
      return graphicalViewer.getRootEditPart();
    if (type == IFigure.class && graphicalViewer != null)
      return ((GraphicalEditPart) graphicalViewer.getRootEditPart()).getFigure();
    
    if (IContentOutlinePage.class.equals(type))
    {
      return getContentOutlinePage();
    }
    
    if (type == ISelectionProvider.class)
    {
       return getSelectionManager();
    }
    
    return super.getAdapter(type);
  }

  protected EditPartFactory getEditPartFactory() {
    return new ASDEditPartFactory();
  }
  
  protected void setEditPartFactory(EditPartFactory factory) {
	  graphicalViewer.setEditPartFactory(factory);
  }
  
  protected void initializeGraphicalViewer()
  {
    graphicalViewer.setContents(model);
    menuProvider = new DesignViewContextMenuProvider(graphicalViewer, getSelectionManager());
  }
  
  protected void pageChange(int newPageIndex) {
    currentPage = newPageIndex;
    super.pageChange(newPageIndex);
  }
  
  public void dispose() {
	  if (currentPage == SOURCE_PAGE_INDEX) {
		  WSDLEditorPlugin.getInstance().setDefaultPage(WSDLEditorPlugin.SOURCE_PAGE);
	  }
	  else {
		  WSDLEditorPlugin.getInstance().setDefaultPage(WSDLEditorPlugin.DESIGN_PAGE);
	  }

	  super.dispose();
  }
  
  public abstract IOpenExternalEditorHelper getOpenExternalEditorHelper();
}
