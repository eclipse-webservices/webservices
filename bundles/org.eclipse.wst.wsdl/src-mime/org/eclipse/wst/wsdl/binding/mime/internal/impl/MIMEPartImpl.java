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
package org.eclipse.wst.wsdl.binding.mime.internal.impl;


import java.util.Collection;
import java.util.List;

import javax.wsdl.extensions.ExtensibilityElement;
import javax.xml.namespace.QName;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.wst.wsdl.WSDLFactory;
import org.eclipse.wst.wsdl.binding.mime.MIMEPackage;
import org.eclipse.wst.wsdl.binding.mime.MIMEPart;
import org.eclipse.wst.wsdl.binding.mime.internal.util.MIMEConstants;
import org.eclipse.wst.wsdl.internal.impl.DefinitionImpl;
import org.eclipse.wst.wsdl.internal.impl.ExtensibilityElementImpl;
import org.eclipse.wst.wsdl.internal.impl.WSDLFactoryImpl;
import org.eclipse.wst.wsdl.util.WSDLConstants;
import org.w3c.dom.Element;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Part</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public class MIMEPartImpl extends ExtensibilityElementImpl implements MIMEPart
{

  /**
   * This class is not intended to be serialized.
   * serialVersionUID is assigned with 1L to avoid
   * compiler warning messages.
   */
  private static final long serialVersionUID = 1L;

  private List extensibilityElements = new java.util.Vector();

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected MIMEPartImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected EClass eStaticClass()
  {
    return MIMEPackage.Literals.MIME_PART;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void addExtensibilityElement(ExtensibilityElement extensibilityElement)
  {
    getExtensibilityElements().add(extensibilityElement);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public List getExtensibilityElements()
  {
    return extensibilityElements;
  }

  //
  // Reconciliation: DOM -> MODEL
  //
  public void handleUnreconciledElement(Element child, Collection remainingModelObjects)
  {
    if (!WSDLConstants.isMatchingNamespace(child.getNamespaceURI(), WSDLConstants.WSDL_NAMESPACE_URI))
    {
      org.eclipse.wst.wsdl.ExtensibilityElement extensibilityElement = useExtensionFactories()
        ? ((WSDLFactoryImpl)WSDLFactory.eINSTANCE).createExtensibilityElement(getNamespace(child), getLocalName(child))
        : ((WSDLFactoryImpl)WSDLFactory.eINSTANCE).createUnknownExtensibilityElement();

      extensibilityElement.setEnclosingDefinition(getEnclosingDefinition());
      extensibilityElement.setElement(child);
      addExtensibilityElement(extensibilityElement);
    }
  }

  private boolean useExtensionFactories()
  {
    // Use extension factories by default.
    return getEnclosingDefinition() == null ? true : ((DefinitionImpl)getEnclosingDefinition()).getUseExtensionFactories();
  }

  public QName getElementType()
  {
    if (elementType == null)
      elementType = new QName(MIMEConstants.MIME_NAMESPACE_URI, MIMEConstants.PART_ELEMENT_TAG);
    return elementType;
  }

} //MIMEPartImpl
