/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.ws.internal.registry;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Hashtable;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.wst.ws.internal.model.v10.registry.Registry;
import org.eclipse.wst.ws.internal.model.v10.registry.RegistryFactory;
import org.eclipse.wst.ws.internal.model.v10.registry.RegistryPackage;
import org.eclipse.wst.ws.internal.model.v10.rtindex.RTIndexPackage;
import org.eclipse.wst.ws.internal.model.v10.taxonomy.Taxonomy;
import org.eclipse.wst.ws.internal.model.v10.taxonomy.TaxonomyFactory;
import org.eclipse.wst.ws.internal.model.v10.taxonomy.TaxonomyPackage;
import org.eclipse.wst.ws.internal.plugin.WSPlugin;

/**
 * RegistryService is the starting class of the Web service registry
 * programming model. Call the <code>RegistryService.instance()</code>
 * static method to get the singleton instance of this class.
 * <p>
 * RegistryService provides factory methods for getting new
 * instances of <code>IRegistryManager</code> objects which,
 * in turn, provide the means for registering and unregistering
 * groups of <code>Registry</code> and <code>Taxonomy</code> models
 * at URL addressable locations in the filesystem or elsewhere.
 * <p>
 * RegistryService provides utility methods to ease the creation,
 * loading and saving raw instances of the <code>Registry</code>
 * and <code>Taxonomy</code> models, and to simplify the process of
 * composing and finding <code>Taxonomy</code> models in
 * <code>Registry</code> models.
 * <p>
 * A typical use of <code>RegistryService</code> and its related
 * classes to create and save a new family of models is:
 * <blockquote>
 * <code>
 * RegistryService registryService = RegistryService.instance();
 * MyRegistry registry = MyRegistryFactory.eINSTANCE.createMyRegistry();
 * // build up your registry model here
 * Taxonomy taxonomy = registryService.newTaxonomy();
 * // build up your taxonomy model here
 * registryService.addTaxonomy(registry,taxonomy);
 * IRegistryManager registryManager = registryService.getDefaultRegistryManager();
 * registryManager.saveRegistry(registry);
 * </code>
 * </blockquote>
 * @see #instance()
 * @see IRegistryManager
 * @see Registry
 * @see Taxonomy
 * @author cbrealey
 */
public class RegistryService
{
	private static RegistryService instance_;
	private static final String DEFAULT_INDEX_XML_FILE = "DefaultIndex.xml";
	private Hashtable registryManagers_ = new Hashtable();
	private IRegistryManager defaultRegistryManager_ = null;
	private GenericResourceFactory resourceFactory_ = new GenericResourceFactory();

	/**
	 * A RegistryService cannot be directly constructed.
	 * Use @link #instance() to get the singleton of this class.
	 */
	private RegistryService ()
	{
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("xml",resourceFactory_);
		RegistryPackage.eINSTANCE.getClass();
		TaxonomyPackage.eINSTANCE.getClass();
		RTIndexPackage.eINSTANCE.getClass();
	}

	/**
	 * Returns the singleton of this class.
	 * @return The singleton of this class.
	 */
	public static RegistryService instance ()
	{
		if (instance_ == null)
		{
			instance_ = new RegistryService();
		}
		return instance_;
	}
	
	/**
	 * Creates a default <code>IRegistryManager</code> for the system.
	 * @return The default <code>IRegistryManager</code> for the system.
	 * @see IRegistryManager
	 */
	public IRegistryManager getDefaultRegistryManager ()
	{
		if (defaultRegistryManager_ == null)
		{
			String indexFilename = WSPlugin.getDefault().getStateLocation().append(DEFAULT_INDEX_XML_FILE).toString();
			defaultRegistryManager_ = new RegistryManager(indexFilename);
		}
		return defaultRegistryManager_;
	}

