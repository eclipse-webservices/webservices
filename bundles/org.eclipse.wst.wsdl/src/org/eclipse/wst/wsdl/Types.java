/**
 * <copyright>
 * </copyright>
 *
 * %W%
 * @version %I% %H%
 */
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
