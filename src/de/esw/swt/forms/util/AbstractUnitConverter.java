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

import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

/**
 * An abstract implementation of the {@link UnitConverter} interface that
 * minimizes the effort required to convert font-dependent sizes to pixels.
 * 
 * @author Karsten Lentzsch
 * @version $Revision: 1.2 $
 * 
 * @see DefaultUnitConverter
 * @see de.esw.swt.forms.layout.Size
 * @see de.esw.swt.forms.layout.Sizes
 */
public abstract class AbstractUnitConverter implements UnitConverter {

	private static final int DTP_RESOLUTION = 72;

	// Unit Converter Implementation
	// *********************************************

	/**
	 * Converts Inches and returns pixels using the specified resolution.
	 * 
	 * @param in
	 *            the Inches
	 * @param control
	 *            the component that provides the graphics object
	 * @return the given Inches as pixels
	 */
	public int inchAsPixel(final double in, final Control control) {
		return inchAsPixel(in, getScreenResolution(control));
	}

	/**
	 * Converts Millimeters and returns pixels using the resolution of the given
	 * component's graphics object.
	 * 
	 * @param mm
	 *            Millimeters
	 * @param control
	 *            the component that provides the graphics object
	 * @return the given Millimeters as pixels
	 */
	public int millimeterAsPixel(final double mm, final Control control) {
		return millimeterAsPixel(mm, getScreenResolution(control));
	}

	/**
	 * Converts Centimeters and returns pixels using the resolution of the given
	 * component's graphics object.
	 * 
	 * @param cm
	 *            Centimeters
	 * @param control
	 *            the component that provides the graphics object
	 * @return the given Centimeters as pixels
	 */
	public int centimeterAsPixel(final double cm, final Control control) {
		return centimeterAsPixel(cm, getScreenResolution(control));
	}

	/**
	 * Converts DTP Points and returns pixels using the resolution of the given
	 * component's graphics object.
	 * 
	 * @param pt
	 *            DTP Points
	 * @param control
	 *            the component that provides the graphics object
	 * @return the given Points as pixels
	 */
	public int pointAsPixel(final int pt, final Control control) {
		return pointAsPixel(pt, getScreenResolution(control));
	}

	/**
	 * Converts horizontal dialog units and returns pixels. Honors the
	 * resolution, dialog font size, platform, and l&amp;f.
	 * 
	 * @param dluX
	 *            the horizontal dialog units
	 * @param control
	 *            a Component that provides the font and graphics
	 * @return the given horizontal dialog units as pixels
	 */
	public int dialogUnitXAsPixel(final int dluX, final Control control) {
		return dialogUnitXAsPixel(dluX, getDialogBaseUnitsX(control));
	}

	/**
	 * Converts vertical dialog units and returns pixels. Honors the resolution,
	 * dialog font size, platform, and l&amp;f.
	 * 
	 * @param dluY
	 *            the vertical dialog units
	 * @param control
	 *            a Component that provides the font and graphics
	 * @return the given vertical dialog units as pixels
	 */
	public int dialogUnitYAsPixel(final int dluY, final Control control) {
		return dialogUnitYAsPixel(dluY, getDialogBaseUnitsY(control));
	}

	// Abstract Behavior *****************************************************

	/**
	 * Gets and returns the horizontal dialog base units. Implementations are
	 * encouraged to cache previously computed dialog base units.
	 * 
	 * @param component
	 *            a Component that provides the font and graphics
	 * @return the horizontal dialog base units
	 */
	protected abstract double getDialogBaseUnitsX(Control control);

	/**
	 * Gets and returns the vertical dialog base units. Implementations are
	 * encouraged to cache previously computed dialog base units.
	 * 
	 * @param component
	 *            a Component that provides the font and graphics
	 * @return the vertical dialog base units
	 */
	protected abstract double getDialogBaseUnitsY(Control control);

	// Convenience Methods ***************************************************

