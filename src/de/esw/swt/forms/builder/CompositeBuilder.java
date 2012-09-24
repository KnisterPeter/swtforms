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

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

import de.esw.swt.forms.factories.ComponentFactory;
import de.esw.swt.forms.factories.DefaultComponentFactory;
import de.esw.swt.forms.layout.CellConstraints;
import de.esw.swt.forms.layout.FormLayout;

/**
 * An general purpose panel builder that uses the {@link FormLayout} to lay out
 * <code>JPanel</code>s. It provides convenience methods to set a default
 * border and to add labels, titles and titled separators.
 * <p>
 * 
 * The CompositeBuilder is the working horse for layouts when more specialized
 * builders like the {@link ButtonBarBuilder} or {@link DefaultFormBuilder} are
 * inappropriate.
 * <p>
 * 
 * The Forms tutorial includes several examples that present and compare
 * different style to build with the CompositeBuilder: static row numbers vs.
 * row variable, explicit CellConstraints vs. builder cursor, static rows vs.
 * dynamically added rows. Also, you may check out the Tips &amp; Tricks section
 * of the Forms HTML documentation.
 * <p>
 * 
 * The text arguments passed to the methods <code>#addLabel</code>,
 * <code>#addTitle</code>, and <code>#addSeparator</code> can contain an
 * optional mnemonic marker. The mnemonic and mnemonic index are indicated by a
 * single ampersand (<tt>&amp;</tt>). For example
 * <tt>&quot;&amp;Save&quot</tt>, or <tt>&quot;Save&nbsp;&amp;as&quot</tt>.
 * To use the ampersand itself duplicate it, for example
 * <tt>&quot;Look&amp;&amp;Feel&quot</tt>.
 * <p>
 * 
 * <strong>Example:</strong><br>
 * This example creates a panel with 3 columns and 3 rows.
 * 
 * <pre>
 * FormLayout layout = new FormLayout(&quot;right:pref, 6dlu, 50dlu, 4dlu, default&quot;, // columns 
 * 		&quot;pref, 3dlu, pref, 3dlu, pref&quot;); // rows
 * 
 * CompositeBuilder builder = new CompositeBuilder(layout);
 * CellConstraints cc = new CellConstraints();
 * builder.addLabel(&quot;&amp;Title&quot;, cc.xy(1, 1));
 * builder.add(new JTextField(), cc.xywh(3, 1, 3, 1));
 * builder.addLabel(&quot;&amp;Price&quot;, cc.xy(1, 3));
 * builder.add(new JTextField(), cc.xy(3, 3));
 * builder.addLabel(&quot;&amp;Author&quot;, cc.xy(1, 5));
 * builder.add(new JTextField(), cc.xy(3, 5));
 * builder.add(new JButton(&quot;...&quot;), cc.xy(5, 5));
 * return builder.getComposite();
 * </pre>
 * 
 * @author Karsten Lentzsch
 * @version $Revision: 1.5 $
 * 
 * @see com.jgoodies.forms.factories.ComponentFactory
 * @see I15dCompositeBuilder
 * @see DefaultFormBuilder
 */
public class CompositeBuilder extends AbstractFormBuilder {

	/**
	 * Refers to a factory that is used to create labels, titles and paragraph
	 * separators.
	 */
	private ComponentFactory componentFactory;

	// Instance Creation ****************************************************

	/**
	 * Constructs a <code>CompositeBuilder</code> for the given FormLayout and
	 * layout container.
	 * 
	 * @param parent
	 *            the parent composite to form to
	 * @param layout
	 *            the FormLayout to use
	 */
	public CompositeBuilder(Composite parent, FormLayout layout) {
		super(layout, new Composite(parent, SWT.NONE));
	}

	/**
	 * Constructs a <code>CompositeBuilder</code> for the given FormLayout and
	 * layout container.
	 * 
	 * @param layout
	 *            the FormLayout to use
	 * @param composite
	 *            the layout container to build on
	 */
	public CompositeBuilder(FormLayout layout, Composite composite) {
		super(layout, composite);
	}

	// Accessors ************************************************************

	/**
	 * Returns the panel used to build the form.
	 * 
	 * @return the panel used by this builder to build the form
	 */
	public final Composite getComposite() {
		return getContainer();
	}

	// Frequently Used Panel Properties ***************************************

	/**
	 * Sets the panel's background color.
	 * 
	 * @param background
	 *            the color to set as new background
	 * 
	 * @see Composite#setBackground(Color)
	 * 
	 * @since 1.1
	 */
	public final void setBackground(Color background) {
		getComposite().setBackground(background);
	}

