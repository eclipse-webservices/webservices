/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.ui.wizard;

import java.util.Hashtable;
import java.util.Vector;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jst.j2ee.internal.servertarget.IServerTargetConstants;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.jst.ws.internal.common.ServerUtils;
import org.eclipse.jst.ws.internal.consumption.ui.ConsumptionUIMessages;
import org.eclipse.jst.ws.internal.consumption.ui.plugin.WebServiceConsumptionUIPlugin;
import org.eclipse.jst.ws.internal.consumption.ui.wsrt.RuntimeDescriptor;
import org.eclipse.jst.ws.internal.consumption.ui.wsrt.WebServiceRuntimeExtensionUtils2;
import org.eclipse.jst.ws.internal.data.TypeRuntimeServer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.help.IWorkbenchHelpSystem;
import org.eclipse.wst.server.core.IModuleType;
import org.eclipse.wst.server.core.IRuntimeType;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.IServerType;
import org.eclipse.wst.server.core.ServerCore;
import org.eclipse.wst.server.ui.ServerUICore;

public class RuntimeServerSelectionDialog extends Dialog implements Listener {

  private Shell thisShell;
  private Group viewSelectionGroup_;
  private Text messageBanner_;
  private Composite primaryGroup_;
  private Group runtimesGroup_;
  private Group serversGroup_;
  private Tree runtimesList_;
  private Tree serverList_;
  private Button viewSelectionByRuntimeButton_;
  private Button viewSelectionByServerButton_;
  private Button viewSelectionByExploreButton_;

  /*
   * CONTEXT_ID PWRS0001 for the selection of runtime, server and project combination
   */
  private String INFOPOP_PWRS_DIALOG = WebServiceConsumptionUIPlugin.ID + ".PWRS0001";
  /* CONTEXT_ID PWRS0002 for the runtime selection on in the dailog */
  private String INFOPOP_PWRS_LIST_RUNTIMES = WebServiceConsumptionUIPlugin.ID + ".PWRS0002";
  /* CONTEXT_ID PWRS0003 for the server selection in the dialog */
  private String INFOPOP_PWRS_LIST_SERVERS = WebServiceConsumptionUIPlugin.ID + ".PWRS0003";
  /*
   * CONTEXT_ID PWRS0004 for the EJB Project combo box of the runtime selection dialog
   */
  //  private String INFOPOP_PWRS_EJB_PROJECT = WebServiceConsumptionUIPlugin.ID
  // + ".PWRS0004";
  /*
   * CONTEXT_ID PWRS0005 for the runtime view radio button of the runtime selection dialog
   */
  private String INFOPOP_PWRS_RADIO_RUNTIME = WebServiceConsumptionUIPlugin.ID + ".PWRS0005";
  /*
   * CONTEXT_ID PWRS0006 for the server view radio button of the runtime selection dialog
   */
  private String INFOPOP_PWRS_RADIO_SERVER = WebServiceConsumptionUIPlugin.ID + ".PWRS0006";
  /*
   * CONTEXT_ID PWRS0007 for the explore view radio button of the runtime selection dialog
   */
  private String INFOPOP_PWRS_RADIO_EXPLORE = WebServiceConsumptionUIPlugin.ID + ".PWRS0007";
  /*
   * CONTEXT_ID PWRS0008 for the Web Project combo box of the runtime selection dialog
   */
  //  private String INFOPOP_PWRS_COMBO_PROJECT =
  // WebServiceConsumptionUIPlugin.ID + ".PWRS0008";
  /*
   * CONTEXT_ID PWRS0009 for the J2EE version combo box of the runtime selection dialog
   */
  private String INFOPOP_PWRS_J2EE_VERSION = WebServiceConsumptionUIPlugin.ID + ".PWRS0009";

  private Hashtable serverLabels_;
  private Hashtable existingServersTable_;
  private String defaultServer_;
  private String defaultRuntime_;
  private String typeId_;

  private RuntimeDescriptor selectedRuntime_;

