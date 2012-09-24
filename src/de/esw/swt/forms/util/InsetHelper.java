/**
 * $Id$
 */
package de.esw.swt.forms.util;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;

/**
 * @author markusw
 * @version $Revision$
 */
public class InsetHelper {

	/**
	 * This method mimics the Insets of AWT for layout caluclation
	 * 
	 * @param composite
	 * @return Insets of the given composite
	 */
	public static Rectangle caluclateInsets(final Composite composite) {
		final Rectangle bounds = composite.getBounds();
		final Rectangle clientArea = composite.getClientArea();

		final int w = (bounds.width - clientArea.width) / 2;
		final int h = (bounds.height - clientArea.height) / 2;

		return new Rectangle(w, h, w, h);
	}

}
