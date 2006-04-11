/*******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060407   135415 rsinha@ca.ibm.com - Rupam Kuehner
 * 20060407   135443 joan@ca.ibm.com - Joan Haggarty
 * 20060410   135442 kathy@ca.ibm.com - Kathy Chan
 * 20060410   135441 joan@ca.ibm.com - Joan Haggarty
 * 20060410   135562 joan@ca.ibm.com - Joan Haggarty
 * 20060411   136167 kathy@ca.ibm.com - Kathy Chan
 *******************************************************************************/
package org.eclipse.jst.ws.internal.creation.ui.widgets;


import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.internal.consumption.common.FacetUtils;
import org.eclipse.jst.ws.internal.consumption.ui.ConsumptionUIMessages;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.IObjectSelectionLaunchable;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.ProjectSelectionDialog;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.WebServiceClientTypeWidget;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.object.ObjectSelectionOutputCommand;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.object.ObjectSelectionRegistry;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.object.ObjectSelectionWidget;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.runtime.ProjectSelectionWidget;
import org.eclipse.jst.ws.internal.consumption.ui.wizard.RuntimeServerSelectionDialog;
import org.eclipse.jst.ws.internal.consumption.ui.wsrt.WebServiceImpl;
import org.eclipse.jst.ws.internal.consumption.ui.wsrt.WebServiceRuntimeExtensionUtils2;
import org.eclipse.jst.ws.internal.context.ScenarioContext;
import org.eclipse.jst.ws.internal.creation.ui.plugin.WebServiceCreationUIPlugin;
import org.eclipse.jst.ws.internal.creation.ui.widgets.runtime.ServerRuntimeSelectionWidgetDefaultingCommand;
import org.eclipse.jst.ws.internal.data.LabelsAndIds;
import org.eclipse.jst.ws.internal.data.TypeRuntimeServer;
import org.eclipse.jst.ws.internal.plugin.WebServicePlugin;
import org.eclipse.jst.ws.internal.ui.common.UIUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.HyperlinkGroup;
import org.eclipse.ui.forms.HyperlinkSettings;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.events.IHyperlinkListener;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.internal.Workbench;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.command.internal.env.core.context.ResourceContext;
import org.eclipse.wst.command.internal.env.ui.widgets.PageInfo;
import org.eclipse.wst.command.internal.env.ui.widgets.SimpleWidgetDataContributor;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetContributor;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetContributorFactory;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetDataEvents;
import org.eclipse.wst.ws.internal.parser.wsil.WebServicesParser;
import org.eclipse.wst.ws.internal.wsrt.WebServiceScenario;

public class ServerWizardWidget extends SimpleWidgetDataContributor {
	
	//INFOPOPS
    /* CONTEXT_ID WSWSCEN0001 for the Scenario Page */
	private String INFOPOP_WSWSCEN_PAGE = "WSWSCEN0001";
	 /* CONTEXT_ID WSWSCEN0010 for the Web Service Type combo box of the Scenario Page */
	 private String INFOPOP_WSWSCEN_COMBO_SERVICETYPE = "WSWSCEN0010";
	 /* CONTEXT_ID WSWSCEN0014 for the monitor service checkbox of the Scenario page */
	 private String INFOPOP_WSWSCEN_CHECKBOX_MONITOR_SERVICE = "WSWSCEN0014";
	 /* CONTEXT_ID WSWSCEN0012 for the Launch UDDI check box of the Scenario Page */
	 private String INFOPOP_WSWSCEN_CHECKBOX_LAUNCH_WS = "WSWSCEN0012";
	 /* CONTEXT_ID WSWSCEN0020 for the Service Implemenation text field of the Scenario Page */
	 private String INFOPOP_WSWSCEN_TEXT_SERVICE_IMPL = "WSWSCEN0020";
	 /* CONTEXT_ID WSWSCEN0021 for theService Slider of the Scenario Page */
	 private String INFOPOP_WSWSCEN_SCALE_SERVICE = "WSWSCEN0021";
	 /* CONTEXT_ID WSWSCEN0022 for the Server hyperlink of the Scenario Page */
	 private String INFOPOP_WSWSCEN_HYPERLINK_SERVER = "WSWSCEN0022";
	 /* CONTEXT_ID WSWSCEN0023 for the Runtime hyperlink of the Scenario Page */
	 private String INFOPOP_WSWSCEN_HYPERLINK_RUNTIME = "WSWSCEN0023";
	 /* CONTEXT_ID WSWSCEN0024 for theProjects hyperlink of the Scenario Page */
	 private String INFOPOP_WSWSCEN_HYPERLINK_PROJECTS  = "WSWSCEN0024";
	 /* CONTEXT_ID WSWSCEN0030 for the Overwrite Files checkbox of the Scenario Page */
	 private String INFOPOP_WSWSCEN_CHECKBOX_OVERWRITE = "WSWSCEN0030";
	 
	private ScaleSelectionListener scaleSelectionListener = new ScaleSelectionListener();
	private Listener statusListener_;

	private ImageRegistry imageReg_;
	
	private TypeRuntimeServer ids_;
	private LabelsAndIds labelIds_;
	private String serviceProjectName_;
	private String serviceEarProjectName_;
	private String serviceComponentType_;
	private WebServicesParser parser_;
	private String serviceRuntimeId_;
	private boolean needEar_;
		 
	private IStructuredSelection objectSelection_;
	private Boolean testService_;
	private Boolean startService_;
	private Boolean installService_;

	private boolean displayPreferences_;

