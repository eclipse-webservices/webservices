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
package org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel;

import java.util.Vector;
import org.eclipse.wst.ws.internal.datamodel.Model;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ModelConstants;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.UDDIActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.uddi.constants.UDDIModelConstants;
import org.uddi4j.datatype.business.BusinessEntity;
import org.uddi4j.datatype.service.BusinessService;
import org.uddi4j.datatype.tmodel.TModel;

public class QueryElement extends AbstractUDDIElement
{
  private final void initQueryProperties(int itemType,Object[] results)
  {
    Vector resultsVector = new Vector();
    for (int i=0;i<results.length;i++)
      resultsVector.addElement(results[i]);
    setPropertyAsString(ModelConstants.REL_CHILDREN,UDDIModelConstants.REL_QUERY_RESULTS);
    setPropertyAsObject(UDDIModelConstants.INITIAL_RESULTS,resultsVector);
    setPropertyAsString(UDDIModelConstants.QUERY_TYPE,String.valueOf(itemType));
  }
  
  public QueryElement(BusinessEntity[] spList,String name,Model model)
  {
    super(name,model);
    initQueryProperties(UDDIActionInputs.QUERY_ITEM_BUSINESSES,spList);
  }

  public QueryElement(BusinessService[] bsList,String name,Model model)
  {
    super(name,model);
    initQueryProperties(UDDIActionInputs.QUERY_ITEM_SERVICES,bsList);
  }

  public QueryElement(TModel[] tModelList,String name,Model model)
  {
    super(name,model);
    initQueryProperties(UDDIActionInputs.QUERY_ITEM_SERVICE_INTERFACES,tModelList);
  }

  public int getQueryType()
  {
    return Integer.parseInt(getPropertyAsString(UDDIModelConstants.QUERY_TYPE));
  }

  public QueryParentElement getQueryParentElement()
  {
    return (QueryParentElement)getParentElement();
  }
}