	// Adding Labels **********************************************************

	/**
	 * Adds a textual label to the form using the default constraints.
	 * <p>
	 * 
	 * <pre>
	 * addLabel(&quot;Name&quot;); // No Mnemonic
	 * addLabel(&quot;N&amp;ame&quot;); // Mnemonic is 'a'
	 * addLabel(&quot;Save &amp;as&quot;); // Mnemonic is the second 'a'
	 * addLabel(&quot;Look&amp;&amp;Feel&quot;); // No mnemonic, text is &quot;look&amp;feel&quot;
	 * </pre>
	 * 
	 * @param textWithMnemonic
	 *            the label's text - may contain an ampersand (<tt>&amp;</tt>)
	 *            to mark a mnemonic
	 * @return the new label
	 * 
	 * @see ComponentFactory
	 */
	public final Label addLabel(String textWithMnemonic) {
		return addLabel(textWithMnemonic, cellConstraints());
	}

	/**
	 * Adds a textual label to the form using the specified constraints.
	 * <p>
	 * 
	 * <pre>
	 * addLabel(&quot;Name&quot;, cc.xy(1, 1)); // No Mnemonic
	 * addLabel(&quot;N&amp;ame&quot;, cc.xy(1, 1)); // Mnemonic is 'a'
	 * addLabel(&quot;Save &amp;as&quot;, cc.xy(1, 1)); // Mnemonic is the second 'a'
	 * addLabel(&quot;Look&amp;&amp;Feel&quot;, cc.xy(1, 1)); // No mnemonic, text is &quot;look&amp;feel&quot;
	 * </pre>
	 * 
	 * @param textWithMnemonic
	 *            the label's text - may contain an ampersand (<tt>&amp;</tt>)
	 *            to mark a mnemonic
	 * @param constraints
	 *            the label's cell constraints
	 * @return the new label
	 * 
	 * @see ComponentFactory
	 */
	public final Label addLabel(String textWithMnemonic,
			CellConstraints constraints) {
		Label label = getComponentFactory().createLabel(getComposite(),
				textWithMnemonic);
		// TODO: Set constraints
		add(label, constraints);
		return label;
	}

	/**
	 * Adds a textual label to the form using the specified constraints.
	 * <p>
	 * 
	 * <pre>
	 * addLabel(&quot;Name&quot;, &quot;1, 1&quot;); // No Mnemonic
	 * addLabel(&quot;N&amp;ame&quot;, &quot;1, 1&quot;); // Mnemonic is 'a'
	 * addLabel(&quot;Save &amp;as&quot;, &quot;1, 1&quot;); // Mnemonic is the second 'a'
	 * addLabel(&quot;Look&amp;&amp;Feel&quot;, &quot;1, 1&quot;); // No mnemonic, text is &quot;look&amp;feel&quot;
	 * </pre>
	 * 
	 * @param textWithMnemonic
	 *            the label's text - may contain an ampersand (<tt>&amp;</tt>)
	 *            to mark a mnemonic
	 * @param encodedConstraints
	 *            a string representation for the constraints
	 * @return the new label
	 * 
	 * @see ComponentFactory
	 */
	public final Label addLabel(String textWithMnemonic,
			String encodedConstraints) {
		return addLabel(textWithMnemonic, new CellConstraints(
				encodedConstraints));
	}

	// Adding Label with related Component ************************************

