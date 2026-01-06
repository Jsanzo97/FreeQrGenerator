package com.example.freeqrgenerator.ui.items

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.uikit.LocalUIViewController
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.Foundation.NSData
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation
import platform.UIKit.UIImagePickerController
import platform.UIKit.UIImagePickerControllerDelegateProtocol
import platform.UIKit.UIImagePickerControllerEditedImage
import platform.UIKit.UIImagePickerControllerOriginalImage
import platform.UIKit.UIImagePickerControllerSourceType
import platform.UIKit.UINavigationControllerDelegateProtocol
import platform.darwin.NSObject

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun CustomImagePicker(
    onImageSelected: (ByteArray) -> Unit,
    modifier: Modifier,
    text: String
) {
    val uiViewController = LocalUIViewController.current

    val delegate = remember {
        object : NSObject(), UIImagePickerControllerDelegateProtocol,
            UINavigationControllerDelegateProtocol {

            override fun imagePickerController(
                picker: UIImagePickerController,
                didFinishPickingMediaWithInfo: Map<Any?, *>
            ) {
                val image = (didFinishPickingMediaWithInfo[UIImagePickerControllerEditedImage]
                    ?: didFinishPickingMediaWithInfo[UIImagePickerControllerOriginalImage]) as? UIImage

                image?.let {
                    val nsData: NSData? = UIImageJPEGRepresentation(it, 1.0)
                    nsData?.let { data ->
                        val bytes = ByteArray(data.length.toInt())
                        bytes.usePinned { pinned ->
                            platform.posix.memcpy(
                                pinned.addressOf(0),
                                data.bytes,
                                data.length
                            )
                        }
                        onImageSelected(bytes)
                    }
                }
                picker.dismissViewControllerAnimated(true, null)
            }

            override fun imagePickerControllerDidCancel(picker: UIImagePickerController) {
                picker.dismissViewControllerAnimated(true, null)
            }
        }
    }

    CustomButton(
        modifier = modifier,
        text = text,
        icon = Icons.Default.Image,
        isCircular = true,
        onClick = {
            val picker = UIImagePickerController().apply {
                sourceType = UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypePhotoLibrary
                this.delegate = delegate
            }
            uiViewController.presentViewController(picker, animated = true, completion = null)
        }
    )
}