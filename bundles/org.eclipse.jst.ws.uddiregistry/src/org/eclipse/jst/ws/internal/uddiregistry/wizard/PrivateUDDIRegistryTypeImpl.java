/*******************************************************************************
 * Copyright (c) 2002, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jst.ws.internal.uddiregistry.wizard;

import java.util.Enumeration;
import java.util.Vector;

import org.eclipse.jst.ws.internal.uddiregistry.widgets.binding.PrivateUDDIWidgetBinding;
import org.eclipse.wst.command.internal.env.ui.widgets.CommandWidgetBinding;


public class PrivateUDDIRegistryTypeImpl implements org.eclipse.jst.ws.internal.ui.uddi.PrivateUDDIRegistryType
{
    public PrivateUDDIRegistryTypeImpl() {
    }

    public boolean isPrivateUDDIRegistryInstalled() {
        PrivateUDDIRegistryType[] types = PrivateUDDIRegistryTypeRegistry.getInstance().getTypes();
        for (int i = 0; i < types.length; i++) {
            if (types[i].isPrivateUDDIRegistryInstalled())
                return true;
        }
        return false;
    }

    public String[] getPrivateUDDIRegistryInquiryAPI() {
        Vector inquiryAPIVector = new Vector();
        PrivateUDDIRegistryType[] types = PrivateUDDIRegistryTypeRegistry.getInstance().getTypes();
        for (int i = 0; i < types.length; i++) {
            if (types[i].isPrivateUDDIRegistryInstalled()) {
                String inquiryAPI = types[i].getInquiryAPI();
                if (inquiryAPI != null && inquiryAPI.startsWith("http"))
                    inquiryAPIVector.add(inquiryAPI);
            }
        }

        String[] inquiryAPIArray = new String[inquiryAPIVector.size()];
        Enumeration e = inquiryAPIVector.elements();
        int j = 0;
        while(e.hasMoreElements()) {
            inquiryAPIArray[j] = (String)e.nextElement();
            j++;
        }
        return inquiryAPIArray;
    }

    public String[] getPrivateUDDIRegistryPublishAPI() {
        Vector publishAPIVector = new Vector();
        PrivateUDDIRegistryType[] types = PrivateUDDIRegistryTypeRegistry.getInstance().getTypes();
        for (int i = 0; i < types.length; i++) {
            if (types[i].isPrivateUDDIRegistryInstalled()) {
                String publishAPI = types[i].getPublishAPI();
                if (publishAPI != null && publishAPI.startsWith("http"))
                    publishAPIVector.add(publishAPI);
            }
        }

        String[] publishAPIArray = new String[publishAPIVector.size()];
        Enumeration e = publishAPIVector.elements();
        int j = 0;
        while(e.hasMoreElements()) {
            publishAPIArray[j] = (String)e.nextElement();
            j++;
        }
        return publishAPIArray;
    }
    
    public CommandWidgetBinding getBinding()
    {
      return new PrivateUDDIWidgetBinding();
    }
}
