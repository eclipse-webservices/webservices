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
 * 20060407   135443 joan@ca.ibm.com - Joan Haggarty
 * 20060410   135441 joan@ca.ibm.com - Joan Haggarty
 * 20060410   136030 kathy@ca.ibm.com - Kathy Chan
 * 20060411   136167 kathy@ca.ibm.com - Kathy Chan
 * 20060417   136390 joan@ca.ibm.com - Joan Haggarty
 * 20060413   135581 rsinha@ca.ibm.com - Rupam Kuehner
 * 20060420   136158 rsinha@ca.ibm.com - Rupam Kuehner
 * 20060420   136705 rsinha@ca.ibm.com - Rupam Kuehner
 * 20060421   136761 rsinha@ca.ibm.com - Rupam Kuehner
 * 20060424   138052 kathy@ca.ibm.com - Kathy Chan
 * 20060425   137831 rsinha@ca.ibm.com - Rupam Kuehner
 * 20060426   137622 joan@ca.ibm.com - Joan Haggarty
 * 20060427   138058 joan@ca.ibm.com - Joan Haggarty
 * 20060504   138035 joan@ca.ibm.com - Joan Haggarty
 * 20060529   141422 kathy@ca.ibm.com - Kathy Chan
 * 20060607   144826 joan@ca.ibm.com - Joan Haggarty
 * 20060607   144049 joan@ca.ibm.com - Joan Haggarty
 * 20060614   146270 joan@ca.ibm.com - Joan Haggarty
 * 20060717   150577 makandre@ca.ibm.com - Andrew Mak
 * 20060726   150865 sengpl@ca.ibm.com - Seng Phung-Lu
 * 20060817   140017 makandre@ca.ibm.com - Andrew Mak, longer project or server/runtime strings do not resize wizard
 * 20060829   155441 makandre@ca.ibm.com - Andrew Mak, web service wizard hangs during resize
 * 20070126   138484 kathy@ca.ibm.com - Kathy Chan
 * 20071130   203826 kathy@ca.ibm.com - Kathy Chan
 * 20080205   170141 kathy@ca.ibm.com - Kathy Chan
 * 20080312   147442 trungha@ca.ibm.com - Trung Ha
 * 20080311   222103 trungha@ca.ibm.com - Trung, Changing service scale doesn't update completely non-UI part of client scale
 * 20080327   224452 trungha@ca.ibm.com - Trung Ha
 * 20080416   215084 gilberta@ca.ibm.com - Gilbert Andrews
 * 20090121   261730 zhang@ca.ibm.com - Allan Zhang, WebService client runtime id return null
 * 20150311   461526 jgwest@ca.ibm.com - Jonathan West,  Allow OSGi bundles to be selected in the Wizard 
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.ui.widgets;


import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.jst.ws.internal.consumption.ui.ConsumptionUIMessages;
import org.eclipse.jst.ws.internal.consumption.ui.common.DefaultingUtils;
import org.eclipse.jst.ws.internal.consumption.ui.common.ValidationUtils;
import org.eclipse.jst.ws.internal.consumption.ui.plugin.WebServiceConsumptionUIPlugin;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.runtime.ClientRuntimeSelectionWidgetDefaultingCommand;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.runtime.ProjectSelectionWidget;
import org.eclipse.jst.ws.internal.consumption.ui.wizard.RuntimeServerSelectionDialog;
import org.eclipse.jst.ws.internal.consumption.ui.wsrt.WebServiceRuntimeExtensionUtils2;
import org.eclipse.jst.ws.internal.context.ScenarioContext;
import org.eclipse.jst.ws.internal.data.LabelsAndIds;
import org.eclipse.jst.ws.internal.data.TypeRuntimeServer;
import org.eclipse.jst.ws.internal.ui.common.UIUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.Accessible;
import org.eclipse.swt.accessibility.AccessibleAdapter;
import org.eclipse.swt.accessibility.AccessibleControlAdapter;
import org.eclipse.swt.accessibility.AccessibleControlEvent;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
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
import org.eclipse.wst.command.internal.env.ui.widgets.PageInfo;
import org.eclipse.wst.command.internal.env.ui.widgets.SimpleWidgetDataContributor;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetContributor;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetContributorFactory;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetDataEvents;
import org.eclipse.wst.ws.internal.parser.wsil.WebServicesParser;


public class WebServiceClientTypeWidget extends SimpleWidgetDataContributor implements IPackable
{    
	 // INFOPOPS	 
	 /* CONTEXT_ID WSWSCEN0022 for the Server hyperlink of the Scenario Page */
	 private String INFOPOP_WSWSCEN_HYPERLINK_SERVER = "WSWSCEN0022";
	 /* CONTEXT_ID WSWSCEN0023 for the Runtime hyperlink of the Scenario Page */
	 private String INFOPOP_WSWSCEN_HYPERLINK_RUNTIME = "WSWSCEN0023";
	 /* CONTEXT_ID WSWSCEN0024 for theProjects hyperlink of the Scenario Page */
     private String INFOPOP_WSWSCEN_HYPERLINK_PROJECTS  = "WSWSCEN0024";
	 /* CONTEXT_ID WSWSCEN0025 for theClient Type combo box of the Scenario Page */
	 private String INFOPOP_WSWSCEN_COMBO_CLIENTTYPE = "WSWSCEN0025";
	 /* CONTEXT_ID WSWSCEN0026 for theClient Slider of the Scenario Page */
	 private String INFOPOP_WSWSCEN_SCALE_CLIENT = "WSWSCEN0026";
	
