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
import fr.inria.atlanmod.dag.impl.DagPackageImpl;
import fr.obeo.emf.specimen.DirectWriteSpecimenGenerator;
import fr.obeo.emf.specimen.ISpecimenConfiguration;
import fr.obeo.emf.specimen.internal.EPackagesData;

/**
 * @author <a href="mailto:amine.benelallma@inria.fr">Amine Benelallam</a>
 */
public class DagGenerator extends DirectWriteSpecimenGenerator{


	protected ListMultimap<EClass, String> indexByKind = ArrayListMultimap.create();
	int verticesSize;
	private final EClass edgeClass = DagPackageImpl.eINSTANCE.getEdge();
	private final EClass vertexClass = DagPackageImpl.eINSTANCE.getVertex();
	private EObject rootObject;
	private final DagPackage dagPck = DagPackage.eINSTANCE;
    /**
     * 
     */
	ListMultimap<String, String> edgesIndex = ArrayListMultimap.create();
    
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

		LOGGER.info(MessageFormat.format("Requested #All={0}", goalObjects));
		
		LOGGER.info(MessageFormat.format("Actual #Vertices={0}", ImmutableSet.copyOf(indexByKind.values()).size()));

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

		LOGGER.info(MessageFormat.format("Generation finished for resource ''{0}'' with size ''{1}''", resource.getURI(), goalObjects));
	}


	private Optional<EObject> generateRootEObject(EClass eClass,
			ListMultimap<EClass, String> indexByKind2) {
		final EObject eObject;
		currentObjects++;
		LOGGER.fine(MessageFormat.format("Generating EObject {0} / ~{1} (EClass={2})", 
				currentObjects, goalObjects, eClass.getName()));
		
		eObject = createEObject(eClass, indexByKind);
		rootObject = eObject;
		generateVertices(rootObject, dagPck.getDAG_Vertices(), indexByKind);
		return Optional.fromNullable(eObject);
	}
	@SuppressWarnings("unchecked")
	protected void generateCrossReferencesForVertex(EObject eObject, ListMultimap<EClass, String> indexByKind) {
		
			EReference eReference = dagPck.getVertex_Outgoing();
		
			//EClass eReferenceType = eReference.getEReferenceType();
			IntegerDistribution distribution = configuration.getDistributionFor(eReference);
	
				//List<Object> values = (List<Object>) eObject.eGet(eReference);
				int sample = distribution.sample();	
				LOGGER.fine(MessageFormat.format("Generating {0} values for EReference ''{1}'' in EObject {2}", sample, eReference.getName(), eObject.toString()));
				for (int i = 0; i < Math.min(sample, verticesSize - i ); i++) {
					// check the edge does not already exist
					IntegerDistribution distId = configuration.getDistributionFor(dagPck.getEdge_Id());
					EObject nextEObject = null;
					int count = verticesSize-i;
					while (count-- > 0) {// limiting max iteration to the number of remaining elements 
						 nextEObject = ((List<EObject>) 
								rootObject.eGet(dagPck.getDAG_Vertices())).get(generator.nextInt(verticesSize-i) + i);
						 if (! existingEdge((Vertex)eObject, (Vertex) nextEObject)) break;
						 
					}
						 if (nextEObject != null &&
								 !nextEObject.equals(eObject)) {
							    Optional<Edge> edge =  generateEdge(eObject, nextEObject,distId);
							    if (edge.isPresent()) {
							    	//values.add(edge.get());
									((List<EObject>) rootObject.eGet(dagPck.getDAG_Edges())).add(edge.get());	
							    }					
						}
					}
	}
	
	private boolean existingEdge(Vertex eObject, Vertex nextEObject) {	
		return edgesIndex.containsEntry(eObject.neoemfId(), nextEObject.neoemfId());
	}
	
	private Optional<Edge> generateEdge(EObject eObject, EObject nextEObject,IntegerDistribution distId) {
		
		//TODO optimize this, use the edges index instead
			LOGGER.info(MessageFormat.format("Generating EObject {0} / ~{1} (EClass={2})", 
				currentObjects, goalObjects, edgeClass.getName()));
			currentObjects++;
			Edge edge =  (Edge) edgeClass.getEPackage().getEFactoryInstance().create(edgeClass);
			
			generateSingleAttribute(edge, dagPck.getEdge_Id(), distId, dagPck.getEdge_Id().getEType().getInstanceClass());
			edge.setFrom((Vertex) eObject);
			edge.setTo((Vertex) nextEObject);
			edgesIndex.put(((Vertex) eObject).neoemfId(), ((Vertex) nextEObject).neoemfId());
		return Optional.fromNullable(edge);
	}


//	protected Optional<EObject> generateEdge(EClass eClass, ListMultimap<String, String> indexByKind) {
//		final EObject eObject;
//		currentObjects++;
//		eObject = edgeClass.getEPackage().getEFactoryInstance().create(edgeClass);
//		generateEAttributes(eObject, eClass);
//		//generateEContainmentReferences(eObject, eClass, indexByKind);
//		return Optional.fromNullable(eObject);
//	}

	/**
	 * @param eObject
	 * @param eReference
	 * @param indexByKind
	 */
	protected void generateVertices(EObject eObject, EReference eReference,
			ListMultimap<EClass, String> indexByKind) {


		ImmutableList<EClass> eAllConcreteSubTypeOrSelf = ePackagesData.eAllConcreteSubTypeOrSelf(eReference);
		ImmutableMultiset<EClass> eAllConcreteSubTypesOrSelf = getEReferenceTypesWithWeight(eReference,
				eAllConcreteSubTypeOrSelf);
		IntegerDistribution distribution = configuration.getDistributionFor(eReference);
		generateManyContainmentReference(eObject, eReference, indexByKind, eAllConcreteSubTypesOrSelf,distribution);

	}

	protected void generateManyContainmentReference(EObject eObject, EReference eReference,
		ListMultimap<EClass, String> indexByKind, ImmutableMultiset<EClass> eAllConcreteSubTypesOrSelf, IntegerDistribution distribution) {
		
		@SuppressWarnings("unchecked")
		List<EObject> values = (List<EObject>) eObject.eGet(eReference);
		verticesSize = distribution.sample();
		LOGGER.fine(MessageFormat.format("Generating {0} values for EReference ''{1}'' in EObject {2}", verticesSize, eReference.getName(), eObject.toString()));
		IntegerDistribution distributionId = configuration.getDistributionFor(dagPck.getVertex_Id());
		for (int i = 0; i < verticesSize; i++) {
			final Optional<EObject> nextEObject = generateVertex(vertexClass, indexByKind,distributionId);
			if (nextEObject.isPresent()) {
				values.add(nextEObject.get());
			}
		}
	}
	private Optional<EObject> generateVertex(EClass eClass,
			ListMultimap<EClass, String> indexByKind, IntegerDistribution distribtion) {
			final EObject eObject;
			currentObjects++;
			LOGGER.fine(MessageFormat.format("Generating EObject {0} / ~{1} (EClass={2})", 
					currentObjects, goalObjects, eClass.getName()));
			eObject = createEObject(eClass, indexByKind);
			generateSingleAttribute(eObject, dagPck.getVertex_Id(), distribtion, dagPck.getVertex_Id().getEType().getInstanceClass());
			return Optional.fromNullable(eObject);
		}
	}


