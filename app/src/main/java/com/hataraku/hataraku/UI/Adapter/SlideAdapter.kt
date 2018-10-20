package com.hataraku.hataraku.UI.Adapter

import android.content.Context
import android.os.Parcelable
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.hataraku.hataraku.R

class SlideAdapter(private val context: Context?) : PagerAdapter() {

    private var inflater: LayoutInflater? = null
    private val images =
            arrayOf(R.drawable.ic_book, R.drawable.ic_book, R.drawable.ic_book)

    override fun instantiateItem(view: ViewGroup, position: Int): Any {
        inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val imageLayout = inflater!!.inflate(R.layout.image_slide, view, false)!!

        val imageView = imageLayout
                .findViewById(R.id.main_slide) as ImageView

        imageView.setImageResource(images[position])

        view.addView(imageLayout, 0)

        return imageLayout
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return images.size
    }

    override fun restoreState(state: Parcelable?, loader: ClassLoader?) {}

    override fun saveState(): Parcelable? {
        return null
    }

}