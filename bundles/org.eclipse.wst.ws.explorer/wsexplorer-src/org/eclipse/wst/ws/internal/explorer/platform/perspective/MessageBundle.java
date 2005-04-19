/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.ws.internal.explorer.platform.perspective;

import java.util.ResourceBundle;
import java.util.MissingResourceException;

/**
* An message bundle is a collection of messages drawn from some
* set of properties.
*/
public class MessageBundle
{
  private ResourceBundle fResourceBundle;

  /**
  * Constructs a new MessageBundle from the given ResourceBundle.
  */
  public MessageBundle ( ResourceBundle resourceBundle )
  {
    fResourceBundle = resourceBundle;
  }

  /**
  * Returns the message identified by messageId.
  * @param messageId The message ID.
  * @return String The message text.
  */
  public String getMessage ( String messageId )
  {
    try
    {
      return fResourceBundle.getString(messageId);
    }
    catch (MissingResourceException e)
    {
      return messageId;
    }
  }

  /**
  * Returns the message identified by messageId.
  * @param messageId The message ID.
  * @param sub A substitution value for "%1" in the message.
  * @return String The message text.
  */
  public String getMessage ( String messageId, String sub )
  {
    String[] s = {sub};
    return getMessage(messageId,s);
  }

  /**
  * Returns the message identified by messageId.
  * @param messageId The message ID.
  * @param sub Substitution values for "%1..%n" (sub[0]..sub[n-1]) in
  * the message.
  * @return String The message text.
  */
  public String getMessage ( String messageId, String[] sub )
  {
    String m = getMessage(messageId);
    int len = m.length();
    int max = sub.length;
    StringBuffer b = new StringBuffer();
    int pos = 0;
    while (pos >= 0)
    {
      int pct = m.indexOf('%',pos);
      if (pct >= 0)
      {
        b.append(m.substring(pos,pct));
        pos = pct + 1;
        if (pos < len)
        {
          char d = m.charAt(pos);
          if (Character.isDigit(d) && d != '0')
          {
            int v = Integer.parseInt(new Character(d).toString());
            if (v <= max)
            {
              b.append(sub[v-1]);
            }
            else
            {
              b.append('?');
            }
            pos++;
          }
          else
          {
            b.append('%');
            b.append(d);
            pos++;
          }
        }
        else
        {
          b.append('%');
          pos = -1;
        }
      }
      else
      {
        b.append(m.substring(pos));
        pos = -1;
      }
    }
    return b.toString();
  }

  public ResourceBundle getResourceBundle()
  {
    return fResourceBundle;
  }
}

