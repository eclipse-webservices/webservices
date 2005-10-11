/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.ws.internal.explorer.platform.wsdl.actions;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.Vector;
import javax.xml.parsers.ParserConfigurationException;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataException;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataParser;
import org.eclipse.wst.ws.internal.explorer.platform.util.XMLUtils;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants.FragmentConstants;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants.WSDLActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants.WSDLModelConstants;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.datamodel.WSDLBindingElement;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.datamodel.WSDLElement;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.datamodel.WSDLOperationElement;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.datamodel.WSDLServiceElement;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.util.SoapHelper;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class InvokeWSDLSOAPOperationSourceAction extends InvokeWSDLSOAPOperationAction
{
  private boolean newFileSelected_;
  private boolean saveAsSelected_;
  private static final String DUMMY_WRAPPER_START_TAG = "<dummyWrapper>";
  private static final String DUMMY_WRAPPER_END_TAG = "</dummyWrapper>";
  
  public InvokeWSDLSOAPOperationSourceAction(Controller controller)
  {
    super(controller);
    newFileSelected_ = false;
    saveAsSelected_ = false;
  }

  protected boolean processParsedResults(MultipartFormDataParser parser) throws MultipartFormDataException
  {
    super.processParsedResults(parser);
    WSDLOperationElement operElement = (WSDLOperationElement)getSelectedNavigatorNode().getTreeElement();
    newFileSelected_ = false;
    saveAsSelected_ = false;
    /*  try and catch is needed if we are doing fragmentization.
    try
    {
    */
      String submissionAction = parser.getParameter(WSDLActionInputs.SUBMISSION_ACTION);
      String sourceContents = parser.getParameter(FragmentConstants.SOURCE_CONTENT);
      if (sourceContents != null)
        operElement.setPropertyAsString(WSDLModelConstants.PROP_SOURCE_CONTENT,sourceContents);
      if (WSDLActionInputs.SUBMISSION_ACTION_BROWSE_FILE.equals(submissionAction))
      {
        newFileSelected_ = true;
        String fileContents = parser.getParameter(WSDLActionInputs.SELECTED_FILE);
        if (fileContents != null)
          operElement.setPropertyAsString(WSDLModelConstants.PROP_SOURCE_CONTENT,fileContents);
      }
      else
      {
        if (WSDLActionInputs.SUBMISSION_ACTION_SAVE_AS.equals(submissionAction))
        {
          // Save As... action
          saveAsSelected_ = true;
        }
        else
        {
          // Fragmentize on Go action.
          // fragmentize(new StringBuffer(sourceContents));
          String[] nsDeclarations = parser.getParameterValues(FragmentConstants.SOURCE_CONTENT_NAMESPACE);
          if (nsDeclarations != null)
            operElement.setPropertyAsObject(WSDLModelConstants.PROP_SOURCE_CONTENT_NAMESPACE,nsDeclarations);
        }
      }
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

  /**
   * Generate a Vector of the elements inside the Soap Body.
   * @param soapEnvelopeNamespaceTable - Hashtable containing a map of the namespace URIs to prefixes.
   * @param operElement - WSDLOperationElement encapsulating the WSDL operation.
   */
  protected Vector getBodyEntries(Hashtable soapEnvelopeNamespaceTable,WSDLOperationElement operElement,WSDLBindingElement bindingElement,WSDLServiceElement serviceElement) throws ParserConfigurationException,Exception
  {
    Vector bodyEntries = new Vector();
    String[] nsDeclarations = (String[])operElement.getPropertyAsObject(WSDLModelConstants.PROP_SOURCE_CONTENT_NAMESPACE);
    for (int i = 0; i < nsDeclarations.length; i++)
    {
      String[] prefix_ns = SoapHelper.decodeNamespaceDeclaration(nsDeclarations[i]);
      if (!soapEnvelopeNamespaceTable.contains(prefix_ns[1]))
        soapEnvelopeNamespaceTable.put(prefix_ns[1], prefix_ns[0]);
    }
    StringBuffer sourceContent = new StringBuffer(operElement.getPropertyAsString(WSDLModelConstants.PROP_SOURCE_CONTENT));
    sourceContent.insert(0,DUMMY_WRAPPER_START_TAG).append(DUMMY_WRAPPER_END_TAG);
    Element dummyWrapperElement = XMLUtils.stringToElement(sourceContent.toString());          
    NodeList nl = dummyWrapperElement.getChildNodes();
    for (int i = 0; i < nl.getLength(); i++)
    {
      if (nl.item(i) instanceof Element)
        bodyEntries.add(nl.item(i));
    }
    if (!operElement.isDocumentStyle())
    {
      try
      {
        addRPCWrapper(bodyEntries,(WSDLElement)serviceElement.getParentElement(),operElement,soapEnvelopeNamespaceTable);
      }
      catch (ParserConfigurationException e)
      {
        throw e;
      }
    }
    return bodyEntries;
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
    pw.println(operElement.getPropertyAsString(WSDLModelConstants.PROP_SOURCE_CONTENT));
    pw.close();
  }
}
