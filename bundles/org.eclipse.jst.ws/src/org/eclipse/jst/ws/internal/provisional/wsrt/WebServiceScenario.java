/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.provisional.wsrt;

import java.util.List;
import java.util.Arrays;
import java.util.Collections;

import org.eclipse.emf.common.util.AbstractEnumerator;


public final class WebServiceScenario extends AbstractEnumerator
{

  public static final int BOTTOMUP = 0;
  public static final int TOPDOWN = 1;
  public static final int CLIENT = 2;
  
  
  public static final WebServiceScenario BOTTOMUP_LITERAL = new WebServiceScenario(BOTTOMUP, "BOTTOMUP");
  public static final WebServiceScenario TOPDOWN_LITERAL = new WebServiceScenario(TOPDOWN, "TOPDOWN");
  public static final WebServiceScenario CLIENT_LITERAL = new WebServiceScenario(CLIENT, "CLIENT");


  private static final WebServiceScenario[] VALUES_ARRAY =
    new WebServiceScenario[]
    {
	  BOTTOMUP_LITERAL,
	  TOPDOWN_LITERAL,
	  CLIENT_LITERAL,
    };

  public static final List VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));


  public static WebServiceScenario get(String name)
  {
    for (int i = 0; i < VALUES_ARRAY.length; ++i)
    {
      WebServiceScenario result = VALUES_ARRAY[i];
      if (result.toString().equals(name))
      {
        return result;
      }
    }
    return null;
  }


  public static WebServiceScenario get(int value)
  {
    switch (value)
    {
      case BOTTOMUP: return BOTTOMUP_LITERAL;
      case TOPDOWN: return TOPDOWN_LITERAL;
      case CLIENT: return CLIENT_LITERAL;
    }
    return null;	
  }

  /**
   * Only this class can construct instances.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private WebServiceScenario(int value, String name)
  {
    super(value, name);
  }

} //WebServiceScenario
