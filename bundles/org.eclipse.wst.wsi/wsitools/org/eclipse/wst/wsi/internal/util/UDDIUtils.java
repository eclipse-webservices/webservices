/*******************************************************************************
 * Copyright (c) 2002-2005 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   IBM - Initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsi.internal.util;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import org.uddi4j.UDDIException;
import org.uddi4j.client.UDDIProxy;
import org.uddi4j.datatype.binding.BindingTemplate;
import org.uddi4j.datatype.binding.TModelInstanceInfo;
import org.uddi4j.datatype.business.BusinessEntity;
import org.uddi4j.datatype.service.BusinessService;
import org.uddi4j.datatype.tmodel.TModel;
import org.uddi4j.response.BusinessDetail;
import org.uddi4j.response.ServiceDetail;
import org.uddi4j.response.TModelDetail;
import org.uddi4j.response.TModelInfo;
import org.uddi4j.response.TModelList;
import org.uddi4j.transport.TransportException;
import org.uddi4j.util.CategoryBag;
import org.uddi4j.util.FindQualifier;
import org.uddi4j.util.FindQualifiers;
import org.uddi4j.util.KeyedReference;

/**
 * This class provide some service functions used by UDDIValidator.
 *
 * @version 1.0.1
 * @author Peter Brittenham
 */
public final class UDDIUtils
{
  static public final FindQualifiers EXACT_NAME_MATCH_QUALIFIER;
  static {
    Vector vector = new Vector();
    EXACT_NAME_MATCH_QUALIFIER = new FindQualifiers();
    vector.add(new FindQualifier(FindQualifier.exactNameMatch));
    vector.add(new FindQualifier(FindQualifier.sortByNameAsc));
    EXACT_NAME_MATCH_QUALIFIER.setFindQualifierVector(vector);
  }

  static final String WS_I_CONFORMANCE_TMODEL_NAME =
    "ws-i-org:conformsTo:2002_12";

  static private Hashtable wsiConformanceTable = new Hashtable();

  /**
   * Gets a business service by key.
   * @param proxy  a UDDI proxy.
   * @param key    a key to a business service.
   * @return a business service specified by the given key.
   * @throws TransportException if error originating within the SOAP transport.
   * @throws UDDIException if the return value of a UDDI API cannot indicate an error condition.
   */
  public static BusinessService getBusinessServiceByKey(
    UDDIProxy proxy,
    String key)
    throws TransportException, UDDIException
  {
    BusinessService result = null;

    ServiceDetail sd = proxy.get_serviceDetail(key);

    if (sd != null)
    {
      Vector v = sd.getBusinessServiceVector();

      if (v != null && v.size() > 0)
      {
        result = (BusinessService) v.firstElement();
      }
    }

    return result;
  }

  /**
   * Gets business entity by key.
   * @param proxy  a UDDI proxy.
   * @param key    a key to a business entiy.
   * @return a business entity specified by the given key.
   * @throws TransportException if error originating within the SOAP transport.
   * @throws UDDIException if the return value of a UDDI API cannot indicate 
   *         an error condition.
   */
  public static BusinessEntity getBusinessByKey(UDDIProxy proxy, String key)
    throws TransportException, UDDIException
  {
    BusinessEntity result = null;

    BusinessDetail bd = proxy.get_businessDetail(key);

    if (bd != null)
    {
      Vector v = bd.getBusinessEntityVector();

      if (v != null && v.size() > 0)
      {
        result = (BusinessEntity) v.firstElement();
      }
    }

    return result;
  }

  /**
   * Gets a tModel by key.
   * @param proxy  a UDDI proxy.
   * @param key    a key to a tModel.
   * @return a tModel specified by the given key.
   * @throws TransportException if error originating within the SOAP transport.
   * @throws UDDIException if the return value of a UDDI API cannot indicate 
   *         an error condition.
   */
  public static TModel getTModelByKey(UDDIProxy proxy, String key)
  {
    TModel result = null;

    try
    {
      TModelDetail bd = proxy.get_tModelDetail(key);

      if (bd != null)
      {
        Vector v = bd.getTModelVector();

        if (v != null && v.size() > 0)
        {
          result = (TModel) v.firstElement();
        }
      }
    }
    catch (Throwable e)
    {
    }

    return result;
  }

