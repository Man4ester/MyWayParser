package main.java.central.blogic.interfaces;

import java.awt.image.BufferedImage;

/**
 * @author bismark
 *
 */
public interface IImageResizing {

	/*
	 * width and height parameters what must be on final result
	 */
	public BufferedImage resize(BufferedImage image, int width, int height);

}
