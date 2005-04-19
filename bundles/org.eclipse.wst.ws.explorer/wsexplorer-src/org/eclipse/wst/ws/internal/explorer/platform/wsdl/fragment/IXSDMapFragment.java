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
package org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment;

public interface IXSDMapFragment extends IXSDFragment {
  public void setXSDToFragmentController(XSDToFragmentController controller);
  public XSDToFragmentController getXSDToFragmentController();

  public String createInstance();

  public String[] getFragmentsOrder();
  public IXSDFragment getFragment(String id);
  public IXSDFragment[] getFragments(String[] ids);
  public IXSDFragment[] getAllFragments();
}
