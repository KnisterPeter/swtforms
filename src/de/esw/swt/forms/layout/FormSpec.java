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
import java.util.StringTokenizer;

import org.eclipse.swt.widgets.Composite;

/**
 * An abstract class that specifies columns and rows in FormLayout by their
 * default alignment, start size and resizing behavior. API users will use the
 * subclasses {@link ColumnSpec} and {@link RowSpec}.
 * 
 * @author Karsten Lentzsch
 * @version $Revision: 1.4 $
 * 
 * @see ColumnSpec
 * @see RowSpec
 * @see FormLayout
 * @see CellConstraints
 */
public abstract class FormSpec implements Serializable {

	// Horizontal and Vertical Default Alignments ***************************

	/**
	 * By default put components in the left.
	 */
	static final DefaultAlignment LEFT_ALIGN = new DefaultAlignment("left");

	/**
	 * By default put components in the right.
	 */
	static final DefaultAlignment RIGHT_ALIGN = new DefaultAlignment("right");

	/**
	 * By default put the components in the top.
	 */
	static final DefaultAlignment TOP_ALIGN = new DefaultAlignment("top");

	/**
	 * By default put the components in the bottom.
	 */
	static final DefaultAlignment BOTTOM_ALIGN = new DefaultAlignment("bottom");

	/**
	 * By default put the components in the center.
	 */
	static final DefaultAlignment CENTER_ALIGN = new DefaultAlignment("center");

	/**
	 * By default fill the column or row.
	 */
	static final DefaultAlignment FILL_ALIGN = new DefaultAlignment("fill");

	/**
	 * An array of all enumeration values used to canonicalize deserialized
	 * default alignments.
	 */
	static final DefaultAlignment[] VALUES = { LEFT_ALIGN, RIGHT_ALIGN,
			TOP_ALIGN, BOTTOM_ALIGN, CENTER_ALIGN, FILL_ALIGN };

	// Resizing Weights *****************************************************

	/**
	 * Gives a column or row a fixed size.
	 */
	public static final double NO_GROW = 0.0d;

	/**
	 * The default resize weight.
	 */
	public static final double DEFAULT_GROW = 1.0d;

	// Fields ***************************************************************

	/**
	 * Holds the default alignment that will be used if a cell does not override
	 * this default.
	 */
	private DefaultAlignment defaultAlignment;

	/**
	 * Holds the size that describes how to size this column or row.
	 */
	private Size size;

	/**
	 * Holds the resize weight; is 0 if not used.
	 */
	private double resizeWeight;

	// Instance Creation ****************************************************

	/**
	 * Constructs a <code>FormSpec</code> for the given default alignment,
	 * size, and resize weight. The resize weight must be a non-negative double;
	 * you can use <code>NONE</code> as a convenience value for no resize.
	 * 
	 * @param defaultAlignment
	 *            the spec's default alignment
	 * @param size
	 *            a constant, component or bounded size
	 * @param resizeWeight
	 *            the spec resize weight
	 * @throws IllegalArgumentException
	 *             if the resize weight is negative
	 */
	protected FormSpec(final DefaultAlignment defaultAlignment,
			final Size size, final double resizeWeight) {
		this.defaultAlignment = defaultAlignment;
		this.size = size;
		this.resizeWeight = resizeWeight;
		if (resizeWeight < 0) {
			throw new IllegalArgumentException(
					"The resize weight must be non-negative.");
		}
	}

	/**
	 * Constructs a <code>FormSpec</code> from the specified encoded
	 * description. The description will be parsed to set initial values.
	 * 
	 * @param defaultAlignment
	 *            the default alignment
	 * @param encodedDescription
	 *            the encoded description
	 */
	protected FormSpec(final DefaultAlignment defaultAlignment,
			final String encodedDescription) {
		this(defaultAlignment, Sizes.DEFAULT, NO_GROW);
		parseAndInitValues(encodedDescription.toLowerCase(Locale.ENGLISH));
	}

	// Public API ***********************************************************

	/**
	 * Returns the default alignment.
	 * 
	 * @return the default alignment
	 */
	public final DefaultAlignment getDefaultAlignment() {
		return this.defaultAlignment;
	}

	/**
	 * Returns the size.
	 * 
	 * @return the size
	 */
	public final Size getSize() {
		return this.size;
	}

	/**
	 * Returns the current resize weight.
	 * 
	 * @return the resize weight.
	 */
	public final double getResizeWeight() {
		return this.resizeWeight;
	}

	/**
	 * Checks and answers whether this spec can grow or not. That is the case if
	 * and only if the resize weight is != <code>NO_GROW</code>.
	 * 
	 * @return true if it can grow, false if it can't grow
	 */
	final boolean canGrow() {
		return getResizeWeight() != NO_GROW;
	}

	// Parsing **************************************************************

