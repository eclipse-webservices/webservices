/*******************************************************************************
 * Copyright (c) 2004, 2007 IBM Corporation and others.
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
package org.eclipse.wst.ws.internal.explorer.platform.uddi.actions;

import java.util.Vector;
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.ListElement;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.FormToolPropertiesInterface;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Node;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.UDDIActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.BusinessElement;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataException;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataParser;
import org.uddi4j.datatype.business.BusinessEntity;

public class BusPublishServiceSimpleAction extends PublishAction
{
  private RegPublishServiceSimpleAction regPublishServiceSimpleAction_;

  public BusPublishServiceSimpleAction(Controller controller)
  {
    super(controller);
    regPublishServiceSimpleAction_ = new RegPublishServiceSimpleAction(controller);
    propertyTable_ = regPublishServiceSimpleAction_.getPropertyTable();
  }

  protected boolean processOthers(MultipartFormDataParser parser,FormToolPropertiesInterface formToolPI) throws MultipartFormDataException
  {
    Node businessNode = getSelectedNavigatorNode();
    BusinessElement businessElement = (BusinessElement)businessNode.getTreeElement();
    BusinessEntity businessEntity = businessElement.getBusinessEntity();
    ListElement busListElement = new ListElement(businessEntity);
    Vector serviceBusinessCopy = new Vector();
    serviceBusinessCopy.addElement(busListElement);
    formToolPI.setProperty(UDDIActionInputs.QUERY_INPUT_SIMPLE_SERVICE_BUSINESS_COPY,serviceBusinessCopy); 
    boolean result = regPublishServiceSimpleAction_.processOthers(parser,formToolPI);
    if (!propertyTable_.containsKey(UDDIActionInputs.QUERY_INPUT_SIMPLE_SERVICE_SERVICE_PROVIDER))
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_SIMPLE_SERVICE_SERVICE_PROVIDER,businessEntity);
    return result;
  }

  public final boolean run()
  {
    return regPublishServiceSimpleAction_.run();
  }
}
