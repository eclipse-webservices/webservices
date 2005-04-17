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
package org.eclipse.jst.ws.internal.consumption.ui.widgets.object;

import java.util.Vector;

import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.wst.command.internal.provisional.env.core.common.MessageUtils;

public class EJBTableViewer extends TableViewer
{
  private String pluginId_ = "org.eclipse.jst.ws.consumption.ui";
  private Vector beanNames;
  private Vector projectNames;

  public EJBTableViewer(Composite parent)
  {
    super(parent, SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
    MessageUtils msgUtils = new MessageUtils( pluginId_ + ".plugin", this );
    String columnProperties[] = {msgUtils.getMessage("TABLE_TITLE_EJB_BEAN_NAMES"), msgUtils.getMessage("TABLE_TITLE_EJB_PROJECT_NAME")};
    Table table = getTable();
    table.setHeaderVisible(true);
    table.setLinesVisible(true);
    TableLayout layout = new TableLayout();
    for (int i = 0; i < columnProperties.length; i++)
    {
      TableColumn column = new TableColumn(table, i);
      column.setText(columnProperties[i]);
      column.setAlignment(SWT.LEFT);
      layout.addColumnData(new ColumnWeightData(50, 80, true));
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