/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.validation.internal.eclipse;

import java.util.HashMap;

import org.apache.xerces.xni.grammars.XMLGrammarPool;
import org.eclipse.wst.validation.internal.provisional.core.IMessage;
import org.eclipse.wst.wsdl.validation.internal.IValidationMessage;
import org.eclipse.wst.wsdl.validation.internal.IValidationReport;
import org.eclipse.wst.xml.core.internal.validation.core.NestedValidatorContext;
import org.eclipse.wst.xml.core.internal.validation.core.ValidationInfo;
import org.eclipse.wst.xml.core.internal.validation.core.ValidationMessage;
import org.eclipse.wst.xml.core.internal.validation.core.ValidationReport;

/**
 * This class extends validator to expose the protected methods
 * for testing.
 */
public class ValidatorWrapper extends Validator 
{
  /* (non-Javadoc)
   * @see org.eclipse.wst.xml.core.internal.validation.eclipse.Validator#addInfoToMessage(org.eclipse.wst.xml.core.internal.validation.core.ValidationMessage, org.eclipse.wst.validation.internal.provisional.core.IMessage)
   */
  public void addInfoToMessage(ValidationMessage validationMessage, IMessage message) 
  {
	super.addInfoToMessage(validationMessage, message);
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.wst.xml.core.internal.validation.eclipse.Validator#setupValidation(org.eclipse.wst.xml.core.internal.validation.core.NestedValidatorContext)
   */
  public void setupValidation(NestedValidatorContext context) 
  {
	super.setupValidation(context);
  }

  /* (non-Javadoc)
   * @see org.eclipse.wst.wsdl.validation.internal.eclipse.Validator#teardownValidation(org.eclipse.wst.xml.core.internal.validation.core.NestedValidatorContext)
   */
  public void teardownValidation(NestedValidatorContext context) 
  {
	super.teardownValidation(context);
  }

  /* (non-Javadoc)
   * @see org.eclipse.wst.wsdl.validation.internal.eclipse.Validator#convertMessage(org.eclipse.wst.wsdl.validation.internal.IValidationMessage, org.eclipse.wst.xml.core.internal.validation.core.ValidationInfo)
   */
  public void convertMessage(IValidationMessage message, ValidationInfo convertedReport) 
  {
	super.convertMessage(message, convertedReport);
  }

  /* (non-Javadoc)
   * @see org.eclipse.wst.wsdl.validation.internal.eclipse.Validator#convertReportToXMLReport(org.eclipse.wst.wsdl.validation.internal.IValidationReport)
   */
  public ValidationReport convertReportToXMLReport(IValidationReport report) 
  {
	return super.convertReportToXMLReport(report);
  }
  
  /**
   * Get the XML grammar pool defined for the given context.
   * 
   * @param context
   * 		The context to use to retrieve the XML grammar pool.
   * @return
   * 		The XML grammar pool associated with the context or null if none is associated.
   */
  public XMLGrammarPool getXMLGrammarPoolForContext(NestedValidatorContext context)
  {
	return (XMLGrammarPool)xmlGrammarPools.get(context);
  }
  
  /**
   * Get the XSD grammar pool defined for the given context.
   * 
   * @param context
   * 		The context to use to retrieve the XSD grammar pool.
   * @return
   * 		The XSD grammar pool associated with the context or null if none is associated.
   */
  public XMLGrammarPool getXSDGrammarPoolForContext(NestedValidatorContext context)
  {
	return (XMLGrammarPool)xsdGrammarPools.get(context);
  }
  
  /**
   * Expose the XML grammar pools HashMap for testing.
   * 
   * @return
   * 		The XML grammar pools HashMap.
   */
  public HashMap getXMLGrammarPoolsMap()
  {
	return xmlGrammarPools;
  }
  
  /**
   * Expose the XSD grammar pools HashMap for testing.
   * 
   * @return
   * 		The XSD grammar pools HashMap.
   */
  public HashMap getXSDGrammarPoolsMap()
  {
	return xsdGrammarPools;
  }
}
