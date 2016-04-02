/**
 */
package fr.inria.atlanmod.dag;

import fr.inria.atlanmod.neoemf.core.NeoEMFEObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Edge</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link fr.inria.atlanmod.dag.Edge#getId <em>Id</em>}</li>
 *   <li>{@link fr.inria.atlanmod.dag.Edge#getFrom <em>From</em>}</li>
 *   <li>{@link fr.inria.atlanmod.dag.Edge#getTo <em>To</em>}</li>
 * </ul>
 *
 * @see fr.inria.atlanmod.dag.DagPackage#getEdge()
 * @model
 * @extends NeoEMFEObject
 * @generated
 */
public interface Edge extends NeoEMFEObject {
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
	 * @see fr.inria.atlanmod.dag.DagPackage#getEdge_Id()
	 * @model required="true"
	 * @generated
	 */
	String getId();

	/**
	 * Sets the value of the '{@link fr.inria.atlanmod.dag.Edge#getId <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Id</em>' attribute.
	 * @see #getId()
	 * @generated
	 */
	void setId(String value);

	/**
	 * Returns the value of the '<em><b>From</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link fr.inria.atlanmod.dag.Vertex#getOutgoing <em>Outgoing</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>From</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>From</em>' reference.
	 * @see #setFrom(Vertex)
	 * @see fr.inria.atlanmod.dag.DagPackage#getEdge_From()
	 * @see fr.inria.atlanmod.dag.Vertex#getOutgoing
	 * @model opposite="outgoing" required="true"
	 * @generated
	 */
	Vertex getFrom();

	/**
	 * Sets the value of the '{@link fr.inria.atlanmod.dag.Edge#getFrom <em>From</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>From</em>' reference.
	 * @see #getFrom()
	 * @generated
	 */
	void setFrom(Vertex value);

	/**
	 * Returns the value of the '<em><b>To</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link fr.inria.atlanmod.dag.Vertex#getIncoming <em>Incoming</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>To</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>To</em>' reference.
	 * @see #setTo(Vertex)
	 * @see fr.inria.atlanmod.dag.DagPackage#getEdge_To()
	 * @see fr.inria.atlanmod.dag.Vertex#getIncoming
	 * @model opposite="incoming" required="true"
	 * @generated
	 */
	Vertex getTo();

	/**
	 * Sets the value of the '{@link fr.inria.atlanmod.dag.Edge#getTo <em>To</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>To</em>' reference.
	 * @see #getTo()
	 * @generated
	 */
	void setTo(Vertex value);

} // Edge
