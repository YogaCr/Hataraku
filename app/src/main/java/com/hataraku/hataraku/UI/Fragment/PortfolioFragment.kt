package com.hataraku.hataraku.UI.Fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.hataraku.hataraku.R
import com.hataraku.hataraku.Utilities.ApiEndPoint
import com.hataraku.hataraku.Utilities.Preferences
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_portfolio.*
import org.json.JSONObject
import java.io.File


class PortfolioFragment : Fragment() {
    lateinit var file: File
    lateinit var pref: SharedPreferences
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_portfolio, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnFile.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Pilih Gambar"), 1)
        }
        pref = activity!!.getSharedPreferences(Preferences.HatarakuPreferences.name, Context.MODE_PRIVATE)
        btn_simpan.setOnClickListener {
            AndroidNetworking.post(ApiEndPoint.PORTOFOLIO.value)
                    .addHeaders("Content-Type", "application/json")
                    .addHeaders("X-API-Key", activity?.resources?.getString(R.string.x_api_key))
                    .addHeaders("Authorization", "Bearer " + pref.getString(Preferences.API_KEY.name, ""))
                    .addBodyParameter("id_user", pref.getInt(Preferences.ID_USER.name, 0).toString())
                    .addBodyParameter("id_kategori", arguments!!.getInt("id_kategori").toString())
                    .addBodyParameter("judul", et_judul.text.toString())
                    .addBodyParameter("isi", et_keterangan.text.toString())
                    .addBodyParameter("file", "")
                    .build()
                    .getAsJSONObject(object : JSONObjectRequestListener {
                        override fun onResponse(response: JSONObject?) {
                            Toasty.success(context!!, "Sukses menambahkan portfolio", Toast.LENGTH_SHORT, true).show()
                            Navigation.findNavController(view).navigate(R.id.action_portfolioFragment_to_tawaranFragment)
                        }

                        override fun onError(anError: ANError?) {
                            Log.d("Portfolio error", anError!!.errorBody)
                        }
                    })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (requestCode == Activity.RESULT_OK) {
                var uri = data!!.data
                file = File(getRealPathFromURI(uri))

            }
        }
    }

    private fun getRealPathFromURI(contentURI: Uri): String? {
        val result: String?
        val cursor = activity!!.contentResolver.query(contentURI, null, null, null, null)
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.path
        } else {
            cursor.moveToFirst()
            val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            result = cursor.getString(idx)
            cursor.close()
        }
        return result
    }
}
