package com.montoto.inventapp.ui.screens

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.montoto.inventapp.R
import com.montoto.inventapp.databinding.FragmentItemDetailBinding
import com.montoto.inventapp.databinding.LayoutShowImageBinding
import com.montoto.inventapp.domain.model.FileImageModel
import com.montoto.inventapp.domain.model.ItemModel
import com.montoto.inventapp.ui.adapters.MultimediaAdapter
import com.montoto.inventapp.ui.base.BaseFragment
import com.montoto.inventapp.ui.viewmodel.MainViewModel
import com.montoto.inventapp.util.Constants.Inventory.Url.FULL_URL
import com.montoto.inventapp.util.toast


class ItemDetailFragment : BaseFragment<FragmentItemDetailBinding>(FragmentItemDetailBinding::inflate), MultimediaAdapter.OnLongClickListener {

    private var adapter: MultimediaAdapter? = null
    private val viewModel: MainViewModel by activityViewModels()
    private var article: ItemModel? = null

    override fun initialize() {
        viewModel.setToolbarTitle(getString(R.string.txt_title_detail_article))
        setUpView()
        setUpObservers()
        setUpListeners()
    }

    private fun setUpListeners() {
        binding.btnMarkDelivered.setOnClickListener {
            viewModel.markArticleAsDelivered(viewModel.itemSelected.value?.idArticle ?: "")
            binding.btnMarkDelivered.isEnabled = false
        }

        binding.btnSendBudget.setOnClickListener {
            sendWhatsapp()
        }
    }

    private fun setUpView() {
        binding.rvFiles.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            setHasFixedSize(true)
        }
    }

    private fun setUpObservers() {
        viewModel.itemSelected.observe(viewLifecycleOwner) {
            it?.let {
                article = it

                with(binding) {
                    txtName.text = it.clientName
                    txtPhone.text = getString(R.string.label_cellphone,  it.clientCellphone)
                    txtItem.text = getString(R.string.label_article, it.article)
                    txtDate.text = getString(R.string.label_date, it.startDate)
                    txtProblemDescription.text = getString(R.string.label_problem_description, it.problemDescription)
                }

            }
            viewModel.fetchFilesImages(it.idArticle)
        }


        viewModel.filesImages.observe(viewLifecycleOwner) {
            it?.let {
                loadRvImages(it)
                binding.imgDefault.visibility = View.INVISIBLE
            }
        }

        viewModel.itemsDelivered.observe(viewLifecycleOwner){
            it?.let {
                binding.btnMarkDelivered.visibility = View.INVISIBLE
            }
        }

        viewModel.showToast.observe(this){
            it?.let {
                activity?.toast(it)
                viewModel.clearToastMessage()
            }
        }

        viewModel.articleUpdateSuccess.observe(this){
            it?.let {
                findNavController().navigateUp()
                viewModel.clearUpdateSuccess()
            }
        }

    }

    private fun loadRvImages(list: List<FileImageModel>) {
        adapter = MultimediaAdapter(list, this,this)
        binding.rvFiles.adapter = adapter
    }

    private fun sendWhatsapp() {
        val cellphone = article?.clientCellphone
        val price = binding.edtBudget.text
        try {
            val text = getString(R.string.message_whatsapp, price)
            val number = getString(R.string.cellphone_whatsapp, cellphone)
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(getString(R.string.uri_whatsapp,number,text))
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(requireContext(), getString(R.string.error_not_app_whatsapp), Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

    private fun showImageFull(file: FileImageModel?) {
        val customDialog = Dialog(requireContext(), R.style.RoundedCornersDialog)
        val binding: LayoutShowImageBinding = LayoutShowImageBinding.inflate(layoutInflater)

        customDialog.setContentView(binding.root)
        Glide.with(requireContext())
            .load(FULL_URL + file?.path)
            .into(binding.img)

        customDialog.show()
    }

    override fun onItemClick(file: FileImageModel?, position: Int) {
        showImageFull(file)
    }

}