
package com.nimble.lupin.pu_manager.views.home.report

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.bumptech.glide.Glide
import com.nimble.lupin.pu_manager.R
import com.nimble.lupin.pu_manager.databinding.FragmentTaskReportsBinding
import com.nimble.lupin.pu_manager.models.reportsModel.PdfDataModel
import com.nimble.lupin.pu_manager.models.reportsModel.SubActivityReportData
import com.nimble.lupin.pu_manager.utils.PdfGenerator
import java.util.Random
import java.util.UUID


class TaskReportsFragment : Fragment() {
    private lateinit var binding: FragmentTaskReportsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_task_reports, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        view.findViewById<AppCompatButton>(R.id.buttonId).setOnClickListener {
            val pdfList: MutableList<PdfDataModel> = mutableListOf()
            val subactivityList: MutableList<SubActivityReportData> = mutableListOf()
            val headerList = mutableListOf(
                "subActivityId",
                "subActivityName",
                "male_count",
                "female_count"
            )
            val subActivityReportData1 = SubActivityReportData(
                1,
                "SUBACTIVITY 1", 5, 2
            )
            val subActivityReportData2 = SubActivityReportData(
                2,
                "SUBACTIVITY 2", 10, 5
            )
            val subActivityReportData3 = SubActivityReportData(
                3,
                "SUBACTIVITY 3", 15, 20
            )
            val subActivityReportData4 = SubActivityReportData(
                4,
                "SUBACTIVITY 4", 5, 2
            )
            val subActivityReportData5 = SubActivityReportData(
                5,
                "SUBACTIVITY 5", 15, 20
            )

            subactivityList.add(subActivityReportData1)
            subactivityList.add(subActivityReportData2)
            subactivityList.add(subActivityReportData3)
            subactivityList.add(subActivityReportData4)
            subactivityList.add(subActivityReportData5)


            val pdfModelList1 = PdfDataModel(
                1,
                "activity Name1",
                "This is activity description1",
                10,
                5,
                subactivityList,
                headerList,
                mutableListOf("s", "s"),
                listOf("https://firebasestorage.googleapis.com/v0/b/demoapplication-87fd5.appspot.com/o/UploadTask%2Fc04e1c39-fedb-4e90-b993-6a995c852513?alt=media&token=7321cd1b-c6c9-481f-af08-6d2400a248f7")
            )
            val pdfModelList2 = PdfDataModel(
                2,
                "activity Name2",
                "This is activity description2",
                15,
                20,
                subactivityList,
                headerList,
                mutableListOf("s", "s"),
                listOf("https://firebasestorage.googleapis.com/v0/b/demoapplication-87fd5.appspot.com/o/Profile%2F1a9158ab-c821-48a1-961a-95501bdb65cc?alt=media&token=b77fc313-1a5a-4e58-b62a-85c8c9b1109a")
            )
            pdfList.add(pdfModelList1)
            pdfList.add(pdfModelList2)
            val uuid: UUID = UUID.randomUUID()
            val options = BitmapFactory.Options()
            options.inScaled = false
             val bb :ImageView = view.findViewById(R.id.imageView22)
            Glide.with(requireContext()).asBitmap().load("https://firebasestorage.googleapis.com/v0/b/demoapplication-87fd5.appspot.com/o/Profile%2F1a9158ab-c821-48a1-961a-95501bdb65cc?alt=media&token=b77fc313-1a5a-4e58-b62a-85c8c9b1109a").into(bb)

        }
    }
}