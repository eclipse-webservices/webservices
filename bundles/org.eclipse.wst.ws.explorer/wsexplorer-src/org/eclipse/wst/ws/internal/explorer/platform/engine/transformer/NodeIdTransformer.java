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

public class NodeIdTransformer implements ITransformer
{
  protected Controller controller;

  public NodeIdTransformer(Controller controller)
  {
    this.controller = controller;
  }

  public Hashtable normalize(Hashtable properties)
  {
    Vector rels = new Vector();
    try
    {
      int nodeId = Integer.parseInt((String)properties.get(ActionInputs.NODEID));
      NodeManager nodeManager = controller.getCurrentPerspective().getNodeManager();
      Node rootNode = nodeManager.getRootNode();
      Node node = nodeManager.getNode(nodeId);
      int depth = 0;
      if (node != null)
        depth = node.getNodeDepth();
      while (rels.size() < depth && node != null && node != rootNode)
      {
        Node parentNode = node.getParent();
        Element element = node.getTreeElement();
        Rel rel = getRel(parentNode.getTreeElement(), element);
        StringBuffer relValue = new StringBuffer(rel.getName());
        relValue.append(ModelConstants.REL_LOCALNAME_SEPARATOR);
        relValue.append(element.getName());
        rels.insertElementAt(relValue.toString(), 0);
        node = parentNode;
      }
    }
    catch (NumberFormatException nfe)
    {
    }
    int numRelationships = rels.size();
    if (numRelationships == 1)
      properties.put(ModelConstants.REL_ID, rels.get(0).toString());
    else if (numRelationships > 1)
      properties.put(ModelConstants.REL_ID, (String[])rels.toArray(new String[0]));
    return properties;
  }

  private Rel getRel(Element sourceElement, Element targetElement)
  {
    Enumeration rels = sourceElement.getRels();
    while (rels.hasMoreElements())
    {
      Rel rel = (Rel)rels.nextElement();
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
    Node node = nodeManager.getRootNode();
    Object value = properties.get(ModelConstants.REL_ID);
    String[] relationships = null;
    if (value instanceof String[])
      relationships = (String[])value;
    else if (value != null)
      relationships = new String[] {value.toString()};
    if (relationships != null)
    {
      for (int i = 0; i < relationships.length; i++)
      {
        int index = relationships[i].indexOf(ModelConstants.REL_LOCALNAME_SEPARATOR);
        if (index == -1)
          return deNormalizeAsSelectedNode(properties);
        String rel = (index != -1) ? relationships[i].substring(0, index) : relationships[i];
        String localname = (index != -1) ? relationships[i].substring(index + 1, relationships[i].length()) : null;
        if (localname == null)
          return deNormalizeAsSelectedNode(properties);
        node = getNode(node, rel, localname);
        if (node == null)
          return deNormalizeAsSelectedNode(properties);
      }
      properties.put(ActionInputs.NODEID, String.valueOf(node.getNodeId()));
      return properties;
    }
    else
      return deNormalizeAsSelectedNode(properties);
  }
  
  private Hashtable deNormalizeAsSelectedNode(Hashtable properties)
  {
    return (new CurrentNodeSelectionTransformer(controller)).deNormalize(properties);
  }

  private Node getNode(Node parent, String rel, String localname)
  {
    Element parentElement = parent.getTreeElement();
    Enumeration e = parentElement.getElements(rel);
    while (e.hasMoreElements())
    {
      Element element = (Element)e.nextElement();
      if (localname.equals(element.getName()))
        return parent.getChildNode((TreeElement)element);
    }
    return null;
  }
}