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

package org.eclipse.wst.ws.internal.explorer.platform.perspective;

import java.util.Enumeration;
import java.util.Vector;
import org.eclipse.wst.ws.internal.explorer.platform.util.HTMLUtils;

public class MessageQueue
{
  protected Vector messageList_;

  public MessageQueue()
  {
    messageList_ = new Vector();
  }

  public void addMessage(String message)
  {
    messageList_.addElement(message);
  }

  public String getMessagesFromList()
  {
    StringBuffer messages = new StringBuffer();

    if (messageList_.size()==0)
      return "";

    Enumeration e = messageList_.elements();
    while (e.hasMoreElements())
    {
      String thisMessage = (String)e.nextElement();
      messages.append(thisMessage);
      messages.append(HTMLUtils.LINE_SEPARATOR);
    }
    messageList_.removeAllElements();
    return messages.toString();
  }
}
