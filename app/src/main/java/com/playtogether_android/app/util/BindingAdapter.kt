package com.playtogether_android.app.util

import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.playtogether_android.app.R
import com.playtogether_android.app.presentation.ui.thunder.list.adapter.ThunderCategoryListItemAdapter
import com.playtogether_android.domain.model.light.CategoryData
import timber.log.Timber

object BindingAdapter {
    @JvmStatic
    @BindingAdapter("subwayLine")
    fun subwayLine(imageView: ImageView, name: String) {
        when (name) {
            "01호선" -> imageView.setImageResource(R.drawable.sub_line_1)
            "02호선" -> imageView.setImageResource(R.drawable.sub_line_2)
            "03호선" -> imageView.setImageResource(R.drawable.sub_line_3)
            "04호선" -> imageView.setImageResource(R.drawable.sub_line_4)
            "05호선" -> imageView.setImageResource(R.drawable.sub_line_5)
            "06호선" -> imageView.setImageResource(R.drawable.sub_line_6)
            "07호선" -> imageView.setImageResource(R.drawable.sub_line_7)
            "08호선" -> imageView.setImageResource(R.drawable.sub_line_8)
            "09호선" -> imageView.setImageResource(R.drawable.sub_line_9)
            "우이신설경전철" -> imageView.setImageResource(R.drawable.sub_wooe)
            "북한산우이" -> imageView.setImageResource(R.drawable.sub_wooe)
            "김포공항" -> imageView.setImageResource(R.drawable.sub_gimpo_gold)
            "김포도시철도" -> imageView.setImageResource(R.drawable.sub_gimpo_gold)
            "수인분당선" -> imageView.setImageResource(R.drawable.sub_suin)
            "서해선" -> imageView.setImageResource(R.drawable.sub_west_sea)
            "의정부경전철" -> imageView.setImageResource(R.drawable.sub_uijeongbu)
            "경의선" -> imageView.setImageResource(R.drawable.sub_gyeongui_center)
            "신림선" -> imageView.setImageResource(R.drawable.sub_sillim)
            "신분당선" -> imageView.setImageResource(R.drawable.sub_shinbundang)
            "공항철도" -> imageView.setImageResource(R.drawable.sub_airport)
            "인천2호선" -> imageView.setImageResource(R.drawable.sub_incheon_2)
            "인천선" -> imageView.setImageResource(R.drawable.sub_incheon_1)
            "경강선" -> imageView.setImageResource(R.drawable.sub_gyeonggang)
            "경춘선" -> imageView.setImageResource(R.drawable.sub_gyeongchun)
            else -> imageView.setImageResource(R.drawable.sub_incheon_2)
        }
    }

    @BindingAdapter("app:imageSelecter")
    @JvmStatic
    fun bindImageSelecter(editText: EditText, imageView: ImageView) {
        if (editText.text.isNullOrEmpty()) {
            imageView.setImageResource(R.drawable.ic_icn_message)
        } else {
            imageView.setImageResource(R.drawable.ic_icn_message_black)
        }
    }

    @BindingAdapter("bindData")
    @JvmStatic
    fun bindRecyclerView(
        recyclerView: RecyclerView,
        list: List<CategoryData>
    ) {
        //최초 연결
        if (recyclerView.adapter == null) {
            val adapter = ThunderCategoryListItemAdapter()
            recyclerView.adapter = adapter
        }

        val mAdapter = recyclerView.adapter as ThunderCategoryListItemAdapter

        mAdapter.submitList(list.toMutableList())
    }
}

