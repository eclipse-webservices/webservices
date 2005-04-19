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
package org.eclipse.wst.ws.internal.explorer.platform.engine.transformer;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.eclipse.wst.ws.internal.datamodel.Element;
import org.eclipse.wst.ws.internal.datamodel.Rel;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ModelConstants;
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.TreeElement;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Node;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.NodeManager;

public class NodeSelectionTransformer implements ITransformer
{
  protected Controller controller;

  public NodeSelectionTransformer(Controller controller)
  {
    this.controller = controller;
  }

  public Hashtable normalize(Hashtable properties)
  {
    try
    {
      NodeManager nodeManager = controller.getCurrentPerspective().getNodeManager();
      Node nextNode = nodeManager.getNode(Integer.parseInt((String) properties.get(ActionInputs.NODEID)));
      Node currNode = nodeManager.getSelectedNode();
      if (currNode == null)
        currNode = nodeManager.getRootNode();
      Vector nextRels = new Vector();
      Vector currRels = new Vector();
      while (nextNode != null && currNode != null && (!isRootElement(nextNode.getTreeElement()) || !isRootElement(currNode.getTreeElement())) && nextNode != currNode)
      {
        if (nextNode.getNodeDepth() >= currNode.getNodeDepth())
        {
          Node parentNode = nextNode.getParent();
          Element nextElement = nextNode.getTreeElement();
          Rel rel = getRel(parentNode.getTreeElement(), nextElement);
          StringBuffer relValue = new StringBuffer(rel.getName());
          relValue.append(ModelConstants.REL_LOCALNAME_SEPARATOR);
          relValue.append(nextElement.getName());
          nextRels.insertElementAt(relValue.toString(), 0);
          nextNode = parentNode;
        }
        else
        {
          Node parentNode = currNode.getParent();
          Element parentElement = parentNode.getTreeElement();
          Rel rel = getRel(currNode.getTreeElement(), parentElement);
          StringBuffer relValue = new StringBuffer(rel.getName());
          relValue.append(ModelConstants.REL_LOCALNAME_SEPARATOR);
          relValue.append(parentElement.getName());
          currRels.add(relValue.toString());
          currNode = parentNode;
        }
      }
      currRels.addAll(nextRels);
      int numRelationships = currRels.size();
      if (numRelationships == 1)
        properties.put(ModelConstants.REL_ID, currRels.get(0).toString());
      else if (numRelationships > 1)
        properties.put(ModelConstants.REL_ID, (String[]) currRels.toArray(new String[0]));
    }
    catch (NumberFormatException nfe)
    {
    }
    return properties;
  }

  private boolean isRootElement(Element e)
  {
    return e.getModel().getRootElement() == e;
  }

  private Rel getRel(Element sourceElement, Element targetElement)
  {
    Enumeration rels = sourceElement.getRels();
    while (rels.hasMoreElements())
    {
      Rel rel = (Rel) rels.nextElement();
      Enumeration targetElements = rel.getTargetElements();
      while (targetElements.hasMoreElements())
      {
        if (targetElements.nextElement() == targetElement)
          return rel;
      }
    }
    return null;
  }

  public Hashtable deNormalize(Hashtable properties)
  {
    NodeManager nodeManager = controller.getCurrentPerspective().getNodeManager();
    Node rootNode = nodeManager.getRootNode();
    Node currNode = nodeManager.getSelectedNode();
    if (currNode == null)
      currNode = rootNode;
    Object value = properties.get(ModelConstants.REL_ID);
    String[] relationships = null;
    if (value instanceof String[])
      relationships = (String[]) value;
    else if (value != null)
      relationships = new String[]{value.toString()};
    if (relationships != null)
    {
      Element currElement = currNode.getTreeElement();
      for (int i = 0; i < relationships.length; i++)
      {
        int index = relationships[i].indexOf(ModelConstants.REL_LOCALNAME_SEPARATOR);
        String rel = (index != -1) ? relationships[i].substring(0, index) : relationships[i];
        String localname = (index != -1) ? relationships[i].substring(index + 1, relationships[i].length()) : null;
        Enumeration e = currElement.getElements(rel);
        Element targetElement = null;
        if (localname != null)
        {
          Element firstElement = null;
          while (e.hasMoreElements())
          {
            Element nextElement = (Element) e.nextElement();
            if (firstElement == null)
              firstElement = nextElement;
            if (localname.equals(nextElement.getName()))
            {
              targetElement = nextElement;
              break;
            }
          }
          if (targetElement == null)
            targetElement = firstElement;
        }
        else
        {
          if (e.hasMoreElements())
            targetElement = (Element) e.nextElement();
        }
        if (targetElement != null)
        {
          currElement = (Element) targetElement;
          Node parentNode = currNode.getParent();
          if (parentNode != null && parentNode.getTreeElement() == currElement)
            currNode = parentNode;
          else
            currNode = currNode.getChildNode((TreeElement) currElement);
        }
      }
    }
    properties.put(ActionInputs.NODEID, String.valueOf(currNode.getNodeId()));
    return properties;
  }
}