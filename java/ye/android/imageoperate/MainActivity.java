package ye.android.imageoperate;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private ImageView iv_show;
    private Bitmap srcBitmap;
    private Bitmap copyBitmap;

    private int scaleFlag = 0;
    private int moveFlag = 0;
    private int rotateFlag = 0;
    private int mirrorFlag = 0;
    private int srcFlag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv_show = (ImageView) findViewById(R.id.iv_show);

        srcBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.course);


    }

    //缩放
    public void scaleImage(View view) {
        if (scaleFlag == 1) {
            iv_show.setImageBitmap(srcBitmap);
            scaleFlag = 0;
            return;
        }

        //创建原图的副本
        copyBitmap = Bitmap.createBitmap(srcBitmap.getWidth(), srcBitmap.getHeight(), srcBitmap.getConfig());

        Canvas canvas = new Canvas(copyBitmap);

        Paint paint = new Paint();

        Matrix matrix = new Matrix();

        matrix.setScale(0.5f, 0.5f);


        canvas.drawBitmap(srcBitmap, matrix, paint);

        iv_show.setImageBitmap(copyBitmap);

        scaleFlag = 1;
    }

    //平移
    public void moveImage(View view) {
        if (moveFlag == 1) {
            iv_show.setImageBitmap(srcBitmap);
            moveFlag = 0;
            return;
        }

        //创建原图的副本
        copyBitmap = Bitmap.createBitmap(srcBitmap.getWidth(), srcBitmap.getHeight(), srcBitmap.getConfig());

        Canvas canvas = new Canvas(copyBitmap);

        Paint paint = new Paint();

        Matrix matrix = new Matrix();

        matrix.setTranslate(0, 300);

        canvas.drawBitmap(srcBitmap, matrix, paint);

        iv_show.setImageBitmap(copyBitmap);

        moveFlag = 1;
    }

    //旋转
    public void rotateImage(View view) {
        if (rotateFlag == 1) {
            iv_show.setImageBitmap(srcBitmap);
            rotateFlag = 0;
            return;
        }

        //创建原图的副本
        copyBitmap = Bitmap.createBitmap(srcBitmap.getWidth(), srcBitmap.getHeight(), srcBitmap.getConfig());

        Canvas canvas = new Canvas(copyBitmap);

        Paint paint = new Paint();

        Matrix matrix = new Matrix();

        matrix.setRotate(30, copyBitmap.getWidth() / 2, copyBitmap.getHeight() / 2);

        canvas.drawBitmap(srcBitmap, matrix, paint);

        iv_show.setImageBitmap(copyBitmap);

        rotateFlag = 1;
    }

    // 镜面显示
    public void mirrorImage(View view) {
        if (mirrorFlag == 1) {
            iv_show.setImageBitmap(srcBitmap);
            mirrorFlag = 0;
            return;
        }

        //创建原图的副本
        copyBitmap = Bitmap.createBitmap(srcBitmap.getWidth(), srcBitmap.getHeight(), srcBitmap.getConfig());

        Canvas canvas = new Canvas(copyBitmap);

        Paint paint = new Paint();

        Matrix matrix = new Matrix();

        //反转
        matrix.setScale(-1, 1);
        //平移
        matrix.postTranslate(copyBitmap.getWidth(), 0);

        canvas.drawBitmap(srcBitmap, matrix, paint);

        iv_show.setImageBitmap(copyBitmap);
        mirrorFlag = 1;
    }

    // 显示原图
    public void srcImage(View view) {
        iv_show.setImageBitmap(srcBitmap);
    }

    //加载大图片
    public void loadBigImage(View view) {
        //获取屏幕尺寸
        WindowManager manager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        manager.getDefaultDisplay().getSize(point);
        int scrX = point.x;
        int scrY = point.y;

        //获取图片尺寸
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), R.drawable.before, options);
        int imgX = options.outWidth;
        int imgY = options.outHeight;

        //计算缩放比
        int scaleX = imgX / scrX;
        int scaleY = imgY / scrY;

        int scale = scaleX >= scaleY ? scaleX : scaleY;

        if (scale > 1) {
            options.inSampleSize = scale;
        }

        options.inJustDecodeBounds = false;

        Bitmap simpleBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.before, options);


        iv_show.setImageBitmap(simpleBitmap);

        Toast.makeText(MainActivity.this, "屏幕尺寸：" + scrX + ".." + scrY + "\r\n" + "原图尺寸：" + imgX + ".." + imgY + "\r\n" + "放缩后：" + simpleBitmap.getWidth() + ".." + simpleBitmap.getHeight() + "\r\n放缩比：" + scaleX + ".." + scaleY, Toast.LENGTH_LONG).show();
    }

    //画图界面
    public void drawImage(View view) {
        this.startActivity(new Intent(this, DrawActivity.class));
    }

    //撕衣服案例
    public void clothes(View view) {
        this.startActivity(new Intent(this, ClothesActivity.class));
    }
}
