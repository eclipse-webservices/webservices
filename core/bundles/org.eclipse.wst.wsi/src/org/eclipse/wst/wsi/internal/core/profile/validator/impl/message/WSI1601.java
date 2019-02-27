/*******************************************************************************
 * Copyright (c) 2002-2005 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   IBM - Initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsi.internal.core.profile.validator.impl.message;

import org.eclipse.wst.wsi.internal.core.profile.validator.impl.BaseMessageValidator;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.envelope.BP1601;

/**
 * WSI1601
 */
public class WSI1601 extends BP1601
{

  /**
   * @param BaseMessageValidator
   */
  public WSI1601(BaseMessageValidator impl)
  {
    super(impl);
  }
}