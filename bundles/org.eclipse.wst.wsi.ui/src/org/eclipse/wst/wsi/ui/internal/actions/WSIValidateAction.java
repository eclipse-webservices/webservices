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

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.wst.validation.internal.provisional.core.IValidator;
import org.eclipse.wst.wsi.internal.analyzer.MessageAnalyzer;
import org.eclipse.wst.wsi.internal.analyzer.WSIAnalyzerException;
import org.eclipse.wst.wsi.ui.internal.WSIUIPlugin;
import org.eclipse.wst.wsi.internal.report.AssertionError;
import org.eclipse.wst.xml.validation.internal.core.ValidateAction;
import org.eclipse.wst.xml.validation.internal.core.ValidationMessage;
import org.xml.sax.SAXParseException;

/**
 * Action for running the validator.
 * 
 * @author David Lauzon, IBM
 * @author Lawrence Mandel, IBM
 */
public class WSIValidateAction extends ValidateAction
{
  protected static final String FILE_PROTOCOL = "file:";
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
   * @param wsdlfile The WSDL file to use for validation.
   * @param elementname The name of the WSDL element to validate.
   * @param namespace The namespace of the WSDL element to validate
   * @param parentname The parent name of the WSDL element to validate.
   * @param type The type of element to validate.
   */
  public WSIValidateAction(IFile f, boolean showDialog, String wsdlfile, String elementname, String namespace, String parentname, String type)
  {
  	this(f, showDialog);
  	this.wsdlfile = wsdlfile;
  	this.elementname = elementname;
  	this.namespace = namespace;
  	this.parentname = parentname;
  	this.type = type;
  	this.wsdlspecified = true;
  }

  /* (non-Javadoc)
   * @see org.eclipse.validate.ValidateAction#validate(org.eclipse.core.resources.IFile)
   */
  protected void validate(final IFile file)
  {
  	final MessageAnalyzer messageanalyzer;
  	
  	if(wsdlspecified)
  	{
  	  messageanalyzer = new MessageAnalyzer(FILE_PROTOCOL + file.getLocation().toOSString(), wsdlfile, elementname, namespace, parentname, type);	
  	}
  	else
  	{	
  	  messageanalyzer = new MessageAnalyzer(FILE_PROTOCOL + file.getLocation().toOSString());
  	}
  	
  	IWorkspaceRunnable op = new IWorkspaceRunnable() 
    {
      public void run(IProgressMonitor progressMonitor) throws CoreException 
      {        
        clearMarkers(file);
        try
        {
          messageanalyzer.validateConformance();
        }
        catch (WSIAnalyzerException ae)
        {
        	if (ae.getTargetException() instanceof SAXParseException)
        	{
        	  exceptionCaught = true;
        	  createMarkers(file, new ValidationMessage[]{createValidationMessageForException((SAXParseException)ae.getTargetException(), IMarker.SEVERITY_ERROR)});
        	}
        	else
        	{
        		createMarkers(file, new ValidationMessage[]{createValidationMessageForException(ae, IMarker.SEVERITY_ERROR)});
        	}
        }
        catch (Exception e)
        {
        }

        createMarkers(file, convertValidationMessages(messageanalyzer.getAssertionWarnings(), IMarker.SEVERITY_WARNING));
        createMarkers(file, convertValidationMessages(messageanalyzer.getAssertionErrors(), IMarker.SEVERITY_ERROR));

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
        String m = assertionError.getErrorMessage();
      
        ValidationMessage message = new ValidationMessage(m,n, c);
        message.setSeverity(marker);
        messages[i] = message;
      }
      return messages;
    }
	return new ValidationMessage[0];
  }
}
