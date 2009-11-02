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
package org.eclipse.jst.ws.internal.jaxws.ui.annotations.initialization;

import static org.eclipse.jst.ws.internal.jaxws.core.utils.JAXWSUtils.VALUE;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.ws.http.HTTPBinding;
import javax.xml.ws.soap.SOAPBinding;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jst.ws.annotations.core.AnnotationsCore;
import org.eclipse.jst.ws.annotations.core.initialization.AnnotationAttributeInitializer;
import org.eclipse.jst.ws.internal.jaxws.ui.JAXWSUIPlugin;
import org.eclipse.swt.graphics.Image;

public class BindingTypeAttributeInitializer extends AnnotationAttributeInitializer {

    private static final String SOAPBinding_SOAP11HTTP_BINDING = "SOAPBinding.SOAP11HTTP_BINDING"; //$NON-NLS-1$
    private static final String SOAPBinding_SOAP12HTTP_BINDING = "SOAPBinding.SOAP12HTTP_BINDING"; //$NON-NLS-1$
    private static final String SOAPBinding_SOAP11HTTP_MTOM_BINDING = "SOAPBinding.SOAP11HTTP_MTOM_BINDING"; //$NON-NLS-1$
    private static final String SOAPBinding_SOAP12HTTP_MTOM_BINDING = "SOAPBinding.SOAP12HTTP_MTOM_BINDING"; //$NON-NLS-1$
    private static final String HTTPBinding_HTTP_BINDING = "HTTPBinding.HTTP_BINDING"; //$NON-NLS-1$

    private static final String SOAP_BINDING = "javax.xml.ws.soap.SOAPBinding"; //$NON-NLS-1$
    private static final String HTTP_BINDING = "javax.xml.ws.http.HTTPBinding"; //$NON-NLS-1$

    public BindingTypeAttributeInitializer() {
        JAXWSUIPlugin.getDefault().getImageRegistry().put(SOAP_BINDING,
                JAXWSUIPlugin.getImageDescriptor("icons/obj16/soapbinding_obj.gif").createImage()); //$NON-NLS-1$
        JAXWSUIPlugin.getDefault().getImageRegistry().put(HTTP_BINDING,
                JAXWSUIPlugin.getImageDescriptor("icons/obj16/httpbinding_obj.gif").createImage()); //$NON-NLS-1$
    }

    @Override
    public List<MemberValuePair> getMemberValuePairs(IJavaElement javaElement, AST ast,
            Class<? extends Annotation> annotationClass) {
        List<MemberValuePair> memberValuePairs = new ArrayList<MemberValuePair>();
        if (javaElement.getElementType() == IJavaElement.TYPE) {
            MemberValuePair value = AnnotationsCore.createStringMemberValuePair(ast, VALUE, getDefault()); //$NON-NLS-1$
            memberValuePairs.add(value);
        }
        return memberValuePairs;
    }

    @Override
    public List<ICompletionProposal> getCompletionProposalsForSingleMemberAnnotation(IJavaElement javaElement,
            SingleMemberAnnotation singleMemberAnnotation) {
        List<ICompletionProposal> completionProposals = new ArrayList<ICompletionProposal>();
        if (javaElement.getElementType() == IJavaElement.TYPE) {
            Expression expression = singleMemberAnnotation.getValue();
            if (expression != null) {
                addQualifiedNameBindingsCompletionProposals(completionProposals, expression);
            }
        }
        return completionProposals;
    }