  private IServer selectedServer_;
  private String selectedServerLabel_;
  private String selectedServerFactoryID_;
  private boolean isExistingServer_;
  private boolean validateOn_ = false;
  private byte selectionMode_;
  private final byte MODE_SERVICE = (byte) 0;
  private final String SERVER_TYPES_ICON = "icons/servers/servers_obj.gif";
  private final String EXISTING_SERVERS_ICON = "icons/servers/existing_server_obj.gif";
  private String serverInstanceID_;

  public RuntimeServerSelectionDialog(Shell shell, byte mode, TypeRuntimeServer ids, String j2eeVersion) {
    super(shell);
    selectionMode_ = mode;
    typeId_ = ids.getTypeId();
    defaultRuntime_ = ids.getRuntimeId();
    defaultServer_ = ids.getServerId();
    serverInstanceID_ = ids.getServerInstanceId();
    setIsExistingServer(ids.getServerInstanceId() != null);
    serverLabels_ = new Hashtable();
    existingServersTable_ = new Hashtable();
  }

  public TypeRuntimeServer getTypeRuntimeServer() {
    TypeRuntimeServer ids = new TypeRuntimeServer();

    ids.setTypeId(typeId_);
    if (selectedRuntime_ != null)
      ids.setRuntimeId(selectedRuntime_.getId());
    
    if( selectedServerFactoryID_ == null )
    {
      ids.setServerId( defaultServer_ );
      ids.setServerInstanceId( serverInstanceID_ );
    }
    else
    {
      ids.setServerId(selectedServerFactoryID_);

      if (isExistingServer_ && selectedServer_ != null) 
      {
        ids.setServerInstanceId(selectedServer_.getId());
      }
    }
    
    return ids;
  }

  private boolean getIsExistingServer() {
    return isExistingServer_;
  }

  private void setIsExistingServer(boolean isExisting) {
    isExistingServer_ = isExisting;
  }

  protected Control createContents(Composite parent) {
    Composite comp = (Composite) super.createContents(parent);
    parent.getShell().pack();
    return comp;
  }

