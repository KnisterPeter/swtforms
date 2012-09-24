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

package de.esw.swt.forms.builder;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;

import de.esw.swt.forms.factories.FormFactory;
import de.esw.swt.forms.layout.CellConstraints;
import de.esw.swt.forms.layout.ColumnSpec;
import de.esw.swt.forms.layout.FormLayout;
import de.esw.swt.forms.layout.RowSpec;

/**
 * An abstract class that minimizes the effort required to implement non-visual
 * builders that use the {@link FormLayout}.
 * <p>
 * 
 * Builders hide details of the FormLayout and provide convenience behavior that
 * assists you in constructing a form. This class provides a cell cursor that
 * helps you traverse a form while you add components. Also, it offers several
 * methods to append custom and logical columns and rows.
 * 
 * @author Karsten Lentzsch
 * @version $Revision: 1.3 $
 * 
 * @see ButtonBarBuilder
 * @see ButtonStackBuilder
 * @see CompositeBuilder
 * @see I15dCompositeBuilder
 * @see DefaultFormBuilder
 */
public abstract class AbstractFormBuilder {

	/**
	 * Holds the layout container that we are building.
	 */
	private final Composite composite;

	/**
	 * Holds the instance of <code>FormLayout</code> that is used to specifiy,
	 * fill and layout this form.
	 */
	private final FormLayout layout;

	/**
	 * Holds an instance of <code>CellConstraints</code> that will be used to
	 * specify the location, extent and alignments of the component to be added
	 * next.
	 */
	private CellConstraints currentCellConstraints;

	/**
	 * Specifies if we fill the grid from left to right or right to left. This
	 * value is initialized during the construction from the layout container's
	 * component orientation.
	 * 
	 * @see #isLeftToRight()
	 * @see #setLeftToRight(boolean)
	 */
	private boolean leftToRight;

	// Instance Creation ****************************************************

	/**
	 * Constructs a <code>AbstractFormBuilder</code> for the given FormLayout
	 * and layout container.
	 * 
	 * @param layout
	 *            the {@link FormLayout} to use
	 * @param composite
	 *            the layout container
	 * 
	 * @throws NullPointerException
	 *             if the layout or container is null
	 */
	public AbstractFormBuilder(FormLayout layout, Composite composite) {
		if (layout == null) {
			throw new NullPointerException("The layout must not be null.");
		}

		if (composite == null) {
			throw new NullPointerException(
					"The layout container must not be null.");
		}

		this.composite = composite;
		this.layout = layout;

		composite.setLayout(layout);
		this.currentCellConstraints = new CellConstraints();

		GC gc = new GC(composite);
		try {
			// TODO: Replace this if available in RWT
			// SWT.LEFT_TO_RIGHT = 1<<25
			this.leftToRight = (gc.getStyle() & 1 << 25) == 1 << 25;
		} finally {
			gc.dispose();
		}
	}

	// Accessors ************************************************************

	/**
	 * Returns the container used to build the form.
	 * 
	 * @return the layout container
	 */
	public final Composite getContainer() {
		return this.composite;
	}

	/**
	 * Returns the instance of {@link FormLayout} used to build this form.
	 * 
	 * @return the FormLayout
	 */
	public final FormLayout getLayout() {
		return this.layout;
	}

	/**
	 * Returns the number of columns in the form.
	 * 
	 * @return the number of columns
	 */
	public final int getColumnCount() {
		return getLayout().getColumnCount();
	}

	/**
	 * Returns the number of rows in the form.
	 * 
	 * @return the number of rows
	 */
	public final int getRowCount() {
		return getLayout().getRowCount();
	}

	// Accessing the Cursor Direction ***************************************

	/**
	 * Returns whether this builder fills the form left-to-right or
	 * right-to-left. The initial value of this property is set during the
	 * builder construction from the layout container's
	 * <code>componentOrientation</code> property.
	 * 
	 * @return true indicates left-to-right, false indicates right-to-left
	 * 
	 * @see #setLeftToRight(boolean)
	 */
	public final boolean isLeftToRight() {
		return this.leftToRight;
	}

