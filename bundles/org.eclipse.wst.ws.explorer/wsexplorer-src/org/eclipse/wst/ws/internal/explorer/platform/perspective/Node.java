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

package org.eclipse.wst.ws.internal.explorer.platform.perspective;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.constants.FrameNames;
import org.eclipse.wst.ws.internal.explorer.platform.datamodel.TreeElement;
import org.eclipse.wst.ws.internal.explorer.platform.util.HTMLUtils;

public abstract class Node
{
  protected TreeElement element_;
  protected NodeManager nodeManager_;
  protected int nodeId_;
  protected int nodeDepth_;
  protected String imagePath_;
  protected boolean isOpen_;
  protected Vector childNodes_;
  protected ToolManager toolManager_;
  protected Node parent_;
  private String anchorName_;
  private int viewId_;

  public Node(TreeElement element,NodeManager nodeManager,int nodeDepth,String imagePath)
  {
    element_ = element;
    nodeManager_ = nodeManager;
    nodeId_ = nodeManager.addToNodeTable(this);
    nodeDepth_ = nodeDepth;
    Controller controller = nodeManager_.getController();
    if (imagePath == null)
      imagePath_ = controller.getPathWithContext(NodeManager.PATH_SPACE);
    else
      imagePath_ = imagePath;
    isOpen_ = true;
    childNodes_ = new Vector();
    toolManager_ = new ToolManager(this);
    initTools();
    anchorName_ = (new StringBuffer("action")).append(nodeId_).toString();
    parent_ = null;
    viewId_ = ActionInputs.VIEWID_DEFAULT;
  }

  /**
  * Returns the TreeElement associated with this node.
  * @return TreeElement The element associated with this node.
  */
  public TreeElement getTreeElement()
  {
    return element_;
  }

  /**
  * Returns the name of the node.
  * @return String
  */
  public String getNodeName()
  {
    return element_.getName();
  }

  /**
  * Returns the NodeManager managing this node.
  * @return NodeManager
  */
  public NodeManager getNodeManager()
  {
    return nodeManager_;
  }

  /**
  * Returns the id of this node.
  * @return int
  */
  public int getNodeId()
  {
    return nodeId_;
  }

  /**
  * Returns the depth in the tree of this node relative to the root node
  * @return int
  */
  public int getNodeDepth()
  {
    return nodeDepth_;
  }

  /**
  * Set the path of the image representing this class of Node.
  * @param String The path.
  */
  public void setImagePath(String path)
  {
    imagePath_ = path;
  }

  /**
  * Returns true if the children of this node are visible. Returns false
  * if they are not.
  * @return boolean
  */
  public boolean isOpen()
  {
    return isOpen_;
  }

  public boolean isVisible()
  {
    Node parentNode = parent_;
    while (parentNode != null)
    {
      if (!parentNode.isOpen())
        return false;
      parentNode = parentNode.getParent();
    }
    return true;
  }

  /**
  * Sets the visibility of this node's children to the specified value.
  * Updates the NodeManager's record of the maximum depth visible in the tree.
  * @param boolean
  */
  public final void setVisibilityOfChildren(boolean open)
  {
    isOpen_ = open;
    nodeManager_.updateMaxDepthVisible();
  }

  /**
  * Gets the maximum depth displayed in this node's branch of the tree
  * Used by NodeManager for html display purposes.
  * @return int
  */
  public int getMaxDepthVisible()
  {
    if (!isOpen_)
      return nodeDepth_;
    else
    {
      int currentDepth = nodeDepth_;
      Enumeration e = childNodes_.elements();
      while (e.hasMoreElements())
      {
        Node thisNode = (Node)e.nextElement();
        int depth = thisNode.getMaxDepthVisible();
        if (depth>currentDepth)
          currentDepth=depth;
      }
      return currentDepth;
    }
  }

  public final void setParent(Node node)
  {
    parent_ = node;
  }

  public final Node getParent()
  {
    return parent_;
  }

  public final void setViewId(int viewId)
  {
    viewId_ = viewId;
  }

  public final int getViewId()
  {
    return viewId_;
  }

  public final int getViewToolId()
  {
    ToolManager viewToolManager = getViewToolManager();
    if (viewToolManager != null)
      return viewToolManager.getSelectedToolId();
    return ActionInputs.VIEWTOOLID_DEFAULT;
  }

  /**
  * Adds the specified node as a child of this node.
  * @param Node The child being added.
  * @return boolean Always true.
  */
  public final boolean addChild(Node childNode)
  {
    childNode.setParent(this);
    childNodes_.addElement(childNode);
    nodeManager_.updateMaxDepthVisible();
    return true;
  }

  /**
  * Returns the child nodes of this node. If this node has no children, an
  * empty vector is returned.
  * @return Vector
  */
  public Vector getChildNodes()
  {
    return childNodes_;
  }

