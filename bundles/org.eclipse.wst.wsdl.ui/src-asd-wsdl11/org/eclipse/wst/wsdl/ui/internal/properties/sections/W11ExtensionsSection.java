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
package org.eclipse.wst.wsdl.ui.internal.properties.sections;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.PageBook;
import org.eclipse.wst.wsdl.ExtensibleElement;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.adapters.WSDLBaseAdapter;
import org.eclipse.wst.wsdl.ui.internal.filter.ExtensiblityElementFilter;
import org.eclipse.wst.xsd.ui.common.commands.AddAppInfoCommand;
import org.eclipse.wst.xsd.ui.common.commands.AddAppInfoElementCommand;
import org.eclipse.wst.xsd.ui.common.commands.AddExtensibilityElementCommand;
import org.eclipse.wst.xsd.ui.common.commands.RemoveAppInfoElementCommand;
import org.eclipse.wst.xsd.ui.common.properties.sections.AbstractSection;
import org.eclipse.wst.xsd.ui.common.properties.sections.appinfo.AddExtensionsComponentDialog;
import org.eclipse.wst.xsd.ui.common.properties.sections.appinfo.ExtensionsSchemasRegistry;
import org.eclipse.wst.xsd.ui.common.properties.sections.appinfo.ExtensionsComponentTableTreeViewer;
import org.eclipse.wst.xsd.ui.common.properties.sections.appinfo.SpecificationForExtensionsSchema;
import org.eclipse.xsd.XSDAnnotation;
import org.eclipse.xsd.XSDConcreteComponent;
import org.eclipse.xsd.XSDElementDeclaration;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class W11ExtensionsSection extends AbstractSection
{
  protected ExtensionsComponentTableTreeViewer tableTree;
  protected TableViewer extensibilityElementsTable;
  protected Label extensibilityElementsLabel, contentLabel;
  protected ISelectionChangedListener elementSelectionChangedListener;

  Element selectedElement;
  private Text simpleText;
  private Composite page, pageBook1, pageBook2;
  private Button textRadioButton, structureRadioButton;
  private Button addButton, removeButton;
  private PageBook pageBook;
  private FilterForDialogOfAddButton filterForDialogOfAddButton =
	  new FilterForDialogOfAddButton();

  /**
   * 
   */
  public W11ExtensionsSection()
  {
    super();
  }

  public ExtensibleElement getInputExtensibleElement() {
	  ExtensibleElement eElement = null;
	  
	  if (input instanceof WSDLBaseAdapter && ((WSDLBaseAdapter) input).getTarget() instanceof ExtensibleElement) {
		  eElement = (ExtensibleElement) ((WSDLBaseAdapter) input).getTarget();
	  }

	  return eElement;
  }
  
  public void createContents(Composite parent)
  {
    composite = getWidgetFactory().createFlatFormComposite(parent);

    GridLayout gridLayout = new GridLayout();
    gridLayout.marginTop = 0;
    gridLayout.marginBottom = 0;
    gridLayout.numColumns = 1;
    composite.setLayout(gridLayout);

    GridData gridData = new GridData();

    page = getWidgetFactory().createComposite(composite);
    gridLayout = new GridLayout();
    gridLayout.marginTop = 0;
    gridLayout.marginBottom = 0;
    gridLayout.numColumns = 1;
    page.setLayout(gridLayout);

    gridData = new GridData();
    gridData.grabExcessHorizontalSpace = true;
    gridData.grabExcessVerticalSpace = true;
    gridData.verticalAlignment = GridData.FILL;
    gridData.horizontalAlignment = GridData.FILL;
    page.setLayoutData(gridData);

    pageBook = new PageBook(page, SWT.FLAT);
    gridData = new GridData();
    gridData.grabExcessHorizontalSpace = true;
    gridData.grabExcessVerticalSpace = true;
    gridData.verticalAlignment = GridData.FILL;
    gridData.horizontalAlignment = GridData.FILL;
    pageBook.setLayoutData(gridData);

    pageBook2 = getWidgetFactory().createComposite(pageBook, SWT.FLAT);

    gridLayout = new GridLayout();
    gridLayout.marginHeight = 2;
    gridLayout.marginWidth = 2;
    gridLayout.numColumns = 1;
    pageBook2.setLayout(gridLayout);

    gridData = new GridData();
    gridData.grabExcessHorizontalSpace = true;
    gridData.grabExcessVerticalSpace = true;
    gridData.verticalAlignment = GridData.FILL;
    gridData.horizontalAlignment = GridData.FILL;
    pageBook2.setLayoutData(gridData);

    SashForm sashForm = new SashForm(pageBook2, SWT.HORIZONTAL);
    gridData = new GridData();
    gridData.grabExcessHorizontalSpace = true;
    gridData.grabExcessVerticalSpace = true;
    gridData.verticalAlignment = GridData.FILL;
    gridData.horizontalAlignment = GridData.FILL;
    sashForm.setLayoutData(gridData);

    Composite leftContent = getWidgetFactory().createComposite(sashForm, SWT.FLAT);
    gridLayout = new GridLayout();
    gridLayout.numColumns = 1;
    leftContent.setLayout(gridLayout);

    extensibilityElementsLabel = getWidgetFactory().createLabel(leftContent, "Application Information Elements");
    extensibilityElementsTable = new TableViewer(leftContent, SWT.FLAT | SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.LINE_SOLID);
    gridLayout = new GridLayout();
    gridLayout.numColumns = 1;
    extensibilityElementsTable.getTable().setLayout(gridLayout);
    gridData = new GridData();
    gridData.grabExcessHorizontalSpace = true;
    gridData.grabExcessVerticalSpace = true;
    gridData.verticalAlignment = GridData.FILL;
    gridData.horizontalAlignment = GridData.FILL;
    extensibilityElementsTable.getTable().setLayoutData(gridData);
    extensibilityElementsTable.setContentProvider(new ElementTableContentProvider());
    extensibilityElementsTable.setLabelProvider(new ElementTableLabelProvider());
    elementSelectionChangedListener = new ElementSelectionChangedListener();
    extensibilityElementsTable.addSelectionChangedListener(elementSelectionChangedListener);
    extensibilityElementsTable.getTable().addMouseTrackListener(new MouseTrackAdapter()
    {
      public void mouseHover(org.eclipse.swt.events.MouseEvent e)
      {
        ISelection selection = extensibilityElementsTable.getSelection();
        if (selection instanceof StructuredSelection)
        {
          Object obj = ((StructuredSelection) selection).getFirstElement();
          if (obj instanceof Element)
          {
            Element element = (Element) obj;
            ExtensionsSchemasRegistry registry = WSDLEditorPlugin.getInstance().getExtensionsSchemasRegistry();
            List properties = registry.getAllExtensionsSchemasContribution();
            int length = properties.size();
            for (int i = 0; i < length; i++)
            {
              SpecificationForExtensionsSchema spec = (SpecificationForExtensionsSchema)properties.get(i);
              if (spec.getNamespaceURI().equals(element.getNamespaceURI()))
              {
                extensibilityElementsTable.getTable().setToolTipText(spec.getDescription());
                break;
              }
            }
          }
        }
      };

    });

    Composite rightContent = getWidgetFactory().createComposite(sashForm, SWT.FLAT);

    contentLabel = getWidgetFactory().createLabel(rightContent, "Content");

    Composite testComp = getWidgetFactory().createComposite(rightContent, SWT.FLAT);

    gridLayout = new GridLayout();
    gridLayout.marginTop = 0;
    gridLayout.marginBottom = 0;
    gridLayout.marginLeft = 0;
    gridLayout.marginRight = 0;
    gridLayout.numColumns = 1;
    gridLayout.marginHeight = 3;
    gridLayout.marginWidth = 3;
    rightContent.setLayout(gridLayout);

    gridData = new GridData();
    gridData.grabExcessHorizontalSpace = true;
    gridData.grabExcessVerticalSpace = true;
    gridData.verticalAlignment = GridData.FILL;
    gridData.horizontalAlignment = GridData.FILL;
    rightContent.setLayoutData(gridData);

    gridLayout = new GridLayout();
    gridLayout.marginTop = 0;
    gridLayout.marginLeft = 0;
    gridLayout.marginRight = 0;
    gridLayout.marginBottom = 0;
    gridLayout.marginHeight = 3;
    gridLayout.marginWidth = 3;
    gridLayout.numColumns = 1;
    testComp.setLayout(gridLayout);

    gridData = new GridData();
    gridData.grabExcessHorizontalSpace = true;
    gridData.grabExcessVerticalSpace = true;
    gridData.verticalAlignment = GridData.FILL;
    gridData.horizontalAlignment = GridData.FILL;
    testComp.setLayoutData(gridData);

    createElementContentWidget(testComp);

    int[] weights = { 30, 70 };
    sashForm.setWeights(weights);

    Composite buttonComposite = getWidgetFactory().createComposite(pageBook2, SWT.FLAT);
    gridLayout = new GridLayout();
    gridLayout.marginTop = 0;
    gridLayout.marginBottom = 0;
    gridLayout.numColumns = 2;
    gridLayout.makeColumnsEqualWidth = true;
    buttonComposite.setLayout(gridLayout);
    addButton = getWidgetFactory().createButton(buttonComposite, "Add...", SWT.FLAT);
    addButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
    addButton.addSelectionListener(this);
    addButton.setToolTipText("Add Application Specific Information");
    removeButton = getWidgetFactory().createButton(buttonComposite, "Remove", SWT.FLAT);
    removeButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
    removeButton.addSelectionListener(this);
    removeButton.setToolTipText("Remove Application Specific Information");
    
    pageBook.showPage(pageBook2);
  }

  protected void createElementContentWidget(Composite parent)
  {
    tableTree = new ExtensionsComponentTableTreeViewer(parent);
    GridData gridData = new GridData();
    gridData.grabExcessHorizontalSpace = true;
    gridData.grabExcessVerticalSpace = true;
    gridData.verticalAlignment = GridData.FILL;
    gridData.horizontalAlignment = GridData.FILL;

    tableTree.getControl().setLayoutData(gridData);
  }

  /*
   * @see org.eclipse.wst.common.ui.properties.internal.provisional.view.ITabbedPropertySection#refresh()
   */
  public void refresh()
  {
    setListenerEnabled(false);
    ExtensibleElement extensibleElement = getInputExtensibleElement();
    if (extensibleElement != null)
    {
      tableTree.setInput(null);
      tableTree.getTree().removeAll();
      extensibilityElementsTable.getTable().removeAll();

      extensibilityElementsLabel.setText("Extensibility Elements");
      extensibilityElementsTable.setInput(extensibleElement);
      
      // TODO (cs)need to revist this... not sure why these were hardcoded to be disabled 
      //addButton.setEnabled(false);
      //removeButton.setEnabled(false);

      if (extensibilityElementsTable.getTable().getSelectionCount() == 0)
      {
        Object o = extensibilityElementsTable.getElementAt(0);
        if (o != null)
        {
          extensibilityElementsTable.setSelection(new StructuredSelection(o));
          if (o instanceof Element)
          {
            tableTree.setInput(((Element) o).getParentNode());
          }
          else if (o instanceof WSDLElement)
          {
            tableTree.setInput(((WSDLElement) o).getElement().getParentNode());
          }
        }
        tableTree.refresh();
      }

    }
    setListenerEnabled(true);

  }

  public Composite getPage()
  {
    return page;
  }

  public void widgetSelected(SelectionEvent event)
  {
    if (event.widget == addButton)
    {
      ExtensionsSchemasRegistry registry = WSDLEditorPlugin.getInstance().getExtensionsSchemasRegistry();
      AddExtensionsComponentDialog dialog = new AddExtensionsComponentDialog(composite.getShell(), registry);
      
      ExtensibleElement inputExtensibleElement = getInputExtensibleElement();
      Element hostElement;
      if (inputExtensibleElement == null ){
    	  MessageBox errBox = new MessageBox(Display.getCurrent().getActiveShell(), SWT.ICON_ERROR);
    	  errBox.setText("Add extensibilty element");
    	  errBox.setMessage("This element is not extensible.");
    	  errBox.open();
    	  return;
      }
	  hostElement = inputExtensibleElement.getElement();
	  filterForDialogOfAddButton.setHostElement(hostElement);
      dialog.addElementsTableFilter(filterForDialogOfAddButton);
      
      List schemaSpecs = registry.getAllExtensionsSchemasContribution();

      dialog.setInput(schemaSpecs);
      dialog.setBlockOnOpen(true);

      if (dialog.open() == Window.OK)
      {
        Object[] result = dialog.getResult();
        if (result != null)
        {
          XSDElementDeclaration element = (XSDElementDeclaration) result[0];
          SpecificationForExtensionsSchema spec = (SpecificationForExtensionsSchema) result[1];

          // The case below will never happen..... What scenario makes sense?
          if (input instanceof XSDConcreteComponent)
          {
            AddAppInfoCommand addAppInfo = new AddAppInfoElementCommand("Add AppInfo", (XSDConcreteComponent) input, element);
            addAppInfo.setSchemaProperties(spec);

            if (getCommandStack() != null)
            {
              getCommandStack().execute(addAppInfo);
            }
          }
          else if (inputExtensibleElement instanceof WSDLElement)
          {
            // TODO getCommandStack
            AddExtensibilityElementCommand addEECommand = new AddExtensibilityElementCommand("Add Extensibility Element", ((WSDLElement) inputExtensibleElement).getElement(), element.getElement());
            addEECommand.setSchemaProperties(spec);
            addEECommand.execute();
          }

        }
        extensibilityElementsTable.refresh();
        refresh();
      }

    }
    else if (event.widget == removeButton)
    {
      ISelection selection = extensibilityElementsTable.getSelection();
      XSDAnnotation xsdAnnotation = (XSDAnnotation) extensibilityElementsTable.getInput();

      if (selection instanceof StructuredSelection)
      {
        Object o = ((StructuredSelection) selection).getFirstElement();
        if (o instanceof Element)
        {
          Node appInfoElement = ((Element) o).getParentNode();
          RemoveAppInfoElementCommand command = new RemoveAppInfoElementCommand("Remove AppInfo", xsdAnnotation, appInfoElement);
          if (getCommandStack() != null)
          {
            getCommandStack().execute(command);
            extensibilityElementsTable.setInput(xsdAnnotation);
            extensibilityElementsTable.refresh();

            if (extensibilityElementsTable.getTable().getItemCount() > 0)
            {
              Object object = extensibilityElementsTable.getElementAt(0);
              if (object != null)
              {
                extensibilityElementsTable.setSelection(new StructuredSelection(object));
              }
            }
            else
            {
              tableTree.setInput(null);
            }
          }
        }
      }

    }
    else if (event.widget == extensibilityElementsTable.getTable())
    {
      System.out.println("ee table selected");
    }
  }

  public void widgetDefaultSelected(SelectionEvent event)
  {

  }

  public boolean shouldUseExtraSpace()
  {
    return true;
  }

  public void dispose()
  {

  }
  
  /**This filter is to be used by the dialog invoked when addButton is pressed 
   */
  private class FilterForDialogOfAddButton extends ViewerFilter{
	private Element hostElement;

	public void setHostElement(Element hostElement) {
		this.hostElement = hostElement;
	}

	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if (element instanceof XSDElementDeclaration) {
			
			String namespace = ((XSDElementDeclaration) element).getTargetNamespace();
			String name = ((XSDElementDeclaration) element).getName();
			
			ExtensiblityElementFilter filter = (ExtensiblityElementFilter) WSDLEditorPlugin
				.getInstance().getExtensiblityElementFilterRegistry()
				.getProperty(namespace, "");

			if (filter != null) {
				return filter.isValidContext(hostElement, name);
			}
				return true;
		}
		return true;
	}
  }

  static class ElementTableContentProvider implements IStructuredContentProvider
  {
    protected String facet;

    public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
    {
    }

    public java.lang.Object[] getElements(java.lang.Object inputElement)
    {
      if (inputElement instanceof XSDAnnotation)
      {
        XSDAnnotation xsdAnnotation = (XSDAnnotation) inputElement;
        List appInfoList = xsdAnnotation.getApplicationInformation();
        List elementList = new ArrayList();
        for (Iterator it = appInfoList.iterator(); it.hasNext();)
        {
          Object obj = it.next();
          if (obj instanceof Element)
          {
            Element appInfo = (Element) obj;
            NodeList nodeList = appInfo.getChildNodes();
            int length = nodeList.getLength();
            for (int i = 0; i < length; i++)
            {
              Node node = nodeList.item(i);
              if (node instanceof Element)
              {
                elementList.add(node);
              }
            }
          }
        }
        return elementList.toArray();
      }
      else if (inputElement instanceof ExtensibleElement)
      {
        ExtensibleElement extensibleElement = (ExtensibleElement) inputElement;
        // List elementList = new ArrayList();
        // for (Iterator it =
        // extensibleElement.getExtensibilityElements().iterator();
        // it.hasNext(); )
        // {
        // Object obj = it.next();
        // elementList.add(obj);
        // }
        // return elementList.toArray();
        return extensibleElement.getExtensibilityElements().toArray();
      }

      return Collections.EMPTY_LIST.toArray();
    }

    public void dispose()
    {

    }
  }

  static class ElementTableLabelProvider extends LabelProvider implements ITableLabelProvider
  {
    public Image getColumnImage(Object element, int columnIndex)
    {
      ExtensionsSchemasRegistry registry = WSDLEditorPlugin.getInstance().getExtensionsSchemasRegistry();

      if (element instanceof WSDLElement)
      {
        ILabelProvider provider = registry.getLabelProvider(((WSDLElement) element).getElement());
        if (provider != null)
        {
          return provider.getImage(((WSDLElement) element).getElement());
        }
      }

      return null;
    }

    public String getColumnText(Object element, int columnIndex)
    {
      ExtensionsSchemasRegistry registry = WSDLEditorPlugin.getInstance().getExtensionsSchemasRegistry();

      if (element instanceof Element)
      {
        Element domElement = (Element) element;
        return domElement.getLocalName();

      }
      else if (element instanceof WSDLElement)
      {
        ILabelProvider provider = registry.getLabelProvider(((WSDLElement) element).getElement());
        if (provider != null)
        {
          return provider.getText(((WSDLElement) element).getElement());
        }
        return ((WSDLElement) element).getElement().getNodeName();
      }
      return "";
    }
  }

  class ElementSelectionChangedListener implements ISelectionChangedListener
  {
    public void selectionChanged(SelectionChangedEvent event)
    {
      ISelection selection = event.getSelection();
      if (selection instanceof StructuredSelection)
      {
        Object obj = ((StructuredSelection) selection).getFirstElement();
        if (obj instanceof Element)
        {
          selectedElement = (Element) obj;
          tableTree.setInput(selectedElement.getParentNode());
          tableTree.setASIElement(selectedElement);
          tableTree.setCommandStack(getCommandStack());
          contentLabel.setText("Structure of " + selectedElement.getLocalName());
          contentLabel.getParent().layout();
        }
        else if (obj instanceof WSDLElement)
        {
          selectedElement = ((WSDLElement) obj).getElement();
          tableTree.setInput(selectedElement.getParentNode());
          tableTree.setASIElement(selectedElement);
          tableTree.setCommandStack(getCommandStack());
          contentLabel.setText("Structure of " + selectedElement.getLocalName());
          contentLabel.getParent().layout();
        }
      }
    }

  }
}
