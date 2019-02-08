/*******************************************************************************
 * Copyright (c) 2004, 2015 IBM Corporation and others.
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
 * 20060524   142635 gilberta@ca.ibm.com - Gilbert Andrews
 * 20060815   104870 makandre@ca.ibm.com - Andrew Mak, enable/disable test page controls base on settings in test facility extension
 * 20060815   153903 makandre@ca.ibm.com - Andrew Mak, Browse does not work in generate client test page
 * 20080325   184761 gilberta@ca.ibm.com - Gilbert Andrews
 * 20080425   221232 gilberta@ca.ibm.com - Gilbert Andrews
 * 20080506   227848 makandre@ca.ibm.com - Andrew Mak, Disabled "Run on Server" checkbox is in checked state
 * 20080527   234192 gilberta@ca.ibm.com - Gilbert Andrews
 * 20080616   237298 gilberta@ca.ibm.com - Gilbert Andrews
 * 20080619   237797 gilberta@ca.ibm.com - Gilbert Andrews
 * 20080723   241303 gilberta@ca.ibm.com - Gilbert Andrews
 * 20080808   243602 rkklai@ca.ibm.com   - Raymond Lai, fix NPE when changing runtime (the server combo)
 * 20090302   242462 ericdp@ca.ibm.com - Eric D. Peters, Save Web services wizard settings
 * 20090324   247535 mahutch@ca.ibm.com - Mark Hutchinson, Wrong server instance(s) is chosen during JAX-RPC sample generation
 * 20150311   461526 jgwest@ca.ibm.com - Jonathan West,  Allow OSGi bundles to be selected in the Wizard
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.ui.widgets.test;

import java.util.Set;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.internal.consumption.common.FacetUtils;
import org.eclipse.jst.ws.internal.consumption.common.FolderResourceFilter;
import org.eclipse.jst.ws.internal.consumption.ui.ConsumptionUIMessages;
import org.eclipse.jst.ws.internal.consumption.ui.common.DefaultingUtils;
import org.eclipse.jst.ws.internal.consumption.ui.common.LabelsAndIds;
import org.eclipse.jst.ws.internal.consumption.ui.plugin.WebServiceConsumptionUIPlugin;
import org.eclipse.jst.ws.internal.ext.test.WebServiceTestExtension;
import org.eclipse.jst.ws.internal.ext.test.WebServiceTestRegistry;
import org.eclipse.jst.ws.internal.ui.common.ComboWithHistory;
import org.eclipse.jst.ws.internal.ui.common.UIUtils;
import org.eclipse.jst.ws.internal.ui.dialog.DialogUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.command.internal.env.core.selection.BooleanSelection;
import org.eclipse.wst.command.internal.env.core.selection.SelectionList;
import org.eclipse.wst.command.internal.env.ui.widgets.SimpleWidgetDataContributor;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetDataEvents;
import org.eclipse.wst.server.core.IRuntime;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.IServerType;
import org.eclipse.wst.server.core.ServerCore;
import org.eclipse.wst.server.core.internal.facets.FacetUtil;


public class ClientTestWidget extends SimpleWidgetDataContributor
{
  private String pluginId_ = "org.eclipse.jst.ws.consumption.ui";

  /*CONTEXT_ID PWSM0001 for the Sample Page*/
  private String INFOPOP_PWSM_PAGE =  "PWSM0001";
  //
  private Button testCheckbox_;
  /*CONTEXT_ID PWSM0002 for the Test check box of the Sample Page*/
  private String INFOPOP_PWSM_CHECKBOX_TEST = "PWSM0002";

  private Combo testTypeCombo_;
   /*CONTEXT_ID PWSM0003 for the Test Type Combo box of the Sample Page*/
  private String INFOPOP_PWSM_COMBOBOX_TEST = "PWSM0003";
  
  private Combo runtimesCombo;
  /*CONTEXT_ID PWSM0004 for the server type combo box of the Sample Page*/
  private String INFOPOP_PWSM_COMBOBOX_SERVER = "PWSM0004";
  
  private Combo serverInstanceTypeCombo_;
  /*CONTEXT_ID PWSM0005 for the server instance combo box of the Sample Page*/
  private String INFOPOP_PWSM_COMBOBOX_SERVER_INSTANCE = "PWSM0005";

  private Text  jspFolderText_;
  /*CONTEXT_ID PWSM0008 for the JSP Folder field of the Sample Page*/
  private String INFOPOP_PWSM_TEXT_JSP_FOLDER = "PWSM0008";

  private Button sampleFolderBrowseButton_;
  /*CONTEXT_ID PWSM0009 for the JSP Folder Browse button of the Sample Page*/
  private String INFOPOP_PWSM_BUTTON_JSP_FOLDER_BROWSE = "PWSM0009";

  private Text projectCombo_;
  /*CONTEXT_ID PWSM0010 for the Project combo box of the Sample Page*/
  private String INFOPOP_PWSM_COMBO_PROJECT = "PWSM0010";

  private Tree methodsTree_;

  private Button selectAllMethodsButton_;
  /*CONTEXT_ID PWSM0006 for the Select All button of the Sample Page*/
  private String INFOPOP_PWSM_BUTTON_SELECT_ALL = "PWSM0006";

  private Button deselectAllMethodsButton_;
  /*CONTEXT_ID PWSM0007 for the Deselect All button of the Sample Page*/
  private String INFOPOP_PWSM_BUTTON_DESELECT_ALL = "PWSM0007";

  private ComboWithHistory sampleFolderText_;
  /*CONTEXT_ID PWSM0014 for the Folder field of the Sample Page*/   
  private String INFOPOP_PWSM_TEXT_SAMPLE_FOLDER = "PWSM0014";
  private ModifyListener sampleFolderTextModifyListener;

  //
  private Button runTestCheckbox_;  
  /*CONTEXT_ID PWSM0015 for the run test check box of the Sample Page*/
  private String INFOPOP_PWSM_CHECKBOX_LAUNCH = "PWSM0015";
  //
  
  private Label earLabel_;
  private Text earCombo_;
  /*CONTEXT_ID PWSM0016 for the EAR combo box of the Sample Page*/
  private String INFOPOP_PWSM_EAR_COMBO = "PWSM0016";
  
  private Composite            comboGroup_; 
  private SelectionList        testFacilities_;
  private FolderResourceFilter folderFilter_ = new FolderResourceFilter();
  private IStructuredSelection initialSelection_;
  private boolean isTestWidget = false;
  private boolean isPopup = false;
  private boolean isWebProject = false;
  
  private LabelsAndIds runtimes_;
  private LabelsAndIds serverInstances_;
  
  private IPath webContentPath_;
  private IResource webContent_;
    
  public WidgetDataEvents addControls( Composite parent, Listener statusListener )
  {
    
	  
	isTestWidget = true;
    UIUtils      uiUtils  = new UIUtils( pluginId_ );
        
	parent.setToolTipText( ConsumptionUIMessages.TOOLTIP_PWSM_PAGE );
	PlatformUI.getWorkbench().getHelpSystem().setHelp( parent, pluginId_ + "." + INFOPOP_PWSM_PAGE );
    
    testCheckbox_ = uiUtils.createCheckbox( parent, ConsumptionUIMessages.BUTTON_TEST,
    		ConsumptionUIMessages.TOOLTIP_PWSM_CHECKBOX_TEST,
                                            INFOPOP_PWSM_CHECKBOX_TEST );
    testCheckbox_.addSelectionListener( new SelectionAdapter()
                                        {
                                          public void widgetSelected( SelectionEvent evt )
                                          {
                                            handleTestButton();
                                          }
                                        });
    
    comboGroup_ = uiUtils.createComposite( parent, 3, 5, 0 );
    
    testTypeCombo_ = uiUtils.createCombo( comboGroup_, ConsumptionUIMessages.LABEL_TEST_TYPES,
    									ConsumptionUIMessages.TOOLTIP_PWSM_COMBOBOX_TEST,
                                          INFOPOP_PWSM_COMBOBOX_TEST,
                                          SWT.SINGLE | SWT.BORDER | SWT.READ_ONLY );
    testTypeCombo_.addSelectionListener(
    		new SelectionAdapter() {
    			public void widgetSelected(SelectionEvent e) {
    				handleTestFacilitySelection();
    			}
    		}
    );    
    
    
    
    new Label( comboGroup_, SWT.NONE );
    
    projectCombo_ = uiUtils.createText( comboGroup_, ConsumptionUIMessages.LABEL_JSP_PROJECT_NAME,
    									ConsumptionUIMessages.TOOLTIP_PWSM_COMBO_PROJECT,
                                          INFOPOP_PWSM_COMBO_PROJECT,
                                          SWT.SINGLE | SWT.BORDER | SWT.READ_ONLY );
    
    new Label( comboGroup_, SWT.NONE );
    
    
    Object[] earComboWidgets = createText( comboGroup_, ConsumptionUIMessages.LABEL_EAR_PROJECTS,
    								ConsumptionUIMessages.TOOLTIP_PWSM_EAR_PROJECT,
                                     INFOPOP_PWSM_EAR_COMBO,
                                     SWT.SINGLE | SWT.BORDER | SWT.READ_ONLY );
    
    earCombo_ = (Text)earComboWidgets[0];
    earLabel_ = (Label)earComboWidgets[1];
    
    new Label( comboGroup_, SWT.NONE );
    
    sampleFolderText_ = uiUtils.createComboWithHistory(comboGroup_, ConsumptionUIMessages.LABEL_FOLDER_NAME,
    										ConsumptionUIMessages.TOOLTIP_PWSM_TEXT_SAMPLE_FOLDER,
                                            INFOPOP_PWSM_TEXT_SAMPLE_FOLDER,
                                            SWT.SINGLE | SWT.BORDER , WebServiceConsumptionUIPlugin.getInstance().getDialogSettings());
    
    
	sampleFolderTextModifyListener = new ModifyListener() 
			{
			public void modifyText(ModifyEvent evt) {
				handleFolderText();
			}

			};
		
    sampleFolderText_.addModifyListener(sampleFolderTextModifyListener);
       
    sampleFolderBrowseButton_ = uiUtils.createPushButton( comboGroup_, ConsumptionUIMessages.BUTTON_BROWSE, 
    											ConsumptionUIMessages.TOOLTIP_PWSM_BUTTON_JSP_FOLDER_BROWSE,
                                                          INFOPOP_PWSM_BUTTON_JSP_FOLDER_BROWSE );
    sampleFolderBrowseButton_.addSelectionListener( new SelectionAdapter()
                                                    {
                                                      public void widgetSelected( SelectionEvent evt )
                                                      {
                                                        handleSampleBrowse(); 
                                                      }
                                                    });
    
    jspFolderText_ = uiUtils.createText(comboGroup_, ConsumptionUIMessages.LABEL_JSP_FOLDER_NAME,
    		ConsumptionUIMessages.TOOLTIP_PWSM_TEXT_JSP_FOLDER,
            INFOPOP_PWSM_TEXT_JSP_FOLDER,
            SWT.SINGLE | SWT.BORDER | SWT.READ_ONLY );
    
    
    new Label( comboGroup_, SWT.NONE );
    
    Group methodsGroup = uiUtils.createGroup( parent, ConsumptionUIMessages.LABEL_METHODS,  
    				ConsumptionUIMessages.TOOLTIP_PWSM_TREE_METHODS, null );
    
	methodsGroup.setLayoutData( uiUtils.createFillAll() );
	
	GridLayout layout = new GridLayout();
	layout.marginHeight = 0;
	layout.marginWidth = 0;
	methodsGroup.setLayout( layout );
	
	// TODO No infopop for this tree.
	methodsTree_ = uiUtils.createTree( methodsGroup, ConsumptionUIMessages.TOOLTIP_PWSM_TREE_METHODS, 
	                                   null,
	                   				   SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL	| SWT.CHECK );

    Composite selectButtons = uiUtils.createComposite( methodsGroup, 2 );
    
    selectAllMethodsButton_ 
      = uiUtils.createPushButton( selectButtons, ConsumptionUIMessages.BUTTON_SELECT_ALL, 
    		  ConsumptionUIMessages.TOOLTIP_PWSM_BUTTON_SELECT_ALL,
                                                 INFOPOP_PWSM_BUTTON_SELECT_ALL );
    selectAllMethodsButton_.addSelectionListener( new SelectionAdapter() 
                                                  {
                                                    public void widgetSelected( SelectionEvent evt )
                                                    {
                                                      handleSelectAll( true );
                                                    }
                                                  });
    
    deselectAllMethodsButton_ 
      = uiUtils.createPushButton( selectButtons, ConsumptionUIMessages.BUTTON_DESELECT_ALL, 
    		  					ConsumptionUIMessages.TOOLTIP_PWSM_BUTTON_DESELECT_ALL,
                                  INFOPOP_PWSM_BUTTON_DESELECT_ALL );
    deselectAllMethodsButton_.addSelectionListener( new SelectionAdapter() 
                                                    {
                                                      public void widgetSelected( SelectionEvent evt )
                                                      {
                                                        handleSelectAll( false );
                                                      }
                                                    });
    
    runTestCheckbox_ = uiUtils.createCheckbox( parent, ConsumptionUIMessages.BUTTON_RUN_TEST,
    										ConsumptionUIMessages.TOOLTIP_PWSM_CHECKBOX_LAUNCH,
                                               INFOPOP_PWSM_CHECKBOX_LAUNCH );
    runTestCheckbox_.addSelectionListener( new SelectionAdapter() 
    {
        public void widgetSelected( SelectionEvent evt )
        {
          handleRunTestCheckBox();
        }
      });
    
    return this;
  }
  
  private void handleRunTestCheckBox(){
	  boolean enabled = runTestCheckbox_.getSelection();
	  runtimesCombo.setEnabled(enabled);
	  serverInstanceTypeCombo_.setEnabled(enabled);
	  
  }
  
  private void handleFolderText()
  {
	// webContentPath_ is set when jspFolderText_ is set for the first time.  Therefore,
	// if webContentPath_ is still null, there is no need to touch jspFolderText_ yet.
	  
	if (webContentPath_ == null)
		return;
	  
  	String folder = sampleFolderText_.getText();	
    jspFolderText_.setText(webContentPath_.toString() + IPath.SEPARATOR + folder);
  }
  
  private void handleSampleBrowse()
  {
    IPath      selectedPath       = null;
    IResource  initialResource    = null;
    
    try
    {
      initialResource = ResourceUtils.getResourceFromSelection( initialSelection_ );
    }
    catch( CoreException exc )
    {  
    }
    
    if( initialResource == null )initialResource = webContent_;
    
    IResource resource = DialogUtils.browseResources( comboGroup_.getShell(), 
                                                      webContent_,
                                                      initialResource, 
                                                      folderFilter_ );
    
    if( resource != null )
    {
      selectedPath = resource.getFullPath();   
    
      int webModuleSegments = selectedPath.matchingFirstSegments(webContentPath_);
  	  
      sampleFolderText_.removeModifyListener(sampleFolderTextModifyListener);
      sampleFolderText_.removeAll();
      
      if ( webModuleSegments < webContentPath_.segmentCount() )
      {
    	sampleFolderText_.add("", 0);
        handleFolderText();
      }
      else
      {
        sampleFolderText_.add(selectedPath.removeFirstSegments(webModuleSegments).toString(), 0);
        handleFolderText();
      }
      sampleFolderText_.select(0);
      sampleFolderText_.restoreWidgetHistory("org.eclipse.jst.ws.internal.consumption.ui.widgets.test.ClientTestWidget.sampleFolderText_");
      sampleFolderText_.addModifyListener(sampleFolderTextModifyListener);

    }
  }
  
  private void handleTestButton()
  {
    boolean enabled = testCheckbox_.getSelection();
    
    testTypeCombo_.setEnabled( enabled );
    
    if (enabled && testFacilities_ != null) {
    	handleTestFacilitySelection();
    	return;
    }    

    sampleFolderText_.setEnabled( enabled );
    methodsTree_.setEnabled( enabled );
    if(canRunTestClient_)
    	runTestCheckbox_.setEnabled( enabled );
    selectAllMethodsButton_.setEnabled( enabled );
    deselectAllMethodsButton_.setEnabled( enabled );
    sampleFolderBrowseButton_.setEnabled( enabled );
    
  
  
  }
  
  private void handleTestFacilitySelection() {
	  
	  String clientTestID = getTestFacility().getSelection();
	  	
	  WebServiceTestExtension testExtension = 
		  (WebServiceTestExtension) WebServiceTestRegistry.getInstance()
		  .getWebServiceExtensionsByName(clientTestID);
	    
      boolean hasCodeGen = testExtension.isCodeGenNeeded();
      
      // folder selection is only applicable for test facilities with codegen
      sampleFolderText_.setEnabled( hasCodeGen );
      sampleFolderBrowseButton_.setEnabled( hasCodeGen && webContent_ != null );
      
      boolean hasMethods = testExtension.areMethodsNeeded();
      
      // method selection is only applicable for test facilities with methods
      methodsTree_.setEnabled( hasMethods );      
      selectAllMethodsButton_.setEnabled( hasMethods );
      deselectAllMethodsButton_.setEnabled( hasMethods );
      
      // run on server only applicable for test facilities that needs launching
      if(canRunTestClient_)
    	  runTestCheckbox_.setEnabled( testExtension.isServerNeeded() );
  }
  
  private void handleSelectAll( boolean value )
  {
	TreeItem[] items = methodsTree_.getItems();
	
	for( int i = 0; i < items.length; i++ ) 
	{
	  items[i].setChecked(value);
	}
  } 
  
  private void handleServerChange()
  {
	  int selection = runtimesCombo.getSelectionIndex();
	  serverInstances_.clear();
	  serverInstanceTypeCombo_.removeAll();
	  IServer[] servers = org.eclipse.wst.server.core.ServerCore.getServers();
	  for(int j =0; j<servers.length;j++){
			String id = runtimes_.getId(selection);
			if(id.equals(servers[j].getServerType().getId())){
				serverInstances_.add(servers[j].getId(), servers[j].getName());
				serverInstanceTypeCombo_.add(servers[j].getName());
			}
	  }
	  serverInstanceTypeCombo_.select(0);
	  
	  serverInstanceTypeCombo_.setEnabled(serverInstances_.size() > 0 );
  }  
  
  // Here are the getters and setters for this widget.
  public void setTestService( Boolean testService )
  {
    testCheckbox_.setSelection( testService.booleanValue() );
    handleTestButton();
  }
  
  public Boolean getTestService()
  {
    return new Boolean( testCheckbox_.getSelection() );
  }
  
  public void setTestFacility( SelectionList testFacilities )
  {
    testFacilities_ = testFacilities;
    testTypeCombo_.setItems( testFacilities.getList() );
    testTypeCombo_.select( testFacilities.getIndex() );
    handleTestFacilitySelection();
  }
  
  public SelectionList getTestFacility()
  {
    testFacilities_.setIndex( testTypeCombo_.getSelectionIndex() );
    return testFacilities_;
  }
  
  public String getTestID()
  {
    return testTypeCombo_.getText();
  }
  
  /**
   * Given a project name which may be of the (obsolete) format "project/module",
   * return only the project part of the name
   * 
   * @param project The project name.
   * @return If the name has the format "project/module", returns only "project", 
   * otherwise returns the name as is.
   */
  private String extractProjectName(String project) {
	  
	  int index = project.indexOf("/");
	  
	  if (index != -1)
		  project = project.substring(0, index);
	  
	  return project;
  }
  
  private String getServerTypeIdForRuntime(IRuntime rt) {
	  IServerType[] serverTypes = ServerCore.getServerTypes();
	  for (IServerType sType : serverTypes) {
		  if (sType.getRuntimeType() == rt.getRuntimeType() ){
			  return sType.getId();

		  }
	  }
	  return null;
  }
  
  public void initServersTypes() 
  {
	if(runtimes_  == null)
		runtimes_ = new LabelsAndIds();
	IServer[] servers = org.eclipse.wst.server.core.ServerCore.getServers();
	IRuntime[] runtimes = org.eclipse.wst.server.core.ServerCore.getRuntimes();
	
	String projectName = projectCombo_.getText();
	
	Set projectFacets = FacetUtils.getFacetsForProject(projectName);
	for(int i = 0;i<runtimes.length;i++){
		boolean showServer = false;
		
		if(runtimes[i].isStub()){
			for(int l =0; l<servers.length;l++){
				if(servers[l].getRuntime() != null){
					if(runtimes[i].getId().equals(servers[l].getRuntime().getId())){
						showServer = true;
					}
				}
			}
		}
		else showServer = true;
		
		if(showServer && canRunTestClient_){	
			if(isWebProject){
				org.eclipse.wst.common.project.facet.core.runtime.IRuntime runtime = FacetUtil.getRuntime(runtimes[i]);
				if(FacetUtils.doesRuntimeSupportFacets(runtime, projectFacets)){
					runtimes_.add(getServerTypeIdForRuntime(runtimes[i]), runtimes[i].getName());
					runtimesCombo.add( runtimes[i].getName() );
				}
			}
			else{
				runtimes_.add(getServerTypeIdForRuntime(runtimes[i]), runtimes[i].getName());
				runtimesCombo.add( runtimes[i].getName() );	
			}
		}
	}
	
	runtimesCombo.select(0);
	if(serverInstances_  == null)
		serverInstances_ = new LabelsAndIds();
	
	
	String id = runtimes_.getId(0);
	if(id != null){
		for(int k =0; k<servers.length;k++){
			if(servers[k].getServerType() != null){
				if(id.equals(servers[k].getServerType().getId())&& canRunTestClient_){
					serverInstances_.add(servers[k].getId(), servers[k].getName());
					serverInstanceTypeCombo_.add(servers[k].getName());
				}
			}
		}
		serverInstanceTypeCombo_.select(0);
	}
		
	serverInstanceTypeCombo_.setEnabled(canRunTestClient_ && runTestCheckbox_.getSelection());
	runtimesCombo.setEnabled(canRunTestClient_ && runTestCheckbox_.getSelection());
  }
  
  
  
  
  public void setSampleProject(String clientProject) 
  {
	projectCombo_.setText(extractProjectName(clientProject));
  }
  
  public void setSampleProjectEAR(String clientProjectEAR) 
  {
    earCombo_.setText(extractProjectName(clientProjectEAR));
    updateEARText();
  }
  
  public String getFolder()
  {
    return sampleFolderText_.getText();
  }
  
  public void setFolder( String folder )
  {
    sampleFolderText_.removeModifyListener(sampleFolderTextModifyListener);
    sampleFolderText_.removeAll();
    sampleFolderText_.add( folder);
    sampleFolderText_.select(0);
    sampleFolderText_.addModifyListener(sampleFolderTextModifyListener);
    
  }
  
  public boolean getIsTestWidget()
  {
  	return isTestWidget;
  }
  
  public void setJspFolder( String folder )
  {
	webContentPath_ = (new Path(folder)).removeLastSegments(1);
	webContent_ = ResourceUtils.findResource(webContentPath_);	
	sampleFolderBrowseButton_.setEnabled(webContent_ != null);
	  
    jspFolderText_.setText( folder );
  }
  
  public String getJspFolder()
  {
    return jspFolderText_.getText();
  }
  
  public void setLaunchedServiceTestName (String launchedServiceTestName)
  {
  }
  
  public boolean getRunTestClient()
  {
    return runTestCheckbox_.getSelection();
  }
  
  public void setRunTestClient( boolean value )
  {
	  runTestCheckbox_.setSelection( value );
  }
  
  public void setIsWebProject( boolean value )
  {
	  isWebProject = value;
  }
  
  private boolean canRunTestClient_;
  
  public void setCanRunTestClient(boolean canRunTestClient){
	  canRunTestClient_ = canRunTestClient;
	  if(!canRunTestClient_) {
		  runTestCheckbox_.setSelection(false);
		  runTestCheckbox_.setEnabled(false);
	  }
  }
  
  public void setPopup(boolean popup){
	  isPopup = popup;
	  if(isPopup){
		  UIUtils      uiUtils  = new UIUtils( pluginId_ );
		  runtimesCombo = uiUtils.createCombo( comboGroup_, ConsumptionUIMessages.LABEL_SERVERS_LIST,
					ConsumptionUIMessages.TOOLTIP_PWSM_COMBOBOX_SERVER,
					INFOPOP_PWSM_COMBOBOX_SERVER,
		              SWT.SINGLE | SWT.BORDER | SWT.READ_ONLY );
		    	runtimesCombo.addSelectionListener(
		    		new SelectionAdapter() {
		    			public void widgetSelected(SelectionEvent e) {
		    				handleServerChange();
		    			}
		    		}
		    	);    
		    
		  new Label( comboGroup_, SWT.NONE );
		
	  
		  serverInstanceTypeCombo_ = uiUtils.createCombo( comboGroup_, ConsumptionUIMessages.LABEL_SERVERS_INSTANCES,
					ConsumptionUIMessages.TOOLTIP_PWSM_COMBOBOX_SERVER_INSTANCE,
					INFOPOP_PWSM_COMBOBOX_SERVER_INSTANCE,
		              SWT.SINGLE | SWT.BORDER | SWT.READ_ONLY );
		    
		  new Label( comboGroup_, SWT.NONE );
		  initServersTypes();
	  
	  }
  }
  
  public boolean getCanRunTestClient()
  {
	  return canRunTestClient_;
  }
  
  public String getServerInstanceId(){
	  int instanceLabel = serverInstanceTypeCombo_.getSelectionIndex();
	  if(instanceLabel == -1) return null;
	  return serverInstances_.getId(instanceLabel);
  }
  
  
  public String getExistingServerId(){
	  int typeLabel = runtimesCombo.getSelectionIndex();
	  if(typeLabel == -1) return null;
	  return runtimes_.getId(typeLabel);
  }
  
  public BooleanSelection[] getMethods()
  {
    TreeItem[]         items   = methodsTree_.getItems();
    BooleanSelection[] methods = new BooleanSelection[items.length];
    
    for( int index = 0; index < items.length; index++ )
    {
      methods[index] = new BooleanSelection( items[index].getText(), items[index].getChecked() );
    }
    
    return methods;
  }
  
  public void setMethods( BooleanSelection[] methods )
  {
    methodsTree_.removeAll();
    
    for( int index = 0; index < methods.length; index++ )
    {
      if(methods[index] == null)continue;
      TreeItem item = new TreeItem( methodsTree_, SWT.NULL );
      item.setText( methods[index].getValue() );
      item.setChecked( methods[index].isSelected() );
    }
  }
  
  public void setInitialSelection( IStructuredSelection selection )
  {
    initialSelection_ = selection;
  }
  
  public IStatus getStatus() {
	  if(!canRunTestClient_)
		  return StatusUtils.warningStatus(ConsumptionUIMessages.MSG_SERVER_NOT_FOUND_WARNING);
  
	  return  Status.OK_STATUS;
  }

