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

package de.esw.swt.forms.debug;

import java.lang.reflect.Method;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

import de.esw.swt.forms.layout.FormLayout;

/**
 * A panel that paints grid bounds if and only if the panel's layout manager is
 * a {@link FormLayout}. You can tweak the debug paint process by setting a
 * custom grid color, painting optional diagonals and painting the grid in the
 * background or foreground.
 * <p>
 * 
 * This class is not intended to be extended. However, it is not marked as
 * <code>final</code> to allow users to subclass it for debugging purposes. In
 * general it is recommended to <em>use</em> JPanel instances, not
 * <em>extend</em> them. You can see this implementation style in the Forms
 * tutorial classes. Rarely there's a need to extend JPanel; for example if you
 * provide a custom behavior for <code>#paintComponent</code> or
 * <code>#updateUI</code>.
 * 
 * @author Karsten Lentzsch
 * @version $Revision: 1.2 $
 * 
 * @see FormDebugUtils
 */
public class FormDebugComposite extends Canvas {

	/**
	 * The default color used to paint the form's debug grid.
	 */
	private static final int DEFAULT_GRID_COLOR = SWT.COLOR_RED;

	/**
	 * Specifies whether the container's diagonals should be painted.
	 */
	private boolean paintDiagonals;

	/**
	 * Holds the color used to paint the debug grid.
	 */
	private Color gridColor;

	// Instance Creation ****************************************************

	/**
	 * Constructs a FormDebugComposite with all options turned off.
	 * 
	 * @param parent
	 */
	public FormDebugComposite(Composite parent) {
		this(parent, null);
	}

	/**
	 * Constructs a FormDebugComposite on the given FormLayout instance that
	 * paints the grid in the foreground and paints no diagonals.
	 * 
	 * @param parent
	 * @param layout
	 *            the panel's FormLayout instance
	 */
	public FormDebugComposite(Composite parent, FormLayout layout) {
		this(parent, layout, false);
	}

	/**
	 * Constructs a FormDebugComposite on the given FormLayout using the
	 * specified settings that are otherwise turned off.
	 * 
	 * @param parent
	 * @param paintDiagonals
	 *            true to paint diagonals, false to not paint them
	 */
	public FormDebugComposite(Composite parent, boolean paintDiagonals) {
		this(parent, null, paintDiagonals);
	}

	/**
	 * Constructs a FormDebugComposite on the given FormLayout using the
	 * specified settings that are otherwise turned off.
	 * 
	 * @param parent
	 * @param layout
	 *            the panel's FormLayout instance
	 * @param paintDiagonals
	 *            true to paint diagonals, false to not paint them
	 */
	public FormDebugComposite(Composite parent, FormLayout layout,
			boolean paintDiagonals) {
		super(parent, SWT.NONE);
		setLayout(layout);
		setPaintDiagonals(paintDiagonals);
		setGridColor(DEFAULT_GRID_COLOR);
		addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				paint(e.gc);
			}
		});
	}

	/**
	 * @see org.eclipse.swt.widgets.Widget#dispose()
	 */
	public void dispose() {
		super.dispose();
		disposeColor(this.gridColor);
	}

	// Accessors ************************************************************

	/**
	 * Enables or disables to paint the panel's diagonals.
	 * 
	 * @param b
	 *            true to paint diagonals, false to not paint them
	 */
	public void setPaintDiagonals(boolean b) {
		this.paintDiagonals = b;
	}

	/**
	 * Sets the debug grid's color.
	 * 
	 * @param color
	 *            the color used to paint the debug grid
	 */
	public void setGridColor(int color) {
		if (this.gridColor != null) {
			disposeColor(this.gridColor);
		}
		this.gridColor = getDisplay().getSystemColor(DEFAULT_GRID_COLOR);
	}

	// Painting *************************************************************

	/**
	 * Paints the panel. If the panel's layout manager is a FormLayout and
	 * foreground painting is enabled, it paints the form's grid lines.
	 * 
	 * @param gc
	 *            the Graphics object to paint on
	 */
	public void paint(GC gc) {
		if (!(getLayout() instanceof FormLayout)) {
			return;
		}
		FormLayout.LayoutInfo layoutInfo = FormDebugUtils.getLayoutInfo(this);
		int left = layoutInfo.getX();
		int top = layoutInfo.getY();
		int width = layoutInfo.getWidth();
		int height = layoutInfo.getHeight();

		gc.setBackground(this.gridColor);
		// Paint the column bounds.
		for (int col = 0; col < layoutInfo.columnOrigins.length; col++) {
			gc.fillRectangle(layoutInfo.columnOrigins[col], top, 1, height);
		}

		// Paint the row bounds.
		for (int row = 0; row < layoutInfo.rowOrigins.length; row++) {
			gc.fillRectangle(left, layoutInfo.rowOrigins[row], width, 1);
		}

		if (this.paintDiagonals) {
			gc.drawLine(left, top, left + width, top + height);
			gc.drawLine(left, top + height, left + width, top);
		}
	}

	// RWT Compatibility ****************************************************

	public void addPaintListener(PaintListener paintListener) {
		try {
			Method method = getClass().getSuperclass().getMethod(
					"addPaintListener", new Class[] { PaintListener.class });
			method.invoke(this, new Object[] { paintListener });
		} catch (Exception e) {
		}
	}

	private void disposeColor(Color color) {
		try {
			Color.class.getMethod("dispose", new Class[0]).invoke(color,
					new Object[0]);
		} catch (Exception e) {
		}
	}

}
