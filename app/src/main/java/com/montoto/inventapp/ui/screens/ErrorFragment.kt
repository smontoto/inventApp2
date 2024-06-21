package com.montoto.inventapp.ui.screens

import androidx.fragment.app.activityViewModels
import com.montoto.inventapp.databinding.FragmentErrorBinding
import com.montoto.inventapp.ui.base.BaseFragment
import com.montoto.inventapp.ui.viewmodel.MainViewModel
import androidx.navigation.fragment.findNavController

class ErrorFragment : BaseFragment<FragmentErrorBinding>(FragmentErrorBinding::inflate) {

    private val viewModel: MainViewModel by activityViewModels()
    override fun initialize() {
        viewModel.setToolbarTitle("Error")

        setUpObservers()

        setUpListeners()
    }

    private fun setUpListeners() {
        binding.btnPrimary.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setUpObservers() {
        viewModel.errorMessage.observe(this){
            it?.let {
                binding.title.text = it.title
                binding.subTitle.text = it.subtitle
            }
        }
    }

}