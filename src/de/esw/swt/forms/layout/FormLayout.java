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

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;

import de.esw.swt.forms.util.InsetHelper;

/**
 * FormLayout is a powerful, flexible and precise general purpose layout
 * manager. It aligns components vertically and horizontally in a dynamic
 * rectangular grid of cells, with each component occupying one or more cells. A
 * <a href="../../../../../whitepaper.pdf" target="secondary">whitepaper</a>
 * about the FormLayout ships with the product documentation and is available <a
 * href="http://www.jgoodies.com/articles/forms.pdf">online</a>.
 * <p>
 * 
 * To use FormLayout you first define the grid by specifying the columns and
 * rows. In a second step you add components to the grid. You can specify
 * columns and rows via human-readable String descriptions or via arrays of
 * {@link ColumnSpec} and {@link RowSpec} instances.
 * <p>
 * 
 * Each component managed by a FormLayout is associated with an instance of
 * {@link CellConstraints}. The constraints object specifies where a component
 * should be located on the form's grid and how the component should be
 * positioned. In addition to its constraints object the <code>FormLayout</code>
 * also considers each component's minimum and preferred sizes in order to
 * determine a component's size.
 * <p>
 * 
 * FormLayout has been designed to work with non-visual builders that help you
 * specify the layout and fill the grid. For example, the
 * {@link de.esw.swt.forms.builder.ButtonBarBuilder} assists you in building
 * button bars; it creates a standardized FormLayout and provides a minimal API
 * that specializes in adding buttons. Other builders can create frequently used
 * panel design, for example a form that consists of rows of label-component
 * pairs.
 * <p>
 * 
 * FormLayout has been prepared to work with different types of sizes as defined
 * by the {@link Size} interface.
 * <p>
 * 
 * <strong>Example 1</strong> (Plain FormLayout):<br>
 * The following example creates a panel with 3 data columns and 3 data rows;
 * the columns and rows are specified before components are added to the form.
 * 
 * <pre>
 * FormLayout layout = new FormLayout(&quot;right:pref, 6dlu, 50dlu, 4dlu, default&quot;, // columns 
 * 		&quot;pref, 3dlu, pref, 3dlu, pref&quot;); // rows
 * 
 * CellConstraints cc = new CellConstraints();
 * JPanel panel = new JPanel(layout);
 * panel.add(new JLabel(&quot;Label1&quot;), cc.xy(1, 1));
 * panel.add(new JTextField(), cc.xywh(3, 1, 3, 1));
 * panel.add(new JLabel(&quot;Label2&quot;), cc.xy(1, 3));
 * panel.add(new JTextField(), cc.xy(3, 3));
 * panel.add(new JLabel(&quot;Label3&quot;), cc.xy(1, 5));
 * panel.add(new JTextField(), cc.xy(3, 5));
 * panel.add(new JButton(&quot;/u2026&quot;), cc.xy(5, 5));
 * return panel;
 * </pre>
 * 
 * <p>
 * 
 * <strong>Example 2</strong> (Using CompositeBuilder):<br>
 * This example creates the same panel as above using the
 * {@link de.esw.swt.forms.builder.CompositeBuilder} to add components to the
 * form.
 * 
 * <pre>
 * FormLayout layout = new FormLayout(&quot;right:pref, 6dlu, 50dlu, 4dlu, default&quot;, // columns 
 * 		&quot;pref, 3dlu, pref, 3dlu, pref&quot;); // rows
 * 
 * CompositeBuilder builder = new CompositeBuilder(layout);
 * CellConstraints cc = new CellConstraints();
 * builder.addLabel(&quot;Label1&quot;, cc.xy(1, 1));
 * builder.add(new JTextField(), cc.xywh(3, 1, 3, 1));
 * builder.addLabel(&quot;Label2&quot;, cc.xy(1, 3));
 * builder.add(new JTextField(), cc.xy(3, 3));
 * builder.addLabel(&quot;Label3&quot;, cc.xy(1, 5));
 * builder.add(new JTextField(), cc.xy(3, 5));
 * builder.add(new JButton(&quot;/u2026&quot;), cc.xy(5, 5));
 * return builder.getPanel();
 * </pre>
 * 
 * <p>
 * 
 * <strong>Example 3</strong> (Using DefaultFormBuilder):<br>
 * This example utilizes the {@link de.esw.swt.forms.builder.DefaultFormBuilder}
 * that ships with the source distribution.
 * 
 * <pre>
 * FormLayout layout = new FormLayout(&quot;right:pref, 6dlu, 50dlu, 4dlu, default&quot;); // 5 columns; add rows later
 * 
 * DefaultFormBuilder builder = new DefaultFormBuilder(layout);
 * builder.append(&quot;Label1&quot;, new JTextField(), 3);
 * builder.append(&quot;Label2&quot;, new JTextField());
 * builder.append(&quot;Label3&quot;, new JTextField());
 * builder.append(new JButton(&quot;/u2026&quot;));
 * return builder.getPanel();
 * </pre>
 * 
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
 * @version $Revision: 1.6 $
 * 
 * @see ColumnSpec
 * @see RowSpec
 * @see CellConstraints
 * @see de.esw.swt.forms.builder.AbstractFormBuilder
 * @see de.esw.swt.forms.builder.ButtonBarBuilder
 * @see de.esw.swt.forms.builder.DefaultFormBuilder
 * @see de.esw.swt.forms.factories.FormFactory
 * @see Size
 * @see Sizes
 */
public final class FormLayout extends Layout {

	/**
	 * Holds the column specifications.
	 * 
	 * @see ColumnSpec
	 * @see #getColumnCount()
	 * @see #getColumnSpec(int)
	 * @see #appendColumn(ColumnSpec)
	 * @see #insertColumn(int, ColumnSpec)
	 * @see #removeColumn(int)
	 */
	private final List colSpecs;

	/**
	 * Holds the row specifications.
	 * 
	 * @see RowSpec
	 * @see #getRowCount()
	 * @see #getRowSpec(int)
	 * @see #appendRow(RowSpec)
	 * @see #insertRow(int, RowSpec)
	 * @see #removeRow(int)
	 */
	private final List rowSpecs;

	/**
	 * Holds the column groups as an array of arrays of column indices.
	 * 
	 * @see #getColumnGroups()
	 * @see #setColumnGroups(int[][])
	 * @see #addGroupedColumn(int)
	 */
	private int[][] colGroupIndices;

	/**
	 * Holds the row groups as an array of arrays of row indices.
	 * 
	 * @see #getRowGroups()
	 * @see #setRowGroups(int[][])
	 * @see #addGroupedRow(int)
	 */
	private int[][] rowGroupIndices;

	/**
	 * Maps components to their associated <code>CellConstraints</code>.
	 * 
	 * @see CellConstraints
	 */
	private final List controls;

	// Fields used by the Layout Algorithm **********************************

	/**
	 * Holds the components that occupy exactly one column. For each column we
	 * keep a list of these components.
	 */
	private transient List[] colComponents;

	/**
	 * Holds the components that occupy exactly one row. For each row we keep a
	 * list of these components.
	 */
	private transient List[] rowComponents;

	/**
	 * Caches component minimum and preferred sizes. All requests for component
	 * sizes shall be directed to the cache.
	 */
	private final ComponentSizeCache componentSizeCache;

	/**
	 * These functional objects are used to measure component sizes. They
	 * abstract from horizontal and vertical orientation and so, allow to
	 * implement the layout algorithm for both orientations with a single set of
	 * methods.
	 */
	private final Measure minimumWidthMeasure;

	private final Measure minimumHeightMeasure;

	private final Measure preferredWidthMeasure;

	private final Measure preferredHeightMeasure;

	// Instance Creation ****************************************************

