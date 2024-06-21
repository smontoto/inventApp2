package com.montoto.inventapp.ui.screens

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.montoto.inventapp.R
import com.montoto.inventapp.databinding.ActivityMainKtBinding
import com.montoto.inventapp.ui.viewmodel.MainViewModel
import com.montoto.inventapp.util.replaceFragment
import com.montoto.inventapp.util.visibilityBy
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainKtBinding
    private val viewModel: MainViewModel by viewModels()

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainKtBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentHost) as NavHostFragment
        navController = navHostFragment.navController

        setUpView()
        setUpObservers()
        setUpToolbarActivity()
    }

    private fun setUpView() {
        binding.toolbarBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setUpToolbarActivity() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.let { it.title = ""}
    }

    private fun setUpObservers() {
        viewModel.toolbarTitle.observe(this) {
            if (it.isNullOrBlank()) binding.toolbar.visibility = View.GONE
            else {
                binding.toolbar.visibility = View.VISIBLE
                binding.toolbarText.text = it
            }
        }

        viewModel.navigation.observe(this) {
            if (it != null) replaceFragment(it)
        }

        viewModel.isLoading.observe(this) {
            binding.progressBar.visibilityBy(it)
        }
    }


}