/*******************************************************************************
 * Copyright (c) 2001, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20080506   202945 pmoogk@ca.ibm.com - Peter Moogk, Allow WSE to be launched from a WSIL file.
 *******************************************************************************/

package org.eclipse.wst.ws.internal.explorer.platform.wsil.perspective;

import java.util.Hashtable;

import javax.servlet.ServletContext;
import org.eclipse.wst.ws.internal.datamodel.BasicModel;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.TreeElement;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.NodeManager;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Perspective;
import org.eclipse.wst.ws.internal.explorer.platform.util.DirUtils;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.actions.OpenWSILAction;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.actions.SwitchPerspectiveFromWSILAction;
import org.eclipse.wst.ws.internal.explorer.platform.wsil.constants.WsilActionInputs;

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

    public final void preloadWSIL( String[] wsilURL )
    {
      if( wsilURL != null )
      {
        for( int index = 0; index < wsilURL.length; index++ )
        {
          OpenWSILAction action = new OpenWSILAction(controller_);
          Hashtable table = action.getPropertyTable();
          table.put(WsilActionInputs.WSIL_URL, wsilURL[index] );
          table.put(WsilActionInputs.WSIL_INSPECTION_TYPE, String.valueOf(WsilActionInputs.WSIL_DETAILS));
          action.run();
        }
        
        if( wsilURL.length >0 )
        {
          controller_.setCurrentPerspective(ActionInputs.PERSPECTIVE_WSIL);          
        }
      }
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