	private Composite groupComposite_;
	private Composite hCompService_;
	private WebServiceClientTypeWidget clientWidget_;
	private Combo webserviceType_;
	private Text serviceImpl_;
	private Scale serviceScale_;
	private Label topologySpot_;
	private Button browseButton_;
	private Button publishButton_;
	private Button monitorButton_;
	private Button overwriteButton_;	
	private ServiceImplSelectionDialog browseDialog_;
	private ProjectSelectionDialog projectDialog_;
	private Hyperlink hLinkServiceRuntime_;
	private Hyperlink hLinkServiceServer_;
	private Hyperlink hLinkServiceProject_;
	private Hyperlink hLinkServiceEAR_;
		
	private Object objectSelectionWidget_; //may be IObjectSelectionLaunchable or IObjectSelectionWidget
	
	private ResourceContext resourceContext_;
	
	private String GRAPHIC_SERVICE_0="icons/service_test.jpg"; //$NON-NLS-N$
	private String GRAPHIC_SERVICE_1="icons/service_run.jpg";  //$NON-NLS-N$
	private String GRAPHIC_SERVICE_2="icons/service_install.jpg"; //$NON-NLS-N$
	private String GRAPHIC_SERVICE_3="icons/service_deploy.jpg"; //$NON-NLS-N$
	private String GRAPHIC_SERVICE_4="icons/service_assemble.jpg"; //$NON-NLS-N$
	private String GRAPHIC_SERVICE_5="icons/service_develop.jpg"; //$NON-NLS-N$
		
	private String ICON_SCALE_BG_0="icons/scale0_bground.jpg"; //$NON-NLS-N$
	private String ICON_SCALE_BG_1="icons/scale1_bground.jpg"; //$NON-NLS-N$
	private String ICON_SCALE_BG_2="icons/scale2_bground.jpg"; //$NON-NLS-N$
	private String ICON_SCALE_BG_3="icons/scale3_bground.jpg"; //$NON-NLS-N$
	private String ICON_SCALE_BG_4="icons/scale4_bground.jpg"; //$NON-NLS-N$
	private String ICON_SCALE_BG_5="icons/scale5_bground.jpg"; //$NON-NLS-N$
	private String ICON_SCALE_BG_6="icons/scale6_bground.jpg"; //$NON-NLS-N$

	private String SERVICE_RUNTIME_PREFIX = ConsumptionUIMessages.LABEL_RUNTIMES_LIST ; 
    private String SERVICE_SERVER_PREFIX =  ConsumptionUIMessages.LABEL_SERVERS_LIST;
    private String SERVICE_PROJECT_PREFIX = ConsumptionUIMessages.LABEL_SERVICE_PROJECT;
    private String SERVICE_EAR_PREFIX = ConsumptionUIMessages.LABEL_SERVICE_EAR_PROJECT;
	
	public ServerWizardWidget(boolean displayPreferences) {
		displayPreferences_ = displayPreferences;
		initImageRegistry();
	}
	
