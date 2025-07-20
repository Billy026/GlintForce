package com.billy.glintforce.mainApplication.settingsTab.reportTab

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.pdf.PdfDocument
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.billy.glintforce.R
import com.billy.glintforce.getDownloadDirectory
import com.billy.glintforce.common.roundDown
import com.billy.glintforce.common.roundUp
import com.billy.glintforce.data.expenses.Expenses
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.Locale

/**
 * Function to generate PDF of monthly expenses
 */
fun generatePDF(
    // Values
    context: Context,
    list: List<Expenses>,
    strList: List<String>,

    // Actions
    //directory: (Context) -> File,
) {
    val activity = context as Activity
    val pdfDocument = PdfDocument()

    createPage(
        pdfDocument = pdfDocument,
        context = context,
        list = list,
        strList = strList,
        pageNum = 1
    )

    // Writing pdf file to external storage
    try {
        val folder = context.getDownloadDirectory()
        val file = File(folder, "${list.first().date.drop(3)}.pdf")

        Log.d("debuggingCheck", "check")
        pdfDocument.writeTo(FileOutputStream(file))
        Toast.makeText(context, strList[6], Toast.LENGTH_SHORT).show()
    } catch (ex: IOException) {
        ex.printStackTrace()
    }
    pdfDocument.close()
}

/**
 * Changes drawable into bitmap image
 */
