package cn.blinkdagger.multiskinmanager

import android.app.Application
import android.content.Context
import android.view.LayoutInflater
import androidx.core.view.LayoutInflaterCompat
import cn.blinkdagger.multiskinmanager.listener.OnApplySkinListener
import cn.blinkdagger.multiskinmanager.resources.SkinResourceManager

/**
 * @Author ls
 * @Date  2020/6/2
 * @Description
 * @Version
 */
class MultiSkinManager {

    private var initialized = false

    companion object{
        @JvmStatic
        val instance : MultiSkinManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            MultiSkinManager()
        }
    }

    fun init(app: Application){
        if(initialized){
            return
        }

        SkinPathPreference.instance.init(app)

        val layoutInflater = LayoutInflater.from(app)
        val mLayoutField = LayoutInflater::class.java.getDeclaredField("mFactorySet")
        mLayoutField.isAccessible = true
        mLayoutField.set(layoutInflater, false)
        val newFactory2 = SkinLayoutInflaterFactory(ViewSkinObserver())
        LayoutInflaterCompat.setFactory2(layoutInflater, newFactory2)

        app.registerActivityLifecycleCallbacks(SkinActivityLifecycle())

        // 加载已经设置的皮肤包
        SkinPathPreference.instance.getAppCurrentSkin()?.let { packPath ->
            SkinResourceManager.instance.loadLocalSkin(app,packPath)
        }?: kotlin.run {
            SkinResourceManager.instance.resetSkin()
        }
        initialized = true
    }

    /**
     * 加载设置皮肤包
     */
    fun loadLocalSkin(context : Context, packPath : String,listener : OnApplySkinListener? = null){
        SkinResourceManager.instance.loadLocalSkin(context,packPath, listener)
    }

    /**
     * 恢复默认皮肤
     */
    fun resetSkin(){
        SkinResourceManager.instance.resetSkin()
    }

}