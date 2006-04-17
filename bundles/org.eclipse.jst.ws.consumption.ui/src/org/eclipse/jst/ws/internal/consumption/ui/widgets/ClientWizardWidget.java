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
 * 20060417   136390/136159 joan@ca.ibm.com - Joan Haggarty
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.ui.widgets;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jst.ws.internal.consumption.ui.ConsumptionUIMessages;
import org.eclipse.jst.ws.internal.consumption.ui.command.data.EclipseIPath2URLStringTransformer;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.runtime.ClientRuntimeSelectionWidgetDefaultingCommand;
import org.eclipse.jst.ws.internal.data.TypeRuntimeServer;
import org.eclipse.jst.ws.internal.plugin.WebServicePlugin;
import org.eclipse.jst.ws.internal.ui.common.UIUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.internal.Workbench;
import org.eclipse.wst.command.internal.env.core.context.ResourceContext;
import org.eclipse.wst.command.internal.env.ui.widgets.PageInfo;
import org.eclipse.wst.command.internal.env.ui.widgets.SimpleWidgetDataContributor;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetContributor;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetContributorFactory;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetDataEvents;
import org.eclipse.wst.ws.internal.parser.wsil.WebServicesParser;


public class ClientWizardWidget extends SimpleWidgetDataContributor
{  
  private WebServiceClientTypeWidget clientWidget_;
  private Button overwriteButton_;
  private Button monitorService_;

  private Text serviceImpl_;
  private Button browseButton_;
  private WSDLSelectionDialog wsdlDialog_;
  private String clientRuntimeId_ ;
  private String clientEarProjectName_;
  private boolean clientNeedEAR_;
  private String componentName_;
  private IProject project_;
  private String webServiceURI_;
  private WebServicesParser parser_;
  private ResourceContext resourceContext_;

  /* CONTEXT_ID WSWSCEN0020 for the Service Implemenation text field of the Scenario Page */
	 private String INFOPOP_WSWSCEN_TEXT_SERVICE_IMPL = "WSWSCEN0020";

  
  /* CONTEXT_ID WSWSCEN0014 for the monitor service checkbox of the Scenario page */
  private String INFOPOP_WSWSCEN_CHECKBOX_MONITOR_SERVICE = "WSWSCEN0014";	
  /* CONTEXT_ID WSWSCEN0030 for the Overwrite Files checkbox of the Scenario Page */
  private String INFOPOP_WSWSCEN_CHECKBOX_OVERWRITE = "WSWSCEN0030";	
  /* CONTEXT_ID WSWSCEN0001 for the Scenario Page */
  private String INFOPOP_WSWSCEN_PAGE = "WSWSCEN0001";
  
