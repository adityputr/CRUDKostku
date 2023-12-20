package com.kelompok7.crudkostku.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.FirebaseDatabase
import com.kelompok7.crudkostku.HomeFragmentDirections
import com.kelompok7.crudkostku.databinding.KostItemBinding
import com.kelompok7.crudkostku.models.Kost
import com.squareup.picasso.Picasso

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
//                tvAlamat.text = currentItem.alamat
//                tvId.text = currentItem.id
                tvHarga.text = currentItem.harga
//                tvWatsap.text = currentItem.watsap
//                tvGmaps.text = currentItem.gmaps
                tvKategori.text = currentItem.kategori
                Picasso.get().load(currentItem.imgUrl).into(tvImage)
                rvContainer.setOnClickListener{

                    val action = HomeFragmentDirections.actionHomeFragmentToUpdateFragment(
                        currentItem.id.toString(),
                        currentItem.nama.toString(),
                        currentItem.alamat.toString(),
                        currentItem.imgUrl.toString(),
                        currentItem.harga.toString(),
                        currentItem.watsap.toString(),
                        currentItem.gmaps.toString(),
                        currentItem.kategori.toString()
                    )
                    findNavController(holder.itemView).navigate(action)
                }

                rvContainer.setOnLongClickListener {
                    MaterialAlertDialogBuilder(holder.itemView.context)
                        .setTitle("Hapus item secara permanen")
                        .setMessage("Apakah kamu yakin ingin menghapus item ini?")
                        .setPositiveButton("Yes"){_,_ ->
                            val firebaseRef = FirebaseDatabase.getInstance().getReference("Kost")
                            val storageRef = FirebaseDatabase.getInstance().getReference("Images")
                            //storage
                            storageRef.child(currentItem.id.toString()).removeValue()

                            //realtime database
                            firebaseRef.child(currentItem.id.toString()).removeValue()
                                .addOnSuccessListener {
                                    Toast.makeText(holder.itemView.context, "Item berhasil dihapus!", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener{error ->
                                    Toast.makeText(holder.itemView.context, "error ${error.message}", Toast.LENGTH_SHORT).show()
                                }
                        }
                        .setNegativeButton("No"){_,_ ->
                            Toast.makeText(holder.itemView.context, "Dibatalkan!", Toast.LENGTH_SHORT).show()
                        }
                        .show()
                    return@setOnLongClickListener true
                }
            }
        }
    }
}