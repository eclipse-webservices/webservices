/*******************************************************************************
 * Copyright (c) 2002, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
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
