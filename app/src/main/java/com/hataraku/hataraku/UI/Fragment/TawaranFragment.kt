package com.hataraku.hataraku.UI.Fragment

import android.app.DatePickerDialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.hataraku.hataraku.R
import kotlinx.android.synthetic.main.fragment_lowongan.*
import kotlinx.android.synthetic.main.fragment_tawaran.*
import java.text.SimpleDateFormat
import java.util.*

class TawaranFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tawaran, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        et_tglBatasTawaran.setText(SimpleDateFormat("dd/MM/yyyy").format(System.currentTimeMillis()))

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val datepicker = DatePickerDialog(context, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            et_tglBatasTawaran.setText("" + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year)
        }, year, month, day)
        et_tglBatasTawaran.setOnClickListener {
            datepicker.show()
        }
    }
}
