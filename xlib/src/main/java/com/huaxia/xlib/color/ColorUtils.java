package com.huaxia.xlib.color;

import android.graphics.Color;


import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.FloatRange;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.huaxia.xlib.common.Utils;


/**
 * 颜色工具类。
 * 参考 https://github.com/Blankj/AndroidUtilCode/blob/master/lib/utilcode/src/main/
 * java/com/blankj/utilcode/util/ColorUtils.java
 *
 * @author xzy
 */
@SuppressWarnings("unused")
public final class ColorUtils {

    private ColorUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }


    /**
     * 矫正颜色的透明度
     *
     * @param color  颜色值
     * @param factor 透明度值
     * @return 颜色整数值
     */
    @ColorInt
    public static int adjustAlpha(@ColorInt int color,
                                  @SuppressWarnings("SameParameterValue") float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }

    /**
     * Returns a color associated with a particular resource ID.
     *
     * @param id The desired resource identifier.
     * @return a color associated with a particular resource ID
     */
    public static int getColor(@ColorRes int id) {
        return ContextCompat.getColor(Utils.getApp(), id);
    }

    /**
     * Set the alpha component of {@code color} to be {@code alpha}.
     *
     * @param color The color.
     * @param alpha Alpha component \([0..255]\) of the color.
     * @return the {@code color} with {@code alpha} component
     */
    public static int setAlphaComponent(@ColorInt final int color,
                                        @IntRange(from = 0x0, to = 0xFF) int alpha) {
        return (color & 0x00ffffff) | (alpha << 24);
    }

    /**
     * Set the alpha component of {@code color} to be {@code alpha}.
     *
     * @param color The color.
     * @param alpha Alpha component \([0..1]\) of the color.
     * @return the {@code color} with {@code alpha} component
     */
    public static int setAlphaComponent(@ColorInt int color,
                                        @FloatRange(from = 0, to = 1) float alpha) {
        return (color & 0x00ffffff) | ((int) (alpha * 255.0f + 0.5f) << 24);
    }

    /**
     * Set the red component of {@code color} to be {@code red}.
     *
     * @param color The color.
     * @param red   Red component \([0..255]\) of the color.
     * @return the {@code color} with {@code red} component
     */
    public static int setRedComponent(@ColorInt int color,
                                      @IntRange(from = 0x0, to = 0xFF) int red) {
        return (color & 0xff00ffff) | (red << 16);
    }

    /**
     * Set the red component of {@code color} to be {@code red}.
     *
     * @param color The color.
     * @param red   Red component \([0..1]\) of the color.
     * @return the {@code color} with {@code red} component
     */
    public static int setRedComponent(@ColorInt int color,
                                      @FloatRange(from = 0, to = 1) float red) {
        return (color & 0xff00ffff) | ((int) (red * 255.0f + 0.5f) << 16);
    }

    /**
     * Set the green component of {@code color} to be {@code green}.
     *
     * @param color The color.
     * @param green Green component \([0..255]\) of the color.
     * @return the {@code color} with {@code green} component
     */
    public static int setGreenComponent(@ColorInt int color,
                                        @IntRange(from = 0x0, to = 0xFF) int green) {
        return (color & 0xffff00ff) | (green << 8);
    }

    /**
     * Set the green component of {@code color} to be {@code green}.
     *
     * @param color The color.
     * @param green Green component \([0..1]\) of the color.
     * @return the {@code color} with {@code green} component
     */
    public static int setGreenComponent(@ColorInt int color,
                                        @FloatRange(from = 0, to = 1) float green) {
        return (color & 0xffff00ff) | ((int) (green * 255.0f + 0.5f) << 8);
    }

    /**
     * Set the blue component of {@code color} to be {@code blue}.
     *
     * @param color The color.
     * @param blue  Blue component \([0..255]\) of the color.
     * @return the {@code color} with {@code blue} component
     */
    public static int setBlueComponent(@ColorInt int color,
                                       @IntRange(from = 0x0, to = 0xFF) int blue) {
        return (color & 0xffffff00) | blue;
    }

    /**
     * Set the blue component of {@code color} to be {@code blue}.
     *
     * @param color The color.
     * @param blue  Blue component \([0..1]\) of the color.
     * @return the {@code color} with {@code blue} component
     */
    public static int setBlueComponent(@ColorInt int color,
                                       @FloatRange(from = 0, to = 1) float blue) {
        return (color & 0xffffff00) | (int) (blue * 255.0f + 0.5f);
    }

    /**
     * Color-string to color-int.
     * <p>Supported formats are:</p>
     *
     * <ul>
     * <li><code>#RRGGBB</code></li>
     * <li><code>#AARRGGBB</code></li>
     * </ul>
     *
     * <p>The following names are also accepted: <code>red</code>, <code>blue</code>,
     * <code>green</code>, <code>black</code>, <code>white</code>, <code>gray</code>,
     * <code>cyan</code>, <code>magenta</code>, <code>yellow</code>, <code>lightgray</code>,
     * <code>darkgray</code>, <code>grey</code>, <code>lightgrey</code>, <code>darkgrey</code>,
     * <code>aqua</code>, <code>fuchsia</code>, <code>lime</code>, <code>maroon</code>,
     * <code>navy</code>, <code>olive</code>, <code>purple</code>, <code>silver</code>,
     * and <code>teal</code>.</p>
     *
     * @param colorString The color-string.
     * @return color-int
     */
    public static int string2Int(@NonNull String colorString) throws IllegalArgumentException {
        return Color.parseColor(colorString);
    }

    /**
     * Color-int to color-string.
     *
     * @param colorInt The color-int.
     * @return color-string
     */
    public static String int2RgbString(@ColorInt int colorInt) {
        colorInt = colorInt & 0x00ffffff;
        StringBuilder color = new StringBuilder(Integer.toHexString(colorInt));
        while (color.length() < 6) {
            color.insert(0, "0");
        }
        return "#" + color;
    }

    /**
     * Color-int to color-string.
     *
     * @param colorInt The color-int.
     * @return color-string
     */
    public static String int2ArgbString(@ColorInt final int colorInt) {
        StringBuilder color = new StringBuilder(Integer.toHexString(colorInt));
        while (color.length() < 6) {
            color.insert(0, "0");
        }
        while (color.length() < 8) {
            color.insert(0, "f");
        }
        return "#" + color;
    }

    /**
     * Return the random color.
     *
     * @return the random color
     */
    public static int getRandomColor() {
        return getRandomColor(true);
    }

    /**
     * Return the random color.
     *
     * @param supportAlpha True to support alpha, false otherwise.
     * @return the random color
     */
    public static int getRandomColor(final boolean supportAlpha) {
        int high = supportAlpha ? (int) (Math.random() * 0x100) << 24 : 0xFF000000;
        return high | (int) (Math.random() * 0x1000000);
    }
}