	/**
	 * Adds a label and component to the panel using the given cell constraints.
	 * <p>
	 * 
	 * <strong>Note:</strong> The {@link CellConstraints} objects for the label
	 * and the component must be different. Cell constraints are implicitly
	 * cloned by the <code>FormLayout</code> when added to the container.
	 * However, in this case you may be tempted to reuse a
	 * <code>CellConstraints</code> object in the same way as with many other
	 * builder methods that require a single <code>CellConstraints</code>
	 * parameter. The pitfall is that the methods
	 * <code>CellConstraints.xy*(...)</code> just set the coordinates but do
	 * <em>not</em> create a new instance. And so the second invocation of
	 * <code>xy*(...)</code> overrides the settings performed in the first
	 * invocation before the object is cloned by the <code>FormLayout</code>.
	 * <p>
	 * 
	 * <strong>Wrong:</strong>
	 * 
	 * <pre>
	 * CellConstraints cc = new CellConstraints();
	 * builder.add(nameLabel, cc.xy(1, 7), // will be modified by the code below
	 * 		nameField, cc.xy(3, 7) // sets the single instance to (3, 7)
	 * 		);
	 * </pre>
	 * 
	 * <strong>Correct:</strong>
	 * 
	 * <pre>
	 * // Using a single CellConstraints instance and cloning
	 * CellConstraints cc = new CellConstraints();
	 * builder.add(nameLabel, (CellConstraints) cc.xy(1, 7).clone(), // cloned before the next modification 
	 * 		nameField, cc.xy(3, 7) // sets this instance to (3, 7)
	 * 		);
	 * 
	 * // Using two CellConstraints instances 
	 * CellConstraints cc1 = new CellConstraints();
	 * CellConstraints cc2 = new CellConstraints();
	 * builder.add(nameLabel, cc1.xy(1, 7), // sets instance 1 to (1, 7)
	 * 		nameField, cc2.xy(3, 7) // sets instance 2 to (3, 7)
	 * 		);
	 * </pre>
	 * 
	 * @param label
	 *            the label to add
	 * @param labelConstraints
	 *            the label's cell constraints
	 * @param component
	 *            the component to add
	 * @param componentConstraints
	 *            the component's cell constraints
	 * @return the added label
	 * @throws IllegalArgumentException
	 *             if the same cell constraints instance is used for the label
	 *             and the component
	 * 
	 * @see DefaultFormBuilder
	 */
	public final Label add(Label label, CellConstraints labelConstraints,
			Control control, CellConstraints componentConstraints) {
		if (labelConstraints == componentConstraints) {
			throw new IllegalArgumentException(
					"You must provide two CellConstraints instances, "
							+ "one for the label and one for the component.\n"
							+ "Consider using #clone(). See the JavaDocs for details.");
		}

		// TODO: Set constraints
		add(label, labelConstraints);
		// TODO: Set constraints
		add(control, componentConstraints);
		// label.setLabelFor(control);
		return label;
	}

	/**
	 * Adds a label and component to the panel using the given cell constraints.
	 * <p>
	 * 
	 * <strong>Note:</strong> The {@link CellConstraints} objects for the label
	 * and the component must be different. Cell constraints are implicitly
	 * cloned by the <code>FormLayout</code> when added to the container.
	 * However, in this case you may be tempted to reuse a
	 * <code>CellConstraints</code> object in the same way as with many other
	 * builder methods that require a single <code>CellConstraints</code>
	 * parameter. The pitfall is that the methods
	 * <code>CellConstraints.xy*(...)</code> just set the coordinates but do
	 * <em>not</em> create a new instance. And so the second invocation of
	 * <code>xy*(...)</code> overrides the settings performed in the first
	 * invocation before the object is cloned by the <code>FormLayout</code>.
	 * <p>
	 * 
	 * <strong>Wrong:</strong>
	 * 
	 * <pre>
	 * builder.addLabel(&quot;&amp;Name:&quot;, // Mnemonic is 'N'
	 * 		cc.xy(1, 7), // will be modified by the code below
	 * 		nameField, cc.xy(3, 7) // sets the single instance to (3, 7)
	 * 		);
	 * </pre>
	 * 
	 * <strong>Correct:</strong>
	 * 
	 * <pre>
	 * // Using a single CellConstraints instance and cloning
	 * CellConstraints cc = new CellConstraints();
	 * builder.addLabel(&quot;&amp;Name:&quot;, (CellConstraints) cc.xy(1, 7).clone(), // cloned before the next modification 
	 * 		nameField, cc.xy(3, 7) // sets this instance to (3, 7)
	 * 		);
	 * 
	 * // Using two CellConstraints instances 
	 * CellConstraints cc1 = new CellConstraints();
	 * CellConstraints cc2 = new CellConstraints();
	 * builder.addLabel(&quot;&amp;Name:&quot;, // Mnemonic is 'N'
	 * 		cc1.xy(1, 7), // sets instance 1 to (1, 7)
	 * 		nameField, cc2.xy(3, 7) // sets instance 2 to (3, 7)
	 * 		);
	 * </pre>
	 * 
	 * @param textWithMnemonic
	 *            the label's text - may contain an ampersand (<tt>&amp;</tt>)
	 *            to mark a mnemonic
	 * @param labelConstraints
	 *            the label's cell constraints
	 * @param component
	 *            the component to add
	 * @param componentConstraints
	 *            the component's cell constraints
	 * @return the added label
	 * @throws IllegalArgumentException
	 *             if the same cell constraints instance is used for the label
	 *             and the component
	 * 
	 * @see ComponentFactory
	 * @see DefaultFormBuilder
	 */
	public final Label addLabel(String textWithMnemonic,
			CellConstraints labelConstraints, Control control,
			CellConstraints componentConstraints) {

		if (labelConstraints == componentConstraints) {
			throw new IllegalArgumentException(
					"You must provide two CellConstraints instances, "
							+ "one for the label and one for the component.\n"
							+ "Consider using #clone(). See the JavaDocs for details.");
		}

		Label label = addLabel(textWithMnemonic, labelConstraints);
		add(control, componentConstraints);
		return label;
	}

