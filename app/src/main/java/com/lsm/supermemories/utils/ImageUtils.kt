package com.lsm.supermemories.utils

import android.content.Context
import android.graphics.drawable.Drawable

object ImageUtils {
    fun loadDrawable(context: Context, resourceId: Int): Drawable? {
        if (resourceId == 0)
            return null

        return context.getDrawable(resourceId)
    }
}