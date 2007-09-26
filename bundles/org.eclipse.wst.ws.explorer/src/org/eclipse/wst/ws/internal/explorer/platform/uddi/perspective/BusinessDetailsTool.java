/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.ws.internal.explorer.platform.uddi.perspective;

import java.util.Vector;
import org.eclipse.wst.ws.internal.explorer.platform.actions.ProxyLoadPageAction;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.ToolManager;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.UDDIActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel.BusinessElement;
import org.uddi4j.datatype.business.BusinessEntity;
import org.uddi4j.util.CategoryBag;
import org.uddi4j.util.DiscoveryURLs;
import org.uddi4j.util.IdentifierBag;

public class BusinessDetailsTool extends DetailsTool
{
  public BusinessDetailsTool(ToolManager toolManager,String alt)
  {
    super(toolManager,alt,ProxyLoadPageAction.getActionLink("uddi/forms/BusinessDetailsForm.jsp"));
  }

  public final void initDefaultProperties()
  {
    clearPropertyTable();
    BusinessElement busElement = (BusinessElement)(toolManager_.getNode().getTreeElement());
    BusinessEntity be = busElement.getBusinessEntity();

    setProperty(UDDIActionInputs.QUERY_INPUT_UUID_BUSINESS_KEY,be.getBusinessKey());

    Vector nameVector = new Vector();
    copyIndexVector(be.getNameVector(),nameVector);
    setProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_NAMES,nameVector);

    Vector descriptionVector = new Vector();
    copyIndexVector(be.getDescriptionVector(),descriptionVector);
    setProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_DESCRIPTIONS,descriptionVector);

    IdentifierBag idBag = be.getIdentifierBag();
    if (idBag != null)
    {
      Vector idVector = new Vector();
      copyIndexVector(idBag.getKeyedReferenceVector(),idVector);
      setProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_IDENTIFIERS,idVector);
    }

    CategoryBag catBag = be.getCategoryBag();
    if (catBag != null)
    {
      Vector catVector = new Vector();
      copyIndexVector(catBag.getKeyedReferenceVector(),catVector);
      setProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_CATEGORIES,catVector);
    }

    DiscoveryURLs discoveryURLs = be.getDiscoveryURLs();
    if (discoveryURLs != null)
    {
      Vector discoveryURLVector = new Vector();
      copyIndexVector(discoveryURLs.getDiscoveryURLVector(),discoveryURLVector);
      setProperty(UDDIActionInputs.QUERY_INPUT_ADVANCED_BUSINESS_DISCOVERYURLS,discoveryURLVector);
    }
  }
}
