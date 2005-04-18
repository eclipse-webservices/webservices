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
package org.eclipse.wst.wsdl.ui.internal;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.INavigationLocation;
import org.eclipse.ui.INavigationLocationProvider;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.TextSelectionNavigationLocation;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.sse.core.internal.provisional.exceptions.SourceEditingRuntimeException;
import org.eclipse.wst.sse.ui.internal.StructuredTextEditor;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.XSDSchemaExtensibilityElement;
import org.eclipse.wst.wsdl.ui.internal.actions.WSDLMenuActionContributor;
import org.eclipse.wst.wsdl.ui.internal.extension.WSDLEditorExtension;
import org.eclipse.wst.wsdl.ui.internal.graph.WSDLGraphViewer;
import org.eclipse.wst.wsdl.ui.internal.graph.editparts.WSDLEditPartFactory;
import org.eclipse.wst.wsdl.ui.internal.model.WSDLGroupObject;
import org.eclipse.wst.wsdl.ui.internal.model.WSDLModelAdapterFactory;
import org.eclipse.wst.wsdl.ui.internal.outline.ExtensibleOutlineProvider;
import org.eclipse.wst.wsdl.ui.internal.outline.ModelAdapterContentProvider;
import org.eclipse.wst.wsdl.ui.internal.outline.ModelAdapterLabelProvider;
import org.eclipse.wst.wsdl.ui.internal.reconciler.SEDDocumentAdapter;
import org.eclipse.wst.wsdl.ui.internal.typesystem.ExtensibleTypeSystemProvider;
import org.eclipse.wst.wsdl.ui.internal.util.ComponentReferenceUtil;
import org.eclipse.wst.wsdl.ui.internal.util.WSDLEditorUtil;
import org.eclipse.wst.wsdl.ui.internal.util.WSDLModelLocatorAdapterFactory;
import org.eclipse.wst.wsdl.ui.internal.util.WSDLNodeAssociationProvider;
import org.eclipse.wst.wsdl.ui.internal.util.WSDLResourceUtil;
import org.eclipse.wst.wsdl.ui.internal.util.XSDSchemaLocationResolverAdapterFactory;
import org.eclipse.wst.wsdl.ui.internal.viewers.WSDLDetailsViewer;
import org.eclipse.wst.wsdl.ui.internal.viewers.WSDLDetailsViewerProvider;
import org.eclipse.wst.wsdl.util.WSDLResourceImpl;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.w3c.dom.Document;

//public class WSDLEditor extends StructuredTextMultiPageEditorPart implements INavigationLocationProvider
public class WSDLEditor extends WSDLMultiPageEditorPart implements INavigationLocationProvider
{
  protected ExtensibleOutlineProvider extensibleOutlineProvider;

  protected WSDLTextEditor textEditor;
  protected WSDLGraphViewer graphViewer;
  protected WSDLDetailsViewer detailsViewer;
  protected WSDLSelectionManager selectionManager;
  protected SashForm sashForm;

  int graphPageIndex;

  //protected Resource resource;
  protected Definition definition;
  protected WSDLEditorResourceChangeHandler resourceChangeHandler;
  
  // Used for Cut, Copy, Paste actions.  This acts as a copy, cut, paste clipboard
  protected WSDLElement clipboardElement;

  public WSDLEditor()
  {
    selectionManager = new WSDLSelectionManager();
  }
  
	public void init(IEditorSite site, IEditorInput input) throws PartInitException
	{
    super.init(site, input);

    try
    {
      IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
      IWorkbenchPage page = window.getActivePage();
      if (page != null)
      {
        page.showView("org.eclipse.ui.views.PropertySheet");
      }
    }
    catch (PartInitException partInitException) 
    {

    }
    catch (Exception exception)
    {
      
    }
	}

  public Object getAdapter(Class key)
  {
    Object result = null;
    if (key == ISelectionProvider.class)
    {
      result = selectionManager;
    }
    else
    {
      result = super.getAdapter(key);
    }
    return result;
  }

  public void dispose()
  {
    // call the extensibleOutlineProvider's inputChanged method a null viewer 
    // so that the outline's contentprovider/adapters don't attempt to update the viewer
    // after the editor closes
    extensibleOutlineProvider.inputChanged(null, null, null);
    if (resourceChangeHandler != null)
    {
      resourceChangeHandler.dispose();
    }
    super.dispose();
  }

  public WSDLSelectionManager getSelectionManager()
  {
    return selectionManager;
  }

  public ExtensibleOutlineProvider getExtensibleOutlineProvider()
  {
    if (extensibleOutlineProvider == null)
    {
      extensibleOutlineProvider = new ExtensibleOutlineProvider(this);
    }
    return extensibleOutlineProvider;
  }

  public WSDLTextEditor getWSDLTextEditor()
  {
    return textEditor;
  }

  public WSDLGraphViewer getGraphViewer()
  {
    return graphViewer;
  }

  public IStructuredModel getStructuredModel()
  {
    return textEditor.getModel();
  }

  public Document getXMLDocument()
  {
    return ((IDOMModel)textEditor.getModel()).getDocument();
  }

