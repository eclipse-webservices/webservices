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

import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.wst.common.ui.properties.internal.provisional.ITabbedPropertySheetPageContributor;
import org.eclipse.wst.sse.core.internal.model.ModelManagerImpl;
import org.eclipse.wst.sse.core.internal.provisional.IModelManager;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocument;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.eclipse.wst.sse.ui.internal.view.events.INodeSelectionListener;
import org.eclipse.wst.sse.ui.internal.view.events.NodeSelectionChangedEvent;
import org.eclipse.wst.wsdl.Binding;
import org.eclipse.wst.wsdl.internal.generator.BindingGenerator;
import org.eclipse.wst.wsdl.ui.internal.dialogs.GenerateBindingOnSaveDialog;
import org.eclipse.wst.wsdl.ui.internal.outline.WSDLContentOutlinePage;
import org.eclipse.wst.wsdl.ui.internal.properties.section.WSDLTabbedPropertySheetPage;
import org.eclipse.wst.wsdl.ui.internal.util.OpenOnSelectionHelper;
import org.eclipse.wst.wsdl.ui.internal.util.SelectionAdapter;
import org.eclipse.wst.wsdl.ui.internal.util.WSDLEditorUtil;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


public class WSDLTextEditor extends StructuredTextEditor implements INodeSelectionListener, ISelectionChangedListener, ITabbedPropertySheetPageContributor
{
  protected WSDLEditor wsdlEditor;
  protected WSDLContentOutlinePage outlinePage;
  protected WSDLSelectionManager wsdlSelectionManager;
  protected InternalSelectionProvider internalSelectionProvider = new InternalSelectionProvider();
  private IPropertySheetPage fPropertySheetPage;

  public WSDLTextEditor(WSDLEditor wsdlEditor)
  {
    this.wsdlEditor = wsdlEditor;
    wsdlSelectionManager = wsdlEditor.getSelectionManager();
    wsdlSelectionManager.addSelectionChangedListener(this);
  }

  public void createPartControl(Composite arg0)
  {
    super.createPartControl(arg0);
    addOpenOnSelectionListener();
  }

  protected void addOpenOnSelectionListener()
  {
    KeyAdapter keyAdapter = new KeyAdapter()
    {
      public void keyReleased(KeyEvent arg0)
      {
        if (arg0.keyCode == SWT.F3)
        {
          List list = getViewerSelectionManager().getSelectedNodes();
          if (list.size() > 0)
          {
            Object object = list.get(0);
            if (object instanceof Node)
            {
              OpenOnSelectionHelper helper = new OpenOnSelectionHelper(wsdlEditor.getDefinition());   
              helper.openEditor((Node)object);
            }
          }
        }
      }
    };
    getTextViewer().getTextWidget().addKeyListener(keyAdapter);
  }

   
  public Object getAdapter(Class required)
  {
		if (IContentOutlinePage.class.equals(required))
		{
		  return getContentOutlinePage();
		}    
		if (IPropertySheetPage.class.equals(required))
		{
			if (fPropertySheetPage == null || fPropertySheetPage.getControl() == null || fPropertySheetPage.getControl().isDisposed())
			{
//			  System.out.println("Create WSDL Property Sheet");
//        PropertySheetConfiguration cfg = createPropertySheetConfiguration();
//        if (cfg != null)
//        {
//            if (cfg instanceof StructuredPropertySheetConfiguration)
//            {
//                ((StructuredPropertySheetConfiguration) cfg).setEditor(this);
//            }
//            ConfigurablePropertySheetPage propertySheetPage = new ConfigurablePropertySheetPage();
//            propertySheetPage.setConfiguration(cfg);
//            propertySheetPage.setModel(getModel());
//            fPropertySheetPage = propertySheetPage;
//        }

//  	    fPropertySheetPage = new WSDLPropertySheetPage(getModel(), getEditorPart());
//  	    ((WSDLPropertySheetPage) fPropertySheetPage).setSelectionManager(getWSDLEditor().getSelectionManager()); //getViewerSelectionManager());
////	  	  ((WSDLPropertySheetPage) fPropertySheetPage).setPropertySourceProvider((WSDLPropertySheetPage) fPropertySheetPage);
//  	    ((WSDLPropertySheetPage)fPropertySheetPage).setPropertySourceProvider(new ExtensiblePropertySourceProvider(getWSDLEditor()));
////  	    getWSDLEditor().getSelectionManager().addSelectionChangedListener((WSDLPropertySheetPage)fPropertySheetPage);

  	    fPropertySheetPage = new WSDLTabbedPropertySheetPage(this, getWSDLEditor());
  	    ((WSDLTabbedPropertySheetPage)fPropertySheetPage).setSelectionManager(getWSDLEditor().getSelectionManager());

			}
			return fPropertySheetPage;
		}
	
		return super.getAdapter(required);
  }

