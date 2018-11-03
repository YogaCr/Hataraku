package com.hataraku.hataraku.UI.Fragment

import android.app.DatePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.navigation.Navigation
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.hataraku.hataraku.Model.PortfolioModel
import com.hataraku.hataraku.R
import com.hataraku.hataraku.Utilities.ApiEndPoint
import com.hataraku.hataraku.Utilities.Preferences
import dmax.dialog.SpotsDialog
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_tawaran.*
import kotlinx.android.synthetic.main.spinner_layout.view.*
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class TawaranFragment : Fragment() {
    lateinit var pref: SharedPreferences
    var year = 0
    var day = 0
    var month = 0
    val listPortofolio: MutableList<PortfolioModel> = mutableListOf()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tawaran, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pref = activity?.getSharedPreferences(Preferences.HatarakuPreferences.name, Context.MODE_PRIVATE)!!
        et_tglBatasTawaran.setText(SimpleDateFormat("dd/MM/yyyy").format(System.currentTimeMillis()))

        val c = Calendar.getInstance()
        year = c.get(Calendar.YEAR)
        month = c.get(Calendar.MONTH) + 1
        day = c.get(Calendar.DAY_OF_MONTH)

        val datepicker = DatePickerDialog(context, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            et_tglBatasTawaran.setText("" + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year)
            this.year = year
            month = (monthOfYear + 1)
            day = dayOfMonth
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH))
        datepicker.datePicker.minDate = Calendar.getInstance().timeInMillis
        et_tglBatasTawaran.setOnClickListener {
            datepicker.show()
        }
        initPortfolio()
        btn_simpan_tawaran.setOnClickListener {
            var portfolio = " "
            if (listPortofolio.size > 0) {
                portfolio = listPortofolio[spin_portofolio.selectedItemPosition].id.toString()
            }
            AndroidNetworking.post(ApiEndPoint.TAWARAN.value)
                    .addHeaders("Content-Type", "application/json")
                    .addHeaders("X-API-Key", activity?.resources?.getString(R.string.x_api_key))
                    .addHeaders("Authorization", "Bearer " + pref.getString(Preferences.API_KEY.name, ""))
                    .addBodyParameter("id_user", pref.getInt(Preferences.ID_USER.name, 0).toString())
                    .addBodyParameter("id_lowongan", arguments?.getInt("id").toString())
                    .addBodyParameter("id_portfolio", portfolio)
                    .addBodyParameter("proposal", et_proposal.text.toString())
                    .addBodyParameter("tarif", et_tarif.text.toString())
                    .addBodyParameter("file", "")
                    .addBodyParameter("tgl_selesai", year.toString() + "-" + month + "-" + day)
                    .build()
                    .getAsJSONObject(object : JSONObjectRequestListener {
                        override fun onResponse(response: JSONObject?) {
                            Navigation.findNavController(it).navigate(R.id.action_tawaranFragment_to_lowonganDetailFragment)
                            Toasty.success(context!!, "Sukses menambah tawaran", Toast.LENGTH_SHORT).show()
                        }

                        override fun onError(anError: ANError?) {
                            Log.d("tawaran error", anError!!.errorBody)
                        }
                    })

        }
        tv_tambahPortfilio.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("id_kategori", arguments!!.getInt("id_kategori"))
            Navigation.findNavController(view).navigate(R.id.action_tawaranFragment_to_portfolioFragment, bundle)
        }
    }

    fun initPortfolio() {
        val context = context
        val dialog = SpotsDialog.Builder()
                .setContext(context!!)
                .setMessage("Harap Tunggu")
                .setCancelable(false)
                .build()
                .apply {
                    show()
                }
//        Toasty.info(context, pref.getInt(Preferences.ID_USER.name, 0).toString(), Toast.LENGTH_SHORT).show()
        AndroidNetworking.get(ApiEndPoint.PORTOFOLIO.value + "?id_user=" + pref.getInt(Preferences.ID_USER.name, 0) + "&id_kategori=" + arguments?.getInt("id_kategori"))
                .addHeaders("Content-Type", "application/json")
                .addHeaders("X-API-Key", activity?.resources?.getString(R.string.x_api_key))
                .addHeaders("Authorization", "Bearer " + pref.getString(Preferences.API_KEY.name, ""))
                .build()
                .getAsJSONArray(object : JSONArrayRequestListener {
                    override fun onResponse(response: JSONArray) {
                        var x = 0
                        while (x < response.length()) {
                            val obj = response.getJSONObject(x)
                            val p = PortfolioModel(obj.getInt("id"), obj.getString("judul"), obj.getString("isi"), obj.getString("kategori"))
                            listPortofolio.add(p)
                            x = x.inc()
                        }
                        spin_portofolio.adapter = CustomAdapter(context, R.layout.spinner_layout, listPortofolio)
                        dialog.dismiss()
                    }

                    override fun onError(anError: ANError?) {

                    }
                })
    }
}

class CustomAdapter(val c: Context, val res: Int, val portfolio: MutableList<PortfolioModel>) : ArrayAdapter<PortfolioModel>(c, res, portfolio) {
    val inflater = LayoutInflater.from(c)
    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createItemView(position, convertView, parent)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createItemView(position, convertView, parent)
    }

    fun createItemView(position: Int, convertView: View?, parent: ViewGroup): View {
        val v = inflater.inflate(res, parent, false)
        v.tv_judul.text = portfolio[position].judul
        v.tv_kategori.text = portfolio[position].kategori
        return v
    }
}
