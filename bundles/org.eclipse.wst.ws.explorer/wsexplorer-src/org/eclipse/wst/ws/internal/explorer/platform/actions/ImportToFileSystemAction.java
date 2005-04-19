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
package org.eclipse.wst.ws.internal.explorer.platform.actions;

import org.eclipse.wst.ws.internal.explorer.platform.constants.*;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.*;
import org.eclipse.wst.wsdl.internal.impl.wsdl4j.WSDLFactoryImpl;
import javax.wsdl.Definition;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLWriter;
import java.io.OutputStreamWriter;
import java.io.OutputStream;
import javax.servlet.http.*;
import org.apache.wsil.WSILDocument;

public abstract class ImportToFileSystemAction extends LinkAction
{
  public ImportToFileSystemAction(Controller controller)
  {
    super(controller);
  }

  // os is the OutputStream of the file in the file system
  public abstract boolean write(OutputStream os);

  // the default name for the file
  public abstract String getDefaultFileName();

  public boolean writeWSDLDefinition(OutputStream os, Definition definition)
  {
    try
    {
      WSDLFactory wsdlFactory = new WSDLFactoryImpl();
      WSDLWriter wsdlWriter = wsdlFactory.newWSDLWriter();
      wsdlWriter.writeWSDL(definition, os);
      return true;
    }
    catch (Throwable t)
    {
      return false;
    }
  }

  public boolean writeWSILDocument(OutputStream os, WSILDocument wsilDoc)
  {
    OutputStreamWriter osw = null;
    try
    {
      osw = new OutputStreamWriter(os);
      wsilDoc.write(osw);
      return true;
    }
    catch (Exception e)
    {
      return false;
    }
    finally
    {
      try
      {
        if (osw != null)
          osw.close();
      }
      catch (Exception e)
      {
      }
    }
  }

  public static String getActionLink(int nodeID, int toolID, int viewID, int viewToolID, String jspPage)
  {
    StringBuffer actionLink = new StringBuffer(jspPage);
    actionLink.append('?');
    actionLink.append(ActionInputs.NODEID);
    actionLink.append('=');
    actionLink.append(nodeID);
    actionLink.append('&');
    actionLink.append(ActionInputs.TOOLID);
    actionLink.append('=');
    actionLink.append(toolID);
    actionLink.append('&');
    actionLink.append(ActionInputs.VIEWID);
    actionLink.append('=');
    actionLink.append(viewID);
    actionLink.append('&');
    actionLink.append(ActionInputs.VIEWTOOLID);
    actionLink.append('=');
    actionLink.append(viewToolID);
    return actionLink.toString();
  }

  protected boolean processLinkParameters(HttpServletRequest request)
  {
    String nodeID = request.getParameter(ActionInputs.NODEID);
    String toolID = request.getParameter(ActionInputs.TOOLID);
    String viewID = request.getParameter(ActionInputs.VIEWID);
    String viewToolID = request.getParameter(ActionInputs.VIEWTOOLID);
    try
    {
      Integer.parseInt(nodeID);
      Integer.parseInt(toolID);
      Integer.parseInt(viewID);
      Integer.parseInt(viewToolID);
      propertyTable_.put(ActionInputs.NODEID, nodeID);
      propertyTable_.put(ActionInputs.TOOLID, toolID);
      propertyTable_.put(ActionInputs.VIEWID, viewID);
      propertyTable_.put(ActionInputs.VIEWTOOLID, viewToolID);
    }
    catch (Exception e)
    {
      return false;
    }
    return true;
  }

  public abstract String getStatusContentVar();

  public abstract String getStatusContentPage();
}