  public final Node getChildNode(TreeElement element)
  {
    Enumeration e = childNodes_.elements();
    while (e.hasMoreElements())
    {
      Node presentNode = (Node)e.nextElement();
      if (presentNode.getTreeElement() == element)
        return presentNode;
    }
    return null;
  }

  public final void removeChildNodes(boolean isEntry)
  {
    int numberOfChildNodes = childNodes_.size();
    for (int i=numberOfChildNodes-1;i>=0;i--)
    {
      Node childNode = (Node)childNodes_.elementAt(i);
      childNode.removeChildNodes(false);
      nodeManager_.removeFromNodeTable(childNode.getNodeId());
    }
    if (numberOfChildNodes > 0)
    {
      childNodes_.removeAllElements();
      if (isEntry)
        nodeManager_.updateMaxDepthVisible();
    }
  }

  public final boolean removeChildNode(TreeElement element)
  {
    Node childNode = getChildNode(element);
    if (childNode != null)
    {
      int childNodeId = childNode.getNodeId();
      if (nodeManager_.getSelectedNodeId() == childNodeId)
        nodeManager_.setSelectedNodeId(nodeId_);
      childNodes_.removeElement(childNode);
      nodeManager_.removeFromNodeTable(childNodeId);
      nodeManager_.updateMaxDepthVisible();
      return true;
    }
    return false;
  }

  /**
  * Get the name of the HTML anchor associated with this node.
  * @return String The name of the anchor.
  */
  public String getAnchorName()
  {
    return anchorName_;
  }

  public ToolManager getToolManager()
  {
    return toolManager_;
  }

  public ToolManager getViewToolManager()
  {
    if (viewId_ != ActionInputs.VIEWID_DEFAULT)
    {
      ViewTool viewTool = (ViewTool)toolManager_.getSelectedTool();
      return viewTool.getToolManager(viewId_);
    }
    return null;
  }
  
  public ToolManager getCurrentToolManager()
  {
    ToolManager toolManager = getViewToolManager();
    if (toolManager == null)
      toolManager = toolManager_;
    return toolManager;
  }

  protected abstract void initTools();

  protected abstract String getToggleNodeActionHref();

  protected String getToggleNodeActionTarget()
  {
    return FrameNames.PERSPECTIVE_WORKAREA;
  }

  protected void addToggle(HttpServletResponse response,StringBuffer treeView,boolean isLastChild)
  {
    if (nodeDepth_ != 1)
    {
      treeView.append("    <td align=\"left\" width=16 height=16 nowrap>");
      String toggleImageTag;
      if (childNodes_.size() > 0)
      {
        // <a href="..." target="...">toggleImageTag</a>
        String href = nodeManager_.getController().getPathWithContext(getToggleNodeActionHref());
        if (isOpen_)
        {
          String toggleAlt = nodeManager_.getController().getMessage("ALT_CLOSE");
          if (isLastChild)
            toggleImageTag = HTMLUtils.getHTMLImageTag(response,nodeManager_.getController().getPathWithContext(NodeManager.PATH_MINUS_LAST),toggleAlt,"16","16",null);
          else
            toggleImageTag = HTMLUtils.getHTMLImageTag(response,nodeManager_.getController().getPathWithContext(NodeManager.PATH_MINUS_NOTLAST),toggleAlt,"16","16",null);
        }
        else
        {
          String toggleAlt = nodeManager_.getController().getMessage("ALT_OPEN");
          if (isLastChild)
            toggleImageTag = HTMLUtils.getHTMLImageTag(response,nodeManager_.getController().getPathWithContext(NodeManager.PATH_PLUS_LAST),toggleAlt,"16","16",null);
          else
            toggleImageTag = HTMLUtils.getHTMLImageTag(response,nodeManager_.getController().getPathWithContext(NodeManager.PATH_PLUS_NOTLAST),toggleAlt,"16","16",null);
        }
        String linkTag = HTMLUtils.getHTMLLinkTag(response,href,getToggleNodeActionTarget(),String.valueOf(nodeId_),toggleImageTag,null);
        treeView.append(linkTag);
      }
      else
      {
        // <img...>
        if (isLastChild)
          toggleImageTag = HTMLUtils.getHTMLImageTag(response,nodeManager_.getController().getPathWithContext(NodeManager.PATH_LINE_LAST),"","16","16",null);
        else
          toggleImageTag = HTMLUtils.getHTMLImageTag(response,nodeManager_.getController().getPathWithContext(NodeManager.PATH_LINE_NOTLAST),"","16","16",null);
        treeView.append(toggleImageTag);
      }
      treeView.append("</td>").append(HTMLUtils.LINE_SEPARATOR);
    }
  }

  protected abstract String getLinkActionHref();

  protected String getLinkActionTarget()
  {
    return FrameNames.PERSPECTIVE_WORKAREA;
  }

  public String getOpenImagePath()
  {
    return imagePath_;
  }

