package com.mena97villalobos.issue_reporter.model

import android.text.TextUtils

/**
 * Issue report model
 * @param title Issue title
 * @param description Issue description
 * @param deviceInfo Device information [DeviceInfo]
 */
data class Report(
    val title: String = "",
    private val description: String = "",
    private val deviceInfo: DeviceInfo? = null,
    private val email: String? = "",
) {

    companion object {
        private const val PARAGRAPH_BREAK = "\n\n"
        private const val HORIZONTAL_RULE = "---"
    }

    fun getDescription(): String {
        val builder = StringBuilder()
        if (!TextUtils.isEmpty(email)) {
            builder
                .append("*Sent by [**")
                .append(email)
                .append("**](mailto:")
                .append(email)
                .append(")*")
                .append(PARAGRAPH_BREAK)
        }
        builder
            .append("Description:\n")
            .append(HORIZONTAL_RULE)
            .append(PARAGRAPH_BREAK)
            .append(description)
            .append(PARAGRAPH_BREAK)
            .append(deviceInfo?.toMarkdown())
        return builder.toString()
    }
}
