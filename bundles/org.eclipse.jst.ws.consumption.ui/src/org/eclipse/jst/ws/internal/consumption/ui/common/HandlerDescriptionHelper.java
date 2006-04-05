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



public class HandlerDescriptionHelper {

  /**
   * Helper method for getting the HandlerDescriptorHolder given the name
   * @param hdhs
   * @param descName
   * @return
   */
  public static HandlerDescriptionHolder getForDescriptionName(HandlerDescriptionHolder[] hdhs, String descName){
    for (int i=0;i<hdhs.length;i++){
      String name = hdhs[i].getDescriptionName();
      if (name!=null && name.equals(descName)){
        return hdhs[i];
      }
    }
    return  null;
  }
  
  /**
   * Helper method for getting all description names
   * @param hdhs
   * @return
   */
  public static String[] getAllDescriptionNames(HandlerDescriptionHolder[] hdhs){
    String[] names = new String[hdhs.length];
    for(int i=0;i<hdhs.length;i++){
      names[i]=hdhs[i].getDescriptionName();
    }
    return names;
  }

  public static String[] getAllHandlerClassNames(HandlerDescriptionHolder[] hdhs){
    Vector v = new Vector();
    for (int i=0;i<hdhs.length;i++){
      List list = hdhs[i].getHandlerList();
      if (list!=null && !list.isEmpty()){
        for (int j=0;j<list.size();j++){
          HandlerTableItem hti = (HandlerTableItem)list.get(j);
          v.add(hti.getHandlerClassName());
        }
      }
    }
    return (String[])v.toArray(new String[0]);
  }
}