  public String getClosedImagePath()
  {
    return imagePath_;
  }

  protected void addHTMLLabel(HttpServletResponse response,StringBuffer treeView,boolean isLastChild)
  {
    // <a href="..." target="..." onClick="selectNode('...')"><img...></a>
    Hashtable additionalAttributes = new Hashtable();
    additionalAttributes.put("name",anchorName_);
    String imagePath = (nodeManager_.getSelectedNodeId() == nodeId_)?getOpenImagePath():getClosedImagePath();

    String imageTag = HTMLUtils.getHTMLImageTag(response,nodeManager_.getController().getPathWithContext(imagePath),"","16","16",additionalAttributes);
    String baseHref = getLinkActionHref();
    String href = ((baseHref == null)?baseHref:nodeManager_.getController().getPathWithContext(baseHref));
    String target = getLinkActionTarget();
    additionalAttributes.clear();

    treeView.append("    <td align=\"left\" width=16 height=16 nowrap>");
    if (href != null)
      treeView.append(HTMLUtils.getHTMLLinkTag(response,href,target,null,imageTag,additionalAttributes));
    else
      treeView.append(imageTag);
    treeView.append("</td>").append(HTMLUtils.LINE_SEPARATOR);
    treeView.append("    <td align=\"left\" width=3 height=10 nowrap>").append(HTMLUtils.getHTMLImageTag(response,nodeManager_.getController().getPathWithContext(NodeManager.PATH_SPACE),"","3","10",null)).append("</td>").append(HTMLUtils.LINE_SEPARATOR);

    // <a name="..." href="..." target="...">label</a>
    treeView.append("    <td align=\"left\" nowrap>").append(HTMLUtils.LINE_SEPARATOR);
    treeView.append("      ");
    if (href != null)
    {
      String textAnchorClass;
      if (nodeManager_.getSelectedNodeId() == nodeId_)
        textAnchorClass="selectedTextAnchor";
      else
        textAnchorClass="unselectedTextAnchor";
      additionalAttributes.put("class",textAnchorClass);
      treeView.append(HTMLUtils.getHTMLLinkTag(response,href,target,anchorName_,getNodeName(),additionalAttributes));
    }
    else
      treeView.append("<strong>").append(getNodeName()).append("</strong>");
    treeView.append(HTMLUtils.LINE_SEPARATOR).append("    </td>").append(HTMLUtils.LINE_SEPARATOR);
  }

  public void renderNode(HttpServletResponse response,StringBuffer treeView,String prefixColumns,boolean isLastChild)
  {
    String space_16x16 = HTMLUtils.getHTMLImageTag(response,nodeManager_.getController().getPathWithContext(NodeManager.PATH_SPACE),"","16","16",null);
    String line_16x16 = HTMLUtils.getHTMLImageTag(response,nodeManager_.getController().getPathWithContext(NodeManager.PATH_LINE),"","16","16",null);

    // Use border=1 to help with debugging.
    treeView.append("<table cellspacing=0 cellpadding=0 border=0>").append(HTMLUtils.LINE_SEPARATOR);
    treeView.append("  <tr>").append(HTMLUtils.LINE_SEPARATOR);
    treeView.append("    <td align=\"left\" width=16 height=16 nowrap>").append(space_16x16).append("</td>").append(HTMLUtils.LINE_SEPARATOR);
    StringBuffer newPrefixColumns = new StringBuffer(prefixColumns);
    if (nodeDepth_ >= 2)
    {
      if (prefixColumns.length() > 0)
        treeView.append(prefixColumns).append(HTMLUtils.LINE_SEPARATOR);
      // generate a new prefix columns.
      newPrefixColumns.append("    <td align=\"left\" width=16 height=16 nowrap>");
      if (isLastChild)
        newPrefixColumns.append(space_16x16);
      else
        newPrefixColumns.append(line_16x16);
      newPrefixColumns.append("</td>").append(HTMLUtils.LINE_SEPARATOR);
    }
    addToggle(response,treeView,isLastChild);
    addHTMLLabel(response,treeView,isLastChild);
    treeView.append("  </tr>").append(HTMLUtils.LINE_SEPARATOR);
    treeView.append("</table>").append(HTMLUtils.LINE_SEPARATOR);
    int numberOfChildNodes = childNodes_.size();
    if (numberOfChildNodes > 0)
    {
      if (isOpen_)
      {
        for (int i=0;i<numberOfChildNodes-1;i++)
        {
          Node childNode = (Node)childNodes_.elementAt(i);
          childNode.renderNode(response,treeView,newPrefixColumns.toString(),false);
        }
        Node lastChildNode = (Node)childNodes_.elementAt(numberOfChildNodes-1);
        lastChildNode.renderNode(response,treeView,newPrefixColumns.toString(),true);
      }
    }
  }

  // For sorting purposes.  
  public final String toString()
  {
    return getNodeName();
  }
}
