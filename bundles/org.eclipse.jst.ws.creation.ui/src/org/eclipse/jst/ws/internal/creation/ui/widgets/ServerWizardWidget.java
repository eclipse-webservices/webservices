/*******************************************************************************
 * Copyright (c) 2004, 2015 IBM Corporation and others.
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
 * 20060417   136390/136391/136159 joan@ca.ibm.com - Joan Haggarty
 * 20060413   135581 rsinha@ca.ibm.com - Rupam Kuehner
 * 20060420   136158 rsinha@ca.ibm.com - Rupam Kuehner
 * 20060420   136705 rsinha@ca.ibm.com - Rupam Kuehner
 * 20060420   136182 kathy@ca.ibm.com - Kathy Chan
 * 20060420   137820 rsinha@ca.ibm.com - Rupam Kuehner
 * 20060420   135912 joan@ca.ibm.com - Joan Haggarty
 * 20060421   136761 rsinha@ca.ibm.com - Rupam Kuehner
 * 20060424   138052 kathy@ca.ibm.com - Kathy Chan
 * 20060425   137831 rsinha@ca.ibm.com - Rupam Kuehner
 * 20060426   138519 joan@ca.ibm.com - Joan Haggarty
 * 20060427   138058 joan@ca.ibm.com - Joan Haggarty
 * 20060504   138035 joan@ca.ibm.com - Joan Haggarty
 * 20060524   142276 joan@ca.ibm.com - Joan Haggarty
 * 20060529   141422 kathy@ca.ibm.com - Kathy Chan
 * 20060605   145081 joan@ca.ibm.com - Joan Haggarty
 * 20060607   144826 joan@ca.ibm.com - Joan Haggarty
 * 20060607   144049 joan@ca.ibm.com - Joan Haggarty
 * 20060614   146270 joan@ca.ibm.com - Joan Haggarty
 * 20060616   147317 joan@ca.ibm.com - Joan Haggarty
 * 20060717   150577 makandre@ca.ibm.com - Andrew Mak
 * 20060726   150865 sengpl@ca.ibm.com -  Seng Phung-Lu
 * 20060803   152486 makandre@ca.ibm.com - Andrew Mak, Typing WSDL in Service definition field is very slow
 * 20060817   140017 makandre@ca.ibm.com - Andrew Mak, longer project or server/runtime strings do not resize wizard
 * 20060825   135570 makandre@ca.ibm.com - Andrew Mak, Service implementation URL not displayed properly on first page
 * 20060829   155441 makandre@ca.ibm.com - Andrew Mak, web service wizard hangs during resize
 * 20060830   155114 pmoogk@ca.ibm.com - Peter Moogk, Updated patch for this defect.
 * 20060831   155441 makandre@ca.ibm.com - Andrew Mak, Small tweak for this bug
 * 20061003   159142 kathy@ca.ibm.com - Kathy Chan
 * 20061212   164177 makandre@ca.ibm.com - Andrew Mak, Incorrect validation error complaining about runtime not supporting a project type
 * 20061212   159911 makandre@ca.ibm.com - Andrew Mak, changing service definition resets some configuration fields
 * 20070205   172687 makandre@ca.ibm.com - Andrew Mak, NPE in Scencario Defaults preferences page
 * 20071130   203826 kathy@ca.ibm.com - Kathy Chan
 * 20080205   170141 kathy@ca.ibm.com - Kathy Chan
 * 20080215   216337 pmoogk@ca.ibm.com - Peter Moogk
 * 20080301   221034 kathy@ca.ibm.com - Kathy Chan
 * 20080312   147442 trungha@ca.ibm.com - Trung Ha
 * 20080318   213330 trungha@ca.ibm.com - Trung, Non-conventional Java naming prevents creating Web Services (client)
 * 20080326   198439 kathy@ca.ibm.com - Kathy Chan
 * 20080402   198439 kathy@ca.ibm.com - Kathy Chan
 * 20080416   215084 gilberta@ca.ibm.com - Gilbert Andrews
 * 20080417   227599 kathy@ca.ibm.com - Kathy Chan
 * 20080613   236523 makandre@ca.ibm.com - Andrew Mak, Overwrite setting on Web service wizard is coupled with preference
 * 20090926   248448 mahutch@ca.ibm.com - Mark Hutchinson, Should not resize WS Wizard for long WSDL file names
 * 20090121   261730 zhang@ca.ibm.com - Allan Zhang, WebService client runtime id return null
 * 20090302   242462 ericdp@ca.ibm.com - Eric D. Peters, Save Web services wizard settings
 * 20090313   268567 ericdp@ca.ibm.com - Eric D. Peters, persisted wizard settings gone unless launching on object
 * 20090326   269097 kchong@ca.ibm.com - Keith Chong, [Accessibility] Web services wizard dialog should be selected after canceling the Browse for class dialog
 * 20100511   309395 mahutch@ca.ibm.com - Mark Hutchinson, WS Wizard Converting Java Project into Utility Project without any warning
 * 20120501   378160 jenyoung@ca.ibm.com - Jennifer Young, Service project and service project type need to be refreshed when web service target runtime changes
 * 20130225   378160 jcayne@ca.ibm.com - Joel Cayne, Updates for refreshing server project type.
 * 20131030   417117 kchong@ca.ibm.com - Keith Chong, NPE in web service wizard when specifying illegal service implementation
 * 20150311   461526 jgwest@ca.ibm.com - Jonathan West,  Allow OSGi bundles to be selected in the Wizard
 *******************************************************************************/
package org.eclipse.jst.ws.internal.creation.ui.widgets;


import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.jst.ws.internal.consumption.ui.ConsumptionUIMessages;
import org.eclipse.jst.ws.internal.consumption.ui.common.DefaultingUtils;
import org.eclipse.jst.ws.internal.consumption.ui.common.ValidationUtils;
import org.eclipse.jst.ws.internal.consumption.ui.plugin.WebServiceConsumptionUIPlugin;
import org.eclipse.jst.ws.internal.consumption.ui.preferences.ProjectTopologyContext;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.IObjectSelectionLaunchable;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.IPackable;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.ProjectSelectionDialog;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.WebServiceClientTypeWidget;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.object.IObjectSelectionWidget;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.object.ObjectSelectionOutputCommand;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.object.ObjectSelectionRegistry;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.object.ObjectSelectionWidget;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.object.Timer;
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
import org.eclipse.jst.ws.internal.ui.common.ComboWithHistory;
import org.eclipse.jst.ws.internal.ui.common.UIUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.Accessible;
import org.eclipse.swt.accessibility.AccessibleAdapter;
import org.eclipse.swt.accessibility.AccessibleControlAdapter;
import org.eclipse.swt.accessibility.AccessibleControlEvent;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
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
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.HyperlinkGroup;
import org.eclipse.ui.forms.HyperlinkSettings;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.events.IHyperlinkListener;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.command.internal.env.core.context.ResourceContext;
import org.eclipse.wst.command.internal.env.ui.widgets.PageInfo;
import org.eclipse.wst.command.internal.env.ui.widgets.SimpleWidgetDataContributor;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetContributor;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetContributorFactory;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetDataEvents;
import org.eclipse.wst.ws.internal.parser.wsil.WebServicesParser;
import org.eclipse.wst.ws.internal.ui.plugin.WSUIPlugin;
import org.eclipse.wst.ws.internal.wsrt.IWebServiceRuntimeChecker;
import org.eclipse.wst.ws.internal.wsrt.WebServiceScenario;

