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
package org.eclipse.wst.wsdl.ui.internal.wizards;

import java.util.List;
import java.util.Vector;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.wst.common.contentmodel.util.NamespaceInfo;
import org.eclipse.wst.common.ui.viewers.NavigableTableViewer;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.xml.ui.dialogs.EditNamespaceInfoDialog;
import org.eclipse.wst.xml.ui.dialogs.UpdateListener;
import org.eclipse.wst.xml.ui.util.XMLCommonResources;
 
public class NamespaceTable extends Composite
{
  protected static final String NAMESPACE_URI = XMLCommonResources.getInstance().getString("_UI_LABEL_NAMESPACE_NAME"); //$NON-NLS-1$
  protected static final String PREFIX = XMLCommonResources.getInstance().getString("_UI_LABEL_PREFIX"); //$NON-NLS-1$
  protected static final String INCLUDE = WSDLEditorPlugin.getWSDLString("_UI_TITLE_SELECT"); //$NON-NLS-1$
    
  protected List namespaceInfoList = new Vector();
  protected List checkedList = new Vector();
  protected NamespaceNavigableTableViewer tableViewer;    
  protected Button addButton;

  protected NamespaceInfoTableLabelProvider provider;
  protected UpdateListener updateListener;   
  protected IPath resourceLocation;  
  protected int visibleRows = -1;  
  protected boolean dummyRowsRemoved = false;
  Table table;

  public NamespaceTable(Composite parent)
  {  
    this(parent, -1, -1, -1);
  }    

  public NamespaceTable(Composite parent, int visibleRows)
  {
    this(parent, -1, -1, visibleRows);
  }

  public NamespaceTable(Composite parent, int widthHint, int heightHint)
  {
    this(parent, widthHint, heightHint, -1);
  }

  public NamespaceTable(Composite parent, int widthHint, int heightHint, int visibleRows)
  {
    super(parent, SWT.NONE);                                                               
    setLayout(createGridLayout());                                 
    GridData fillGD= new GridData();
    fillGD.horizontalAlignment= GridData.FILL;
    fillGD.grabExcessHorizontalSpace= true;
    fillGD.verticalAlignment= GridData.FILL;
    fillGD.grabExcessVerticalSpace= true;
    
    setLayoutData(fillGD);             
    
    Group namespaceInfoGroup = new Group(this, SWT.NONE);
    namespaceInfoGroup.setText(WSDLEditorPlugin.getWSDLString("_UI_LABEL_NAMESPACE_URIS_TO_BE_INCLUDED")); //$NON-NLS-1$
    namespaceInfoGroup.setLayout(new GridLayout());   
    GridData gd = fillGD;
    if (widthHint != -1)
    {
      gd.widthHint = widthHint;
    }   
    if (heightHint != -1)
    {
      gd.heightHint = heightHint;
    }
    namespaceInfoGroup.setLayoutData(gd); 
    //WorkbenchHelp.setHelp(namespaceInfoGroup, new ControlContextComputer(namespaceInfoGroup, XMLBuilderContextIds.XMLC_NAMESPACE_GROUP));
                    
    String[] titleArray = {INCLUDE, PREFIX, NAMESPACE_URI};

    table = new Table(namespaceInfoGroup, SWT.FULL_SELECTION);
    table.setHeaderVisible(true);
    
    table.setLayoutData(fillGD);
    table.setLinesVisible(true);
    table.addMouseListener(new MouseAdapter()
    {
      public void mouseDown(MouseEvent e)
      {
        TableItem item = table.getItem(new Point(e.x, e.y));
        if (item != null)
        {
          Object obj = item.getData();
          if (obj != null)
          {
            TableElement holder = (TableElement)obj;
            TableColumn col = table.getColumn(0);
            if (e.x < col.getWidth()) // if the point falls within the Select column then perform check/uncheck
            {
              String currentState = holder.getChecked();
              if (currentState.equals("true")) //$NON-NLS-1$
              {
                holder.setChecked("false"); //$NON-NLS-1$
                checkedList.set(holder.getIndex(), "false"); //$NON-NLS-1$
              }  
              else if (currentState.equals("false")) //$NON-NLS-1$
              {
                holder.setChecked("true");  //$NON-NLS-1$
                checkedList.set(holder.getIndex(), "true"); //$NON-NLS-1$
              }
              tableViewer.refresh();
              if (updateListener != null)
              {
                updateListener.updateOccured(this, namespaceInfoList);
              }
            }
          }
        }        
      }
    });

    tableViewer = new NamespaceNavigableTableViewer(table);
    provider = new NamespaceInfoTableLabelProvider();
    tableViewer.setContentProvider(provider);   
    tableViewer.setLabelProvider(provider);
    tableViewer.setColumnProperties(titleArray);
    tableViewer.setCellModifier(new NamespaceInfoCellModifier());

                    
    int[] widthArray = {10, 20, 50};
    TableLayout layout = new TableLayout();  

    for (int i = 0; i < titleArray.length; i++)
    {
      TableColumn column = new TableColumn(table, i);
      column.setText(titleArray[i]);
      column.setAlignment(SWT.LEFT);
      layout.addColumnData(new ColumnWeightData(widthArray[i], true));
    }         
    this.visibleRows = visibleRows;
    for (int i = 0; i < visibleRows; i++)
    {
      TableItem item = new TableItem(table, SWT.NONE); 
      item.setText("#######"); //$NON-NLS-1$
    }
    table.setLayout(layout);  
    
    CellEditor[] cellEditors = new CellEditor[titleArray.length];
    cellEditors[0] = new NamespaceInfoCheckboxCellEditor(table);
    cellEditors[1] = new TextCellEditor(table);
    tableViewer.setCellEditors(cellEditors);

//    createButtons(namespaceInfoGroup);            

    initialize();    
  }    

