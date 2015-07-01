package fr.inria.atlanmod.instantiator.neoEMF;

import java.io.File;
import java.net.URI;


public class HBaseMetamodelGenerator extends GenericMetamodelGenerator{

//	private final static Logger LOGGER = Logger.getLogger(GenericMetamodelGenerator.class.getName());
	

	public HBaseMetamodelGenerator(GenericMetamodelConfig config) throws IllegalArgumentException {
		super(config);
	}

	public HBaseMetamodelGenerator (GenericMetamodelConfig config, URI prefix) {
		super(config);
		setSamplesPath(prefix.toString());
	}
	

	protected org.eclipse.emf.common.util.URI formatURI(String modelPrefix, long maxElement, int index) {
		StringBuilder builder = new StringBuilder();
		builder.append(samplesPath.toString());
		builder.append(File.separator);
		builder.append(modelPrefix);
		builder.append(File.separator);
		builder.append("model");
		builder.append(maxElement);
		builder.append(File.separator);
		builder.append("gen");
		builder.append(maxElement);
		return org.eclipse.emf.common.util.URI.createURI(builder.toString());
	}
	
	
}
