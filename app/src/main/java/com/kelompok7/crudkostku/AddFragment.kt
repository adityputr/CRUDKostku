package com.kelompok7.crudkostku

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.kelompok7.crudkostku.databinding.FragmentAddBinding
import com.kelompok7.crudkostku.models.Kost

class AddFragment : Fragment() {
    private var _binding : FragmentAddBinding? = null
    private val binding get() = _binding!!
    private lateinit var firebaseRef : DatabaseReference
    private lateinit var storageRef : StorageReference

    private var uri: Uri? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        firebaseRef = FirebaseDatabase.getInstance().getReference("Kost")
        storageRef = FirebaseStorage.getInstance().getReference("Images")
        binding.btnSend.setOnClickListener {
            saveData()
        }
        val picImage = registerForActivityResult(ActivityResultContracts.GetContent()){
            binding.imageAdd.setImageURI(it)
            if (it != null){
                uri = it
            }
        }
        binding.btnPickImage.setOnClickListener {
            picImage.launch("image/*")
        }
        return binding.root
    }

    private fun saveData() {
        val nama = binding.editNamaKos.text.toString()
        val alamat = binding.editAlamatKos.text.toString()


        if (nama.isEmpty()) binding.editNamaKos.error = "Masukan nama kos!"
        if (alamat.isEmpty()) binding.editAlamatKos.error = "Masukan alamat kos!"


        val kostId = firebaseRef.push().key!!
        var kost : Kost

        uri?.let {
            storageRef.child(kostId).putFile(it)
                .addOnSuccessListener { task->
                    task.metadata!!.reference!!.downloadUrl
                        .addOnSuccessListener { url ->
                            val imgUrl = url.toString()

                            kost = Kost( kostId, nama, alamat, imgUrl)

                            firebaseRef.child(kostId).setValue(kost)
                                .addOnCompleteListener{
                                    Toast.makeText(context, "Data berhasil ditambahkan!", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener{
                                    Toast.makeText(context, "Data gagal ditambahkan!${it.message}", Toast.LENGTH_SHORT).show()
                                }
                        }
                }
        }

    }
}