	/**
	 * Creates a new or existing <code>IRegistryManager</code> for the given URL.
	 * @param url A URL to the XML document that persists
	 * the set of registries managed by this <code>IRegistryManager</code>.
	 * @return The new or existing <code>RegistryManager</code> for the given URL.
	 * @see IRegistryManager
	 */
	public IRegistryManager getRegistryManager ( URL url )
	{
		IRegistryManager registryManager = (IRegistryManager)registryManagers_.get(url);
		if (registryManager == null)
		{
			registryManager = new RegistryManager(url);
			registryManagers_.put(url,registryManager);
		}
		return registryManager;
	}
	
	/**
	 * Removes the <code>IRegistryManager</code> for the given URL.
	 * No physical resources are deleted.
	 * @param url A URL to the XML document that persists
	 * the set of registries managed by this <code>IRegistryManager</code>.
	 * @see IRegistryManager
	 */
	public void removeRegistryManager ( URL url )
	{
		registryManagers_.remove(url);
	}

	/**
	 * Creates and returns a new <code>Registry</code> model.
	 * As a general rule, this method has little actual value as
	 * most concrete registry models will be of types that extend
	 * the base Registry model, such as <code>UDDIRegistry</code>.
	 * For example, <code>UDDIRegistryService.newUDDIRegistry()</code>
	 * returns a new <code>UDDIRegistry</code>. Providers of other
	 * concrete types of registries may provide similar facilities.
	 * @return A new <code>Registry</code> model. Never returns null.
	 * @see Registry
	 * @see UDDIRegistryService#newUDDIRegistry()
	 */
	public Registry newRegistry ()
	{
		return RegistryFactory.eINSTANCE.createRegistry();
	}
	
	/**
	 * Loads the <code>Registry</code> model from an XML document.
	 * If loading the model of a registry that extends the base
	 * Registry interface and Schema, you must initialize the EMF
	 * eCore package (eg. <code>MyRegistryPackage.eINSTANCE</code>
	 * for your model before calling this load method.
	 * For UDDI Registry models, this is done automatically by
	 * calling <code>UDDIRegistryService.instance()</code>.
	 * @param url The URL to the XML document to load.
	 * @return The <code>Registry</code> model.
	 * @throws <code>CoreException</code> If the model cannot be loaded.
	 * @see Registry
	 * @see UDDIRegistryService#instance()
	 */
	public Registry loadRegistry ( URL url ) throws CoreException
	{
		Resource resource = resourceFactory_.createResource(URI.createURI("*.xml"));
		try
		{
			resource.load(getInputStreamFor(url),null);
		}
		catch (IOException e)
		{
			throw new CoreException(new Status(IStatus.ERROR,WSPlugin.ID,0,"",e));
		}
		org.eclipse.wst.ws.internal.model.v10.registry.DocumentRoot document = (org.eclipse.wst.ws.internal.model.v10.registry.DocumentRoot)resource.getContents().get(0);
		return document.getRegistry();
	}

	/**
	 * Saves the <code>Registry</code> model to an XML document.
	 * @param url The URL to the XML document to save.
	 * @param registry The <code>Registry</code> model to save.
	 * @throws <code>CoreException</code> If the model cannot be saved.
	 * @see Registry
	 */
	public void saveRegistry ( URL url, Registry registry ) throws CoreException
	{
		org.eclipse.wst.ws.internal.model.v10.registry.DocumentRoot document = RegistryFactory.eINSTANCE.createDocumentRoot();
		document.setRegistry(registry);
		Resource resource = resourceFactory_.createResource(URI.createURI("*.xml"));
		resource.getContents().add(document);
		try
		{
			resource.save(getOutputStreamFor(url),null);
		}
		catch (IOException e)
		{
			throw new CoreException(new Status(IStatus.ERROR,WSPlugin.ID,0,"",e));
		}
	}

	/**
	 * Creates and returns a new <code>Taxonomy</code> model.
	 * @return A new <code>Taxonomy</code> model. Never returns null.
	 * @see Taxonomy
	 */
	public Taxonomy newTaxonomy ()
	{
		return TaxonomyFactory.eINSTANCE.createTaxonomy();
	}
	
