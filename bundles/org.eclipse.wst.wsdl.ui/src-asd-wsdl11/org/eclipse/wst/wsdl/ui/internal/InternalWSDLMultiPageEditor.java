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
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.KeyStroke;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.IPostSelectionProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.INodeNotifier;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.XSDSchemaExtensibilityElement;
import org.eclipse.wst.wsdl.ui.internal.actions.OpenInNewEditor;
import org.eclipse.wst.wsdl.ui.internal.adapters.WSDLBaseAdapter;
import org.eclipse.wst.wsdl.ui.internal.adapters.actions.W11AddPartAction;
import org.eclipse.wst.wsdl.ui.internal.adapters.actions.W11OpenImportAction;
import org.eclipse.wst.wsdl.ui.internal.adapters.actions.W11SetExistingElementAction;
import org.eclipse.wst.wsdl.ui.internal.adapters.actions.W11SetExistingMessageAction;
import org.eclipse.wst.wsdl.ui.internal.adapters.actions.W11SetExistingTypeAction;
import org.eclipse.wst.wsdl.ui.internal.adapters.actions.W11SetNewElementAction;
import org.eclipse.wst.wsdl.ui.internal.adapters.actions.W11SetNewMessageAction;
import org.eclipse.wst.wsdl.ui.internal.adapters.actions.W11SetNewTypeAction;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11Description;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11Type;
import org.eclipse.wst.wsdl.ui.internal.asd.ASDMultiPageEditor;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.ASDAddMessageAction;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.ASDDirectEditAction;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.BaseSelectionAction;
import org.eclipse.wst.wsdl.ui.internal.asd.design.DesignViewGraphicalViewer;
import org.eclipse.wst.wsdl.ui.internal.asd.design.KeyboardDragImpl;
import org.eclipse.wst.wsdl.ui.internal.asd.design.editparts.DefinitionsEditPart;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IDescription;
import org.eclipse.wst.wsdl.ui.internal.asd.outline.ASDContentOutlinePage;
import org.eclipse.wst.wsdl.ui.internal.asd.util.IOpenExternalEditorHelper;
import org.eclipse.wst.wsdl.ui.internal.edit.W11BindingReferenceEditManager;
import org.eclipse.wst.wsdl.ui.internal.edit.W11InterfaceReferenceEditManager;
import org.eclipse.wst.wsdl.ui.internal.edit.W11MessageReferenceEditManager;
import org.eclipse.wst.wsdl.ui.internal.edit.WSDLXSDElementReferenceEditManager;
import org.eclipse.wst.wsdl.ui.internal.edit.WSDLXSDTypeReferenceEditManager;
import org.eclipse.wst.wsdl.ui.internal.text.WSDLModelAdapter;
import org.eclipse.wst.wsdl.ui.internal.util.ComponentReferenceUtil;
import org.eclipse.wst.wsdl.ui.internal.util.W11OpenExternalEditorHelper;
import org.eclipse.wst.wsdl.ui.internal.util.WSDLAdapterFactoryHelper;
import org.eclipse.wst.wsdl.ui.internal.util.WSDLEditorUtil;
import org.eclipse.wst.wsdl.ui.internal.util.WSDLResourceUtil;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMDocument;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xsd.ui.internal.adt.design.IKeyboardDrag;
import org.eclipse.wst.xsd.ui.internal.adt.editor.EditorMode;
import org.eclipse.wst.xsd.ui.internal.adt.editor.EditorModeManager;
import org.eclipse.wst.xsd.ui.internal.adt.editor.ProductCustomizationProvider;
import org.eclipse.wst.xsd.ui.internal.editor.XSDElementReferenceEditManager;
import org.eclipse.wst.xsd.ui.internal.editor.XSDTypeReferenceEditManager;
import org.eclipse.xsd.XSDSchema;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


public class InternalWSDLMultiPageEditor extends ASDMultiPageEditor
{ 
	protected WSDLEditorResourceChangeHandler resourceChangeHandler;
	
	/**
	 * @deprecated call getexistingadapter on idomdocument instead
	 */
	protected WSDLModelAdapter modelAdapter;
	protected SourceEditorSelectionListener fSourceEditorSelectionListener;
	protected WSDLSelectionManagerSelectionListener fWSDLSelectionListener;

