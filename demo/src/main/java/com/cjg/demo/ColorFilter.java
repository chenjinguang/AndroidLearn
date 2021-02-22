package com.cjg.demo;

import androidx.annotation.IntDef;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author chenjinguang
 * @描述
 * @创建时间 2021/2/5
 * @修改人和其它信息
 */
public class ColorFilter {


    public static final int COLOR_YELLOW = 0;
    public static final int COLOR_RED = 1;
    public static final int COLOR_GREEN = 2;
    public static final int COLOR_BLUE = 3;


    @IntDef(flag = true,value = {COLOR_YELLOW,COLOR_RED, COLOR_GREEN,COLOR_BLUE})
    @Target({ElementType.PARAMETER,ElementType.METHOD,ElementType.FIELD})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Model{

    }

    private @Model int color = COLOR_YELLOW;
    public void setColor(@Model int color){
        this.color = color;
    }

    @Model
    public int getColor(){
        return this.color;
    }

    public void test(){
        ColorFilter colorFilter = new ColorFilter();
        colorFilter.setColor(COLOR_BLUE);
    }

}
