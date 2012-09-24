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

import org.eclipse.swt.widgets.Control;

/**
 * An interface that describes how to convert general sizes to pixel sizes. For
 * example, <i>dialog units</i> require a conversion that honors the font and
 * resolution. The {@link de.esw.swt.forms.layout.Sizes} class delegates all
 * size conversions to an implementation of this interface.
 * 
 * @author Karsten Lentzsch
 * @version $Revision: 1.2 $
 * @see de.esw.swt.forms.layout.Sizes
 * @see de.esw.swt.forms.layout.ConstantSize
 * @see AbstractUnitConverter
 * @see DefaultUnitConverter
 */
public interface UnitConverter {

	/**
	 * Converts Inches and returns pixels using the specified resolution.
	 * 
	 * @param in
	 *            the Inches
	 * @param control
	 *            the component that provides the graphics object
	 * @return the given Inches as pixels
	 */
	int inchAsPixel(double in, Control control);

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
	int millimeterAsPixel(double mm, Control control);

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
	int centimeterAsPixel(double cm, Control control);

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
	int pointAsPixel(int pt, Control control);

	/**
	 * Converts horizontal dialog units and returns pixels. Honors the
	 * resolution, dialog font size, platform and look&amp;feel.
	 * 
	 * @param dluX
	 *            the horizontal dialog units
	 * @param control
	 *            a component that provides the font and graphics
	 * @return the given horizontal dialog units as pixels
	 */
	int dialogUnitXAsPixel(int dluX, Control control);

	/**
	 * Converts vertical dialog units and returns pixels. Honors the resolution,
	 * dialog font size, platform and look&amp;feel.
	 * 
	 * @param dluY
	 *            the vertical dialog units
	 * @param control
	 *            a component that provides the font and graphics
	 * @return the given vertical dialog units as pixels
	 */
	int dialogUnitYAsPixel(int dluY, Control control);

}
