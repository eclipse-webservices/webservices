/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.binding.soap.internal.util;

import javax.wsdl.extensions.ExtensibilityElement;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.binding.soap.SOAPAddress;
import org.eclipse.wst.wsdl.binding.soap.SOAPBinding;
import org.eclipse.wst.wsdl.binding.soap.SOAPBody;
import org.eclipse.wst.wsdl.binding.soap.SOAPFault;
import org.eclipse.wst.wsdl.binding.soap.SOAPHeader;
import org.eclipse.wst.wsdl.binding.soap.SOAPHeaderBase;
import org.eclipse.wst.wsdl.binding.soap.SOAPHeaderFault;
import org.eclipse.wst.wsdl.binding.soap.SOAPOperation;
import org.eclipse.wst.wsdl.binding.soap.SOAPPackage;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.wst.wsdl.binding.soap.SOAPPackage
 * @generated
 */
public class SOAPAdapterFactory extends AdapterFactoryImpl {
  /**
   * The cached model package.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	protected static SOAPPackage modelPackage;

  /**
   * Creates an instance of the adapter factory.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public SOAPAdapterFactory()
  {
    if (modelPackage == null)
    {
      modelPackage = SOAPPackage.eINSTANCE;
    }
  }

  /**
   * Returns whether this factory is applicable for the type of the object.
   * <!-- begin-user-doc -->
	 * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
	 * <!-- end-user-doc -->
   * @return whether this factory is applicable for the type of the object.
   * @generated
   */
	public boolean isFactoryForType(Object object)
  {
    if (object == modelPackage)
    {
      return true;
    }
    if (object instanceof EObject)
    {
      return ((EObject)object).eClass().getEPackage() == modelPackage;
    }
    return false;
  }

  /**
   * The switch the delegates to the <code>createXXX</code> methods.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	protected SOAPSwitch modelSwitch =
    new SOAPSwitch()
    {
      public Object caseSOAPBinding(SOAPBinding object)
      {
        return createSOAPBindingAdapter();
      }
      public Object caseSOAPBody(SOAPBody object)
      {
        return createSOAPBodyAdapter();
      }
      public Object caseSOAPHeaderBase(SOAPHeaderBase object)
      {
        return createSOAPHeaderBaseAdapter();
      }
      public Object caseSOAPFault(SOAPFault object)
      {
        return createSOAPFaultAdapter();
      }
      public Object caseSOAPOperation(SOAPOperation object)
      {
        return createSOAPOperationAdapter();
      }
      public Object caseSOAPAddress(SOAPAddress object)
      {
        return createSOAPAddressAdapter();
      }
      public Object caseSOAPHeaderFault(SOAPHeaderFault object)
      {
        return createSOAPHeaderFaultAdapter();
      }
      public Object caseSOAPHeader(SOAPHeader object)
      {
        return createSOAPHeaderAdapter();
      }
      public Object caseWSDLElement(WSDLElement object)
      {
        return createWSDLElementAdapter();
      }
      public Object caseIExtensibilityElement(ExtensibilityElement object)
      {
        return createIExtensibilityElementAdapter();
      }
      public Object caseExtensibilityElement(org.eclipse.wst.wsdl.ExtensibilityElement object)
      {
        return createExtensibilityElementAdapter();
      }
      public Object defaultCase(EObject object)
      {
        return createEObjectAdapter();
      }
    };

  /**
   * Creates an adapter for the <code>target</code>.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param target the object to adapt.
   * @return the adapter for the <code>target</code>.
   * @generated
   */
	public Adapter createAdapter(Notifier target)
  {
    return (Adapter)modelSwitch.doSwitch((EObject)target);
  }


  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.wst.wsdl.binding.soap.SOAPBinding <em>Binding</em>}'.
   * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.wst.wsdl.binding.soap.SOAPBinding
   * @generated
   */
	public Adapter createSOAPBindingAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.wst.wsdl.binding.soap.SOAPBody <em>Body</em>}'.
   * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.wst.wsdl.binding.soap.SOAPBody
   * @generated
   */
	public Adapter createSOAPBodyAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.wst.wsdl.binding.soap.SOAPHeaderBase <em>Header Base</em>}'.
   * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.wst.wsdl.binding.soap.SOAPHeaderBase
   * @generated
   */
	public Adapter createSOAPHeaderBaseAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.wst.wsdl.binding.soap.SOAPFault <em>Fault</em>}'.
   * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.wst.wsdl.binding.soap.SOAPFault
   * @generated
   */
	public Adapter createSOAPFaultAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.wst.wsdl.binding.soap.SOAPOperation <em>Operation</em>}'.
   * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.wst.wsdl.binding.soap.SOAPOperation
   * @generated
   */
	public Adapter createSOAPOperationAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.wst.wsdl.binding.soap.SOAPAddress <em>Address</em>}'.
   * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.wst.wsdl.binding.soap.SOAPAddress
   * @generated
   */
	public Adapter createSOAPAddressAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.wst.wsdl.binding.soap.SOAPHeaderFault <em>Header Fault</em>}'.
   * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.wst.wsdl.binding.soap.SOAPHeaderFault
   * @generated
   */
	public Adapter createSOAPHeaderFaultAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.wst.wsdl.binding.soap.SOAPHeader <em>Header</em>}'.
   * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.wst.wsdl.binding.soap.SOAPHeader
   * @generated
   */
	public Adapter createSOAPHeaderAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.wst.wsdl.WSDLElement <em>Element</em>}'.
   * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.wst.wsdl.WSDLElement
   * @generated
   */
	public Adapter createWSDLElementAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link javax.wsdl.extensions.ExtensibilityElement <em>IExtensibility Element</em>}'.
   * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
   * @return the new adapter.
   * @see javax.wsdl.extensions.ExtensibilityElement
   * @generated
   */
	public Adapter createIExtensibilityElementAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.wst.wsdl.ExtensibilityElement <em>Extensibility Element</em>}'.
   * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.wst.wsdl.ExtensibilityElement
   * @generated
   */
	public Adapter createExtensibilityElementAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for the default case.
   * <!-- begin-user-doc -->
	 * This default implementation returns null.
	 * <!-- end-user-doc -->
   * @return the new adapter.
   * @generated
   */
	public Adapter createEObjectAdapter()
  {
    return null;
  }

} //SOAPAdapterFactory
