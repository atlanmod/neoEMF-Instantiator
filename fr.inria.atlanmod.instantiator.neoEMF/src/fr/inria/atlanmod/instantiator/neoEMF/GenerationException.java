/*******************************************************************************
 * Copyright (c) 2015 Abel G�mez (AtlanMod) 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Abel G�mez (AtlanMod) - Additional modifications      
 *******************************************************************************/

package fr.inria.atlanmod.instantiator.neoEMF;


/**
 * Generation Exception
 * @author <a href="mailto:abel.gomez-llana@inria.fr">Abel G�mez</a>
 *
 */
public class GenerationException extends Exception {

	private static final long serialVersionUID = 1L;

	public GenerationException(String message) {
		super(message);
	}

	public GenerationException() {
		super();
	}

	public GenerationException(Exception e) {
		super(e.getMessage(), e.getCause());
	}

}