  protected Control createDialogArea(Composite parent) {
    validateOn_ = false;
    thisShell = parent.getShell();
    if (thisShell == null) {
      thisShell = createShell();
    }
    Composite composite = (Composite) super.createDialogArea(parent);
    GridLayout gl;
    GridData gd;
    // Window title
    if (selectionMode_ == MODE_SERVICE)
      thisShell.setText(ConsumptionUIMessages.PAGE_TITLE_WS_RUNTIME_SELECTION);
    else
      thisShell.setText(ConsumptionUIMessages.PAGE_TITLE_WS_CLIENT_RUNTIME_SELECTION);
    
    IWorkbenchHelpSystem helpSystem = PlatformUI.getWorkbench().getHelpSystem();
    
    helpSystem.setHelp(thisShell, INFOPOP_PWRS_DIALOG);
    // Dialog description banner
    messageBanner_ = new Text(composite, SWT.READ_ONLY | SWT.WRAP);
    messageBanner_.setText(ConsumptionUIMessages.PAGE_DESC_WS_RUNTIME_SELECTION + "\n" + "      "); // reserves a second line for message display
    messageBanner_.setToolTipText(ConsumptionUIMessages.PAGE_DESC_WS_RUNTIME_SELECTION);
    gd = new GridData(GridData.FILL_HORIZONTAL);
    messageBanner_.setLayoutData(gd);
    //  -----------------------------------------------------------------------//
    new Label(composite, SWT.HORIZONTAL);
    //  -----------------------------------------------------------------------//
    // Selection
    thisShell.setToolTipText(ConsumptionUIMessages.TOOLTIP_PWRS_PAGE);
    viewSelectionGroup_ = new Group(composite, SWT.NONE);
    gl = new GridLayout();
    gl.marginHeight = 0;
    gl.marginWidth = 0;
    viewSelectionGroup_.setLayout(gl);
    gd = new GridData(GridData.FILL_HORIZONTAL);
    viewSelectionGroup_.setLayoutData(gd);
    if (selectionMode_ == MODE_SERVICE)
      viewSelectionGroup_.setText(ConsumptionUIMessages.LABEL_SELECTION_VIEW_TITLE);
    else
      viewSelectionGroup_.setText(ConsumptionUIMessages.LABEL_CLIENT_SELECTION_VIEW_TITLE);
    viewSelectionByServerButton_ = new Button(viewSelectionGroup_, SWT.RADIO);
    viewSelectionByServerButton_.setText(ConsumptionUIMessages.LABEL_SELECTION_VIEW_SERVER);
    viewSelectionByServerButton_.addListener(SWT.Selection, this);
    viewSelectionByServerButton_.setToolTipText(ConsumptionUIMessages.TOOLTIP_PWRS_RADIO_SERVER);
    viewSelectionByServerButton_.setSelection(true);
    helpSystem.setHelp(viewSelectionByServerButton_, INFOPOP_PWRS_RADIO_SERVER);
    viewSelectionByRuntimeButton_ = new Button(viewSelectionGroup_, SWT.RADIO);
    viewSelectionByRuntimeButton_.setText(ConsumptionUIMessages.LABEL_SELECTION_VIEW_RUNTIME);
    viewSelectionByRuntimeButton_.addListener(SWT.Selection, this);
    viewSelectionByRuntimeButton_.setToolTipText(ConsumptionUIMessages.TOOLTIP_PWRS_RADIO_RUNTIME);
    helpSystem.setHelp(viewSelectionByRuntimeButton_, INFOPOP_PWRS_RADIO_RUNTIME);
    viewSelectionByExploreButton_ = new Button(viewSelectionGroup_, SWT.RADIO);
    viewSelectionByExploreButton_.setText(ConsumptionUIMessages.LABEL_SELECTION_VIEW_EXPLORE);
    viewSelectionByExploreButton_.addListener(SWT.Selection, this);
    viewSelectionByExploreButton_.setToolTipText(ConsumptionUIMessages.TOOLTIP_PWRS_RADIO_EXPLORE);
    helpSystem.setHelp(viewSelectionByExploreButton_, INFOPOP_PWRS_RADIO_EXPLORE);
    primaryGroup_ = new Composite(composite, SWT.NONE);
    gl = new GridLayout();
    gl.numColumns = 1;
    gl.makeColumnsEqualWidth = true;
    gd = new GridData(GridData.FILL_BOTH);
    primaryGroup_.setLayout(gl);
    primaryGroup_.setLayoutData(gd);
    runtimesGroup_ = new Group(primaryGroup_, SWT.NONE);
    gl = new GridLayout();
    runtimesGroup_.setLayout(gl);
    gd = new GridData(GridData.FILL_BOTH);
    runtimesGroup_.setLayoutData(gd);
    runtimesGroup_.setText(ConsumptionUIMessages.LABEL_RUNTIMES_LIST);
    runtimesList_ = new Tree(runtimesGroup_, SWT.SINGLE | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
    gd = new GridData(GridData.FILL_BOTH);
    runtimesList_.setLayoutData(gd);
    runtimesList_.addListener(SWT.Selection, this);
    runtimesList_.setToolTipText(ConsumptionUIMessages.TOOLTIP_PWRS_LIST_RUNTIMES);
    helpSystem.setHelp(runtimesList_, INFOPOP_PWRS_LIST_RUNTIMES);
    //  Server labels control
    serversGroup_ = new Group(primaryGroup_, SWT.NONE);
    gl = new GridLayout();
    serversGroup_.setLayout(gl);
    gd = new GridData(GridData.FILL_BOTH);
    serversGroup_.setLayoutData(gd);
    serversGroup_.setText(ConsumptionUIMessages.LABEL_SERVERS_LIST);
    serverList_ = new Tree(serversGroup_, SWT.SINGLE | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
    serverList_.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL));
    serverList_.addListener(SWT.Selection, this);
    serverList_.setToolTipText(ConsumptionUIMessages.TOOLTIP_PWRS_LIST_SERVERS);
    ServersList serverList = new ServersList();
    serverList.setServerTreeItems(serverList_);
    helpSystem.setHelp(serverList_, INFOPOP_PWRS_LIST_SERVERS);
    setRuntimesGroup();
    //  -----------------------------------------------------------------------//
    new Label(composite, SWT.HORIZONTAL);
    validateOn_ = true;
    org.eclipse.jface.dialogs.Dialog.applyDialogFont(parent);
    return composite;
  }


