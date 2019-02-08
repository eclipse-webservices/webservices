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
 * 20060407   135415 rsinha@ca.ibm.com - Rupam Kuehner
 * 20060417   136390/136159 joan@ca.ibm.com - Joan Haggarty
 * 20060413   135581 rsinha@ca.ibm.com - Rupam Kuehner
 * 20060420   135912 joan@ca.ibm.com - Joan Haggarty
 * 20060424   138052 kathy@ca.ibm.com - Kathy Chan
 * 20060425   137831 rsinha@ca.ibm.com - Rupam Kuehner
 * 20060509   119296 pmoogk@ca.ibm.com - Peter Moogk
 * 20060529   141422 kathy@ca.ibm.com - Kathy Chan
 * 20060612   145081 pmoogk@ca.ibm.com - Peter Moogk
 * 20060725   149351 makandre@ca.ibm.com - Andrew Mak, Deleted service definition keeps reappearing
 * 20060803   152486 makandre@ca.ibm.com - Andrew Mak, Typing WSDL in Service definition field is very slow
 * 20060817   140017 makandre@ca.ibm.com - Andrew Mak, longer project or server/runtime strings do not resize wizard
 * 20060825   135570 makandre@ca.ibm.com - Andrew Mak, Service implementation URL not displayed properly on first page
 * 20060829   155441 makandre@ca.ibm.com - Andrew Mak, web service wizard hangs during resize
 * 20060907   156211 makandre@ca.ibm.com - Andrew Mak, Selecting service definition invalidated project config when creating web service java client
 * 20061212   159911 makandre@ca.ibm.com - Andrew Mak, changing service definition resets some configuration fields
 * 20060125   159911 kathy@ca.ibm.com - Kathy Chan, Remove unused method and imports
 * 20080613   236523 makandre@ca.ibm.com - Andrew Mak, Overwrite setting on Web service wizard is coupled with preference
 * 20090926   248448 mahutch@ca.ibm.com - Mark Hutchinson, Should not resize WS Wizard for long WSDL file names
 * 20090302   242462 ericdp@ca.ibm.com - Eric D. Peters, Save Web services wizard settings
 * 20090313   268567 ericdp@ca.ibm.com - Eric D. Peters, persisted wizard settings gone unless launching on object
 * 20090326   269097 kchong@ca.ibm.com - Keith Chong, [Accessibility] Web services wizard dialog should be selected after canceling the Browse for class dialog
 * 20150311   461526 jgwest@ca.ibm.com - Jonathan West,  Allow OSGi bundles to be selected in the Wizard
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.ui.widgets;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jst.ws.internal.consumption.common.WSDLParserFactory;
import org.eclipse.jst.ws.internal.consumption.ui.ConsumptionUIMessages;
import org.eclipse.jst.ws.internal.consumption.ui.command.data.EclipseIPath2URLStringTransformer;
import org.eclipse.jst.ws.internal.consumption.ui.common.ValidationUtils;
import org.eclipse.jst.ws.internal.consumption.ui.plugin.WebServiceConsumptionUIPlugin;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.object.Timer;
import org.eclipse.jst.ws.internal.data.TypeRuntimeServer;
import org.eclipse.jst.ws.internal.plugin.WebServicePlugin;
import org.eclipse.jst.ws.internal.ui.common.ComboWithHistory;
import org.eclipse.jst.ws.internal.ui.common.UIUtils;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.command.internal.env.core.context.ResourceContext;
import org.eclipse.wst.command.internal.env.ui.widgets.PageInfo;
import org.eclipse.wst.command.internal.env.ui.widgets.SimpleWidgetDataContributor;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetContributor;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetContributorFactory;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetDataEvents;
import org.eclipse.wst.ws.internal.parser.wsil.WebServicesParser;


public class ClientWizardWidget extends SimpleWidgetDataContributor implements Runnable
{  
  private WebServiceClientTypeWidget clientWidget_;
  private Button overwriteButton_;
  private Button monitorService_;

  private ComboWithHistory serviceImpl_;
  private Button browseButton_;
  private WSDLSelectionDialog wsdlDialog_;
  private String componentName_;
  private IProject project_;
  private String webServiceURI_;
  private WebServicesParser parser_;
  private ResourceContext resourceContext_;
  
  private Listener statusListener_;
  private ModifyListener objectModifyListener_ ;
  private int validationState_;
  private boolean validObjectSelection_ = true;
  private WSDLSelectionWidgetWrapper wsdlValidatorWidget_;

  private Timer timer_ = null;
  
