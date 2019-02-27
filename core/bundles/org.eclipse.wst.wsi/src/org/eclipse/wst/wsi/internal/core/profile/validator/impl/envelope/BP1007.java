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
import org.w3c.dom.Document;


/**
 * BP1007
 *
 * <context>For a candidate message, in the message log file</context>
 * <assertionDescription>DTDs relating to soap:header or soap:body documents, are not present in the message: no DOCTYPE element is present.</assertionDescription>
 */
public class BP1007 extends AssertionProcess
{
  private final BaseMessageValidator validator;

  /**
   * @param BaseMessageValidator
   */
  public BP1007(BaseMessageValidator impl)
  {
    super(impl);
    this.validator = impl;
  }

  public AssertionResult validate(
    TestAssertion testAssertion,
    EntryContext entryContext)
    throws WSIException
  {
    // Parse log message 
    Document doc = entryContext.getMessageEntryDocument();
    if (doc == null)
    {
      // message is empty or invalid, the assertion is not applicable
      result = AssertionResult.RESULT_NOT_APPLICABLE;
    }
    // else if the message contains a Document Type Declaration, the assertion failed
    else if (doc.getDoctype() != null)
    {
      result = AssertionResult.RESULT_FAILED;
      failureDetail = validator.createFailureDetail(
        testAssertion.getFailureDetailDescription(), entryContext);
    }
    // Return assertion result
    return validator.createAssertionResult(
      testAssertion, result, failureDetail);
  }
}