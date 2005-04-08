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

//import java.util.Iterator;
//import java.util.List;
import org.eclipse.core.resources.IFile;
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
import org.eclipse.wst.xml.validation.internal.core.ValidateAction;
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
//        	  createMarker(file, ae, ERROR_MARKER);
        	}
        }
        catch (Exception e)
        {
        }
//        createMarkers(file, messageanalyzer.getAssertionWarnings(), WARNING_MARKER);
//        createMarkers(file, messageanalyzer.getAssertionErrors(), ERROR_MARKER);
//
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
   * Create a marker for the specified resource with the given severity.
   * @param resource the resource that 
   * @param ae the WS-I Analyzer exception
   * @param error_marker flag indicating severity of problem
   */
//  protected void createMarker(IResource resource, WSIAnalyzerException ae, int error_marker) 
//  {
// 	Throwable throwable = ae.getTargetException();
//
//	if (throwable instanceof SAXParseException)
//	{
//		int n = ((SAXParseException)throwable).getLineNumber();
//		int c = ((SAXParseException)throwable).getColumnNumber();
//		
//		getOrCreateReporter().addErrorMessage(resource, ((SAXParseException)throwable).getMessage(), n, c);
//	}
//	else
//	{
//		getOrCreateReporter().addErrorMessage(resource, ae.getMessage(), 0, 0);
//	}
//  }

  /* (non-Javadoc)
   * @see org.eclipse.validate.ValidateAction#createMarkers(org.eclipse.core.resources.IResource, java.util.List, int)
   */
//  public void createMarkers(IResource resource, List list, int marker)
//  { 
//    for (Iterator i = list.iterator(); i.hasNext(); )
//    {
//      AssertionError assertionError = (AssertionError) i.next();
//
//      int n = assertionError.getLine();
//      int c = assertionError.getColumn();
//      
//      if(marker == WARNING_MARKER)
//      {
//        getOrCreateReporter().addWarningMessage(resource, assertionError.getErrorMessage(), n, c);
//      }
//      else if (marker == ERROR_MARKER)
//      {
//        getOrCreateReporter().addErrorMessage(resource, assertionError.getErrorMessage(), n, c);
//      }
//    }
//  }
  
  /**
   * Clear all the markers on the given resource generated by this validator.
   * 
   * @param resource The resource with the markers to clear.
   */
//  public void clearMarkers(IResource resource)
//  {
//    getOrCreateReporter().removeAllMessages(resource);
//  }

 /**
  * If a reporter doesn't exist creates it or uses the reporter already created.
  * 
  * @return The reporter.
  */
//  protected IReporter getOrCreateReporter()
//  {
//    if (reporter == null)
//    {
//      reporter = ReporterRegister.getInstance().getReporter(WSIMessageValidator.WSI_MESSAGE_VALIDATOR_ID);
//    }
//    return reporter;
//  }

 /**
  * Gets the validator.
  * 
  * @return Returns a IValidator.
  */
  public IValidator getValidator()
  {
    return validator;
  }

 /**
  * Sets the validator.
  * 
  * @param validator The validator to set.
  */
  public void setValidator(IValidator validator)
  {
    this.validator = validator;
  }
}
