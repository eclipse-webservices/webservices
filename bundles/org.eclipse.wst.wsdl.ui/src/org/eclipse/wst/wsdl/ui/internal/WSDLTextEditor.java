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
import java.util.Vector;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.wst.common.ui.properties.internal.provisional.ITabbedPropertySheetPageContributor;
import org.eclipse.wst.sse.ui.internal.StructuredTextEditor;
import org.eclipse.wst.sse.ui.internal.view.events.INodeSelectionListener;
import org.eclipse.wst.sse.ui.internal.view.events.NodeSelectionChangedEvent;
import org.eclipse.wst.wsdl.util.WSDLConstants;
import org.eclipse.wst.wsdl.ui.internal.extension.IModelQueryContributor;
import org.eclipse.wst.wsdl.ui.internal.extension.WSDLEditorExtension;
import org.eclipse.wst.wsdl.ui.internal.filter.ExtensiblityElementFilter;
import org.eclipse.wst.wsdl.ui.internal.outline.WSDLContentOutlinePage;
import org.eclipse.wst.wsdl.ui.internal.properties.section.WSDLTabbedPropertySheetPage;
import org.eclipse.wst.wsdl.ui.internal.util.ComponentReferenceUtil;
import org.eclipse.wst.wsdl.ui.internal.util.OpenOnSelectionHelper;
import org.eclipse.wst.wsdl.ui.internal.util.SelectionAdapter;
import org.eclipse.wst.wsdl.ui.internal.util.WSDLEditorUtil;
import org.eclipse.wst.xml.core.document.IDOMModel;
import org.eclipse.wst.xml.core.internal.contentmodel.CMDocument;
import org.eclipse.wst.xml.core.internal.contentmodel.CMElementDeclaration;
import org.eclipse.wst.xml.core.internal.contentmodel.CMNode;
import org.eclipse.wst.xml.core.internal.contentmodel.modelquery.ModelQuery;
import org.eclipse.wst.xml.core.internal.contentmodel.modelquery.extension.DataTypeValueExtension;
import org.eclipse.wst.xml.core.internal.contentmodel.modelquery.extension.ElementContentFilterExtension;
import org.eclipse.wst.xml.core.internal.modelquery.ModelQueryUtil;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


public class WSDLTextEditor extends StructuredTextEditor implements INodeSelectionListener, ISelectionChangedListener, ITabbedPropertySheetPageContributor
{
  protected WSDLEditor wsdlEditor;
  protected WSDLContentOutlinePage outlinePage;
  protected WSDLSelectionManager wsdlSelectionManager;
  protected InternalSelectionProvider internalSelectionProvider = new InternalSelectionProvider();
  protected ModelQueryExtensionHelper modelQueryExtensionHelper;
  protected List modelQueryContributorList = new ArrayList();

