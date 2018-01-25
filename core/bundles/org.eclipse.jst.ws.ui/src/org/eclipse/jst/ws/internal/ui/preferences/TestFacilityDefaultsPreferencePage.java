/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060217   113169 pmoogk@ca.ibm.com - Peter Moogk
 * 20060222   118711 pmoogk@ca.ibm.com - Peter Moogk
 * 20070314   154543 makandre@ca.ibm.com - Andrew Mak, WebServiceTestRegistry is tracking extensions using label attribute instead of ID
 * 20080416   227359 makandre@ca.ibm.com - Andrew Mak, Test facilities do not respect default order in plugin customization
 * 20080805   242775 mahutch@ca.ibm.com - Mark Hutchinson, Test facility ID showing up even if no test facility extension exists
 *******************************************************************************/
package org.eclipse.jst.ws.internal.ui.preferences;

import java.util.Vector;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jst.ws.internal.context.PersistentScenarioContext;
import org.eclipse.jst.ws.internal.context.ScenarioContext;
import org.eclipse.jst.ws.internal.context.ScenarioDefaults;
import org.eclipse.jst.ws.internal.ext.test.WebServiceTestExtension;
import org.eclipse.jst.ws.internal.ext.test.WebServiceTestRegistry;
import org.eclipse.jst.ws.internal.plugin.WebServicePlugin;
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
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;



public class TestFacilityDefaultsPreferencePage extends PreferencePage implements IWorkbenchPreferencePage, SelectionListener
{
  
  private TableViewer webServiceTestTypeViewer_; 
  private Button moveUp_;
  private Button moveDown_;
  private Vector webServiceTestTypes_;
  
  /*CONTEXT_ID PPSD0001 for the Scenario Defaults Preference Page*/
  private String INFOPOP_PPSD_PAGE = WebServiceUIPlugin.ID + ".PPSD0001";
  //
  private Button launchSample;
  /*CONTEXT_ID PPSD0002 for the launch sample check box on the Scenario Defaults Preference Page*/
  private final String INFOPOP_PPSD_CHECKBOX_LAUNCH_SAMPLE = WebServiceUIPlugin.ID + ".PPSD0002";
  //

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
    parent.setToolTipText(WSUIPluginMessages.TOOLTIP_PPSD_PAGE);
    parent.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
    PlatformUI.getWorkbench().getHelpSystem().setHelp(parent,INFOPOP_PPSD_PAGE);    

    launchSample = createCheckBox(parent, WSUIPluginMessages.BUTTON_LAUNCH_SAMPLE);
    launchSample.setToolTipText(WSUIPluginMessages.TOOLTIP_PPSD_CHECKBOX_LAUNCH_SAMPLE);
    PlatformUI.getWorkbench().getHelpSystem().setHelp(launchSample, INFOPOP_PPSD_CHECKBOX_LAUNCH_SAMPLE);

    
    Text testServiceTypeLabel = new Text(parent, SWT.READ_ONLY | SWT.WRAP);
    testServiceTypeLabel.setText(WSUIPluginMessages.LABEL_SAMPLE_TYPES);
    testServiceTypeLabel.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

    Composite webServiceTestTypeComposite = new Composite(parent, SWT.NONE);
    GridLayout gl = new GridLayout();
    gl.numColumns = 2;
    gl.marginHeight = 0;
    gl.marginWidth = 0;
    webServiceTestTypeComposite.setLayout(gl);
    webServiceTestTypeComposite.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

