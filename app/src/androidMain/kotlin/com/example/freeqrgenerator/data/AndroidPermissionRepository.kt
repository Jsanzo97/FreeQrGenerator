package com.example.freeqrgenerator.data

import android.Manifest
import android.content.Context
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import com.example.freeqrgenerator.domain.repository.PermissionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class AndroidPermissionRepository(
    private val context: Context
) : PermissionRepository {

    private val _permissionRequests = MutableSharedFlow<Unit>()
    override val permissionRequests: Flow<Unit> = _permissionRequests.asSharedFlow()

    override fun isWriteStorageGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            true
        } else {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PermissionChecker.PERMISSION_GRANTED
        }
    }

    override suspend fun requestWriteStoragePermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            _permissionRequests.emit(Unit)
        }
    }
}