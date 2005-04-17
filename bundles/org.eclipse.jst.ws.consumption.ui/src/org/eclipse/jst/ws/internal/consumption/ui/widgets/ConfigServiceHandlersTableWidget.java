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
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.eclipse.core.runtime.IPath;
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
import org.eclipse.jst.ws.internal.consumption.ui.plugin.WebServiceConsumptionUIPlugin;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.object.HandlerTableItem;
import org.eclipse.jst.ws.internal.ui.common.UIUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
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
import org.eclipse.wst.command.internal.env.ui.widgets.SimpleWidgetDataContributor;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetDataEvents;
import org.eclipse.wst.command.internal.provisional.env.core.common.MessageUtils;
import org.eclipse.wst.command.internal.provisional.env.core.common.SimpleStatus;
import org.eclipse.wst.command.internal.provisional.env.core.common.Status;

/**
 * ConfigServiceHandlersTreeWidget
 *  
 */
public class ConfigServiceHandlersTableWidget extends SimpleWidgetDataContributor {

  private String pluginId_ = "org.eclipse.jst.ws.consumption.ui";
  private MessageUtils msgUtils_ = null;
  private Listener statusListener_;
  private Composite parent_;
  private boolean isGenSkeletonEnabled_;
  private String outputLocation_;

  private Table handlersTable_;
  private TableViewer tableViewer_;
  private Combo webServiceDescCombo_;
  private Button addButton_;
  private Button removeButton_;
  private Button genSkeletonRadioButton_;
  private Combo sourceLocationCombo_;
  private Text text_; // selected Text

  private HandlerTableItem[] handlers;
  private Hashtable pathsTable_ = new Hashtable();
  private Hashtable wsDescToHandlers_;
  private Hashtable wsDescToPorts_;
  private Hashtable serviceDescNameToDescObj_;
  private String descriptionName_ = null;

  private Vector orderedHandlers_;

  private int DEFAULT_COLUMN_WIDTH = 90;

  // ----TOOLTIPS Section----
  /* CONTEXT_ID SHLD0001 for the Handler Config Page */
  private final String INFOPOP_HDLR_WS_HANDLERS = "SHDL0001"; //$NON-NLS-1$
  private final String TOOLTIP_EDIT_WS_HANDLERS = "TOOLTIP_EDIT_WS_HANDLERS";

  /* CONTEXT_ID SHDL0002 for the Handler Config Page */
  private final String INFOPOP_HDLR_GEN_SKELETON = "SHDL0002"; //$NON-NLS-1$
  private final String TOOLTIP_BUTTON_GEN_SKELETON = "TOOLTIP_BUTTON_GEN_SKELETON";

  /* CONTEXT_ID SHDL0003 for the Handler Config Page */
  private final String INFOPOP_COMBO_SOURCE_LOC = "SHDL0003"; //$NON-NLS-1$
  private final String TOOLTIP_COMBO_SOURCE_LOC = "TOOLTIP_COMBO_SOURCE_LOC";

  /* CONTEXT_ID SHDL0004 for the Handler Config Page */
  private final String INFOPOP_WS_SERVICE_DESC = "SHDL0004"; //$NON-NLS-1$
  private final String TOOLTIP_WS_SERVICE_DESC = "TOOLTIP_WS_SERVICE_DESC";

  // ------------------------

  public ConfigServiceHandlersTableWidget() {
    super();
  }

  public WidgetDataEvents addControls(Composite parent, Listener statusListener) {
    msgUtils_ = new MessageUtils(WebServiceConsumptionUIPlugin.ID + ".plugin", this);
    UIUtils uiUtils = new UIUtils(msgUtils_, pluginId_);

    parent_ = parent;
    statusListener_ = statusListener;

    // Web service reference combo
    Composite webServiceRefComp = uiUtils.createComposite(parent_, 2);
    webServiceDescCombo_ = uiUtils.createCombo(webServiceRefComp, "LABEL_COMBO_WS_SERVICE_DESC", TOOLTIP_WS_SERVICE_DESC, TOOLTIP_WS_SERVICE_DESC,
        SWT.READ_ONLY);
    webServiceDescCombo_.addSelectionListener(new SelectionAdapter() {

      public void widgetSelected(SelectionEvent evt) {
        handleWebServiceDescCombo(evt);
      }
    });

    Composite displayComp = new Composite(parent, SWT.NONE);

    GridLayout gridlayout = new GridLayout(2, false);
    displayComp.setLayout(gridlayout);
    displayComp.setLayoutData(uiUtils.createFillAll());

    Composite handlersComp = uiUtils.createComposite(displayComp, 1);
    GridData griddata = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING
        | GridData.VERTICAL_ALIGN_FILL);
    handlersComp.setLayoutData(griddata);
    handlersComp.setSize(130, 600);

