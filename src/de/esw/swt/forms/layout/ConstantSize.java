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
import java.util.Locale;

import org.eclipse.swt.widgets.Composite;

/**
 * An implementation of the {@link Size} interface that represents constant
 * sizes described by a value and unit, for example: 10&nbsp;pixel,
 * 15&nbsp;point or 4&nbsp;dialog units. You can get instances of
 * <code>ConstantSize</code> using the factory methods and constants in the
 * {@link Sizes} class. Logical constant sizes that vary with the current layout
 * style are delivered by the {@link de.esw.swt.forms.util.LayoutStyle} class.
 * <p>
 * 
 * This class supports different size units: <table>
 * <tr>
 * <td><b>Unit</b>&nbsp; </td>
 * <td>&nbsp;<b>Abbreviation</b>&nbsp;</td>
 * <td>&nbsp; <b>Size</b></td>
 * </tr>
 * <tr>
 * <td>Millimeter</td>
 * <td>mm</td>
 * <td>0.1 cm</td>
 * </tr>
 * <tr>
 * <td>Centimeter</td>
 * <td>cm</td>
 * <td>10.0 mm</td>
 * </tr>
 * <tr>
 * <td>Inch</td>
 * <td>in</td>
 * <td>25.4 mm</td>
 * </tr>
 * <tr>
 * <td>DTP Point</td>
 * <td>pt</td>
 * <td>1/72 in</td>
 * </tr>
 * <tr>
 * <td>Pixel</td>
 * <td>px</td>
 * <td>1/(resolution in dpi) in</td>
 * </tr>
 * <tr>
 * <td>Dialog Unit</td>
 * <td>dlu</td>
 * <td>honors l&amp;f, resolution, and dialog font size</td>
 * </tr>
 * </table>
 * <p>
 * 
 * <strong>Examples:</strong>
 * 
 * <pre>
 * Sizes.ZERO;
 * Sizes.DLUX9;
 * Sizes.dluX(42); 
 * Sizes.pixel(99);
 * </pre>
 * 
 * @author Karsten Lentzsch
 * @version $Revision: 1.7 $
 * 
 * @see Size
 * @see Sizes
 */
public final class ConstantSize implements Size, Serializable {

	// Public Units *********************************************************

	/**
	 * 
	 */
	private static final long serialVersionUID = -7891611904067188355L;

	/** */
	public static final Unit PIXEL = new Unit("Pixel", "px", true);

	/** */
	public static final Unit POINT = new Unit("Point", "pt", true);

	/** */
	public static final Unit DIALOG_UNITS_X = new Unit("Dialog units X",
			"dluX", true);

	/** */
	public static final Unit DLUX = DIALOG_UNITS_X;

	/** */
	public static final Unit DIALOG_UNITS_Y = new Unit("Dialog units Y",
			"dluY", true);

	/** */
	public static final Unit DLUY = DIALOG_UNITS_Y;

	/** */
	public static final Unit MILLIMETER = new Unit("Millimeter", "mm", false);

	/** */
	public static final Unit MM = MILLIMETER;

	/** */
	public static final Unit CENTIMETER = new Unit("Centimeter", "cm", false);

	/** */
	public static final Unit CM = CENTIMETER;

	/** */
	public static final Unit INCH = new Unit("Inch", "in", false);

	/** */
	public static final Unit IN = INCH;

	/**
	 * An array of all enumeration values used to canonicalize deserialized
	 * units.
	 */
	static final Unit[] VALUES = { PIXEL, POINT, DIALOG_UNITS_X,
			DIALOG_UNITS_Y, MILLIMETER, CENTIMETER, INCH };

	// Fields ***************************************************************

	private final double value;

	private final Unit unit;

	// Instance Creation ****************************************************

	/**
	 * Constructs a ConstantSize for the given size and unit.
	 * 
	 * @param value
	 *            the size value interpreted in the given units
	 * @param unit
	 *            the size's unit
	 * 
	 * @since 1.1
	 */
	public ConstantSize(final int value, final Unit unit) {
		this.value = value;
		this.unit = unit;
	}

	/**
	 * Constructs a ConstantSize for the given size and unit.
	 * 
	 * @param value
	 *            the size value interpreted in the given units
	 * @param unit
	 *            the size's unit
	 * 
	 * @since 1.1
	 */
	public ConstantSize(final double value, final Unit unit) {
		this.value = value;
		this.unit = unit;
	}

