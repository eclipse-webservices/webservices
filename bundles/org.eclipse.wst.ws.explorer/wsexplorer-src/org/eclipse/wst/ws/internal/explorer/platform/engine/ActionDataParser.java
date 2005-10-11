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
package org.eclipse.wst.ws.internal.explorer.platform.engine;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import org.eclipse.wst.ws.internal.explorer.platform.engine.constants.ActionDataConstants;
import org.eclipse.wst.ws.internal.explorer.platform.engine.data.ActionDescriptor;
import org.eclipse.wst.ws.internal.explorer.platform.engine.data.ScenarioDescriptor;
import org.eclipse.wst.ws.internal.explorer.platform.engine.data.TransactionDescriptor;
import org.w3c.dom.CDATASection;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ActionDataParser
{
  private Document doc;

  public ScenarioDescriptor parseScenario(Element e)
  {
    ScenarioDescriptor scenarioDescriptor = new ScenarioDescriptor();
    NodeList transactions = e.getElementsByTagName(ActionDataConstants.ELEMENT_TRANSACTION);
    TransactionDescriptor[] transactionDescriptors = new TransactionDescriptor[transactions.getLength()];
    for (int i = 0; i < transactionDescriptors.length; i++)
      transactionDescriptors[i] = parseTransaction((Element) transactions.item(i));
    scenarioDescriptor.setTransactionDescriptors(transactionDescriptors);
    return scenarioDescriptor;
  }

  public TransactionDescriptor parseTransaction(Element e)
  {
    TransactionDescriptor transactionDescriptor = new TransactionDescriptor();
    NodeList actions = e.getElementsByTagName(ActionDataConstants.ELEMENT_ACTION);
    ActionDescriptor[] actionDescriptors = new ActionDescriptor[actions.getLength()];
    for (int i = 0; i < actionDescriptors.length; i++)
      actionDescriptors[i] = parseAction((Element) actions.item(i));
    transactionDescriptor.setActionDescriptors(actionDescriptors);
    return transactionDescriptor;
  }

  public ActionDescriptor parseAction(Element e)
  {
    ActionDescriptor actionDescriptor = new ActionDescriptor();
    actionDescriptor.setId(e.getAttribute(ActionDataConstants.ATTR_ID));
    try
    {
      actionDescriptor.setAttempts(Integer.parseInt(e.getAttribute(ActionDataConstants.ATTR_ATTEMPTS)));
    }
    catch (Throwable t)
    {
      actionDescriptor.setAttempts(1);
    }
    actionDescriptor.setStatusId(e.getAttribute(ActionDataConstants.ATTR_STATUS_ID));
    Hashtable propertiesTable = new Hashtable();
    NodeList properties = e.getElementsByTagName(ActionDataConstants.ELEMENT_PROPERTY);
    for (int i = 0; i < properties.getLength(); i++)
    {
      Element property = (Element) properties.item(i);
      String name = property.getAttribute(ActionDataConstants.ATTR_NAME);
      NodeList valueList = property.getElementsByTagName(ActionDataConstants.ELEMENT_VALUE);
      if (valueList.getLength() > 1)
      {
        String[] values = new String[valueList.getLength()];
        for (int j = 0; j < values.length; j++)
        {
          Element value = (Element) valueList.item(j);
          Node textNode = value.getFirstChild();
          values[j] = (textNode != null) ? textNode.getNodeValue().trim() : "";
        }
        propertiesTable.put(name, values);
      }
      else
      {
        Node textNode = ((Element) valueList.item(0)).getFirstChild();
        propertiesTable.put(name, (textNode != null) ? textNode.getNodeValue().trim() : "");
      }
    }
    actionDescriptor.setProperties(propertiesTable);
    NodeList statusList = e.getElementsByTagName(ActionDataConstants.ELEMENT_STATUS);
    for (int i = 0; i < statusList.getLength(); i++)
    {
      Element status = (Element)statusList.item(i);
      CDATASection cData = (CDATASection)status.getFirstChild();
      actionDescriptor.addStatus(cData.getData());
    }
    return actionDescriptor;
  }

  public Element toElement(ScenarioDescriptor scenarioDescriptor)
  {
    try
    {
      Document document = getDocument();
      Element scenarioElement = document.createElement(ActionDataConstants.ELEMENT_SCENARIO);
      TransactionDescriptor[] transactionDescriptors = scenarioDescriptor.getTransactionDescriptors();
      for (int i = 0; i < transactionDescriptors.length; i++)
      {
        Element transactionElement = toElement(transactionDescriptors[i]);
        if (transactionElement != null)
          scenarioElement.appendChild(transactionElement);
      }
      return scenarioElement;
    }
    catch (DOMException dome)
    {
      return null;
    }
  }

  public Element toElement(TransactionDescriptor transactionDescriptor)
  {
    try
    {
      Document document = getDocument();
      Element transactionElement = document.createElement(ActionDataConstants.ELEMENT_TRANSACTION);
      ActionDescriptor[] actionDescriptors = transactionDescriptor.getActionDescriptors();
      for (int i = 0; i < actionDescriptors.length; i++)
      {
        Element actionElement = toElement(actionDescriptors[i]);
        if (actionElement != null)
          transactionElement.appendChild(actionElement);
      }
      return transactionElement;
    }
    catch (DOMException dome)
    {
      return null;
    }
  }

  public Element toElement(ActionDescriptor actionDescriptor)
  {
    try
    {
      Document document = getDocument();
      Element actionElement = document.createElement(ActionDataConstants.ELEMENT_ACTION);
      actionElement.setAttribute(ActionDataConstants.ATTR_ID, actionDescriptor.getId());
      actionElement.setAttribute(ActionDataConstants.ATTR_ATTEMPTS, String.valueOf(actionDescriptor.getAttempts()));
      String statusId = actionDescriptor.getStatusId();
      if (statusId != null)
        actionElement.setAttribute(ActionDataConstants.ATTR_STATUS_ID, statusId);
      Hashtable properties = actionDescriptor.getProperties();
      if (properties != null)
      {
        for (Iterator it = properties.keySet().iterator(); it.hasNext();)
        {
          Object key = it.next();
          Object value = properties.get(key);
          Object[] values;
          if (value.getClass().isArray())
            values = (Object[])value;
          else if (value instanceof List)
            values = ((List)value).toArray();
          else
            values = new Object[] {value};
          boolean isAdded = false;
          Element propertyElement = document.createElement(ActionDataConstants.ELEMENT_PROPERTY);
          propertyElement.setAttribute(ActionDataConstants.ATTR_NAME, key.toString());
          for (int i = 0; i < values.length; i++)
          {
            if (values[i] instanceof String)
            {
              Element valueElement = document.createElement(ActionDataConstants.ELEMENT_VALUE);
              valueElement.appendChild(document.createTextNode(values[i].toString()));
              propertyElement.appendChild(valueElement);
              isAdded = true;
            }
          }
          if (isAdded)
            actionElement.appendChild(propertyElement);
        }
      }
      List status = actionDescriptor.getStatus();
      if (status != null)
      {
        for (Iterator it = status.iterator(); it.hasNext();)
        {
          Element statusElement = document.createElement(ActionDataConstants.ELEMENT_STATUS);
          CDATASection cData = document.createCDATASection(it.next().toString());
          statusElement.appendChild(cData);
          actionElement.appendChild(statusElement);
        }
      }
      return actionElement;
    }
    catch (DOMException dome)
    {
      return null;
    }
  }

  private Document getDocument()
  {
    try
    {
      if (doc == null)
      {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        doc = builder.newDocument();
      }
      return doc;
    }
    catch (FactoryConfigurationError fce)
    {
      return null;
    }
    catch (ParserConfigurationException pce)
    {
      return null;
    }
  }
}