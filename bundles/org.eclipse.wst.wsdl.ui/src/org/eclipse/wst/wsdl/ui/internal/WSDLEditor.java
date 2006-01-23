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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IPostSelectionProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.INavigationLocation;
import org.eclipse.ui.INavigationLocationProvider;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.TextSelectionNavigationLocation;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.wst.common.ui.properties.internal.provisional.ITabbedPropertySheetPageContributor;
import org.eclipse.wst.sse.core.internal.provisional.INodeNotifier;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.eclipse.wst.sse.ui.internal.contentoutline.ConfigurableContentOutlinePage;
import org.eclipse.wst.wsdl.Binding;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.XSDSchemaExtensibilityElement;
import org.eclipse.wst.wsdl.internal.generator.BindingGenerator;
import org.eclipse.wst.wsdl.ui.internal.actions.WSDLMenuActionContributor;
import org.eclipse.wst.wsdl.ui.internal.dialogs.GenerateBindingOnSaveDialog;
import org.eclipse.wst.wsdl.ui.internal.extension.WSDLEditorExtension;
import org.eclipse.wst.wsdl.ui.internal.graph.WSDLGraphViewer;
import org.eclipse.wst.wsdl.ui.internal.graph.editparts.WSDLEditPartFactory;
import org.eclipse.wst.wsdl.ui.internal.model.WSDLGroupObject;
import org.eclipse.wst.wsdl.ui.internal.model.WSDLModelAdapterFactory;
import org.eclipse.wst.wsdl.ui.internal.outline.ModelAdapterContentProvider;
import org.eclipse.wst.wsdl.ui.internal.outline.ModelAdapterLabelProvider;
import org.eclipse.wst.wsdl.ui.internal.properties.section.WSDLTabbedPropertySheetPage;
import org.eclipse.wst.wsdl.ui.internal.text.WSDLModelAdapter;
import org.eclipse.wst.wsdl.ui.internal.util.ComponentReferenceUtil;
import org.eclipse.wst.wsdl.ui.internal.util.WSDLEditorUtil;
import org.eclipse.wst.wsdl.ui.internal.util.WSDLNodeAssociationProvider;
import org.eclipse.wst.wsdl.ui.internal.util.WSDLResourceUtil;
import org.eclipse.wst.wsdl.ui.internal.viewers.WSDLDetailsViewer;
import org.eclipse.wst.wsdl.ui.internal.viewers.WSDLDetailsViewerProvider;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

// public class WSDLEditor extends StructuredTextMultiPageEditorPart
// implements
// INavigationLocationProvider
public class WSDLEditor extends WSDLMultiPageEditorPart implements INavigationLocationProvider, ITabbedPropertySheetPageContributor {
	protected StructuredTextEditor textEditor;
	protected WSDLGraphViewer graphViewer;
	protected WSDLDetailsViewer detailsViewer;
	protected WSDLSelectionManager selectionManager;
	protected SashForm sashForm;
	int graphPageIndex;
	protected WSDLModelAdapter modelAdapter;
	protected WSDLEditorResourceChangeHandler resourceChangeHandler;
	// Used for Cut, Copy, Paste actions. This acts as a copy, cut, paste
	// clipboard
	protected WSDLElement clipboardElement;
	private IPropertySheetPage fPropertySheetPage;
	private IContentOutlinePage fContentOutlinePage;
	private SourceEditorSelectionListener fSourceEditorSelectionListener;
	private WSDLSelectionManagerSelectionListener fWSDLSelectionListener;

	/**
	 * Listener on SSE's outline page's selections that converts DOM
	 * selections into wsdl selections and notifies WSDL selection manager
	 */
	class OutlineTreeSelectionChangeListener implements ISelectionChangedListener, IDoubleClickListener {
		private ISelectionProvider fProvider = null;

		public OutlineTreeSelectionChangeListener() {
			super();
		}
		
