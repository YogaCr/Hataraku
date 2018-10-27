package com.hataraku.hataraku.UI.Fragment

import android.app.Activity
import android.app.DatePickerDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.hataraku.hataraku.R
import kotlinx.android.synthetic.main.fragment_lowongan.*
import java.text.SimpleDateFormat
import java.util.*

class LowonganFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lowongan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = "Buat Lowongan"

        @Suppress("UNCHECKED_CAST")
        var spinAdapter:ArrayAdapter<Any> = spin_kategoriLowongan.adapter as ArrayAdapter<Any>
        var pos:Int = spinAdapter.getPosition(arguments!!
                .getString("kategori", "Pilih Kategori"))
        spin_kategoriLowongan.setSelection(pos)

        et_tglBatas.setText(SimpleDateFormat("dd/MM/yyyy").format(System.currentTimeMillis()))

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val datepicker = DatePickerDialog(context, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            et_tglBatas.setText("" + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year)
        }, year, month, day)
        et_tglBatas.setOnClickListener {
            datepicker.show()
        }

    }
}