  /* CONTEXT_ID WSWSCEN0020 for the Service Implemenation text field of the Scenario Page */
	 private String INFOPOP_WSWSCEN_TEXT_SERVICE_IMPL = "WSWSCEN0020";

  
  /* CONTEXT_ID WSWSCEN0014 for the monitor service checkbox of the Scenario page */
  private String INFOPOP_WSWSCEN_CHECKBOX_MONITOR_SERVICE = "WSWSCEN0014";	
  /* CONTEXT_ID WSWSCEN0030 for the Overwrite Files checkbox of the Scenario Page */
  private String INFOPOP_WSWSCEN_CHECKBOX_OVERWRITE = "WSWSCEN0030";	
  /* CONTEXT_ID WSWSCEN0001 for the Scenario Page */
  private String INFOPOP_WSWSCEN_PAGE = "WSWSCEN0001";
  
  /**
   * Run this ClientWizardWidget, which validates the entry field values. 
   */
  public void run() {
	  validationState_ = ValidationUtils.VALIDATE_ALL;
	  statusListener_.handleEvent(null);
	  if (validObjectSelection_)
		  callObjectTransformation(project_, 
				  wsdlValidatorWidget_.getComponentName(), 
				  wsdlValidatorWidget_.getWsdlURI());	  
  }
  
