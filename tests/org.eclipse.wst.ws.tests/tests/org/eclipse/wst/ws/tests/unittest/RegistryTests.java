package org.eclipse.wst.ws.tests.unittest;

import java.io.File;
import java.net.MalformedURLException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.emf.common.util.EList;
import org.eclipse.wst.ws.internal.model.v10.registry.Name;
import org.eclipse.wst.ws.internal.model.v10.registry.RegistryFactory;
import org.eclipse.wst.ws.internal.model.v10.taxonomy.Category;
import org.eclipse.wst.ws.internal.model.v10.taxonomy.Taxonomy;
import org.eclipse.wst.ws.internal.model.v10.taxonomy.TaxonomyFactory;
import org.eclipse.wst.ws.internal.model.v10.uddiregistry.Taxonomies;
import org.eclipse.wst.ws.internal.model.v10.uddiregistry.UDDIRegistry;
import org.eclipse.wst.ws.internal.model.v10.uddiregistry.UDDIRegistryFactory;
import org.eclipse.wst.ws.internal.registry.IRegistryManager;
import org.eclipse.wst.ws.internal.registry.RegistryService;
import org.eclipse.wst.ws.internal.registry.UDDIRegistryService;
import org.eclipse.wst.ws.internal.registry.UDDITaxonomyFinder;

public class RegistryTests extends TestCase
{
	public static Test suite ()
	{
		return new TestSuite(RegistryTests.class);
	}

	/**
	 * Creates a new Taxonomy model and passes it to
	 * <code>RegistryService.saveTaxonomy()</code>
	 * where the model is persisted to a platform
	 * specified temporary filesystem location.
	 */
	public void test_RegistryService_saveTaxonomy ()
	{
		System.out.println("test_RegistryService_saveTaxonomy starting");
		try
		{
			System.out.println("Creating RegistryService and Taxonomy");
			RegistryService registryService = RegistryService.instance();			
			Taxonomy taxonomy = registryService.newTaxonomy();

			System.out.println("Building a simple Taxonomy model");
			taxonomy.setId("my.taxonomy");
			taxonomy.setName("My Taxonomy");
			EList list = taxonomy.getCategory();
			Category category1 = TaxonomyFactory.eINSTANCE.createCategory();
			category1.setName("My first category");
			category1.setCode("1");
			Category category2 = TaxonomyFactory.eINSTANCE.createCategory();
			category2.setName("My second category");
			category2.setCode("2");
			Category category3 = TaxonomyFactory.eINSTANCE.createCategory();
			category3.setName("My third category");
			category3.setCode("2.1");
			category2.getCategory().add(category3);
			list.add(category1);
			list.add(category2);

			File file = File.createTempFile("taxonomy.",".xml");
			System.out.println("Saving the Taxonomy model to "+file.getCanonicalPath());
			registryService.saveTaxonomy(file.toURL(),taxonomy);
		}
		catch (Throwable exc)
		{
			exc.printStackTrace();
			fail("Unexpected Throwable ["+exc.getMessage()+"]");
		}
		finally
		{
			System.out.println("test_RegistryService_saveTaxonomy finished");
		}
	}

	/**
	 * Creates a new UDDI Registry model and passes it to
	 * <code>RegistryService.saveRegistry()</code>
	 * where the model is persisted to a platform
	 * specified temporary filesystem location.
	 */
	public void test_RegistryService_saveRegistry ()
	{
		System.out.println("test_RegistryService_saveRegistry starting");
		try
		{
			System.out.println("Creating RegistryService, UDDIRegistryService and Registry");
			RegistryService registryService = RegistryService.instance();
			UDDIRegistryService uddiRegistryService = UDDIRegistryService.instance();			
			UDDIRegistry uddiRegistry = uddiRegistryService.newUDDIRegistry();

			System.out.println("Building a simple Registry model");
			uddiRegistry.setId("my.registry");
			uddiRegistry.setVersion("2.0");
			Name name = RegistryFactory.eINSTANCE.createName();
			name.setValue("My Registry");
			uddiRegistry.getName().add(name);
			Taxonomy taxonomyRef = registryService.newTaxonomy();
			taxonomyRef.setRef("my.taxonomy");
			Taxonomies t = uddiRegistry.getTaxonomies();
			if (t == null)
			{
				t = UDDIRegistryFactory.eINSTANCE.createTaxonomies();
				uddiRegistry.setTaxonomies(t);
			}
			EList e = t.getTaxonomy();
			e.add(taxonomyRef);

			File file = File.createTempFile("registry.",".xml");
			System.out.println("Saving the Registry model to "+file.getCanonicalPath());
			registryService.saveRegistry(file.toURL(),uddiRegistry);
		}
		catch (Throwable exc)
		{
			exc.printStackTrace();
			fail("Unexpected Throwable ["+exc.getMessage()+"]");
		}
		finally
		{
			System.out.println("test_RegistryService_saveRegistry finished");
		}
	}

