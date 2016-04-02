/**
 */
package fr.inria.atlanmod.dag;

import fr.inria.atlanmod.neoemf.core.NeoEMFEObject;
import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Vertex</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link fr.inria.atlanmod.dag.Vertex#getId <em>Id</em>}</li>
 *   <li>{@link fr.inria.atlanmod.dag.Vertex#getOutgoing <em>Outgoing</em>}</li>
 *   <li>{@link fr.inria.atlanmod.dag.Vertex#getIncoming <em>Incoming</em>}</li>
 * </ul>
 *
 * @see fr.inria.atlanmod.dag.DagPackage#getVertex()
 * @model
 * @extends NeoEMFEObject
 * @generated
 */
public interface Vertex extends NeoEMFEObject {
	/**
	 * Returns the value of the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Id</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Id</em>' attribute.
	 * @see #setId(String)
	 * @see fr.inria.atlanmod.dag.DagPackage#getVertex_Id()
	 * @model required="true"
	 * @generated
	 */
	String getId();

	/**
	 * Sets the value of the '{@link fr.inria.atlanmod.dag.Vertex#getId <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Id</em>' attribute.
	 * @see #getId()
	 * @generated
	 */
	void setId(String value);

	/**
	 * Returns the value of the '<em><b>Outgoing</b></em>' reference list.
	 * The list contents are of type {@link fr.inria.atlanmod.dag.Edge}.
	 * It is bidirectional and its opposite is '{@link fr.inria.atlanmod.dag.Edge#getFrom <em>From</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Outgoing</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Outgoing</em>' reference list.
	 * @see fr.inria.atlanmod.dag.DagPackage#getVertex_Outgoing()
	 * @see fr.inria.atlanmod.dag.Edge#getFrom
	 * @model opposite="from"
	 * @generated
	 */
	EList<Edge> getOutgoing();

	/**
	 * Returns the value of the '<em><b>Incoming</b></em>' reference list.
	 * The list contents are of type {@link fr.inria.atlanmod.dag.Edge}.
	 * It is bidirectional and its opposite is '{@link fr.inria.atlanmod.dag.Edge#getTo <em>To</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Incoming</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Incoming</em>' reference list.
	 * @see fr.inria.atlanmod.dag.DagPackage#getVertex_Incoming()
	 * @see fr.inria.atlanmod.dag.Edge#getTo
	 * @model opposite="to"
	 * @generated
	 */
	EList<Edge> getIncoming();

} // Vertex