	/**
	 * Constructs an empty FormLayout. Columns and rows must be added before
	 * components can be added to the layout container.
	 * <p>
	 * 
	 * This constructor is intended to be used in environments that add columns
	 * and rows dynamically.
	 */
	public FormLayout() {
		this(new ColumnSpec[0], new RowSpec[0]);
	}

	/**
	 * Constructs a FormLayout using the given encoded column specifications.
	 * The constructed layout has no rows; these must be added before components
	 * can be added to the layout container.
	 * <p>
	 * 
	 * This constructor is primarily intended to be used with builder classes
	 * that add rows dynamically, such as the <code>DefaultFormBuilder</code>.
	 * <p>
	 * 
	 * <strong>Examples:</strong>
	 * 
	 * <pre>
	 * // Label, gap, component
	 * FormLayout layout = new FormLayout(&quot;pref, 4dlu, pref&quot;);
	 * 
	 * // Right-aligned label, gap, component, gap, component                                         
	 * FormLayout layout = new FormLayout(&quot;right:pref, 4dlu, 50dlu, 4dlu, 50dlu&quot;);
	 * 
	 * // Left-aligned labels, gap, components, gap, components                                         
	 * FormLayout layout = new FormLayout(&quot;left:pref, 4dlu, pref, 4dlu, pref&quot;);
	 * </pre>
	 * 
	 * See the class comment for more examples.
	 * 
	 * @param encodedColumnSpecs
	 *            comma separated encoded column specifications
	 * @throws NullPointerException
	 *             if encodedColumnSpecs is <code>null</code>
	 */
	public FormLayout(final String encodedColumnSpecs) {
		this(ColumnSpec.decodeSpecs(encodedColumnSpecs), new RowSpec[0]);
	}

	/**
	 * Constructs a FormLayout using the given encoded column and row
	 * specifications.
	 * <p>
	 * 
	 * This constructor is recommended for most hand-coded layouts.
	 * <p>
	 * 
	 * <strong>Examples:</strong>
	 * 
	 * <pre>
	 * FormLayout layout = new FormLayout(&quot;pref, 4dlu, pref&quot;, // columns 
	 * 		&quot;p, 3dlu, p&quot;); // rows
	 * 
	 * FormLayout layout = new FormLayout(&quot;right:pref, 4dlu, pref&quot;, // columns 
	 * 		&quot;p, 3dlu, p, 3dlu, fill:p:grow&quot;); // rows
	 * 
	 * FormLayout layout = new FormLayout(&quot;left:pref, 4dlu, 50dlu&quot;, // columns 
	 * 		&quot;p, 2px, p, 3dlu, p, 9dlu, p&quot;); // rows
	 * 
	 * FormLayout layout = new FormLayout(&quot;max(75dlu;pref), 4dlu, default&quot;, // columns 
	 * 		&quot;p, 3dlu, p, 3dlu, p, 3dlu, p&quot;); // rows
	 * </pre>
	 * 
	 * See the class comment for more examples.
	 * 
	 * @param encodedColumnSpecs
	 *            comma separated encoded column specifications
	 * @param encodedRowSpecs
	 *            comma separated encoded row specifications
	 * @throws NullPointerException
	 *             if encodedColumnSpecs or encodedRowSpecs is <code>null</code>
	 */
	public FormLayout(final String encodedColumnSpecs,
			final String encodedRowSpecs) {
		this(ColumnSpec.decodeSpecs(encodedColumnSpecs), RowSpec
				.decodeSpecs(encodedRowSpecs));
	}

	/**
	 * Constructs a FormLayout using the given column specifications. The
	 * constructed layout has no rows; these must be added before components can
	 * be added to the layout container.
	 * 
	 * @param colSpecs
	 *            an array of column specifications.
	 * @throws NullPointerException
	 *             if colSpecs is null
	 * 
	 * @since 1.1
	 */
	public FormLayout(final ColumnSpec[] colSpecs) {
		this(colSpecs, new RowSpec[] {});
	}

	/**
	 * Constructs a FormLayout using the given column and row specifications.
	 * 
	 * @param colSpecs
	 *            an array of column specifications.
	 * @param rowSpecs
	 *            an array of row specifications.
	 * @throws NullPointerException
	 *             if colSpecs or rowSpecs is null
	 */
	public FormLayout(final ColumnSpec[] colSpecs, final RowSpec[] rowSpecs) {
		if (colSpecs == null) {
			throw new NullPointerException(
					"The column specifications must not be null.");
		}
		if (rowSpecs == null) {
			throw new NullPointerException(
					"The row specifications must not be null.");
		}

		this.colSpecs = new ArrayList(Arrays.asList(colSpecs));
		this.rowSpecs = new ArrayList(Arrays.asList(rowSpecs));
		this.colGroupIndices = new int[][] {};
		this.rowGroupIndices = new int[][] {};
		final int initialCapacity = colSpecs.length * rowSpecs.length / 4;
		this.controls = new ArrayList();
		this.componentSizeCache = new ComponentSizeCache(initialCapacity);
		this.minimumWidthMeasure = new MinimumWidthMeasure(
				this.componentSizeCache);
		this.minimumHeightMeasure = new MinimumHeightMeasure(
				this.componentSizeCache);
		this.preferredWidthMeasure = new PreferredWidthMeasure(
				this.componentSizeCache);
		this.preferredHeightMeasure = new PreferredHeightMeasure(
				this.componentSizeCache);
	}

	// Accessing the Column and Row Specifications **************************

	/**
	 * Returns the number of columns in this layout.
	 * 
	 * @return the number of columns
	 */
	public int getColumnCount() {
		return this.colSpecs.size();
	}

	/**
	 * Returns the number of rows in this layout.
	 * 
	 * @return the number of rows
	 */
	public int getRowCount() {
		return this.rowSpecs.size();
	}

	/**
	 * Returns the <code>ColumnSpec</code> at the specified column index.
	 * 
	 * @param columnIndex
	 *            the column index of the requested <code>ColumnSpec</code>
	 * @return the <code>ColumnSpec</code> at the specified column
	 * @throws IndexOutOfBoundsException
	 *             if the column index is out of range
	 */
	public ColumnSpec getColumnSpec(final int columnIndex) {
		return (ColumnSpec) this.colSpecs.get(columnIndex - 1);
	}

	/**
	 * Sets the <code>ColumnSpec</code> at the specified column index.
	 * 
	 * @param columnIndex
	 *            the index of the column to be changed
	 * @param columnSpec
	 *            the <code>ColumnSpec</code> to be set
	 * @throws NullPointerException
	 *             if the column specification is null
	 * @throws IndexOutOfBoundsException
	 *             if the column index is out of range
	 */
	public void setColumnSpec(final int columnIndex, final ColumnSpec columnSpec) {
		if (columnSpec == null) {
			throw new NullPointerException("The column spec must not be null.");
		}
		this.colSpecs.set(columnIndex - 1, columnSpec);
	}

	/**
	 * Returns the <code>RowSpec</code> at the specified row index.
	 * 
	 * @param rowIndex
	 *            the row index of the requested <code>RowSpec</code>
	 * @return the <code>RowSpec</code> at the specified row
	 * @throws IndexOutOfBoundsException
	 *             if the row index is out of range
	 */
	public RowSpec getRowSpec(final int rowIndex) {
		return (RowSpec) this.rowSpecs.get(rowIndex - 1);
	}

	/**
	 * Sets the <code>RowSpec</code> at the specified row index.
	 * 
	 * @param rowIndex
	 *            the index of the row to be changed
	 * @param rowSpec
	 *            the <code>RowSpec</code> to be set
	 * @throws NullPointerException
	 *             if the row specification is null
	 * @throws IndexOutOfBoundsException
	 *             if the row index is out of range
	 */
	public void setRowSpec(final int rowIndex, final RowSpec rowSpec) {
		if (rowSpec == null) {
			throw new NullPointerException("The row spec must not be null.");
		}
		this.rowSpecs.set(rowIndex - 1, rowSpec);
	}

