package com.intelligence.findme.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.intelligence.findme.R
import com.intelligence.findme.models.Service
import com.intelligence.findme.util.GlideApp

class SpinnerAdapter(val context: Context, var listItems: List<Service>) : BaseAdapter() {

    private val mLayoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val vh: ItemRowHolder

        if (convertView == null) {
            view = mLayoutInflater.inflate(R.layout.view_drop_down_menu, parent, false)
            vh = ItemRowHolder(
                view
            )
            view?.tag = vh
        } else {
            view = convertView
            vh = view.tag as ItemRowHolder
        }

        val params = view.layoutParams
        params.height = 82
        view.layoutParams = params

        vh.label.text = listItems.get(position).category
        GlideApp.with(context)
            .load(listItems.get(position).image_url)
            .placeholder(R.drawable.ic_history)
            .into(vh.img)

        return view
    }

    override fun getItem(position: Int): Any? {
        return listItems.get(position)
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return listItems.size
    }

    private class ItemRowHolder(row: View?) {
        val label: TextView
        val img: ImageView

        init {
            this.img = row?.findViewById(R.id.imgDropDownMenuIcon) as ImageView
            this.label = row?.findViewById(R.id.txtDropDownLabel) as TextView
        }
    }

}