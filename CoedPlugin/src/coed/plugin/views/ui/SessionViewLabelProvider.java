/**
 * 
 */
package coed.plugin.views.ui;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import coed.plugin.views.model.ModelDirectory;

/**
 * @author neobi008
 *
 */
public class SessionViewLabelProvider extends LabelProvider  {

	public String getText(Object obj) {
		return obj.toString();
	}
	
	public Image getImage(Object obj) {
		String imageKey = ISharedImages.IMG_OBJ_ELEMENT;
		if (obj instanceof ModelDirectory)
		   imageKey = ISharedImages.IMG_OBJ_FOLDER;
		return PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);
	}
}