	/**
	 * Sets the form fill direction to left-to-right or right-to-left. The
	 * initial value of this property is set during the builder construction
	 * from the layout container's <code>componentOrientation</code> property.
	 * 
	 * @param b
	 *            true indicates left-to-right, false right-to-left
	 * 
	 * @see #isLeftToRight()
	 */
	public final void setLeftToRight(boolean b) {
		this.leftToRight = b;
	}

	// Accessing the Cursor Location and Extent *****************************

	/**
	 * Returns the cursor's column.
	 * 
	 * @return the cursor's column
	 */
	public final int getColumn() {
		return this.currentCellConstraints.gridX;
	}

	/**
	 * Sets the cursor to the given column.
	 * 
	 * @param column
	 *            the cursor's new column index
	 */
	public final void setColumn(int column) {
		this.currentCellConstraints.gridX = column;
	}

	/**
	 * Returns the cursor's row.
	 * 
	 * @return the cursor's row
	 */
	public final int getRow() {
		return this.currentCellConstraints.gridY;
	}

	/**
	 * Sets the cursor to the given row.
	 * 
	 * @param row
	 *            the cursor's new row index
	 */
	public final void setRow(int row) {
		this.currentCellConstraints.gridY = row;
	}

	/**
	 * Sets the cursor's column span.
	 * 
	 * @param columnSpan
	 *            the cursor's new column span (grid width)
	 */
	public final void setColumnSpan(int columnSpan) {
		this.currentCellConstraints.gridWidth = columnSpan;
	}

	/**
	 * Sets the cursor's row span.
	 * 
	 * @param rowSpan
	 *            the cursor's new row span (grid height)
	 */
	public final void setRowSpan(int rowSpan) {
		this.currentCellConstraints.gridHeight = rowSpan;
	}

	/**
	 * Sets the cursor's origin to the given column and row.
	 * 
	 * @param column
	 *            the new column index
	 * @param row
	 *            the new row index
	 */
	public final void setOrigin(int column, int row) {
		setColumn(column);
		setRow(row);
	}

	/**
	 * Sets the cursor's extent to the given column span and row span.
	 * 
	 * @param columnSpan
	 *            the new column span (grid width)
	 * @param rowSpan
	 *            the new row span (grid height)
	 */
	public final void setExtent(int columnSpan, int rowSpan) {
		setColumnSpan(columnSpan);
		setRowSpan(rowSpan);
	}

	/**
	 * Sets the cell bounds (location and extent) to the given column, row,
	 * column span and row span.
	 * 
	 * @param column
	 *            the new column index (grid x)
	 * @param row
	 *            the new row index (grid y)
	 * @param columnSpan
	 *            the new column span (grid width)
	 * @param rowSpan
	 *            the new row span (grid height)
	 */
	public final void setBounds(int column, int row, int columnSpan, int rowSpan) {
		setColumn(column);
		setRow(row);
		setColumnSpan(columnSpan);
		setRowSpan(rowSpan);
	}

	/**
	 * Moves to the next column, does the same as #nextColumn(1).
	 */
	public final void nextColumn() {
		nextColumn(1);
	}

	/**
	 * Moves to the next column.
	 * 
	 * @param columns
	 *            number of columns to move
	 */
	public final void nextColumn(int columns) {
		this.currentCellConstraints.gridX += columns * getColumnIncrementSign();
	}

	/**
	 * Increases the row by one; does the same as #nextRow(1).
	 */
	public final void nextRow() {
		nextRow(1);
	}

	/**
	 * Increases the row by the specified rows.
	 * 
	 * @param rows
	 *            number of rows to move
	 */
	public final void nextRow(int rows) {
		this.currentCellConstraints.gridY += rows;
	}

	/**
	 * Moves to the next line: increases the row and resets the column; does the
	 * same as #nextLine(1).
	 */
	public final void nextLine() {
		nextLine(1);
	}

	/**
	 * Moves the cursor down several lines: increases the row by the specified
	 * number of lines and sets the cursor to the leading column.
	 * 
	 * @param lines
	 *            number of rows to move
	 */
	public final void nextLine(int lines) {
		nextRow(lines);
		setColumn(getLeadingColumn());
	}

	// Form Constraints Alignment *******************************************

	/**
	 * Sets the horizontal alignment.
	 * 
	 * @param alignment
	 *            the new horizontal alignment
	 */
	public final void setHAlignment(CellConstraints.Alignment alignment) {
		this.currentCellConstraints.hAlign = alignment;
	}

