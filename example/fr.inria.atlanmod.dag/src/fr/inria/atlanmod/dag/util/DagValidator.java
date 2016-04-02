package fr.inria.atlanmod.dag.util;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.map.HashedMap;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

import fr.inria.atlanmod.dag.DAG;
import fr.inria.atlanmod.dag.DagPackage;
import fr.inria.atlanmod.dag.Edge;
import fr.inria.atlanmod.dag.Vertex;
import fr.inria.atlanmod.dag.impl.DagPackageImpl;
import fr.inria.atlanmod.neoemf.core.NeoEMFResource;
import fr.inria.atlanmod.neoemf.core.impl.NeoEMFResourceFactoryImpl;
import fr.inria.atlanmod.neoemf.util.NeoEMFURI;

public class DagValidator {

	private static final ResourceSetImpl rs = new ResourceSetImpl();
	private static final String resourceURI = "neoemfhbase://localhost:2181/dag/model0_gen0_1000";
	
	
	{
		Resource.Factory.Registry.INSTANCE.getProtocolToFactoryMap()
				.put(NeoEMFURI.NEOEMF_HBASE_SCHEME, new NeoEMFResourceFactoryImpl());
	}
	
	public static boolean validate (DAG dag) {
		Set<Vertex> visited = new HashSet<Vertex>();  
		for (Vertex v : dag.getVertices()) {	
			if (isCycle(v, visited, v.getOutgoing()) ) 
					return false;
		}
		return true;
	}

	private static boolean isCycle(Vertex v, Set<Vertex> visited,
			EList<Edge> outgoing) {
		// TODO implement a traversal looking for cycles
		return false;
	}
 
	public static void main(String[] args) {
		{
			Resource.Factory.Registry.INSTANCE.getProtocolToFactoryMap()
				.put(NeoEMFURI.NEOEMF_HBASE_SCHEME, new NeoEMFResourceFactoryImpl());
		
			DagPackageImpl.init();
		}
		Map<Object,Object> readOnlyOptions = new HashedMap<Object, Object>();
		readOnlyOptions.put(NeoEMFResource.OPTIONS_HBASE_READ_ONLY, true);
		Resource inResource = rs.createResource(URI.createURI(resourceURI));
		
		
		System.out.println("Start validating DAG");
		
		DagValidator.validate((DAG)(inResource.getContents().get(0)));
			
		System.out.println("Finish Validating DAG");
	}
}
