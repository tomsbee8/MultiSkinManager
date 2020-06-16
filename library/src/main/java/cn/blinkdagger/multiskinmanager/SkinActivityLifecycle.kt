package cn.blinkdagger.multiskinmanager

import android.app.Activity
import android.app.Application
import android.app.Instrumentation
import android.app.UiAutomation
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ActivityInfo
import android.os.*
import android.view.ContextThemeWrapper
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.MotionEvent
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.LayoutInflaterCompat
import cn.blinkdagger.multiskinmanager.resources.SkinResourceManager
import cn.blinkdagger.multiskinmanager.util.ThemeUtil
import java.util.*


/**
 * @Author ls
 * @Date  2020/6/2
 * @Description
 * @Version
 */
class SkinActivityLifecycle : Application.ActivityLifecycleCallbacks {

    private val mObserverMap: WeakHashMap<Int, ViewSkinObserver> = WeakHashMap()

    override fun onActivityPaused(activity: Activity?) {

    }

    override fun onActivityResumed(activity: Activity?) {
    }

    override fun onActivityStarted(activity: Activity?) {
    }

    override fun onActivityDestroyed(activity: Activity?) {

    }

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
    }

    override fun onActivityStopped(activity: Activity?) {
        mObserverMap.remove(activity.hashCode())?.apply {
            SkinResourceManager.instance.deleteObserver(this)
        }
    }


    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
        activity?.let { context ->

            // 对xml布局支持换肤
            var layoutInflater = LayoutInflater.from(context)
            val mLayoutField = LayoutInflater::class.java.getDeclaredField("mFactorySet")
            mLayoutField.isAccessible = true
            mLayoutField.set(layoutInflater, false)
            val attrObserver = ViewSkinObserver()
            val newFactory2 = SkinLayoutInflaterFactory(attrObserver)
            LayoutInflaterCompat.setFactory2(layoutInflater, newFactory2)

            // 添加观察者
            mObserverMap[activity.hashCode()] = attrObserver
            SkinResourceManager.instance.addObserver(attrObserver)
        }
    }
}