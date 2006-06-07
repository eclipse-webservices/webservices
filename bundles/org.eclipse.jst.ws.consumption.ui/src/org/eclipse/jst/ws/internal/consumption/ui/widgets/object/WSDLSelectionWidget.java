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
 * 20060410   135441 joan@ca.ibm.com - Joan Haggarty
 * 20060410   136011 kathy@ca.ibm.com - Kathy Chan
 * 20060420   135912 joan@ca.ibm.com - Joan Haggarty
 * 20060504   119296 pmoogk@ca.ibm.com - Peter Moogk
 * 20060525   142281 joan@ca.ibm.com - Joan Haggarty
 * 20060607   144932 kathy@ca.ibm.com - Kathy Chan
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.ui.widgets.object;

import java.io.File;
import java.net.MalformedURLException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.IJobManager;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jst.j2ee.webservice.wsclient.ServiceRef;
import org.eclipse.jst.ws.internal.common.J2EEActionAdapterFactory;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.internal.consumption.common.WSDLParserFactory;
import org.eclipse.jst.ws.internal.consumption.ui.ConsumptionUIMessages;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.TimedWSDLSelectionConditionCommand;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.WSDLSelectionTreeWidget;
import org.eclipse.jst.ws.internal.ui.common.DialogResourceBrowser;
import org.eclipse.jst.ws.internal.ui.common.FileExtensionFilter;
import org.eclipse.jst.ws.internal.ui.common.UIUtils;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetDataEvents;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.ws.internal.parser.wsil.WebServiceEntity;
import org.eclipse.wst.ws.internal.parser.wsil.WebServicesParser;
import org.eclipse.wst.ws.internal.plugin.WSPlugin;
import org.eclipse.wst.ws.internal.preferences.PersistentWSDLValidationContext;
import org.eclipse.wst.wsdl.internal.impl.ServiceImpl;
import org.eclipse.wst.wsdl.util.WSDLResourceImpl;
import org.eclipse.wst.wsdl.validation.internal.IValidationMessage;

public class WSDLSelectionWidget extends AbstractObjectSelectionWidget implements IObjectSelectionWidget, Runnable
{
  private String              pluginId_;
  private FileExtensionFilter wsFilter_;
  private WebServicesParser webServicesParser;
  private String wsdlURI_;
  
  private Composite parent_;
  private Listener  statusListener_;
  private WSDLSelectionTreeWidget tree;
  
  /*CONTEXT_ID PCON0001 for the WSDL Selection Page*/
  private final String INFOPOP_PCON_PAGE = "PCON0001";

  /*CONTEXT_ID PCON0002 for the WSDL Document text field of the WSDL Selection Page*/
  private final String INFOPOP_PCON_TEXT_WSDL = "PCON0002";
  private Text webServiceURI;

  /*CONTEXT_ID PCON0003 for the WSDL Resource Browse button of the WSDL Selection Page*/
  private final String INFOPOP_PCON_BUTTON_BROWSE_WSDL = "PCON0003";
  private Button wsBrowseButton_;
  
  /*CONTEXT_ID PCON0004 for the Wizard WSDL Validation table of the WSDL Selection Page*/
  private ValidationMessageViewerWidget msgViewer_;
  private ValidateWSDLJob validateWSDLJob_;
  private JobChangeAdapter    jobChangeAdapter_;
  
  /*CONTEXT_ID PCON0005 for the Wizard WSDL Validation summary message of the WSDL Selection Page*/
  private Text validationSummaryText_;
  private Text validationSummaryText2_;

  
  public WSDLSelectionWidget()
  {
    pluginId_ = "org.eclipse.jst.ws.consumption.ui";
    wsFilter_ = new FileExtensionFilter(new String[] {"wsdl", "wsil", "html"});
    webServicesParser = WSDLParserFactory.getWSDLParser();
    
    final Runnable handleValidationMessages = new Runnable()
    {
	  public void run() 
      {
		  msgViewer_.setInput(validateWSDLJob_.getValidationMessages());
		  updateValidationSummary(validateWSDLJob_.getValidationMessageSeverity());
      }
    };
    
    jobChangeAdapter_ = new JobChangeAdapter()
    {
      public void done(IJobChangeEvent event) 
      {   	
    	  if (msgViewer_!= null && msgViewer_.getContentProvider() != null) {
    		  Display.getDefault().asyncExec( handleValidationMessages );
		  }
      }
    };
  }
  
