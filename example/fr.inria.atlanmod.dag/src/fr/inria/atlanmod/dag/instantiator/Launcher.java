/*******************************************************************************
 * Copyright (c) 2015  (AtlanMod Inria, Mines Nantes, Lina) 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Amine Benelallam (AtlanMod) - Adaptation for NoeEMF-HBase      
 *******************************************************************************/

package fr.inria.atlanmod.dag.instantiator;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.Comparator;
import java.util.logging.Logger;

import jline.Terminal;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.Range;
import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;

import fr.inria.atlanmod.dag.DAG;
import fr.inria.atlanmod.instantiator.neoEMF.GenerationException;
import fr.inria.atlanmod.instantiator.neoEMF.GenericMetamodelConfig;
import fr.inria.atlanmod.instantiator.neoEMF.GenericMetamodelGenerator;
import fr.inria.atlanmod.instantiator.neoEMF.IGenerator;
import fr.inria.atlanmod.neoemf.core.NeoEMFResourceFactory;
import fr.inria.atlanmod.neoemf.util.NeoEMFURI;
import fr.obeo.emf.specimen.ISpecimenConfiguration;

/**
 *  
 * @author <a href="mailto:amine.benelallam@inria.fr">Amine Benelallam</a>
 *
 */

public class Launcher {
	
	private final static Logger LOGGER = Logger.getLogger(Launcher.class.getName());

	private static final int DEFAULT_AVERAGE_MODEL_SIZE = 1000;
	private static final float DEFAULT_DEVIATION = 0.1f;
	
	private static final int ERROR = 1;


	private static final String OUTPUT_PATH 				= "u";
	private static final String OUTPUT_PATH_LONG			= "suffixe";
	private static final String N_MODELS					= "n";
	private static final String N_MODELS_LONG				= "number-models";
	private static final String SIZE 						= "s";
	private static final String SIZE_LONG					= "size";
	private static final String VARIATION 					= "v";
	private static final String VARIATION_LONG				= "variation";
	private static final String PROP_VARIATION 				= "p";
	private static final String PROP_VARIATION_LONG			= "properties-variation";
	private static final String DEGREE 						= "r";
	private static final String DEGREE_LONG 				= "references-degree";
	private static final String VALUES_SIZE 				= "z";
	private static final String VALUES_SIZE_LONG			= "values-size";
	private static final String SEED 						= "e";
	private static final String SEED_LONG 					= "seed";
	private static final String FORCE 						= "f";
	private static final String FORCE_LONG 					= "force";
	private static final String DIAGNOSE	 				= "g";
	private static final String DIAGNOSE_LONG				= "diagnose";

	private static class OptionComarator<T extends Option> implements Comparator<T> {
	    private static final String OPTS_ORDER = "unvspdrzefg";

	    @Override
		public int compare(T o1, T o2) {
	        return OPTS_ORDER.indexOf(o1.getOpt()) - OPTS_ORDER.indexOf(o2.getOpt());
	    }
	}

