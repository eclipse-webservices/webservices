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
package org.eclipse.wst.wsi.internal.core.profile.validator;

import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.analyzer.AnalyzerContext;
import org.eclipse.wst.wsi.internal.core.profile.ProfileArtifact;
import org.eclipse.wst.wsi.internal.core.report.ReportArtifact;
import org.eclipse.wst.wsi.internal.core.report.Reporter;
import org.eclipse.wst.wsi.internal.core.wsdl.WSDLDocument;

/**
 * The WSDL validator will verify that the WSDL and associated XML schema definitions
 * are in conformance with the profile.
 *
 * @version 1.0.1
 * @author Peter Brittenham
 */
public interface WSDLValidator extends BaseValidator
{
  /**
   * Initiailize validation test procedure.
   * @param analyzerContext the analyzerContext.
   * @param artifact        the profile artifact.
   * @param reportArtifact  the report artifact.
   * @param wsdlURL         the WSDL document location.
   * @param wsdlDocument    the WSDL document.
   * @param reporter        a Reporter object.
   * @throws WSIException if WSDL validator could not be initialized.
   */
  public void init(
    AnalyzerContext analyzerContext,
    ProfileArtifact artifact,
    ReportArtifact reportArtifact,
    String wsdlURL,
    WSDLDocument wsdlDocument,
    Reporter reporter)
    throws WSIException;

  /**
   * Validate the WSDL based service description.
   * @return the WSDL document.
   * @throws WSIException if an unexpected error occurred
   *         while validating he WSDL based service description.
   */
  public WSDLDocument validate() throws WSIException;
}
