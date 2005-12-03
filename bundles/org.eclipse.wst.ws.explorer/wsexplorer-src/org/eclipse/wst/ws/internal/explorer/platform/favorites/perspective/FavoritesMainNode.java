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

import org.eclipse.wst.ws.internal.explorer.platform.constants.ModelConstants;
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.TreeElement;
import org.eclipse.wst.ws.internal.explorer.platform.favorites.constants.FavoritesModelConstants;
import org.eclipse.wst.ws.internal.explorer.platform.favorites.datamodel.FavoritesMainElement;
import org.eclipse.wst.ws.internal.explorer.platform.favorites.datamodel.FavoritesUDDIBusinessFolderElement;
import org.eclipse.wst.ws.internal.explorer.platform.favorites.datamodel.FavoritesUDDIRegistryFolderElement;
import org.eclipse.wst.ws.internal.explorer.platform.favorites.datamodel.FavoritesUDDIServiceFolderElement;
import org.eclipse.wst.ws.internal.explorer.platform.favorites.datamodel.FavoritesUDDIServiceInterfaceFolderElement;
import org.eclipse.wst.ws.internal.explorer.platform.favorites.datamodel.FavoritesUserDefUDDIRegistryFolderElement;
import org.eclipse.wst.ws.internal.explorer.platform.favorites.datamodel.FavoritesWSDLServiceFolderElement;
import org.eclipse.wst.ws.internal.explorer.platform.favorites.datamodel.FavoritesWSILFolderElement;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.NodeManager;

