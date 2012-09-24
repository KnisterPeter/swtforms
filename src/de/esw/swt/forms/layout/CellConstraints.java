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
import java.util.Locale;
import java.util.StringTokenizer;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;

/**
 * Defines constraints for components that are layed out with the FormLayout.
 * Defines the components display area: grid&nbsp;x, grid&nbsp;y, grid width
 * (column span), grid height (row span), horizontal alignment and vertical
 * alignment.
 * <p>
 * 
 * Most methods return <em>this</em> object to enable method chaining.
 * <p>
 * 
 * You can set optional insets in a constructor. This is useful if you need to
 * use a pixel-size insets to align perceived component bounds with pixel data,
 * for example an icon. Anyway, this is rarely used. The insets don't affect the
 * size computation for columns and rows. I consider renaming the insets to
 * offsets to better indicate the motivation for this option.
 * <p>
 * 
 * <strong>Examples</strong>:<br>
 * The following cell constraints locate a component in the third column of the
 * fifth row; column and row span are 1; the component will be aligned with the
 * column's right-hand side and the row's bottom.
 * 
 * <pre>
 * CellConstraints cc = new CellConstraints();
 * cc.xy(3, 5);
 * cc.xy(3, 5, CellConstraints.RIGHT, CellConstraints.BOTTOM);
 * cc.xy(3, 5, &quot;right, bottom&quot;);
 * 
 * cc.xyw(3, 5, 1);
 * cc.xyw(3, 5, 1, CellConstraints.RIGHT, CellConstraints.BOTTOM);
 * cc.xyw(3, 5, 1, &quot;right, bottom&quot;);
 * 
 * cc.xywh(3, 5, 1, 1);
 * cc.xywh(3, 5, 1, 1, CellConstraints.RIGHT, CellConstraints.BOTTOM);
 * cc.xywh(3, 5, 1, 1, &quot;right, bottom&quot;);
 * </pre>
 * 
 * See also the examples in the {@link FormLayout} class comment.
 * <p>
 * 
 * <strong>Note:</strong> The new method sets <code>#rc</code>,
 * <code>#rcw</code>, and <code>#rchw</code> are experimental and maybe
 * removed from the 1.1 final.
 * <p>
 * 
 * TODO: Remove the above comment in the 1.1 final.
 * <p>
 * 
 * TODO: Explain in the JavaDocs that the insets are actually offsets. And
 * describe that these offsets are not taken into account when FormLayout
 * computes the column and row sizes.
 * <p>
 * 
 * TODO: Rename the inset to offsets.
 * <p>
 * 
 * TODO: In the Forms 1.0.x invisible components are not taken into account when
 * the FormLayout lays out the container. Add an optional setting for this on
 * both the container-level and component-level. So one can specify that
 * invisible components shall be taken into account, but may exclude individual
 * components. Or the other way round, exclude invisible components, and include
 * individual components. The API of both the FormLayout and CellConstraints
 * classes shall be extended to support this option. This feature is planned for
 * the Forms version 1.1 and is described in <a
 * href="https://forms.dev.java.net/issues/show_bug.cgi?id=28">issue #28</a> of
 * the Forms' issue tracker where you can track the progress.
 * 
 * @author Karsten Lentzsch
 * @version $Revision: 1.4 $
 */