  public WSDLTextEditor(WSDLEditor wsdlEditor)
  {
    this.wsdlEditor = wsdlEditor;
    wsdlSelectionManager = wsdlEditor.getSelectionManager();
    wsdlSelectionManager.addSelectionChangedListener(this);

    WSDLEditorExtension[] extensions = WSDLEditorPlugin.getInstance().getWSDLEditorExtensionRegistry().getRegisteredExtensions(WSDLEditorExtension.MODEL_QUERY_CONTRIBUTOR);
    for (int i = 0; i < extensions.length; i++)
    {
      Object o = extensions[i].createExtensionObject(WSDLEditorExtension.MODEL_QUERY_CONTRIBUTOR, wsdlEditor);
      if (o != null)
      {
        modelQueryContributorList.add(o);
      }
    }
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

 
  /*
   * @see StructuredTextEditor#setModel(IFileEditorInput)
   */
  public void setModel(IFileEditorInput input)
  {
    if (modelQueryExtensionHelper != null)
    {
      modelQueryExtensionHelper.dispose();
      modelQueryExtensionHelper = null;
    }

    super.setModel(input);

    for (Iterator i = modelQueryContributorList.iterator(); i.hasNext();)
    {
      IModelQueryContributor modelQueryContributor = (IModelQueryContributor)i.next();
      modelQueryContributor.setModel((IDOMModel)getModel());
    }

    // contribute the ModelQueryExtensionHelper as an extension too
    //
    modelQueryExtensionHelper = new ModelQueryExtensionHelper((IDOMModel)getModel());
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
      if (!(node instanceof org.eclipse.wst.xml.core.document.IDOMNode))
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
    if (outlinePage != null)
      outlinePage.setModel(getModel());
  }

  public class ModelQueryExtensionHelper
  {
    protected ModelQuery modelQuery;
    protected DataTypeValueExtension dataTypeValueExtension;
    protected ElementContentFilterExtension elementContentFilterExtension;

    public ModelQueryExtensionHelper(IDOMModel xmlModel)
    {
      dataTypeValueExtension = new WSDLDataTypeValueExtension();
      elementContentFilterExtension = new WSDLElementContentFilterExtension();

      modelQuery = ModelQueryUtil.getModelQuery(xmlModel.getDocument());
      if (modelQuery != null && modelQuery.getExtensionManager() != null)
      {
        modelQuery.getExtensionManager().addExtension(dataTypeValueExtension);
        modelQuery.getExtensionManager().addExtension(elementContentFilterExtension);
      }
    }

    protected void dispose()
    {
      if (modelQuery != null && modelQuery.getExtensionManager() != null)
      {
        modelQuery.getExtensionManager().removeExtension(dataTypeValueExtension);
        modelQuery.getExtensionManager().removeExtension(elementContentFilterExtension);
      }
    }
  }

  /**
   * This class is used to extend the ModelQuery behaviour so that we can contribute our own
   * 'allowed values' for attributes or elements (e.g. the 'type' attribute).
   */
  public class WSDLDataTypeValueExtension implements DataTypeValueExtension
  {
    public int getType()
    {
      return DATA_TYPE_VALUE_EXTENSION;
    }

    public String getId()
    {
      return "WSDLDataTypeValueExtension";
    }

    public java.util.List getDataTypeValues(Element element, CMNode cmNode)
    {
      java.util.List list = new Vector();
      if (cmNode.getNodeType() == CMNode.ATTRIBUTE_DECLARATION)
      {
        ComponentReferenceUtil util = new ComponentReferenceUtil(getWSDLEditor().getDefinition());
        String name = cmNode.getNodeName();
        String currentElementName = element.getLocalName();
        Node parentNode = element.getParentNode();
        String parentName = "";
        if (parentNode != null)
        {
          parentName = parentNode.getLocalName();
        }

        if (checkName(name, "message"))
        {
          list.addAll(util.getMessageNames());
        }
        else if (checkName(name, "binding"))
        {
          list.addAll(util.getBindingNames());
        }
        else if (checkName(name, "type"))
        {
          if (checkName(currentElementName, "binding"))
          {
            list.addAll(util.getPortTypeNames());
          }
          else if (checkName(currentElementName, "part"))
          {
            list.addAll(util.getComponentNameList(true));
          }
        }
        else if (checkName(name, "element"))
        {
          if (checkName(currentElementName, "part"))
          {
            list.addAll(util.getComponentNameList(false));
          }
        }
      }
      return list;
    }

    protected boolean checkName(String localName, String token)
    {
      if (localName != null && localName.trim().equals(token))
      {
        return true;
      }
      return false;
    }
  }

  /**
   * This class performs some filtering for some known extensiblity elements to enable
   * smarter suggestions than those provided by the 'dumb' wsdl schema 
   */
  public class WSDLElementContentFilterExtension implements ElementContentFilterExtension
  {
    public int getType()
    {
      return ELEMENT_CONTENT_FILTER;
    }

    public String getId()
    {
      return "WSDLElementContentFilterExtension";
    }

    protected boolean isParentElementMessageReference(String parentElementName)
    {
      return parentElementName.equals("input") || parentElementName.equals("output") || parentElementName.equals("fault");
    }

    protected boolean isCMNodeMessageReferenceContent(String cmNodeName)
    {
      return cmNodeName.equals("body") || cmNodeName.equals("header") || cmNodeName.equals("fault") || cmNodeName.equals("urlReplacement") || cmNodeName.equals("urlEncoded");
    }

    public void filterAvailableElementContent(List list, Element element, CMElementDeclaration ed)
    {
      String parentElementNamespaceURI = element.getNamespaceURI();
      String parentElementName = element.getLocalName();

      // only filter children for 'non-schema' elements
      //     	
      if (!WSDLConstants.XSD_NAMESPACE_URI.equals(parentElementNamespaceURI))
      {
        for (int i = list.size() - 1; i >= 0; i--)
        {
          boolean include = true;
          CMNode cmNode = (CMNode)list.get(i);
          String cmNodeName = cmNode.getNodeName();
          if (parentElementName != null && cmNodeName != null && cmNode.getNodeType() == CMNode.ELEMENT_DECLARATION)
          {
            CMDocument cmDocument = (CMDocument)cmNode.getProperty("CMDocument");
            if (cmDocument != null)
            {
              String namespaceURI = (String)cmDocument.getProperty("http://org.eclipse.wst/cm/properties/targetNamespaceURI");
              if (namespaceURI != null)
              {
                // TODO... provide a list of namespaces that should always get filtered out 
                //
                if (namespaceURI.equals("http://schemas.xmlsoap.org/soap/encoding/") || namespaceURI.equals(WSDLConstants.XSD_NAMESPACE_URI))
                {
                  // exclude soap-enc elements
                  //
                  include = false;
                }
                else
                {
                  ExtensiblityElementFilter filter = (ExtensiblityElementFilter)WSDLEditorPlugin.getInstance().getExtensiblityElementFilterRegistry().get(namespaceURI);
                  if (filter != null)
                  {
                    include = filter.isValidContext(element, cmNodeName);
                  }
                }
              }
            }
          }
          if (!include)
          {
            list.remove(i);
          }
        }
      }
    }
  }
  
  public InternalSelectionProvider getInternalSelectionProvider() {
	return internalSelectionProvider;
  }
}
