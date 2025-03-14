package com.example.janapps.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class ListLazy (
    @StringRes val stringResourceId: Int,
    @StringRes val stringResourceId2: Int,
    @DrawableRes val imageResourceId: Int,
    @StringRes val hobbies: Int
)