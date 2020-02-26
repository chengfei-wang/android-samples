package xyz.nfcv.test.photo

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.exifinterface.media.ExifInterface
import kotlinx.android.synthetic.main.activity_main.*
import pub.devrel.easypermissions.EasyPermissions
import java.io.IOException


class MainActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {

    private val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>?) {
        Toast.makeText(this, "获取权限被拒绝，将无法选择图片", Toast.LENGTH_LONG).show()
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>?) {}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getPermission()
        choose_img.setOnClickListener {
            getPicture()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            when (requestCode) {
                RESULT_LOAD_IMAGE -> {
                    data.data?.let { uri -> load(this, uri)?.also { image_test.setImageBitmap(it) } }
//                    data.data?.let {
//                        image_test.setImageURI(it)
//                        image_test.setImageBitmap(load(this, it).cropCenter().zoom(1000, 1000))
//                        Glide.with(this).asBitmap().load(it).centerCrop().into(image_test)
//                    }
                }
            }
        }
    }

    private fun getPicture() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, RESULT_LOAD_IMAGE)
    }

    private fun getPermission() {
        if (!EasyPermissions.hasPermissions(this, *permissions)) {
            EasyPermissions.requestPermissions(this, "我们需要你的相册权限", 1, *permissions)
        }
    }

    companion object {
        const val RESULT_LOAD_IMAGE = 10

        fun load(context: Context, uri: Uri): Bitmap? {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    return ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, uri))
                } else {
                    val columns = arrayOf(MediaStore.Images.Media.DATA)
                    val cursor = context.contentResolver?.query(uri, columns, null, null, null)
                    if (cursor != null && cursor.moveToFirst() && cursor.count > 0) {
                        val path = cursor.getString(cursor.getColumnIndex(columns[0]))
                        cursor.close()
                        return load(path, true)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }

        private fun load(path: String): Bitmap {
            return BitmapFactory.decodeFile(path)
        }

        /** 从给定的路径加载图片，并指定是否自动旋转方向
         * @param path 图片路径
         * @param adjustOrientation 是否自动旋转方向
         */
        private fun load(path: String, adjustOrientation: Boolean): Bitmap {
            if (!adjustOrientation) {
                return load(path)
            } else {
                var bm = load(path)
                var degree = 0
                val exif: ExifInterface?
                exif = try {
                    ExifInterface(path)
                } catch (e: IOException) {
                    e.printStackTrace()
                    null
                }

                if (exif != null) {
                    // 读取图片中相机方向信息
                    // 计算旋转角度
                    degree = when (exif.getAttributeInt(
                            ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_UNDEFINED
                    )) {
                        ExifInterface.ORIENTATION_ROTATE_90 -> 90
                        ExifInterface.ORIENTATION_ROTATE_180 -> 180
                        ExifInterface.ORIENTATION_ROTATE_270 -> 270
                        else -> 0
                    }
                }
                if (degree != 0) {
                    // 旋转图片
                    val m = Matrix()
                    m.postRotate(degree.toFloat())
                    bm = Bitmap.createBitmap(
                            bm, 0, 0, bm.width,
                            bm.height, m, true
                    )
                }
                return bm
            }
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

}