/*******************************************************************************
 * Copyright (c) 2009 by SAP AG, Walldorf. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     SAP AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.jaxws.dom.ui;

import java.net.URL;
import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jst.ws.jaxws.dom.ui.internal.plugin.DomUi;
import org.eclipse.jst.ws.jaxws.utils.logging.Logger;
import org.eclipse.swt.graphics.Image;

/**
 * Images bundle. Use this class when you need to load some image. 
 * 
 * @author Georgi Vachkov
 */
public class Images
{
	public static final Images INSTANCE = new Images();
	
    public static final String IMG_WEB_METHOD		= "WebMethod"; //$NON-NLS-1$
    public static final String IMG_WEB_PARAM_IN		= "WebParamIn"; //$NON-NLS-1$
    public static final String IMG_WEB_PARAM_OUT	= "WebParamOut"; //$NON-NLS-1$
    public static final String IMG_WEB_SERVICE		= "WebService"; //$NON-NLS-1$
    public static final String IMG_WEB_SERVICE_GROUP = "WebServiceGroup"; //$NON-NLS-1$
    public static final String IMG_SEI				= "SEI"; //$NON-NLS-1$
    public static final String IMG_SEI_GROUP		= "SEIGroup"; //$NON-NLS-1$
    public static final String IMG_DOM_WS_PROVIDER	= "DOMWSProvider"; //$NON-NLS-1$
    public static final String IMG_ERROR_MARKER		= "error_obj"; //$NON-NLS-1$
    public static final String IMG_WARNING_MARKER	= "warning_obj"; //$NON-NLS-1$

    private static ResourceBundle iconsBundle;
    private static ImageRegistry mImageRegistry;

    /**
     * Constructor
     */
    public Images() 
    {
		try {
			iconsBundle = ResourceBundle.getBundle("org.eclipse.jst.ws.jaxws.dom.ui.internal.plugin.WSImageBundle"); //$NON-NLS-1$
		} 
		catch (MissingResourceException x) {
			(new Logger()).logError("Unable to load image bundle for this plugin!", x); //$NON-NLS-1$
			iconsBundle = null;
		}
	}
    
    
    protected void initializeImageRegistry() 
    {
        try {
            mImageRegistry = new ImageRegistry();
            
            mImageRegistry.put(IMG_WEB_SERVICE, getImageResource(iconsBundle
                    .getString("IMG_WEB_SERVICE")));  //$NON-NLS-1$
            mImageRegistry.put(IMG_WEB_SERVICE_GROUP, getImageResource(iconsBundle
                    .getString("IMG_WEB_SERVICE_GROUP"))); //$NON-NLS-1$
            mImageRegistry.put(IMG_SEI, getImageResource(iconsBundle
                    .getString("IMG_SEI"))); //$NON-NLS-1$
            mImageRegistry.put(IMG_SEI_GROUP, getImageResource(iconsBundle
                    .getString("IMG_SEI_GROUP"))); //$NON-NLS-1$
            mImageRegistry.put(IMG_DOM_WS_PROVIDER, getImageResource(iconsBundle
                    .getString("IMG_DOM_WS_PROVIDER"))); //$NON-NLS-1$
            mImageRegistry.put(IMG_WEB_METHOD, getImageResource(iconsBundle
                    .getString("IMG_WEB_METHOD"))); //$NON-NLS-1$
            mImageRegistry.put(IMG_WEB_PARAM_IN, getImageResource(iconsBundle
                    .getString("IMG_WEB_PARAM_IN")));    //$NON-NLS-1$
            mImageRegistry.put(IMG_WEB_PARAM_OUT, getImageResource(iconsBundle
                    .getString("IMG_WEB_PARAM_OUT")));             //$NON-NLS-1$
            mImageRegistry.put(IMG_ERROR_MARKER, getImageResource(iconsBundle
                    .getString("IMG_ERROR_MARKER")));  //$NON-NLS-1$
            mImageRegistry.put(IMG_WARNING_MARKER, getImageResource(iconsBundle
                          .getString("IMG_WARNING_MARKER"))); //$NON-NLS-1$
            
        } catch (RuntimeException e) {
            (new Logger()).logError("Unable to initialize image registry !", e); //$NON-NLS-1$
        }
    }
    
    public ImageRegistry getImageRegistry() 
    {
        if (mImageRegistry == null) {
            initializeImageRegistry();
        }
        
        return mImageRegistry;
    }
    
    public Image getImage(String imageName) 
    {
        return INSTANCE.getImageRegistry().get(imageName);
    }
    
    public static ImageDescriptor getImageResourceByKey(String resourceName) {
        return getImageResource(iconsBundle.getString(resourceName));
    }
    
    public static ImageDescriptor getImageResource(String resourceName) 
    {
        try {
        	URL url = DomUi.getDefault().getBundle().getResource(resourceName);
            return ImageDescriptor.createFromURL(url);
        } 
        catch (NullPointerException npe) {
            (new Logger()).logError(MessageFormat.format("Unable to locate resource {0}!",resourceName), npe); //$NON-NLS-1$
            return ImageDescriptor.getMissingImageDescriptor();
        }
    }
}
