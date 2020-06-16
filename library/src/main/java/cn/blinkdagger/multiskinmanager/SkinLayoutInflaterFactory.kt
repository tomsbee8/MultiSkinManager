package cn.blinkdagger.multiskinmanager

import android.content.Context
import android.content.ContextWrapper
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import cn.blinkdagger.multiskinmanager.entity.AttrributeParams
import java.lang.reflect.Constructor


/**
 * @Author ls
 * @Date  2020/6/1
 * @Description
 * @Version
 */
class SkinLayoutInflaterFactory(attrObserver: ViewSkinObserver) : LayoutInflater.Factory2 {

    /**
     * 保存view的构造方法
     */
    private val sConstructorMap: HashMap<String, Constructor<out View?>> =
        HashMap<String, Constructor<out View?>>()

    val mConstructorSignature = arrayOf(
        Context::class.java, AttributeSet::class.java
    )


    val a = arrayOf(
        "android.widget.",
        "android.view.",
        "android.webkit."
    )


    private val mAttrManager = attrObserver

    override fun onCreateView(parent: View?, name: String, context: Context, attrs: AttributeSet): View? {
        getActivityFromContext(context)?.let { activity ->
            var view: View? = activity.delegate.createView(parent, name, context, attrs)

            if (view == null) {
                view = createViewFormTag(name, context, attrs)
            }

            if (view == null) {
                view = createView(name, context, attrs)
            }

            view?.let {
                val params = ArrayList<AttrributeParams>()
                for (index in 0 until attrs.attributeCount) {
                    val attrName = attrs.getAttributeName(index)
                    val attrValue = attrs.getAttributeValue(index)
                    params.add(AttrributeParams(attrName,attrValue))
                    if(name.contains("toolbar",true)){
                        Log.d("ToolBarATttr", "name = $attrName  value = $attrValue")
                    }
                }
                mAttrManager.applySkin(it, params)
                mAttrManager.cacheViewAttributeData(it, params)

            }
            return view
        }
        return null
    }

    override fun onCreateView(name: String?, context: Context?, attrs: AttributeSet?): View? {
        return null
    }


    private fun getActivityFromContext(ctx: Context?): AppCompatActivity? {
        var context = ctx
        while (context is ContextWrapper) {
            if (context is AppCompatActivity) {
                return context
            }
            context = context.baseContext
        }
        return null
    }

    /**
     * 参考LayoutInflater源码
     *
     * @param name
     * @param context
     * @param attrs
     * @return
     */
    private fun createViewFormTag(name: String, context: Context, attrs: AttributeSet): View? {
        if (-1 != name.indexOf('.')) {
            return null
        }
        var view: View? = null
        for (element in a) {
            view = createView(element + name, context, attrs)
            if (view != null) {
                break
            }
        }
        return view
    }


    /**
     * 参考LayoutInflater源码
     * 获取构造函数，创建view
     *
     * @param name
     * @param context
     * @param attrs
     * @return
     */
    private fun createView(name: String, context: Context, attrs: AttributeSet): View? {
        val constructor =
            findConstructor(context, name)
        try {
            return constructor!!.newInstance(context, attrs)
        } catch (e: Exception) {
        }
        return null
    }


    /**
     * 参考LayoutInflater源码
     * 通过反射获取View构造函数
     *
     * @param context
     * @param name
     * @return
     */
    private fun findConstructor(context: Context, name: String): Constructor<out View>? {
        var constructor =
            sConstructorMap[name]
        if (null == constructor) {
            try {
                val clazz =
                    context.classLoader.loadClass(name).asSubclass(
                        View::class.java
                    )
                constructor = clazz.getConstructor(* mConstructorSignature)
                sConstructorMap[name] = constructor
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return constructor
    }
}