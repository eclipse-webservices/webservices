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

import javax.wsdl.Definition;
import javax.xml.namespace.QName;

import org.eclipse.wst.wsi.internal.WSIConstants;
import org.eclipse.wst.wsi.internal.WSIException;
import org.eclipse.wst.wsi.internal.WSITag;
import org.eclipse.wst.wsi.internal.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.profile.validator.impl.AssertionProcess;
import org.eclipse.wst.wsi.internal.report.AssertionResult;
import org.eclipse.wst.wsi.internal.util.Utils;
import org.eclipse.wst.wsi.internal.xml.XMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 * BP2018. 
 * <context>For a candidate Web service definition</context>
 * <assertionDescription>The wsdl:types element occurs either as the first child in the WSDL namespace of the wsdl:definitions element if no wsdl:documentation or wsdl:import element is present; or immediately following the wsdl:documentation element(s) if they are present but wsdl:import(s) are not, or immediately following both the wsdl:documentation and wsdl:import elemen(s) if present.</assertionDescription>
 */
public class BP2018 extends AssertionProcess implements WSITag
{
  private final WSDLValidatorImpl validator;

  /**
   * @param WSDLValidatorImpl
   */
  public BP2018(WSDLValidatorImpl impl)
  {
    super(impl);
    this.validator = impl;
  }

  /*
   * @param el - xml element
   * @return if element is extensibility element returns true.
   */
  private boolean isExtensibilityElement(Element el)
  {
    boolean isEx = true;
    isEx = isEx && !XMLUtils.equals(el, WSDL_BINDING);
    isEx = isEx && !XMLUtils.equals(el, WSDL_DEFINITIONS);
    isEx = isEx && !XMLUtils.equals(el, WSDL_DOCUMENTATION);
    isEx = isEx && !XMLUtils.equals(el, WSDL_FAULT);
    isEx = isEx && !XMLUtils.equals(el, WSDL_IMPORT);
    isEx = isEx && !XMLUtils.equals(el, WSDL_INPUT);
    isEx = isEx && !XMLUtils.equals(el, WSDL_MESSAGE);
    isEx = isEx && !XMLUtils.equals(el, WSDL_OPERATION);
    isEx = isEx && !XMLUtils.equals(el, WSDL_OUTPUT);
    isEx = isEx && !XMLUtils.equals(el, WSDL_PART);
    isEx = isEx && !XMLUtils.equals(el, WSDL_PORT);
    isEx = isEx && !XMLUtils.equals(el, WSDL_PORTTYPE);
    isEx = isEx && !XMLUtils.equals(el, WSDL_SERVICE);
    isEx = isEx && !XMLUtils.equals(el, WSDL_TYPES);

    return isEx;
  }

  /*
   * Create failed report.
  * @param el - xml element
  * @param entryContext - Entry context
  */
  private void createFailed(
    String message,
    Element el,
    EntryContext entryContext)
  {
    QName context =
      (el != null)
        ? new QName(el.getNamespaceURI(), el.getLocalName())
        : new QName("definition");
    result = AssertionResult.RESULT_FAILED;
    failureDetail = this.validator.createFailureDetail(message, entryContext);
  }

  /**
   * Validates the test assertion.
   * @see org.eclipse.wst.wsi.test.profile.validator.impl.BaseValidatorImpl.AssertionProcess#validate(org.wsi.test.profile.TestAssertion, org.wsi.test.profile.validator.EntryContext)
   */
  public AssertionResult validate(
    TestAssertion testAssertion,
    EntryContext entryContext)
    throws WSIException
  {
    result = AssertionResult.RESULT_PASSED;

    // Get the location of the WSDL document
    Definition definition =
      (Definition) entryContext.getEntry().getEntryDetail();
    try
    {
      // Parse the WSDL document as an XML file
      Document doc =
        validator.parseXMLDocumentURL(definition.getDocumentBaseURI(), null);

      Element root = doc.getDocumentElement(); // get definition

      Element types = XMLUtils.findChildElement((Element) root, WSDL_TYPES);

      if (types != null)
      {
        Element el = XMLUtils.findPreviousSibling(types);
        while (isExtensibilityElement(el) && el != null)
          if (el != null)
            el = XMLUtils.findPreviousSibling(el);

        boolean documentIsPresent =
          (XMLUtils.findChildElement((Element) root, WSDL_DOCUMENTATION)
            != null);
        boolean importIsPresent =
          (XMLUtils.findChildElement((Element) root, WSDL_IMPORT) != null);

        if (importIsPresent)
          if (!XMLUtils.equals(el, WSDL_IMPORT))
          {
            createFailed(
              "Types element can not follow import element.",
              el,
              entryContext);
            return validator.createAssertionResult(
              testAssertion,
              result,
              failureDetail);
          }
          else
            el = XMLUtils.findPreviousSibling(el);

        while (isExtensibilityElement(el) && el != null)
          if (el != null)
            el = XMLUtils.findPreviousSibling(el);

        if (documentIsPresent)
          if (!XMLUtils.equals(el, WSDL_DOCUMENTATION))
          {
            createFailed(
              "Types element must follow only a documentation element.",
              el,
              entryContext);
            return validator.createAssertionResult(
              testAssertion,
              result,
              failureDetail);
          }
          else
            el = XMLUtils.findPreviousSibling(el);

        if (!importIsPresent && !documentIsPresent && el != null)
          createFailed(
            "Types element must follow only a documentation element.",
            el,
            entryContext);

        if (importIsPresent
          && documentIsPresent
          && el != null
          && !XMLUtils.equals(el, WSDL_DEFINITIONS)
          && el.getNamespaceURI().equals(WSIConstants.NS_URI_WSDL))
          createFailed(
            "Types element must not follow the "
              + el.getTagName()
              + " element.",
            el,
            entryContext);
      }
    }

    catch (Throwable t)
    {
      result = AssertionResult.RESULT_NOT_APPLICABLE;
      failureDetail =
        this.validator.createFailureDetail(
          "An error occurred while processing the document at "
            + definition.getDocumentBaseURI()
            + ".\n\n"
            + Utils.getExceptionDetails(t),
          entryContext);
    }

    return validator.createAssertionResult(testAssertion, result, failureDetail);
  }
}