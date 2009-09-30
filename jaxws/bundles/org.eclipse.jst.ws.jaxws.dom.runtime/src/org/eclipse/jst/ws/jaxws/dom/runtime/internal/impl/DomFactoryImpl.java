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

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.DomFactory;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.DomPackage;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IDOM;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IJavaWebServiceElement;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebMethod;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebParam;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebService;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebServiceProject;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebType;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.SOAPBindingParameterStyle;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.SOAPBindingStyle;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.SOAPBindingUse;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.WebParamKind;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class DomFactoryImpl extends EFactoryImpl implements DomFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static DomFactory init() {
		try
		{
			DomFactory theDomFactory = (DomFactory)EPackage.Registry.INSTANCE.getEFactory("http:///org/eclipse/jst/ws/jaxws/dom/runtime/dom.ecore"); //$NON-NLS-1$ 
			if (theDomFactory != null)
			{
				return theDomFactory;
			}
		}
		catch (Exception exception)
		{
			EcorePlugin.INSTANCE.log(exception);
		}
		return new DomFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DomFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID())
		{
			case DomPackage.IDOM: return createIDOM();
			case DomPackage.IJAVA_WEB_SERVICE_ELEMENT: return createIJavaWebServiceElement();
			case DomPackage.ISERVICE_ENDPOINT_INTERFACE: return createIServiceEndpointInterface();
			case DomPackage.IWEB_METHOD: return createIWebMethod();
			case DomPackage.IWEB_PARAM: return createIWebParam();
			case DomPackage.IWEB_SERVICE: return createIWebService();
			case DomPackage.IWEB_SERVICE_PROJECT: return createIWebServiceProject();
			case DomPackage.IWEB_TYPE: return createIWebType();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object createFromString(EDataType eDataType, String initialValue) {
		switch (eDataType.getClassifierID())
		{
			case DomPackage.WEB_PARAM_KIND:
				return createWebParamKindFromString(eDataType, initialValue);
			case DomPackage.SOAP_BINDING_STYLE:
				return createSOAPBindingStyleFromString(eDataType, initialValue);
			case DomPackage.SOAP_BINDING_USE:
				return createSOAPBindingUseFromString(eDataType, initialValue);
			case DomPackage.SOAP_BINDING_PARAMETER_STYLE:
				return createSOAPBindingParameterStyleFromString(eDataType, initialValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String convertToString(EDataType eDataType, Object instanceValue) {
		switch (eDataType.getClassifierID())
		{
			case DomPackage.WEB_PARAM_KIND:
				return convertWebParamKindToString(eDataType, instanceValue);
			case DomPackage.SOAP_BINDING_STYLE:
				return convertSOAPBindingStyleToString(eDataType, instanceValue);
			case DomPackage.SOAP_BINDING_USE:
				return convertSOAPBindingUseToString(eDataType, instanceValue);
			case DomPackage.SOAP_BINDING_PARAMETER_STYLE:
				return convertSOAPBindingParameterStyleToString(eDataType, instanceValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IDOM createIDOM() {
		IDOMImpl idom = new IDOMImpl();
		return idom;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IJavaWebServiceElement createIJavaWebServiceElement() {
		IJavaWebServiceElementImpl iJavaWebServiceElement = new IJavaWebServiceElementImpl();
		return iJavaWebServiceElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IServiceEndpointInterface createIServiceEndpointInterface() {
		IServiceEndpointInterfaceImpl iServiceEndpointInterface = new IServiceEndpointInterfaceImpl();
		return iServiceEndpointInterface;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IWebMethod createIWebMethod() {
		IWebMethodImpl iWebMethod = new IWebMethodImpl();
		return iWebMethod;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IWebParam createIWebParam() {
		IWebParamImpl iWebParam = new IWebParamImpl();
		return iWebParam;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IWebService createIWebService() {
		IWebServiceImpl iWebService = new IWebServiceImpl();
		return iWebService;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IWebServiceProject createIWebServiceProject() {
		IWebServiceProjectImpl iWebServiceProject = new IWebServiceProjectImpl();
		return iWebServiceProject;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IWebType createIWebType() {
		IWebTypeImpl iWebType = new IWebTypeImpl();
		return iWebType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public WebParamKind createWebParamKindFromString(EDataType eDataType, String initialValue) {
		WebParamKind result = WebParamKind.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertWebParamKindToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SOAPBindingStyle createSOAPBindingStyleFromString(EDataType eDataType, String initialValue) {
		SOAPBindingStyle result = SOAPBindingStyle.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertSOAPBindingStyleToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SOAPBindingUse createSOAPBindingUseFromString(EDataType eDataType, String initialValue) {
		SOAPBindingUse result = SOAPBindingUse.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertSOAPBindingUseToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SOAPBindingParameterStyle createSOAPBindingParameterStyleFromString(EDataType eDataType, String initialValue) {
		SOAPBindingParameterStyle result = SOAPBindingParameterStyle.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertSOAPBindingParameterStyleToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DomPackage getDomPackage() {
		return (DomPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static DomPackage getPackage() {
		return DomPackage.eINSTANCE;
	}

} //DomFactoryImpl
