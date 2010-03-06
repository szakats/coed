package coed.plugin.base;

import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IPageListener;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;


/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin{
	//image descriptors
	public static final String IMAGE_USER = "coed.image.user";
	
	// The plug-in ID
	public static final String PLUGIN_ID = "CoedEditor";

	// The shared instance
	private static Activator plugin;
	private static IController controller;
	private static ImageRegistry imageRegistry;
	
	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		controller = new Controller();
		imageRegistry = new ImageRegistry();
		//TODO: add icons
		/* smthg like:
		 URL myUrl=	new URL(MyPlugin.getInstance().getDescriptor().getInstallURL(),"images/my_action.gif");
		 ImageDescriptor id = ImageDescriptor.createFromURL(myUrl);
		 imageRegisrty.put(name, image);....
		 */
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}
	
	public static IController getController() {
		return controller;
	}
	
	public static ImageRegistry getMyImageRegistry(){
		return imageRegistry;
	}
}
