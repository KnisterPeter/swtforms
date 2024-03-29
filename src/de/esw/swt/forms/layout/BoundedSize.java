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

package de.esw.swt.forms.layout;

import java.io.Serializable;
import java.util.List;

import org.eclipse.swt.widgets.Composite;

/**
 * Describes sizes that provide lower and upper bounds as used by the JGoodies
 * FormLayout.
 * 
 * @author Karsten Lentzsch
 * @version $Revision: 1.9 $
 * 
 * @see Sizes
 * @see ConstantSize
 */
public final class BoundedSize implements Size, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7010868083642771406L;

	/**
	 * Holds the base size.
	 */
	private final Size basis;

	/**
	 * Holds an optional lower bound.
	 */
	private Size lowerBound;

	/**
	 * Holds an optional upper bound.
	 */
	private Size upperBound;

	// Instance Creation ****************************************************

	/**
	 * Constructs a BoundedSize for the given basis using the specified lower
	 * and upper bounds.
	 * <p>
	 * 
	 * TODO: Consider throwing an IllegalArgumentException, if the lower bound
	 * and upper bound are both <code>null</code>.
	 * 
	 * @param basis
	 *            the base size
	 * @param lowerBound
	 *            the lower bound size
	 * @param upperBound
	 *            the upper bound size
	 * 
	 * @throws NullPointerException
	 *             if the basis is null
	 * 
	 * @since 1.1
	 */
	public BoundedSize(final Size basis, final Size lowerBound, final Size upperBound) {
		if (basis == null) {
			throw new NullPointerException(
					"The basis of a bounded size must not be null.");
		}
		this.basis = basis;
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
	}

	// Accessors ************************************************************

	/**
	 * Returns the base size, which is not-<code>null</code>.
	 * 
	 * @return the base size
	 * 
	 * @since 1.1
	 */
	public Size getBasis() {
		return this.basis;
	}

	/**
	 * Returns the optional lower bound.
	 * 
	 * @return the optional lower bound
	 * 
	 * @since 1.1
	 */
	public Size getLowerBound() {
		return this.lowerBound;
	}

	/**
	 * Returns the optional upper bound.
	 * 
	 * @return the optional upper bound
	 * 
	 * @since 1.1
	 */
	public Size getUpperBound() {
		return this.upperBound;
	}

	// Implementation of the Size Interface *********************************

	/**
	 * Returns this size as pixel size. Neither requires the component list nor
	 * the specified measures. Honors the lower and upper bound.
	 * <p>
	 * 
	 * Invoked by <code>FormSpec</code> to determine the size of a column or
	 * row.
	 * 
	 * @param composite
	 *            the layout container
	 * @param components
	 *            the list of components to measure
	 * @param minMeasure
	 *            the measure used to determine the minimum size
	 * @param prefMeasure
	 *            the measure used to determine the preferred size
	 * @param defaultMeasure
	 *            the measure used to determine the default size
	 * @return the maximum size in pixels
	 * @see FormSpec#maximumSize(Composite, List, FormLayout.Measure,
	 *      FormLayout.Measure, FormLayout.Measure)
	 */
	public int maximumSize(final Composite composite, final List components,
			final FormLayout.Measure minMeasure, final FormLayout.Measure prefMeasure,
			final FormLayout.Measure defaultMeasure) {
		int size = this.basis.maximumSize(composite, components, minMeasure,
				prefMeasure, defaultMeasure);
		if (this.lowerBound != null) {
			size = Math.max(size, this.lowerBound.maximumSize(composite,
					components, minMeasure, prefMeasure, defaultMeasure));
		}
		if (this.upperBound != null) {
			size = Math.min(size, this.upperBound.maximumSize(composite,
					components, minMeasure, prefMeasure, defaultMeasure));
		}
		return size;
	}

	/**
	 * Describes if this Size can be compressed, if container space gets scarce.
	 * Used by the FormLayout size computations in <code>#compressedSizes</code>
	 * to check whether a column or row can be compressed or not.
	 * <p>
	 * 
	 * BoundedSizes are compressible if the base Size is compressible.
	 * 
	 * @return <code>true</code> if and only if the basis is compressible
	 * 
	 * @since 1.1
	 */
	public boolean compressible() {
		return getBasis().compressible();
	}

	// Overriding Object Behavior *******************************************

	/**
	 * Indicates whether some other BoundedSize is "equal to" this one.
	 * 
	 * @param object
	 *            the object with which to compare
	 * @return <code>true</code> if this object is the same as the object
	 *         argument, <code>false</code> otherwise.
	 * @see Object#hashCode()
	 * @see java.util.Hashtable
	 */
	public boolean equals(final Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof BoundedSize)) {
			return false;
		}
		final BoundedSize size = (BoundedSize) object;
		return this.basis.equals(size.basis)
				&& (this.lowerBound == null && size.lowerBound == null || this.lowerBound != null && this.lowerBound
						.equals(size.lowerBound))
				&& (this.upperBound == null && size.upperBound == null || this.upperBound != null && this.upperBound
						.equals(size.upperBound));
	}

	/**
	 * Returns a hash code value for the object. This method is supported for
	 * the benefit of hashtables such as those provided by
	 * <code>java.util.Hashtable</code>.
	 * 
	 * @return a hash code value for this object.
	 * @see Object#equals(Object)
	 * @see java.util.Hashtable
	 */
	public int hashCode() {
		int hashValue = this.basis.hashCode();
		if (this.lowerBound != null) {
			hashValue = hashValue * 37 + this.lowerBound.hashCode();
		}
		if (this.upperBound != null) {
			hashValue = hashValue * 37 + this.upperBound.hashCode();
		}
		return hashValue;
	}

	/**
	 * Returns a string representation of this size object.
	 * 
	 * <strong>Note:</strong> The string representation may change at any time.
	 * It is strongly recommended to not use this string for parsing purposes.
	 * 
	 * @return a string representation of the constant size
	 */
	public String toString() {
		if (this.lowerBound != null) {
			return this.upperBound == null ? "max(" + this.basis + ';'
					+ this.lowerBound + ')' : "max(" + this.lowerBound + ';'
					+ "min(" + this.basis + ';' + this.upperBound + "))";
		} else if (this.upperBound != null) {
			return "min(" + this.basis + ';' + this.upperBound + ')';
		} else {
			return "bounded(" + this.basis + ')';
		}
	}

}