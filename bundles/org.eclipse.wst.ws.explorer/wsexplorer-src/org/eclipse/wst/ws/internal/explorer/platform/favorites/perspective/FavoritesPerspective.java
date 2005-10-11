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

package org.eclipse.wst.ws.internal.explorer.platform.favorites.perspective;

import javax.servlet.ServletContext;
import org.eclipse.wst.ws.internal.datamodel.BasicModel;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.favorites.actions.SwitchPerspectiveFromFavoritesAction;
import org.eclipse.wst.ws.internal.explorer.platform.favorites.datamodel.FavoritesMainElement;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.NodeManager;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Perspective;
import org.eclipse.wst.ws.internal.explorer.platform.util.DirUtils;

public class FavoritesPerspective extends Perspective
{
    private BasicModel model_;
    private NodeManager nodeManager_;
    private String perspectiveContentFramesetCols_;
    private String savedPerspectiveContentFramesetCols_;
    private String actionsContainerFramesetRows_;
    private String savedActionsContainerFramesetRows_;

    public FavoritesPerspective(Controller controller)
    {
        super("favorites",controller);
    }

    public final void initPerspective(ServletContext application)
    {
        model_ = new BasicModel("FavoritesModel");
        FavoritesMainElement favoritesMainElement = new FavoritesMainElement(getMessage("FAVORITES_MAIN_NODE"), model_, controller_);
        model_.setRootElement(favoritesMainElement);
        nodeManager_ = new NodeManager(controller_);
        FavoritesMainNode favMainNode = new FavoritesMainNode(favoritesMainElement, nodeManager_);
        nodeManager_.setRootNode(favMainNode);
        
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
        return "favorites/fav_perspective_content.jsp";
    }

    public int getPerspectiveId()
    {
        return ActionInputs.PERSPECTIVE_FAVORITES;
    }

    public String getPanesFile() {
        return "favorites/scripts/favoritesPanes.jsp";
    }
    
    public String getProcessFramesetsForm()
    {
      return "favorites/forms/ProcessFavoritesFramesetsForm.jsp";
    }
    
    public String getFramesetsFile()
    {
      return "favorites/scripts/favoritesframesets.jsp";
    }

    public String getTreeContentVar() {
        return "favNavigatorContent";
    }

    public String getTreeContentPage() {
        return "favorites/fav_navigator_content.jsp";
    }

    public String getPropertiesContainerVar() {
        return "favPropertiesContainer";
    }

    public String getPropertiesContainerPage() {
        return "favorites/fav_properties_container.jsp";
    }

    public String getStatusContentVar() {
        return "favStatusContent";
    }

    public String getStatusContentPage() {
        return "favorites/fav_status_content.jsp";
    }

    public String getPropertiesContentVar() {
        return "favPropertiesContent";
    }

    public String getPropertiesContentPage() {
        return "favorites/fav_properties_content.jsp";
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
      return SwitchPerspectiveFromFavoritesAction.getFormActionLink(targetPerspectiveId,forHistory);
    }
}