	/**
	 * Creates and returns a ConstantSize from the given encoded size and unit
	 * description.
	 * 
	 * @param encodedValueAndUnit
	 *            the size's value and unit as string
	 * @param horizontal
	 *            true for horizontal, false for vertical
	 * @return a constant size for the given encoding and unit description
	 * 
	 * @throws IllegalArgumentException
	 *             if the unit requires integer but the value is not an integer
	 */
	static ConstantSize valueOf(final String encodedValueAndUnit,
			final boolean horizontal) {
		final String split[] = ConstantSize
				.splitValueAndUnit(encodedValueAndUnit);
		final String encodedValue = split[0];
		final String encodedUnit = split[1];
		final Unit unit = Unit.valueOf(encodedUnit, horizontal);
		final double value = Double.parseDouble(encodedValue);
		if (unit.requiresIntegers) {
			if (value != (int) value) {
				throw new IllegalArgumentException(unit.toString() + " value "
						+ encodedValue + " must be an integer.");
			}
		}
		return new ConstantSize(value, unit);
	}

	/**
	 * Creates and returns a ConstantSize for the specified size value in
	 * horizontal dialog units.
	 * 
	 * @param value
	 *            size value in horizontal dialog units
	 * @return the associated Size instance
	 */
	static ConstantSize dluX(final int value) {
		return new ConstantSize(value, DLUX);
	}

	/**
	 * Creates and returns a ConstantSize for the specified size value in
	 * vertical dialog units.
	 * 
	 * @param value
	 *            size value in vertical dialog units
	 * @return the associated Size instance
	 */
	static ConstantSize dluY(final int value) {
		return new ConstantSize(value, DLUY);
	}

	// Accessors ************************************************************

	/**
	 * Returns this size's value.
	 * 
	 * @return the size value
	 * 
	 * @since 1.1
	 */
	public double getValue() {
		return this.value;
	}

	/**
	 * Returns this size's unit.
	 * 
	 * @return the size unit
	 * 
	 * @since 1.1
	 */
	public Unit getUnit() {
		return this.unit;
	}

	// Accessing the Value **************************************************

	/**
	 * Converts the size if necessary and returns the value in pixels.
	 * 
	 * @param composite
	 *            the associated component
	 * @return the size in pixels
	 */
	public int getPixelSize(final Composite composite) {
		if (this.unit == PIXEL) {
			return intValue();
		} else if (this.unit == POINT) {
			return Sizes.pointAsPixel(intValue(), composite);
		} else if (this.unit == INCH) {
			return Sizes.inchAsPixel(this.value, composite);
		} else if (this.unit == MILLIMETER) {
			return Sizes.millimeterAsPixel(this.value, composite);
		} else if (this.unit == CENTIMETER) {
			return Sizes.centimeterAsPixel(this.value, composite);
		} else if (this.unit == DIALOG_UNITS_X) {
			return Sizes.dialogUnitXAsPixel(intValue(), composite);
		} else if (this.unit == DIALOG_UNITS_Y) {
			return Sizes.dialogUnitYAsPixel(intValue(), composite);
		} else {
			throw new IllegalStateException("Invalid unit " + this.unit);
		}
	}

	// Implementing the Size Interface **************************************

	/**
	 * Returns this size as pixel size. Neither requires the component list nor
	 * the specified measures.
	 * <p>
	 * 
	 * Invoked by {@link de.esw.swt.forms.layout.FormSpec} to determine the size
	 * of a column or row.
	 * 
	 * @param composite
	 *            the layout container
	 * @param components
	 *            the list of components used to compute the size
	 * @param minMeasure
	 *            the measure that determines the minimum sizes
	 * @param prefMeasure
	 *            the measure that determines the preferred sizes
	 * @param defaultMeasure
	 *            the measure that determines the default sizes
	 * @return the computed maximum size in pixel
	 */
	public int maximumSize(final Composite composite, final List components,
			final FormLayout.Measure minMeasure,
			final FormLayout.Measure prefMeasure,
			final FormLayout.Measure defaultMeasure) {
		return getPixelSize(composite);
	}

	/**
	 * Describes if this Size can be compressed, if container space gets scarce.
	 * Used by the FormLayout size computations in <code>#compressedSizes</code>
	 * to check whether a column or row can be compressed or not.
	 * <p>
	 * 
	 * ConstantSizes are incompressible.
	 * 
	 * @return <code>false</code>
	 * 
	 * @since 1.1
	 */
	public boolean compressible() {
		return false;
	}

