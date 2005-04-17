/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.ui.widgets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.internal.consumption.common.PropertiesResourceFilter;
import org.eclipse.jst.ws.internal.ui.dialog.DialogUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wst.command.env.core.common.MessageUtils;
import org.eclipse.wst.command.env.core.common.SimpleStatus;
import org.eclipse.wst.command.env.core.common.Status;
import org.eclipse.wst.command.internal.env.ui.widgets.SimpleWidgetDataContributor;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetDataEvents;


public class TableViewerWidget extends SimpleWidgetDataContributor
{
  private int DEFAULT_TABLE_HEIGHT_HINT = 100;
  private int DEFAULT_COLUMN_WIDTH = 80;

  public static byte MAP_ONE_TO_ONE = 3;
  public static byte MAP_MANY_TO_ONE = 1;
  public static byte MAP_MANY_TO_MANY = 0;

  private String[] columns_;
  private TableViewer tableViewer_;
  private Table table_;
  private TableEditor editor_;
  private TableEditorListener tableEditorListener_;
  private Text text_;
  private Button import_;
  private Button add_;
  private Button remove_;
  
  private Composite parent_;
  private Listener  statusListener_;

  private String message = null;
  private byte rescriction = MAP_MANY_TO_MANY;

  private List values_;
  private Object defaultValue_;

  private PropertiesResourceFilter filter_ = new PropertiesResourceFilter();

  public TableViewerWidget(String[] columns, List initValues, Object defaultValue, byte rescriction)
  {
	columns_ = columns;
	values_ = new ArrayList();
	if (initValues != null && initValues.size() > 0)
	  values_.addAll(initValues);
	defaultValue_ = (defaultValue != null) ? defaultValue : new String("");
	this.rescriction = rescriction;
  }

