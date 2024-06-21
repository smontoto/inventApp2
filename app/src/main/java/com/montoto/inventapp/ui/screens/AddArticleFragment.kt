package com.montoto.inventapp.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.montoto.inventapp.R
import com.montoto.inventapp.databinding.FragmentAddArticleBinding
import com.montoto.inventapp.domain.model.FileImageModel
import com.montoto.inventapp.domain.model.ItemModel
import com.montoto.inventapp.ui.adapters.MultimediaAdapter
import com.montoto.inventapp.ui.base.BaseFragment
import com.montoto.inventapp.ui.viewmodel.MainViewModel
import com.montoto.inventapp.util.Util
import com.montoto.inventapp.util.toast

class AddArticleFragment :
    BaseFragment<FragmentAddArticleBinding>(FragmentAddArticleBinding::inflate),
    MultimediaAdapter.OnLongClickListener {

    private val viewModel: MainViewModel by activityViewModels()

    private var filesAdapter: MultimediaAdapter? = null
    private var layoutManagerFiles: RecyclerView.LayoutManager? = null
    private val filesList = ArrayList<FileImageModel>()

    override fun initialize() {
        viewModel.setToolbarTitle(getString(R.string.title_add_article))
        viewModel.fetchIdForNewArticle()
        initRvFiles()
        setUpObservers()
        setUpListeners()
    }

    private fun setUpListeners() {
        binding.btnSave.setOnClickListener {
            with(binding) {
                viewModel.sendArticle(ItemModel(
                    idArticle = inputIdItem.text.toString(),
                    article = inputArticle.text.toString(),
                    clientName = inputClientName.text.toString(),
                    clientCellphone = inputPhone.text.toString(),
                    problemDescription = inputProblemDescription.text.toString())
                )

                viewModel.sendFilesToServer(filesList, binding.inputIdItem.text.toString())
            }

        }

        binding.btnAddImages.setOnClickListener {
            pickFile()
        }
    }

    private fun setUpObservers() {
        viewModel.idNewArticle.observe(this) {
            binding.inputIdItem.setText(it)
        }

        viewModel.showToast.observe(this) {
            it?.let {
                activity?.toast(it)
            }
        }

        viewModel.articleUpdateSuccess.observe(this) {
            it?.let {
                viewModel.fetchIdForNewArticle()
                resetFields()
            }
        }
    }

    private fun pickFile() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            cameraResult.launch(intent)
        } else {
            if (hasPermissions(activity as Context, PERMISSIONS)) {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                cameraResult.launch(intent)
            } else {
                permissionsResult.launch(PERMISSIONS)
            }
        }
    }

    private fun initRvFiles() {
        layoutManagerFiles =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)

        filesAdapter = MultimediaAdapter(filesList, this, this)

        binding.rvFiles.layoutManager = layoutManagerFiles
        binding.rvFiles.adapter = filesAdapter
    }

    private fun showImageInProfileAndAddToArray(path: String, size: Float) {
        binding.imgDefault.visibility = View.INVISIBLE

        val position = filesList.size
        filesList.add(position, FileImageModel("", path))
        filesAdapter?.notifyItemInserted(position)
        layoutManagerFiles?.scrollToPosition(position)
    }

    private val cameraResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                intent?.extras.let {
                    val thumbnail: Bitmap = intent?.extras!!.get("data") as Bitmap
                    val path = Util.saveImageInternalStorageAndGetPath(thumbnail, requireContext())
                    showImageInProfileAndAddToArray(path, 0f)
                }
            }
        }

    private val permissionsResult =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val granted = permissions.entries.all {
                it.value
            }
            if (granted) {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                cameraResult.launch(intent)
            } else {
                Toast.makeText(context, "Sin permisos necesarios", Toast.LENGTH_SHORT).show()
            }
        }

    //region permissions
    private fun hasPermissions(context: Context, permissions: Array<String>): Boolean =
        permissions.all {
            ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
    //endregion

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    private fun resetFields() {
        filesList.clear()
        filesAdapter?.notifyDataSetChanged()

        with(binding) {
            inputIdItem.setText("")
            inputArticle.setText("")
            inputClientName.setText("")
            inputPhone.setText("")
            inputProblemDescription.setText("")
        }

    }

    override fun onItemClick(file: FileImageModel?, position: Int) {
        filesList.removeAt(position)
        filesAdapter!!.notifyItemRemoved(position)
        if (filesList.size == 0) {
            binding.imgDefault.visibility = View.VISIBLE
        }
    }

    companion object {
        var PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }
}