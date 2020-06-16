package cn.blinkdagger.multiskinmanager.sample

import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import cn.blinkdagger.multiskinmanager.MultiSkinManager
import cn.blinkdagger.multiskinmanager.resources.SkinResourceManager
import cn.blinkdagger.multiskinmanager.util.ThemeUtil
import kotlinx.android.synthetic.main.activity_second.*
import java.io.File

class SecondActivity : AppCompatActivity() {

    private val SKIN_NAME = "NightMode.skin"
    private val SKIN_DIR: String = Environment.getExternalStorageDirectory().absolutePath + File.separator.toString() + SKIN_NAME


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        btn_one.setOnClickListener {
            MultiSkinManager.instance.loadLocalSkin(this, SKIN_DIR)
        }
    }

}
