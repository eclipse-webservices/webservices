/*******************************************************************************
 * Copyright (c) 2002, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.ws.internal.explorer.platform.wsdl.actions;

import java.io.IOException;
import java.net.URLConnection;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.MessageQueue;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.perspective.WSDLPerspective;
import org.eclipse.wst.ws.internal.parser.discovery.NetUtils;

public class InvokeWSDLHttpGetOperationFormAction extends InvokeWSDLHttpOperationFormAction
{
  public InvokeWSDLHttpGetOperationFormAction(Controller controller)
  {
    super(controller);
  }
    
  public boolean run()
  {
    WSDLPerspective wsdlPerspective = controller_.getWSDLPerspective();
    MessageQueue messageQueue = wsdlPerspective.getMessageQueue();
    StringBuffer endPoint = new StringBuffer(getEndPoint());
    endPoint.append('?');
    addParameters(endPoint);
    String endPointString = endPoint.toString();
    boolean result = false;
    try
    {
      URLConnection conn = NetUtils.getURLConnection(endPointString);
      if (conn != null)
      {
        recordHttpResponse(conn,messageQueue);
        wsdlPerspective.setOperationNode(getSelectedNavigatorNode());
        result = true;
      }
      else
        throw new IOException(wsdlPerspective.getMessage("MSG_ERROR_UNABLE_TO_CONNECT",endPointString));
    }
    catch (IOException e)
    {
      handleUnexpectedException(wsdlPerspective,messageQueue,"IOException",e);
    }
    return result;
  }
}
