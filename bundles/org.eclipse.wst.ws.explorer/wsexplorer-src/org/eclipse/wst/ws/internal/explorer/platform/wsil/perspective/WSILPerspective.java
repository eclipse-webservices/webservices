/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.ws.internal.explorer.platform.wsil.perspective;

import javax.servlet.ServletContext;
import org.eclipse.wst.ws.internal.datamodel.BasicModel;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.TreeElement;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.NodeManager;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Perspective;
import org.eclipse.wst.ws.internal.explorer.platform.util.DirUtils;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.actions.SwitchPerspectiveFromWSILAction;

public class WSILPerspective extends Perspective
{
    private BasicModel model_;
    private NodeManager nodeManager_;
    private String perspectiveContentFramesetCols_;
    private String savedPerspectiveContentFramesetCols_;
    private String actionsContainerFramesetRows_;
    private String savedActionsContainerFramesetRows_;

    public WSILPerspective(Controller controller)
    {
        super("wsil",controller);
    }

    public final void initPerspective(ServletContext application)
    {
        model_ = new BasicModel("WsilModel");
        TreeElement treeElement = new TreeElement(getMessage("WSIL_MAIN_NODE"), model_);
        model_.setRootElement(treeElement);
        nodeManager_ = new NodeManager(controller_);
        WsilMainNode wsilMainNode = new WsilMainNode(treeElement, nodeManager_);
        nodeManager_.setRootNode(wsilMainNode);

        // Starting frameset sizes.
        if (!DirUtils.isRTL())
          perspectiveContentFramesetCols_ = "30%,*";
        else
          perspectiveContentFramesetCols_ = "*,30%";
        savedPerspectiveContentFramesetCols_ = perspectiveContentFramesetCols_;
        actionsContainerFramesetRows_ = "75%,*";
        savedActionsContainerFramesetRows_ = actionsContainerFramesetRows_;
    }

    public NodeManager getNodeManager() {
        return nodeManager_;
    }

    public String getPerspectiveContentPage()
    {
        return "wsil/wsil_perspective_content.jsp";
    }

    public int getPerspectiveId()
    {
        return ActionInputs.PERSPECTIVE_WSIL;
    }

    public String getPanesFile() {
        return "wsil/scripts/wsilPanes.jsp";
    }
    
    public String getFramesetsFile()
    {
      return "wsil/scripts/wsilframesets.jsp";
    }
    
    public String getProcessFramesetsForm()
    {
      return "wsil/forms/ProcessWSILFramesetsForm.jsp";
    }

    public String getTreeContentVar() {
        return "wsilNavigatorContent";
    }

    public String getTreeContentPage() {
        return "wsil/wsil_navigator_content.jsp";
    }

    public String getPropertiesContainerVar() {
        return "wsilPropertiesContainer";
    }

    public String getPropertiesContainerPage() {
        return "wsil/wsil_properties_container.jsp";
    }

    public String getStatusContentVar() {
        return "wsilStatusContent";
    }

    public String getStatusContentPage() {
        return "wsil/wsil_status_content.jsp";
    }
    
    public final String getPerspectiveContentFramesetCols()
    {
      return perspectiveContentFramesetCols_;
    }
    
    public final void setPerspectiveContentFramesetCols(String cols)
    {
      perspectiveContentFramesetCols_ = cols;
    }
    
    public final String getSavedPerspectiveContentFramesetCols()
    {
      return savedPerspectiveContentFramesetCols_;
    }
    
    public final void setSavedPerspectiveContentFramesetCols(String cols)
    {
      savedPerspectiveContentFramesetCols_ = cols;
    }
    
    public final String getActionsContainerFramesetRows()
    {
      return actionsContainerFramesetRows_;
    }
    
    public final void setActionsContainerFramesetRows(String rows)
    {
      actionsContainerFramesetRows_ = rows;
    }
    
    public final String getSavedActionsContainerFramesetRows()
    {
      return savedActionsContainerFramesetRows_;
    }
    
    public final void setSavedActionsContainerFramesetRows(String rows)
    {
      savedActionsContainerFramesetRows_ = rows;
    }
    
    public final String getSwitchPerspectiveFormActionLink(int targetPerspectiveId,boolean forHistory)
    {
      return SwitchPerspectiveFromWSILAction.getFormActionLink(targetPerspectiveId,forHistory);
    }
}
