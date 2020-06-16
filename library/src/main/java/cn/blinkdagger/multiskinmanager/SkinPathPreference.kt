package cn.blinkdagger.multiskinmanager

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

/**
 * @Author ls
 * @Date  2020/6/3
 * @Description
 * @Version
 */
class SkinPathPreference {

    private val KEY_SKIN_PERFERENCE = "multi-skin-perfenece"
    private val KEY_SKIN_PATH = "app-current-skin-path"

    private var  mPreference : SharedPreferences? = null


    companion object{
        @JvmStatic
        val instance : SkinPathPreference by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            SkinPathPreference()
        }
    }

    /**
     * 初始化
     */
    fun init(application: Application) {
        mPreference = application.getSharedPreferences(KEY_SKIN_PERFERENCE, Context.MODE_PRIVATE)
    }

    /**
     * 保存当前使用的皮肤
     */
    fun setAppCurrentSkin(skinPath : String){
        mPreference?.edit()?.putString(KEY_SKIN_PATH, skinPath)?.apply()
    }

    fun removeAppCurrentSkin(){
        mPreference?.edit()?.remove(KEY_SKIN_PATH)?.apply()
    }

    /**
     * 获取当前使用的皮肤
     */
    fun getAppCurrentSkin() : String? {
        return mPreference?.getString(KEY_SKIN_PATH, null)
    }

}