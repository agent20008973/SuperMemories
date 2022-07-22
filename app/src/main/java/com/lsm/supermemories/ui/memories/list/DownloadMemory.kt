package com.lsm.supermemories.ui.memories.list


import android.Manifest
import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker.checkSelfPermission
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import coil.load
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.lsm.supermemories.MainActivity
import com.lsm.supermemories.MainApplication
import com.lsm.supermemories.R
import com.lsm.supermemories.databinding.DownloadMemoryFragmentBinding
import com.lsm.supermemories.ui.base.BaseFragment
import com.lsm.supermemories.utils.ImageUtils
import com.lsm.supermemories.viewmodels.DownloadMemoryInfo
import com.lsm.supermemories.viewmodels.DownloadViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@AndroidEntryPoint
class DownloadMemory: BaseFragment() {
    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    var imageUri = ""
    private var interstitialAd: InterstitialAd? = null

    private var mBinding: DownloadMemoryFragmentBinding? = null
    private val memoryViewModel: DownloadViewModel by viewModels()
    val sharedViewModel: DownloadMemoryInfo by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = DataBindingUtil.inflate<DownloadMemoryFragmentBinding>(
                inflater, R.layout.download_memory_fragment, container, false
        )

        sharedViewModel._title.value = ""
        sharedViewModel._text.value = ""
        interstitialAd = newInterstitialAd()

        loadInterstitial()


        this.mBinding = fragmentBinding


        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding?.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = memoryViewModel
            viewModelInfo = sharedViewModel
            downloadMemory = this@DownloadMemory
            imageUtils = ImageUtils
        }


        observeModelNavigation(memoryViewModel)
    }

    fun EditText.StringValue() = text.toString()

    fun get_time(): String {

        val currentDateTime =
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    LocalDateTime.now()
                } else {
                    TODO("VERSION.SDK_INT < O")
                }
        var current_date = currentDateTime.format(DateTimeFormatter.ISO_DATE)
        return current_date.toString()

    }

    fun addMemory() {
        sharedViewModel._title.value = mBinding!!.editTextTitle.StringValue()
        sharedViewModel._text.value = mBinding!!.editTextContent.StringValue()
        if (sharedViewModel._title.value != ""){
            lifecycleScope.launch {
                memoryViewModel?.add_new_memory(
                        sharedViewModel._text.value!!,
                        sharedViewModel._title.value!!,
                        get_time(),
                        imageUri,

                )
            }
                Toast.makeText(activity, "Pomyślnie dodano wspomnienie", Toast.LENGTH_LONG).show()

        } else {
            Toast.makeText(MainApplication.applicationContext(),"Aby dodać wspomnienie musisz dodać jego tytuł",Toast.LENGTH_LONG).show()
        }
        val context = MainApplication.applicationContext()
        var sharedPreferences = context.getSharedPreferences("Image", Context.MODE_PRIVATE)

        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.remove("Image").clear().apply()

        editor.putString("Image", imageUri).apply()
        setAlarm(1)


    }

    @SuppressLint("WrongConstant")
    fun addImage() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //sprawdza czy kod nie wykonuje się na urządzeniach starszych niż Android 8.0.
            if (checkSelfPermission(
                            MainApplication.applicationContext(),
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_DENIED
            ) {
                val permissions = arrayOf((Manifest.permission.READ_EXTERNAL_STORAGE));
                requestPermissions(permissions, PERMISSION_CODE);
            } else {
                pickImageFromGallery();
            }
        } else {
            pickImageFromGallery();
        }

    }

    companion object {
        private val IMAGE_PICK_CODE = 1000;
        private val PERMISSION_CODE = 1001;
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImageFromGallery()
                } else {
                    Toast.makeText(activity, "Perission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"

        startActivityForResult(intent, IMAGE_PICK_CODE)


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            imageUri = data!!.data!!.toString()
            mBinding!!.imageView.setImageURI(data?.data)
            val uri = data.data!!
            val inputStream = requireContext().contentResolver.openInputStream(uri)
            val cursor = requireContext().contentResolver.query(uri, null, null, null, null)
            cursor?.use { c ->
                val nameIndex = c.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (c.moveToFirst()) {
                    val name = c.getString(nameIndex)
                    inputStream?.let { inputStream ->
                        val file = File(requireContext().cacheDir, name)
                        val os = file.outputStream()
                        os.use {
                            inputStream.copyTo(it)
                        }
                        imageUri = file.absolutePath

                        val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                        mBinding!!.imageView.load(bitmap)
                    }
                }
            }



        }
    }




    private fun newInterstitialAd(): InterstitialAd {
        return InterstitialAd(MainApplication.applicationContext()).apply {
            adUnitId = getString(R.string.interstitial_ad_unit_id2)
            adListener = object : AdListener() {
                override fun onAdLoaded() {
                    interstitialAd?.show()
                    super.onAdLoaded()

                }


            }
        }


    }

    private fun loadInterstitial() {

        val adRequest = AdRequest.Builder()
                .setRequestAgent("android_studio:ad_template")
                .build()
        interstitialAd?.loadAd(adRequest)


    }

    private fun setAlarm(number: Int) {
        val simpleDateFormat = SimpleDateFormat("HH:mm:ss")
        val am = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val now = Calendar.getInstance()
        val text_timer = StringBuilder()
        val calendarList = ArrayList<Calendar>()
        for (i in 1..number)
            calendarList.add(now)
        for (calendar in calendarList) {
            calendar.add(Calendar.SECOND, 30)
            val requestCode = Random().nextInt()
            val intent = Intent(MainApplication.applicationContext(), MyAlarmReceiver::class.java)
            intent.putExtra("REQUEST_CODE", requestCode)
            intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES)
            intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND)
            val pi = PendingIntent.getBroadcast(MainApplication.applicationContext(), requestCode, intent, 0)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pi)
            else
                am.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pi)

            text_timer.append(simpleDateFormat.format(calendar.timeInMillis)).append("\n")
        }

    }

    fun get_coordinates() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(MainApplication.applicationContext())
        if (ActivityCompat.checkSelfPermission(
                        MainApplication.applicationContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1);
        }
        fusedLocationClient.lastLocation.addOnSuccessListener(MainActivity()) { location ->
            if (location != null) {
                lastLocation = location

                val context = MainApplication.applicationContext()
                var sharedPreferences = context.getSharedPreferences("latitude", Context.MODE_PRIVATE)
                var sharedPreferences2 = context.getSharedPreferences("longitude", Context.MODE_PRIVATE)
                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                val editor2: SharedPreferences.Editor = sharedPreferences2.edit()
                editor.putFloat("latitude", location.latitude.toFloat()).apply()
                editor2.putFloat("longitude", location.longitude.toFloat()).apply()
                Toast.makeText(MainApplication.applicationContext(),"Pomyślnie dodano współrzędne",Toast.LENGTH_LONG).show()

            }
        }
    }
}