	/**
	 * Loads the <code>Taxonomy</code> model from an XML document.
	 * @param url The URL to the XML document to load.
	 * @return the <code>Taxonomy</code> model.
	 * @throws <code>CoreException</code> If the model cannot be loaded.
	 * @see Taxonomy
	 */
	public Taxonomy loadTaxonomy ( URL url ) throws CoreException
	{
		Resource resource = resourceFactory_.createResource(URI.createURI("*.xml"));
		try
		{
			resource.load(getInputStreamFor(url),null);
		}
		catch (IOException e)
		{
			throw new CoreException(new Status(IStatus.ERROR,WSPlugin.ID,0,"",e));
		}
		org.eclipse.wst.ws.internal.model.v10.taxonomy.DocumentRoot document = (org.eclipse.wst.ws.internal.model.v10.taxonomy.DocumentRoot)resource.getContents().get(0);
		return document.getTaxonomy();
	}

	/**
	 * Saves the <code>Taxonomy</code> model to an XML document.
	 * @param url The URL to the XML document to save.
	 * @param taxonomy The <code>Taxonomy</code> model to save.
	 * @throws <code>CoreException</code> If the model cannot be saved.
	 * @see Taxonomy
	 */
	public void saveTaxonomy ( URL url, Taxonomy taxonomy ) throws CoreException
	{
		org.eclipse.wst.ws.internal.model.v10.taxonomy.DocumentRoot document = TaxonomyFactory.eINSTANCE.createDocumentRoot();
		document.setTaxonomy(taxonomy);
		Resource resource = resourceFactory_.createResource(URI.createURI("*.xml"));
		resource.getContents().add(document);
		try
		{
			resource.save(getOutputStreamFor(url),null);
		}
		catch (IOException e)
		{
			throw new CoreException(new Status(IStatus.ERROR,WSPlugin.ID,0,"",e));
		}
	}

	/**
	 * Constructs and returns a new Registry model configured
	 * to reference the given <code>registry</code>.
	 * <p>
	 * The given <code>registry</code> can be either a full
	 * model as signified by having a non-null, non-empty
	 * identifier (<code>registry.getId()</code>), or a
	 * reference itself as signified by having a non-null,
	 * non-empty reference (<code>registry.getRef()</code>).
	 * <p>
	 * If the given <code>registry</code> is a full model,
	 * then a new <code>Registry</code> is returned such that
	 * <ul>
	 * <li><code>getRef()</code> equals <code>registry.getId()</code></li>
	 * <li><code>getLocation()</code> is null</li>
	 * <li><code>getId()</code> is null</li>
	 * </ul>
	 * <p>
	 * In the given <code>registry</code> is itself a reference,
	 * then a new <code>Registry</code> is returned such that
	 * <ul>
	 * <li><code>getRef()</code> equals <code>registry.getRef()</code></li>
	 * <li><code>getLocation()</code> equals <code>registry.getLocation()</code></li>
	 * <li><code>getId()</code> is null</li>
	 * </ul>
	 * 
	 * @param registry The <code>Registry</code> model to clone or
	 * refer to depending on whether the given <code>registry</code>
	 * is a full model or a reference.
	 * @return A new <code>Registry</code>, guaranteed to be a reference.
	 */
	public Registry newRegistryReference ( Registry registry )
	{
		String id = registry.getId();
		String ref = registry.getRef();
		Registry registryRef = newRegistry();
		if (ref == null || "".equals(ref))
		{
			registryRef.setRef(id);
		}
		else
		{
			registryRef.setRef(ref);
			registryRef.setLocation(registry.getLocation());
		}
		return registryRef;
	}

