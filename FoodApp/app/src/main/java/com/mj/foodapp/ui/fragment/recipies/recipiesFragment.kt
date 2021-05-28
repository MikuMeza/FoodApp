package com.mj.foodapp.ui.fragment.recipies

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mj.foodapp.R
import com.mj.foodapp.RecipesAdapter
import com.mj.foodapp.util.NetworkResult
import com.mj.foodapp.viewmodel.MainViewModel
import com.mj.foodapp.viewmodel.RecipiesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_recipies.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@AndroidEntryPoint
class recipiesFragment : Fragment() {

    private val mainViewModel: MainViewModel by viewModels()
    private val recipeViewModel: RecipiesViewModel by viewModels()

    private val mAdapter by lazy { RecipesAdapter() }
    private lateinit var mView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_recipies, container, false)

        setupRecyclerView()
        getRecipiesApi()

        return mView
    }

    fun getRecipiesApi() {
        mainViewModel.getRecipiesSafeCall(recipeViewModel.applyQueries())
            .observe(viewLifecycleOwner, { response ->
                when (response) {
                    is NetworkResult.Success -> {
                        hideShimmerEffect()
                        response.data?.let { mAdapter.setData(it) }
                    }
                    is NetworkResult.Error -> {
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

    private fun setupRecyclerView() {
        mView.recyclerview.adapter = mAdapter
        mView.recyclerview.layoutManager = LinearLayoutManager(requireContext())
        showShimmerEffect()
    }

    private fun showShimmerEffect() {
        mView.recyclerview.showShimmer()
    }

    private fun hideShimmerEffect() {
        mView.recyclerview.hideShimmer()
    }

}