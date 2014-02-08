package com.ju.it.pratik.img;

public interface WMConsts {

	public static final String RESOURCE_IMAGES = "src/main/resources/images/";
	public static final String WATERMARKED_IMAGES = RESOURCE_IMAGES + "watermarked/";
	public static final String OUTPUT_FOLDER = RESOURCE_IMAGES + "output/";
	public static final String DEFAULT_WATERMARK_INPUT_FILE_NAME = "lena_512.bmp";//"Hydrangeas_512.jpg";
	public static final String DEFAULT_WATERMARKED_IMAGE = "lena_512_rgb_wm.bmp";
	public static final String WATERMARK_LOGO = RESOURCE_IMAGES + "watermark_logo.bmp";
	
	public static final String LENA = "lena_512.bmp";
	public static final String LENA_WM = "lena_512_rgb_wm.bmp";
	public static final String LENA_CROPPED_WM = "lena_256_cropped_rgb_wm2.bmp";	
	public static final String LENA_ROTATED_WM = "lena_512_rotated_rgb_wm.bmp";
	public static final String LENA_ROTATED_90_WM = "lena_512_rotated_90_rgb_wm.bmp";
	public static final String LENA_ROTATED_45_WM = "lena_512_rotated_45_rgb_wm.bmp";
	
	public static final String SHIP = "ship_512.bmp";
	
	public static final int BLOCK_SIZE = 8;
	
	public static final String[] SAMPLE_IMAGES = new String[] {
		"lena_512.bmp"
	};
	public static final int LENA_INDEX = 0;
	
	public static final String WATER_MARK = "This is abc test";
	
	public static final int WM_STRENGTH = 25;
	public static final int WM_THRESHOLD = 2;
}