	/**
	 * Appends the given column specification to the right hand side of all
	 * columns.
	 * 
	 * @param columnSpec
	 *            the column specification to be added
	 * @throws NullPointerException
	 *             if the column specification is null
	 */
	public void appendColumn(final ColumnSpec columnSpec) {
		if (columnSpec == null) {
			throw new NullPointerException("The column spec must not be null.");
		}
		this.colSpecs.add(columnSpec);
	}

	/**
	 * Inserts the specified column at the specified position. Shifts components
	 * that intersect the new column to the right hand side and readjusts column
	 * groups.
	 * <p>
	 * 
	 * The component shift works as follows: components that were located on the
	 * right hand side of the inserted column are shifted one column to the
	 * right; component column span is increased by one if it intersects the new
	 * column.
	 * <p>
	 * 
	 * Column group indices that are greater or equal than the given column
	 * index will be increased by one.
	 * 
	 * @param columnIndex
	 *            index of the column to be inserted
	 * @param columnSpec
	 *            specification of the column to be inserted
	 * @throws IndexOutOfBoundsException
	 *             if the column index is out of range
	 */
	public void insertColumn(final int columnIndex, final ColumnSpec columnSpec) {
		if (columnIndex < 1 || columnIndex > getColumnCount()) {
			throw new IndexOutOfBoundsException("The column index "
					+ columnIndex + "must be in the range [1, "
					+ getColumnCount() + "].");
		}
		this.colSpecs.add(columnIndex - 1, columnSpec);
		shiftComponentsHorizontally(columnIndex, false);
		adjustGroupIndices(this.colGroupIndices, columnIndex, false);
	}

	/**
	 * Removes the column with the given column index from the layout.
	 * Components will be rearranged and column groups will be readjusted.
	 * Therefore, the column must not contain components and must not be part of
	 * a column group.
	 * <p>
	 * 
	 * The component shift works as follows: components that were located on the
	 * right hand side of the removed column are moved one column to the left;
	 * component column span is decreased by one if it intersects the removed
	 * column.
	 * <p>
	 * 
	 * Column group indices that are greater than the column index will be
	 * decreased by one.
	 * <p>
	 * 
	 * <strong>Note:</strong> If one of the constraints mentioned above is
	 * violated, this layout's state becomes illegal and it is unsafe to work
	 * with this layout. A typical layout implementation can ensure that these
	 * constraints are not violated. However, in some cases you may need to
	 * check these conditions before you invoke this method. The Forms extras
	 * contain source code for class <code>FormLayoutUtils</code> that
	 * provides the required test methods:<br>
	 * <code>#columnContainsComponents(Composite, int)</code> and<br>
	 * <code>#isGroupedColumn(FormLayout, int)</code>.
	 * 
	 * @param columnIndex
	 *            index of the column to remove
	 * @throws IndexOutOfBoundsException
	 *             if the column index is out of range
	 * @throws IllegalStateException
	 *             if the column contains components or if the column is already
	 *             grouped
	 */
	public void removeColumn(final int columnIndex) {
		if (columnIndex < 1 || columnIndex > getColumnCount()) {
			throw new IndexOutOfBoundsException("The column index "
					+ columnIndex + " must be in the range [1, "
					+ getColumnCount() + "].");
		}
		this.colSpecs.remove(columnIndex - 1);
		shiftComponentsHorizontally(columnIndex, true);
		adjustGroupIndices(this.colGroupIndices, columnIndex, true);
	}

	/**
	 * Appends the given row specification to the bottom of all rows.
	 * 
	 * @param rowSpec
	 *            the row specification to be added to the form layout
	 * @throws NullPointerException
	 *             if the rowSpec is null
	 */
	public void appendRow(final RowSpec rowSpec) {
		if (rowSpec == null) {
			throw new NullPointerException("The row spec must not be null.");
		}
		this.rowSpecs.add(rowSpec);
	}

	/**
	 * Inserts the specified column at the specified position. Shifts components
	 * that intersect the new column to the right and readjusts column groups.
	 * <p>
	 * 
	 * The component shift works as follows: components that were located on the
	 * right hand side of the inserted column are shifted one column to the
	 * right; component column span is increased by one if it intersects the new
	 * column.
	 * <p>
	 * 
	 * Column group indices that are greater or equal than the given column
	 * index will be increased by one.
	 * 
	 * @param rowIndex
	 *            index of the row to be inserted
	 * @param rowSpec
	 *            specification of the row to be inserted
	 * @throws IndexOutOfBoundsException
	 *             if the row index is out of range
	 */
	public void insertRow(final int rowIndex, final RowSpec rowSpec) {
		if (rowIndex < 1 || rowIndex > getRowCount()) {
			throw new IndexOutOfBoundsException("The row index " + rowIndex
					+ " must be in the range [1, " + getRowCount() + "].");
		}
		this.rowSpecs.add(rowIndex - 1, rowSpec);
		shiftComponentsVertically(rowIndex, false);
		adjustGroupIndices(this.rowGroupIndices, rowIndex, false);
	}

	/**
	 * Removes the row with the given row index from the layout. Components will
	 * be rearranged and row groups will be readjusted. Therefore, the row must
	 * not contain components and must not be part of a row group.
	 * <p>
	 * 
	 * The component shift works as follows: components that were located below
	 * the removed row are moved up one row; component row span is decreased by
	 * one if it intersects the removed row.
	 * <p>
	 * 
	 * Row group indices that are greater than the row index will be decreased
	 * by one.
	 * <p>
	 * 
	 * <strong>Note:</strong> If one of the constraints mentioned above is
	 * violated, this layout's state becomes illegal and it is unsafe to work
	 * with this layout. A typical layout implementation can ensure that these
	 * constraints are not violated. However, in some cases you may need to
	 * check these conditions before you invoke this method. The Forms extras
	 * contain source code for class <code>FormLayoutUtils</code> that
	 * provides the required test methods:<br>
	 * <code>#rowContainsComponents(Composite, int)</code> and<br>
	 * <code>#isGroupedRow(FormLayout, int)</code>.
	 * 
	 * @param rowIndex
	 *            index of the row to remove
	 * @throws IndexOutOfBoundsException
	 *             if the row index is out of range
	 * @throws IllegalStateException
	 *             if the row contains components or if the row is already
	 *             grouped
	 */
	public void removeRow(final int rowIndex) {
		if (rowIndex < 1 || rowIndex > getRowCount()) {
			throw new IndexOutOfBoundsException("The row index " + rowIndex
					+ "must be in the range [1, " + getRowCount() + "].");
		}
		this.rowSpecs.remove(rowIndex - 1);
		shiftComponentsVertically(rowIndex, true);
		adjustGroupIndices(this.rowGroupIndices, rowIndex, true);
	}

	/**
	 * Shifts components horizontally, either to the right if a column has been
	 * inserted or to the left if a column has been removed.
	 * 
	 * @param columnIndex
	 *            index of the column to remove
	 * @param remove
	 *            true for remove, false for insert
	 * @throws IllegalStateException
	 *             if a removed column contains components
	 */
	private void shiftComponentsHorizontally(final int columnIndex,
			final boolean remove) {
		final int offset = remove ? -1 : 1;
		for (final Iterator i = this.controls.iterator(); i.hasNext();) {
			Control control = (Control) i.next();
			final CellConstraints constraints = (CellConstraints) control
					.getLayoutData();
			final int x1 = constraints.gridX;
			final int w = constraints.gridWidth;
			final int x2 = x1 + w - 1;
			if (x1 == columnIndex && remove) {
				throw new IllegalStateException("The removed column "
						+ columnIndex
						+ " must not contain component origins.\n"
						+ "Illegal component=" + control);
			} else if (x1 >= columnIndex) {
				constraints.gridX += offset;
			} else if (x2 >= columnIndex) {
				constraints.gridWidth += offset;
			}
		}
	}

