/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.ws.internal.explorer.platform.wsdl.actions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLConnection;
import java.util.Iterator;
import javax.wsdl.Part;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.MessageQueue;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Node;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataException;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataParser;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants.WSDLActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.datamodel.WSDLOperationElement;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.IFragment;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.IXSDFragment;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.util.HTTPUtil;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.perspective.InvokeWSDLOperationTool;

public abstract class InvokeWSDLHttpOperationFormAction extends WSDLPropertiesFormAction
{
  private static final String CONTENT_TYPE_CHARSETEQ = "charset=";
  
  public InvokeWSDLHttpOperationFormAction(Controller controller)
  {
    super(controller);
  }
  
  protected boolean processParsedResults(MultipartFormDataParser parser) throws MultipartFormDataException
  {
    super.processParsedResults(parser);
    String endPoint = parser.getParameter(WSDLActionInputs.END_POINT);
    Node selectedNode = getSelectedNavigatorNode();
    InvokeWSDLOperationTool invokeWSDLOperationTool = (InvokeWSDLOperationTool)(selectedNode.getCurrentToolManager().getSelectedTool());
    invokeWSDLOperationTool.setEndPoint(endPoint);
    WSDLOperationElement operElement = (WSDLOperationElement)(selectedNode.getTreeElement());
    propertyTable_.put(WSDLActionInputs.OPERATION_ELEMENT,operElement);
    Iterator it = operElement.getOrderedBodyParts().iterator();
    boolean resultsValid = true;
    while (it.hasNext()) {
      Part part = (Part)it.next();
      IFragment frag = operElement.getFragment(part);
      if (!frag.processParameterValues(parser))
        resultsValid = false;
    }
    return resultsValid;
  }  
  
  protected String getEndPoint()
  {
    StringBuffer endPoint = new StringBuffer((String)propertyTable_.get(WSDLActionInputs.END_POINT));
    WSDLOperationElement operElement = (WSDLOperationElement)propertyTable_.get(WSDLActionInputs.OPERATION_ELEMENT);
    if (endPoint.charAt(endPoint.length()-1) != '/')
      endPoint.append('/');
    endPoint.append(operElement.getName());
    return endPoint.toString();    
  }
  
  protected void addParameters(StringBuffer buffer)
  {
    WSDLOperationElement operElement = (WSDLOperationElement)propertyTable_.get(WSDLActionInputs.OPERATION_ELEMENT);
    Iterator it = operElement.getOrderedBodyParts().iterator();
    while (it.hasNext())
    {
      Part part = (Part)it.next();
      IXSDFragment frag = operElement.getFragment(part);
      buffer.append(HTTPUtil.genURLEncodedParameters(frag));
      if (it.hasNext())
        buffer.append('&');
    }
  }
  
  protected void recordHttpResponse(URLConnection conn,MessageQueue messageQueue) throws IOException
  {
    BufferedReader br = null;
    try
    {
      InputStreamReader in = null;
      String contentType = conn.getContentType();
      if (contentType != null)
      {
        int charsetEqPos = contentType.indexOf(CONTENT_TYPE_CHARSETEQ);
        if (charsetEqPos != -1)
          in = new InputStreamReader(conn.getInputStream(),contentType.substring(charsetEqPos+CONTENT_TYPE_CHARSETEQ.length()));
      }
      if (in == null)
        in = new InputStreamReader(conn.getInputStream());
      br = new BufferedReader(in);
      String s;
      while ((s = br.readLine()) != null)
        messageQueue.addMessage(s);
      br.close();
      br = null;
    }
    catch (IOException e)
    {
      if (br != null)
        br.close();
      throw e;
    }
  }
}
