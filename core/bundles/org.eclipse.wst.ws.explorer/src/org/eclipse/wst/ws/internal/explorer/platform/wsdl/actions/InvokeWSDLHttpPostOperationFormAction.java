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
import java.io.PrintWriter;
import java.net.URLConnection;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.MessageQueue;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.perspective.WSDLPerspective;
import org.eclipse.wst.ws.internal.parser.discovery.NetUtils;

public class InvokeWSDLHttpPostOperationFormAction extends InvokeWSDLHttpOperationFormAction
{

  public InvokeWSDLHttpPostOperationFormAction(Controller controller)
  {
    super(controller);
  }

  public boolean run()
  {
    WSDLPerspective wsdlPerspective = controller_.getWSDLPerspective();
    MessageQueue messageQueue = wsdlPerspective.getMessageQueue();
    boolean result = false;
    String endPointString = getEndPoint();
    URLConnection conn = NetUtils.getURLConnection(endPointString);
    if (conn != null)
    {
      StringBuffer parameters = new StringBuffer();
      addParameters(parameters);
      conn.setDoOutput(true);
      PrintWriter out = null;
      try
      {
        out = new PrintWriter(conn.getOutputStream());
        out.print(parameters);
        out.close();
        out = null;
        recordHttpResponse(conn,messageQueue);
        wsdlPerspective.setOperationNode(getSelectedNavigatorNode());
        result = true;
      }
      catch (IOException e)
      {
        handleUnexpectedException(wsdlPerspective,messageQueue,"IOException",e);
      }
      finally
      {
        if (out != null)
          out.close();
      }
    }
    else
      handleUnexpectedException(wsdlPerspective,messageQueue,"Exception",new IOException(wsdlPerspective.getMessage("MSG_ERROR_UNABLE_TO_CONNECT",endPointString)));
    return result;
  }
}
