package com.example.zeng.choujiang;

import java.util.Vector;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

public class SnowView extends View {
	float x;
	float y;

    boolean isStart;
	private int[] imageIds = { R.drawable.color_debris_a,
			R.drawable.color_debris_b, R.drawable.color_debris_c,
			R.drawable.color_debris_d, R.drawable.color_debris_e,
			R.drawable.color_debris_f, R.drawable.color_debris_g };

	public SnowView(Context context) {
		super(context);
		x = 100;
		y = 100;
	}

	public SnowView(Context context, AttributeSet arg1) {
		super(context, arg1);

		x = 100;
		y = 100;
	}

	private Handler h = new Handler(new Callback() {

		@Override
		public boolean handleMessage(Message arg0) {
			SnowView.this.invalidate();
			return false;
		}

	});
	Vector<Snow> list = new Vector<Snow>();
	public static int G = 10;
	private float random = 75f;

	@Override
	public void draw(Canvas canvas) {

        if(isStart) {
            if (Math.random() * 100 > random   ) {
                int imageId = (int)(Math.random() * 6);


                Snow s = new Snow(Math.random() * canvas.getWidth(), Math.random(),
                        BitmapFactory.decodeResource(getResources(),
                                imageIds[imageId]));

                list.add(s);
            }
            for (int i = 0; i < list.size(); i++) {
                Snow snow = list.get(i);
                snow.draw(canvas);
                if(snow.y > canvas.getHeight() - snow.bitmap.getHeight() - 10) {
                    list.remove(i);
                }
            }
//		 h.sendEmptyMessageDelayed(0, 15);
            this.invalidate();
        }


	}

	public void big() {
		// TODO Auto-generated method stub
		if (random < 0.1)
			return;
		random -= 0.1f;
		G += 1;
	}

	public void small() {
		if (random >= 1)
			return;
		// TODO Auto-generated method stub
		random += 0.1f;
		if (G > 1)
			G -= 1;
	}


    public void viewStart(){
        isStart = true;
        invalidate();
    }

    public void  viewStop() {
        isStart = false;
    }
}
