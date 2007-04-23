/*******************************************************************************
 * Copyright (c) 2002, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.impl;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataException;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataParser;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.IXSDAttributeFragment;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.IXSDFragment;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.IXSDMapFragment;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.XSDToFragmentConfiguration;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.XSDToFragmentController;

public abstract class XSDMapFragment extends XSDFragment implements IXSDMapFragment {
  private XSDToFragmentController controller_;
  private Vector fragmentsOrder_;
  private Hashtable fragments_;
  private Hashtable attributeFragments_;

  public XSDMapFragment(String id, String name, XSDToFragmentConfiguration config, XSDToFragmentController controller) {
    super(id, name, config);
    controller_ = controller;
    fragmentsOrder_ = new Vector();
    fragments_ = new Hashtable();
    attributeFragments_ = new Hashtable();
  }

  public boolean processParameterValues(MultipartFormDataParser parser) throws MultipartFormDataException {
    boolean valuesValid = true;
    String[] params = parser.getParameterValues(getID());
    Vector frags = new Vector();
    Vector attfrags = new Vector();
    if (params != null) {
      for (int i = 0; i < params.length; i++) {
        if (params[i] != null) {
          IXSDFragment frag = getFragment(params[i]);
          if (frag != null) {
            frags.add(frag);
            if (!frag.processParameterValues(parser))
              valuesValid = false;
          }
          else {
          	IXSDAttributeFragment afrag = getAttributeFragment(params[i]);
          	if (afrag != null) {
          	  attfrags.add(afrag);
              if (!afrag.processParameterValues(parser))
                 valuesValid = false;
            }  
          
          }
          	
        }
      }
    }
    removeAllFragments();
    removeAllAttributeFragments();
    addAttributeFragments(attfrags);
    addFragments(frags);
    return valuesValid;
  }

  public void setParameterValues(String paramKey, String[] params) {
    IXSDFragment frag = getFragment(paramKey);
    if (frag != null)
      frag.setParameterValues(paramKey, params);
  }

  public String[] getParameterValues(String paramKey) {
    IXSDFragment frag = getFragment(paramKey);
    return (frag != null) ? frag.getParameterValues(paramKey) : null;
  }

  public boolean validateAllParameterValues() {
    IXSDFragment[] fragments = getAllFragments();
    for (int i = 0; i < fragments.length; i++) {
      if (!fragments[i].validateAllParameterValues())
        return false;
    }
    return true;
  }

  public boolean validateParameterValues(String paramKey) {
    IXSDFragment frag = getFragment(paramKey);
    return (frag != null) ? frag.validateParameterValues(paramKey) : true;
  }

  public boolean validateParameterValue(String paramKey, int paramIndex) {
    IXSDFragment frag = getFragment(paramKey);
    return (frag != null) ? frag.validateParameterValue(paramKey, paramIndex) : true;
  }

  public void setXSDToFragmentController(XSDToFragmentController controller) {
    controller_ = controller;
  }

  public XSDToFragmentController getXSDToFragmentController() {
    return controller_;
  }

  protected void addFragment(String id, IXSDFragment frag) {
    addFragment(id, frag, true);
  }

  protected void addFragments(String[] id, IXSDFragment[] frags) {
    addFragments(id, frags, true);
  }

  protected void addFragment(IXSDFragment frag) {
    addFragment(frag, true);
  }

  protected void addFragments(IXSDFragment[] frags) {
    addFragments(frags, true);
  }

  protected void addFragments(Vector frags) {
    addFragments(frags, true);
  }

  protected void addFragment(String id, IXSDFragment frag, boolean addToController) {
    fragmentsOrder_.add(id);
    fragments_.put(id, frag);
    controller_.addToCache(id, frag);
  }
  
  protected void addAttributeFragment(String id, IXSDFragment frag) {
    attributeFragments_.put(id, frag);
    controller_.addToCache(id, frag);
  }
  
  public IXSDAttributeFragment[] getAllAttributeFragments() {
    IXSDAttributeFragment[] fragments = new XSDAttributeFragment[attributeFragments_.size()];
    Iterator it = attributeFragments_.values().iterator();
    int i = 0;
    while(it.hasNext()){
      fragments[i] = (IXSDAttributeFragment)it.next();
      i++; 
    }
    return fragments;
  }
  
  protected void addFragments(String[] id, IXSDFragment[] frags, boolean addToController) {
    for (int i = 0; i < id.length && i < frags.length; i++) {
      addFragment(id[i], frags[i], addToController);
    }
  }

  protected void addFragment(IXSDFragment frag, boolean addToController) {
    addFragment(frag.getID(), frag, addToController);
  }

  protected void addFragments(IXSDFragment[] frags, boolean addToController) {
    for (int i = 0; i < frags.length; i++) {
      addFragment(frags[i].getID(), frags[i], addToController);
    }
  }

  protected void addFragments(Vector frags, boolean addToController) {
    Iterator it = frags.iterator();
    while (it.hasNext()) {
      Object obj = it.next();
      if (obj instanceof IXSDFragment) {
        IXSDFragment frag = (IXSDFragment)obj;
        addFragment(frag.getID(), frag, addToController);
      }
    }
  }

  protected void addAttributeFragments(Vector frags) {
    Iterator it = frags.iterator();
    while (it.hasNext()) {
      Object obj = it.next();
      if (obj instanceof IXSDAttributeFragment) {
        IXSDAttributeFragment frag = (IXSDAttributeFragment)obj;
        addAttributeFragment(frag.getID(), frag);
      }
    }
  }
  
  public String[] getFragmentsOrder() {
    String[] fragmentsOrder = new String[fragmentsOrder_.size()];
    for (int i = 0; i < fragmentsOrder.length; i++) {
      fragmentsOrder[i] = (String)fragmentsOrder_.get(i);
    }
    return fragmentsOrder;
  }

  protected boolean setFragmentsOrder(String[] fragmentsOrder) {
    if (fragmentsOrder.length != fragmentsOrder_.size())
      return false;
    Vector fragmentsOrderCopy = new Vector(fragmentsOrder_);
    for (int i = 0; i < fragmentsOrder.length; i++) {
      if (!fragmentsOrderCopy.remove(fragmentsOrder[i]))
        return false;
    }
    fragmentsOrder_.clear();
    for (int j = 0; j < fragmentsOrder.length; j++) {
      fragmentsOrder_.add(fragmentsOrder[j]);
    }
    return true;
  }

  public IXSDAttributeFragment getAttributeFragment(String id) {
    return (IXSDAttributeFragment)attributeFragments_.get(id);
  }
  
  public IXSDFragment getFragment(String id) {
    return (IXSDFragment)fragments_.get(id);
  }

  public IXSDFragment[] getFragments(String[] ids) {
    IXSDFragment[] frags = new IXSDFragment[ids.length];
    for (int i = 0; i < frags.length; i++) {
      frags[i] = getFragment(ids[i]);
    }
    return frags;
  }

  public IXSDFragment[] getAllFragments() {
    IXSDFragment[] fragments = new IXSDFragment[fragmentsOrder_.size()];
    for (int i = 0; i < fragmentsOrder_.size(); i++) {
      fragments[i] = getFragment((String)fragmentsOrder_.get(i));
    }
    return fragments;
  }

  protected void removeFragment(String id) {
    removeFragment(id, true);
  }

  protected void removeFragments(String[] ids) {
    removeFragments(ids, true);
  }

  protected void removeAllFragments() {
    removeAllFragments(true);
  }

  protected void removeFragment(String id, boolean removeFromController) {
    fragmentsOrder_.remove(id);
    fragments_.remove(id);
    if (removeFromController)
      controller_.removeFromCache(id);
  }

  protected void removeFragments(String[] ids, boolean removeFromController) {
    for (int i = 0; i < ids.length; i++) {
      removeFragment(ids[i], removeFromController);
    }
  }

  protected void removeAllFragments(boolean removeFromController) {
    if (removeFromController)
    {
      Enumeration ids = fragments_.keys();
      while (ids.hasMoreElements())
      {
        String id = (String)ids.nextElement();
        controller_.removeFromCache(id);
      }
    }
    fragmentsOrder_.clear();
    fragments_.clear();
  }
  
  protected void removeAllAttributeFragments() {
      Enumeration ids = attributeFragments_.keys();
      while (ids.hasMoreElements())
      {
        String id = (String)ids.nextElement();
        controller_.removeFromCache(id);
      }
    
      attributeFragments_.clear();
  }
  
  
}
