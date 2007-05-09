/*******************************************************************************
 * Copyright (c) 2004, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20070305   117034 makandre@ca.ibm.com - Andrew Mak, Web Services Explorer should support SOAP Headers
 * 20070413   176493 makandre@ca.ibm.com - Andrew Mak, WSE: Make message/transport stack pluggable
 *******************************************************************************/
package org.eclipse.wst.ws.internal.explorer.platform.wsdl.actions;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Hashtable;

import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataException;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataParser;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants.FragmentConstants;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants.WSDLActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants.WSDLModelConstants;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.datamodel.WSDLOperationElement;
import org.eclipse.wst.ws.internal.explorer.transport.ISOAPMessage;

public class InvokeWSDLSOAPOperationSourceAction extends InvokeWSDLSOAPOperationAction
{
  private boolean newFileSelected_;
  private boolean saveAsSelected_;
  private boolean isHeader_;
  
  public InvokeWSDLSOAPOperationSourceAction(Controller controller)
  {
    super(controller);
    newFileSelected_ = false;
    saveAsSelected_ = false;
    isHeader_ = false;
  }

  protected boolean processParsedResults(MultipartFormDataParser parser) throws MultipartFormDataException
  {
    super.processParsedResults(parser);
    WSDLOperationElement operElement = (WSDLOperationElement)getSelectedNavigatorNode().getTreeElement();
    newFileSelected_ = false;
    saveAsSelected_ = false;
    isHeader_ = false;
    /*  try and catch is needed if we are doing fragmentization.
    try
    {
    */
      String submissionAction = parser.getParameter(WSDLActionInputs.SUBMISSION_ACTION);
      String sourceContents = parser.getParameter(FragmentConstants.SOURCE_CONTENT_HEADER);
      if (sourceContents != null)
    	operElement.setPropertyAsString(WSDLModelConstants.PROP_SOURCE_CONTENT_HEADER,sourceContents);  
      sourceContents = parser.getParameter(FragmentConstants.SOURCE_CONTENT);
      if (sourceContents != null)
        operElement.setPropertyAsString(WSDLModelConstants.PROP_SOURCE_CONTENT,sourceContents);
      if (WSDLActionInputs.SUBMISSION_ACTION_BROWSE_FILE_HEADER.equals(submissionAction))
      {
    	newFileSelected_ = true;
        String fileContents = parser.getParameter(WSDLActionInputs.SELECTED_FILE_HEADER);
        if (fileContents != null)
          operElement.setPropertyAsString(WSDLModelConstants.PROP_SOURCE_CONTENT_HEADER,fileContents);
      }
      else if (WSDLActionInputs.SUBMISSION_ACTION_BROWSE_FILE.equals(submissionAction))
      {
        newFileSelected_ = true;
        String fileContents = parser.getParameter(WSDLActionInputs.SELECTED_FILE);
        if (fileContents != null)
          operElement.setPropertyAsString(WSDLModelConstants.PROP_SOURCE_CONTENT,fileContents);
      }
      else if (WSDLActionInputs.SUBMISSION_ACTION_SAVE_AS_HEADER.equals(submissionAction))
      {
    	saveAsSelected_ = true;
    	isHeader_ = true;
    	return true;
      }
      else if (WSDLActionInputs.SUBMISSION_ACTION_SAVE_AS.equals(submissionAction))
      {
        // Save As... action
        saveAsSelected_ = true;      
    	return true;
      }
      
      String[] nsDeclarations = parser.getParameterValues(FragmentConstants.SOURCE_CONTENT_NAMESPACE);
      if (nsDeclarations != null)
        operElement.setPropertyAsObject(WSDLModelConstants.PROP_SOURCE_CONTENT_NAMESPACE,nsDeclarations);    	

      return true;
    /*
    }       
    catch (ParserConfigurationException e)
    {
      handleUnexpectedException(wsdlPerspective,messageQueue,"ParserConfigurationException",e);
    }
    catch (SAXException e)
    {
      handleUnexpectedException(wsdlPerspective,messageQueue,"SAXException",e);
    }
    catch (UnsupportedEncodingException e)
    {
      handleUnexpectedException(wsdlPerspective,messageQueue,"UnsupportedEncodingException",e);
    }
    catch (IOException e)
    {
      handleUnexpectedException(wsdlPerspective,messageQueue,"IOException",e);
    }
    return false;
    */
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.wst.ws.internal.explorer.platform.wsdl.actions.InvokeWSDLSOAPOperationAction#getSOAPRequestMessage(org.eclipse.wst.ws.internal.explorer.platform.wsdl.datamodel.WSDLOperationElement)
   */
  protected ISOAPMessage getSOAPRequestMessage(WSDLOperationElement operElement) {	  
	  return (ISOAPMessage) operElement.getPropertyAsObject(WSDLModelConstants.PROP_SOAP_REQUEST_TMP);
  } 

  /* (non-Javadoc)
   * @see org.eclipse.wst.ws.internal.explorer.platform.wsdl.actions.InvokeWSDLSOAPOperationAction#setHeaderContent(java.util.Hashtable, org.eclipse.wst.ws.internal.explorer.platform.wsdl.datamodel.WSDLOperationElement, org.eclipse.wst.ws.internal.explorer.transport.ISOAPMessage)
   */
  protected void setHeaderContent(Hashtable soapEnvelopeNamespaceTable, WSDLOperationElement operElement, ISOAPMessage soapMessage) {	
   	String headerContent = operElement.getPropertyAsString(WSDLModelConstants.PROP_SOURCE_CONTENT_HEADER);
    operElement.getSOAPTransportProvider().newTransport().newDeserializer()
    	.deserialize(ISOAPMessage.HEADER_CONTENT, headerContent, soapMessage);
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.wst.ws.internal.explorer.platform.wsdl.actions.InvokeWSDLSOAPOperationAction#setBodyContent(java.util.Hashtable, org.eclipse.wst.ws.internal.explorer.platform.wsdl.datamodel.WSDLOperationElement, org.eclipse.wst.ws.internal.explorer.transport.ISOAPMessage)
   */ 
  protected void setBodyContent(Hashtable soapEnvelopeNamespaceTable, WSDLOperationElement operElement, ISOAPMessage soapMessage) {
    String bodyContent = operElement.getPropertyAsString(WSDLModelConstants.PROP_SOURCE_CONTENT);    
    operElement.getSOAPTransportProvider().newTransport().newDeserializer()
    	.deserialize(ISOAPMessage.BODY_CONTENT, bodyContent, soapMessage);        
  }

  public final boolean wasNewFileSelected()
  {
    return newFileSelected_;
  }
  
  public final boolean wasSaveAsSelected()
  {
    return saveAsSelected_;
  }
  
  public final String getDefaultSaveAsFileName()
  {
    WSDLOperationElement operElement = (WSDLOperationElement)(getSelectedNavigatorNode().getTreeElement());
    return (new StringBuffer(operElement.getOperation().getName())).append(".txt").toString();
  }
  
  public final void writeSourceContent(OutputStream os)
  {
    WSDLOperationElement operElement = (WSDLOperationElement)(getSelectedNavigatorNode().getTreeElement());
    PrintWriter pw = new PrintWriter(os);
    if (isHeader_)
      pw.println(operElement.getPropertyAsString(WSDLModelConstants.PROP_SOURCE_CONTENT_HEADER));
    else
   	  pw.println(operElement.getPropertyAsString(WSDLModelConstants.PROP_SOURCE_CONTENT));
    pw.close();
  }
}
