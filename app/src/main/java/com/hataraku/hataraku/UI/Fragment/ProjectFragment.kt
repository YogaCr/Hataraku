package com.hataraku.hataraku.UI.Fragment

import android.os.Bundle
import android.os.Handler
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hataraku.hataraku.R
import com.hataraku.hataraku.UI.Adapter.SlideAdapter
import java.util.*

class ProjectFragment : Fragment() {

    internal lateinit var viewPager: ViewPager
    internal lateinit var indicator: TabLayout
    var currentPage = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_project, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPager = view.findViewById(R.id.slider_pager) as ViewPager
        indicator = view.findViewById(R.id.indicator) as TabLayout

        val adapter = SlideAdapter(context)
        viewPager.adapter = adapter
        indicator.setupWithViewPager(viewPager, true)


        val handler = Handler()
        val Update = Runnable {
            if (currentPage == adapter.count) {
                currentPage = 0
            }
            viewPager.setCurrentItem(currentPage++, true)
        }
        val swipeTimer = Timer()
        swipeTimer.schedule(object : TimerTask() {
            override fun run() {
                handler.post(Update)
            }
        }, 3000, 3000)
    }
}