	/**
	 * Shifts components vertically, either to the bottom if a row has been
	 * inserted or to the top if a row has been removed.
	 * 
	 * @param rowIndex
	 *            index of the row to remove
	 * @param remove
	 *            true for remove, false for insert
	 * @throws IllegalStateException
	 *             if a removed column contains components
	 */
	private void shiftComponentsVertically(final int rowIndex,
			final boolean remove) {
		final int offset = remove ? -1 : 1;
		for (final Iterator i = this.controls.iterator(); i.hasNext();) {
			final Control control = (Control) i.next();
			final CellConstraints constraints = (CellConstraints) control
					.getLayoutData();
			final int y1 = constraints.gridY;
			final int h = constraints.gridHeight;
			final int y2 = y1 + h - 1;
			if (y1 == rowIndex && remove) {
				throw new IllegalStateException("The removed row " + rowIndex
						+ " must not contain component origins.\n"
						+ "Illegal component=" + control);
			} else if (y1 >= rowIndex) {
				constraints.gridY += offset;
			} else if (y2 >= rowIndex) {
				constraints.gridHeight += offset;
			}
		}
	}

	/**
	 * Adjusts group indices. Shifts the given groups to left, right, up, down
	 * according to the specified remove or add flag.
	 * 
	 * @param allGroupIndices
	 *            the groups to be adjusted
	 * @param modifiedIndex
	 *            the modified column or row index
	 * @param remove
	 *            true for remove, false for add
	 * @throws IllegalStateException
	 *             if we remove and the index is grouped
	 */
	private void adjustGroupIndices(final int[][] allGroupIndices,
			final int modifiedIndex, final boolean remove) {
		final int offset = remove ? -1 : +1;
		for (int group = 0; group < allGroupIndices.length; group++) {
			final int[] groupIndices = allGroupIndices[group];
			for (int i = 0; i < groupIndices.length; i++) {
				final int index = groupIndices[i];
				if (index == modifiedIndex && remove) {
					throw new IllegalStateException("The removed index "
							+ modifiedIndex + " must not be grouped.");
				} else if (index >= modifiedIndex) {
					groupIndices[i] += offset;
				}
			}
		}
	}

	// Accessing Constraints ************************************************

	/**
	 * Looks up and returns the constraints for the specified component. A copy
	 * of the actual <code>CellConstraints</code> object is returned.
	 * 
	 * @param control
	 *            the component to be queried
	 * @return the <code>CellConstraints</code> for the specified component
	 * @throws NullPointerException
	 *             if component is <code>null</code> or has not been added to
	 *             the container
	 */
	public CellConstraints getConstraints(final Control control) {
		if (control == null) {
			throw new NullPointerException("The component must not be null.");
		}

		return (CellConstraints) ((CellConstraints) control.getLayoutData())
				.clone();
	}

	/**
	 * Sets the constraints for the specified component in this layout.
	 * 
	 * @param control
	 *            the component to be modified
	 * @param constraints
	 *            the constraints to be applied
	 * @throws NullPointerException
	 *             if the component or constraints object is <code>null</code>
	 */
	public void setConstraints(final Control control,
			final CellConstraints constraints) {
		if (control == null) {
			throw new NullPointerException("The component must not be null.");
		}
		if (constraints == null) {
			throw new NullPointerException("The constraints must not be null.");
		}

		constraints.ensureValidGridBounds(getColumnCount(), getRowCount());
		control.setLayoutData(constraints.clone());
		this.controls.add(control);
	}

	// Accessing Column and Row Groups **************************************

	/**
	 * Returns a deep copy of the column groups.
	 * 
	 * @return the column groups as two-dimensional int array
	 */
	public int[][] getColumnGroups() {
		return deepClone(this.colGroupIndices);
	}

	/**
	 * Sets the column groups, where each column in a group gets the same group
	 * wide width. Each group is described by an array of integers that are
	 * interpreted as column indices. The parameter is an array of such group
	 * descriptions.
	 * <p>
	 * 
	 * <strong>Examples:</strong>
	 * 
	 * <pre>
	 * // Group columns 1, 3 and 4. 
	 * setColumnGroups(new int[][] { { 1, 3, 4 } });
	 * 
	 * // Group columns 1, 3, 4, and group columns 7 and 9
	 * setColumnGroups(new int[][] { { 1, 3, 4 }, { 7, 9 } });
	 * </pre>
	 * 
	 * @param colGroupIndices
	 *            a two-dimensional array of column groups indices
	 * @throws IndexOutOfBoundsException
	 *             if an index is outside the grid
	 * @throws IllegalArgumentException
	 *             if a column index is used twice
	 */
	public void setColumnGroups(final int[][] colGroupIndices) {
		final int maxColumn = getColumnCount();
		final boolean[] usedIndices = new boolean[maxColumn + 1];
		for (int group = 0; group < colGroupIndices.length; group++) {
			for (int j = 0; j < colGroupIndices[group].length; j++) {
				final int colIndex = colGroupIndices[group][j];
				if (colIndex < 1 || colIndex > maxColumn) {
					throw new IndexOutOfBoundsException(
							"Invalid column group index " + colIndex
									+ " in group " + (group + 1));
				}
				if (usedIndices[colIndex]) {
					throw new IllegalArgumentException("Column index "
							+ colIndex
							+ " must not be used in multiple column groups.");
				}
				usedIndices[colIndex] = true;
			}
		}
		this.colGroupIndices = deepClone(colGroupIndices);
	}

	/**
	 * Adds the specified column index to the last column group. In case there
	 * are no groups, a new group will be created.
	 * 
	 * @param columnIndex
	 *            the column index to be set grouped
	 */
	public void addGroupedColumn(final int columnIndex) {
		int[][] newColGroups = getColumnGroups();
		// Create a group if none exists.
		if (newColGroups.length == 0) {
			newColGroups = new int[][] { { columnIndex } };
		} else {
			final int lastGroupIndex = newColGroups.length - 1;
			final int[] lastGroup = newColGroups[lastGroupIndex];
			final int groupSize = lastGroup.length;
			final int[] newLastGroup = new int[groupSize + 1];
			System.arraycopy(lastGroup, 0, newLastGroup, 0, groupSize);
			newLastGroup[groupSize] = columnIndex;
			newColGroups[lastGroupIndex] = newLastGroup;
		}
		setColumnGroups(newColGroups);
	}

	/**
	 * Returns a deep copy of the row groups.
	 * 
	 * @return the row groups as two-dimensional int array
	 */
	public int[][] getRowGroups() {
		return deepClone(this.rowGroupIndices);
	}

	/**
	 * Sets the row groups, where each row in such a group gets the same group
	 * wide height. Each group is described by an array of integers that are
	 * interpreted as row indices. The parameter is an array of such group
	 * descriptions.
	 * <p>
	 * 
	 * <strong>Examples:</strong>
	 * 
	 * <pre>
	 * // Group rows 1 and 2.
	 * setRowGroups(new int[][] { { 1, 2 } });
	 * 
	 * // Group rows 1 and 2, and group rows 5, 7, and 9.
	 * setRowGroups(new int[][] { { 1, 2 }, { 5, 7, 9 } });
	 * </pre>
	 * 
	 * @param rowGroupIndices
	 *            a two-dimensional array of row group indices.
	 * @throws IndexOutOfBoundsException
	 *             if an index is outside the grid
	 */
	public void setRowGroups(final int[][] rowGroupIndices) {
		final int rowCount = getRowCount();
		final boolean[] usedIndices = new boolean[rowCount + 1];
		for (int i = 0; i < rowGroupIndices.length; i++) {
			for (int j = 0; j < rowGroupIndices[i].length; j++) {
				final int rowIndex = rowGroupIndices[i][j];
				if (rowIndex < 1 || rowIndex > rowCount) {
					throw new IndexOutOfBoundsException(
							"Invalid row group index " + rowIndex
									+ " in group " + (i + 1));
				}
				if (usedIndices[rowIndex]) {
					throw new IllegalArgumentException("Row index " + rowIndex
							+ " must not be used in multiple row groups.");
				}
				usedIndices[rowIndex] = true;
			}
		}
		this.rowGroupIndices = deepClone(rowGroupIndices);
	}

