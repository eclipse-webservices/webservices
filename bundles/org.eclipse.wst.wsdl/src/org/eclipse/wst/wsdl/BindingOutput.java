/**
 * <copyright>
 * </copyright>
 *
 * %W%
 * @version %I% %H%
 */
package org.eclipse.wst.wsdl;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Binding Output</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * This class represents a WSDL output element within a operation within a binding. An output element within an operation within a binding specifies binding information for the output of the operation. 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.wsdl.BindingOutput#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.wsdl.BindingOutput#getEOutput <em>EOutput</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.wsdl.WSDLPackage#getBindingOutput()
 * @model 
 * @generated
 */
public interface BindingOutput extends ExtensibleElement, javax.wsdl.BindingOutput{
  /**
   * Returns the value of the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Name</em>' attribute.
   * @see #setName(String)
   * @see org.eclipse.wsdl.WSDLPackage#getBindingOutput_Name()
   * @model 
   * @generated
   */
	String getName();

  /**
   * Sets the value of the '{@link org.eclipse.wsdl.BindingOutput#getName <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Name</em>' attribute.
   * @see #getName()
   * @generated
   */
	void setName(String value);

  /**
   * Returns the value of the '<em><b>EOutput</b></em>' reference.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>EOutput</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>EOutput</em>' reference.
   * @see #setEOutput(Output)
   * @see org.eclipse.wsdl.WSDLPackage#getBindingOutput_EOutput()
   * @model required="true"
   * @generated
   */
	Output getEOutput();

  /**
   * Sets the value of the '{@link org.eclipse.wsdl.BindingOutput#getEOutput <em>EOutput</em>}' reference.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>EOutput</em>' reference.
   * @see #getEOutput()
   * @generated
   */
	void setEOutput(Output value);

  /**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @model parameters=""
   * @generated
   */
	javax.wsdl.Output getOutput();

  /**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @model 
   * @generated
   */
	void setOutput(javax.wsdl.Output output);

} // BindingOutput
