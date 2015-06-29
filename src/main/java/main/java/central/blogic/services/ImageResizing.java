package main.java.central.blogic.services;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import main.java.central.blogic.interfaces.IImageResizing;

/**
 * @author bismark
 *
 */
public class ImageResizing implements IImageResizing {

	@Override
	public BufferedImage resize(BufferedImage image, int width, int height) {
		int w, h;
		if (image.getWidth() == width && image.getHeight() == height) {
			w = image.getWidth();
			h = image.getHeight();
		} else {
			float dx = ((float) width) / image.getWidth();
			float dy = ((float) height) / image.getHeight();
			if (dx == dy) {
				w = width;
				h = (int) (dx * image.getHeight());
			} else {
				w = (int) (dy * image.getWidth());
				h = height;
			}
		}
		BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics2D = null;
		try {
			graphics2D = bufferedImage.createGraphics();
			graphics2D.fillRect(0, 0, width, height);
			graphics2D.drawImage(image.getScaledInstance(w, h, BufferedImage.SCALE_SMOOTH), 0, 0, null);
		} finally {
			if (graphics2D != null) {
				graphics2D.dispose();
			}
		}
		return bufferedImage;
	}

}
