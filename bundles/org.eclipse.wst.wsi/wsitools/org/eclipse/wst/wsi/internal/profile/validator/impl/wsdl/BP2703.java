/*******************************************************************************
 * Copyright (c) 2002-2005 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   IBM - Initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsi.internal.profile.validator.impl.wsdl;

import java.util.Iterator;
import java.util.Map;

import javax.wsdl.Definition;

import org.eclipse.wst.wsi.internal.WSIConstants;
import org.eclipse.wst.wsi.internal.WSIException;
import org.eclipse.wst.wsi.internal.analyzer.AssertionFailException;
import org.eclipse.wst.wsi.internal.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.profile.validator.impl.AssertionProcess;
import org.eclipse.wst.wsi.internal.report.AssertionResult;
import org.eclipse.wst.wsi.internal.util.TestUtils;
import org.eclipse.wst.wsi.internal.util.Utils;
import org.eclipse.wst.wsi.internal.xml.XMLUtils;
import org.xml.sax.SAXException;


/**
 * BP2703.
 *   <context>For a candidate description within a WSDL document, if it uses the WSDL namespace</context>
 *   <assertionDescription>The definition conforms to the WSDL schema located at http://schemas.xmlsoap.org/wsdl/2003-02-11.xsd</assertionDescription>
 */
public class BP2703 extends AssertionProcess
{
  private final WSDLValidatorImpl validator;

  /**
   * @param WSDLValidatorImpl
   */
  public BP2703(WSDLValidatorImpl impl)
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
      Definition def = (Definition) entryContext.getEntry().getEntryDetail();

      if (def == null)
      {
        throw new AssertionFailException("Definition null");
      }

      String wsdlURI = entryContext.getEntry().getReferenceID();

      Map namespaces = def.getNamespaces();
      for (Iterator iter = namespaces.values().iterator(); iter.hasNext();)
      {
        String ns = (String) iter.next();

        if (WSIConstants.NS_URI_WSDL.equalsIgnoreCase(ns))
        {
        	XMLUtils.parseXMLDocument(wsdlURI, TestUtils.getWSDLSchemaLocation());
        }

        if (WSIConstants.NS_URI_WSDL_SOAP.equalsIgnoreCase(ns))
        {
        	XMLUtils.parseXMLDocument(wsdlURI, TestUtils.getWSDLSOAPSchemaLocation());
        }
      }
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