public class ServerWizardWidget extends SimpleWidgetDataContributor implements Runnable, IPackable {
	
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
	private ModifyListener objectModifyListener_ ;
	private int validationState_;
	boolean validObjectSelection_ = true;

	private ImageRegistry imageReg_;
	
	private TypeRuntimeServer ids_;
	private LabelsAndIds labelIds_;
	private String serviceProjectName_;
	private IProject initialProject_;
	private String serviceEarProjectName_;
	private String serviceComponentType_;
	private WebServicesParser parser_;
	private boolean needEar_;
		 
	private boolean developService_;
	private boolean assembleService_;
	private boolean deployService_;
	
	private IStructuredSelection objectSelection_;
	private Boolean testService_;
	private Boolean startService_;
	private Boolean installService_;

	private boolean displayPreferences_;
	private boolean preferencesPage_;

	private Composite groupComposite_;
	private Composite hCompService_;	
	private WebServiceClientTypeWidget clientWidget_;
    private Label serviceLabel_;
	private Combo webserviceType_;
	private ComboWithHistory serviceImpl_;
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
	
	private Timer timer_ = null;
		
	private UIUtils utils_ = new UIUtils("org.eclipse.jst.ws.creation.ui");
	
	private boolean forceRefreshRuntime = false;
	
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
    
    /**
     * @param displayPreferences Set to true if preferences such as overwrite should be displayed on the wizard page
     * @param prefPage  Set to true if the widget is being used on the preferences page.  Alters widget layout.
     */
	public ServerWizardWidget(boolean displayPreferences, boolean prefPage) {
		displayPreferences_ = displayPreferences;
		preferencesPage_ = prefPage;
		initImageRegistry();
		validationState_ = ValidationUtils.VALIDATE_ALL;
	}
	
	private Composite serviceComposite_;
	
	/**
	 * Run this ServerWizardWidget, which validates the entry field values.
	 */
	public void run() {
		
		// do minimal check here, this call will determine if we at least
		// have a "validObjectSelection_"
		checkServiceImplTextStatus();
		
		if (validObjectSelection_)
		{
			boolean refreshRuntime = (getWebServiceScenario() != WebServiceScenario.TOPDOWN); 
			
			if (objectSelectionWidget_ instanceof IObjectSelectionLaunchable)
		       {
				IObjectSelectionLaunchable launchable = (IObjectSelectionLaunchable)objectSelectionWidget_;
				callObjectTransformation(launchable.getObjectSelection(), launchable.getProject(), launchable.getComponentName(), refreshRuntime);								
		       }
			else 
			{
				IObjectSelectionWidget widget = (IObjectSelectionWidget)objectSelectionWidget_;
				callObjectTransformation(widget.getObjectSelection(), widget.getProject(), widget.getComponentName(), refreshRuntime);
		    }	
		}
		else
			setObjectSelection(null);
		
		// do full validation after all transformations and calculations have occurred
		validationState_ = ValidationUtils.VALIDATE_ALL;
		statusListener_.handleEvent(null);		
	}
	
	/**
	 * Pack this widget and the client-side widget as well. 
	 */
	private void packSelf() {
		// don't need to call packIt(), since clientWidget_ has a callback to packIt() already.
		clientWidget_.packIt();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jst.ws.internal.consumption.ui.widgets.IPackable#packIt()
	 */
	public void packIt() {		
		groupComposite_.pack(true);
		utils_.horizontalResize(groupComposite_.getShell(), groupComposite_, UIUtils.DEFAULT_PADDING);
	}
	
	public WidgetDataEvents addControls(Composite parent,
			Listener statusListener) {
		
		statusListener_ = statusListener;
		utils_.createInfoPop(parent, INFOPOP_WSWSCEN_PAGE);
		
		Composite typeComposite = utils_.createComposite(parent, 3);

		webserviceType_ = utils_.createCombo(typeComposite,
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
				String oldTypeId = ids_.getTypeId();
				int currentSelectionIdx = webserviceType_.getSelectionIndex();
				String currentTypeId = labelIds_.getIds_()[currentSelectionIdx];
				if (!oldTypeId.equals(currentTypeId)) {					
					ids_.setTypeId(currentTypeId);
					
					// don't need to refresh runtime or validate fields if we are on the preference page, just return
					if (preferencesPage_)
						return;
						
					int oldScenario = WebServiceRuntimeExtensionUtils2.getScenarioFromTypeId(oldTypeId);
					int currentScenario = WebServiceRuntimeExtensionUtils2.getScenarioFromTypeId(currentTypeId);

			      objectSelectionWidget_ = getSelectionWidget();
					// change the label for the service
					// implementation/definition based on the web service type
					handleTypeChange();					

					// After bug 159911, we will not refresh the runtime in TOPDOWN scenario unless we absolutely
					// have to.  Therefore, if we are switching from another scenario type, we should check if the 
					// current selections are valid for this TOPDOWN scenario, if not, try to pick a more suitable 
					// runtime.
					if (currentScenario == WebServiceScenario.TOPDOWN) {
						validationState_ = ValidationUtils.VALIDATE_SERVER_RUNTIME_CHANGES;
						
						// this will force a refresh the next time the wsdl is changed
						forceRefreshRuntime = !checkErrorStatus().isOK();
					}
					
					//if the web service type change is from one top-down type to another
					//top-down type leave the object selection field in tact and refresh
					//the server/runtime project defaulting.
					//Otherwise clear the object selection field since it's value is not valid anymore
					if (oldScenario==WebServiceScenario.TOPDOWN && currentScenario==WebServiceScenario.TOPDOWN)						
					{
						// if switching from one TOPDOWN scenario to another TOPDOWN scenario and
						// we've determined that the current runtime selection is not suitable, we 
						// can refresh the runtime now (don't need to wait for the next time the
						// wsdl is changed).
						if (forceRefreshRuntime) {
							refreshServerRuntimeSelection();
							forceRefreshRuntime = false;
						}
					}
					else
					{
						//clear the object selection field
						// serviceImpl may be null if on the preferences page
						if (serviceImpl_ != null)
						{
							serviceImpl_.removeModifyListener(objectModifyListener_);
							serviceImpl_.setText("");
							setObjectSelection(null);
							serviceImpl_.addModifyListener(objectModifyListener_);
						}						   
					}	
					//restore widget values, serviceImpl may be null if on the preferences page
					if (serviceImpl_ != null) {
						String webServiceTypeID= null;
						   if (ids_ != null)
							   webServiceTypeID = ids_.getTypeId();
							serviceImpl_.removeModifyListener(objectModifyListener_);
							String currentlySelectedValue = serviceImpl_.getText();
							serviceImpl_.removeAll();
							if (currentlySelectedValue != null)
								serviceImpl_.setText(currentlySelectedValue);
							serviceImpl_.restoreWidgetHistory(webServiceTypeID);
							serviceImpl_.addModifyListener(objectModifyListener_);

					}

			   validationState_ = ValidationUtils.VALIDATE_ALL;
			   statusListener_.handleEvent(null);			   
					
			}
		  }
			
		});
		
