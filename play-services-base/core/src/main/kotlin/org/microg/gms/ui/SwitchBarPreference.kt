/*
 * SPDX-FileCopyrightText: 2023 microG Project Team
 * SPDX-License-Identifier: Apache-2.0
 */

package org.microg.gms.ui

import android.content.Context
import android.content.res.Configuration
import android.util.AttributeSet
import androidx.appcompat.widget.SwitchCompat
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceViewHolder
import androidx.preference.TwoStatePreference
import org.microg.gms.base.core.R

// TODO
class SwitchBarPreference : TwoStatePreference {
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context) : super(context)

    init {
        layoutResource = R.layout.preference_switch_bar
    }

    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)
        holder.isDividerAllowedBelow = false
        holder.isDividerAllowedAbove = false
        val switch = holder.findViewById(R.id.switch_widget) as SwitchCompat
        switch.setOnCheckedChangeListener(null)
        switch.isChecked = isChecked
        switch.setOnCheckedChangeListener { view, isChecked ->
            if (!callChangeListener(isChecked)) {
                view.isChecked = !isChecked
                return@setOnCheckedChangeListener
            }
            this.isChecked = isChecked
        }

        val systemTheme = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        var switchBarColor = androidx.appcompat.R.attr.colorButtonNormal
        var switchBarColorDisabled = androidx.appcompat.R.attr.colorControlHighlight
        if (AppCompatDelegate.getDefaultNightMode() != AppCompatDelegate.MODE_NIGHT_YES && systemTheme == Configuration.UI_MODE_NIGHT_NO) {
            switchBarColor = androidx.appcompat.R.attr.colorControlNormal
            switchBarColorDisabled = androidx.appcompat.R.attr.colorControlNormal
        }
        holder.itemView.setBackgroundColorAttribute(when {
            isChecked -> androidx.appcompat.R.attr.colorControlActivated
            isEnabled -> switchBarColor

            else -> switchBarColorDisabled
        })
    }
}

@Deprecated("Get rid")
interface PreferenceSwitchBarCallback {
    fun onChecked(newStatus: Boolean)
}