	/**
	 * Adds the specified row index to the last row group. In case there are no
	 * groups, a new group will be created.
	 * 
	 * @param rowIndex
	 *            the index of the row that should be grouped
	 */
	public void addGroupedRow(final int rowIndex) {
		int[][] newRowGroups = getRowGroups();
		// Create a group if none exists.
		if (newRowGroups.length == 0) {
			newRowGroups = new int[][] { { rowIndex } };
		} else {
			final int lastGroupIndex = newRowGroups.length - 1;
			final int[] lastGroup = newRowGroups[lastGroupIndex];
			final int groupSize = lastGroup.length;
			final int[] newLastGroup = new int[groupSize + 1];
			System.arraycopy(lastGroup, 0, newLastGroup, 0, groupSize);
			newLastGroup[groupSize] = rowIndex;
			newRowGroups[lastGroupIndex] = newLastGroup;
		}
		setRowGroups(newRowGroups);
	}

	// Layout Algorithm *****************************************************

	/**
	 * Initializes two lists for columns and rows that hold a column's or row's
	 * components that span only this column or row.
	 * <p>
	 * 
	 * Iterates over all components and their associated constraints; every
	 * component that has a column span or row span of 1 is put into the
	 * column's or row's component list.
	 * <p>
	 * 
	 * As of the Forms version 1.0.x invisible components are not taken into
	 * account when the container is layed out. See the TODO in the JavaDoc
	 * class commment for details on this issue.
	 */
	private void initializeColAndRowComponentLists() {
		this.colComponents = new LinkedList[getColumnCount()];
		for (int i = 0; i < getColumnCount(); i++) {
			this.colComponents[i] = new LinkedList();
		}

		this.rowComponents = new LinkedList[getRowCount()];
		for (int i = 0; i < getRowCount(); i++) {
			this.rowComponents[i] = new LinkedList();
		}

		for (final Iterator i = this.controls.iterator(); i.hasNext();) {
			final Control control = (Control) i.next();
			// This has been commented out, because for SWT the layout process
			// could occurr while all controls are invisible.
			// if (!control.isVisible()) {
			// continue;
			// }

			final CellConstraints constraints = (CellConstraints) control
					.getLayoutData();
			if (constraints.gridWidth == 1) {
				this.colComponents[constraints.gridX - 1].add(control);
			}

			if (constraints.gridHeight == 1) {
				this.rowComponents[constraints.gridY - 1].add(control);
			}
		}
	}

	/**
	 * Computes and returns the layout size of the given <code>parent</code>
	 * container using the specified measures.
	 * 
	 * @param parent
	 *            the container in which to do the layout
	 * @param defaultWidthMeasure
	 *            the measure used to compute the default width
	 * @param defaultHeightMeasure
	 *            the measure used to compute the default height
	 * @return the layout size of the <code>parent</code> container
	 */
	private Point computeLayoutSize(final Composite parent,
			final Measure defaultWidthMeasure,
			final Measure defaultHeightMeasure) {
		initializeColAndRowComponentLists();
		final int[] colWidths = maximumSizes(parent, this.colSpecs,
				this.colComponents, this.minimumWidthMeasure,
				this.preferredWidthMeasure, defaultWidthMeasure);
		final int[] rowHeights = maximumSizes(parent, this.rowSpecs,
				this.rowComponents, this.minimumHeightMeasure,
				this.preferredHeightMeasure, defaultHeightMeasure);
		final int[] groupedWidths = groupedSizes(this.colGroupIndices,
				colWidths);
		final int[] groupedHeights = groupedSizes(this.rowGroupIndices,
				rowHeights);

		// Convert sizes to origins.
		final int[] xOrigins = computeOrigins(groupedWidths, 0);
		final int[] yOrigins = computeOrigins(groupedHeights, 0);

		final int width1 = sum(groupedWidths);
		final int height1 = sum(groupedHeights);
		int maxWidth = width1;
		int maxHeight = height1;

		/*
		 * Take components that span multiple columns or rows into account. This
		 * shall be done if and only if a component spans an interval that can
		 * grow.
		 */
		// First computes the maximum number of cols/rows a component
		// can span without spanning a growing column.
		final int[] maxFixedSizeColsTable = computeMaximumFixedSpanTable(this.colSpecs);
		final int[] maxFixedSizeRowsTable = computeMaximumFixedSpanTable(this.rowSpecs);

		for (final Iterator i = this.controls.iterator(); i.hasNext();) {
			final Control control = (Control) i.next();
			if (!control.isVisible()) {
				continue;
			}

			final CellConstraints constraints = (CellConstraints) control
					.getLayoutData();
			if (constraints.gridWidth > 1
					&& constraints.gridWidth > maxFixedSizeColsTable[constraints.gridX - 1]) {
				// int compWidth = minimumWidthMeasure.sizeOf(component);
				final int compWidth = defaultWidthMeasure.sizeOf(control);
				// int compWidth = preferredWidthMeasure.sizeOf(component);
				final int gridX1 = constraints.gridX - 1;
				final int gridX2 = gridX1 + constraints.gridWidth;
				final int lead = xOrigins[gridX1];
				final int trail = width1 - xOrigins[gridX2];
				final int myWidth = lead + compWidth + trail;
				if (myWidth > maxWidth) {
					maxWidth = myWidth;
				}
			}

			if (constraints.gridHeight > 1
					&& constraints.gridHeight > maxFixedSizeRowsTable[constraints.gridY - 1]) {
				// int compHeight = minimumHeightMeasure.sizeOf(component);
				final int compHeight = defaultHeightMeasure.sizeOf(control);
				// int compHeight =
				// preferredHeightMeasure.sizeOf(component);
				final int gridY1 = constraints.gridY - 1;
				final int gridY2 = gridY1 + constraints.gridHeight;
				final int lead = yOrigins[gridY1];
				final int trail = height1 - yOrigins[gridY2];
				final int myHeight = lead + compHeight + trail;
				if (myHeight > maxHeight) {
					maxHeight = myHeight;
				}
			}
		}
		final Rectangle insets = InsetHelper.caluclateInsets(parent);
		final int width = maxWidth + insets.x + insets.width;
		final int height = maxHeight + insets.y + insets.height;
		return new Point(width, height);
	}

