/**
 * <copyright>
 * </copyright>
 *
 * $Id: UDDIRegistryPackage.java,v 1.2 2005/12/03 04:06:49 cbrealey Exp $
 */
package org.eclipse.wst.ws.internal.model.v10.uddiregistry;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.wst.ws.internal.model.v10.registry.RegistryPackage;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * <!-- begin-model-doc -->
 * 
 *    See http://www.w3.org/XML/1998/namespace.html and
 *    http://www.w3.org/TR/REC-xml for information about this namespace.
 * 
 *     This schema document describes the XML namespace, in a form
 *     suitable for import by other schema documents.  
 * 
 *     Note that local names in this namespace are intended to be defined
 *     only by the World Wide Web Consortium or its subgroups.  The
 *     following names are currently defined in this namespace and should
 *     not be used with conflicting semantics by any Working Group,
 *     specification, or document instance:
 * 
 *     base (as an attribute name): denotes an attribute whose value
 *          provides a URI to be used as the base for interpreting any
 *          relative URIs in the scope of the element on which it
 *          appears; its value is inherited.  This name is reserved
 *          by virtue of its definition in the XML Base specification.
 * 
 *     id   (as an attribute name): denotes an attribute whose value
 *          should be interpreted as if declared to be of type ID.
 *          The xml:id specification is not yet a W3C Recommendation,
 *          but this attribute is included here to facilitate experimentation
 *          with the mechanisms it proposes.  Note that it is _not_ included
 *          in the specialAttrs attribute group.
 * 
 *     lang (as an attribute name): denotes an attribute whose value
 *          is a language code for the natural language of the content of
 *          any element; its value is inherited.  This name is reserved
 *          by virtue of its definition in the XML specification.
 *   
 *     space (as an attribute name): denotes an attribute whose
 *          value is a keyword indicating what whitespace processing
 *          discipline is intended for the content of the element; its
 *          value is inherited.  This name is reserved by virtue of its
 *          definition in the XML specification.
 * 
 *     Father (in any context at all): denotes Jon Bosak, the chair of 
 *          the original XML Working Group.  This name is reserved by 
 *          the following decision of the W3C XML Plenary and 
 *          XML Coordination groups:
 * 
 *              In appreciation for his vision, leadership and dedication
 *              the W3C XML Plenary on this 10th day of February, 2000
 *              reserves for Jon Bosak in perpetuity the XML name
 *              xml:Father
 *   
 * This schema defines attributes and an attribute group
 *         suitable for use by
 *         schemas wishing to allow xml:base, xml:lang, xml:space or xml:id
 *         attributes on elements they define.
 * 
 *         To enable this, such a schema must import this schema
 *         for the XML namespace, e.g. as follows:
 *         &lt;schema . . .&gt;
 *          . . .
 *          &lt;import namespace="http://www.w3.org/XML/1998/namespace"
 *                     schemaLocation="http://www.w3.org/2001/xml.xsd"/&gt;
 * 
 *         Subsequently, qualified reference to any of the attributes
 *         or the group defined below will have the desired effect, e.g.
 * 
 *         &lt;type . . .&gt;
 *          . . .
 *          &lt;attributeGroup ref="xml:specialAttrs"/&gt;
 *  
 *          will define a type which will schema-validate an instance
 *          element with any of those attributes
 * In keeping with the XML Schema WG's standard versioning
 *    policy, this schema document will persist at
 *    http://www.w3.org/2005/08/xml.xsd.
 *    At the date of issue it can also be found at
 *    http://www.w3.org/2001/xml.xsd.
 *    The schema document at that URI may however change in the future,
 *    in order to remain compatible with the latest version of XML Schema
 *    itself, or with the XML namespace itself.  In other words, if the XML
 *    Schema or XML namespaces change, the version of this document at
 *    http://www.w3.org/2001/xml.xsd will change
 *    accordingly; the version at
 *    http://www.w3.org/2005/08/xml.xsd will not change.
 *   
 * <!-- end-model-doc -->
 * @see org.eclipse.wst.ws.internal.model.v10.uddiregistry.UDDIRegistryFactory
 * @generated
 */
