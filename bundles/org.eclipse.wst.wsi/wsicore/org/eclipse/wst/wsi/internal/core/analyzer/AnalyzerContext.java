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
package org.eclipse.wst.wsi.internal.core.analyzer;

import org.eclipse.wst.wsi.internal.core.xml.XMLDocumentCache;

/**
 * This class contains contextual information which is required by test assertions across all artifacts.
 * 
 * @version 1.0.1
 * @author Peter Brittenham  (peterbr@us.ibm.com)
 */
public class AnalyzerContext
{
  protected ServiceReference serviceReference = null;
  protected CandidateInfo candidateInfo = null;
  protected XMLDocumentCache documentList = new XMLDocumentCache();

  /**
   * Do not allow the usage of the null constructor.
   */
  private AnalyzerContext()
  {
  }

  /**
   * Create analyzer context with service reference.
   * @param serviceReference a service reference.
   */
  public AnalyzerContext(ServiceReference serviceReference)
  {
    this.serviceReference = serviceReference;
  }

  /**
   * Get service reference.
   * @return service reference.
   * @see #setServiceReference
   */
  public ServiceReference getServiceReference()
  {
    return this.serviceReference;
  }

  /**
   * Set service reference.
   * @param serviceReference a service reference.
   * @see #getServiceReference
   */
  public void setServiceReference(ServiceReference serviceReference)
  {
    this.serviceReference = serviceReference;
  }

  /**
   * Returns the candidateInfo.
   * @return CandidateInfo.
   * @see #setCandidateInfo
   */
  public CandidateInfo getCandidateInfo()
  {
    return candidateInfo;
  }

  /**
   * Sets the candidateInfo.
   * @param candidateInfo the candidateInfo to set.
   * @see #getCandidateInfo
   */
  public void setCandidateInfo(CandidateInfo candidateInfo)
  {
    this.candidateInfo = candidateInfo;
  }

  /**
   * Get the list of cached documents.
   * @return the list of cached documents.
   * @see #setDocumentList
   */
  public XMLDocumentCache getDocumentList()
  {
    return documentList;
  }

  /**
   * Set the list of cached documents.
   * @param documentList a list of cached documents.
   * @see #getDocumentList
   */
  public void setDocumentList(XMLDocumentCache documentList)
  {
    this.documentList = documentList;
  }

  /**
   * Add a document to the list of cached documents.
   * @param key a key to associate with the given document.
   * @param document a document to be identified with the given key.
   */
  public void addDocument(Object key, Object document)
  {
    this.documentList.put(key, document);
  }

}
