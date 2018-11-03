package com.hataraku.hataraku.UI.Fragment

import android.app.DatePickerDialog
import android.content.Context
import android.content.SharedPreferences

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener

import com.hataraku.hataraku.R
import com.hataraku.hataraku.Utilities.ApiEndPoint
import com.hataraku.hataraku.Utilities.Preferences
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_biodata.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class BiodataFragment : Fragment() {
    lateinit var pref: SharedPreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_biodata, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pref = activity!!.getSharedPreferences(Preferences.HatarakuPreferences.name, Context.MODE_PRIVATE)
        activity?.title = "Edit Profile"
        et_tglLahir.setText(SimpleDateFormat("dd/MM/yyyy").format(System.currentTimeMillis()))

        val c = Calendar.getInstance()
        var y = c.get(Calendar.YEAR)
        var month = c.get(Calendar.MONTH)
        var day = c.get(Calendar.DAY_OF_MONTH)

        val datepicker = DatePickerDialog(context, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            et_tglLahir.setText("" + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year)
            y = year
            month = (monthOfYear + 1)
            day = dayOfMonth
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH))
        et_tglLahir.setOnClickListener {
            datepicker.show()
        }
        datepicker.datePicker.minDate = c.timeInMillis
        et_nama.setText(pref.getString(Preferences.NAMA.name, ""))
        et_email.setText(pref.getString(Preferences.EMAIL.name, ""))
        et_hp.setText(pref.getString(Preferences.NO_HP.name, ""))
        et_about.setText(pref.getString(Preferences.ABOUT.name, ""))
        et_alamat.setText(pref.getString(Preferences.ALAMAT.name, ""))
        if (pref.getString(Preferences.KELAMIN.name, "").equals("L")) {
            spin_jenisKelamin.setSelection(1)
        } else if (pref.getString(Preferences.KELAMIN.name, "").equals("P")) {
            spin_jenisKelamin.setSelection(2)
        }
        if (!pref.getString(Preferences.TGL_LAHIR.name, "").equals("0000-00-00")) {
            val dt = SimpleDateFormat("yyyy-MM-dd").parse(pref.getString(Preferences.TGL_LAHIR.name, ""))
            et_tglLahir.setText(SimpleDateFormat("dd-MM-yyyy").format(dt))
        }
        btn_simpan_profile.setOnClickListener {
            if (spin_jenisKelamin.selectedItemPosition != 0) {
                var kelamin = ""
                if (spin_jenisKelamin.selectedItemPosition == 1) {
                    kelamin = "L"
                } else if (spin_jenisKelamin.selectedItemPosition == 2) {
                    kelamin = "P"
                }
                AndroidNetworking.put(ApiEndPoint.MEMBER.value + "/" + pref.getInt(Preferences.ID_USER.name, 0))
                        .addHeaders("Content-Type", "application/json")
                        .addHeaders("X-API-Key", activity?.resources?.getString(R.string.x_api_key))
                        .addHeaders("Authorization", "Bearer " + pref.getString(Preferences.API_KEY.name, ""))
                        .addJSONObjectBody(JSONObject().put("id_user", pref.getInt(Preferences.ID_USER.name, 0))
                                .put("about", et_about.text.toString())
                                .put("tgl_lahir", y.toString() + "-" + month + "-" + day)
                                .put("jenis_kelamin", kelamin)
                                .put("no_hp", et_hp.text.toString())
                                .put("foto", "")
                                .put("alamat", et_alamat.text.toString()))
                        .build()
                        .getAsJSONObject(object : JSONObjectRequestListener {
                            override fun onResponse(response: JSONObject) {
                                val edit = pref.edit()
                                edit.putString(Preferences.ABOUT.name, et_about.text.toString())
                                edit.putString(Preferences.TGL_LAHIR.name, y.toString() + "-" + month + "-" + day)
                                edit.putString(Preferences.KELAMIN.name, kelamin)
                                edit.putString(Preferences.NO_HP.name, et_hp.text.toString())
                                edit.putString(Preferences.ALAMAT.name, et_alamat.text.toString())
                                edit.apply()
                                Toasty.success(context!!, "Sukses mengedit profil", Toast.LENGTH_SHORT, true).show()
                            }

                            override fun onError(anError: ANError?) {

                            }
                        })
            }
        }
    }


}
