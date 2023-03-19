package com.eyeque.eyequeconnect.ui


import android.content.pm.PackageManager
import android.media.MediaCodec
import android.media.MediaCodecInfo
import android.media.MediaDataSource
import android.media.MediaFormat
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Surface
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.eyeque.basecomponents.ui.FragmentHelper
import com.eyeque.enterprisedatamodule.APIResult
import com.eyeque.enterprisedatamodule.Test
import com.eyeque.enterprisedatamodule.model.request.Credential
import com.eyeque.eyequeconnect.R
import com.eyeque.eyequeconnect.adapter.RoleAdapter
import com.eyeque.eyequeconnect.databinding.FragmentLoginBinding
import com.eyeque.eyequeconnect.viewmodel.LoginViewModel
import com.eyeque.workermanager.LogInWork
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.hls.DefaultHlsDataSourceFactory
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment(), FragmentHelper {

    companion object{
        const val TAG = "LoginFragment"
    }
    private val viewModel: LoginViewModel by viewModels()
    private lateinit var binding: FragmentLoginBinding
    private lateinit var roleAdapter: RoleAdapter
    private var token: String? = null

    @Inject lateinit var logInWork: LogInWork

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        Log.i(TAG, Test().say())
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupLiveDataObservers()
        setupDataFlowObservers()
        setupRolesRV()
        setupClickListeners()
        viewModel.login(Credential("long.chen@eyeque.com","123456"))
        viewModel.test()
        logInWork.start()
        viewModel.workOnJobOne()
        viewModel.workOnJobTwo()
        viewModel.workOnJob1()
        viewModel.workOnJob2()
        viewModel.workOnTest2()
        startCamera()

        //requestPermissionResult.launch(arrayOf(Manifest.permission.))
        requestPermissionResult.launch(arrayOf(android.Manifest.permission.CAMERA))
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private val requestPermissionResult = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
        var allPermissionGranted = true
        it.forEach { permission, isGranted ->
            if(!isGranted){
                Toast.makeText(requireContext(), "Permissions must be granted", Toast.LENGTH_SHORT).show()
                allPermissionGranted = false
            }
        }
        if(allPermissionGranted){
            Toast.makeText(requireContext(), "Permissions granted!", Toast.LENGTH_SHORT).show()
            startCamera()
        }
    }

    override fun setupDataFlowObservers(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.loginFlow.collect{
                    when(it.status){
                        APIResult.ApiStatus.SUCCESS ->{
                            it.data?.let {data ->
                                roleAdapter.updateRoleList(data.roles)
                            }
                        }
                        else -> {}
                    }
                }
            }

            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.testFlow.collect{
                    Log.i(TAG, "testFlow emit $it")
                }
            }

            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.test2Flow.collect{
                    Log.i(TAG, "test2Flow emit $it")
                }
            }

            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.job1Flow.collect{
                    Log.i(TAG, "job1Flow emit ${it.toString()}")
                }
            }

            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.job2Flow.collect{
                    Log.i(TAG, "job2Flow emit ${it.toString()}")
                }
            }

        }

    }

    override fun setupLiveDataObservers() {
//        viewModel.logInResponse.observe(viewLifecycleOwner, Observer {
//            when(it.status){
//                APIResult.ApiStatus.ERROR -> {
//                    Log.i(TAG, "login error")
//                }
//
//                APIResult.ApiStatus.SUCCESS -> {
//                    Log.i(TAG, "login success")
//                }
//
//                APIResult.ApiStatus.LOADING -> {
//                    Log.i(TAG, "login loading")
//                }
//                else -> {}
//            }
//        })

        viewModel.logInResponse.observe(viewLifecycleOwner) {

        }

        viewModel.job1LiveData.observe(viewLifecycleOwner){
            Log.i(TAG, "job1LiveData is observed ${it.toString()}")
        }

        viewModel.job2LiveData.observe(viewLifecycleOwner){
            Log.i(TAG, "job2LiveData is observed ${it.toString()}")
        }
    }

    override fun setupClickListeners() {
        binding.nextBtn.setOnClickListener {
            Log.i(TAG, "nextBtn clicked")
            goToUserInfoPage(it)

            viewModel.test()
        }
    }

    private fun goToUserInfoPage(view: View){
//        val action = LoginFragmentDirections.actionLoginFragmentToPatientListFragment(token)
//        view.findNavController().navigate(action)
        findNavController().navigate(R.id.action_loginFragment_to_patientListFragment)
    }

    private fun setupRolesRV(){
        roleAdapter = RoleAdapter(mutableListOf())
        binding.rolesRV.adapter = roleAdapter
        binding.rolesRV.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    private lateinit var cameraProvider: ProcessCameraProvider
    private lateinit var camera: Camera

    fun startCamera(){
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build()
            val imageCapture = ImageCapture.Builder().build()
            cameraProvider.unbindAll()
            camera = cameraProvider.bindToLifecycle(viewLifecycleOwner, CameraSelector.DEFAULT_FRONT_CAMERA, preview, imageCapture)
            val previewSurface = binding.previewView.surfaceProvider
            preview.setSurfaceProvider(previewSurface)

        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private val videoEncoder = MediaCodec.createEncoderByType("video/avc")
    private lateinit var inputSurface: Surface

    private fun startVideoEncoder() {
        val videoFormat = MediaFormat.createVideoFormat("video/avc", 1280, 720)
        videoFormat.setInteger(MediaFormat.KEY_BIT_RATE, 1000000)
        videoFormat.setInteger(MediaFormat.KEY_FRAME_RATE, 30)
        videoFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface)
        videoFormat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 2)
        videoEncoder.configure(videoFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE)
        inputSurface = videoEncoder.createInputSurface()
        videoEncoder.start()
    }

    private fun stopVideoEncoder() {
        videoEncoder.stop()
        videoEncoder.release()
    }

    private lateinit var dataSourceFactory: DefaultHlsDataSourceFactory
    private lateinit var mediaSource: HlsMediaSource
    private lateinit var mediaDataSource: MediaDataSource

//    private fun startVideoStreaming() {
//        dataSourceFactory = DefaultHlsDataSourceFactory("exoplayer")
//        mediaDataSource = DefaultMediaSourceFactory(this).createDataSource()
//        mediaSource = HlsMediaSource.Factory(dataSourceFactory).createMediaSource(MediaItem.fromUri("http://example.com/stream.m3u8"))
//        exoPlayer.setMediaSource(mediaSource)
//        exoPlayer.prepare()
//        exoPlayer.play()
//    }

}