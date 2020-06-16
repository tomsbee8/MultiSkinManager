package cn.blinkdagger.multiskinmanager

import android.content.res.Resources
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.LayoutInflaterCompat
import cn.blinkdagger.multiskinmanager.resources.SkinResourceManager


/**
 * @Author ls
 * @Date  2020/6/8
 * @Description
 * @Version
 */
class SkinBaseActivity : AppCompatActivity() {

    var mAttrObserver: ViewSkinObserver? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        installViewFactory()
        super.onCreate(savedInstanceState)
    }

    override fun onStop() {
        mAttrObserver?.let {
            SkinResourceManager.instance.deleteObserver(it)
        }
        super.onStop()
    }

    private fun installViewFactory() {
        // 对xml布局支持换肤
        val layoutInflater = LayoutInflater.from(this)
        mAttrObserver = ViewSkinObserver()
        mAttrObserver?.let {
            val newFactory2 = SkinLayoutInflaterFactory(it)
            LayoutInflaterCompat.setFactory2(layoutInflater, newFactory2)
            SkinResourceManager.instance.addObserver(it)
        }
    }

    override fun getResources(): Resources {
        // 对代码中支持换肤
        return SkinResourceManager.instance.getCurrentResource() ?: super.getResources()
    }

}