	public WidgetDataEvents addControls(Composite parent,
			Listener statusListener) {
		
		String createPluginId = "org.eclipse.jst.ws.creation.ui";
		UIUtils utils = new UIUtils(createPluginId);
		statusListener_ = statusListener;
		utils.createInfoPop(parent, INFOPOP_WSWSCEN_PAGE);
		
		Composite typeComposite = utils.createComposite(parent, 3);

		// Create webservice combo box.
		webserviceType_ = utils.createCombo(typeComposite,
				ConsumptionUIMessages.LABEL_WEBSERVICETYPE,
				ConsumptionUIMessages.TOOLTIP_PWPR_COMBO_TYPE,
				INFOPOP_WSWSCEN_COMBO_SERVICETYPE, SWT.SINGLE | SWT.BORDER
						| SWT.READ_ONLY);
		GridData gdata1 = (GridData) webserviceType_.getLayoutData();
		gdata1.horizontalSpan = 2;
		webserviceType_.setLayoutData(gdata1);
		
		webserviceType_.addSelectionListener(new SelectionListener(){
			public void widgetDefaultSelected(SelectionEvent e) {}
			
			public void widgetSelected(SelectionEvent e) {
			   objectSelectionWidget_ = getSelectionWidget();
		       // jvh - not sure if we should clear obj selection field once type is changed?   
			   // serviceImpl_.setText("");
			}
			
		});
		webserviceType_.addListener(SWT.Modify, statusListener);
		
		// Create text field and browse for object selection
		//TODO: add text listener for the field so users can type - for now READ_ONLY
		serviceImpl_ = utils.createText(typeComposite, ConsumptionUIMessages.LABEL_WEBSERVICEIMPL, 
				ConsumptionUIMessages.TOOLTIP_WSWSCEN_TEXT_IMPL,
				INFOPOP_WSWSCEN_TEXT_SERVICE_IMPL, SWT.LEFT | SWT.BORDER | SWT.READ_ONLY);
		browseButton_ = utils.createPushButton(typeComposite,
				ConsumptionUIMessages.BUTTON_BROWSE, ConsumptionUIMessages.TOOLTIP_WSWSCEN_BUTTON_BROWSE_IMPL, null);
		
	    browseDialog_ = new ServiceImplSelectionDialog(Workbench.getInstance().getActiveWorkbenchWindow().getShell(), 
			  						new PageInfo(ConsumptionUIMessages.DIALOG_TITILE_SERVICE_IMPL_SELECTION, "", 
			                        new WidgetContributorFactory()
			  						{	
			  							public WidgetContributor create()
			  							{	  						 
			  							   return new ObjectSelectionWidget();
			  							}
			  						}));		
		browseButton_.addSelectionListener(new ServiceImplBrowseListener());
		browseButton_.addListener(SWT.Modify, statusListener);  //jvh - added for validation on object selection?
		
		// Service Lifecycle section - scales for service & client, graphic
		
		groupComposite_ = new Composite(parent, SWT.NONE);
		GridLayout gclayout = new GridLayout();
		gclayout.numColumns = 2;
		gclayout.horizontalSpacing=0;		
		gclayout.marginHeight=0;		
		gclayout.marginBottom=5;
		groupComposite_.setLayout( gclayout );
	    GridData gcGridData = new GridData(SWT.BEGINNING, SWT.BEGINNING, true, true); 
	    groupComposite_.setLayoutData(gcGridData);
		
		Composite serviceComposite =  new Composite(groupComposite_, SWT.NONE);
		GridLayout gridlayout   = new GridLayout();
	    gridlayout.numColumns   = 2;
	    gridlayout.horizontalSpacing=0;
	    gridlayout.marginHeight=0;
	    serviceComposite.setLayout( gridlayout );
	    GridData scGridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
	    serviceComposite.setLayoutData(scGridData);    
	    
		serviceComposite.setToolTipText(ConsumptionUIMessages.TOOLTIP_WSWSCEN_SCALE_SERVICE);
	
		
		serviceScale_ = new Scale(serviceComposite, SWT.VERTICAL | SWT.BORDER | SWT.CENTER);
	    utils.createInfoPop(serviceScale_, INFOPOP_WSWSCEN_SCALE_SERVICE);
		serviceScale_.setMinimum(0);
		serviceScale_.setMaximum(6);
		serviceScale_.setIncrement(1);
		serviceScale_.addSelectionListener(scaleSelectionListener);
		serviceScale_.setSelection(getServiceGeneration());
		serviceScale_.setToolTipText(ConsumptionUIMessages.TOOLTIP_WSWSCEN_SCALE_SERVICE);
		
		GridData layoutData1 = new GridData();
		layoutData1.horizontalAlignment=SWT.BEGINNING;
		layoutData1.verticalAlignment = SWT.BEGINNING;
		Rectangle scaleR = (imageReg_.get(ICON_SCALE_BG_0)).getBounds();
		layoutData1.heightHint=scaleR.height;
		layoutData1.widthHint=scaleR.width;
		serviceScale_.setLayoutData(layoutData1);		
		
		topologySpot_ = new Label(serviceComposite, SWT.BORDER | SWT.TOP );
		topologySpot_.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		topologySpot_.setToolTipText(ConsumptionUIMessages.TOOLTIP_WSWSCEN_SCALE_SERVICE);
		
		GridData layoutData2 = new GridData();		
		layoutData2.horizontalAlignment=SWT.BEGINNING;				
		layoutData2.verticalAlignment = SWT.BEGINNING;
		Rectangle topR = (imageReg_.get(GRAPHIC_SERVICE_0)).getBounds();
		layoutData2.heightHint=topR.height;
		layoutData2.widthHint=topR.width;
		topologySpot_.setLayoutData(layoutData2);		
				
		setGraphics(getServiceGeneration());
		
		hCompService_ = utils.createComposite(groupComposite_, 1);
		
		Label serviceDetailsLabel = new Label(hCompService_, SWT.NONE);
		serviceDetailsLabel.setText(ConsumptionUIMessages.LABEL_SUMMARY);
		
		hLinkServiceServer_= new Hyperlink(hCompService_, SWT.NULL);
		utils.createInfoPop(hLinkServiceServer_, INFOPOP_WSWSCEN_HYPERLINK_SERVER);
		hLinkServiceServer_.setToolTipText(ConsumptionUIMessages.TOOLTIP_PWRS_TEXT_SERVER);
		hLinkServiceServer_.addHyperlinkListener(new IHyperlinkListener(){
			public void linkActivated(HyperlinkEvent e){				
				launchRuntimeSelectionDialog(false);				
			}
			public void linkEntered(HyperlinkEvent e){}
			public void linkExited(HyperlinkEvent e){}			
		});

		hLinkServiceRuntime_ = new Hyperlink(hCompService_, SWT.NULL);
		utils.createInfoPop(hLinkServiceRuntime_, INFOPOP_WSWSCEN_HYPERLINK_RUNTIME);
		hLinkServiceRuntime_.setToolTipText(ConsumptionUIMessages.TOOLTIP_PWRS_TEXT_RUNTIME);
		hLinkServiceRuntime_.addHyperlinkListener(new IHyperlinkListener(){
			public void linkActivated(HyperlinkEvent e){
								launchRuntimeSelectionDialog(false);
			}
			public void linkEntered(HyperlinkEvent e){}
			public void linkExited(HyperlinkEvent e){}			
		});
		
		projectDialog_ = new ProjectSelectionDialog(Workbench.getInstance().getActiveWorkbenchWindow().getShell(), 
				new PageInfo(ConsumptionUIMessages.DIALOG_TITILE_SERVICE_PROJECT_SETTINGS, "", 
                        new WidgetContributorFactory()
  						{	
  							public WidgetContributor create()
  							{	  						 
  							   return new ProjectSelectionWidget();
  							}
  						}));
		
		hLinkServiceProject_= new Hyperlink(hCompService_, SWT.NULL);
		utils.createInfoPop(hLinkServiceRuntime_, INFOPOP_WSWSCEN_HYPERLINK_PROJECTS);
		hLinkServiceProject_.setToolTipText(ConsumptionUIMessages.TOOLTIP_WSWSCEN_SERVICEPROJECT_LINK);
		hLinkServiceProject_.addHyperlinkListener(new IHyperlinkListener(){
			public void linkActivated(HyperlinkEvent e){
				launchProjectDialog();				
			}
			public void linkEntered(HyperlinkEvent e){}
			public void linkExited(HyperlinkEvent e){}			
		});
		
		hLinkServiceEAR_= new Hyperlink(hCompService_, SWT.NULL);
		utils.createInfoPop(hLinkServiceRuntime_, INFOPOP_WSWSCEN_HYPERLINK_PROJECTS);
		hLinkServiceEAR_.setToolTipText(ConsumptionUIMessages.TOOLTIP_WSWSCEN_SERVICEPROJECT_LINK);
		hLinkServiceEAR_.addHyperlinkListener(new IHyperlinkListener(){
			public void linkActivated(HyperlinkEvent e){
				launchProjectDialog();			
			}
			public void linkEntered(HyperlinkEvent e){}
			public void linkExited(HyperlinkEvent e){}			
		});		

		hLinkServiceServer_.setText(SERVICE_SERVER_PREFIX); 
		hLinkServiceRuntime_.setText(SERVICE_RUNTIME_PREFIX);
		hLinkServiceProject_.setText(SERVICE_PROJECT_PREFIX);
		hLinkServiceEAR_.setText(SERVICE_EAR_PREFIX);
		
		HyperlinkGroup serverRuntimeGroup = new HyperlinkGroup(Display.getCurrent());
		
		serverRuntimeGroup.add(hLinkServiceServer_);
		serverRuntimeGroup.add(hLinkServiceRuntime_);
		serverRuntimeGroup.add(hLinkServiceProject_);
		serverRuntimeGroup.add(hLinkServiceEAR_);
		serverRuntimeGroup.setHyperlinkUnderlineMode(HyperlinkSettings.UNDERLINE_ALWAYS);
    
		utils.createHorizontalSeparator(parent, 1);		
		
		// Add client widgets...
		clientWidget_ = new WebServiceClientTypeWidget();
	    clientWidget_.addControls(parent, statusListener );
	    clientWidget_.setClientOnly(false);
	    clientWidget_.enableClientSlider(serviceScale_.getSelection()<=ScenarioContext.WS_START);
		
		// Advanced buttons section
		utils.createHorizontalSeparator(parent, 3);
		
		Composite advancedButtonPanel = utils.createComposite(parent, 1);
		
		publishButton_ = utils.createCheckbox(advancedButtonPanel,
				ConsumptionUIMessages.BUTTON_WSWSCEN_PUBLISH, ConsumptionUIMessages.TOOLTIP_PWPR_CHECKBOX_LAUNCH_WS, INFOPOP_WSWSCEN_CHECKBOX_LAUNCH_WS);		
		monitorButton_ = utils.createCheckbox(advancedButtonPanel,
				ConsumptionUIMessages.CHECKBOX_MONITOR_WEBSERVICE, ConsumptionUIMessages.TOOLTIP_PWPR_CHECKBOX_MONITOR_SERVICE, INFOPOP_WSWSCEN_CHECKBOX_MONITOR_SERVICE);

		if (displayPreferences_ && getResourceContext().isOverwriteFilesEnabled()) {
			utils.createHorizontalSeparator(parent, 1);			
			Composite prefButtonPanel = utils.createComposite(parent, 1);
			overwriteButton_ = utils.createCheckbox(prefButtonPanel,
					ConsumptionUIMessages.CHECKBOX_OVERWRITE_FILES, ConsumptionUIMessages.TOOLTIP_WSWSCEN_BUTTON_OVERWRITE_FILES, INFOPOP_WSWSCEN_CHECKBOX_OVERWRITE);
			overwriteButton_.setSelection(getResourceContext()
					.isOverwriteFilesEnabled());
		}

		return this;
	}
	
