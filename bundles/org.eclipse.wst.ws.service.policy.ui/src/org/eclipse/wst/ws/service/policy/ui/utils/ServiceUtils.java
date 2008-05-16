/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20080211   218520 pmoogk@ca.ibm.com - Peter Moogk
 * 20080516   232603 pmoogk@ca.ibm.com - Peter Moogk, Clean up java doc
 *******************************************************************************/
package org.eclipse.wst.ws.service.policy.ui.utils;

import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.Vector;

import org.eclipse.wst.ws.service.policy.IServicePolicy;

/**
 * 
 * This class contains utility method for service policies.
 *
 */
public class ServiceUtils
{
  /**
   * Sorts a list of service policies.
   * 
   * @param unsortedList an unsorted list
   * @return returns a list of sorted service policies base not their long names.
   */
  public static List<IServicePolicy> sortList( List<IServicePolicy> unsortedList )
  {
    TreeSet<IServicePolicy> sortedSet = new TreeSet<IServicePolicy>( new CompareServicePolicy() );
    Vector<IServicePolicy>  sortedList = new Vector<IServicePolicy>();
    
    sortedSet.addAll( unsortedList );
    sortedList.addAll( sortedSet );
    
    return sortedList;
  }
  
  private static class CompareServicePolicy implements Comparator<IServicePolicy>
  {
    public int compare(IServicePolicy arg0, IServicePolicy arg1)
    {
      String name0 = arg0.getDescriptor().getLongName();
      String name1 = arg1.getDescriptor().getLongName();
      
      return name0.compareTo( name1 ); 
    }    
  }
}
