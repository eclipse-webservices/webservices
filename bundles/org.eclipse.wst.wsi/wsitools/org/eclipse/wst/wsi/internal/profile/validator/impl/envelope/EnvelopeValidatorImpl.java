/*******************************************************************************
 * Copyright (c) 2002-2003 IBM Corporation, Parasoft and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   IBM      - Initial API and implementation
 *   Parasoft - Initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsi.internal.profile.validator.impl.envelope;

import org.eclipse.wst.wsi.internal.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.profile.validator.EnvelopeValidator;
import org.eclipse.wst.wsi.internal.profile.validator.impl.BaseMessageValidator;
import org.eclipse.wst.wsi.internal.util.EntryType;

/**
 * @version 1.0
 */
public class EnvelopeValidatorImpl
  extends BaseMessageValidator
  implements EnvelopeValidator
{
  /* (non-Javadoc)
   * @see org.wsi.test.profile.validator.impl.BaseValidatorImpl#isPrimaryEntryTypeMatch(org.wsi.test.profile.TestAssertion, org.wsi.test.profile.validator.EntryContext)
   */
  protected boolean isPrimaryEntryTypeMatch(
    TestAssertion testAssertion,
    EntryContext entryContext)
  {
    boolean match = false;

    // Verify that the entry and test assertion have the same primary context
    if ((testAssertion.getEntryTypeName().equals(EntryType.TYPE_ENVELOPE_ANY))
      || (testAssertion.getEntryTypeName().equals(EntryType.TYPE_ENVELOPE_REQUEST)
        && (entryContext
          .getEntry()
          .getEntryType()
          .getTypeName()
          .equals(EntryType.TYPE_MESSAGE_REQUEST)))
      || (testAssertion.getEntryTypeName().equals(EntryType.TYPE_ENVELOPE_RESPONSE)
        && (entryContext
          .getEntry()
          .getEntryType()
          .getTypeName()
          .equals(EntryType.TYPE_MESSAGE_RESPONSE))))
    {
      match = true;
    }

    return match;
  }
}