	/**
	 * Constructs and returns a new Taxonomy model configured
	 * to reference the given <code>taxonomy</code>.
	 * <p>
	 * The given <code>taxonomy</code> can be either a full
	 * model as signified by having a non-null, non-empty
	 * identifier (<code>taxonomy.getId()</code>), or a
	 * reference itself as signified by having a non-null,
	 * non-empty reference (<code>taxonomy.getRef()</code>).
	 * <p>
	 * If the given <code>taxonomy</code> is a full model,
	 * then a new <code>Taxonomy</code> is returned such that
	 * <ul>
	 * <li><code>getRef()</code> equals <code>taxonomy.getId()</code></li>
	 * <li><code>getLocation()</code> is null</li>
	 * <li><code>getId()</code> is null</li>
	 * </ul>
	 * <p>
	 * In the given <code>taxonomy</code> is itself a reference,
	 * then a new <code>Taxonomy</code> is returned such that
	 * <ul>
	 * <li><code>getRef()</code> equals <code>taxonomy.getRef()</code></li>
	 * <li><code>getLocation()</code> equals <code>taxonomy.getLocation()</code></li>
	 * <li><code>getId()</code> is null</li>
	 * </ul>
	 * 
	 * @param taxonomy The <code>Taxonomy</code> model to clone or
	 * refer to depending on whether the given <code>taxonomy</code>
	 * is a full model or a reference.
	 * @return A new <code>Taxonomy</code>, guaranteed to be a reference.
	 */
	public Taxonomy newTaxonomyReference ( Taxonomy taxonomy )
	{
		String id = taxonomy.getId();
		String ref = taxonomy.getRef();
		Taxonomy taxonomyRef = newTaxonomy();
		if (ref == null || "".equals(ref))
		{
			taxonomyRef.setRef(id);
		}
		else
		{
			taxonomyRef.setRef(ref);
			taxonomyRef.setLocation(taxonomy.getLocation());
		}
		return taxonomyRef;
	}

	/**
	 * Returns true if and only if the given URL identifies
	 * a resource that exists and is a file.
	 * @param url The URL of the resource to check for existence.
	 * @return True if the URL resource exists and is a file,
	 * or false otherwise.
	 */
	static boolean exists ( URL url )
	{
		if ("platform".equals(url.getProtocol()))
		{
			String urlFile = url.getFile().trim();
			IPath path = new Path(urlFile).removeFirstSegments(1);
			if (path.segmentCount() <= 1)
			{
				return false;
			}
			IPath osPath = ResourcesPlugin.getWorkspace().getRoot().getFile(path).getLocation();
			if (osPath == null)
			{
				return false;
			}
			File file = new File(osPath.toString());
			return (file.exists() && !file.isDirectory());
		}
		else if ("file".equals(url.getProtocol()))
		{
			File file = new File(url.getFile().trim());
			return (file.exists() && !file.isDirectory());
		}
		return false;
	}

	/**
	 * Opens an <code>OutputStream</code> for writing to
	 * the given <code>url</code>.
	 * @param url The URL of the resource to write.
	 * @return An OuptutStream to write to.
	 * @throws IOException If the stream could not be opened.
	 */
	static OutputStream getOutputStreamFor ( URL url ) throws IOException
	{
		OutputStream outputStream = null;
		if ("platform".equals(url.getProtocol()))
		{
			String urlFile = url.getFile().trim();
			IPath path = new Path(urlFile).removeFirstSegments(1);
			if (path.segmentCount() <= 1)
			{
				throw new IOException("The Platform URL {0} is not bound to a local, writable file.");
			}
			IPath osPath = ResourcesPlugin.getWorkspace().getRoot().getFile(path).getLocation();
			if (osPath == null)
			{
				throw new IOException("The Platform URL {0} is not bound to a local, writable file.");
			}
			File file = new File(osPath.toString());
			if (file.isDirectory())
			{
				throw new IOException("The Platform URL {0} is bound to a folder.");
			}
			outputStream = new FileOutputStream(file);
		}
		else if ("file".equals(url.getProtocol()))
		{
			File file = new File(url.getFile().trim());
			if (file.isDirectory())
			{
				throw new IOException("The File URL {0} is bound to a directory.");
			}
			outputStream = new FileOutputStream(file);
		}
		else
		{
			URLConnection connection = url.openConnection();
			connection.setDoOutput(true);
			outputStream = connection.getOutputStream();
		}
		return outputStream;
	}

	/**
	 * Opens an <code>InputStream</code> for reading from
	 * the given <code>url</code>.
	 * @param url The URL of the resource to read.
	 * @return An InputStream to read from.
	 * @throws IOException If the stream could not be opened.
	 */
	static InputStream getInputStreamFor ( URL url ) throws IOException
	{
		return url.openStream();
	}
}
