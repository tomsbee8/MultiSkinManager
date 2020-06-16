package cn.blinkdagger.multiskinmanager.listener

/**
 * @Author ls
 * @Date  2020/6/2
 * @Description
 * @Version
 */
abstract class OnApplySkinListener {

    /**
     * 皮肤包文件路径错误
     */
    fun onSkinPathError(){}

    /**
     * 皮肤包文件错误，不是正确的皮肤包
     */
    fun onSkinPackageError(){}

    /**
     * 皮肤包加载出错
     */
    abstract fun onApplySkinException(e : Throwable)

    /**
     * 皮肤包加载成功
     */
    abstract fun onApplySkinSuccess(skinPath : String)

}