/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
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
 *******************************************************************************/
package org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryregistry.util;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.ArchiveFile;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibraryRegistry;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibraryRegistryPackage;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibrary;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.PluginProvidedJAXRSLibrary;

/**
 * <!-- begin-user-doc --> The <b>Switch</b> for the model's inheritance
 * hierarchy. It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object and proceeding up the
 * inheritance hierarchy until a non-null result is returned, which is the
 * result of the switch. <!-- end-user-doc -->
 * 
 * @see org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibraryRegistryPackage
 * @generated
 */
public class JAXRSLibraryRegistrySwitch {
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */

	/**
	 * The cached model package <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected static JAXRSLibraryRegistryPackage modelPackage;

	/**
	 * Creates an instance of the switch. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	public JAXRSLibraryRegistrySwitch() {
		if (modelPackage == null) {
			modelPackage = JAXRSLibraryRegistryPackage.eINSTANCE;
		}
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns
	 * a non null result; it yields that result. <!-- begin-user-doc -->
	 * 
	 * @param theEObject
	 *            <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code>
	 *         call.
	 * @generated
	 */
	public Object doSwitch(EObject theEObject) {
		return doSwitch(theEObject.eClass(), theEObject);
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns
	 * a non null result; it yields that result. <!-- begin-user-doc -->
	 * 
	 * @param theEClass
	 * @param theEObject
	 *            <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code>
	 *         call.
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	protected Object doSwitch(EClass theEClass, EObject theEObject) {
		if (theEClass.eContainer() == modelPackage) {
			return doSwitch(theEClass.getClassifierID(), theEObject);
		}
		List eSuperTypes = theEClass.getESuperTypes();
		return eSuperTypes.isEmpty() ? defaultCase(theEObject) : doSwitch(
				(EClass) eSuperTypes.get(0), theEObject);
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns
	 * a non null result; it yields that result. <!-- begin-user-doc -->
	 * 
	 * @param classifierID
	 * @param theEObject
	 *            <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code>
	 *         call.
	 * @generated
	 */
	protected Object doSwitch(int classifierID, EObject theEObject) {
		switch (classifierID) {
		case JAXRSLibraryRegistryPackage.JAXRS_LIBRARY_REGISTRY: {
			JAXRSLibraryRegistry JAXRSLibraryRegistry = (JAXRSLibraryRegistry) theEObject;
			Object result = caseJAXRSLibraryRegistry(JAXRSLibraryRegistry);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case JAXRSLibraryRegistryPackage.JAXRS_LIBRARY: {
			JAXRSLibrary JAXRSLibrary = (JAXRSLibrary) theEObject;
			Object result = caseJAXRSLibrary(JAXRSLibrary);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case JAXRSLibraryRegistryPackage.PLUGIN_PROVIDED_JAXRS_LIBRARY: {
			PluginProvidedJAXRSLibrary pluginProvidedJAXRSLibrary = (PluginProvidedJAXRSLibrary) theEObject;
			Object result = casePluginProvidedJAXRSLibrary(pluginProvidedJAXRSLibrary);
			if (result == null)
				result = caseJAXRSLibrary(pluginProvidedJAXRSLibrary);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case JAXRSLibraryRegistryPackage.ARCHIVE_FILE: {
			ArchiveFile archiveFile = (ArchiveFile) theEObject;
			Object result = caseArchiveFile(archiveFile);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		default:
			return defaultCase(theEObject);
		}
	}

	/**
	 * Returns the result of interpretting the object as an instance of '
	 * <em>JAXRS Library Registry</em>'. <!-- begin-user-doc --> This
	 * implementation returns null; returning a non-null result will terminate
	 * the switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpretting the object as an instance of '
	 *         <em>JAXRS Library Registry</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseJAXRSLibraryRegistry(JAXRSLibraryRegistry object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '
	 * <em>JAXRS Library</em>'. <!-- begin-user-doc --> This implementation
	 * returns null; returning a non-null result will terminate the switch. <!--
	 * end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpretting the object as an instance of '
	 *         <em>JAXRS Library</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseJAXRSLibrary(JAXRSLibrary object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '
	 * <em>Archive File</em>'. <!-- begin-user-doc --> This implementation
	 * returns null; returning a non-null result will terminate the switch. <!--
	 * end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpretting the object as an instance of '
	 *         <em>Archive File</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseArchiveFile(ArchiveFile object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '
	 * <em>Plugin Provided JAXRS Library</em>'. <!-- begin-user-doc --> This
	 * implementation returns null; returning a non-null result will terminate
	 * the switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpretting the object as an instance of '
	 *         <em>Plugin Provided JAXRS Library</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object casePluginProvidedJAXRSLibrary(
			PluginProvidedJAXRSLibrary object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '
	 * <em>EObject</em>'. <!-- begin-user-doc --> This implementation returns
	 * null; returning a non-null result will terminate the switch, but this is
	 * the last case anyway. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpretting the object as an instance of '
	 *         <em>EObject</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject)
	 * @generated
	 */
	public Object defaultCase(EObject object) {
		return null;
	}

} // JAXRSLibraryRegistrySwitch
