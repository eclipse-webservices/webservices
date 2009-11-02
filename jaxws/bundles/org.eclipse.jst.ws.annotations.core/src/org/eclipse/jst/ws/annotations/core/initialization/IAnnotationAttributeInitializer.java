/*******************************************************************************
 * Copyright (c) 2009 Shane Clarke.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Shane Clarke - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.annotations.core.initialization;

import java.util.List;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.eclipse.jface.text.contentassist.ICompletionProposal;

/**
 * Base class for initializers contributed to the
 * <code>org.eclipse.jst.ws.annotations.core.annotationInitializer</code> extension point.
 * <p>
 * <strong>Provisional API:</strong> This class/interface is part of an interim API that is still under
 * development and expected to change significantly before reaching stability. It is being made available at
 * this early stage to solicit feedback from pioneering adopters on the understanding that any code that uses
 * this API will almost certainly be broken (repeatedly) as the API evolves.
 * </p>
 */
public interface IAnnotationAttributeInitializer {

	/**
	 * Returns a list of <code>MemberValuePair</code> that may be added to a <code>org.eclipse.jdt.core.dom.NormalAnnotation</code>.
	 * @param javaElement the <code>IJavaElement</code> that is the parent of the <code>org.eclipse.jdt.core.dom.NormalAnnotation</code>
	 * into which the member value pairs may be added.
	 * @param ast the <code>AST</code> with which to create the member value pairs.
	 * @param annotationClass the <code>java.lang.annotation.Annotation</code> class.
	 * @return a list of <code>MemberValuePair</code>.
	 */
    public List<MemberValuePair> getMemberValuePairs(IJavaElement javaElement, AST ast,
            Class<? extends java.lang.annotation.Annotation> annotationClass);

    /**
     * Used to provide a list of <code>ICompletionProposal</code> for a <code>MemberValuePair</code> value.
	 * @param javaElement the <code>IJavaElement</code> that is the parent of the <code>org.eclipse.jdt.core.dom.NormalAnnotation</code>
	 * in which content assist was invoked.
     * @param memberValuePair the <code>MemberValuePair</code> in which content assist was invoked.
     * @return a list of <code>ICompletionProposal</code>.
     */
    public List<ICompletionProposal> getCompletionProposalsForMemberValuePair(IJavaElement javaElement,
            MemberValuePair memberValuePair);

    /**
     * Used to provide a list of <code>ICompletionProposal</code> for a <code>SingleMemberAnnotation</code> value.
     * @param javaElement the <code>IJavaElement</code> that is the parent of the <code>org.eclipse.jdt.core.dom.SingleMemberAnnotation</code>
     * @param singleMemberAnnotation the <code>SingleMemberAnnotation</code> in which content assist was invoked.
     * @return a list of <code>ICompletionProposal</code>.
     */
    public List<ICompletionProposal> getCompletionProposalsForSingleMemberAnnotation(IJavaElement javaElement,
            SingleMemberAnnotation singleMemberAnnotation);


}