    Composite buttonsComp = uiUtils.createComposite(displayComp, 1);
    griddata = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING);
    buttonsComp.setLayoutData(griddata);

    Text handlersText = new Text(handlersComp, SWT.READ_ONLY);
    handlersText.setText(msgUtils_.getMessage("LABEL_HANDLERS_CONFIG"));
    GridData gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
    gd.horizontalSpan = 2;
    handlersText.setLayoutData(gd);

    handlersTable_ = uiUtils.createTable(handlersComp, TOOLTIP_EDIT_WS_HANDLERS, INFOPOP_HDLR_WS_HANDLERS, SWT.MULTI | SWT.FULL_SELECTION);
    handlersTable_.setHeaderVisible(true);
    handlersTable_.setLinesVisible(true);

    // empty space
    Label wsLabel = new Label(buttonsComp, SWT.NONE);
    gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
    gd.horizontalSpan = 2;
    wsLabel.setLayoutData(gd);

    Button moveUpButton = uiUtils.createPushButton(buttonsComp, "LABEL_BUTTON_MOVE_UP", null, null);
    griddata = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING);
    moveUpButton.setLayoutData(griddata);
    moveUpButton.addSelectionListener(new SelectionListener() {

      public void widgetSelected(SelectionEvent event) {
        handleMoveUpButtonSelected(event);
      }

      public void widgetDefaultSelected(SelectionEvent event) {
      }
    });

    Button moveDownButton = uiUtils.createPushButton(buttonsComp, "LABEL_BUTTON_MOVE_DOWN", null, null);
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

    addButton_ = uiUtils.createPushButton(buttonsComp, "LABEL_BUTTON_ADD", null, null);
    griddata = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING);
    addButton_.setLayoutData(griddata);
    addButton_.addSelectionListener(new SelectionListener() {

      public void widgetSelected(SelectionEvent event) {
        handleAddButtonSelected(event);
      }

      public void widgetDefaultSelected(SelectionEvent event) {
      }
    });

    removeButton_ = uiUtils.createPushButton(buttonsComp, "LABEL_BUTTON_REMOVE", null, null);
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
    String[] columns_ = new String[] { msgUtils_.getMessage("LABEL_HANDLER_NAME"), msgUtils_.getMessage("LABLE_HANDLER_CLASS"),
        msgUtils_.getMessage("LABEL_HANDLER_PORT")};

    for (int i = 0; i < columns_.length; i++) {
      TableColumn tableColumn = new TableColumn(handlersTable_, i);
      tableColumn.setText(columns_[i]);
      tableColumn.setAlignment(SWT.LEFT);
      tableColumn.setWidth(DEFAULT_COLUMN_WIDTH);
      tableColumn.setResizable(true);
    }

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

    genSkeletonRadioButton_ = uiUtils.createCheckbox(parent_, "LABEL_BUTTON_GEN_SKELETON", TOOLTIP_BUTTON_GEN_SKELETON, INFOPOP_HDLR_GEN_SKELETON);
    genSkeletonRadioButton_.addSelectionListener(new SelectionAdapter() {

      public void widgetSelected(SelectionEvent evt) {
        handleGenSkeletonRadioButton();
      }
    });

    Composite sourceLocationComp = uiUtils.createComposite(parent_, 2);
    sourceLocationCombo_ = uiUtils.createCombo(sourceLocationComp, "LABEL_COMBO_SOURCE_LOC", TOOLTIP_COMBO_SOURCE_LOC, INFOPOP_COMBO_SOURCE_LOC,
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

      int sizeOfHandlers = wsDescToHandlers_.size();
      String[] wsRefs = new String[sizeOfHandlers];

      String[] wsRefNames = (String[]) wsDescToHandlers_.keySet().toArray(new String[0]);
      webServiceDescCombo_.setItems(wsRefNames);

      if (sizeOfHandlers < 1) {
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
      // get webServiceRef hint
      String wsRef = webServiceDescCombo_.getText();

      if (wsDescToHandlers_.get(wsRef) != null) {
        orderedHandlers_ = (Vector) wsDescToHandlers_.get(wsRef);
        tableViewer_.setInput(orderedHandlers_);
        tableViewer_.refresh();
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }

  }

  /**
   * @return Returns the handlers.
   */
  public List getHandlersList() {
    // convert to a ArrayList and return as List
    List list = new ArrayList();
    if (orderedHandlers_ != null && !orderedHandlers_.isEmpty()) list.addAll(orderedHandlers_);

    return list;
  }

  public Hashtable getWsDescToHandlers() {
    return this.wsDescToHandlers_;
  }

  /**
   * @param handlers
   *          The handlers to set.
   */
  public void setHandlers(HandlerTableItem[] handlers) {
    this.handlers = handlers;
    populateHandlersTable();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.wst.command.env.ui.widgets.WidgetContributor#getStatus()
   */
  public Status getStatus() {
    Status finalStatus = new SimpleStatus("");

    return finalStatus;
  }

  private void handleAddButtonSelected(SelectionEvent event) {
  	
  	String descName = webServiceDescCombo_.getText();
  	WebServiceDescription serviceDesc = (WebServiceDescription) serviceDescNameToDescObj_.get(descName);
  	
    AddHandlerDialog dialog = dialog = new AddHandlerDialog(parent_.getShell(), false);
    dialog.create();
    
    String[] portNames = getPortComponentNames(serviceDesc);
    dialog.setPortNames(portNames);
    dialog.getShell().setSize( 500, 200 );
    
    int result = dialog.open();

    if (result == Window.OK) {
      String name = dialog.getName();
      String className = dialog.getClassName();
      String port = dialog.getPortName();

      HandlerTableItem hi = new HandlerTableItem();
      hi.setHandlerName(name);
      hi.setHandlerClassName(className);
      hi.setPortName(port);
      
      
      hi.setWsDescRef(serviceDesc);

      orderedHandlers_ = (Vector) wsDescToHandlers_.get(descName);
      orderedHandlers_.add(hi);

    }

    refresh();
  }

  private String[] getPortComponentNames(WebServiceDescription webServiceDesc) {
  	
  	List ports = webServiceDesc.getPortComponents();
  	if (ports!=null){
  		String[] portNames = new String[ports.size()];
  		for(int i=0;i<ports.size();i++){
  			PortComponent pc = (PortComponent)ports.get(i);
  			String pcName = pc.getPortComponentName();
  			portNames[i] = pcName;
  		}
  		return portNames;
  	}
  	return new String[0];
  }
  
  private void handleRemoveButtonSelected(SelectionEvent event) {
    handleDeleteKeyPressed();
  }

  private void handleMoveUpButtonSelected(SelectionEvent event) {

    int index = tableViewer_.getTable().getSelectionIndex();
    ISelection selected = tableViewer_.getSelection();
    if (index != -1) {
      if (index > 0) {
        orderedHandlers_ = (Vector) wsDescToHandlers_.get(webServiceDescCombo_.getText());
        Object object = orderedHandlers_.remove(index);
        orderedHandlers_.insertElementAt(object, index - 1);
        tableViewer_.refresh();
      }
    }

  }

  private void handleMoveDownButtonSelected(SelectionEvent event) {

    int index = tableViewer_.getTable().getSelectionIndex();
    ISelection selected = tableViewer_.getSelection();
    if (index != -1) {
      if (index < orderedHandlers_.size() - 1) {
        orderedHandlers_ = (Vector) wsDescToHandlers_.get(webServiceDescCombo_.getText());
        Object object = orderedHandlers_.remove(index);
        orderedHandlers_.insertElementAt(object, index + 1);
        tableViewer_.refresh();
      }
    }

  }

  public void handleSourceLocationCombo(SelectionEvent event) {
    outputLocation_ = sourceLocationCombo_.getText();
  }

  public void handleGenSkeletonRadioButton() {
    if (genSkeletonRadioButton_.isEnabled()) {
      if (genSkeletonRadioButton_.getSelection())
        this.isGenSkeletonEnabled_ = true;
      else
        this.isGenSkeletonEnabled_ = false;
    }
  }

  public void handleWebServiceDescCombo(SelectionEvent event) {

    if (webServiceDescCombo_.isEnabled()) {
      String webServiceRefName = webServiceDescCombo_.getText();
      Vector hndlers = (Vector) wsDescToHandlers_.get(webServiceRefName);
      if (hndlers != null) {
        tableViewer_.setInput(hndlers);
        tableViewer_.refresh();
      }
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

  private void handleDeleteKeyPressed() {
    //internalDispose();
    ISelection selection = tableViewer_.getSelection();
    if (selection != null && !selection.isEmpty() && (selection instanceof IStructuredSelection)) {
      int selectionIndex = handlersTable_.getSelectionIndex();
      int selectionCount = handlersTable_.getItemCount();

      orderedHandlers_ = (Vector) wsDescToHandlers_.get(webServiceDescCombo_.getText());
      orderedHandlers_.remove(selectionIndex);
      refresh();
    }
  }

  public void refresh() {
    try {
      tableViewer_.setInput(null);
      if (orderedHandlers_ != null) tableViewer_.setInput(orderedHandlers_);
      tableViewer_.refresh();
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

  public void setSourceOutputLocation(IPath[] locations) {
    this.outputLocation_ = locations[0].toString();

    String[] paths = new String[locations.length];
    for (int i = 0; i < locations.length; i++) {
      paths[i] = locations[i].toString();
      pathsTable_.put(paths[i], locations[i]);
    }
    sourceLocationCombo_.setItems(paths);
    sourceLocationCombo_.select(0);

  }

  public IPath getSourceOutputLocation() {
    IPath outputPath = (IPath) pathsTable_.get(outputLocation_);
    return outputPath;
  }

  /**
   * @return Returns the handlerClassNames.
   */
  public String[] getHandlerClassNames() {
    
    Vector handlerClasses = new Vector();
    Enumeration e = wsDescToHandlers_.keys();
    while (e.hasMoreElements()) {
      
      String wsDescName = (String)e.nextElement();
      orderedHandlers_ = (Vector) wsDescToHandlers_.get(wsDescName);
      
      if (orderedHandlers_ != null) {
        
        for (int i = 0; i < orderedHandlers_.size(); i++) {
          HandlerTableItem hti = (HandlerTableItem) orderedHandlers_.get(i);
          handlerClasses.add(hti.getHandlerClassName());
        }
      }
    }
    return (String[])handlerClasses.toArray(new String[0]);
  }

  /**
   * @param wsDescToHandlersArray_
   *          The wsDescToHandlersArray_ to set.
   */
  public void setWsRefsToHandlers(Hashtable wsDescToHandlersArray) {

    wsDescToHandlers_ = new Hashtable();
    wsDescToPorts_ = new Hashtable();
    try {

      if (wsDescToHandlersArray != null) {
        int sizeOfHandlers = wsDescToHandlersArray.size();
        String[] wsRefs = new String[sizeOfHandlers];
        // store the wsRefs
        Enumeration wsDesc = wsDescToHandlersArray.keys();
        while (wsDesc.hasMoreElements()) {
          String wsDescName = (String) wsDesc.nextElement();

          HandlerTableItem[] handlers = (HandlerTableItem[]) wsDescToHandlersArray.get(wsDescName);
          // process the handlersTable
          orderedHandlers_ = new Vector();
          HashSet portNameVector = new HashSet();

          for (int i = 0; i < handlers.length; i++) {
            orderedHandlers_.add(handlers[i]);
            String portName = handlers[i].getPortName();
            portNameVector.add(new String(portName));
          }
          wsDescToHandlers_.put(wsDescName, orderedHandlers_);
          wsDescToPorts_.put(wsDescName, (String[]) portNameVector.toArray(new String[0]));
        }
      }
      populateHandlersTable();
    }
    catch (Exception e) {
      e.printStackTrace();
    }

  }

  public void setDescriptionName(String wsDescName) {
    descriptionName_ = wsDescName;
  }

  public void setServiceDescNameToDescObj(Hashtable serviceDescNameToDescObj) {
    this.serviceDescNameToDescObj_ = serviceDescNameToDescObj;
  }
}