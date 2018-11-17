package com.hataraku.hataraku.UI.Fragment

import android.app.DatePickerDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.navigation.Navigation
import com.hataraku.hataraku.R
import es.dmoral.toasty.Toasty
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
        var spinAdapter: ArrayAdapter<Any> = spin_kategoriLowongan.adapter as ArrayAdapter<Any>
        var pos: Int = spinAdapter.getPosition(arguments!!
                .getString("kategori", "Pilih Kategori"))
        spin_kategoriLowongan.setSelection(pos)

        et_tglBatas.setText(SimpleDateFormat("dd/MM/yyyy").format(System.currentTimeMillis()))

        val c = Calendar.getInstance()
        var y = c.get(Calendar.YEAR)
        var month = c.get(Calendar.MONTH)
        var day = c.get(Calendar.DAY_OF_MONTH)

        val datepicker = DatePickerDialog(context, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            et_tglBatas.setText("" + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year)
            y = year
            month = (monthOfYear + 1)
            day = dayOfMonth
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH))
        datepicker.datePicker.minDate = Calendar.getInstance().timeInMillis
        et_tglBatas.setOnClickListener {
            datepicker.show()
        }
        spin_kategoriLowongan.setSelection(activity!!.intent.extras.getInt("kategori"))
        btn_review_lowongan.setOnClickListener {
            if (spin_kategoriLowongan.selectedItemPosition == 0) {
                Toasty.error(context!!, "Kategori belum dipilih", Toast.LENGTH_SHORT, true).show()
            } else {
                val bundle = Bundle()
                bundle.putInt("id_kategori", spin_kategoriLowongan.selectedItemPosition)
                bundle.putString("nama_kategori", spin_kategoriLowongan.selectedItem.toString())
                bundle.putString("judul", et_judul.text.toString())
                bundle.putString("isi", et_keterangan.text.toString())
                bundle.putString("skill", et_skill.text.toString())
                bundle.putString("alamat", et_alamat.text.toString())
                bundle.putString("budget", et_budget.text.toString())
                bundle.putInt("kategori", spin_kategoriLowongan.selectedItemPosition)
                bundle.putString("tgl_akhir", y.toString() + "-" + month + "-" + day)
                Navigation.findNavController(view).navigate(R.id.action_lowonganFragment_to_lowonganNextFragment, bundle)
            }
        }
    }

}