/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jst.ws.internal.ui.preferences;

import java.util.Vector;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jst.ws.internal.context.ProjectTopologyContext;
import org.eclipse.jst.ws.internal.context.ProjectTopologyDefaults;
import org.eclipse.jst.ws.internal.plugin.WebServicePlugin;
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
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;



public class ProjectTopologyPreferencePage extends PreferencePage implements IWorkbenchPreferencePage, SelectionListener
{
  /*CONTEXT_ID PPSD0001 for the Project Topology Preference Page*/
  private String INFOPOP_PTPP_PAGE = WebServiceUIPlugin.ID + ".PPTP0001";

  private TableViewer clientTypeViewer_;
  private Button moveUp_;
  private Button moveDown_;
  private Vector clientTypes_;
  /* CONTEXT_ID PTPP0002 for the client type table viewer on the Project Topology Preference Page */
  private final String INFOPOP_PTPP_CLIENT_TYPE = WebServiceUIPlugin.ID + ".PTPP0002";
  /* CONTEXT_ID PTPP0003 for the move up button on the Project Topology Preference Page */
  private final String INFOPOP_PTPP_MOVE_UP = WebServiceUIPlugin.ID + ".PTPP0003";
  /* CONTEXT_ID PTPP0004 for the move down button on the Project Topology Preference Page */
  private final String INFOPOP_PTPP_MOVE_DOWN = WebServiceUIPlugin.ID + ".PTPP0004";

  private Button twoEAR_;
  /* CONTEXT_ID PTPP0005 for the enable 2 EARs checkbox on the Project Topology Preference Page */
  private final String INFOPOP_PTPP_TWO_EAR_CHECKBOX = WebServiceUIPlugin.ID + ".PTPP0005";

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
    parent.setToolTipText(getMessage("%TOOLTIP_PTPP_PAGE"));
    parent.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
    PlatformUI.getWorkbench().getHelpSystem().setHelp(parent,INFOPOP_PTPP_PAGE);    

    Text clientTypeLabel = new Text(parent, SWT.READ_ONLY | SWT.WRAP);
    clientTypeLabel.setText(getMessage("%LABEL_CLIENT_TYPE_NAME"));

    Composite clientTypeComposite = new Composite(parent, SWT.NONE);
    GridLayout gl = new GridLayout();
    gl.numColumns = 2;
    gl.marginHeight = 0;
    gl.marginWidth = 0;
    clientTypeComposite.setLayout(gl);

    Table table= new Table(clientTypeComposite, SWT.BORDER | SWT.SINGLE | SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL);
    GridData gd = new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL);
    gd.widthHint = 256;
    table.setLayoutData(gd);
    table.setToolTipText(getMessage("%TOOLTIP_CLIENT_TYPE_TABLE_VIEWER"));

    clientTypes_ = new Vector();
    clientTypeViewer_ = new TableViewer(table);
    clientTypeViewer_.setContentProvider(new ClientTypeContentProvider());
    clientTypeViewer_.setLabelProvider(new ClientTypeLabelProvider());
    clientTypeViewer_.setInput(clientTypes_);

    TableLayout tableLayout = new TableLayout();
    TableColumn tableColumn = new TableColumn(table, SWT.NONE);
    tableColumn.setText(getMessage("%LABEL_CLIENT_TYPE"));
    ColumnWeightData columnData = new ColumnWeightData(256, 256, false);
    tableLayout.addColumnData(columnData);
    table.setLayout(tableLayout);

    Composite c = new Composite(clientTypeComposite, SWT.NONE);
    gl = new GridLayout();
    gl.numColumns = 1;
    gl.marginHeight = 0;
    gl.marginWidth = 0;
    c.setLayout(gl);

    moveUp_ = new Button(c, SWT.PUSH);
    moveUp_.setText(getMessage("%LABEL_MOVE_UP"));
    moveUp_.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
    moveUp_.addSelectionListener(this);
    moveUp_.setToolTipText(getMessage("%TOOLTIP_MOVE_UP"));

    moveDown_ = new Button(c, SWT.PUSH);
    moveDown_.setText(getMessage("%LABEL_MOVE_DOWN"));
    moveDown_.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
    moveDown_.addSelectionListener(this);
    moveDown_.setToolTipText(getMessage("%TOOLTIP_MOVE_DOWN"));

    twoEAR_ = new Button(parent, SWT.CHECK);
    twoEAR_.setText(getMessage("%LABEL_ENABLE_TWO_EARS"));
    twoEAR_.setToolTipText(getMessage("%TOOLTIP_ENABLE_TWO_EARS"));

    initializeValues();
    org.eclipse.jface.dialogs.Dialog.applyDialogFont(superparent);    

    return parent;
  }
  
  private String getMessage(String key) 
  {
      	return WebServiceUIPlugin.getMessage(key);
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
   * Initializes states of the controls to their burned-in defaults.
   */
  private void initializeDefaults()
  {
    clientTypes_.clear();
    String[] types = ProjectTopologyDefaults.getClientTypes();
    for (int i = 0; i < types.length; i++)
      clientTypes_.add(types[i]);
    clientTypeViewer_.refresh();
    twoEAR_.setSelection(true);
  }

  /**
   * Initializes states of the controls from the preference helper.
   */
  private void initializeValues()
  {
    ProjectTopologyContext context = WebServicePlugin.getInstance().getProjectTopologyContext();
    String[] types = context.getClientTypes();
    for (int i = 0; i < types.length; i++)
      clientTypes_.add(types[i]);
    // check whether we missed any types from the default list
    boolean missed = false;
    types = ProjectTopologyDefaults.getClientTypes();
    for (int i = 0; i < types.length; i++)
    {
      if (clientTypes_.indexOf(types[i]) == -1)
      {
        clientTypes_.add(types[i]);
        missed = true;
      }
    }
    if (missed)
    {
      types = new String[clientTypes_.size()];
      clientTypes_.copyInto(types);
      context.setClientTypes(types);
    }
    // refresh viewer
    clientTypeViewer_.refresh();
    twoEAR_.setSelection(context.isUseTwoEARs());
   }

  /**
   * Stores the values of the controls back to the preference helper.
   */
  private void storeValues()
  {
    ProjectTopologyContext context = WebServicePlugin.getInstance().getProjectTopologyContext();
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
    int index = clientTypeViewer_.getTable().getSelectionIndex();
    if (index != -1)
    {
      if (e.widget == moveUp_ && index > 0)
      {
        Object object = clientTypes_.remove(index);
        clientTypes_.insertElementAt(object, index-1);
        clientTypeViewer_.refresh();
      }
      else if (e.widget == moveDown_ && index < clientTypes_.size()-1)
      {
        Object object = clientTypes_.remove(index);
        clientTypes_.insertElementAt(object, index+1);
        clientTypeViewer_.refresh();
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
    private IConfigurationElement[] configElements_;

    public ClientTypeLabelProvider()
    {
      IExtensionRegistry reg = Platform.getExtensionRegistry();
      configElements_ = reg.getConfigurationElementsFor("org.eclipse.jst.ws.consumption.ui", "clientProjectType");
    }

    private IConfigurationElement getElementByAttribute(String name, String value)
    {
      for (int i = 0; i < configElements_.length; i++)
        if (configElements_[i].getAttribute(name).equals(value))
          return configElements_[i];
      return null;
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
      return getElementByAttribute("id", value.toString()).getAttribute("label"); 
    }
  }
}
