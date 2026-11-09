package com.example.simplegamespace

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast

class FloatingService : Service() {

    private lateinit var windowManager: WindowManager
    private lateinit var floatingView: View
    private lateinit var expandedView: View
    private var isExpanded = false

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager

        floatingView = LayoutInflater.from(this).inflate(R.layout.floating_widget, null)
        expandedView = LayoutInflater.from(this).inflate(R.layout.expanded_menu, null)

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else
                WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
            x = 100
            y = 100
        }

        windowManager.addView(floatingView, params)

        val fabButton = floatingView.findViewById<View>(R.id.fab_icon)
        fabButton.setOnClickListener {
            toggleMenu(params)
        }

        expandedView.findViewById<Button>(R.id.btn_powersave).setOnClickListener {
            Toast.makeText(this, "تم تفعيل وضع توفير الطاقة", Toast.LENGTH_SHORT).show()
        }

        expandedView.findViewById<Button>(R.id.btn_balanced).setOnClickListener {
            Toast.makeText(this, "تم تفعيل الوضع المتوازن", Toast.LENGTH_SHORT).show()
        }

        expandedView.findViewById<Button>(R.id.btn_performance).setOnClickListener {
            Toast.makeText(this, "تم تفعيل وضع الأداء العالي", Toast.LENGTH_SHORT).show()
        }
    }

    private fun toggleMenu(params: WindowManager.LayoutParams) {
        if (!isExpanded) {
            windowManager.removeView(floatingView)
            windowManager.addView(expandedView, params)
            isExpanded = true
        } else {
            windowManager.removeView(expandedView)
            windowManager.addView(floatingView, params)
            isExpanded = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::floatingView.isInitialized) windowManager.removeView(floatingView)
        if (::expandedView.isInitialized) windowManager.removeView(expandedView)
    }
}
