/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.jetpacksample.utils.ext

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.IdRes
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.jetpacksample.utils.DensityUtils

/**
 * The `fragment` is added to the container view with id `frameId`. The operation is
 * performed by the `fragmentManager`.
 */
fun AppCompatActivity.replaceFragmentInActivity(fragment: Fragment, @IdRes frameId: Int) {
    supportFragmentManager.transact {
        replace(frameId, fragment)
    }
}

/**
 * The `fragment` is added to the container view with tag. The operation is
 * performed by the `fragmentManager`.
 */
fun AppCompatActivity.addFragmentToActivity(fragment: Fragment, tag: String) {
    supportFragmentManager.transact {
        add(fragment, tag)
    }
}

fun AppCompatActivity.setupActionBar(@IdRes toolbarId: Int, action: ActionBar.() -> Unit) {
    setSupportActionBar(findViewById(toolbarId))
    supportActionBar?.run {
        action()
    }
}

fun AppCompatActivity.setupToolbar(toolbar: Toolbar) {
    setSupportActionBar(toolbar)
    supportActionBar?.setDisplayShowHomeEnabled(true)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
}

/**
 * Runs a FragmentTransaction, then calls commit().
 */
private inline fun FragmentManager.transact(action: FragmentTransaction.() -> Unit) {
    beginTransaction().apply {
        action()
    }.commit()
}

@SuppressLint("ClickableViewAccessibility")
fun AppCompatActivity.setupUi(rootView: View) {
    if (rootView !is EditText) {
        rootView.setOnTouchListener { view, _ ->
            val inputManager: InputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(view.windowToken, 0)
            false
        }
    }

    if (rootView is ViewGroup) {
        for (i in 0 until rootView.childCount) {
            setupUi(rootView.getChildAt(i))
        }
    }
}

fun AppCompatActivity.setStatusBarTextColor(isBlack: Boolean) {
    if ((Build.MANUFACTURER.equals("OPPO", ignoreCase = true) || Build.MANUFACTURER.equals(
            "xiaomi",
            ignoreCase = true
        ) || Build.MANUFACTURER.equals("vivo", ignoreCase = true)) &&
        Build.VERSION.SDK_INT < Build.VERSION_CODES.M && isBlack
    ) {
        window.statusBarColor = ContextCompat.getColor(this, 0x4D000000)
    } else {
        if (isBlack && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }
    }
}

fun AppCompatActivity.calculateSoftKeyboardHeight(result: (Int) -> Unit) {
    window.decorView.findViewById<View>(android.R.id.content).viewTreeObserver.addOnGlobalLayoutListener(
        object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val rect = Rect()
                window.decorView.getWindowVisibleDisplayFrame(rect)
                val screenHeight = DensityUtils.screenHeight
                val rectHeight = rect.bottom - rect.top
                if (rectHeight != screenHeight) {
                    val keyboardHeight = screenHeight - rectHeight
                    if (keyboardHeight > 0) {
                        result(keyboardHeight)
                        window.decorView.findViewById<View>(android.R.id.content).viewTreeObserver.removeOnGlobalLayoutListener(
                            this
                        )
                    }
                }
            }
        })
}
