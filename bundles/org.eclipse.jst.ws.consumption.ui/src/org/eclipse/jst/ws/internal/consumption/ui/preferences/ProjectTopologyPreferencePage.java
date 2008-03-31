/*******************************************************************************
 * Copyright (c) 2003, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060227   124392 rsinha@ca.ibm.com - Rupam Kuehner
 * 20060515   141398 cbrealey@ca.ibm.com - Chris Brealey
 *******************************************************************************/

package org.eclipse.jst.ws.internal.consumption.ui.preferences;

import java.util.Vector;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jst.ws.internal.consumption.ui.plugin.WebServiceConsumptionUIPlugin;
import org.eclipse.jst.ws.internal.ui.WSUIPluginMessages;
import org.eclipse.jst.ws.internal.ui.plugin.WebServiceUIPlugin;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.common.project.facet.core.IFacetedProjectTemplate;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;



public class ProjectTopologyPreferencePage extends PreferencePage implements IWorkbenchPreferencePage, SelectionListener
{
  /*CONTEXT_ID PTPP0001 for the Project Topology Preference Page*/
  private String INFOPOP_PTPP_PAGE = WebServiceUIPlugin.ID + ".PTPP0001";

  private TableViewer serviceTypeViewer_;
  private Button serviceMoveUp_;
  private Button serviceMoveDown_;
  private Vector serviceTypes_;
  
  private TableViewer clientTypeViewer_;
  private Button moveUp_;
  private Button moveDown_;
  private Vector clientTypes_;

  private Button twoEAR_;