    Table table= new Table(webServiceTestTypeComposite, SWT.BORDER | SWT.SINGLE | SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL);
    GridData gd = new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL);
    gd.widthHint = 256;
    table.setLayoutData(gd);
    table.setToolTipText(WSUIPluginMessages.TOOLTIP_CLIENT_TYPE_TABLE_VIEWER);

    webServiceTestTypes_ = new Vector();
    webServiceTestTypeViewer_ = new TableViewer(table);
    webServiceTestTypeViewer_.setContentProvider(new WebServiceTestTypeContentProvider());
    webServiceTestTypeViewer_.setLabelProvider(new WebServiceTestTypeLabelProvider());
    webServiceTestTypeViewer_.setInput(webServiceTestTypes_);

    TableLayout tableLayout = new TableLayout();
    TableColumn tableColumn = new TableColumn(table, SWT.NONE);
    tableColumn.setText(WSUIPluginMessages.LABEL_CLIENT_TYPE_NAME);
    ColumnWeightData columnData = new ColumnWeightData(256, 256, false);
    tableLayout.addColumnData(columnData);
    table.setLayout(tableLayout);

    Composite c = new Composite(webServiceTestTypeComposite, SWT.NONE);
    gl = new GridLayout();
    gl.numColumns = 1;
    gl.marginHeight = 10;
    gl.marginWidth = 0;
    c.setLayout(gl);
    c.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));

    moveUp_ = new Button(c, SWT.PUSH);
    moveUp_.setText(WSUIPluginMessages.LABEL_MOVE_UP);
    moveUp_.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
    moveUp_.addSelectionListener(this);
    moveUp_.setToolTipText(WSUIPluginMessages.TOOLTIP_MOVE_UP);

    moveDown_ = new Button(c, SWT.PUSH);
    moveDown_.setText(WSUIPluginMessages.LABEL_MOVE_DOWN);
    moveDown_.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
    moveDown_.addSelectionListener(this);
    moveDown_.setToolTipText(WSUIPluginMessages.TOOLTIP_MOVE_DOWN);

    table.addSelectionListener(new TableSelectionListener(moveUp_, moveDown_, table));
    
    initializeValues();
    org.eclipse.jface.dialogs.Dialog.applyDialogFont(superparent);    

    return parent;
  }

  private Button createCheckBox( Composite parent, String text )
  {
    Button button = new Button( parent, SWT.CHECK );
    button.setText( text );
    return button;
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
    try{
      storeValues();
    }catch (Exception exc){
    	exc.printStackTrace();}
    return true;
  }

  protected void performApply()
  {
    try{
  	performOk();
    }catch(NullPointerException exc){
      exc.printStackTrace();
    }
  }

  /**
   * @see IWorkbenchPreferencePage
   */
  public void init(IWorkbench workbench)  { }

  /**
   * Initializes states of the controls to their burned-in defaults.
   */
  private void initializeDefaults()
  {
    PersistentScenarioContext context = (PersistentScenarioContext) 
    	WebServicePlugin.getInstance().getScenarioContext();
    
    webServiceTestTypes_.clear();
    String[] types = context.getDefaultWebServiceTestIds();
    for (int i = 0; i < types.length; i++) {
      //mh: check that the test facility extension exists before adding it to the list (bug 242775)
      if ( WebServiceTestRegistry.getInstance().getWebServiceExtensionsById(types[i]) != null) 
      {
    	  webServiceTestTypes_.add(types[i]);
      }
    }
    webServiceTestTypeViewer_.refresh();
  }

  /**
   * Initializes states of the controls from the preference helper.
   */
  private void initializeValues()
  {
  	ScenarioContext  context  = WebServicePlugin.getInstance().getScenarioContext();
    ScenarioDefaults defaults = new ScenarioDefaults();
    
    launchSample.setSelection( context.isLaunchSampleEnabled());
        
    String[] types = context.getWebServiceTestIds();
    
    for (int i = 0; i < types.length; i++) {
      //mh: check that the test facility extension exists before adding it to the list (bug 242775)
      if ( WebServiceTestRegistry.getInstance().getWebServiceExtensionsById(types[i]) != null) 
      {
    	  webServiceTestTypes_.add(types[i]);
      }
    }
    // check whether we missed any types from the default list
    boolean missed = false;
    types = defaults.getWebServiceTestIds();
    for (int i = 0; i < types.length; i++)
    {
      if (webServiceTestTypes_.indexOf(types[i]) == -1)
      {   	
    	  //mh: check that the test facility extension exists before adding it to the list (bug 242775)
          if ( WebServiceTestRegistry.getInstance().getWebServiceExtensionsById(types[i]) != null) 
          {
    		webServiceTestTypes_.add(types[i]);
    		missed = true;
          }
      }
    }
    if (missed)
    {
      types = new String[webServiceTestTypes_.size()];
      webServiceTestTypes_.copyInto(types);
      context.setWebServiceTestIds(types);
    }
    // refresh viewer
    webServiceTestTypeViewer_.refresh();
    
    // Select the first item in the table
    Table table = webServiceTestTypeViewer_.getTable();
	if (table.getItemCount() > 0){
    	table.setSelection(0);
    	table.notifyListeners(SWT.Selection, new Event());
    }
   }

  /**
   * Stores the values of the controls back to the preference helper.
   */
  private void storeValues()
  {
    ScenarioContext context = WebServicePlugin.getInstance().getScenarioContext();
    String[] types = new String[webServiceTestTypes_.size()];
    webServiceTestTypes_.copyInto(types);
    context.setWebServiceTestIds(types);
    
    context.setLaunchSampleEnabled(launchSample.getSelection());
  }

  public void widgetDefaultSelected(SelectionEvent e)
  {
  }

  public void widgetSelected(SelectionEvent e)
  {
    int index = webServiceTestTypeViewer_.getTable().getSelectionIndex();
    if (index != -1)
    {
      if (e.widget == moveUp_ && index > 0)
      {
        Object object = webServiceTestTypes_.remove(index);
        webServiceTestTypes_.insertElementAt(object, index-1);
        webServiceTestTypeViewer_.refresh();
      }
      else if (e.widget == moveDown_ && index < webServiceTestTypes_.size()-1)
      {
        Object object = webServiceTestTypes_.remove(index);
        webServiceTestTypes_.insertElementAt(object, index+1);
        webServiceTestTypeViewer_.refresh();
      }
      webServiceTestTypeViewer_.getTable().notifyListeners(SWT.Selection, new Event());
    }
  }

  private class WebServiceTestTypeContentProvider implements IStructuredContentProvider
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

  private class WebServiceTestTypeLabelProvider extends LabelProvider
  {
    public WebServiceTestTypeLabelProvider()
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
      WebServiceTestExtension extension = (WebServiceTestExtension) WebServiceTestRegistry.getInstance()
      		.getWebServiceExtensionsById(value.toString());
      if (extension != null)
    	  return extension.getLabel();
      return value.toString();
    }
  }
  
  protected class TableSelectionListener implements SelectionListener
  {
    Button up;
    Button down;
    Table  table;

    public void widgetDefaultSelected(SelectionEvent arg0)
    {
      widgetSelected(arg0);
    }

    public void widgetSelected(SelectionEvent arg0)
    {
      int i = table.getSelectionIndex();
      if (i == table.getItemCount() - 1) down.setEnabled(false);
      else if (i > -1) down.setEnabled(true);
      if (i == 0) up.setEnabled(false);
      else if (i > -1) up.setEnabled(true);
    }

    public TableSelectionListener(Button up, Button down, Table table)
    {
      this.up = up;
      this.down = down;
      this.table = table;
    }
  }
}
