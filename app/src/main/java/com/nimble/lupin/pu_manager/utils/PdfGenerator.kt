package com.nimble.lupin.pu_manager.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.itextpdf.text.BaseColor
import com.itextpdf.text.Document
import com.itextpdf.text.Element
import com.itextpdf.text.Image
import com.itextpdf.text.PageSize
import com.itextpdf.text.Paragraph
import com.itextpdf.text.Phrase
import com.itextpdf.text.Rectangle
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import com.nimble.lupin.pu_manager.R
import com.nimble.lupin.pu_manager.models.reportsModel.PdfDataModel
import com.nimble.lupin.pu_manager.models.reportsModel.SubActivityReportData
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.CountDownLatch


class PdfGenerator(
    pdfReportData: List<PdfDataModel>,
    fileName: String,
    val context: Context
) {
    private var doc = Document(PageSize.A4, 5f, 5f, 0f, 5f)
    var file: File
    private var writer: PdfWriter
    private val TABLE_TOP_PADDING = 10f
    private val TABLE_HORIZONTAL_PADDING = 5f

    init {
        val outPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            .toString() + "/$fileName" + ".pdf"
        file = File(outPath)
        if (file.exists()) {
            file.delete()
        }

        writer = PdfWriter.getInstance(doc, FileOutputStream(file))
        doc.open()
        val size = pdfReportData.size

        for ((index, it) in pdfReportData.withIndex()) {
            callActivityData(it.activityName, it.activityDescription)


            val imageCell = addImage(/*imageBitmap*/)
            doc.add(imageCell)

            addParagraph("Analysis")
            val analysisTable = PdfPTable(4)
            val widthArray = floatArrayOf(0.5f, 0.5f, 0.5f, 0.5f)
            analysisTable.isLockedWidth = true
            analysisTable.totalWidth = PageSize.A4.width
            analysisTable.setWidths(widthArray)
            val totalUnit = createCell("Total Unit", Rectangle.ALIGN_CENTER)
            val totalUnitValues = createCell("15", Rectangle.ALIGN_LEFT)
            val totalTask = createCell("Total TASK", Rectangle.ALIGN_RIGHT)
            val totalTaskUnit = createCell("20", Rectangle.ALIGN_LEFT)
            analysisTable.addCell(totalUnit)
            analysisTable.addCell(totalUnitValues)
            analysisTable.addCell(totalTask)
            analysisTable.addCell(totalTaskUnit)
            doc.add(analysisTable)
            addParagraph(null)
            setTableData(it.subActivityReportDataList, it.headerList)
            doc.newPage()

/*            val imageTable = PdfPTable(1)
            val widthArray = floatArrayOf(1f)
            imageTable.isLockedWidth = true
            imageTable.totalWidth = PageSize.A4.width
            imageTable.setWidths(widthArray)

            setImages(it.imageReportList) { imagesBitmaps ->
                val imageSize = imagesBitmaps.size
                for ((index, imageBitmap) in imagesBitmaps.withIndex()) {



                }


            }*/
            if (index == size - 1) {
                doc.close()
                val uri = FileProvider.getUriForFile(
                    context,
                    context.packageName + ".provider",
                    file
                )
                Toast.makeText(context, "Complete", Toast.LENGTH_SHORT).show()
                Log.d("sachin", "Completed " + uri.toString())
            }
        }

        /*for ((index, it) in pdfReportData.withIndex()) {
            callActivityData(it.activityName, it.activityDescription)
            val imageTable = PdfPTable(2)
            val widthArray = floatArrayOf(0.5f,0.5f)
            imageTable.isLockedWidth = true
            imageTable.totalWidth = PageSize.A4.width
            imageTable.setWidths(widthArray)
            setImages(it.imageReportList){imagesBitmaps->

                imagesBitmaps.forEach{
                    if (it!=null){
                       val imageCell =  addImage(it)
                        imageTable.addCell(imageCell)
                        doc.add(imageTable)
                    }
                }
                addParagraph("Analysis")
                val analysisTable = PdfPTable(4)
                val widthArray = floatArrayOf(0.5f,0.5f,0.5f,0.5f)
                analysisTable.isLockedWidth = true
                analysisTable.totalWidth = PageSize.A4.width
                analysisTable.setWidths(widthArray)
                val totalUnit = createCell("Total Unit",Rectangle.ALIGN_CENTER)
                val totalUnitValues = createCell("15",Rectangle.ALIGN_LEFT)
                val totalTask = createCell("Total TASK",Rectangle.ALIGN_RIGHT)
                val totalTaskUnit = createCell("20",Rectangle.ALIGN_LEFT)
                analysisTable.addCell(totalUnit)
                analysisTable.addCell(totalUnitValues)
                analysisTable.addCell(totalTask)
                analysisTable.addCell(totalTaskUnit)
                doc.add(analysisTable)
                addParagraph(null)
                setTableData(it.subActivityReportDataList ,it.headerList)
                if (index == size -1){
                    doc.close()
                    val uri =  FileProvider.getUriForFile(
                        context,
                        context.packageName+ ".provider",
                        file
                    )
                    Toast.makeText(context,"Complete",Toast.LENGTH_SHORT).show()
                    Log.d("sachin", "Completed "+uri.toString())
                }
                doc.newPage()
            }


        }*/

    }

    private fun addImage(/*imageBitmap: Bitmap*/): PdfPTable {
        val options = BitmapFactory.Options()
        options.inScaled = false
        val source: Bitmap = BitmapFactory.decodeResource(context.resources,R.mipmap.ic_launcher , options)
        val stream = ByteArrayOutputStream()
        source.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val image = Image.getInstance(stream.toByteArray())
        image.scaleToFit(140f, 77f)
        val headerTable = PdfPTable(1)
        headerTable.setWidths(
            floatArrayOf(
                1f
            )
        )
        headerTable.isLockedWidth = true
        headerTable.totalWidth = PageSize.A4.width // set content width to fill document
        val cell = PdfPCell(Image.getInstance(image)) // Logo Cell
        cell.border = Rectangle.NO_BORDER // Removes border
        cell.paddingTop = 0f // sets padding
        cell.paddingLeft = 0.25f
        cell.horizontalAlignment = Rectangle.ALIGN_CENTER
        cell.paddingBottom = 0f

        cell.backgroundColor = BaseColor.WHITE
        cell.horizontalAlignment = Element.ALIGN_CENTER
        headerTable.addCell(cell)


        return headerTable

    }

    private fun setTableData(
        subActivityReportDataList: List<SubActivityReportData>,
        headerList: List<String>
    ) {
        val coloumsSize = headerList.size
        val dataTable = PdfPTable(coloumsSize)
        var widthArray = floatArrayOf(0.7f, 0.15f, 0.7f)
        if (coloumsSize == 3) {
            widthArray = floatArrayOf(0.7f, 0.15f, 0.7f)
        } else if (coloumsSize == 4) {
            widthArray = floatArrayOf(0.5f, 1.50f, 0.5f, 0.5f)
        }
        dataTable.isLockedWidth = true
        dataTable.totalWidth = PageSize.A4.width
        dataTable.setWidths(widthArray)

        headerList.forEach {
            val cell = createCell(it, Rectangle.ALIGN_CENTER)
            dataTable.addCell(cell)
            doc.add(dataTable)
        }
        for (valueItem in subActivityReportDataList) {
            dataTable.deleteBodyRows()
            headerList.forEach {
                when (it) {
                    "subActivityId" -> {
                        val cell = createItemCell(
                            valueItem.subActivityId.toString(),
                            Rectangle.ALIGN_CENTER
                        )
                        dataTable.addCell(cell)
                    }

                    "subActivityName" -> {
                        val cell = createItemCell(
                            valueItem.subActivityName.toString(),
                            Rectangle.ALIGN_CENTER
                        )
                        dataTable.addCell(cell)
                    }

                    "male_count" -> {
                        val cell =
                            createItemCell(valueItem.male_count.toString(), Rectangle.ALIGN_CENTER)
                        dataTable.addCell(cell)
                    }

                    "female_count" -> {
                        val cell = createItemCell(
                            valueItem.female_count.toString(),
                            Rectangle.ALIGN_CENTER
                        )
                        dataTable.addCell(cell)
                    }

                    "wells_count" -> {
                        val cell =
                            createItemCell(valueItem.wells_count.toString(), Rectangle.ALIGN_CENTER)
                        dataTable.addCell(cell)
                    }

                    "survey_count" -> {
                        val cell = createItemCell(
                            valueItem.survey_count.toString(),
                            Rectangle.ALIGN_CENTER
                        )
                        dataTable.addCell(cell)
                    }

                    "village_count" -> {
                        val cell = createItemCell(
                            valueItem.village_count.toString(),
                            Rectangle.ALIGN_CENTER
                        )
                        dataTable.addCell(cell)
                    }

                    "no_of_farmers" -> {
                        val cell = createItemCell(
                            valueItem.no_of_farmers.toString(),
                            Rectangle.ALIGN_CENTER
                        )
                        dataTable.addCell(cell)
                    }

                }
            }
            doc.add(dataTable)

        }


    }

    fun addParagraph(message: String?) {
        if (message == null) {
            doc.add(Paragraph("\n"))
        } else {
            doc.add(Paragraph("\n $message"))
        }

    }

    private fun createCell(columnName: String, alignment: Int): PdfPCell {
        val cell = PdfPCell(Phrase(columnName))
        cell.border = Rectangle.NO_BORDER
        cell.paddingTop = TABLE_TOP_PADDING
        cell.horizontalAlignment = alignment
        cell.paddingBottom = TABLE_TOP_PADDING
        cell.paddingRight = TABLE_HORIZONTAL_PADDING
        return cell

    }

    private fun createItemCell(itemValue: String, alignment: Int): PdfPCell {
        val cell = PdfPCell(Phrase(itemValue))
        cell.border = Rectangle.NO_BORDER
        cell.paddingTop = TABLE_TOP_PADDING
        cell.horizontalAlignment = alignment
        cell.paddingBottom = TABLE_TOP_PADDING
        cell.paddingRight = TABLE_HORIZONTAL_PADDING
        return cell

    }


    private fun callActivityData(activityName: String, description: String) {
        doc.add(Paragraph(activityName))
        doc.add(Paragraph(description))
    }

    private fun setImages(images: List<String>, callback: (MutableList<Bitmap>) -> Unit) {
        val bitmap = mutableListOf<Bitmap>()
        var counter = 0
        val size = images.size

        fun checkCompletion() {
            if (counter == size) {
                callback(bitmap)
            }
        }

        images.forEach {
            Log.d("sachin", "downloading")
            downloadImages(it) { imageBitmap ->
                counter++
                Log.d("sachin", "download Completed")
                if (imageBitmap != null) {
                    bitmap.add(imageBitmap)
                }
                checkCompletion()
            }
        }
    }

    private fun downloadImages(imageUrl: String, callback: (Bitmap?) -> Unit) {


        Glide.with(context)
            .asBitmap()
            .load(imageUrl)
            .into(object : SimpleTarget<Bitmap>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?
                ) {

                    callback(resource)
                }

                override fun onLoadFailed(errorDrawable: Drawable?) {
                    // Handle load failure here
                    callback(null)
                }
            })

    }

}