/*******************************************************************************
 * Copyright (c) 2002-2006 IBM Corporation and others.
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
package org.eclipse.wst.wsi.internal.core.profile.validator.impl.wsdl;

import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.AssertionProcess;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;
import org.eclipse.wst.wsi.internal.core.util.Utils;
import org.xml.sax.SAXException;

/**
  * BP2700.
  *   <context>For a candidate wsdl:definitions element within a WSDL document.</context>
  *   <assertionDescription>The wsdl:definitions is a well-formed XML 1.0 document.</assertionDescription>
  */
public class BP2700 extends AssertionProcess
{
  private final WSDLValidatorImpl validator;

  /**
   * @param WSDLValidatorImpl
   */
  public BP2700(WSDLValidatorImpl impl)
  {
    super(impl);
    this.validator = impl;
  }

  /* (non-Javadoc)
  * @see org.wsi.test.profile.validator.impl.BaseValidatorImpl.AssertionProcess#validate(org.wsi.test.profile.TestAssertion, org.wsi.test.profile.validator.EntryContext)
  */
  public AssertionResult validate(
    TestAssertion testAssertion,
    EntryContext entryContext)
    throws WSIException
  {

    result = AssertionResult.RESULT_PASSED;

    try
    {
      //String wsdlURI = entryContext.getWSDLDocument().getDefinitions().getDocumentBaseURI();  
      // TEMP: Need to define a better way to get the WSDL URI
      String wsdlURI = entryContext.getEntry().getReferenceID();
      // non-validating parse
      validator.parseXMLDocumentURL(wsdlURI, null);
    }

    catch (WSIException e)
    {
      if (e.getTargetException() instanceof SAXException)
      {
        result = AssertionResult.RESULT_FAILED;
        failureDetail =
          this.validator.createFailureDetail(
            Utils.getExceptionDetails(e.getTargetException()),
            entryContext);
      }
    }

    catch (Exception e)
    {
      result = AssertionResult.RESULT_FAILED;
      failureDetail =
        this.validator.createFailureDetail(Utils.getExceptionDetails(e), entryContext);
    }

    // Return assertion result
    return validator.createAssertionResult(testAssertion, result, failureDetail);
  }
}