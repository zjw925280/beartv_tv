package com.mybase.libb.ext

import android.app.Activity
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.XXPermissions



fun Activity.questPermissions(
    vararg permissions: String,
    onDenied: ((permissions: MutableList<String>?, never: Boolean) -> Unit)? = null,
    onNext: () -> Unit
) {
    if (!XXPermissions.isGranted(this, permissions)) {
        XXPermissions.with(this).permission(permissions).request(object : OnPermissionCallback {
            override fun onGranted(permissions: MutableList<String>?, all: Boolean) {
                if (all) {
                    onNext.invoke()
                }
            }

            override fun onDenied(permissions: MutableList<String>?, never: Boolean) {
                super.onDenied(permissions, never)
                onDenied?.invoke(permissions, never)
            }
        })
    } else {
        onNext.invoke()
    }

}