package com.kelompok7.crudkostku

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.kelompok7.crudkostku.databinding.FragmentUpdateBinding
import com.kelompok7.crudkostku.models.Kost

class UpdateFragment : Fragment() {

    private var _binding : FragmentUpdateBinding? = null
    private val binding get() = _binding!!

    private val args : UpdateFragmentArgs by navArgs()

    private lateinit var firebaseRef : DatabaseReference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpdateBinding.inflate(inflater, container,false)

        firebaseRef = FirebaseDatabase.getInstance().getReference("Kost")

        binding.apply {
            editUpdateNama.setText(args.nama)
            editUpdateAlamat.setText(args.alamat)
            btnUpdate.setOnClickListener{
                updateData()
                findNavController().navigate(R.id.action_updateFragment_to_homeFragment)
            }
        }

        return binding.root
    }

    private fun updateData() {
        val nama = binding.editUpdateNama.text.toString()
        val alamat = binding.editUpdateAlamat.text.toString()
        val kost = Kost(args.id, nama, alamat)

        firebaseRef.child(args.id).setValue(kost)
            .addOnCompleteListener{
                Toast.makeText(context, "Data berhasil diupdate!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{
                Toast.makeText(context, "Data gagal diupdate!${it.message}", Toast.LENGTH_SHORT).show()
        }
    }
}