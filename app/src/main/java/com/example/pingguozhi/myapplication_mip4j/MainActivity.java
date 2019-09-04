package com.example.pingguozhi.myapplication_mip4j;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.InputStream;

import static android.graphics.Color.blue;
import static android.graphics.Color.green;
import static android.graphics.Color.red;
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView im_mr;
    Button btn1 ;
    Button btn_LG ;
    SeekBar sb_2G;
    Context mContext;
    TextView txt_cur;
TextView tv_mr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        im_mr=findViewById(R.id.imageView_mr);
        ((Button) findViewById(R.id.button_mr)).setOnClickListener(this);
        ((Button) findViewById(R.id.button_LG)).setOnClickListener(this);
        ((Button) findViewById(R.id.button_re)).setOnClickListener(this);

        tv_mr=(TextView) findViewById(R.id.tv_mr);
        //InputStream is = aa.open("raw/mr.dcm");
        Bitmap bm_or= setDcm();
        im_mr.setImageBitmap(bm_or);
        bindViews();
    }


    public void onClick(View v) {
        Bitmap bitmap   =setDcm();
        switch (v.getId()) {
            case R.id.button_mr:

                Bitmap B2G =bitmap2Gray(bitmap);
                im_mr.setImageBitmap(B2G);
               // B2G.recycle();
                //B2G=null;
               // bitmap.recycle();
                //bitmap=null;
                break;
            case R.id.button_LG:
                Bitmap Bm_LG =bitmap2Gray(bitmap);
                Bm_LG =lineGrey(Bm_LG);
                im_mr.setImageBitmap(Bm_LG);
              //  Bm_LG.recycle();
               // Bm_LG=null;
               // bitmap.recycle();
              //  bitmap=null;
                break;
            case R.id.button_re:
                im_mr.setImageBitmap(bitmap);
              //  bitmap.recycle();
               // bitmap=null;
                break;


        }
    }
     void bindViews() {
        sb_2G = (SeekBar) findViewById(R.id.seekBar_2G);
         txt_cur = (TextView) findViewById(R.id.textView_2G);
        sb_2G.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                txt_cur.setText("對圖像進行二值化(" + progress + "  / 255) ");
                Bitmap bitmap   =setDcm();
                int ss=seekBar.getProgress();
                Bitmap Bm_2B =gray2Binary(setDcm(),ss);
                im_mr.setImageBitmap(Bm_2B);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
             //   Toast.makeText(mContext, "觸碰中SeekBar", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

               // Toast.makeText(mContext, "以放開SeekBar", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public Bitmap setDcm()
    {
        Resources resources = getResources();
        InputStream is = resources.openRawResource(R.raw.mr);
        MR mr = new MR(is);
        //short [] pa =mr.pixelArray;
        int a=mr.max-mr.min;
        int[] int_array = new int[mr.width * mr.height];
        for (int w = 0; w< ( mr.height); w++) {
            for (int h = 0; h < (mr.width ); h++){
                int tempnumber =(h * mr.width) + w;
                int_array[tempnumber]= (int) mr.pixelArray[tempnumber];
                float r = red ((int)mr.pixelArray[tempnumber]);
                float g = green ((int)mr.pixelArray[tempnumber]);
                float b = blue ((int)mr.pixelArray[tempnumber]);

                int_array[tempnumber] =Color.rgb(r,g,b);

                //System.out.println(int_array[(w * mr.width) + h]);


            }


        }
        System.out.println("Claer");

        Bitmap basic_bm = Bitmap.createBitmap(int_array, 512, 512, Bitmap.Config.RGB_565);
        im_mr.setImageBitmap(basic_bm);
        tv_mr.setText(mr.toString());
        return basic_bm;
    }
//圖像灰度化
    public Bitmap bitmap2Gray(Bitmap bmSrc) {
        // 得到圖片的長和寬
        int width = bmSrc.getWidth();
        int height = bmSrc.getHeight();
        // 創建目標灰度圖像
        Bitmap bmpGray = null;
        bmpGray = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        // 創建畫布
        Canvas c = new Canvas(bmpGray);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmSrc, 0, 0, paint);
        return bmpGray;
    }
    //對圖像進行線性灰度變化
    public Bitmap lineGrey(Bitmap image)
    {
        //得到圖像的寬度和長度
        int width = image.getWidth();
        int height = image.getHeight();
        //創建線性拉昇灰度圖像
        Bitmap linegray = null;
        linegray = image.copy(Bitmap.Config.ARGB_8888, true);
        //依次循環對圖像的像素進行處理
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                //得到每點的像素值
                int col = image.getPixel(i, j);
                int alpha = col & 0xFF000000;
                int red = (col & 0x00FF0000) >> 16;
                int green = (col & 0x0000FF00) >> 8;
                int blue = (col & 0x000000FF);
                // 增加了圖像的亮度
                red = (int) (1.1 * red + 30);
                green = (int) (1.1 * green + 30);
                blue = (int) (1.1 * blue + 30);
                //對圖像像素越界進行處理
                if (red >= 255)
                {
                    red = 255;
                }

                if (green >= 255) {
                    green = 255;
                }

                if (blue >= 255) {
                    blue = 255;
                }
                // 新的ARGB
                int newColor = alpha | (red << 16) | (green << 8) | blue;
                //設置新圖像的RGB值
                linegray.setPixel(i, j, newColor);
            }
        }
        return linegray;
    }
    //對圖像進行二值化
    public Bitmap gray2Binary(Bitmap graymap,int temp) {
        //得到圖形的寬度和長度
        int width = graymap.getWidth();
        int height = graymap.getHeight();
        //創建二值化圖像
        Bitmap binarymap = null;
        binarymap = graymap.copy(Bitmap.Config.ARGB_8888, true);
        //依次循環，對圖像的像素進行處理
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                //得到當前像素的值
                int col = binarymap.getPixel(i, j);
                //得到alpha通道的值
                int alpha = col & 0xFF000000;
                //得到圖像的像素RGB的值
                int red = (col & 0x00FF0000) >> 16;
                int green = (col & 0x0000FF00) >> 8;
                int blue = (col & 0x000000FF);
                // 用公式X = 0.3×R+0.59×G+0.11×B計算出X代替原來的RGB
                int gray = (int) ((float) red * 0.3 + (float) green * 0.59 + (float) blue * 0.11);
                //對圖像進行二值化處理
                if (gray <= temp) {
                    gray = 0;
                } else {
                    gray = 255;
                }
                // 新的ARGB
                int newColor = alpha | (gray << 16) | (gray << 8) | gray;
                //設置新圖像的當前像素值
                binarymap.setPixel(i, j, newColor);
            }
        }
        return binarymap;
    }

}
