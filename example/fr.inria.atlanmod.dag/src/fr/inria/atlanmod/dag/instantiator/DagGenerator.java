/*******************************************************************************
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Abel G�mez (AtlanMod) - Additional modifications      
 *******************************************************************************/

package fr.inria.atlanmod.dag.instantiator;

import static com.google.common.collect.Iterables.get;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;

import org.apache.commons.math3.distribution.IntegerDistribution;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;

import com.google.common.base.Optional;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ListMultimap;

import fr.inria.atlanmod.dag.DagPackage;
import fr.inria.atlanmod.dag.Edge;
import fr.inria.atlanmod.dag.Vertex;
import fr.obeo.emf.specimen.DirectWriteSpecimenGenerator;
import fr.obeo.emf.specimen.ISpecimenConfiguration;
import fr.obeo.emf.specimen.internal.EPackagesData;

/**
 * @author <a href="mailto:amine.benelallma@inria.fr">Amine Benelallam</a>
 */
public class DagGenerator extends DirectWriteSpecimenGenerator{


	protected ListMultimap<EClass, String> indexByKind = ArrayListMultimap.create();
	int verticesSize;
	private EObject rootObject;
	private final DagPackage dagPck = DagPackage.eINSTANCE;

	public DagGenerator() {
		super();
	}
	public DagGenerator (ISpecimenConfiguration configuration, long seed) {
		LOGGER = Logger.getLogger(DagGenerator.class.getName());
		this.configuration = configuration;
		ePackagesData = new EPackagesData(configuration.ePackages(), configuration.ignoredEClasses());
		generator = new Random(seed);
	}

	public void generate(Resource resource) {
		
		resource.setModified(true);

		ImmutableSet<EClass> possibleRootEClasses = configuration.possibleRootEClasses();

		currentDepth = 0;
		currentMaxDepth = 0;
		currentObjects = 0;
		
		goalObjects = configuration.getResourceSizeDistribution().sample();

		LOGGER.info("Generating vertices");
		
		EClass eClass = configuration.getNextRootEClass(possibleRootEClasses);
		currentMaxDepth = configuration.getDepthDistributionFor(eClass).sample();
		generateRootEObject(eClass, indexByKind);
		resource.getContents().add(rootObject);
		
		LOGGER.info("Generating edges");

		int totalEObjects = currentObjects;
		int currentEObject = 0;
		TreeIterator<EObject> eAllVertices = rootObject.eAllContents();
		for (int i =0; i< verticesSize; i++) {
				if (currentObjects == goalObjects) break; 
			LOGGER.fine(MessageFormat.format("Generating edges {0} / {1}", currentEObject, totalEObjects));
			EObject eObject = eAllVertices.next();
			generateCrossReferencesForVertex(eObject, indexByKind);
		}

		LOGGER.info(MessageFormat.format("Requested #EObject={0}", goalObjects));
		
		LOGGER.info(MessageFormat.format("Actual #EObject={0}", ImmutableSet.copyOf(indexByKind.values()).size()));

		for (Map.Entry<EClass, Collection<String>> entry : indexByKind.asMap().entrySet()) {
			// Log number of elements for resolved EClasses
			EClass eClazz = entry.getKey();
			if (!eClazz.eIsProxy() || (eClazz.eIsProxy() && EcoreUtil.resolve(eClazz, resource) != eClazz)) {
				LOGGER.info(MessageFormat.format("#{0}::{1}={2}", 
						eClazz.getEPackage().getNsURI(),
						eClazz.getName(),
						entry.getValue().size()));
			}
		}
		
		for (Map.Entry<EClass, Collection<String>> entry : indexByKind.asMap().entrySet()) {
			EClass eClazz = entry.getKey();
			if (eClazz.eIsProxy() && EcoreUtil.resolve(eClazz, resource) == eClazz) {
				// Warn about unresolved EClasses
				LOGGER.warning(MessageFormat.format("#{0} (unresolved)={1}", 
						EcoreUtil.getURI(eClazz),
						entry.getValue().size()));
			}
		}

		LOGGER.info(MessageFormat.format("Generation finished for resource ''{0}''", resource.getURI()));
	}


