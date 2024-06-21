package com.montoto.inventapp.ui.screens

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.montoto.inventapp.R
import com.montoto.inventapp.databinding.FragmentListArticlesBinding
import com.montoto.inventapp.domain.model.ItemModel
import com.montoto.inventapp.ui.adapters.ArticlesAdapter
import com.montoto.inventapp.ui.base.BaseFragment
import com.montoto.inventapp.ui.viewmodel.MainViewModel

class ListArticlesFragment : BaseFragment<FragmentListArticlesBinding>(FragmentListArticlesBinding::inflate),
    ArticlesAdapter.OnItemClickListener, MenuProvider {

    private val viewModel: MainViewModel by activityViewModels()
    private var listItems: List<ItemModel>? = null
    private lateinit var articlesAdapter: ArticlesAdapter

    private val args: ListArticlesFragmentArgs by navArgs()

    override fun initialize() {
        viewModel.fetchItems(args.typeOfItem)
        setUpView()
        setUpObservers()
        setUpToolbar()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.resetVars()
    }

    private fun setUpView() {
        articlesAdapter = ArticlesAdapter(this)

        binding.rvItems.apply {
            adapter = articlesAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            setHasFixedSize(true)
        }
    }

    private fun setUpObservers() {
        viewModel.articles.observe(this) {
            it?.let {
                listItems = it
                articlesAdapter.setListData(it)
            }
        }

        viewModel.filteredList.observe(this){
            it?.let{
                articlesAdapter.setListData(it)
            }
        }

        viewModel.errorState.observe(this){
            it?.let {
                findNavController().navigate(R.id.errorFragment)
                viewModel.clearError()
            }
        }
    }

    //region toolbar
    private fun setUpToolbar() {
        viewModel.setToolbarTitle(args.title)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_search, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        if (menuItem.itemId == R.id.searcher){
            val searchView: SearchView = menuItem.actionView as SearchView

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    newText?.let { listItems?.let { list -> viewModel.filterArticles(it, list) } }
                    return true
                }
            })

            searchView.queryHint = getString(R.string.txt_search)
            return false
        }
        return false
    }
    //endregion

    override fun onItemClick(article: ItemModel?, position: Int) {
        viewModel.setItemSelected(article!!)
        findNavController().navigate(ListArticlesFragmentDirections.actionListArticlesFragmentToItemDetailFragment())
    }


}