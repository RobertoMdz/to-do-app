package com.coopera.todoappproject.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.coopera.todoappproject.R
import com.coopera.todoappproject.data.models.ToDoData
import com.coopera.todoappproject.data.viewmodel.ToDoViewModel
import com.coopera.todoappproject.databinding.FragmentListBinding
import com.coopera.todoappproject.fragments.SharedViewModel
import com.coopera.todoappproject.fragments.list.adapter.ListAdapter
import com.coopera.todoappproject.utils.hideKeyboard
import com.google.android.material.snackbar.Snackbar
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator

class ListFragment : Fragment(), SearchView.OnQueryTextListener {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private val adapter: ListAdapter by lazy { ListAdapter() }
    private val mToDoViewModel: ToDoViewModel by viewModels()
    private val mSharedViewModel: SharedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.mSharedViewModel = mSharedViewModel

        // Setup RecyclerView
        setupRecyclerView()

        mToDoViewModel.getAllData.observe(viewLifecycleOwner, {
            data ->
                mSharedViewModel.checkIfDatabaseIsEmpty(data)
                adapter.setData(data)
        })

        // Set menu
        setHasOptionsMenu(true)
        // Hide soft keyboard
        hideKeyboard(requireActivity())
        return binding.root
    }

    private fun setupRecyclerView() {
        val recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.itemAnimator = SlideInUpAnimator().apply {
            addDuration = 300
        }

        // Swipe to Delete
        swipeToDelete(recyclerView)
    }

    private fun swipeToDelete(recyclerView: RecyclerView) {
        val swipeToDeleteCallback = object : SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedItem = adapter.dataList[viewHolder.adapterPosition]
                // Delete Item
                mToDoViewModel.deleteItem(deletedItem)
                adapter.notifyItemRemoved(viewHolder.adapterPosition)
                // Show Restore Deleted Item Snackbar
                restoredDeletedItem(viewHolder.itemView, deletedItem)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun restoredDeletedItem(view: View, deletedItem: ToDoData) {
        val snackBar = Snackbar.make(view, "Deleted ${deletedItem.title}", Snackbar.LENGTH_LONG)
        snackBar.setAction("Undo") {
            mToDoViewModel.insertData(deletedItem)
        }
        snackBar.show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
       inflater.inflate(R.menu.list_fragment_menu, menu)

        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_delete_all -> confirmItemRemoval()
            R.id.menu_priority_high -> mToDoViewModel.sortByHighPriority.observe(this, { adapter.setData(it)})
            R.id.menu_priority_low -> mToDoViewModel.sortByLowPriority.observe(this, { adapter.setData(it)})
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
       if(query != null) {
           searchThroughDatabase(query)
       }
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        if(query != null) {
            searchThroughDatabase(query)
        }
        return true
    }

    private fun searchThroughDatabase(query: String) {
        val searchQuery: String = "%$query%"
        mToDoViewModel.searchDatabase(searchQuery).observe(this, {
            list ->
            list?.let {
                adapter.setData(it)
            }
        })
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