	private final static String WSDL_EDITOR_MODE_EXTENSION_ID = "org.eclipse.wst.wsdl.ui.editorModes"; //$NON-NLS-1$

	public IDescription buildModel(IEditorInput editorInput) {   
	  try {
	    // ISSUE: This code which deals with the structured model is similar to the one in the XSD editor. 
	    // It could be refactored into the base class.

	    IDocument doc = structuredTextEditor.getDocumentProvider().getDocument(editorInput);
	    createAndSetModel(doc);
	  }
	  catch (Exception e) {
	    e.printStackTrace();
	  }    

	  return model;
	}

	/**
	 * Creates a WSDL model based on the given text document and sets 
	 * the model for this editor.
	 * @param textDocument
	 */
	private void createAndSetModel(IDocument textDocument) {
	  /*
	   * ISSUE: if model was not successfully created, model is not 
	   * changed because there are numerous places that assume model 
	   * is never null.
	   */
	  if (textDocument != null) {
	    Document document = null;
	    IStructuredModel structuredModel = StructuredModelManager.getModelManager().getExistingModelForRead(textDocument);
	    try {
	      if (structuredModel instanceof IDOMModel)
	        document = ((IDOMModel)structuredModel).getDocument();
	    }
	    finally {
	      if (structuredModel != null)
	        structuredModel.releaseFromRead();
	    }
	    if (document instanceof INodeNotifier) {
	      WSDLModelAdapter modelAdapter = WSDLModelAdapter.lookupOrCreateModelAdapter(document);
	      Definition definition = modelAdapter.createDefinition(document);
	      WSDLAdapterFactoryHelper helper = WSDLAdapterFactoryHelper.getInstance();
	      model = (IDescription)helper.adapt(definition);
	    }
	  }
	}
	
	private XSDSchema[] getInlineSchemas() {
		List types = getModel().getTypes();
		XSDSchema[] schemas = new XSDSchema[types.size()];
		for (int index = 0; index < types.size(); index++) {
			W11Type type = (W11Type) types.get(index);
			schemas[index] = (XSDSchema) type.getTarget();
		}
		
		return schemas;
	}
	
	public Object getAdapter(Class type) {
		if (type == Definition.class)
		{
			return ((W11Description) getModel()).getTarget();
		}
		else if (type == ISelectionMapper.class)
		{
			return new WSDLSelectionMapper();
		}
        else if (type == Definition.class && model instanceof Adapter)
        {
          return ((Adapter)model).getTarget(); 
        }
		else if (type == IOpenExternalEditorHelper.class) {
			return new W11OpenExternalEditorHelper(getEditorInput());
		}

        else if (type == XSDTypeReferenceEditManager.class)
        {
          IEditorInput editorInput = getEditorInput();
          if (editorInput instanceof IFileEditorInput)
          {
            IFileEditorInput fileEditorInput = (IFileEditorInput) editorInput;
            WSDLXSDTypeReferenceEditManager refManager = new WSDLXSDTypeReferenceEditManager(fileEditorInput.getFile(), null);
            refManager.setSchemas(getInlineSchemas());
            return refManager;
          }
        }
        else if (type == XSDElementReferenceEditManager.class)
        {
          IEditorInput editorInput = getEditorInput();
          if (editorInput instanceof IFileEditorInput)
          {
            IFileEditorInput fileEditorInput = (IFileEditorInput) editorInput;
            WSDLXSDElementReferenceEditManager refManager = new WSDLXSDElementReferenceEditManager(fileEditorInput.getFile(), null);
            refManager.setSchemas(getInlineSchemas());
            return refManager;
          }
        }
        else if (type == W11BindingReferenceEditManager.class) {
            IEditorInput editorInput = getEditorInput();
            if (editorInput instanceof IFileEditorInput)
            {
            	IFileEditorInput fileEditorInput = (IFileEditorInput) editorInput;
            	return new W11BindingReferenceEditManager((W11Description) getModel(), fileEditorInput.getFile());
            }
        }
        else if (type == W11InterfaceReferenceEditManager.class) {
            IEditorInput editorInput = getEditorInput();
            if (editorInput instanceof IFileEditorInput)
            {
            	IFileEditorInput fileEditorInput = (IFileEditorInput) editorInput;
            	return new W11InterfaceReferenceEditManager((W11Description) getModel(), fileEditorInput.getFile());
            }        	
        }
        else if (type == W11MessageReferenceEditManager.class) {
            IEditorInput editorInput = getEditorInput();
            if (editorInput instanceof IFileEditorInput)
            {
            	IFileEditorInput fileEditorInput = (IFileEditorInput) editorInput;
            	return new W11MessageReferenceEditManager((W11Description) getModel(), fileEditorInput.getFile());
            }        	
        }
        else if (type == ProductCustomizationProvider.class)
        {
          return WSDLEditorPlugin.getInstance().getProductCustomizationProvider();
        }
        else if (type == IKeyboardDrag.class) {
			return new KeyboardDragImpl();
		}
        else if (type == IOpenExternalEditorHelper.class) {
        	return new W11OpenExternalEditorHelper(getEditorInput());
        }
		return super.getAdapter(type);
	}
	