		// Create text field and browse for object selection if not on preferences page
		if (!preferencesPage_)
		{
			//text of this label is based on the web service type selected in webserviceType_ combo
			serviceLabel_ = new Label( typeComposite, SWT.WRAP);
			serviceLabel_.setText(ConsumptionUIMessages.LABEL_WEBSERVICEIMPL);
			serviceLabel_.setToolTipText(ConsumptionUIMessages.TOOLTIP_WSWSCEN_TEXT_IMPL);

			serviceImpl_ = new ComboWithHistory(typeComposite, SWT.LEFT | SWT.BORDER, WSUIPlugin.getInstance().getDialogSettings());
			GridData griddata = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
			griddata.widthHint = 225;
			serviceImpl_.setLayoutData( griddata );
			serviceImpl_.setToolTipText(ConsumptionUIMessages.TOOLTIP_WSWSCEN_TEXT_IMPL);
			utils_.createInfoPop(serviceImpl_, INFOPOP_WSWSCEN_TEXT_SERVICE_IMPL);
			
			objectModifyListener_ = new ModifyListener(){
				public void modifyText(ModifyEvent e) {
				    if (serviceImpl_.getText().indexOf(':') > 0) {
				        timer_ = Timer.newInstance(timer_, Display.getCurrent(), ServerWizardWidget.this);
				        timer_.startTimer();
				    }
				    else
				        run();
				}
			};
			
			serviceImpl_.addModifyListener(objectModifyListener_);
			serviceImpl_.addSelectionListener(	new SelectionAdapter()
			{
					public void widgetSelected(SelectionEvent e) {
						int index = serviceImpl_.getSelectionIndex();
						serviceImpl_.removeModifyListener(objectModifyListener_);
				       	serviceImpl_.setText(serviceImpl_.getItem(index));
				        serviceImpl_.addModifyListener(objectModifyListener_);
				}
			});
			browseButton_ = utils_.createPushButton(typeComposite,
					ConsumptionUIMessages.BUTTON_BROWSE, ConsumptionUIMessages.TOOLTIP_WSWSCEN_BUTTON_BROWSE_IMPL, null);

			browseButton_.addSelectionListener(new ServiceImplBrowseListener());
		}
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
	    
	    groupComposite_.addControlListener(new ControlListener()
		{
			public void controlMoved(ControlEvent e) {
				// TODO Auto-generated method stub
				
			}
			public void controlResized(ControlEvent e) {
				groupComposite_.pack(true);				
			}
		});
	    
		serviceComposite_ =  new Composite(groupComposite_, SWT.NONE);
		GridLayout gridlayout   = new GridLayout();
	    gridlayout.numColumns   = 2;
	    gridlayout.horizontalSpacing=0;
	    gridlayout.marginHeight=0;
	    serviceComposite_.setLayout( gridlayout );
	    GridData scGridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
	    serviceComposite_.setLayoutData(scGridData);    
	    
		serviceComposite_.setToolTipText(ConsumptionUIMessages.TOOLTIP_WSWSCEN_SCALE_SERVICE);
	
		
		serviceScale_ = new Scale(serviceComposite_, SWT.VERTICAL | SWT.BORDER);
	    utils_.createInfoPop(serviceScale_, INFOPOP_WSWSCEN_SCALE_SERVICE);
		serviceScale_.setMinimum(0);
		serviceScale_.setMaximum(6);
		serviceScale_.setIncrement(1);
		serviceScale_.setPageIncrement(1);
		serviceScale_.addSelectionListener(scaleSelectionListener);
		serviceScale_.setSelection(getServiceGeneration());
		serviceScale_.setToolTipText(ConsumptionUIMessages.TOOLTIP_WSWSCEN_SCALE_SERVICE);
		
		Accessible accessibleScale = serviceScale_.getAccessible();
		accessibleScale.addAccessibleListener(new AccessibleAdapter(){
			//	return a string that describes what to do with the slider
			//  this is read by the screen reader when the slider first gets focus
			public void getName(AccessibleEvent e) {
				e.result = ConsumptionUIMessages.TOOLTIP_WSWSCEN_SCALE_SERVICE;			
			}			
		});
		accessibleScale.addAccessibleControlListener(new AccessibleControlAdapter(){
			// return a meaningful string when the screen reader
			// attempts to get the value from the service scale
			public void getValue(AccessibleControlEvent e) {
				int selection = serviceScale_.getSelection();
				switch (selection) {
				case 0:
					e.result = ConsumptionUIMessages.TOOLTIP_WSWSCEN_SCALE_TEST;
					break;
				case 1:
					e.result = ConsumptionUIMessages.TOOLTIP_WSWSCEN_SCALE_RUN;
					break;
				case 2:
					e.result = ConsumptionUIMessages.TOOLTIP_WSWSCEN_SCALE_INSTALL;
					break;
				case 3:
					e.result = ConsumptionUIMessages.TOOLTIP_WSWSCEN_SCALE_DEPLOY;
					break;
				case 4:
					e.result = ConsumptionUIMessages.TOOLTIP_WSWSCEN_SCALE_ASSEMBLE;
					break;
				case 5:					
				case 6:
					e.result = ConsumptionUIMessages.TOOLTIP_WSWSCEN_SCALE_DEVELOP;
					break;
				default:
					break;
				}								
			}
		});	
		
		GridData layoutData1 = new GridData();
		layoutData1.horizontalAlignment=SWT.CENTER;
		layoutData1.verticalAlignment = SWT.BEGINNING;
		Rectangle scaleR = (imageReg_.get(ICON_SCALE_BG_0)).getBounds();
		layoutData1.heightHint=scaleR.height;
		layoutData1.widthHint=scaleR.width+1;
		serviceScale_.setLayoutData(layoutData1);		
		
		topologySpot_ = new Label(serviceComposite_, SWT.BORDER | SWT.TOP );
		topologySpot_.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		topologySpot_.setToolTipText(ConsumptionUIMessages.TOOLTIP_WSWSCEN_SCALE_SERVICE);
		
		GridData layoutData2 = new GridData();		
		layoutData2.horizontalAlignment=SWT.BEGINNING;				
		layoutData2.verticalAlignment = SWT.BEGINNING;
		Rectangle topR = (imageReg_.get(GRAPHIC_SERVICE_0)).getBounds();
		layoutData2.heightHint=topR.height;
		layoutData2.widthHint=topR.width;
		topologySpot_.setLayoutData(layoutData2);		
				
		setGraphics(getServiceGeneration());
		
		hCompService_ = utils_.createComposite(groupComposite_, 1);
		
		Label serviceDetailsLabel = new Label(hCompService_, SWT.NONE);
		serviceDetailsLabel.setText(ConsumptionUIMessages.LABEL_SUMMARY);
		
		hLinkServiceServer_= new Hyperlink(hCompService_, SWT.NULL);
		utils_.createInfoPop(hLinkServiceServer_, INFOPOP_WSWSCEN_HYPERLINK_SERVER);
		hLinkServiceServer_.setToolTipText(ConsumptionUIMessages.TOOLTIP_PWRS_TEXT_SERVER);
		hLinkServiceServer_.addHyperlinkListener(new IHyperlinkListener(){
			public void linkActivated(HyperlinkEvent e){				
				launchRuntimeSelectionDialog(false, true);				
			}
			public void linkEntered(HyperlinkEvent e){}
			public void linkExited(HyperlinkEvent e){}			
		});

		hLinkServiceRuntime_ = new Hyperlink(hCompService_, SWT.NULL);
		utils_.createInfoPop(hLinkServiceRuntime_, INFOPOP_WSWSCEN_HYPERLINK_RUNTIME);
		hLinkServiceRuntime_.setToolTipText(ConsumptionUIMessages.TOOLTIP_PWRS_TEXT_RUNTIME);
		hLinkServiceRuntime_.addHyperlinkListener(new IHyperlinkListener(){
			public void linkActivated(HyperlinkEvent e){
								launchRuntimeSelectionDialog(false);
			}
			public void linkEntered(HyperlinkEvent e){}
			public void linkExited(HyperlinkEvent e){}			
		});
		
