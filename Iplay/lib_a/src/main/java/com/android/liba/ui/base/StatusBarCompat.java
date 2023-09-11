package com.android.liba.ui.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.liba.R;
import com.android.liba.ui.base.titlebar.TitleBarConfig;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


public class StatusBarCompat {
    public static final int DEFAULT_STATUS_BAR_ALPHA = 60;
    private static final int FAKE_STATUS_BAR_VIEW_ID = R.id.statusbarutil_fake_status_bar_view;
    private static final int FAKE_TRANSLUCENT_VIEW_ID = R.id.statusbarutil_translucent_view;
    private static final int TAG_KEY_HAVE_SET_OFFSET = -123;

    public static void setColor(Activity activity, @ColorInt int color) {
        setColor(activity, color, DEFAULT_STATUS_BAR_ALPHA);
    }

    public static void setColor(Activity activity, @ColorInt int color, @IntRange(from = 0, to = 255) int statusBarAlpha) {
        boolean lightMode = isWhiteColor(color);
        if (lightMode) {
            setLightMode(activity);
        } else if (isBlackColor(color)) {
            setDarkMode(activity);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            boolean supportApiM = lightMode && statusBarAlpha < DEFAULT_STATUS_BAR_ALPHA && Build.VERSION.SDK_INT < Build.VERSION_CODES.M;
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().setStatusBarColor(calculateStatusColor(color, supportApiM ? statusBarAlpha = DEFAULT_STATUS_BAR_ALPHA : statusBarAlpha));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
            View fakeStatusBarView = decorView.findViewById(FAKE_STATUS_BAR_VIEW_ID);
            if (fakeStatusBarView != null) {
                if (fakeStatusBarView.getVisibility() == View.GONE) {
                    fakeStatusBarView.setVisibility(View.VISIBLE);
                }
                fakeStatusBarView.setBackgroundColor(calculateStatusColor(color, statusBarAlpha));
            } else {
                int finalColor = calculateStatusColor(color, statusBarAlpha);
                decorView.addView(createStatusBarView(activity, finalColor));
            }
            setRootView(activity);
        }
    }




