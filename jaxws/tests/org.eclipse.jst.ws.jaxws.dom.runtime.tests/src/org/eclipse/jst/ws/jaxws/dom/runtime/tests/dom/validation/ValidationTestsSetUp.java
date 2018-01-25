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

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.apt.core.internal.AptPlugin;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jst.ws.jaxws.dom.runtime.DomUtil;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebService;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebServiceProject;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.JaxWsWorkspaceResource;
import org.eclipse.jst.ws.jaxws.testutils.IWaitCondition;
import org.eclipse.jst.ws.jaxws.testutils.assertions.Assertions;
import org.eclipse.jst.ws.jaxws.testutils.assertions.ConditionCheckException;
import org.eclipse.jst.ws.jaxws.testutils.dom.WaitingDomUtil;
import org.eclipse.jst.ws.jaxws.testutils.jmock.MockObjectTestCase;
import org.eclipse.jst.ws.jaxws.testutils.project.TestProject;
import org.eclipse.jst.ws.jaxws.testutils.project.TestProjectsUtils;
import org.eclipse.jst.ws.jaxws.utils.resources.FileUtils;

@SuppressWarnings("restriction")
public class ValidationTestsSetUp extends MockObjectTestCase
{
	/**
	 * The APT nonreconcile marker ID
	 */
	protected static final String VALIDATION_PROBLEM_MARKER_ID = AptPlugin.APT_NONRECONCILE_COMPILATION_PROBLEM_MARKER;
	
	protected TestProject testProject;
	protected JaxWsWorkspaceResource target;
	protected IPackageFragment testPack;
	protected DomUtil util;
	
	@Override
	public void setUp() throws Exception
	{
		util = new WaitingDomUtil();
		final TestProject[] fixtureProjects = createFixtureProjects();
		
		IJavaModel javaModel = JavaCore.create(ResourcesPlugin.getWorkspace().getRoot());
		target = new JaxWsWorkspaceResource(javaModel)
		{
			@Override
			public boolean approveProject(IJavaProject prj)
			{
				for (TestProject tp : fixtureProjects)
				{
					if (prj.getElementName().equals(tp.getJavaProject().getElementName()))
					{
						return true;
					}
				}

				return false;
			}
		};
		target.load(null);
		target.startSynchronizing();
	}
	
	protected TestProject[] createFixtureProjects() throws CoreException
	{
		testProject = new TestProject(TestProjectsUtils.createWeb25Project("ValidationTest" + System.currentTimeMillis()));
		testPack = testProject.createPackage("test");
		testProject.setAptProcessingEnabled(true, false);
		testProject.build(IncrementalProjectBuilder.INCREMENTAL_BUILD);
		
		return new TestProject[]{testProject};
	}

	@Override
	public void tearDown() throws Exception
	{
		super.tearDown();
		try {
			disposeFixtureProjects();
		} catch (Exception _) {}
	}
	
	protected void disposeFixtureProjects() throws CoreException
	{
		testProject.dispose();
	}

	protected IWebService findWs(final String wsFQName)
	{
		final IWebServiceProject wsProject = util.findProjectByName(target.getDOM(), testProject.getProject().getName());
		final IWebService[] result = new IWebService[1];
		// WS model might need some time (resource change events processing) to update itself with the web service specified. The code below 
		// would wait some time in order to make sure that the web service is available in the model
		Assertions.waitAssert(new IWaitCondition()
		{
			public boolean checkCondition() throws ConditionCheckException
			{
				result[0] = util.findWsByImplName(wsProject, wsFQName);
				return result[0] != null;
			}
		}, MessageFormat.format("Web service {0} not found", wsFQName));
		return result[0];
	}

	protected IServiceEndpointInterface findSei(final String seiFQName)
	{
		final IWebServiceProject wsProject = util.findProjectByName(target.getDOM(), testProject.getProject().getName());
		final IServiceEndpointInterface[] result = new IServiceEndpointInterface[1];
		// WS model might need some time (resource change events processing) to update itself with the SEI specified. The code below 
		// would wait some time in order to make sure that the SEI is available in the model
		Assertions.waitAssert(new IWaitCondition()
		{
			public boolean checkCondition() throws ConditionCheckException
			{
				result[0] = util.findSeiByImplName(wsProject, seiFQName);
				return result[0] != null;
			}
		}, MessageFormat.format("SEI {0} not found", seiFQName));
		return result[0];
	}
	
