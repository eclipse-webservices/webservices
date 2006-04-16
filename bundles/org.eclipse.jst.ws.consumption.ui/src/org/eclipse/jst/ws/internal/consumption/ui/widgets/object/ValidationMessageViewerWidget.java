/*******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060411   136134 kathy@ca.ibm.com - Kathy Chan
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.ui.widgets.object;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jst.ws.internal.consumption.ui.ConsumptionUIMessages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.command.internal.env.ui.widgets.SimpleWidgetDataContributor;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetDataEvents;
import org.eclipse.wst.wsdl.validation.internal.IValidationMessage;


public class ValidationMessageViewerWidget extends SimpleWidgetDataContributor
{
  private int DEFAULT_TABLE_HEIGHT_HINT = 100;
  private TableViewer tableViewer_;
  private Table table_;
  private String message = null;
  static final String columns_[] = {ConsumptionUIMessages.TABLE_COLUMN_VALIDATION_SEVERITY,
		ConsumptionUIMessages.TABLE_COLUMN_VALIDATION_LINE,
		ConsumptionUIMessages.TABLE_COLUMN_VALIDATION_COLUMN,
		ConsumptionUIMessages.TABLE_COLUMN_VALIDATION_MESSAGE};

  static final int columnsWidth_[] = {10, 10, 10, 60};

  

  public ValidationMessageViewerWidget()
  {
  }

  public WidgetDataEvents addControls( Composite parent, Listener statusListener )
  { 
   
	Composite  composite = new Composite(parent, SWT.NONE);
	GridLayout gl        = new GridLayout();
	
	gl.marginHeight = 0;
	gl.marginWidth = 0;
	composite.setLayout(gl);
	composite.setLayoutData(new GridData(GridData.FILL_BOTH));
	
    Label messageLabel = new Label( composite, SWT.WRAP);
    messageLabel.setText( ConsumptionUIMessages.LABEL_VALIDATE_MESSAGES);
    GridData gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
    messageLabel.setLayoutData(gd);
    messageLabel.setToolTipText( ConsumptionUIMessages.TOOLTIP_VALIDATE_TEXT_MESSAGE );

	table_ = new Table(composite, SWT.SINGLE | SWT.FULL_SELECTION | SWT.BORDER);
	gd = new GridData(GridData.FILL_BOTH);
	gd.heightHint = DEFAULT_TABLE_HEIGHT_HINT;
	table_.setLayoutData(gd);
	table_.setHeaderVisible(true);
	table_.setLinesVisible(true);
	table_.setToolTipText(ConsumptionUIMessages.TOOLTIP_TABLE_VALIDATE_MESSAGE );
	TableLayout tableLayout = new TableLayout();
	for (int i = 0; i < columns_.length; i++)
	{
	  TableColumn tableColumn = new TableColumn(table_, SWT.NONE);
	  tableColumn.setText(columns_[i]);
	  tableColumn.pack();
	  ColumnWeightData columnData = new ColumnWeightData(columnsWidth_[i], columnsWidth_[i], true);
	  tableLayout.addColumnData(columnData);
	}
	table_.setLayout(tableLayout);

	tableViewer_ = new TableViewer(table_);

	tableViewer_.setContentProvider(new ListContentProvider());
	tableViewer_.setLabelProvider(new ListLabelProvider());

	return this;
  }

  public void setInput(IValidationMessage[] messages)
  {
  	tableViewer_.setInput(messages);
  	tableViewer_.refresh();
  }
  
  public void clearInput()
  {
	  IValidationMessage emptyMessages[] = {};
	  this.setInput(emptyMessages);
  }
  
  public IContentProvider getContentProvider()
  {
	  return tableViewer_.getContentProvider();
  }

  public IStatus getStatus()
  {
    return message == null ? Status.OK_STATUS : 
                             StatusUtils.errorStatus(message );
  }

  public void refresh()
  {
	tableViewer_.refresh();
  }

  public TableItem[] getItems()
  {
	//internalRefresh();
	return table_.getItems();
  }

  public void setEnabled(boolean enabled)
  {
  }

  public void dispose()
  {	
	if (table_ != null)
	  table_.dispose();
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
		if (inputElement instanceof IValidationMessage[])
	  {
			IValidationMessage[] valMessages = (IValidationMessage[]) inputElement;
		  return valMessages;
	  }
	  else
		return new Object[0];
	}
  }

  protected class ListLabelProvider implements ITableLabelProvider
  {
	public Image getColumnImage(Object element, int columnIndex)
	{
		if (element instanceof IValidationMessage)
		{
			IValidationMessage msg = (IValidationMessage) element;
			
			if (columnIndex == 0)
			{	
				int severity = msg.getSeverity();
				if (severity == 0) {
					return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJS_ERROR_TSK);
				} else if (severity == 1) {
					return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJS_WARN_TSK);
				} else if (severity == 2) {
					return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJS_INFO_TSK);
				}
			}
		}	
	  return null;
	}

	public String getColumnText(Object element, int columnIndex)
	{
		String text = "";
		if (element instanceof IValidationMessage)
		{
			IValidationMessage msg = (IValidationMessage) element;
			
			switch (columnIndex) {
			case 0:		
				// no text to display, only display severity as image
				break;
			case 1:
				text = (new Integer(msg.getLine())).toString();
				break;
			case 2:
				text = (new Integer(msg.getColumn())).toString();
				break;
			case 3:
				text = msg.getMessage();
				break;		
				
			default:
				break;
			} 
		}
		return text;
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