	private void launchProjectDialog()
	{
		projectDialog_.setProjectName(getServiceProjectName());
		projectDialog_.setEarProjectName(getServiceEarProjectName());
		projectDialog_.setProjectComponentType(getServiceComponentType());
		projectDialog_.setNeedEAR(getServiceNeedEAR());
		
		int status = projectDialog_.open();  //jvh validation on settings??
		if (status == Window.OK)
		{
			setServiceProjectName(projectDialog_.getProjectName());
			setServiceEarProjectName(projectDialog_.getEarProjectName());
			setServiceComponentType(projectDialog_.getProjectComponentType());
			setServiceNeedEAR(projectDialog_.getNeedEAR());	
			refreshClientServerRuntimeSelection();
		}
	}
	
	private void launchRuntimeSelectionDialog(boolean clientContext)
	{
		byte mode = clientContext ? (byte)1 : (byte)0;
		
		//TODO: jvh - investigate - don't think j2ee version shouldn't be hard coded (last parm) 
		//  question - where to pick it up from?
		RuntimeServerSelectionDialog rssd = new RuntimeServerSelectionDialog(
				Workbench.getInstance().getActiveWorkbenchWindow().getShell(), mode, getServiceTypeRuntimeServer(), "14");	
		int result = rssd.open();		
		if (result == Window.OK)
		{
			setServiceTypeRuntimeServer(rssd.getTypeRuntimeServer());	
			refreshClientServerRuntimeSelection();
		}		
	}	
	
	public void setClientTypeRuntimeServer(TypeRuntimeServer ids) {
		clientWidget_.setTypeRuntimeServer( ids );
	}

	public TypeRuntimeServer getClientTypeRuntimeServer() {
		return clientWidget_.getTypeRuntimeServer();
	}

	public void setInstallClient(Boolean install) {
		clientWidget_.setInstallClient( install );
	}

	public Boolean getInstallClient() {		
		return clientWidget_.getInstallClient();
	}