	protected void setContents(final ICompilationUnit cu, final String contents) throws CoreException
	{
		final IWorkspaceRunnable setContentsRunnable = new IWorkspaceRunnable()
		{
			public void run(IProgressMonitor monitor) throws CoreException
			{
				String newContent = "package " + testPack.getElementName() + ";\n" + contents;
				FileUtils.getInstance().setCompilationUnitContent(cu, newContent, false, monitor);
				cu.getResource().getProject().build(IncrementalProjectBuilder.INCREMENTAL_BUILD, monitor);
			}
		};

		TestProjectsUtils.executeWorkspaceRunnable(setContentsRunnable);
	}

	/**
	 * Validates whether the resource specified has markers with the specified expecteations
	 * 
	 * @param resource
	 *            the resource to validate
	 * @param markersExpectations
	 *            expectations for resource markers
	 * @throws CoreException
	 */
	protected void validateResourceMarkers(final IResource resource, final MarkerData... markersExpectations) throws CoreException
	{
		validateMarkerCount(resource, markersExpectations);
		for (MarkerData mData : markersExpectations)
		{
			validateSingleResourceMarker(resource, mData);
		}
	}
	
	private void validateMarkerCount(final IResource resource, final MarkerData... markersExpectations) throws CoreException
	{
		final Map<String, Integer> markersCount = new HashMap<String, Integer>();
		for(MarkerData md : markersExpectations)
		{
			if(markersCount.get(md.markerType) == null)
			{
				markersCount.put(md.markerType, 0);
			}
			markersCount.put(md.markerType, markersCount.get(md.markerType) +1);
		}
		
		for(MarkerData md : markersExpectations)
		{
			final IMarker[] markers = resource.findMarkers(md.markerType, true, IResource.DEPTH_INFINITE);
			final int markersFound = markers == null ? 0 : markers.length;
			assertEquals(MessageFormat.format("Unexpected count of markers of type {0}", md.markerType), markersCount.get(md.markerType), new Integer(markersFound));
		}
	}

	private void validateSingleResourceMarker(final IResource resource, final MarkerData markerExpectation) throws CoreException
	{
		final IMarker[] markers = resource.findMarkers(markerExpectation.markerType, true, IResource.DEPTH_INFINITE);
		assertTrue(MessageFormat.format("No markers with type {0} found in resource {1}", markerExpectation.markerType, resource.getName()), markers.length > 0);

		boolean found = false;
		for (IMarker m : markers)
		{
			found = found || markerHasAttributes(m, markerExpectation.markerAttributes);
		}

		assertTrue("Could not find marker with attributes specified: " + markerExpectation.markerAttributes, found);
	}

	private boolean markerHasAttributes(final IMarker marker, final Map<String, Object> attributes) throws CoreException
	{
		if (marker.getAttributes() == null || marker.getAttributes().isEmpty())
		{
			return false;
		}

		for (String attributeName : attributes.keySet())
		{
			final Object markerAttribute = getMarkerAttribute(marker, attributeName);
			if (markerAttribute == null)
			{
				return false;
			}

			if (attributes.get(attributeName).equals(markerAttribute))
			{
				continue;
			}

			return false;
		}

		return true;
	}

	private Object getMarkerAttribute(final IMarker marker, final String attributeName)
	{
		try
		{
			return marker.getAttribute(attributeName);
		} catch (CoreException e)
		{
			return null;
		}
	}

	protected void assertNoValidationErrors(final IResource validatedResource, final String markerType) throws CoreException
	{
		final IMarker[] foundMarkers = validatedResource.findMarkers(markerType, true, IResource.DEPTH_INFINITE);
		final int markersCount = (foundMarkers == null ? 0 : foundMarkers.length);
		assertEquals("Markers unexpectedly found", 0, markersCount);
	}

	/**
	 * Metadata for markers (instances of {@link IMarker} on resources
	 * 
	 * @author I030720
	 */
	protected class MarkerData
	{
		protected final IResource resource;
		protected final Map<String, Object> markerAttributes;
		protected final String markerType;

		/**
		 * Constructor
		 * 
		 * @param resource
		 *            the resource the marker belongs to
		 * @param markerType
		 *            the marker type
		 * @param markersAttributes
		 *            a properties map containing marker attributes names as keys and expected attribute values
		 * @see IMarker#getAttribute(String)
		 */
		public MarkerData(final IResource resource, final String markerType, final Map<String, Object> markerAttributes)
		{
			this.resource = resource;
			this.markerAttributes = markerAttributes;
			this.markerType = markerType;
		}
	}
}