	/**
	 * Computes and returns the grid's origins.
	 * 
	 * @param container
	 *            the layout container
	 * @param totalSize
	 *            the total size to assign
	 * @param offset
	 *            the offset from left or top margin
	 * @param formSpecs
	 *            the column or row specs, resp.
	 * @param componentLists
	 *            the components list for each col/row
	 * @param minMeasure
	 *            the measure used to determin min sizes
	 * @param prefMeasure
	 *            the measure used to determin pre sizes
	 * @param groupIndices
	 *            the group specification
	 * @return an int array with the origins
	 */
	private int[] computeGridOrigins(final Composite composite,
			final int totalSize, final int offset, final List formSpecs,
			final List[] componentLists, final int[][] groupIndices,
			final Measure minMeasure, final Measure prefMeasure) {
		/*
		 * For each spec compute the minimum and preferred size that is the
		 * maximum of all component minimum and preferred sizes resp.
		 */
		final int[] minSizes = maximumSizes(composite, formSpecs,
				componentLists, minMeasure, prefMeasure, minMeasure);
		final int[] prefSizes = maximumSizes(composite, formSpecs,
				componentLists, minMeasure, prefMeasure, prefMeasure);

		final int[] groupedMinSizes = groupedSizes(groupIndices, minSizes);
		final int[] groupedPrefSizes = groupedSizes(groupIndices, prefSizes);
		final int totalMinSize = sum(groupedMinSizes);
		final int totalPrefSize = sum(groupedPrefSizes);
		final int[] compressedSizes = compressedSizes(formSpecs, totalSize,
				totalMinSize, totalPrefSize, groupedMinSizes, prefSizes);
		final int[] groupedSizes = groupedSizes(groupIndices, compressedSizes);
		final int totalGroupedSize = sum(groupedSizes);
		final int[] sizes = distributedSizes(formSpecs, totalSize,
				totalGroupedSize, groupedSizes);
		return computeOrigins(sizes, offset);
	}

	/**
	 * Computes origins from sizes taking the specified offset into account.
	 * 
	 * @param sizes
	 *            the array of sizes
	 * @param offset
	 *            an offset for the first origin
	 * @return an array of origins
	 */
	private int[] computeOrigins(final int[] sizes, final int offset) {
		final int count = sizes.length;
		final int[] origins = new int[count + 1];
		origins[0] = offset;
		for (int i = 1; i <= count; i++) {
			origins[i] = origins[i - 1] + sizes[i - 1];
		}
		return origins;
	}

	/**
	 * Lays out the components using the given x and y origins, the column and
	 * row specifications, and the component constraints.
	 * <p>
	 * 
	 * The actual computation is done by each component's form constraint
	 * object. We just compute the cell, the cell bounds and then hand over the
	 * component, cell bounds, and measure to the form constraints. This will
	 * allow potential subclasses of <code>CellConstraints</code> to do
	 * special micro-layout corrections. For example, such a subclass could map
	 * JComponent classes to visual layout bounds that may lead to a slightly
	 * different bounds.
	 * 
	 * @param x
	 *            an int array of the horizontal origins
	 * @param y
	 *            an int array of the vertical origins
	 */
	private void layoutComponents(final int[] x, final int[] y) {
		final Rectangle cellBounds = new Rectangle(0, 0, 0, 0);
		for (final Iterator i = this.controls.iterator(); i.hasNext();) {
			final Control control = (Control) i.next();
			final CellConstraints constraints = (CellConstraints) control
					.getLayoutData();

			final int gridX = constraints.gridX - 1;
			final int gridY = constraints.gridY - 1;
			final int gridWidth = constraints.gridWidth;
			final int gridHeight = constraints.gridHeight;
			cellBounds.x = x[gridX];
			cellBounds.y = y[gridY];
			cellBounds.width = x[gridX + gridWidth] - cellBounds.x;
			cellBounds.height = y[gridY + gridHeight] - cellBounds.y;

			constraints.setBounds(control, this, cellBounds,
					this.minimumWidthMeasure, this.minimumHeightMeasure,
					this.preferredWidthMeasure, this.preferredHeightMeasure);
		}
	}

	/**
	 * Invalidates the component size caches.
	 */
	private void invalidateCaches() {
		this.componentSizeCache.invalidate();
	}

	/**
	 * Computes and returns the sizes for the given form specs, component lists
	 * and measures fot minimum, preferred, and default size.
	 * 
	 * @param container
	 *            the layout container
	 * @param formSpecs
	 *            the column or row specs, resp.
	 * @param componentLists
	 *            the components list for each col/row
	 * @param minMeasure
	 *            the measure used to determin min sizes
	 * @param prefMeasure
	 *            the measure used to determin pre sizes
	 * @param defaultMeasure
	 *            the measure used to determin default sizes
	 * @return the column or row sizes
	 */
	private int[] maximumSizes(final Composite composite, final List formSpecs,
			final List[] componentLists, final Measure minMeasure,
			final Measure prefMeasure, final Measure defaultMeasure) {
		FormSpec formSpec;
		final int size = formSpecs.size();
		final int[] result = new int[size];
		for (int i = 0; i < size; i++) {
			formSpec = (FormSpec) formSpecs.get(i);
			result[i] = formSpec.maximumSize(composite, componentLists[i],
					minMeasure, prefMeasure, defaultMeasure);
		}
		return result;
	}

	/**
	 * Computes and returns the compressed sizes. Compresses space for columns
	 * and rows iff the available space is less than the total preferred size
	 * but more than the total minimum size.
	 * <p>
	 * 
	 * Only columns and rows that are specified to be compressable will be
	 * affected. You can specify a column and row as compressable by giving it
	 * the component size <tt>default</tt>.
	 * 
	 * @param formSpecs
	 *            the column or row specs to use
	 * @param totalSize
	 *            the total available size
	 * @param totalMinSize
	 *            the sum of all minimum sizes
	 * @param totalPrefSize
	 *            the sum of all preferred sizes
	 * @param minSizes
	 *            an int array of column/row minimum sizes
	 * @param prefSizes
	 *            an int array of column/row preferred sizes
	 * @return an int array of compressed column/row sizes
	 */
	private int[] compressedSizes(final List formSpecs, final int totalSize,
			final int totalMinSize, final int totalPrefSize,
			final int[] minSizes, final int[] prefSizes) {

		// If we have less space than the total min size, answer the min sizes.
		if (totalSize < totalMinSize) {
			return minSizes;
		}
		// If we have more space than the total pref size, answer the pref
		// sizes.
		if (totalSize >= totalPrefSize) {
			return prefSizes;
		}

		final int count = formSpecs.size();
		final int[] sizes = new int[count];

		final double totalCompressionSpace = totalPrefSize - totalSize;
		final double maxCompressionSpace = totalPrefSize - totalMinSize;
		final double compressionFactor = totalCompressionSpace
				/ maxCompressionSpace;

		// System.out.println("Total compression space=" +
		// totalCompressionSpace);
		// System.out.println("Max compression space =" + maxCompressionSpace);
		// System.out.println("Compression factor =" + compressionFactor);

		for (int i = 0; i < count; i++) {
			final FormSpec formSpec = (FormSpec) formSpecs.get(i);
			sizes[i] = prefSizes[i];
			if (formSpec.getSize().compressible()) {
				sizes[i] -= (int) Math.round((prefSizes[i] - minSizes[i])
						* compressionFactor);
			}
		}
		return sizes;
	}

	/**
	 * Computes and returns the grouped sizes. Gives grouped columns and rows
	 * the same size.
	 * 
	 * @param groups
	 *            the group specification
	 * @param rawSizes
	 *            the raw sizes before the grouping
	 * @return the grouped sizes
	 */
	private int[] groupedSizes(final int[][] groups, final int[] rawSizes) {
		// Return the compressed sizes if there are no groups.
		if (groups == null || groups.length == 0) {
			return rawSizes;
		}

		// Initialize the result with the given compressed sizes.
		final int[] sizes = new int[rawSizes.length];
		for (int i = 0; i < sizes.length; i++) {
			sizes[i] = rawSizes[i];
		}

		// For each group equalize the sizes.
		for (int group = 0; group < groups.length; group++) {
			final int[] groupIndices = groups[group];
			int groupMaxSize = 0;
			// Compute the group's maximum size.
			for (int i = 0; i < groupIndices.length; i++) {
				final int index = groupIndices[i] - 1;
				groupMaxSize = Math.max(groupMaxSize, sizes[index]);
			}
			// Set all sizes of this group to the group's maximum size.
			for (int i = 0; i < groupIndices.length; i++) {
				final int index = groupIndices[i] - 1;
				sizes[index] = groupMaxSize;
			}
		}
		return sizes;
	}