	public void setServiceTypeRuntimeServer(TypeRuntimeServer ids) {
		LabelsAndIds labelIds = WebServiceRuntimeExtensionUtils2
				.getServiceTypeLabels();
		// rskreg
		int selection = 0;
		String[] serviceIds = labelIds.getIds_();
		String selectedId = ids.getTypeId();

		webserviceType_.removeListener(SWT.Modify, statusListener_);
		webserviceType_.setItems(labelIds.getLabels_());

		// Now find the selected one.
		for (int index = 0; index < serviceIds.length; index++) {
			if (selectedId.equals(serviceIds[index])) {
				selection = index;
				break;
			}
		}
		webserviceType_.select(selection);
		webserviceType_.addListener(SWT.Modify, statusListener_);
		
		ids_ = ids;
		
		if (ids_ != null)
		{
			String serviceServerText = WebServiceRuntimeExtensionUtils2.getServerLabelById(ids_.getServerId());
			String serviceRuntimeText = WebServiceRuntimeExtensionUtils2.getRuntimeLabelById(ids_.getRuntimeId());
			hLinkServiceServer_.setText(SERVICE_SERVER_PREFIX + " " + serviceServerText);
			hLinkServiceRuntime_.setText(SERVICE_RUNTIME_PREFIX + " " + serviceRuntimeText);
			groupComposite_.pack(true);
		}				
		labelIds_ = labelIds;
		
		if (projectDialog_ != null)
			projectDialog_.setTypeRuntimeServer(ids_);
	}

	public TypeRuntimeServer getServiceTypeRuntimeServer() {
		int selectionIndex = webserviceType_.getSelectionIndex();

		ids_.setTypeId(labelIds_.getIds_()[selectionIndex]);

		return ids_;
	}

	public void setServiceGeneration(int value)
	{
		serviceScale_.setSelection(value);
		setGraphics(value);
		setTestService(new Boolean(value <= ScenarioContext.WS_TEST));
		setInstallService(new Boolean(value <= ScenarioContext.WS_INSTALL));
		setStartService(new Boolean(value <= ScenarioContext.WS_START));
		//enable client widget based on service scale setting
		clientWidget_.enableClientSlider(value<=ScenarioContext.WS_START);	
	}	

	public int getServiceGeneration()
	{
		return serviceScale_.getSelection();
	}
	
	public Boolean getStartService(){
		return startService_;
	}
	
	public void setStartService(Boolean value) {
        startService_=value;
	}

	public Boolean getInstallService() {
		return installService_;
	}

	public void setInstallService(Boolean value) {
        installService_=value;
	}

	public Boolean getTestService() {
        return 
	          new Boolean(testService_.booleanValue() || clientWidget_.getTestClient().booleanValue());
	}

	public void setTestService(Boolean value) {
        testService_= value;
	}
	public Boolean getMonitorService() {
		return new Boolean(monitorButton_.getSelection());
	}

	public void setMonitorService(Boolean value) {
		monitorButton_.setSelection(value.booleanValue());
	}

	public Boolean getPublishService() {		
		return new Boolean(publishButton_.getSelection());
	}

	public void setPublishService(Boolean value) {
		publishButton_.setSelection( value.booleanValue() );
	}

	public Boolean getGenerateProxy() {
		int clientSelection = clientWidget_.getClientGeneration();
		if (clientSelection <= ScenarioContext.WS_DEVELOP)
		  return new Boolean(true);
		return new Boolean(false);		
	}

	public void setClientGeneration(int value)
	{
		clientWidget_.setClientGeneration(value);
	}
	
	public String getClientEarProjectName()
	{
		return clientWidget_.getClientEarProjectName();
	}
	
	public String getClientProjectName()
	{
		return clientWidget_.getClientProjectName();
	}
	
	public int getClientGeneration()
	{
		return clientWidget_.getClientGeneration();
	}	

	public ResourceContext getResourceContext() {
		if (resourceContext_ == null) {
			resourceContext_ = WebServicePlugin.getInstance()
					.getResourceContext();
		}
		return resourceContext_;
	}

	public void setResourceContext(ResourceContext context) {
		resourceContext_ = context;
	}

	public IStructuredSelection getObjectSelection()
	{
		return objectSelection_;		
	}
	
	
	public void setObjectSelection(IStructuredSelection selection )
	{
        objectSelection_ = selection;
        if (selection != null && selection.size()==1)
        {
        	//Update the serviceImpl_ field.
        	Object[] selectionArray = selection.toArray();
        	Object selectedObject = selectionArray[0];
        	if (selectedObject instanceof String)
        	{
        		serviceImpl_.setText((String)selectedObject);
	}
        }
	
	}
	
	public WebServicesParser getWebServicesParser()
	{
		return parser_;
	}
	
	public void setWebServicesParser(WebServicesParser parser)
	{
		parser_ = parser;
		clientWidget_.setWebServicesParser(parser);
	}
	
	public void internalize() {		
	}

	public IStatus getStatus() {
		IStatus status = Status.OK_STATUS;

		// If the webservice has not been selected then user can not move
		// forward to the next page.
		
		//TODO: jvh - need to add base object plus minimum service gen to the requirements for the page...
		if (webserviceType_.getText().equals("")) {
			status = StatusUtils.errorStatus("");
		}

		return status;
	}
	