		IWorkbench workbench = PlatformUI.getWorkbench();
		projectDialog_ = new ProjectSelectionDialog(workbench.getActiveWorkbenchWindow().getShell(), 
				new PageInfo(ConsumptionUIMessages.DIALOG_TITILE_SERVICE_PROJECT_SETTINGS, "", 
                        new WidgetContributorFactory()
  						{	
  							public WidgetContributor create()
  							{	  						 
  							   return new ProjectSelectionWidget();
  							}
  						}));
		
		hLinkServiceProject_= new Hyperlink(hCompService_, SWT.NULL);
		utils_.createInfoPop(hLinkServiceRuntime_, INFOPOP_WSWSCEN_HYPERLINK_PROJECTS);
		hLinkServiceProject_.setToolTipText(ConsumptionUIMessages.TOOLTIP_WSWSCEN_SERVICEPROJECT_LINK);
		hLinkServiceProject_.addHyperlinkListener(new IHyperlinkListener(){
			public void linkActivated(HyperlinkEvent e){
				launchProjectDialog();				
			}
			public void linkEntered(HyperlinkEvent e){}
			public void linkExited(HyperlinkEvent e){}			
		});
		
		hLinkServiceEAR_= new Hyperlink(hCompService_, SWT.NULL);
		utils_.createInfoPop(hLinkServiceRuntime_, INFOPOP_WSWSCEN_HYPERLINK_PROJECTS);
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
    
		utils_.createHorizontalSeparator(parent, 1);		
		
		// Add client widgets...
		clientWidget_ = new WebServiceClientTypeWidget(false);
	    clientWidget_.addControls(parent, statusListener );
	    clientWidget_.setPackable(this);
	    clientWidget_.enableClientSlider(serviceScale_.getSelection()<=ScenarioContext.WS_START);
		
		// Advanced buttons section
		utils_.createHorizontalSeparator(parent, 3);
		
		Composite advancedButtonPanel = utils_.createComposite(parent, 1);
		
		publishButton_ = utils_.createCheckbox(advancedButtonPanel,
				ConsumptionUIMessages.BUTTON_WSWSCEN_PUBLISH, ConsumptionUIMessages.TOOLTIP_PWPR_CHECKBOX_LAUNCH_WS, INFOPOP_WSWSCEN_CHECKBOX_LAUNCH_WS);		
		monitorButton_ = utils_.createCheckbox(advancedButtonPanel,
				ConsumptionUIMessages.CHECKBOX_MONITOR_WEBSERVICE, ConsumptionUIMessages.TOOLTIP_PWPR_CHECKBOX_MONITOR_SERVICE, INFOPOP_WSWSCEN_CHECKBOX_MONITOR_SERVICE);

