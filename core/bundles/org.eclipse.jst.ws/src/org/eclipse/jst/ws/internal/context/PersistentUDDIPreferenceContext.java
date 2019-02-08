/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
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
package org.eclipse.jst.ws.internal.context;

import org.eclipse.jst.ws.internal.plugin.WebServicePlugin;
import org.eclipse.wst.command.internal.env.context.PersistentContext;


public class PersistentUDDIPreferenceContext extends PersistentContext implements UDDIPreferenceContext
{
  public PersistentUDDIPreferenceContext()
  {
    super(WebServicePlugin.getInstance());
  }

  public void load()
  {
    setDefault(PREFERENCE_UDDI_CAT_DATA_COLUMN_DELIMITER, UDDIPreferenceDefaults.getUddiCatDataColumnDelimiter());
    setDefault(PREFERENCE_UDDI_CAT_DATA_STRING_DELIMITER, UDDIPreferenceDefaults.getUddiCatDataStringDelimiter());
  }

  public void setUddiCatDataColumnDelimiter(String delimiter)
  {
    setValue(PREFERENCE_UDDI_CAT_DATA_COLUMN_DELIMITER, delimiter);
  }

  public String getUddiCatDataColumnDelimiter()
  {
    return getValueAsString(PREFERENCE_UDDI_CAT_DATA_COLUMN_DELIMITER);
  }

  public void setUddiCatDataStringDelimiter(String delimiter)
  {
    setValue(PREFERENCE_UDDI_CAT_DATA_STRING_DELIMITER, delimiter);
  }

  public String getUddiCatDataStringDelimiter()
  {
    return getValueAsString(PREFERENCE_UDDI_CAT_DATA_STRING_DELIMITER);
  }
}
