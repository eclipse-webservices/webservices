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
import java.util.Enumeration;

import org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataParser;

public class MultipartFormDataParserTransformer implements ITransformer
{
  protected Controller controller;

  public MultipartFormDataParserTransformer(Controller controller)
  {
    this.controller = controller;
  }

  public Hashtable normalize(Hashtable properties)
  {
    MultipartFormDataParser parser = (MultipartFormDataParser)properties.get(ActionInputs.MULTIPART_FORM_DATA_PARSER);
    String[] keys;
    try
    {
      keys = parser.getParameterNames();
    }
    catch (Throwable t)
    {
      keys = new String[0];
    }
    for (int i = 0; i < keys.length; i++)
    {
      StringBuffer newKey = new StringBuffer(ActionInputs.MULTIPART_FORM_DATA_PARSER);
      newKey.append(keys[i]);
      try
      {
        properties.put(newKey.toString(), parser.getParameterValues(keys[i]));
      }
      catch (Throwable t)
      {
      }
    }
    return properties;
  }
  
  public Hashtable deNormalize(Hashtable properties)
  {
    Enumeration e = properties.keys();
    while (e.hasMoreElements())
    {
      Object key = e.nextElement();
      if (key instanceof String)
      {
        if (((String)key).startsWith(ActionInputs.MULTIPART_FORM_DATA_PARSER))
        {
          String realKey = ((String)key).substring(ActionInputs.MULTIPART_FORM_DATA_PARSER.length());
          if (!properties.containsKey(realKey))
          {
            Object value = properties.remove(key);
            properties.put(realKey, value);
          }
        }
      }
    }
    return properties;
  }
}