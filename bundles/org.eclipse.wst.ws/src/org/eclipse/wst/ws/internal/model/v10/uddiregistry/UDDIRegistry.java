/**
 * <copyright>
 * </copyright>
 *
 * $Id: UDDIRegistry.java,v 1.1 2005/11/25 21:54:35 cbrealey Exp $
 */
package org.eclipse.wst.ws.internal.model.v10.uddiregistry;

import org.eclipse.wst.ws.internal.model.v10.registry.Registry;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>UDDIRegistry</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.wst.ws.internal.model.v10.uddiregistry.UDDIRegistry#getVersion <em>Version</em>}</li>
 *   <li>{@link org.eclipse.wst.ws.internal.model.v10.uddiregistry.UDDIRegistry#getDiscoveryURL <em>Discovery URL</em>}</li>
 *   <li>{@link org.eclipse.wst.ws.internal.model.v10.uddiregistry.UDDIRegistry#getPublicationURL <em>Publication URL</em>}</li>
 *   <li>{@link org.eclipse.wst.ws.internal.model.v10.uddiregistry.UDDIRegistry#getSecuredDiscoveryURL <em>Secured Discovery URL</em>}</li>
 *   <li>{@link org.eclipse.wst.ws.internal.model.v10.uddiregistry.UDDIRegistry#getSecuredPublicationURL <em>Secured Publication URL</em>}</li>
 *   <li>{@link org.eclipse.wst.ws.internal.model.v10.uddiregistry.UDDIRegistry#getDefaultLogin <em>Default Login</em>}</li>
 *   <li>{@link org.eclipse.wst.ws.internal.model.v10.uddiregistry.UDDIRegistry#getDefaultPassword <em>Default Password</em>}</li>
 *   <li>{@link org.eclipse.wst.ws.internal.model.v10.uddiregistry.UDDIRegistry#getTaxonomies <em>Taxonomies</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.wst.ws.internal.model.v10.uddiregistry.UDDIRegistryPackage#getUDDIRegistry()
 * @model 
 * @generated
 */
public interface UDDIRegistry extends Registry {
	/**
	 * Returns the value of the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Version</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Version</em>' attribute.
	 * @see #setVersion(String)
	 * @see org.eclipse.wst.ws.internal.model.v10.uddiregistry.UDDIRegistryPackage#getUDDIRegistry_Version()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 * @generated
	 */
	String getVersion();

	/**
	 * Sets the value of the '{@link org.eclipse.wst.ws.internal.model.v10.uddiregistry.UDDIRegistry#getVersion <em>Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Version</em>' attribute.
	 * @see #getVersion()
	 * @generated
	 */
	void setVersion(String value);

	/**
	 * Returns the value of the '<em><b>Discovery URL</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Discovery URL</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Discovery URL</em>' attribute.
	 * @see #setDiscoveryURL(String)
	 * @see org.eclipse.wst.ws.internal.model.v10.uddiregistry.UDDIRegistryPackage#getUDDIRegistry_DiscoveryURL()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
	 * @generated
	 */
	String getDiscoveryURL();

	/**
	 * Sets the value of the '{@link org.eclipse.wst.ws.internal.model.v10.uddiregistry.UDDIRegistry#getDiscoveryURL <em>Discovery URL</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Discovery URL</em>' attribute.
	 * @see #getDiscoveryURL()
	 * @generated
	 */
	void setDiscoveryURL(String value);

	/**
	 * Returns the value of the '<em><b>Publication URL</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Publication URL</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Publication URL</em>' attribute.
	 * @see #setPublicationURL(String)
	 * @see org.eclipse.wst.ws.internal.model.v10.uddiregistry.UDDIRegistryPackage#getUDDIRegistry_PublicationURL()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
	 * @generated
	 */
	String getPublicationURL();

	/**
	 * Sets the value of the '{@link org.eclipse.wst.ws.internal.model.v10.uddiregistry.UDDIRegistry#getPublicationURL <em>Publication URL</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Publication URL</em>' attribute.
	 * @see #getPublicationURL()
	 * @generated
	 */
	void setPublicationURL(String value);

	/**
	 * Returns the value of the '<em><b>Secured Discovery URL</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Secured Discovery URL</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Secured Discovery URL</em>' attribute.
	 * @see #setSecuredDiscoveryURL(String)
	 * @see org.eclipse.wst.ws.internal.model.v10.uddiregistry.UDDIRegistryPackage#getUDDIRegistry_SecuredDiscoveryURL()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
	 * @generated
	 */
	String getSecuredDiscoveryURL();

	/**
	 * Sets the value of the '{@link org.eclipse.wst.ws.internal.model.v10.uddiregistry.UDDIRegistry#getSecuredDiscoveryURL <em>Secured Discovery URL</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Secured Discovery URL</em>' attribute.
	 * @see #getSecuredDiscoveryURL()
	 * @generated
	 */
	void setSecuredDiscoveryURL(String value);

	/**
	 * Returns the value of the '<em><b>Secured Publication URL</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Secured Publication URL</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Secured Publication URL</em>' attribute.
	 * @see #setSecuredPublicationURL(String)
	 * @see org.eclipse.wst.ws.internal.model.v10.uddiregistry.UDDIRegistryPackage#getUDDIRegistry_SecuredPublicationURL()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
	 * @generated
	 */
	String getSecuredPublicationURL();

	/**
	 * Sets the value of the '{@link org.eclipse.wst.ws.internal.model.v10.uddiregistry.UDDIRegistry#getSecuredPublicationURL <em>Secured Publication URL</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Secured Publication URL</em>' attribute.
	 * @see #getSecuredPublicationURL()
	 * @generated
	 */
	void setSecuredPublicationURL(String value);

	/**
	 * Returns the value of the '<em><b>Default Login</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Default Login</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Default Login</em>' attribute.
	 * @see #setDefaultLogin(String)
	 * @see org.eclipse.wst.ws.internal.model.v10.uddiregistry.UDDIRegistryPackage#getUDDIRegistry_DefaultLogin()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String"
	 * @generated
	 */
	String getDefaultLogin();

	/**
	 * Sets the value of the '{@link org.eclipse.wst.ws.internal.model.v10.uddiregistry.UDDIRegistry#getDefaultLogin <em>Default Login</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Default Login</em>' attribute.
	 * @see #getDefaultLogin()
	 * @generated
	 */
	void setDefaultLogin(String value);

	/**
	 * Returns the value of the '<em><b>Default Password</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Default Password</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Default Password</em>' attribute.
	 * @see #setDefaultPassword(String)
	 * @see org.eclipse.wst.ws.internal.model.v10.uddiregistry.UDDIRegistryPackage#getUDDIRegistry_DefaultPassword()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String"
	 * @generated
	 */
	String getDefaultPassword();

	/**
	 * Sets the value of the '{@link org.eclipse.wst.ws.internal.model.v10.uddiregistry.UDDIRegistry#getDefaultPassword <em>Default Password</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Default Password</em>' attribute.
	 * @see #getDefaultPassword()
	 * @generated
	 */
	void setDefaultPassword(String value);

	/**
	 * Returns the value of the '<em><b>Taxonomies</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Taxonomies</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Taxonomies</em>' containment reference.
	 * @see #setTaxonomies(Taxonomies)
	 * @see org.eclipse.wst.ws.internal.model.v10.uddiregistry.UDDIRegistryPackage#getUDDIRegistry_Taxonomies()
	 * @model containment="true" resolveProxies="false"
	 * @generated
	 */
	Taxonomies getTaxonomies();

	/**
	 * Sets the value of the '{@link org.eclipse.wst.ws.internal.model.v10.uddiregistry.UDDIRegistry#getTaxonomies <em>Taxonomies</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Taxonomies</em>' containment reference.
	 * @see #getTaxonomies()
	 * @generated
	 */
	void setTaxonomies(Taxonomies value);

} // UDDIRegistry
