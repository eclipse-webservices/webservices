/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060404 134913   sengpl@ca.ibm.com - Seng Phung-Lu       
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.ui.widgets;

import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.jst.j2ee.webservice.wsclient.ServiceRef;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.internal.consumption.ui.ConsumptionUIMessages;
import org.eclipse.jst.ws.internal.consumption.ui.common.HandlerServiceRefHelper;
import org.eclipse.jst.ws.internal.consumption.ui.common.HandlerServiceRefHolder;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.object.HandlerTableItem;
import org.eclipse.jst.ws.internal.ui.common.UIUtils;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.command.internal.env.ui.widgets.SimpleWidgetDataContributor;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetDataEvents;

/**
 * ConfigClientHandlersTreeWidget
 *  
 */
public class ConfigClientHandlersTableWidget extends SimpleWidgetDataContributor {

  private String pluginId_ = "org.eclipse.jst.ws.consumption.ui";

  private Composite parent_;
  private String outputLocation_;
  private Composite webServiceRefComp_;
  private Combo webServiceRefCombo_;
  private Text webServiceDescText_ = null;
  private Table handlersTable_;
  private TableViewer tableViewer_;
  private Button addButton_;
  private Button removeButton_;
  private Button genSkeletonRadioButton_;
  private Composite sourceLocationComp_;
  private Combo sourceLocationCombo_;

  private HandlerServiceRefHolder[] handlerServiceRefHolder_;
  private HandlerServiceRefHolder currentHSRH_;
  private Hashtable pathsTable_ = new Hashtable();
  private boolean isMultipleSelection_ = false;
  private boolean isGenSkeletonEnabled_;
  private String serviceRefName_;
  private int DEFAULT_COLUMN_WIDTH = 150;

  // ----TOOLTIPS Section----
  /* CONTEXT_ID HDLR0001 for the Handler Config Page */
  private final String INFOPOP_HDLR_WS_HANDLERS = "HDLR0001"; //$NON-NLS-1$

  /* CONTEXT_ID HDLR0002 for the Handler Config Page */
  private final String INFOPOP_HDLR_GEN_SKELETON = "HDLR0002"; //$NON-NLS-1$

  /* CONTEXT_ID HDLR0003 for the Handler Config Page */
  private final String INFOPOP_COMBO_SOURCE_LOC = "HDLR0003"; //$NON-NLS-1$

  /* CONTEXT_ID HDLR0004 for the Handler Config Page */
  private final String INFOPOP_WS_CLIENT_REF = "HDLR0004"; //$NON-NLS-1$

  // ------------------------

  public ConfigClientHandlersTableWidget() {
    super();
  }

  public WidgetDataEvents addControls(Composite parent, Listener statusListener) {

    UIUtils uiUtils = new UIUtils(pluginId_);

    parent_ = parent;

    // Web service reference combo
    webServiceRefComp_ = uiUtils.createComposite(parent_, 2);

    Composite displayComp = new Composite(parent_, SWT.NONE);
    GridLayout gridlayout = new GridLayout(2, false);
    displayComp.setLayout(gridlayout);
    displayComp.setLayoutData(uiUtils.createFillAll());

    final Composite handlersComp = uiUtils.createComposite(displayComp, 1);
    GridData griddata = new GridData(GridData.HORIZONTAL_ALIGN_FILL |GridData.GRAB_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING 
            | GridData.VERTICAL_ALIGN_FILL | GridData.GRAB_VERTICAL | GridData.FILL_VERTICAL);
    handlersComp.setLayoutData(griddata);
    handlersComp.setSize(130, 600);

    Composite buttonsComp = uiUtils.createComposite(displayComp, 1);
    griddata = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING);
    buttonsComp.setLayoutData(griddata);

    Text handlersText = new Text(handlersComp, SWT.READ_ONLY);
    handlersText.setText(ConsumptionUIMessages.LABEL_HANDLERS_CONFIG);
    GridData gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
    gd.horizontalSpan = 2;
    handlersText.setLayoutData(gd);

