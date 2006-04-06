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
package org.eclipse.wst.wsdl.asd.editor;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.MouseWheelHandler;
import org.eclipse.gef.MouseWheelZoomHandler;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CommandStackListener;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.UpdateAction;
import org.eclipse.gef.ui.actions.ZoomInAction;
import org.eclipse.gef.ui.actions.ZoomOutAction;
import org.eclipse.gef.ui.parts.SelectionSynchronizer;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FontDialog;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertySheetPageContributor;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.eclipse.wst.wsdl.asd.design.DesignViewContextMenuProvider;
import org.eclipse.wst.wsdl.asd.design.DesignViewGraphicalViewer;
import org.eclipse.wst.wsdl.asd.design.directedit.DirectEditSelectionTool;
import org.eclipse.wst.wsdl.asd.design.editparts.ASDEditPartFactory;
import org.eclipse.wst.wsdl.asd.design.editparts.ASDRootEditPart;
import org.eclipse.wst.wsdl.asd.editor.actions.ASDAddBindingAction;
import org.eclipse.wst.wsdl.asd.editor.actions.ASDAddEndPointAction;
import org.eclipse.wst.wsdl.asd.editor.actions.ASDAddFaultAction;
import org.eclipse.wst.wsdl.asd.editor.actions.ASDAddImportAction;
import org.eclipse.wst.wsdl.asd.editor.actions.ASDAddInputAction;
import org.eclipse.wst.wsdl.asd.editor.actions.ASDAddInterfaceAction;
import org.eclipse.wst.wsdl.asd.editor.actions.ASDAddOperationAction;
import org.eclipse.wst.wsdl.asd.editor.actions.ASDAddOutputAction;
import org.eclipse.wst.wsdl.asd.editor.actions.ASDAddSchemaAction;
import org.eclipse.wst.wsdl.asd.editor.actions.ASDAddServiceAction;
import org.eclipse.wst.wsdl.asd.editor.actions.ASDDeleteAction;
import org.eclipse.wst.wsdl.asd.editor.actions.ASDGenerateBindingAction;
import org.eclipse.wst.wsdl.asd.editor.actions.ASDSetExistingBindingAction;
import org.eclipse.wst.wsdl.asd.editor.actions.ASDSetExistingInterfaceAction;
import org.eclipse.wst.wsdl.asd.editor.actions.ASDSetNewBindingAction;
import org.eclipse.wst.wsdl.asd.editor.actions.ASDSetNewInterfaceAction;
import org.eclipse.wst.wsdl.asd.editor.actions.BaseSelectionAction;
import org.eclipse.wst.wsdl.asd.editor.outline.ASDContentOutlinePage;
import org.eclipse.wst.wsdl.asd.editor.outline.ASDContentOutlineProvider;
import org.eclipse.wst.wsdl.asd.editor.properties.sections.ASDTabbedPropertySheetPage;
import org.eclipse.wst.wsdl.asd.editor.util.IOpenExternalEditorHelper;
import org.eclipse.wst.wsdl.asd.facade.IDescription;

public abstract class ASDMultiPageEditor extends MultiPageEditorPart implements IResourceChangeListener, CommandStackListener, ITabbedPropertySheetPageContributor, IPropertyListener
{
  protected DesignViewContextMenuProvider menuProvider;
  protected ASDContentOutlinePage fOutlinePage;
  protected long lastModificationStamp;

  protected IDescription model;
  private DesignViewGraphicalViewer graphicalViewer;
  private DefaultEditDomain editDomain;
  private SelectionSynchronizer synchronizer;
  private ActionRegistry actionRegistry;
//  private List selectionActions = new ArrayList();
  private List stackActions = new ArrayList();
//  private List propertyActions = new ArrayList();

  /** The text editor used in page 0. */
  protected StructuredTextEditor editor;

  /** The font chosen in page 1. */
  private Font font;

  /** The text widget used in page 2. */
  private StyledText text;

  /**
   * Creates a multi-page editor example.
   */
  public ASDMultiPageEditor()
  {
    super();
    DefaultEditDomain defaultGEFEditDomain = new DefaultEditDomain(this);
    setEditDomain(defaultGEFEditDomain);
    
    getEditDomain().setActiveTool(new DirectEditSelectionTool());
    getEditDomain().setDefaultTool(new DirectEditSelectionTool());
  }

  public String getContributorId()
  {
    return "org.eclipse.wst.wsdl.ui.internal.WSDLEditor";
  }

  private ASDSelectionManager selectionProvider;
  private ASDSelectionManager selectionManager;
  
  public ASDSelectionManager getSelectionProvider()
  {
    return selectionProvider;
  }
  