  public WidgetDataEvents addControls( Composite parent, Listener statusListener )
  {
    UIUtils uiUtils  = new UIUtils( pluginId_ );
    parent_          = parent;
    statusListener_  = statusListener;

	  parent.setToolTipText( ConsumptionUIMessages.TOOLTIP_PCON_PAGE );
	  PlatformUI.getWorkbench().getHelpSystem().setHelp( parent, pluginId_ + "." + INFOPOP_PCON_PAGE );
    
    Composite wsdlGroup = uiUtils.createComposite( parent, 2, 5, 0 );
    
    Label wsLabel = new Label( wsdlGroup, SWT.WRAP);
    wsLabel.setText( ConsumptionUIMessages.LABEL_WS_SELECTION);
    GridData gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
    gd.horizontalSpan = 2;
    wsLabel.setLayoutData(gd);
    wsLabel.setToolTipText( ConsumptionUIMessages.TOOLTIP_PCON_TEXT_WS );
    
    webServiceURI = uiUtils.createText( wsdlGroup, null, 
    						ConsumptionUIMessages.TOOLTIP_PCON_TEXT_WS, 
    						INFOPOP_PCON_TEXT_WSDL, SWT.SINGLE | SWT.BORDER );
    webServiceURI.addModifyListener(
      new ModifyListener()
      {
        public void modifyText(ModifyEvent event)
        {
          handleWebServiceURIModifyEvent();
        }
      });
//    webServiceURI.addListener( SWT.Modify, statusListener );

    wsBrowseButton_ = uiUtils.createPushButton( wsdlGroup, ConsumptionUIMessages.BUTTON_BROWSE, 
    								ConsumptionUIMessages.TOOLTIP_PCON_BUTTON_BROWSE_WS, 
    								INFOPOP_PCON_BUTTON_BROWSE_WSDL );
    wsBrowseButton_.addSelectionListener(
      new SelectionListener()
      {
        public void widgetDefaultSelected(SelectionEvent event)
        {
          handleWSDLButton();
        }
        
        public void widgetSelected(SelectionEvent event)
        {
          handleWSDLButton();
        } 
      });

    tree = new WSDLSelectionTreeWidget();
    tree.addControls(parent, statusListener);
    tree.setWebServicesParser(webServicesParser);    
    
    msgViewer_ = new ValidationMessageViewerWidget();
    msgViewer_.addControls(parent, statusListener);
    
    validationSummaryText_ = new Text( parent, SWT.WRAP);
    validationSummaryText_.setEditable(false);
    GridData gd1 = new GridData(GridData.FILL_BOTH);
    validationSummaryText_.setLayoutData(gd1);
    validationSummaryText_.setToolTipText( ConsumptionUIMessages.TOOLTIP_VALIDATE_TEXT_MESSAGE_SUMMARY );
    
    validationSummaryText2_ = new Text( parent, SWT.WRAP);
    validationSummaryText2_.setEditable(false);
    validationSummaryText2_.setLayoutData(gd1);
    
    setMessageSummary();
    return this;
  }
  
  private void setMessageSummary() {
	  String validationMessageSummary = ConsumptionUIMessages.MESSAGE_VALIDATE_NO_WSDL;
	  PersistentWSDLValidationContext wsdlValidationContext = WSPlugin.getInstance().getWSDLValidationContext();
	  String validationSelection = wsdlValidationContext.getPersistentWSDLValidation();
	  if (PersistentWSDLValidationContext.VALIDATE_REMOTE_WSDL.equals(validationSelection)) {
		  validationMessageSummary = ConsumptionUIMessages.MESSAGE_VALIDATE_REMOTE_WSDL;
	  } else if (PersistentWSDLValidationContext.VALIDATE_ALL_WSDL.equals(validationSelection)) {
		  validationMessageSummary = ConsumptionUIMessages.MESSAGE_VALIDATE_ALL_WSDL;
	  }
	  validationSummaryText_.setText( validationMessageSummary );
	  validationSummaryText2_.setText(" ");
  }
  
  private void handleWebServiceURIModifyEvent()
  {
    if (webServiceURI.getText().indexOf(':') > 0)
      Timer.newInstance(Display.getCurrent(), this).startTimer();
    else
      handleWebServiceURI();
    statusListener_.handleEvent(null);
  }
  