    handlersTable_ = uiUtils.createTable(handlersComp, ConsumptionUIMessages.TOOLTIP_EDIT_WS_HANDLERS, INFOPOP_HDLR_WS_HANDLERS, SWT.MULTI | SWT.FULL_SELECTION | SWT.BORDER);
    handlersTable_.setHeaderVisible(true);
    handlersTable_.setLinesVisible(true);

    // empty space
    Label wsLabel = new Label(buttonsComp, SWT.NONE);
    gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
    gd.horizontalSpan = 2;
    wsLabel.setLayoutData(gd);

    Button moveUpButton = uiUtils.createPushButton(buttonsComp, ConsumptionUIMessages.LABEL_BUTTON_MOVE_UP, null, null);
    griddata = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING);
    moveUpButton.setLayoutData(griddata);
    moveUpButton.addSelectionListener(new SelectionListener() {

      public void widgetSelected(SelectionEvent event) {
        handleMoveUpButtonSelected(event);
      }

      public void widgetDefaultSelected(SelectionEvent event) {
      }
    });

    Button moveDownButton = uiUtils.createPushButton(buttonsComp, ConsumptionUIMessages.LABEL_BUTTON_MOVE_DOWN, null, null);
    griddata = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING);
    moveDownButton.setLayoutData(griddata);
    moveDownButton.addSelectionListener(new SelectionListener() {

      public void widgetSelected(SelectionEvent event) {
        handleMoveDownButtonSelected(event);
      }

      public void widgetDefaultSelected(SelectionEvent event) {
      }
    });

    // empty space
    wsLabel = new Label(buttonsComp, SWT.NONE);
    gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
    gd.horizontalSpan = 2;
    wsLabel.setLayoutData(gd);

    addButton_ = uiUtils.createPushButton(buttonsComp, ConsumptionUIMessages.LABEL_BUTTON_ADD, null, null);
    griddata = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING);
    addButton_.setLayoutData(griddata);
    addButton_.addSelectionListener(new SelectionListener() {

      public void widgetSelected(SelectionEvent event) {
        handleAddButtonSelected(event);
      }

      public void widgetDefaultSelected(SelectionEvent event) {
      }
    });

    removeButton_ = uiUtils.createPushButton(buttonsComp, ConsumptionUIMessages.LABEL_BUTTON_REMOVE, null, null);
    griddata = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING);
    removeButton_.setLayoutData(griddata);
    removeButton_.addSelectionListener(new SelectionListener() {

      public void widgetSelected(SelectionEvent event) {
        handleRemoveButtonSelected(event);
      }

      public void widgetDefaultSelected(SelectionEvent event) {
      }
    });
    enableRemove(false);

    // table stuff
    String[] columns_ = new String[] { ConsumptionUIMessages.LABEL_HANDLER_NAME, ConsumptionUIMessages.LABLE_HANDLER_CLASS};

    final TableColumn[] tableCols = new TableColumn[columns_.length];
    for (int i = 0; i < columns_.length; i++) {
      TableColumn tableColumn = new TableColumn(handlersTable_, i);
      tableColumn.setText(columns_[i]);
      tableColumn.setAlignment(SWT.LEFT);
      tableColumn.setWidth(DEFAULT_COLUMN_WIDTH);
      tableColumn.setResizable(true);
      tableCols[i] = tableColumn;
    }

    handlersComp.addControlListener(new ControlAdapter() {
        public void controlResized(ControlEvent e) {
          Rectangle area = handlersComp.getClientArea();
          Point preferredSize = handlersTable_.computeSize(SWT.DEFAULT, SWT.DEFAULT);
          int width = area.width - 2*handlersTable_.getBorderWidth()-10;
          if (preferredSize.y > area.height + handlersTable_.getHeaderHeight()) {
            // Subtract the scrollbar width from the total column width
            // if a vertical scrollbar will be required
            Point vBarSize = handlersTable_.getVerticalBar().getSize();
            width -= vBarSize.x;
          }
          Point oldSize = handlersTable_.getSize();
          if (oldSize.x > area.width) {
            // table is getting smaller so make the columns 
            // smaller first and then resize the table to
            // match the client area width
            tableCols[0].setWidth(width/2);
            tableCols[1].setWidth(width - tableCols[0].getWidth());
            handlersTable_.setSize(area.width, area.height);
          } else {
            // table is getting bigger so make the table 
            // bigger first and then make the columns wider
            // to match the client area width
              handlersTable_.setSize(area.width, area.height);
              tableCols[0].setWidth(width/2);
              tableCols[1].setWidth(width - tableCols[0].getWidth());
          }
        }
      });    
    
    tableViewer_ = new TableViewer(handlersTable_);
    Control control = tableViewer_.getControl();

    GridData data = new GridData();
    data.horizontalAlignment = GridData.FILL;
    data.grabExcessHorizontalSpace = true;
    data.verticalAlignment = GridData.FILL;
    data.grabExcessVerticalSpace = true;
    control.setLayoutData(data);

    tableViewer_.setColumnProperties(columns_);
    tableViewer_.setContentProvider(new ListContentProvider());
    tableViewer_.setLabelProvider(new ListLabelProvider());
    tableViewer_.addSelectionChangedListener(new ISelectionChangedListener() {

      public void selectionChanged(SelectionChangedEvent event) {
        enableRemove(true);
      }
    });

    // gen skeleton check box
    genSkeletonRadioButton_ = uiUtils.createCheckbox(parent_, ConsumptionUIMessages.LABEL_BUTTON_GEN_SKELETON, ConsumptionUIMessages.TOOLTIP_BUTTON_GEN_SKELETON, INFOPOP_HDLR_GEN_SKELETON);
    genSkeletonRadioButton_.addSelectionListener(new SelectionAdapter() {

      public void widgetSelected(SelectionEvent evt) {
        handleGenSkeletonRadioButton();
      }
    });

    // source location combo
    sourceLocationComp_ = uiUtils.createComposite(parent_, 2);
    sourceLocationCombo_ = uiUtils.createCombo(sourceLocationComp_, ConsumptionUIMessages.LABEL_COMBO_SOURCE_LOC, ConsumptionUIMessages.TOOLTIP_COMBO_SOURCE_LOC, INFOPOP_COMBO_SOURCE_LOC,
        SWT.READ_ONLY);
    sourceLocationCombo_.addSelectionListener(new SelectionAdapter() {

      public void widgetSelected(SelectionEvent evt) {
        handleSourceLocationCombo(evt);
      }
    });

    return this;
  }

  // Called at start
  private void populateHandlersTable() {
    try {
      UIUtils uiUtils = new UIUtils(pluginId_);

      // process for multiple service selection
      if (isMultipleSelection_) {
        webServiceDescText_ = uiUtils.createText(webServiceRefComp_, ConsumptionUIMessages.LABEL_COMBO_WS_CLIENT_REF, 
            ConsumptionUIMessages.TOOLTIP_WS_CLIENT_REF, INFOPOP_WS_CLIENT_REF, SWT.READ_ONLY);
        if (handlerServiceRefHolder_!=null){
          currentHSRH_ = handlerServiceRefHolder_[0];
          String text = NLS.bind(ConsumptionUIMessages.MSG_TEXT_NUM_OF_CLIENTS, Integer.toString(handlerServiceRefHolder_.length));          
          webServiceDescText_.setText(text);
        }
        
        genSkeletonRadioButton_.setSelection(false);
        genSkeletonRadioButton_.setEnabled(false);
        genSkeletonRadioButton_.setVisible(false);
        
        sourceLocationComp_.setVisible(false);
        sourceLocationCombo_.setEnabled(false);
        sourceLocationCombo_.setVisible(false);
        
        refresh();

      }
      else {
        
        webServiceRefCombo_ = uiUtils.createCombo(webServiceRefComp_, ConsumptionUIMessages.LABEL_COMBO_WS_CLIENT_REF, 
            ConsumptionUIMessages.TOOLTIP_WS_CLIENT_REF, INFOPOP_WS_CLIENT_REF, SWT.READ_ONLY);
        webServiceRefCombo_.addSelectionListener(new SelectionAdapter() {
          public void widgetSelected(SelectionEvent evt) {
            handleWebServiceRefCombo(evt);
          }
        });

        String[] wsRefNames = HandlerServiceRefHelper.getAllServiceRefNames(handlerServiceRefHolder_); 
        webServiceRefCombo_.setItems(wsRefNames);
  
        if (handlerServiceRefHolder_.length < 1) {
          webServiceRefCombo_.select(0);
          webServiceRefCombo_.setEnabled(false);
        }
        else {
          if (serviceRefName_ != null) {
            int index = webServiceRefCombo_.indexOf(serviceRefName_);
            if (index != -1) webServiceRefCombo_.select(index);
          }
          else
            webServiceRefCombo_.select(0);
        }
    
        // set handler table
        HandlerServiceRefHolder hsrh = HandlerServiceRefHelper.getForServiceRefName(handlerServiceRefHolder_, webServiceRefCombo_.getText());
        if (hsrh != null) {
          currentHSRH_ = hsrh;
          List handlers = hsrh.getHandlerList();
          tableViewer_.setInput(handlers);
          tableViewer_.refresh();
        }
        
        // set output folder
        setSourceOutputLocation();
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.wst.command.env.ui.widgets.WidgetContributor#getStatus()
   */
  public IStatus getStatus() {
    IStatus finalStatus = Status.OK_STATUS;
    if (isMultipleSelection_)
      return StatusUtils.infoStatus(ConsumptionUIMessages.PAGE_DESC_MULTIPLE_CLIENTS_CONFIG);
    return finalStatus;
  }

  protected void handleAddButtonSelected(SelectionEvent event) {

    AddHandlerDialog dialog = new AddHandlerDialog(parent_.getShell(), true);
    dialog.create();
    dialog.getShell().setSize( 500, 200 );
    
    int result = dialog.open();

    if (result == Window.OK) {
      String name = dialog.getName();
      String className = dialog.getClassName();

      HandlerTableItem hi = new HandlerTableItem();
      hi.setHandlerName(name);
      hi.setHandlerClassName(className);
      
      ServiceRef serviceRef = currentHSRH_.getServiceRef();
      hi.setWsDescRef(serviceRef);
      currentHSRH_.getHandlerList().add(hi);
    }

    refresh();
  }

  protected void handleRemoveButtonSelected(SelectionEvent event) {
    handleDeleteKeyPressed();
  }

  protected void handleMoveUpButtonSelected(SelectionEvent event) {

    int index = tableViewer_.getTable().getSelectionIndex();
    if (index != -1) {
      if (index > 0) {
        if (currentHSRH_!=null) {
          List handlers = currentHSRH_.getHandlerList();
          Object object = handlers.remove(index);
          handlers.add( index - 1, object);
          tableViewer_.refresh();
        }
      }
    }

  }

  protected void handleMoveDownButtonSelected(SelectionEvent event) {

    int index = tableViewer_.getTable().getSelectionIndex();
    if (index != -1) {
      if (currentHSRH_!=null){
        List handlers = currentHSRH_.getHandlerList();
        if (index < handlers.size() - 1) {
          Object object = handlers.remove(index);
          handlers.add(index + 1, object);
          tableViewer_.refresh();
        }
      }
    }

  }

  protected void handleSourceLocationCombo(SelectionEvent event) {
    outputLocation_ = sourceLocationCombo_.getText();
    IPath outputPath = (IPath) pathsTable_.get(outputLocation_);
    currentHSRH_.setSourceOutputPath(outputPath);
  }

  protected void handleGenSkeletonRadioButton() {
    if (genSkeletonRadioButton_.isEnabled()) {
      if (genSkeletonRadioButton_.getSelection()) {
        this.isGenSkeletonEnabled_ = true;
        sourceLocationCombo_.setEnabled(true);
      }
      else {
        this.isGenSkeletonEnabled_ = false;
        sourceLocationCombo_.setEnabled(false);
      }
    }
  }

  protected void handleWebServiceRefCombo(SelectionEvent event) {

    if (webServiceRefCombo_!=null && webServiceRefCombo_.isEnabled()) {
      HandlerServiceRefHolder hsrh = HandlerServiceRefHelper.getForServiceRefName(handlerServiceRefHolder_, webServiceRefCombo_.getText());
      currentHSRH_ = hsrh;      
      refresh();
    }
    
    if (isGenSkeletonEnabled_){
      setSourceOutputLocation();
    }
    
  }

  /**
   * @return Returns the isGenSkeletonEnabled_.
   */
  public boolean isGenSkeletonEnabled_() {
    return isGenSkeletonEnabled_;
  }

  protected class ListLabelProvider extends LabelProvider implements ITableLabelProvider {

    public Image getColumnImage(Object element, int columnIndex) {
      return null;
    }

    public String getColumnText(Object object, int columnIndex) {

      if (object instanceof HandlerTableItem) {
        HandlerTableItem hti = (HandlerTableItem) object;
        if (columnIndex == 0) {

          return hti.getHandlerName() != null ? hti.getHandlerName() : "";
        }
        else if (columnIndex == 1) { return hti.getHandlerClassName() != null ? hti.getHandlerClassName() : ""; }
      }
      return null;
    }

  }

  private void enableRemove(boolean enabled) {
    removeButton_.setEnabled(enabled && !tableViewer_.getSelection().isEmpty());
  }

  protected class ListContentProvider implements IStructuredContentProvider {

    public Object[] getElements(Object element) {
      if (element instanceof Vector) { return ((Vector) element).toArray(); }
      return null;
    }

    public void dispose() {
    }

    public void inputChanged(Viewer viewer, Object old, Object newobj) {
    }

    public boolean isDeleted(Object object) {
      return false;
    }
  }

  protected void handleDeleteKeyPressed() {
    ISelection selection = tableViewer_.getSelection();
    if (selection != null && !selection.isEmpty() && (selection instanceof IStructuredSelection)) {
      int selectionIndex = handlersTable_.getSelectionIndex();
      if (currentHSRH_!=null){
        currentHSRH_.getHandlerList().remove(selectionIndex);
      }
      refresh();
    }
  }

  public void refresh() {
    
    try {
      tableViewer_.setInput(null);
      if (currentHSRH_!=null){
        List handlers = currentHSRH_.getHandlerList();
        tableViewer_.setInput(handlers);
        tableViewer_.refresh();        
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    
  }

  public void setGenSkeletonEnabled(boolean isGenSkeletonEnabled) {
    this.isGenSkeletonEnabled_ = isGenSkeletonEnabled;
    genSkeletonRadioButton_.setSelection(isGenSkeletonEnabled);

  }

  public boolean getGenSkeletonEnabled() {
    return this.isGenSkeletonEnabled_;
  }

  private void setSourceOutputLocation(){
    if (currentHSRH_!=null){    
      IProject project = currentHSRH_.getProject();
      if (project!=null){
        IPath[]locations = ResourceUtils.getAllJavaSourceLocations(project);
        String[] paths = new String[locations.length];
        for (int i = 0; i < locations.length; i++) {
          paths[i] = locations[i].toString();
          pathsTable_.put(paths[i], locations[i]);
        }
        sourceLocationCombo_.setItems(paths);
        sourceLocationCombo_.select(0);        
      }
      
      // set in model
      outputLocation_ = sourceLocationCombo_.getText();
      IPath outputPath = (IPath) pathsTable_.get(outputLocation_);
      currentHSRH_.setSourceOutputPath(outputPath);      
    }
  }
  
  /**
   * Called at the beginning 
   */
  public void internalize(){
    populateHandlersTable();
  } 
  
  /**
   * Initial selection
   * @param wsRefName
   */
  public void setServiceRefName(String wsRefName) {
    serviceRefName_ = wsRefName;
  }

  /**
   * true if multiple clients are selected
   * @param isMulitpleSelection
   */
  public void setIsMultipleSelection(boolean isMulitpleSelection) {
    this.isMultipleSelection_ = isMulitpleSelection;
  }
  
  /**
   * An array of HandlerDescriptionHolders
   * @return
   */
  public void setHandlerServiceRefHolder(HandlerServiceRefHolder[] handlerHolders){
    this.handlerServiceRefHolder_ = handlerHolders;
  }
  
  public HandlerServiceRefHolder[] getHandlerServiceRefHolder(){
    return this.handlerServiceRefHolder_;
  }
  
}