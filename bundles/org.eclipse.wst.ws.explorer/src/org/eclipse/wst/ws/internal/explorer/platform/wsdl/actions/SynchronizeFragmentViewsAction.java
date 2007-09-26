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

import java.util.Iterator;
import java.util.Map;

import javax.wsdl.Part;

import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Node;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataException;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataParser;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants.FragmentConstants;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants.WSDLModelConstants;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.datamodel.WSDLOperationElement;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.IXSDFragment;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.impl.SOAPHeaderWrapperFragment;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.perspective.InvokeWSDLOperationTool;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.util.SOAPMessageUtils;
import org.eclipse.wst.ws.internal.explorer.transport.ISOAPMessage;
import org.eclipse.wst.wsdl.binding.soap.SOAPHeader;
import org.w3c.dom.Element;

public class SynchronizeFragmentViewsAction extends WSDLPropertiesFormAction
{
  public SynchronizeFragmentViewsAction(Controller controller)
  {
    super(controller);
  }

  protected boolean processParsedResults(MultipartFormDataParser parser) throws MultipartFormDataException
  {
    super.processParsedResults(parser);
    Node selectedNode = getSelectedNavigatorNode();
    WSDLOperationElement operElement = (WSDLOperationElement)selectedNode.getTreeElement();
    
    InvokeWSDLOperationTool invokeWSDLOperationTool = (InvokeWSDLOperationTool)(selectedNode.getCurrentToolManager().getSelectedTool());
    String viewID = parser.getParameter(FragmentConstants.FRAGMENT_VIEW_ID);
    propertyTable_.put(FragmentConstants.FRAGMENT_VIEW_ID, viewID);
    if (viewID.equals(FragmentConstants.FRAGMENT_VIEW_SWITCH_FORM_TO_SOURCE))
    {
      invokeWSDLOperationTool.setFragmentViewID(FragmentConstants.FRAGMENT_VIEW_SWITCH_FORM_TO_SOURCE);
      operElement.setPropertyAsObject(WSDLModelConstants.PROP_SOURCE_CONTENT_NAMESPACE, null);
      
      return processFormViewParsedResultsHeader(parser, operElement) &		// need to process both header and body 
             processFormViewParsedResults(parser, operElement);
    }
    else
    {
      invokeWSDLOperationTool.setFragmentViewID(FragmentConstants.FRAGMENT_VIEW_SWITCH_SOURCE_TO_FORM);
      String[] nsDeclarations = parser.getParameterValues(FragmentConstants.SOURCE_CONTENT_NAMESPACE);    
      if (nsDeclarations != null)
        operElement.setPropertyAsObject(WSDLModelConstants.PROP_SOURCE_CONTENT_NAMESPACE,nsDeclarations);
      
      ISOAPMessage soapMessage = (ISOAPMessage) operElement.getPropertyAsObject(WSDLModelConstants.PROP_SOAP_REQUEST_TMP);
      boolean rc = processSourceViewParsedResultsHeader(parser, operElement, soapMessage) &	// need to process both header and body
		 		   processSourceViewParsedResults(parser, operElement, soapMessage); 
      operElement.setPropertyAsObject(WSDLModelConstants.PROP_SOAP_REQUEST_TMP, null);
      
      return rc;
    }
  }

  private boolean processFormViewParsedResultsHeader(MultipartFormDataParser parser, WSDLOperationElement operElement) throws MultipartFormDataException
  { 
	operElement.setPropertyAsObject(WSDLModelConstants.PROP_SOURCE_CONTENT_HEADER, null);	
	boolean resultsValid = true;
	    
	Iterator it = operElement.getSOAPHeaders().iterator();
	while (it.hasNext())
	{
	  SOAPHeader soapHeader = (SOAPHeader) it.next();        
	  IXSDFragment frag = operElement.getHeaderFragment(soapHeader);
	  if (!frag.processParameterValues(parser))
        resultsValid = false;
	}
	if (resultsValid)
	  operElement.setPropertyAsString(WSDLModelConstants.PROP_SOURCE_CONTENT_HEADER,null);
    return resultsValid;
  }
  