	/**
	 * Converts Inches and returns pixels using the specified resolution.
	 * 
	 * @param in
	 *            the Inches
	 * @param dpi
	 *            the resolution
	 * @return the given Inches as pixels
	 */
	protected final int inchAsPixel(final double in, final int dpi) {
		return (int) Math.round(dpi * in);
	}

	/**
	 * Converts Millimeters and returns pixels using the specified resolution.
	 * 
	 * @param mm
	 *            Millimeters
	 * @param dpi
	 *            the resolution
	 * @return the given Millimeters as pixels
	 */
	protected final int millimeterAsPixel(final double mm, final int dpi) {
		return (int) Math.round(dpi * mm * 10 / 254);
	}

	/**
	 * Converts Centimeters and returns pixels using the specified resolution.
	 * 
	 * @param cm
	 *            Centimeters
	 * @param dpi
	 *            the resolution
	 * @return the given Centimeters as pixels
	 */
	protected final int centimeterAsPixel(final double cm, final int dpi) {
		return (int) Math.round(dpi * cm * 100 / 254);
	}

	/**
	 * Converts DTP Points and returns pixels using the specified resolution.
	 * 
	 * @param pt
	 *            DTP Points
	 * @param dpi
	 *            the resolution in dpi
	 * @return the given Points as pixels
	 */
	protected final int pointAsPixel(final int pt, final int dpi) {
		return Math.round(dpi * pt / DTP_RESOLUTION);
	}

	/**
	 * Converts horizontal dialog units and returns pixels.
	 * 
	 * @param dluX
	 *            the horizontal dialog units
	 * @param dialogBaseUnitsX
	 *            the horizontal dialog base units
	 * @return the given dialog base units as pixels
	 */
	protected int dialogUnitXAsPixel(final int dluX,
			final double dialogBaseUnitsX) {
		return (int) Math.round(dluX * dialogBaseUnitsX / 4);
	}

	/**
	 * Converts vertical dialog units and returns pixels.
	 * 
	 * @param dluY
	 *            the vertical dialog units
	 * @param dialogBaseUnitsY
	 *            the vertical dialog base units
	 * @return the given dialog base units as pixels
	 */
	protected int dialogUnitYAsPixel(final int dluY,
			final double dialogBaseUnitsY) {
		return (int) Math.round(dluY * dialogBaseUnitsY / 8);
	}

	// Helper Code ************************************************************

	/**
	 * Computes and returns the average character width of the specified test
	 * string using the given FontMetrics. The test string shall represent an
	 * "average" text.
	 * 
	 * @param metrics
	 *            used to compute the test string's width
	 * @param testString
	 *            the string that shall represent an "average" text
	 * @return the test string's average character width.
	 */
	protected double computeAverageCharWidth(final FontMetrics metrics,
			final String testString) {
		return metrics.getAverageCharWidth();
	}

	/**
	 * Returns the components screen resolution or the default screen resolution
	 * if the component is null or has no toolkit assigned yet.
	 * 
	 * @param control
	 *            the component to ask for a toolkit
	 * @return the component's screen resolution
	 */
	protected int getScreenResolution(final Control control) {
		if (control == null) {
			return getDefaultScreenResolution();
		}

		final Display display = Display.getCurrent();
		return display != null ? getDPI(display).x
				: getDefaultScreenResolution();
	}

	private static int defaultScreenResolution = -1;

	/**
	 * Computes and returns the default resolution.
	 * 
	 * @return the default screen resolution
	 */
	protected int getDefaultScreenResolution() {
		if (defaultScreenResolution == -1) {
			defaultScreenResolution = getDPI(Display.getDefault()).x;
		}
		return defaultScreenResolution;
	}

	/**
	 * Returns the DPI for the given display if the API has this method. Returns
	 * -1, -1 otherwise.
	 * 
	 * TODO: Remove this method if RWT API supports this method.
	 * 
	 * @param display
	 * @return
	 */
	private Point getDPI(Display display) {
		try {
			return (Point) Display.class.getMethod("getDPI", new Class[0])
					.invoke(display, new Object[0]);
		} catch (Exception e) {
			return new Point(-1, -1);
		}
	}

}
