package fr.inria.atlanmod.dag.instantiator;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.Range;
import org.apache.commons.math3.distribution.IntegerDistribution;
import org.apache.commons.math3.distribution.UniformIntegerDistribution;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;

import com.google.common.collect.ImmutableSet;

import fr.inria.atlanmod.dag.DagPackage;
import fr.inria.atlanmod.instantiator.neoEMF.GenericMetamodelConfig;

public class DagMetamodelConfig extends GenericMetamodelConfig {


	protected final static DagPackage pck = DagPackage.eINSTANCE;
	private static Resource initMetamodelResource() {
		ResourceSet rsSet = new ResourceSetImpl();
		rsSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(
				EcorePackage.eNS_PREFIX, new EcoreResourceFactoryImpl());
		Resource dagRes = rsSet.createResource(URI.createURI("dummy.ecore")); 
		dagRes.getContents().add(pck);
		return dagRes;
	}

	private float density;
	
	/**
	 * Creates an instance of the dag metamodel configuration 
	 * 
	 * @param elementsRange
	 * @param seed the transformation seed 
	 */
	public DagMetamodelConfig( Range<Integer> elementsRange, long seed) {
		super(initMetamodelResource(), elementsRange, seed);
	}
	
	/**
	 * Creates an instance of the dag metamodel configuration 
	 * 
	 * @param elementsRange
	 */
	public DagMetamodelConfig (Range<Integer> elementsRange) {
		super(initMetamodelResource(), elementsRange);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ImmutableSet<EClass> possibleRootEClasses() {
		
		Set<EClass> result = new HashSet<EClass>();
		result.add(DagPackage.eINSTANCE.getDAG());
		return ImmutableSet.copyOf(result);
		
	}
	
	@Override
	public IntegerDistribution getDistributionFor(EReference eReference) {
		IntegerDistribution distribution = distributions.get(eReference);
		if (distribution !=  null) 
			return distribution;
		
		if (eReference.equals(pck.getDAG_Vertices())) {
			distribution = new UniformIntegerDistribution(elementsRange.getMinimum(), elementsRange.getMaximum());
			distribution.reseedRandomGenerator(random.nextLong());
			distributions.put(eReference, distribution);
			return distribution;
		} else if (eReference.equals(pck.getDAG_Edges())) {
			int density = (referencesRange.getMaximum() + referencesRange.getMinimum()) / 2;
			distribution = new UniformIntegerDistribution(elementsRange.getMinimum() * density, elementsRange.getMaximum() * density);
			distribution.reseedRandomGenerator(random.nextLong());
			distributions.put(eReference, distribution);
			return distribution;
		}
			
		return super.getDistributionFor(eReference);
	}

	@Override
	public IntegerDistribution getResourceSizeDistribution() {
		IntegerDistribution distribution = distributions.get(metamodelResource);
		if (distribution == null) {
			int coef = ((referencesRange.getMaximum() + referencesRange.getMinimum()) / 2) + 1 ;
			distribution = new UniformIntegerDistribution(elementsRange.getMinimum() * coef, elementsRange.getMaximum() * coef);
			distribution.reseedRandomGenerator(random.nextLong());
			distributions.put(metamodelResource, distribution);
		}
		return distribution;
	}
}
