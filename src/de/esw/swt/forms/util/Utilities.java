/*
 * Copyright (c) 2002-2007 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 *  o Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer. 
 *     
 *  o Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution. 
 *     
 *  o Neither the name of JGoodies Karsten Lentzsch nor the names of 
 *    its contributors may be used to endorse or promote products derived 
 *    from this software without specific prior written permission. 
 *     
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, 
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR 
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR 
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; 
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE 
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, 
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */

package de.esw.swt.forms.util;

/**
 * Consists only of static utility methods.
 * 
 * This class may be merged with the FormLayoutUtils extra - or not. *
 * 
 * @author Karsten Lentzsch
 * @version $Revision: 1.4 $
 */
public final class Utilities {

	// Instance *************************************************************

	private Utilities() {
		// Suppresses default constructor, ensuring non-instantiability.
	}

	/**
	 * Lazily checks and answers whether the Aqua look&amp;feel is active.
	 * 
	 * @return true if the current look&amp;feel is Aqua
	 */
	public static boolean isLafAqua() {
		if (cachedIsLafAqua == null) {
			cachedIsLafAqua = Boolean.valueOf(computeIsLafAqua());
		}
		return cachedIsLafAqua.booleanValue();
	}

	// Caching and Lazily Computing the Laf State *****************************

	/**
	 * Holds the cached result of the Aqua l&amp;f check. Is invalidated by the
	 * <code>LookAndFeelChangeHandler</code> if the look&amp;feel changes.
	 */
	private static Boolean cachedIsLafAqua;

	/**
	 * Computes and answers whether an Aqua look&amp;feel is active. This may be
	 * Apple's Aqua L&amp;f, or a sub-L&amp;f that uses the same ID, because it
	 * doesn't substantially change the look.
	 * 
	 * @return true if the current look&amp;feel is Aqua
	 */
	private static boolean computeIsLafAqua() {
		// TODO: Check for aqua/macos
		return false;
		// return UIManager.getLookAndFeel().getID().equals("Aqua");
	}

}