	protected void initImageRegistry()
	{
		imageReg_ = new ImageRegistry(Display.getCurrent());
		
		imageReg_.put(ICON_SCALE_BG_0, WebServiceCreationUIPlugin
				.getImageDescriptor(ICON_SCALE_BG_0));
		imageReg_.put(ICON_SCALE_BG_1, WebServiceCreationUIPlugin
				.getImageDescriptor(ICON_SCALE_BG_1));
		imageReg_.put(ICON_SCALE_BG_2, WebServiceCreationUIPlugin
				.getImageDescriptor(ICON_SCALE_BG_2));
		imageReg_.put(ICON_SCALE_BG_3, WebServiceCreationUIPlugin
				.getImageDescriptor(ICON_SCALE_BG_3));
		imageReg_.put(ICON_SCALE_BG_4, WebServiceCreationUIPlugin
				.getImageDescriptor(ICON_SCALE_BG_4));
		imageReg_.put(ICON_SCALE_BG_5, WebServiceCreationUIPlugin
				.getImageDescriptor(ICON_SCALE_BG_5));
		imageReg_.put(ICON_SCALE_BG_6, WebServiceCreationUIPlugin
				.getImageDescriptor(ICON_SCALE_BG_6));
		
		imageReg_.put(GRAPHIC_SERVICE_0, WebServiceCreationUIPlugin
				.getImageDescriptor(GRAPHIC_SERVICE_0));
		imageReg_.put(GRAPHIC_SERVICE_1, WebServiceCreationUIPlugin
				.getImageDescriptor(GRAPHIC_SERVICE_1));
		imageReg_.put(GRAPHIC_SERVICE_2, WebServiceCreationUIPlugin
				.getImageDescriptor(GRAPHIC_SERVICE_2));
		imageReg_.put(GRAPHIC_SERVICE_3, WebServiceCreationUIPlugin
				.getImageDescriptor(GRAPHIC_SERVICE_3));
		imageReg_.put(GRAPHIC_SERVICE_4, WebServiceCreationUIPlugin
				.getImageDescriptor(GRAPHIC_SERVICE_4));
		imageReg_.put(GRAPHIC_SERVICE_5, WebServiceCreationUIPlugin
				.getImageDescriptor(GRAPHIC_SERVICE_5));
	}
	
	private void setAdvancedOptions(boolean enabled)
	{
		monitorButton_.setEnabled(enabled); 
		publishButton_.setEnabled(enabled);		
	}
	
	public void setServiceProjectName(String name)
	  {
		serviceProjectName_= name;
		hLinkServiceProject_.setText(SERVICE_PROJECT_PREFIX + " " + serviceProjectName_);
		hLinkServiceProject_.pack(true);		
	  }
	
	 public void setServiceEarProjectName(String name)
	  {	  
		 serviceEarProjectName_ = name;
		 refreshEARLink();
	  }
	 
	  public void refreshEARLink()
	  {
		  hLinkServiceEAR_.setVisible(needEar_);
		  if (needEar_)
		  {			 
			  hLinkServiceEAR_.setText(SERVICE_EAR_PREFIX + " " + serviceEarProjectName_);
			  hLinkServiceEAR_.pack(true);
		  }
	  }
	 public void setServiceComponentType( String type )
	  {
		 serviceComponentType_ = type;
	  }
	 
	  public void setClientProjectName(String name)
	  {
	    clientWidget_.setClientProjectName(name);  
	  }  
	  
	  public void setClientEarProjectName(String name)
	  {
	    clientWidget_.setClientEarProjectName(name);  
	  }
	  
	  String componentName_;
	  IProject project_;
	  
	  public void setComponentName(String name)
	  {
		  componentName_ = name;
	  }
	  
	  public String getComponentName()
	  {
		  return componentName_;
	  }
	  
	  public String getServiceComponentType()
	  {
		  return serviceComponentType_;
	  }
	  
	  public void setClientComponentType(String type)
	  {
		  clientWidget_.setClientComponentType(type);
	  }
	  
	  public String getClientComponentType()
	  {
		  return clientWidget_.getClientComponentType();
	  }
	 
	  public String getServiceProjectName()
	  {
		  return serviceProjectName_;
	  }
	  
	  public String getServiceEarProjectName()
	  {
		  return serviceEarProjectName_;
	  }
	  
	  public void setClientProject(IProject project)
	  {
		  clientWidget_.setProject(project);
	  }
	  
	  private IProject getClientProject()
	  {
		  return clientWidget_.getProject(); 
	  }
	  
	  public void setProject(IProject project)
	  {
		  project_ = project;
	  }	  
	  	  
	  public IProject getProject()
	  {
		  return project_;
	  }
	  
	  public void setServiceRuntimeId(String runtimeId)
	  {
		  serviceRuntimeId_ = runtimeId;
	  }
	  
	  public String getServiceRuntimeId()
	  {
		  return serviceRuntimeId_;
	  }
	  
	  public void setClientRuntimeId(String id)
	  {
		  clientWidget_.setClientRuntimeId(id);
	  }
	  
	  public String getClientRuntimeId()
	  {
		  return clientWidget_.getClientRuntimeId();
	  }
	  
	  public void setServiceNeedEAR(boolean b)
	  {
		  needEar_ = b;
		  refreshEARLink();
	  }
	  
	  public boolean getServiceNeedEAR()
	  {
		  return needEar_;
	  }
	  
	  public void setClientNeedEAR(boolean b)
	  {
	    clientWidget_.setClientNeedEAR(b);
	  }	  
	  
	  public boolean getClientNeedEAR()
	  {
		  return clientWidget_.getClientNeedEAR();
	  }
	  