	/**
	 * Parses an encoded form spec and initializes all required fields. The
	 * encoded description must be in lower case.
	 * 
	 * @param encodedDescription
	 *            the FormSpec in an encoded format
	 * @throws IllegalArgumentException
	 *             if the string is empty, has no size, or is otherwise invalid
	 */
	private void parseAndInitValues(final String encodedDescription) {
		final StringTokenizer tokenizer = new StringTokenizer(
				encodedDescription, ":");
		if (!tokenizer.hasMoreTokens()) {
			throw new IllegalArgumentException(
					"The form spec must not be empty.");
		}
		String token = tokenizer.nextToken();

		// Check if the first token is an orientation.
		final DefaultAlignment alignment = DefaultAlignment.valueOf(token,
				isHorizontal());
		if (alignment != null) {
			this.defaultAlignment = alignment;
			if (!tokenizer.hasMoreTokens()) {
				throw new IllegalArgumentException(
						"The form spec must provide a size.");
			}
			token = tokenizer.nextToken();
		}

		parseAndInitSize(token);

		if (tokenizer.hasMoreTokens()) {
			this.resizeWeight = decodeResize(tokenizer.nextToken());
		}
	}

	/**
	 * Parses an encoded size spec and initializes the size fields.
	 * 
	 * @param token
	 *            a token that represents a size, either bounded or plain
	 */
	private void parseAndInitSize(final String token) {
		if (token.startsWith("max(") && token.endsWith(")")) {
			this.size = parseAndInitBoundedSize(token, false);
			return;
		}
		if (token.startsWith("min(") && token.endsWith(")")) {
			this.size = parseAndInitBoundedSize(token, true);
			return;
		}
		this.size = decodeAtomicSize(token);
	}

	/**
	 * Parses an encoded compound size and sets the size fields. The compound
	 * size has format: max(&lt;atomic size&gt;;&lt;atomic size2&gt;) |
	 * min(&lt;atomic size1&gt;;&lt;atomic size2&gt;) One of the two atomic
	 * sizes must be a logical size, the other must be a size constant.
	 * 
	 * @param token
	 *            a token for a bounded size, e.g. "max(50dlu; pref)"
	 * @param setMax
	 *            if true we set a maximum size, otherwise a minimum size
	 * @return a Size that represents the parse result
	 */
	private Size parseAndInitBoundedSize(final String token,
			final boolean setMax) {
		final int semicolonIndex = token.indexOf(';');
		final String sizeToken1 = token.substring(4, semicolonIndex);
		final String sizeToken2 = token.substring(semicolonIndex + 1, token
				.length() - 1);

		final Size size1 = decodeAtomicSize(sizeToken1);
		final Size size2 = decodeAtomicSize(sizeToken2);

		// Check valid combinations and set min or max.
		if (size1 instanceof ConstantSize) {
			if (size2 instanceof Sizes.ComponentSize) {
				return new BoundedSize(size2, setMax ? null : size1,
						setMax ? size1 : null);
			}
			throw new IllegalArgumentException(
					"Bounded sizes must not be both constants.");
		}
		if (size2 instanceof ConstantSize) {
			return new BoundedSize(size1, setMax ? null : size2, setMax ? size2
					: null);
		}
		throw new IllegalArgumentException(
				"Bounded sizes must not be both logical.");
	}

	/**
	 * Decodes and returns an atomic size that is either a constant size or a
	 * component size.
	 * 
	 * @param token
	 *            the encoded size
	 * @return the decoded size either a constant or component size
	 */
	private Size decodeAtomicSize(final String token) {
		final Sizes.ComponentSize componentSize = Sizes.ComponentSize
				.valueOf(token);
		if (componentSize != null) {
			return componentSize;
		}
		return ConstantSize.valueOf(token, isHorizontal());
	}

	/**
	 * Decodes an encoded resize mode and resize weight and answers the resize
	 * weight.
	 * 
	 * @param token
	 *            the encoded resize weight
	 * @return the decoded resize weight
	 * @throws IllegalArgumentException
	 *             if the string description is an invalid string representation
	 */
	private double decodeResize(final String token) {
		if (token.equals("g") || token.equals("grow")) {
			return DEFAULT_GROW;
		}
		if (token.equals("n") || token.equals("nogrow") || token.equals("none")) {
			return NO_GROW;
		}
		// Must have format: grow(<double>)
		if ((token.startsWith("grow(") || token.startsWith("g("))
				&& token.endsWith(")")) {
			final int leftParen = token.indexOf('(');
			final int rightParen = token.indexOf(')');
			final String substring = token.substring(leftParen + 1, rightParen);
			return Double.parseDouble(substring);
		}
		throw new IllegalArgumentException(
				"The resize argument '"
						+ token
						+ "' is invalid. "
						+ " Must be one of: grow, g, none, n, grow(<double>), g(<double>)");
	}

	// Misc *****************************************************************

