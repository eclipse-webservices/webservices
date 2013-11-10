package org.eclipse.jst.ws.jaxws.dom.runtime.tests.dom.validation;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.ws.internal.jaxws.core.JAXWSCoreMessages;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.WsDOMLoadCanceledException;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.WSAnnotationFeatures;
import org.eclipse.jst.ws.jaxws.testutils.project.TestProject;
import org.eclipse.jst.ws.jaxws.testutils.project.TestProjectsUtils;
import org.eclipse.jst.ws.jaxws.utils.annotations.AnnotationFactory;
import org.eclipse.jst.ws.jaxws.utils.annotations.AnnotationGeneratorException;
import org.eclipse.jst.ws.jaxws.utils.annotations.AnnotationWriter;
import org.eclipse.jst.ws.jaxws.utils.annotations.IAnnotation;
import org.eclipse.jst.ws.jaxws.utils.annotations.IParamValuePair;

public class EndpointIsSessionBeanRuleTest extends ValidationTestsSetUp
{
	private TestProject ejbProject;
	private TestProject webProject;
	private static final String CU_NAME = "MyTestClass";
	private static final String CU_PACKAGE = "test";
	private static final String CU_CONTENT = MessageFormat.format("public class {0} '{'public void test()'{'}}", CU_NAME);

	@Override
	public void setUp() throws Exception
	{
		super.setUp();

		TestProjectsUtils.deleteWorkspaceProjects();

		ejbProject = new TestProject(TestProjectsUtils.createEjb3Project("EJB3" + System.currentTimeMillis()).getProject());
		createCompilationUnit(ejbProject);
		ejbProject.setAptProcessingEnabled(true, false);

		webProject = new TestProject(TestProjectsUtils.createWeb25Project("Web25" + System.currentTimeMillis()).getProject());
		createCompilationUnit(webProject);
		webProject.setAptProcessingEnabled(true, false);
	}

	@Override
	protected TestProject[] createFixtureProjects() throws CoreException
	{
		ejbProject = new TestProject(TestProjectsUtils.createEjb3Project("EJB3" + System.currentTimeMillis()).getProject());
		createCompilationUnit(ejbProject);
		ejbProject.setAptProcessingEnabled(true, false);

		webProject = new TestProject(TestProjectsUtils.createWeb25Project("Web25" + System.currentTimeMillis()).getProject());
		createCompilationUnit(webProject);
		webProject.setAptProcessingEnabled(true, false);

		return new TestProject[] { ejbProject, webProject };
	}

	private void createCompilationUnit(final TestProject project) throws CoreException
	{
		final IPackageFragment pkg = project.createPackage(CU_PACKAGE);
		final IType cu = project.createType(pkg, CU_NAME + ".java", CU_CONTENT);
		refreshAndBuildProject(project);
		assertNoValidationErrors(cu.getResource(), VALIDATION_PROBLEM_MARKER_ID);
	}

	private void refreshAndBuildProject(final TestProject project) throws CoreException
	{
		final IWorkspaceRunnable runnable = new IWorkspaceRunnable()
		{
			public void run(IProgressMonitor monitor) throws CoreException
			{
				project.getProject().refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
				project.getProject().build(IncrementalProjectBuilder.FULL_BUILD, new NullProgressMonitor());
			}
		};

		TestProjectsUtils.executeWorkspaceRunnable(runnable);
	}

	private IType findTestEndpoint(final TestProject project) throws JavaModelException
	{
		final IJavaProject javaP = project.getJavaProject();
		final IType result = javaP.findType(CU_PACKAGE + "." + CU_NAME);
		assertNotNull(result);
		return result;
	}

	@SuppressWarnings("unchecked")
	public void testWsAnnotationOnJavaClassInEjbProject() throws AnnotationGeneratorException, CoreException, IOException, WsDOMLoadCanceledException
	{
		final IType endpointType = findTestEndpoint(ejbProject);

		final IAnnotation<IType> wsann = (IAnnotation<IType>) AnnotationFactory.createAnnotation(WSAnnotationFeatures.WS_ANNOTATION, endpointType, new HashSet<IParamValuePair>());
		AnnotationWriter.getInstance().setAppliedElement(wsann, endpointType);
		refreshAndBuildProject(ejbProject);
		final Map<String, Object> markersExpectations = new HashMap<String, Object>();
		markersExpectations.put(IMarker.CHAR_START, 46);
		markersExpectations.put(IMarker.CHAR_END, 56);
		markersExpectations.put(IMarker.LINE_NUMBER, 5);
		markersExpectations.put(IMarker.MESSAGE, JAXWSCoreMessages.WEBSERVICE_ONLY_ON_STATELESS_OR_SINGLETON_SESSION_BEANS);
		validateResourceMarkers(endpointType.getResource(), new MarkerData(endpointType.getResource(), VALIDATION_PROBLEM_MARKER_ID, markersExpectations));
	}

	@SuppressWarnings("unchecked")
	public void testWsAnnotationInEjbStateless() throws AnnotationGeneratorException, CoreException, IOException, WsDOMLoadCanceledException
	{
		final IType endpointType = findTestEndpoint(ejbProject);
		final IAnnotation<IType> beanAnnotation = (IAnnotation<IType>) AnnotationFactory.createAnnotation("javax.ejb.Stateless", endpointType, new HashSet<IParamValuePair>());
		AnnotationWriter.getInstance().setAppliedElement(beanAnnotation, endpointType);
		refreshAndBuildProject(ejbProject);
		assertNoValidationErrors(endpointType.getResource(), VALIDATION_PROBLEM_MARKER_ID);
	}

	@SuppressWarnings("unchecked")
	public void testWsAnnotationInWeb() throws AnnotationGeneratorException, CoreException, IOException, WsDOMLoadCanceledException
	{
		final IType endpointType = findTestEndpoint(webProject);

		final IAnnotation<IType> wsann = (IAnnotation<IType>) AnnotationFactory.createAnnotation(WSAnnotationFeatures.WS_ANNOTATION, endpointType, new HashSet<IParamValuePair>());
		AnnotationWriter.getInstance().setAppliedElement(wsann, endpointType);
		refreshAndBuildProject(webProject);
		assertNoValidationErrors(endpointType.getResource(), VALIDATION_PROBLEM_MARKER_ID);
	}

	@Override
	public void tearDown() throws Exception
	{
		super.tearDown();
	}

	@Override
	protected void disposeFixtureProjects() throws CoreException
	{
		ejbProject.dispose();
		webProject.dispose();
	}
}
