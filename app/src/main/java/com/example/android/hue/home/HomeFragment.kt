package com.example.android.hue.home

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.hue.BuildConfig
import com.example.android.hue.R
import com.example.android.hue.SharedViewModel
import com.example.android.hue.databinding.FragmentHomeBinding
import timber.log.Timber

class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentHomeBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_home,
            container,
            false)

        val sharedViewModel : SharedViewModel by activityViewModels()

        val application = requireNotNull(this.activity).application

        val viewModelFactory = HomeViewModelFactory(application)

        val homeViewModel
                = ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)

        val adapter = HomeAdapter()

        binding.viewModel = homeViewModel

        binding.lifecycleOwner = this

        binding.lightList.adapter = adapter

        binding.lightList.layoutManager = LinearLayoutManager(this.context)

        sharedViewModel.user.observe(viewLifecycleOwner, Observer {
            homeViewModel.user.value = it
        })

        sharedViewModel.lightList.observe(viewLifecycleOwner, Observer {
            homeViewModel.lightList.value = it
            adapter.data = homeViewModel.lightList.value!!
        })

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        //Inflate menu from specified xml resource
        inflater.inflate(R.menu.overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Initialize timber instance
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}