package io.polydev.public_transport.utils

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.Result

import me.dm7.barcodescanner.zxing.ZXingScannerView


class QrCodeScanner: AppCompatActivity(), ZXingScannerView.ResultHandler{



    private lateinit var mScannerView: ZXingScannerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mScannerView = ZXingScannerView(this)
        setContentView(mScannerView)
        setUpWindow()

    }

    override fun onResume() {
        super.onResume()
        mScannerView.setResultHandler(this)
        mScannerView.startCamera()
        mScannerView.setAspectTolerance(0.5f)

    }


    public override fun onPause() {
        super.onPause()
        mScannerView.stopCamera()
    }

    private fun setUpWindow() {
        window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        window.statusBarColor = Color.TRANSPARENT
    }

    override fun handleResult(rawResult: Result) {
        val intent = Intent()
        intent.putExtra(AppConstants.KEY_QR_CODE, rawResult.text)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }



}