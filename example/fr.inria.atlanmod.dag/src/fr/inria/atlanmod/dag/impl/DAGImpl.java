/**
 */
package fr.inria.atlanmod.dag.impl;

import fr.inria.atlanmod.dag.DAG;
import fr.inria.atlanmod.dag.DagPackage;
import fr.inria.atlanmod.dag.Edge;
import fr.inria.atlanmod.dag.Vertex;

import fr.inria.atlanmod.neoemf.core.impl.NeoEMFEObjectImpl;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>DAG</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link fr.inria.atlanmod.dag.impl.DAGImpl#getVertices <em>Vertices</em>}</li>
 *   <li>{@link fr.inria.atlanmod.dag.impl.DAGImpl#getEdges <em>Edges</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DAGImpl extends NeoEMFEObjectImpl implements DAG {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DAGImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DagPackage.Literals.DAG;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected int eStaticFeatureCount() {
		return 0;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	public EList<Vertex> getVertices() {
		return (EList<Vertex>)eGet(DagPackage.Literals.DAG__VERTICES, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	public EList<Edge> getEdges() {
		return (EList<Edge>)eGet(DagPackage.Literals.DAG__EDGES, true);
	}

} //DAGImpl
