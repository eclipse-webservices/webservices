/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl;

import java.util.List;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Types</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * This class represents a WSDL types element. The types element encloses data type definitions that are relevant for the exchanged messages.
 * <!-- end-model-doc -->
 *
 *
 * @see org.eclipse.wsdl.WSDLPackage#getTypes()
 * @model 
 * @generated
 */
public interface Types extends ExtensibleElement, javax.wsdl.Types{
  /**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @model parameters=""
   * @generated
   */
	List getSchemas();

  /**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @model 
   * @generated
   */
	List getSchemas(String namespaceURI);

} // Types
