/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.wsdl.validation.internal.ui.eclipse;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.wst.validation.internal.provisional.core.IMessage;
import org.eclipse.wst.wsdl.validation.internal.IValidationMessage;
import org.eclipse.wst.wsdl.validation.internal.IValidationReport;
import org.eclipse.wst.wsdl.validation.internal.ValidationMessageImpl;
import org.eclipse.wst.wsdl.validation.internal.xml.XMLMessageInfoHelper;
import org.eclipse.wst.xml.validation.internal.core.ValidateAction;

/**
 * Eclipse action for running the WSDL validator. 
 */
public class ValidateWSDLAction extends ValidateAction
{
  private static final String REFERENCED_FILE_ERROR_OPEN = "referencedFileError(";

  private static final String REFERENCED_FILE_ERROR_CLOSE = ")";
  private final String FILE_PROTOCOL = "file:///";

  private static final String _UI_SAVE_DIRTY_FILE_MESSAGE = "_UI_SAVE_DIRTY_FILE_MESSAGE";

  private static final String _UI_SAVE_DIRTY_FILE_TITLE = "_UI_SAVE_DIRTY_FILE_TITLE";
  private static final String _UI_REF_FILE_ERROR_MESSAGE = "_UI_REF_FILE_ERROR_MESSAGE";
  private static final String NESTED_ERRORS = "NESTED_ERRORS";

  private InputStream inputStream = null;
  /**
   * Constructor.
   * 
   * @param file The file to validate.
   * @param showDialog Whether to show a dialog with the status upon completion.
   */
  public ValidateWSDLAction(IFile file, boolean showDialog)
  {
  	super(file, showDialog);
  }


