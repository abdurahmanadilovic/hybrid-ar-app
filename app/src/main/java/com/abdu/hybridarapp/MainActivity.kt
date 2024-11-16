package com.abdu.hybridarapp

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.commit
import com.abdu.hybridarapp.presentation.PlanePlacement

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.commit {
            add(R.id.containerFragment, PlanePlacement::class.java, Bundle())
        }
    }
}