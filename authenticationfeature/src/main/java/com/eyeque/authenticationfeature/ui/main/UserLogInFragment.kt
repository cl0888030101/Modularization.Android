package com.eyeque.authenticationfeature.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.eyeque.authenticationfeature.R

class UserLogInFragment : Fragment() {

    companion object {
        fun newInstance() = UserLogInFragment()
    }

    private lateinit var viewModel: UserLogInViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_user_log_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.log_in_btn).setOnClickListener {
            findNavController().navigate(R.id.action_userLogInFragment_to_userListFragment)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(UserLogInViewModel::class.java)

    }

}