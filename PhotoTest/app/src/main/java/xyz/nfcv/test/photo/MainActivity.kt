package xyz.nfcv.test.photo

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import pub.devrel.easypermissions.EasyPermissions


class MainActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        choose_img.setOnClickListener {
            getPicture()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            when (requestCode) {
                RESULT_LOAD_IMAGE -> {
                    data.data?.let { uri -> load(this, uri)?.cropCenter().also { image_test.setImageBitmap(it) } }
                }
            }
        }
    }

    private fun getPicture() {
        val intent = Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, RESULT_LOAD_IMAGE)
    }



    companion object {
        const val RESULT_LOAD_IMAGE = 10

        fun load(context: Context, uri: Uri): Bitmap? {
            try {
                return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, uri))
                } else {
//                    BitmapFactory.decodeStream(context.contentResolver.openInputStream(uri))
                    MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }

        fun Bitmap.cropCenter(): Bitmap {
            val width = this.width
            val height = this.height
            return if (width > height) {
                Bitmap.createBitmap(this, (width - height) / 2, 0, height, height)
            } else {
                Bitmap.createBitmap(this, 0, (height - width) / 2, width, width)
            }
        }

        fun Bitmap.zoom(newWidth: Int, newHeight: Int): Bitmap{
            val width = width
            val height = height
            val scaleWidth = newWidth / width.toFloat()
            val scaleHeight: Float = newHeight / height.toFloat()
            val matrix = Matrix()
            matrix.postScale(scaleWidth, scaleHeight)
            return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
        }
    }

    private val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>?) {
        Toast.makeText(this, "获取权限被拒绝，将无法选择图片", Toast.LENGTH_LONG).show()
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>?) {}

    private fun getPermission() {
        if (!EasyPermissions.hasPermissions(this, *permissions)) {
            EasyPermissions.requestPermissions(this, "我们需要你的相册权限", 1, *permissions)
        }
    }

}