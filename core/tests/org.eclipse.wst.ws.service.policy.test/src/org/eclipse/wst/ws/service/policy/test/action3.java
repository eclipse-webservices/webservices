/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
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
 * 20071120   196997 pmoogk@ca.ibm.com - Peter Moogk, Initial coding.
 *******************************************************************************/
package org.eclipse.wst.ws.service.policy.test;

import org.eclipse.core.runtime.Status;
import org.eclipse.wst.ws.service.policy.IServicePolicy;
import org.eclipse.wst.ws.service.policy.ui.IQuickFixAction;

public class action3 implements IQuickFixAction
{

  public void action(IServicePolicy policy)
  {
    policy.setStatus( Status.OK_STATUS );
  }
}