private fun drawableToBitmap(drawable: Drawable): Bitmap? {
    if (drawable is BitmapDrawable) {
        return drawable.bitmap
    }
    val bitmap = Bitmap.createBitmap(
        drawable.intrinsicWidth,
        drawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    return bitmap
}

/**
 * Creates page template for PDF
 */
private fun createPage(
    // Values
    pdfDocument: PdfDocument,
    context: Context,
    list: List<Expenses>,
    strList: List<String>,
    pageNum: Int
) {
    // Properties of pdf doc
    val pageHeight = 1120
    val pageWidth = 792
    var finished = false

    // Initialisation of pdf doc and its components
    val paint = Paint()
    val border = Paint()
    val title = Paint()

    // Initialisation of data about pdf doc
    val myPageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNum).create()
    val myPage = pdfDocument.startPage(myPageInfo)
    val canvas: Canvas = myPage.canvas

    // Creating bitmap of GlintForce logo
    val bitmap: Bitmap? = drawableToBitmap(context.resources.getDrawable(R.drawable.logo))
    val scaleBitmap: Bitmap = Bitmap.createScaledBitmap(bitmap!!, 60, 60, false)

    /**
     * Designing of pdf doc
     */
    // Header
    canvas.drawBitmap(scaleBitmap, 30f, 30f, paint)
    title.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
    title.textSize = 50f
    canvas.drawText(
        strList[0],
        250f, 110f, title
    )
    canvas.drawText(
        list.first().date.filterIndexed { index, _ -> index > 2 },
        250f, 160f, title
    )
    title.textSize = 18f
    canvas.drawText(pageNum.toString(), pageWidth - 60f, 68f, title)

    // Table header background color
    paint.color = ContextCompat.getColor(context, R.color.lightBlue)
    border.color = ContextCompat.getColor(context, R.color.black)
    canvas.drawRect(50f, 255f, 160f, 295f, border)
    canvas.drawRect(55f, 260f, 157.5f, 292.5f, paint)
    canvas.drawRect(160f, 255f, 250f, 295f, border)
    canvas.drawRect(162.5f, 260f, 247.5f, 292.5f, paint)
    canvas.drawRect(250f, 255f, 400f, 295f, border)
    canvas.drawRect(252.5f, 260f, 397.5f, 292.5f, paint)
    canvas.drawRect(400f, 255f, 640f, 295f, border)
    canvas.drawRect(402.5f, 260f, 637.5f, 292.5f, paint)
    canvas.drawRect(640f, 255f, 742f, 295f, border)
    canvas.drawRect(642.5f, 260f, 737f, 292.5f, paint)

    // Table header text
    title.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
    title.textSize = 20f
    canvas.drawText(strList[1], 60f, 280f, title)
    canvas.drawText(strList[2], 170f, 280f, title)
    canvas.drawText(strList[3], 260f, 280f, title)
    canvas.drawText(strList[4], 410f, 280f, title)
    canvas.drawText(strList[5], 650f, 280f, title)

    // Content
    var currHeight = 297.5f
    paint.color = ContextCompat.getColor(context, R.color.white)
    title.textSize = 16f
    list.forEach {
        // Values for wrap around of text
        val maxCategoryIteration = roundUp(int = it.category.length, divisor = 17)
        val maxDescIteration = roundUp(int = it.desc.length, divisor = 27)
        // Dynamic current height of Expense table
        val height = 18f * maxOf(a = maxCategoryIteration, b = maxDescIteration) + 10f

        // Creates new page if height of next Expense will exceed remaining page height
        if (currHeight + height + 5f > pageHeight - 50f) {
            pdfDocument.finishPage(myPage)
            finished = true
            createPage(
                pdfDocument = pdfDocument,
                context = context,
                list = list.dropWhile { expense -> expense != it },
                strList = strList,
                pageNum = pageNum + 1
            )
        } else {
            // Drawing border for individual Expenses
            canvas.drawRect(50f, currHeight - 2.5f, 160f, currHeight + height + 2.5f, border)
            canvas.drawRect(55f, currHeight, 157.5f, currHeight + height, paint)
            canvas.drawRect(160f, currHeight - 2.5f, 250f, currHeight + height + 2.5f, border)
            canvas.drawRect(162.5f, currHeight, 247.5f, currHeight + height, paint)
            canvas.drawRect(250f, currHeight - 2.5f, 400f, currHeight + height + 2.5f, border)
            canvas.drawRect(252.5f, currHeight, 397.5f, currHeight + height, paint)
            canvas.drawRect(400f, currHeight - 2.5f, 640f, currHeight + height + 2.5f, border)
            canvas.drawRect(402.5f, currHeight, 637.5f, currHeight + height, paint)
            canvas.drawRect(640f, currHeight - 2.5f, 742f, currHeight + height + 2.5f, border)
            canvas.drawRect(642.5f, currHeight, 737f, currHeight + height, paint)

            // Drawing text for individual Expenses
            val len = it.date.length
            canvas.drawText(
                it.date.filterIndexed { index, _ -> index < 6 || index > len - 6 },
                60f,
                currHeight + 21f,
                title
            )
            canvas.drawText(it.time, 170f, currHeight + 21f, title)
            canvas.drawText(
                "S$ ${String.format(Locale.getDefault(), "%.2f", it.cost)}",
                650f,
                currHeight + 21f,
                title
            )
            for (i in 0..<maxCategoryIteration) {
                canvas.drawText(
                    it.category.filterIndexed { index, _ ->
                        roundDown(
                            int = index,
                            divisor = 17
                        ) == i
                    },
                    260f,
                    currHeight + 21f + 18f * i,
                    title
                )
            }
            for (i in 0..<maxDescIteration) {
                canvas.drawText(
                    it.desc.filterIndexed { index, _ -> roundDown(int = index, divisor = 27) == i },
                    410f,
                    currHeight + 21f + 18f * i,
                    title
                )
            }

            // Updating current height of table
            currHeight += height
            currHeight += 5f
        }
    }

    // To finish last page
    if (!finished) {
        pdfDocument.finishPage(myPage)
    }
}

fun checkPermissions(context: Context): Boolean {
    // on below line we are creating a variable for both of our permissions.

    // on below line we are creating a variable for writing to external storage permission
    val writeStoragePermission = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    // on below line we are creating a variable for
    // reading external storage permission
    val readStoragePermission = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    // on below line we are returning true if both the
    // permissions are granted and returning false if permissions are not granted.
    return writeStoragePermission == PackageManager.PERMISSION_GRANTED && readStoragePermission == PackageManager.PERMISSION_GRANTED
}

// on below line we are creating a function to request permission.
fun requestPermission(activity: Activity) {

    // on below line we are requesting read and write to
    // storage permission for our application.
    ActivityCompat.requestPermissions(
        activity,
        arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ), 101
    )
}