  private void validateServerRuntimeSelection() {
    // Check if the extension exists
    if (selectionMode_ == MODE_SERVICE) {
      if (selectedServerFactoryID_!=null && selectedRuntime_!=null) {

      boolean wssrtSupported = WebServiceRuntimeExtensionUtils2.isServerRuntimeTypeSupported(selectedServerFactoryID_,selectedRuntime_.getId(),typeId_);
	  
      if (wssrtSupported && WebServiceRuntimeExtensionUtils2.isServerSupportedForChosenServiceType(typeId_, selectedServerFactoryID_)) {
	        setOKStatusMessage();
	      }	  
      else {
        setERRORStatusMessage(ConsumptionUIMessages.MSG_INVALID_SRT_SELECTIONS);
      }
      }
    }
    else {

      String clientId = typeId_;
      if (selectedServerFactoryID_==null || selectedRuntime_ == null || !WebServiceRuntimeExtensionUtils2.isServerClientRuntimeTypeSupported(selectedServerFactoryID_, selectedRuntime_.getId(), clientId)) {
	        setERRORStatusMessage(ConsumptionUIMessages.MSG_INVALID_SRT_SELECTIONS);
	  }	  
      else {
        setOKStatusMessage();
      }
    }
    // Disable OK button if the runtime selection is invalid
    TreeItem[] runtimeSel = runtimesList_.getSelection();
    if (runtimeSel == null || runtimeSel.length <= 0 || runtimeSel[0].getText().length() == 0) {
      disableOKButton();
    }
    // Disable OK button if server selection is invalid
    TreeItem[] serverSel = serverList_.getSelection();
    String currentSelection = (serverSel != null && serverSel.length > 0) ? serverSel[0].getText() : "";
    if (serverSel == null || currentSelection.length() == 0) {
      disableOKButton();
    }
    if (!serverLabels_.containsKey(currentSelection) || !existingServersTable_.containsKey(currentSelection)) {
      disableOKButton();
      setOKStatusMessage();
    }
    // Disable OK button if category is selected rather than a server
    if (serverSel.length > 0 && serverSel[0].getItemCount()!=0) {
    	disableOKButton();
    }
    else {
    	enableOKButton();
    }
    
  }

  private void setOKStatusMessage() {
    messageBanner_.setText(ConsumptionUIMessages.PAGE_DESC_WS_RUNTIME_SELECTION);
    Color black = new Color(thisShell.getDisplay(), 0x00, 0, 0);
    messageBanner_.setForeground(black);
    enableOKButton();
  }

  private void setERRORStatusMessage(String message) {
    messageBanner_.setText(message);
    Color red = new Color(thisShell.getDisplay(), 0xFF, 0, 0);
    messageBanner_.setForeground(red);
    disableOKButton();
  }

  private void disableOKButton() {
    if (getButton(0) != null)
      getButton(0).setEnabled(false);
  }

  private void enableOKButton() {
    if (getButton(0) != null)
      getButton(0).setEnabled(true);
  }

  protected void okPressed() {
    setReturnCode(OK);
    close();
  }

