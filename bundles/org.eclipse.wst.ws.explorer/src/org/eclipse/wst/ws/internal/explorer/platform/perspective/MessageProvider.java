/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.ws.internal.explorer.platform.perspective;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

// A Class which provides String messages.
public class MessageProvider
{
  protected MessageBundle messageBundle_;
  private String messageProviderName_;

  public MessageProvider(String messageBundleFileName)
  {
    try
    {
      // Load the message bundle from the WEB-INF\classes subdirectory.
      messageProviderName_ = messageBundleFileName;
      messageBundle_ = new MessageBundle(ResourceBundle.getBundle(messageBundleFileName));
    }
    catch (MissingResourceException e)
    {
    	System.err.println("Could not load "+messageBundleFileName);
    	e.printStackTrace();
    }
  }

  public String getMessage(String messageId)
  {
    return messageBundle_.getMessage(messageId);
  }

  public String getMessage(String messageId,String sub)
  {
    return messageBundle_.getMessage(messageId,sub);
  }

  public String getMessage(String messageId,String[] sub)
  {
    return messageBundle_.getMessage(messageId,sub);
  }
  
  public String getMessageProviderName()
  {
    return messageProviderName_;
  }
}
