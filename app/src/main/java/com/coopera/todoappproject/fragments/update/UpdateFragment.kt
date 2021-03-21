package com.coopera.todoappproject.fragments.update

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.coopera.todoappproject.R
import com.coopera.todoappproject.data.models.Priority
import com.coopera.todoappproject.data.models.ToDoData
import com.coopera.todoappproject.data.viewmodel.ToDoViewModel
import com.coopera.todoappproject.databinding.FragmentListBinding
import com.coopera.todoappproject.databinding.FragmentUpdateBinding
import com.coopera.todoappproject.fragments.SharedViewModel

class UpdateFragment : Fragment() {

    private val args by navArgs<UpdateFragmentArgs>()
    private val mSharedViewModel: SharedViewModel by viewModels()
    private val mToDoViewModel: ToDoViewModel by viewModels()

    private var _binding: FragmentUpdateBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // Data Binding
        _binding = FragmentUpdateBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.args = args

        // Set menu
        setHasOptionsMenu(true)

        // Spinner Item Selected listener
        binding.currentSpinnerPriorities.onItemSelectedListener = mSharedViewModel.listener

        return binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_save ->  updateItem()
            R.id.menu_delete ->  confirmItemRemoval()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun updateItem() {
        val mTitle = binding.edtxtCurrentTodoTitle.text.toString()
        val mPriority = binding.currentSpinnerPriorities.selectedItem.toString()
        val mDescription = binding.edtxtCurrentTodoDescription.text.toString()

        val validation = mSharedViewModel.verifyDataFromUser(mTitle, mDescription)
        if(validation) {
            val updatedItem = ToDoData(
                    args.currentItem.id,
                    mTitle,
                    mSharedViewModel.parsePriority(mPriority),
                    mDescription
            )
            mToDoViewModel.updateDate(updatedItem)
            Toast.makeText(requireContext(), "Successfully updated.", Toast.LENGTH_SHORT).show()
            // Navigate Back
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        } else {
            Toast.makeText(requireContext(), "Please fill out all fields.", Toast.LENGTH_SHORT).show()
        }
    }

    // Show AlertDialog to confirm item removal
    private fun confirmItemRemoval() {
        val currentItem = args.currentItem
        val builder =  AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") {
            _, _ -> mToDoViewModel.deleteItem(currentItem)
            Toast.makeText(
                    requireContext(),
                    "Successfully removed: ${currentItem.title}",
                    Toast.LENGTH_SHORT
            ).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }
        builder.setNegativeButton("No") {_, _ -> }
        builder.setTitle("Delete ${currentItem.title}?")
        builder.setMessage("Are you sure you want to remove ${currentItem.title}?")
        builder.create().show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_fragment_menu, menu)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}