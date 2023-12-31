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

package org.eclipse.wst.ws.internal.explorer.platform.wsdl.perspective;

import java.util.Vector;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.MessageQueue;

public class SOAPMessageQueue extends MessageQueue
{
  private Vector cachedMessageList_;

  public SOAPMessageQueue()
  {
    super();
    cachedMessageList_ = new Vector();
  }

  public void clear()
  {
    cachedMessageList_.removeAllElements();
    messageList_.removeAllElements();
  }

  public String getMessagesFromList()
  {
    if (messageList_.size() == 0)
      copyMessages(cachedMessageList_,messageList_);
    else
      copyMessages(messageList_,cachedMessageList_);
    return super.getMessagesFromList();
  }

  private final void copyMessages(Vector sourceList,Vector destList)
  {
    for (int i=0;i<sourceList.size();i++)
      destList.addElement(sourceList.elementAt(i));
  }
}