		void connect(IContentOutlinePage provider) {
			fProvider = provider;
			fProvider.addSelectionChangedListener(OutlineTreeSelectionChangeListener.this);
			if (provider instanceof ConfigurableContentOutlinePage) {
				((ConfigurableContentOutlinePage) provider).addDoubleClickListener(OutlineTreeSelectionChangeListener.this);
			}
		}
		
		void disconnect() {
			fProvider.removeSelectionChangedListener(OutlineTreeSelectionChangeListener.this);
			if (fProvider instanceof ConfigurableContentOutlinePage) {
				((ConfigurableContentOutlinePage) fProvider).removeDoubleClickListener(OutlineTreeSelectionChangeListener.this);
			}
			fProvider = null;
		}

		private ISelection getWSDLSelection(ISelection selection) {
			ISelection sel = null;
			if (selection instanceof IStructuredSelection) {
				IStructuredSelection structuredSelection = (IStructuredSelection) selection;
				Object o = structuredSelection.getFirstElement();

				// TODO ...
				// we need to implement a selectionManagerMapping
				// extension point
				// so that extensions can specify how they'd like to map
				// view objects
				// to selection objects
				//                                        
				// if (o instanceof Element)
				// {
				// try
				// {
				// Object modelObject =
				// WSDLEditorUtil.getInstance().findModelObjectForElement(wsdlEditor.getDefinition(),
				// (Element)o);
				// if (modelObject != null && !(modelObject instanceof
				// UnknownExtensibilityElement))
				// {
				// o = modelObject;
				// }
				// }
				// catch (Exception e)
				// {
				// }
				// }
				if (o != null)
					sel = new StructuredSelection(o);
			}
			return sel;
		}

		public void doubleClick(DoubleClickEvent event) {
			/*
			 * Selection in outline tree changed so set outline tree's
			 * selection into editor's selection and say it came from outline
			 * tree
			 */
			if (getSelectionManager() != null && getSelectionManager().enableNotify) {
				ISelection selection = getWSDLSelection(event.getSelection());
				if (selection != null) {
					getSelectionManager().setSelection(selection, fProvider);
				}

				if(getTextEditor() != null && selection instanceof IStructuredSelection) {
					int start = -1;
					int length = 0;
					Object o = ((IStructuredSelection)selection).getFirstElement();
					if (o != null)
						o = WSDLEditorUtil.getInstance().getNodeForObject(o);
					if (o instanceof IndexedRegion) {
						start = ((IndexedRegion) o).getStartOffset();
						length = ((IndexedRegion) o).getEndOffset() - start;
					}
					if(start > -1) {
						getTextEditor().selectAndReveal(start, length);
					}
				}
			}
		}

		public void selectionChanged(SelectionChangedEvent event) {
			/*
			 * Selection in outline tree changed so set outline tree's
			 * selection into editor's selection and say it came from outline
			 * tree
			 */
			if (getSelectionManager() != null && getSelectionManager().enableNotify) {
				ISelection selection = getWSDLSelection(event.getSelection());
				if (selection != null) {
					getSelectionManager().setSelection(selection, fProvider);
				}
			}
		}
	}
	private OutlineTreeSelectionChangeListener fOutlineTreeListener = null;

	/**
	 * Listener on SSE's source editor's selections that converts DOM
	 * selections into wsdl selections and notifies WSDL selection manager
	 */
	private class SourceEditorSelectionListener implements ISelectionChangedListener {
		/**
		 * Determines WSDL node based on object (DOM node)
		 * 
		 * @param object
		 * @return
		 */
		private Object getWSDLNode(Object object) {
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
				Object modelObject = WSDLEditorUtil.getInstance().findModelObjectForElement(getDefinition(), element);
				if (modelObject != null) {
					o = modelObject;
				}
			}
			return o;
		}

