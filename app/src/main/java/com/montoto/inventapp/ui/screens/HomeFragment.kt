package com.montoto.inventapp.ui.screens

import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.montoto.inventapp.R
import com.montoto.inventapp.databinding.FragmentHomeBinding
import com.montoto.inventapp.ui.base.BaseFragment
import com.montoto.inventapp.ui.viewmodel.MainViewModel
import com.montoto.inventapp.util.Constants.Inventory.States.ITEMS_DELIVERED
import com.montoto.inventapp.util.Constants.Inventory.States.ITEMS_ENTERED
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val viewModel: MainViewModel by activityViewModels()

    override fun initialize() {
        viewModel.setToolbarTitle(getString(R.string.app_name_title))
        viewModel.resetVars()
        setUpView()
    }

    private fun setUpView() {
        binding.btnListEntered.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToListArticlesFragment(title = getString(R.string.txt_title_list_articles_entered), typeOfItem = ITEMS_ENTERED))
        }

        binding.btnListDelivered.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToListArticlesFragment(title = getString(R.string.txt_title_list_articles_delivered), typeOfItem = ITEMS_DELIVERED))
            viewModel.setItemAsDelivered(true)
        }

        binding.btnAddItem.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addArticleFragment)
        }

    }

}