	/**
	 * Listener on SSE's source editor's selections that converts DOM
	 * selections into xsd selections and notifies WSDL selection manager
	 */
	private class SourceEditorSelectionListener implements ISelectionChangedListener {
		/**
		 * Determines WSDL facade object based on DOM node
		 * 
		 * @param object
		 * @return
		 */
		private Object getWSDLFacadeObject(Object object) {
			// get the element node
			Element element = null;
			if (object instanceof Node) {
				Node node = (Node) object;
				if (node != null) {
					if (node.getNodeType() == Node.ELEMENT_NODE) {
						element = (Element) node;
					}
					else if (node.getNodeType() == Node.ATTRIBUTE_NODE) {
						element = ((Attr) node).getOwnerElement();
					}
				}
			}
			Object o = element;
			if (element != null) {
				Definition def = (Definition) ((W11Description) model).getTarget();
				Object modelObject = WSDLEditorUtil.getInstance().findModelObjectForElement(def, element);
				
				// rmah: We handle this special scenario where the modelObject is an XSDSchemaExtensibilityElement.
				// We actually want the XSDSchema object.  However, our node reconciler will always return
				// XSDSchemaExtensibilityElement because both XSDSchemaExtensibilityElement and XSDSchema
				// both have the SAME Element object, and the reconciler encounters the XSDSchemaExtensibilityElement
				// first....  See WSDLNodeNodeAssociationProvider for more details.....
				if (modelObject instanceof XSDSchemaExtensibilityElement) {
					modelObject = ((XSDSchemaExtensibilityElement) modelObject).getSchema();
				}
				if (modelObject != null) {
					o = WSDLAdapterFactoryHelper.getInstance().adapt((Notifier) modelObject);
				}
			}
			return o;
		}
		
		public void selectionChanged(SelectionChangedEvent event) {
			if (getSelectionManager().getEnableNotify() && getActivePage() == 1)
			{
				ISelection selection = event.getSelection();
				if (selection instanceof IStructuredSelection)
				{
					List selections = new ArrayList();
					for (Iterator i = ((IStructuredSelection) selection).iterator(); i.hasNext();)
					{
						Object domNode = i.next();
						Object facade = getWSDLFacadeObject(domNode);
						if (facade != null)
						{
							selections.add(facade);
						}
					}
					
					if (!selections.isEmpty())
					{
						StructuredSelection wsdlSelection = new StructuredSelection(selections);
						getSelectionManager().setSelection(wsdlSelection, getTextEditor().getSelectionProvider());
					}
				}
			}
		}
	}
	
	/**
	 * Listener on WSDL's selection manager's selections that converts WSDL
	 * selections into DOM selections and notifies SSE's selection provider
	 */
	private class WSDLSelectionManagerSelectionListener implements ISelectionChangedListener {
		/**
		 * Determines DOM node based on object (wsdl node)
		 * 
		 * @param object
		 * @return
		 */
		private Object getObjectForOtherModel(Object object) {
			Node node = null;
			
			if (object instanceof Node) {
				node = (Node) object;
			}
			else if (object instanceof String) {
				// The string is expected to be a URI fragment used to identify a WSDL element.
				// The URI fragment should be relative to the definition being edited in this editor.
				String uriFragment = (String)object;
				EObject definition = ((Definition)((W11Description)getModel()).getTarget());
				EObject modelObject = definition.eResource().getEObject(uriFragment);

				if (modelObject != null) {
					node = WSDLEditorUtil.getInstance().getNodeForObject(modelObject);
				}        
			}
			else {
				node = WSDLEditorUtil.getInstance().getNodeForObject(object);
			}
			
			// the text editor can only accept sed nodes!
			//
			if (!(node instanceof IDOMNode)) {
				node = null;
			}
			return node;
		}
		
