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

public class BusPublishServiceAdvancedAction extends PublishAction
{
  private RegPublishServiceAdvancedAction regPublishServiceAdvancedAction_;
  public BusPublishServiceAdvancedAction(Controller controller)
  {
    super(controller);
    regPublishServiceAdvancedAction_ = new RegPublishServiceAdvancedAction(controller);
    propertyTable_ = regPublishServiceAdvancedAction_.getPropertyTable();
  }

  protected final boolean processOthers(MultipartFormDataParser parser,FormToolPropertiesInterface formToolPI) throws MultipartFormDataException
  {
    Node businessNode = getSelectedNavigatorNode();
    BusinessElement businessElement = (BusinessElement)businessNode.getTreeElement();
    BusinessEntity businessEntity = businessElement.getBusinessEntity();
    ListElement busListElement = new ListElement(businessEntity);
    Vector serviceBusinessCopy = new Vector();
    serviceBusinessCopy.addElement(busListElement);
    formToolPI.setProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_BUSINESS_COPY,serviceBusinessCopy);
    boolean result = regPublishServiceAdvancedAction_.processOthers(parser,formToolPI);
    if (!propertyTable_.containsKey(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_SERVICE_PROVIDER))
      propertyTable_.put(UDDIActionInputs.QUERY_INPUT_ADVANCED_SERVICE_SERVICE_PROVIDER,businessEntity);
    return result;
  }

  public final boolean run()
  {
    return regPublishServiceAdvancedAction_.run();
  }
}
