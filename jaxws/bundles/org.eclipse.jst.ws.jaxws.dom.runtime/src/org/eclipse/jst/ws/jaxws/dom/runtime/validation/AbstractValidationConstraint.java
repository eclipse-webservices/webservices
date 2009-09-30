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
package org.eclipse.jst.ws.jaxws.dom.runtime.validation;

import static org.eclipse.jst.ws.jaxws.utils.ContractChecker.nullCheckParam;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.eclipse.emf.validation.model.ModelConstraint;
import org.eclipse.emf.validation.service.IConstraintDescriptor;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.annotation.AnnotationAdapterFactory;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.annotation.IAnnotationAdapter;
import org.eclipse.jst.ws.jaxws.dom.runtime.util.Dom2ResourceMapper;
import org.eclipse.jst.ws.jaxws.dom.runtime.util.Jee5DomUtils;
import org.eclipse.jst.ws.jaxws.dom.runtime.util.LocatorExtractor;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.impl.ConstraintStatusExtended;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.impl.ProblemLocation;
import org.eclipse.jst.ws.jaxws.utils.annotations.IAnnotation;
import org.eclipse.jst.ws.jaxws.utils.annotations.ILocator;
import org.eclipse.jst.ws.jaxws.utils.annotations.IParamValuePair;
import org.eclipse.jst.ws.jaxws.utils.logging.ILogger;
import org.eclipse.jst.ws.jaxws.utils.logging.Logger;

/**
 * Base class to be used by other DOM constraints. Provides useful methods for
 * creating {@link IConstraintStatus} during validation, extraction of 
 * error location etc.
 * 
 * @author Georgi Vachkov
 */
public abstract class AbstractValidationConstraint extends ModelConstraint
{
	private ILogger logger;
	private Jee5DomUtils jee5DomUtil;
	
	/**
	 * Constructor 
	 * @param descriptor - the {@link IConstraintDescriptor} for the specific constraint
	 * should not be <code>null</code>
	 */
	public AbstractValidationConstraint(IConstraintDescriptor descriptor) {
		super(descriptor);
		
		nullCheckParam(descriptor, "descriptor");//$NON-NLS-1$
		logger = new Logger();
		jee5DomUtil = Jee5DomUtils.getInstance();
	}
	
	/**
	 * Abstract method called when the real validation have to take place. Override
	 * this method and implement your validation logic. 
	 * @param ctx the validation context 
	 * @return the status of your validation
	 * @throws CoreException
	 */
	protected abstract IStatus doValidate(IValidationContext ctx) throws CoreException; 

	public IStatus validate(IValidationContext ctx) 
	{
		try {
			return doValidate(ctx);
		} catch (CoreException e) {
			logger().logError(e.getMessage(), e);
		}	
		
		return Status.OK_STATUS;
	}
	
	/**
	 * Creates OK status
	 * @param object the validated object
	 * @return {@link IConstraintStatus} instance with severity {@link IStatus#OK}
	 * @throws JavaModelException
	 */
	protected IConstraintStatusExtended createOkStatus(EObject object) throws JavaModelException
	{
		return new ConstraintStatusExtended(this, object, IStatus.OK, 0, "", new HashSet<IProblemLocation>(0));//$NON-NLS-1$
	}
	
	/**
	 * Creates {@link IConstraintStatus} instance with severity depending on the severity defined by 
	 * {@link IConstraintDescriptor#getSeverity()}.
	 * @param object the validated object
	 * @param message the message to be displayed
	 * @param annFQName the fully qualified name on the annotation validated
	 * @param attributeName the name of the attribute validated
	 * @return created object
	 * @throws JavaModelException
	 */
	protected IConstraintStatusExtended createStatus(EObject object, String message, String annFQName, String attributeName) throws JavaModelException
	{
		return createStatus(object, message, getLocator(object, annFQName, attributeName));
	}
	
	protected IConstraintStatusExtended createStatus(final EObject object, final String message, final IProblemLocation locator) 
	{
		final Set<IProblemLocation> problems = new HashSet<IProblemLocation>(1);
		problems.add(locator);
		
		return new ConstraintStatusExtended(this, object, message, problems);
	}

	/**
	 * Creates {@link IProblemLocation} instance out of {@link EObject} instance. 
	 * Defines which is the {@link IResource} containing this DOM object and tries to 
	 * extract {@link ILocator}.
	 * @param eObject
	 * @param annFQName
	 * @param attributeName
	 * @return created location
	 * @throws JavaModelException
	 */
	protected IProblemLocation getLocator(EObject eObject, String annFQName, String attributeName) throws JavaModelException
	{
		final IAnnotationAdapter adapter = (IAnnotationAdapter)AnnotationAdapterFactory.INSTANCE.adapt(eObject, IAnnotationAdapter.class);
		final IAnnotation<? extends IJavaElement> annotation = annFQName==null ? null : adapter.getAnnotation(annFQName);
		if (annotation==null) {
			return new ProblemLocation(findResource(eObject), getLocatorForImplementation(eObject));
		}
		
		ILocator locator = null;
		if (attributeName != null) {
			locator = getLocatorForAttributeValue(annotation, attributeName);
		}
		
		if (locator == null) {
			locator = annotation.getLocator();
		}
		
		return new ProblemLocation(annotation.getAppliedElement().getResource(), locator);
	}

	protected ILocator getLocatorForAttributeValue(IAnnotation<? extends IJavaElement> annotation, String attributeName)
	{
		for (IParamValuePair pair : annotation.getParamValuePairs()) 
		{
			if(pair.getParam().equals(attributeName)) {
				return pair.getValue().getLocator(); 
			}
		}
		
		return null;
	}
	
	protected ILocator getLocatorForImplementation(final EObject eObject) throws JavaModelException  {
		try {
			return LocatorExtractor.getInstance().find(eObject);
		} catch (BadLocationException e) {
			logger().logError(e.getMessage(), e);
		}
		
		return null;
	}
	
	protected IResource findResource(EObject eObject) throws JavaModelException {
		return Dom2ResourceMapper.INSTANCE.findResource(eObject);
	}
	
	protected IAnnotation<? extends IJavaElement> findAnnotation(final EObject eObject, final String annFQName) 
	{
		final IAnnotationAdapter adapter = (IAnnotationAdapter)AnnotationAdapterFactory.INSTANCE.adapt(eObject, IAnnotationAdapter.class);
		return adapter.getAnnotation(annFQName);
	}
	
	protected ILogger logger() {
		return logger;
	}

	protected Jee5DomUtils jee5DomUtil() {
		return jee5DomUtil;
	}
}
