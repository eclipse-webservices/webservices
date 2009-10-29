package org.eclipse.jst.ws.jaxws.dom.runtime.tests.dom.validation;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashSet;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.ws.jaxws.dom.runtime.DomUtil;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebService;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebServiceProject;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.WsDOMLoadCanceledException;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.Jee5WsDomRuntimeExtension;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.WSAnnotationFeatures;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.messages.ValidationMessages;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.webservice.EndpointIsSessionBeanConstraint;
import org.eclipse.jst.ws.jaxws.testutils.jmock.Mock;
import org.eclipse.jst.ws.jaxws.testutils.jmock.MockObjectTestCase;
import org.eclipse.jst.ws.jaxws.testutils.project.TestProject;
import org.eclipse.jst.ws.jaxws.testutils.project.TestProjectsUtils;
import org.eclipse.jst.ws.jaxws.utils.annotations.AnnotationFactory;
import org.eclipse.jst.ws.jaxws.utils.annotations.AnnotationGeneratorException;
import org.eclipse.jst.ws.jaxws.utils.annotations.AnnotationWriter;
import org.eclipse.jst.ws.jaxws.utils.annotations.IAnnotation;
import org.eclipse.jst.ws.jaxws.utils.annotations.IParamValuePair;
import org.eclipse.jst.ws.jaxws.utils.dom.validation.DomValidationConstants;

public class EndpointIsSessionBeanConstraintTest extends MockObjectTestCase
{
	private TestProject ejbProject;
	private TestProject webProject;
	private static final String CU_NAME = "MyTestClass";
	private static final String CU_PACKAGE = "test";
	private static final String CU_CONTENT = MessageFormat.format("public class {0} '{'public void test()'{'}}", CU_NAME);

	private MyEndpointSessionBeanConstraint constraint;
	
	@Override
	protected void setUp() throws Exception
	{
		super.setUp();

		TestProjectsUtils.deleteWorkspaceProjects();
		
		constraint = new MyEndpointSessionBeanConstraint();
		
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
	}
	
	private IType findTestEndpoint(final TestProject project) throws JavaModelException
	{
		final IJavaProject javaP = project.getJavaProject();
		final IType result = javaP.findType(CU_PACKAGE + "." + CU_NAME);
		assertNotNull(result);
		return result;
	}
	
	private IWebService findWebServiceInProject(final TestProject project) throws IOException, WsDOMLoadCanceledException
	{
		final Jee5WsDomRuntimeExtension jee5Runtime = new Jee5WsDomRuntimeExtension();
		jee5Runtime.createDOM(new NullProgressMonitor());
		IWebServiceProject wsProj = DomUtil.INSTANCE.findProjectByName(jee5Runtime.getDOM(), project.getProject().getName());
		final EList<IWebService> webservices = wsProj.getWebServices();
		
		assertEquals("One web service expected", 1, webservices.size());
		return webservices.get(0);
	}

	@SuppressWarnings("unchecked")
	public void testWsAnnotationInEjbStateful() throws AnnotationGeneratorException, CoreException, IOException, WsDOMLoadCanceledException
	{
		final IType endpointType = findTestEndpoint(ejbProject);

		final IAnnotation<IType> wsann = (IAnnotation<IType>) AnnotationFactory.createAnnotation(WSAnnotationFeatures.WS_ANNOTATION, endpointType, new HashSet<IParamValuePair>());
		AnnotationWriter.getInstance().setAppliedElement(wsann, endpointType);
		refreshAndBuildProject(ejbProject);
		IStatus validationStatus = constraint.doValidate(createValidationConstraint(findWebServiceInProject(ejbProject)));
		assertEquals("Error status expected", IStatus.ERROR, validationStatus.getSeverity());
		assertEquals("Unexpected status message", ValidationMessages.WsValidation_WsAnnotationOnNonSessionBean_Error, validationStatus.getMessage());

		final IAnnotation<IType> beanAnnotation = (IAnnotation<IType>) AnnotationFactory.createAnnotation("com.sun.xml.internal.ws.developer.Stateful", endpointType, new HashSet<IParamValuePair>());
		AnnotationWriter.getInstance().setAppliedElement(beanAnnotation, endpointType);
		refreshAndBuildProject(ejbProject);
		validationStatus = constraint.doValidate(createValidationConstraint(findWebServiceInProject(ejbProject)));
		assertEquals("OK status expected", IStatus.OK, validationStatus.getSeverity());
	}

	private IValidationContext createValidationConstraint(final IWebService ws)
	{
		final Mock<IValidationContext> context = mock(IValidationContext.class);
		context.stubs().method("getTarget").will(returnValue(ws));
		
		return context.proxy();
	}

	@SuppressWarnings("unchecked")
	public void testWsAnnotationInWeb() throws AnnotationGeneratorException, CoreException, IOException, WsDOMLoadCanceledException
	{
		final IType endpointType = findTestEndpoint(webProject);

		final IAnnotation<IType> wsann = (IAnnotation<IType>) AnnotationFactory.createAnnotation(WSAnnotationFeatures.WS_ANNOTATION, endpointType, new HashSet<IParamValuePair>());
		AnnotationWriter.getInstance().setAppliedElement(wsann, endpointType);
		refreshAndBuildProject(webProject);
		final IStatus validationStatus = constraint.doValidate(createValidationConstraint(findWebServiceInProject(webProject)));
		assertEquals("OK status expected", IStatus.OK, validationStatus.getSeverity());

	}

	@Override
	protected void tearDown() throws Exception
	{
		super.tearDown();
		ejbProject.dispose();
		webProject.dispose();
	}
	
	private class MyEndpointSessionBeanConstraint extends EndpointIsSessionBeanConstraint
	{
		@Override
		protected IStatus doValidate(IValidationContext ctx) throws CoreException
		{
			return super.doValidate(ctx);
		}
	}
}