  /**
   * Gets key of the WSI Conformance tModel.
   * @param proxy  a UDDI proxy.
   * @return the key of the WSI Conformance tModel.
   */
  public static String getWSIConformanceTModelKey(UDDIProxy proxy)
  {
    if (proxy == null)
      throw new IllegalArgumentException("UDDI proxy cannot be null.");

    String result = null;

    if (wsiConformanceTable.containsKey(proxy))
    {
      result = wsiConformanceTable.get(proxy).toString();
    }
    else
    {
      result = getTModelKeyByName(proxy, WS_I_CONFORMANCE_TMODEL_NAME);

      if (result == null)
      {
        throw new IllegalStateException("WS-I conformance taxonomy tModel was not found");
      }

      wsiConformanceTable.put(proxy, result);
    }

    return result;
  }

  /**
   * Gets a tModel key by tModel name.
   * @param proxy  a UDDI proxy.
   * @param name   a tModel name.
   * @return a tModel key specified by the given tModel name.
   */
  public static String getTModelKeyByName(UDDIProxy proxy, String name)
  {
    String result = null;
    try
    {
      TModelList list =
        proxy.find_tModel(name, null, null, EXACT_NAME_MATCH_QUALIFIER, 1);
      TModelInfo info =
        (TModelInfo) list.getTModelInfos().getTModelInfoVector().firstElement();
      result = info.getTModelKey();
    }
    catch (Throwable e)
    {
    }

    return result;
  }

  /**
   * Get string representation of bindingTemplate.
   * @param bindingTemplate  a BindingTemplate object.
   * @return a string representation of bindingTemplate.
   */
  public static String bindingTemplateToString(BindingTemplate bindingTemplate)
  {
    String returnString = "";

    if (bindingTemplate == null)
      returnString = "null";

    else
    {
      returnString =
        "accessPoint: "
          + (bindingTemplate.getAccessPoint() == null
            ? "null"
            : bindingTemplate.getAccessPoint().getText());

      if (bindingTemplate.getTModelInstanceDetails() == null
        || bindingTemplate
          .getTModelInstanceDetails()
          .getTModelInstanceInfoVector()
          == null)
      {
        returnString += ", [no tModel reference]";
      }

      else
      {
        Iterator iterator =
          bindingTemplate
            .getTModelInstanceDetails()
            .getTModelInstanceInfoVector()
            .iterator();

        int infoCount = 1;
        TModelInstanceInfo info;
        while (iterator.hasNext())
        {
          info = (TModelInstanceInfo) iterator.next();
          returnString += ", ["
            + infoCount++
            + "] tModelKey: "
            + info.getTModelKey();
        }
      }
    }

    return returnString;
  }

  /**
   * Get string representation of tModel.
   * @param tModel  a TModel object.
   * @return a string representation of tModel.
   */
  public static String tModelToString(TModel tModel)
  {
    String returnString = "";

    if (tModel == null)
      returnString = "null";

    else
    {
      returnString =
        "name: "
          + tModel.getNameString()
          + ", categoryBag: "
          + (tModel.getCategoryBag() == null
            ? "null"
            : categoryBagToString(tModel.getCategoryBag()))
          + ", overviewURL: "
          + (tModel.getOverviewDoc() == null
            ? "null"
            : tModel.getOverviewDoc().getOverviewURLString());
    }

    return returnString;
  }

  /**
   * Get string representation of categoryBag.
   * @param categoryBag  a CategoryBag object.
   * @return a tring representation of categoryBag.
   */
  public static String categoryBagToString(CategoryBag categoryBag)
  {
    String returnString = "";

    if (categoryBag == null)
    {
      returnString += "null";
    }

    else
    {
      returnString += "KeyedReferenceList: ";

      Vector krList = null;
      if ((krList = categoryBag.getKeyedReferenceVector()) == null)
      {
        returnString += "null";
      }

      else if (krList.size() == 0)
      {
        returnString += "empty";
      }

      else
      {
        KeyedReference kr = null;
        Iterator iterator = krList.iterator();
        while (iterator.hasNext())
        {
          kr = (KeyedReference) iterator.next();
          returnString += "tModelKey: "
            + kr.getTModelKey()
            + ", keyName: "
            + kr.getKeyName()
            + ", keyValue: "
            + kr.getKeyValue();
        }
      }
    }

    return returnString;
  }
}
