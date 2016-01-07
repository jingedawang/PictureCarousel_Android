package com.ucas.developer.picturecarousel_android;

/**
 * 本控件运行时异常的父类
 * @author 金戈大王
 * @version 0.1
 */
public class CarouselException extends RuntimeException {
	public CarouselException(String msg) {
		super(msg);
	}
}

/**
 * 图片资源为空异常，当传入空的图片资源时抛出
 * @author 金戈大王
 * @version 0.1
 */
class BitmapsNullException extends CarouselException {
	public BitmapsNullException(String msg) {
		super(msg);
	}
}