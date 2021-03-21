package com.coopera.todoappproject.fragments.update

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.coopera.todoappproject.R
import com.coopera.todoappproject.data.models.Priority
import com.coopera.todoappproject.databinding.FragmentListBinding
import com.coopera.todoappproject.databinding.FragmentUpdateBinding
import com.coopera.todoappproject.fragments.SharedViewModel

class UpdateFragment : Fragment() {

    private val args by navArgs<UpdateFragmentArgs>()
    private val mSharedViewModel: SharedViewModel by viewModels()

    private var _binding: FragmentUpdateBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentUpdateBinding.inflate(inflater, container, false)

        // Set menu
        setHasOptionsMenu(true)

        val toDoArg = args.currentItem

        binding.edtxtCurrentTodoTitle.setText(toDoArg.title)
        binding.currentSpinnerPriorities.setSelection(parsePriority(toDoArg.priority))
        binding.currentSpinnerPriorities.onItemSelectedListener = mSharedViewModel.listener
        binding.edtxtCurrentTodoDescription.setText(toDoArg.description)

        return binding.root
    }

    private fun parsePriority(priority: Priority): Int {
        return when(priority) {
            Priority.HIGH -> 0
            Priority.MEDIUM -> 1
            Priority.LOW -> 2
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_fragment_menu, menu)
    }

}