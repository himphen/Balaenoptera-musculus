package com.himphen.myapplication.ui.currency.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.himphen.myapplication.databinding.ItemCurrencyListCryptoBinding
import com.himphen.myapplication.databinding.ItemCurrencyListFiatBinding

class CurrencyListAdapter :
    ListAdapter<CurrencyListAdapterItem, RecyclerView.ViewHolder>(DiffCallback()) {

    companion object {
        const val VIEW_TYPE_CRYPTO = 1
        const val VIEW_TYPE_FIAT = 2
    }

    class DiffCallback : DiffUtil.ItemCallback<CurrencyListAdapterItem>() {
        override fun areItemsTheSame(
            oldItem: CurrencyListAdapterItem,
            newItem: CurrencyListAdapterItem
        ): Boolean {
            if (oldItem is CurrencyListAdapterItem.Crypto && newItem is CurrencyListAdapterItem.Crypto)
                return oldItem.id == newItem.id

            if (oldItem is CurrencyListAdapterItem.Fiat && newItem is CurrencyListAdapterItem.Fiat)
                return oldItem.id == newItem.id

            return false
        }

        override fun areContentsTheSame(
            oldItem: CurrencyListAdapterItem,
            newItem: CurrencyListAdapterItem
        ): Boolean {
            if (oldItem is CurrencyListAdapterItem.Crypto && newItem is CurrencyListAdapterItem.Crypto)
                return oldItem == newItem

            if (oldItem is CurrencyListAdapterItem.Fiat && newItem is CurrencyListAdapterItem.Fiat)
                return oldItem == newItem

            return false
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        when (viewType) {
            VIEW_TYPE_CRYPTO -> {
                return ItemCryptoViewHolder(
                    ItemCurrencyListCryptoBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            VIEW_TYPE_FIAT -> {
                return ItemFiatViewHolder(
                    ItemCurrencyListFiatBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            else -> {
                throw IllegalArgumentException("This viewType $viewType is not support")
            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is CurrencyListAdapterItem.Crypto -> VIEW_TYPE_CRYPTO
            is CurrencyListAdapterItem.Fiat -> VIEW_TYPE_FIAT
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ItemCryptoViewHolder -> {
                val itemBinding = holder.viewBinding
                val item = getItem(position) as? CurrencyListAdapterItem.Crypto ?: return
                itemBinding.tvName.text = item.name
                itemBinding.tvShortName.text = item.name.first().toString()
            }

            is ItemFiatViewHolder -> {
                val itemBinding = holder.viewBinding
                val item = getItem(position) as? CurrencyListAdapterItem.Fiat ?: return
                itemBinding.tvName.text = item.name
                itemBinding.tvShortName.text = item.name.first().toString()
            }
        }
    }

    internal class ItemCryptoViewHolder(val viewBinding: ItemCurrencyListCryptoBinding) :
        RecyclerView.ViewHolder(viewBinding.root)

    internal class ItemFiatViewHolder(val viewBinding: ItemCurrencyListFiatBinding) :
        RecyclerView.ViewHolder(viewBinding.root)


}