	private Optional<EObject> generateRootEObject(EClass eClass,
			ListMultimap<EClass, String> indexByKind2) {
		final EObject eObject;
		currentObjects++;
		LOGGER.fine(MessageFormat.format("Generating EObject {0} / ~{1} (EClass={2})", 
				currentObjects, goalObjects, eClass.getName()));
		
		eObject = createEObject(eClass, indexByKind);
		rootObject = eObject;
		generateEAttributes(eObject, eClass);
		generateVertices();
		return Optional.fromNullable(eObject);
	}
	@SuppressWarnings("unchecked")
	protected void generateCrossReferencesForVertex(EObject eObject, ListMultimap<EClass, String> indexByKind) {
		
			EReference eReference = dagPck.getVertex_Outgoing();
		
			//EClass eReferenceType = eReference.getEReferenceType();
			IntegerDistribution distribution = configuration.getDistributionFor(eReference);
	
				List<Object> values = (List<Object>) eObject.eGet(eReference);
				int sample = distribution.sample();	
				LOGGER.fine(MessageFormat.format("Generating {0} values for EReference ''{1}'' in EObject {2}", sample, eReference.getName(), eObject.toString()));
				for (int i = 0; i < Math.min(sample, verticesSize - i ); i++) {
					// check the edge does not already exist
					EObject nextEObject = null;
					int count = verticesSize-i;
					while (count-- > 0) {// limiting max iteration to the number of remaining elements 
						 nextEObject = ((List<EObject>) 
								rootObject.eGet(dagPck.getDAG_Vertices())).get(generator.nextInt(verticesSize-i) + i);
						 if (! existingEdge(eObject, nextEObject)) break;
						 
					}
						 if (nextEObject != null &&
								 !nextEObject.equals(eObject)) {
							Optional<EObject> edge = generateEdge(eObject, nextEObject);
							if (edge.isPresent()) {
								values.add(edge.get());
								((List<EObject>) rootObject.eGet(dagPck.getDAG_Edges())).add(edge.get());
							}
						}
					}
	}
	
	@SuppressWarnings("unchecked")
	private boolean existingEdge(EObject eObject, EObject nextEObject) {
		for ( EObject e : (List<EObject>) eObject.eGet(dagPck.getVertex_Outgoing())) {
			if (e.eGet(dagPck.getEdge_To()).equals(nextEObject))
				return true;
		}
		return false;
	}
	private Optional<EObject> generateEdge(EObject eObject, EObject nextEObject) {
		Optional<EObject> result = generateEObject(dagPck.getEdge(), indexByKind);
		
		if (result.isPresent()) {
			Edge edge = (Edge)result.get();
			edge.setFrom((Vertex) eObject);
			edge.setTo((Vertex) nextEObject);
		}
		return result;
	}


	protected Optional<EObject> generateEObject(EClass eClass, ListMultimap<EClass, String> indexByKind) {
		final EObject eObject;
		currentObjects++;
		LOGGER.fine(MessageFormat.format("Generating EObject {0} / ~{1} (EClass={2})", 
				currentObjects, goalObjects, eClass.getName()));
		eObject = createEObject(eClass, indexByKind);
		generateEAttributes(eObject, eClass);
		//generateEContainmentReferences(eObject, eClass, indexByKind);
		return Optional.fromNullable(eObject);
	}

	/**
	 * @param eObject
	 * @param eClass
	 * @param indexByKind
	 */
	protected void generateVertices() {
			// generate vertices 
			generateEContainmentReference(rootObject, dagPck.getDAG_Vertices(), indexByKind);	

		}

	/**
	 * @param eObject
	 * @param eReference
	 * @param indexByKind
	 */
	protected void generateEContainmentReference(EObject eObject, EReference eReference,
			ListMultimap<EClass, String> indexByKind) {


		ImmutableList<EClass> eAllConcreteSubTypeOrSelf = ePackagesData.eAllConcreteSubTypeOrSelf(eReference);
		ImmutableMultiset<EClass> eAllConcreteSubTypesOrSelf = getEReferenceTypesWithWeight(eReference,
				eAllConcreteSubTypeOrSelf);
		
		generateManyContainmentReference(eObject, eReference, indexByKind, eAllConcreteSubTypesOrSelf);

	}

	protected void generateManyContainmentReference(EObject eObject, EReference eReference,
			ListMultimap<EClass, String> indexByKind, ImmutableMultiset<EClass> eAllConcreteSubTypesOrSelf) {
		IntegerDistribution distribution = configuration.getDistributionFor(eReference);
		@SuppressWarnings("unchecked")
		List<EObject> values = (List<EObject>) eObject.eGet(eReference);
		verticesSize = distribution.sample();
		LOGGER.fine(MessageFormat.format("Generating {0} values for EReference ''{1}'' in EObject {2}", verticesSize, eReference.getName(), eObject.toString()));
		for (int i = 0; i < verticesSize; i++) {
			int idx = generator.nextInt(eAllConcreteSubTypesOrSelf.size());
			final Optional<EObject> nextEObject = generateEObject(get(eAllConcreteSubTypesOrSelf, idx), indexByKind);
			if (nextEObject.isPresent()) {
				values.add(nextEObject.get());
			}
		}
	}



//	protected boolean areCodependent(String trgUri, String srcUri) {
//		
//		return trgUri.compareTo(srcUri) > 0;
////		if (ancestors.get(srcUri).size() == 1) {
////			return false;
////		} 
////		
////		if (ancestors.containsEntry(srcUri, trgUri))
////				return true;
////		for (String newTrg : ancestors.get(srcUri)){
////			if (areCodependent(newTrg, trgUri));
////				return true;
////		}
////		
////		return false;
//	}
}
