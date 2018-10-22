package com.hataraku.hataraku.UI.Fragment

import android.os.Bundle
import android.os.Handler
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation.findNavController
import com.hataraku.hataraku.R
import com.hataraku.hataraku.UI.Adapter.SlideAdapter
import kotlinx.android.synthetic.main.fragment_project.*
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

        initProyek()

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

    private fun initProyek() {
        var bundle = Bundle()
        proyek_bangun.setOnClickListener {
            bundle.putString("kategori", "Bangun")
            findNavController(it).navigate(R.id.lowonganFragment, bundle)
        }
        proyek_renov.setOnClickListener {
            bundle.putString("kategori", "Renovasi")
            findNavController(it).navigate(R.id.lowonganFragment, bundle)
        }
        proyek_cat.setOnClickListener {
            bundle.putString("kategori", "Cat")
            findNavController(it).navigate(R.id.lowonganFragment, bundle)
        }
        proyek_ledeng.setOnClickListener {
            bundle.putString("kategori", "Ledeng")
            findNavController(it).navigate(R.id.lowonganFragment, bundle)
        }
        proyek_listrik.setOnClickListener {
            bundle.putString("kategori", "Listrik")
            findNavController(it).navigate(R.id.lowonganFragment, bundle)
        }
    }
}
