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
import org.eclipse.jst.j2ee.webservice.wsdd.PortComponent;
import org.eclipse.jst.j2ee.webservice.wsdd.WebServiceDescription;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.internal.consumption.ui.ConsumptionUIMessages;
import org.eclipse.jst.ws.internal.consumption.ui.common.HandlerDescriptionHelper;
import org.eclipse.jst.ws.internal.consumption.ui.common.HandlerDescriptionHolder;
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
 * ConfigServiceHandlersTreeWidget
 * 
 */
public class ConfigServiceHandlersTableWidget extends SimpleWidgetDataContributor {

  private String pluginId_ = "org.eclipse.jst.ws.consumption.ui";
  
  private Composite parent_;
  private Table handlersTable_;
  private TableViewer tableViewer_;
  private Composite webServiceDescComp_;
  private Combo webServiceDescCombo_ = null;
  private Text webServiceDescText_ = null;
  private Button addButton_;
  private Button removeButton_;
  private Button genSkeletonRadioButton_;
  private Composite sourceLocationComp_;
  private Combo sourceLocationCombo_;
  
  private HandlerDescriptionHolder[] handlerDescriptionHolder_;
  private HandlerDescriptionHolder currentHDH_;
  private Hashtable pathsTable_ = new Hashtable();  // available paths for combo  
  private String outputLocation_;                   // selected path from combo
  private String descriptionName_ = null;
  private boolean isMultipleSelection_ = false;
  private boolean isGenSkeletonEnabled_;

  private int DEFAULT_COLUMN_WIDTH = 100;

  // ----TOOLTIPS Section----
  /* CONTEXT_ID SHLD0001 for the Handler Config Page */
  private final String INFOPOP_HDLR_WS_HANDLERS = "SHDL0001"; //$NON-NLS-1$

  /* CONTEXT_ID SHDL0002 for the Handler Config Page */
  private final String INFOPOP_HDLR_GEN_SKELETON = "SHDL0002"; //$NON-NLS-1$

  /* CONTEXT_ID SHDL0003 for the Handler Config Page */
  private final String INFOPOP_COMBO_SOURCE_LOC = "SHDL0003"; //$NON-NLS-1$

  /* CONTEXT_ID SHDL0004 for the Handler Config Page */
  private final String INFOPOP_WS_SERVICE_DESC = "SHDL0004"; //$NON-NLS-1$

  // ------------------------

  public ConfigServiceHandlersTableWidget() {
    super();
  }

  public WidgetDataEvents addControls(Composite parent, Listener statusListener) {
    UIUtils uiUtils = new UIUtils(pluginId_);

    parent_ = parent;

    int maxWidth = 130;
    int maxHeight = 600;

    // Web service description composite (combo/text to be created later)
    webServiceDescComp_ = uiUtils.createComposite(parent_, 2);
    
    Composite displayComp = new Composite(parent, SWT.NONE);

    GridLayout gridlayout = new GridLayout(2, false);
    displayComp.setLayout(gridlayout);
    displayComp.setLayoutData(uiUtils.createFillAll());

    final Composite handlersComp = uiUtils.createComposite(displayComp, 1);
    GridData griddata = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING
        | GridData.VERTICAL_ALIGN_FILL | GridData.GRAB_VERTICAL | GridData.FILL_VERTICAL);
    handlersComp.setLayoutData(griddata);
    handlersComp.setSize(maxWidth, maxHeight);