public class FavoritesMainNode extends FavoritesNavigatorNode {
    public FavoritesMainNode(TreeElement treeElement, NodeManager nodeManager) {
        super(treeElement, nodeManager, 1, "images/root_main.gif");

        // Create UDDI Registry Folder Node
        FavoritesUDDIRegistryFolderElement favUDDIRegistryFolderElement = new FavoritesUDDIRegistryFolderElement(getMessage("FAVORITES_UDDI_REGISTRIES_FOLDER_NODE"), treeElement.getModel(), nodeManager);
        treeElement.connect(favUDDIRegistryFolderElement, FavoritesModelConstants.REL_UDDI_REGISTRY_FOLDER_NODE, ModelConstants.REL_OWNER);
        FavoritesUDDIRegistryFolderNode favUDDIRegistryFolderNode = new FavoritesUDDIRegistryFolderNode(favUDDIRegistryFolderElement, nodeManager, nodeDepth_ + 1);
        addChild(favUDDIRegistryFolderNode);
        favUDDIRegistryFolderNode.setVisibilityOfChildren(false);
        favUDDIRegistryFolderElement.init((FavoritesMainElement)treeElement);

        // Create User defined UDDI Registry Folder Node
        FavoritesUserDefUDDIRegistryFolderElement favUserDefUDDIFolderElement = new FavoritesUserDefUDDIRegistryFolderElement(getMessage("FAVORITES_USER_DEF_UDDI_REGISTRIES_FOLDER_NODE"), treeElement.getModel(), nodeManager);
        treeElement.connect(favUserDefUDDIFolderElement, FavoritesModelConstants.REL_USER_DEF_UDDI_REGISTRY_FOLDER_NODE, ModelConstants.REL_OWNER);
        FavoritesUserDefUDDIRegistryFolderNode favUserDefUDDIFolderNode = new FavoritesUserDefUDDIRegistryFolderNode(favUserDefUDDIFolderElement, nodeManager, nodeDepth_ + 1);
        addChild(favUserDefUDDIFolderNode);
        favUserDefUDDIFolderNode.setVisibilityOfChildren(false);
        favUserDefUDDIFolderElement.init((FavoritesMainElement)treeElement);

        // Create UDDI Business Folder Node
        FavoritesUDDIBusinessFolderElement favUDDIBusinessFolderElement = new FavoritesUDDIBusinessFolderElement(getMessage("FAVORITES_UDDI_BUSINESSES_FOLDER_NODE"), treeElement.getModel(), nodeManager);
        treeElement.connect(favUDDIBusinessFolderElement, FavoritesModelConstants.REL_UDDI_BUSINESS_FOLDER_NODE, ModelConstants.REL_OWNER);
        FavoritesUDDIBusinessFolderNode favUDDIBusinessFolderNode = new FavoritesUDDIBusinessFolderNode(favUDDIBusinessFolderElement, nodeManager, nodeDepth_ + 1);
        addChild(favUDDIBusinessFolderNode);
        favUDDIBusinessFolderNode.setVisibilityOfChildren(false);
        favUDDIBusinessFolderElement.init((FavoritesMainElement)treeElement);

        // Create UDDI Service Folder Node
        FavoritesUDDIServiceFolderElement favUDDIServiceFolderElement = new FavoritesUDDIServiceFolderElement(getMessage("FAVORITES_UDDI_SERVICE_FOLDER_NODE"), treeElement.getModel(), nodeManager);
        treeElement.connect(favUDDIServiceFolderElement, FavoritesModelConstants.REL_UDDI_SERVICE_FOLDER_NODE, ModelConstants.REL_OWNER);
        FavoritesUDDIServiceFolderNode favUDDIServiceFolderNode = new FavoritesUDDIServiceFolderNode(favUDDIServiceFolderElement, nodeManager, nodeDepth_ + 1);
        addChild(favUDDIServiceFolderNode);
        favUDDIServiceFolderNode.setVisibilityOfChildren(false);
        favUDDIServiceFolderElement.init((FavoritesMainElement)treeElement);

        // Create UDDI Service Interface Folder Node
        FavoritesUDDIServiceInterfaceFolderElement favUDDIServiceInterfaceFolderElement = new FavoritesUDDIServiceInterfaceFolderElement(getMessage("FAVORITES_UDDI_SERVICE_INTERFACES_FOLDER_NODE"), treeElement.getModel(), nodeManager);
        treeElement.connect(favUDDIServiceInterfaceFolderElement, FavoritesModelConstants.REL_UDDI_SERVICE_INTERFACE_FOLDER_NODE, ModelConstants.REL_OWNER);
        FavoritesUDDIServiceInterfaceFolderNode favUDDIServiceInterfaceFolderNode = new FavoritesUDDIServiceInterfaceFolderNode(favUDDIServiceInterfaceFolderElement, nodeManager, nodeDepth_ + 1);
        addChild(favUDDIServiceInterfaceFolderNode);
        favUDDIServiceInterfaceFolderNode.setVisibilityOfChildren(false);
        favUDDIServiceInterfaceFolderElement.init((FavoritesMainElement)treeElement);

        // Create WSIL Folder Node
        FavoritesWSILFolderElement favWSILFolderElement = new FavoritesWSILFolderElement(getMessage("FAVORITES_WSIL_FOLDER_NODE"), treeElement.getModel(), nodeManager);
        treeElement.connect(favWSILFolderElement, FavoritesModelConstants.REL_WSIL_FOLDER_NODE, ModelConstants.REL_OWNER);
        FavoritesWSILFolderNode favWSILFolderNode = new FavoritesWSILFolderNode(favWSILFolderElement, nodeManager, nodeDepth_ + 1);
        addChild(favWSILFolderNode);
        favWSILFolderNode.setVisibilityOfChildren(false);
        favWSILFolderElement.init((FavoritesMainElement)treeElement);

        // Create WSDL Service Folder Node
        FavoritesWSDLServiceFolderElement favWSDLServiceFolderElement = new FavoritesWSDLServiceFolderElement(getMessage("FAVORITES_WSDL_SERVICE_FOLDER_NODE"), treeElement.getModel(), nodeManager);
        treeElement.connect(favWSDLServiceFolderElement, FavoritesModelConstants.REL_WSDL_SERVICE_FOLDER_NODE, ModelConstants.REL_OWNER);
        FavoritesWSDLServiceFolderNode favWSDLServiceFolderNode = new FavoritesWSDLServiceFolderNode(favWSDLServiceFolderElement, nodeManager, nodeDepth_ + 1);
        addChild(favWSDLServiceFolderNode);
        favWSDLServiceFolderNode.setVisibilityOfChildren(false);
        favWSDLServiceFolderElement.init((FavoritesMainElement)treeElement);

    }

    protected void initTools() {
    }

    private String getMessage(String msg) {
        Controller controller = nodeManager_.getController();
        FavoritesPerspective favPerspective = controller.getFavoritesPerspective();
        return favPerspective.getMessage(msg);
    }

}