 /**
   * Creates preference page controls on demand.
   *   @param parent  the parent for the preference page
   */
  protected Control createContents(Composite superparent)
  {
    Composite   parent = new Composite( superparent, SWT.NONE );	
    GridLayout layout = new GridLayout();
    layout.numColumns = 1;
    parent.setLayout( layout );
    parent.setToolTipText(WSUIPluginMessages.TOOLTIP_PTPP_PAGE);
    parent.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
    PlatformUI.getWorkbench().getHelpSystem().setHelp(parent,INFOPOP_PTPP_PAGE);  

    Group serviceTypeComposite = new Group( parent, SWT.NONE );
    GridLayout servicegl = new GridLayout();
    servicegl.numColumns = 2;
    //servicegl.marginHeight = 0;
    //servicegl.marginWidth = 0;
    serviceTypeComposite.setLayout(servicegl);
    serviceTypeComposite.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ));
    serviceTypeComposite.setText(WSUIPluginMessages.LABEL_SERVICE_TYPE_NAME);
    
    Table serviceTable= new Table(serviceTypeComposite, SWT.BORDER | SWT.SINGLE | SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL);
    GridData servicegd = new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL);
    servicegd.widthHint = 256;
    serviceTable.setLayoutData(servicegd);
    serviceTable.setToolTipText(WSUIPluginMessages.TOOLTIP_SERVICE_TYPE_TABLE_VIEWER);

    serviceTypes_ = new Vector();
    serviceTypeViewer_ = new TableViewer(serviceTable);
    serviceTypeViewer_.setContentProvider(new ClientTypeContentProvider());
    serviceTypeViewer_.setLabelProvider(new ClientTypeLabelProvider());
    serviceTypeViewer_.setInput(serviceTypes_);

    TableLayout serviceTableLayout = new TableLayout();
    TableColumn serviceTableColumn = new TableColumn(serviceTable, SWT.NONE);
    serviceTableColumn.setText(WSUIPluginMessages.LABEL_SERVICE_TYPE_NAME);
    ColumnWeightData serviceColumnData = new ColumnWeightData(256, 256, false);
    serviceTableLayout.addColumnData(serviceColumnData);
    serviceTable.setLayout(serviceTableLayout);
    
    Composite servicec = new Composite(serviceTypeComposite, SWT.NONE);
    servicegl = new GridLayout();
    servicegl.numColumns = 1;
    //servicegl.marginHeight = 10;
    servicegl.marginWidth = 0;
    servicec.setLayout(servicegl);
    servicec.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));

    serviceMoveUp_ = new Button(servicec, SWT.PUSH);
    serviceMoveUp_.setText(WSUIPluginMessages.LABEL_MOVE_UP);
    serviceMoveUp_.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
    serviceMoveUp_.addSelectionListener(this);
    serviceMoveUp_.setToolTipText(WSUIPluginMessages.TOOLTIP_MOVE_UP);

    serviceMoveDown_ = new Button(servicec, SWT.PUSH);
    serviceMoveDown_.setText(WSUIPluginMessages.LABEL_MOVE_DOWN);
    serviceMoveDown_.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
    serviceMoveDown_.addSelectionListener(this);
    serviceMoveDown_.setToolTipText(WSUIPluginMessages.TOOLTIP_MOVE_DOWN);

    serviceTable.addSelectionListener(new TableSelectionListener(serviceMoveUp_, serviceMoveDown_, serviceTable));

    Group clientTypeComposite = new Group( parent, SWT.NONE );
    GridLayout gl = new GridLayout();
    gl.numColumns = 2;
    //gl.marginHeight = 0;
    //gl.marginWidth = 0;
    clientTypeComposite.setLayout(gl);
    clientTypeComposite.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ));
    clientTypeComposite.setText(WSUIPluginMessages.LABEL_CLIENT_TYPE_NAME);

    Table clientTable= new Table(clientTypeComposite, SWT.BORDER | SWT.SINGLE | SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL);
    GridData gd = new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL);
    gd.widthHint = 256;
    clientTable.setLayoutData(gd);
    clientTable.setToolTipText(WSUIPluginMessages.TOOLTIP_CLIENT_TYPE_TABLE_VIEWER);

    clientTypes_ = new Vector();
    clientTypeViewer_ = new TableViewer(clientTable);
    clientTypeViewer_.setContentProvider(new ClientTypeContentProvider());
    clientTypeViewer_.setLabelProvider(new ClientTypeLabelProvider());
    clientTypeViewer_.setInput(clientTypes_);

    TableLayout tableLayout = new TableLayout();
    TableColumn tableColumn = new TableColumn(clientTable, SWT.NONE);
    tableColumn.setText(WSUIPluginMessages.LABEL_CLIENT_TYPE_NAME);
    ColumnWeightData columnData = new ColumnWeightData(256, 256, false);
    tableLayout.addColumnData(columnData);
    clientTable.setLayout(tableLayout);

    Composite c = new Composite(clientTypeComposite, SWT.NONE);
    gl = new GridLayout();
    gl.numColumns = 1;
    //gl.marginHeight = 10;
    gl.marginWidth = 0;
    c.setLayout(gl);
    c.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));

    moveUp_ = new Button(c, SWT.PUSH);
    moveUp_.setText(WSUIPluginMessages.LABEL_MOVE_UP_2);
    moveUp_.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
    moveUp_.addSelectionListener(this);
    moveUp_.setToolTipText(WSUIPluginMessages.TOOLTIP_MOVE_UP);

    moveDown_ = new Button(c, SWT.PUSH);
    moveDown_.setText(WSUIPluginMessages.LABEL_MOVE_DOWN_2);
    moveDown_.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
    moveDown_.addSelectionListener(this);
    moveDown_.setToolTipText(WSUIPluginMessages.TOOLTIP_MOVE_DOWN);

    clientTable.addSelectionListener(new TableSelectionListener(moveUp_, moveDown_, clientTable));

    twoEAR_ = new Button(parent, SWT.CHECK | SWT.WRAP );
    twoEAR_.setText(WSUIPluginMessages.LABEL_ENABLE_TWO_EARS);
    twoEAR_.setToolTipText(WSUIPluginMessages.TOOLTIP_ENABLE_TWO_EARS);

    initializeValues();
    org.eclipse.jface.dialogs.Dialog.applyDialogFont(superparent);    

    return parent;
  }
  
  /**
   * Does anything necessary because the default button has been pressed.
   */
  protected void performDefaults()
  {
    super.performDefaults();
    initializeDefaults();
  }

  /**
   * Do anything necessary because the OK button has been pressed.
   *  @return whether it is okay to close the preference page
   */
  public boolean performOk()
  {
    storeValues();
    return true;
  }

  protected void performApply()
  {
    performOk();
  }

  /**
   * @see IWorkbenchPreferencePage
   */
  public void init(IWorkbench workbench)  { }

  
  /**
   * Adds the String elements of array a into Vector v. If String array b contains any elements
   * that are not already in array a, appends these elements from array b to the end of Vector v. 
   * @param a a String array
   * @param b a String array
   * @param v a non-null Vector
   */
  private void setVectorContentsFromTwoArrays(String[] a, String[] b, Vector v)
  {
    for (int i = 0; i < a.length; i++)
    {
      v.add(a[i]);
    }
    
    for (int i = 0; i < b.length; i++)
    {
      if (v.indexOf(b[i]) == -1)
      {
        v.add(b[i]);
      }
    }   
  }
  
  /**
   * Initializes states of the controls to their burned-in defaults.
   */
  private void initializeDefaults()
  {
    ProjectTopologyContext context = WebServiceConsumptionUIPlugin.getInstance().getProjectTopologyContext();
    serviceTypes_.clear();
    String[] defaultServiceTypes = context.getDefaultServiceTypes();
    String[] allServiceTypes = ProjectTopologyDefaults.getServiceTypes();
    setVectorContentsFromTwoArrays(defaultServiceTypes, allServiceTypes, serviceTypes_);    
    serviceTypeViewer_.refresh();
    
    clientTypes_.clear();
    String[] defaultClientTypes = context.getDefaultClientTypes();
    String[] allClientTypes = ProjectTopologyDefaults.getClientTypes();
    setVectorContentsFromTwoArrays(defaultClientTypes, allClientTypes, clientTypes_);         
    clientTypeViewer_.refresh();
    
    twoEAR_.setSelection(true);
  }
  
  /**
   * Initializes states of the controls from the preference helper.
   */
  private void initializeValues()
  {
    //Initial service project types.
    ProjectTopologyContext context = WebServiceConsumptionUIPlugin.getInstance().getProjectTopologyContext();
    String[] serviceTypesFromContext = context.getServiceTypes();
    String[] allServiceTypes = ProjectTopologyDefaults.getServiceTypes();
    setVectorContentsFromTwoArrays(serviceTypesFromContext, allServiceTypes, serviceTypes_);
    if (allServiceTypes.length > serviceTypesFromContext.length)
    {
      String[] serviceTypesArray = new String[serviceTypes_.size()];
      serviceTypes_.copyInto(serviceTypesArray);
      context.setServiceTypes(serviceTypesArray);
    }
    // refresh viewer
    serviceTypeViewer_.refresh();
    
    // select the first item in this table
    if (serviceTypeViewer_.getTable().getItemCount() > 0){
    	serviceTypeViewer_.getTable().setSelection(0);
    	serviceTypeViewer_.getTable().notifyListeners(SWT.Selection, new Event());
    }

    //Initialize client project types.
    String[] clientTypesFromContext = context.getClientTypes();
    String[] allClientTypes = ProjectTopologyDefaults.getClientTypes();
    setVectorContentsFromTwoArrays(clientTypesFromContext, allClientTypes, clientTypes_);
    if (allClientTypes.length > clientTypesFromContext.length)
    {
      String[] clientTypesArray = new String[clientTypes_.size()];
      clientTypes_.copyInto(clientTypesArray);
      context.setClientTypes(clientTypesArray);
    }
    
    // refresh viewer
    clientTypeViewer_.refresh();
    
    // select the first item in this table
    Table table = clientTypeViewer_.getTable();
	if (table.getItemCount() > 0){
    	table.setSelection(0);
    	table.notifyListeners(SWT.Selection, new Event());
    }
    
    twoEAR_.setSelection(context.isUseTwoEARs());
   }

  /**
   * Stores the values of the controls back to the preference helper.
   */
  private void storeValues()
  {
    ProjectTopologyContext context = WebServiceConsumptionUIPlugin.getInstance().getProjectTopologyContext();
    String[] serviceTypesArray = new String[serviceTypes_.size()];
    serviceTypes_.copyInto(serviceTypesArray);
    context.setServiceTypes(serviceTypesArray);    
    String[] types = new String[clientTypes_.size()];
    clientTypes_.copyInto(types);
    context.setClientTypes(types);
    context.setUseTwoEARs(twoEAR_.getSelection());
  }

  public void widgetDefaultSelected(SelectionEvent e)
  {
  }

  public void widgetSelected(SelectionEvent e)
  {
    if (e.widget == serviceMoveUp_ || e.widget == serviceMoveDown_)
    {
      int index = serviceTypeViewer_.getTable().getSelectionIndex();
      if (index != -1)
      {
        if (e.widget == serviceMoveUp_ && index > 0)
        {
          Object object = serviceTypes_.remove(index);
          serviceTypes_.insertElementAt(object, index-1);
          serviceTypeViewer_.refresh();
        }
        else if (e.widget == serviceMoveDown_ && index < serviceTypes_.size()-1)
        {
          Object object = serviceTypes_.remove(index);
          serviceTypes_.insertElementAt(object, index+1);
          serviceTypeViewer_.refresh();
        }
        serviceTypeViewer_.getTable().notifyListeners(SWT.Selection, new Event());
      }
    }
    else if (e.widget == moveUp_ || e.widget == moveDown_)
    {
      int index = clientTypeViewer_.getTable().getSelectionIndex();
      if (index != -1)
      {
        if (e.widget == moveUp_ && index > 0)
        {
          Object object = clientTypes_.remove(index);
          clientTypes_.insertElementAt(object, index - 1);
          clientTypeViewer_.refresh();
        } else if (e.widget == moveDown_ && index < clientTypes_.size() - 1)
        {
          Object object = clientTypes_.remove(index);
          clientTypes_.insertElementAt(object, index + 1);
          clientTypeViewer_.refresh();
        }
        clientTypeViewer_.getTable().notifyListeners(SWT.Selection, new Event());
      }
    }
  }

  private class ClientTypeContentProvider implements IStructuredContentProvider
  {
    /**
     * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(Object)
     */
    public Object[] getElements(Object value) 
    {
      return ((Vector)value).toArray(new String[0]);
    }

    /**
     * @see org.eclipse.jface.viewers.IContentProvider#dispose()
     */
    public void dispose() 
    {
    }

    /**
     * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(Viewer, Object, Object)
     */
    public void inputChanged(Viewer arg0, Object arg1, Object arg2) 
    {
    }
  }

  private class ClientTypeLabelProvider extends LabelProvider
  {
    public ClientTypeLabelProvider()
    {
    }

    /**
    * @see org.eclipse.jface.viewers.ILabelProvider#getImage(Object)
    */
    public Image getImage(Object arg0) 
    { 
      return null;
    }

    /**
     * @see org.eclipse.jface.viewers.ILabelProvider#getText(Object)
     */
    public String getText(Object value) 
    {
      IFacetedProjectTemplate template = ProjectFacetsManager.getTemplate( (String)value );
      
      return template.getLabel();
    }
  }
  
  protected class TableSelectionListener implements SelectionListener
  {
    private Button down;
    private Button up;
    private Table  table;

    public TableSelectionListener(Button up, Button down, Table ownerTable)
    {
      this.up = up;
      this.down = down;
      this.table = ownerTable;
    }

    public void widgetDefaultSelected(SelectionEvent e)
    {
      widgetSelected(e);
    }

    public void widgetSelected(SelectionEvent e)
    {
      int i = table.getSelectionIndex();
      if (i == table.getItemCount() - 1) down.setEnabled(false);
      else if (i > -1) down.setEnabled(true);
      if (i == 0) up.setEnabled(false);
      else if (i > -1) up.setEnabled(true);
    }
  }
}
