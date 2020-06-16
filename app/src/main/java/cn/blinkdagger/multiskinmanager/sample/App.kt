package cn.blinkdagger.multiskinmanager.sample

import android.app.Application
import android.content.Context
import android.util.Log
import cn.blinkdagger.multiskinmanager.MultiSkinManager
import cn.blinkdagger.multiskinmanager.resources.ProxyResource
import cn.blinkdagger.multiskinmanager.resources.SkinResourceManager

/**
 * @Author ls
 * @Date  2020/6/4
 * @Description
 * @Version
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        MultiSkinManager.instance.init(this)
    }

}