  public Definition getDefinition()
  {
    return definition;
  }

  /**
   * Creates the pages of this multi-page editor.
   * <p>
   * Subclasses of <code>MultiPageEditor</code> must implement this method.
   * </p>
   */
  protected void createPages()
  {
    try
    {
      if (resourceChangeHandler == null)
      {
        resourceChangeHandler = new WSDLEditorResourceChangeHandler(this);
        resourceChangeHandler.attach();
      }
      createSourcePage();
      addSourcePage();

      ResourceSet resourceSet = new ResourceSetImpl();
      resourceSet.getAdapterFactories().add(new WSDLModelLocatorAdapterFactory());
      resourceSet.getAdapterFactories().add(new XSDSchemaLocationResolverAdapterFactory());
      resourceSet.getLoadOptions().put(WSDLResourceImpl.USE_EXTENSION_FACTORIES, Boolean.FALSE);

      IFile file = ((IFileEditorInput)getEditorInput()).getFile();

      // create a definition based on the text editor document
      //                                                     
      definition = WSDLResourceUtil.createDefinition(resourceSet, file, getXMLDocument());

      WSDLEditorUtil.getInstance().setTypeSystemProvider(definition, new ExtensibleTypeSystemProvider(this));

      //ReferenceManager.adaptDefinition(definition);
      new SEDDocumentAdapter(getStructuredModel(), definition);
      createAndAddGraphPage();

      // get the type of page and set the active page to show
      int pageIndexToShow = getDefaultPageIndex();
      setActivePage(pageIndexToShow);
      
      getSelectionManager().setSelection(new StructuredSelection(getDefinition()));
    }
    catch (PartInitException exception)
    {
      throw new SourceEditingRuntimeException(WSDLEditorPlugin.getWSDLString("An_error_has_occurred_when1_ERROR_")); //$NON-NLS-1$ = "An error has occurred when initializing the input for the the editor's source page."
    }
    // TODO: add a catch block here for any exception the design page throws and convert it into a more informative message.
  }

  protected int getDefaultPageIndex()
  {
    if (WSDLEditorPlugin.getInstance().getDefaultPage().equals(WSDLEditorPlugin.GRAPH_PAGE))
    {
      if (graphPageIndex != -1)
      {
        return graphPageIndex;
      }
    }
    return sourcePageIndex;
  }

  /**
   * @see org.eclipse.wst.wsdl.ui.WSDLMultiPageEditorPart#createTextEditor()
   */
  protected StructuredTextEditor createTextEditor()
  {
    textEditor = new WSDLTextEditor(this);
    return textEditor;
  }

  /**
   * create our own
   */
  protected void createSourcePage() throws PartInitException
  {
    super.createSourcePage();

    textEditor = (WSDLTextEditor)getTextEditor();
  }

  int sourcePageIndex = -1;
  /**
   * Adds the source page of the multi-page editor.
   */
  protected void addSourcePage() throws PartInitException
  {
    sourcePageIndex = addPage(textEditor, getEditorInput());

  	setPageText(sourcePageIndex, WSDLEditorPlugin.getWSDLString("_UI_TAB_SOURCE"));
    // defect 223043 ... do textEditor.setModel() here instead of in createSourcePage()
    textEditor.setModel((IFileEditorInput)getEditorInput());
    // the updates critical, to get viewer selection manager and highlighting to work
    textEditor.update();
  }
  
  int[] weights;
  public void setDesignWeights(int[] weights, boolean updateSourceDesign)
  {
    this.weights = weights;
    if (updateSourceDesign)
    {
      sashForm.setWeights(weights);
    }
  }

  protected void pageChange(int arg)
  {
    super.pageChange(arg);
    if (getPageText(arg).equals(WSDLEditorPlugin.getWSDLString("_UI_TAB_SOURCE"))) // TRANSLATE !
    {
      // update the input
    }
    else if (getPageText(arg).equals(WSDLEditorPlugin.getWSDLString("_UI_TAB_GRAPH"))) // TRANSLATE !
    {
      // update the input
    }
  }

  static private Color dividerColor;

  /**
   * Creates the graph page and adds it to the multi-page editor.
   **/
  protected void createAndAddGraphPage() throws PartInitException
  {
    // create the graph page
    sashForm = new SashForm(getContainer(), SWT.BORDER);
    sashForm.setLayoutData(new GridData(GridData.FILL_BOTH));
    sashForm.setOrientation(SWT.VERTICAL);
    int[] weights = { 8, 3 };

    graphPageIndex = addPage(sashForm);
    setPageText(graphPageIndex, WSDLEditorPlugin.getWSDLString("_UI_TAB_GRAPH"));

    // create the graph viewer
    graphViewer = new WSDLGraphViewer(this);
    graphViewer.createControl(sashForm);

//    detailsViewer = new WSDLDetailsViewer(this);    
//    detailsViewer.createControl(sashForm);  
//
//    sashForm.setWeights(weights);

    if (dividerColor == null)
    {
      dividerColor = new Color(getContainer().getDisplay(), 143, 141, 138);
    }

    getContainer().addPaintListener(new PaintListener()
    {
      /**
       * @see org.eclipse.swt.events.PaintListener#paintControl(PaintEvent)
       */
      public void paintControl(PaintEvent e)
      {
        Object source = e.getSource();
        if (source instanceof Composite)
        {
          Composite comp = (Composite)source;
          Rectangle boundary = comp.getClientArea();
          e.gc.setForeground(dividerColor);
          e.gc.drawLine(boundary.x, boundary.y, boundary.x + boundary.width, boundary.y);
          setDesignWeights(sashForm.getWeights(), true);
        }
      }
    });
  }