  private void handleWebServiceURI()
  {
    String wsURI = webServiceURI.getText();
    
    if (wsURI.indexOf(':') < 0)
    {
      IFile file = uri2IFile(wsURI);
      if (file != null)
        wsURI = iFile2URI(file);
    }
    if (wsURI != null && wsURI.indexOf(':') >= 0 && webServicesParser.getWebServiceEntityByURI(wsURI) == null)
    {
      TimedWSDLSelectionConditionCommand cmd = new TimedWSDLSelectionConditionCommand();
      cmd.setWebServicesParser(webServicesParser);
      cmd.setWebServiceURI(wsURI);
      cmd.execute(null, null);
    }
    WebServiceEntity entity = webServicesParser.getWebServiceEntityByURI(wsURI);
    if (entity != null && entity.getType() == WebServiceEntity.TYPE_WSDL)
      tree.setEnabled(false);
    else
      tree.setEnabled(true);
    tree.setWebServiceURI(wsURI);
    tree.refreshTreeViewer();  
    wsdlURI_ = wsURI;
  }
  
  public void run()
  {
    handleWebServiceURI();
    statusListener_.handleEvent(null);
  }

  private void handleWSDLButton()
  {
    DialogResourceBrowser dialog = new DialogResourceBrowser( parent_.getShell(), null, wsFilter_);
    dialog.open();
    IResource res = dialog.getFirstSelection();
    if( res != null )
    {
    	wsdlURI_ = res.getFullPath().toString();
    	webServiceURI.setText( wsdlURI_ );    	
    }
    	
    statusListener_.handleEvent(null);
  }
  
  public IStatus getStatus()
  {
    // Timer validation
    /*
     * Commenting out because we don't want to block fast typers from hitting Next/Finish 
    if (Timer.isRunning())
      return new SimpleStatus("", ConsumptionUIMessages.PAGE_MSG_LOADING_WEB_SERVICE_URI, Status.ERROR);
    */
	
    // Validate the String representation of the Web service URI
    // For example, is it pointing to an existing resource in the workspace?
    String wsPath  = webServiceURI.getText();
    if( wsPath == null || wsPath.length() <= 0 )
      return StatusUtils.errorStatus( ConsumptionUIMessages.PAGE_MSG_INVALID_WEB_SERVICE_URI );
    else if( wsPath.indexOf(':') < 0 )
    {
      IResource res = ResourceUtils.findResource(wsPath);
      if( res == null ) {
    	  msgViewer_.clearInput();
        return StatusUtils.errorStatus( NLS.bind(ConsumptionUIMessages.PAGE_MSG_NO_SUCH_FILE, new Object[] {wsPath}) );
      }
      else if( res.getType() != IResource.FILE ) {
    	  msgViewer_.clearInput();
        return StatusUtils.errorStatus( ConsumptionUIMessages.PAGE_MSG_INVALID_WEB_SERVICE_URI );
      }
    }

    
    // Validate the content of the Web service URI
    // For example, is selection a WSDL URI?
    if (!Timer.isRunning() && tree.isEnabled())
    {
      IStatus status = tree.getStatus();
      if (status != null)
      {
        int severity = status.getSeverity();
        if (severity == Status.ERROR || severity == Status.WARNING) {
        	msgViewer_.clearInput();
          return status;
        }
      }
    }
    else
    {
    	if( wsPath.indexOf(':') < 0 )
        {
          String wsdlURI = iFile2URI((IFile)ResourceUtils.findResource(wsPath));
          if (webServicesParser.getWSDLDefinition(wsdlURI) == null) {
        	  msgViewer_.clearInput();
            return StatusUtils.errorStatus(ConsumptionUIMessages.PAGE_MSG_SELECTION_MUST_BE_WSDL );
          }
        }
    }
    
    
    if (!Timer.isRunning()) {
    	String wsdlURI1 = wsPath;
    	 boolean isRemote = true;
    	if (tree.isEnabled()) { // is wsil
    		wsdlURI1 = tree.getWsdlURI();
    		if (wsdlURI1.startsWith("file:")) {
    			isRemote = false;
    		}
    	} else { // is wsil
    	
    		if( wsPath.indexOf(':') < 0 )
    		{      	// not remote
    			isRemote = false;
    			wsdlURI1 = iFile2URI((IFile)ResourceUtils.findResource(wsPath));
    		}
    	}
  	  
      setMessageSummary();
	  msgViewer_.clearInput();
  	  validateWSDL(wsdlURI1, isRemote);
    }
    

    // OK status
    return Status.OK_STATUS;
  }
  
