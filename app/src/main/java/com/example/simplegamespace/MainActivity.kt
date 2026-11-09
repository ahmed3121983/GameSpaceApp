package com.example.simplegamespace

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkOverlayPermission()

        val btnStart = findViewById<Button>(R.id.btn_start_service)
        btnStart.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
                Toast.makeText(this, "برجاء السماح بالإذن أولاً ليظهر الزر العائم", Toast.LENGTH_SHORT).show()
                checkOverlayPermission()
            } else {
                val intent = Intent(this, FloatingService::class.java)
                startService(intent)
                Toast.makeText(this, "تم تشغيل Game Space بنجاح، اخرج وفتح أي لعبة!", Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }

    private fun checkOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:$packageName")
                )
                startActivityForResult(intent, 2201)
            }
        }
    }
}
