package cn.blinkdagger.multiskinmanager.util

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.content.res.Resources.Theme
import android.os.Build
import android.util.Log
import java.lang.reflect.Field


/**
 * @Author ls
 * @Date  2020/6/10
 * @Description
 * @Version
 */
class ThemeUtil {

    companion object {
        @JvmStatic
        fun getThemeResTd( theme: Resources.Theme): Int {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val fThemeImpl: Field = theme.javaClass.getDeclaredField("mThemeImpl")
                fThemeImpl.isAccessible = true
                val mThemeImpl: Any = fThemeImpl.get(theme)
                val fThemeResId: Field = mThemeImpl.javaClass.getDeclaredField("mThemeResId")
                fThemeResId.isAccessible = true
                fThemeResId.getInt(mThemeImpl)
            } else {
                val fThemeResId: Field = theme.javaClass.getDeclaredField("mThemeResId")
                fThemeResId.isAccessible = true
                fThemeResId.getInt(theme)
            }
        }
        @JvmStatic
        fun getThemeResName(activity: Activity, theme: Resources.Theme): String {
            return try {
                val mThemeResId: Int = getThemeResTd(theme)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    theme.resources.getResourceEntryName(mThemeResId)
                } else {
                    activity.resources.getResourceEntryName(mThemeResId)
                }
            } catch (e: Exception) {
                // Theme returned by application#getTheme() is always Theme.DeviceDefault
                "Theme.DeviceDefault"
            }
        }

    }
}