	/**
	 * Creates a new UDDI Registry model and a new Taxonomy
	 * model, associates the latter with the former, and
	 * drives <code>IRegistryManager.saveRegistry()</code>
	 * to save the models and the index document to the
	 * preferred location of the default registry manager.
	 */
	public void test_RegistryManager_saveRegistry()
	{
		System.out.println("test_RegistryManager_saveRegistry starting");
		try {
			System.out.println("Creating RegistryService and Getting RegistryManager");	
			RegistryService registryService = RegistryService.instance();	
			IRegistryManager regManager = registryService.getDefaultRegistryManager();

			try{
				System.out.println("The location of the default index is....." + regManager.getURL());
			}catch(MalformedURLException me){
				me.printStackTrace();
				fail("Unexpected MalformedURLException ["+me.getMessage()+"]");
			}

			//build the hockey taxonomy
			Taxonomy hockeyTaxonomy = registryService.newTaxonomy();

			System.out.println("Building the hockey Taxonomy model");
			hockeyTaxonomy.setId("hockey.taxonomy");
			hockeyTaxonomy.setName("hockey Taxonomy");
			EList list = hockeyTaxonomy.getCategory();
			Category category1 = TaxonomyFactory.eINSTANCE.createCategory();
			Category category4 = TaxonomyFactory.eINSTANCE.createCategory();
			category4.setName("Pro");
			category4.setCode("4");
			Category category5 = TaxonomyFactory.eINSTANCE.createCategory();
			category5.setName("AHL");
			category5.setCode("4.1");
			Category category6 = TaxonomyFactory.eINSTANCE.createCategory();
			category6.setName("NHL");
			category6.setCode("4.2");
			category1.setName("Junior");
			category1.setCode("1");
			Category category2 = TaxonomyFactory.eINSTANCE.createCategory();
			category2.setName("Midget");
			category2.setCode("2");
			Category category3 = TaxonomyFactory.eINSTANCE.createCategory();
			category3.setName("Bantam");
			category3.setCode("3");
			category4.getCategory().add(category5);
			category4.getCategory().add(category6);
			list.add(category1);
			list.add(category2);
			list.add(category3);
			list.add(category4);

			//make the hockey registry

			UDDIRegistryService uddiRegistryService = UDDIRegistryService.instance();			
			UDDIRegistry uddiRegistry = uddiRegistryService.newUDDIRegistry();

			System.out.println("Building the hockey Registry model");
			uddiRegistry.setId("hockey.registry");
			uddiRegistry.setVersion("2.0");
			Name name = RegistryFactory.eINSTANCE.createName();
			name.setValue("Hockey Registry");
			uddiRegistry.getName().add(name);
			Taxonomies t = uddiRegistry.getTaxonomies();
			if (t == null)
			{
				t = UDDIRegistryFactory.eINSTANCE.createTaxonomies();
				uddiRegistry.setTaxonomies(t);
			}	
			EList e = t.getTaxonomy();
			e.add(hockeyTaxonomy);

			//First we have to add the uddiTaxonomyFinder
			UDDITaxonomyFinder UITFinder = new UDDITaxonomyFinder();
			regManager.addTaxonomyFinder(uddiRegistry.getClass().getName(),UITFinder);

			//now test the registry manager
			System.out.println("Saving the hockey Registry model");
			try{
				regManager.saveRegistry(uddiRegistry);
			}catch(Throwable exc){
				exc.printStackTrace();
				fail("Unexpected Throwable ["+exc.getMessage()+"]");
			}
		}
		finally
		{
			System.out.println("test_RegistryManager_saveRegistry finished");
		}
	}
}
