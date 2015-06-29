package main.java.fullparser.controllers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.java.central.blogic.interfaces.IImageResizing;
import main.java.central.blogic.services.ImageResizing;

public class ImageResize {

	private static final String FOLDER = "/home/bismark/tmp/image_source/";

	public static void main(String[] args) {
		System.out.println("Start");
		IImageResizing service = new ImageResizing();
		try {
			BufferedImage img = ImageIO.read(new File(FOLDER + "img_1.jpg"));
			BufferedImage img2 = service.resize(img, 240, 480);
			ImageIO.write(img2, "jpg", new File(FOLDER + "img_1_small"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println();
	}

}
