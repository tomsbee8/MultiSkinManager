package cn.blinkdagger.multiskinmanager

import android.content.res.Resources
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.ViewCompat
import cn.blinkdagger.multiskinmanager.entity.AttrributeParams
import cn.blinkdagger.multiskinmanager.entity.ViewAttribute
import cn.blinkdagger.multiskinmanager.resources.ProxyResource
import cn.blinkdagger.multiskinmanager.resources.SkinResourceManager
import org.jetbrains.anko.custom.style
import java.util.*


/**
 * @Author ls
 * @Date  2020/6/3
 * @Description
 * @Version
 */
class ViewSkinObserver : Observer {

    private var mViewList: ArrayList<ViewAttribute> = ArrayList()

    companion object {
        val supportedAttributeSet = arrayOf(
            "background",
            "src",
            "drawableLeft",
            "drawableRight",
            "drawableTop",
            "drawableBottom",
            "textColor",
            "style"
        )
    }

    override fun update(observer: Observable, o: Any?) {
        mViewList.forEach {
            it.apply {
                applySkin(view, params)
            }
        }
    }

    fun applySkin(view: View, params: List<AttrributeParams>) {
        params.filter { param ->
            supportedAttributeSet.contains(param.attrTypeName) && param.attrValue.startsWith("@")
        }.forEachIndexed { index, param ->
            SkinResourceManager.instance.getCurrentResource()?.let { proxyResource ->
                try {
                    val valueId = param.attrValue.substring(1).toInt()
                    // 值的类型【可能为color或者drawable】
                    val typeName = SkinResourceManager.instance.getAppResource()?.getResourceTypeName(valueId).orEmpty()

                    var left: Drawable? = null
                    var right: Drawable? = null
                    var top: Drawable? = null
                    var bottom: Drawable? = null
                    when (param.attrTypeName) {
                        "textColor" -> setViewTextColor(view, typeName, valueId, proxyResource)
                        "background" -> setViewBackground(
                            view,
                            typeName,
                            valueId,
                            proxyResource
                        )
                        "src" -> setViewSrc(view, typeName, valueId, proxyResource)
                        "drawableLeft" -> {
                            left = proxyResource.getDrawable(valueId)
                        }
                        "drawableRight" -> {
                            right = proxyResource.getDrawable(valueId)
                        }
                        "drawableTop" -> {
                            top = proxyResource.getDrawable(valueId)
                        }
                        "drawableBottom" -> {
                            bottom = proxyResource.getDrawable(valueId)
                        }
                    }
                    if (view is TextView && (left != null || right != null || top != null || bottom != null)) {
                        view.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun cacheViewAttributeData(view: View, params: List<AttrributeParams>){
        params.filter { param ->
            supportedAttributeSet.contains(param.attrTypeName) && param.attrValue.startsWith("@")
        }.takeIf {
            it.isNotEmpty()
        }?.let {
            mViewList.add(ViewAttribute(view, params))
        }
    }

    fun setViewTextColor(view: View, typeName: String, valueId: Int, proxyResource: Resources) {
        if (view is TextView) {
            when (typeName) {
                ("color") -> {
                    view.setTextColor(proxyResource.getColor(valueId))
                }
                ("drawable") -> {
                    view.setTextColor(proxyResource.getColorStateList(valueId))
                }
            }
        }
    }

    fun setViewBackground(view: View, typeName: String, valueId: Int, proxyResource: Resources) {
        when (typeName) {
            ("color") -> {
                view.setBackgroundColor(proxyResource.getColor(valueId))
            }
            ("drawable") -> {
                ViewCompat.setBackground(view, proxyResource.getDrawable(valueId))
            }
        }
    }

    fun setViewSrc(view: View, typeName: String, valueId: Int, proxyResource: Resources) {
        if (view is ImageView) {
            when (typeName) {
                ("color") -> {
                    view.setImageDrawable(ColorDrawable(proxyResource.getColor(valueId)))
                }
                ("drawable") -> {
                    view.setImageDrawable(proxyResource.getDrawable(valueId))
                }
            }
        }
    }
}