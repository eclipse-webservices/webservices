/*******************************************************************************
 * Copyright (c) 2001, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060427   136449 brunssen@us.ibm.com - Vince Brunssen  
 *******************************************************************************/

package org.eclipse.wst.ws.internal.explorer.platform.perspective;

import java.util.Hashtable;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.wst.ws.internal.explorer.platform.constants.FrameNames;
import org.eclipse.wst.ws.internal.explorer.platform.util.HTMLUtils;

// A class for representing toolbar tools.
public abstract class Tool
{
  protected ToolManager toolManager_;
  private String enabledImageLink_;
  private String highlightedImageLink_;
  private String alt_;
  protected int toolId_;
  protected byte toolType_;

  public Tool(ToolManager toolManager,String enabledImageLink,String highlightedImageLink,String alt,byte toolType)
  {
    toolManager_ = toolManager;
    enabledImageLink_ = enabledImageLink;
    highlightedImageLink_ = highlightedImageLink;
    alt_ = alt;
    toolId_ = toolManager_.addTool(this);
    toolType_ = toolType;
  }

  public abstract String getSelectToolActionHref(boolean forHistory);

  public String getSelectToolActionTarget()
  {
    return FrameNames.PERSPECTIVE_WORKAREA;
  }

  private final void generateOnMouseValue(StringBuffer value,HttpServletResponse response,String fullImageLink)
  {
    value.append("src='").append(response.encodeURL(fullImageLink)).append('\'');
  }

  public String renderTool(HttpServletResponse response,Controller controller)
  {
    Hashtable jsImageProperties = new Hashtable();
    StringBuffer propertyValue = new StringBuffer();
    String fullEnabledImageLink = controller.getPathWithContext(enabledImageLink_);
    generateOnMouseValue(propertyValue,response,fullEnabledImageLink);
    int propertyValueLength = propertyValue.length();
    jsImageProperties.put("class","normal");
    jsImageProperties.put("onMouseOut",propertyValue.append(";mouseout(this)").toString());
    propertyValue.delete(propertyValueLength,propertyValue.length());
    jsImageProperties.put("onMouseUp",propertyValue.append(";mouseup(this)").toString());
    propertyValue.setLength(0);
    generateOnMouseValue(propertyValue,response,controller.getPathWithContext(highlightedImageLink_));
    propertyValueLength = propertyValue.length();
    jsImageProperties.put("onMouseOver",propertyValue.append(";mouseover(this)").toString());
    propertyValue.delete(propertyValueLength,propertyValue.length());
    jsImageProperties.put("onMouseDOwn",propertyValue.append(";mousedown(this)").toString());

    String imageTag = HTMLUtils.getHTMLImageTag(response,fullEnabledImageLink,alt_,"16","16",jsImageProperties);
    return HTMLUtils.getHTMLLinkTag(response,controller.getPathWithContext(getSelectToolActionHref(false)),getSelectToolActionTarget(),null,imageTag,null);
  }

  public final ToolManager getToolManager()
  {
    return toolManager_;
  }

  public final int getToolId()
  {
    return toolId_;
  }

  public final byte getToolType()
  {
    return toolType_;
  }
  
  public final String getEnabledImageLink()
  {
    return enabledImageLink_;
  }
  
  public final String getHighlightedImageLink()
  {
    return highlightedImageLink_;
  }
  
  public final String getAltText()
  {
    return alt_;
  }
  
  public final void setAltText(String alt)
  {
  	alt_ = alt;
  }  

  public abstract String getFormLink();
  public abstract String getActionLink();
}
