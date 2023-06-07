package id.adiandrea.scanmath.feature.main

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import id.adiandrea.scanmath.R
import id.adiandrea.scanmath.base.BaseActivity
import id.adiandrea.scanmath.databinding.ActivityMainBinding
import id.adiandrea.scanmath.feature.scan.InputCameraActivity
import id.adiandrea.scanmath.util.Constant.Companion.VALUE_STORAGE_DATABASE
import id.adiandrea.scanmath.util.Constant.Companion.VALUE_STORAGE_FILE
import timber.log.Timber

class MainActivity: BaseActivity<MainViewModel>() {
    override val viewModelClass: Class<MainViewModel> = MainViewModel::class.java
    private lateinit var binding: ActivityMainBinding

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // PERMISSION GRANTED
            startActivity(
                Intent(this, InputCameraActivity::class.java)
            )
        } else {
            Toast.makeText(
                this,
                "Cant proceed further! this feature need access camera permission!",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.actionCameraInput.setOnClickListener {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }

        binding.rgStorage.setOnCheckedChangeListener { _, id ->
            when(id){
                R.id.rb_database -> { viewModel.setSelectedStorage(VALUE_STORAGE_DATABASE) }
                R.id.rb_encrypted -> { viewModel.setSelectedStorage(VALUE_STORAGE_FILE) }
            }
            viewModel.loadHistory()
        }

        viewModel.onHistoryLoaded.observe(this) {
            Timber.i("loaded ${it.size} data")
            if(it.isEmpty()){
                binding.message.visibility = View.VISIBLE
                binding.rv.visibility = View.GONE
            }else{
                binding.message.visibility = View.GONE
                binding.rv.visibility = View.VISIBLE
            }
            binding.rv.layoutManager = LinearLayoutManager(this)
            binding.rv.adapter = HistoryAdapter(it)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadHistory()
        if(viewModel.getCurrentStorage() == VALUE_STORAGE_DATABASE){
            binding.rbDatabase.isChecked = true
        }else{
            binding.rbEncrypted.isChecked = true
        }
    }

}