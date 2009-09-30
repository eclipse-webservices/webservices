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
package org.eclipse.jst.ws.jaxws.dom.runtime.persistence.serializer;

import java.util.TreeSet;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.ws.jaxws.dom.runtime.DomUtil;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebServiceProject;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.IAnnotationSerializer;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.JaxWsDefaultsCalculator;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.JaxWsWorkspaceResource;
import org.eclipse.jst.ws.jaxws.dom.runtime.util.ParamValueComparator;
import org.eclipse.jst.ws.jaxws.utils.ContractChecker;
import org.eclipse.jst.ws.jaxws.utils.annotations.AnnotationFactory;
import org.eclipse.jst.ws.jaxws.utils.annotations.AnnotationGeneratorException;
import org.eclipse.jst.ws.jaxws.utils.annotations.AnnotationWriter;
import org.eclipse.jst.ws.jaxws.utils.annotations.IAnnotation;
import org.eclipse.jst.ws.jaxws.utils.annotations.IParamValuePair;
import org.eclipse.jst.ws.jaxws.utils.annotations.IValue;
import org.eclipse.jst.ws.jaxws.utils.logging.ILogger;
import org.eclipse.jst.ws.jaxws.utils.logging.Logger;

/**
 * Base class for DOM adapters that listen to changes in DOM objects and applies it
 * to the underlying java class. Extenders should implement <code>getAnnotation()</code>
 * method to provide the {@link IAnnotation} instance to be saved to java class.
 * 
 * @author Georgi Vachkov
 */
public abstract class AbstractSerializerAdapter extends AdapterImpl implements IAnnotationSerializer
{
	private final JaxWsWorkspaceResource resource;
	private final DomUtil util = new DomUtil();
	private final JaxWsDefaultsCalculator defCalc = new JaxWsDefaultsCalculator();
	
	/**
	 * Constructor
	 * @param resource
	 * @throws NullPointerException in case <code>resource</code> is <code>null</code>
	 */
	public AbstractSerializerAdapter(final JaxWsWorkspaceResource resource) 
	{
		ContractChecker.nullCheckParam(resource, "resource");//$NON-NLS-1$
		
		this.resource = resource;
	}
	
	@Override
	public void notifyChanged(final Notification msg)
	{
		if (!resource.isSaveEnabled() || msg.isTouch()) {
			return;
		}
		
		if (msg.getEventType() != Notification.SET && msg.getEventType() != Notification.UNSET) {
			return;
		}
		
		if (checkValue(msg)) {
			save(msg);
		}
	}
	
	/**
	 * Default value check - works only for String values, trims the
	 * new value checks for <code>null</code> and if it is not null puts the
	 * value to the object and returns <code>true</code> otherwise <code>false</code>
	 * @param msg
	 * @return <code>true</code> in case the value is valid
	 */
	protected boolean checkValue(final Notification msg)
	{
		EObject obj = (EObject)getTarget();
		final String newValue = getNewStringValue(msg);
		if (newValue==null) {			
			return revertValue(obj, msg);
		}
		
		// this call is needed cause newValue might be trimmed
		putValue(obj, (EStructuralFeature)msg.getFeature(), newValue);
		return true;
	}
	
	public void save(final Notification msg)
	{
		boolean processed = false;
		try {
			final IAnnotation<? extends IJavaElement> annotation = getAnnotation();
			if (annotation == null) {
				return;
			}
			
			if (annotation.getParamValuePairs().size()==0 && !isAnnotationRequired()) {
				getAnnotationWriter().remove(annotation);
			} else {
				getAnnotationWriter().update(annotation);
			}
			processed = true;
		} 
		catch (AnnotationGeneratorException e) {
			logger().logError(e.getMessage(), e);
		} 
		catch (CoreException e) {
			logger().logError(e.getMessage(), e);
		}
		finally {
			if(!processed) {
				revertValue((EObject)getTarget(), msg);
			}
		}
	}
	
	protected abstract IAnnotation<? extends IJavaElement> getAnnotation() throws JavaModelException;
	protected abstract boolean isAnnotationRequired();
	
	@Override
	public boolean isAdapterForType(Object type)
	{
	    return IAnnotationSerializer.class==type;
	}
	
	protected JaxWsWorkspaceResource resource()	{
		return resource;
	}
	
	protected DomUtil util() {
		return util;
	}
	
	protected JaxWsDefaultsCalculator defCalc()	{
		return defCalc;
	}
	
	protected AnnotationWriter getAnnotationWriter()
	{
		return AnnotationWriter.getInstance();
	}

	protected IParamValuePair createParamValue(String param, String value)
	{
		final IValue iValue = AnnotationFactory.createStringValue(value);
		return AnnotationFactory.createParamValuePairValue(param, iValue);
	}
	
	protected IParamValuePair createParamValue(String param, boolean value)
	{
		final IValue iValue = AnnotationFactory.createBooleanValue(value);
		return AnnotationFactory.createParamValuePairValue(param, iValue);
	}
	
	protected IType findType(final EObject object, final String typeFQName) throws JavaModelException
	{
		EObject webProject = object.eContainer();
		while(!(webProject instanceof IWebServiceProject) && webProject!=null) {
			webProject = webProject.eContainer();
		}
		
		if (webProject==null) {
			return null;
		}

		final String projectName = ((IWebServiceProject)webProject).getName();
		final IJavaProject javaProject = resource.javaModel().getJavaProject(projectName);		
		return javaProject.findType(typeFQName);
	}
	
	/**
	 * Reads the new string value from <code>msg</code>. 
	 * @param msg
	 * @return <code>null</code> if the value is empty string or <code>null</code>, otherwise the trimmed string value.
	 */
	protected String getNewStringValue(final Notification msg) 
	{
		final String newValue = (msg.getNewStringValue() == null) ? null : msg.getNewStringValue().trim();
		if (newValue != null && newValue.length() > 0) {
			return newValue;
		}
		
		return null;
	}
	
	/**
	 * Reverts the current value for the changed feature to the old value and returns <code>false</code>
	 * @param obj the object to be reverted
	 * @param msg the message
	 * @return always returns false
	 */
	protected boolean revertValue(final EObject obj, final Notification msg)
	{
		putValue(obj, (EStructuralFeature)msg.getFeature(), msg.getOldValue());
		return false;
	}
	
	/**
	 * Puts the <code>newValue</code> to the changed feature.
	 * @param obj the object to be updated
	 * @param msg the notification message
	 * @param newValue the new value to be put
	 */
	protected void putValue(final EObject obj, final EStructuralFeature feature, final Object newValue)
	{
		try {
			obj.eSetDeliver(false);
			obj.eSet(feature, newValue);
		} finally {
			obj.eSetDeliver(true);
		}
	}
	
	protected TreeSet<IParamValuePair> createParamValueSortedTreeSet()
	{
		return new TreeSet<IParamValuePair>(new ParamValueComparator());
	}
	
	private ILogger logger()
	{
		return new Logger();
	}
}
