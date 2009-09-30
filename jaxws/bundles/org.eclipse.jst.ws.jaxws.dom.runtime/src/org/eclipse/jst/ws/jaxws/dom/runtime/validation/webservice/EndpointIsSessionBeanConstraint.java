package org.eclipse.jst.ws.jaxws.dom.runtime.validation.webservice;

import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.jdt.core.IType;
import org.eclipse.jst.j2ee.model.IModelProvider;
import org.eclipse.jst.j2ee.model.ModelProviderManager;
import org.eclipse.jst.javaee.ejb.SessionBean;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebService;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.WSAnnotationFeatures;
import org.eclipse.jst.ws.jaxws.dom.runtime.util.Dom2ResourceMapper;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.AbstractValidationConstraint;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.messages.ValidationMessages;
import org.eclipse.jst.ws.jaxws.utils.facets.FacetUtils;
import org.eclipse.jst.ws.jaxws.utils.facets.IFacetUtils;

/**
 * Constraint for validating whether a class in a EJB module annotated with the "@WebService" annotation is a session bean
 * 
 * @author Danail Branekov
 */
public class EndpointIsSessionBeanConstraint extends AbstractValidationConstraint
{
	public EndpointIsSessionBeanConstraint()
	{
		super(new WsConstraintDescriptor());
	}

	@Override
	protected IStatus doValidate(final IValidationContext ctx) throws CoreException
	{
		final IWebService ws = (IWebService) ctx.getTarget();
		final IType wsType = Dom2ResourceMapper.INSTANCE.findType(ws);
		if (wsType == null || wsType.getResource() == null)
		{
			return createOkStatus(ws);
		}

		final IProject project = wsType.getResource().getProject();
		if (!facetUtils().hasFacetWithVersion(project, IFacetUtils.EJB_30_FACET_VERSION, IFacetUtils.EJB_30_FACET_ID))
		{
			return createOkStatus(ws);
		}

		if (!isSessionBean(wsType))
		{
			return createStatus(ws, ValidationMessages.WsValidation_WsAnnotationOnNonSessionBean_Error, WSAnnotationFeatures.WS_ANNOTATION, null);
		}

		return createOkStatus(ws);

	}

	private IFacetUtils facetUtils()
	{
		return new FacetUtils();
	}

	@SuppressWarnings("unchecked")
	private boolean isSessionBean(final IType endpointType)
	{
		final IModelProvider provider = ModelProviderManager.getModelProvider(endpointType.getResource().getProject());
		final Object modelObject = provider.getModelObject();
		final List<SessionBean> sessionBeans = ((org.eclipse.jst.javaee.ejb.EJBJar) modelObject).getEnterpriseBeans().getSessionBeans();
		for (SessionBean bean : sessionBeans)
		{
			if (bean.getEjbClass().equals(endpointType.getFullyQualifiedName()))
			{
				return true;
			}
		}

		return false;
	}
}
