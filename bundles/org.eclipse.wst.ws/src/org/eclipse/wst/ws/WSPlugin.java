package org.eclipse.wst.ws;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

import java.text.MessageFormat;
import java.util.*;

/**
 * The main plugin class to be used in the desktop.
 */
public class WSPlugin extends Plugin {

	// The shared instance.
	private static WSPlugin plugin;

	// Resource bundle.
	private ResourceBundle resourceBundle;

	/**
	 * The constructor.
	 */
	public WSPlugin() {
		super();
		plugin = this;
	}

	/**
	 * This method is called upon plug-in activation
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		plugin = null;
		resourceBundle = null;
	}

	/**
	 * Returns the shared instance.
	 */
	public static WSPlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns the string from the plugin's resource bundle, or 'key' if not
	 * found.
	 */
	public static String getResourceString(String key) {
		ResourceBundle bundle = WSPlugin.getDefault().getResourceBundle();
		try {
			return (bundle != null) ? bundle.getString(key) : key;
		} catch (MissingResourceException e) {
			return key;
		}
	}

	/**
	 * Returns the string from the plugin's resource bundle, or 'key' if not
	 * found. Replaces substitution variables in the message by objects in 'args'.
	 */
	public static String getResourceString(String key, Object[] args) {
		return MessageFormat.format(getResourceString(key), args);
	}

	/**
	 * Returns the plugin's resource bundle,
	 */
	public ResourceBundle getResourceBundle() {
		try {
			if (resourceBundle == null)
				resourceBundle = ResourceBundle
						.getBundle("org.eclipse.wst.ws.WSPluginResources");
		} catch (MissingResourceException x) {
			resourceBundle = null;
		}
		return resourceBundle;
	}
}
