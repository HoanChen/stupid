package me.khrystal.bouncescrollview

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_house_targaryen.*
import kotlinx.android.synthetic.main.layout_toolbar.*

/**
 *
 * usage:
 * author: kHRYSTAL
 * create time: 18/6/25
 * update time:
 * email: 723526676@qq.com
 */
class HouseTargaryenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_house_targaryen)

        setSupportActionBar(mToolbar)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }

        mSwitch.setOnCheckedChangeListener { _, isChecked ->
            mBounceScrollViewVer.isIncrementalDamping = isChecked
            mBounceScrollViewHor.isIncrementalDamping = isChecked
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }
}