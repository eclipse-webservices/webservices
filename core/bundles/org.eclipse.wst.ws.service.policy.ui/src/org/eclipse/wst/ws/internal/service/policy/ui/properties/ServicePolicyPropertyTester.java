/***************************************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 **************************************************************************************************/
package org.eclipse.wst.ws.internal.service.policy.ui.properties;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.wst.ws.service.policy.ServicePolicyPlatform;

public class ServicePolicyPropertyTester extends PropertyTester
{

  public boolean test(Object receiver, String property, Object[] args, Object expectedValue)
  {
    return ServicePolicyPlatform.getInstance().isEnabled( receiver );
  }
}
