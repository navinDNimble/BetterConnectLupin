package com.nimble.lupin.pu_manager.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import com.itextpdf.text.*
import com.itextpdf.text.pdf.*
import com.nimble.lupin.pu_manager.R
import com.nimble.lupin.pu_manager.models.TaskUpdatesModel

import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class PdfGeneratorLearning(private val context: Context){
    private val TAG = "InvoiceGenerator"
    private var colorPrimary = BaseColor(40, 116, 240)
    private var textColor = BaseColor.BLACK
    private val FONT_SIZE_DEFAULT = 12f
    private val FONT_SIZE_SMALL = 8f
    private val FONT_SIZE_LARGE = 24f
    private var baseFontLight: BaseFont =
        BaseFont.createFont("assets/fonts/app_font_light.ttf", "UTF-8", BaseFont.EMBEDDED)
    private var baseFontRegular: BaseFont =
        BaseFont.createFont("assets/fonts/app_font_regular.ttf", "UTF-8", BaseFont.EMBEDDED)
    private var appFontRegular = Font(baseFontRegular, FONT_SIZE_DEFAULT)
    private var baseFontSemiBold: BaseFont =
        BaseFont.createFont("assets/fonts/app_font_semi_bold.ttf", "UTF-8", BaseFont.EMBEDDED)
    private var appFontSemiBold = Font(baseFontSemiBold, 24f)
    private var baseFontBold: BaseFont =
        BaseFont.createFont("assets/fonts/app_font_bold.ttf", "UTF-8", BaseFont.EMBEDDED)
    private var appFontBold = Font(baseFontBold, FONT_SIZE_DEFAULT)
    private val PADDING_EDGE = 40f
    private val TEXT_TOP_PADDING = 3f
    private val TABLE_TOP_PADDING = 10f
    private val TABLE_HORIZONTAL_PADDING = 5f
    private val TEXT_TOP_PADDING_EXTRA = 30f
    private var invoiceCurrency = ""

    private var invoiceLogoId = R.mipmap.ic_launcher_round
//    private var headerDataSource: ModelInvoiceHeader = ModelInvoiceHeader()
//    private var invoiceInfoDataSource: ModelInvoiceInfo = ModelInvoiceInfo()
//    private var invoiceTableHeaderDataSource: ModelTableHeader = ModelTableHeader()
    private var invoiceTableData = ArrayList<TaskUpdatesModel>()
//    private var invoicePriceDetailsDataSource: ModelInvoicePriceInfo = ModelInvoicePriceInfo()
//    private var invoiceFooterDataSource: ModelInvoiceFooter = ModelInvoiceFooter()

    init {
        textColor = getColor("#43425D")
        appFontRegular.color = textColor
        appFontRegular.size = 10f

    }

    fun setInvoiceColor(colorCode: String) {
        val color: Int = Color.parseColor(colorCode)
        val red: Int = Color.red(color)
        val green: Int = Color.green(color)
        val blue: Int = Color.blue(color)
        val alpha: Int = Color.alpha(color)
        colorPrimary = BaseColor(red, green, blue, alpha)

    }
    fun getColor(colorCode: String) : BaseColor
    {
        val color: Int = Color.parseColor(colorCode)
        val red: Int = Color.red(color)
        val green: Int = Color.green(color)
        val blue: Int = Color.blue(color)
        val alpha: Int = Color.alpha(color)
        return  BaseColor(red, green, blue, alpha)

    }

    fun setInvoiceTableData(invoiceTableData: List<TaskUpdatesModel>) {
        this.invoiceTableData = invoiceTableData as ArrayList<TaskUpdatesModel>
    }

//    fun setPriceInfoData(invoicePriceDetailsDataSource: ModelInvoicePriceInfo) {
//        this.invoicePriceDetailsDataSource = invoicePriceDetailsDataSource
//    }
//
//    fun setInvoiceFooterData(invoiceFooterDataSource: ModelInvoiceFooter) {
//        this.invoiceFooterDataSource = invoiceFooterDataSource
//    }


    fun generatePDF(filename: String): Uri {
        val doc = Document(PageSize.A4, 0f, 0f, 0f, 0f)
        val outPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/$filename" //location where the pdf will store
        val file = File(outPath)
        if (file.exists()) {
            file.delete()
        }
        val writer = PdfWriter.getInstance(doc, FileOutputStream(file))
        doc.open()
        initInvoiceHeader(doc)
        val contentByte = writer.directContent
        val currentY: Float = writer.getVerticalPosition(false)
        val currentX: Float = contentByte.horizontalScaling
        addLine(contentByte, 0f, currentY+120, currentX + 600, currentY+120,"#AD2040")
        addLine(contentByte, 0f, currentY, currentX + 600, currentY,"#293A8C")
        doc.add(Paragraph("\n"))
        initTableHeader(doc)
        initItemsTable(doc)
//        initPriceDetails(doc)
//        initFooter(doc)
        addLine(contentByte, 0f, contentByte.xtlm+20, currentX + 600, contentByte.ytlm+20,"#293A8C")
        doc.close()

        return FileProvider.getUriForFile(
            context,
             context.packageName+ ".provider",
            file
        )
    }

    private fun initInvoiceHeader(doc: Document) {
        val options = BitmapFactory.Options()
        options.inScaled = false
        val source: Bitmap = BitmapFactory.decodeResource(context.resources, invoiceLogoId, options)
        val stream = ByteArrayOutputStream()
        source.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val image = Image.getInstance(stream.toByteArray())
        image.scaleToFit(140f, 77f)
        val headerTable = PdfPTable(2)
        headerTable.setWidths(
            floatArrayOf(
                0.3f,
                1f
            )
        ) // adds 2 colomn horizontally
        headerTable.isLockedWidth = true
        headerTable.totalWidth = PageSize.A4.width // set content width to fill document
        val cell = PdfPCell(Image.getInstance(image)) // Logo Cell
        cell.border = Rectangle.NO_BORDER // Removes border
        cell.paddingTop = TEXT_TOP_PADDING_EXTRA // sets padding
        cell.paddingLeft = 0.25f
        cell.horizontalAlignment = Rectangle.ALIGN_CENTER
        cell.paddingBottom = TEXT_TOP_PADDING_EXTRA

        cell.backgroundColor = BaseColor.WHITE // sets background color
        cell.horizontalAlignment = Element.ALIGN_CENTER
        headerTable.addCell(cell) // Adds first cell with logo
        val tittleTable =
            PdfPTable(1) // new vertical table for contact details
        appFontSemiBold.color = textColor
        val tittleCell = PdfPCell(Paragraph(context.getString(R.string.lbl_pdf_title)+" "+"05-01-2024", appFontSemiBold))
        tittleCell.border = Rectangle.NO_BORDER
        tittleCell.horizontalAlignment = Element.ALIGN_LEFT
        tittleCell.paddingTop = TEXT_TOP_PADDING

        tittleTable.addCell(tittleCell)
        val headCell = PdfPCell(tittleTable)
        headCell.border = Rectangle.NO_BORDER
        headCell.horizontalAlignment = Element.ALIGN_LEFT
        headCell.verticalAlignment = Element.ALIGN_MIDDLE
        headCell.backgroundColor = BaseColor.WHITE
        headerTable.addCell(headCell)
        doc.add(headerTable)

    }

    private fun initTableHeader(doc: Document) {
        doc.add(Paragraph("\n")) //adds blank line to place table header below the line
        val titleTable = PdfPTable(7)
        titleTable.isLockedWidth = true
        titleTable.totalWidth = PageSize.A4.width
        titleTable.setWidths(
            floatArrayOf(.32f, .8f, .8f, .75f, 0.40f ,.3f,0.5f)
        )

        appFontBold.color = textColor
        appFontBold.size = 13f

        val srNoCell = PdfPCell(Phrase("FirstColumn", appFontBold))
        srNoCell.border = Rectangle.NO_BORDER
        srNoCell.paddingTop = TABLE_TOP_PADDING
        srNoCell.horizontalAlignment = Rectangle.ALIGN_CENTER
        srNoCell.paddingBottom = TABLE_TOP_PADDING
        srNoCell.paddingRight  = TABLE_HORIZONTAL_PADDING
        titleTable.addCell(srNoCell)


        val medicineCell = PdfPCell(Phrase("secondColumn", appFontBold))
        medicineCell.border = Rectangle.NO_BORDER
        medicineCell.horizontalAlignment = Rectangle.ALIGN_LEFT
        medicineCell.paddingBottom = TABLE_TOP_PADDING
        medicineCell.paddingTop = TABLE_TOP_PADDING
        medicineCell.paddingRight  = TABLE_HORIZONTAL_PADDING
        medicineCell.paddingLeft = TABLE_HORIZONTAL_PADDING
        titleTable.addCell(medicineCell)

        val packagingCell = PdfPCell(Phrase("ThirdColumn", appFontBold))
        packagingCell.border = Rectangle.NO_BORDER
        packagingCell.horizontalAlignment = Rectangle.ALIGN_LEFT
        packagingCell.paddingBottom = TABLE_TOP_PADDING
        packagingCell.paddingTop = TABLE_TOP_PADDING
        packagingCell.paddingRight  = TABLE_HORIZONTAL_PADDING
        packagingCell.paddingLeft = TABLE_HORIZONTAL_PADDING
        titleTable.addCell(packagingCell)

        val manufactureCell = PdfPCell(Phrase("forthColumn", appFontBold))
        manufactureCell.border = Rectangle.NO_BORDER
        manufactureCell.horizontalAlignment = Rectangle.ALIGN_LEFT
        manufactureCell.paddingBottom = TABLE_TOP_PADDING
        manufactureCell.paddingTop = TABLE_TOP_PADDING
        manufactureCell.paddingRight  = TABLE_HORIZONTAL_PADDING
        manufactureCell.paddingLeft = TABLE_HORIZONTAL_PADDING
        titleTable.addCell(manufactureCell)

        val mrpCell = PdfPCell(Phrase("fifthColumn", appFontBold))
        mrpCell.horizontalAlignment = Rectangle.ALIGN_LEFT
        mrpCell.border = Rectangle.NO_BORDER
        mrpCell.paddingTop = TABLE_TOP_PADDING
        mrpCell.paddingBottom = TABLE_TOP_PADDING
        mrpCell.paddingRight  = TABLE_HORIZONTAL_PADDING
        mrpCell.paddingLeft = TABLE_HORIZONTAL_PADDING
        titleTable.addCell(mrpCell)

        val qty = PdfPCell(Phrase("sixColumn", appFontBold))
        qty.horizontalAlignment = Rectangle.ALIGN_LEFT
        qty.border = Rectangle.NO_BORDER
        qty.paddingTop = TABLE_TOP_PADDING
        qty.paddingBottom = TABLE_TOP_PADDING
        qty.paddingRight  = TABLE_HORIZONTAL_PADDING
        qty.paddingLeft = TABLE_HORIZONTAL_PADDING
        titleTable.addCell(qty)

        val amt = PdfPCell(Phrase("sevenColumn", appFontBold))
        amt.horizontalAlignment = Rectangle.ALIGN_LEFT
        amt.border = Rectangle.NO_BORDER
        amt.paddingTop = TABLE_TOP_PADDING
        amt.paddingBottom = TABLE_TOP_PADDING
        amt.paddingRight  = TABLE_HORIZONTAL_PADDING
        amt.paddingLeft = TABLE_HORIZONTAL_PADDING
        titleTable.addCell(amt)


        doc.add(titleTable)
    }

    private fun initItemsTable(doc: Document) {
        val itemsTable = PdfPTable(7)
        itemsTable.isLockedWidth = true
        itemsTable.totalWidth = PageSize.A4.width
        itemsTable.setWidths(
            floatArrayOf(.32f, .8f, .8f, .75f, 0.40f ,.3f,0.5f)
        )
        appFontRegular.size = 10f
        for (item in invoiceTableData) {
            itemsTable.deleteBodyRows()

            val srNo = PdfPCell(Phrase("item.SrNo", appFontRegular))
            srNo.border = Rectangle.NO_BORDER
            srNo.border = Rectangle.NO_BORDER
            srNo.paddingTop = TABLE_TOP_PADDING
            srNo.horizontalAlignment = Rectangle.ALIGN_CENTER
            srNo.paddingRight  = TABLE_HORIZONTAL_PADDING
            itemsTable.addCell(srNo)


            val medicineName = PdfPCell(Phrase("item.MedicineName", appFontRegular))
            medicineName.border = Rectangle.NO_BORDER
            medicineName.horizontalAlignment = Rectangle.ALIGN_LEFT
            medicineName.paddingTop = TABLE_TOP_PADDING
            medicineName.paddingRight  = TABLE_HORIZONTAL_PADDING
            medicineName.paddingLeft = TABLE_HORIZONTAL_PADDING
            itemsTable.addCell(medicineName)

            val packaging = PdfPCell(Phrase("item.Packaging", appFontRegular))
            packaging.border = Rectangle.NO_BORDER
            packaging.horizontalAlignment = Rectangle.ALIGN_LEFT
            packaging.paddingTop = TABLE_TOP_PADDING
            packaging.paddingRight  = TABLE_HORIZONTAL_PADDING
            packaging.paddingLeft = TABLE_HORIZONTAL_PADDING
            itemsTable.addCell(packaging)

            val manufacturer = PdfPCell(Phrase("item.Manufacturer", appFontRegular))
            manufacturer.border = Rectangle.NO_BORDER
            manufacturer.horizontalAlignment = Rectangle.ALIGN_LEFT
            manufacturer.paddingTop = TABLE_TOP_PADDING
            manufacturer.paddingRight  = TABLE_HORIZONTAL_PADDING
            manufacturer.paddingLeft = TABLE_HORIZONTAL_PADDING
            itemsTable.addCell(manufacturer)

            val mrp = PdfPCell(Phrase("item.MRP", appFontRegular))
            mrp.horizontalAlignment = Rectangle.ALIGN_LEFT
            mrp.border = Rectangle.NO_BORDER
            mrp.paddingTop = TABLE_TOP_PADDING
            mrp.paddingRight  = TABLE_HORIZONTAL_PADDING
            mrp.paddingLeft = TABLE_HORIZONTAL_PADDING
            itemsTable.addCell(mrp)

            val qty = PdfPCell(Phrase("item.Quantity", appFontRegular))
            qty.horizontalAlignment = Rectangle.ALIGN_CENTER
            qty.border = Rectangle.NO_BORDER
            qty.paddingTop = TABLE_TOP_PADDING
            qty.paddingRight  = TABLE_HORIZONTAL_PADDING
            qty.paddingLeft = TABLE_HORIZONTAL_PADDING
            itemsTable.addCell(qty)

            val amt = PdfPCell(Phrase("item.Amount", appFontRegular))
            amt.horizontalAlignment = Rectangle.ALIGN_LEFT
            amt.border = Rectangle.NO_BORDER
            amt.paddingTop = TABLE_TOP_PADDING
            amt.paddingRight  = TABLE_HORIZONTAL_PADDING
            amt.paddingLeft = TABLE_HORIZONTAL_PADDING
            itemsTable.addCell(amt)

            doc.add(itemsTable)
        }
    }

//    private fun initFooter(doc: Document) {
//        appFontRegular.color = getColor("#AD2040")
//        appFontRegular.size = 11f
//        val footerTable = PdfPTable(1)
//        footerTable.totalWidth = PageSize.A4.width
//        footerTable.isLockedWidth = true
//        val thankYouCell =
//            PdfPCell(Phrase("invoiceFooterDataSource.message", appFontRegular))
//        thankYouCell.border = Rectangle.NO_BORDER
//        thankYouCell.paddingLeft = PADDING_EDGE
//        thankYouCell.paddingTop = PADDING_EDGE
//        thankYouCell.horizontalAlignment = Rectangle.ALIGN_LEFT
//        footerTable.addCell(thankYouCell)
//        doc.add(footerTable)
//    }


    private fun addLine(
        contentByte: PdfContentByte,
        startX: Float,
        startY: Float,
        endX: Float,
        endY: Float,
        color :String
    ) {
        contentByte.moveTo(startX, startY)
        contentByte.setColorStroke(getColor(color))
        contentByte.lineTo(endX, endY)
        contentByte.setLineWidth(15f)
        contentByte.stroke()
    }

}
