/**
 * <copyright>
 * </copyright>
 *
 * %W%
 * @version %I% %H%
 */
package org.eclipse.wst.wsdl;

import javax.xml.namespace.QName;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Message</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * This class represents a WSDL message element. A WSDL message is an abstract, typed definition of the data being communicated.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.wsdl.Message#getQName <em>QName</em>}</li>
 *   <li>{@link org.eclipse.wsdl.Message#isUndefined <em>Undefined</em>}</li>
 *   <li>{@link org.eclipse.wsdl.Message#isProxy <em>Proxy</em>}</li>
 *   <li>{@link org.eclipse.wsdl.Message#getResourceURI <em>Resource URI</em>}</li>
 *   <li>{@link org.eclipse.wsdl.Message#getEParts <em>EParts</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.wsdl.WSDLPackage#getMessage()
 * @model 
 * @generated
 */
public interface Message extends WSDLElement, javax.wsdl.Message{
  /**
   * Returns the value of the '<em><b>QName</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>QName</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>QName</em>' attribute.
   * @see #setQName(QName)
   * @see org.eclipse.wsdl.WSDLPackage#getMessage_QName()
   * @model dataType="org.eclipse.wsdl.QName"
   * @generated
   */
	QName getQName();

  /**
   * Sets the value of the '{@link org.eclipse.wsdl.Message#getQName <em>QName</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>QName</em>' attribute.
   * @see #getQName()
   * @generated
   */
	void setQName(QName value);

  /**
   * Returns the value of the '<em><b>Undefined</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Undefined</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Undefined</em>' attribute.
   * @see #setUndefined(boolean)
   * @see org.eclipse.wsdl.WSDLPackage#getMessage_Undefined()
   * @model 
   * @generated
   */
	boolean isUndefined();

  /**
   * Sets the value of the '{@link org.eclipse.wsdl.Message#isUndefined <em>Undefined</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Undefined</em>' attribute.
   * @see #isUndefined()
   * @generated
   */
	void setUndefined(boolean value);

  /**
   * Returns the value of the '<em><b>Proxy</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Proxy</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Proxy</em>' attribute.
   * @see #setProxy(boolean)
   * @see org.eclipse.wsdl.WSDLPackage#getMessage_Proxy()
   * @model 
   * @generated
   */
	boolean isProxy();

  /**
   * Sets the value of the '{@link org.eclipse.wsdl.Message#isProxy <em>Proxy</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Proxy</em>' attribute.
   * @see #isProxy()
   * @generated
   */
	void setProxy(boolean value);

  /**
   * Returns the value of the '<em><b>Resource URI</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Resource URI</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Resource URI</em>' attribute.
   * @see #setResourceURI(String)
   * @see org.eclipse.wsdl.WSDLPackage#getMessage_ResourceURI()
   * @model 
   * @generated
   */
	String getResourceURI();

  /**
   * Sets the value of the '{@link org.eclipse.wsdl.Message#getResourceURI <em>Resource URI</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Resource URI</em>' attribute.
   * @see #getResourceURI()
   * @generated
   */
	void setResourceURI(String value);

  /**
   * Returns the value of the '<em><b>EParts</b></em>' containment reference list.
   * The list contents are of type {@link org.eclipse.wsdl.Part}.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>EParts</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>EParts</em>' containment reference list.
   * @see org.eclipse.wsdl.WSDLPackage#getMessage_EParts()
   * @model type="org.eclipse.wsdl.Part" containment="true"
   * @generated
   */
	EList getEParts();

} // Message
