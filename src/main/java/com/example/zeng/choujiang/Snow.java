package com.example.zeng.choujiang;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

public class Snow {
	private final int windX = 2;
	private final int windY = 2;
	
	public Snow(double d,double e,Bitmap bitmap){
		x = (float)d;
		this.y = (float) 0;
		this.size = (float) e;
		if(size <1.5)
			size = 1.5f;
//		this.bitmap = util.zoomBitmap(bitmap, bitmap.getWidth()*size,bitmap.getHeight()*size);
		this.bitmap = bitmap;
		
		r = (float) (Math.random() + 0.5);
		flag = Math.random() > 0.5?true:false;
	}
	public float x,y,size,dy,r;
	public Bitmap bitmap = null;
	public boolean flag;
	
	public void draw(Canvas canvas){
		Paint p = new Paint();
		Matrix m =  new  Matrix();
		m.postTranslate(x, y);
		
		if(flag) {
			
			m.postRotate(y / canvas.getHeight() * 180 * r ,x,y);
		} else {
			m.postRotate(-y / canvas.getHeight() * 180 * r ,x,y);

		}
		canvas.drawBitmap(bitmap, m, p);
 //		canvas.drawBitmap(bitmap,x,y,p);
   		if(canvas.getHeight()<= y+bitmap.getHeight())
			return;
   		if (Math.random() * 100 > 90   ) {
   			
   			x += 1.2*Math.random()*windX - Math.random()*windX;
   		}
//		y += Math.random()*windY +SnowView.G;
		
		dy = (float) (Math.random()*windY +SnowView.G);
		y += dy;
		
	}
}