	/**
	 * Distributes free space over columns and rows and returns the sizes after
	 * this distribution process.
	 * 
	 * @param formSpecs
	 *            the column/row specifications to work with
	 * @param totalSize
	 *            the total available size
	 * @param totalPrefSize
	 *            the sum of all preferred sizes
	 * @param inputSizes
	 *            the input sizes
	 * @return the distributed sizes
	 */
	private int[] distributedSizes(final List formSpecs, final int totalSize,
			final int totalPrefSize, final int[] inputSizes) {
		final double totalFreeSpace = totalSize - totalPrefSize;
		// Do nothing if there's no free space.
		if (totalFreeSpace < 0) {
			return inputSizes;
		}

		// Compute the total weight.
		final int count = formSpecs.size();
		double totalWeight = 0.0;
		for (int i = 0; i < count; i++) {
			final FormSpec formSpec = (FormSpec) formSpecs.get(i);
			totalWeight += formSpec.getResizeWeight();
		}

		// Do nothing if there's no resizing column.
		if (totalWeight == 0.0) {
			return inputSizes;
		}

		final int[] sizes = new int[count];

		double restSpace = totalFreeSpace;
		int roundedRestSpace = (int) totalFreeSpace;
		for (int i = 0; i < count; i++) {
			final FormSpec formSpec = (FormSpec) formSpecs.get(i);
			final double weight = formSpec.getResizeWeight();
			if (weight == FormSpec.NO_GROW) {
				sizes[i] = inputSizes[i];
			} else {
				final double roundingCorrection = restSpace - roundedRestSpace;
				final double extraSpace = totalFreeSpace * weight / totalWeight;
				final double correctedExtraSpace = extraSpace
						- roundingCorrection;
				final int roundedExtraSpace = (int) Math
						.round(correctedExtraSpace);
				sizes[i] = inputSizes[i] + roundedExtraSpace;
				restSpace -= extraSpace;
				roundedRestSpace -= roundedExtraSpace;
			}
		}
		return sizes;
	}

	/**
	 * Computes and returns the sum of integers in the given array of ints.
	 * 
	 * @param sizes
	 *            an array of ints to sum up
	 * @return the sum of ints in the array
	 */
	private int sum(final int[] sizes) {
		int sum = 0;
		for (int i = sizes.length - 1; i >= 0; i--) {
			sum += sizes[i];
		}
		return sum;
	}

	/**
	 * Computes and returns a table that maps a column/row index to the maximum
	 * number of columns/rows that a component can span without spanning a
	 * growing column.
	 * <p>
	 * 
	 * Iterates over the specs from right to left/bottom to top, sets the table
	 * value to zero if a spec can grow, otherwise increases the span by one.
	 * <p>
	 * 
	 * <strong>Examples:</strong>
	 * 
	 * <pre>
	 * &quot;pref, 4dlu, pref, 2dlu, p:grow, 2dlu,      pref&quot; -&gt;
	 * [4,    3,    2,    1,    0,      MAX_VALUE, MAX_VALUE]
	 * 
	 * &quot;p:grow, 4dlu, p:grow, 9dlu,      pref&quot; -&gt;
	 * [0,      1,    0,      MAX_VALUE, MAX_VALUE]
	 * 
	 * &quot;p, 4dlu, p, 2dlu, 0:grow&quot; -&gt;
	 * [4, 3,    2, 1,    0]
	 * </pre>
	 * 
	 * @param formSpecs
	 *            the column specs or row specs
	 * @return a table that maps a spec index to the maximum span for fixed size
	 *         specs
	 */
	private int[] computeMaximumFixedSpanTable(final List formSpecs) {
		final int size = formSpecs.size();
		final int[] table = new int[size];
		int maximumFixedSpan = Integer.MAX_VALUE; // Could be 1
		for (int i = size - 1; i >= 0; i--) {
			final FormSpec spec = (FormSpec) formSpecs.get(i); // ArrayList
			// access
			if (spec.canGrow()) {
				maximumFixedSpan = 0;
			}
			table[i] = maximumFixedSpan;
			if (maximumFixedSpan < Integer.MAX_VALUE) {
				maximumFixedSpan++;
			}
		}
		return table;
	}

	// Measuring Component Sizes ********************************************

	/**
	 * An interface that describes how to measure a <code>Component</code>.
	 * Used to abstract from horizontal and vertical dimensions as well as
	 * minimum and preferred sizes.
	 * 
	 * @since 1.1
	 */
	public static interface Measure {

		/**
		 * Computes and returns the size of the given <code>Component</code>.
		 * 
		 * @param control
		 *            the component to measure
		 * @return the component's size
		 */
		int sizeOf(Control control);
	}

	/**
	 * An abstract implementation of the <code>Measure</code> interface that
	 * caches component sizes.
	 */
	private abstract static class CachingMeasure implements Measure,
			Serializable {

		/**
		 * Holds previously requested component sizes. Used to minimize size
		 * requests to subcomponents.
		 */
		protected final ComponentSizeCache cache;

		CachingMeasure(final ComponentSizeCache cache) {
			this.cache = cache;
		}

	}

	/**
	 * Measures a component by computing its minimum width.
	 */
	private static final class MinimumWidthMeasure extends CachingMeasure {
		/**
		 * 
		 */
		private static final long serialVersionUID = 4511656831703233770L;

		MinimumWidthMeasure(final ComponentSizeCache cache) {
			super(cache);
		}

		/**
		 * @see de.esw.swt.forms.layout.FormLayout.Measure#sizeOf(org.eclipse.swt.widgets.Control)
		 */
		public int sizeOf(final Control c) {
			return this.cache.getMinimumSize(c).x;
		}
	}

	/**
	 * Measures a component by computing its minimum height.
	 */
	private static final class MinimumHeightMeasure extends CachingMeasure {
		/**
		 * 
		 */
		private static final long serialVersionUID = 8041755299779940959L;

		MinimumHeightMeasure(final ComponentSizeCache cache) {
			super(cache);
		}

		/**
		 * @see de.esw.swt.forms.layout.FormLayout.Measure#sizeOf(org.eclipse.swt.widgets.Control)
		 */
		public int sizeOf(final Control c) {
			return this.cache.getMinimumSize(c).y;
		}
	}

	/**
	 * Measures a component by computing its preferred width.
	 */
	private static final class PreferredWidthMeasure extends CachingMeasure {
		/**
		 * 
		 */
		private static final long serialVersionUID = -6028810133887344159L;

		PreferredWidthMeasure(final ComponentSizeCache cache) {
			super(cache);
		}

		/**
		 * @see de.esw.swt.forms.layout.FormLayout.Measure#sizeOf(org.eclipse.swt.widgets.Control)
		 */
		public int sizeOf(final Control c) {
			return this.cache.getPreferredSize(c).x;
		}
	}

	/**
	 * Measures a component by computing its preferred height.
	 */
	private static final class PreferredHeightMeasure extends CachingMeasure {
		/**
		 * 
		 */
		private static final long serialVersionUID = 3777095602999005792L;

		PreferredHeightMeasure(final ComponentSizeCache cache) {
			super(cache);
		}

		/**
		 * @see de.esw.swt.forms.layout.FormLayout.Measure#sizeOf(org.eclipse.swt.widgets.Control)
		 */
		public int sizeOf(final Control c) {
			return this.cache.getPreferredSize(c).y;
		}
	}

	// Caching Component Sizes **********************************************

	/**
	 * A cache for component minimum and preferred sizes. Used to reduce the
	 * requests to determine a component's size.
	 */
	private static final class ComponentSizeCache implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1399928854965514659L;

		/** Maps components to their minimum sizes. */
		private final Map minimumSizes;