  public ASDSelectionManager getSelectionManager()
  {
    if (selectionManager == null)
    {
      selectionManager = new ASDSelectionManager(this);
    }
    return selectionManager;
  }
  
  public ContentOutlinePage getContentOutlinePage() {
	  if ((fOutlinePage == null) || fOutlinePage.getControl() == null || (fOutlinePage.getControl().isDisposed())) {
		  ASDContentOutlineProvider provider = new ASDContentOutlineProvider(this, model);
		  fOutlinePage = new ASDContentOutlinePage(this, menuProvider);
		  fOutlinePage.setContentProvider(provider);
		  fOutlinePage.setLabelProvider(provider);
		  fOutlinePage.setModel(getModel());
		  fOutlinePage.addSelectionChangedListener(selectionManager);
	      selectionManager.addSelectionChangedListener(fOutlinePage);
	    }
	    return fOutlinePage;
  }
  
  protected void addSourcePage() {
	  editor.update();
	  editor.setEditorPart(this);
	  editor.addPropertyListener(this);
	  
	  firePropertyChange(PROP_TITLE);
  }
  
  /**
   * Creates page 0 of the multi-page editor, which contains a text editor.
   */
  void createSourcePage()
  {
	  editor = new StructuredTextEditor();
  }

  /**
   * Creates page 1 of the multi-page editor, which allows you to change the
   * font used in page 2.
   */
  Composite createDesignPage()
  {
    Composite parent = new Composite(getContainer(), SWT.NONE);
    parent = new Composite(getContainer(), SWT.NONE);
    parent.setLayout(new FillLayout());
    graphicalViewer = new DesignViewGraphicalViewer(this, getSelectionProvider());
    graphicalViewer.createControl(parent);
    getEditDomain().addViewer(graphicalViewer);
    configureGraphicalViewer();
    hookGraphicalViewer();

    return parent;
  }

  /**
   * Creates the pages of the multi-page editor.
   */
  protected void createPages()
  {
    selectionProvider = getSelectionManager();
    getEditorSite().setSelectionProvider(selectionProvider);

    createSourcePage();

    Composite designPage = createDesignPage();
    try {
    	int index;
    	index = addPage(designPage);
    	setPageText(index, "Design");
    	
    	index = addPage(editor, getEditorInput());
    	setPageText(index, "Source");
    }
    catch (PartInitException e) {
    	ErrorDialog.openError(getSite().getShell(), "Error creating nested text editor", null, e.getStatus());
    }    
    addSourcePage();
    
    
    buildAndSetModel();
    initializeGraphicalViewer();
    setActivePage(0);
  }
  
  public void buildAndSetModel() {
	  model = buildModel((IFileEditorInput)getEditorInput());
  }
  
	/**
	 * Indicates that a property has changed.
	 * 
	 * @param source
	 *            the object whose property has changed
	 * @param propId
	 *            the id of the property which has changed; property ids are
	 *            generally defined as constants on the source class
	 */
	public void propertyChanged(Object source, int propId) {
		switch (propId) {
			// had to implement input changed "listener" so that
			// strucutedText could tell it containing editor that
			// the input has change, when a 'resource moved' event is
			// found.
			case IEditorPart.PROP_INPUT :
			case IEditorPart.PROP_DIRTY : {
				if (source == editor) {
					if (editor.getEditorInput() != getEditorInput()) {
						setInput(editor.getEditorInput());
						// title should always change when input changes.
						// create runnable for following post call
						Runnable runnable = new Runnable() {
							public void run() {
								_firePropertyChange(IWorkbenchPart.PROP_TITLE);
							}
						};
						// Update is just to post things on the display queue
						// (thread). We have to do this to get the dirty
						// property to get updated after other things on the
						// queue are executed.
						postOnDisplayQue(runnable);
					}
				}
				break;
			}
			case IWorkbenchPart.PROP_TITLE : {
				// update the input if the title is changed
				if (source == editor) {
					if (editor.getEditorInput() != getEditorInput()) {
						setInput(editor.getEditorInput());
					}
				}
				break;
			}
			default : {
				// propagate changes. Is this needed? Answer: Yes.
				if (source == editor) {
					firePropertyChange(propId);
				}
				break;
			}
		}

	}
	
	/*
	 * This method is just to make firePropertyChanged accessbible from some
	 * (anonomous) inner classes.
	 */
	protected void _firePropertyChange(int property) {
		super.firePropertyChange(property);
	}

	/**
	 * Posts the update code "behind" the running operation.
	 */
	protected void postOnDisplayQue(Runnable runnable) {
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow[] windows = workbench.getWorkbenchWindows();
		if (windows != null && windows.length > 0) {
			Display display = windows[0].getShell().getDisplay();
			display.asyncExec(runnable);
		}
		else
			runnable.run();
	}
	
