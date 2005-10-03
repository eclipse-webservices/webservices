/*******************************************************************************
 * Copyright (c) 2002-2005 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   IBM - Initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsi.ui.internal.actions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.wst.validation.internal.core.IMessageAccess;
import org.eclipse.wst.validation.internal.provisional.core.IMessage;
import org.eclipse.wst.validation.internal.provisional.core.IReporter;
import org.eclipse.wst.validation.internal.provisional.core.IValidator;
import org.eclipse.wst.validation.internal.provisional.core.MessageLimitException;
import org.eclipse.wst.wsi.internal.analyzer.MessageAnalyzer;
import org.eclipse.wst.wsi.internal.analyzer.WSIAnalyzerException;
import org.eclipse.wst.wsi.internal.core.WSIConstants;
import org.eclipse.wst.wsi.ui.internal.WSIUIPlugin;
import org.eclipse.wst.wsi.internal.report.AssertionError;
import org.eclipse.wst.xml.core.internal.validation.core.ValidationMessage;
import org.eclipse.wst.xml.ui.internal.validation.ValidateAction;
import org.xml.sax.SAXParseException;

/**
 * Action for running the validator.
 * 
 * @author David Lauzon, IBM
 * @author Lawrence Mandel, IBM
 */
public class WSIValidateAction extends ValidateAction
{
  protected IValidator validator;
  
  protected String wsdlfile = null;
  protected String elementname = null;
  protected String namespace = null;
  protected String parentname = null;
  protected String type = null;
  protected boolean wsdlspecified = false;
  protected boolean exceptionCaught = false;

  /**
   * Constructor.
   * 
   * @param f The file to validate
   * @param showDialog Whether or not to show a status dialog after validation.
   */
  public WSIValidateAction(IFile f, boolean showDialog)
  {
  	super(f, showDialog);
  }
  
  /**
   * Constructor with WSDL element specified.
   * 
   * @param f The file to validate.
   * @param showDialog Whether or not to show a status dialog after validation.
   * @param file The WSDL file to use for validation.
   * @param elementname The name of the WSDL element to validate.
   * @param namespace The namespace of the WSDL element to validate
   * @param parentname The parent name of the WSDL element to validate.
   * @param type The type of element to validate.
   */
  public WSIValidateAction(IFile f, boolean showDialog, String file, String elementname, String namespace, String parentname, String type)
  {
  	this(f, showDialog);
  	if (file != null)
  	{
  	  wsdlfile = file.replace('\\', '/');
  	  if ((!wsdlfile.startsWith(WSIConstants.FILE_PREFIX)) && 
  		  (!wsdlfile.startsWith(WSIConstants.HTTP_PREFIX)))
      {
  	    wsdlfile = WSIConstants.FILE_PROTOCOL + wsdlfile;  
  	  }
  	}
  	this.elementname = elementname;
  	this.namespace = namespace;
  	this.parentname = parentname;
  	this.type = type;
  	this.wsdlspecified = true;
  }

