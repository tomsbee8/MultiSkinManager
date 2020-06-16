package cn.blinkdagger.multiskinmanager.resources

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.*
import android.graphics.Movie
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.TypedValue
import java.io.InputStream


/**
 * @Author ls
 * @Date  2020/6/2
 * @Description
 * @Version
 */
class ProxyResource(assets: AssetManager, metrics: DisplayMetrics, config: Configuration) : Resources(assets, metrics, config) {

    private var mPackResources: Resources? = null
    private var mResources: Resources? = null
    private var mResPackPkgName: String? = null


    fun prepare(packResources: Resources, appResources: Resources,packPkgName : String){
        this.mPackResources = packResources
        this.mResources = appResources
        this.mResPackPkgName = packPkgName
    }

    /**
     * 获取插件包中的资源Id
     */
    protected  fun getTargetIdentifier(resId: Int): Int {
        return try {
            val resName: String? = mResources?.getResourceEntryName(resId)
            val resType: String? = mResources?.getResourceTypeName(resId)
            mPackResources?.getIdentifier(resName, resType, mResPackPkgName)?: 0
        } catch (e: Throwable) {
            // 有些情况就是很特殊 比如webView的34800147资源 使用mAppResources.getResourceEntryName会抛出 notfound 异常 但是使用getString 却又可以拿到这个资源的字符串
            0
        }
    }

    /**
     * 获取插件包中的theme
     */
    fun getStyleTargetIdentifier(styleName: String): Theme?  {
        mPackResources?.let {resources ->
            return try {
                val resId = resources.getIdentifier(styleName, "style", mResPackPkgName)
                val newTheme = resources.newTheme()
                newTheme?.applyStyle(resId, true)
                newTheme
            } catch (e: java.lang.Exception) {
                null
            }
        }?: kotlin.run {
            return null
        }
    }

    override fun getColorStateList(id: Int): ColorStateList {
       val packId = getTargetIdentifier(id)
        if(packId == 0){
            // 资源包中没有此ID 的资源
            return mResources?.getColorStateList(id)?: super.getColorStateList(id)
        }else{
            return mPackResources?.getColorStateList(packId) ?: super.getColorStateList(id)
        }
    }

    override fun getColor(id: Int): Int {
        val packId = getTargetIdentifier(id)
        if(packId == 0){
            // 资源包中没有此ID 的资源
            return mResources?.getColor(id)?: super.getColor(id)
        }else{
            return mPackResources?.getColor(packId) ?: super.getColor(id)
        }
    }

    override fun getString(id: Int): String {
        val packId = getTargetIdentifier(id)
        if(packId == 0){
            // 资源包中没有此ID 的资源
            return mResources?.getString(id)?: super.getString(id)
        }else{
            return mPackResources?.getString(packId) ?: super.getString(id)
        }
    }

    override fun getDrawable(id: Int): Drawable {
        val packId = getTargetIdentifier(id)
        if(packId == 0){
            // 资源包中没有此ID 的资源
            return mResources?.getDrawable(id)?: super.getDrawable(id)
        }else{
            return mPackResources?.getDrawable(packId) ?: super.getDrawable(id)
        }
    }