  public WidgetDataEvents addControls( Composite parent, Listener statusListener )
  { 
    parent_         = parent;
    statusListener_ = statusListener;
    
    String       pluginId = "org.eclipse.jst.ws.consumption.ui";
    MessageUtils msgUtils = new MessageUtils( pluginId + ".plugin", this );
    
	Composite  composite = new Composite(parent, SWT.NONE);
	GridLayout gl        = new GridLayout();
	
	gl.marginHeight = 0;
	gl.marginWidth = 0;
	composite.setLayout(gl);
	composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

	table_ = new Table(composite, SWT.SINGLE | SWT.FULL_SELECTION | SWT.BORDER);
	GridData gd = new GridData(GridData.FILL_BOTH);
	gd.heightHint = DEFAULT_TABLE_HEIGHT_HINT;
	table_.setLayoutData(gd);
	table_.setHeaderVisible(true);
	table_.setLinesVisible(true);
	TableLayout tableLayout = new TableLayout();
	int maxWidth = DEFAULT_COLUMN_WIDTH;
	for (int i = 0; i < columns_.length; i++)
	{
	  TableColumn tableColumn = new TableColumn(table_, SWT.NONE);
	  tableColumn.setText(columns_[i]);
	  tableColumn.pack();
	  int tableColumnWidth = Math.max(DEFAULT_COLUMN_WIDTH, tableColumn.getWidth());
	  maxWidth = Math.max(maxWidth, tableColumnWidth);
	  ColumnWeightData columnData = new ColumnWeightData(tableColumnWidth, tableColumnWidth, true);
	  tableLayout.addColumnData(columnData);
	}
	table_.setLayout(tableLayout);
	// initialize the table editor
	editor_ = new TableEditor(table_);
	// The editor must have the same size as the cell and must
	// not be any smaller than 50 pixels.
	editor_.horizontalAlignment = SWT.LEFT;
	editor_.grabHorizontal = true;
	editor_.minimumWidth = maxWidth;
	tableEditorListener_ = new TableEditorListener();
	table_.addMouseListener(tableEditorListener_);
	tableViewer_ = new TableViewer(table_);
	tableViewer_.getControl().addKeyListener(
	  new KeyListener()
	  {
		public void keyPressed(KeyEvent e)
		{
		  int asciiDel = (int)e.character;
		  // Del
		  if (asciiDel == 127) 
			handleDeleteKeyPressed();
                // Enter or space
                if (((int)e.character) == 13 || ((int)e.character) == 32)
                  tableEditorListener_.editSelection();
		}
		public void keyReleased(KeyEvent e)
		{
		}
	  }
	);
	tableViewer_.setContentProvider(new ListContentProvider());
	tableViewer_.setLabelProvider(new ListLabelProvider());
	tableViewer_.setInput(values_);
	tableViewer_.addSelectionChangedListener(
	  new ISelectionChangedListener()
	  {
		public void selectionChanged(SelectionChangedEvent event)
		{
		  enableRemove(true);
		}
	  }
	);

	Composite buttonComposite = new Composite(composite, SWT.NONE);
	gl = new GridLayout();
	gl.numColumns = 3;
	gl.makeColumnsEqualWidth = true;
	buttonComposite.setLayout(gl);
	buttonComposite.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_BEGINNING));

	import_ = new Button(buttonComposite, SWT.PUSH);
	import_.setText(msgUtils.getMessage("LABEL_IMPORT"));
	import_.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
	import_.addSelectionListener(
	  new SelectionListener()
	  {
		public void widgetSelected(SelectionEvent event)
		{
		  handleImportButtonSelected(event);
		}
		public void widgetDefaultSelected(SelectionEvent event)
		{
		}
	  }
	);
	
	add_ = new Button(buttonComposite, SWT.PUSH);
	add_.setText(msgUtils.getMessage("LABEL_ADD"));
	add_.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
	add_.addSelectionListener(
	  new SelectionListener()
	  {
		public void widgetSelected(SelectionEvent event)
		{
		  handleAddButtonSelected(event);
		}
		public void widgetDefaultSelected(SelectionEvent event)
		{
		}
	  }
	);

	
	remove_ = new Button(buttonComposite, SWT.PUSH);
	remove_.setText(msgUtils.getMessage("LABEL_REMOVE"));
	remove_.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
	remove_.addSelectionListener(
	  new SelectionListener()
	  {
		public void widgetSelected(SelectionEvent event)
		{
		  handleRemoveButtonSelected(event);
		}
		public void widgetDefaultSelected(SelectionEvent event)
		{
		}
	  }
	);
	enableRemove(false);
	
	return this;
  }

  private void enableRemove(boolean enabled)
  {
	remove_.setEnabled(enabled && !tableViewer_.getSelection().isEmpty());
  }

  private void handleImportButtonSelected(SelectionEvent event)
  {
	IResource resource = DialogUtils.browseResources(
	  parent_.getShell(),
	  ResourceUtils.getWorkspaceRoot(),
	  null,
	  filter_
	);
	if (resource != null && resource instanceof IFile)
	{
	  try
	  {
		Properties props = new Properties();
		props.load(((IFile)resource).getContents());
		Set set = props.entrySet();
		java.util.List list = new LinkedList();
		Iterator i = set.iterator();
		while (i.hasNext())
		{
		  Map.Entry me = (Map.Entry)i.next();
		  String key = (String)me.getKey();
		  String val = (String)me.getValue();
		  list.add(new String[] {key,val});
		}
		values_.addAll(list);
	  }
	  catch (Exception e)
	  {
	    // TODO Report some error here.
	  }
	}
	refresh();
  }

  private void handleAddButtonSelected(SelectionEvent event)
  {
	values_.add(defaultValue_);
	refresh();
	setSelectionAsObject(values_.get(table_.getItemCount()-1));
	tableEditorListener_.editSelection();
  }

  private void handleRemoveButtonSelected(SelectionEvent event)
  {
	handleDeleteKeyPressed();
  }

  private void handleDeleteKeyPressed()
  {
	internalDispose();
	ISelection selection = tableViewer_.getSelection();
	if (selection != null && !selection.isEmpty() && (selection instanceof IStructuredSelection))
	{
	  int selectionIndex = table_.getSelectionIndex();
	  int selectionCount = table_.getItemCount();
	  values_.remove(selectionIndex);
	  if (selectionIndex < selectionCount-1)
		setSelectionAsObject(values_.get(selectionIndex));
	  else if (selectionCount -2 >= 0)
		setSelectionAsObject(values_.get(selectionCount-2));
	  refresh();
	}
  }

  private void internalRefresh()
  {
	// synchronize text field, previously selected table cell and model (inputObject)
	if (text_ != null)
	{
	  TableItem oldTableItem = editor_.getItem();
	  int oldColumnIndex = editor_.getColumn();
	  if (oldTableItem != null && oldColumnIndex >= 0 && oldColumnIndex < columns_.length)
	  {
		String oldText = text_.getText();
		oldTableItem.setText(oldColumnIndex, oldText);
		int oldRowIndex = table_.indexOf(oldTableItem);
		values_.set(oldRowIndex, new String[] {oldTableItem.getText(0), oldTableItem.getText(1)});
	  }
	}
	checkMappingConstraints();
	statusListener_.handleEvent( null );
  }

  private void checkMappingConstraints() {

	HashMap map = new HashMap();
    String       pluginId = "org.eclipse.jst.ws.consumption.ui";
    MessageUtils msgUtils = new MessageUtils( pluginId + ".plugin", this );
    
	for(int cnt=0; cnt<values_.size(); cnt++){
		String[] value = (String[])values_.get(cnt);
		if(map.containsKey(value[0]) && ((rescriction & 1)!=0)){
			message = msgUtils.getMessage("MSG_MAPPING_DUPLICATE_ENTRIES", new String[]{columns_[0],columns_[1]});
			return;
		}
		if(map.containsValue(value[1]) && ((rescriction & 2)!=0)){
			message = msgUtils.getMessage("MSG_MAPPING_DUPLICATE_ENTRIES", new String[]{columns_[1],columns_[0]});
			return;
		}
		map.put(value[0],value[1]);

	}
	message = null;

  }

  public Status getStatus()
  {
    return message == null ? new SimpleStatus("") : 
                             new SimpleStatus( "", message, Status.ERROR );
  }

  private void setSelectionAsObject(Object object)
  {
	tableViewer_.setSelection(new StructuredSelection(object), true);
  }

  public void refresh()
  {
	internalRefresh();
	tableViewer_.refresh();
  }

  public TableItem[] getItems()
  {
	//internalRefresh();
	return table_.getItems();
  }

  public void setEnabled(boolean enabled)
  {
	add_.setEnabled(enabled);
	enableRemove(enabled);
  }

  public void dispose()
  {
	internalDispose();
	if (editor_ != null)
	  editor_.dispose();
	if (table_ != null)
	  table_.dispose();
	if (add_ != null)
	  add_.dispose();
	if (remove_ != null)
	  remove_.dispose();
  }

  private void internalDispose()
  {
	if (text_ != null)
	  text_.dispose();
	text_ = null;
  }

  protected class TableEditorListener implements MouseListener
  {
	private int currSelectionIndex_;
    private int editRow_;
    private int editColumn_;

	public TableEditorListener()
	{
	  super();
	  currSelectionIndex_ = -1;
         editRow_ = -1;
         editColumn_ = -1;
	}

	public void mouseDoubleClick(MouseEvent e)
	{
	  mouseDown(e);
	}

	public void mouseDown(MouseEvent e)
	{
	  // refresh table
	  internalRefresh();
	  // Clean up previous text editor control
	  internalDispose();
	  // update table
	  if (table_.isFocusControl())
	  {
		int selectedRowIndex = getSelectedRow(table_, e.y);
		if (currSelectionIndex_ != -1 && selectedRowIndex != -1 && currSelectionIndex_ == selectedRowIndex)
		{
		  TableItem tableItem = table_.getItem(selectedRowIndex);
		  int selectedColumnIndex = getSelectedColumn(tableItem, e.x, e.y);
		  if (selectedColumnIndex != -1 && (text_ == null || text_.isDisposed() || selectedColumnIndex != editor_.getColumn()))
			editSelection(selectedRowIndex, selectedColumnIndex);
		}
		currSelectionIndex_ = selectedRowIndex;
	  }
	}

	public void mouseUp(MouseEvent e)
	{
	}

	private int getSelectedRow(Table table, int y)
	{
	  TableItem[] tableItems = table.getItems();
	  for (int i = 0; i < tableItems.length; i++)
	  {
		Rectangle rectangle = tableItems[i].getBounds(0);
		if (rectangle != null && y >= rectangle.y && y < (rectangle.y + rectangle.height))
		  return i;
	  }
	  return -1;
	}

	private int getSelectedColumn(TableItem tableItem, int x, int y)
	{
	  for (int i = 0; i < columns_.length; i++)
	  {
		if (tableItem.getBounds(i).contains(x, y))
		  return i;
	  }
	  return -1;
	}

	private void editSelection(int row, int column)
	{
         editRow_ = row;
         editColumn_ = column;
	  TableItem tableItem = table_.getItem(row);
	  // Setup adapter for the new selection
	  text_ = new Text(table_, SWT.NONE);
	  String text = tableItem.getText(column);
	  text_.setText((text != null) ? text : "");
         text_.addKeyListener(new KeyListener()
           {
             public void keyPressed(KeyEvent e)
             {
               // Esc
               if (((int)e.character) == 27)
                 cancelSelection();
             }
             public void keyReleased(KeyEvent e)
             {
             }
           }
         );
         text_.addTraverseListener(new TraverseListener()
           {
             public void keyTraversed(TraverseEvent e)
             {
               if (e.detail == SWT.TRAVERSE_TAB_NEXT)
                 traverseTabNext();
               else if (e.detail == SWT.TRAVERSE_TAB_PREVIOUS)
                 traverseTabPrevious();
             }
           }
         );
	  editor_.setEditor(text_, tableItem, column);
	  text_.setFocus();
	  text_.selectAll();
	}

    protected void traverseTabNext()
    {
      internalRefresh();
      internalDispose();
      if (!(editRow_ == table_.getItems().length-1 && editColumn_ == columns_.length-1))
      {
        if (editColumn_ < columns_.length-1)
          editColumn_++;
        else
        {
          editColumn_ = 0;
          editRow_++;
          table_.select(editRow_);
        }
        editSelection(editRow_, editColumn_);
      }
    }

    protected void traverseTabPrevious()
    {
      internalRefresh();
      internalDispose();
      if (!(editRow_ == 0 && editColumn_ == 0))
      {
        if (editColumn_ > 0)
          editColumn_--;
        else
        {
          editColumn_ = columns_.length-1;
          editRow_--;
          table_.select(editRow_);
        }
        editSelection(editRow_, editColumn_);
      }
    }

    public void cancelSelection()
    {
      internalDispose();
      internalRefresh();
    }

	public void editSelection()
	{
	  int selectedRowIndex = table_.getSelectionIndex();
	  if (selectedRowIndex != -1 && columns_.length > 0)
	  {
		// refresh table
		internalRefresh();
		// Clean up any previous editor control
		internalDispose();
		editSelection(selectedRowIndex, 0);
	  }
	  currSelectionIndex_ = selectedRowIndex;
	}
  }

  protected class ListContentProvider implements IStructuredContentProvider
  {
	public void dispose()
	{
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
	{
	  tableViewer_.add(getElements(newInput));
	}

	public Object[] getElements(Object inputElement)
	{
	  if (inputElement instanceof List)
	  {
		List list = (List)inputElement;
		Object[] objects = new Object[list.size()];
		for (int i = 0; i < objects.length; i++)
		  objects[i] = list.get(i);
		return objects;
	  }
	  else
		return new Object[0];
	}
  }

  protected class ListLabelProvider implements ITableLabelProvider
  {
	public Image getColumnImage(Object element, int columnIndex)
	{
	  return null;
	}

	public String getColumnText(Object element, int columnIndex)
	{
		return ((String[])element)[columnIndex];	  	
	}

	public void addListener(ILabelProviderListener listener)
	{
	}

	public void removeListener(ILabelProviderListener listener)
	{
	}

	public boolean isLabelProperty(Object element, String property)
	{
	  return true;
	}

	public void dispose()
	{
	}
  }
 }
