/**
 * <copyright>
 * </copyright>
 *
 * $Id: UDDIRegistryImpl.java,v 1.2 2005/12/03 04:06:50 cbrealey Exp $
 */
package org.eclipse.wst.ws.internal.model.v10.uddiregistry.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.wst.ws.internal.model.v10.registry.impl.RegistryImpl;

import org.eclipse.wst.ws.internal.model.v10.uddiregistry.Taxonomies;
import org.eclipse.wst.ws.internal.model.v10.uddiregistry.UDDIRegistry;
import org.eclipse.wst.ws.internal.model.v10.uddiregistry.UDDIRegistryPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>UDDIRegistry</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.wst.ws.internal.model.v10.uddiregistry.impl.UDDIRegistryImpl#getVersion <em>Version</em>}</li>
 *   <li>{@link org.eclipse.wst.ws.internal.model.v10.uddiregistry.impl.UDDIRegistryImpl#getDiscoveryURL <em>Discovery URL</em>}</li>
 *   <li>{@link org.eclipse.wst.ws.internal.model.v10.uddiregistry.impl.UDDIRegistryImpl#getPublicationURL <em>Publication URL</em>}</li>
 *   <li>{@link org.eclipse.wst.ws.internal.model.v10.uddiregistry.impl.UDDIRegistryImpl#getSecuredDiscoveryURL <em>Secured Discovery URL</em>}</li>
 *   <li>{@link org.eclipse.wst.ws.internal.model.v10.uddiregistry.impl.UDDIRegistryImpl#getSecuredPublicationURL <em>Secured Publication URL</em>}</li>
 *   <li>{@link org.eclipse.wst.ws.internal.model.v10.uddiregistry.impl.UDDIRegistryImpl#getDefaultLogin <em>Default Login</em>}</li>
 *   <li>{@link org.eclipse.wst.ws.internal.model.v10.uddiregistry.impl.UDDIRegistryImpl#getDefaultPassword <em>Default Password</em>}</li>
 *   <li>{@link org.eclipse.wst.ws.internal.model.v10.uddiregistry.impl.UDDIRegistryImpl#getTaxonomies <em>Taxonomies</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class UDDIRegistryImpl extends RegistryImpl implements UDDIRegistry {
	/**
	 * The default value of the '{@link #getVersion() <em>Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVersion()
	 * @generated
	 * @ordered
	 */
	protected static final String VERSION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getVersion() <em>Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVersion()
	 * @generated
	 * @ordered
	 */
	protected String version = VERSION_EDEFAULT;

	/**
	 * The default value of the '{@link #getDiscoveryURL() <em>Discovery URL</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDiscoveryURL()
	 * @generated
	 * @ordered
	 */
	protected static final String DISCOVERY_URL_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDiscoveryURL() <em>Discovery URL</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDiscoveryURL()
	 * @generated
	 * @ordered
	 */
	protected String discoveryURL = DISCOVERY_URL_EDEFAULT;

	/**
	 * The default value of the '{@link #getPublicationURL() <em>Publication URL</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPublicationURL()
	 * @generated
	 * @ordered
	 */
	protected static final String PUBLICATION_URL_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getPublicationURL() <em>Publication URL</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPublicationURL()
	 * @generated
	 * @ordered
	 */
	protected String publicationURL = PUBLICATION_URL_EDEFAULT;

	/**
	 * The default value of the '{@link #getSecuredDiscoveryURL() <em>Secured Discovery URL</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSecuredDiscoveryURL()
	 * @generated
	 * @ordered
	 */
	protected static final String SECURED_DISCOVERY_URL_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getSecuredDiscoveryURL() <em>Secured Discovery URL</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSecuredDiscoveryURL()
	 * @generated
	 * @ordered
	 */
	protected String securedDiscoveryURL = SECURED_DISCOVERY_URL_EDEFAULT;

	/**
	 * The default value of the '{@link #getSecuredPublicationURL() <em>Secured Publication URL</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSecuredPublicationURL()
	 * @generated
	 * @ordered
	 */
	protected static final String SECURED_PUBLICATION_URL_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getSecuredPublicationURL() <em>Secured Publication URL</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSecuredPublicationURL()
	 * @generated
	 * @ordered
	 */
	protected String securedPublicationURL = SECURED_PUBLICATION_URL_EDEFAULT;

	/**
	 * The default value of the '{@link #getDefaultLogin() <em>Default Login</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDefaultLogin()
	 * @generated
	 * @ordered
	 */
	protected static final String DEFAULT_LOGIN_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDefaultLogin() <em>Default Login</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDefaultLogin()
	 * @generated
	 * @ordered
	 */
	protected String defaultLogin = DEFAULT_LOGIN_EDEFAULT;

	/**
	 * The default value of the '{@link #getDefaultPassword() <em>Default Password</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDefaultPassword()
	 * @generated
	 * @ordered
	 */
	protected static final String DEFAULT_PASSWORD_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDefaultPassword() <em>Default Password</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDefaultPassword()
	 * @generated
	 * @ordered
	 */
	protected String defaultPassword = DEFAULT_PASSWORD_EDEFAULT;

	/**
	 * The cached value of the '{@link #getTaxonomies() <em>Taxonomies</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTaxonomies()
	 * @generated
	 * @ordered
	 */
	protected Taxonomies taxonomies = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected UDDIRegistryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return UDDIRegistryPackage.eINSTANCE.getUDDIRegistry();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setVersion(String newVersion) {
		String oldVersion = version;
		version = newVersion;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, UDDIRegistryPackage.UDDI_REGISTRY__VERSION, oldVersion, version));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getDiscoveryURL() {
		return discoveryURL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDiscoveryURL(String newDiscoveryURL) {
		String oldDiscoveryURL = discoveryURL;
		discoveryURL = newDiscoveryURL;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, UDDIRegistryPackage.UDDI_REGISTRY__DISCOVERY_URL, oldDiscoveryURL, discoveryURL));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getPublicationURL() {
		return publicationURL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPublicationURL(String newPublicationURL) {
		String oldPublicationURL = publicationURL;
		publicationURL = newPublicationURL;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, UDDIRegistryPackage.UDDI_REGISTRY__PUBLICATION_URL, oldPublicationURL, publicationURL));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getSecuredDiscoveryURL() {
		return securedDiscoveryURL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSecuredDiscoveryURL(String newSecuredDiscoveryURL) {
		String oldSecuredDiscoveryURL = securedDiscoveryURL;
		securedDiscoveryURL = newSecuredDiscoveryURL;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, UDDIRegistryPackage.UDDI_REGISTRY__SECURED_DISCOVERY_URL, oldSecuredDiscoveryURL, securedDiscoveryURL));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getSecuredPublicationURL() {
		return securedPublicationURL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSecuredPublicationURL(String newSecuredPublicationURL) {
		String oldSecuredPublicationURL = securedPublicationURL;
		securedPublicationURL = newSecuredPublicationURL;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, UDDIRegistryPackage.UDDI_REGISTRY__SECURED_PUBLICATION_URL, oldSecuredPublicationURL, securedPublicationURL));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getDefaultLogin() {
		return defaultLogin;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDefaultLogin(String newDefaultLogin) {
		String oldDefaultLogin = defaultLogin;
		defaultLogin = newDefaultLogin;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, UDDIRegistryPackage.UDDI_REGISTRY__DEFAULT_LOGIN, oldDefaultLogin, defaultLogin));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getDefaultPassword() {
		return defaultPassword;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDefaultPassword(String newDefaultPassword) {
		String oldDefaultPassword = defaultPassword;
		defaultPassword = newDefaultPassword;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, UDDIRegistryPackage.UDDI_REGISTRY__DEFAULT_PASSWORD, oldDefaultPassword, defaultPassword));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Taxonomies getTaxonomies() {
		return taxonomies;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetTaxonomies(Taxonomies newTaxonomies, NotificationChain msgs) {
		Taxonomies oldTaxonomies = taxonomies;
		taxonomies = newTaxonomies;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, UDDIRegistryPackage.UDDI_REGISTRY__TAXONOMIES, oldTaxonomies, newTaxonomies);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTaxonomies(Taxonomies newTaxonomies) {
		if (newTaxonomies != taxonomies) {
			NotificationChain msgs = null;
			if (taxonomies != null)
				msgs = ((InternalEObject)taxonomies).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - UDDIRegistryPackage.UDDI_REGISTRY__TAXONOMIES, null, msgs);
			if (newTaxonomies != null)
				msgs = ((InternalEObject)newTaxonomies).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - UDDIRegistryPackage.UDDI_REGISTRY__TAXONOMIES, null, msgs);
			msgs = basicSetTaxonomies(newTaxonomies, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, UDDIRegistryPackage.UDDI_REGISTRY__TAXONOMIES, newTaxonomies, newTaxonomies));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
		if (featureID >= 0) {
			switch (eDerivedStructuralFeatureID(featureID, baseClass)) {
				case UDDIRegistryPackage.UDDI_REGISTRY__NAME:
					return ((InternalEList)getName()).basicRemove(otherEnd, msgs);
				case UDDIRegistryPackage.UDDI_REGISTRY__DESCRIPTION:
					return ((InternalEList)getDescription()).basicRemove(otherEnd, msgs);
				case UDDIRegistryPackage.UDDI_REGISTRY__TAXONOMIES:
					return basicSetTaxonomies(null, msgs);
				default:
					return eDynamicInverseRemove(otherEnd, featureID, baseClass, msgs);
			}
		}
		return eBasicSetContainer(null, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object eGet(EStructuralFeature eFeature, boolean resolve) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case UDDIRegistryPackage.UDDI_REGISTRY__NAME:
				return getName();
			case UDDIRegistryPackage.UDDI_REGISTRY__DESCRIPTION:
				return getDescription();
			case UDDIRegistryPackage.UDDI_REGISTRY__ID:
				return getId();
			case UDDIRegistryPackage.UDDI_REGISTRY__LOCATION:
				return getLocation();
			case UDDIRegistryPackage.UDDI_REGISTRY__REF:
				return getRef();
			case UDDIRegistryPackage.UDDI_REGISTRY__VERSION:
				return getVersion();
			case UDDIRegistryPackage.UDDI_REGISTRY__DISCOVERY_URL:
				return getDiscoveryURL();
			case UDDIRegistryPackage.UDDI_REGISTRY__PUBLICATION_URL:
				return getPublicationURL();
			case UDDIRegistryPackage.UDDI_REGISTRY__SECURED_DISCOVERY_URL:
				return getSecuredDiscoveryURL();
			case UDDIRegistryPackage.UDDI_REGISTRY__SECURED_PUBLICATION_URL:
				return getSecuredPublicationURL();
			case UDDIRegistryPackage.UDDI_REGISTRY__DEFAULT_LOGIN:
				return getDefaultLogin();
			case UDDIRegistryPackage.UDDI_REGISTRY__DEFAULT_PASSWORD:
				return getDefaultPassword();
			case UDDIRegistryPackage.UDDI_REGISTRY__TAXONOMIES:
				return getTaxonomies();
		}
		return eDynamicGet(eFeature, resolve);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void eSet(EStructuralFeature eFeature, Object newValue) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case UDDIRegistryPackage.UDDI_REGISTRY__NAME:
				getName().clear();
				getName().addAll((Collection)newValue);
				return;
			case UDDIRegistryPackage.UDDI_REGISTRY__DESCRIPTION:
				getDescription().clear();
				getDescription().addAll((Collection)newValue);
				return;
			case UDDIRegistryPackage.UDDI_REGISTRY__ID:
				setId((String)newValue);
				return;
			case UDDIRegistryPackage.UDDI_REGISTRY__LOCATION:
				setLocation((String)newValue);
				return;
			case UDDIRegistryPackage.UDDI_REGISTRY__REF:
				setRef((String)newValue);
				return;
			case UDDIRegistryPackage.UDDI_REGISTRY__VERSION:
				setVersion((String)newValue);
				return;
			case UDDIRegistryPackage.UDDI_REGISTRY__DISCOVERY_URL:
				setDiscoveryURL((String)newValue);
				return;
			case UDDIRegistryPackage.UDDI_REGISTRY__PUBLICATION_URL:
				setPublicationURL((String)newValue);
				return;
			case UDDIRegistryPackage.UDDI_REGISTRY__SECURED_DISCOVERY_URL:
				setSecuredDiscoveryURL((String)newValue);
				return;
			case UDDIRegistryPackage.UDDI_REGISTRY__SECURED_PUBLICATION_URL:
				setSecuredPublicationURL((String)newValue);
				return;
			case UDDIRegistryPackage.UDDI_REGISTRY__DEFAULT_LOGIN:
				setDefaultLogin((String)newValue);
				return;
			case UDDIRegistryPackage.UDDI_REGISTRY__DEFAULT_PASSWORD:
				setDefaultPassword((String)newValue);
				return;
			case UDDIRegistryPackage.UDDI_REGISTRY__TAXONOMIES:
				setTaxonomies((Taxonomies)newValue);
				return;
		}
		eDynamicSet(eFeature, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void eUnset(EStructuralFeature eFeature) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case UDDIRegistryPackage.UDDI_REGISTRY__NAME:
				getName().clear();
				return;
			case UDDIRegistryPackage.UDDI_REGISTRY__DESCRIPTION:
				getDescription().clear();
				return;
			case UDDIRegistryPackage.UDDI_REGISTRY__ID:
				setId(ID_EDEFAULT);
				return;
			case UDDIRegistryPackage.UDDI_REGISTRY__LOCATION:
				setLocation(LOCATION_EDEFAULT);
				return;
			case UDDIRegistryPackage.UDDI_REGISTRY__REF:
				setRef(REF_EDEFAULT);
				return;
			case UDDIRegistryPackage.UDDI_REGISTRY__VERSION:
				setVersion(VERSION_EDEFAULT);
				return;
			case UDDIRegistryPackage.UDDI_REGISTRY__DISCOVERY_URL:
				setDiscoveryURL(DISCOVERY_URL_EDEFAULT);
				return;
			case UDDIRegistryPackage.UDDI_REGISTRY__PUBLICATION_URL:
				setPublicationURL(PUBLICATION_URL_EDEFAULT);
				return;
			case UDDIRegistryPackage.UDDI_REGISTRY__SECURED_DISCOVERY_URL:
				setSecuredDiscoveryURL(SECURED_DISCOVERY_URL_EDEFAULT);
				return;
			case UDDIRegistryPackage.UDDI_REGISTRY__SECURED_PUBLICATION_URL:
				setSecuredPublicationURL(SECURED_PUBLICATION_URL_EDEFAULT);
				return;
			case UDDIRegistryPackage.UDDI_REGISTRY__DEFAULT_LOGIN:
				setDefaultLogin(DEFAULT_LOGIN_EDEFAULT);
				return;
			case UDDIRegistryPackage.UDDI_REGISTRY__DEFAULT_PASSWORD:
				setDefaultPassword(DEFAULT_PASSWORD_EDEFAULT);
				return;
			case UDDIRegistryPackage.UDDI_REGISTRY__TAXONOMIES:
				setTaxonomies((Taxonomies)null);
				return;
		}
		eDynamicUnset(eFeature);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean eIsSet(EStructuralFeature eFeature) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case UDDIRegistryPackage.UDDI_REGISTRY__NAME:
				return name != null && !name.isEmpty();
			case UDDIRegistryPackage.UDDI_REGISTRY__DESCRIPTION:
				return description != null && !description.isEmpty();
			case UDDIRegistryPackage.UDDI_REGISTRY__ID:
				return ID_EDEFAULT == null ? id != null : !ID_EDEFAULT.equals(id);
			case UDDIRegistryPackage.UDDI_REGISTRY__LOCATION:
				return LOCATION_EDEFAULT == null ? location != null : !LOCATION_EDEFAULT.equals(location);
			case UDDIRegistryPackage.UDDI_REGISTRY__REF:
				return REF_EDEFAULT == null ? ref != null : !REF_EDEFAULT.equals(ref);
			case UDDIRegistryPackage.UDDI_REGISTRY__VERSION:
				return VERSION_EDEFAULT == null ? version != null : !VERSION_EDEFAULT.equals(version);
			case UDDIRegistryPackage.UDDI_REGISTRY__DISCOVERY_URL:
				return DISCOVERY_URL_EDEFAULT == null ? discoveryURL != null : !DISCOVERY_URL_EDEFAULT.equals(discoveryURL);
			case UDDIRegistryPackage.UDDI_REGISTRY__PUBLICATION_URL:
				return PUBLICATION_URL_EDEFAULT == null ? publicationURL != null : !PUBLICATION_URL_EDEFAULT.equals(publicationURL);
			case UDDIRegistryPackage.UDDI_REGISTRY__SECURED_DISCOVERY_URL:
				return SECURED_DISCOVERY_URL_EDEFAULT == null ? securedDiscoveryURL != null : !SECURED_DISCOVERY_URL_EDEFAULT.equals(securedDiscoveryURL);
			case UDDIRegistryPackage.UDDI_REGISTRY__SECURED_PUBLICATION_URL:
				return SECURED_PUBLICATION_URL_EDEFAULT == null ? securedPublicationURL != null : !SECURED_PUBLICATION_URL_EDEFAULT.equals(securedPublicationURL);
			case UDDIRegistryPackage.UDDI_REGISTRY__DEFAULT_LOGIN:
				return DEFAULT_LOGIN_EDEFAULT == null ? defaultLogin != null : !DEFAULT_LOGIN_EDEFAULT.equals(defaultLogin);
			case UDDIRegistryPackage.UDDI_REGISTRY__DEFAULT_PASSWORD:
				return DEFAULT_PASSWORD_EDEFAULT == null ? defaultPassword != null : !DEFAULT_PASSWORD_EDEFAULT.equals(defaultPassword);
			case UDDIRegistryPackage.UDDI_REGISTRY__TAXONOMIES:
				return taxonomies != null;
		}
		return eDynamicIsSet(eFeature);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (version: ");
		result.append(version);
		result.append(", discoveryURL: ");
		result.append(discoveryURL);
		result.append(", publicationURL: ");
		result.append(publicationURL);
		result.append(", securedDiscoveryURL: ");
		result.append(securedDiscoveryURL);
		result.append(", securedPublicationURL: ");
		result.append(securedPublicationURL);
		result.append(", defaultLogin: ");
		result.append(defaultLogin);
		result.append(", defaultPassword: ");
		result.append(defaultPassword);
		result.append(')');
		return result.toString();
	}

} //UDDIRegistryImpl