  public void setUpdateListener(UpdateListener updateListener)
  {
    this.updateListener = updateListener;
  }

  public void setNamespaceInfoList(List namespaceInfoList)
  {    
    this.namespaceInfoList = namespaceInfoList;
    update();
  }                       

  public List getNamespaceInfoList()
  {
    return namespaceInfoList;
  }  

  public List getNamespaceCheckedList()
  {
    return checkedList;
  }  

  public String getWSDLPrefix()
  {
    return ((NamespaceInfo)(namespaceInfoList.get(0))).prefix;
  }

  public void setResourceLocation(IPath resourceLocation)
  {
    this.resourceLocation = resourceLocation;
  }

  public GridLayout createGridLayout()
  {
    GridLayout gridLayout = new GridLayout();
    gridLayout.marginWidth = 0;
    gridLayout.horizontalSpacing = 0;
    return gridLayout;
  }
    
  protected void initialize()
  {
    // better way to do this....
    NamespaceInfo info1 = new NamespaceInfo();
    info1.prefix = "wsdl"; //$NON-NLS-1$
    info1.uri = "http://schemas.xmlsoap.org/wsdl/"; //$NON-NLS-1$
    namespaceInfoList.add(info1);
    checkedList.add("true"); //$NON-NLS-1$
    NamespaceInfo info2 = new NamespaceInfo();
    info2.prefix = "soap"; //$NON-NLS-1$
    info2.uri = "http://schemas.xmlsoap.org/wsdl/soap/"; //$NON-NLS-1$
    namespaceInfoList.add(info2);
    checkedList.add("false"); //$NON-NLS-1$
    NamespaceInfo info3 = new NamespaceInfo();
    info3.prefix = "http";  //$NON-NLS-1$
    info3.uri = "http://schemas.xmlsoap.org/wsdl/http/"; //$NON-NLS-1$
    namespaceInfoList.add(info3);
    checkedList.add("false"); //$NON-NLS-1$
    NamespaceInfo info4 = new NamespaceInfo();
    info4.prefix = "mime"; //$NON-NLS-1$
    info4.uri = "http://schemas.xmlsoap.org/wsdl/mime/"; //$NON-NLS-1$
    namespaceInfoList.add(info4);
    checkedList.add("false"); //$NON-NLS-1$
    NamespaceInfo info5 = new NamespaceInfo();
    info5.prefix = "soapenc"; //$NON-NLS-1$
    info5.uri = "http://schemas.xmlsoap.org/soap/encoding/"; //$NON-NLS-1$
    namespaceInfoList.add(info5);
    checkedList.add("false"); //$NON-NLS-1$
    NamespaceInfo info6 = new NamespaceInfo();
    info6.prefix = "soapenv"; //$NON-NLS-1$
    info6.uri = "http://schemas.xmlsoap.org/soap/envelope/"; //$NON-NLS-1$
    namespaceInfoList.add(info6);
    checkedList.add("false"); //$NON-NLS-1$
    NamespaceInfo info7 = new NamespaceInfo();
    info7.prefix = "xsi"; //$NON-NLS-1$
    info7.uri = "http://www.w3.org/2001/XMLSchema-instance"; //$NON-NLS-1$
    namespaceInfoList.add(info7);
    checkedList.add("false"); //$NON-NLS-1$
    NamespaceInfo info8 = new NamespaceInfo();
    info8.prefix = "xsd"; //$NON-NLS-1$
    info8.uri = "http://www.w3.org/2001/XMLSchema"; //$NON-NLS-1$
    namespaceInfoList.add(info8);
    checkedList.add("true"); //$NON-NLS-1$
    update();
  }

