/*******************************************************************************
 * Copyright (c) 2009, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20091021   291954 ericdp@ca.ibm.com - Eric D. Peters, JAX-RS: Implement JAX-RS Facet
 * 20100420   309846 ericdp@ca.ibm.com - Eric D. Peters, Remove dead code related to e.p. pluginProvidedJaxrsLibraries
 *******************************************************************************/
package org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '
 * <em><b>Archive File</b></em>'. <!-- end-user-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>
 * {@link org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.ArchiveFile#isRelativeToWorkspace
 * <em>Relative To Workspace</em>}</li>
 * <li>
 * {@link org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.ArchiveFile#getSourceLocation
 * <em>Source Location</em>}</li>
 * <li>
 * {@link org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.ArchiveFile#getRelativeDestLocation
 * <em>Relative Dest Location</em>}</li>
 * <li>
 * {@link org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.ArchiveFile#getJAXRSLibrary
 * <em>JAXRS Library</em>}</li>
 * </ul>
 * </p>
 * 
 * @see org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibraryRegistryPackage#getArchiveFile()
 * @model
 * @generated
 * 
 * @deprecated
 * 
 * <p>
 * <b>Provisional API - subject to change - do not use</b>
 * </p>
 */
public interface ArchiveFile extends EObject {
	/**
	 * Returns the value of the '<em><b>Source Location</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Source Location</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Source Location</em>' attribute.
	 * @see #setSourceLocation(String)
	 * @see org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibraryRegistryPackage#getArchiveFile_SourceLocation()
	 * @model required="true"
	 * @generated
	 */
	String getSourceLocation();

	/**
	 * Sets the value of the '
	 * {@link org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.ArchiveFile#getSourceLocation
	 * <em>Source Location</em>}' attribute. <!-- begin-user-doc --> If the
	 * value passed is found to be relative to the workspace, a
	 * workspace-relative location is stored; to prevent this behaviour, call
	 * isRelativeToWorkspace(false) before calling this method. <!--
	 * end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Source Location</em>' attribute.
	 * @see #getSourceLocation()
	 * @generated
	 */
	void setSourceLocation(String value);

	/**
	 * Returns the value of the '<em><b>Relative To Workspace</b></em>'
	 * attribute. The default value is <code>"true"</code>. <!-- begin-user-doc
	 * -->
	 * <p>
	 * If the meaning of the '<em>Relative To Workspace</em>' attribute isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Relative To Workspace</em>' attribute.
	 * @see #setRelativeToWorkspace(boolean)
	 * @see org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibraryRegistryPackage#getArchiveFile_RelativeToWorkspace()
	 * @model default="true" required="true"
	 * @generated
	 */
	boolean isRelativeToWorkspace();

	/**
	 * Sets the value of the '
	 * {@link org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.ArchiveFile#isRelativeToWorkspace
	 * <em>Relative To Workspace</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Relative To Workspace</em>'
	 *            attribute.
	 * @see #isRelativeToWorkspace()
	 * @generated
	 */
	void setRelativeToWorkspace(boolean value);

	/**
	 * Returns the value of the '<em><b>Relative Dest Location</b></em>'
	 * attribute. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Relative Dest Location</em>' attribute isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Relative Dest Location</em>' attribute.
	 * @see #setRelativeDestLocation(String)
	 * @see org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibraryRegistryPackage#getArchiveFile_RelativeDestLocation()
	 * @model required="true"
	 * @generated
	 */
	String getRelativeDestLocation();

	/**
	 * Sets the value of the '
	 * {@link org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.ArchiveFile#getRelativeDestLocation
	 * <em>Relative Dest Location</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Relative Dest Location</em>'
	 *            attribute.
	 * @see #getRelativeDestLocation()
	 * @generated
	 */
	void setRelativeDestLocation(String value);

	/**
	 * Returns the value of the '<em><b>JAXRS Library</b></em>' container
	 * reference. It is bidirectional and its opposite is '
	 * {@link org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibrary#getArchiveFiles
	 * <em>Archive Files</em>}'. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>JAXRS Library</em>' container reference isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>JAXRS Library</em>' container reference.
	 * @see #setJAXRSLibrary(JAXRSLibrary)
	 * @see org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibraryRegistryPackage#getArchiveFile_JAXRSLibrary()
	 * @see org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibrary#getArchiveFiles
	 * @model opposite="ArchiveFiles" required="true"
	 * @generated
	 */
	JAXRSLibrary getJAXRSLibrary();

	/**
	 * Sets the value of the '
	 * {@link org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.ArchiveFile#getJAXRSLibrary
	 * <em>JAXRS Library</em>}' container reference. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>JAXRS Library</em>' container
	 *            reference.
	 * @see #getJAXRSLibrary()
	 * @generated
	 */
	void setJAXRSLibrary(JAXRSLibrary value);

	/**
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Path</em>' attribute isn't clear, there really
	 * should be more of a description here...
	 * </p>
	 * 
	 * @return the path <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 * @generated
	 */
	String getPath();

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @return the name <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 * @generated
	 */
	String getName();

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @return true if exists <!-- end-user-doc -->
	 * @model required="true"
	 * @generated
	 */
	boolean exists();

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @param object
	 * @return true if equal <!-- end-user-doc -->
	 * @model required="true" objectRequired="true"
	 * @generated
	 */
	boolean equals(Object object);

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @return the hashCode <!-- end-user-doc -->
	 * @model required="true"
	 * @generated
	 */
	int hashCode();

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @param baseDestLocation
	 * @return true if copied succeeds <!-- end-user-doc -->
	 * @model required="true" baseDestLocationRequired="true"
	 * @generated
	 */
	boolean copyTo(String baseDestLocation);

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @return the resolved source location <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 * @generated
	 */
	String getResolvedSourceLocation();

} // ArchiveFile