  /**
   * Called when an event occurs on the page. Handles the event and revalidates the page.
   * 
   * @param event
   *          The event that occured.
   */
  public void handleEvent(Event event) {
    if (!validateOn_)
      return;
    enableOKButton();
    if (viewSelectionByRuntimeButton_ == event.widget) {
      handleRuntimeViewSelectionEvent();
      return;
    }
    else if (viewSelectionByServerButton_ == event.widget) {
      handleServerViewSelectionEvent();
      return;
    }
    else if (viewSelectionByExploreButton_ == event.widget) {
      handleExploreViewSelectionEvent();
      return;
    }
    else if (runtimesList_ == event.widget) {
      TreeItem[] runtimeSel = runtimesList_.getSelection();
      processRuntimeListSelection(runtimeSel[0].getText());
      validateServerRuntimeSelection();
    }
    else if (serverList_ == event.widget) {
      processServerListSelection();
      validateServerRuntimeSelection();
    }
  }

  private void handleRuntimeViewSelectionEvent() {
    GridLayout gl = new GridLayout();
    gl.numColumns = 1;
    gl.makeColumnsEqualWidth = true;
    GridData gd = new GridData(GridData.FILL_BOTH);
    primaryGroup_.setLayout(gl);
    primaryGroup_.setLayoutData(gd);
    primaryGroup_.layout();

    // TODO: Show all runtimes, and only servers supported by current type id
  }

  // 
  // Called by handleEvent() when the user selects the layout to view
  //
  public void handleServerViewSelectionEvent() {
    GridLayout gl = new GridLayout();
    gl.numColumns = 1;
    gl.makeColumnsEqualWidth = true;
    GridData gd = new GridData(GridData.FILL_BOTH);
    primaryGroup_.setLayout(gl);
    primaryGroup_.setLayoutData(gd);
    primaryGroup_.layout();
    serversGroup_.setLocation(5, 5);
    runtimesGroup_.setLocation(4, 100);
    serversGroup_.layout();
    runtimesGroup_.layout();

    // TODO: Show all servers, and only runtimes supported by the current type id
  }

  // 
  // Called by handleEvent() when the user selects the layout to view
  //
  private void handleExploreViewSelectionEvent() {
    GridLayout gl;
    gl = (GridLayout) primaryGroup_.getLayout();
    gl.numColumns = 2;
    gl.makeColumnsEqualWidth = true;
    GridData gd = new GridData(GridData.FILL_BOTH);
    primaryGroup_.setLayout(gl);
    primaryGroup_.setLayoutData(gd);
    primaryGroup_.layout();

    // TODO: Show all servers and runtimes from the type id

  }

  private void processRuntimeListSelection(String runtimeName) {

    if (selectionMode_ == MODE_SERVICE) {
		selectedRuntime_ = WebServiceRuntimeExtensionUtils2.getRuntimeByLabel(runtimeName);
    }
    else {
		selectedRuntime_ = WebServiceRuntimeExtensionUtils2.getRuntimeByLabel(runtimeName);
    }
  }

  private void processServerListSelection() {
    String currentSelection;
    TreeItem[] serverSel = serverList_.getSelection();
    if (serverSel != null && serverSel.length > 0) {
      currentSelection = serverSel[0].getText();
      if (serverLabels_.containsKey(currentSelection) || existingServersTable_.containsKey(currentSelection)) {
        if (existingServersTable_.containsKey(currentSelection)) {
          selectedServer_ = (IServer) existingServersTable_.get(currentSelection);
          selectedServerLabel_ = currentSelection;
          selectedServerFactoryID_ = selectedServer_.getServerType().getId();
        }
        else if (serverLabels_.containsKey(currentSelection)) {
          TreeItem parentItem = serverSel[0].getParentItem();
          if (parentItem != null && !parentItem.getText().equalsIgnoreCase(ConsumptionUIMessages.LABEL_TREE_EXISTING_SERVERS)) {
            selectedServerLabel_ = currentSelection;
            selectedServer_ = null;
            selectedServerFactoryID_ = (String) serverLabels_.get(currentSelection);
          }
        }

        // check if isExistingServer or new ServerType
        TreeItem parentItem = serverSel[0].getParentItem();
        if (parentItem != null && parentItem.getText().equalsIgnoreCase(ConsumptionUIMessages.LABEL_TREE_EXISTING_SERVERS)) {
          setIsExistingServer(true);
        }
        else {
          setIsExistingServer(false);
        }
      }
      else {
        selectedServer_ = null;
        selectedServerLabel_ = null;
        selectedServerFactoryID_ = null;
        setIsExistingServer(false);
        return;
      }      
    }
  }

//  private static String getMessage(String key) {
//    return WebServiceConsumptionUIPlugin.getMessage(key);
//  }