	// Adding Titles ----------------------------------------------------------

	/**
	 * Adds a title label to the form using the default constraints.
	 * <p>
	 * 
	 * <pre>
	 * addTitle(&quot;Name&quot;); // No mnemonic
	 * addTitle(&quot;N&amp;ame&quot;); // Mnemonic is 'a'
	 * addTitle(&quot;Save &amp;as&quot;); // Mnemonic is the second 'a'
	 * addTitle(&quot;Look&amp;&amp;Feel&quot;); // No mnemonic, text is Look&amp;Feel
	 * </pre>
	 * 
	 * @param textWithMnemonic
	 *            the title label's text - may contain an ampersand (<tt>&amp;</tt>)
	 *            to mark a mnemonic
	 * @return the added title label
	 * 
	 * @see ComponentFactory
	 */
	public final Label addTitle(String textWithMnemonic) {
		return addTitle(textWithMnemonic, cellConstraints());
	}

	/**
	 * Adds a title label to the form using the specified constraints.
	 * <p>
	 * 
	 * <pre>
	 * addTitle(&quot;Name&quot;, cc.xy(1, 1)); // No mnemonic
	 * addTitle(&quot;N&amp;ame&quot;, cc.xy(1, 1)); // Mnemonic is 'a'
	 * addTitle(&quot;Save &amp;as&quot;, cc.xy(1, 1)); // Mnemonic is the second 'a'
	 * addTitle(&quot;Look&amp;&amp;Feel&quot;, cc.xy(1, 1)); // No mnemonic, text is Look&amp;Feel
	 * </pre>
	 * 
	 * @param textWithMnemonic
	 *            the title label's text - may contain an ampersand (<tt>&amp;</tt>)
	 *            to mark a mnemonic
	 * @param constraints
	 *            the separator's cell constraints
	 * @return the added title label
	 * 
	 * @see ComponentFactory
	 */
	public final Label addTitle(String textWithMnemonic,
			CellConstraints constraints) {
		Label titleLabel = getComponentFactory().createTitle(getComposite(),
				textWithMnemonic);
		add(titleLabel, constraints);
		return titleLabel;
	}

	/**
	 * Adds a title label to the form using the specified constraints.
	 * <p>
	 * 
	 * <pre>
	 * addTitle(&quot;Name&quot;, &quot;1, 1&quot;); // No mnemonic
	 * addTitle(&quot;N&amp;ame&quot;, &quot;1, 1&quot;); // Mnemonic is 'a'
	 * addTitle(&quot;Save &amp;as&quot;, &quot;1, 1&quot;); // Mnemonic is the second 'a'
	 * addTitle(&quot;Look&amp;&amp;Feel&quot;, &quot;1, 1&quot;); // No mnemonic, text is Look&amp;Feel
	 * </pre>
	 * 
	 * @param textWithMnemonic
	 *            the title label's text - may contain an ampersand (<tt>&amp;</tt>)
	 *            to mark a mnemonic
	 * @param encodedConstraints
	 *            a string representation for the constraints
	 * @return the added title label
	 * 
	 * @see ComponentFactory
	 */
	public final Label addTitle(String textWithMnemonic,
			String encodedConstraints) {
		return addTitle(textWithMnemonic, new CellConstraints(
				encodedConstraints));
	}

	// Adding Separators ------------------------------------------------------

	/**
	 * Adds a titled separator to the form that spans all columns.
	 * <p>
	 * 
	 * <pre>
	 * addSeparator(&quot;Name&quot;); // No Mnemonic
	 * addSeparator(&quot;N&amp;ame&quot;); // Mnemonic is 'a'
	 * addSeparator(&quot;Save &amp;as&quot;); // Mnemonic is the second 'a'
	 * addSeparator(&quot;Look&amp;&amp;Feel&quot;); // No mnemonic, text is &quot;look&amp;feel&quot;
	 * </pre>
	 * 
	 * @param textWithMnemonic
	 *            the separator label's text - may contain an ampersand (<tt>&amp;</tt>)
	 *            to mark a mnemonic
	 * @return the added separator
	 */
	public final Control addSeparator(String textWithMnemonic) {
		return addSeparator(textWithMnemonic, getLayout().getColumnCount());
	}

