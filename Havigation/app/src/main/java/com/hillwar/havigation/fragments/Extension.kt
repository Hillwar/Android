package com.hillwar.havigation.fragments

import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment

fun Fragment.navigate(
    navDirection: NavDirections,
    navOptions: NavOptions? = null
) {
    NavHostFragment
        .findNavController(this)
        .navigate(navDirection, navOptions)
}