  /**
   * The <code>MultiPageEditorPart</code> implementation of this
   * <code>IWorkbenchPart</code> method disposes all nested editors.
   * Subclasses may extend.
   */
  public void dispose()
  {
    getCommandStack().removeCommandStackListener(this);
    ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
    getActionRegistry().dispose();
    super.dispose();
  }

  /**
   * Saves the multi-page editor's document.
   */
  public void doSave(IProgressMonitor monitor)
  {
    getEditor(1).doSave(monitor); 
  }

  /**
   * Saves the multi-page editor's document as another file. Also updates the
   * text for page 0's tab, and updates this multi-page editor's input to
   * correspond to the nested editor's.
   */
  public void doSaveAs()
  {
    IEditorPart editor = getEditor(1);
    editor.doSaveAs();
    setPageText(1, editor.getTitle());
    setInput(editor.getEditorInput());
  }

  /*
   * (non-Javadoc) Method declared on IEditorPart
   */
  public void gotoMarker(IMarker marker)
  {
    setActivePage(0);
    IDE.gotoMarker(getEditor(0), marker);
  }

  /**
   * The <code>MultiPageEditorExample</code> implementation of this method
   * checks that the input is an instance of <code>IFileEditorInput</code>.
   */
  public void init(IEditorSite site, IEditorInput editorInput) throws PartInitException
  {
    if (!(editorInput instanceof IFileEditorInput))
      throw new PartInitException("Invalid Input: Must be IFileEditorInput");
    super.init(site, editorInput);
    
    getCommandStack().addCommandStackListener(this);
    initializeActionRegistry();    
    
    String title = null;
    if (getEditorInput() != null) {
      title = getEditorInput().getName();
    }
    setPartName(title);
  }

  /*
   * (non-Javadoc) Method declared on IEditorPart.
   */
  public boolean isSaveAsAllowed()
  {
    return true;
  }

  /**
   * Calculates the contents of page 2 when the it is activated.
   */
  protected void pageChange(int newPageIndex)
  {
    super.pageChange(newPageIndex);
    if (newPageIndex == 1)
    {
    }
  }

  /**
   * Closes all project files on project close.
   */
  public void resourceChanged(final IResourceChangeEvent event)
  {
    if (event.getType() == IResourceChangeEvent.PRE_CLOSE)
    {
      Display.getDefault().asyncExec(new Runnable()
      {
        public void run()
        {
          IWorkbenchPage[] pages = getSite().getWorkbenchWindow().getPages();
          for (int i = 0; i < pages.length; i++)
          {
            if (((FileEditorInput) editor.getEditorInput()).getFile().getProject().equals(event.getResource()))
            {
              IEditorPart editorPart = pages[i].findEditor(editor.getEditorInput());
              pages[i].closeEditor(editorPart, true);
            }
          }
        }
      });
    }
  }

  /**
   * Sets the font related data to be applied to the text in page 2.
   */
  void setFont()
  {
    FontDialog fontDialog = new FontDialog(getSite().getShell());
    fontDialog.setFontList(text.getFont().getFontData());
    FontData fontData = fontDialog.open();
    if (fontData != null)
    {
      if (font != null)
        font.dispose();
      font = new Font(text.getDisplay(), fontData);
      text.setFont(font);
    }
  }

  abstract public IDescription buildModel(IFileEditorInput editorInput);
  
  protected void initializeActionRegistry()
  {
    createActions();
  }

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
    