  private void validateWSDL (String wsdlURI, boolean isRemote) {

	  String validationSelection = WSPlugin.getInstance().getWSDLValidationContext().getPersistentWSDLValidation();;
	  if ((PersistentWSDLValidationContext.VALIDATE_ALL_WSDL.equals(validationSelection)) ||
			  (PersistentWSDLValidationContext.VALIDATE_REMOTE_WSDL.equals(validationSelection) && isRemote)) {

		  IJobManager    jobManager     = Platform.getJobManager();
		  Job[]          jobs           = jobManager.find( ValidateWSDLJob.VALIDATE_WSDL_JOB_FAMILY );
		  ValidateWSDLJob existingValidateWSDLJob = null;
		  
		  boolean startWSDLValidation = true;
		  validationSummaryText_.setText( ConsumptionUIMessages.MESSAGE_VALIDATE_IN_PROGRESS );
		  validationSummaryText2_.setText(" ");
		  if( jobs.length > 0 )
		  {
			  for (int i=0; i<jobs.length; i++) {
				  existingValidateWSDLJob = (ValidateWSDLJob)jobs[i];
				  
				  if (existingValidateWSDLJob.getState() != Job.NONE) { 
					  // Job running or to be run
					  // If the job is validating the same wsdlURI, let it finish running and ignore this one.
					  // It is not for the same wsdlURI, cancel the job and schedule this one.

					  if (!wsdlURI.equals(existingValidateWSDLJob.getWsdlURI())) {
						  existingValidateWSDLJob.cancel();
					  } else {						  
						  startWSDLValidation = false;
					  }
				  } 
			  }
		  } 
		  
		  if (startWSDLValidation) {
			  startWSDLValidationJob(wsdlURI);
		  }
	  }
	  return;
  }
  
  private void startWSDLValidationJob (String wsdlURI) {
	  validateWSDLJob_ = new ValidateWSDLJob(wsdlURI);
	  validateWSDLJob_.addJobChangeListener( jobChangeAdapter_ );
	  validateWSDLJob_.schedule();
  }
  
  public void updateValidationSummary(int messageSeverity)
  {
  	
  	switch (messageSeverity) {
  	case IValidationMessage.SEV_ERROR:
		validationSummaryText_.setText(ConsumptionUIMessages.ERROR_MESSAGES_IN_VALIDATION);
		validationSummaryText2_.setText(ConsumptionUIMessages.WARNING_IF_CONTINUE);
		break;
	case IValidationMessage.SEV_WARNING:
		validationSummaryText_.setText(ConsumptionUIMessages.WARNING_MESSAGES_IN_VALIDATION);
		validationSummaryText2_.setText(ConsumptionUIMessages.WARNING_IF_CONTINUE);
		break;
	default:
		validationSummaryText_.setText(ConsumptionUIMessages.VALIDATION_COMPLETED);
		validationSummaryText2_.setText(" ");
		break;
	}
  }
  
  private IFile uri2IFile(String uri)
  {
    IResource res = ResourcesPlugin.getWorkspace().getRoot().findMember(uri);
    
    if (res instanceof IFile)
      return (IFile)res;
    else
      return null;
  }
  
  private String iFile2URI(IFile file)
  {
  	File f = file.getLocation().toFile();
    try
    {
      return f.toURL().toString();
    }
    catch (MalformedURLException murle)
    {
    }
    return f.toString();
  }

  public void setInitialSelection(IStructuredSelection initialSelection)
  {
    if (initialSelection != null && !initialSelection.isEmpty())
    {
      Object object = initialSelection.getFirstElement();
      String wsdlURI = toWsdlURI(object);  
      wsdlURI_ = wsdlURI;
      
      if (wsdlURI != null && webServiceURI != null)
      {
        webServiceURI.setText(wsdlURI);       
        handleWebServiceURI();
      }
    }  
  }
  
  private String toWsdlURI(Object object)
  {
    if (object instanceof ServiceImpl)
      return J2EEActionAdapterFactory.getWSDLURI((ServiceImpl)object);
    else if (object instanceof WSDLResourceImpl)
      return J2EEActionAdapterFactory.getWSDLURI((WSDLResourceImpl)object);
    else if (object instanceof ServiceRef)
      return J2EEActionAdapterFactory.getWSDLURI((ServiceRef)object);
    else if (object instanceof IFile)
      return ((IFile)object).getFullPath().toString();
    else if (object instanceof String)
      return (String)object;
    else
      return null;
  }
  
