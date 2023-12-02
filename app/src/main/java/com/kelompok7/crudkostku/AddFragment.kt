package com.kelompok7.crudkostku

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.kelompok7.crudkostku.databinding.FragmentAddBinding
import com.kelompok7.crudkostku.databinding.FragmentHomeBinding
import com.kelompok7.crudkostku.models.Kost

class AddFragment : Fragment() {
    private var _binding : FragmentAddBinding? = null
    private val binding get() = _binding!!
    private lateinit var firebaseRef : DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        firebaseRef = FirebaseDatabase.getInstance().getReference("Kost")

        binding.btnSend.setOnClickListener {
            saveData()
        }

        return binding.root
    }

    private fun saveData() {
        val nama = binding.editNamaKos.text.toString()
        val alamat = binding.editAlamatKos.text.toString()

        if (nama.isEmpty()) binding.editNamaKos.error = "Masukan nama kos!"
        if (alamat.isEmpty()) binding.editAlamatKos.error = "Masukan alamat kos!"

        val kostId = firebaseRef.push().key!!
        val kost = Kost(kostId, nama, alamat)

        firebaseRef.child(kostId).setValue(kost)
            .addOnCompleteListener{
                Toast.makeText(context, "Data berhasil ditambahkan!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{
                Toast.makeText(context, "Data gagal ditambahkan!${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
}