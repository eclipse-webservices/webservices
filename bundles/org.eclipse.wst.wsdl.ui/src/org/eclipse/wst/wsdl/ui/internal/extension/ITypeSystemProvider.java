/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.extension;

import java.util.List;

import org.eclipse.wst.wsdl.Definition;

public interface ITypeSystemProvider
{                                                      
  public static final int UNKNOWN_TYPE = 0x0;
  public static final int BUILT_IN_TYPE = 0x1;
  public static final int USER_DEFINED_SIMPLE_TYPE = 0x2;
  public static final int USER_DEFINED_COMPLEX_TYPE = 0x4;

  public List getAvailableTypeNames(Definition definition, int typeNameCategory);
  public List getAvailableElementNames(Definition definition);  
  public int getCategoryForTypeName(Definition definition, String typeName);
}