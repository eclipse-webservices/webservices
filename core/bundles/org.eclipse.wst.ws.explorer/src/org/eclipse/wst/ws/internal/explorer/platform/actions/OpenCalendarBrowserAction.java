/*******************************************************************************
 * Copyright (c) 2001, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.ws.internal.explorer.platform.actions;

import javax.servlet.http.HttpServletRequest;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs;

public class OpenCalendarBrowserAction extends Action
{
  public OpenCalendarBrowserAction()
  {
  }

  // calendar_browser.jsp?sessionId=...&calendarType=...
  public static final String getActionLink(String sessionId,int calendarType)
  {
    StringBuffer actionLink = new StringBuffer("calendar_browser.jsp?");
    actionLink.append(ActionInputs.SESSIONID).append('=').append(sessionId);
    actionLink.append('&').append(ActionInputs.CALENDAR_TYPE).append('=').append(calendarType);
    return actionLink.toString();
  }
  
  public static final String getActionLinkForDate(String sessionId)
  {
    return getActionLink(sessionId,ActionInputs.CALENDAR_TYPE_DATE);
  }
  
  public static final String getActionLinkForDateTime(String sessionId)
  {
    return getActionLink(sessionId,ActionInputs.CALENDAR_TYPE_DATETIME);
  }
  
  public static final String getActionLinkForGYearMonth(String sessionId)
  {
    return getActionLink(sessionId,ActionInputs.CALENDAR_TYPE_GYEARMONTH);
  }
  
  public static final String getActionLinkForGDay(String sessionId)
  {
    return getActionLink(sessionId,ActionInputs.CALENDAR_TYPE_GDAY);
  }
  
  public static final String getActionLinkForGMonthDay(String sessionId)
  {
    return getActionLink(sessionId,ActionInputs.CALENDAR_TYPE_GMONTHDAY);
  }

  public final boolean populatePropertyTable(HttpServletRequest request)
  {
    return true;
  }

  public final boolean run()
  {
    return true;
  }
}