	  // looks through extensions and returns the appropriate selection widget
	  private Object getSelectionWidget()
	  {
			String wst = getServiceTypeRuntimeServer().getTypeId();
			int scenario = WebServiceRuntimeExtensionUtils2.getScenarioFromTypeId(wst);
			String implId = WebServiceRuntimeExtensionUtils2.getWebServiceImplIdFromTypeId(wst);
		    WebServiceImpl wsimpl = WebServiceRuntimeExtensionUtils2.getWebServiceImplById(implId);
		    
		      if (wsimpl != null)
		      {
		        String objectSelectionWidgetId = null;
		        if (scenario == WebServiceScenario.TOPDOWN)
		        {
		          objectSelectionWidgetId = "org.eclipse.jst.ws.internal.consumption.ui.widgets.object.WSDLSelectionWidget";
		        }
		        else
		        {
		          objectSelectionWidgetId = wsimpl.getObjectSelectionWidget();
		        }
			
	        IConfigurationElement[] elements = ObjectSelectionRegistry.getInstance().getConfigurationElements();
			   for (int i = 0; i < elements.length; i++)
		          {
		            if (objectSelectionWidgetId.equals(elements[i].getAttribute("id")))
		            {
                        try
		            	{
		            	   Object object = elements[i].createExecutableExtension("class");		                	
		                   return object;
		                }
		            	catch (Throwable t){ }
		            }
		         }
	        }
		    return null;
	  }
	 
	// for the purposes of disabling the service implementation controls from the preferences dialog
	public void disableNonPreferenceWidgets()
	{
		if (serviceImpl_ != null)
			serviceImpl_.setEnabled(false);
		if (browseButton_ != null)
			browseButton_.setEnabled(false);
		if (hCompService_ != null)
		{
			hCompService_.setVisible(false);			
		}
		clientWidget_.disableNonPreferenceWidgets();
	}
	
	private void setGraphics(int value)
	{
		String iconImage = "";
		String toplogyImage = "";
		switch (value) {
		case 0:
			iconImage=ICON_SCALE_BG_0;
			toplogyImage=GRAPHIC_SERVICE_0;
			break;
		case 1:
			iconImage=ICON_SCALE_BG_1;
			toplogyImage=GRAPHIC_SERVICE_1;
			break;
		case 2:
			iconImage=ICON_SCALE_BG_2;
			toplogyImage=GRAPHIC_SERVICE_2;
			break;
		case 3:
			iconImage=ICON_SCALE_BG_3;
			toplogyImage=GRAPHIC_SERVICE_3;
			break;
		case 4:
			iconImage=ICON_SCALE_BG_4;
			toplogyImage=GRAPHIC_SERVICE_4;
			break;
		case 5:					
		case 6:
			iconImage=ICON_SCALE_BG_5;
			toplogyImage=GRAPHIC_SERVICE_5;
			break;
		default:
			break;
		}
		serviceScale_.setBackgroundImage(imageReg_.get(iconImage));
		topologySpot_.setImage(imageReg_.get(toplogyImage));
	}
	
	private void refreshServerRuntimeSelection()
	{		
		//new up ServerRuntimeSelectionWidgetDefaultingCommand
		ServerRuntimeSelectionWidgetDefaultingCommand serverRTDefaultCmd = new ServerRuntimeSelectionWidgetDefaultingCommand();
		
		  //call setters of new defaulting command:
	      serverRTDefaultCmd.setInitialSelection(getObjectSelection());
	      serverRTDefaultCmd.setInitialProject(getProject());
	      serverRTDefaultCmd.setInitialComponentName(getComponentName());      
	      serverRTDefaultCmd.setGenerateProxy(clientWidget_.getGenerateProxy());
	      serverRTDefaultCmd.setServiceTypeRuntimeServer(getServiceTypeRuntimeServer());
		  serverRTDefaultCmd.setWebServicesParser(getWebServicesParser());     
		  serverRTDefaultCmd.setClientInitialSelection(getObjectSelection());
	      serverRTDefaultCmd.setClientInitialProject(getClientProject());
	      serverRTDefaultCmd.setClientEarProjectName(clientWidget_.getClientEarProjectName());
		  serverRTDefaultCmd.setClientTypeRuntimeServer(getClientTypeRuntimeServer());
		  		  
		  serverRTDefaultCmd.execute(null, null);
		  
		  //perform mappings from the defaulting command to the project settings...	
		  setServiceRuntimeId(serverRTDefaultCmd.getServiceRuntimeId());
		  setServiceProjectName(serverRTDefaultCmd.getServiceProjectName());
		  setServiceEarProjectName(serverRTDefaultCmd.getServiceEarProjectName());
		  setServiceComponentType(serverRTDefaultCmd.getServiceComponentType());
		  setClientProjectName(serverRTDefaultCmd.getClientProjectName());
		  setClientEarProjectName(serverRTDefaultCmd.getClientEarProjectName());
		  setServiceTypeRuntimeServer(serverRTDefaultCmd.getServiceTypeRuntimeServer());
          setClientTypeRuntimeServer(serverRTDefaultCmd.getClientTypeRuntimeServer());
          setServiceNeedEAR(serverRTDefaultCmd.getServiceNeedEAR());
          setClientNeedEAR(serverRTDefaultCmd.getClientNeedEAR());
          setClientRuntimeId(serverRTDefaultCmd.getClientRuntimeId());
          setClientComponentType(serverRTDefaultCmd.getClientComponentType());
	}
	
	/**
	 *  Update client server and runtime based on service side
	 */
	private void refreshClientServerRuntimeSelection()
	{	
		TypeRuntimeServer clientTypeRuntimeserver = getClientTypeRuntimeServer();
		clientTypeRuntimeserver.setRuntimeId(getServiceTypeRuntimeServer().getRuntimeId());
		clientTypeRuntimeserver.setServerId(getServiceTypeRuntimeServer().getServerId());
		clientTypeRuntimeserver.setServerInstanceId(getServiceTypeRuntimeServer().getServerInstanceId());
		setClientTypeRuntimeServer(clientTypeRuntimeserver);
		setClientNeedEAR(clientProjectNeedEAR());
	}	
		
