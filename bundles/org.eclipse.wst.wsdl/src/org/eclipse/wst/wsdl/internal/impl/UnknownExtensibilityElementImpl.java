/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.internal.impl;

import java.util.Collection;
import java.util.Iterator;

import javax.xml.namespace.QName;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.UnknownExtensibilityElement;
import org.eclipse.wst.wsdl.WSDLFactory;
import org.eclipse.wst.wsdl.WSDLPackage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Unknown Extensibility Element</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.wst.wsdl.impl.UnknownExtensibilityElementImpl#getChildren <em>Children</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class UnknownExtensibilityElementImpl extends ExtensibilityElementImpl implements UnknownExtensibilityElement
{
    /**
     * The cached value of the '{@link #getChildren() <em>Children</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getChildren()
     * @generated
     * @ordered
     */
    protected EList children = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected UnknownExtensibilityElementImpl()
    {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass()
    {
        return WSDLPackage.eINSTANCE.getUnknownExtensibilityElement();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList getChildren()
    {
        if (children == null)
        {
            children = new EObjectContainmentEList(UnknownExtensibilityElement.class, this, WSDLPackage.UNKNOWN_EXTENSIBILITY_ELEMENT__CHILDREN);
        }
        return children;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs)
    {
        if (featureID >= 0)
        {
            switch (eDerivedStructuralFeatureID(featureID, baseClass))
            {
            case WSDLPackage.UNKNOWN_EXTENSIBILITY_ELEMENT__CHILDREN:
                return ((InternalEList) getChildren()).basicRemove(otherEnd, msgs);
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
    public Object eGet(EStructuralFeature eFeature, boolean resolve)
    {
        switch (eDerivedStructuralFeatureID(eFeature))
        {
        case WSDLPackage.UNKNOWN_EXTENSIBILITY_ELEMENT__DOCUMENTATION_ELEMENT:
            return getDocumentationElement();
        case WSDLPackage.UNKNOWN_EXTENSIBILITY_ELEMENT__ELEMENT:
            return getElement();
        case WSDLPackage.UNKNOWN_EXTENSIBILITY_ELEMENT__REQUIRED:
            return isRequired() ? Boolean.TRUE : Boolean.FALSE;
        case WSDLPackage.UNKNOWN_EXTENSIBILITY_ELEMENT__ELEMENT_TYPE:
            return getElementType();
        case WSDLPackage.UNKNOWN_EXTENSIBILITY_ELEMENT__CHILDREN:
            return getChildren();
        }
        return eDynamicGet(eFeature, resolve);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void eSet(EStructuralFeature eFeature, Object newValue)
    {
        switch (eDerivedStructuralFeatureID(eFeature))
        {
        case WSDLPackage.UNKNOWN_EXTENSIBILITY_ELEMENT__DOCUMENTATION_ELEMENT:
            setDocumentationElement((Element) newValue);
            return;
        case WSDLPackage.UNKNOWN_EXTENSIBILITY_ELEMENT__ELEMENT:
            setElement((Element) newValue);
            return;
        case WSDLPackage.UNKNOWN_EXTENSIBILITY_ELEMENT__REQUIRED:
            setRequired(((Boolean) newValue).booleanValue());
            return;
        case WSDLPackage.UNKNOWN_EXTENSIBILITY_ELEMENT__ELEMENT_TYPE:
            setElementType((QName) newValue);
            return;
        case WSDLPackage.UNKNOWN_EXTENSIBILITY_ELEMENT__CHILDREN:
            getChildren().clear();
            getChildren().addAll((Collection) newValue);
            return;
        }
        eDynamicSet(eFeature, newValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void eUnset(EStructuralFeature eFeature)
    {
        switch (eDerivedStructuralFeatureID(eFeature))
        {
        case WSDLPackage.UNKNOWN_EXTENSIBILITY_ELEMENT__DOCUMENTATION_ELEMENT:
            setDocumentationElement(DOCUMENTATION_ELEMENT_EDEFAULT);
            return;
        case WSDLPackage.UNKNOWN_EXTENSIBILITY_ELEMENT__ELEMENT:
            setElement(ELEMENT_EDEFAULT);
            return;
        case WSDLPackage.UNKNOWN_EXTENSIBILITY_ELEMENT__REQUIRED:
            setRequired(REQUIRED_EDEFAULT);
            return;
        case WSDLPackage.UNKNOWN_EXTENSIBILITY_ELEMENT__ELEMENT_TYPE:
            setElementType(ELEMENT_TYPE_EDEFAULT);
            return;
        case WSDLPackage.UNKNOWN_EXTENSIBILITY_ELEMENT__CHILDREN:
            getChildren().clear();
            return;
        }
        eDynamicUnset(eFeature);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean eIsSet(EStructuralFeature eFeature)
    {
        switch (eDerivedStructuralFeatureID(eFeature))
        {
        case WSDLPackage.UNKNOWN_EXTENSIBILITY_ELEMENT__DOCUMENTATION_ELEMENT:
            return DOCUMENTATION_ELEMENT_EDEFAULT == null ? documentationElement != null : !DOCUMENTATION_ELEMENT_EDEFAULT.equals(documentationElement);
        case WSDLPackage.UNKNOWN_EXTENSIBILITY_ELEMENT__ELEMENT:
            return ELEMENT_EDEFAULT == null ? element != null : !ELEMENT_EDEFAULT.equals(element);
        case WSDLPackage.UNKNOWN_EXTENSIBILITY_ELEMENT__REQUIRED:
            return required != REQUIRED_EDEFAULT;
        case WSDLPackage.UNKNOWN_EXTENSIBILITY_ELEMENT__ELEMENT_TYPE:
            return ELEMENT_TYPE_EDEFAULT == null ? elementType != null : !ELEMENT_TYPE_EDEFAULT.equals(elementType);
        case WSDLPackage.UNKNOWN_EXTENSIBILITY_ELEMENT__CHILDREN:
            return children != null && !children.isEmpty();
        }
        return eDynamicIsSet(eFeature);
    }

    // 
    // Reconcile methods
    //

    public void setElement(Element element)
    {
        if (element == null && !isReconciling)
        {
            //System.out.println("ExtensibilityElement.setElement(): Preserving old element");
        } 
        else
        {
            setElementGen(element);
        }
    }

    public void reconcileAttributes(Element changedElement)
    {
        //System.out.println("UnknownExtensibilityElementImpl.reconcileAttributes()");
    }

    public void handleUnreconciledElement(Element child, Collection remainingModelObjects)
    {
        //System.out.println("UnknownExtensibilityElementImpl.handleUnreconciledElement()");
        UnknownExtensibilityElement extensibilityElement = WSDLFactory.eINSTANCE.createUnknownExtensibilityElement();
        extensibilityElement.setEnclosingDefinition(getEnclosingDefinition());
        extensibilityElement.setElement(child);
        
        // TODO..  we need to figure out where the child should go in the in current list
        // so that it doesn't always end up going to the end of the list 
        // (since a new element might be added at the start)
        getChildren().add(extensibilityElement);         
    }

    protected void handleReconciliation(Collection remainingModelObjects)
    {
        for (Iterator i = remainingModelObjects.iterator(); i.hasNext();)
        {
            remove(this, i.next());
        }
    }

    protected void remove(Object component, Object modelObject)
    {
        UnknownExtensibilityElement uee = (UnknownExtensibilityElement) component;
        if (modelObject instanceof UnknownExtensibilityElement)
        {
            uee.getChildren().remove(modelObject);
        }
    }

    public Element createElement()
    {
        Definition definition = getEnclosingDefinition();
        if (definition == null)
            return null;

        Document document = definition.getDocument();
        if (document == null)
            document = ((DefinitionImpl) definition).updateDocument();

        Element newElement = null;
        if (element != null) // This is an old element.
        {
            newElement = (Element) document.importNode(element, true);
            element = newElement;
        } 
        else
        {
            String namespace = getElementType().getNamespaceURI();
            String qualifier = definition.getPrefix(namespace);
            newElement = document.createElementNS(namespace, (qualifier == null ? "" : qualifier + ":") + getElementType().getLocalPart());
            element = newElement;
        }

        return newElement;
    }

    private java.util.Map properties = new java.util.HashMap();

    public void setProperty(String key, String value)
    {
        properties.put(key, value);
    }

    protected void changeAttribute(EAttribute eAttribute)
    {
        if (isReconciling)
            return;

        super.changeAttribute(eAttribute);
        Element theElement = getElement();
        if (theElement != null)
        {
            Iterator iterator = properties.entrySet().iterator();
            java.util.Map.Entry entry = null;
            while (iterator.hasNext())
            {
                entry = (java.util.Map.Entry) iterator.next();
                String attribute = (String) entry.getKey();
                String value = (String) entry.getValue();
                niceSetAttribute(theElement, attribute, value);
            }
        }
    }
} //UnknownExtensibilityElementImpl
