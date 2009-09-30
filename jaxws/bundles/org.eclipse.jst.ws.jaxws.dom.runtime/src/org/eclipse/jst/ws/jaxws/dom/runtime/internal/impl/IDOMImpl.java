/*******************************************************************************
 * Copyright (c) 2009 by SAP AG, Walldorf. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     SAP AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.jaxws.dom.runtime.internal.impl;


import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.DomPackage;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IDOM;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebServiceProject;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>IDOM</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.jst.ws.jaxws.dom.runtime.internal.impl.IDOMImpl#getWebServiceProjects <em>Web Service Projects</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class IDOMImpl extends EObjectImpl implements IDOM {
	/**
	 * The cached value of the '{@link #getWebServiceProjects() <em>Web Service Projects</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWebServiceProjects()
	 * @generated
	 * @ordered
	 */
	protected EList<IWebServiceProject> webServiceProjects;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected IDOMImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DomPackage.Literals.IDOM;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<IWebServiceProject> getWebServiceProjects() {
		if (webServiceProjects == null)
		{
			webServiceProjects = new EObjectContainmentEList<IWebServiceProject>(IWebServiceProject.class, this, DomPackage.IDOM__WEB_SERVICE_PROJECTS);
		}
		return webServiceProjects;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID)
		{
			case DomPackage.IDOM__WEB_SERVICE_PROJECTS:
				return ((InternalEList<?>)getWebServiceProjects()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID)
		{
			case DomPackage.IDOM__WEB_SERVICE_PROJECTS:
				return getWebServiceProjects();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID)
		{
			case DomPackage.IDOM__WEB_SERVICE_PROJECTS:
				getWebServiceProjects().clear();
				getWebServiceProjects().addAll((Collection<? extends IWebServiceProject>)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID)
		{
			case DomPackage.IDOM__WEB_SERVICE_PROJECTS:
				getWebServiceProjects().clear();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID)
		{
			case DomPackage.IDOM__WEB_SERVICE_PROJECTS:
				return webServiceProjects != null && !webServiceProjects.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //IDOMImpl
