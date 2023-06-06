package id.adiandrea.scanmath.feature.main

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import id.adiandrea.scanmath.base.BaseActivity
import id.adiandrea.scanmath.data.local.history.History
import id.adiandrea.scanmath.databinding.ActivityMainBinding
import id.adiandrea.scanmath.feature.scan.InputCameraActivity
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

        viewModel.onHistoryLoaded.observe(this) {
            Timber.i("loaded ${it.size} data")
            binding.rv.layoutManager = LinearLayoutManager(this)
            binding.rv.adapter = HistoryAdapter(it)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadHistoryFromLocalDatabase()
    }

}