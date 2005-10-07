/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.axis.consumption.ui.command;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import javax.wsdl.Definition;
import javax.wsdl.Import;
import javax.wsdl.xml.WSDLWriter;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.internal.plugin.WebServicePlugin;
import org.eclipse.wst.command.internal.env.common.FileResourceUtils;
import org.eclipse.wst.command.internal.provisional.env.core.AbstractDataModelOperation;
import org.eclipse.wst.command.internal.provisional.env.core.common.MessageUtils;
import org.eclipse.wst.command.internal.provisional.env.core.common.StatusUtils;
import org.eclipse.wst.command.internal.provisional.env.core.context.ResourceContext;
import org.eclipse.wst.common.environment.Environment;
import org.eclipse.wst.ws.internal.parser.wsil.WebServicesParser;


/**
 *
 */
public class CopyClientWSDLCommand extends AbstractDataModelOperation
{
  private String pluginId_ = "org.eclipse.jst.ws.axis.consumption.ui";
  private MessageUtils msgUtils_;  
  private String wsdlURL_;
  private String clientWSDLPathName_;
  private WebServicesParser wsParser_;
  
  public CopyClientWSDLCommand()
  {
    msgUtils_ = new MessageUtils(pluginId_ + ".plugin", this);    
  }
  
	public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable ) 
	{
		Environment env = getEnvironment();
    IStatus status = Status.OK_STATUS;
    Definition def = wsParser_.getWSDLDefinition(wsdlURL_);
    if(def==null)
    {
      status = StatusUtils.errorStatus( msgUtils_.getMessage("MSG_ERROR_WSDL_NO_DEFINITION",new String[]{wsdlURL_}));
      env.getStatusHandler().reportError(status);
      return status;
    }
    IPath clientWSDLPath = new Path(clientWSDLPathName_);
    IWorkspaceRoot workspaceRoot = FileResourceUtils.getWorkspaceRoot();
    status = resolveWSDL(workspaceRoot, def, clientWSDLPath, env, monitor);
    return status;
  }

	private IStatus resolveWSDL(
		IWorkspaceRoot workspace,
		Definition wsdlDef,
		IPath wsdlPath,
		Environment env, 
		IProgressMonitor monitor) {
		try {
			writeWSDLFile(workspace, wsdlDef, wsdlPath, env, monitor);
			Map importDefs = wsdlDef.getImports();
			Set keysSet = importDefs.keySet();
			for (Iterator e = keysSet.iterator(); e.hasNext();) {
				Object keyName = e.next();
				Vector vector = (Vector) importDefs.get(keyName);
				for (int i = 0; i < vector.size(); i++) {
					Import importDef = (Import) vector.get(i);
					Definition def = importDef.getDefinition();
					String newPathString =
						wsdlPath.toString().substring(
							0,
							wsdlPath.toString().lastIndexOf("/") + 1);	 //$NON-NLS-1$
					if (isInvalidImportWSDL(importDef.getLocationURI())) {
					    return StatusUtils.errorStatus( msgUtils_.getMessage("MSG_ERROR_IMPORT_WSDL",new String[]{importDef.getLocationURI()}));
					}
					IPath newPath =
						new Path(newPathString + importDef.getLocationURI());
					IStatus status = resolveWSDL(workspace, def, newPath, env, monitor);
					if (status != null
						&& status.getSeverity() == Status.ERROR) {
						return status;
					}
				}
			}
		} catch (Exception e) {
		    return StatusUtils.errorStatus( msgUtils_.getMessage("MSG_ERROR_WRITE_WSDL",new String[] { wsdlPath.toString() }), e);
		}
		return Status.OK_STATUS;
	}

	private boolean isInvalidImportWSDL(String wsdlPath) {

		// relative url
		if (!wsdlPath.toLowerCase().startsWith("http://")) { //$NON-NLS-1$

			if (wsdlPath.indexOf("/") != -1) { //$NON-NLS-1$
				if (!wsdlPath.startsWith("./")) { //$NON-NLS-1$
					return true;
				}
			}

		}
		return false;
	}

	private void writeWSDLFile(
		IWorkspaceRoot workspace,
		Definition wsdlDef,
		IPath wsdlPath,
		Environment env, 
		IProgressMonitor monitor)
		throws Exception {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		//WSDLFactory wsdlFactory = WSDLFactory.newInstance();
		//WSDLWriter wsdlWriter = wsdlFactory.newWSDLWriter();
		WSDLWriter wsdlWriter = (new org.eclipse.wst.wsdl.internal.impl.wsdl4j.WSDLFactoryImpl()).newWSDLWriter();
		wsdlWriter.writeWSDL(wsdlDef, baos);
		byte[] b = baos.toByteArray();
		ByteArrayInputStream bais = new ByteArrayInputStream(b);

		ResourceContext context =
			WebServicePlugin.getInstance().getResourceContext();
		FileResourceUtils.createFile(
			context,
			wsdlPath.makeAbsolute(),
			bais,
			monitor,
			env.getStatusHandler());

		baos.close();
		bais.close();

	}  
  
  /**
   * @param clientWSDLPathName_ The clientWSDLPathName_ to set.
   */
  public void setClientWSDLPathName(String clientWSDLPathName)
  {
    this.clientWSDLPathName_ = clientWSDLPathName;
  }
  /**
   * @param wsdlURL_ The wsdlURL_ to set.
   */
  public void setWsdlURL(String wsdlURL)
  {
    this.wsdlURL_ = wsdlURL;
  }
  /**
   * @param wsParser_ The wsParser_ to set.
   */
  public void setWsParser(WebServicesParser wsParser)
  {
    this.wsParser_ = wsParser;
  }
}
