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

package de.esw.swt.forms.factories;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

/**
 * An interface that defines the factory methods as used by the
 * {@link de.esw.swt.forms.builder.CompositeBuilder} and its subclasses.
 * <p>
 * 
 * The String arguments passed to the methods <code>#createLabel(String)</code>,
 * <code>#createTitle(String)</code>, and
 * <code>#createSeparator(String, int)</code> can contain an optional mnemonic
 * marker. The mnemonic and mnemonic index are indicated by a single ampersand (<tt>&amp;</tt>).
 * For example <tt>&quot;&amp;Save&quot</tt>, or
 * <tt>&quot;Save&nbsp;&amp;as&quot</tt>. To use the ampersand itself
 * duplicate it, for example <tt>&quot;Look&amp;&amp;Feel&quot</tt>.
 * 
 * @author Karsten Lentzsch
 * @version $Revision: 1.2 $
 * 
 * @see DefaultComponentFactory
 * @see de.esw.swt.forms.builder.CompositeBuilder
 */

public interface ComponentFactory {

	/**
	 * Creates and returns a label with an optional mnemonic.
	 * <p>
	 * 
	 * <pre>
	 * createLabel(&quot;Name&quot;); // No mnemonic
	 * createLabel(&quot;N&amp;ame&quot;); // Mnemonic is 'a'
	 * createLabel(&quot;Save &amp;as&quot;); // Mnemonic is the second 'a'
	 * createLabel(&quot;Look&amp;&amp;Feel&quot;); // No mnemonic, text is Look&amp;Feel
	 * </pre>
	 * 
	 * @param parent
	 * @param textWithMnemonic
	 *            the label's text - may contain an ampersand (<tt>&amp;</tt>)
	 *            to mark a mnemonic
	 * @return an label with optional mnemonic
	 */
	public Label createLabel(Composite parent, String textWithMnemonic);

	/**
	 * Creates and returns a title label that uses the foreground color and font
	 * of a <code>TitledBorder</code>.
	 * <p>
	 * 
	 * <pre>
	 * createTitle(&quot;Name&quot;); // No mnemonic
	 * createTitle(&quot;N&amp;ame&quot;); // Mnemonic is 'a'
	 * createTitle(&quot;Save &amp;as&quot;); // Mnemonic is the second 'a'
	 * createTitle(&quot;Look&amp;&amp;Feel&quot;); // No mnemonic, text is Look&amp;Feel
	 * </pre>
	 * 
	 * @param parent
	 * @param textWithMnemonic
	 *            the label's text - may contain an ampersand (<tt>&amp;</tt>)
	 *            to mark a mnemonic
	 * @return an emphasized title label
	 */
	public Label createTitle(Composite parent, String textWithMnemonic);

	/**
	 * Creates and returns a labeled separator. Useful to separate paragraphs in
	 * a panel, which is often a better choice than a <code>TitledBorder</code>.
	 * <p>
	 * 
	 * <pre>
	 * final int LEFT = SwingConstants.LEFT;
	 * createSeparator(&quot;Name&quot;, LEFT); // No mnemonic
	 * createSeparator(&quot;N&amp;ame&quot;, LEFT); // Mnemonic is 'a'
	 * createSeparator(&quot;Save &amp;as&quot;, LEFT); // Mnemonic is the second 'a'
	 * createSeparator(&quot;Look&amp;&amp;Feel&quot;, LEFT); // No mnemonic, text is Look&amp;Feel
	 * </pre>
	 * 
	 * @param parent
	 * @param textWithMnemonic
	 *            the label's text - may contain an ampersand (<tt>&amp;</tt>)
	 *            to mark a mnemonic
	 * @param alignment
	 *            text alignment, one of <code>SwingConstants.LEFT</code>,
	 *            <code>SwingConstants.CENTER</code>,
	 *            <code>SwingConstants.RIGHT</code>
	 * @return a title label with separator on the side
	 */
	public Control createSeparator(Composite parent, String textWithMnemonic,
			int alignment);

}
