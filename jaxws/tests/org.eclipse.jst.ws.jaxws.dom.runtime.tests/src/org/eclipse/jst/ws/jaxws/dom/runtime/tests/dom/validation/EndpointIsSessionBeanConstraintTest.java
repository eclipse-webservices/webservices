package org.eclipse.jst.ws.jaxws.dom.runtime.tests.dom.validation;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.WSAnnotationFeatures;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.messages.ValidationMessages;
import org.eclipse.jst.ws.jaxws.testutils.IWaitCondition;
import org.eclipse.jst.ws.jaxws.testutils.assertions.Assertions;
import org.eclipse.jst.ws.jaxws.testutils.assertions.ConditionCheckException;
import org.eclipse.jst.ws.jaxws.testutils.jobs.JobUtils;
import org.eclipse.jst.ws.jaxws.testutils.project.TestProject;
import org.eclipse.jst.ws.jaxws.testutils.project.TestProjectsUtils;
import org.eclipse.jst.ws.jaxws.utils.annotations.AnnotationFactory;
import org.eclipse.jst.ws.jaxws.utils.annotations.AnnotationGeneratorException;
import org.eclipse.jst.ws.jaxws.utils.annotations.AnnotationWriter;
import org.eclipse.jst.ws.jaxws.utils.annotations.IAnnotation;
import org.eclipse.jst.ws.jaxws.utils.annotations.IParamValuePair;
import org.eclipse.jst.ws.jaxws.utils.dom.validation.DomValidationConstants;

public class EndpointIsSessionBeanConstraintTest extends TestCase
{
	private TestProject ejbProject;
	private TestProject webProject;
	private static final String CU_NAME = "MyTestClass";
	private static final String CU_PACKAGE = "test";
	private static final String CU_CONTENT = MessageFormat.format("public class {0} '{'public void test()'{'}}", CU_NAME);

	@Override
	protected void setUp() throws Exception
	{
		super.setUp();

		TestProjectsUtils.deleteWorkspaceProjects();
		
		ejbProject = new TestProject(TestProjectsUtils.createEjb3Project("EJB3" + System.currentTimeMillis()).getProject());
		createCompilationUnit(ejbProject);
		webProject = new TestProject(TestProjectsUtils.createWeb25Project("Web25" + System.currentTimeMillis()).getProject());
		createCompilationUnit(webProject);
	}

	private void createCompilationUnit(final TestProject project) throws CoreException
	{
		final IPackageFragment pkg = project.createPackage(CU_PACKAGE);
		final IType cu = project.createType(pkg, CU_NAME + ".java", CU_CONTENT);
		refreshAndBuildProject(project);
		assertEquals("No errors expected", 0, cu.getResource().findMarkers(DomValidationConstants.MARKER_ID, false, IResource.DEPTH_ONE).length);
	}
	
	private void refreshAndBuildProject(final TestProject project) throws CoreException
	{
		project.getProject().refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
		project.getProject().build(IncrementalProjectBuilder.FULL_BUILD, new NullProgressMonitor());
		//FIXME Changed from waitForJobs(). checkMarkers intermittently failing in build. 
		JobUtils.waitForJobsSlow();
	}

	private IType findTestEndpoint(final TestProject project) throws JavaModelException
	{
		final IJavaProject javaP = project.getJavaProject();
		final IType result = javaP.findType(CU_PACKAGE + "." + CU_NAME);
		assertNotNull(result);
		return result;
	}

	@SuppressWarnings("unchecked")
	public void testWsAnnotationInEjbStateful() throws AnnotationGeneratorException, CoreException
	{
		final IType endpointType = findTestEndpoint(ejbProject);

		final IAnnotation<IType> wsann = (IAnnotation<IType>) AnnotationFactory.createAnnotation(WSAnnotationFeatures.WS_ANNOTATION, endpointType, new HashSet<IParamValuePair>());
		AnnotationWriter.getInstance().setAppliedElement(wsann, endpointType);
		refreshAndBuildProject(ejbProject);
		checkMarkers(endpointType.getResource(), IMarker.SEVERITY_ERROR, 1, ValidationMessages.WsValidation_WsAnnotationOnNonSessionBean_Error);

		final IAnnotation<IType> beanAnnotation = (IAnnotation<IType>) AnnotationFactory.createAnnotation("com.sun.xml.internal.ws.developer.Stateful", endpointType, new HashSet<IParamValuePair>());
		AnnotationWriter.getInstance().setAppliedElement(beanAnnotation, endpointType);
		refreshAndBuildProject(ejbProject);
		checkMarkers(endpointType.getResource(), IMarker.SEVERITY_ERROR, 0, ValidationMessages.WsValidation_WsAnnotationOnNonSessionBean_Error);
	}

	@SuppressWarnings("unchecked")
	public void testWsAnnotationInWeb() throws AnnotationGeneratorException, CoreException
	{
		final IType endpointType = findTestEndpoint(webProject);

		final IAnnotation<IType> wsann = (IAnnotation<IType>) AnnotationFactory.createAnnotation(WSAnnotationFeatures.WS_ANNOTATION, endpointType, new HashSet<IParamValuePair>());
		AnnotationWriter.getInstance().setAppliedElement(wsann, endpointType);
		refreshAndBuildProject(webProject);
		checkMarkers(endpointType.getResource(), IMarker.SEVERITY_ERROR, 0, ValidationMessages.WsValidation_WsAnnotationOnNonSessionBean_Error);

	}

	private void checkMarkers(final IResource resource, final int severity, final int expectedMarkers, final String expectedMarkerMessage)
	{
		Assertions.waitAssert(new IWaitCondition()
		{
			public boolean checkCondition() throws ConditionCheckException
			{
				try
				{
					final IMarker[] markers = resource.findMarkers(DomValidationConstants.MARKER_ID, false, IResource.DEPTH_ONE);
					final List<IMarker> matchedMarkers = new ArrayList<IMarker>();
					for (IMarker m : markers)
					{
						if (m.getAttribute(IMarker.SEVERITY) == null || m.getAttribute(IMarker.MESSAGE) == null)
						{
							continue;
						}

						if (((Integer) m.getAttribute(IMarker.SEVERITY) == severity) && (expectedMarkerMessage.equals((String) m.getAttribute(IMarker.MESSAGE))))
						{
							matchedMarkers.add(m);
						}
					}

					return matchedMarkers.size() == expectedMarkers;
				} catch (CoreException e)
				{
					throw new ConditionCheckException(e);
				}
			}
		}, "expected markers not found");
	}

	@Override
	protected void tearDown() throws Exception
	{
		super.tearDown();
		ejbProject.dispose();
		webProject.dispose();
	}

}
