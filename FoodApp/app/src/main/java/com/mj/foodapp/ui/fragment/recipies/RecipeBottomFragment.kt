package com.mj.foodapp.ui.fragment.recipies

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.mj.foodapp.R
import com.mj.foodapp.util.Constants.Companion.DEFAULT_DIET_TYPE
import com.mj.foodapp.util.Constants.Companion.DEFAULT_MEAL_TYPE
import com.mj.foodapp.viewmodel.RecipiesViewModel
import kotlinx.android.synthetic.main.fragment_recipe_bottom.view.*
import java.lang.Exception
import java.util.*


class RecipeBottomFragment : BottomSheetDialogFragment() {

    private lateinit var recipesViewModel: RecipiesViewModel

    private var mealTypeChip = DEFAULT_MEAL_TYPE
    private var mealTypeChipId = 0
    private var dietTypeChip = DEFAULT_DIET_TYPE
    private var dietTypeChipId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recipesViewModel = ViewModelProvider(requireActivity()).get(RecipiesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var mView = inflater.inflate(R.layout.fragment_recipe_bottom, container, false)
        recipesViewModel.readMealAndDietType.asLiveData().observe(viewLifecycleOwner, {
            mealTypeChip = it.selectedMealType
            dietTypeChip = it.selectedDietType
            updateChip(it.selectedMealTypeId,mView.mealType_chipGroup)
            updateChip(it.selectedDietTypeId,mView.dietType_chipGroup)
        })

        mView.dietType_chipGroup.setOnCheckedChangeListener { group, checkedId ->
            val chip = group.findViewById<Chip>(checkedId)
            val selectedMealType = chip.text.toString().toLowerCase(Locale.ROOT)
            mealTypeChip = selectedMealType
            mealTypeChipId = checkedId
        }
        mView.mealType_chipGroup.setOnCheckedChangeListener { group, checkedId ->
            val chip = group.findViewById<Chip>(checkedId)
            val selectedMealType = chip.text.toString().toLowerCase(Locale.ROOT)
            dietTypeChip = selectedMealType
            dietTypeChipId = checkedId
        }

        mView.apply_btn.setOnClickListener {
            recipesViewModel.saveMealandDietType(
                mealTypeChip,
                mealTypeChipId,
                dietTypeChip,
                dietTypeChipId
            )
        }

        return mView
    }

    private fun updateChip(chipId: Int, chipGroup: ChipGroup) {
        if (chipId != 0) {
            try {
                chipGroup.findViewById<Chip>(chipId).isChecked = true
            } catch (e: Exception) {
                Log.d("RecipesBottomSheet", e.message.toString())
            }
        }
    }


}