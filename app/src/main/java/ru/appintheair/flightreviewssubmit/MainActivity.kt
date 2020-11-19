package ru.appintheair.flightreviewssubmit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(R.id.container, SubmitScreen())
                .commit()
        }

        KeyboardUtil(this, findViewById(R.id.container))
    }
}