/**
 */
package fr.inria.atlanmod.dag.impl;

import fr.inria.atlanmod.dag.DagPackage;
import fr.inria.atlanmod.dag.Edge;
import fr.inria.atlanmod.dag.Vertex;
import fr.inria.atlanmod.neoemf.core.impl.NeoEMFEObjectImpl;
import org.eclipse.emf.ecore.EClass;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Edge</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link fr.inria.atlanmod.dag.impl.EdgeImpl#getId <em>Id</em>}</li>
 *   <li>{@link fr.inria.atlanmod.dag.impl.EdgeImpl#getFrom <em>From</em>}</li>
 *   <li>{@link fr.inria.atlanmod.dag.impl.EdgeImpl#getTo <em>To</em>}</li>
 * </ul>
 *
 * @generated
 */
public class EdgeImpl extends NeoEMFEObjectImpl implements Edge {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EdgeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DagPackage.Literals.EDGE;
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
		return (String)eGet(DagPackage.Literals.EDGE__ID, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setId(String newId) {
		eSet(DagPackage.Literals.EDGE__ID, newId);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Vertex getFrom() {
		return (Vertex)eGet(DagPackage.Literals.EDGE__FROM, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFrom(Vertex newFrom) {
		eSet(DagPackage.Literals.EDGE__FROM, newFrom);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Vertex getTo() {
		return (Vertex)eGet(DagPackage.Literals.EDGE__TO, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTo(Vertex newTo) {
		eSet(DagPackage.Literals.EDGE__TO, newTo);
	}

} //EdgeImpl