		public void selectionChanged(SelectionChangedEvent event) {
			// do not fire selection in source editor if the current active page is the InternalWSDLMultiPageEditor (source)
			// We only want to make source selections if the active page is either the outline or properties (a modify
			// has been done via the outline or properties and not the source view).  We don't want to be selecting
			// and unselecting things in the source when editing in the source!!
			boolean makeSelection = true;
			if (PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage() != null  && !(event.getSource() instanceof IPostSelectionProvider)) {
				IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				if (page.getActivePart() instanceof InternalWSDLMultiPageEditor) {
					if (getActiveEditor() instanceof StructuredTextEditor) {
						makeSelection = false;
					}
				}
			}
			
			// do not fire selection in source editor if selection event came
			// from source editor
				if (event.getSource() != getTextEditor().getSelectionProvider() && makeSelection) {
				ISelection selection = event.getSelection();
				if (selection instanceof IStructuredSelection) {
					List otherModelObjectList = new ArrayList();
					for (Iterator i = ((IStructuredSelection) selection).iterator(); i.hasNext();) {
						Object wsdlObject = i.next();

						if (wsdlObject instanceof WSDLBaseAdapter) {
							wsdlObject = ((WSDLBaseAdapter) wsdlObject).getTarget();
						}

						Object otherModelObject = getObjectForOtherModel(wsdlObject);
						if (otherModelObject != null) {
							otherModelObjectList.add(otherModelObject);
						}
					}
					if (!otherModelObjectList.isEmpty()) {
						StructuredSelection nodeSelection = new StructuredSelection(otherModelObjectList);
						getTextEditor().getSelectionProvider().setSelection(nodeSelection);
					}
				}
			}
		}
	}
	
	protected void configureGraphicalViewer() {
		super.configureGraphicalViewer();
		graphicalViewer.getKeyHandler().put(KeyStroke.getPressed(SWT.F2, 0), getActionRegistry().getAction(GEFActionConstants.DIRECT_EDIT));
		setEditPartFactory(getEditorModeManager().getCurrentMode().getEditPartFactory());
	}
	
	protected void createPages() {
		super.createPages();
		
		if (resourceChangeHandler == null) {
			resourceChangeHandler = new WSDLEditorResourceChangeHandler(this);
			resourceChangeHandler.attach();
		}
		
		fSourceEditorSelectionListener = new SourceEditorSelectionListener();
		ISelectionProvider provider = getTextEditor().getSelectionProvider();
		if (provider instanceof IPostSelectionProvider) {
			((IPostSelectionProvider) provider).addPostSelectionChangedListener(fSourceEditorSelectionListener);
		}
		else {
			provider.addSelectionChangedListener(fSourceEditorSelectionListener);
		}
		
		fWSDLSelectionListener = new WSDLSelectionManagerSelectionListener();
		getSelectionManager().addSelectionChangedListener(fWSDLSelectionListener);
	}
	
	public void dispose() {
		if (resourceChangeHandler != null) {
			resourceChangeHandler.dispose();
		}
		getSelectionManager().removeSelectionChangedListener(fWSDLSelectionListener);
		super.dispose();
	}
	
	public void reloadDependencies() {
		try {
			Definition definition = (Definition) ((W11Description) getModel()).getTarget();
			if (definition != null) {
				WSDLResourceUtil.reloadDirectives(definition);
				ComponentReferenceUtil.updateBindingReferences(definition);
				ComponentReferenceUtil.updatePortTypeReferences(definition);
				ComponentReferenceUtil.updateMessageReferences(definition);
				ComponentReferenceUtil.updateSchemaReferences(definition);
				// the line below simply causes a notification in order to
				// update our
				// views
				//
				definition.setDocumentationElement(definition.getDocumentationElement());
			}
		}
		finally {
		}
	}