  protected void createButtons(Composite parent)
  {
    Composite composite = new Composite(parent, SWT.NONE);
    GridData hFillGD = new GridData();
    hFillGD.horizontalAlignment= GridData.FILL;
    hFillGD.grabExcessHorizontalSpace= true;
    
    composite.setLayoutData(hFillGD);
    GridLayout gridLayout = new GridLayout();
    gridLayout.numColumns = 2;
    gridLayout.marginHeight = 0;
    gridLayout.marginWidth = 0;
    composite.setLayout(gridLayout);

    GridData gd = new GridData();
    gd.horizontalAlignment = GridData.FILL;
    gd.grabExcessHorizontalSpace= true;
    
    Button hiddenButton = new Button(composite, SWT.NONE);
    hiddenButton.setLayoutData(gd);
    hiddenButton.setVisible(false);
    hiddenButton.setEnabled(false);

    SelectionListener selectionListener = new SelectionAdapter()
    {
      public void widgetSelected(SelectionEvent e)
      {  
        if (e.widget == addButton)
        {
          performNew();
        }
      }
    };   
    
    // create a composite to hold the three buttons
    Composite buttonComposite = new Composite(composite, SWT.NONE);

    buttonComposite.setLayoutData(hFillGD);
    GridLayout buttonGridLayout = new GridLayout();
    buttonGridLayout.numColumns = 1;
    buttonGridLayout.makeColumnsEqualWidth = true;
    buttonComposite.setLayout(buttonGridLayout);

    // add the New button
    //
    gd = new GridData();
    gd.horizontalAlignment = GridData.FILL;
    gd.grabExcessHorizontalSpace = true;

    addButton = new Button(buttonComposite, SWT.NONE);
    addButton.setText(WSDLEditorPlugin.getWSDLString("_UI_ACTION_ADD")); //$NON-NLS-1$
    addButton.setLayoutData(gd);//ViewUtility.createHorizontalFill());
    addButton.addSelectionListener(selectionListener);
  }

  public void performNew()
  {
    NamespaceInfo info = new NamespaceInfo();
    EditNamespaceInfoDialog dialog = invokeDialog(XMLCommonResources.getInstance().getString("_UI_LABEL_NEW_NAMESPACE_INFORMATION"), info); //$NON-NLS-1$
    if (dialog.getReturnCode() == Dialog.OK)
    {
      namespaceInfoList.add(info);
      checkedList.add("true"); //$NON-NLS-1$
      performDelayedUpdate();
    }
  } 
                     
