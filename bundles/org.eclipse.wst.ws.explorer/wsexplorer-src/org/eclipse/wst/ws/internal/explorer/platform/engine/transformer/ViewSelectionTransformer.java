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
package org.eclipse.wst.ws.internal.explorer.platform.engine.transformer;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.eclipse.wst.ws.internal.datamodel.Element;
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.ListElement;
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.ListManager;
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.TreeElement;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Node;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.NodeManager;

public class ViewSelectionTransformer implements ITransformer
{
  protected Controller controller;
  protected String listManagerKey;
  protected String viewKey;

  public ViewSelectionTransformer(Controller controller, String listManagerKey, String viewKey)
  {
    this.controller = controller;
    this.listManagerKey = listManagerKey;
    this.viewKey = viewKey;
  }

  public Hashtable normalize(Hashtable properties)
  {
    Vector normalizedViewIds = new Vector();
    NodeManager nodeManager = controller.getCurrentPerspective().getNodeManager();
    Node currNode = nodeManager.getSelectedNode();
    TreeElement currElement = currNode.getTreeElement();
    Object listManagerObj = currElement.getPropertyAsObject(listManagerKey);
    if (listManagerObj instanceof ListManager)
    {
      ListManager listManager = (ListManager) listManagerObj;
      String[] viewIds = getViewValues(properties);
      for (int i = 0; i < viewIds.length; i++)
      {
        ListElement listElement = null;
        try
        {
          listElement = listManager.getElementWithViewId(Integer.parseInt(viewIds[i]));
        }
        catch (NumberFormatException nfe)
        {
        }
        if (listElement != null)
        {
          Object object = listElement.getObject();
          if (object != null && object instanceof Element)
          {
            Element element = (Element) object;
            normalizedViewIds.add(element.getName());
          }
        }
      }
    }
    properties.put(viewKey, normalizedViewIds.toArray(new String[0]));
    return properties;
  }

  public Hashtable deNormalize(Hashtable properties)
  {
    Vector viewIds = new Vector();
    Node currNode = controller.getCurrentPerspective().getNodeManager().getSelectedNode();
    if (currNode != null)
    {
      TreeElement currElement = currNode.getTreeElement();
      Object listManagerObj = currElement.getPropertyAsObject(listManagerKey);
      if (listManagerObj instanceof ListManager)
      {
        ListManager listManager = (ListManager) listManagerObj;
        String[] normalizedViewIds = getViewValues(properties);
        for (int i = 0; i < normalizedViewIds.length; i++)
        {
          Enumeration e = listManager.getListElements();
          while (e.hasMoreElements())
          {
            ListElement listElement = (ListElement) e.nextElement();
            Element element = (Element) listElement.getObject();
            if (element != null && normalizedViewIds[i].equals(element.getName()))
            {
              viewIds.add(String.valueOf(listElement.getViewId()));
              break;
            }
          }
        }
      }
    }
    int size = viewIds.size();
    if (size == 1)
      properties.put(viewKey, viewIds.get(0));
    else if (size > 1)
      properties.put(viewKey, viewIds.toArray(new String[0]));
    return properties;
  }

  private String[] getViewValues(Hashtable properties)
  {
    Object viewValueObj = properties.get(viewKey);
    String[] viewValues;
    if (viewValueObj == null)
      viewValues = new String[0];
    else if (viewValueObj.getClass().isArray())
      viewValues = (String[]) viewValueObj;
    else
      viewValues = new String[]{(String) viewValueObj};
    return viewValues;
  }
}
