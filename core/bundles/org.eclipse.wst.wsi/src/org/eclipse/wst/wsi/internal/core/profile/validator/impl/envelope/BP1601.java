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
package org.eclipse.wst.wsi.internal.core.profile.validator.impl.envelope;

import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.AssertionProcess;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.BaseMessageValidator;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;
import org.eclipse.wst.wsi.internal.core.xml.XMLUtils;
import org.xml.sax.SAXException;


/**
 * BP1601.
 */
public class BP1601 extends AssertionProcess
{
  private final BaseMessageValidator validator;

  /**
   * @param BaseMessageValidator
   */
  public BP1601(BaseMessageValidator impl)
  {
    super(impl);
    this.validator = impl;
  }

  public AssertionResult validate(
    TestAssertion testAssertion,
    EntryContext entryContext)
    throws WSIException
  {
    // One-way responses will not contain a SOAP message
    if (this.validator.isOneWayResponse(entryContext))
    {
      result = AssertionResult.RESULT_NOT_APPLICABLE;
    }
    else
    {
      try
      {
        // Try to parse the SOAP message
        //Document doc = XMLUtils.parseXML(entryContext.getMessageEntry().getMessage());
        XMLUtils.parseXML(entryContext.getMessageEntry().getMessage());
      }
      catch (WSIException e)
      {
        if (e.getTargetException() instanceof SAXException)
        {
          result = AssertionResult.RESULT_FAILED;
          failureDetail =
            this.validator.createFailureDetail(
              e.getTargetException().getMessage(),
              entryContext);
        }
      }
    }

    // Return assertion result
    return validator.createAssertionResult(testAssertion, result, failureDetail);
  }
}