public void externalize() {
	super.externalize();
	sampleFolderText_.storeWidgetHistory("org.eclipse.jst.ws.internal.consumption.ui.widgets.test.ClientTestWidget.sampleFolderText_");
}

public void internalize() {		
	sampleFolderText_.removeModifyListener(sampleFolderTextModifyListener);
	sampleFolderText_.restoreWidgetHistory("org.eclipse.jst.ws.internal.consumption.ui.widgets.test.ClientTestWidget.sampleFolderText_");
	sampleFolderText_.addModifyListener(sampleFolderTextModifyListener);
}



	/** Returns [ Text, Label]  */
	private Object[] createText( Composite parent, String labelName, String tooltip, String infopop, int style )
	{    
	  tooltip = tooltip == null ? labelName : tooltip;
	  
	  Label label = null;
	  if( labelName != null )
	  {
	    label = new Label( parent, SWT.LEAD);
	    label.setText(  labelName  );
	    label.setToolTipText(  tooltip );
	  }
	  
	  Text text = new Text( parent, style );
	  GridData griddata = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
	  
	  text.setLayoutData( griddata );
	  text.setToolTipText( tooltip);
	  
	  if( infopop != null ) PlatformUI.getWorkbench().getHelpSystem().setHelp( text, pluginId_ + "." + infopop );
	  
	  Object[] result = new Object[2];
	  result[0] = text;
	  result[1] = label;
	  
	  return result;      
	}


	private boolean isOSGI() 
	{
		 if(earCombo_ != null) 
		 {
			 if(DefaultingUtils.isOSGIProject(earCombo_.getText())) 
			 {
				 return true;
			 }
		 }
		 	 
		 return false;
	}

	
	private void updateEARText() {
		if(isOSGI() ) {
			if(earCombo_ != null) {
				earCombo_.setToolTipText(ConsumptionUIMessages.TOOLTIP_PWSM_OSGI_PROJECT);
			}
			if(earLabel_ != null) {
				earLabel_.setText(ConsumptionUIMessages.LABEL_OSGI_PROJECTS);
				earLabel_.setToolTipText(ConsumptionUIMessages.TOOLTIP_PWSM_OSGI_PROJECT);
			}
			
			
		} else {
			if(earCombo_ != null) {
				earCombo_.setToolTipText(ConsumptionUIMessages.TOOLTIP_PWSM_EAR_PROJECT);
			}
			if(earLabel_ != null) {
				earLabel_.setText(ConsumptionUIMessages.LABEL_EAR_PROJECTS);
				earLabel_.setToolTipText(ConsumptionUIMessages.TOOLTIP_PWSM_EAR_PROJECT);
			}
			
		}
	}

}