  private String GRAPHIC_CLIENT_0="icons/client_test.jpg";  //$NON-NLS-N$
  private String GRAPHIC_CLIENT_1="icons/client_run.jpg";   //$NON-NLS-N$
  private String GRAPHIC_CLIENT_2="icons/client_install.jpg"; //$NON-NLS-N$
  private String GRAPHIC_CLIENT_3="icons/client_deploy.jpg"; //$NON-NLS-N$
  private String GRAPHIC_CLIENT_4="icons/client_assemble.jpg"; //$NON-NLS-N$
  private String GRAPHIC_CLIENT_5="icons/client_develop.jpg";  //$NON-NLS-N$
  private String GRAPHIC_CLIENT_6="icons/client_none.jpg";  //$NON-NLS-N$
  
  private String ICON_SCALE_BG_0="icons/scale0_bground.jpg";  //$NON-NLS-N$
  private String ICON_SCALE_BG_1="icons/scale1_bground.jpg";  //$NON-NLS-N$
  private String ICON_SCALE_BG_2="icons/scale2_bground.jpg";  //$NON-NLS-N$
  private String ICON_SCALE_BG_3="icons/scale3_bground.jpg";  //$NON-NLS-N$
  private String ICON_SCALE_BG_4="icons/scale4_bground.jpg";  //$NON-NLS-N$
  private String ICON_SCALE_BG_5="icons/scale5_bground.jpg";  //$NON-NLS-N$
  private String ICON_SCALE_BG_6="icons/scale6_bground.jpg";  //$NON-NLS-N$
  
  private Composite clientComposite_;
  private Composite hCompClient_;
  private Shell shell_;
  private Combo  clientTypeCombo_;  
  private Scale clientScale_;  
  private Label topologySpot_;
  private Label clientDetailsLabel_;  
  private Hyperlink hLinkClientRuntime_;
  private Hyperlink hLinkClientServer_;
  private Hyperlink hLinkClientProject_;
  private Hyperlink hLinkClientEAR_;
  private ProjectSelectionDialog projectDialog_;
  
  private boolean developClient_;
  private boolean assembleClient_;
  private boolean deployClient_;
	
  private Boolean testClient_;
  private Boolean installClient_;
  private Boolean startClient_;    
    
  private TypeRuntimeServer ids_;
  private IStructuredSelection objectSelection_;
  private boolean enableProxy_;  //service scale is set to a level that the client scale can be enabled
  private boolean clientOnly_=false;
  private int clientScaleSetting_;
  private ImageRegistry imageReg_;
  private IProject project_;
  private WebServicesParser parser_;
  private String earProjectName_;
  private String projectName_;
  private boolean needEar_;
  private String clientComponentType_;
  
  private Listener statusListener_;
  private int validationState_;
  
  private ScaleSelectionListener scaleSelectionListener = new ScaleSelectionListener();
  
  private String CLIENT_RUNTIME_PREFIX = ConsumptionUIMessages.LABEL_RUNTIMES_LIST ; 
  private String CLIENT_SERVER_PREFIX =  ConsumptionUIMessages.LABEL_SERVERS_LIST;
  private String CLIENT_PROJECT_PREFIX = ConsumptionUIMessages.LABEL_CLIENT_PROJECT;
  private String CLIENT_EAR_PREFIX = ConsumptionUIMessages.LABEL_CLIENT_EAR_PROJECT;
  
  private Composite clientGroupComposite_ ;
	
  private UIUtils utils_ = new UIUtils("org.eclipse.jst.ws.consumption.ui");
  private IPackable packable_ = null;  
  private LabelsAndIds labelIds_;
  
  public WebServiceClientTypeWidget(boolean clientOnly) {
	    clientOnly_ = clientOnly;
		initImageRegistry();
		validationState_ = ValidationUtils.VALIDATE_ALL;
	}
   
    /**
     * Give this widget a handle to an IPackable instance, which will also be
     * packed when this widget's packIt() is called.
     * 
     * @param packable The IPackable instance.
     */
    public void setPackable(IPackable packable) {
    	packable_ = packable;
    }
  
	/* (non-Javadoc)
	 * @see org.eclipse.jst.ws.internal.consumption.ui.widgets.IPackable#packIt()
	 */
	public void packIt() {
		clientGroupComposite_.pack(true);		
		utils_.horizontalResize(clientGroupComposite_.getShell(), clientGroupComposite_, UIUtils.DEFAULT_PADDING);
		if (!clientOnly_ && packable_ != null)
			packable_.packIt();
	}
  
  /* (non-Javadoc)
   * @see org.eclipse.wst.command.env.ui.widgets.WidgetContributor#addControls(org.eclipse.swt.widgets.Composite, org.eclipse.swt.widgets.Listener)
   */
  public WidgetDataEvents addControls( Composite parent, Listener statusListener)
  {
    statusListener_ = statusListener;
    Composite clientTypeComposite = new Composite(parent, SWT.NONE);
	GridLayout cclayout = new GridLayout();
	cclayout.numColumns = 2;
	cclayout.marginTop=3;
	clientTypeComposite.setLayout( cclayout );
    GridData ccGridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL); 
    clientTypeComposite.setLayoutData(ccGridData);
    
