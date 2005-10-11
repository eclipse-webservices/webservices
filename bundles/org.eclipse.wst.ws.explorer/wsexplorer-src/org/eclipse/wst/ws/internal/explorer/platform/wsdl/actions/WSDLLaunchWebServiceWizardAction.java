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

package org.eclipse.wst.ws.internal.explorer.platform.wsdl.actions;

import org.eclipse.wst.ws.internal.explorer.platform.actions.LaunchWebServiceWizardAction;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.FormTool;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Node;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.NodeManager;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.datamodel.WSDLElement;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.perspective.WSDLPerspective;

public class WSDLLaunchWebServiceWizardAction extends LaunchWebServiceWizardAction {

  public WSDLLaunchWebServiceWizardAction(Controller controller) {
    super(controller);
  }

  public FormTool getSelectedFormTool() {
    WSDLPerspective wsdlPerspective = controller_.getWSDLPerspective();
    return (FormTool)wsdlPerspective.getNodeManager().getSelectedNode().getCurrentToolManager().getSelectedTool();
  }

  public boolean run() {
    WSDLPerspective wsdlPerspective = controller_.getWSDLPerspective();
    NodeManager nodeManager = wsdlPerspective.getNodeManager();
    Node node = nodeManager.getSelectedNode();
    WSDLElement element = (WSDLElement)node.getTreeElement();
    return launchWizard(element.getWsdlUrl());
  }

  public final String getStatusContentVar() {
    return controller_.getWSDLPerspective().getStatusContentVar();
  }

  public final String getStatusContentPage() {
    return controller_.getWSDLPerspective().getStatusContentPage();
  }
}