public final class CellConstraints implements Cloneable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9116232598597092681L;

	// Alignment Constants *************************************************

	/*
	 * Implementation Note: Do not change the order of the following constants.
	 * The serialization of class Alignment is ordinal-based and relies on it.
	 */

	/**
	 * Use the column's or row's default alignment.
	 */
	public static final Alignment DEFAULT = new Alignment("default",
			Alignment.BOTH);

	/**
	 * Fill the cell either horizontally or vertically.
	 */
	public static final Alignment FILL = new Alignment("fill", Alignment.BOTH);

	/**
	 * Put the component in the left.
	 */
	public static final Alignment LEFT = new Alignment("left",
			Alignment.HORIZONTAL);

	/**
	 * Put the component in the right.
	 */
	public static final Alignment RIGHT = new Alignment("right",
			Alignment.HORIZONTAL);

	/**
	 * Put the component in the center.
	 */
	public static final Alignment CENTER = new Alignment("center",
			Alignment.BOTH);

	/**
	 * Put the component in the top.
	 */
	public static final Alignment TOP = new Alignment("top", Alignment.VERTICAL);

	/**
	 * Put the component in the bottom.
	 */
	public static final Alignment BOTTOM = new Alignment("bottom",
			Alignment.VERTICAL);

	/**
	 * An array of all enumeration values used to canonicalize deserialized
	 * alignments.
	 */
	static final Alignment[] VALUES = { DEFAULT, FILL, LEFT, RIGHT, CENTER,
			TOP, BOTTOM };

	/**
	 * A reusable <code>Insets</code> object to reduce object instantiation.
	 */
	private static final Rectangle EMPTY_INSETS = new Rectangle(0, 0, 0, 0);

	// Fields ***************************************************************

	/**
	 * Describes the component's horizontal grid origin (starts at 1).
	 */
	public int gridX;

	/**
	 * Describes the component's vertical grid origin (starts at 1).
	 */
	public int gridY;

	/**
	 * Describes the component's horizontal grid extend (number of cells).
	 */
	public int gridWidth;

	/**
	 * Describes the component's vertical grid extent (number of cells).
	 */
	public int gridHeight;

	/**
	 * Describes the component's horizontal alignment.
	 */
	public Alignment hAlign;

	/**
	 * Describes the component's vertical alignment.
	 */
	public Alignment vAlign;

	/**
	 * Describes the component's <code>Insets</code> in it's display area.
	 */
	public Rectangle insets;

	// Instance Creation ****************************************************

	/**
	 * Constructs a default instance of <code>CellConstraints</code>.
	 */
	public CellConstraints() {
		this(1, 1);
	}

	/**
	 * Constructs an instance of <code>CellConstraints</code> for the given
	 * cell position.
	 * <p>
	 * 
	 * <strong>Examples:</strong>
	 * 
	 * <pre>
	 * new CellConstraints(1, 3);
	 * new CellConstraints(1, 3);
	 * </pre>
	 * 
	 * @param gridX
	 *            the component's horizontal grid origin
	 * @param gridY
	 *            the component's vertical grid origin
	 */
	public CellConstraints(final int gridX, final int gridY) {
		this(gridX, gridY, 1, 1);
	}

	/**
	 * Constructs an instance of <code>CellConstraints</code> for the given
	 * cell position, anchor, and fill.
	 * <p>
	 * 
	 * <strong>Examples:</strong>
	 * 
	 * <pre>
	 * new CellConstraints(1, 3, CellConstraints.LEFT, CellConstraints.BOTTOM);
	 * new CellConstraints(1, 3, CellConstraints.CENTER, CellConstraints.FILL);
	 * </pre>
	 * 
	 * @param gridX
	 *            the component's horizontal grid origin
	 * @param gridY
	 *            the component's vertical grid origin
	 * @param hAlign
	 *            the component's horizontal alignment
	 * @param vAlign
	 *            the component's vertical alignment
	 */
	public CellConstraints(final int gridX, final int gridY,
			final Alignment hAlign, final Alignment vAlign) {
		this(gridX, gridY, 1, 1, hAlign, vAlign, EMPTY_INSETS);
	}

	/**
	 * Constructs an instance of <code>CellConstraints</code> for the given
	 * cell position and size.
	 * <p>
	 * 
	 * <strong>Examples:</strong>
	 * 
	 * <pre>
	 * new CellConstraints(1, 3, 2, 1);
	 * new CellConstraints(1, 3, 7, 3);
	 * </pre>
	 * 
	 * @param gridX
	 *            the component's horizontal grid origin
	 * @param gridY
	 *            the component's vertical grid origin
	 * @param gridWidth
	 *            the component's horizontal extent
	 * @param gridHeight
	 *            the component's vertical extent
	 */
	public CellConstraints(final int gridX, final int gridY,
			final int gridWidth, final int gridHeight) {
		this(gridX, gridY, gridWidth, gridHeight, DEFAULT, DEFAULT);
	}

	/**
	 * Constructs an instance of <code>CellConstraints</code> for the given
	 * cell position and size, anchor, and fill.
	 * <p>
	 * 
	 * <strong>Examples:</strong>
	 * 
	 * <pre>
	 * new CellConstraints(1, 3, 2, 1, CellConstraints.LEFT, CellConstraints.BOTTOM);
	 * new CellConstraints(1, 3, 7, 3, CellConstraints.CENTER, CellConstraints.FILL);
	 * </pre>
	 * 
	 * @param gridX
	 *            the component's horizontal grid origin
	 * @param gridY
	 *            the component's vertical grid origin
	 * @param gridWidth
	 *            the component's horizontal extent
	 * @param gridHeight
	 *            the component's vertical extent
	 * @param hAlign
	 *            the component's horizontal alignment
	 * @param vAlign
	 *            the component's vertical alignment
	 */
	public CellConstraints(final int gridX, final int gridY,
			final int gridWidth, final int gridHeight, final Alignment hAlign,
			final Alignment vAlign) {
		this(gridX, gridY, gridWidth, gridHeight, hAlign, vAlign, EMPTY_INSETS);
	}

	/**
	 * Constructs an instance of <code>CellConstraints</code> for the complete
	 * set of available properties.
	 * <p>
	 * 
	 * <strong>Examples:</strong>
	 * 
	 * <pre>
	 * new CellConstraints(1, 3, 2, 1, CellConstraints.LEFT, CellConstraints.BOTTOM,
	 * 		new Insets(0, 1, 0, 3));
	 * new CellConstraints(1, 3, 7, 3, CellConstraints.CENTER, CellConstraints.FILL,
	 * 		new Insets(0, 1, 0, 0));
	 * </pre>
	 * 
	 * @param gridX
	 *            the component's horizontal grid origin
	 * @param gridY
	 *            the component's vertical grid origin
	 * @param gridWidth
	 *            the component's horizontal extent
	 * @param gridHeight
	 *            the component's vertical extent
	 * @param hAlign
	 *            the component's horizontal alignment
	 * @param vAlign
	 *            the component's vertical alignment
	 * @param insets
	 *            the component's display area <code>Insets</code>
	 * @throws IndexOutOfBoundsException
	 *             if the grid origin or extent is negative
	 * @throws NullPointerException
	 *             if the horizontal or vertical alignment is null
	 * @throws IllegalArgumentException
	 *             if an alignment orientation is invalid
	 */
	public CellConstraints(final int gridX, final int gridY,
			final int gridWidth, final int gridHeight, final Alignment hAlign,
			final Alignment vAlign, final Rectangle insets) {
		this.gridX = gridX;
		this.gridY = gridY;
		this.gridWidth = gridWidth;
		this.gridHeight = gridHeight;
		this.hAlign = hAlign;
		this.vAlign = vAlign;
		this.insets = insets;
		if (gridX <= 0) {
			throw new IndexOutOfBoundsException(
					"The grid x must be a positive number.");
		}
		if (gridY <= 0) {
			throw new IndexOutOfBoundsException(
					"The grid y must be a positive number.");
		}
		if (gridWidth <= 0) {
			throw new IndexOutOfBoundsException(
					"The grid width must be a positive number.");
		}
		if (gridHeight <= 0) {
			throw new IndexOutOfBoundsException(
					"The grid height must be a positive number.");
		}
		if (hAlign == null) {
			throw new NullPointerException(
					"The horizontal alignment must not be null.");
		}
		if (vAlign == null) {
			throw new NullPointerException(
					"The vertical alignment must not be null.");
		}
		ensureValidOrientations(hAlign, vAlign);
	}

	/**
	 * Constructs an instance of <code>CellConstraints</code> from the given
	 * encoded string properties.
	 * <p>
	 * 
	 * <strong>Examples:</strong>
	 * 
	 * <pre>
	 * new CellConstraints(&quot;1, 3&quot;);
	 * new CellConstraints(&quot;1, 3, left, bottom&quot;);
	 * new CellConstraints(&quot;1, 3, 2, 1, left, bottom&quot;);
	 * new CellConstraints(&quot;1, 3, 2, 1, l, b&quot;);
	 * </pre>
	 * 
	 * @param encodedConstraints
	 *            the constraints encoded as string
	 */
	public CellConstraints(final String encodedConstraints) {
		this();
		initFromConstraints(encodedConstraints);
	}

	// Setters with Column-Row Order ******************************************

	/**
	 * Sets column and row origins; sets width and height to 1; uses the default
	 * alignments.
	 * <p>
	 * 
	 * <strong>Examples:</strong>
	 * 
	 * <pre>
	 * cc.xy(1, 1);
	 * cc.xy(1, 3);
	 * </pre>
	 * 
	 * @param col
	 *            the new column index
	 * @param row
	 *            the new row index
	 * @return this
	 */
	public CellConstraints xy(final int col, final int row) {
		return xywh(col, row, 1, 1);
	}

	/**
	 * Sets column and row origins; sets width and height to 1; decodes
	 * horizontal and vertical alignments from the given string.
	 * <p>
	 * 
	 * <strong>Examples:</strong>
	 * 
	 * <pre>
	 * cc.xy(1, 3, &quot;left, bottom&quot;);
	 * cc.xy(1, 3, &quot;l, b&quot;);
	 * cc.xy(1, 3, &quot;center, fill&quot;);
	 * cc.xy(1, 3, &quot;c, f&quot;);
	 * </pre>
	 * 
	 * @param col
	 *            the new column index
	 * @param row
	 *            the new row index
	 * @param encodedAlignments
	 *            describes the horizontal and vertical alignments
	 * @return this
	 * 
	 * @throws IllegalArgumentException
	 *             if an alignment orientation is invalid
	 */
	public CellConstraints xy(final int col, final int row,
			final String encodedAlignments) {
		return xywh(col, row, 1, 1, encodedAlignments);
	}

	/**
	 * Sets the column and row origins; sets width and height to 1; set
	 * horizontal and vertical alignment using the specified objects.
	 * <p>
	 * 
	 * <strong>Examples:</strong>
	 * 
	 * <pre>
	 * cc.xy(1, 3, CellConstraints.LEFT, CellConstraints.BOTTOM);
	 * cc.xy(1, 3, CellConstraints.CENTER, CellConstraints.FILL);
	 * </pre>
	 * 
	 * @param col
	 *            the new column index
	 * @param row
	 *            the new row index
	 * @param colAlign
	 *            horizontal component alignment
	 * @param rowAlign
	 *            vertical component alignment
	 * @return this
	 */
	public CellConstraints xy(final int col, final int row,
			final Alignment colAlign, final Alignment rowAlign) {
		return xywh(col, row, 1, 1, colAlign, rowAlign);
	}

	/**
	 * Sets the column, row, width, and height; uses a height (row span) of 1
	 * and the horizontal and vertical default alignments.
	 * <p>
	 * 
	 * <strong>Examples:</strong>
	 * 
	 * <pre>
	 * cc.xyw(1, 3, 7);
	 * cc.xyw(1, 3, 2);
	 * </pre>
	 * 
	 * @param col
	 *            the new column index
	 * @param row
	 *            the new row index
	 * @param colSpan
	 *            the column span or grid width
	 * @return this
	 */
	public CellConstraints xyw(final int col, final int row, final int colSpan) {
		return xywh(col, row, colSpan, 1, DEFAULT, DEFAULT);
	}

	/**
	 * Sets the column, row, width, and height; decodes the horizontal and
	 * vertical alignments from the given string. The row span (height) is set
	 * to 1.
	 * <p>
	 * 
	 * <strong>Examples:</strong>
	 * 
	 * <pre>
	 * cc.xyw(1, 3, 7, &quot;left, bottom&quot;);
	 * cc.xyw(1, 3, 7, &quot;l, b&quot;);
	 * cc.xyw(1, 3, 2, &quot;center, fill&quot;);
	 * cc.xyw(1, 3, 2, &quot;c, f&quot;);
	 * </pre>
	 * 
	 * @param col
	 *            the new column index
	 * @param row
	 *            the new row index
	 * @param colSpan
	 *            the column span or grid width
	 * @param encodedAlignments
	 *            describes the horizontal and vertical alignments
	 * @return this
	 * @throws IllegalArgumentException
	 *             if an alignment orientation is invalid
	 */
	public CellConstraints xyw(final int col, final int row, final int colSpan,
			final String encodedAlignments) {
		return xywh(col, row, colSpan, 1, encodedAlignments);
	}

	/**
	 * Sets the column, row, width, and height; sets the horizontal and vertical
	 * aligment using the specified alignment objects. The row span (height) is
	 * set to 1.
	 * <p>
	 * 
	 * <strong>Examples:</strong>
	 * 
	 * <pre>
	 * cc.xyw(1, 3, 2, CellConstraints.LEFT, CellConstraints.BOTTOM);
	 * cc.xyw(1, 3, 7, CellConstraints.CENTER, CellConstraints.FILL);
	 * </pre>
	 * 
	 * @param col
	 *            the new column index
	 * @param row
	 *            the new row index
	 * @param colSpan
	 *            the column span or grid width
	 * @param colAlign
	 *            horizontal component alignment
	 * @param rowAlign
	 *            vertical component alignment
	 * @return this
	 * @throws IllegalArgumentException
	 *             if an alignment orientation is invalid
	 */
	public CellConstraints xyw(final int col, final int row, final int colSpan,
			final Alignment colAlign, final Alignment rowAlign) {
		return xywh(col, row, colSpan, 1, colAlign, rowAlign);
	}

	/**
	 * Sets the column, row, width, and height; uses default alignments.
	 * <p>
	 * 
	 * <strong>Examples:</strong>
	 * 
	 * <pre>
	 * cc.xywh(1, 3, 2, 1);
	 * cc.xywh(1, 3, 7, 3);
	 * </pre>
	 * 
	 * @param col
	 *            the new column index
	 * @param row
	 *            the new row index
	 * @param colSpan
	 *            the column span or grid width
	 * @param rowSpan
	 *            the row span or grid height
	 * @return this
	 */
	public CellConstraints xywh(final int col, final int row,
			final int colSpan, final int rowSpan) {
		return xywh(col, row, colSpan, rowSpan, DEFAULT, DEFAULT);
	}

	/**
	 * Sets the column, row, width, and height; decodes the horizontal and
	 * vertical alignments from the given string.
	 * <p>
	 * 
	 * <strong>Examples:</strong>
	 * 
	 * <pre>
	 * cc.xywh(1, 3, 2, 1, &quot;left, bottom&quot;);
	 * cc.xywh(1, 3, 2, 1, &quot;l, b&quot;);
	 * cc.xywh(1, 3, 7, 3, &quot;center, fill&quot;);
	 * cc.xywh(1, 3, 7, 3, &quot;c, f&quot;);
	 * </pre>
	 * 
	 * @param col
	 *            the new column index
	 * @param row
	 *            the new row index
	 * @param colSpan
	 *            the column span or grid width
	 * @param rowSpan
	 *            the row span or grid height
	 * @param encodedAlignments
	 *            describes the horizontal and vertical alignments
	 * @return this
	 * @throws IllegalArgumentException
	 *             if an alignment orientation is invalid
	 */
	public CellConstraints xywh(final int col, final int row,
			final int colSpan, final int rowSpan, final String encodedAlignments) {
		final CellConstraints result = xywh(col, row, colSpan, rowSpan);
		result.setAlignments(encodedAlignments, true);
		return result;
	}

	/**
	 * Sets the column, row, width, and height; sets the horizontal and vertical
	 * aligment using the specified alignment objects.
	 * <p>
	 * 
	 * <strong>Examples:</strong>
	 * 
	 * <pre>
	 * cc.xywh(1, 3, 2, 1, CellConstraints.LEFT, CellConstraints.BOTTOM);
	 * cc.xywh(1, 3, 7, 3, CellConstraints.CENTER, CellConstraints.FILL);
	 * </pre>
	 * 
	 * @param col
	 *            the new column index
	 * @param row
	 *            the new row index
	 * @param colSpan
	 *            the column span or grid width
	 * @param rowSpan
	 *            the row span or grid height
	 * @param colAlign
	 *            horizontal component alignment
	 * @param rowAlign
	 *            vertical component alignment
	 * @return this
	 * @throws IllegalArgumentException
	 *             if an alignment orientation is invalid
	 */
	public CellConstraints xywh(final int col, final int row,
			final int colSpan, final int rowSpan, final Alignment colAlign,
			final Alignment rowAlign) {
		this.gridX = col;
		this.gridY = row;
		this.gridWidth = colSpan;
		this.gridHeight = rowSpan;
		this.hAlign = colAlign;
		this.vAlign = rowAlign;
		ensureValidOrientations(this.hAlign, this.vAlign);
		return this;
	}

	// Setters with Row-Column Order ******************************************

	/**
	 * Sets row and column origins; sets height and width to 1; uses the default
	 * alignments.
	 * <p>
	 * 
	 * <strong>Examples:</strong>
	 * 
	 * <pre>
	 * cc.rc(1, 1);
	 * cc.rc(3, 1);
	 * </pre>
	 * 
	 * @param row
	 *            the new row index
	 * @param col
	 *            the new column index
	 * @return this
	 * 
	 * @since 1.1
	 */
	public CellConstraints rc(final int row, final int col) {
		return rchw(row, col, 1, 1);
	}

	/**
	 * Sets row and column origins; sets height and width to 1; decodes vertical
	 * and horizontal alignments from the given string.
	 * <p>
	 * 
	 * <strong>Examples:</strong>
	 * 
	 * <pre>
	 * cc.rc(3, 1, &quot;bottom, left&quot;);
	 * cc.rc(3, 1, &quot;b, l&quot;);
	 * cc.rc(3, 1, &quot;fill, center&quot;);
	 * cc.rc(3, 1, &quot;f, c&quot;);
	 * </pre>
	 * 
	 * @param row
	 *            the new row index
	 * @param col
	 *            the new column index
	 * @param encodedAlignments
	 *            describes the vertical and horizontal alignments
	 * @return this
	 * 
	 * @throws IllegalArgumentException
	 *             if an alignment orientation is invalid
	 * 
	 * @since 1.1
	 */
	public CellConstraints rc(final int row, final int col,
			final String encodedAlignments) {
		return rchw(row, col, 1, 1, encodedAlignments);
	}

	/**
	 * Sets the row and column origins; sets width and height to 1; set
	 * horizontal and vertical alignment using the specified objects.
	 * <p>
	 * 
	 * <strong>Examples:</strong>
	 * 
	 * <pre>
	 * cc.rc(3, 1, CellConstraints.BOTTOM, CellConstraints.LEFT);
	 * cc.rc(3, 1, CellConstraints.FILL, CellConstraints.CENTER);
	 * </pre>
	 * 
	 * @param row
	 *            the new row index
	 * @param col
	 *            the new column index
	 * @param rowAlign
	 *            vertical component alignment
	 * @param colAlign
	 *            horizontal component alignment
	 * @return this
	 * 
	 * @since 1.1
	 */
	public CellConstraints rc(final int row, final int col,
			final Alignment rowAlign, final Alignment colAlign) {
		return rchw(row, col, 1, 1, rowAlign, colAlign);
	}

	/**
	 * Sets the row, column, height, and width; uses a height (row span) of 1
	 * and the vertical and horizontal default alignments.
	 * <p>
	 * 
	 * <strong>Examples:</strong>
	 * 
	 * <pre>
	 * cc.rcw(3, 1, 7);
	 * cc.rcw(3, 1, 2);
	 * </pre>
	 * 
	 * @param row
	 *            the new row index
	 * @param col
	 *            the new column index
	 * @param colSpan
	 *            the column span or grid width
	 * @return this
	 * 
	 * @since 1.1
	 */
	public CellConstraints rcw(final int row, final int col, final int colSpan) {
		return rchw(row, col, 1, colSpan, DEFAULT, DEFAULT);
	}

	/**
	 * Sets the row, column, height, and width; decodes the vertical and
	 * horizontal alignments from the given string. The row span (height) is set
	 * to 1.
	 * <p>
	 * 
	 * <strong>Examples:</strong>
	 * 
	 * <pre>
	 * cc.rcw(3, 1, 7, &quot;bottom, left&quot;);
	 * cc.rcw(3, 1, 7, &quot;b, l&quot;);
	 * cc.rcw(3, 1, 2, &quot;fill, center&quot;);
	 * cc.rcw(3, 1, 2, &quot;f, c&quot;);
	 * </pre>
	 * 
	 * @param row
	 *            the new row index
	 * @param col
	 *            the new column index
	 * @param colSpan
	 *            the column span or grid width
	 * @param encodedAlignments
	 *            describes the vertical and horizontal alignments
	 * @return this
	 * 
	 * @throws IllegalArgumentException
	 *             if an alignment orientation is invalid
	 * 
	 * @since 1.1
	 */
	public CellConstraints rcw(final int row, final int col, final int colSpan,
			final String encodedAlignments) {
		return rchw(row, col, 1, colSpan, encodedAlignments);
	}

	/**
	 * Sets the row, column, height, and width; sets the vertical and
	 * horizontalaligment using the specified alignment objects. The row span
	 * (height) is set to 1.
	 * <p>
	 * 
	 * <strong>Examples:</strong>
	 * 
	 * <pre>
	 * cc.rcw(3, 1, 2, CellConstraints.BOTTOM, CellConstraints.LEFT);
	 * cc.rcw(3, 1, 7, CellConstraints.FILL, CellConstraints.CENTER);
	 * </pre>
	 * 
	 * @param row
	 *            the new row index
	 * @param col
	 *            the new column index
	 * @param colSpan
	 *            the column span or grid width
	 * @param rowAlign
	 *            vertical component alignment
	 * @param colAlign
	 *            horizontal component alignment
	 * @return this
	 * 
	 * @throws IllegalArgumentException
	 *             if an alignment orientation is invalid
	 * 
	 * @since 1.1
	 */
	public CellConstraints rcw(final int row, final int col, final int colSpan,
			final Alignment rowAlign, final Alignment colAlign) {
		return rchw(row, col, 1, colSpan, rowAlign, colAlign);
	}

	/**
	 * Sets the row, column, height, and width; uses default alignments.
	 * <p>
	 * 
	 * <strong>Examples:</strong>
	 * 
	 * <pre>
	 * cc.rchw(1, 3, 2, 1);
	 * cc.rchw(1, 3, 7, 3);
	 * </pre>
	 * 
	 * @param row
	 *            the new row index
	 * @param col
	 *            the new column index
	 * @param rowSpan
	 *            the row span or grid height
	 * @param colSpan
	 *            the column span or grid width
	 * @return this
	 * 
	 * @since 1.1
	 */
	public CellConstraints rchw(final int row, final int col,
			final int rowSpan, final int colSpan) {
		return rchw(row, col, rowSpan, colSpan, DEFAULT, DEFAULT);
	}

	/**
	 * Sets the row, column, height, and width; decodes the vertical and
	 * horizontal alignments from the given string.
	 * <p>
	 * 
	 * <strong>Examples:</strong>
	 * 
	 * <pre>
	 * cc.rchw(3, 1, 1, 2, &quot;bottom, left&quot;);
	 * cc.rchw(3, 1, 1, 2, &quot;b, l&quot;);
	 * cc.rchw(3, 1, 3, 7, &quot;fill, center&quot;);
	 * cc.rchw(3, 1, 3, 7, &quot;f, c&quot;);
	 * </pre>
	 * 
	 * @param row
	 *            the new row index
	 * @param col
	 *            the new column index
	 * @param rowSpan
	 *            the row span or grid height
	 * @param colSpan
	 *            the column span or grid width
	 * @param encodedAlignments
	 *            describes the vertical and horizontal alignments
	 * @return this
	 * @throws IllegalArgumentException
	 *             if an alignment orientation is invalid
	 * 
	 * @since 1.1
	 */
	public CellConstraints rchw(final int row, final int col,
			final int rowSpan, final int colSpan, final String encodedAlignments) {
		final CellConstraints result = rchw(row, col, rowSpan, colSpan);
		result.setAlignments(encodedAlignments, false);
		return result;
	}

	/**
	 * Sets the row, column, height, and width; sets the vertical and horizontal
	 * aligment using the specified alignment objects.
	 * <p>
	 * 
	 * <strong>Examples:</strong>
	 * 
	 * <pre>
	 * cc.rchw(3, 1, 1, 2, CellConstraints.BOTTOM, CellConstraints.LEFT);
	 * cc.rchw(3, 1, 3, 7, CellConstraints.FILL, CellConstraints.CENTER);
	 * </pre>
	 * 
	 * @param row
	 *            the new row index
	 * @param col
	 *            the new column index
	 * @param rowSpan
	 *            the row span or grid height
	 * @param colSpan
	 *            the column span or grid width
	 * @param rowAlign
	 *            vertical component alignment
	 * @param colAlign
	 *            horizontal component alignment
	 * @return this
	 * 
	 * @throws IllegalArgumentException
	 *             if an alignment orientation is invalid
	 * 
	 * @since 1.1
	 */
	public CellConstraints rchw(final int row, final int col,
			final int rowSpan, final int colSpan, final Alignment rowAlign,
			final Alignment colAlign) {
		return xywh(col, row, colSpan, rowSpan, colAlign, rowAlign);
	}

	// Parsing and Decoding String Descriptions *****************************

	/**
	 * Decodes and returns the grid bounds and alignments for this constraints
	 * as an array of six integers. The string representation is a comma
	 * separated sequence, one of
	 * 
	 * <pre>
	 * &quot;x, y&quot;
	 * &quot;x, y, w, h&quot;
	 * &quot;x, y, hAlign, vAlign&quot;
	 * &quot;x, y, w, h, hAlign, vAlign&quot;
	 * </pre>
	 * 
	 * @param encodedConstraints
	 *            represents horizontal and vertical alignment
	 * 
	 * @throws IllegalArgumentException
	 *             if the encoded constraints do not follow the constraint
	 *             syntax
	 */
	private void initFromConstraints(final String encodedConstraints) {
		final StringTokenizer tokenizer = new StringTokenizer(
				encodedConstraints, " ,");
		final int argCount = tokenizer.countTokens();
		if (!(argCount == 2 || argCount == 4 || argCount == 6)) {
			throw new IllegalArgumentException(
					"You must provide 2, 4 or 6 arguments.");
		}

		Integer nextInt = decodeInt(tokenizer.nextToken());
		if (nextInt == null) {
			throw new IllegalArgumentException(
					"First cell constraint element must be a number.");
		}
		this.gridX = nextInt.intValue();
		if (this.gridX <= 0) {
			throw new IndexOutOfBoundsException(
					"The grid x must be a positive number.");
		}

		nextInt = decodeInt(tokenizer.nextToken());
		if (nextInt == null) {
			throw new IllegalArgumentException(
					"Second cell constraint element must be a number.");
		}
		this.gridY = nextInt.intValue();
		if (this.gridY <= 0) {
			throw new IndexOutOfBoundsException(
					"The grid y must be a positive number.");
		}

		if (!tokenizer.hasMoreTokens()) {
			return;
		}

		String token = tokenizer.nextToken();
		nextInt = decodeInt(token);
		if (nextInt != null) {
			// Case: "x, y, w, h" or
			// "x, y, w, h, hAlign, vAlign"
			this.gridWidth = nextInt.intValue();
			if (this.gridWidth <= 0) {
				throw new IndexOutOfBoundsException(
						"The grid width must be a positive number.");
			}
			nextInt = decodeInt(tokenizer.nextToken());
			if (nextInt == null) {
				throw new IllegalArgumentException(
						"Fourth cell constraint element must be like third.");
			}
			this.gridHeight = nextInt.intValue();
			if (this.gridHeight <= 0) {
				throw new IndexOutOfBoundsException(
						"The grid height must be a positive number.");
			}

			if (!tokenizer.hasMoreTokens()) {
				return;
			}
			token = tokenizer.nextToken();
		}

		this.hAlign = decodeAlignment(token);
		this.vAlign = decodeAlignment(tokenizer.nextToken());
		ensureValidOrientations(this.hAlign, this.vAlign);
	}

	/**
	 * Decodes a string description for the horizontal and vertical alignment
	 * and sets this CellConstraints' alignment values. If the boolean is
	 * <code>true</code> the horizontal alignment is the first token, and the
	 * vertical alignment is the second token. if the boolean is
	 * <code>false</code> the vertical alignment comes first.
	 * <p>
	 * 
	 * Valid horizontal aligmnents are: left, middle, right, default, and fill.
	 * Valid vertical alignments are: top, center, bottom, default, and fill.
	 * The anchor's string representation abbreviates the alignment: l, m, r, d,
	 * f, t, c, and b.
	 * <p>
	 * 
	 * Anchor examples: "m, c" is centered, "l, t" is northwest, "m, t" is
	 * north, "r, c" east. "m, d" is horizontally centered and uses the row's
	 * default alignment. "d, t" is on top of the cell and uses the column's
	 * default alignment.
	 * <p>
	 * 
	 * @param encodedAlignments
	 *            represents horizontal and vertical alignment
	 * @throws IllegalArgumentException
	 *             if an alignment orientation is invalid
	 */
	private void setAlignments(final String encodedAlignments,
			final boolean horizontalThenVertical) {
		final StringTokenizer tokenizer = new StringTokenizer(
				encodedAlignments, " ,");
		final Alignment first = decodeAlignment(tokenizer.nextToken());
		final Alignment second = decodeAlignment(tokenizer.nextToken());
		this.hAlign = horizontalThenVertical ? first : second;
		this.vAlign = horizontalThenVertical ? second : first;
		ensureValidOrientations(this.hAlign, this.vAlign);
	}

	/**
	 * Decodes an integer string representation and returns the associated
	 * Integer or null in case of an invalid number format.
	 * 
	 * @param token
	 *            the encoded integer
	 * @return the decoded Integer or null
	 */
	private Integer decodeInt(final String token) {
		try {
			return Integer.decode(token);
		} catch (final NumberFormatException e) {
			return null;
		}
	}

	/**
	 * Parses an alignment string description and returns the corresponding
	 * alignment value.
	 * 
	 * @param encodedAlignment
	 *            the encoded alignment
	 * @return the associated <code>Alignment</code> instance
	 */
	private Alignment decodeAlignment(final String encodedAlignment) {
		return Alignment.valueOf(encodedAlignment);
	}

	/**
	 * Checks and verifies that this constraints object has valid grid index
	 * values, i. e. the display area cells are inside the form's grid.
	 * 
	 * @param colCount
	 *            number of columns in the grid
	 * @param rowCount
	 *            number of rows in the grid
	 * @throws IndexOutOfBoundsException
	 *             if the display area described by this constraints object is
	 *             not inside the grid
	 */
	void ensureValidGridBounds(final int colCount, final int rowCount) {
		if (this.gridX <= 0) {
			throw new IndexOutOfBoundsException("The column index "
					+ this.gridX + " must be positive.");
		}
		if (this.gridX > colCount) {
			throw new IndexOutOfBoundsException("The column index "
					+ this.gridX + " must be less than or equal to " + colCount
					+ ".");
		}
		if (this.gridX + this.gridWidth - 1 > colCount) {
			throw new IndexOutOfBoundsException("The grid width "
					+ this.gridWidth + " must be less than or equal to "
					+ (colCount - this.gridX + 1) + ".");
		}
		if (this.gridY <= 0) {
			throw new IndexOutOfBoundsException("The row index " + this.gridY
					+ " must be positive.");
		}
		if (this.gridY > rowCount) {
			throw new IndexOutOfBoundsException("The row index " + this.gridY
					+ " must be less than or equal to " + rowCount + ".");
		}
		if (this.gridY + this.gridHeight - 1 > rowCount) {
			throw new IndexOutOfBoundsException("The grid height "
					+ this.gridHeight + " must be less than or equal to "
					+ (rowCount - this.gridY + 1) + ".");
		}
	}

	/**
	 * Checks and verifies that the horizontal alignment is a horizontal and the
	 * vertical alignment is vertical.
	 * 
	 * @param horizontalAlignment
	 *            the horizontal alignment
	 * @param verticalAlignment
	 *            the vertical alignment
	 * @throws IllegalArgumentException
	 *             if an alignment is invalid
	 */
	private void ensureValidOrientations(final Alignment horizontalAlignment,
			final Alignment verticalAlignment) {
		if (!horizontalAlignment.isHorizontal()) {
			throw new IllegalArgumentException(
					"The horizontal alignment must be one of: left, center, right, fill, default.");
		}
		if (!verticalAlignment.isVertical()) {
			throw new IllegalArgumentException(
					"The vertical alignment must be one of: top, center, botto, fill, default.");
		}
	}

	// Settings Component Bounds ********************************************

	/**
	 * Sets the component's bounds using the given component and cell bounds.
	 * 
	 * @param c
	 *            the component to set bounds
	 * @param layout
	 *            the FormLayout instance that computes the bounds
	 * @param cellBounds
	 *            the cell's bounds
	 * @param minWidthMeasure
	 *            measures the minimum width
	 * @param minHeightMeasure
	 *            measures the minimum height
	 * @param prefWidthMeasure
	 *            measures the preferred width
	 * @param prefHeightMeasure
	 *            measures the preferred height
	 */
	void setBounds(final Control c, final FormLayout layout,
			final Rectangle cellBounds,
			final FormLayout.Measure minWidthMeasure,
			final FormLayout.Measure minHeightMeasure,
			final FormLayout.Measure prefWidthMeasure,
			final FormLayout.Measure prefHeightMeasure) {
		final ColumnSpec colSpec = this.gridWidth == 1 ? layout
				.getColumnSpec(this.gridX) : null;
		final RowSpec rowSpec = this.gridHeight == 1 ? layout
				.getRowSpec(this.gridY) : null;
		final Alignment concreteHAlign = concreteAlignment(this.hAlign, colSpec);
		final Alignment concreteVAlign = concreteAlignment(this.vAlign, rowSpec);
		final Rectangle concreteInsets = this.insets != null ? this.insets
				: EMPTY_INSETS;
		final int cellX = cellBounds.x + concreteInsets.x;
		final int cellY = cellBounds.y + concreteInsets.y;
		final int cellW = cellBounds.width - concreteInsets.x
				- concreteInsets.width;
		final int cellH = cellBounds.height - concreteInsets.y
				- concreteInsets.height;
		final int compW = componentSize(c, colSpec, cellW, minWidthMeasure,
				prefWidthMeasure);
		final int compH = componentSize(c, rowSpec, cellH, minHeightMeasure,
				prefHeightMeasure);
		final int x = origin(concreteHAlign, cellX, cellW, compW);
		final int y = origin(concreteVAlign, cellY, cellH, compH);
		final int w = extent(concreteHAlign, cellW, compW);
		final int h = extent(concreteVAlign, cellH, compH);
		c.setBounds(x, y, w, h);
	}

	/**
	 * Computes and returns the concrete alignment. Takes into account the cell
	 * alignment and <i>the</i> <code>FormSpec</code> if applicable.
	 * <p>
	 * 
	 * If this constraints object doesn't belong to a single column or row, the
	 * <code>formSpec</code> parameter is <code>null</code>. In this case
	 * the cell alignment is answered, but <code>DEFAULT</code> is mapped to
	 * <code>FILL</code>.
	 * <p>
	 * 
	 * If the cell belongs to a single column or row, we use the cell alignment,
	 * unless it is <code>DEFAULT</code>, where the alignment is inherited
	 * from the column or row resp.
	 * 
	 * @param cellAlignment
	 *            this cell's aligment
	 * @param formSpec
	 *            the associated column or row specification
	 * @return the concrete alignment
	 */
	private Alignment concreteAlignment(final Alignment cellAlignment,
			final FormSpec formSpec) {
		return formSpec == null ? cellAlignment == DEFAULT ? FILL
				: cellAlignment : usedAlignment(cellAlignment, formSpec);
	}

	/**
	 * Returns the alignment used for a given form constraints object. The cell
	 * alignment overrides the column or row default, unless it is
	 * <code>DEFAULT</code>. In the latter case, we use the column or row
	 * alignment.
	 * 
	 * @param cellAlignment
	 *            this cell constraint's alignment
	 * @param formSpec
	 *            the associated column or row specification
	 * @return the alignment used
	 */
	private Alignment usedAlignment(final Alignment cellAlignment,
			final FormSpec formSpec) {
		if (cellAlignment != CellConstraints.DEFAULT) {
			// Cell alignments other than DEFAULT override col/row alignments
			return cellAlignment;
		}
		final FormSpec.DefaultAlignment defaultAlignment = formSpec
				.getDefaultAlignment();
		if (defaultAlignment == FormSpec.FILL_ALIGN) {
			return FILL;
		}
		if (defaultAlignment == ColumnSpec.LEFT) {
			return LEFT;
		} else if (defaultAlignment == FormSpec.CENTER_ALIGN) {
			return CENTER;
		} else if (defaultAlignment == ColumnSpec.RIGHT) {
			return RIGHT;
		} else if (defaultAlignment == RowSpec.TOP) {
			return TOP;
		} else {
			return BOTTOM;
		}
	}

	/**
	 * Computes and returns the pixel size of the given component using the
	 * given form specification, measures, and cell size.
	 * 
	 * @param control
	 *            the component to measure
	 * @param formSpec
	 *            the specification of the component's column/row
	 * @param minMeasure
	 *            the measure for the minimum size
	 * @param prefMeasure
	 *            the measure for the preferred size
	 * @param cellSize
	 *            the cell size
	 * @return the component size as measured or a constant
	 */
	private int componentSize(final Control control, final FormSpec formSpec,
			final int cellSize, final FormLayout.Measure minMeasure,
			final FormLayout.Measure prefMeasure) {
		if (formSpec == null) {
			return prefMeasure.sizeOf(control);
		} else if (formSpec.getSize() == Sizes.MINIMUM) {
			return minMeasure.sizeOf(control);
		} else if (formSpec.getSize() == Sizes.PREFERRED) {
			return prefMeasure.sizeOf(control);
		} else { // default mode
			return Math.min(cellSize, prefMeasure.sizeOf(control));
		}
	}

	/**
	 * Computes and returns the component's pixel origin.
	 * 
	 * @param alignment
	 *            the component's alignment
	 * @param cellOrigin
	 *            the origin of the display area
	 * @param cellSize
	 *            the extent of the display area
	 * @param componentSize
	 * @return the component's pixel origin
	 */
	private int origin(final Alignment alignment, final int cellOrigin,
			final int cellSize, final int componentSize) {
		if (alignment == RIGHT || alignment == BOTTOM) {
			return cellOrigin + cellSize - componentSize;
		} else if (alignment == CENTER) {
			return cellOrigin + (cellSize - componentSize) / 2;
		} else { // left, top, fill
			return cellOrigin;
		}
	}

	/**
	 * Returns the component's pixel extent.
	 * 
	 * @param alignment
	 *            the component's alignment
	 * @param cellSize
	 *            the size of the display area
	 * @param componentSize
	 *            the component's size
	 * @return the component's pixel extent
	 */
	private int extent(final Alignment alignment, final int cellSize,
			final int componentSize) {
		return alignment == FILL ? cellSize : componentSize;
	}

	// Misc *****************************************************************

	/**
	 * Creates a copy of this cell constraints object.
	 * 
	 * @return a copy of this cell constraints object
	 */
	public Object clone() {
		try {
			final CellConstraints c = (CellConstraints) super.clone();
			c.insets = new Rectangle(this.insets.x, this.insets.y,
					this.insets.width, this.insets.height);
			return c;
		} catch (final CloneNotSupportedException e) {
			// This shouldn't happen, since we are Cloneable.
			throw new InternalError();
		}
	}

	/**
	 * Constructs and returns a string representation of this constraints
	 * object.
	 * 
	 * @return string representation of this constraints object
	 */
	public String toString() {
		final StringBuffer buffer = new StringBuffer("CellConstraints");
		buffer.append("[x=");
		buffer.append(this.gridX);
		buffer.append("; y=");
		buffer.append(this.gridY);
		buffer.append("; w=");
		buffer.append(this.gridWidth);
		buffer.append("; h=");
		buffer.append(this.gridHeight);
		buffer.append("; hAlign=");
		buffer.append(this.hAlign);
		buffer.append("; vAlign=");
		buffer.append(this.vAlign);
		if (!EMPTY_INSETS.equals(this.insets)) {
			buffer.append("; insets=");
			buffer.append(this.insets);
		}

		buffer.append(']');
		return buffer.toString();
	}

	/**
	 * Returns a short string representation of this constraints object.
	 * 
	 * @return a short string representation of this constraints object
	 */
	public String toShortString() {
		return toShortString(null);
	}

	/**
	 * Returns a short string representation of this constraints object. This
	 * method can use the given <code>FormLayout</code> to display extra
	 * information how default alignments are mapped to concrete alignments.
	 * Therefore it asks the related column and row as specified by this
	 * constraints object.
	 * 
	 * @param layout
	 *            the layout to be presented as a string
	 * @return a short string representation of this constraints object
	 */
	public String toShortString(final FormLayout layout) {
		final StringBuffer buffer = new StringBuffer("(");
		buffer.append(formatInt(this.gridX));
		buffer.append(", ");
		buffer.append(formatInt(this.gridY));
		buffer.append(", ");
		buffer.append(formatInt(this.gridWidth));
		buffer.append(", ");
		buffer.append(formatInt(this.gridHeight));
		buffer.append(", \"");
		buffer.append(this.hAlign.abbreviation());
		if (this.hAlign == DEFAULT && layout != null) {
			buffer.append('=');
			final ColumnSpec colSpec = this.gridWidth == 1 ? layout
					.getColumnSpec(this.gridX) : null;
			buffer.append(concreteAlignment(this.hAlign, colSpec)
					.abbreviation());
		}
		buffer.append(", ");
		buffer.append(this.vAlign.abbreviation());
		if (this.vAlign == DEFAULT && layout != null) {
			buffer.append('=');
			final RowSpec rowSpec = this.gridHeight == 1 ? layout
					.getRowSpec(this.gridY) : null;
			buffer.append(concreteAlignment(this.vAlign, rowSpec)
					.abbreviation());
		}
		buffer.append("\"");
		if (!EMPTY_INSETS.equals(this.insets)) {
			buffer.append(", ");
			buffer.append(this.insets);
		}

		buffer.append(')');
		return buffer.toString();
	}

	// Helper Class *********************************************************

	/**
	 * An ordinal-based serializable typesafe enumeration for component
	 * alignment types as used by the {@link FormLayout}.
	 */
	public static final class Alignment implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = -3505268525410971865L;

		private static final int HORIZONTAL = 0;

		private static final int VERTICAL = 1;

		private static final int BOTH = 2;

		private final transient String name;

		private final transient int orientation;

		Alignment(final String name, final int orientation) {
			this.name = name;
			this.orientation = orientation;
		}

		static Alignment valueOf(final String nameOrAbbreviation) {
			final String str = nameOrAbbreviation.toLowerCase(Locale.ENGLISH);
			if (str.equals("d") || str.equals("default")) {
				return DEFAULT;
			} else if (str.equals("f") || str.equals("fill")) {
				return FILL;
			} else if (str.equals("c") || str.equals("center")) {
				return CENTER;
			} else if (str.equals("l") || str.equals("left")) {
				return LEFT;
			} else if (str.equals("r") || str.equals("right")) {
				return RIGHT;
			} else if (str.equals("t") || str.equals("top")) {
				return TOP;
			} else if (str.equals("b") || str.equals("bottom")) {
				return BOTTOM;
			} else {
				throw new IllegalArgumentException(
						"Invalid alignment "
								+ nameOrAbbreviation
								+ ". Must be one of: left, center, right, top, bottom, "
								+ "fill, default, l, c, r, t, b, f, d.");
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

		boolean isHorizontal() {
			return this.orientation != VERTICAL;
		}

		boolean isVertical() {
			return this.orientation != HORIZONTAL;
		}

		// Serialization
		// *********************************************************

		private static int nextOrdinal = 0;

		private final int ordinal = nextOrdinal++;

		private Object readResolve() {
			return VALUES[this.ordinal]; // Canonicalize
		}

	}

	/**
	 * Returns an integer that has a minimum of two characters.
	 * 
	 * @param number
	 *            the number to format
	 * @return a string representation for a number with a minum of two chars
	 */
	private String formatInt(final int number) {
		final String str = Integer.toString(number);
		return number < 10 ? " " + str : str;
	}

}
