package com.mj.foodapp.ui.fragment.recipies

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mj.foodapp.R
import com.mj.foodapp.adapter.RecipesAdapter
import com.mj.foodapp.databinding.FragmentRecipiesBinding
import com.mj.foodapp.util.NetworkResult
import com.mj.foodapp.util.observeOnce
import com.mj.foodapp.viewmodel.MainViewModel
import com.mj.foodapp.viewmodel.RecipiesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_recipies.view.*
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@AndroidEntryPoint
class RecipiesFragment : Fragment() {
    private lateinit var binding: FragmentRecipiesBinding
    private val mainViewModel: MainViewModel by viewModels()
    private val recipeViewModel: RecipiesViewModel by viewModels()

    private val mAdapter by lazy { RecipesAdapter() }
//    private lateinit var mView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        mView = inflater.inflate(R.layout.fragment_recipies, container, false)
        binding = FragmentRecipiesBinding.inflate(inflater, container, false)

        setupRecyclerView()
        readDatabase()

        binding.recipesFab.setOnClickListener {
            //navigation to bottomsheet dialog
            findNavController().navigate(R.id.action_recipesFragment_to_recipeBottomFragment)
        }

        return binding.root
    }

    fun readDatabase() {
        lifecycleScope.launch {
            mainViewModel.readRecipies.observe(viewLifecycleOwner, { database ->
                if (database.isNotEmpty()) {
                    //if database not empty get data from ROOM database
                    mAdapter.setData(database[0].foodRecipe)
                    hideShimmerEffect()
                } else {
                    //database empty so call api
                    getRecipiesApi()
                }
            })
        }

    }

    fun getRecipiesApi() {
        mainViewModel.getRecipes(recipeViewModel.applyQueries())
        mainViewModel.recipesResponse
            .observeOnce(viewLifecycleOwner, { response ->
                when (response) {
                    is NetworkResult.Success -> {
                        hideShimmerEffect()
                        response.data?.let { mAdapter.setData(it) }
                    }
                    is NetworkResult.Error -> {
                        //error response returned so loading data from database
                        loadDataFromCache()
                        hideShimmerEffect()
                        Toast.makeText(
                            requireContext(),
                            response.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    is NetworkResult.Loading -> {
                        showShimmerEffect()
                    }
                }
            })
    }

    fun loadDataFromCache() {
        lifecycleScope.launch {
            mainViewModel.readRecipies.observe(viewLifecycleOwner, {database->
                if (database.isNotEmpty()) {
                    mAdapter.setData(database[0].foodRecipe)
                }
            })
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerview.adapter = mAdapter
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())
        showShimmerEffect()
    }

    private fun showShimmerEffect() {
        binding.recyclerview.showShimmer()
    }

    private fun hideShimmerEffect() {
        binding.recyclerview.hideShimmer()
    }

}