    @Deprecated
    public static void setColorDiff(Activity activity, @ColorInt int color) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        transparentStatusBar(activity);
        ViewGroup contentView = activity.findViewById(android.R.id.content);
        // 移除半透明矩形,以免叠加
        View fakeStatusBarView = contentView.findViewById(FAKE_STATUS_BAR_VIEW_ID);
        if (fakeStatusBarView != null) {
            if (fakeStatusBarView.getVisibility() == View.GONE) {
                fakeStatusBarView.setVisibility(View.VISIBLE);
            }
            fakeStatusBarView.setBackgroundColor(color);
        } else {
            contentView.addView(createStatusBarView(activity, color));
        }
        setRootView(activity);
    }

    public static void setTranslucent(Activity activity) {
        setTranslucent(activity, DEFAULT_STATUS_BAR_ALPHA);
    }

    public static void setTranslucent(Activity activity, @IntRange(from = 0, to = 255) int statusBarAlpha) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        setTransparent(activity);
        addTranslucentView(activity, statusBarAlpha);
    }

    public static void setTranslucentForCoordinatorLayout(Activity activity, @IntRange(from = 0, to = 255) int statusBarAlpha) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        transparentStatusBar(activity);
        addTranslucentView(activity, statusBarAlpha);
    }

    public static void setTransparent(Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        transparentStatusBar(activity);
        setRootView(activity);
    }

    @Deprecated
    public static void setTranslucentDiff(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 设置状态栏透明
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            setRootView(activity);
        }
    }

    public static void setColorForDrawerLayout(Activity activity, DrawerLayout drawerLayout, @ColorInt int color) {
        setColorForDrawerLayout(activity, drawerLayout, color, DEFAULT_STATUS_BAR_ALPHA);
    }

    public static void setColorNoTranslucentForDrawerLayout(Activity activity, DrawerLayout drawerLayout, @ColorInt int color) {
        setColorForDrawerLayout(activity, drawerLayout, color, 0);
    }

    public static void setColorForDrawerLayout(Activity activity, DrawerLayout drawerLayout, @ColorInt int color,
                                               @IntRange(from = 0, to = 255) int statusBarAlpha) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        // 生成一个状态栏大小的矩形
        // 添加 statusBarView 到布局中
        ViewGroup contentLayout = (ViewGroup) drawerLayout.getChildAt(0);
        View fakeStatusBarView = contentLayout.findViewById(FAKE_STATUS_BAR_VIEW_ID);
        if (fakeStatusBarView != null) {
            if (fakeStatusBarView.getVisibility() == View.GONE) {
                fakeStatusBarView.setVisibility(View.VISIBLE);
            }
            fakeStatusBarView.setBackgroundColor(color);
        } else {
            contentLayout.addView(createStatusBarView(activity, color), 0);
        }
        // 内容布局不是 LinearLayout 时,设置padding top
        if (!(contentLayout instanceof LinearLayout) && contentLayout.getChildAt(1) != null) {
            contentLayout.getChildAt(1)
                    .setPadding(contentLayout.getPaddingLeft(), getStatusBarHeight(activity) + contentLayout.getPaddingTop(),
                            contentLayout.getPaddingRight(), contentLayout.getPaddingBottom());
        }
        // 设置属性
        setDrawerLayoutProperty(drawerLayout, contentLayout);
        addTranslucentView(activity, statusBarAlpha);
    }

    private static void setDrawerLayoutProperty(DrawerLayout drawerLayout, ViewGroup drawerLayoutContentLayout) {
        ViewGroup drawer = (ViewGroup) drawerLayout.getChildAt(1);
        drawerLayout.setFitsSystemWindows(false);
        drawerLayoutContentLayout.setFitsSystemWindows(false);
        drawerLayoutContentLayout.setClipToPadding(true);
        drawer.setFitsSystemWindows(false);
    }

    @Deprecated
    public static void setColorForDrawerLayoutDiff(Activity activity, DrawerLayout drawerLayout, @ColorInt int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 生成一个状态栏大小的矩形
            ViewGroup contentLayout = (ViewGroup) drawerLayout.getChildAt(0);
            View fakeStatusBarView = contentLayout.findViewById(FAKE_STATUS_BAR_VIEW_ID);
            if (fakeStatusBarView != null) {
                if (fakeStatusBarView.getVisibility() == View.GONE) {
                    fakeStatusBarView.setVisibility(View.VISIBLE);
                }
                fakeStatusBarView.setBackgroundColor(calculateStatusColor(color, DEFAULT_STATUS_BAR_ALPHA));
            } else {
                // 添加 statusBarView 到布局中
                contentLayout.addView(createStatusBarView(activity, color), 0);
            }
            // 内容布局不是 LinearLayout 时,设置padding top
            if (!(contentLayout instanceof LinearLayout) && contentLayout.getChildAt(1) != null) {
                contentLayout.getChildAt(1).setPadding(0, getStatusBarHeight(activity), 0, 0);
            }
            // 设置属性
            setDrawerLayoutProperty(drawerLayout, contentLayout);
        }
    }

    public static void setTranslucentForDrawerLayout(Activity activity, DrawerLayout drawerLayout) {
        setTranslucentForDrawerLayout(activity, drawerLayout, DEFAULT_STATUS_BAR_ALPHA);
    }

    public static void setTranslucentForDrawerLayout(Activity activity, DrawerLayout drawerLayout,
                                                     @IntRange(from = 0, to = 255) int statusBarAlpha) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        setTransparentForDrawerLayout(activity, drawerLayout);
        addTranslucentView(activity, statusBarAlpha);
    }

    public static void setTransparentForDrawerLayout(Activity activity, DrawerLayout drawerLayout) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        ViewGroup contentLayout = (ViewGroup) drawerLayout.getChildAt(0);
        // 内容布局不是 LinearLayout 时,设置padding top
        if (!(contentLayout instanceof LinearLayout) && contentLayout.getChildAt(1) != null) {
            contentLayout.getChildAt(1).setPadding(0, getStatusBarHeight(activity), 0, 0);
        }

        // 设置属性
        setDrawerLayoutProperty(drawerLayout, contentLayout);
    }

    @Deprecated
    public static void setTranslucentForDrawerLayoutDiff(Activity activity, DrawerLayout drawerLayout) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 设置状态栏透明
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 设置内容布局属性
            ViewGroup contentLayout = (ViewGroup) drawerLayout.getChildAt(0);
            contentLayout.setFitsSystemWindows(true);
            contentLayout.setClipToPadding(true);
            // 设置抽屉布局属性
            ViewGroup vg = (ViewGroup) drawerLayout.getChildAt(1);
            vg.setFitsSystemWindows(false);
            // 设置 DrawerLayout 属性
            drawerLayout.setFitsSystemWindows(false);
        }
    }

    public static void setTransparentForImageView(Activity activity, View needOffsetView) {
        setTranslucentForImageView(activity, 0, needOffsetView);
    }

    public static void setTranslucentForImageView(Activity activity, View needOffsetView) {
        setTranslucentForImageView(activity, DEFAULT_STATUS_BAR_ALPHA, needOffsetView);
    }

    public static void setTranslucentForImageView(Activity activity, @IntRange(from = 0, to = 255) int statusBarAlpha,
                                                  View needOffsetView) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        setTransparentForWindow(activity);
        addTranslucentView(activity, statusBarAlpha);
        if (needOffsetView != null) {
            Object haveSetOffset = needOffsetView.getTag(TAG_KEY_HAVE_SET_OFFSET);
            if (haveSetOffset != null && (Boolean) haveSetOffset) {
                return;
            }
            ViewGroup.LayoutParams layoutParams = needOffsetView.getLayoutParams();
            if (!(layoutParams instanceof ViewGroup.MarginLayoutParams)) {
                layoutParams = new ViewGroup.MarginLayoutParams(layoutParams.width, layoutParams.height);
            }
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) layoutParams;
            params.topMargin = params.topMargin + getStatusBarHeight(activity);
            needOffsetView.setTag(TAG_KEY_HAVE_SET_OFFSET, true);
            needOffsetView.setLayoutParams(params);
        }
    }

    public static void setTranslucentForImageViewInFragment(Activity activity, View needOffsetView) {
        setTranslucentForImageViewInFragment(activity, DEFAULT_STATUS_BAR_ALPHA, needOffsetView);
    }

    public static void setTransparentForImageViewInFragment(Activity activity, View needOffsetView) {
        setTranslucentForImageViewInFragment(activity, 0, needOffsetView);
    }

    public static void setTranslucentForImageViewInFragment(Activity activity, @IntRange(from = 0, to = 255) int statusBarAlpha,
                                                            View needOffsetView) {
        setTranslucentForImageView(activity, statusBarAlpha, needOffsetView);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            clearPreviousSetting(activity);
        }
    }

    public static void hideFakeStatusBarView(Activity activity) {
        ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        View fakeStatusBarView = decorView.findViewById(FAKE_STATUS_BAR_VIEW_ID);
        if (fakeStatusBarView != null) {
            fakeStatusBarView.setVisibility(View.GONE);
        }
        View fakeTranslucentView = decorView.findViewById(FAKE_TRANSLUCENT_VIEW_ID);
        if (fakeTranslucentView != null) {
            fakeTranslucentView.setVisibility(View.GONE);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static void setLightMode(Activity activity) {
        setLightMode(activity, false);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static void setLightMode(Activity activity, boolean fitsSystemUI) {
        setMIUIStatusBarDarkIcon(activity, true);
        setMeizuStatusBarDarkIcon(activity, true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int uiVisibility = activity.getWindow().getDecorView().getSystemUiVisibility();
            uiVisibility |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            if (fitsSystemUI) {
                uiVisibility |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            }
            activity.getWindow().getDecorView().setSystemUiVisibility(uiVisibility);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static void setDarkMode(Activity activity) {
        setDarkMode(activity, false);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static void setDarkMode(Activity activity, boolean fitsSystemUI) {
        setMIUIStatusBarDarkIcon(activity, false);
        setMeizuStatusBarDarkIcon(activity, false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int uiVisibility = activity.getWindow().getDecorView().getSystemUiVisibility();
            uiVisibility &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            if (fitsSystemUI) {
                uiVisibility |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            }
            activity.getWindow().getDecorView().setSystemUiVisibility(uiVisibility);
        }
    }

    private static void setMIUIStatusBarDarkIcon(@NonNull Activity activity, boolean darkIcon) {
        Class<? extends Window> clazz = activity.getWindow().getClass();
        try {
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            int darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(activity.getWindow(), darkIcon ? darkModeFlag : 0, darkModeFlag);
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    private static void setMeizuStatusBarDarkIcon(@NonNull Activity activity, boolean darkIcon) {
        try {
            WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
            Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
            Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
            darkFlag.setAccessible(true);
            meizuFlags.setAccessible(true);
            int bit = darkFlag.getInt(null);
            int value = meizuFlags.getInt(lp);
            if (darkIcon) {
                value |= bit;
            } else {
                value &= ~bit;
            }
            meizuFlags.setInt(lp, value);
            activity.getWindow().setAttributes(lp);
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static void clearPreviousSetting(Activity activity) {
        ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        View fakeStatusBarView = decorView.findViewById(FAKE_STATUS_BAR_VIEW_ID);
        if (fakeStatusBarView != null) {
            decorView.removeView(fakeStatusBarView);
            ViewGroup rootView = (ViewGroup) ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
            rootView.setPadding(0, 0, 0, 0);
        }
    }

    private static void addTranslucentView(Activity activity, @IntRange(from = 0, to = 255) int statusBarAlpha) {
        ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
        View fakeTranslucentView = contentView.findViewById(FAKE_TRANSLUCENT_VIEW_ID);
        if (fakeTranslucentView != null) {
            if (fakeTranslucentView.getVisibility() == View.GONE) {
                fakeTranslucentView.setVisibility(View.VISIBLE);
            }
            fakeTranslucentView.setBackgroundColor(Color.argb(statusBarAlpha, 0, 0, 0));
        } else {
            contentView.addView(createTranslucentStatusBarView(activity, statusBarAlpha));
        }
    }

    private static View createStatusBarView(Activity activity, @ColorInt int finalColor) {
        // 绘制一个和状态栏一样高的矩形
        View statusBarView = new View(activity);
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(activity));
        statusBarView.setLayoutParams(params);
        statusBarView.setBackgroundColor(finalColor);
        statusBarView.setId(FAKE_STATUS_BAR_VIEW_ID);
        return statusBarView;
    }

    private static void setRootView(Activity activity) {
        ViewGroup parent = activity.findViewById(android.R.id.content);
        for (int i = 0, count = parent.getChildCount(); i < count; i++) {
            View childView = parent.getChildAt(i);
            if (childView instanceof ViewGroup) {
                childView.setFitsSystemWindows(true);
                ((ViewGroup) childView).setClipToPadding(true);
            }
        }
    }

    private static void setTransparentForWindow(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
            activity.getWindow()
                    .getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow()
                    .setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static void transparentStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    private static View createTranslucentStatusBarView(Activity activity, int alpha) {
        // 绘制一个和状态栏一样高的矩形
        View statusBarView = new View(activity);
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(activity));
        statusBarView.setLayoutParams(params);
        statusBarView.setBackgroundColor(Color.argb(alpha, 0, 0, 0));
        statusBarView.setId(FAKE_TRANSLUCENT_VIEW_ID);
        return statusBarView;
    }

    public static int getStatusBarHeight(Context context) {
        // 获得状态栏高度
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }

    private static int calculateStatusColor(@ColorInt int color, int alpha) {
        if (alpha == 0) {
            return color;
        }
        float a = 1 - alpha / 255f;
        int red = color >> 16 & 0xff;
        int green = color >> 8 & 0xff;
        int blue = color & 0xff;
        red = (int) (red * a + 0.5);
        green = (int) (green * a + 0.5);
        blue = (int) (blue * a + 0.5);
        return 0xff << 24 | red << 16 | green << 8 | blue;
    }

    public static int calculateColorLightMode(@ColorInt int color, int alpha) {
        return calculateColorLightMode(color, alpha, 0);
    }

    public static int calculateColorLightMode(@ColorInt int color, int alpha, int darkAlpha) {
        int red = color >> 16 & 0xff;
        int green = color >> 8 & 0xff;
        int blue = color & 0xff;
        if (darkAlpha > 0) {
            float a = 1 - darkAlpha / 255f;
            red = (int) (red * a + 0.5);
            green = (int) (green * a + 0.5);
            blue = (int) (blue * a + 0.5);
        }
        return alpha << 24 | red << 16 | green << 8 | blue;
    }

    public static void setStatusColorForImageView(Activity activity, @ColorInt int color, @IntRange(from = 0, to = 255) int statusBarAlpha, View needOffsetView) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        setFullScreenForWindow(activity, isWhiteColor(color));
        setColorWithAlpha(activity, color, statusBarAlpha);
        if (needOffsetView != null) {
            Object haveSetOffset = needOffsetView.getTag(TAG_KEY_HAVE_SET_OFFSET);
            if (haveSetOffset != null && (Boolean) haveSetOffset) {
                return;
            }
            ViewGroup.LayoutParams layoutParams = needOffsetView.getLayoutParams();
            if (!(layoutParams instanceof ViewGroup.MarginLayoutParams)) {
                layoutParams = new ViewGroup.MarginLayoutParams(layoutParams.width, layoutParams.height);
            }
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) layoutParams;
            params.topMargin = params.topMargin + getStatusBarHeight(activity);
            needOffsetView.setTag(TAG_KEY_HAVE_SET_OFFSET, true);
            needOffsetView.setLayoutParams(params);
        }
    }

    private static boolean isWhiteColor(@ColorInt int color) {
        if (color == Color.WHITE) {
            return true;
        }
//        int red = color >> 16 & 0xff;
//        int green = color >> 8 & 0xff;
//        int blue = color & 0xff;
//        return red == 255 | green == 255 | blue == 255;
        return false;
    }

    private static boolean isBlackColor(@ColorInt int color) {
        if (color == Color.BLACK) {
            return true;
        }
        return false;
    }

    public static void setColorWithAlpha(Activity activity, @ColorInt int color, @IntRange(from = 0, to = 255) int statusBarAlpha) {
        boolean lightMode = isWhiteColor(color);
        if (lightMode) {
            setLightMode(activity);
        } else {
            setDarkMode(activity);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            boolean supportApiM = lightMode && Build.VERSION.SDK_INT < Build.VERSION_CODES.M;
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().setStatusBarColor(calculateColorLightMode(color, statusBarAlpha, supportApiM ? DEFAULT_STATUS_BAR_ALPHA : 0));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
            View fakeStatusBarView = decorView.findViewById(FAKE_STATUS_BAR_VIEW_ID);
            if (fakeStatusBarView != null) {
                if (fakeStatusBarView.getVisibility() == View.GONE) {
                    fakeStatusBarView.setVisibility(View.VISIBLE);
                }
                fakeStatusBarView.setBackgroundColor(calculateColorLightMode(color, statusBarAlpha));
            } else {
                int finalColor = calculateColorLightMode(color, statusBarAlpha);
                decorView.addView(createStatusBarView(activity, finalColor));
            }
            setRootView(activity);
        }
    }

    public static void setFullScreenForWindow(Activity activity, boolean lightMode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int uiVisibility = activity.getWindow().getDecorView().getSystemUiVisibility();
            uiVisibility |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            if (lightMode && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                uiVisibility |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            } else {
                uiVisibility |= View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            }
            activity.getWindow().getDecorView().setSystemUiVisibility(uiVisibility);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
    public static View handleView(TitleBarConfig titleConfigure, View contentView) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return noStatusBarView( titleConfigure, contentView);
        }
        return statusBarView( titleConfigure,contentView);
    }

    private static View noStatusBarView(TitleBarConfig titleConfigure, View contentView) {
        if (titleConfigure.getTitleBar() == null) {
            return contentView;
        } else {
                LinearLayout parent = new LinearLayout(titleConfigure.getActivity());
                parent.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                parent.setOrientation(LinearLayout.VERTICAL);
                parent.addView(titleConfigure.getTitleBar());
                parent.addView(contentView);
                return parent;
        }
    }
    public static View addTitleBar(TitleBarConfig titleConfigure, View contentView)
    {
        if (titleConfigure.getTitleBar() != null) {
            LinearLayout parent = new LinearLayout(titleConfigure.getActivity());
            parent.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            parent.setOrientation(LinearLayout.VERTICAL);
            parent.addView(titleConfigure.getTitleBar());

            parent.addView(contentView);
            if(contentView.getBackground() instanceof  ColorDrawable)
            {
                ColorDrawable drawable=(ColorDrawable)contentView.getBackground();
                parent.setBackgroundColor(drawable.getColor());
            }
            return parent;
        } else {
            return contentView;
        }
    }
    private static View statusBarView(TitleBarConfig titleConfigure, View contentView) {
        if (titleConfigure.getLayoutMode() == TitleBarConfig.LayoutMode.FULLSCREEN) {
            setTransparentForImageView(titleConfigure.getActivity(), null);
            return contentView;
        } else if (titleConfigure.getLayoutMode() == TitleBarConfig.LayoutMode.TITLE_BAR) {
            setColor(titleConfigure.getActivity(), ContextCompat.getColor(titleConfigure.getActivity(), titleConfigure.getColor()), 0);
            return  addTitleBar( titleConfigure,  contentView);
        } else {
            return contentView;
        }
    }

}
