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

import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.AssertionProcess;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.BaseMessageValidator;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;

/**
 * AP1946
 *
 * <context>For a candidate multipart/related message
 * containing non-root parts</context>
 * <assertionDescription>The candidate multipart/related message
 * containing non-root parts.</assertionDescription>
 */
public class AP1946 extends AssertionProcess
{
  private final BaseMessageValidator validator;

  /**
   * @param WSDLValidatorImpl
   */
  public AP1946(BaseMessageValidator impl)
  {
    super(impl);
    this.validator = impl;
  }

  /* Validates the test assertion.
  * @see org.wsi.test.profile.validator.impl.BaseValidatorImpl.AssertionProcess#validate(org.wsi.test.profile.TestAssertion, org.wsi.test.profile.validator.EntryContext)
  */
  public AssertionResult validate(
    TestAssertion testAssertion,
    EntryContext entryContext)
    throws WSIException
  {
    // If there are more than one MIME parts, the assertion passed 
    if (entryContext.getMessageEntry().getMimeParts().count() > 1)
    {
      failureDetail = validator.createFailureDetail(
          testAssertion.getDetailDescription(), entryContext);
    }
    // The assertion is not applicable
    else
    {
      result = AssertionResult.RESULT_NOT_APPLICABLE;
    }
    // Return assertion result
    return validator.createAssertionResult(
      testAssertion, result, failureDetail);
  }
}