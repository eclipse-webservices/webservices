/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.binding.soap.internal.impl;


import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.wst.wsdl.binding.soap.SOAPAddress;
import org.eclipse.wst.wsdl.binding.soap.SOAPBinding;
import org.eclipse.wst.wsdl.binding.soap.SOAPBody;
import org.eclipse.wst.wsdl.binding.soap.SOAPFactory;
import org.eclipse.wst.wsdl.binding.soap.SOAPFault;
import org.eclipse.wst.wsdl.binding.soap.SOAPHeader;
import org.eclipse.wst.wsdl.binding.soap.SOAPHeaderBase;
import org.eclipse.wst.wsdl.binding.soap.SOAPHeaderFault;
import org.eclipse.wst.wsdl.binding.soap.SOAPOperation;
import org.eclipse.wst.wsdl.binding.soap.SOAPPackage;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class SOAPFactoryImpl extends EFactoryImpl implements SOAPFactory
{
  /**
   * Creates and instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SOAPFactoryImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EObject create(EClass eClass)
  {
    switch (eClass.getClassifierID())
    {
      case SOAPPackage.SOAP_BINDING:
      return createSOAPBinding();
      case SOAPPackage.SOAP_BODY:
      return createSOAPBody();
      case SOAPPackage.SOAP_HEADER_BASE:
      return createSOAPHeaderBase();
      case SOAPPackage.SOAP_FAULT:
      return createSOAPFault();
      case SOAPPackage.SOAP_OPERATION:
      return createSOAPOperation();
      case SOAPPackage.SOAP_ADDRESS:
      return createSOAPAddress();
      case SOAPPackage.SOAP_HEADER_FAULT:
      return createSOAPHeaderFault();
      case SOAPPackage.SOAP_HEADER:
      return createSOAPHeader();
      default:
      throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Object createFromString(EDataType eDataType, String initialValue)
  {
    switch (eDataType.getClassifierID())
    {
      case SOAPPackage.ISTRING:
      return createIStringFromString(eDataType, initialValue);
      default:
      throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertToString(EDataType eDataType, Object instanceValue)
  {
    switch (eDataType.getClassifierID())
    {
      case SOAPPackage.ISTRING:
      return convertIStringToString(eDataType, instanceValue);
      default:
      throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SOAPBinding createSOAPBinding()
  {
    SOAPBindingImpl soapBinding = new SOAPBindingImpl();
    return soapBinding;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SOAPBody createSOAPBody()
  {
    SOAPBodyImpl soapBody = new SOAPBodyImpl();
    return soapBody;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SOAPHeaderBase createSOAPHeaderBase()
  {
    SOAPHeaderBaseImpl soapHeaderBase = new SOAPHeaderBaseImpl();
    return soapHeaderBase;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SOAPFault createSOAPFault()
  {
    SOAPFaultImpl soapFault = new SOAPFaultImpl();
    return soapFault;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SOAPOperation createSOAPOperation()
  {
    SOAPOperationImpl soapOperation = new SOAPOperationImpl();
    return soapOperation;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SOAPAddress createSOAPAddress()
  {
    SOAPAddressImpl soapAddress = new SOAPAddressImpl();
    return soapAddress;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SOAPHeaderFault createSOAPHeaderFault()
  {
    SOAPHeaderFaultImpl soapHeaderFault = new SOAPHeaderFaultImpl();
    return soapHeaderFault;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SOAPHeader createSOAPHeader()
  {
    SOAPHeaderImpl soapHeader = new SOAPHeaderImpl();
    return soapHeader;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String createIStringFromString(EDataType eDataType, String initialValue)
  {
    return (String)super.createFromString(eDataType, initialValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertIStringToString(EDataType eDataType, Object instanceValue)
  {
    return super.convertToString(eDataType, instanceValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SOAPPackage getSOAPPackage()
  {
    return (SOAPPackage)getEPackage();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @deprecated
   * @generated
   */
  public static SOAPPackage getPackage()
  {
    return SOAPPackage.eINSTANCE;
  }
} //SOAPFactoryImpl