	/**
	 * Sets the vertical alignment.
	 * 
	 * @param alignment
	 *            the new vertical alignment
	 */
	public final void setVAlignment(CellConstraints.Alignment alignment) {
		this.currentCellConstraints.vAlign = alignment;
	}

	/**
	 * Sets the horizontal and vertical alignment.
	 * 
	 * @param hAlign
	 *            the new horizontal alignment
	 * @param vAlign
	 *            the new vertical alignment
	 */
	public final void setAlignment(CellConstraints.Alignment hAlign,
			CellConstraints.Alignment vAlign) {
		setHAlignment(hAlign);
		setVAlignment(vAlign);
	}

	// Appending Columns ******************************************************

	/**
	 * Appends the given column specification to the builder's layout.
	 * 
	 * @param columnSpec
	 *            the column specification object to append
	 * 
	 * @see #appendColumn(String)
	 */
	public final void appendColumn(ColumnSpec columnSpec) {
		getLayout().appendColumn(columnSpec);
	}

	/**
	 * Appends a column specification to the builder's layout that represents
	 * the given string encoding.
	 * 
	 * @param encodedColumnSpec
	 *            the column specification to append in encoded form
	 * 
	 * @see #appendColumn(ColumnSpec)
	 */
	public final void appendColumn(String encodedColumnSpec) {
		appendColumn(new ColumnSpec(encodedColumnSpec));
	}

	/**
	 * Appends a glue column.
	 * 
	 * @see #appendLabelComponentsGapColumn()
	 * @see #appendRelatedComponentsGapColumn()
	 * @see #appendUnrelatedComponentsGapColumn()
	 */
	public final void appendGlueColumn() {
		appendColumn(FormFactory.GLUE_COLSPEC);
	}

	/**
	 * Appends a column that is the default gap between a label and its
	 * associated component.
	 * 
	 * @since 1.0.3
	 * 
	 * @see #appendGlueColumn()
	 * @see #appendRelatedComponentsGapColumn()
	 * @see #appendUnrelatedComponentsGapColumn()
	 */
	public final void appendLabelComponentsGapColumn() {
		appendColumn(FormFactory.LABEL_COMPONENT_GAP_COLSPEC);
	}

	/**
	 * Appends a column that is the default gap for related components.
	 * 
	 * @see #appendGlueColumn()
	 * @see #appendLabelComponentsGapColumn()
	 * @see #appendUnrelatedComponentsGapColumn()
	 */
	public final void appendRelatedComponentsGapColumn() {
		appendColumn(FormFactory.RELATED_GAP_COLSPEC);
	}

	/**
	 * Appends a column that is the default gap for unrelated components.
	 * 
	 * @see #appendGlueColumn()
	 * @see #appendLabelComponentsGapColumn()
	 * @see #appendRelatedComponentsGapColumn()
	 */
	public final void appendUnrelatedComponentsGapColumn() {
		appendColumn(FormFactory.UNRELATED_GAP_COLSPEC);
	}

	// Appending Rows ********************************************************

	/**
	 * Appends the given row specification to the builder's layout.
	 * 
	 * @param rowSpec
	 *            the row specification object to append
	 * 
	 * @see #appendRow(String)
	 */
	public final void appendRow(RowSpec rowSpec) {
		getLayout().appendRow(rowSpec);
	}

	/**
	 * Appends a row specification to the builder's layout that represents the
	 * given string encoding.
	 * 
	 * @param encodedRowSpec
	 *            the row specification to append in encoded form
	 * 
	 * @see #appendRow(RowSpec)
	 */
	public final void appendRow(String encodedRowSpec) {
		appendRow(new RowSpec(encodedRowSpec));
	}

	/**
	 * Appends a glue row.
	 * 
	 * @see #appendRelatedComponentsGapRow()
	 * @see #appendUnrelatedComponentsGapRow()
	 * @see #appendParagraphGapRow()
	 */
	public final void appendGlueRow() {
		appendRow(FormFactory.GLUE_ROWSPEC);
	}