    int comboStyle = SWT.SINGLE | SWT.BORDER | SWT.READ_ONLY;
    clientTypeCombo_ = utils_.createCombo( clientTypeComposite, 
    		ConsumptionUIMessages.LABEL_WEBSERVICECLIENTTYPE,
    		ConsumptionUIMessages.TOOLTIP_PWPR_COMBO_CLIENTTYPE, 
    		INFOPOP_WSWSCEN_COMBO_CLIENTTYPE, 
                                          comboStyle );
    GridData comboGridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
    clientTypeCombo_.setLayoutData(comboGridData);
	clientTypeCombo_.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				String oldTypeId = ids_.getTypeId();
				int currentSelectionIdx = clientTypeCombo_.getSelectionIndex();
				String currentTypeId = labelIds_.getIds_()[currentSelectionIdx];
				if (!oldTypeId.equals(currentTypeId)) {		
					// handle changing client type
					ids_.setTypeId(currentTypeId);
					refreshServerRuntimeSelection();
				}
				validationState_ = ValidationUtils.VALIDATE_ALL;
				statusListener_.handleEvent(null);
			}

		});    
    
    clientGroupComposite_ = new Composite(parent, SWT.NONE);    
	GridLayout gclayout = new GridLayout();
	gclayout.numColumns = 2;
	gclayout.horizontalSpacing=0;		
	gclayout.marginHeight=0;		
	gclayout.marginBottom=5;
	clientGroupComposite_.setLayout( gclayout );
    GridData gcGridData = new GridData(SWT.BEGINNING, SWT.BEGINNING, true, true); 
    clientGroupComposite_.setLayoutData(gcGridData);
    
    clientGroupComposite_.addControlListener(new ControlListener()
	{
		public void controlMoved(ControlEvent e) {
			// TODO Auto-generated method stub
			
		}
		public void controlResized(ControlEvent e) {
			clientGroupComposite_.pack(true);
		}
	});
    
       
	clientComposite_ =  new Composite(clientGroupComposite_, SWT.NONE);
	GridLayout gridlayout   = new GridLayout();
    gridlayout.numColumns   = 2;
    gridlayout.horizontalSpacing=0;
    gridlayout.marginHeight=0;
    clientComposite_.setLayout( gridlayout );
    GridData scGridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
    clientComposite_.setLayoutData(scGridData);
    
    clientScale_ = new Scale(clientComposite_ , SWT.VERTICAL | SWT.BORDER);		
	utils_.createInfoPop(clientScale_, INFOPOP_WSWSCEN_SCALE_CLIENT);	
	clientScale_.setMinimum(0);
	clientScale_.setMaximum(6);
	clientScale_.setIncrement(1);
	clientScale_.setPageIncrement(1);
	clientScale_.addSelectionListener(scaleSelectionListener);		
	setClientScale(getClientGeneration());
	clientScale_.setToolTipText(ConsumptionUIMessages.TOOLTIP_WSWSCEN_SCALE_CLIENT);
	
	Accessible accessibleScale = clientScale_.getAccessible();
	accessibleScale.addAccessibleListener(new AccessibleAdapter(){
		// return a string that describes what to do with the slider
		//  this is read by the screen reader when the slider first gets focus
		public void getName(AccessibleEvent e) {
			e.result = ConsumptionUIMessages.TOOLTIP_WSWSCEN_SCALE_CLIENT;			
		}			
	});
	accessibleScale.addAccessibleControlListener(new AccessibleControlAdapter(){
		// return a meaningful string when the screen reader
		// attempts to get the value from the service scale
		public void getValue(AccessibleControlEvent e) {
			int selection = clientScale_.getSelection();
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
				e.result = ConsumptionUIMessages.TOOLTIP_WSWSCEN_SCALE_DEVELOP;
				break;
			case 6:				
				if (clientOnly_)
					e.result = ConsumptionUIMessages.TOOLTIP_WSWSCEN_SCALE_CLIENT_ONLY;
				else
					e.result = ConsumptionUIMessages.TOOLTIP_WSWSCEN_SCALE_CLIENT;				
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
	clientScale_.setLayoutData(layoutData1);
		
	topologySpot_ = new Label(clientComposite_ , SWT.CENTER | SWT.BORDER );
	topologySpot_.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
	if (clientOnly_)
		topologySpot_.setToolTipText(ConsumptionUIMessages.TOOLTIP_WSWSCEN_SCALE_CLIENT_ONLY);
	else
	topologySpot_.setToolTipText(ConsumptionUIMessages.TOOLTIP_WSWSCEN_SCALE_CLIENT);
	
	GridData layoutData = new GridData();
	layoutData.verticalAlignment=SWT.BEGINNING;
	layoutData.horizontalAlignment=SWT.BEGINNING;
	Rectangle topR = (imageReg_.get(GRAPHIC_CLIENT_6)).getBounds();
	layoutData.widthHint=topR.width;
	layoutData.heightHint=topR.height;
	topologySpot_.setLayoutData(layoutData);		
	
	setGraphics(getClientGeneration());
		
	hCompClient_ = utils_.createComposite(clientGroupComposite_, 1);
	
	clientDetailsLabel_ = new Label(hCompClient_, SWT.NONE);
	clientDetailsLabel_.setText(ConsumptionUIMessages.LABEL_SUMMARY);
	
	hLinkClientServer_= new Hyperlink(hCompClient_, SWT.NULL);
	utils_.createInfoPop(hLinkClientServer_, INFOPOP_WSWSCEN_HYPERLINK_SERVER);
	hLinkClientServer_.setToolTipText(ConsumptionUIMessages.TOOLTIP_PWRS_TEXT_SERVER);
	hLinkClientServer_.addHyperlinkListener(new IHyperlinkListener(){
		public void linkActivated(HyperlinkEvent e){			
			launchRuntimeSelectionDialog(true, true);				
		}
		public void linkEntered(HyperlinkEvent e){}
		public void linkExited(HyperlinkEvent e){}			
	});
	
	hLinkClientRuntime_ = new Hyperlink(hCompClient_, SWT.NULL);
	utils_.createInfoPop(hLinkClientRuntime_, INFOPOP_WSWSCEN_HYPERLINK_RUNTIME);
	hLinkClientRuntime_.setToolTipText(ConsumptionUIMessages.TOOLTIP_PWRS_TEXT_RUNTIME);
	hLinkClientRuntime_.addHyperlinkListener(new IHyperlinkListener(){
		public void linkActivated(HyperlinkEvent e){			
			launchRuntimeSelectionDialog(true);
		}
		public void linkEntered(HyperlinkEvent e){}
		public void linkExited(HyperlinkEvent e){}			
	});
	
	IWorkbench workbench = PlatformUI.getWorkbench();
	projectDialog_ = new ProjectSelectionDialog(workbench.getActiveWorkbenchWindow().getShell(), 
			new PageInfo(ConsumptionUIMessages.DIALOG_TITILE_CLIENT_PROJECT_SETTINGS, "", 
                    new WidgetContributorFactory()
						{	
							public WidgetContributor create()
							{	  						 
							   return new ProjectSelectionWidget(true);
							}
						}));
	
	hLinkClientProject_ = new Hyperlink(hCompClient_, SWT.NULL);
	utils_.createInfoPop(hLinkClientRuntime_, INFOPOP_WSWSCEN_HYPERLINK_PROJECTS);
	hLinkClientProject_.setToolTipText(ConsumptionUIMessages.TOOLTIP_WSWSCEN_CLIENTPROJECT_LINK);
	hLinkClientProject_.addHyperlinkListener(new IHyperlinkListener(){
		public void linkActivated(HyperlinkEvent e){			
			launchProjectDialog();
		}
		public void linkEntered(HyperlinkEvent e){}
		public void linkExited(HyperlinkEvent e){}			
	});
	
	hLinkClientEAR_ = new Hyperlink(hCompClient_, SWT.NULL);	
	utils_.createInfoPop(hLinkClientRuntime_, INFOPOP_WSWSCEN_HYPERLINK_PROJECTS);
	hLinkClientEAR_.setToolTipText(ConsumptionUIMessages.TOOLTIP_WSWSCEN_CLIENTPROJECT_LINK);
	hLinkClientEAR_.addHyperlinkListener(new IHyperlinkListener(){
		public void linkActivated(HyperlinkEvent e){			
			launchProjectDialog();
		}
		public void linkEntered(HyperlinkEvent e){}
		public void linkExited(HyperlinkEvent e){}			
	});
	
	hLinkClientServer_.setText(CLIENT_SERVER_PREFIX); 
	hLinkClientRuntime_.setText(CLIENT_RUNTIME_PREFIX);
	hLinkClientProject_.setText(CLIENT_PROJECT_PREFIX);
	hLinkClientEAR_.setText(CLIENT_EAR_PREFIX);	
	
	HyperlinkGroup serverRuntimeGroup = new HyperlinkGroup(Display.getCurrent());
	serverRuntimeGroup.add(hLinkClientServer_);
	serverRuntimeGroup.add(hLinkClientRuntime_);
	serverRuntimeGroup.add(hLinkClientProject_);
	serverRuntimeGroup.add(hLinkClientEAR_);
	serverRuntimeGroup.setHyperlinkUnderlineMode(HyperlinkSettings.UNDERLINE_ALWAYS);
    
	enableClientSlider(getGenerateProxy());
	
    return this;
  }
  
  public void enableClientSlider( boolean enable )
  {
	enableProxy_ = enable;
	clientScale_.setEnabled(enable);
	int selection = getClientGeneration();
	if (enable)
	{				
		setGraphics(selection);
	}
	else
	{
		setClientScale(ScenarioContext.WS_NONE);
		updateFields(ScenarioContext.WS_NONE);
		setGraphics(ScenarioContext.WS_NONE);
		clientScale_.setBackgroundImage(null);  //override background for disable to grey		
	}	
	showSummary(enable && (selection <= ScenarioContext.WS_DEVELOP));
  }

  private void updateFields(int selection) {
		setDevelopClient(selection <= ScenarioContext.WS_DEVELOP);
		setAssembleClient(selection <= ScenarioContext.WS_ASSEMBLE);
		setDeployClient(selection <= ScenarioContext.WS_DEPLOY);
		setTestClient(new Boolean(selection <= ScenarioContext.WS_TEST));
		setInstallClient(new Boolean(selection <= ScenarioContext.WS_INSTALL));
		setStartClient(new Boolean(selection <= ScenarioContext.WS_START));
  }

  private void showSummary(boolean show)
  {
	  if (clientOnly_)
		  show = true;  //short circuit to eliminate flicker...	  
	  
	  hLinkClientEAR_.setVisible(show && needEar_);
	  hLinkClientProject_.setVisible(show);
	  hLinkClientRuntime_.setVisible(show);
	  hLinkClientServer_.setVisible(show);
	  
	  if (show)
	  {
		  clientDetailsLabel_.setText(ConsumptionUIMessages.LABEL_SUMMARY);
	  }
	  else
	  {
		  if (clientOnly_)
			  hCompClient_.setToolTipText(ConsumptionUIMessages.TOOLTIP_WSWSCEN_SCALE_CLIENT_ONLY);
			else
		  hCompClient_.setToolTipText(ConsumptionUIMessages.TOOLTIP_WSWSCEN_SCALE_CLIENT);
		  clientDetailsLabel_.setText(ConsumptionUIMessages.LABEL_SUMMARY_NO_CLIENT);		  
	  }	  
	  clientDetailsLabel_.pack(true);
	  hLinkClientProject_.pack(true);
	  hLinkClientEAR_.pack(true);
	  packIt();
  }

  public void setTypeRuntimeServer( TypeRuntimeServer ids )
  {
	LabelsAndIds                 labelIds   = WebServiceRuntimeExtensionUtils2.getClientTypeLabels();
    int                          selection  = 0;
    String[]                     clientIds  = labelIds.getIds_();
    String                       selectedId = ids.getTypeId();
    
    labelIds_ = labelIds;
	// rskreg
    clientTypeCombo_.setItems( labelIds.getLabels_() );
    
    // Now find the selected one.
    for( int index = 0; index < clientIds.length; index++ )
    {
      if( selectedId.equals( clientIds[index ]) )
      {
        selection = index;
        break;
      }
    }
    
    clientTypeCombo_.select( selection );    
	ids_ = ids;
	
	if (ids_ != null)
	{
		String clientServerText = "";
		String serverId = ids_.getServerId();
		if (serverId != null && serverId.length()>0)
		{
			clientServerText = WebServiceRuntimeExtensionUtils2.getServerLabelById(serverId);
		} 
		String clientRuntimeText = WebServiceRuntimeExtensionUtils2.getRuntimeLabelById(ids_.getRuntimeId());
		
		String currentServerText = hLinkClientServer_.getText();
		String currentRuntimeText = hLinkClientRuntime_.getText();
		String newServerText = CLIENT_SERVER_PREFIX + " " + clientServerText;
		String newRuntimeText = CLIENT_RUNTIME_PREFIX + " " + clientRuntimeText;
		hLinkClientServer_.setText(newServerText);
		hLinkClientRuntime_.setText(newRuntimeText);
		
		/*check to see if text has changed for server or runtime
		if so, repaint links */
		if (!newServerText.equals(currentServerText))
		{
			hLinkClientServer_.pack(true);
			packIt();
		}			
		
		if (!newRuntimeText.equals(currentRuntimeText))
		{
			hLinkClientRuntime_.pack(true);
			packIt();
		} 	
	}
	
    if (projectDialog_ != null)
    	projectDialog_.setTypeRuntimeServer(ids_);
    
	//When the server changes, the state of needEar could change.
	//If the the server change results in a change in the state of needEar,
	//update needEar and clientEarProjectName.    
    
	ValidationUtils vu = new ValidationUtils();
	boolean oldNeedEar = getClientNeedEAR();
	boolean clientProjectOrProjectTypeNeedsEar;

	if (!oldNeedEar) {
			// If an EAR was not needed previously it could have been because of
			// the project/project type or the server.
			// If it was because of the project/project type, changing the
			// server should have no impact
			// on the state of needEar.
			clientProjectOrProjectTypeNeedsEar = vu.projectOrProjectTypeNeedsEar(getClientProjectName(),
					getClientComponentType());
		} else {
			clientProjectOrProjectTypeNeedsEar = true;
		}

		// boolean clientProjectOrProjectTypeNeedsEar =
		// vu.projectOrProjectTypeNeedsEar(getClientProjectName(),
		// getClientComponentType());
		if (clientProjectOrProjectTypeNeedsEar) {
			// Could not rule out need for an Ear from the project/project type
			// so changing the server
			// may impact the need for an Ear.
			boolean currentServerNeedsEar = vu.serverNeedsEAR(getTypeRuntimeServer().getServerId());
			if (oldNeedEar != currentServerNeedsEar) {
				// Update needEar and serviceEarProjectName.
				if (currentServerNeedsEar) {
					// Calculate a reasonable default for the Ear project name
					String earProjectName = DefaultingUtils.getDefaultEARProjectName(getClientProjectName());
					setClientNeedEAR(currentServerNeedsEar);
					setClientEarProjectName(earProjectName);
				} else {
					setClientNeedEAR(currentServerNeedsEar);
					setClientEarProjectName("");
				}

			}
		}		    
  }
  
  public TypeRuntimeServer getTypeRuntimeServer()
  {
    return ids_;  
  }
      
  public boolean getGenerateProxy()
  {
	  return getClientGeneration() <= ScenarioContext.WS_DEVELOP;
  }
  
  public boolean getDevelopClient() {
	  return developClient_;
  }

  public void setDevelopClient(boolean developClient) {
	  this.developClient_ = developClient;
  }	

  public boolean getAssembleClient() {
	  return assembleClient_;
  }

  public void setAssembleClient(boolean assembleClient) {
	  this.assembleClient_ = assembleClient;
  }

  public boolean getDeployClient() {
	  return deployClient_;
  }

  public void setDeployClient(boolean deployClient) {
	  this.deployClient_ = deployClient;
  }
  
  public void setTestClient(Boolean value)
  {
	  testClient_ = value;
  }
  
  public Boolean getTestClient()
  {
	  return testClient_;
  }
  
  public Boolean getInstallClient()
  {
	  return installClient_;
  }
  
  public Boolean getStartClient()
  {
	  return startClient_;
  }
  
  public void setInstallClient( Boolean value )
  {
      installClient_ = value;    
  }
    
  public void setStartClient( Boolean value )
  {
      startClient_ = value;    
  }

  
	private void launchProjectDialog()
	{
		String currentProjectName = getClientProjectName();
		String currentEarProjectName = getClientEarProjectName();
		String currentProjectType = getClientComponentType();
		boolean currentNeedEar = getClientNeedEAR();
		
		projectDialog_.setProjectName(currentProjectName);
		projectDialog_.setEarProjectName(currentEarProjectName);
		projectDialog_.setNeedEAR(currentNeedEar);	
		projectDialog_.setProjectComponentType(currentProjectType);
		
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
				setClientProjectName(newProjectName);
				setClientEarProjectName(newEarProjectName);
				setClientNeedEAR(newNeedEar);
				setClientComponentType(newProjectType);				
				validationState_ = (new ValidationUtils()).getNewValidationState(validationState_, ValidationUtils.VALIDATE_PROJECT_CHANGES);
				statusListener_.handleEvent(null);
			}
			
			/*check to see if text has changed for project or EAR
			if so, repaint links */
			if (!newProjectName.equals(currentProjectName))
			{
				hLinkClientProject_.pack(true);
				packIt();
			}
			refreshEARLink();
		}		
	}
  
  private void launchRuntimeSelectionDialog(boolean clientContext) {
	  launchRuntimeSelectionDialog(clientContext, false);
  }
  
  private void launchRuntimeSelectionDialog(boolean clientContext, boolean selectServerFirst)
	{
	    TypeRuntimeServer currentClientTRS = getTypeRuntimeServer();
	    RuntimeServerSelectionDialog rssd = new RuntimeServerSelectionDialog(shell_, (byte)1, getTypeRuntimeServer(), "14");
	    rssd.setSelectServerFirst(selectServerFirst);
	    int status = rssd.open();
		if (status == Window.OK)
		{
			TypeRuntimeServer newClientTRS = rssd.getTypeRuntimeServer();			
			if (!currentClientTRS.equals(newClientTRS))
			{
				setTypeRuntimeServer(rssd.getTypeRuntimeServer());
				validationState_ = (new ValidationUtils()).getNewValidationState(validationState_, ValidationUtils.VALIDATE_SERVER_RUNTIME_CHANGES);
				statusListener_.handleEvent(null); //validate the page
			}
		}		
	}
  
  //Private method should be used whenever clientScale_ setting is changed
  //instead of calling clientScale_.setSelection directly.
  private void setClientScale(int setting)
  {
	  clientScale_.setSelection(setting);
	  clientScaleSetting_ = setting;
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
		
		imageReg_.put(GRAPHIC_CLIENT_0, WebServiceConsumptionUIPlugin
				.getImageDescriptor(GRAPHIC_CLIENT_0));
		imageReg_.put(GRAPHIC_CLIENT_1, WebServiceConsumptionUIPlugin
				.getImageDescriptor(GRAPHIC_CLIENT_1));
		imageReg_.put(GRAPHIC_CLIENT_2, WebServiceConsumptionUIPlugin
				.getImageDescriptor(GRAPHIC_CLIENT_2));
		imageReg_.put(GRAPHIC_CLIENT_3, WebServiceConsumptionUIPlugin
				.getImageDescriptor(GRAPHIC_CLIENT_3));
		imageReg_.put(GRAPHIC_CLIENT_4, WebServiceConsumptionUIPlugin
				.getImageDescriptor(GRAPHIC_CLIENT_4));
		imageReg_.put(GRAPHIC_CLIENT_5, WebServiceConsumptionUIPlugin
				.getImageDescriptor(GRAPHIC_CLIENT_5));		
		imageReg_.put(GRAPHIC_CLIENT_6, WebServiceConsumptionUIPlugin
				.getImageDescriptor(GRAPHIC_CLIENT_6));
	}
  
  private void setGraphics(int selection)
  {
     String iconImage = "";
     String topologyImage = "";
     
	  switch (selection) {
	    case ScenarioContext.WS_TEST:
			iconImage=ICON_SCALE_BG_0;
			topologyImage=GRAPHIC_CLIENT_0;
			clientScale_.setToolTipText(ConsumptionUIMessages.TOOLTIP_WSWSCEN_SCALE_TEST);
			break;
	    case ScenarioContext.WS_START:
			iconImage=ICON_SCALE_BG_1;
			topologyImage=GRAPHIC_CLIENT_1;
			clientScale_.setToolTipText(ConsumptionUIMessages.TOOLTIP_WSWSCEN_SCALE_RUN);
			break;
		case ScenarioContext.WS_INSTALL:
			iconImage=ICON_SCALE_BG_2;
			topologyImage=GRAPHIC_CLIENT_2;
			clientScale_.setToolTipText(ConsumptionUIMessages.TOOLTIP_WSWSCEN_SCALE_INSTALL);
			break;
		case ScenarioContext.WS_DEPLOY:
			iconImage=ICON_SCALE_BG_3;
			topologyImage=GRAPHIC_CLIENT_3;
			clientScale_.setToolTipText(ConsumptionUIMessages.TOOLTIP_WSWSCEN_SCALE_DEPLOY);
			break;
		case ScenarioContext.WS_ASSEMBLE:
			iconImage=ICON_SCALE_BG_4;
			topologyImage=GRAPHIC_CLIENT_4;
			clientScale_.setToolTipText(ConsumptionUIMessages.TOOLTIP_WSWSCEN_SCALE_ASSEMBLE);
			break;
		case ScenarioContext.WS_DEVELOP:
			iconImage=ICON_SCALE_BG_5;
			topologyImage=GRAPHIC_CLIENT_5;
			clientScale_.setToolTipText(ConsumptionUIMessages.TOOLTIP_WSWSCEN_SCALE_DEVELOP);
			break;
		case ScenarioContext.WS_NONE:			
			if (!clientOnly_)
			{
				if (enableProxy_)  //if service is start or test...
					iconImage=ICON_SCALE_BG_6;  
				else
					iconImage=null;
				topologyImage=GRAPHIC_CLIENT_6;
				clientScale_.setToolTipText(ConsumptionUIMessages.TOOLTIP_WSWSCEN_SCALE_CLIENT);
			}
			else
			{
				setClientScale(ScenarioContext.WS_DEVELOP); //"no selection" is not allowed...must develop client @ minimum
				iconImage=ICON_SCALE_BG_5;
				topologyImage=GRAPHIC_CLIENT_5;				
				clientScale_.setToolTipText(ConsumptionUIMessages.TOOLTIP_WSWSCEN_SCALE_DEVELOP);			
			}
			break;
		default:
			break;
		}
		clientScale_.setBackgroundImage(imageReg_.get(iconImage));
		topologySpot_.setImage(imageReg_.get(topologyImage));	
  }
  
	public IStructuredSelection getObjectSelection()
	{
		return objectSelection_;		
	}
	
	public void setObjectSelection(IStructuredSelection selection)
	{
        objectSelection_ = selection;
	}
	
  public int getClientGeneration()
  {
	  return clientScaleSetting_;
  }
  
  public void setClientGeneration(int value)
  {
	  if (clientOnly_ && value == ScenarioContext.WS_NONE) {
		  value = ScenarioContext.WS_DEVELOP;
		  setDevelopClient(true);
	  }

	  setClientScale(value);
	  	  
	  setGraphics(value);
	  showSummary(value < ScenarioContext.WS_NONE);
	  updateFields(value);
  }
  
  public void setProject(IProject project)
  {
	  project_ = project;
  }
  
  public IProject getProject()
  {
	  return project_;
  }
  
  public String getClientRuntimeId()
  {
	  // calculate the most appropriate clientRuntimeId based on current settings.
	  return WebServiceRuntimeExtensionUtils2.getClientRuntimeId(getTypeRuntimeServer(), getClientProjectName(), getClientComponentType());   

  }
  
  public void setClientComponentType(String type)
  {
	  clientComponentType_= type;
  }

  public String getClientComponentType()
  {
	  return clientComponentType_;
  }

  public void setClientProjectName(String name)
  {
    projectName_ = name;      
	hLinkClientProject_.setText(CLIENT_PROJECT_PREFIX + " " + projectName_);	
	hLinkClientProject_.pack(true);   
	packIt();
  }
  
  public String getClientProjectName()
  {  
	  if (projectName_ == null)
		  return "";
	  return projectName_;
  }
  
  public void setClientEarProjectName(String name)
  {
    earProjectName_ = name;  
    refreshEARLink();
   		   
  }
  
  
 public boolean isOSGI() 
 {
	 
	 if(DefaultingUtils.isOSGIProject(projectName_)) 
	 {
		 return true;
	 }
	 
	 if(clientComponentType_ != null && DefaultingUtils.isOSGITemplate(clientComponentType_)) 
	 {
		 return true;
	 }
	 
	 return false;
 }

  
  public void refreshEARLink()
  {
	  hLinkClientEAR_.setVisible(needEar_ && getGenerateProxy());
	  if (needEar_)
	  {
		  if(isOSGI()) {
			  hLinkClientEAR_.setText(ConsumptionUIMessages.LABEL_CLIENT_OSGI_PROJECT + " " + earProjectName_);
			  hLinkClientEAR_.setToolTipText(ConsumptionUIMessages.TOOLTIP_WSWSCEN_CLIENTPROJECT_LINK_OSGI);
			  
			  hLinkClientProject_.setToolTipText(ConsumptionUIMessages.TOOLTIP_WSWSCEN_CLIENTPROJECT_LINK_OSGI);
			  
		  } else {
			  hLinkClientEAR_.setText(CLIENT_EAR_PREFIX + " " + earProjectName_);
			  hLinkClientEAR_.setToolTipText(ConsumptionUIMessages.TOOLTIP_WSWSCEN_CLIENTPROJECT_LINK);
			  hLinkClientProject_.setToolTipText(ConsumptionUIMessages.TOOLTIP_WSWSCEN_CLIENTPROJECT_LINK);
		  }
		  
		  hLinkClientEAR_.pack(true); 
		  packIt();
	  }  
  }
	    
  public String getClientEarProjectName()
  {
	  if (earProjectName_ == null)
		  return "";
     return earProjectName_;  
  }
  
  public void setClientNeedEAR(boolean b)
  {
     needEar_ = b;
     refreshEARLink();
  } 
  
  public boolean getClientNeedEAR()
  {
     return needEar_;
  } 
  
  public WebServicesParser getWebServicesParser()
	{
		return parser_;
	}
	
	public void setWebServicesParser(WebServicesParser parser)
	{
		parser_ = parser;		
	}
  
    //for the purposes of disabling the service implementation controls from the preferences dialog
	public void disableNonPreferenceWidgets()
	{
		if (hCompClient_ != null)
		{
			hCompClient_.setVisible(false);			
		}
	}
	
	public int getValidationState()
	{
		return validationState_;
	}
	
	public void setValidationState(int state)
	{
		validationState_ = state;
	}
		
	public IStatus checkMissingFieldStatus() {

		ValidationUtils valUtils = new ValidationUtils();
		String runtimeId = getTypeRuntimeServer().getRuntimeId();
		String serverId = getTypeRuntimeServer().getServerId();
		String projectName = getClientProjectName();
		boolean needEar = getClientNeedEAR();
		String earProjectName = getClientEarProjectName();
		String projectTypeId = getClientComponentType();
		IStatus missingFieldStatus = valUtils.checkMissingFieldStatus(validationState_, null, null, runtimeId, serverId,
				projectName, needEar, earProjectName, projectTypeId, true);
		return missingFieldStatus;
	}

	public IStatus checkErrorStatus() {

		ValidationUtils valUtils = new ValidationUtils();
		String projectName = getClientProjectName();
		boolean needEar = getClientNeedEAR();
		String earProjectName = getClientEarProjectName();
		String projectTypeId = getClientComponentType();
		// Defect 215084 - no need to pass initial project for client scenario
		IStatus possibleErrorStatus = valUtils.checkErrorStatus(validationState_, getTypeRuntimeServer(), projectName,"", needEar, earProjectName, projectTypeId, true);
			return possibleErrorStatus;
	}

	public IStatus checkWarningStatus() {
		ValidationUtils valUtils = new ValidationUtils();

		int scaleSetting = getClientGeneration();
		String serverId = getTypeRuntimeServer().getServerId();
		IStatus warningStatus = valUtils.checkWarningStatus(validationState_, scaleSetting, serverId, getTypeRuntimeServer().getServerInstanceId(), true);
		return warningStatus;
	}	
	
	
  private class ScaleSelectionListener implements SelectionListener
	{
		public void widgetSelected(SelectionEvent e) {
			    int oldClientScaleSetting = clientScaleSetting_;
			    				
				int selection = clientScale_.getSelection();
				
				//WARNING: No scale change should be done after the line below, 'selection' might not have the correct value
				setClientGeneration(selection);
				
				//When client slider moves out of the "WS_NONE" state, VALIDATE_ALL should be done. 
				//Otherwise, VALIDATE_SCALE_CHANGES is sufficient.
				if (oldClientScaleSetting==ScenarioContext.WS_NONE && clientScaleSetting_!=ScenarioContext.WS_NONE)
				{
					validationState_ = ValidationUtils.VALIDATE_ALL;
				}
				else
				{
					validationState_ = (new ValidationUtils()).getNewValidationState(validationState_, ValidationUtils.VALIDATE_SCALE_CHANGES);	
				}				
				statusListener_.handleEvent(null); //validate the page
			}
		

		public void widgetDefaultSelected(SelectionEvent e) {
			widgetSelected(e);
		}
	} 
  
  private void refreshServerRuntimeSelection()
	{		
		//new up ServerRuntimeSelectionWidgetDefaultingCommand
		ClientRuntimeSelectionWidgetDefaultingCommand clientRTDefaultCmd = new ClientRuntimeSelectionWidgetDefaultingCommand();
		
		  //call setters of new defaulting command:
		  clientRTDefaultCmd.setClientInitialSelection(getObjectSelection());
		  clientRTDefaultCmd.setClientInitialProject(getProject());	      
	      clientRTDefaultCmd.setClientTypeRuntimeServer(getTypeRuntimeServer());
		  clientRTDefaultCmd.setWebServicesParser(getWebServicesParser());     
	      clientRTDefaultCmd.setClientEarProjectName(getClientEarProjectName());
		  		  
		  clientRTDefaultCmd.execute(null, null);
		  
		  //perform mappings from the defaulting command to the project settings...	
		  setClientProjectName(clientRTDefaultCmd.getClientProjectName());
		  setClientEarProjectName(clientRTDefaultCmd.getClientEarProjectName());
		  setClientComponentType(clientRTDefaultCmd.getClientComponentType());
		  setTypeRuntimeServer(clientRTDefaultCmd.getClientTypeRuntimeServer());
          setClientNeedEAR(clientRTDefaultCmd.getClientNeedEAR());
	}
}

