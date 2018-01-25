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
 * 20060404 134913  sengpl@ca.ibm.com - Seng Phung-Lu 
 * -------- -------- -----------------------------------------------------------
 */
package org.eclipse.jst.ws.internal.consumption.ui.common;

import java.util.List;
import java.util.Vector;

import org.eclipse.jst.ws.internal.consumption.ui.widgets.object.HandlerTableItem;


public class HandlerServiceRefHelper {

  /**
   * Helper method for getting the HandlerServiceRefHolder given the name
   * @param hsrh
   * @param descName
   * @return
   */
  public static HandlerServiceRefHolder getForServiceRefName(HandlerServiceRefHolder[] hsrh, String descName){
    for (int i=0;i<hsrh.length;i++){
      String name = hsrh[i].getServiceRefName();
      if (name!=null && name.equals(descName)){
        return hsrh[i];
      }
    }
    return  null;
  }
  
  /**
   * Gets all the handler Class names associated with each service ref
   * @param hsrh
   * @return
   */
  public static String[] getAllHandlerClassNames(HandlerServiceRefHolder[] hsrh){
    Vector v = new Vector();
    for (int i=0;i<hsrh.length;i++){
      List list = hsrh[i].getHandlerList();
      if (list!=null && !list.isEmpty()){
        for (int j=0;j<list.size();j++){
          HandlerTableItem hti = (HandlerTableItem)list.get(j);
          v.add(hti.getHandlerClassName());
        }
      }
    }
    return (String[])v.toArray(new String[0]);
  }
  
  /**
   * Get all the service Ref names
   * @param hsrh
   * @return
   */
  public static String[] getAllServiceRefNames(HandlerServiceRefHolder[] hsrh){
    String[] names = new String[hsrh.length];
    for(int i=0;i<hsrh.length;i++){
      names[i]=hsrh[i].getServiceRefName();
    }
    return names;
  }
  
}
