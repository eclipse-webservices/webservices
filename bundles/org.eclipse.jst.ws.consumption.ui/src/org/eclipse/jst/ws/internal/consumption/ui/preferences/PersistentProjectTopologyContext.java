/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060202   119780 pmoogk@ca.ibm.com - Peter Moogk
 * 20060216   127138 pmoogk@ca.ibm.com - Peter Moogk
 * 20060227   124392 rsinha@ca.ibm.com - Rupam Kuehner
 *******************************************************************************/

package org.eclipse.jst.ws.internal.consumption.ui.preferences;

import org.eclipse.jst.ws.internal.consumption.ui.plugin.WebServiceConsumptionUIPlugin;
import org.eclipse.wst.command.internal.env.context.PersistentContext;
import com.ibm.icu.util.StringTokenizer;


public class PersistentProjectTopologyContext extends PersistentContext implements ProjectTopologyContext
{

  public PersistentProjectTopologyContext () 
  {
	  super( WebServiceConsumptionUIPlugin.getInstance());
  }
  
  public void load() 
  {
    //Load the service project types
    String[] serviceIds = ProjectTopologyDefaults.getServiceTypes();
    StringBuffer serviceSb = new StringBuffer();
    for (int i = 0; i < serviceIds.length; i++)
    {
      if (i != 0)
        serviceSb.append(" ");
      serviceSb.append(serviceIds[i]);
    }
    setDefaultStringIfNoDefault(PREFERENCE_SERVICE_TYPES, serviceSb.toString());
    
    //Load the client project types
    String[] ids = ProjectTopologyDefaults.getClientTypes();
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < ids.length; i++)
    {
      if (i != 0)
        sb.append(" ");
      sb.append(ids[i]);
    }
    setDefaultStringIfNoDefault(PREFERENCE_CLIENT_TYPES, sb.toString());
    setDefault(PREFERENCE_USE_TWO_EARS, ProjectTopologyDefaults.isUseTwoEARs());
 }

  public void setServiceTypes(String[] ids)
  {
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < ids.length; i++)
    {
      if (i != 0)
        sb.append(" ");
      sb.append(ids[i]);
    }
    setValue(PREFERENCE_SERVICE_TYPES, sb.toString());
  }

  public String[] getServiceTypes()
  {
    StringTokenizer st = new StringTokenizer(getValueAsString(PREFERENCE_SERVICE_TYPES));
    String[] s = new String[st.countTokens()];
    for (int i = 0; i < s.length; i++)
      s[i] = st.nextToken();
    return s;
  }
  
  public String[] getDefaultServiceTypes()
  {
    StringTokenizer st = new StringTokenizer(getDefaultString(PREFERENCE_SERVICE_TYPES));
    String[] s = new String[st.countTokens()];
    for (int i = 0; i < s.length; i++)
      s[i] = st.nextToken();
    return s;        
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
 
 public String[] getDefaultClientTypes()
 {
   StringTokenizer st = new StringTokenizer(getDefaultString(PREFERENCE_CLIENT_TYPES));
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
    context.setServiceTypes(getServiceTypes());
    context.setDefaultServiceTypes(getDefaultServiceTypes());
	context.setClientTypes(getClientTypes());
    context.setDefaultClientTypes(getDefaultClientTypes());
	context.setUseTwoEARs(isUseTwoEARs());
	return context;
}

}