	/**
	 * Appends a row that is the default gap for related components.
	 * 
	 * @see #appendGlueRow()
	 * @see #appendUnrelatedComponentsGapRow()
	 * @see #appendParagraphGapRow()
	 */
	public final void appendRelatedComponentsGapRow() {
		appendRow(FormFactory.RELATED_GAP_ROWSPEC);
	}

	/**
	 * Appends a row that is the default gap for unrelated components.
	 * 
	 * @see #appendGlueRow()
	 * @see #appendRelatedComponentsGapRow()
	 * @see #appendParagraphGapRow()
	 */
	public final void appendUnrelatedComponentsGapRow() {
		appendRow(FormFactory.UNRELATED_GAP_ROWSPEC);
	}

	/**
	 * Appends a row that is the default gap for paragraphs.
	 * 
	 * @since 1.0.3
	 * 
	 * @see #appendGlueRow()
	 * @see #appendRelatedComponentsGapRow()
	 * @see #appendUnrelatedComponentsGapRow()
	 */
	public final void appendParagraphGapRow() {
		appendRow(FormFactory.PARAGRAPH_GAP_ROWSPEC);
	}

	// Adding Components ****************************************************

	/**
	 * Adds a component to the panel using the given cell constraints.
	 * 
	 * @param control
	 *            the component to add
	 * @param cellConstraints
	 *            the component's cell constraints
	 * @return the added component
	 */
	public final Control add(Control control, CellConstraints cellConstraints) {
		Layout layout = this.composite.getLayout();
		if (!(layout instanceof FormLayout)) {
			throw new IllegalArgumentException(
					"To use a FormBuilder the composite must have a FormLayout.");
		}

		((FormLayout) layout).setConstraints(control, cellConstraints);
		return control;
	}

	/**
	 * Adds a component to the panel using the given encoded cell constraints.
	 * 
	 * @param control
	 *            the component to add
	 * @param encodedCellConstraints
	 *            the component's encoded cell constraints
	 * @return the added component
	 */
	public final Control add(Control control, String encodedCellConstraints) {
		add(control, new CellConstraints(encodedCellConstraints));
		return control;
	}

	/**
	 * Adds a component to the container using the default cell constraints.
	 * Note that when building from left to right, this method won't adjust the
	 * cell constraints if the column span is larger than 1. In this case you
	 * should use {@link #add(Control, CellConstraints)} with a cell constraints
	 * object created by {@link #createLeftAdjustedConstraints(int)}.
	 * 
	 * @param control
	 *            the component to add
	 * @return the added component
	 * 
	 * @see #add(Control, CellConstraints)
	 * @see #createLeftAdjustedConstraints(int)
	 */
	public final Control add(Control control) {
		add(control, this.currentCellConstraints);
		return control;
	}

	// Misc *****************************************************************

	/**
	 * Returns the CellConstraints object that is used as a cursor and holds the
	 * current column span and row span.
	 * 
	 * @return the builder's current {@link CellConstraints} object
	 */
	protected final CellConstraints cellConstraints() {
		return this.currentCellConstraints;
	}

	/**
	 * Returns the index of the leading column.
	 * <p>
	 * 
	 * Subclasses may override this method, for example, if the form has a
	 * leading gap column that should not be filled with components.
	 * 
	 * @return the leading column
	 */
	protected int getLeadingColumn() {
		return isLeftToRight() ? 1 : getColumnCount();
	}

	/**
	 * Returns the sign (-1 or 1) used to increment the cursor's column when
	 * moving to the next column.
	 * 
	 * @return -1 for right-to-left, 1 for left-to-right
	 */
	protected final int getColumnIncrementSign() {
		return isLeftToRight() ? 1 : -1;
	}

	/**
	 * Creates and returns a <code>CellConstraints</code> object at the
	 * current cursor position that uses the given column span and is adjusted
	 * to the left. Useful when building from right to left.
	 * 
	 * @param columnSpan
	 *            the column span to be used in the constraints
	 * @return CellConstraints adjusted to the left hand side
	 */
	protected final CellConstraints createLeftAdjustedConstraints(int columnSpan) {
		int firstColumn = isLeftToRight() ? getColumn() : getColumn() + 1
				- columnSpan;
		return new CellConstraints(firstColumn, getRow(), columnSpan,
				cellConstraints().gridHeight);
	}

}