  protected EditNamespaceInfoDialog invokeDialog(String title, NamespaceInfo info)
  {
    Shell shell = XMLCommonResources.getInstance().getWorkbench().getActiveWorkbenchWindow().getShell();
    EditNamespaceInfoDialog dialog = new EditNamespaceInfoDialog(shell, info);
    dialog.create();
    dialog.getShell().setText(title);
    dialog.setBlockOnOpen(true);
    dialog.setResourceLocation(resourceLocation);
    dialog.open();      
    return dialog;
  }

  protected void performDelayedUpdate()
  {
    Runnable delayedUpdate = new Runnable()
    {
      public void run()
      {
        update();
      }
    };
    Display.getCurrent().asyncExec(delayedUpdate);

    if (updateListener != null)
    {
      updateListener.updateOccured(this, namespaceInfoList);
    }
  }
 
  protected NamespaceInfo getTargetNamespaceInfo()
  {
    return (namespaceInfoList != null && namespaceInfoList.size() > 0) ? 
           (NamespaceInfo)namespaceInfoList.get(0) :
           null;
  }    

                    
  public void update()
  {
    updateHelper(namespaceInfoList);
  }
                           
  public void updateHelper(List namespaceInfoList)
  {                                      
    if (visibleRows != -1 && !dummyRowsRemoved)
    {
//      dummyRowsRemoved = true;
//      tableViewer.getTable().removeAll();
    }                                                      
    ISelection selection = tableViewer.getSelection();
    tableViewer.setInput(namespaceInfoList);
    if (selection.isEmpty())
    {                     
      if (namespaceInfoList.size() > 0)
      {
        tableViewer.setSelection(new StructuredSelection(namespaceInfoList.get(0)));
      }
    }
    else
    {
      tableViewer.setSelection(selection);
    }
  }

  protected class NamespaceInfoCheckboxCellEditor extends CheckboxCellEditor implements MouseListener
  {
    public NamespaceInfoCheckboxCellEditor(Composite parent)
    {
      super(parent);
    }

    protected void doSetValue(Object value)
    {
    }

    public void activate()
    {
       super.activate();
       deactivate();
       Display.getCurrent().getFocusControl().redraw();
    }

    public void mouseDown(MouseEvent e)   
    {      
      if (tableViewer.getTable().getItem(new Point(e.x, e.y)) != null)
      {
      }
    }    
    public void mouseDoubleClick(MouseEvent e) {}
    public void mouseUp(MouseEvent e) {}


  }


  protected class NamespaceInfoCellModifier implements ICellModifier
  {
    public NamespaceInfoCellModifier()
    {
      
    }
    
    public boolean canModify(Object element, String property)
    {
      if (property.equals(INCLUDE))
      {
        return true;
      }
      else if (property.equals(PREFIX))
      {
        return true;
      }
      return false;      
    }

    public Object getValue(Object element, String property)
    {
      int column = 0;
      if (property.equals(INCLUDE))
      {
        column = 0;
      }
      else if (property.equals(PREFIX))
      {
        column = 1;
      }
      else if (property.equals(NAMESPACE_URI))
      {
        column = 2;
      }

      if (element instanceof TableElement)
      {
        return provider.getColumnText(element, column);
      }
      else
      {
        return null;
      }
    }

    public void modify(Object element, String property, Object value)
    {
      TableItem item = (TableItem)element;
      if (item != null)
      {
        Object obj = item.getData();
        if (obj != null)
        {
          TableElement holder = (TableElement)obj;
          NamespaceInfo info = holder.getNamespaceInfo();
          if (property.equals(INCLUDE))
          {
            if (value instanceof Boolean)
            {
              if (((Boolean)value).booleanValue())
              {
                holder.setChecked("true");
                checkedList.set(holder.getIndex(), "true");
              }
              else
              {
                holder.setChecked("false");
                checkedList.set(holder.getIndex(), "false");
              }
            }
          }
          else if (property.equals(PREFIX))
          {
            info.prefix = ((String)value).trim();
          }

          Runnable delayedUpdate = new Runnable()
          {
            public void run()
            {
              tableViewer.refresh();
            }
          };
          Display.getCurrent().asyncExec(delayedUpdate);
          if (updateListener != null)
          {
            updateListener.updateOccured(this, namespaceInfoList);
          }

//          performDelayedUpdate();
        }
      }
    }
  }                   