	// Overriding Object Behavior *******************************************

	/**
	 * Indicates whether some other ConstantSize is "equal to" this one.
	 * 
	 * @param o
	 *            the Object with which to compare
	 * @return <code>true</code> if this object is the same as the obj
	 *         argument; <code>false</code> otherwise.
	 * 
	 * @see java.lang.Object#hashCode()
	 * @see java.util.Hashtable
	 */
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof ConstantSize)) {
			return false;
		}
		final ConstantSize size = (ConstantSize) o;
		return this.value == size.value && this.unit == size.unit;
	}

	/**
	 * Returns a hash code value for the object. This method is supported for
	 * the benefit of hashtables such as those provided by
	 * <code>java.util.Hashtable</code>.
	 * 
	 * @return a hash code value for this object.
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 * @see java.util.Hashtable
	 */
	public int hashCode() {
		return new Double(this.value).hashCode() + 37 * this.unit.hashCode();
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
		return this.value == intValue() ? Integer.toString(intValue())
				+ this.unit.abbreviation() : Double.toString(this.value)
				+ this.unit.abbreviation();
	}

	// Helper Code **********************************************************

	private int intValue() {
		return (int) Math.round(this.value);
	}

	/**
	 * Splits a string that encodes size with unit into the size and unit
	 * substrings. Returns an array of two strings.
	 * 
	 * @param encodedValueAndUnit
	 *            a strings that represents a size with unit
	 * @return the first element is size, the second is unit
	 */
	static String[] splitValueAndUnit(final String encodedValueAndUnit) {
		final String[] result = new String[2];
		final int len = encodedValueAndUnit.length();
		int firstLetterIndex = len;
		while (firstLetterIndex > 0
				&& Character.isLetter(encodedValueAndUnit
						.charAt(firstLetterIndex - 1))) {
			firstLetterIndex--;
		}
		result[0] = encodedValueAndUnit.substring(0, firstLetterIndex);
		result[1] = encodedValueAndUnit.substring(firstLetterIndex);
		return result;
	}

	// Helper Class *********************************************************

	/**
	 * An ordinal-based serializable typesafe enumeration for units as used in
	 * instances of {@link ConstantSize}.
	 */
	public static final class Unit implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 7668962919768388021L;

		private final transient String name;

		private final transient String abbreviation;

		final transient boolean requiresIntegers;

		Unit(final String name, final String abbreviation,
				final boolean requiresIntegers) {
			this.name = name;
			this.abbreviation = abbreviation;
			this.requiresIntegers = requiresIntegers;
		}

		/**
		 * Returns a Unit that corresponds to the specified string.
		 * 
		 * @param str
		 *            the encoded unit
		 * @param horizontal
		 *            true for a horizontal unit, false for vertical
		 * @return the corresponding Unit
		 * @throws IllegalArgumentException
		 *             if no Unit exists for the string
		 */
		static Unit valueOf(final String str, final boolean horizontal) {
			final String lowerCase = str.toLowerCase(Locale.ENGLISH);
			if (lowerCase.equals("px") || lowerCase.length() == 0) {
				return PIXEL;
			} else if (lowerCase.equals("dlu")) {
				return horizontal ? DIALOG_UNITS_X : DIALOG_UNITS_Y;
			} else if (lowerCase.equals("pt")) {
				return POINT;
			} else if (lowerCase.equals("in")) {
				return INCH;
			} else if (lowerCase.equals("mm")) {
				return MILLIMETER;
			} else if (lowerCase.equals("cm")) {
				return CENTIMETER;
			} else {
				throw new IllegalArgumentException("Invalid unit name '" + str
						+ "'. Must be one of: " + "px, dlu, pt, mm, cm, in");
			}
		}

		/**
		 * @see java.lang.Object#toString()
		 */
		public String toString() {
			return this.name;
		}

		/**
		 * Returns the first character of this Unit's name. Used to identify it
		 * in short format strings.
		 * 
		 * @return the first character of this Unit's name.
		 */
		public String abbreviation() {
			return this.abbreviation;
		}

		// Serialization *****************************************************

		private static int nextOrdinal = 0;

		private final int ordinal = nextOrdinal++;

		private Object readResolve() {
			return VALUES[this.ordinal]; // Canonicalize
		}

	}

}