		public void selectionChanged(SelectionChangedEvent event) {
			ISelection selection = event.getSelection();
			if (selection instanceof IStructuredSelection) {
				List wsdlSelections = new ArrayList();
				for (Iterator i = ((IStructuredSelection) selection).iterator(); i.hasNext();) {
					Object domNode = i.next();
					Object wsdlNode = getWSDLNode(domNode);
					if (wsdlNode != null) {
						wsdlSelections.add(wsdlNode);
					}
				}

				if (!wsdlSelections.isEmpty()) {
					StructuredSelection wsdlSelection = new StructuredSelection(wsdlSelections);
					getSelectionManager().setSelection(wsdlSelection, getTextEditor().getSelectionProvider());
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
			// do not fire selection in source editor if selection event came
			// from source editor
			if (event.getSource() != getTextEditor().getSelectionProvider()) {
				ISelection selection = event.getSelection();
				if (selection instanceof IStructuredSelection) {
					List otherModelObjectList = new ArrayList();
					for (Iterator i = ((IStructuredSelection) selection).iterator(); i.hasNext();) {
						Object modelObject = i.next();
						Object otherModelObject = getObjectForOtherModel(modelObject);
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

	public WSDLEditor() {
		selectionManager = new WSDLSelectionManager();
	}

	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		super.init(site, input);
		try {
			IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
			IWorkbenchPage page = window.getActivePage();
			if (page != null) {
				page.showView("org.eclipse.ui.views.PropertySheet");
			}
		}
		catch (PartInitException partInitException) {
		}
		catch (Exception exception) {
		}
	}

	public Object getAdapter(Class key) {
		Object result = null;
		if (key == ISelectionProvider.class) {
			result = selectionManager;
		}
		else if (IPropertySheetPage.class.equals(key)) {
			if (fPropertySheetPage == null || fPropertySheetPage.getControl() == null || fPropertySheetPage.getControl().isDisposed()) {
				fPropertySheetPage = new WSDLTabbedPropertySheetPage(this, this);
				((WSDLTabbedPropertySheetPage) fPropertySheetPage).setSelectionManager(getSelectionManager());

			}
			return fPropertySheetPage;
		}
		else if (IContentOutlinePage.class.equals(key)) {
			if (fContentOutlinePage == null || fContentOutlinePage.getControl() == null || fContentOutlinePage.getControl().isDisposed()) {
				fContentOutlinePage = (IContentOutlinePage) super.getAdapter(key);
				if (fContentOutlinePage != null) {
					fOutlineTreeListener = new OutlineTreeSelectionChangeListener();
					fOutlineTreeListener.connect(fContentOutlinePage);
				}
			}
			result = fContentOutlinePage;
		}
		else {
			result = super.getAdapter(key);
		}
		return result;
	}

	public void dispose() {
		// call the extensibleOutlineProvider's inputChanged method a null
		// viewer
		// so that the outline's contentprovider/adapters don't attempt to
		// update
		// the viewer
		// after the editor closes
		// extensibleOutlineProvider.inputChanged(null, null, null);
		if (resourceChangeHandler != null) {
			resourceChangeHandler.dispose();
		}

		ISelectionProvider provider = getTextEditor().getSelectionProvider();
		if (provider instanceof IPostSelectionProvider) {
			((IPostSelectionProvider) provider).removePostSelectionChangedListener(fSourceEditorSelectionListener);
		}
		else {
			provider.removeSelectionChangedListener(fSourceEditorSelectionListener);
		}
		if (fOutlineTreeListener != null) {
			fOutlineTreeListener.disconnect();
			fOutlineTreeListener = null;
		}
		getSelectionManager().removeSelectionChangedListener(fWSDLSelectionListener);
		super.dispose();
	}

	public WSDLSelectionManager getSelectionManager() {
		return selectionManager;
	}

	public WSDLGraphViewer getGraphViewer() {
		return graphViewer;
	}

	public IStructuredModel getStructuredModel() {
		return textEditor.getModel();
		// IDocument doc =
		// textEditor.getDocumentProvider().getDocument(getEditorInput());
		// IModelManager modelManager = ModelManagerImpl.getInstance();
		// return modelManager.getModelForRead((IStructuredDocument) doc);
	}

	public Document getXMLDocument() {
		return ((IDOMModel) getStructuredModel()).getDocument();
	}

	public Definition getDefinition() {
		return modelAdapter != null ? modelAdapter.getDefinition() : null;
	}

	/**
	 * Creates the pages of this multi-page editor.
	 * <p>
	 * Subclasses of <code>MultiPageEditor</code> must implement this
	 * method.
	 * </p>
	 */
	protected void createPages() {
		try {
			if (resourceChangeHandler == null) {
				resourceChangeHandler = new WSDLEditorResourceChangeHandler(this);
				resourceChangeHandler.attach();
			}
			createSourcePage();
			addSourcePage();
			// create the wsdl model
			//
			lookupOrCreateWSDLModel();
			createAndAddGraphPage();

			// get the type of page and set the active page to show
			int pageIndexToShow = getDefaultPageIndex();
			setActivePage(pageIndexToShow);
			Definition definition = getDefinition();
            if (definition != null) {
                getSelectionManager().setSelection(new StructuredSelection(definition));
            } else {
              getSelectionManager().setSelection(new StructuredSelection());
            }

			// added selection listeners after setting selection to avoid
			// navigation exception
			ISelectionProvider provider = getTextEditor().getSelectionProvider();
			fSourceEditorSelectionListener = new SourceEditorSelectionListener();
			if (provider instanceof IPostSelectionProvider) {
				((IPostSelectionProvider) provider).addPostSelectionChangedListener(fSourceEditorSelectionListener);
			}
			else {
				provider.addSelectionChangedListener(fSourceEditorSelectionListener);
			}
			fWSDLSelectionListener = new WSDLSelectionManagerSelectionListener();
			getSelectionManager().addSelectionChangedListener(fWSDLSelectionListener);
		}
		catch (PartInitException e) {
			// log for now, unless we find reason not to
			Logger.log(Logger.INFO, e.getMessage());
		}
		// TODO: add a catch block here for any exception the design page
		// throws and
		// convert it into a more informative message.
	}

	protected void lookupOrCreateWSDLModel() {
		try {
			Document document = ((IDOMModel) getModel()).getDocument();
			if (document instanceof INodeNotifier) {
				INodeNotifier notifier = (INodeNotifier) document;
				modelAdapter = (WSDLModelAdapter) notifier.getAdapterFor(WSDLModelAdapter.class);
				if (modelAdapter == null) {
					modelAdapter = new WSDLModelAdapter();
					notifier.addAdapter(modelAdapter);
                    modelAdapter.createDefinition(document.getDocumentElement(), document);
				}
			}
		}
		catch (Exception e) {
		}
	}

	protected int getDefaultPageIndex() {
		if (WSDLEditorPlugin.getInstance().getDefaultPage().equals(WSDLEditorPlugin.GRAPH_PAGE)) {
			if (graphPageIndex != -1) {
				return graphPageIndex;
			}
		}
		return sourcePageIndex;
	}

	/**
	 * @see org.eclipse.wst.wsdl.ui.WSDLMultiPageEditorPart#createTextEditor()
	 */
	protected StructuredTextEditor createTextEditor() {
		textEditor = new StructuredTextEditor();
		return textEditor;
	}

	/**
	 * create our own
	 */
	protected void createSourcePage() throws PartInitException {
		super.createSourcePage();
		textEditor = getTextEditor();
	}

	int sourcePageIndex = -1;

	/**
	 * Adds the source page of the multi-page editor.
	 */
	protected void addSourcePage() throws PartInitException {
		sourcePageIndex = addPage(textEditor, getEditorInput());
		setPageText(sourcePageIndex, WSDLEditorPlugin.getWSDLString("_UI_TAB_SOURCE"));
		// the update's critical, to get viewer selection manager and
		// highlighting to
		// work
		textEditor.update();
	}

	int[] weights;

	public void setDesignWeights(int[] weights, boolean updateSourceDesign) {
		this.weights = weights;
		if (updateSourceDesign) {
			sashForm.setWeights(weights);
		}
	}

	protected void pageChange(int arg) {
		super.pageChange(arg);
		if (getPageText(arg).equals(WSDLEditorPlugin.getWSDLString("_UI_TAB_SOURCE"))) // TRANSLATE
		// !
		{
			// update the input
		}
		else if (getPageText(arg).equals(WSDLEditorPlugin.getWSDLString("_UI_TAB_GRAPH"))) // TRANSLATE
		// !
		{
			// update the input
		}
	}

	static private Color dividerColor;

	/**
	 * Creates the graph page and adds it to the multi-page editor.
	 */
	protected void createAndAddGraphPage() throws PartInitException {
		// create the graph page
		sashForm = new SashForm(getContainer(), SWT.BORDER);
		sashForm.setLayoutData(new GridData(GridData.FILL_BOTH));
		sashForm.setOrientation(SWT.VERTICAL);
		graphPageIndex = addPage(sashForm);
		setPageText(graphPageIndex, WSDLEditorPlugin.getWSDLString("_UI_TAB_GRAPH"));
		// create the graph viewer
		graphViewer = new WSDLGraphViewer(this);
		graphViewer.createControl(sashForm);
		// detailsViewer = new WSDLDetailsViewer(this);
		// detailsViewer.createControl(sashForm);
		//
		// sashForm.setWeights(weights);
		if (dividerColor == null) {
			dividerColor = new Color(getContainer().getDisplay(), 143, 141, 138);
		}
		getContainer().addPaintListener(new PaintListener() {
			/**
			 * @see org.eclipse.swt.events.PaintListener#paintControl(PaintEvent)
			 */
			public void paintControl(PaintEvent e) {
				Object source = e.getSource();
				if (source instanceof Composite) {
					Composite comp = (Composite) source;
					Rectangle boundary = comp.getClientArea();
					e.gc.setForeground(dividerColor);
					e.gc.drawLine(boundary.x, boundary.y, boundary.x + boundary.width, boundary.y);
					setDesignWeights(sashForm.getWeights(), true);
				}
			}
		});
	}

	public void setFocus() {
		super.setFocus();
		int activePage = getActivePage();
		if (activePage == sourcePageIndex) {
			WSDLEditorPlugin.getInstance().setDefaultPage(WSDLEditorPlugin.SOURCE_PAGE);
		}
		else {
			WSDLEditorPlugin.getInstance().setDefaultPage(WSDLEditorPlugin.GRAPH_PAGE);
		}
	}

	//
	//
	public static class BuiltInWSDLEditorExtension implements WSDLEditorExtension {
		public boolean isExtensionTypeSupported(int type) {
			return type == OUTLINE_TREE_CONTENT_PROVIDER || type == OUTLINE_LABEL_PROVIDER || type == EDIT_PART_FACTORY || type == DETAILS_VIEWER_PROVIDER || type == MENU_ACTION_CONTRIBUTOR || type == NODE_RECONCILER || type == NODE_ASSOCIATION_PROVIDER;
		}

		public boolean isApplicable(Object object) {
			return (object instanceof WSDLElement && !(object instanceof XSDSchemaExtensibilityElement)) || (object instanceof WSDLGroupObject);
		}

		public Object createExtensionObject(int type, WSDLEditor wsdlEditor) {
			Object result = null;
			switch (type) {
				case OUTLINE_TREE_CONTENT_PROVIDER : {
					result = new ModelAdapterContentProvider(WSDLModelAdapterFactory.getWSDLModelAdapterFactory());
					break;
				}
				case OUTLINE_LABEL_PROVIDER : {
					result = new ModelAdapterLabelProvider(WSDLModelAdapterFactory.getWSDLModelAdapterFactory());
					break;
				}
				case DETAILS_VIEWER_PROVIDER : {
					result = new WSDLDetailsViewerProvider();
					break;
				}
				case MENU_ACTION_CONTRIBUTOR : {
					result = new WSDLMenuActionContributor(wsdlEditor);
					break;
				}
				case NODE_ASSOCIATION_PROVIDER : {
					result = new WSDLNodeAssociationProvider();
					break;
				}
				case EDIT_PART_FACTORY : {
					result = new WSDLEditPartFactory();
					break;
				}
			}
			return result;
		}
	}

	public void reloadDependencies() {
		try {
			getGraphViewer().getComponentViewer().setPreserveExpansionEnabled(true);
			Definition definition = getDefinition();
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
			getGraphViewer().getComponentViewer().setPreserveExpansionEnabled(false);
		}
	}

	public void openOnSelection(String specification) {
		EObject eObject = getDefinition().eResource().getEObject(specification);
		if (eObject != null) {
			getSelectionManager().setSelection(new StructuredSelection(eObject));
		}
	}

	public INavigationLocation createEmptyNavigationLocation() {
		return new InternalTextSelectionNavigationLocation(textEditor, false);
	}

	public INavigationLocation createNavigationLocation() {
		return new InternalTextSelectionNavigationLocation(textEditor, true);
	}

	static class InternalTextSelectionNavigationLocation extends TextSelectionNavigationLocation {
		public InternalTextSelectionNavigationLocation(ITextEditor part, boolean initialize) {
			super(part, initialize);
		}

		protected IEditorPart getEditorPart() {
			IEditorPart part = super.getEditorPart();
			if (part instanceof WSDLEditor) {
				part = ((WSDLEditor) part).getTextEditor();
			}
			return part;
		}

		public String getText() {
			// ISSUE: how to get title?
			// IEditorPart part = getEditorPart();
			// if (part instanceof WSDLTextEditor) {
			// return ((WSDLTextEditor) part).getWSDLEditor().getTitle();
			// }
			// else {
			// return super.getText();
			// }
			return super.getText();
		}
	}

	// Returns the element currently on the copy, cut, paste clipboard
	public WSDLElement getClipboardContents() {
		return clipboardElement;
	}

	public void setClipboardContents(WSDLElement element) {
		clipboardElement = element;
	}

	/**
	 * @see org.eclipse.wst.common.ui.properties.internal.provisional.ITabbedPropertySheetPageContributor#getContributorId()
	 */
	public String getContributorId() {
		return "org.eclipse.wst.wsdl.ui.internal.WSDLEditor";
		// return getSite().getId();
	}

	/*
	 * We override this method so we can hook in our automatic Binding
	 * generation. We will generate the Binding after a save is executed (If
	 * this preference has been set to true).
	 */
	public void doSave(IProgressMonitor monitor) {
		try {
			// Display prompt message
			boolean continueRegeneration = false;
			if (WSDLEditorPlugin.getInstance().getPluginPreferences().getBoolean("Prompt Regenerate Binding on save")) {
				Shell shell = Display.getCurrent().getActiveShell();
				GenerateBindingOnSaveDialog dialog = new GenerateBindingOnSaveDialog(shell);

				int rValue = dialog.open();
				if (rValue == SWT.YES) {
					continueRegeneration = true;
				}
				else if (rValue == SWT.NO) {
					continueRegeneration = false;
				}
				else if (rValue == SWT.CANCEL) {
					return;
				}
				else {
					System.out.println("\nNothing: " + rValue);
				}
			}
			else {
				continueRegeneration = WSDLEditorPlugin.getInstance().getPluginPreferences().getBoolean(WSDLEditorPlugin.getWSDLString("_UI_PREF_PAGE_AUTO_REGENERATE_BINDING"));
			}

			if (continueRegeneration) {
				Iterator bindingsIt = getDefinition().getEBindings().iterator();
				while (bindingsIt.hasNext()) {
					Binding binding = (Binding) bindingsIt.next();
					BindingGenerator generator = new BindingGenerator(binding.getEnclosingDefinition(), binding);
					generator.setOverwrite(false);
					generator.generateBinding();
				}

				// Little hack to 'redraw' connecting lines in the graph
				// viewer
				getDefinition().setQName(getDefinition().getQName());
			}
		}
		catch (Exception e) {
			// e.printStackTrace();
		}
		super.doSave(monitor);
	}
}
