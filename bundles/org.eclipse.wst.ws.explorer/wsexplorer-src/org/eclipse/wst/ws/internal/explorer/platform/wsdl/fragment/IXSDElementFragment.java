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
package org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment;

public interface IXSDElementFragment extends IXSDDelegationFragment {
  public static String NIL = "nilValue";  
  public static String NIL_VALUE = "nil"; 
  
  public int getMinOccurs();
  public int getMaxOccurs();
  public boolean isNillable();
  public String getNilID();
  public boolean isNil();
  
  
}
