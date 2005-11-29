/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jst.ws.internal.consumption.ui.preferences;

import java.util.StringTokenizer;
import org.eclipse.jst.ws.internal.consumption.ui.plugin.WebServiceConsumptionUIPlugin;
import org.eclipse.wst.command.internal.env.context.PersistentContext;


public class PersistentProjectTopologyContext extends PersistentContext implements ProjectTopologyContext
{

  public PersistentProjectTopologyContext () 
  {
	  super( WebServiceConsumptionUIPlugin.getInstance());
  }
  
  public void load() 
  {
    String[] ids = ProjectTopologyDefaults.getClientTypes();
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < ids.length; i++)
    {
      if (i != 0)
        sb.append(" ");
      sb.append(ids[i]);
    }
    setDefault(PREFERENCE_CLIENT_TYPES, sb.toString());
    setDefault(PREFERENCE_USE_TWO_EARS, ProjectTopologyDefaults.isUseTwoEARs());
 }

 public void setClientTypes(String[] ids)
 {
   StringBuffer sb = new StringBuffer();
   for (int i = 0; i < ids.length; i++)
   {
     if (i != 0)
       sb.append(" ");
     sb.append(ids[i]);
   }
   setValue(PREFERENCE_CLIENT_TYPES, sb.toString());
 }

 public String[] getClientTypes()
 {
   StringTokenizer st = new StringTokenizer(getValueAsString(PREFERENCE_CLIENT_TYPES));
   String[] s = new String[st.countTokens()];
   for (int i = 0; i < s.length; i++)
     s[i] = st.nextToken();
   return s;
 }

 public void setUseTwoEARs(boolean use)
 {
   setValue(PREFERENCE_USE_TWO_EARS, use);
 }
 public boolean isUseTwoEARs()
 {
   return getValueAsBoolean(PREFERENCE_USE_TWO_EARS);
 }

 public ProjectTopologyContext copy() {
 	TransientProjectTopologyContext context = new TransientProjectTopologyContext();
	context.setClientTypes(getClientTypes());
	context.setUseTwoEARs(isUseTwoEARs());
	return context;
}
}
