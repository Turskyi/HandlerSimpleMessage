package io.github.turskyi.handlersimplemessage

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    companion object {
        const val STATUS_NONE = 0

        const val STATUS_CONNECTING = 1

        const val STATUS_CONNECTED = 2
    }


    private var h: Handler? = null

    var tvStatus: TextView? = null
    var pbConnect: ProgressBar? = null
    var btnConnect: Button? = null

    /** Called when the activity is first created.  */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tvStatus = findViewById<View>(R.id.tvStatus) as TextView
        pbConnect = findViewById<View>(R.id.pbConnect) as ProgressBar
        btnConnect = findViewById<View>(R.id.btnConnect) as Button
        h = object : Handler(Looper.getMainLooper()) {
            override
            fun handleMessage(msg: Message) {
                when (msg.what) {
                    STATUS_NONE -> {
                        btnConnect?.isEnabled = true
                        tvStatus!!.text = getString(R.string.not_connected)
                    }
                    STATUS_CONNECTING -> {
                        btnConnect?.isEnabled = false
                        pbConnect!!.visibility = View.VISIBLE
                        tvStatus!!.text = getString(R.string.connecting)
                    }
                    STATUS_CONNECTED -> {
                        pbConnect!!.visibility = View.GONE
                        tvStatus!!.text = getString(R.string.connected)
                    }
                }
            }
        }
        h?.sendEmptyMessage(STATUS_NONE)
    }

    fun onclick(@Suppress("UNUSED_PARAMETER") v: View?) {
        val t = Thread {
            try {
                h?.sendEmptyMessage(STATUS_CONNECTING)
                TimeUnit.SECONDS.sleep(2)

                h?.sendEmptyMessage(STATUS_CONNECTED)

                TimeUnit.SECONDS.sleep(3)

                h?.sendEmptyMessage(STATUS_NONE)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
        t.start()
    }
}