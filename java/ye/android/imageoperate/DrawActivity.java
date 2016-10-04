package ye.android.imageoperate;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by ye on 2016/8/17.
 */
public class DrawActivity extends AppCompatActivity {
    private Bitmap resBitmap;
    private Bitmap destBitmap;
    private ImageView iv_draw;
    private Paint paint;
    private Matrix matrix;
    private Canvas canvas;

    private boolean colorFlag = true;
    private boolean widthFlag = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);

        iv_draw = (ImageView) findViewById(R.id.iv_draw);

        resBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.back);

        //创建原图的副本
        destBitmap = Bitmap.createBitmap(resBitmap.getWidth(), resBitmap.getHeight(), resBitmap.getConfig());

        canvas = new Canvas(destBitmap);

        paint = new Paint();

        matrix = new Matrix();

        canvas.drawBitmap(resBitmap, matrix, paint);

        iv_draw.setImageBitmap(destBitmap);

        iv_draw.setOnTouchListener(new View.OnTouchListener() {
            private int startX;
            private int startY;
            private int stopX;
            private int stopY;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = (int) motionEvent.getX();
                        startY = (int) motionEvent.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        stopX = (int) motionEvent.getX();
                        stopY = (int) motionEvent.getY();

                        canvas.drawLine(startX, startY, stopX, stopY, paint);
                        iv_draw.setImageBitmap(destBitmap);

                        startX = stopX;
                        startY = stopY;
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                }

                return true;
            }
        });

    }

    @Override
    public void onBackPressed() {
        backMain(new View(this));
    }

    public void backMain(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("注意");
        builder.setMessage("你要保存这幅画吗？");
        builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                File dir = new File(Environment.getExternalStorageDirectory().getPath() + "/mypngs/");
                dir.mkdirs();
                //将图片保存到sd卡
                final File file = new File(dir, System.currentTimeMillis() + ".png");
                try {
                    FileOutputStream stream = new FileOutputStream(file);
                    destBitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                new Thread() {
                    @Override
                    public void run() {
                        sendBroad();
                    }
                };

                Toast.makeText(DrawActivity.this, "图片保存成功！", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        builder.setNegativeButton("不保存", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });

        builder.show();
    }

    private void sendBroad() {
        //发送sd卡挂载广播让图库应用加载新的图片
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MEDIA_MOUNTED);
        intent.setData(Uri.fromFile(Environment.getExternalStorageDirectory()));
        sendBroadcast(intent);
    }

    public void changeColor(View view) {
        if (colorFlag) {
            paint.setColor(Color.BLUE);
            colorFlag = false;
        } else {
            paint.setColor(Color.BLACK);
            colorFlag = true;
        }
    }

    public void changeWidth(View view) {
        if (widthFlag) {
            paint.setStrokeWidth(10);
            widthFlag = false;
        } else {
            paint.setStrokeWidth(1);
            widthFlag = true;
        }
    }

    public void clearImage(View view) {
        //创建原图的副本
        destBitmap = Bitmap.createBitmap(resBitmap.getWidth(), resBitmap.getHeight(), resBitmap.getConfig());

        canvas = new Canvas(destBitmap);

        paint = new Paint();

        matrix = new Matrix();

        canvas.drawBitmap(resBitmap, matrix, paint);

        iv_draw.setImageBitmap(destBitmap);

        colorFlag = true;
        widthFlag = true;
    }
}
