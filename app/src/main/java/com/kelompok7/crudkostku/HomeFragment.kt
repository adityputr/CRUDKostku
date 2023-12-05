package com.kelompok7.crudkostku

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kelompok7.crudkostku.adapter.Adapter
import com.kelompok7.crudkostku.databinding.FragmentHomeBinding
import com.kelompok7.crudkostku.models.Kost

class HomeFragment : Fragment() {

    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var kostList: ArrayList<Kost>
    private lateinit var firebaseRef : DatabaseReference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       _binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.btnAdd.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment2_to_addFragment2)
        }

        firebaseRef = FirebaseDatabase.getInstance().getReference("Kost")
        kostList = arrayListOf()

        fetchData()

        binding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this.context)
        }
        return binding.root
    }

    private fun fetchData() {
        firebaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                kostList.clear()
                if (snapshot.exists()){
                    for (kostSnap in snapshot.children){
                        val kost = kostSnap.getValue(Kost::class.java)
                        kostList.add(kost!!)
                    }
                }
                val rvAdapter = Adapter(kostList)
                binding.recyclerView.adapter = rvAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context,"Terjadi error! $error", Toast.LENGTH_LONG).show()
            }

        })
    }
}
