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

import org.eclipse.wst.wsi.internal.core.WSIConstants;
import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.analyzer.AssertionFailException;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.AssertionProcess;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.BaseMessageValidator;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;
import org.eclipse.wst.wsi.internal.core.xml.XMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
   * BP1306.
   * The soap:Fault element does not have children other than soap:faultcode, soap:faultstring, soap:faultactor or soap:detail.
   */
public class BP1306 extends AssertionProcess
{
  private final BaseMessageValidator validator;

  /**
   * @param BaseMessageValidator
   */
  public BP1306(BaseMessageValidator impl)
  {
    super(impl);
    this.validator = impl;
  }

  public AssertionResult validate(
    TestAssertion testAssertion,
    EntryContext entryContext)
    throws WSIException
  {

    Document doc;
    // Check if this is one way response
    // or message is mepty or invalid
    if (this.validator.isOneWayResponse(entryContext)
 	    || (doc = entryContext.getMessageEntryDocument()) == null)
    {
      result = AssertionResult.RESULT_NOT_APPLICABLE;
    }
    else
    {
      // look for <soap:Fault> element:
      NodeList faultList =
        doc.getElementsByTagNameNS(
          WSIConstants.NS_URI_SOAP,
          XMLUtils.SOAP_ELEM_FAULT);
      if ((faultList == null) || (faultList.getLength() == 0))
      {
        // Response does not contain a soap:fault
        result = AssertionResult.RESULT_NOT_APPLICABLE;
      }
      else
      {
        // check that no soap:fault contains only faultcode, faultstring, faultactor, detail.
        // If it does, further check that it is unqualified.
        // For each <soap:Fault>
        try
        {
          for (int n = 0; n < faultList.getLength(); n++)
          {
            for (Node child = faultList.item(n).getFirstChild();
              child != null;
              child = child.getNextSibling())
            {
              if ((child.getNodeType() == Node.ELEMENT_NODE)
                && !(child
                  .getLocalName()
                  .equalsIgnoreCase(XMLUtils.SOAP_ELEM_FAULT_CODE)
                  || child.getLocalName().equalsIgnoreCase(
                    XMLUtils.SOAP_ELEM_FAULT_STRING)
                  || child.getLocalName().equalsIgnoreCase(
                    XMLUtils.SOAP_ELEM_FAULT_ACTOR)
                  || child.getLocalName().equalsIgnoreCase(
                    XMLUtils.SOAP_ELEM_FAULT_DETAIL)))
              {
                // PB: This is checked in 1316 now
                //|| (child.getNamespaceURI() != null)) {
                // Assertion failed
                throw new AssertionFailException(
                  entryContext.getMessageEntry().getMessage());
              }
            }
          }
        }
        catch (AssertionFailException e)
        {
          result = AssertionResult.RESULT_FAILED;
          failureDetail = this.validator.createFailureDetail(e.getMessage(), entryContext);
        }
      }
    }

    // Return assertion result
    return validator.createAssertionResult(testAssertion, result, failureDetail);
  }
}