	public static void main(String[] args) throws GenerationException, IOException {

		ResourceSetImpl resourceSet = new ResourceSetImpl();
		{ // initializing the registry
			
			resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(
					EcorePackage.eNS_PREFIX, new EcoreResourceFactoryImpl());
			resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(
					Resource.Factory.Registry.DEFAULT_EXTENSION, new XMIResourceFactoryImpl());
			resourceSet.getResourceFactoryRegistry().getProtocolToFactoryMap().put(
					NeoEMFURI.NEOEMF_HBASE_SCHEME, NeoEMFResourceFactory.eINSTANCE);

			
		}

		
		Options options = new Options();

		configureOptions(options);

		CommandLineParser parser = new GnuParser();

		try {
			CommandLine commandLine = parser.parse(options, args);

//			String epackage_class = commandLine.getOptionValue(E_PACKAGE_CLASS);
//			
//			LOGGER.info("Start loading the package");
//			Class<?> inClazz = Launcher.class.getClassLoader().loadClass(epackage_class);
//			EPackage _package = (EPackage) inClazz.getMethod("init").invoke(null);
//			
//			Resource metamodelResource = new XMIResourceImpl(URI.createFileURI("dummy"));
//		    metamodelResource.getContents().add(_package);
//		    LOGGER.info("Finish loading the package");
		    
			int size = Launcher.DEFAULT_AVERAGE_MODEL_SIZE;
			if (commandLine.hasOption(SIZE)) {
				Number number = (Number) commandLine.getParsedOptionValue(SIZE);
				 size = (int) Math.min(Integer.MAX_VALUE, number.longValue());
			}
			
			float variation = Launcher.DEFAULT_DEVIATION;
			if (commandLine.hasOption(VARIATION)) {
				Number number = (Number) commandLine.getParsedOptionValue(VARIATION);
				if (number.floatValue() < 0.0f || number.floatValue() > 1.0f) {
					throw new ParseException(MessageFormat.format("Invalid value for option -{0}: {1}", VARIATION, number.floatValue()));
				}
				variation = number.floatValue();
			}
			
			
			float propVariation = Launcher.DEFAULT_DEVIATION;
			if (commandLine.hasOption(PROP_VARIATION)) {
				Number number = (Number) commandLine.getParsedOptionValue(PROP_VARIATION);
				if (number.floatValue() < 0.0f || number.floatValue() > 1.0f) {
					throw new ParseException(MessageFormat.format("Invalid value for option -{0}: {1}", PROP_VARIATION, number.floatValue()));
				}
				propVariation = number.floatValue();
			}
			
			
			long seed = System.currentTimeMillis();
			if (commandLine.hasOption(SEED)) {
				seed = ((Number) commandLine.getParsedOptionValue(SEED)).longValue();
			}

			Range<Integer> range = Range.between(
					Math.round(size * (1 - variation)), 
					Math.round(size * (1 + variation)));
			
			ISpecimenConfiguration config = new DagMetamodelConfig(range, seed);
			IGenerator generator = new DagGenerator(config, config.getSeed());	

			GenericMetamodelGenerator modelGen = new GenericMetamodelGenerator(generator);


			if (commandLine.hasOption(OUTPUT_PATH)) {
				String outDir = commandLine.getOptionValue(OUTPUT_PATH);
				//java.net.URI intermediateURI = java.net.URI.create(outDir);
				modelGen.setSamplesPath(outDir);
			} 

			int numberOfModels = 1;
			if (commandLine.hasOption(N_MODELS)) {
				numberOfModels = ((Number) commandLine.getParsedOptionValue(N_MODELS)).intValue();
			}

			int valuesSize = GenericMetamodelConfig.DEFAULT_AVERAGE_VALUES_LENGTH;
			if (commandLine.hasOption(VALUES_SIZE)) {
				Number number = (Number) commandLine.getParsedOptionValue(VALUES_SIZE);
				valuesSize = (int) Math.min(Integer.MAX_VALUE, number.longValue());
			}

			int referencesSize = GenericMetamodelConfig.DEFAULT_AVERAGE_REFERENCES_SIZE;
			if (commandLine.hasOption(DEGREE)) {
				Number number = (Number) commandLine.getParsedOptionValue(DEGREE);
				referencesSize = (int) Math.min(Integer.MAX_VALUE, number.longValue());
			} 
			
			config.setValuesRange(
					Math.round(valuesSize * (1 - propVariation)), 
					Math.round(valuesSize * (1 + propVariation)));
			
			config.setReferencesRange(
					Math.round(referencesSize * (1 - propVariation)), 
					Math.round(referencesSize * (1 + propVariation)));
			
			config.setPropertiesRange(
					Math.round(referencesSize * (1 - propVariation)), 
					Math.round(referencesSize * (1 + propVariation)));
			
			long start = System.currentTimeMillis();
			modelGen.runGeneration(resourceSet, numberOfModels, size, variation);
			long end = System.currentTimeMillis();
			LOGGER.info(MessageFormat.format("Generation finished after {0} s", Long.toString((end-start)/1000)));
			
//			for (Resource rsc : resourceSet.getResources()) {
//				if (rsc.getContents().get(0) instanceof DAG) {
//					
//				}
//					
//			}
			
			if (commandLine.hasOption(DIAGNOSE)) {
				for (Resource resource : resourceSet.getResources()) {
					LOGGER.info(MessageFormat.format("Requested validation for resource ''{0}''", resource.getURI()));
					BasicDiagnostic diagnosticChain = diagnoseResource(resource);
					if (!isFailed(diagnosticChain)) {
						LOGGER.info(MessageFormat.format("Result of the diagnosis of resurce ''{0}'' is ''OK''",
												resource.getURI()));
					} else {
						LOGGER.severe(MessageFormat.format("Found ''{0}'' error(s) in the resource ''{1}''",
								diagnosticChain.getChildren().size(), resource.getURI()));
						for (Diagnostic diagnostic : diagnosticChain.getChildren()) {
							LOGGER.fine(diagnostic.getMessage());
						}
					}
				}
				LOGGER.info("Validation finished");
			}
			
		} catch (ParseException e) {
			System.err.println(e.getLocalizedMessage());
			HelpFormatter formatter = new HelpFormatter();
			formatter.setOptionComparator(new OptionComarator<Option>());
			try {
				formatter.setWidth(Math.max(Terminal.getTerminal().getTerminalWidth(), 80));
			} catch (Throwable t) {
				LOGGER.warning("Unable to get console information");
			};
			formatter.printHelp("java -jar <this-file.jar>", options, true);
			System.exit(ERROR);
		} catch (Throwable t) {
			System.err.println("ERROR: " + t.getLocalizedMessage());
			StringWriter stringWriter = new StringWriter();
			t.printStackTrace(new PrintWriter(stringWriter));
			System.err.println(t);
			LOGGER.severe(stringWriter.toString());
			System.exit(ERROR);
		} 
	}



