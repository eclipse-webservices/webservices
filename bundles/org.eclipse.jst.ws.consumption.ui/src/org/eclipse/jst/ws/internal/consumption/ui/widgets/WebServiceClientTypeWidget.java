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
 * 20060407   135443 joan@ca.ibm.com - Joan Haggarty
 * 20060410   135441 joan@ca.ibm.com - Joan Haggarty
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.ui.widgets;


import org.eclipse.core.resources.IProject;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.jst.ws.internal.consumption.ui.ConsumptionUIMessages;
import org.eclipse.jst.ws.internal.consumption.ui.plugin.WebServiceConsumptionUIPlugin;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.runtime.ProjectSelectionWidget;
import org.eclipse.jst.ws.internal.consumption.ui.wizard.RuntimeServerSelectionDialog;
import org.eclipse.jst.ws.internal.consumption.ui.wsrt.WebServiceRuntimeExtensionUtils2;
import org.eclipse.jst.ws.internal.context.ScenarioContext;
import org.eclipse.jst.ws.internal.data.LabelsAndIds;
import org.eclipse.jst.ws.internal.data.TypeRuntimeServer;
import org.eclipse.jst.ws.internal.ui.common.UIUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
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
import org.eclipse.ui.forms.HyperlinkGroup;
import org.eclipse.ui.forms.HyperlinkSettings;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.events.IHyperlinkListener;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.internal.Workbench;
import org.eclipse.wst.command.internal.env.ui.widgets.PageInfo;
import org.eclipse.wst.command.internal.env.ui.widgets.SimpleWidgetDataContributor;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetContributor;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetContributorFactory;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetDataEvents;
import org.eclipse.wst.ws.internal.parser.wsil.WebServicesParser;


