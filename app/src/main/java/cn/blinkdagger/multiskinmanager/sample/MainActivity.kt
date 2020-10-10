package cn.blinkdagger.multiskinmanager.sample

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import cn.blinkdagger.multiskinmanager.MultiSkinManager
import cn.blinkdagger.multiskinmanager.resources.ProxyResource
import cn.blinkdagger.multiskinmanager.resources.SkinResourceManager
import cn.blinkdagger.multiskinmanager.util.ThemeUtil
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File


class MainActivity : AppCompatActivity() {

    private val SKIN_NAME = "NightMode.skin"
    private val SKIN_DIR: String =
        Environment.getExternalStorageDirectory().absolutePath + File.separator.toString() + SKIN_NAME

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_one.setOnClickListener {
            MultiSkinManager.instance.loadLocalSkin(this, SKIN_DIR)
        }
        btn_two.setOnClickListener {
            MultiSkinManager.instance.resetSkin()
        }
        btn_three.setOnClickListener {
            if (resources is ProxyResource) {
                Log.e("currentRes", "proxy")
            } else {
                Log.e("currentRes", "origin")
            }
            btn_three.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
        }
    }

    override fun getResources(): Resources {
        return SkinResourceManager.instance.getCurrentResource() ?: super.getResources()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_confirm -> {
                startActivity(Intent(this, SecondActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