  /* (non-Javadoc)
   * @see org.eclipse.validate.ValidateAction#validate(org.eclipse.core.resources.IFile)
   */
  protected void validate(final IFile file1)
  {
  	final MessageAnalyzer messageanalyzer;
  	
  	if(wsdlspecified)
  	{
  	  messageanalyzer = new MessageAnalyzer(WSIConstants.FILE_PROTOCOL + file1.getLocation().toString(), wsdlfile, elementname, namespace, parentname, type);	
  	}
  	else
  	{	
  	  messageanalyzer = new MessageAnalyzer(WSIConstants.FILE_PROTOCOL + file1.getLocation().toString());
  	}
  	
  	IWorkspaceRunnable op = new IWorkspaceRunnable() 
    {
      public void run(IProgressMonitor progressMonitor) throws CoreException 
      {        
        clearMarkers(file1);
        try
        {
          messageanalyzer.validateConformance();
        }
        catch (WSIAnalyzerException ae)
        {
        	exceptionCaught = true;
        	if (ae.getTargetException() instanceof SAXParseException)
        	{
        	  createMarkers(file1, new ValidationMessage[]{createValidationMessageForException((SAXParseException)ae.getTargetException(), ValidationMessage.SEV_NORMAL)});
        	}
        	else
        	{
         	  createMarkers(file1, new ValidationMessage[]{createValidationMessageForException(ae, ValidationMessage.SEV_NORMAL)});
        	}
        }
        catch (Exception e)
        {
        }

        createMarkers(file1, convertValidationMessages(messageanalyzer.getAssertionWarnings(), ValidationMessage.SEV_LOW));
        createMarkers(file1, convertValidationMessages(messageanalyzer.getAssertionErrors(), ValidationMessage.SEV_NORMAL));
        file.setSessionProperty(ValidationMessage.ERROR_MESSAGE_MAP_QUALIFIED_NAME, getOrCreateReporter().getMessages());
		}
    };
   
    
     try
    {
      ResourcesPlugin.getWorkspace().run(op, null);
     if (showDialog)
      {
        if (exceptionCaught)
        {
          MessageDialog.openError(Display.getDefault().getActiveShell(), 
          		WSIUIPlugin.getResourceString("_UI_UNABLE_TO_VALIDATE"), 
          		WSIUIPlugin.getResourceString("_UI_PROBLEMS_READING_WSIMSG_FILE"));
        }
        else if (messageanalyzer.getAssertionErrors().size() != 0)
        {
          MessageDialog.openError(Display.getDefault().getActiveShell(), 
          		WSIUIPlugin.getResourceString("_UI_VALIDATION_FAILED"), 
          		WSIUIPlugin.getResourceString("_UI_THE_WSIMSG_FILE_IS_NOT_VALID"));
        }
        else if (messageanalyzer.getAssertionWarnings().size() != 0)
        {                                                           
          String title = WSIUIPlugin.getResourceString("_UI_VALIDATION_SUCEEDED");
          String message = WSIUIPlugin.getResourceString("_UI_VALIDATION_WARNINGS_DETECTED");
          MessageDialog.openInformation(Display.getDefault().getActiveShell(), title, message);
        }
        else
        {
          String title = WSIUIPlugin.getResourceString("_UI_VALIDATION_SUCEEDED");
          String message = WSIUIPlugin.getResourceString("_UI_THE_WSIMSG_FILE_IS_VALID");
          MessageDialog.openInformation(Display.getDefault().getActiveShell(), title, message);
        }
      }
    }
    catch(Exception e){}
  }
  
  /**
   * Create a validation message from the exception and severity.
   * 
   * @param error The error.
   * @param severity The severity.
   * @return An error message.
   */
  protected ValidationMessage createValidationMessageForException(SAXParseException error, int severity)
  {
    String uri = error.getSystemId();
    if(uri == null)
    {
      uri = error.getPublicId();
    }
    ValidationMessage validationMessage = new ValidationMessage(error.getMessage(), error.getLineNumber(), error.getColumnNumber(), uri);
    validationMessage.setSeverity(severity);
    return validationMessage;
  }


  /**
   * Create a validation message from the exception and severity.
   * 
   * @param error The error.
   * @param severity The severity.
   * @return An error message.
   */
  protected ValidationMessage createValidationMessageForException(WSIAnalyzerException error, int severity)
  {
    ValidationMessage validationMessage = new ValidationMessage(error.getMessage(), 0, 0);
    validationMessage.setSeverity(severity);
    return validationMessage;
  }
 
  public ValidationMessage[]  convertValidationMessages(List list, int marker)
  {
	if (list != null)
	{
	  int size = list.size();
	  ValidationMessage[] messages = new ValidationMessage[size];
	 
      for (int i = 0; i < size; i++)
      {
        AssertionError assertionError = (AssertionError) list.get(i);

        int n = assertionError.getLine();
        int c = assertionError.getColumn();
        String m = WSIConstants.WSI_PREFIX + "(" + assertionError.getAssertionID()+ ") " + assertionError.getErrorMessage();
      
        ValidationMessage message = new ValidationMessage(m,n, c);
        message.setSeverity(marker);
        messages[i] = message;
      }
      return messages;
    }
	return new ValidationMessage[0];
  }

 /**
  protected IReporter getOrCreateReporter()
  {
    if (reporter == null)
    { 
      reporter = new Reporter();
    }
    return reporter;
  }
  */
  // My Implementation of IReporter
  class Reporter implements IReporter 
  {
	List list = new ArrayList();

	public Reporter() {
		super();
	}

	public IMessageAccess getMessageAccess() {
		return null; // do not need to implement
	}

	public boolean isCancelled() {
		return false; // do not need to implement
	}

	public void removeAllMessages(IValidator origin, Object object) { // do
																		// not
																		// need
																		// to
																		// implement
	}

	public void removeAllMessages(IValidator origin) {// do not need to
														// implement
	}

	public void removeMessageSubset(IValidator validator, Object obj, String groupName) {// do
																							// not
																							// need
																							// to
																							// implement
	}

	public List getMessages() {
		return list;
	}

	public void addMessage(IValidator origin, IMessage message) throws MessageLimitException 
	{
		list.add(message);
	}

	public void displaySubtask(IValidator validator, IMessage message) 
	{
		// TODO Auto-generated method stub
		
	}
  }
}