    action = new ASDAddSchemaAction(this);
    action.setSelectionProvider(getSelectionManager());
    registry.registerAction(action);
  }

  public IDescription getModel()
  {
    return model;
  }

  //protected XSDModelAdapterFactoryImpl xsdModelAdapterFactory;
  //protected XSDAdapterFactoryLabelProvider adapterFactoryLabelProvider;

  public Object getAdapter(Class type)
  {
    if (type == ZoomManager.class)
      return graphicalViewer.getProperty(ZoomManager.class.toString());
    /*
    if (type == ISelectionProvider.class)
    {
      result = getSelectionManager();
    }
    */

    if (type == org.eclipse.ui.views.properties.IPropertySheetPage.class)
    {
    	ASDTabbedPropertySheetPage page = new ASDTabbedPropertySheetPage(this);
    	return page;
    }
    if (type == GraphicalViewer.class)
      return graphicalViewer;
    if (type == CommandStack.class)
      return getCommandStack();
    if (type == ActionRegistry.class)
      return getActionRegistry();
    if (type == EditPart.class && graphicalViewer != null)
      return graphicalViewer.getRootEditPart();
    if (type == IFigure.class && graphicalViewer != null)
      return ((GraphicalEditPart) graphicalViewer.getRootEditPart()).getFigure();
    
    if (IContentOutlinePage.class.equals(type))
    {
      return getContentOutlinePage();
    }

    return super.getAdapter(type);
  }

  protected DefaultEditDomain getEditDomain()
  {
    return editDomain;
  }


  protected void configureGraphicalViewer()
  {
    graphicalViewer.getControl().setBackground(ColorConstants.listBackground);

    // Set the root edit part
    // ScalableFreeformRootEditPart root = new ScalableFreeformRootEditPart();
    ASDRootEditPart root = new ASDRootEditPart();

    List zoomLevels = new ArrayList(3);
    zoomLevels.add(ZoomManager.FIT_ALL);
    zoomLevels.add(ZoomManager.FIT_WIDTH);
    zoomLevels.add(ZoomManager.FIT_HEIGHT);
    root.getZoomManager().setZoomLevelContributions(zoomLevels);

    IAction zoomIn = new ZoomInAction(root.getZoomManager());
    IAction zoomOut = new ZoomOutAction(root.getZoomManager());
    getActionRegistry().registerAction(zoomIn);
    getActionRegistry().registerAction(zoomOut);

    getSite().getKeyBindingService().registerAction(zoomIn);
    getSite().getKeyBindingService().registerAction(zoomOut);

    //ConnectionLayer connectionLayer = (ConnectionLayer) root.getLayer(LayerConstants.CONNECTION_LAYER);
    //connectionLayer.setConnectionRouter(new BendpointConnectionRouter());

    //connectionLayer.setConnectionRouter(new ShortestPathConnectionRouter(connectionLayer));
    // connectionLayer.setVisible(false);

    // Zoom
    ZoomManager manager = (ZoomManager) graphicalViewer.getProperty(ZoomManager.class.toString());
    if (manager != null)
      manager.setZoom(1.0);
    // Scroll-wheel Zoom
    graphicalViewer.setProperty(MouseWheelHandler.KeyGenerator.getKey(SWT.CTRL), MouseWheelZoomHandler.SINGLETON);
    graphicalViewer.setRootEditPart(root);
    setEditPartFactory(new ASDEditPartFactory());
  }
  
  protected void setEditPartFactory(EditPartFactory factory) {
	  graphicalViewer.setEditPartFactory(factory);
  }

  protected void hookGraphicalViewer()
  {
    getSelectionSynchronizer().addViewer(graphicalViewer);
    // getSelectionManager().addSelectionChangedListener(graphicalViewer);
  }

  protected SelectionSynchronizer getSelectionSynchronizer()
  {
    if (synchronizer == null)
      synchronizer = new SelectionSynchronizer();
    return synchronizer;
  }

  protected void initializeGraphicalViewer()
  {
    graphicalViewer.setContents(model);
    menuProvider = new DesignViewContextMenuProvider(graphicalViewer, getSelectionProvider());
  }

  protected void setEditDomain(DefaultEditDomain ed)
  {
    this.editDomain = ed;
  }

  protected CommandStack getCommandStack()
  {
    return getEditDomain().getCommandStack();
  }

  protected ActionRegistry getActionRegistry()
  {
    if (actionRegistry == null)
      actionRegistry = new ActionRegistry();
    return actionRegistry;
  }

  public void commandStackChanged(EventObject event)
  {
    updateActions(stackActions);
    firePropertyChange(PROP_DIRTY);
  }

  /**
   * From GEF GraphicalEditor A convenience method for updating a set of actions
   * defined by the given List of action IDs. The actions are found by looking
   * up the ID in the {@link #getActionRegistry() action registry}. If the
   * corresponding action is an {@link UpdateAction}, it will have its
   * <code>update()</code> method called.
   * 
   * @param actionIds
   *          the list of IDs to update
   */
  protected void updateActions(List actionIds)
  {
    ActionRegistry registry = getActionRegistry();
    Iterator iter = actionIds.iterator();
    while (iter.hasNext())
    {
      IAction action = registry.getAction(iter.next());
      if (action instanceof UpdateAction)
        ((UpdateAction) action).update();
    }
  }

  /**
   * Returns <code>true</code> if the command stack is dirty
   * 
   * @see org.eclipse.ui.ISaveablePart#isDirty()
   */
  public boolean isDirty()
  {
    return super.isDirty();
    // TODO: rmah: Or do we need to tell the CommandStack that a save has occurred?
//    return getCommandStack().isDirty();
  }

  public StructuredTextEditor getTextEditor()
  {
	  return editor;
  }
  
  public abstract IOpenExternalEditorHelper getOpenExternalEditorHelper();
}