public class WebServiceClientTypeWidget extends SimpleWidgetDataContributor
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
  
  private Composite groupComposite_;
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
  
  private Boolean testClient_;
  private Boolean installClient_;
  private Boolean startClient_;    
  private String clientRuntimeId_;
  
  private TypeRuntimeServer ids_;
  private IStructuredSelection objectSelection_;
  private LabelsAndIds      labelIds_;
  private boolean enableProxy_;  //service scale is set to a level that the client scale can be enabled
  private boolean clientOnly_=false;
  private ImageRegistry imageReg_;
  private IProject project_;
  private WebServicesParser parser_;
  private String earProjectName_;
  private String projectName_;
  private boolean needEar_;
  private String clientComponentType_;
  
  private ScaleSelectionListener scaleSelectionListener = new ScaleSelectionListener();
  
	private String CLIENT_RUNTIME_PREFIX = ConsumptionUIMessages.LABEL_RUNTIMES_LIST ; 
	private String CLIENT_SERVER_PREFIX =  ConsumptionUIMessages.LABEL_SERVERS_LIST;
	private String CLIENT_PROJECT_PREFIX = ConsumptionUIMessages.LABEL_CLIENT_PROJECT;
	private String CLIENT_EAR_PREFIX = ConsumptionUIMessages.LABEL_CLIENT_EAR_PROJECT;
    
  public WebServiceClientTypeWidget() {
		initImageRegistry();
	}
  
  /* (non-Javadoc)
   * @see org.eclipse.wst.command.env.ui.widgets.WidgetContributor#addControls(org.eclipse.swt.widgets.Composite, org.eclipse.swt.widgets.Listener)
   */
  public WidgetDataEvents addControls( Composite parent, Listener statusListener)
  {
    String       pluginId = "org.eclipse.jst.ws.consumption.ui";
    UIUtils      utils    = new UIUtils( pluginId );   
	
    Composite clientComposite = new Composite(parent, SWT.NONE);
	GridLayout cclayout = new GridLayout();
	cclayout.numColumns = 2;
	cclayout.marginTop=3;
	clientComposite.setLayout( cclayout );
    GridData ccGridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL); 
    clientComposite.setLayoutData(ccGridData);
    
    int comboStyle = SWT.SINGLE | SWT.BORDER | SWT.READ_ONLY;
    clientTypeCombo_ = utils.createCombo( clientComposite, 
    		ConsumptionUIMessages.LABEL_WEBSERVICECLIENTTYPE,
    		ConsumptionUIMessages.TOOLTIP_PWPR_COMBO_CLIENTTYPE, 
    		INFOPOP_WSWSCEN_COMBO_CLIENTTYPE, 
                                          comboStyle );
    GridData comboGridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
    clientTypeCombo_.setLayoutData(comboGridData);
    
    groupComposite_ = new Composite(parent, SWT.NONE);
	GridLayout gclayout = new GridLayout();
	gclayout.numColumns = 2;
	gclayout.horizontalSpacing=0;		
	gclayout.marginHeight=0;		
	gclayout.marginBottom=5;
	groupComposite_.setLayout( gclayout );
    GridData gcGridData = new GridData(SWT.BEGINNING, SWT.BEGINNING, true, true); 
    groupComposite_.setLayoutData(gcGridData);
       
	Composite clientScaleComposite_ =  new Composite(groupComposite_, SWT.NONE);
	GridLayout gridlayout   = new GridLayout();
    gridlayout.numColumns   = 2;
    gridlayout.horizontalSpacing=0;
    gridlayout.marginHeight=0;
    clientScaleComposite_.setLayout( gridlayout );
    GridData scGridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
    clientScaleComposite_.setLayoutData(scGridData);    
    
    clientScale_ = new Scale(clientScaleComposite_ , SWT.VERTICAL | SWT.BORDER | SWT.CENTER);		
	utils.createInfoPop(clientScale_, INFOPOP_WSWSCEN_SCALE_CLIENT);	
	clientScale_.setMinimum(0);
	clientScale_.setMaximum(6);
	clientScale_.setIncrement(1);
	clientScale_.addSelectionListener(scaleSelectionListener);		
	clientScale_.setSelection(getClientGeneration());
	clientScale_.setToolTipText(ConsumptionUIMessages.TOOLTIP_WSWSCEN_SCALE_CLIENT);
	
	GridData layoutData1 = new GridData();
	layoutData1.horizontalAlignment=SWT.BEGINNING;
	layoutData1.verticalAlignment = SWT.BEGINNING;
	Rectangle scaleR = (imageReg_.get(ICON_SCALE_BG_0)).getBounds();	
	layoutData1.heightHint=scaleR.height;
	layoutData1.widthHint=scaleR.width;
	clientScale_.setLayoutData(layoutData1);
		
	topologySpot_ = new Label(clientScaleComposite_ , SWT.CENTER | SWT.BORDER );
	topologySpot_.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
	topologySpot_.setToolTipText(ConsumptionUIMessages.TOOLTIP_WSWSCEN_SCALE_CLIENT);
	
	GridData layoutData = new GridData();
	layoutData.verticalAlignment=SWT.BEGINNING;
	layoutData.horizontalAlignment=SWT.BEGINNING;
	Rectangle topR = (imageReg_.get(GRAPHIC_CLIENT_6)).getBounds();
	layoutData.widthHint=topR.width;
	layoutData.heightHint=topR.height;
	topologySpot_.setLayoutData(layoutData);		
	
	setGraphics(getClientGeneration());
		
	hCompClient_ = utils.createComposite(groupComposite_, 1);
	
	clientDetailsLabel_ = new Label(hCompClient_, SWT.NONE);
	clientDetailsLabel_.setText(ConsumptionUIMessages.LABEL_SUMMARY);
	
	hLinkClientServer_= new Hyperlink(hCompClient_, SWT.NULL);
	utils.createInfoPop(hLinkClientServer_, INFOPOP_WSWSCEN_HYPERLINK_SERVER);
	hLinkClientServer_.setToolTipText(ConsumptionUIMessages.TOOLTIP_PWRS_TEXT_SERVER);
	hLinkClientServer_.addHyperlinkListener(new IHyperlinkListener(){
		public void linkActivated(HyperlinkEvent e){			
			launchRuntimeSelectionDialog(true);				
		}
		public void linkEntered(HyperlinkEvent e){}
		public void linkExited(HyperlinkEvent e){}			
	});
	
	hLinkClientRuntime_ = new Hyperlink(hCompClient_, SWT.NULL);
	utils.createInfoPop(hLinkClientRuntime_, INFOPOP_WSWSCEN_HYPERLINK_RUNTIME);
	hLinkClientRuntime_.setToolTipText(ConsumptionUIMessages.TOOLTIP_PWRS_TEXT_RUNTIME);
	hLinkClientRuntime_.addHyperlinkListener(new IHyperlinkListener(){
		public void linkActivated(HyperlinkEvent e){			
			launchRuntimeSelectionDialog(true);
		}
		public void linkEntered(HyperlinkEvent e){}
		public void linkExited(HyperlinkEvent e){}			
	});
	
	projectDialog_ = new ProjectSelectionDialog(Workbench.getInstance().getActiveWorkbenchWindow().getShell(), 
			new PageInfo(ConsumptionUIMessages.DIALOG_TITILE_CLIENT_PROJECT_SETTINGS, "", 
                    new WidgetContributorFactory()
						{	
							public WidgetContributor create()
							{	  						 
							   return new ProjectSelectionWidget(true);
							}
						}));
	
	hLinkClientProject_ = new Hyperlink(hCompClient_, SWT.NULL);
	utils.createInfoPop(hLinkClientRuntime_, INFOPOP_WSWSCEN_HYPERLINK_PROJECTS);
	hLinkClientProject_.setToolTipText(ConsumptionUIMessages.TOOLTIP_WSWSCEN_CLIENTPROJECT_LINK);
	hLinkClientProject_.addHyperlinkListener(new IHyperlinkListener(){
		public void linkActivated(HyperlinkEvent e){			
			launchProjectDialog();
		}
		public void linkEntered(HyperlinkEvent e){}
		public void linkExited(HyperlinkEvent e){}			
	});
	
	hLinkClientEAR_ = new Hyperlink(hCompClient_, SWT.NULL);	
	utils.createInfoPop(hLinkClientRuntime_, INFOPOP_WSWSCEN_HYPERLINK_PROJECTS);
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
		clientScale_.setSelection(ScenarioContext.WS_NONE);
		setGraphics(ScenarioContext.WS_NONE);
		clientScale_.setBackgroundImage(null);  //override background for disable to grey		
	}	
	showSummary(enable && (selection <= ScenarioContext.WS_DEVELOP));
  }
  
  public void setClientOnly(boolean b)
	{
		clientOnly_ = b;
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
		  hCompClient_.setToolTipText(ConsumptionUIMessages.TOOLTIP_WSWSCEN_SCALE_CLIENT);
		  clientDetailsLabel_.setText(ConsumptionUIMessages.LABEL_SUMMARY_NO_CLIENT);		  
	  }	  
	  hLinkClientProject_.pack(true);
	  hLinkClientEAR_.pack(true);
  }

  public void setTypeRuntimeServer( TypeRuntimeServer ids )
  {
		// rskreg
    //WebServiceClientTypeRegistry registry   = WebServiceClientTypeRegistry.getInstance();
    //LabelsAndIds                 labelIds   = registry.getClientTypeLabels();
	LabelsAndIds                 labelIds   = WebServiceRuntimeExtensionUtils2.getClientTypeLabels();
    int                          selection  = 0;
    String[]                     clientIds  = labelIds.getIds_();
    String                       selectedId = ids.getTypeId();
    
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
		String clientServerText = WebServiceRuntimeExtensionUtils2.getServerLabelById(ids_.getServerId());
		String clientRuntimeText = WebServiceRuntimeExtensionUtils2.getRuntimeLabelById(ids_.getRuntimeId());		
		hLinkClientServer_.setText(CLIENT_SERVER_PREFIX + " " + clientServerText);
		hLinkClientRuntime_.setText(CLIENT_RUNTIME_PREFIX + " " + clientRuntimeText);
		groupComposite_.pack(true);		
	}
	
    labelIds_ = labelIds;
    
    if (projectDialog_ != null)
    	projectDialog_.setTypeRuntimeServer(ids_);
  }
  
  public TypeRuntimeServer getTypeRuntimeServer()
  {
    int selectionIndex = clientTypeCombo_.getSelectionIndex();
    
    ids_.setTypeId( labelIds_.getIds_()[selectionIndex] );
    
    return ids_;  
  }
      
  public boolean getGenerateProxy()
  {
	  return getClientGeneration() <= ScenarioContext.WS_DEVELOP;
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
		projectDialog_.setProjectName(getClientProjectName());
		projectDialog_.setEarProjectName(getClientEarProjectName());
		projectDialog_.setNeedEAR(getClientNeedEAR());	
		projectDialog_.setProjectComponentType(getClientComponentType());
		
		int status = projectDialog_.open();  //jvh validation on settings??
		
		if (status == Window.OK)
		{
			setClientProjectName(projectDialog_.getProjectName());
			setClientEarProjectName(projectDialog_.getEarProjectName());
			setClientNeedEAR(projectDialog_.getNeedEAR());
			setClientComponentType(projectDialog_.getProjectComponentType());
		}		
	}
  
  private void launchRuntimeSelectionDialog(boolean clientContext)
	{
	    RuntimeServerSelectionDialog rssd = new RuntimeServerSelectionDialog(shell_, (byte)1, getTypeRuntimeServer(), "14");
	    int status = rssd.open();
		if (status == Window.OK)
		{
			setTypeRuntimeServer(rssd.getTypeRuntimeServer());			
		}		
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
		case 0:
			iconImage=ICON_SCALE_BG_0;
			topologyImage=GRAPHIC_CLIENT_0;
			clientScale_.setToolTipText(ConsumptionUIMessages.TOOLTIP_WSWSCEN_SCALE_TEST);
			break;
		case 1:
			iconImage=ICON_SCALE_BG_1;
			topologyImage=GRAPHIC_CLIENT_1;
			clientScale_.setToolTipText(ConsumptionUIMessages.TOOLTIP_WSWSCEN_SCALE_RUN);
			break;
		case 2:
			iconImage=ICON_SCALE_BG_2;
			topologyImage=GRAPHIC_CLIENT_2;
			clientScale_.setToolTipText(ConsumptionUIMessages.TOOLTIP_WSWSCEN_SCALE_INSTALL);
			break;
		case 3:
			iconImage=ICON_SCALE_BG_3;
			topologyImage=GRAPHIC_CLIENT_3;
			clientScale_.setToolTipText(ConsumptionUIMessages.TOOLTIP_WSWSCEN_SCALE_DEPLOY);
			break;
		case 4:
			iconImage=ICON_SCALE_BG_4;
			topologyImage=GRAPHIC_CLIENT_4;
			clientScale_.setToolTipText(ConsumptionUIMessages.TOOLTIP_WSWSCEN_SCALE_ASSEMBLE);
			break;
		case 5:
			iconImage=ICON_SCALE_BG_5;
			topologyImage=GRAPHIC_CLIENT_5;
			clientScale_.setToolTipText(ConsumptionUIMessages.TOOLTIP_WSWSCEN_SCALE_DEVELOP);
			break;
		case 6:			
			if (!clientOnly_)
			{
				if (enableProxy_)  //if service is install run or test...
					iconImage=ICON_SCALE_BG_6;  
				else
					iconImage=null;
				topologyImage=GRAPHIC_CLIENT_6;
				clientScale_.setToolTipText(ConsumptionUIMessages.TOOLTIP_WSWSCEN_SCALE_CLIENT);
			}
			else
			{
				clientScale_.setSelection(5); //"no selection" is not allowed...must develop service @ minimum
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
	  return clientScale_.getSelection();
  }
  
  public void setClientGeneration(int value)
  {
	  if (clientOnly_ && value == ScenarioContext.WS_NONE)
		  value = ScenarioContext.WS_DEVELOP;
	  
	  clientScale_.setSelection(value);
	  
	  setTestClient(new Boolean(value <= ScenarioContext.WS_TEST));
	  setInstallClient(new Boolean(value <= ScenarioContext.WS_INSTALL));
	  setStartClient(new Boolean(value <= ScenarioContext.WS_START));
	  
	  setGraphics(value);
	  showSummary(value < ScenarioContext.WS_NONE);
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
	  return clientRuntimeId_;
  }
  
  public void setClientComponentType(String type)
  {
	  clientComponentType_= type;
  }

  public String getClientComponentType()
  {
	  return clientComponentType_;
  }

  
  public void setClientRuntimeId(String id)
  {
	  clientRuntimeId_ = id;
  }
  
  public void setClientProjectName(String name)
  {
    projectName_ = name;      
	hLinkClientProject_.setText(CLIENT_PROJECT_PREFIX + " " + projectName_);	
	hLinkClientEAR_.pack(true);     
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
    
    hLinkClientEAR_.setVisible(needEar_);
    
    if (needEar_)
    {    	
		hLinkClientEAR_.setText(CLIENT_EAR_PREFIX + " " + earProjectName_);
		hLinkClientEAR_.pack(true);  
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
	
	
  private class ScaleSelectionListener implements SelectionListener
	{
		public void widgetSelected(SelectionEvent e) {
			    int selection = getClientGeneration();
			    
			    setTestClient(new Boolean(selection <= ScenarioContext.WS_TEST));
				setInstallClient(new Boolean(selection <= ScenarioContext.WS_INSTALL));
				setStartClient(new Boolean(selection <= ScenarioContext.WS_START));
				
				setGraphics(selection);
				//disable the client settings if the client scenario setting isn't at least "DEVELOP"
				boolean generate = selection<=ScenarioContext.WS_DEVELOP;
				showSummary(generate);							
			}
		

		public void widgetDefaultSelected(SelectionEvent e) {
			widgetSelected(e);
		}
	}  
}

