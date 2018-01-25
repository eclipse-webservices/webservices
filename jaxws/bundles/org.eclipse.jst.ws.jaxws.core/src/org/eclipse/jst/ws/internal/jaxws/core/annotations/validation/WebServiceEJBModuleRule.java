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
package org.eclipse.jst.ws.internal.jaxws.core.annotations.validation;

import java.util.Collection;
import java.util.Set;

import javax.jws.WebService;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.apt.core.env.EclipseAnnotationProcessorEnvironment;
import org.eclipse.jst.ws.annotations.core.processor.AbstractAnnotationProcessor;
import org.eclipse.jst.ws.annotations.core.utils.AnnotationUtils;
import org.eclipse.jst.ws.internal.jaxws.core.JAXWSCoreMessages;
import org.eclipse.jst.ws.internal.jaxws.core.JAXWSCorePlugin;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;

import com.sun.mirror.declaration.AnnotationMirror;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;
import com.sun.mirror.declaration.ClassDeclaration;
import com.sun.mirror.declaration.Declaration;

public class WebServiceEJBModuleRule extends AbstractAnnotationProcessor {

    private static final String STATELESS = "javax.ejb.Stateless"; //$NON-NLS-1$
    private static final String SINGLETON = "javax.ejb.Singleton"; //$NON-NLS-1$
    private static final String EJB_FACET = "jst.ejb"; //$NON-NLS-1$
    private static final String EJB_FACET_VERSION = "3.0"; //$NON-NLS-1$

    @Override
    public void process() {
        if (environment instanceof EclipseAnnotationProcessorEnvironment) {
            EclipseAnnotationProcessorEnvironment eclipseEnvironment = (EclipseAnnotationProcessorEnvironment) environment;

            IProject project = eclipseEnvironment.getJavaProject().getProject();
            try {
                if (isFacetedProject(project)) {
                    IFacetedProject facetedProject = ProjectFacetsManager.create(project);
                    if (facetedProject.hasProjectFacet(ProjectFacetsManager.getProjectFacet(EJB_FACET))) {
                        IProjectFacetVersion facetVersion = facetedProject.getProjectFacetVersion(ProjectFacetsManager.getProjectFacet(EJB_FACET));
                        if (facetVersion.getVersionString().equals(EJB_FACET_VERSION)) {
                            AnnotationTypeDeclaration webServiceDeclaration = (AnnotationTypeDeclaration) eclipseEnvironment.getTypeDeclaration(WebService.class.getName());
                            Collection<Declaration> annotatedTypes = eclipseEnvironment.getDeclarationsAnnotatedWith(webServiceDeclaration);
                            for (Declaration declaration : annotatedTypes) {
                                if (declaration instanceof ClassDeclaration && getStatelessOrSingletonAnnotation(declaration) == null) {
                                    AnnotationMirror webService = AnnotationUtils.getAnnotation(declaration, WebService.class);
                                    printError(webService.getPosition(), JAXWSCoreMessages.WEBSERVICE_ONLY_ON_STATELESS_OR_SINGLETON_SESSION_BEANS);
                                }
                            }
                        }
                    }
                }
            } catch (CoreException ce) {
                JAXWSCorePlugin.log(ce.getStatus());
            }
        }
    }

    private AnnotationMirror getStatelessOrSingletonAnnotation(Declaration declaration) {
        Collection<AnnotationMirror> aannotationMirrors = declaration.getAnnotationMirrors();

        for (AnnotationMirror annotationMirror : aannotationMirrors) {
            AnnotationTypeDeclaration annotationTypeDeclaration = annotationMirror.getAnnotationType().getDeclaration();
            if (annotationTypeDeclaration != null
                    && (annotationTypeDeclaration.getQualifiedName().equals(STATELESS)
                    		|| annotationTypeDeclaration.getQualifiedName().equals(SINGLETON))) {
                return annotationMirror;
            }
        }
        return null;
    }

    private boolean isFacetedProject(IProject project) throws CoreException {
        Set<IFacetedProject> facetedProjects = ProjectFacetsManager.getFacetedProjects();
        for (IFacetedProject facetedProject : facetedProjects) {
            if (facetedProject.getProject().equals(project)) {
                return true;
            }
        }
        return false;
    }

}
