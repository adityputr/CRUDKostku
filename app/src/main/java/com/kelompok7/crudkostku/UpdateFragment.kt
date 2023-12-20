package com.kelompok7.crudkostku

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.kelompok7.crudkostku.databinding.FragmentUpdateBinding
import com.kelompok7.crudkostku.models.Kost
import com.squareup.picasso.Picasso

class UpdateFragment : Fragment() {

    private var _binding : FragmentUpdateBinding? = null
    private val binding get() = _binding!!

    private val args : UpdateFragmentArgs by navArgs()

    private lateinit var firebaseRef : DatabaseReference
    private lateinit var storageRef : StorageReference

    private var uri: Uri? = null
    private var imageUrl : String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpdateBinding.inflate(inflater, container,false)

        firebaseRef = FirebaseDatabase.getInstance().getReference("Kost")
        storageRef = FirebaseStorage.getInstance().getReference("Images")

        val picImage = registerForActivityResult(ActivityResultContracts.GetContent()){
            binding.imgUpdate.setImageURI(it)
            if (it != null){
                uri = it
            }
        }
        imageUrl = args.imageUrl

        binding.apply {
            editUpdateNama.setText(args.nama)
            editUpdateAlamat.setText(args.alamat)
            editUpdateHarga.setText(args.harga)
            editUpdateWA.setText(args.watsap)
            editUpdateLokasi.setText(args.gmaps)
            editUpdateKategori.setText(args.kategori)
            Picasso.get().load(imageUrl).into(imgUpdate)

            btnUpdate.setOnClickListener{
                updateData()
                findNavController().navigate(R.id.action_updateFragment_to_homeFragment)
            }
            imgUpdate.setOnClickListener{
                context?.let { context ->
                    MaterialAlertDialogBuilder(context)
                        .setTitle("Ganti foto")
                        .setMessage("Apakah anda yakin ingin mengganti?")
                        .setPositiveButton("Ganti gambar"){_,_->
                            picImage.launch("image/*")

                        }
                        .setNegativeButton("Hapus gambar"){_,_->
                            imageUrl = null
                            binding.imgUpdate.setImageResource(R.drawable.ic_sponbob)
                        }
                        .setNeutralButton("Batal"){_,_->}
                            .show()
                        }
                }
        }

        return binding.root
    }

    private fun updateData() {
        val nama = binding.editUpdateNama.text.toString()
        val alamat = binding.editUpdateAlamat.text.toString()
        val harga = binding.editUpdateHarga.text.toString()
        val watsap = binding.editUpdateWA.text.toString()
        val gmaps = binding.editUpdateLokasi.text.toString()
        val kategori = binding.editUpdateKategori.text.toString()
        var kost: Kost

        if (uri != null) {
            storageRef.child(args.id).putFile(uri!!)
                .addOnSuccessListener { task ->
                    task.metadata!!.reference!!.downloadUrl
                        .addOnSuccessListener { url ->
                            imageUrl = url.toString()
                            kost = Kost(args.id, nama, alamat, imageUrl, harga, watsap, gmaps, kategori)
                            firebaseRef.child(args.id).setValue(kost)
                                .addOnCompleteListener {
                                    Toast.makeText(
                                        context,
                                        "Data berhasil diupdate!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(
                                        context,
                                        "Data gagal diupdate!${it.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        }
                }

        }
        if (uri == null){
            kost = Kost(args.id, nama, alamat, imageUrl, harga, watsap, gmaps, kategori)
            firebaseRef.child(args.id).setValue(kost)
                .addOnCompleteListener {
                    Toast.makeText(
                        context,
                        "Data berhasil diupdate!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .addOnFailureListener {
                    Toast.makeText(
                        context,
                        "Data gagal diupdate!${it.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
        if (imageUrl == null) storageRef.child(args.id).delete()
    }
}