	protected void createActions() {
		super.createActions();

    ActionRegistry registry = getActionRegistry();
    BaseSelectionAction action;
    if (!isFileReadOnly()) {
	    action = new ASDAddMessageAction(this);
	    action.setSelectionProvider(getSelectionManager());
	    registry.registerAction(action);

	    action = new W11AddPartAction(this);
	    action.setSelectionProvider(getSelectionManager());
	    registry.registerAction(action);
	    
	    action = new W11SetNewMessageAction(this);
	    action.setSelectionProvider(getSelectionManager());
	    registry.registerAction(action);

	    action = new W11SetExistingMessageAction(this);
	    action.setSelectionProvider(getSelectionManager());
	    registry.registerAction(action);
	    
	    action = new W11SetNewTypeAction(this);
	    action.setSelectionProvider(getSelectionManager());
	    registry.registerAction(action);
	    
	    action = new W11SetExistingTypeAction(this);
	    action.setSelectionProvider(getSelectionManager());
	    registry.registerAction(action);
	    
	    action = new W11SetNewElementAction(this);
	    action.setSelectionProvider(getSelectionManager());
	    registry.registerAction(action);
	    
	    action = new W11SetExistingElementAction(this);
	    action.setSelectionProvider(getSelectionManager());
	    registry.registerAction(action);
	    
      ASDDirectEditAction directEditAction = new ASDDirectEditAction(this);
      directEditAction.setSelectionProvider(getSelectionManager());
      registry.registerAction(directEditAction);
    }
    action = new W11OpenImportAction(this);
    action.setSelectionProvider(getSelectionManager());
    registry.registerAction(action);

    action = new OpenInNewEditor(this);
    action.setSelectionProvider(getSelectionManager());
    registry.registerAction(action);
	}

  private static final String DEFAULT_EDITOR_MODE_ID = "org.eclipse.wst.wsdl.ui.defaultEditorModeId"; //$NON-NLS-1$
  
  protected EditorModeManager createEditorModeManager()
  {
    final ProductCustomizationProvider productCustomizationProvider = (ProductCustomizationProvider)getAdapter(ProductCustomizationProvider.class);
    EditorModeManager manager = new EditorModeManager(WSDL_EDITOR_MODE_EXTENSION_ID)
    {
      public void init()
      {
        if (productCustomizationProvider == null || 
            productCustomizationProvider.isEditorModeApplicable(DefaultEditorMode.ID))
        {          
          addMode(new DefaultEditorMode());
        }  
        super.init();
      }
      
      protected EditorMode getDefaultMode()
      {
        String defaultModeId = WSDLEditorPlugin.getInstance().getPreferenceStore().getString(DEFAULT_EDITOR_MODE_ID);
        if (defaultModeId != null)
        {
          EditorMode editorMode = getEditorMode(defaultModeId);
          if (editorMode != null)
          {
            return editorMode;
          }  
        }               
        return super.getDefaultMode();
      }      
    };
    manager.setProductCustomizationProvider(productCustomizationProvider);
    return manager;
  }
  
  public void editorModeChanged(EditorMode newEditorMode)
  {
    EditPartFactory editPartFactory = newEditorMode.getEditPartFactory();
    if (editPartFactory != null)
    {  
      graphicalViewer.setEditPartFactory(editPartFactory);
      if (graphicalViewer instanceof DesignViewGraphicalViewer)
      {  
        DesignViewGraphicalViewer viewer = (DesignViewGraphicalViewer)graphicalViewer;
        DefinitionsEditPart editPart = (DefinitionsEditPart)viewer.getRootEditPart().getContents();
        editPart.setModelChildren(Collections.EMPTY_LIST);
        editPart.refresh();
        editPart.setModelChildren(null);
        editPart.refresh();        
      }
    }  
    IContentProvider provider = newEditorMode.getOutlineProvider();
    if (provider != null)
    {
      ASDContentOutlinePage outline = (ASDContentOutlinePage)getContentOutlinePage();
      if (outline != null)
      {
        TreeViewer treeViewer = outline.getTreeViewer();
        if (treeViewer != null)
        {      
          outline.getTreeViewer().setContentProvider(provider);
          outline.getTreeViewer().refresh();
        }
      }  

    }  
  }
  
