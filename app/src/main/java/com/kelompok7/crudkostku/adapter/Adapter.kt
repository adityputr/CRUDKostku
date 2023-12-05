package com.kelompok7.crudkostku.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.contentValuesOf
import androidx.recyclerview.widget.RecyclerView
import com.kelompok7.crudkostku.databinding.KostItemBinding
import com.kelompok7.crudkostku.models.Kost

class Adapter(private val kostList : java.util.ArrayList<Kost>) : RecyclerView.Adapter<Adapter.ViewHolder>() {
    class ViewHolder(val binding: KostItemBinding) : RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(KostItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return kostList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = kostList[position]
        holder.apply {
            binding.apply {
                tvNama.text = currentItem.nama
                tvAlamat.text = currentItem.alamat
                tvId.text = currentItem.id
            }
        }
    }
}