	/**
	 * Adds a titled separator to the form using the specified constraints.
	 * <p>
	 * 
	 * <pre>
	 * addSeparator(&quot;Name&quot;, cc.xy(1, 1)); // No Mnemonic
	 * addSeparator(&quot;N&amp;ame&quot;, cc.xy(1, 1)); // Mnemonic is 'a'
	 * addSeparator(&quot;Save &amp;as&quot;, cc.xy(1, 1)); // Mnemonic is the second 'a'
	 * addSeparator(&quot;Look&amp;&amp;Feel&quot;, cc.xy(1, 1)); // No mnemonic, text is &quot;look&amp;feel&quot;
	 * </pre>
	 * 
	 * @param textWithMnemonic
	 *            the separator label's text - may contain an ampersand (<tt>&amp;</tt>)
	 *            to mark a mnemonic
	 * @param constraints
	 *            the separator's cell constraints
	 * @return the added separator
	 */
	public final Control addSeparator(String textWithMnemonic,
			CellConstraints constraints) {
		int titleAlignment = isLeftToRight() ? SWT.LEFT : SWT.RIGHT;
		Control titledSeparator = getComponentFactory().createSeparator(
				getComposite(), textWithMnemonic, titleAlignment);
		add(titledSeparator, constraints);
		return titledSeparator;
	}

	/**
	 * Adds a titled separator to the form using the specified constraints.
	 * <p>
	 * 
	 * <pre>
	 * addSeparator(&quot;Name&quot;, &quot;1, 1&quot;); // No Mnemonic
	 * addSeparator(&quot;N&amp;ame&quot;, &quot;1, 1&quot;); // Mnemonic is 'a'
	 * addSeparator(&quot;Save &amp;as&quot;, &quot;1, 1&quot;); // Mnemonic is the second 'a'
	 * addSeparator(&quot;Look&amp;&amp;Feel&quot;, &quot;1, 1&quot;); // No mnemonic, text is &quot;look&amp;feel&quot;
	 * </pre>
	 * 
	 * @param textWithMnemonic
	 *            the separator label's text - may contain an ampersand (<tt>&amp;</tt>)
	 *            to mark a mnemonic
	 * @param encodedConstraints
	 *            a string representation for the constraints
	 * @return the added separator
	 */
	public final Control addSeparator(String textWithMnemonic,
			String encodedConstraints) {
		return addSeparator(textWithMnemonic, new CellConstraints(
				encodedConstraints));
	}

	/**
	 * Adds a titled separator to the form that spans the specified columns.
	 * <p>
	 * 
	 * <pre>
	 * addSeparator(&quot;Name&quot;, 3); // No Mnemonic
	 * addSeparator(&quot;N&amp;ame&quot;, 3); // Mnemonic is 'a'
	 * addSeparator(&quot;Save &amp;as&quot;, 3); // Mnemonic is the second 'a'
	 * addSeparator(&quot;Look&amp;&amp;Feel&quot;, 3); // No mnemonic, text is &quot;look&amp;feel&quot;
	 * </pre>
	 * 
	 * @param textWithMnemonic
	 *            the separator label's text - may contain an ampersand (<tt>&amp;</tt>)
	 *            to mark a mnemonic
	 * @param columnSpan
	 *            the number of columns the separator spans
	 * @return the added separator
	 */
	public final Control addSeparator(String textWithMnemonic, int columnSpan) {
		return addSeparator(textWithMnemonic,
				createLeftAdjustedConstraints(columnSpan));
	}

	// Accessing the ComponentFactory *****************************************

	/**
	 * Returns the builder's component factory. If no factory has been set
	 * before, it is lazily initialized using with an instance of
	 * {@link com.jgoodies.forms.factories.DefaultComponentFactory}.
	 * 
	 * @return the component factory
	 * 
	 * @see #setComponentFactory(ComponentFactory)
	 */
	public final ComponentFactory getComponentFactory() {
		if (this.componentFactory == null) {
			this.componentFactory = DefaultComponentFactory.getInstance();
		}
		return this.componentFactory;
	}

	/**
	 * Sets a new component factory.
	 * 
	 * @param newFactory
	 *            the component factory to be set
	 * 
	 * @see #getComponentFactory()
	 */
	public final void setComponentFactory(ComponentFactory newFactory) {
		this.componentFactory = newFactory;
	}

}
