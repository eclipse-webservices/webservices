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

import java.util.StringTokenizer;
import java.util.Vector;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IPluginRegistry;
import org.eclipse.core.runtime.Platform;

public class PrivateUDDIRegistryTypeRegistry {

    private static PrivateUDDIRegistryTypeRegistry instance_;
    private IConfigurationElement[] configElements_;

    private PrivateUDDIRegistryTypeRegistry() {
    }
  
    /**
    * Returns a singleton instance of this class.
    * @return A singleton WebServiceClientTypeRegistry object.
    */
    public static PrivateUDDIRegistryTypeRegistry getInstance() {
        if (instance_ == null) {
            instance_ = new PrivateUDDIRegistryTypeRegistry();
            instance_.getConfigElements();
        }
        return instance_;
    }
  
    public IConfigurationElement[] getConfigElements() {
        IPluginRegistry reg = Platform.getPluginRegistry();
        configElements_ = reg.getConfigurationElementsFor(
                                     "org.eclipse.jst.ws.uddiregistry",
                                     "privateUDDIRegistryType");
        Vector v = new Vector();
        for (int i = 0; i < configElements_.length; i++)
        {
          int weight = getWeight(configElements_[i]);
          int index = -1;
          for (int j = v.size()-1; j >= 0; j--)
          {
            if (weight > getWeight((IConfigurationElement)v.get(j)))
              index = j;
            else
              break;
          }
          if (index != -1)
            v.add(index, configElements_[i]);
          else
            v.add(configElements_[i]);
        }
        v.copyInto(configElements_);
        return configElements_;
    }

    private int getWeight(IConfigurationElement e)
    {
      try
      {
        return Integer.parseInt(e.getAttribute("weight"));
      }
      catch (NumberFormatException nfe)
      {
        return -1;
      }
    }

    private IConfigurationElement getConfigElementByID(String id) {
        for (int i = 0; i <configElements_.length; i++) {
            if (configElements_[i].getAttribute("id").equals(id))
                return configElements_[i];
        }
        return null;
    }

    public PrivateUDDIRegistryType[] getTypes() {
        Vector types = new Vector();
        for (int i = 0; i < configElements_.length; i++) {
            try {
                Object typeObj = configElements_[i].createExecutableExtension("class");
                if (typeObj instanceof PrivateUDDIRegistryType)
                    types.add(typeObj);
            }
            catch (Exception e) {}
        }

        PrivateUDDIRegistryType[] typesArray = new PrivateUDDIRegistryType[types.size()];
        for (int j = 0; j < types.size(); j++) {
            typesArray[j] = (PrivateUDDIRegistryType)types.elementAt(j);
        }
        return typesArray;
    }

    public PrivateUDDIRegistryType getTypeByID(String id) {
        try {
            Object typeObj = getConfigElementByID(id).createExecutableExtension("class");
            if (typeObj instanceof PrivateUDDIRegistryType)
                return (PrivateUDDIRegistryType)typeObj; 
        }
        catch (Exception e) {}
        return null;
    }

    public String[] getSupportedServerFactoryIDByID(String id) {
        IConfigurationElement configElement = getConfigElementByID(id);
        if (configElement == null)
            return new String[0];
        Vector idVector = new Vector();
        StringTokenizer st = new StringTokenizer(configElement.getAttribute("serverFactoryID"), ",");
        while (st.hasMoreTokens()) {
            idVector.add(st.nextToken());
        }
        String[] ids = new String[idVector.size()];
        for (int i = 0; i < idVector.size(); i++) {
            ids[i] = (String)idVector.elementAt(i);
        }
        return ids;
    }

}
