/*
 * $Id: NativeSupport.java 38 2012-01-04 22:44:15Z andre@naef.com $
 * See LICENSE.txt for license terms.
 */

package com.naef.jnlua;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Loads the JNLua native library.
 * 
 * The class provides and configures a default loader implementation that loads
 * the JNLua native library by means of the <code>System.loadLibrary</code>
 * method. In some situations, you may want to override this behavior. For
 * example, when using JNLua as an OSGi bundle, the native library is loaded by
 * the OSGi runtime. Therefore, the OSGi bundle activator replaces the loader by
 * a no-op implementaion. Note that the loader must be configured before
 * LuaState is accessed.
 */
public final class NativeSupport {
	// -- Static
	private static final NativeSupport INSTANCE = new NativeSupport();

	// -- State
	private Loader loader = new DefaultLoader();

	/**
	 * Returns the instance.
	 * 
	 * @return the instance
	 */
	public static NativeSupport getInstance() {
		return INSTANCE;
	}

	// -- Construction
	/**
	 * Private constructor to prevent external instantiation.
	 */
	private NativeSupport() {
	}

	// -- Properties
	/**
	 * Return the native library loader.
	 * 
	 * @return the loader
	 */
	public Loader getLoader() {
		return loader;
	}

	/**
	 * Sets the native library loader.
	 * 
	 * @param loader
	 *            the loader
	 */
	public void setLoader(Loader loader) {
		if (loader == null) {
			throw new NullPointerException("loader must not be null");
		}
		this.loader = loader;
	}

	// -- Member types
	/**
	 * Loads the library.
	 */
	public interface Loader {
		public void load();
	}

	private class DefaultLoader implements Loader {
		@Override
		public void load() {
			
			String path = "native/"+(System.getProperty("os.name")).replaceAll(" ", "")+"/libjnlua52";
			URL url = ClassLoader.getSystemClassLoader().getResource(path);
			    InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream(path);
			    byte[] buffer = new byte[1024];
			    int read = -1;
			    Object name;
				File temp = null;
				try {
					temp = File.createTempFile(System.currentTimeMillis()+"", "");
					FileOutputStream fos = new FileOutputStream(temp);

					while((read = in.read(buffer)) != -1) {
					       fos.write(buffer, 0, read);
					}
					fos.close();
					in.close();
				} catch (IOException e) {
					
					e.printStackTrace();
				}
			   
			    System.load(temp.getAbsolutePath());
		}
	}
}
