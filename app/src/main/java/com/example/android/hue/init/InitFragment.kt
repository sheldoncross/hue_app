package com.example.android.hue.init

/*
* Initial fragment loaded on the back stack. This fragment will check if the needed values to
* display the home fragment are null and will assign values to them if so.
*/

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.hue.database.light.LightDatabase
import com.example.android.hue.BuildConfig
import com.example.android.hue.R
import com.example.android.hue.SharedViewModel
import com.example.android.hue.database.user.UserDatabase
import com.example.android.hue.databinding.FragmentInitBinding
import timber.log.Timber

class InitFragment : Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        /*
        *Inflate the layout for the fragment from the layout package
        *This layout will display the prompt to press the hue bridge to obtain the initial value
        * If values are already present then the home page will be immediately loaded
        */
        val binding: FragmentInitBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_init,
            container,
            false)

        /*Shared view model containing shared values for and from the database that'll be shared
        *between fragments and activities
         */
        val sharedViewModel : SharedViewModel by activityViewModels()

        val application = requireNotNull(this.activity).application

        //Instance of the user database
        val userDataSource = UserDatabase.getInstance(application).userDatabaseDao

        //Instance of light database
        val lightDataSource = LightDatabase.getInstance(application).lightDatabaseDao

        //View model which adds the user & light databases to the init view model
        val viewModelFactory = InitViewModelFactory(userDataSource, lightDataSource, application)

        //Instantiate the view model for the fragment
        val initViewModel = ViewModelProvider(this, viewModelFactory)
            .get(InitViewModel::class.java)

        //Attach the view model to viewModel binding present in the fragment layout
        binding.viewModel = initViewModel

        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.lifecycleOwner = this

        /*Observe the init models navigate to home variable to know when to navigate away from the
        * init fragment to the home fragment and to update shared view model values
        */
        initViewModel.navigateToHome.observe(viewLifecycleOwner, Observer {
            it?.let{
                //Navigate away from the init fragment towards the home fragment
                this.findNavController()
                    .navigate(InitFragmentDirections.actionInitFragmentToHomeFragment())
                //Update the shared view model values
                sharedViewModel.user.value = initViewModel.user.value
                sharedViewModel.lightList.value = initViewModel.lightList.value
            }
        })

        //Change the activities title to "Home"
        activity?.title = "Home"

        //Create an options menu
        setHasOptionsMenu(true)

        //Return the root view obtained from the binding
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