  protected void storeCurrentModePreference(String id)
  {
    WSDLEditorPlugin.getInstance().getPreferenceStore().setValue(DEFAULT_EDITOR_MODE_ID, id);    
  }
  
  protected void setInputToGraphicalViewer(IDocument newDocument)
  {
    createAndSetModel(newDocument);
    
    // Update the design page
    
    if (graphicalViewer != null)
    {
      graphicalViewer.setContents(model);
      graphicalViewer.getContents().refresh();
    }
    
    // Update outline page
    
    if (getContentOutlinePage() instanceof ASDContentOutlinePage)
    {
      ASDContentOutlinePage outline = (ASDContentOutlinePage)getContentOutlinePage();
      TreeViewer treeViewer = outline.getTreeViewer();
      if (treeViewer != null)
      {
        outline.setModel(model);
        treeViewer.setInput(model);
        treeViewer.refresh();
      } 
    }
  }

  public void doSaveAs()
  {
    // When performing a save as, the document changes.   Our model state listeners should listen
    // to the new document.

    // First get the current (soon to be old) document
    IDocument oldDocument = getDocument();
    WSDLModelAdapter oldAdapter = null;
    IDOMDocument doc = null;
    if (oldDocument != null)
    {
      IStructuredModel structuredModel = StructuredModelManager.getModelManager().getExistingModelForRead(oldDocument);
      try
      {
        if (structuredModel instanceof IDOMModel)
        {
          // Get the associated IDOMDocument model
          doc = ((IDOMModel) structuredModel).getDocument();
          // and now get our adapter that listens to DOM changes
          if (doc != null)
          {
            oldAdapter = (WSDLModelAdapter) doc.getExistingAdapter(WSDLModelAdapter.class);
            // Assert.isTrue(currentAdapter == modelAdapter);
          }
        }
      }
      finally
      {
        if (structuredModel != null)
          structuredModel.releaseFromRead();
      }
    }

    IEditorInput oldEditorInput = structuredTextEditor.getEditorInput();

    // perform save as
    structuredTextEditor.doSaveAs();
    
    IEditorInput newEditorInput = structuredTextEditor.getEditorInput();

    // if saveAs cancelled then don't setInput because the input hasn't change
    // See AbstractDecoratedTextEditor's performSaveAs
    if (oldEditorInput != newEditorInput)
    {
      setInput(newEditorInput);
      setPartName(newEditorInput.getName());

      getCommandStack().markSaveLocation();

      // Now do the clean up on the old document
      if (oldAdapter != null)
      {
        // clear out model adapter
        oldAdapter.clear();
        oldAdapter = null;
      }
    }
  }
  
  public void propertyChanged(Object source, int propId)
  {
    switch (propId)
    {
      // when refactor rename while file is open in editor, need to reset
      // editor contents to reflect new document
      case IEditorPart.PROP_INPUT:
      {
        Definition definition = (Definition)getAdapter(Definition.class);        
        if (source == structuredTextEditor && definition != null)
        {
          IStructuredModel structuredModel = StructuredModelManager.getModelManager().getExistingModelForRead(getDocument());
          try
          {
            if (structuredModel instanceof IDOMModel)
            {
              Document definitionDocument = definition.getDocument();
              Document domModelDocument = ((IDOMModel)structuredModel).getDocument();
              // if dom documents are not the same, they need to be reset
              if (definitionDocument != domModelDocument)
              {
                WSDLModelAdapter oldModelAdapter = null;
                if (definitionDocument instanceof IDOMDocument)
                {
                  // save this model adapter for cleanup later
                  oldModelAdapter = (WSDLModelAdapter) ((IDOMDocument)definitionDocument).getExistingAdapter(WSDLModelAdapter.class);
                }

                // update multipage editor with new editor input
                IEditorInput editorInput = structuredTextEditor.getEditorInput();
                setInput(editorInput);
                setPartName(editorInput.getName());
                getCommandStack().markSaveLocation();

                // Now do the clean up model adapter
                if (oldModelAdapter != null)
                {
                  oldModelAdapter.clear();
                  oldModelAdapter = null;
                }
              }
            }
          }
          finally
          {
            if (structuredModel != null)
              structuredModel.releaseFromRead();
          }
        }
        break;
      }
    }
    super.propertyChanged(source, propId);
  }
}
