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

package org.eclipse.wst.ws.internal.explorer.platform.uddi.actions;

import java.io.File;
import java.io.FileOutputStream;
import javax.wsdl.Definition;
import org.eclipse.wst.ws.internal.explorer.platform.actions.LaunchWebServiceWizardAction;
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.TreeElement;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.FormTool;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.MessageQueue;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Node;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.NodeManager;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.ServiceElement;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.ServiceInterfaceElement;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective.UDDIPerspective;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.util.Uddi4jHelper;
import org.uddi4j.datatype.service.BusinessService;
import org.uddi4j.datatype.tmodel.TModel;

public class UDDILaunchWebServiceWizardAction extends LaunchWebServiceWizardAction
{
  public UDDILaunchWebServiceWizardAction(Controller controller)
  {
    super(controller);
  }

  public FormTool getSelectedFormTool()
  {
    UDDIPerspective uddiPerspective = controller_.getUDDIPerspective();
    return (FormTool)uddiPerspective.getNavigatorManager().getSelectedNode().getCurrentToolManager().getSelectedTool();
  }

  public boolean run()
  {
    UDDIPerspective uddiPerspective = controller_.getUDDIPerspective();
    MessageQueue messageQueue = uddiPerspective.getMessageQueue();
    NodeManager nodeManager = uddiPerspective.getNavigatorManager();
    Node node = nodeManager.getSelectedNode();
    TreeElement element = node.getTreeElement();
    Uddi4jHelper uddi4jHelper = new Uddi4jHelper();
    String wsdlUrl = null;
    if (element instanceof ServiceElement)
    {
      ServiceElement serviceElement = (ServiceElement)element;
      BusinessService bs = serviceElement.getBusinessService();
      wsdlUrl = uddi4jHelper.getWSDL(bs, serviceElement.getRegistryElement().getProxy());
    }
    else if (element instanceof ServiceInterfaceElement)
    {
      TModel tModel = ((ServiceInterfaceElement)element).getTModel();
      wsdlUrl = uddi4jHelper.getWSDL(tModel);
    }

    try
    {
      if (wsdlUrl != null)
      {
        Definition def = uddi4jHelper.getWSDLDefinition(wsdlUrl);
        File tempFile = File.createTempFile("temp",".wsdl");
        UDDIImportWSDLToFileSystemAction action = new UDDIImportWSDLToFileSystemAction(controller_);
        action.writeWSDLDefinition(new FileOutputStream(tempFile),def);
        wsdlUrl = tempFile.toURL().toString();
        return launchWizard(wsdlUrl);
      }
    }
    catch (Throwable t)
    {
      messageQueue.addMessage(controller_.getMessage("MSG_ERROR_RETRIEVING_WSDL_DOC"));
    }
    return false;
  }

  public final String getStatusContentVar()
  {
    return controller_.getUDDIPerspective().getStatusContentVar();
  }

  public final String getStatusContentPage()
  {
    return controller_.getUDDIPerspective().getStatusContentPage();
  }
}
