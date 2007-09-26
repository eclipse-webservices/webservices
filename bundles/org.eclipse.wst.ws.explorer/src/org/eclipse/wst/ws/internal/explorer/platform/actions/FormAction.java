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
package org.eclipse.wst.ws.internal.explorer.platform.actions;

import java.util.Hashtable;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.FormTool;
import org.eclipse.wst.ws.internal.explorer.platform.util.HTMLUtils;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataException;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataParser;

public abstract class FormAction extends Action
{
  public FormAction(Controller controller)
  {
    super(controller);
  }

  protected abstract boolean processParsedResults(MultipartFormDataParser parser) throws MultipartFormDataException;

  public final boolean populatePropertyTable(HttpServletRequest request)
  {
    try
    {
      MultipartFormDataParser parser = new MultipartFormDataParser();
      parser.parseRequest(request,HTMLUtils.UTF8_ENCODING);
      getSelectedFormTool().clearErrors();
      boolean result = processParsedResults(parser);
      removedProperties_.removeAllElements();
      return result;
    }
    catch (MultipartFormDataException e)
    {
    }
    return false;
  }
  
  public void setPropertyTable(Hashtable propertyTable)
  {
    try
    {
      processParsedResults(new MultipartFormDataParser(propertyTable));
    }
    catch (MultipartFormDataException mfde)
    {
    }
    for (Iterator it = propertyTable.keySet().iterator(); it.hasNext();)
    {
      Object key = it.next();
      Hashtable thisPropertyTable = getPropertyTable();
      if (!thisPropertyTable.containsKey(key))
        addProperty(key, propertyTable.get(key));
    }
  }

  protected final void addToHistory(int perspectiveId,String url)
  {
    controller_.addToHistory(perspectiveId,url);
  }

  public abstract FormTool getSelectedFormTool();
}