	private static BasicDiagnostic diagnoseResource(Resource resource) {
		BasicDiagnostic diagnosticChain = new BasicDiagnostic();
		for (EObject eObject : resource.getContents()) {
			Diagnostician.INSTANCE.validate(eObject, diagnosticChain);
		}
		return diagnosticChain;
	}
	
	private static boolean isFailed(BasicDiagnostic diagnosticChain) {
		return (diagnosticChain.getSeverity() & Diagnostic.ERROR) == Diagnostic.ERROR;
	}

	@SuppressWarnings("unused")
	private static boolean isWarning(BasicDiagnostic diagnosticChain) {
		return (diagnosticChain.getSeverity() & Diagnostic.WARNING) == Diagnostic.WARNING;
	}


	/**
	 * Configures the program options
	 *
	 * @param options
	 */
	private static void configureOptions(Options options) {



		Option outDirOpt = OptionBuilder.create(OUTPUT_PATH);
		outDirOpt.setLongOpt(OUTPUT_PATH_LONG);
		outDirOpt.setArgName("neoEMF output uri");
		outDirOpt.setDescription("Output directory (defaults to working dir)");
		outDirOpt.setArgs(1);
		outDirOpt.setRequired(true);

		Option nModelsOpt = OptionBuilder.create(N_MODELS);
		nModelsOpt.setLongOpt(N_MODELS_LONG);
		nModelsOpt.setArgName("models");
		nModelsOpt.setDescription("Number of generated models (defaults to 1)");
		nModelsOpt.setType(Number.class);
		nModelsOpt.setArgs(1);

		Option sizeOption = OptionBuilder.create(SIZE);
		sizeOption.setLongOpt(SIZE_LONG);
		sizeOption.setArgName("size");
		sizeOption.setDescription(MessageFormat.format("Average models'' size (defaults to {0})", Launcher.DEFAULT_AVERAGE_MODEL_SIZE));
		sizeOption.setType(Number.class);
		sizeOption.setArgs(1);

		Option densityOption = OptionBuilder.create(VARIATION);
		densityOption.setLongOpt(VARIATION_LONG);
		densityOption.setArgName("proportion");
		densityOption.setDescription(
				MessageFormat.format("Variation ([0..1]) in the models'' size (defaults to {0})",
				Launcher.DEFAULT_DEVIATION));
		densityOption.setType(Number.class);
		densityOption.setArgs(1);

	
		Option propVariationOption = OptionBuilder.create(PROP_VARIATION);
		propVariationOption.setLongOpt(PROP_VARIATION_LONG);
		propVariationOption.setArgName("properties deviation");
		propVariationOption.setDescription(
				MessageFormat.format("Variation ([0..1]) in the properties'' size (defaults to {0})",
				Launcher.DEFAULT_DEVIATION));
		propVariationOption.setType(Number.class);
		propVariationOption.setArgs(1);
		
		Option seedOption = OptionBuilder.create(SEED);
		seedOption.setLongOpt(SEED_LONG);
		seedOption.setArgName("seed");
		seedOption.setDescription("Seed number (random by default)");
		seedOption.setType(Number.class);
		seedOption.setArgs(1);

		Option valuesSizeOption = OptionBuilder.create(VALUES_SIZE);
		valuesSizeOption.setLongOpt(VALUES_SIZE_LONG);
		valuesSizeOption.setArgName("size");
		valuesSizeOption.setDescription(
				MessageFormat.format("Average size for attributes with variable length (defaults to {0}). Actual sizes may vary +/- {1}%.", 
				GenericMetamodelConfig.DEFAULT_AVERAGE_VALUES_LENGTH, GenericMetamodelConfig.DEFAULT_VALUES_DEVIATION * 100));
		valuesSizeOption.setType(Number.class);
		valuesSizeOption.setArgs(1);

		Option degreeOption = OptionBuilder.create(DEGREE);
		degreeOption.setLongOpt(DEGREE_LONG);
		degreeOption.setArgName("degree");
		degreeOption.setDescription(
				MessageFormat.format("Average number of references per EObject (defaults to {0}). Actual sizes may vary +/- {1}%.",
						GenericMetamodelConfig.DEFAULT_AVERAGE_REFERENCES_SIZE, GenericMetamodelConfig.DEFAULT_REFERENCES_DEVIATION * 100));
		degreeOption.setType(Number.class);
		degreeOption.setArgs(1);
		
		Option forceOption = OptionBuilder.create(FORCE);
		forceOption.setLongOpt(FORCE_LONG);
		forceOption.setDescription("Force the generation, even if input metamodels contain errors");
		
		Option diagnoseOption = OptionBuilder.create(DIAGNOSE);
		diagnoseOption.setLongOpt(DIAGNOSE_LONG);
		diagnoseOption.setDescription("Run diagnosis on the result model");
				
		options.addOption(outDirOpt);
		options.addOption(nModelsOpt);
		options.addOption(sizeOption);
		options.addOption(propVariationOption);
		options.addOption(valuesSizeOption);
		options.addOption(degreeOption);
		options.addOption(seedOption);
		options.addOption(forceOption);
		options.addOption(diagnoseOption);
		options.addOption(densityOption);
	}
	/**
	 * Registers the packages
	 * @param resource
	 */
	@SuppressWarnings("unused")
	private static void registerPackages(Resource resource) {
		EObject eObject = resource.getContents().get(0);
		if (eObject instanceof EPackage) {
			EPackage p = (EPackage) eObject;
			EPackage.Registry.INSTANCE.put(p.getNsURI(), p);
		}
	}
}
