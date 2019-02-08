/*******************************************************************************
 * Copyright (c) 2001, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060217   127138 pmoogk@ca.ibm.com - Peter Moogk
 *******************************************************************************/

package org.eclipse.wst.ws.internal.explorer.platform.util;

import java.util.Vector;
import com.ibm.icu.text.Collator;

public class QuickSort
{
  public static final void sort(Object[] a,int lo,int hi)
  {
    Object tmp;
    int i = lo;
    int j = hi;
    Object x = a[(lo+hi)/2];
    Collator collator = Collator.getInstance();

    do
    {
      while (collator.compare(a[i].toString(),x.toString()) < 0) i++;
      while (collator.compare(a[j].toString(),x.toString()) > 0) j--;
      if (i <= j)
      {
        tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;
        i++;
        j--;
      }
    } while (i <= j);

    if (lo < j)
      sort(a,lo,j);
    if (i < hi)
      sort(a,i,hi);
  }

  /*
  * This method will use the same algorithm as above
  * with the exception that any nulls will be attached
  * at the end
  */
  public static final void sort(Vector v)
  {
    // do not sort if vector contains less than 2 elements
    if (v.size() < 2)
        return;
  
    // pulls out the nulls
    Vector nulls = new Vector();
    for (int i = 0; i < v.size(); i++) {
      Object obj = v.elementAt(i);
      if (obj == null || obj.toString() == null) {
        nulls.add(obj);
        v.remove(i);
        i--;
      }
    }

    // sort the not null objects
    Object[] objs = v.toArray();
    // do not sort if array contains less than 2 objects
    if (objs.length >= 2)
        sort(objs, 0, objs.length-1);

    // re-construct the vector from the sorted objects, then the nulls
    v.clear();
    for (int j = 0; j < objs.length; j++) {
        v.add(objs[j]);
    }
    v.addAll(nulls);
  }
}