public interface UDDIRegistryPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "uddiregistry";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://eclipse.org/wst/ws/internal/model/v10/uddiregistry";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "uddiregistry";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	UDDIRegistryPackage eINSTANCE = org.eclipse.wst.ws.internal.model.v10.uddiregistry.impl.UDDIRegistryPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.wst.ws.internal.model.v10.uddiregistry.impl.DocumentRootImpl <em>Document Root</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.wst.ws.internal.model.v10.uddiregistry.impl.DocumentRootImpl
	 * @see org.eclipse.wst.ws.internal.model.v10.uddiregistry.impl.UDDIRegistryPackageImpl#getDocumentRoot()
	 * @generated
	 */
	int DOCUMENT_ROOT = 0;

	/**
	 * The feature id for the '<em><b>Mixed</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__MIXED = 0;

	/**
	 * The feature id for the '<em><b>XMLNS Prefix Map</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__XMLNS_PREFIX_MAP = 1;

	/**
	 * The feature id for the '<em><b>XSI Schema Location</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__XSI_SCHEMA_LOCATION = 2;

	/**
	 * The feature id for the '<em><b>Taxonomies</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__TAXONOMIES = 3;

	/**
	 * The feature id for the '<em><b>Uddi Registry</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__UDDI_REGISTRY = 4;

	/**
	 * The number of structural features of the the '<em>Document Root</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT_FEATURE_COUNT = 5;

	/**
	 * The meta object id for the '{@link org.eclipse.wst.ws.internal.model.v10.uddiregistry.impl.TaxonomiesImpl <em>Taxonomies</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.wst.ws.internal.model.v10.uddiregistry.impl.TaxonomiesImpl
	 * @see org.eclipse.wst.ws.internal.model.v10.uddiregistry.impl.UDDIRegistryPackageImpl#getTaxonomies()
	 * @generated
	 */
	int TAXONOMIES = 1;

	/**
	 * The feature id for the '<em><b>Taxonomy</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TAXONOMIES__TAXONOMY = 0;

	/**
	 * The number of structural features of the the '<em>Taxonomies</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TAXONOMIES_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link org.eclipse.wst.ws.internal.model.v10.uddiregistry.impl.UDDIRegistryImpl <em>UDDIRegistry</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.wst.ws.internal.model.v10.uddiregistry.impl.UDDIRegistryImpl
	 * @see org.eclipse.wst.ws.internal.model.v10.uddiregistry.impl.UDDIRegistryPackageImpl#getUDDIRegistry()
	 * @generated
	 */
	int UDDI_REGISTRY = 2;

	/**
	 * The feature id for the '<em><b>Name</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UDDI_REGISTRY__NAME = RegistryPackage.REGISTRY__NAME;

	/**
	 * The feature id for the '<em><b>Description</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UDDI_REGISTRY__DESCRIPTION = RegistryPackage.REGISTRY__DESCRIPTION;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UDDI_REGISTRY__ID = RegistryPackage.REGISTRY__ID;

	/**
	 * The feature id for the '<em><b>Location</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UDDI_REGISTRY__LOCATION = RegistryPackage.REGISTRY__LOCATION;

	/**
	 * The feature id for the '<em><b>Ref</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UDDI_REGISTRY__REF = RegistryPackage.REGISTRY__REF;

	/**
	 * The feature id for the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UDDI_REGISTRY__VERSION = RegistryPackage.REGISTRY_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Discovery URL</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UDDI_REGISTRY__DISCOVERY_URL = RegistryPackage.REGISTRY_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Publication URL</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UDDI_REGISTRY__PUBLICATION_URL = RegistryPackage.REGISTRY_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Secured Discovery URL</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UDDI_REGISTRY__SECURED_DISCOVERY_URL = RegistryPackage.REGISTRY_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Secured Publication URL</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UDDI_REGISTRY__SECURED_PUBLICATION_URL = RegistryPackage.REGISTRY_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Default Login</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UDDI_REGISTRY__DEFAULT_LOGIN = RegistryPackage.REGISTRY_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Default Password</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UDDI_REGISTRY__DEFAULT_PASSWORD = RegistryPackage.REGISTRY_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>Taxonomies</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UDDI_REGISTRY__TAXONOMIES = RegistryPackage.REGISTRY_FEATURE_COUNT + 7;

	/**
	 * The number of structural features of the the '<em>UDDIRegistry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UDDI_REGISTRY_FEATURE_COUNT = RegistryPackage.REGISTRY_FEATURE_COUNT + 8;


	/**
	 * Returns the meta object for class '{@link org.eclipse.wst.ws.internal.model.v10.uddiregistry.DocumentRoot <em>Document Root</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Document Root</em>'.
	 * @see org.eclipse.wst.ws.internal.model.v10.uddiregistry.DocumentRoot
	 * @generated
	 */
	EClass getDocumentRoot();

	/**
	 * Returns the meta object for the attribute list '{@link org.eclipse.wst.ws.internal.model.v10.uddiregistry.DocumentRoot#getMixed <em>Mixed</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Mixed</em>'.
	 * @see org.eclipse.wst.ws.internal.model.v10.uddiregistry.DocumentRoot#getMixed()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EAttribute getDocumentRoot_Mixed();

	/**
	 * Returns the meta object for the map '{@link org.eclipse.wst.ws.internal.model.v10.uddiregistry.DocumentRoot#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>XMLNS Prefix Map</em>'.
	 * @see org.eclipse.wst.ws.internal.model.v10.uddiregistry.DocumentRoot#getXMLNSPrefixMap()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_XMLNSPrefixMap();

	/**
	 * Returns the meta object for the map '{@link org.eclipse.wst.ws.internal.model.v10.uddiregistry.DocumentRoot#getXSISchemaLocation <em>XSI Schema Location</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>XSI Schema Location</em>'.
	 * @see org.eclipse.wst.ws.internal.model.v10.uddiregistry.DocumentRoot#getXSISchemaLocation()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_XSISchemaLocation();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.wst.ws.internal.model.v10.uddiregistry.DocumentRoot#getTaxonomies <em>Taxonomies</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Taxonomies</em>'.
	 * @see org.eclipse.wst.ws.internal.model.v10.uddiregistry.DocumentRoot#getTaxonomies()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_Taxonomies();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.wst.ws.internal.model.v10.uddiregistry.DocumentRoot#getUddiRegistry <em>Uddi Registry</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Uddi Registry</em>'.
	 * @see org.eclipse.wst.ws.internal.model.v10.uddiregistry.DocumentRoot#getUddiRegistry()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_UddiRegistry();

	/**
	 * Returns the meta object for class '{@link org.eclipse.wst.ws.internal.model.v10.uddiregistry.Taxonomies <em>Taxonomies</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Taxonomies</em>'.
	 * @see org.eclipse.wst.ws.internal.model.v10.uddiregistry.Taxonomies
	 * @generated
	 */
	EClass getTaxonomies();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.wst.ws.internal.model.v10.uddiregistry.Taxonomies#getTaxonomy <em>Taxonomy</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Taxonomy</em>'.
	 * @see org.eclipse.wst.ws.internal.model.v10.uddiregistry.Taxonomies#getTaxonomy()
	 * @see #getTaxonomies()
	 * @generated
	 */
	EReference getTaxonomies_Taxonomy();

	/**
	 * Returns the meta object for class '{@link org.eclipse.wst.ws.internal.model.v10.uddiregistry.UDDIRegistry <em>UDDIRegistry</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>UDDIRegistry</em>'.
	 * @see org.eclipse.wst.ws.internal.model.v10.uddiregistry.UDDIRegistry
	 * @generated
	 */
	EClass getUDDIRegistry();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.wst.ws.internal.model.v10.uddiregistry.UDDIRegistry#getVersion <em>Version</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Version</em>'.
	 * @see org.eclipse.wst.ws.internal.model.v10.uddiregistry.UDDIRegistry#getVersion()
	 * @see #getUDDIRegistry()
	 * @generated
	 */
	EAttribute getUDDIRegistry_Version();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.wst.ws.internal.model.v10.uddiregistry.UDDIRegistry#getDiscoveryURL <em>Discovery URL</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Discovery URL</em>'.
	 * @see org.eclipse.wst.ws.internal.model.v10.uddiregistry.UDDIRegistry#getDiscoveryURL()
	 * @see #getUDDIRegistry()
	 * @generated
	 */
	EAttribute getUDDIRegistry_DiscoveryURL();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.wst.ws.internal.model.v10.uddiregistry.UDDIRegistry#getPublicationURL <em>Publication URL</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Publication URL</em>'.
	 * @see org.eclipse.wst.ws.internal.model.v10.uddiregistry.UDDIRegistry#getPublicationURL()
	 * @see #getUDDIRegistry()
	 * @generated
	 */
	EAttribute getUDDIRegistry_PublicationURL();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.wst.ws.internal.model.v10.uddiregistry.UDDIRegistry#getSecuredDiscoveryURL <em>Secured Discovery URL</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Secured Discovery URL</em>'.
	 * @see org.eclipse.wst.ws.internal.model.v10.uddiregistry.UDDIRegistry#getSecuredDiscoveryURL()
	 * @see #getUDDIRegistry()
	 * @generated
	 */
	EAttribute getUDDIRegistry_SecuredDiscoveryURL();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.wst.ws.internal.model.v10.uddiregistry.UDDIRegistry#getSecuredPublicationURL <em>Secured Publication URL</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Secured Publication URL</em>'.
	 * @see org.eclipse.wst.ws.internal.model.v10.uddiregistry.UDDIRegistry#getSecuredPublicationURL()
	 * @see #getUDDIRegistry()
	 * @generated
	 */
	EAttribute getUDDIRegistry_SecuredPublicationURL();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.wst.ws.internal.model.v10.uddiregistry.UDDIRegistry#getDefaultLogin <em>Default Login</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Default Login</em>'.
	 * @see org.eclipse.wst.ws.internal.model.v10.uddiregistry.UDDIRegistry#getDefaultLogin()
	 * @see #getUDDIRegistry()
	 * @generated
	 */
	EAttribute getUDDIRegistry_DefaultLogin();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.wst.ws.internal.model.v10.uddiregistry.UDDIRegistry#getDefaultPassword <em>Default Password</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Default Password</em>'.
	 * @see org.eclipse.wst.ws.internal.model.v10.uddiregistry.UDDIRegistry#getDefaultPassword()
	 * @see #getUDDIRegistry()
	 * @generated
	 */
	EAttribute getUDDIRegistry_DefaultPassword();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.wst.ws.internal.model.v10.uddiregistry.UDDIRegistry#getTaxonomies <em>Taxonomies</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Taxonomies</em>'.
	 * @see org.eclipse.wst.ws.internal.model.v10.uddiregistry.UDDIRegistry#getTaxonomies()
	 * @see #getUDDIRegistry()
	 * @generated
	 */
	EReference getUDDIRegistry_Taxonomies();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	UDDIRegistryFactory getUDDIRegistryFactory();

} //UDDIRegistryPackage