  public String[] getPropertyCategories()
  {
    return new String[] { "general", "namespace", "other", "attributes", "documentation", "facets" };
  }

	/**
	 * @see org.eclipse.wst.common.ui.properties.internal.provisional.ITabbedPropertySheetPageContributor#getContributorId()
	 */
	public String getContributorId()
	{
    return "org.eclipse.wst.wsdl.ui.internal.WSDLTextEditor";
	//	 return getSite().getId();
  }
  
  /*
   * @see StructuredTextEditor#getContentOutlinePage()
   */
  public IContentOutlinePage getContentOutlinePage()
  {
    if ((outlinePage == null) || outlinePage.getControl() == null || (outlinePage.getControl().isDisposed()))
    {
      outlinePage = new WSDLContentOutlinePage(wsdlEditor);
      outlinePage.setContentProvider(wsdlEditor.getExtensibleOutlineProvider());
      outlinePage.setLabelProvider(wsdlEditor.getExtensibleOutlineProvider());
      outlinePage.setModel(wsdlEditor.getDefinition()); //XMLDocument());

      getViewerSelectionManager().addNodeSelectionListener(this);
      internalSelectionProvider.addSelectionChangedListener(getViewerSelectionManager());
      internalSelectionProvider.setEventSource(outlinePage);
    }
    return outlinePage;
  }

  public WSDLEditor getWSDLEditor()
  {
    return (WSDLEditor)getEditorPart();
  }

  // used to map selections from the outline view to the source view
  // this class thinks of selections in terms of DOM element
  class InternalSelectionProvider extends SelectionAdapter
  {
    protected Object getObjectForOtherModel(Object object)
    {
      Node node = null;

      if (object instanceof Node)
      {
        node = (Node)object;
      }
      else
      {
        node = WSDLEditorUtil.getInstance().getNodeForObject(object);
      }

      // the text editor can only accept sed nodes!
      //
      if (!(node instanceof IDOMNode))
      {
        node = null;
      }
      return node;
    }
  }

  public void selectionChanged(SelectionChangedEvent event)
  {
    // here we convert the model selection to a node selection req'd for the source view
    //
    internalSelectionProvider.setSelection(event.getSelection());
  }

  public void nodeSelectionChanged(NodeSelectionChangedEvent event)
  {
    // here we convert an node seleciton to a model selection as req'd by the other views
    //
    if (!event.getSource().equals(internalSelectionProvider))
    {
      Element element = null;
      List list = event.getSelectedNodes();
      for (Iterator i = list.iterator(); i.hasNext();)
      {
        Node node = (Node)i.next();
        if (node != null)
        {
	        if (node.getNodeType() == Node.ELEMENT_NODE)
	        {
	          element = (Element)node;
	          break;
	        }
	        else if (node.getNodeType() == Node.ATTRIBUTE_NODE)
	        {
	          element = ((Attr)node).getOwnerElement();
	          break;
	        }
        }
      }

      Object o = element;
      if (element != null)
      {
        Object modelObject = WSDLEditorUtil.getInstance().findModelObjectForElement(wsdlEditor.getDefinition(), element);
        if (modelObject != null)
        {
          o = modelObject;
        }
      }

      if (o != null)
      {
        wsdlSelectionManager.setSelection(new StructuredSelection(o), internalSelectionProvider);
      }
      else
      {
        wsdlSelectionManager.setSelection(new StructuredSelection(), internalSelectionProvider);
      }
    }
  }

  public void update()
  {
    super.update();
    if (outlinePage != null) {    	
    	IDocument doc = getDocumentProvider().getDocument(getEditorInput());	
    	IModelManager modelManager = ModelManagerImpl.getInstance();    	
    	outlinePage.setModel(modelManager.getModelForRead((IStructuredDocument) doc));
    }
  }
  
  /*
   * We override this method so we can hook in our automatic Binding generation.
   * We will generate the Binding after a save is executed (If this preference
   * has been set to true).
   */
  public void doSave(IProgressMonitor monitor) {
      try{
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
			  Iterator bindingsIt = wsdlEditor.getDefinition().getEBindings().iterator();
			  while (bindingsIt.hasNext()) {
				  Binding binding = (Binding) bindingsIt.next();
				  BindingGenerator generator = new BindingGenerator(binding.getEnclosingDefinition(), binding);
				  generator.setOverwrite(false);
				  generator.generateBinding();
			  }
			  
			  // Little hack to 'redraw' connecting lines in the graph viewer
			  wsdlEditor.getDefinition().setQName(wsdlEditor.getDefinition().getQName());
		  }
      }
      catch (Exception e)
      {
//    	  e.printStackTrace();
      }
	  super.doSave(monitor);
  }

  public InternalSelectionProvider getInternalSelectionProvider() {
	return internalSelectionProvider;
  }
}