  public IStructuredSelection getObjectSelection()
  {
	  StructuredSelection ss; 
	  if (tree != null)
		  ss = new StructuredSelection(tree.getWsdlURI());
	  else
		  ss = new StructuredSelection(wsdlURI_);
	return new StructuredSelection( 
               new WSDLSelectionWrapper( webServicesParser, ss));
  }
  
  public WebServicesParser getWebServicesParser()
  {
    return webServicesParser;
  }
  
  public IStatus validateSelection(IStructuredSelection objectSelection)
  {
    return Status.OK_STATUS;
  }
  
  public IProject getProject()
  {
	String wsdlURI;

	if (tree != null)
       wsdlURI = tree.getWsdlURI();
	else
		wsdlURI = wsdlURI_;
	
    if (wsdlURI != null)
    {
      IProject p = getProjectFromURI(wsdlURI);
      if (p!=null && p.exists())
        return p;
      
      String wsRelPath = wsdlURI_;
      IResource wsRes = ResourceUtils.findResource(wsRelPath);
      if (wsRes!=null && wsRes instanceof IFile)
      {
        IProject p2 = ((IFile)wsRes).getProject();
        return p2;
      }
      
    }
    return null;
  }
  
  public String getComponentName()
  {
	 
    String wsdlURI;
    if (tree != null)
    	wsdlURI = tree.getWsdlURI();
    else 
    	wsdlURI = wsdlURI_;
    
    if (wsdlURI != null)
    {
      String cname = getComponentNameFromURI(wsdlURI);
      if (cname!=null && cname.length()>0)
        return cname;
      
      String wsRelPath = wsdlURI_;
      IResource wsRes = ResourceUtils.findResource(wsRelPath);
      if (wsRes!=null && wsRes instanceof IFile)
      {
        IVirtualComponent comp = ResourceUtils.getComponentOf(wsRes);
        if (comp!=null)
        {
          return comp.getName();
        }
      }
      
    }
    return null;
  }
  
  private IProject getProjectFromURI(String uri)
  {
    IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
    String wkspcRootLoc = root.getLocation().toString();
    int idx = uri.indexOf(wkspcRootLoc);
    if (idx != -1) 
    {
      String relPath = uri.substring(wkspcRootLoc.length()+idx);
      IResource res = root.findMember(new Path(relPath));
      if (res instanceof IFile)
      {
        IProject p = ((IFile)res).getProject();
        return p;
      }
    }
    return null;
  }
  
  private String getComponentNameFromURI(String uri)
  {
    IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
    String wkspcRootLoc = root.getLocation().toString();
    int idx = uri.indexOf(wkspcRootLoc);
    if (idx != -1) 
    {
      String relPath = uri.substring(wkspcRootLoc.length()+idx);
      IResource res = root.findMember(new Path(relPath));
      if (res instanceof IFile)
      {
        IVirtualComponent comp = ResourceUtils.getComponentOf(res);
        if (comp!=null)
        {
          return comp.getName();
        }        
      }
    }
    return null;    
  }
  
  public String getObjectSelectionDisplayableString() {	
	    return wsdlURI_;
	  }
  
  public Point getWidgetSize() {	
	  return new Point( 580, 580);  
  }
  
  public boolean validate(String s) {
	  String wsURI = s;
	    
	    if (wsURI.indexOf(':') < 0)
	    {
	      IFile file = uri2IFile(wsURI);
	      if (file != null)
	        wsURI = iFile2URI(file);
	    }
	    if (wsURI != null && wsURI.indexOf(':') >= 0 && webServicesParser.getWebServiceEntityByURI(wsURI) == null)
	    {
	      TimedWSDLSelectionConditionCommand cmd = new TimedWSDLSelectionConditionCommand();
	      cmd.setWebServicesParser(webServicesParser);
	      cmd.setWebServiceURI(wsURI);
	      cmd.execute(null, null);
	    }
		
	    // prime widget based on the string
	    wsdlURI_ = wsURI;
	    if (tree != null)
	      tree.setWebServiceURI(wsURI);
	    
	    WebServiceEntity entity = webServicesParser.getWebServiceEntityByURI(wsURI);
	    if (entity != null && entity.getType() == WebServiceEntity.TYPE_WSDL)
             return true;
	    else
	    	return false;
	    
  } 
}
