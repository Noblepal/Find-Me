package com.intelligence.findme.fragments

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.intelligence.findme.R

class BottomSheetFragment : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentView = inflater.inflate(R.layout.bottom_sheet, container, true)
        try {
            val bottomSheetDialog: Dialog = dialog!!
            val bottomSheet: View = bottomSheetDialog.findViewById(R.id.bottomSheet)
            BottomSheetBehavior.from(bottomSheet).peekHeight = 250
        } catch (e: Exception) {
            Log.e("BottomSheetFragment", "Error inflating bottomSheet: ${e.localizedMessage}")
        }

        return fragmentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {

    }
}
