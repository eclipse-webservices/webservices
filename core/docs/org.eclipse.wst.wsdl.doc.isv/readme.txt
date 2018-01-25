To generate javadoc follow these steps:

- make sure you have these plug-ins in the workspace
	org.eclipse.wst.wsdl
	javax.wsdl (as a binary import)
	org.eclipse.xsd (as a binary import)
	
- run the main build.xml script with the antRunner application
	create a new platform launch configuration
	select run an application
	select org.eclipse.ant.core.antRunner as the application to run
	add the following as the program arguments 
		-buildfile {workspace_loc}\org.eclipse.wst.wsdl.doc.isv\buildDocs.xml