	/**
	 *  Check if client project need EAR based on service side, project and client type
	 */
	private boolean clientProjectNeedEAR()
	{	
		// if client did not need EAR originally, check if it's because it's because 
		// either the project is Java utility project or type is Java utility
		if (!getClientNeedEAR()) {
			// If the project is a simple Java project or the project type is 
			// Java utility return false.
			String projectName = getClientProjectName();
			if (projectName != null && projectName.length()>0)
			{
				IProject project = ResourceUtils.getWorkspaceRoot().getProject(projectName);
				if (project.exists())
				{
					if (FacetUtils.isJavaProject(project))
					{
						return false;
					}
				}
			}

			//Project didn't rule out the need for an EAR
			//so check the project type
			String templateId = getClientComponentType();
			if (templateId != null && templateId.length()>0)
			{
				if (FacetUtils.isUtilityTemplate(templateId))
				{
					return false;
				}
			}
		} // end of !clientNeedEAR
		
		// have clientNeedEAR follows server side
		return getServiceNeedEAR();
		
	}
	private class ScaleSelectionListener implements SelectionListener
	{
		public void widgetSelected(SelectionEvent e) {			
			    
			    Scale scale = (Scale)e.widget;
				int selection = scale.getSelection();
				
				setTestService(new Boolean(selection <= ScenarioContext.WS_TEST));
				setInstallService(new Boolean(selection <= ScenarioContext.WS_INSTALL));
				setStartService(new Boolean(selection <= ScenarioContext.WS_START));
				setAdvancedOptions(selection <= ScenarioContext.WS_INSTALL);
				clientWidget_.enableClientSlider(selection <= ScenarioContext.WS_START);
				
				setGraphics(selection);
				
				switch (selection) {
				case 0:
					serviceScale_.setToolTipText(ConsumptionUIMessages.TOOLTIP_WSWSCEN_SCALE_TEST);
					break;
				case 1:
					serviceScale_.setToolTipText(ConsumptionUIMessages.TOOLTIP_WSWSCEN_SCALE_RUN);
					break;
				case 2:
					serviceScale_.setToolTipText(ConsumptionUIMessages.TOOLTIP_WSWSCEN_SCALE_INSTALL);
					break;
				case 3:
					serviceScale_.setToolTipText(ConsumptionUIMessages.TOOLTIP_WSWSCEN_SCALE_DEPLOY);
					break;
				case 4:
					serviceScale_.setToolTipText(ConsumptionUIMessages.TOOLTIP_WSWSCEN_SCALE_ASSEMBLE);
					break;
				case 5:					
				case 6:
					scale.setSelection(5); //"no selection" is not allowed...must develop service @ minimum
					serviceScale_.setToolTipText(ConsumptionUIMessages.TOOLTIP_WSWSCEN_SCALE_DEVELOP);
					break;
				default:
					break;
				}				
			}		

		public void widgetDefaultSelected(SelectionEvent e) {
			widgetSelected(e);
		}
	}
	
	private class ServiceImplBrowseListener implements SelectionListener
	{
		public void widgetDefaultSelected(SelectionEvent e) {
			// TODO Auto-generated method stub			
		}
		
		public void widgetSelected(SelectionEvent e) {     
			   
			  if (objectSelectionWidget_ == null)
				   objectSelectionWidget_ = getSelectionWidget();			
			   
			   ObjectSelectionOutputCommand objOutputCommand = new ObjectSelectionOutputCommand();
			   IStructuredSelection objectSelection = null;
			   IProject project = null;
			   String componentName="";
			   int result=Dialog.CANCEL;
			   
			   if (objectSelectionWidget_ instanceof IObjectSelectionLaunchable)
		       {      	
				 IObjectSelectionLaunchable launchable = ((IObjectSelectionLaunchable)objectSelectionWidget_);
				 launchable.setInitialSelection(getObjectSelection());
		         result = launchable.launch(Workbench.getInstance().getActiveWorkbenchWindow().getShell());
		         if (result == Dialog.OK)
		         {
			         serviceImpl_.setText(launchable.getObjectSelectionDisplayableString());
			         objectSelection = launchable.getObjectSelection();
			         project = launchable.getProject();
			         componentName= launchable.getComponentName();
		         }
		       }
			   else
			   {
				   browseDialog_.setTypeRuntimeServer(getServiceTypeRuntimeServer());
				   browseDialog_.setInitialSelection(getObjectSelection());
			       result = browseDialog_.open();
			       if (result == Dialog.OK)
			       {
				       serviceImpl_.setText(browseDialog_.getDisplayableSelectionString());
				       objectSelection = browseDialog_.getObjectSelection();
				       project = browseDialog_.getProject();
				       componentName= browseDialog_.getComponentName();			       
			       }
			   }
			   
			   // call ObjectSelectionOutputCommand to carry out any transformation on the objectSelection
			   if (result == Dialog.OK)
			   {
				   objOutputCommand.setTypeRuntimeServer(getServiceTypeRuntimeServer());
				   objOutputCommand.setObjectSelection(objectSelection);
			       objOutputCommand.setProject(project);
			       objOutputCommand.setComponentName(componentName);			   
			       
			       objOutputCommand.execute(null, null);
	               
			       setWebServicesParser(objOutputCommand.getWebServicesParser());
			       setObjectSelection(objOutputCommand.getObjectSelection());
			       setComponentName(objOutputCommand.getComponentName());
			       setProject(objOutputCommand.getProject());	       		       
			       refreshServerRuntimeSelection();   
			   }			   
	    }
	}	
}

