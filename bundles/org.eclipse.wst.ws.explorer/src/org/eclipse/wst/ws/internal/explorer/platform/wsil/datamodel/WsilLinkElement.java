/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.ws.internal.explorer.platform.wsil.datamodel;

import java.util.Vector;
import org.apache.wsil.Abstract;
import org.apache.wsil.Link;
import org.eclipse.wst.ws.internal.datamodel.Model;

/**
* The data model element that represents 
* a WSIL Link
*/
public class WsilLinkElement extends WsilCommonElement
{
  protected Link link_;

  public WsilLinkElement(String name, Model model, Link link)
  {
    super(name, model);
    link_ = link;
  }

  public Vector getLinkAbstractLangs()
  {
    Vector v = new Vector();
    Abstract[] abstracts = link_.getAbstracts();
    for (int i = 0; i < abstracts.length; i++)
    {
      v.add(abstracts[i].getLang());
    }
    return v;
  }

  public Vector getLinkAbstracts()
  {
    Vector v = new Vector();
    Abstract[] abstracts = link_.getAbstracts();
    for (int i = 0; i < abstracts.length; i++)
    {
      v.add(abstracts[i].getText());
    }
    return v;
  }
}