  public WidgetDataEvents addControls( Composite parent, Listener statusListener)
  {
    String       pluginId = "org.eclipse.jst.ws.consumption.ui";
    UIUtils      utils    = new UIUtils( pluginId );
    utils.createInfoPop(parent, INFOPOP_WSWSCEN_PAGE);
    
  	// Create text field and browse for service selection
  	Composite typeComposite = utils.createComposite(parent, 3);
	serviceImpl_ = utils.createText(typeComposite, ConsumptionUIMessages.LABEL_WEBSERVICEDEF, 
			ConsumptionUIMessages.TOOLTIP_WSWSCEN_TEXT_IMPL,
			INFOPOP_WSWSCEN_TEXT_SERVICE_IMPL, SWT.LEFT | SWT.BORDER | SWT.READ_ONLY);
	browseButton_ = utils.createPushButton(typeComposite,
			ConsumptionUIMessages.BUTTON_BROWSE, ConsumptionUIMessages.TOOLTIP_WSWSCEN_BUTTON_BROWSE_IMPL, null);
	
    wsdlDialog_ = new WSDLSelectionDialog(Workbench.getInstance().getActiveWorkbenchWindow().getShell(), 
		  						new PageInfo(ConsumptionUIMessages.DIALOG_TITILE_SERVICE_IMPL_SELECTION, "", 
		                        new WidgetContributorFactory()
		  						{	
		  							public WidgetContributor create()
		  							{	  						 
		  							   return new WSDLSelectionWidgetWrapper();
		  							}
		  						}));		
	browseButton_.addSelectionListener(new WSDLBrowseListener());
	browseButton_.addListener(SWT.Modify, statusListener); 
	
	utils.createHorizontalSeparator(parent, 1);
	
	Composite clientComposite = utils.createComposite( parent, 1 );
	
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
  
  public void setClientRuntimeId(String id)
  {
	  clientRuntimeId_ = id;
	  clientWidget_.setClientRuntimeId(id);
  }
  
  public void setClientProjectName(String name)
  {	  
	  clientWidget_.setClientProjectName(name);
  }
  
  public void setClientEarProjectName(String name)
  {
	  clientEarProjectName_ = name;
	  clientWidget_.setClientEarProjectName(name);
  }
  
  public void setClientComponentType(String name)
  {
	  clientWidget_.setClientComponentType(name);	  
  }
  
  public void setClientNeedEAR(boolean b)
  {
	  clientNeedEAR_ = b;
	  clientWidget_.setClientNeedEAR(b);
  }
  
  public String getClientRuntimeId()
  {
	  return clientRuntimeId_;
  }
  
  public String getClientEarProjectName()
  {
	  return clientEarProjectName_;
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
	  return clientNeedEAR_;
  }
  
 public void setWebServiceURI(String uri)
 {
     webServiceURI_ = uri;    
     wsdlDialog_.setWebServiceURI(uri);
     String wsdlDialogDisplayableString = wsdlDialog_.getDisplayableSelectionString();
     if (uri != null && uri.length() > 0)
     {
       if (wsdlDialogDisplayableString != null && wsdlDialogDisplayableString.length() > 0)
       {
    	   serviceImpl_.setText(wsdlDialogDisplayableString);     	 
       }
       else 
       {
    	   //This else clause is to handle the call to the enclosing method
    	   //when the page first comes up since wsdlDialog_ will not have been
    	   //properly initialized with a WSDLSelectionWidgetWrapper containing
    	   //a non-null WSDLSelectionWidget.
    	   EclipseIPath2URLStringTransformer transformer = new EclipseIPath2URLStringTransformer();
    	   webServiceURI_ = (String)transformer.transform(uri);
    	   serviceImpl_.setText(uri);    	 
       }
     }
     
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
		return parser_;
	}
	
	public void setWebServicesParser(WebServicesParser parser)
	{
		parser_ = parser;	
		clientWidget_.setWebServicesParser(parser);
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
  
  public Boolean getMonitorService()
  {
    return new Boolean(monitorService_.getSelection());
  }
  
  public void setMonitorService(Boolean value)
  {
    monitorService_.setSelection(value.booleanValue());
  }

  private void refreshClientRuntimeSelection()
	{		
		//new up ServerRuntimeSelectionWidgetDefaultingCommand
		ClientRuntimeSelectionWidgetDefaultingCommand clientRTDefaultCmd = new ClientRuntimeSelectionWidgetDefaultingCommand();
		
		// jvh - don't seem to need these??
	/*	clientRTDefaultCmd.setClientInitialSelection();  //selected in object selection
		clientRTDefaultCmd.setInitialInitialSelection();  //original selection...*/		
		clientRTDefaultCmd.setResourceContext(resourceContext_);
		clientRTDefaultCmd.setClientEarProjectName(getClientEarProjectName());
		clientRTDefaultCmd.setClientInitialProject(getProject());  
        clientRTDefaultCmd.setClientTypeRuntimeServer(getClientTypeRuntimeServer());
		clientRTDefaultCmd.setTestService(getTestService().booleanValue());
		clientRTDefaultCmd.setWebServicesParser(getWebServicesParser());
		clientRTDefaultCmd.setWsdlURI(getWsdlURI());
		clientRTDefaultCmd.execute(null, null);
		
		clientRTDefaultCmd.execute(null, null);
		  
		setClientComponentType(clientRTDefaultCmd.getClientComponentType());
		setClientEarProjectName(clientRTDefaultCmd.getClientEarProjectName());
		setClientNeedEAR(clientRTDefaultCmd.getClientNeedEAR());
		setClientProjectName(clientRTDefaultCmd.getClientProjectName());
		setClientRuntimeId(clientRTDefaultCmd.getClientRuntimeId());
		setClientTypeRuntimeServer(clientRTDefaultCmd.getClientTypeRuntimeServer());
		
		//setServiceRuntimeId(clientRTDefaultCmd.getXXX()));
		  
	}
  
  private class WSDLBrowseListener implements SelectionListener
  {
	  public void widgetDefaultSelected(SelectionEvent e) {
		// TODO Auto-generated method stub
		
	}
	  public void widgetSelected(SelectionEvent e) {
		  
		   wsdlDialog_.setComponentName(getComponentName());
		   wsdlDialog_.setProject(getProject());
		   wsdlDialog_.setWebServiceURI(getWebServiceURI());		   
		   
		   int result = wsdlDialog_.open();
		   
		   if (result == Dialog.OK)
		   {
			   serviceImpl_.setText(wsdlDialog_.getDisplayableSelectionString());
			   
			   // call WSDLSelectionOutputCommand to carry out any transformation on the objectSelection
			   WSDLSelectionOutputCommand wsdlOutputCommand = new WSDLSelectionOutputCommand();
			   wsdlOutputCommand.setComponentName(wsdlDialog_.getComponentName());
			   wsdlOutputCommand.setProject(wsdlDialog_.getProject());
			   wsdlOutputCommand.setWsdlURI(wsdlDialog_.getWebServiceURI());
			   wsdlOutputCommand.setTestService(getTestService().booleanValue());
			   wsdlOutputCommand.setWebServicesParser(getWebServicesParser());
			
	           wsdlOutputCommand.execute(null, null);
	      
	           setComponentName(wsdlOutputCommand.getComponentName());
	           setProject(wsdlOutputCommand.getProject());
	           setWebServicesParser(wsdlOutputCommand.getWebServicesParser());
	           setWebServiceURI(wsdlOutputCommand.getWsdlURI());
	       
		       refreshClientRuntimeSelection();   
		   }
	  }
	}
}




