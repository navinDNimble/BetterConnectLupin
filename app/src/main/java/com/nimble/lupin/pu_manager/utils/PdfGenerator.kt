package com.nimble.lupin.pu_manager.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Environment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.itextpdf.text.Document
import com.itextpdf.text.PageSize
import com.itextpdf.text.Paragraph
import com.itextpdf.text.Phrase
import com.itextpdf.text.Rectangle
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import com.nimble.lupin.pu_manager.models.TaskUpdatesModel
import com.nimble.lupin.pu_manager.models.reportsModel.PdfDataModel
import com.nimble.lupin.pu_manager.models.reportsModel.SubActivityReportData
import java.io.File
import java.io.FileOutputStream


class PdfGenerator(
    private val pdfReportData: List<PdfDataModel>,
    private val fileName: String,
    val context: Context
) {
    private var doc = Document(PageSize.A4, 0f, 0f, 0f, 0f)
    private var writer: PdfWriter
    private val TABLE_TOP_PADDING = 10f
    private val TABLE_HORIZONTAL_PADDING = 5f
    private var invoiceTableData = ArrayList<TaskUpdatesModel>()

    init {
        val outPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            .toString() + "/$fileName" //location where the pdf will store
        val file = File(outPath)
        if (file.exists()) {
            file.delete()
        }
        writer = PdfWriter.getInstance(doc, FileOutputStream(file))
        doc.open()

        pdfReportData.forEach { it ->
            callActivityData(it.activityName, it.activityDescription)
            setImages(it.imageReportList){done->
                if (done){
                   setTableData(it.subActivityReportDataList ,it.headerList)
                }
            }
        }
    }
    private fun setTableData(
        subActivityReportDataList: List<SubActivityReportData>,
        headerList: List<String>
    ){
       val  coloumsSize = headerList.size
        val titleTable = PdfPTable(coloumsSize)
        var widtharray :FloatArray
        if (coloumsSize==3){
            widtharray = floatArrayOf(0.7f,0.15f,0.7f)
        }else if (coloumsSize==4){
            widtharray = floatArrayOf(0.5f,0.15f,0.5f,0.5f)
        }

        titleTable.isLockedWidth = true
        titleTable.totalWidth = PageSize.A4.width
        titleTable.setWidths(
            floatArrayOf(.32f, .8f, .8f, .75f, 0.40f ,.3f,0.5f)
        )
        createTable()
        addTableHeader(headerList)
        addTableData(subActivityReportDataList)
    }
    private fun createTable(){


        val srNoCell = PdfPCell(Phrase("FirstColumn"))
        srNoCell.border = Rectangle.NO_BORDER
        srNoCell.paddingTop = TABLE_TOP_PADDING
        srNoCell.horizontalAlignment = Rectangle.ALIGN_CENTER
        srNoCell.paddingBottom = TABLE_TOP_PADDING
        srNoCell.paddingRight  = TABLE_HORIZONTAL_PADDING
        titleTable.addCell(srNoCell)


        val medicineCell = PdfPCell(Phrase("secondColumn"))
        medicineCell.border = Rectangle.NO_BORDER
        medicineCell.horizontalAlignment = Rectangle.ALIGN_LEFT
        medicineCell.paddingBottom = TABLE_TOP_PADDING
        medicineCell.paddingTop = TABLE_TOP_PADDING
        medicineCell.paddingRight  = TABLE_HORIZONTAL_PADDING
        medicineCell.paddingLeft = TABLE_HORIZONTAL_PADDING
        titleTable.addCell(medicineCell)

        val packagingCell = PdfPCell(Phrase("ThirdColumn"))
        packagingCell.border = Rectangle.NO_BORDER
        packagingCell.horizontalAlignment = Rectangle.ALIGN_LEFT
        packagingCell.paddingBottom = TABLE_TOP_PADDING
        packagingCell.paddingTop = TABLE_TOP_PADDING
        packagingCell.paddingRight  = TABLE_HORIZONTAL_PADDING
        packagingCell.paddingLeft = TABLE_HORIZONTAL_PADDING
        titleTable.addCell(packagingCell)

        val manufactureCell = PdfPCell(Phrase("forthColumn"))
        manufactureCell.border = Rectangle.NO_BORDER
        manufactureCell.horizontalAlignment = Rectangle.ALIGN_LEFT
        manufactureCell.paddingBottom = TABLE_TOP_PADDING
        manufactureCell.paddingTop = TABLE_TOP_PADDING
        manufactureCell.paddingRight  = TABLE_HORIZONTAL_PADDING
        manufactureCell.paddingLeft = TABLE_HORIZONTAL_PADDING
        titleTable.addCell(manufactureCell)

        val mrpCell = PdfPCell(Phrase("fifthColumn"))
        mrpCell.horizontalAlignment = Rectangle.ALIGN_LEFT
        mrpCell.border = Rectangle.NO_BORDER
        mrpCell.paddingTop = TABLE_TOP_PADDING
        mrpCell.paddingBottom = TABLE_TOP_PADDING
        mrpCell.paddingRight  = TABLE_HORIZONTAL_PADDING
        mrpCell.paddingLeft = TABLE_HORIZONTAL_PADDING
        titleTable.addCell(mrpCell)

        val qty = PdfPCell(Phrase("sixColumn"))
        qty.horizontalAlignment = Rectangle.ALIGN_LEFT
        qty.border = Rectangle.NO_BORDER
        qty.paddingTop = TABLE_TOP_PADDING
        qty.paddingBottom = TABLE_TOP_PADDING
        qty.paddingRight  = TABLE_HORIZONTAL_PADDING
        qty.paddingLeft = TABLE_HORIZONTAL_PADDING
        titleTable.addCell(qty)

        val amt = PdfPCell(Phrase("sevenColumn"))
        amt.horizontalAlignment = Rectangle.ALIGN_LEFT
        amt.border = Rectangle.NO_BORDER
        amt.paddingTop = TABLE_TOP_PADDING
        amt.paddingBottom = TABLE_TOP_PADDING
        amt.paddingRight  = TABLE_HORIZONTAL_PADDING
        amt.paddingLeft = TABLE_HORIZONTAL_PADDING
        titleTable.addCell(amt)

        doc.add(titleTable)

    }
    fun addTableHeader(headerList: List<String>){
        doc.add(Paragraph("\n"))
    }


    private fun addTableData(subActivityReportDataList: List<SubActivityReportData>){
        val itemsTable = PdfPTable(7)
        itemsTable.isLockedWidth = true
        itemsTable.totalWidth = PageSize.A4.width
        itemsTable.setWidths(
            floatArrayOf(.32f, .8f, .8f, .75f, 0.40f ,.3f,0.5f)
        )
        for (item in subActivityReportDataList) {
            itemsTable.deleteBodyRows()

            val srNo = PdfPCell(Phrase("item.SrNo"))
            srNo.border = Rectangle.NO_BORDER
            srNo.border = Rectangle.NO_BORDER
            srNo.paddingTop = TABLE_TOP_PADDING
            srNo.horizontalAlignment = Rectangle.ALIGN_CENTER
            srNo.paddingRight  = TABLE_HORIZONTAL_PADDING
            itemsTable.addCell(srNo)


            val medicineName = PdfPCell(Phrase("item.MedicineName"))
            medicineName.border = Rectangle.NO_BORDER
            medicineName.horizontalAlignment = Rectangle.ALIGN_LEFT
            medicineName.paddingTop = TABLE_TOP_PADDING
            medicineName.paddingRight  = TABLE_HORIZONTAL_PADDING
            medicineName.paddingLeft = TABLE_HORIZONTAL_PADDING
            itemsTable.addCell(medicineName)

            val packaging = PdfPCell(Phrase("item.Packaging"))
            packaging.border = Rectangle.NO_BORDER
            packaging.horizontalAlignment = Rectangle.ALIGN_LEFT
            packaging.paddingTop = TABLE_TOP_PADDING
            packaging.paddingRight  = TABLE_HORIZONTAL_PADDING
            packaging.paddingLeft = TABLE_HORIZONTAL_PADDING
            itemsTable.addCell(packaging)

            val manufacturer = PdfPCell(Phrase("item.Manufacturer"))
            manufacturer.border = Rectangle.NO_BORDER
            manufacturer.horizontalAlignment = Rectangle.ALIGN_LEFT
            manufacturer.paddingTop = TABLE_TOP_PADDING
            manufacturer.paddingRight  = TABLE_HORIZONTAL_PADDING
            manufacturer.paddingLeft = TABLE_HORIZONTAL_PADDING
            itemsTable.addCell(manufacturer)

            val mrp = PdfPCell(Phrase("item.MRP"))
            mrp.horizontalAlignment = Rectangle.ALIGN_LEFT
            mrp.border = Rectangle.NO_BORDER
            mrp.paddingTop = TABLE_TOP_PADDING
            mrp.paddingRight  = TABLE_HORIZONTAL_PADDING
            mrp.paddingLeft = TABLE_HORIZONTAL_PADDING
            itemsTable.addCell(mrp)

            val qty = PdfPCell(Phrase("item.Quantity"))
            qty.horizontalAlignment = Rectangle.ALIGN_CENTER
            qty.border = Rectangle.NO_BORDER
            qty.paddingTop = TABLE_TOP_PADDING
            qty.paddingRight  = TABLE_HORIZONTAL_PADDING
            qty.paddingLeft = TABLE_HORIZONTAL_PADDING
            itemsTable.addCell(qty)

            val amt = PdfPCell(Phrase("item.Amount"))
            amt.horizontalAlignment = Rectangle.ALIGN_LEFT
            amt.border = Rectangle.NO_BORDER
            amt.paddingTop = TABLE_TOP_PADDING
            amt.paddingRight  = TABLE_HORIZONTAL_PADDING
            amt.paddingLeft = TABLE_HORIZONTAL_PADDING
            itemsTable.addCell(amt)

            doc.add(itemsTable)
        }

    }


    private fun callActivityData(activityName: String, description: String) {
        doc.add(Paragraph(activityName))
        doc.add(Paragraph(description))
    }
    private fun setImages(images: List<String>,callback: (Boolean) -> Unit) {
        val bitmap = mutableListOf<Bitmap>()
        var counter = 0
        val size = images.size
        images.forEach {
            downloadImages(it) { imageBitmap ->
                counter++
                if (imageBitmap!=null){
                    bitmap.add(imageBitmap)
                }
                if (counter==size){
                    callback(true)
                }

            }
        }
    }
    private fun downloadImages(imageUrl: String, callback: (Bitmap?) -> Unit) {
        Glide.with(context)
            .asBitmap()
            .load(imageUrl)
            .into(object : SimpleTarget<Bitmap?>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: com.bumptech.glide.request.transition.Transition<in Bitmap?>?
                ) {
                    callback(resource)
                }

                override fun onLoadFailed(errorDrawable: Drawable?) {
                    super.onLoadFailed(errorDrawable)
                    callback(null)
                }
            })
    }

}