	/**
	 * Returns a string representation of this form specification. The string
	 * representation consists of three elements separated by a colon (<tt>":"</tt>),
	 * first the alignment, second the size, and third the resize spec.
	 * <p>
	 * 
	 * This method does <em>not</em> return a decoded version of this object;
	 * the contrary is the case. Many instances will return a string that cannot
	 * be parsed.
	 * <p>
	 * 
	 * <strong>Note:</strong> The string representation may change at any time.
	 * It is strongly recommended to not use this string for parsing purposes.
	 * 
	 * @return a string representation of the form specification.
	 */
	public final String toString() {
		final StringBuffer buffer = new StringBuffer();
		buffer.append(this.defaultAlignment);

		buffer.append(":");
		buffer.append(this.size.toString());
		buffer.append(':');
		if (this.resizeWeight == NO_GROW) {
			buffer.append("noGrow");
		} else if (this.resizeWeight == DEFAULT_GROW) {
			buffer.append("grow");
		} else {
			buffer.append("grow(");
			buffer.append(this.resizeWeight);
			buffer.append(')');
		}
		return buffer.toString();
	}

	/**
	 * Returns a string representation of this form specification. The string
	 * representation consists of three elements separated by a colon (<tt>":"</tt>),
	 * first the alignment, second the size, and third the resize spec.
	 * <p>
	 * 
	 * This method does <em>not</em> return a decoded version of this object;
	 * the contrary is the case. Many instances will return a string that cannot
	 * be parsed.
	 * <p>
	 * 
	 * <strong>Note:</strong> The string representation may change at any time.
	 * It is strongly recommended to not use this string for parsing purposes.
	 * 
	 * @return a string representation of the form specification.
	 */
	public final String toShortString() {
		final StringBuffer buffer = new StringBuffer();
		buffer.append(this.defaultAlignment.abbreviation());

		buffer.append(":");
		buffer.append(this.size.toString());
		buffer.append(':');
		if (this.resizeWeight == NO_GROW) {
			buffer.append("n");
		} else if (this.resizeWeight == DEFAULT_GROW) {
			buffer.append("g");
		} else {
			buffer.append("g(");
			buffer.append(this.resizeWeight);
			buffer.append(')');
		}
		return buffer.toString();
	}

	// Abstract Behavior ****************************************************

	/**
	 * Returns if this is a horizontal specification (vs. vertical). Used to
	 * distinct between horizontal and vertical dialog units, which have
	 * different conversion factors.
	 * 
	 * @return true for horizontal, false for vertical
	 */
	abstract boolean isHorizontal();

	// Helper Code **********************************************************

	/**
	 * Computes the maximum size for the given list of components, using this
	 * form spec and the specified measure.
	 * <p>
	 * 
	 * Invoked by FormLayout to determine the size of one of my elements
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
	 */
	final int maximumSize(final Composite composite, final List components,
			final FormLayout.Measure minMeasure,
			final FormLayout.Measure prefMeasure,
			final FormLayout.Measure defaultMeasure) {
		return this.size.maximumSize(composite, components, minMeasure,
				prefMeasure, defaultMeasure);
	}

	/**
	 * An ordinal-based serializable typesafe enumeration for the column and row
	 * default alignment types.
	 */
	public static final class DefaultAlignment implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = -4590398816089537409L;

		private final transient String name;

		DefaultAlignment(final String name) {
			this.name = name;
		}

		/**
		 * Returns a DefaultAlignment that corresponds to the specified string,
		 * null if no such aignment exists.
		 * 
		 * @param str
		 *            the encoded alignment
		 * @param isHorizontal
		 *            indicates the values orientation
		 * @return the corresponding DefaultAlignment or null
		 */
		static DefaultAlignment valueOf(final String str,
				final boolean isHorizontal) {
			if (str.equals("f") || str.equals("fill")) {
				return FILL_ALIGN;
			} else if (str.equals("c") || str.equals("center")) {
				return CENTER_ALIGN;
			} else if (isHorizontal) {
				if (str.equals("r") || str.equals("right")) {
					return RIGHT_ALIGN;
				} else if (str.equals("l") || str.equals("left")) {
					return LEFT_ALIGN;
				} else {
					return null;
				}
			} else {
				if (str.equals("t") || str.equals("top")) {
					return TOP_ALIGN;
				} else if (str.equals("b") || str.equals("bottom")) {
					return BOTTOM_ALIGN;
				} else {
					return null;
				}
			}
		}

		/**
		 * Returns this Alignment's name.
		 * 
		 * @return this alignment's name.
		 */
		public String toString() {
			return this.name;
		}

		/**
		 * Returns the first character of this Alignment's name. Used to
		 * identify it in short format strings.
		 * 
		 * @return the name's first character.
		 */
		public char abbreviation() {
			return this.name.charAt(0);
		}

		// Serialization *****************************************************

		private static int nextOrdinal = 0;

		private final int ordinal = nextOrdinal++;

		private Object readResolve() {
			return VALUES[this.ordinal]; // Canonicalize
		}

	}

}
