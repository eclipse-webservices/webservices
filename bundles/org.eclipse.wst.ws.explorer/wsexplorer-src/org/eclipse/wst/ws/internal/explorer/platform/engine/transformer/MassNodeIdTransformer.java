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

import java.util.Hashtable;
import java.util.Vector;

import org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ModelConstants;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;

public class MassNodeIdTransformer extends NodeIdTransformer
{
  protected String massNodeIdKey;

  public MassNodeIdTransformer(Controller controller, String massNodeIdKey)
  {
    super(controller);
    this.massNodeIdKey = massNodeIdKey;
  }

  public Hashtable normalize(Hashtable properties)
  {
    if (!properties.containsKey(massNodeIdKey))
      return super.normalize(properties);
    Object origNodeId = properties.get(ActionInputs.NODEID);
    String[] massNodeIds = getValueAsStringArray(properties, massNodeIdKey);
    for (int i = 0; i < massNodeIds.length; i++)
    {
      properties.put(ActionInputs.NODEID, massNodeIds[i]);
      properties = super.normalize(properties);
      String[] rels = getValueAsStringArray(properties, ModelConstants.REL_ID);
      if (rels.length > 0)
      {
        StringBuffer sb = new StringBuffer(ModelConstants.REL_ID);
        sb.append(ModelConstants.REL_LOCALNAME_SEPARATOR);
        sb.append(massNodeIds[i]);
        properties.put(sb.toString(), rels);
      }
      properties.remove(ActionInputs.NODEID);
      properties.remove(ModelConstants.REL_ID);
    }
    if (origNodeId != null)
      properties.put(ActionInputs.NODEID, origNodeId);
    return properties;
  }

  public Hashtable deNormalize(Hashtable properties)
  {
    if (!properties.containsKey(massNodeIdKey))
      return super.deNormalize(properties);
    Vector massNodeIdVector = new Vector();
    Object origNodeId = properties.get(ActionInputs.NODEID);
    properties.remove(ActionInputs.NODEID);
    String[] massNodeIds = getValueAsStringArray(properties, massNodeIdKey);
    for (int i = 0; i < massNodeIds.length; i++)
    {
      StringBuffer sb = new StringBuffer(ModelConstants.REL_ID);
      sb.append(ModelConstants.REL_LOCALNAME_SEPARATOR);
      sb.append(massNodeIds[i]);
      Object rels = properties.get(sb.toString());
      if (rels != null)
      {
        properties.put(ModelConstants.REL_ID, rels);
        properties = super.deNormalize(properties);
        Object nodeId = properties.get(ActionInputs.NODEID);
        if (nodeId != null)
        {
          massNodeIdVector.add(nodeId);
          properties.remove(ActionInputs.NODEID);
        }
        properties.remove(ModelConstants.REL_ID);
      }
    }
    if (origNodeId != null)
      properties.put(ActionInputs.NODEID, origNodeId);
    int size = massNodeIdVector.size();
    if (size == 1)
      properties.put(massNodeIdKey, massNodeIdVector.get(0));
    else if (size > 1)
      properties.put(massNodeIdKey, massNodeIdVector.toArray(new String[0]));
    else
      properties.remove(massNodeIdKey);
    return properties;
  }

  private String[] getValueAsStringArray(Hashtable properties, String key)
  {
    Object values = properties.get(key);
    if (values == null)
      return new String[0];
    else if (values instanceof String[])
      return (String[])values;
    else
      return new String[] {values.toString()};
  }
}