  private void setRuntimesGroup() {
    runtimesList_.removeAll();
    String[] runtimes = null;

    if (selectionMode_ == MODE_SERVICE) {
		runtimes = WebServiceRuntimeExtensionUtils2.getRuntimesByServiceType(typeId_);
    }
    else {
	  runtimes = WebServiceRuntimeExtensionUtils2.getAllRuntimesForClientSide();
    }

    TreeItem[] runtimeName = new TreeItem[runtimes.length];

    if (runtimes != null) {
      for (int i = 0; i < runtimes.length; i++) {
        String runtimeLabel = getRuntimeLabel(runtimes[i]);
        runtimeName[i] = new TreeItem(runtimesList_, SWT.NONE);
        runtimeName[i].setText(runtimeLabel);

        if (runtimes[i].equalsIgnoreCase(defaultRuntime_)) {
          runtimesList_.setSelection(new TreeItem[] { runtimeName[i]});
          selectedRuntime_ = getRuntime(runtimes[i]);
        }
      }
    }
  }

  private String getRuntimeLabel(String type) {
    return getRuntime(type).getLabel();
  }

  private RuntimeDescriptor getRuntime(String type) {
    if (selectionMode_ == MODE_SERVICE) {
	  return WebServiceRuntimeExtensionUtils2.getRuntimeById(type);
    }
    else {
	  return WebServiceRuntimeExtensionUtils2.getRuntimeById(type);
    }
  }

  /**
   * 
   * ServersList class
   * 
   * Class which easily creates the Tree structure and entries for existing servers and server types
   */
  public class ServersList {

    private TreeItem[] existingServersTree;
    boolean existingServer = false;

    public void setServerTreeItems(Tree serversList) {
      this.setExistingServersTree(serversList);
      this.setServerTypesTree(serversList);
    }

    public void setExistingServersTree(Tree serverList) {
      String[] serverIds = this.getAllExistingServers();
      existingServersTree = new TreeItem[1];
      TreeItem[] existingServerItems = new TreeItem[serverIds.length];
      existingServersTree[0] = new TreeItem(serverList, SWT.NONE);
      existingServersTree[0].setText(ConsumptionUIMessages.LABEL_TREE_EXISTING_SERVERS);
      ImageDescriptor id = WebServiceConsumptionUIPlugin.getImageDescriptor(EXISTING_SERVERS_ICON);
      if (id != null)
        existingServersTree[0].setImage(id.createImage());
      for (int k = 0; k < serverIds.length; k++) {
        IServerType serverType = ServerCore.findServerType(((IServer) existingServersTable_.get(serverIds[k])).getServerType().getId());
        if (serverType != null) {
          existingServerItems[k] = new TreeItem(existingServersTree[0], SWT.NONE);
          existingServerItems[k].setText(serverIds[k]);
          if (serverIds[k].equalsIgnoreCase(defaultServer_) && getIsExistingServer()) {
            existingServersTree[0].setExpanded(true);
            serverList.setSelection(new TreeItem[] { existingServerItems[k]});
            existingServer = true;
            RuntimeServerSelectionDialog.this.setIsExistingServer(true);
            selectedServer_ = (IServer) existingServersTable_.get(defaultServer_);
            selectedServerLabel_ = serverIds[k];
            selectedServerFactoryID_ = selectedServer_.getServerType().getId();
          }
          existingServerItems[k].setImage(ServerUICore.getLabelProvider().getImage(serverType));
        }
      }
    }

