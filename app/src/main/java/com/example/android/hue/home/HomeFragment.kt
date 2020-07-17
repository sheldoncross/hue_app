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

/*
* Fragment which displays the obtained light list within a recycler view and implements the basic
* quick functions for each light and links to each lights more advanced settings. The fragments also
* instantiates the applications primary navigation
*/

class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        /*
        *Inflate the home layout for the fragment from the layout package
        *This layout will display the list of lights and allow the user to interact with them
        */
        val binding: FragmentHomeBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_home,
            container,
            false)

        /*Shared view model containing shared values for and from the database that'll be shared
        *between fragments and activities
        */
        val sharedViewModel : SharedViewModel by activityViewModels()

        val application = requireNotNull(this.activity).application

        //View model factory which adds the application to the home view model?
        val viewModelFactory = HomeViewModelFactory(application)

        //View model for the home fragment
        val homeViewModel
                = ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)

        //Recycler view adapter
        val adapter = HomeAdapter(object : LightSwitchListener{
            override val username: String
                get() = homeViewModel.user.value!!.username

            override var idNumber = 0

            override fun onButtonClick() {
                homeViewModel.testOnState(idNumber)
            }
        })

        //Bind the view model to the fragments layout
        binding.viewModel = homeViewModel

        //Set the lifecycle owner of the layout
        binding.lifecycleOwner = this

        //Set the layouts recyclerview adapter to equal the fragments adapter
        binding.lightList.adapter = adapter

        //Set the layout manager for the recycler-view
        binding.lightList.layoutManager = LinearLayoutManager(this.context)

        //Set the shared view model to observe the home view models user values
        sharedViewModel.user.observe(viewLifecycleOwner, Observer {
            //Set the home view models user value to equal the shared view model's user value
            homeViewModel.user.value = it
        })

        //Set the shared view model to observe the home view model's light list values
        sharedViewModel.lightList.observe(viewLifecycleOwner, Observer {
            //Set the home view models light list values to equal the shared view model's values
            homeViewModel.lightList.value = it
            //Set the adapter data list to equal the home view model's light list values
            adapter.data = homeViewModel.lightList.value!!
        })

        //Create an options menu
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