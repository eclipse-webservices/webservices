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
package org.eclipse.wst.ws.internal.provisional.wsrt;

import java.util.List;
import java.util.Arrays;
import java.util.Collections;

import org.eclipse.emf.common.util.AbstractEnumerator;


public final class WebServiceState extends AbstractEnumerator
{

  public static final int UNKNOWN = 0;
  public static final int DEVELOPED = 1;
  public static final int ASSEMBLED = 2;
  public static final int DEPLOYED = 3;
  public static final int INSTALLED = 4;
  public static final int RUNNING = 5;
  
  
  public static final WebServiceState UNKNOWN_LITERAL = new WebServiceState(UNKNOWN, "UNKNOWN");
  public static final WebServiceState DEVELOPED_LITERAL = new WebServiceState(DEVELOPED, "DEVELOPED");
  public static final WebServiceState ASSEMBLED_LITERAL = new WebServiceState(ASSEMBLED, "ASSEMBLED");
  public static final WebServiceState DEPLOYED_LITERAL = new WebServiceState(DEPLOYED, "DEPLOYED");
  public static final WebServiceState INSTALLED_LITERAL = new WebServiceState(INSTALLED, "INSTALLED");
  public static final WebServiceState RUNNING_LITERAL = new WebServiceState(RUNNING, "RUNNING");

  private static final WebServiceState[] VALUES_ARRAY =
    new WebServiceState[]
    {
	  UNKNOWN_LITERAL,
	  DEVELOPED_LITERAL,
	  ASSEMBLED_LITERAL,
	  DEPLOYED_LITERAL,
	  INSTALLED_LITERAL,
	  RUNNING_LITERAL,
    };

  public static final List VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));


  public static WebServiceState get(String name)
  {
    for (int i = 0; i < VALUES_ARRAY.length; ++i)
    {
      WebServiceState result = VALUES_ARRAY[i];
      if (result.toString().equals(name))
      {
        return result;
      }
    }
    return null;
  }


  public static WebServiceState get(int value)
  {
    switch (value)
    {
      case UNKNOWN: return UNKNOWN_LITERAL;
      case DEVELOPED: return DEVELOPED_LITERAL;
      case ASSEMBLED: return ASSEMBLED_LITERAL;
	  case DEPLOYED: return DEPLOYED_LITERAL;
	  case INSTALLED: return INSTALLED_LITERAL;
	  case RUNNING: return RUNNING_LITERAL;
    }
    return null;	
  }

  /**
   * Only this class can construct instances.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private WebServiceState(int value, String name)
  {
    super(value, name);
  }

} //WebServiceState

