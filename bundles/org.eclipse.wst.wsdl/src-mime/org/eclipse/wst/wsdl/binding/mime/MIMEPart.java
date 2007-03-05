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
package org.eclipse.wst.wsdl.binding.mime;


import java.util.List;

import org.eclipse.wst.wsdl.ExtensibilityElement;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Part</b></em>'.
 * <!-- end-user-doc -->
 *
 *
 * @see org.eclipse.wst.wsdl.binding.mime.MIMEPackage#getMIMEPart()
 * @model 
 * @generated
 */
public interface MIMEPart extends ExtensibilityElement, javax.wsdl.extensions.mime.MIMEPart
{
  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model 
   * @generated
   */
  void addExtensibilityElement(javax.wsdl.extensions.ExtensibilityElement extensibilityElement);

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model dataType="org.eclipse.wst.wsdl.binding.mime.List" parameters=""
   * @generated
   */
  List getExtensibilityElements();

} // MIMEPart
