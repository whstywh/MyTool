package com.example.mytool

import android.content.Intent
import android.net.VpnService
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mytool.vpn.MVPNService

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    /*TODO: 开启VPN*/
    fun startVpnService() {
        val mVpnIntent = VpnService.prepare(this)

        if (mVpnIntent != null)
            startActivityForResult(mVpnIntent, 10000)
        else {
            val intent = Intent(mVpnIntent, MVPNService::class.java)
            startService(intent)
        }
    }




    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 10000 && resultCode == RESULT_OK) {
            startService(Intent(this, MVPNService::class.java))
        }
    }

}