		if (displayPreferences_ && getResourceContext().isOverwriteFilesEnabled()) {
			utils_.createHorizontalSeparator(parent, 1);			
			Composite prefButtonPanel = utils_.createComposite(parent, 1);
			overwriteButton_ = utils_.createCheckbox(prefButtonPanel,
					ConsumptionUIMessages.CHECKBOX_OVERWRITE_FILES, ConsumptionUIMessages.TOOLTIP_WSWSCEN_BUTTON_OVERWRITE_FILES, INFOPOP_WSWSCEN_CHECKBOX_OVERWRITE);
			overwriteButton_.setSelection(getResourceContext()
					.isOverwriteFilesEnabled());
			overwriteButton_.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					getResourceContext().setOverwriteFilesEnabled(overwriteButton_.getSelection());
				}
			});
		}

		return this;
	}	
	
	private void launchProjectDialog()
	{
		String currentProjectName = getServiceProjectName();
		String currentEarProjectName = serviceEarProjectName_;
		String currentProjectType = getServiceComponentType();
		boolean currentNeedEar = getServiceNeedEAR();
		
		projectDialog_.setProjectName(currentProjectName);
		projectDialog_.setEarProjectName(currentEarProjectName);
		projectDialog_.setProjectComponentType(currentProjectType);
		projectDialog_.setNeedEAR(currentNeedEar);
		
		int status = projectDialog_.open();
		if (status == Window.OK)
		{
			String newProjectName = projectDialog_.getProjectName();
			String newEarProjectName = projectDialog_.getEarProjectName();
			String newProjectType = projectDialog_.getProjectComponentType();
			boolean newNeedEar = projectDialog_.getNeedEAR();	
			
			//Update project settings and validate page if selections changed.
			if (!newProjectName.equals(currentProjectName)
					|| !newEarProjectName.equals(currentEarProjectName)
					|| !newProjectType.equals(currentProjectType)
					|| newNeedEar != currentNeedEar) {
				setServiceProjectName(newProjectName);
				setServiceEarProjectName(newEarProjectName);
				setServiceComponentType(newProjectType);
				setServiceNeedEAR(newNeedEar);				
				validationState_ = (new ValidationUtils()).getNewValidationState(validationState_, ValidationUtils.VALIDATE_PROJECT_CHANGES);
				statusListener_.handleEvent(null);
			}
			
			/*check to see if text has changed for project and EAR
			if so, repaint links */
			if (!newProjectName.equals(currentProjectName))
			{
				hLinkServiceProject_.pack(true);
				packSelf();	 
			}
			if (!newEarProjectName.equals(currentEarProjectName))
			{
				hLinkServiceEAR_.pack(true);
				packSelf();	  					
			}
		}
	}
		
	private void launchRuntimeSelectionDialog(boolean clientContext)
	{
		launchRuntimeSelectionDialog(clientContext, false);
	}
		
	private void launchRuntimeSelectionDialog(boolean clientContext, boolean selectServerFirst) 	
	{	
		byte mode = clientContext ? (byte)1 : (byte)0;
		
		//TODO: jvh - investigate - don't think j2ee version shouldn't be hard coded (last parm) 
		//  question - where to pick it up from?
		//Remember the current values
		TypeRuntimeServer currentServiceTRS = getServiceTypeRuntimeServer();
		IWorkbench workbench = PlatformUI.getWorkbench();
		RuntimeServerSelectionDialog rssd = new RuntimeServerSelectionDialog(
				workbench.getActiveWorkbenchWindow().getShell(), mode, currentServiceTRS, "14");	
		rssd.setSelectServerFirst(selectServerFirst);		
		int result = rssd.open();		
		if (result == Window.OK)
		{
			TypeRuntimeServer newServiceTRS = rssd.getTypeRuntimeServer();
			if (!currentServiceTRS.equals(newServiceTRS))
			{
				setServiceTypeRuntimeServer(newServiceTRS);	
				refreshServiceProjectType();
				refreshClientServerRuntimeSelection();
				validationState_ = (new ValidationUtils()).getNewValidationState(validationState_, ValidationUtils.VALIDATE_SERVER_RUNTIME_CHANGES);
				clientWidget_.setValidationState(ValidationUtils.VALIDATE_SERVER_RUNTIME_CHANGES);
				statusListener_.handleEvent(null); //Revalidate the page since server/runtime selections changed.
			}
			
		}		
	}	
	
	/**
	 * Update the service project type
	 */
	private void refreshServiceProjectType(){
			
		IProject serviceProject = ResourcesPlugin.getWorkspace().getRoot().getProject(getServiceProjectName());
		if (!serviceProject.exists())
		{
			// Set the project template
			setServiceComponentType(getDefaultServiceProjectTemplate());
		}
		else
		{
			//Set it to an empty String
			setServiceComponentType("");
		}
    }
	

	
	/*
	 * Computes an acceptable value for the service project type based on the 
	 * current runtime
	 * @return A default value for the service project type
	*/
	private String getDefaultServiceProjectTemplate()
	{
		String serviceTypeId = getServiceTypeRuntimeServer().getTypeId();  
		String serviceRuntimeId = getServiceTypeRuntimeServer().getRuntimeId();
		String serviceServerId = getServiceTypeRuntimeServer().getServerId();
	    String[] templates = WebServiceRuntimeExtensionUtils2.getServiceProjectTemplates(serviceTypeId, serviceRuntimeId, serviceServerId);
	    String oldServiceComponentType = getServiceComponentType();
	    for(String templateId : templates) {
	    	if(templateId.equals(oldServiceComponentType))
	    		return templateId;
	    }
	    
	    //Walk the list of service project types in the project topology preference
	    ProjectTopologyContext ptc= WebServiceConsumptionUIPlugin.getInstance().getProjectTopologyContext();
	    String[] preferredTemplateIds = ptc.getServiceTypes();
	    
	    for (int j = 0; j < preferredTemplateIds.length; j++)
	    {
	      for (int i = 0; i < templates.length; i++)
	      {
	        String templateId = templates[i];
	        if (templateId.equals(preferredTemplateIds[j]))
	        {
	            return templates[i];
	        }
	      }
	    }
	    // No supported template was found that matched a runtime.
	    return "";   
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
	
	public void setStartClient(Boolean startClient) {
		clientWidget_.setStartClient( startClient );
	}

	public Boolean getStartClient() {		
		return clientWidget_.getStartClient();
	}
	
	public void setTestClient(Boolean testClient) {
		clientWidget_.setTestClient( testClient );
	}

	public Boolean getTestClient() {		
		return clientWidget_.getTestClient();
	}
	
	public void setDevelopClient(boolean develop) {
		clientWidget_.setDevelopClient( develop );
	}

	public boolean getDevelopClient() {		
		return clientWidget_.getDevelopClient();
	}
	
	public void setAssembleClient(boolean assemble) {
		clientWidget_.setAssembleClient( assemble );
	}

	public boolean getAssembleClient() {		
		return clientWidget_.getAssembleClient();
	}
	
	public void setDeployClient(boolean deploy) {
		clientWidget_.setDeployClient( deploy );
	}

	public boolean getDeployClient() {		
		return clientWidget_.getDeployClient();
	}

private void handleTypeChange()
	{
		if (!preferencesPage_) {
		   int scenario = getWebServiceScenario();
		   
		   if (scenario == WebServiceScenario.BOTTOMUP)
		   {
			  serviceLabel_.setText(ConsumptionUIMessages.LABEL_WEBSERVICEIMPL);
		   }
		   else if (scenario == WebServiceScenario.TOPDOWN)
		   {
			   serviceLabel_.setText(ConsumptionUIMessages.LABEL_WEBSERVICEDEF);
		   }
		}
	}

	public void setServiceTypeRuntimeServer(TypeRuntimeServer ids) {
		LabelsAndIds labelIds = WebServiceRuntimeExtensionUtils2
				.getServiceTypeLabels();
		// rskreg
		int selection = 0;
		String[] serviceIds = labelIds.getIds_();
		String selectedId = ids.getTypeId();

		webserviceType_.setItems(labelIds.getLabels_());

		// Now find the selected one.
		for (int index = 0; index < serviceIds.length; index++) {
			if (selectedId.equals(serviceIds[index])) {
				selection = index;
				break;
			}
		}
		webserviceType_.select(selection);
		
		ids_ = ids;
		
		if (ids_ != null)
		{
			String serviceServerText = "";
			String serverId = ids_.getServerId();
			if (serverId != null && serverId.length()>0)
			{
			  serviceServerText = WebServiceRuntimeExtensionUtils2.getServerLabelById(serverId);
			}
			String serviceRuntimeText = WebServiceRuntimeExtensionUtils2.getRuntimeLabelById(ids_.getRuntimeId());
			
			String currentServerText = hLinkServiceServer_.getText();
			String currentRuntimeText = hLinkServiceRuntime_.getText();
			String newServerText = SERVICE_SERVER_PREFIX + " " + serviceServerText;
			String newRuntimeText = SERVICE_RUNTIME_PREFIX + " " + serviceRuntimeText;
			hLinkServiceServer_.setText(newServerText);
			hLinkServiceRuntime_.setText(newRuntimeText);		
			
			/*check to see if text has changed for server or runtime
			if so, repaint links */
			if (!newServerText.equals(currentServerText))
			{
				hLinkServiceServer_.pack(true);
				packSelf();
			}			
			
			if (!newRuntimeText.equals(currentRuntimeText))
			{
				hLinkServiceRuntime_.pack(true);
				packSelf();
			} 
		}				
		labelIds_ = labelIds;
		handleTypeChange();
		
		if (projectDialog_ != null)
			projectDialog_.setTypeRuntimeServer(ids_);
		
		
		//When the server changes, the state of needEar could change.
		//If the the server change results in a change in the state of needEar,
		//update needEar and serviceEarProjectName.
		ValidationUtils vu = new ValidationUtils();
		boolean oldNeedEar = getServiceNeedEAR();
		boolean serviceProjectOrProjectTypeNeedsEar;

		if (!oldNeedEar)
		{
			//If an EAR was not needed previously it could have been because of the project/project type or the server.
			//If it was because of the project/project type, changing the server should have no impact
			//on the state of needEar.
			serviceProjectOrProjectTypeNeedsEar = vu.projectOrProjectTypeNeedsEar(getServiceProjectName(), getServiceComponentType());
		}
		else
		{
			serviceProjectOrProjectTypeNeedsEar = true;
		}
		
		//boolean serviceProjectOrProjectTypeNeedsEar = vu.projectOrProjectTypeNeedsEar(getServiceProjectName(), getServiceComponentType());
		if (serviceProjectOrProjectTypeNeedsEar)
		{
			//Could not rule out need for an Ear from the project/project type so changing the server
			//may impact the need for an Ear.

			boolean currentServiceServerNeedsEar = vu.serverNeedsEAR(getServiceTypeRuntimeServer().getServerId());
			if (oldNeedEar != currentServiceServerNeedsEar)
			{
				//Update needEar and serviceEarProjectName.
				if (currentServiceServerNeedsEar)
				{
					//Calculate a reasonable default for the Ear project name
					String earProjectName = DefaultingUtils.getDefaultEARProjectName(getServiceProjectName());
					setServiceNeedEAR(currentServiceServerNeedsEar);
					setServiceEarProjectName(earProjectName);
				}
				else
				{
					setServiceNeedEAR(currentServiceServerNeedsEar);
					setServiceEarProjectName("");					
				}
				
			}
		}		
	}

	public TypeRuntimeServer getServiceTypeRuntimeServer() {
		return ids_;
	}

	public void setServiceGeneration(int value)
	{
		serviceScale_.setSelection(value);
		setGraphics(value);
		
		//enable client widget based on service scale setting
		clientWidget_.enableClientSlider(value<=ScenarioContext.WS_START);
		
		/*for popup case need to make sure that the UI is refreshed based on 
		changes to data*/
		packSelf();
	}	

	public int getServiceGeneration()
	{
		return serviceScale_.getSelection();
	}
	
	public boolean getDevelopService() {
		return developService_;
	}

	public void setDevelopService(boolean developService) {
		this.developService_ = developService;
	}	
	
	public boolean getAssembleService() {
		return assembleService_;
	}

	public void setAssembleService(boolean assembleService) {
		this.assembleService_ = assembleService;
	}

	public boolean getDeployService() {
		return deployService_;
	}

	public void setDeployService(boolean deployService) {
		this.deployService_ = deployService;
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
        new Boolean( testService_.booleanValue() || clientWidget_.getTestClient().booleanValue());
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
		return new Boolean(clientWidget_.getGenerateProxy());		
	}

	public void setClientGeneration(int value)
	{
		clientWidget_.setClientGeneration(value);
		/*for popup case need to make sure that the UI is refreshed based on 
			changes to data*/
		packSelf();
	}
	
	public String getClientEarProjectName()
	{
		if(clientWidget_.isOSGI()) {
			return null;
		} else {
			return clientWidget_.getClientEarProjectName();
		}
	}
	
	public String getClientOsgiAppProjectName() {
		if(clientWidget_.isOSGI()) {
			return clientWidget_.getClientEarProjectName();
		} else {
			return null;
		}
		
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
	public void externalize() {
		super.externalize();
		if (!preferencesPage_) {
			   String webServiceTypeID= null;
			   if (ids_ != null)
				   webServiceTypeID = ids_.getTypeId();
			   serviceImpl_.storeWidgetHistory(webServiceTypeID);
			}
	}
	
	public void internalize() {		
		
		if (preferencesPage_)
			return;
		
		if (objectSelectionWidget_ == null)
			objectSelectionWidget_ = getSelectionWidget();
		
		String displayable = null;
		
		if (objectSelectionWidget_ instanceof IObjectSelectionLaunchable) {		
			IObjectSelectionLaunchable launchable = (IObjectSelectionLaunchable) objectSelectionWidget_;
			launchable.setInitialSelection(objectSelection_);
			objectSelection_ = launchable.getObjectSelection();
			displayable = launchable.getObjectSelectionDisplayableString();
		}
		else {
			IObjectSelectionWidget selectionWidget = (IObjectSelectionWidget) objectSelectionWidget_;
			selectionWidget.setInitialSelection(objectSelection_);
		    displayable = selectionWidget.getObjectSelectionDisplayableString();
		}

		serviceImpl_.removeModifyListener(objectModifyListener_);
		if (displayable != null && displayable.length() != 0)
			serviceImpl_.setText(displayable);
		String webServiceTypeID = null;
		if (ids_ != null)
			webServiceTypeID = ids_.getTypeId();
		serviceImpl_.restoreWidgetHistory(webServiceTypeID);
		serviceImpl_.addModifyListener(objectModifyListener_);

			
	}

	public IStatus getStatus() {
		IStatus status = Status.OK_STATUS;

		IStatus missingFieldStatus = checkMissingFieldStatus();
		if (missingFieldStatus.getSeverity() == IStatus.ERROR) {
			return missingFieldStatus;
		}

		IStatus serviceImplValidationStatus = checkServiceImplTextStatus();
		if (serviceImplValidationStatus.getSeverity() == IStatus.ERROR) {
			return serviceImplValidationStatus;
		}
		else if (serviceImplValidationStatus.getSeverity() == IStatus.WARNING){
			return serviceImplValidationStatus;
		}

		IStatus possibleErrorStatus = checkErrorStatus();
		if (possibleErrorStatus.getSeverity() == IStatus.ERROR || possibleErrorStatus.getSeverity() == IStatus.WARNING) {
			return possibleErrorStatus;
		}

		IStatus possibleWarningStatus = checkWarningStatus();
		if (possibleWarningStatus.getSeverity() == IStatus.WARNING) {
			return possibleWarningStatus;
		}

		// If no warnings/errors were reported, clear validation state on
		// service side and client side (if enabled)
		validationState_ = ValidationUtils.VALIDATE_NONE;
		if (clientWidget_.getGenerateProxy()) {
			clientWidget_.setValidationState(ValidationUtils.VALIDATE_NONE);
		}

		return status;
	}
	
	/*
	 * call validation code in the object selection widget to ensure any
	 * modifications to the serviceImpl_ field are valid
	 */
	private IStatus checkServiceImplTextStatus() {
		
		String fieldText = serviceImpl_.getText().trim();

		if (objectSelectionWidget_ == null)
		{
			objectSelectionWidget_ = getSelectionWidget();
		}
		
		IObjectSelectionLaunchable launchable = null;
		if (objectSelectionWidget_ instanceof IObjectSelectionLaunchable)
		    {      	
				launchable = (IObjectSelectionLaunchable)objectSelectionWidget_;
				validObjectSelection_ = launchable.validate(fieldText);
			}
			else 
			{
				IObjectSelectionWidget widget = (IObjectSelectionWidget)objectSelectionWidget_;
				validObjectSelection_ = widget.validate(fieldText);
			}
		
		if (!validObjectSelection_)
		{
			int scenario = getWebServiceScenario();
			
			if (scenario == WebServiceScenario.BOTTOMUP){
				if (launchable != null){
					IStatus valStatus = launchable.validateSelection(new StructuredSelection(fieldText));
					if (valStatus.getSeverity() == IStatus.WARNING)
 					  return StatusUtils.warningStatus(ConsumptionUIMessages.MSG_WARN_SERVICE_IMPL_NAMING_CONVENTION);
					else if (valStatus.getSeverity() == IStatus.ERROR)
						return StatusUtils.errorStatus(ConsumptionUIMessages.MSG_INVALID_SERVICE_IMPL);
				}
				else
					return StatusUtils.errorStatus(ConsumptionUIMessages.MSG_INVALID_SERVICE_IMPL);
			}
			else
				return StatusUtils.errorStatus(ConsumptionUIMessages.MSG_INVALID_SERVICE_DEF);			
		}		
		
		return Status.OK_STATUS;
	}

	private int getWebServiceScenario()
	{
		   int index = webserviceType_.getSelectionIndex();	
		   String typeId = labelIds_.getIds_()[index];
		   return WebServiceRuntimeExtensionUtils2.getScenarioFromTypeId(typeId);		
	}
	
	private IStatus checkMissingFieldStatus() {

		// 1. Check for missing fields on service side
		ValidationUtils valUtils = new ValidationUtils();
		String serviceImpl = serviceImpl_.getText().trim();
		String typeId = getServiceTypeRuntimeServer().getTypeId();
		String runtimeId = getServiceTypeRuntimeServer().getRuntimeId();
		String serverId = getServiceTypeRuntimeServer().getServerId();
		String projectName = getServiceProjectName();
		boolean needEar = getServiceNeedEAR();
		String earProjectName = serviceEarProjectName_;
		String projectTypeId = getServiceComponentType();
		
		IStatus serviceMissingFieldStatus = valUtils.checkMissingFieldStatus(validationState_, typeId, serviceImpl,
				runtimeId, serverId, projectName, needEar, earProjectName, projectTypeId, false);
		if (serviceMissingFieldStatus.getSeverity() == IStatus.ERROR) {
			return serviceMissingFieldStatus;
		}

		// 2. Check for missing fields on the client side if it's visible.
		if (clientWidget_.getGenerateProxy()) {
			IStatus clientMissingFieldsStatus = clientWidget_.checkMissingFieldStatus();
			if (clientMissingFieldsStatus.getSeverity() == IStatus.ERROR) {
				return clientMissingFieldsStatus;
			}

		}

		return Status.OK_STATUS;
	}

	private IStatus checkErrorStatus() {

		ValidationUtils valUtils = new ValidationUtils();

		// 1. Check for errors on service side
		String projectName = getServiceProjectName();
		boolean needEar = getServiceNeedEAR();
		String earProjectName = serviceEarProjectName_;
		String projectTypeId = getServiceComponentType();
		String initialProjectName = "";
		if (initialProject_ != null) {
			initialProjectName = initialProject_.getName();
		} 
		IStatus serviceSideErrorStatus = valUtils.checkErrorStatus(validationState_, getServiceTypeRuntimeServer(), projectName, initialProjectName, needEar, earProjectName, projectTypeId, false);
		if (serviceSideErrorStatus.getSeverity() == IStatus.ERROR) {
			return serviceSideErrorStatus;
		}

		// 2. Check for errors on client side if it is visible
		if (clientWidget_.getGenerateProxy()) {
			IStatus clientSideErrorStatus = clientWidget_.checkErrorStatus();
			if (clientSideErrorStatus.getSeverity() == IStatus.ERROR) {
				return clientSideErrorStatus;
			}

			// 3. Check for errors that span service and client if client side
			// is visible.
			int clientValidationState = clientWidget_.getValidationState();
			if (validationState_ == ValidationUtils.VALIDATE_ALL
					|| validationState_ == ValidationUtils.VALIDATE_PROJECT_CHANGES
					|| clientValidationState == ValidationUtils.VALIDATE_ALL
					|| clientValidationState == ValidationUtils.VALIDATE_PROJECT_CHANGES) {
				IWebServiceRuntimeChecker runtimeChecker = null;

				String serviceRuntimeId = WebServiceRuntimeExtensionUtils2.getServiceRuntimeId(getServiceTypeRuntimeServer(), projectName, projectTypeId);	    		
				runtimeChecker = WebServiceRuntimeExtensionUtils2.getServiceRuntimeChecker(serviceRuntimeId);

				if (runtimeChecker != null) {
					return runtimeChecker.checkServiceClientCompatibility(
							getServiceNeedEAR(), serviceEarProjectName_, getServiceProjectName(), 
							clientWidget_.getClientNeedEAR(), clientWidget_.getClientEarProjectName(), 
							clientWidget_.getClientProjectName());
				}
			}
		}

		return Status.OK_STATUS;
	}

	private IStatus checkWarningStatus() {
		ValidationUtils valUtils = new ValidationUtils();
		// 1. Check for warnings on the service side
		int scaleSetting = getServiceGeneration();
		String serverId = getServiceTypeRuntimeServer().getServerId();
		IStatus serviceWarningStatus = valUtils.checkWarningStatus(validationState_, scaleSetting, serverId, getServiceTypeRuntimeServer().getServerInstanceId(), false, getServiceProjectName(), initialProject_, getServiceTypeRuntimeServer().getTypeId());
		if (serviceWarningStatus.getSeverity() == IStatus.WARNING) {
			return serviceWarningStatus;
		}

		// 2. Check for warnings on the client side if it's enabled
		if (clientWidget_.getGenerateProxy()) {
			IStatus clientWarningStatus = clientWidget_.checkWarningStatus();
			if (clientWarningStatus.getSeverity() == IStatus.WARNING) {
				return clientWarningStatus;
			}

		}

		return Status.OK_STATUS;
	}
		
	protected void initImageRegistry()
	{
		imageReg_ = new ImageRegistry(Display.getCurrent());
		
		imageReg_.put(ICON_SCALE_BG_0, WebServiceConsumptionUIPlugin
				.getImageDescriptor(ICON_SCALE_BG_0));
		imageReg_.put(ICON_SCALE_BG_1, WebServiceConsumptionUIPlugin
				.getImageDescriptor(ICON_SCALE_BG_1));
		imageReg_.put(ICON_SCALE_BG_2, WebServiceConsumptionUIPlugin
				.getImageDescriptor(ICON_SCALE_BG_2));
		imageReg_.put(ICON_SCALE_BG_3, WebServiceConsumptionUIPlugin
				.getImageDescriptor(ICON_SCALE_BG_3));
		imageReg_.put(ICON_SCALE_BG_4, WebServiceConsumptionUIPlugin
				.getImageDescriptor(ICON_SCALE_BG_4));
		imageReg_.put(ICON_SCALE_BG_5, WebServiceConsumptionUIPlugin
				.getImageDescriptor(ICON_SCALE_BG_5));
		imageReg_.put(ICON_SCALE_BG_6, WebServiceConsumptionUIPlugin
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
		packSelf();	
	  }
	
	public void setInitialProject(IProject project)
	{
		initialProject_ = project;
	}
	
	 public void setServiceEarProjectName(String name)
	  {	  
		 serviceEarProjectName_ = name;
		 refreshEARLink();
	  }
	 
	 private boolean isOSGI() 
	 {
		 
		 if(DefaultingUtils.isOSGIProject(serviceProjectName_)) 
		 {
			 return true;
		 }
		 
		 if(serviceComponentType_ != null && DefaultingUtils.isOSGITemplate(serviceComponentType_)) 
		 {
			 return true;
		 }
		 
		 return false;
	 }
	 
	  public void refreshEARLink()
	  {
		  hLinkServiceEAR_.setVisible(needEar_); 
		  if (needEar_)
		  {			 
			  
			  if(isOSGI()) 
			  {
				  
				  hLinkServiceEAR_.setText(ConsumptionUIMessages.LABEL_SERVICE_OSGI_PROJECT + " " + serviceEarProjectName_);
				  hLinkServiceEAR_.setToolTipText(ConsumptionUIMessages.TOOLTIP_WSWSCEN_SERVICEPROJECT_LINK_OSGI);
				  hLinkServiceProject_.setToolTipText(ConsumptionUIMessages.TOOLTIP_WSWSCEN_SERVICEPROJECT_LINK_OSGI);
			  } else 
			  {
				  hLinkServiceEAR_.setText(SERVICE_EAR_PREFIX + " " + serviceEarProjectName_);
				  hLinkServiceEAR_.setToolTipText(ConsumptionUIMessages.TOOLTIP_WSWSCEN_SERVICEPROJECT_LINK);
				  hLinkServiceProject_.setToolTipText(ConsumptionUIMessages.TOOLTIP_WSWSCEN_SERVICEPROJECT_LINK);
			  }
			  
			  hLinkServiceEAR_.pack(true);
			  packSelf();
			  
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
	  
	  IProject project_;
	  
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
		  if(isOSGI()) 
		  {
			  return null; 
		  } 
		  else 
		  {
			  return serviceEarProjectName_;
		  }
	  }
	  
	  public String getServiceOsgiAppProjectName() {
		  if(isOSGI()) 
		  {
			  return serviceEarProjectName_;
		  } else 
		  {
			  return null;
		  }
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
	  
	  public String getServiceRuntimeId()
	  {
		  // calculate the most appropriate serviceRuntimeId based on current settings.
		  return WebServiceRuntimeExtensionUtils2.getServiceRuntimeId(getServiceTypeRuntimeServer(), getServiceProjectName(), getServiceComponentType());    
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
			String         wst      = getServiceTypeRuntimeServer().getTypeId();
			int            scenario = WebServiceRuntimeExtensionUtils2.getScenarioFromTypeId(wst);
			String         implId   = WebServiceRuntimeExtensionUtils2.getWebServiceImplIdFromTypeId(wst);
		  WebServiceImpl wsimpl   = WebServiceRuntimeExtensionUtils2.getWebServiceImplById(implId);
      Object         result   = null;
        
		    
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
        
        ObjectSelectionRegistry registry = ObjectSelectionRegistry.getInstance();
        boolean editable = registry.getExternalModify( objectSelectionWidgetId );
                
        serviceImpl_.setEnabled( editable );
        result = registry.getSelectionWidget( objectSelectionWidgetId );
      }
      
		  return result;
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
	      serverRTDefaultCmd.setGenerateProxy(clientWidget_.getGenerateProxy());
	      serverRTDefaultCmd.setServiceTypeRuntimeServer(getServiceTypeRuntimeServer());
		  serverRTDefaultCmd.setWebServicesParser(getWebServicesParser());     
		  serverRTDefaultCmd.setClientInitialSelection(getObjectSelection());
	      serverRTDefaultCmd.setClientInitialProject(getClientProject());
	      serverRTDefaultCmd.setClientEarProjectName(clientWidget_.getClientEarProjectName());
		  serverRTDefaultCmd.setClientTypeRuntimeServer(getClientTypeRuntimeServer());
		  		  
		  serverRTDefaultCmd.execute(null, null);
		  
		  //perform mappings from the defaulting command to the project settings...	
		  setServiceProjectName(serverRTDefaultCmd.getServiceProjectName());
		  setServiceEarProjectName(serverRTDefaultCmd.getServiceEarProjectName());
		  setServiceComponentType(serverRTDefaultCmd.getServiceComponentType());
		  setClientProjectName(serverRTDefaultCmd.getClientProjectName());
		  setClientEarProjectName(serverRTDefaultCmd.getClientEarProjectName());
		  setServiceTypeRuntimeServer(serverRTDefaultCmd.getServiceTypeRuntimeServer());
          setClientTypeRuntimeServer(serverRTDefaultCmd.getClientTypeRuntimeServer());
          setServiceNeedEAR(serverRTDefaultCmd.getServiceNeedEAR());
          setClientNeedEAR(serverRTDefaultCmd.getClientNeedEAR());
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
	}	
		
	
	private void callObjectTransformation(IStructuredSelection objectSelection,
			IProject project, String componentName, boolean refreshRuntime)
	{
		ObjectSelectionOutputCommand objOutputCommand = new ObjectSelectionOutputCommand();
		   objOutputCommand.setTypeRuntimeServer(getServiceTypeRuntimeServer());
		   objOutputCommand.setObjectSelection(objectSelection);
	       objOutputCommand.setProject(project);
	       objOutputCommand.setComponentName(componentName);			   
	       
	       objOutputCommand.execute(null, null);
        
	       setWebServicesParser(objOutputCommand.getWebServicesParser());
	       setObjectSelection(objOutputCommand.getObjectSelection());
	       setProject(objOutputCommand.getProject());      		       
	       
	       if (refreshRuntime || forceRefreshRuntime)
	    	   refreshServerRuntimeSelection();  
	       
	       forceRefreshRuntime = false;
	}
	
	private class ScaleSelectionListener implements SelectionListener
	{
		public void widgetSelected(SelectionEvent e) {			
			    
			    Scale scale = (Scale)e.widget;
				int selection = scale.getSelection();
				
				//TODO: change the hard coded integers here to the the ScenarioContext.WS_xxx when in less critical phase
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
					serviceScale_.setSelection(ScenarioContext.WS_DEVELOP); //"no selection" is not allowed...must develop service @ minimum
					serviceScale_.setToolTipText(ConsumptionUIMessages.TOOLTIP_WSWSCEN_SCALE_DEVELOP);
					//reset local selection so that setters get correct value below
					selection = ScenarioContext.WS_DEVELOP;
					break;
				default:
					break;
				}
				
				setDevelopService(selection <= ScenarioContext.WS_DEVELOP);
				setAssembleService(selection <= ScenarioContext.WS_ASSEMBLE);
				setDeployService(selection <= ScenarioContext.WS_DEPLOY);
				
				setTestService(new Boolean(selection <= ScenarioContext.WS_TEST));
				setInstallService(new Boolean(selection <= ScenarioContext.WS_INSTALL));
				setStartService(new Boolean(selection <= ScenarioContext.WS_START));
				setAdvancedOptions(selection <= ScenarioContext.WS_INSTALL);
				clientWidget_.enableClientSlider(selection <= ScenarioContext.WS_START);
				
				setGraphics(selection);
				
				//Validate the page
				validationState_ = (new ValidationUtils()).getNewValidationState(validationState_, ValidationUtils.VALIDATE_SCALE_CHANGES);
				statusListener_.handleEvent(null);
				
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
			  
			   IStructuredSelection objectSelection = null;
			   IProject project = null;
			   String componentName="";
			   int result=Dialog.CANCEL;
			   
			   // This should be the Web Service Wizard Dialog's shell, and should be non-null.
			   Shell shell = Display.getCurrent().getActiveShell();
			   
			   if (objectSelectionWidget_ instanceof IObjectSelectionLaunchable)
		       {      	
				 IObjectSelectionLaunchable launchable = ((IObjectSelectionLaunchable)objectSelectionWidget_);
				 launchable.setInitialSelection(getObjectSelection());

				 // Bug 269097 - Use the Web Service's dialog shell, not the platform workbench's shell
		         result = launchable.launch(shell);
		         if (result == Dialog.OK)
		         {
		        	 serviceImpl_.removeModifyListener(objectModifyListener_);
			         serviceImpl_.setText(launchable.getObjectSelectionDisplayableString());
			         serviceImpl_.addModifyListener(objectModifyListener_);
			         objectSelection = launchable.getObjectSelection();
			         project = launchable.getProject();
			         componentName= launchable.getComponentName();
		         }
		       }
			   else
			   {
					// bug 269097 -  - Use the Web Service's dialog shell, not the platform workbench's shell.
					browseDialog_ = new ServiceImplSelectionDialog(shell, 
							new PageInfo(ConsumptionUIMessages.DIALOG_TITILE_SERVICE_IMPL_SELECTION, "", 
									new WidgetContributorFactory()
							{	
								public WidgetContributor create()
								{	  						 
									return new ObjectSelectionWidget();
								}
							}));		

				   browseDialog_.setTypeRuntimeServer(getServiceTypeRuntimeServer());
				   browseDialog_.setInitialSelection(getObjectSelection());
			       result = browseDialog_.open();
			       if (result == Dialog.OK)
			       {
			    	   serviceImpl_.removeModifyListener(objectModifyListener_);
				       serviceImpl_.setText(browseDialog_.getDisplayableSelectionString()); 
				       serviceImpl_.addModifyListener(objectModifyListener_);
				       objectSelection = browseDialog_.getObjectSelection();
				       project = browseDialog_.getProject();
				       componentName= browseDialog_.getComponentName();			       
			       }
			       browseDialog_ = null;
			   }
			   
			   // call ObjectSelectionOutputCommand to carry out any transformation on the objectSelection
			   if (result == Dialog.OK)
			   {
				   callObjectTransformation(objectSelection, project, componentName, getWebServiceScenario() != WebServiceScenario.TOPDOWN);				   
			       validationState_ = ValidationUtils.VALIDATE_ALL;
			       statusListener_.handleEvent(null); // validate the page
			   }			   
	    }
	}

	
}