  public void setFocus()
  {
    super.setFocus();
    int activePage = getActivePage();
    if (activePage == sourcePageIndex)
    {
      WSDLEditorPlugin.getInstance().setDefaultPage(WSDLEditorPlugin.SOURCE_PAGE);
    }
    else
    {
      WSDLEditorPlugin.getInstance().setDefaultPage(WSDLEditorPlugin.GRAPH_PAGE);
    }
  }

  //
  //
  public static class BuiltInWSDLEditorExtension implements WSDLEditorExtension
  {
    public boolean isExtensionTypeSupported(int type)
    {
      return type == OUTLINE_TREE_CONTENT_PROVIDER
        || type == OUTLINE_LABEL_PROVIDER
        || type == EDIT_PART_FACTORY
        || type == DETAILS_VIEWER_PROVIDER
        || type == MENU_ACTION_CONTRIBUTOR
        || type == NODE_RECONCILER
        || type == NODE_ASSOCIATION_PROVIDER;
    }

    public boolean isApplicable(Object object)
    {
      return (object instanceof WSDLElement && !(object instanceof XSDSchemaExtensibilityElement)) || (object instanceof WSDLGroupObject);
    }

    public Object createExtensionObject(int type, WSDLEditor wsdlEditor)
    {
      Object result = null;
      switch (type)
      {
        case OUTLINE_TREE_CONTENT_PROVIDER :
          {
            result = new ModelAdapterContentProvider(WSDLModelAdapterFactory.getWSDLModelAdapterFactory());
            break;
          }
        case OUTLINE_LABEL_PROVIDER :
          {
            result = new ModelAdapterLabelProvider(WSDLModelAdapterFactory.getWSDLModelAdapterFactory());
            break;
          }
        case DETAILS_VIEWER_PROVIDER :
          {
            result = new WSDLDetailsViewerProvider();
            break;
          }
        case MENU_ACTION_CONTRIBUTOR :
          {
            result = new WSDLMenuActionContributor(wsdlEditor);
            break;
          }
        case NODE_ASSOCIATION_PROVIDER :
          {
            result = new WSDLNodeAssociationProvider();
            break;
          }
        case EDIT_PART_FACTORY :
          {
            result = new WSDLEditPartFactory();
            break;
          }
      }
      return result;
    }
  }

 
  public void reloadDependencies()
  {
    try
    {
      getGraphViewer().getComponentViewer().setPreserveExpansionEnabled(true);
      WSDLResourceUtil.reloadDirectives(definition);
      ComponentReferenceUtil.updateBindingReferences(definition);
      ComponentReferenceUtil.updatePortTypeReferences(definition);
      ComponentReferenceUtil.updateMessageReferences(definition);
      ComponentReferenceUtil.updateSchemaReferences(definition);

      // the line below simply causes a notification in order to update our views
      //
      definition.setDocumentationElement(definition.getDocumentationElement());
    }
    finally
    {
      getGraphViewer().getComponentViewer().setPreserveExpansionEnabled(false);
    }
  }

  public void openOnSelection(String specification)
  {
    EObject eObject = getDefinition().eResource().getEObject(specification);
    if (eObject != null)
    {
      getSelectionManager().setSelection(new StructuredSelection(eObject));
    }
  }

  public INavigationLocation createEmptyNavigationLocation()
  {
    return new InternalTextSelectionNavigationLocation(textEditor, false);
  }

  public INavigationLocation createNavigationLocation()
  {
    return new InternalTextSelectionNavigationLocation(textEditor, true);
  }

  static class InternalTextSelectionNavigationLocation extends TextSelectionNavigationLocation
  {
    public InternalTextSelectionNavigationLocation(ITextEditor part, boolean initialize)
    {
      super(part, initialize);
    }

    protected IEditorPart getEditorPart()
    {
      IEditorPart part = super.getEditorPart();
      if (part instanceof WSDLEditor)
      {
        part = ((WSDLEditor)part).getTextEditor();
      }
      return part;
    }

    public String getText()
    {
      IEditorPart part = getEditorPart();
      if (part instanceof WSDLTextEditor)
      {
        return ((WSDLTextEditor)part).getWSDLEditor().getTitle();
      }
      else
      {
        return super.getText();
      }
    }
  }
  
  // Returns the element currently on the copy, cut, paste clipboard
  public WSDLElement getClipboardContents() {
  	return clipboardElement;
  }
  
  public void setClipboardContents(WSDLElement element) {
  	clipboardElement = element;
  }
}
