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
package org.eclipse.jst.ws.jaxws.dom.runtime.tests.dom.validation;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.service.IValidationListener;
import org.eclipse.emf.validation.service.ModelValidationService;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.ws.jaxws.dom.runtime.DomUtil;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebService;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebServiceProject;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.JaxWsWorkspaceResource;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.JaxWsDomValidator;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.WsProblemsReporter;
import org.eclipse.jst.ws.jaxws.testutils.jmock.Mock;
import org.eclipse.jst.ws.jaxws.testutils.jmock.MockObjectTestCase;
import org.eclipse.jst.ws.jaxws.testutils.project.TestProject;
import org.eclipse.jst.ws.jaxws.utils.resources.FileUtils;
import org.jmock.core.Constraint;

public class ValidationTestsSetUp extends MockObjectTestCase
{
	protected TestProject testProject;
	protected JaxWsWorkspaceResource target;
	protected IPackageFragment testPack;
	private JaxWsDomValidator validator;
	protected DomUtil util = DomUtil.INSTANCE;
	
	@Override
	public void setUp() throws Exception
	{
		testProject = new TestProject();
		testProject.createSourceFolder("src");
		testPack = testProject.createPackage("test");
		validator = new JaxWsDomValidator();
		
		IJavaModel javaModel = JavaCore.create(ResourcesPlugin.getWorkspace().getRoot());
		target = new JaxWsWorkspaceResource(javaModel) 
		{
			@Override
			public boolean approveProject(IJavaProject prj) {
				return prj.getElementName().equals(testProject.getJavaProject().getElementName());
			}
		};
		target.load(null);
		target.startSynchronizing();
	}

	@Override
	public void tearDown() throws Exception
	{
		super.tearDown();
		try {
			testProject.dispose();
		} catch (Exception _) {}
	}

	protected IWebService findWs(String wsFQName)
	{
		final IWebServiceProject wsProject = util.findProjectByName(target.getDOM(), testProject.getProject().getName());
		return util.findWsByImplName(wsProject, wsFQName);			
	}

	protected IServiceEndpointInterface findSei(String seiFQName) 
	{
		final IWebServiceProject wsProject = util.findProjectByName(target.getDOM(), testProject.getProject().getName());
		return util.findSeiByImplName(wsProject, seiFQName);		
	}
	
	protected void setContents(final ICompilationUnit cu, final String contents) throws JavaModelException 
	{
		String newContent = "import " + testPack.getElementName() + ";\n" + contents;
		FileUtils.getInstance().setCompilationUnitContent(cu, newContent, false, null);
	}

	/**
	 * Forces validation of the object specified and verifies that the resource is marked with a marker which has the supplied properties  
	 * @param object the object to validate
	 * @param markersExpectations expectations for resource markers 
	 */
	protected void validate(final EObject object, final MarkerData... markersExpectations)
	{
		final List<IValidationListener> testListeners = new LinkedList<IValidationListener>();
		final Map<IResource, Mock<IResource>> resourceToMockMap = new HashMap<IResource, Mock<IResource>>();
		try
		{
			for(MarkerData expectation : markersExpectations)
			{
				Mock<IResource> resource = resourceToMockMap.get(expectation.resource);
				if(resource == null)
				{
					resource = mock(IResource.class, "Mock for resource '" + expectation.resource.getName() + "'");
					resourceToMockMap.put(expectation.resource, resource);
				}
				
				// No marker attributes => assume no markers are expected
				if(expectation.markerAttributes.isEmpty())
				{
					continue;
				}
				final IMarker marker = createMarkerWithExpectations(resource, expectation.markerType, expectation.markerAttributes);
				resource.expects(once()).method("createMarker").with(eq(expectation.markerType)).will(returnValue(marker));
				
				final TestValidationListener l = new TestValidationListener(expectation.resource, resource.proxy(), new WsProblemsReporter(), this);
				testListeners.add(l);
				ModelValidationService.getInstance().addValidationListener(l);
			}
			
			validator.validate(object);
		}
		finally
		{
			for(IValidationListener l : testListeners)
			{
				ModelValidationService.getInstance().removeValidationListener(l);
			}
		}
		
	}
	
	private IMarker createMarkerWithExpectations(final Mock<IResource> resourceMock, final String markerType, final Map<Object, Constraint> markerAttributes)
	{
		final Mock<IMarker> markerMock = mock(IMarker.class);
		for (final Object propKey : markerAttributes.keySet())
		{
			markerMock.expects(once()).method("setAttribute").with(eq(propKey), markerAttributes.get(propKey));
		}

		// Accept setting other attributes
		markerMock.stubs().method("setAttribute").with(notInConstraint(markerAttributes.keySet()), ANYTHING);
		
		return markerMock.proxy();
	}

	private Constraint notInConstraint(final Set<Object> set)
	{
		return new Constraint()
		{
			public boolean eval(Object arg0)
			{
				return !set.contains(arg0);
			}

			public StringBuffer describeTo(StringBuffer arg0)
			{
				return arg0;
			}
		};
	}

	protected void assertNoValidationErrors(final IResource validatedResource, final String markerType, final EObject eObject)
	{
		final MarkerData noErrorsData = new MarkerData(validatedResource, markerType, new HashMap<Object, Constraint>());
		validate(eObject, noErrorsData);
	}
	
	/**
	 * Metadata for markers (instances of {@link IMarker} on resources
	 * @author I030720
	 */
	protected class MarkerData
	{
		protected final IResource resource;
		protected final Map<Object, Constraint> markerAttributes;
		protected final String markerType;

		/**
		 * Constructor
		 * @param resource the resource the marker belongs to
		 * @param markerType the marker type
		 * @param markersAttributes a properties map containing marker attributes names as keys and expectations for attribute values 
		 * @see IMarker#getAttribute(String) 
		 */
		public MarkerData(final IResource resource,  final String markerType, final Map<Object, Constraint> markerAttributes)
		{
			this.resource = resource;
			this.markerAttributes = markerAttributes;
			this.markerType = markerType;
		}
	}
	
}