  /**
   * Validate the given file and assign markers correspondingly to the file. If
   * required, show a dialog with the results of the validation.
   * 
   * @param file The file to validate.
   */
  protected void validate(final IFile file)
  {
    final ValidationOutcome validationOutcome = new ValidationOutcome();
    IWorkspaceRunnable op = new IWorkspaceRunnable()
    {
      public void run(IProgressMonitor progressMonitor) throws CoreException
      {
        WSDLValidator wsdlValidator = WSDLValidator.getInstance();
        clearMarkers(file);
        IValidationReport valReport = null;

        String location = null;
        try
        {
          location = file.getLocation().toFile().getCanonicalFile().getAbsolutePath();
        }
        catch (IOException e)
        {
          location = file.getLocation().toString();
        }
        if (location.startsWith("/"))
        {
          valReport = wsdlValidator.validate(location, inputStream);
        }
        else
        {
          valReport = wsdlValidator.validate(FILE_PROTOCOL + location, inputStream);
        }
        validationOutcome.isWSDLValid = valReport.isWSDLValid();
        validationOutcome.isValid = !valReport.hasErrors();
        if (valReport.getValidationMessages().length == 0)
        {
          validationOutcome.hasMessages = false;
        }
        else
        {
          validationOutcome.hasMessages = true;
        }

        createMarkers(file, convertValidationMessages(valReport.getValidationMessages()));
        //createMarkers(file, validatormanager.getWarningList(), WARNING_MARKER);
        
        //file.setSessionProperty(ValidationMessage.ERROR_MESSAGE_MAP_QUALIFIED_NAME, valReport.getNestedMessages());
        file.setSessionProperty(org.eclipse.wst.xml.validation.internal.core.ValidationMessage.ERROR_MESSAGE_MAP_QUALIFIED_NAME, convertNestedValidationMessages(valReport.getNestedMessages()));
      }
    };

    try
    {
      ResourcesPlugin.getWorkspace().run(op, null);

      if (showDialog)
      {
        showDialog(validationOutcome);
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
  
  /**
   * Convert WSDL validation messages to validation framework messages.
   * 
   * @param messages The WSDL validation messages to convert.
   * @return The converted validation messages.
   */
  private org.eclipse.wst.xml.validation.internal.core.ValidationMessage[] convertValidationMessages(IValidationMessage[] messages)
  {
  	int numMessages = messages.length;
  	org.eclipse.wst.xml.validation.internal.core.ValidationMessage[] convertedMessages = new org.eclipse.wst.xml.validation.internal.core.ValidationMessage[numMessages];
  	
  	for(int i = 0; i < numMessages; i++)
  	{
  	  IValidationMessage mess = messages[i];
      
      org.eclipse.wst.xml.validation.internal.core.ValidationMessage convertMess = null;
      if (mess instanceof ValidationMessageImpl)
      {   String errorKey = ((ValidationMessageImpl)mess).getErrorKey();
          Object[] messageArgs = ((ValidationMessageImpl)mess).getMessageArguments();
          convertMess = new org.eclipse.wst.xml.validation.internal.core.ValidationMessage(mess.getMessage(), mess.getLine(), mess.getColumn(), mess.getURI(), errorKey, messageArgs);
      }
      else
      {
         convertMess = new org.eclipse.wst.xml.validation.internal.core.ValidationMessage(mess.getMessage(),mess.getLine(),mess.getColumn(), mess.getURI());
      }
  	  if(mess.getSeverity() == IValidationMessage.SEV_WARNING)
	  {
	  	convertMess.setSeverity(org.eclipse.wst.xml.validation.internal.core.ValidationMessage.SEV_LOW);
	  }
	  else
	  {
	  	convertMess.setSeverity(org.eclipse.wst.xml.validation.internal.core.ValidationMessage.SEV_HIGH);
	  }
  	  
  	  // Convert any nested messages.
  	  List nestedMessages = mess.getNestedMessages();
  	  if(nestedMessages != null && !nestedMessages.isEmpty())
  	  {
  	  	Iterator nestedIter = nestedMessages.iterator();
  	    while(nestedIter.hasNext())
        {
          IValidationMessage nestedMess = (IValidationMessage)nestedIter.next();
    	  org.eclipse.wst.xml.validation.internal.core.ValidationMessage convertNestedMess = new org.eclipse.wst.xml.validation.internal.core.ValidationMessage(nestedMess.getMessage(),nestedMess.getLine(),nestedMess.getColumn(), nestedMess.getURI());
    	  if(nestedMess.getSeverity() == IValidationMessage.SEV_WARNING)
    	  {
    	  	convertNestedMess.setSeverity(org.eclipse.wst.xml.validation.internal.core.ValidationMessage.SEV_LOW);
    	  }
    	  else
    	  {
    	  	convertNestedMess.setSeverity(org.eclipse.wst.xml.validation.internal.core.ValidationMessage.SEV_HIGH);
    	  }
    	  convertMess.addNestedMessage(convertNestedMess);
        }
  	  }
  	  convertedMessages[i] = convertMess;
  	}
  	return convertedMessages;
  }
  
  /**
   * Convert the nested messages hashmap.
   * 
   * @param nestedMessages The nested messages hashmap to convert.
   * @return A hashmap with the converted validation messages.
   */
  private HashMap convertNestedValidationMessages(HashMap nestedMessages)
  {
  	HashMap convertedMap = new HashMap();
  	
  	Set keySet = nestedMessages.keySet();
  	Iterator keysIter = keySet.iterator();
  	while(keysIter.hasNext())
  	{
  	  String key = (String)keysIter.next();
  	  IValidationMessage message = (IValidationMessage)nestedMessages.get(key);
  	  org.eclipse.wst.xml.validation.internal.core.ValidationMessage[] convertedMessage = convertValidationMessages(new IValidationMessage[]{message});
  	  
  	  convertedMap.put(key, convertedMessage[0]);
  	}
  	return convertedMap;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jface.action.IAction#run()
   */
//  public void run()
//  {
//    // Only run the validation if the file exists and is available.
//    if (fileIsOK(file))
//    {
//      if (showDialog)
//      {
//        // Check if the file is dirty - prompts user to save.
//        checkIfFileDirty(file);
//      }
//      super.run();
//    }
//  }

  /**
   * Test whether the file given is OK to use. A file is OK to use if 1. It is
   * not null 2. It exists. 3. The project containing the file is accessible.
   * 
   * @param file The file to check to see if it is OK to validate.
   * @return True if the file is OK to validate, false otherwise.
   */
//  protected boolean fileIsOK(IFile file)
//  {
//    if (file != null && file.exists() && file.getProject().isAccessible())
//    {
//      return true;
//    }
//    return false;
//  }

  /**
   * Check if the file is dirty. A file is dirty if there is an open editor for
   * the file that contains changes that haven't been saved.
   * 
   * @param file The file to check to see if it is dirty.
   */
//  protected void checkIfFileDirty(IFile file)
//  {
//    IEditorPart[] dirtyEditors = ValidateWSDLPlugin.getInstance().getWorkbench().getActiveWorkbenchWindow().getActivePage().getDirtyEditors();
//
//    int numeditors = dirtyEditors.length;
//    for (int i = 0; i < numeditors; i++)
//    {
//      IEditorInput editorInput = dirtyEditors[i].getEditorInput();
//      if (editorInput instanceof FileEditorInput)
//      {
//        FileEditorInput fileEditorInput = (FileEditorInput) editorInput;
//        if (fileEditorInput.getFile().equals(file))
//        {
//          String message = ValidateWSDLPlugin.getInstance().getString(_UI_SAVE_DIRTY_FILE_MESSAGE);
//          String title = ValidateWSDLPlugin.getInstance().getString(_UI_SAVE_DIRTY_FILE_TITLE);
//          if (MessageDialog.openQuestion(Display.getDefault().getActiveShell(), title, message))
//          {
//            dirtyEditors[i].doSave(null);
//          }
//          // There can only be one open editor/file so we can break.
//          break;
//        }
//      }
//
//    }
//  }

  /**
   * Create a marker on the file of the indicated type for each
   * ValidationMessage in the array.
   * 
   * @param iFile
   *          The file to add the markers to.
   * @param valMessages
   *          The array of messages to add as markers.
   */
//  public void createMarkers(IFile iFile, ValidationMessage[] valMessages)
//  {
//    if (!fileIsOK(iFile))
//    {
//      return;
//    }
//    int numValMessages = valMessages.length;
//    for (int i = 0; i < numValMessages; i++)
//    {
//      ValidationMessage validationMessage = valMessages[i];
//
//      int line = validationMessage.getLine();
//      int column = validationMessage.getColumn();
//      String message = validationMessage.getMessage();
//      int severity = validationMessage.getSeverity();
//      IMarker marker = null;
//      
//      boolean hasNestedErrors = false;
//      List nestederrors = validationMessage.getNestedMessages();
//      if(nestederrors != null && !nestederrors.isEmpty())
//      {
//        hasNestedErrors = true;
//        message += ValidateWSDLPlugin.getInstance().getString(_UI_REF_FILE_ERROR_MESSAGE); 
//      }
//      if (severity == ValidationMessage.SEV_ERROR)
//      {
//        marker = getOrCreateReporter().addErrorMessage(iFile, message, line, column);
//      }
//      else if (severity == ValidationMessage.SEV_WARNING)
//      {
//        marker = getOrCreateReporter().addWarningMessage(iFile, message, line, column);
//      }
//      
//      if (hasNestedErrors && marker != null)
//      {
//        try
//        {
//          marker.setAttribute("groupName", REFERENCED_FILE_ERROR_OPEN + validationMessage.getURI() + REFERENCED_FILE_ERROR_CLOSE);
//          marker.setAttribute(IMarker.DONE, true);
//        }
//        catch(CoreException e)
//        {
//        }
//      }
//      
//    }
//  }

  /**
   * Show a dialog containing the status of the validation. Will display valid
   * or invalid depending on whether there are errors or not.
   * 
   * @param validationOutcome
   *          The outcome of the validation.
   */
  public void showDialog(ValidationOutcome validationOutcome)
  {
    String title, message;
    if (validationOutcome.isValid)
    {
      if (validationOutcome.hasMessages)
      {
        showProblemsView();
        title = ValidateWSDLPlugin.getInstance().getString("_VALIDATION_SUCCEEDED");
        message = ValidateWSDLPlugin.getInstance().getString("_UI_THE_WSDL_FILE_IS_VALID_WITH_WARNINGS");
        MessageDialog.openWarning(Display.getDefault().getActiveShell(), title, message);
      }
      else
      {
        title = ValidateWSDLPlugin.getInstance().getString("_VALIDATION_SUCCEEDED");
        message = ValidateWSDLPlugin.getInstance().getString("_UI_THE_WSDL_FILE_IS_VALID");
        MessageDialog.openInformation(Display.getDefault().getActiveShell(), title, message);
      }

    }
    else
    {
      if (validationOutcome.isWSDLValid)
      {
        title = ValidateWSDLPlugin.getInstance().getString("_VALIDATION_FAILED");
        message = ValidateWSDLPlugin.getInstance().getString("_UI_THE_WSDL_FILE_IS_VALID_WSDL11");
      }
      else
      {
        title = ValidateWSDLPlugin.getInstance().getString("_VALIDATION_FAILED");
        message = ValidateWSDLPlugin.getInstance().getString("_UI_THE_WSDL_FILE_IS_NOT_VALID");
      }
      showProblemsView();
      MessageDialog.openError(Display.getDefault().getActiveShell(), title, message);
    }

  }
  
  /**
   * Show the problems view if it is not already visible.
   */
//  protected void showProblemsView()
//  {
//    IWorkbenchWindow dw = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
//    IWorkbenchPage page = dw.getActivePage();
//    IWorkbenchPart activePart = page.getActivePart();
//    try
//    {
//      if (page != null)
//      {
//        page.showView("org.eclipse.ui.views.ProblemView");
//      }
//    }
//    catch (PartInitException e)
//    {
//    }
//    page.activate(activePart);
//  }

  /**
   * If a reporter doesn't exist creates it or uses the reporter already
   * created.
   * 
   * @return a reporter
   */
//  protected IReporter getOrCreateReporter()
//  {
//    if (reporter == null)
//    {
//      reporter = new WorkbenchReporter(file.getProject(), new NullProgressMonitor());
//    }
//    return reporter;
//  }

  /**
   * Clear all the markers on the given resource generated by this validator.
   * 
   * @param resource -
   *          the resrouce to clear all the markers from
   */
//  public void clearMarkers(IResource resource)
//  {
//    getOrCreateReporter().removeAllMessages(resource);
//  }

  protected class ValidationOutcome
  {
    public boolean isValid = true;

    public boolean isWSDLValid = true;

    public boolean hasMessages = false;

    public ValidationOutcome()
    {
    }
  }

  public void setInputStream(InputStream inputStream)
  {  this.inputStream = inputStream;
  }
  
  /**
   * Set extra attributes in the IMessage to provide information on what should be "squiggled"
   * @param valMess the ValidationMessage corresponding to this error
   * @param message the IMessage to set the attributes for
   */
  protected void addInfoToMessage(org.eclipse.wst.xml.validation.internal.core.ValidationMessage valMess, IMessage message)
  {   if (valMess.getKey() != null)
      {
          XMLMessageInfoHelper helper = new XMLMessageInfoHelper();
          String[] squiggleInfo = helper.createMessageInfo(valMess.getKey(), valMess.getMessageArguments());
      
          message.setAttribute(COLUMN_NUMBER_ATTRIBUTE, new Integer(valMess.getColumnNumber()));
          message.setAttribute(SQUIGGLE_SELECTION_STRATEGY_ATTRIBUTE, squiggleInfo[0]);
          message.setAttribute(SQUIGGLE_NAME_OR_VALUE_ATTRIBUTE, squiggleInfo[1]);
      }
  }

}