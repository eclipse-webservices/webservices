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

package org.eclipse.jst.ws.internal.consumption.common;

import java.util.Hashtable;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.wst.common.environment.EnvironmentService;
import org.eclipse.wst.common.environment.Log;



/**
* This is the registry of WebServiceStartServerType objects.
*/
public class WebServiceStartServerRegistry
{

  private static WebServiceStartServerRegistry instance_;
  private Hashtable StartServerTypes;

  //
  // Loads WebServiceStartServerType objects into this registry.
  // See method getInstance().
  //
  private void load ()
  {
    StartServerTypes = new Hashtable();
    IExtensionRegistry reg = Platform.getExtensionRegistry();
    IConfigurationElement[] config = reg.getConfigurationElementsFor(
                                     "org.eclipse.jst.ws.consumption",
                                     "webServiceStartServerType");

    for(int idx=0; idx<config.length; idx++) 
    {
      IConfigurationElement elem = config[idx];
      try 
      {
          String factoryId_ = elem.getAttribute("factoryId");
		  StartServerTypes.put(factoryId_, elem);
	  
      } 
      catch (Exception e)
      {
        Log log = EnvironmentService.getEclipseLog();
        log.log(Log.ERROR, 5047, this, "load", e);
      }
      
    }
  }

  /**
  * Returns a singleton instance of this class.
  * @return A singleton WebServiceStartServerRegistry object.
  */
  public static WebServiceStartServerRegistry getInstance ()
  {
    if (instance_ == null)
    {
      instance_ = new WebServiceStartServerRegistry();
      instance_.load();
    }
    return instance_;
  }

   public Object getServerStartByTypeId (String typeID) throws CoreException
   {
    if (typeID==null)
      return null;
    return ((IConfigurationElement)StartServerTypes.get(typeID)).createExecutableExtension("class");
  }
  
   public boolean isRemoveEARRequired(String typeId) throws CoreException {
   	if (typeId!=null) {
   		IConfigurationElement elem = (IConfigurationElement)StartServerTypes.get(typeId);
   		if (elem!=null){
   			String value = elem.getAttribute("removeEAR");
   			if (value!=null){
   				return Boolean.valueOf(value).booleanValue();
   			}
   		}
   	}
	return true;   		 
   }   
}




