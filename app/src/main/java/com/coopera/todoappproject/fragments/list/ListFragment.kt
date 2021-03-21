package com.coopera.todoappproject.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.coopera.todoappproject.R
import com.coopera.todoappproject.data.viewmodel.ToDoViewModel
import com.coopera.todoappproject.databinding.FragmentListBinding
import com.coopera.todoappproject.fragments.SharedViewModel
import com.coopera.todoappproject.fragments.list.adapter.ListAdapter

class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private val adapter: ListAdapter by lazy { ListAdapter() }
    private val mToDoViewModel: ToDoViewModel by viewModels()
    private val mSharedViewModel: SharedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.mSharedViewModel = mSharedViewModel

        // Setup RecyclerView
        setupRecyclerView()

        mToDoViewModel.getAllData.observe(viewLifecycleOwner, Observer {
            data ->
                mSharedViewModel.checkIfDatabaseIsEmpty(data)
                adapter.setData(data)
        })

        // Set menu
        setHasOptionsMenu(true)
        return binding.root
    }

    private fun setupRecyclerView() {
        val recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
       inflater.inflate(R.menu.list_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId ==  R.id.menu_delete_all) {
            confirmItemRemoval()
        }
        return super.onOptionsItemSelected(item)
    }

    // Show AlertDialog to confirm all item removal
    private fun confirmItemRemoval() {
        val builder =  AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") {
            _, _ -> mToDoViewModel.deleteAll()
            Toast.makeText(
                    requireContext(),
                    "Successfully removed everything",
                    Toast.LENGTH_SHORT
            ).show()
        }
        builder.setNegativeButton("No") {_, _ -> }
        builder.setTitle("Delete everything?")
        builder.setMessage("Are you sure you want to remove everything?")
        builder.create().show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}