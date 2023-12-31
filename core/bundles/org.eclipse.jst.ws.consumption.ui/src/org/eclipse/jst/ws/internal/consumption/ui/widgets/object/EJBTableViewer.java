/*******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060524   141194 joan@ca.ibm.com - Joan Haggarty
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.ui.widgets.object;

import java.util.Vector;

import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jst.ws.internal.consumption.ui.ConsumptionUIMessages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class EJBTableViewer extends TableViewer
{
  private Vector beanNames;
  private Vector projectNames;

  public EJBTableViewer(Composite parent)
  {
    super(parent, SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
    
    String columnProperties[] = {ConsumptionUIMessages.TABLE_TITLE_EJB_BEAN_NAMES, ConsumptionUIMessages.TABLE_TITLE_EJB_PROJECT_NAME};
    int columnsWidth[] = {60, 20};
    int columnMins[] = {175, 125};
    
    Table table = getTable();
    table.setHeaderVisible(true);
    table.setLinesVisible(true);
    TableLayout layout = new TableLayout();
    for (int i = 0; i < columnProperties.length; i++)
    {
      TableColumn column = new TableColumn(table, SWT.NONE, i);
      column.setText(columnProperties[i]);
      column.pack();
      layout.addColumnData(new ColumnWeightData(columnsWidth[i], columnMins[i], true));      
    }
    table.setLayout(layout);
    setColumnProperties(columnProperties);
    setContentProvider(new EJBContentProvider());
    setLabelProvider(new EJBLabelProvider());
  }
  
  public void setData(Vector beanNames, Vector projectNames)
  {
    this.beanNames = beanNames;
    this.projectNames = projectNames;
  }
  private class EJBLabelProvider extends LabelProvider implements ITableLabelProvider
  {
    public String getColumnText(Object object, int columnIndex)
    {
      String result = null;
      if (object instanceof Integer)
      {
        int index = ((Integer) object).intValue();
        if (columnIndex == 0)
        {
          return (String) (beanNames.elementAt(index));
        }
        else if (columnIndex == 1)
        {
          return (String) (projectNames.elementAt(index));
        }
      }
      return result;
    }

    public Image getColumnImage(Object object, int columnIndex)
    {
      return null;
    }
  }
  public class EJBContentProvider implements IStructuredContentProvider
  {
    public Object[] getElements(Object element)
    {
      if (beanNames != null)
      {
        int size = beanNames.size();
        Object[] result = new Object[size];
        for (int index = 0; index < size; index++)
        {
          result[index] = new Integer(index);
        }
        return result;
      }
      return null;
    }

    public void dispose()
    {
    }

    public void inputChanged(Viewer viewer, Object old, Object newobj)
    {
    }

    public boolean isDeleted(Object object)
    {
      return false;
    }
  }
}
