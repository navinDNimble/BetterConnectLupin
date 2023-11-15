package com.nimble.lupin.admin.views.navigation.user.createUser

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nimble.lupin.admin.R


class PostBottomSheetFragment : BottomSheetDialogFragment() {

    companion object{
        const val TAG = "PostBottomSheetDialog"
    }

    interface BottomSheetListenerPost {
        fun onPostSelected(value: String, id: String)
    }

    private lateinit var mListener: BottomSheetListenerPost


    private fun createNewTextView(displayText1: Context, displayText: String): TextView {
        // Create a new TextView instance
        val textView = TextView(requireContext())

        // Set attributes for the TextView
        textView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        textView.setPadding(
            resources.getDimensionPixelSize(R.dimen.activity_vertical_margin),
            resources.getDimensionPixelSize(R.dimen.activity_horizontal_margin),
            resources.getDimensionPixelSize(R.dimen.activity_vertical_margin),
            resources.getDimensionPixelSize(R.dimen.activity_horizontal_margin)
        )

        textView.text = displayText
        textView.hint = "text"
        textView.setTextSize(
            TypedValue.COMPLEX_UNIT_PX,
            resources.getDimension(R.dimen.activity_vertical_margin)
        )
        textView.setBackgroundResource(R.drawable.corner_text_shape_radius)
        textView.gravity = Gravity.CENTER

        // Set margins for the TextView
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        // Set margins in pixels (left, top, right, bottom)
        params.setMargins(10, 10, 10, 10)
        textView.layoutParams = params

        return textView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_post_bottom_sheet, container, false)

        rootView.findViewById<LinearLayout>(R.id.post_bottom_sheet_background)
            ?.let { linearLayout ->
                Log.d("navin", "" + linearLayout)
                listOf("Project Manager", "Pu Manager", "Project Coordinator", "Field Facilitator")
                    .map { createNewTextView(requireContext(), it) }
                    .forEach { linearLayout.addView(it) }
            }
        return rootView
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = try {
            context as BottomSheetListenerPost
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement BottomSheetListener")
        }
    }
}