		/** Maps components to their preferred sizes. */
		private final Map preferredSizes;

		/**
		 * Constructs a <code>ComponentSizeCache</code>.
		 * 
		 * @param initialCapacity
		 *            the initial cache capacity
		 */
		ComponentSizeCache(final int initialCapacity) {
			this.minimumSizes = new HashMap(initialCapacity);
			this.preferredSizes = new HashMap(initialCapacity);
		}

		/**
		 * Invalidates the cache. Clears all stored size information.
		 */
		void invalidate() {
			this.minimumSizes.clear();
			this.preferredSizes.clear();
		}

		/**
		 * Returns the minimum size for the given component. Tries to look up
		 * the value from the cache; lazily creates the value if it has not been
		 * requested before.
		 * 
		 * @param component
		 *            the component to compute the minimum size
		 * @return the component's minimum size
		 */
		Point getMinimumSize(final Control control) {
			Point size = (Point) this.minimumSizes.get(control);
			if (size == null) {
				size = control.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
				this.minimumSizes.put(control, size);
			}
			return size;
		}

		/**
		 * Returns the preferred size for the given component. Tries to look up
		 * the value from the cache; lazily creates the value if it has not been
		 * requested before.
		 * 
		 * @param component
		 *            the component to compute the preferred size
		 * @return the component's preferred size
		 */
		Point getPreferredSize(final Control control) {
			Point size = (Point) this.preferredSizes.get(control);
			if (size == null) {
				size = control.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
				this.preferredSizes.put(control, size);
			}
			return size;
		}

		void removeEntry(final Control control) {
			this.minimumSizes.remove(control);
			this.preferredSizes.remove(control);
		}
	}

	// Exposing the Layout Information **************************************

	/**
	 * Computes and returns the horizontal and vertical grid origins. Performs
	 * the same layout process as <code>#layoutContainer</code> but does not
	 * layout the components.
	 * <p>
	 * 
	 * This method has been added only to make it easier to debug the form
	 * layout. <strong>You must not call this method directly; It may be removed
	 * in a future release or the visibility may be reduced.</strong>
	 * 
	 * @param parent
	 *            the <code>Container</code> to inspect
	 * @return an object that comprises the grid x and y origins
	 */
	public LayoutInfo getLayoutInfo(final Composite parent) {
		initializeColAndRowComponentLists();
		final Point size = parent.getSize();

		final Rectangle insets = InsetHelper.caluclateInsets(parent);
		final int totalWidth = size.x - insets.x - insets.width;
		final int totalHeight = size.y - insets.y - insets.height;

		final int[] x = computeGridOrigins(parent, totalWidth, insets.x,
				this.colSpecs, this.colComponents, this.colGroupIndices,
				this.minimumWidthMeasure, this.preferredWidthMeasure);
		final int[] y = computeGridOrigins(parent, totalHeight, insets.y,
				this.rowSpecs, this.rowComponents, this.rowGroupIndices,
				this.minimumHeightMeasure, this.preferredHeightMeasure);
		return new LayoutInfo(x, y);
	}

	/**
	 * Stores column and row origins.
	 */
	public static final class LayoutInfo {

		/**
		 * Holds the origins of the columns.
		 */
		public final int[] columnOrigins;

		/**
		 * Holds the origins of the rows.
		 */
		public final int[] rowOrigins;

		LayoutInfo(final int[] xOrigins, final int[] yOrigins) {
			this.columnOrigins = xOrigins;
			this.rowOrigins = yOrigins;
		}

		/**
		 * Returns the layout's horizontal origin, the origin of the first
		 * column.
		 * 
		 * @return the layout's horizontal origin, the origin of the first
		 *         column.
		 */
		public int getX() {
			return this.columnOrigins[0];
		}

		/**
		 * Returns the layout's vertical origin, the origin of the first row.
		 * 
		 * @return the layout's vertical origin, the origin of the first row.
		 */
		public int getY() {
			return this.rowOrigins[0];
		}

		/**
		 * Returns the layout's width, the size between the first and the last
		 * column origin.
		 * 
		 * @return the layout's width.
		 */
		public int getWidth() {
			return this.columnOrigins[this.columnOrigins.length - 1]
					- this.columnOrigins[0];
		}

		/**
		 * Returns the layout's height, the size between the first and last row.
		 * 
		 * @return the layout's height.
		 */
		public int getHeight() {
			return this.rowOrigins[this.rowOrigins.length - 1]
					- this.rowOrigins[0];
		}

	}

	// Helper Code **********************************************************

	/**
	 * Creates and returns a deep copy of the given array. Unlike
	 * <code>#clone</code> that performs a shallow copy, this method copies
	 * both array levels.
	 * 
	 * @param array
	 *            the array to clone
	 * @return a deep copy of the given array
	 * 
	 * @see Object#clone()
	 */
	private int[][] deepClone(final int[][] array) {
		final int[][] result = new int[array.length][];
		for (int i = 0; i < result.length; i++) {
			result[i] = (int[]) array[i].clone();
		}
		return result;
	}

	// Serialization ********************************************************

	/**
	 * In addition to the default serialization mechanism this class invalidates
	 * the component size cache. The cache will be populated again after the
	 * deserialization. Also, the fields <code>colComponents</code> and
	 * <code>rowComponents</code> have been marked as transient to exclude
	 * them from the serialization.
	 */
	private void writeObject(final ObjectOutputStream out) throws IOException {
		invalidateCaches();
		out.defaultWriteObject();
	}

	// Implementing the Layout Interface *********

	/**
	 * @see org.eclipse.swt.widgets.Layout#computeSize(org.eclipse.swt.widgets.Composite,
	 *      int, int, boolean)
	 */
	protected Point computeSize(final Composite composite, final int wHint,
			final int hHint, final boolean flushCache) {
		if (flushCache) {
			invalidateCaches();
		}
		return computeLayoutSize(composite, this.preferredWidthMeasure,
				this.preferredHeightMeasure);
	}

	/**
	 * @see org.eclipse.swt.widgets.Layout#layout(org.eclipse.swt.widgets.Composite,
	 *      boolean)
	 */
	protected void layout(final Composite composite, final boolean flushCache) {
		if (flushCache) {
			invalidateCaches();
		}

		initializeColAndRowComponentLists();
		final Rectangle clientArea = composite.getClientArea();
		final Rectangle insets = InsetHelper.caluclateInsets(composite);
		final int totalWidth = clientArea.width;
		final int totalHeight = clientArea.height;

		final int[] x = computeGridOrigins(composite, totalWidth, insets.x,
				this.colSpecs, this.colComponents, this.colGroupIndices,
				this.minimumWidthMeasure, this.preferredWidthMeasure);
		final int[] y = computeGridOrigins(composite, totalHeight, insets.y,
				this.rowSpecs, this.rowComponents, this.rowGroupIndices,
				this.minimumHeightMeasure, this.preferredHeightMeasure);

		layoutComponents(x, y);
	}

	// Debug Helper Code ****************************************************

	/*
	 * // Prints the given column widths and row heights. private void
	 * printSizes(String title, int[] colWidths, int[] rowHeights) {
	 * System.out.println(); System.out.println(title); int totalWidth = 0;
	 * System.out.print("Column widths: "); for (int i=0; i < getColumnCount();
	 * i++) { int width = colWidths[i]; totalWidth += width;
	 * System.out.print(width + ", "); } System.out.println(" Total=" +
	 * totalWidth);
	 * 
	 * int totalHeight = 0; System.out.print("Row heights: "); for (int i=0; i <
	 * getRowCount(); i++) { int height = rowHeights[i]; totalHeight += height;
	 * System.out.print(height + ", "); } System.out.println(" Total=" +
	 * totalHeight); System.out.println(); }
	 * 
	 */

}