    Composite buttonsComp = uiUtils.createComposite(displayComp, 1);
    griddata = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING);
    buttonsComp.setLayoutData(griddata);

    Text handlersText = new Text(handlersComp, SWT.READ_ONLY);
    handlersText.setText(ConsumptionUIMessages.LABEL_HANDLERS_CONFIG);
    GridData gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
    gd.horizontalSpan = 2;
    handlersText.setLayoutData(gd);

    handlersTable_ = uiUtils.createTable(handlersComp, ConsumptionUIMessages.TOOLTIP_EDIT_WS_HANDLERS, INFOPOP_HDLR_WS_HANDLERS, SWT.MULTI
        | SWT.FULL_SELECTION | SWT.BORDER);
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

    // table stuff here
    String[] columns_ = new String[] { ConsumptionUIMessages.LABEL_HANDLER_NAME, ConsumptionUIMessages.LABLE_HANDLER_CLASS,
        ConsumptionUIMessages.LABEL_HANDLER_PORT};

    final TableColumn[] tableCols = new TableColumn[columns_.length];
    for (int i = 0; i < columns_.length; i++) {
      TableColumn tableColumn = new TableColumn(handlersTable_, SWT.LEFT);
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
        int width = area.width - 2 * handlersTable_.getBorderWidth() - 10;
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
          tableCols[0].setWidth(width / 3);
          tableCols[1].setWidth(width / 3);
          tableCols[2].setWidth(width - (tableCols[0].getWidth() + tableCols[1].getWidth()));
          handlersTable_.setSize(area.width, area.height);
        }
        else {
          // table is getting bigger so make the table
          // bigger first and then make the columns wider
          // to match the client area width
          handlersTable_.setSize(area.width, area.height);
          tableCols[0].setWidth(width / 3);
          tableCols[1].setWidth(width / 3);
          tableCols[2].setWidth(width - (tableCols[0].getWidth() + tableCols[1].getWidth()));
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

    genSkeletonRadioButton_ = uiUtils.createCheckbox(parent_, ConsumptionUIMessages.LABEL_BUTTON_GEN_SKELETON,
        ConsumptionUIMessages.TOOLTIP_BUTTON_GEN_SKELETON, INFOPOP_HDLR_GEN_SKELETON);
    griddata = new GridData(GridData.VERTICAL_ALIGN_BEGINNING | GridData.GRAB_HORIZONTAL | GridData.VERTICAL_ALIGN_FILL);
    genSkeletonRadioButton_.setLayoutData(griddata);
    genSkeletonRadioButton_.addSelectionListener(new SelectionAdapter() {

      public void widgetSelected(SelectionEvent evt) {
        handleGenSkeletonRadioButton();
      }
    });

    sourceLocationComp_ = uiUtils.createComposite(parent_, 2);
    sourceLocationCombo_ = uiUtils.createCombo(sourceLocationComp_, ConsumptionUIMessages.LABEL_COMBO_SOURCE_LOC,
        ConsumptionUIMessages.TOOLTIP_COMBO_SOURCE_LOC, INFOPOP_COMBO_SOURCE_LOC, SWT.READ_ONLY);
    sourceLocationCombo_.addSelectionListener(new SelectionAdapter() {

      public void widgetSelected(SelectionEvent evt) {
        handleSourceLocationCombo(evt);
      }
    });
    return this;
  }

  // Called at start and only for single service selection 
  private void populateHandlersTable() {
    try {
      UIUtils uiUtils = new UIUtils(pluginId_);
     
      // process for multiple service selection
      if (isMultipleSelection_) {
        parent_.getShell().setText(ConsumptionUIMessages.PAGE_DESC_MULTIPLE_SERVICES_CONFIG);
        
        webServiceDescText_ = uiUtils.createText(webServiceDescComp_, ConsumptionUIMessages.LABEL_COMBO_WS_SERVICE_DESC, 
            ConsumptionUIMessages.TOOLTIP_WS_SERVICE_DESC, INFOPOP_WS_SERVICE_DESC, SWT.READ_ONLY);
        if (handlerDescriptionHolder_!=null){
          currentHDH_ = handlerDescriptionHolder_[0];
          String text = NLS.bind(ConsumptionUIMessages.MSG_TEXT_NUM_OF_SERVICES, Integer.toString(handlerDescriptionHolder_.length));
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
        // populate the table for a single service selection
        
        webServiceDescCombo_ = uiUtils.createCombo(webServiceDescComp_, ConsumptionUIMessages.LABEL_COMBO_WS_SERVICE_DESC,
            ConsumptionUIMessages.TOOLTIP_WS_SERVICE_DESC, INFOPOP_WS_SERVICE_DESC, SWT.READ_ONLY);
        webServiceDescCombo_.addSelectionListener(new SelectionAdapter() {
    
          public void widgetSelected(SelectionEvent evt) {
            handleWebServiceDescCombo(evt);
          }
        });
        
        String[] wsDescNames = HandlerDescriptionHelper.getAllDescriptionNames(handlerDescriptionHolder_);
        webServiceDescCombo_.setItems(wsDescNames);

        if (handlerDescriptionHolder_.length < 1) {
          webServiceDescCombo_.select(0);
          webServiceDescCombo_.setEnabled(false);
        }
        else {
          if (descriptionName_ != null) {
            int index = webServiceDescCombo_.indexOf(descriptionName_);
            if (index != -1) webServiceDescCombo_.select(index);
          }
          else
            webServiceDescCombo_.select(0);
        }
       
        // set Descriptions
        HandlerDescriptionHolder hdh = HandlerDescriptionHelper.getForDescriptionName(handlerDescriptionHolder_, webServiceDescCombo_.getText());
        if (hdh!=null){
          currentHDH_ = hdh;
          List handlers = hdh.getHandlerList();
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
      return StatusUtils.infoStatus(ConsumptionUIMessages.PAGE_DESC_MULTIPLE_SERVICES_CONFIG);
    return finalStatus;
  }

  protected void handleAddButtonSelected(SelectionEvent event) {
    AddHandlerDialog dialog = new AddHandlerDialog(parent_.getShell(), false);
    dialog.setIsMultipleServices(isMultipleSelection_);
    dialog.create();
    
    WebServiceDescription serviceDesc = null;
    if (!isMultipleSelection_ ){
      if (currentHDH_!=null) {
        serviceDesc =  currentHDH_.getDescriptionObject();
        String[] portNames = getPortComponentNames(serviceDesc);
        dialog.setPortNames(portNames);
      }
    }

    dialog.getShell().setSize(500, 200);

    int result = dialog.open();

    if (result == Window.OK) {
      String name = dialog.getName();
      String className = dialog.getClassName();
      String port = dialog.getPortName();
      if (port == null)
        port = "*";

      HandlerTableItem hi = new HandlerTableItem();
      hi.setHandlerName(name);
      hi.setHandlerClassName(className);
      hi.setPortName(port);

      if (serviceDesc!=null){
        hi.setWsDescRef(serviceDesc);
      }
      
      if (currentHDH_!=null){
        List handlers = currentHDH_.getHandlerList();
        handlers.add(hi);
      }

    }

    refresh();
  }

  private String[] getPortComponentNames(WebServiceDescription webServiceDesc) {

    List ports = webServiceDesc.getPortComponents();
    if (ports != null) {
      String[] portNames = new String[ports.size()];
      for (int i = 0; i < ports.size(); i++) {
        PortComponent pc = (PortComponent) ports.get(i);
        String pcName = pc.getPortComponentName();
        portNames[i] = pcName;
      }
      return portNames;
    }
    return new String[0];
  }

  protected void handleRemoveButtonSelected(SelectionEvent event) {
    handleDeleteKeyPressed();
  }

  protected void handleMoveUpButtonSelected(SelectionEvent event) {

    int index = tableViewer_.getTable().getSelectionIndex();
    if (index != -1) {
      if (index > 0) {
        if (currentHDH_!=null){
            List handlers = currentHDH_.getHandlerList();
            Object object = handlers.remove(index);
            handlers.add(index - 1, object);
          }
        tableViewer_.refresh();
      }
    }

  }

  protected void handleMoveDownButtonSelected(SelectionEvent event) {

    int index = tableViewer_.getTable().getSelectionIndex();
    if (index != -1) {
      if (currentHDH_!=null){
        List handlers = currentHDH_.getHandlerList();
        if (index < handlers.size() - 1) {
          Object object = handlers.remove(index);
          handlers.add(index+1, object);
        }
        tableViewer_.refresh();
      }
    }

  }

  protected void handleSourceLocationCombo(SelectionEvent event) {
    outputLocation_ = sourceLocationCombo_.getText();
    IPath outputPath = (IPath) pathsTable_.get(outputLocation_);
    currentHDH_.setSourceOutputPath(outputPath);    
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

  protected void handleWebServiceDescCombo(SelectionEvent event) {

    if (webServiceDescCombo_!=null && webServiceDescCombo_.isEnabled()) {
      HandlerDescriptionHolder hdh = HandlerDescriptionHelper.getForDescriptionName(handlerDescriptionHolder_, webServiceDescCombo_.getText());
      currentHDH_ = hdh;      
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
        else if (columnIndex == 1) {
          return hti.getHandlerClassName() != null ? hti.getHandlerClassName() : "";
        }
        else {
          return hti.getPortName() != null ? hti.getPortName() : "";
        }
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
    // internalDispose();
    ISelection selection = tableViewer_.getSelection();
    if (selection != null && !selection.isEmpty() && (selection instanceof IStructuredSelection)) {
      int selectionIndex = handlersTable_.getSelectionIndex();
      if (currentHDH_!=null){
        currentHDH_.getHandlerList().remove(selectionIndex);
      }
      refresh();
    }
  }

  public void refresh() {
    try {
      tableViewer_.setInput(null);
      if (currentHDH_!=null){
        List handlers = currentHDH_.getHandlerList();
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
    IProject project = currentHDH_.getProject();
    if (project!=null){
      IPath[] locations = ResourceUtils.getAllJavaSourceLocations(project);
      
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
    currentHDH_.setSourceOutputPath(outputPath);  
    
  }
  
  /**
   * An array of HandlerDescriptionHolders
   * @return
   */
  public void setHandlerDescriptionHolders(HandlerDescriptionHolder[] handlerHolders){
    this.handlerDescriptionHolder_ = handlerHolders;
  }
  
  public HandlerDescriptionHolder[] getHandlerDescriptionHolders(){
    return this.handlerDescriptionHolder_;
  }
  
  /**
   * For getting the handlers to apply for multiple services
   * @return
   */
  public List getHandlersList(){
    if (handlerDescriptionHolder_[0]!=null)
      return handlerDescriptionHolder_[0].getHandlerList();
    return null;
  }
  
  public void setDescriptionName(String wsDescName) {
    descriptionName_ = wsDescName;
  }

  public void setIsMultipleSelection(boolean isMulitpleSelection) {
    this.isMultipleSelection_ = isMulitpleSelection;
  }

  public void internalize(){
    populateHandlersTable();
  }

}