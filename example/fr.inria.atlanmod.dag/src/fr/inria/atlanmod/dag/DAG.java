/**
 */
package fr.inria.atlanmod.dag;

import fr.inria.atlanmod.neoemf.core.NeoEMFEObject;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>DAG</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link fr.inria.atlanmod.dag.DAG#getVertices <em>Vertices</em>}</li>
 *   <li>{@link fr.inria.atlanmod.dag.DAG#getEdges <em>Edges</em>}</li>
 * </ul>
 *
 * @see fr.inria.atlanmod.dag.DagPackage#getDAG()
 * @model
 * @extends NeoEMFEObject
 * @generated
 */
public interface DAG extends NeoEMFEObject {
	/**
	 * Returns the value of the '<em><b>Vertices</b></em>' containment reference list.
	 * The list contents are of type {@link fr.inria.atlanmod.dag.Vertex}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vertices</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vertices</em>' containment reference list.
	 * @see fr.inria.atlanmod.dag.DagPackage#getDAG_Vertices()
	 * @model containment="true" required="true"
	 * @generated
	 */
	EList<Vertex> getVertices();

	/**
	 * Returns the value of the '<em><b>Edges</b></em>' containment reference list.
	 * The list contents are of type {@link fr.inria.atlanmod.dag.Edge}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Edges</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Edges</em>' containment reference list.
	 * @see fr.inria.atlanmod.dag.DagPackage#getDAG_Edges()
	 * @model containment="true"
	 * @generated
	 */
	EList<Edge> getEdges();

} // DAG