  public WidgetDataEvents addControls( Composite parent, Listener statusListener)
  {
    String       pluginId = "org.eclipse.jst.ws.consumption.ui";
    UIUtils      utils    = new UIUtils( pluginId );
    utils.createInfoPop(parent, INFOPOP_WSWSCEN_PAGE);

    statusListener_ = statusListener;
	validationState_ = ValidationUtils.VALIDATE_ALL;
  	// Create text field and browse for service selection
  	Composite typeComposite = utils.createComposite(parent, 3);
	serviceImpl_ = utils.createComboWithHistory(typeComposite, ConsumptionUIMessages.LABEL_WEBSERVICEDEF, 
			ConsumptionUIMessages.TOOLTIP_WSWSCEN_TEXT_IMPL,
			INFOPOP_WSWSCEN_TEXT_SERVICE_IMPL, SWT.LEFT | SWT.BORDER, WebServiceConsumptionUIPlugin.getInstance().getDialogSettings() );
	
	Object layoutData = serviceImpl_.getLayoutData();
	if (layoutData instanceof GridData) {
		((GridData)layoutData).widthHint = 225;
	}
	
	objectModifyListener_ = new ModifyListener(){
		public void modifyText(ModifyEvent e) {
			if (serviceImpl_.getText().indexOf(':') > 0) {
		        timer_ = Timer.newInstance(timer_, Display.getCurrent(), ClientWizardWidget.this);
		        timer_.startTimer();
			}
		    else
		        run();
		}
	};
	
	serviceImpl_.addModifyListener(objectModifyListener_);
	
	browseButton_ = utils.createPushButton(typeComposite,
			ConsumptionUIMessages.BUTTON_BROWSE, ConsumptionUIMessages.TOOLTIP_WSWSCEN_BUTTON_BROWSE_IMPL, null);
	
	browseButton_.addSelectionListener(new WSDLBrowseListener());
	
	utils.createHorizontalSeparator(parent, 1);
	
	Composite clientComposite = utils.createComposite( parent, 1, 0, 0 );
	
    clientWidget_ = new WebServiceClientTypeWidget(true);
    clientWidget_.addControls(clientComposite , statusListener );
   
    //  Create test service check box.
    Composite buttonGroup = utils.createComposite(clientComposite,1);
    
    // Create monitor service check box.
    monitorService_ = utils.createCheckbox(buttonGroup , ConsumptionUIMessages.CHECKBOX_MONITOR_WEBSERVICE,
    									ConsumptionUIMessages.TOOLTIP_PWPR_CHECKBOX_MONITOR_SERVICE,
    									INFOPOP_WSWSCEN_CHECKBOX_MONITOR_SERVICE);

    //show overwrite if it is enabled in the preferences
    if (getResourceContext().isOverwriteFilesEnabled()) {
		Label prefSeparator = utils.createHorizontalSeparator(parent, 1);
		prefSeparator.setText("File Options");
		Composite prefButtonPanel = utils.createComposite(parent, 1);
		overwriteButton_ = utils.createCheckbox(prefButtonPanel,
				ConsumptionUIMessages.CHECKBOX_OVERWRITE_FILES, 
				ConsumptionUIMessages.TOOLTIP_WSWSCEN_BUTTON_OVERWRITE_FILES, 
				INFOPOP_WSWSCEN_CHECKBOX_OVERWRITE);
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
  
  public void setResourceContext( ResourceContext context )
  {    
	  resourceContext_ = context;
  }
   
  public ResourceContext getResourceContext()
  {   
    if (resourceContext_ == null) {
		resourceContext_ = WebServicePlugin.getInstance()
				.getResourceContext();
	}
	return resourceContext_;
  }
  
  public void setClientTypeRuntimeServer( TypeRuntimeServer ids )
  {	  
    clientWidget_.setTypeRuntimeServer( ids );
  }
  
  public void setClientProjectName(String name)
  {
	  clientWidget_.setClientProjectName(name);
  }
  
  public void setClientEarProjectName(String name)
  {
	  clientWidget_.setClientEarProjectName(name);
  }
  
  public void setClientComponentType(String name)
  {
	  clientWidget_.setClientComponentType(name);	  
  }
  
  public void setClientNeedEAR(boolean b)
  {
	  clientWidget_.setClientNeedEAR(b);
  }
  
  public String getClientRuntimeId()
  {
	  return clientWidget_.getClientRuntimeId();
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
  
  public String getClientComponentType()
  {
	  return clientWidget_.getClientComponentType();
  }
  
  public boolean getClientNeedEAR()
  {
	  return clientWidget_.getClientNeedEAR();
  }
  
 public void setWebServiceURI(String uri)
 {
     webServiceURI_ = uri;    
 }
	public void externalize() {
		super.externalize();
				if (getClientTypeRuntimeServer() != null ) {
				    serviceImpl_.storeWidgetHistory(getClientTypeRuntimeServer().getTypeId());
			}
	}
public void internalize() {		
	serviceImpl_.removeModifyListener(objectModifyListener_);
	if (webServiceURI_ != null && webServiceURI_.length() != 0)
		serviceImpl_.setText(webServiceURI_);  
	if (getClientTypeRuntimeServer() != null ) {
	   serviceImpl_.restoreWidgetHistory(getClientTypeRuntimeServer().getTypeId());
	}
	serviceImpl_.addModifyListener(objectModifyListener_);
		
	EclipseIPath2URLStringTransformer transformer = new EclipseIPath2URLStringTransformer();
	webServiceURI_ = (String) transformer.transform(webServiceURI_);    
}
 
 public void setProject(IProject project)
 {
     project_ = project;
 }
  public void setComponentName(String name)
  {
      componentName_ = name;      
  } 
  
  public String getWebServiceURI()
  {
      return webServiceURI_ ;
  }
  
  public String getWsdlURI()
  {
	  return getWebServiceURI();
  }
  
  public IProject getProject()
  {
      return project_;
  }
  
   public String getComponentName()
   {
       return componentName_ ;
   }
  
   public WebServicesParser getWebServicesParser()
	{
		return WSDLParserFactory.getWSDLParser();
	}
	
	public void setWebServicesParser(WebServicesParser parser)
	{
		parser_ = getWebServicesParser();
		clientWidget_.setWebServicesParser(parser_);
	}
   
  public TypeRuntimeServer getClientTypeRuntimeServer()
  {
    return clientWidget_.getTypeRuntimeServer();  
  }
  
  public void setInstallClient( Boolean install)
  {
    clientWidget_.setInstallClient( install );
  }
  
  public Boolean getInstallClient()
  {
	  return clientWidget_.getInstallClient();   
  }
  
  public Boolean getTestService()
  {
	  return clientWidget_.getTestClient(); 
  }
  
  public void setTestService( Boolean value )
  {
      clientWidget_.setTestClient(value);  
  }
  
  public int getClientGeneration()
  {
	  return clientWidget_.getClientGeneration();
  }
  
  public void setClientGeneration(int value)
  {
	  clientWidget_.setClientGeneration(value);
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
	
	public void setStartClient(Boolean start) {
		clientWidget_.setStartClient( start );
	}

	public Boolean getStartClient() {	
		return clientWidget_.getStartClient();
	}
  
  public Boolean getMonitorService()
  {
    return new Boolean(monitorService_.getSelection());
  }
  
  public void setMonitorService(Boolean value)
  {
    monitorService_.setSelection(value.booleanValue());
  }

  public IStatus getStatus() {
				
		validObjectSelection_ = false;	// assume false at first

		IStatus missingFieldStatus = checkMissingFieldStatus();
		if (missingFieldStatus.getSeverity() == IStatus.ERROR) {
			return missingFieldStatus;
		}

		IStatus invalidServiceImplStatus = checkServiceImplTextStatus();
		if (invalidServiceImplStatus.getSeverity() == IStatus.ERROR) {
			return invalidServiceImplStatus;
		}

		IStatus possibleErrorStatus = checkErrorStatus();
		if (possibleErrorStatus.getSeverity() == IStatus.ERROR) {
			return possibleErrorStatus;
		}

		IStatus possibleWarningStatus = checkWarningStatus();
		if (possibleWarningStatus.getSeverity() == IStatus.WARNING) {
			return possibleWarningStatus;
		}

		// if no errors or warnings were found, clear the validation state.
		validationState_ = ValidationUtils.VALIDATE_NONE;
		clientWidget_.setValidationState(ValidationUtils.VALIDATE_NONE);

		return Status.OK_STATUS;
	}
	
	private IStatus checkMissingFieldStatus() {

		if (validationState_ == ValidationUtils.VALIDATE_ALL) {
			if (serviceImpl_.getText().trim().length() == 0) {
				return StatusUtils.errorStatus(NLS.bind(ConsumptionUIMessages.MSG_NO_SERVICE_SELECTION, new String[]{ConsumptionUIMessages.LABEL_WEBSERVICEIMPL}));
			}
		}
		
		IStatus clientMissingFieldsStatus = clientWidget_.checkMissingFieldStatus();
		if (clientMissingFieldsStatus.getSeverity() == IStatus.ERROR) {
			return clientMissingFieldsStatus;
		}
		
		return Status.OK_STATUS;

	}

	private IStatus checkErrorStatus() {
		IStatus clientSideErrorStatus = clientWidget_.checkErrorStatus();
		if (clientSideErrorStatus.getSeverity() == IStatus.ERROR) {
			return clientSideErrorStatus;
		}
		return Status.OK_STATUS;
	}

	private IStatus checkWarningStatus() {
		IStatus clientWarningStatus = clientWidget_.checkWarningStatus();
		if (clientWarningStatus.getSeverity() == IStatus.WARNING) {
			return clientWarningStatus;
		}
		return Status.OK_STATUS;
	}	
	
	/*call validation code in the object selection widget to ensure
	 any modifications to the serviceImpl_ field are valid*/
	private IStatus checkServiceImplTextStatus() {
		
		String fieldText = serviceImpl_.getText().trim();

		if (wsdlValidatorWidget_ == null)
			wsdlValidatorWidget_ = new WSDLSelectionWidgetWrapper();
		
		validObjectSelection_ = wsdlValidatorWidget_.validate(fieldText);
				
		if (!validObjectSelection_)
		{
			return StatusUtils.errorStatus(ConsumptionUIMessages.MSG_INVALID_SERVICE_DEF);			
		}		
		
		return Status.OK_STATUS;
	}
	
	private void callObjectTransformation(IProject project, String componentName,
			String wsdlURI)
	{
		   WSDLSelectionOutputCommand wsdlOutputCommand = new WSDLSelectionOutputCommand();
		   wsdlOutputCommand.setComponentName(componentName);
		   wsdlOutputCommand.setProject(project);
		   wsdlOutputCommand.setWsdlURI(wsdlURI);
		   wsdlOutputCommand.setTestService(getTestService().booleanValue());
		   wsdlOutputCommand.setWebServicesParser(getWebServicesParser());
		
           wsdlOutputCommand.execute(null, null);
      
           setComponentName(wsdlOutputCommand.getComponentName());
           setProject(wsdlOutputCommand.getProject());
           setWebServicesParser(wsdlOutputCommand.getWebServicesParser());
           setWebServiceURI(wsdlOutputCommand.getWsdlURI());
           
           // bug 159911: our starting point is a wsdl, we should not need to
           // refresh the runtime everytime the user specify a new wsdl path
           // refreshClientRuntimeSelection();
     }
	
  private class WSDLBrowseListener implements SelectionListener
  {
	  public void widgetDefaultSelected(SelectionEvent e) {
		// TODO Auto-generated method stub
		
	}
	  public void widgetSelected(SelectionEvent e) {
		  
			// bug 269097 - Use the Web Service Client's dialog shell, not the platform workbench's shell.
			wsdlDialog_ = new WSDLSelectionDialog(Display.getCurrent().getActiveShell(), 
				  						new PageInfo(ConsumptionUIMessages.DIALOG_TITILE_SERVICE_IMPL_SELECTION, "", 
				                        new WidgetContributorFactory()
				  						{	
				  							public WidgetContributor create()
				  							{	  						 
				  							   return new WSDLSelectionWidgetWrapper();
				  							}
				  						}));

		   wsdlDialog_.setComponentName(getComponentName());
		   wsdlDialog_.setProject(getProject());
		   wsdlDialog_.setWebServiceURI( serviceImpl_.getText() );		
		   
		   int result = wsdlDialog_.open();
		   
		   if (result == Dialog.OK)
		   {
			   serviceImpl_.removeModifyListener(objectModifyListener_);
			   serviceImpl_.setText(wsdlDialog_.getDisplayableSelectionString());
			   serviceImpl_.addModifyListener(objectModifyListener_);
			   
			   // call WSDLSelectionOutputCommand to carry out any transformation on the objectSelection
	           callObjectTransformation(project_,
	        			wsdlDialog_.getComponentName(), wsdlDialog_.getWebServiceURI());
	           
		       validationState_ = ValidationUtils.VALIDATE_ALL;
		       clientWidget_.setValidationState(ValidationUtils.VALIDATE_ALL);
		       statusListener_.handleEvent(null); //validate the page
		   }
		   wsdlDialog_ = null;
	  }
	}
}




