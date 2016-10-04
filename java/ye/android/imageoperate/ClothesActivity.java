package ye.android.imageoperate;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by ye on 2016/8/18.
 */
public class ClothesActivity extends AppCompatActivity {
    private ImageView iv_before;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clothes);

        iv_before = (ImageView) findViewById(R.id.iv_before);

        Bitmap simBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.preimg);

        //创建原图的副本，便于操作
        final Bitmap preBitmap = Bitmap.createBitmap(simBitmap.getWidth(), simBitmap.getHeight(), simBitmap.getConfig());
        Canvas canvas = new Canvas(preBitmap);
        canvas.drawBitmap(simBitmap, new Matrix(), new Paint());

        iv_before.setImageBitmap(preBitmap);

        iv_before.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        try {
                            for (int i = -10; i < 10; i++) {
                                for (int j = -10; j < 10; j++) {
                                    if (Math.sqrt(i * i + j * j) < 10) {            //加上这个判断的话，触摸笔迹是一个圆，不加则为矩形
                                        preBitmap.setPixel((int) motionEvent.getX() + i, (int) motionEvent.getY() + j, Color.TRANSPARENT);
                                    }
                                }
                            }
                        } catch (Exception e) {
                            Log.i("info", e.toString());
                        }
                        iv_before.setImageBitmap(preBitmap);
                        break;
                }

                return true;
            }
        });
    }

    public void backMain(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("注意");
        builder.setMessage("你确定退出吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(ClothesActivity.this, "明智的选择！", Toast.LENGTH_SHORT).show();
            }
        });

        builder.show();
    }
}