    public void setServerTypesTree(Tree serverList) {

      TreeItem[] serverTypesTree = new TreeItem[1];
      serverTypesTree[0] = new TreeItem(serverList, SWT.NONE);
      serverTypesTree[0].setText(ConsumptionUIMessages.LABEL_TREE_SERVER_TYPES);
      ImageDescriptor id = WebServiceConsumptionUIPlugin.getImageDescriptor(SERVER_TYPES_ICON);
      if (id != null)
        serverTypesTree[0].setImage(id.createImage());
      Hashtable categories_ = new Hashtable();
      Hashtable categoryTreeItem = new Hashtable();
      String[] serverIds = null;
      // rm String wst = wse.getWebServiceType();
      if (selectionMode_ == MODE_SERVICE) {
		serverIds = WebServiceRuntimeExtensionUtils2.getServerFactoryIdsByServiceType(typeId_);
      }
      else {
		serverIds = WebServiceRuntimeExtensionUtils2.getAllClientServerFactoryIds();
      }
      // rm serverId =
      // wssrtRegistry.getServerFactoryIdsByType(wse.getWebServiceType());
      if (serverIds == null) {
        serverIds = getAllServerTypes();
      }
      TreeItem[] parent = new TreeItem[serverIds.length];
      TreeItem[] item = new TreeItem[serverIds.length];
      if (serverIds != null) {
        for (int i = 0; i < serverIds.length; i++) {
          String server = ServerUtils.getInstance().getServerLabelForId(serverIds[i]);
          if (server != null) {
            RuntimeServerSelectionDialog.this.serverLabels_.put(server, serverIds[i]);
            IServerType serverType = ServerCore.findServerType(serverIds[i]);
            IRuntimeType runtimeType = serverType.getRuntimeType();
            if (!categories_.containsKey(serverType) && runtimeType != null) {
              categories_.put(serverType, runtimeType);
              if (categoryTreeItem.get(runtimeType) == null) {
                String categoryText = runtimeType.getName();
                Image categoryImage = ServerUICore.getLabelProvider().getImage(runtimeType);
                parent[i] = new TreeItem(serverTypesTree[0], SWT.NONE);
                parent[i].setText(categoryText);
                parent[i].setImage(categoryImage);
                categoryTreeItem.put(runtimeType, parent[i]);
              }
              else {
                parent[i] = (TreeItem) categoryTreeItem.get(runtimeType);
              }
              String factoryText = serverType.getName();
              Image factoryImage = ServerUICore.getLabelProvider().getImage(serverType);
              item[i] = new TreeItem(parent[i], SWT.NONE);
              item[i].setText(factoryText);
              item[i].setImage(factoryImage);
              item[i].setData(serverType);
              if (factoryText.equalsIgnoreCase(defaultServer_) && !existingServer) {
                serverList.setSelection(new TreeItem[] { item[i]});
                selectedServer_ = null;
                selectedServerLabel_ = factoryText;
                selectedServerFactoryID_ = (String) serverLabels_.get(selectedServerLabel_);
              }
            }
          }
        }
      }

    }

    /**
     * Get all servers in the workspace
     * 
     * @return String[] of existing server names
     */
    private String[] getAllExistingServers() {
      Vector serverIds = new Vector();
      {
        IServer[] servers = ServerCore.getServers();
        if (servers != null && servers.length!=0) {
          for (int i = 0; i < servers.length; i++) {
            IServer server = (IServer) servers[i];
            serverIds.add(server.getName());
            existingServersTable_.put(server.getName(), server);
          }
        }
      }
      return (String[]) serverIds.toArray(new String[serverIds.size()]);
    }

    /**
     * Get all server types available for creation
     * 
     * @return String[] of server names
     */
    private String[] getAllServerTypes() {
      Vector serverTypes_ = new Vector();
      IServerType[] defaultServersList = ServerCore.getServerTypes();
      for (int i = 0; i < defaultServersList.length; i++) {
        IServerType serverType = (IServerType) defaultServersList[i];
        serverTypes_.add(serverType.getId());
      }
      return (String[]) serverTypes_.toArray(new String[serverTypes_.size()]);
    }
  }
}