    @Override
    public List<ICompletionProposal> getCompletionProposalsForMemberValuePair(IJavaElement javaElement,
            MemberValuePair memberValuePair) {
        List<ICompletionProposal> completionProposals = new ArrayList<ICompletionProposal>();
        if (javaElement.getElementType() == IJavaElement.TYPE) {
            IType type = (IType) javaElement;
            String memberValuePairName = memberValuePair.getName().getIdentifier();
            if (memberValuePairName.equals(VALUE)) { //$NON-NLS-1$
                String value = memberValuePair.getValue().toString();
                if (value.equals(MISSING_IDENTIFER) || value.charAt(0) != '\"') {
                    Map<String, String> bindings = getShortNameBindingsMap();
                    Iterator<Map.Entry<String, String>> bindingsIter = bindings.entrySet().iterator();
                    while (bindingsIter.hasNext()) {
                        Map.Entry<String, String> bindingEntry = bindingsIter.next();
                        String proposal = bindingEntry.getKey();
                        String fullyQualifiedTypeName = bindingEntry.getValue();
                        int replacementOffset = memberValuePair.getValue().getStartPosition();
                        int replacementLength = memberValuePair.getValue().getLength();
                        Image image = JAXWSUIPlugin.getDefault().getImageRegistry().get(fullyQualifiedTypeName);
                        completionProposals.add(new BindingTypeCompletionProposal(proposal, type.getCompilationUnit(),
                              replacementOffset, replacementLength, image, new StyledString(proposal), 5,
                              fullyQualifiedTypeName));
                    }
                } else {
                    addQualifiedNameBindingsCompletionProposals(completionProposals, memberValuePair.getValue());
                }
             }
        }
        return completionProposals;
    }

    private void addQualifiedNameBindingsCompletionProposals(List<ICompletionProposal> completionProposals, Expression value) {
        Map<String, String> bindings = getQualifiedNameBindingsMap();
        Iterator<Map.Entry<String, String>> bindingsIter = bindings.entrySet().iterator();
        while (bindingsIter.hasNext()) {
            Map.Entry<String, String> bindingEntry = bindingsIter.next();
            String proposal = bindingEntry.getKey();
            Image image = JAXWSUIPlugin.getDefault().getImageRegistry().get(bindingEntry.getValue());
            completionProposals.add(createCompletionProposal(proposal, value, image, getDisplayString(proposal)));
        }
    }

    public String getDefault() {
        return SOAPBinding.SOAP11HTTP_BINDING;
    }

    private String getDisplayString(String binding) {
        if (binding.equals(SOAPBinding.SOAP11HTTP_BINDING)) {
            return SOAPBinding_SOAP11HTTP_BINDING;
        }
        if (binding.equals(SOAPBinding.SOAP12HTTP_BINDING)) {
            return SOAPBinding_SOAP12HTTP_BINDING;
        }
        if (binding.equals(SOAPBinding.SOAP11HTTP_MTOM_BINDING)) {
            return SOAPBinding_SOAP11HTTP_MTOM_BINDING;
        }
        if (binding.equals(SOAPBinding.SOAP12HTTP_MTOM_BINDING)) {
            return SOAPBinding_SOAP12HTTP_MTOM_BINDING;
        }
        if (binding.equals(HTTPBinding.HTTP_BINDING)) {
            return HTTPBinding_HTTP_BINDING;
        }
        return binding;
    }

    private Map<String, String> getQualifiedNameBindingsMap() {
        Map<String, String> bindings = new HashMap<String, String>();
        bindings.put(SOAPBinding.SOAP11HTTP_BINDING, SOAPBinding.class.getCanonicalName());
        bindings.put(SOAPBinding.SOAP12HTTP_BINDING, SOAPBinding.class.getCanonicalName());
        bindings.put(SOAPBinding.SOAP11HTTP_MTOM_BINDING, SOAPBinding.class.getCanonicalName());
        bindings.put(SOAPBinding.SOAP12HTTP_MTOM_BINDING, SOAPBinding.class.getCanonicalName());
        bindings.put(HTTPBinding.HTTP_BINDING, HTTPBinding.class.getCanonicalName());
        return bindings;
    }

    private Map<String, String> getShortNameBindingsMap() {
        Map<String, String> bindings = new HashMap<String, String>();
        bindings.put("SOAPBinding.SOAP11HTTP_BINDING", SOAPBinding.class.getCanonicalName());
        bindings.put("SOAPBinding.SOAP12HTTP_BINDING", SOAPBinding.class.getCanonicalName());
        bindings.put("SOAPBinding.SOAP11HTTP_MTOM_BINDING", SOAPBinding.class.getCanonicalName());
        bindings.put("SOAPBinding.SOAP12HTTP_MTOM_BINDING", SOAPBinding.class.getCanonicalName());
        bindings.put("HTTPBinding.HTTP_BINDING", HTTPBinding.class.getCanonicalName());
        return bindings;
    }
}