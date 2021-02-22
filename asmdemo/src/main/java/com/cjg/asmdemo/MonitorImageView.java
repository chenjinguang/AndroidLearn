package com.cjg.asmdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Looper;
import android.os.MessageQueue;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.cjg.asmdemo.util.Reflector;

import java.lang.reflect.Field;

/**
 * @author chenjinguang
 * @描述
 * @创建时间 2021/2/2
 * @修改人和其它信息
 */
public class MonitorImageView extends ImageView implements MessageQueue.IdleHandler {

    private static final String TAG = "MonitorImageView";
    private static final int MAX_ALARM_IMAGE_SIZE = 2 * 1024 * 1024;
    private static final int MAX_ALARM_MULTIPLE = 2;

    public MonitorImageView(Context context) {
        super(context);
    }

    public MonitorImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MonitorImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        addImageLegalMonitor();
    }

    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {
        super.setImageDrawable(drawable);
        addImageLegalMonitor();
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        addImageLegalMonitor();
    }

    @Override
    public void setBackgroundResource(int resId) {
        super.setBackgroundResource(resId);
        addImageLegalMonitor();
    }

    @Override
    public void setBackground(Drawable background) {
        super.setBackground(background);
        addImageLegalMonitor();
    }

    private void addImageLegalMonitor() {
        Looper.myQueue().removeIdleHandler(this);
        Looper.myQueue().addIdleHandler(this);
    }


    @Override
    public boolean queueIdle() {
        try {
            Drawable drawable = getDrawable();
            if (drawable != null) {
                checkIsLegal(drawable, "图片");
            }
            drawable = getBackground();
            if (drawable != null) {
                checkIsLegal(drawable, "背景");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void checkIsLegal(Drawable drawable, String tag) {

        int intrinsicWidth = drawable.getIntrinsicWidth();
        int intrinsicHeight = drawable.getIntrinsicHeight();

        int measuredHeight = getMeasuredHeight();
        int measuredWidth = getMeasuredWidth();

        int imageSize = calculateImageSize(drawable);
        if (imageSize > MAX_ALARM_IMAGE_SIZE) {
            Log.e(TAG, "图片不合法，" + tag + "大小 ->" + intrinsicWidth);
            dealWarning(drawable,intrinsicWidth, intrinsicHeight, measuredWidth, measuredHeight, imageSize);
        }

        if (intrinsicHeight > measuredHeight * MAX_ALARM_MULTIPLE) {
            Log.e(TAG, "图片加载不合法, 控件宽度 " + tag + "大小 ->" + intrinsicHeight);
            dealWarning(drawable,intrinsicWidth, intrinsicHeight, measuredWidth, measuredHeight, imageSize);
        }

        if (intrinsicWidth > measuredWidth * MAX_ALARM_MULTIPLE) {
            Log.e(TAG, "图片加载不合法, 控件宽度 " + tag + "大小 ->" + intrinsicWidth);
            dealWarning(drawable,intrinsicWidth, intrinsicHeight, measuredWidth, measuredHeight, imageSize);
        }
    }

    private void dealWarning(final Drawable drawable,final int intrinsicWidth, final int intrinsicHeight, final int measuredWidth,
                             final int measuredHeight, final double fileSize) {
        // 线上线下处理方式需要不一致，伪代码
        // 线下弹出提示窗口把信息输出，同时提供一个关闭打开开关
        if (BuildConfig.DEBUG) {
            Log.e(TAG, "图片不合适了");
            post(new Runnable() {
                @Override
                public void run() {
                    LargeImageManager.getInstance().showDialog(drawable, intrinsicWidth, intrinsicHeight, fileSize, 0, measuredWidth, measuredHeight);
                }
            });
        } else {
            // 线上需要搜集代码信息，代码具体在哪里，把信息上报到服务器
        }
        // ......

    }

    /**
     * 计算 drawable 的大小
     */
    private int calculateImageSize(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            return bitmap.getByteCount();
        }
        int pixelSize = drawable.getOpacity() != PixelFormat.OPAQUE ? 4 : 2;
        return pixelSize * drawable.getIntrinsicWidth() * drawable.getIntrinsicHeight();
    }


    public int getResource() {
        Field[] fields = getClass().getDeclaredFields();
        int imgid = 0;
        for (Field f : fields) {
            if (f.getName().equals("mResource")) {
                f.setAccessible(true);
                try {
                    imgid = f.getInt(this);
                    break;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return imgid;
    }

}
