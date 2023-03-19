package com.eyeque.eyequeconnect.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.eyeque.basecomponents.ui.FragmentHelper
import com.eyeque.eyequeconnect.databinding.FragmentPatientListBinding
import com.eyeque.eyequeconnect.viewmodel.PatientListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PatientListFragment : Fragment(), FragmentHelper {

    companion object{
       const val TAG = "PatientListFragment"
    }

    private val viewModel: PatientListViewModel by viewModels()
    private lateinit var binding : FragmentPatientListBinding
    private val arg : PatientListFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPatientListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupLiveDataObservers()
        setupDataFlowObservers()
        setupClickListeners()
        viewModel.startCountDownTimer()
        viewModel.getPatientList()
    }

    override fun setupLiveDataObservers() {

    }

    override fun setupDataFlowObservers() {
        lifecycleScope.launchWhenCreated {
            viewModel.timerFlow.collect{
                binding.timerTv.text = "${it}%"
            }
        }

        lifecycleScope.launch{
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.patientListFlow.collect{
                    binding.aTv.text = it.data.toString()
                }
            }

            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.patientEncounterListFlow.collect{
                    binding.bTv.text = it.toString()
                }
            }
        }


    }

    override fun setupClickListeners() {}


}