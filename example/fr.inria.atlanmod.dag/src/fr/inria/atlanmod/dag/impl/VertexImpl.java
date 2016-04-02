/**
 */
package fr.inria.atlanmod.dag.impl;

import fr.inria.atlanmod.dag.DagPackage;
import fr.inria.atlanmod.dag.Edge;
import fr.inria.atlanmod.dag.Vertex;

import fr.inria.atlanmod.neoemf.core.impl.NeoEMFEObjectImpl;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Vertex</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link fr.inria.atlanmod.dag.impl.VertexImpl#getId <em>Id</em>}</li>
 *   <li>{@link fr.inria.atlanmod.dag.impl.VertexImpl#getOutgoing <em>Outgoing</em>}</li>
 *   <li>{@link fr.inria.atlanmod.dag.impl.VertexImpl#getIncoming <em>Incoming</em>}</li>
 * </ul>
 *
 * @generated
 */
public class VertexImpl extends NeoEMFEObjectImpl implements Vertex {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected VertexImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DagPackage.Literals.VERTEX;
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
	public String getId() {
		return (String)eGet(DagPackage.Literals.VERTEX__ID, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setId(String newId) {
		eSet(DagPackage.Literals.VERTEX__ID, newId);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	public EList<Edge> getOutgoing() {
		return (EList<Edge>)eGet(DagPackage.Literals.VERTEX__OUTGOING, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	public EList<Edge> getIncoming() {
		return (EList<Edge>)eGet(DagPackage.Literals.VERTEX__INCOMING, true);
	}

} //VertexImpl