    override fun getColorStateList(id: Int, theme: Theme?): ColorStateList {
        val packId = getTargetIdentifier(id)

        return if(packId == 0 ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mResources?.getColorStateList(id, theme) ?: super.getColorStateList(id, theme)
            } else {
                super.getColorStateList(id, theme)
            }
        }else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mPackResources?.getColorStateList(packId, theme) ?:  super.getColorStateList(id, theme)
            }else{
                super.getColorStateList(id, theme)
            }
        }
    }

    override fun getColor(id: Int, theme: Theme?): Int {
        val packId = getTargetIdentifier(id)

        return if(packId == 0 ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mResources?.getColor(id, theme) ?: super.getColor(id, theme)
            } else {
                super.getColor(id, theme)
            }
        }else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mPackResources?.getColor(packId, theme) ?:  super.getColor(id, theme)
            }else{
                super.getColor(id, theme)
            }
        }
    }

    // -- Use default app resources start ---

    override fun getString(id: Int, vararg formatArgs: Any?): String {
        return mResources?.getString(id,formatArgs)?: super.getString(id, *formatArgs)
    }

    override fun getDrawable(id: Int, theme: Theme?): Drawable {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mResources?.getDrawable(id,theme)?:super.getDrawable(id, theme)
        } else {
            super.getDrawable(id, theme)
        }
    }

    override fun getTextArray(id: Int): Array<CharSequence> {
        return mResources?.getTextArray(id)?:super.getTextArray(id)
    }

    override fun obtainTypedArray(id: Int): TypedArray {
        return mResources?.obtainTypedArray(id)?:super.obtainTypedArray(id)
    }

    override fun getAnimation(id: Int): XmlResourceParser {
        return mResources?.getAnimation(id)?:super.getAnimation(id)
    }

    override fun getText(id: Int): CharSequence {
        return mResources?.getText(id)?:super.getText(id)
    }

    override fun getText(id: Int, def: CharSequence?): CharSequence {
        return mResources?.getText(id,def)?:super.getText(id, def)
    }

    override fun getDisplayMetrics(): DisplayMetrics {
        return mResources?.getDisplayMetrics()?:super.getDisplayMetrics()
    }

    override fun getDrawableForDensity(id: Int, density: Int): Drawable? {
        return mResources?.getDrawableForDensity(id, density)?: super.getDrawableForDensity(id, density)
    }

    override fun getDrawableForDensity(id: Int, density: Int, theme: Theme?): Drawable? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mResources?.getDrawableForDensity(id, density, theme)?: super.getDrawableForDensity(id, density, theme)
        } else {
            super.getDrawableForDensity(id, density, theme)
        }
    }

    override fun getConfiguration(): Configuration {
        return mResources?.getConfiguration()?: super.getConfiguration()
    }

    override fun obtainAttributes(set: AttributeSet?, attrs: IntArray?): TypedArray {
        return mResources?.obtainAttributes(set, attrs)?:super.obtainAttributes(set, attrs)
    }

    override fun getDimensionPixelSize(id: Int): Int {
        return mResources?.getDimensionPixelSize(id)?: super.getDimensionPixelSize(id)
    }

    override fun getIntArray(id: Int): IntArray {
        return mResources?.getIntArray(id)?: super.getIntArray(id)
    }

    override fun getValue(id: Int, outValue: TypedValue?, resolveRefs: Boolean) {
        return mResources?.getValue(id, outValue, resolveRefs)?:
        super.getValue(id, outValue, resolveRefs)
    }

    override fun getValue(name: String?, outValue: TypedValue?, resolveRefs: Boolean) {
        super.getValue(name, outValue, resolveRefs)
    }

    override fun getQuantityString(id: Int, quantity: Int, vararg formatArgs: Any?): String {
        return mResources?.getQuantityString(id, quantity, *formatArgs)?:
        super.getQuantityString(id, quantity, *formatArgs)
    }

    override fun getQuantityString(id: Int, quantity: Int): String {
        return mResources?.getQuantityString(id, quantity)?:
        super.getQuantityString(id, quantity)
    }

    override fun getResourcePackageName(resid: Int): String {
        return mResources?.getResourcePackageName(resid)?: super.getResourcePackageName(resid)
    }

    override fun getStringArray(id: Int): Array<String> {
        return mResources?.getStringArray(id)?:super.getStringArray(id)
    }

    override fun openRawResourceFd(id: Int): AssetFileDescriptor {
        return mResources?.openRawResourceFd(id)?: super.openRawResourceFd(id)
    }

    override fun getDimension(id: Int): Float {
        return mResources?.getDimension(id)?:super.getDimension(id)
    }

    override fun getBoolean(id: Int): Boolean {
        return mResources?.getBoolean(id)?:super.getBoolean(id)
    }

    override fun getIdentifier(name: String?, defType: String?, defPackage: String?): Int {
        return mResources?.getIdentifier(name, defType, defPackage)?:
        super.getIdentifier(name, defType, defPackage)
    }

    override fun getQuantityText(id: Int, quantity: Int): CharSequence {
        return mResources?.getQuantityText(id, quantity)?: super.getQuantityText(id, quantity)
    }

    override fun openRawResource(id: Int): InputStream {
        return mResources?.openRawResource(id)?:super.openRawResource(id)
    }

    override fun openRawResource(id: Int, value: TypedValue?): InputStream {
        return mResources?.openRawResource(id, value)?:super.openRawResource(id, value)
    }

    override fun getMovie(id: Int): Movie {
        return mResources?.getMovie(id)?: super.getMovie(id)
    }

    override fun getInteger(id: Int): Int {
        return mResources?.getInteger(id)?:super.getInteger(id)
    }

    override fun parseBundleExtras(parser: XmlResourceParser?, outBundle: Bundle?) {
        return mResources?.parseBundleExtras(parser, outBundle)?: super.parseBundleExtras(parser, outBundle)
    }

    override fun getResourceTypeName(resid: Int): String {
        return mResources?.getResourceTypeName(resid)?: super.getResourceTypeName(resid)
    }

    override fun getLayout(id: Int): XmlResourceParser {
        return mResources?.getLayout(id)?: super.getLayout(id)
    }

    override fun getFont(id: Int): Typeface {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mResources?.getFont(id)?:super.getFont(id)
        } else {
            super.getFont(id)
        }
    }

    override fun updateConfiguration(config: Configuration?, metrics: DisplayMetrics?) {
        return mResources?.updateConfiguration(config, metrics)?:
        super.updateConfiguration(config, metrics)
    }

    override fun getXml(id: Int): XmlResourceParser {
        return mResources?.getXml(id)?:
        super.getXml(id)
    }

    override fun getResourceName(resid: Int): String {
        return mResources?.getResourceName(resid)?:
        super.getResourceName(resid)
    }

    override fun parseBundleExtra(tagName: String?, attrs: AttributeSet?, outBundle: Bundle?) {
        return mResources?.parseBundleExtra(tagName, attrs, outBundle)?:
        super.parseBundleExtra(tagName, attrs, outBundle)
    }

    override fun getDimensionPixelOffset(id: Int): Int {
        return mResources?.getDimensionPixelOffset(id)?: super.getDimensionPixelOffset(id)
    }

    override fun getValueForDensity(
        id: Int,
        density: Int,
        outValue: TypedValue?,
        resolveRefs: Boolean
    ) {
        mResources?.getValueForDensity(id, density, outValue, resolveRefs)?:
        super.getValueForDensity(id, density, outValue, resolveRefs)
    }

    override fun getResourceEntryName(resid: Int): String {
        return mResources?.getResourceEntryName(resid)?:super.getResourceEntryName(resid)
    }

    override fun getFraction(id: Int, base: Int, pbase: Int): Float {
        return mResources?.getFraction(id,base, pbase)?:super.getFraction(id, base, pbase)
    }
}