  protected class TableElement
  {
    NamespaceInfo info;
    int index;
    String checked = "true";
    
    TableElement(NamespaceInfo info, int index)
    {
      this.info = info;
      this.index = index;
    }
    
    public NamespaceInfo getNamespaceInfo()
    {
      return info;
    }
    
    public int getIndex()
    {
      return index;
    }
    
    public String getChecked()
    {
      return checked;
    }
    
    public void setChecked(String checked)
    {
      this.checked = checked;
    }
  }

  /**
   * NamespaceInfoTableLabelProvider
   */
  protected class NamespaceInfoTableLabelProvider implements ITableLabelProvider, IStructuredContentProvider
  {
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
    {
    }

    public boolean isLabelProperty(Object element, String property)
    {
      return false;
    }

    public boolean isDeleted(Object element)
    {
      return false;
    }

    public Object[] getElements(Object inputElement)
    {
      Vector v = new Vector();
      for (int i = 0; i < namespaceInfoList.size(); i++)
      {
        NamespaceInfo info = (NamespaceInfo)namespaceInfoList.get(i);
        TableElement holder = new TableElement(info, i);
        holder.setChecked((String)checkedList.get(i));
        v.add(holder);
      }
      return v.toArray();
    }

    public String getColumnText(Object object, int column)
    {
      TableElement holder = (TableElement)object;
      NamespaceInfo info = holder.getNamespaceInfo();
      String result = null;
      switch (column)
      {
        case 1: { result = info.prefix; break; }
        case 2: { result = info.uri; break; }
      }
      result = result != null ? result.trim() : "";
      if (result.equals(""))
      {
        switch (column)
        {
          case 2: 
          { 
            result = XMLCommonResources.getInstance().getString("_UI_NO_NAMESPACE_NAME");  //$NON-NLS-1$
            break; 
          }
        }
      }
      return result;
    }

//    private String getDefaultPrefix()
//    {
//      String defaultPrefix = "p";
//      if (namespaceInfoList == null)
//        return defaultPrefix;
//
//      Vector v = new Vector();
//      for (int i=0; i<namespaceInfoList.size(); i++)
//      {
//        NamespaceInfo nsinfo = (NamespaceInfo)namespaceInfoList.get(i);
//        if (nsinfo.prefix != null)
//          v.addElement(nsinfo.prefix);
//      }
//
//      if (v.contains(defaultPrefix))
//      {
//        String s = defaultPrefix;
//        for (int j=0; v.contains(s); j++)
//        {
//          s = defaultPrefix + Integer.toString(j);
//        }
//        return s;
//      }
//      else
//        return defaultPrefix;
//    }

    public Image getColumnImage(Object object, int columnIndex)
    {
      if (columnIndex == 0)  // Output Column
      {
        if (object instanceof TableElement)
        {
          TableElement holder = (TableElement) object;
          String result = holder.getChecked();
          if (result.equals("true")) //$NON-NLS-1$
          {
            return WSDLEditorPlugin.getInstance().getImage("icons/output_yes.gif"); //$NON-NLS-1$
          }
          else
          {
            return WSDLEditorPlugin.getInstance().getImage("icons/output_no.gif"); //$NON-NLS-1$
          }
        }
      }
      return null;
    }

    public boolean isLabelProperty(Object object, Object property)
    {
      return false;
    }

    public void addListener(ILabelProviderListener listener)
    {
    }

    public void removeListener(ILabelProviderListener listener)
    {
    }

    public void dispose()
    {
    }
  }

  class NamespaceNavigableTableViewer extends NavigableTableViewer
  {
    public NamespaceNavigableTableViewer(Table parent)
    {
      super(parent);
    }
  }
}
