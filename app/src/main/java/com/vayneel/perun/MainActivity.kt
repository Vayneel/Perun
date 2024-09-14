package com.vayneel.perun

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import org.jsoup.Jsoup
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val url = "https://zakarpat.energy/customers/break-in-electricity-supply/schedule/"

        val todayImage: ImageView = findViewById(R.id.todayImage)
        val tomorrowImage: ImageView = findViewById(R.id.tomorrowImage)

//        var todayImageBitmap: Bitmap
//        var tomorrowImageBitmap: Bitmap


        val mainButton = findViewById<Button>(R.id.mainButton)


        fun updateImages() {
            val scrapingThread = Thread {
                for (i in 0..1){
                    val imageView = if (i == 0) todayImage else tomorrowImage

                    try {
                        val document = Jsoup.connect(url).get()

                        val image = document.select(".contacts-page__main div p img")[i].attr("src")

                        val imageUrl = "https://zakarpat.energy$image"

                        val urlConnection = URL(imageUrl)

                        val connection = urlConnection.openConnection() as HttpURLConnection
                        connection.doInput = true
                        connection.connect()
                        val input = connection.inputStream
                        val imageBitmap = BitmapFactory.decodeStream(input)

                        runOnUiThread {
                            imageView.setImageBitmap(imageBitmap)
                        }

                    }
                    catch (e: Exception) {
                        Log.v("<Error>", e.toString())
                    }
                }

            }

            scrapingThread.start()
        }

        updateImages()

        mainButton.setOnClickListener {
            updateImages()
            Toast.makeText(this, getString(R.string.updateToast), Toast.LENGTH_SHORT).show()
        }

//        todayImage.setOnClickListener {
//            copyImageToClipboard(todayImageBitmap)
//        }

    }

//    private fun copyImageToClipboard(bitmap: Bitmap) {
//        try {
//            val file = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "copied_image.png")
//            val outputStream: OutputStream = FileOutputStream(file)
//            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
//            outputStream.flush()
//            outputStream.close()
//
//            val uri: Uri = Uri.fromFile(file)
//
//            val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
//            val clipData = ClipData.newUri(contentResolver, "Image", uri)
//            clipboardManager.setPrimaryClip(clipData)
//
//            Toast.makeText(this, "Image copied to clipboard", Toast.LENGTH_SHORT).show()
//        } catch (e: Exception) {
//            Log.v("<Error>", e.toString())
//            Toast.makeText(this, "Failed to copy image", Toast.LENGTH_SHORT).show()
//        }
//    }
}