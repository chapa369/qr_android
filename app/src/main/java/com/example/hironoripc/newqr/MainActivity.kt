package com.example.hironoripc.newqr

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult

class MainActivity : AppCompatActivity() {

    private val url = "http://192.168.100.91:1881/qr"
    private val sleep_time = 30

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        val integrator = IntentIntegrator(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.setPrompt("  ")
        integrator.setCameraId(0) // Use a specific camera of the device
        integrator.setBeepEnabled(false)
        integrator.setBarcodeImageEnabled(true)
        integrator.initiateScan()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(applicationContext, "Cancelled", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(applicationContext, "Scanned: " + result.contents, Toast.LENGTH_LONG).show()

                //ここでQRから読み取ったurlをresult_textに渡す
                val result_text = result.contents

                sendResult(result_text)
                Toast.makeText(applicationContext, "message sended!", Toast.LENGTH_LONG).show()

                //待ち時間を作る
                try {
                    Thread.sleep((sleep_time * 1000).toLong())
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    protected fun sendResult(s: String) {
        //final TextView mTextView = (TextView) findViewById(R.id.text);

        val queue = Volley.newRequestQueue(this)

        val send_text = "$url?qr=$s"

        // Request a string response from the provided URL.
        val stringRequest = StringRequest(Request.Method.GET, send_text,
                Response.Listener { response ->
                    // Display the first 500 characters of the response string.
                    Toast.makeText(applicationContext, "Response is: $response", Toast.LENGTH_LONG).show()
                }, Response.ErrorListener { Toast.makeText(applicationContext, "That didn't work!", Toast.LENGTH_LONG).show() })

        // Add the request to the RequestQueue.
        queue.add(stringRequest)
    }
}
