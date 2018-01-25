/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.wsdl.tests.performance.scalability;

import org.eclipse.wst.common.tests.performance.internal.scalability.RepeatOpenEditorTestCase;
import org.eclipse.wst.wsdl.tests.performance.PerformancePlugin;

public abstract class RepeatOpenWSDLEditorTestCase extends RepeatOpenEditorTestCase
{
  protected String getEditorId()
  {
    return PerformancePlugin.WSDL_EDITOR_ID;
  }

  protected String getBundleId()
  {
    return PerformancePlugin.BUNDLE_ID;
  }

  protected String getFilePath()
  {
    return "data/100KB.wsdl";
  }
}
