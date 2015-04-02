package com.deep.android_customview_gallery3d;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;

/**
 * Created by mac on 15/4/2.
 */
public class GetInvertedImage {

    //倒影所占原图的比例0~1，默认0.4
    private float proportion;
    //倒影与原图的间隔，默认5个像素
    private int interval;
    //倒影开始的透明度
    private int startAlpha;
    //倒影结束时的透明度
    private int endAlpha;

    public GetInvertedImage(){
        proportion = 0.4f;
        interval = 5;
        this.startAlpha = 0x70ffffff;
        this.endAlpha = 0x00ffffff;
    }



    //通过设置改变倒影部分占原图的比例
    public void setProportion(float proportion) {
        if (proportion < 0){
            this.proportion = 0;
        }else if (proportion > 1){
            this.proportion = 1;
        }else {
            this.proportion = proportion;
        }
    }

    //通过设置修改原图与倒影之间的间隔
    public void setInterval(int interval) {
        this.interval = interval;
    }

    //设置开始透明度
    public void setStartAlpha(int startAlpha) {
        this.startAlpha = startAlpha;
    }

    //设置结束透明度
    public void setEndAlpha(int endAlpha) {
        this.endAlpha = endAlpha;
    }

    public Bitmap getImageByBitmap(Bitmap source){

        //原图此存
        int width = source.getWidth();
        int height = source.getHeight();
        //裁剪图片的位置
        int x = 0;
        int y = (int) (height*(1-proportion));
        //裁剪图片的高
        int cutHeight = (int) (height*proportion);
        //最终倒影图片的高度
        int resHeight = (int) (height*(1+proportion) + interval);

        //剪切图片
        Matrix matrix = new Matrix();
        //图片垂直翻转
        matrix.setScale(1f, -1f);
        //获得裁剪倒影图片原图
        Bitmap cutBitmap = Bitmap.createBitmap(source, x, y, width, cutHeight, matrix, false);
        //合成最终图片
        Bitmap ResBitmap = Bitmap.createBitmap(width, resHeight, Bitmap.Config.ARGB_4444);
        //将原图以及倒影图片绘制到最终图片上
        Canvas canvas = new Canvas(ResBitmap);
        canvas.drawBitmap(source, 0, 0, null);
        canvas.drawBitmap(cutBitmap, 0, height+interval, null);

        //为倒影部分绘制遮罩
        Paint paint = new Paint();
        LinearGradient linear = new LinearGradient(0, height+interval, 0, resHeight, startAlpha, endAlpha, Shader.TileMode.CLAMP);
        paint.setShader(linear);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas.drawRect(0, height+interval, width, resHeight, paint);
        return ResBitmap;
    }
}