  private boolean processFormViewParsedResults(MultipartFormDataParser parser, WSDLOperationElement operElement) throws MultipartFormDataException
  {
    operElement.setPropertyAsObject(WSDLModelConstants.PROP_SOURCE_CONTENT, null);
    boolean resultsValid = true;
	
    Iterator it = operElement.getOrderedBodyParts().iterator();    
    while (it.hasNext())
    {
      Part part = (Part)it.next();
      IXSDFragment frag = operElement.getFragment(part);
      if (!frag.processParameterValues(parser))
        resultsValid = false;
    }
    if (resultsValid)
      operElement.setPropertyAsString(WSDLModelConstants.PROP_SOURCE_CONTENT,null);
    return resultsValid;
  }
  
  private boolean processSourceViewParsedResultsHeader(MultipartFormDataParser parser, WSDLOperationElement operElement, ISOAPMessage soapMessage) 
  	throws MultipartFormDataException {
	  
    String sourceContent = parser.getParameter(FragmentConstants.SOURCE_CONTENT_HEADER);
    if (sourceContent != null)
      operElement.setPropertyAsString(WSDLModelConstants.PROP_SOURCE_CONTENT_HEADER, sourceContent);    
           
    Iterator it = operElement.getSOAPHeaders().iterator();
    if (!it.hasNext())
      return true;
    
    try
    {
      operElement.getSOAPTransportProvider().newTransport().newDeserializer()
      	.deserialize(ISOAPMessage.HEADER_CONTENT, sourceContent, soapMessage);  
    	
      Element[] instanceDocuments = soapMessage.getHeaderContent();
      Map namespaceTable = soapMessage.getNamespaceTable();
      
      boolean sourceElementsValid = true;
      int start = 0;
      while (it.hasNext() && start < instanceDocuments.length)
      {
        SOAPHeader soapHeader = (SOAPHeader)it.next();
        SOAPHeaderWrapperFragment frag = (SOAPHeaderWrapperFragment) operElement.getHeaderFragment(soapHeader);
        
        int pos = SOAPMessageUtils.findFirstMatchingElement(
        		soapHeader.getEPart(),
				instanceDocuments,
				namespaceTable,
				frag.getName(),
				start);
			
		if (pos == -1)
			continue;
			
		Element element = instanceDocuments[pos];
		start = pos + 1;			
        
        if (!frag.setParameterValuesFromInstanceDocument(element, namespaceTable))
          sourceElementsValid = false;
      }
      return sourceElementsValid;
    }
    catch (Throwable t)
    {
      return false;
    }    
  }
  
  private boolean processSourceViewParsedResults(MultipartFormDataParser parser, WSDLOperationElement operElement, ISOAPMessage soapMessage)
  	throws MultipartFormDataException {
	  
    String sourceContent = parser.getParameter(FragmentConstants.SOURCE_CONTENT);
    if (sourceContent != null)
      operElement.setPropertyAsString(WSDLModelConstants.PROP_SOURCE_CONTENT, sourceContent);
    
    Iterator it = operElement.getOrderedBodyParts().iterator();    
    
    try
    {
      operElement.getSOAPTransportProvider().newTransport().newDeserializer()
      	.deserialize(ISOAPMessage.BODY_CONTENT, sourceContent, soapMessage);      	
    	
      Element[] instanceDocuments = soapMessage.getBodyContent();
      
      boolean sourceElementsValid = true;
      while (it.hasNext())
      {
        Part part = (Part)it.next();
        IXSDFragment frag = operElement.getFragment(part);
        if (!frag.setParameterValuesFromInstanceDocuments(instanceDocuments))
          sourceElementsValid = false;
      }
      return sourceElementsValid;
    }
    catch (Throwable t)
    {
      return false;
    }
  }

  public boolean run() {
    return true;
  }
}