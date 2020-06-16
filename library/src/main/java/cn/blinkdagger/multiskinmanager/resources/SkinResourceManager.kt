package cn.blinkdagger.multiskinmanager.resources

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.content.res.Resources
import android.content.res.Resources.Theme
import cn.blinkdagger.multiskinmanager.SkinPathPreference
import cn.blinkdagger.multiskinmanager.listener.OnApplySkinListener
import java.io.File
import java.util.*

/**
 * @Author ls
 * @Date  2020/6/2
 * @Description
 * @Version
 */
class SkinResourceManager() : Observable() {

    // 外部皮肤包的Resource
    private var proxyResource: ProxyResource? = null
    // App本身的Resource
    private var mResource: Resources? = null
    // 皮肤包路径
    private var loadedPackPath: String? = null
    // 是否使用皮肤包
    private var useSkinResources = false

    companion object {
        @JvmStatic
        val instance: SkinResourceManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            SkinResourceManager()
        }
    }

    fun getCurrentResource(): Resources? {
        return if (useSkinResources) {
            proxyResource
        } else {
            mResource
        }
    }

    fun getProxyTheme(styleName :String): Theme?{
        return  proxyResource?.getStyleTargetIdentifier(styleName)
    }

    fun getAppResource() = mResource

    // 需要读写权限
    fun loadLocalSkin(context: Context, packPath: String, listener: OnApplySkinListener? = null) {
        if (packPath.isEmpty() || packPath == loadedPackPath) {
            return
        }

        val file = File(packPath)
        if (file.exists() && file.isFile) {

            try {
                val assetManager = AssetManager::class.java.newInstance()
                val method = assetManager.javaClass.getMethod("addAssetPath", String::class.java)
                method.isAccessible = true
                method.invoke(assetManager, packPath)

                val appResource = context.resources
                mResource = appResource

                val skinPackResources = Resources(assetManager, appResource.displayMetrics, appResource.configuration)
                proxyResource = ProxyResource(appResource.assets, appResource.displayMetrics, appResource.configuration)

                // 获取皮肤包的包名
                val packPkgName: String? = context.packageManager.getPackageArchiveInfo(packPath, PackageManager.GET_ACTIVITIES).packageName
                packPkgName?.let {
                    useSkinResources = true
                    proxyResource?.prepare(skinPackResources, appResource, packPkgName)

                    //通知观察者更新
                    setChanged()
                    notifyObservers()

                    //保存到本地路径
                    loadedPackPath = packPath
                    SkinPathPreference.instance.setAppCurrentSkin(packPath)

                    listener?.onApplySkinSuccess(packPath)
                } ?: kotlin.run {
                    // 皮肤包文件错误
                    listener?.onSkinPackageError()
                }
            } catch (e: Exception) {
                listener?.onApplySkinException(e)
            }
        } else {
            // 皮肤路径错误
            listener?.onSkinPathError()
        }
    }

    fun resetSkin() {
        loadedPackPath = null
        useSkinResources = false
        proxyResource = null

        //通知观察者更新
        setChanged()
        notifyObservers()

        SkinPathPreference.instance.removeAppCurrentSkin()
    }

}
