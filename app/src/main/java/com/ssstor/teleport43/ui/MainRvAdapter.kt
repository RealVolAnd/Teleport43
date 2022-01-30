package com.ssstor.teleport43.ui

import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ssstor.teleport43.App
import com.ssstor.teleport43.ITEM_TYPE_POINT
import com.ssstor.teleport43.ITEM_TYPE_TRACK
import com.ssstor.teleport43.R
import com.ssstor.teleport43.databinding.LocationItemBinding

class MainRvAdapter(private val presenter: MainPresenter,
                    private val itemClickListener: OnItemClick) :
    RecyclerView.Adapter<MainRvAdapter.ViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MainRvAdapter.ViewHolder = ViewHolder(
      LocationItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: MainRvAdapter.ViewHolder, position: Int) {

        holder.itemNameText .text = presenter.itemList[position].locationItemName
       val tracksNum = App.instance.itemStringToList(presenter.itemList[position].locationItemTrack).size
        if(tracksNum>1){
            holder.itemTypeText.text = ITEM_TYPE_TRACK
        } else {
            holder.itemTypeText.text = ITEM_TYPE_POINT
        }
        holder.itemSettingsButton.setOnClickListener {
            itemClickListener.onItemToolButtonClick(presenter.itemList[position].locationItemId)
        }
        holder.rootLayout.setOnClickListener {
            itemClickListener.onItemClick(presenter.itemList[position].locationItemId)
        }
    }

    override fun getItemCount() = presenter.getCount()

    inner class ViewHolder(private val vb: LocationItemBinding) : RecyclerView.ViewHolder(vb.root) {
        val itemNameText = vb.itemNameText
        val itemTypeText = vb.itemTypeText
        val itemSettingsButton = vb.itemSettingsButton
        val rootLayout = vb.rootLayout

    }
}