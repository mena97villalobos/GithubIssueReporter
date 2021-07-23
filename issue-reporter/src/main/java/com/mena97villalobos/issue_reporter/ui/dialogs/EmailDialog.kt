package com.mena97villalobos.issue_reporter.ui.dialogs

import android.content.Context
import com.maxkeppeler.sheets.core.IconButton
import com.maxkeppeler.sheets.core.SheetStyle
import com.maxkeppeler.sheets.input.InputSheet
import com.maxkeppeler.sheets.input.ValidationResult
import com.maxkeppeler.sheets.input.type.InputEditText
import com.maxkeppeler.sheets.input.type.InputSeparator
import com.mena97villalobos.issue_reporter.R
import com.mena97villalobos.issue_reporter.model.github.GithubLogin

private const val EMAIL = "USER_NAME"

/**
 * Create a dialog to input an email to attach it to the issue Used when setting authentication type
 * to [EMAIL_OPTIONAL][com.mena97villalobos.issue_reporter.AuthenticationType.EMAIL_OPTIONAL] or
 * [EMAIL_REQUIRED][com.mena97villalobos.issue_reporter.AuthenticationType.EMAIL_REQUIRED]
 *
 * @param context Android context to show the dialog
 * @param required Require the user to input an email
 * @param onEmail Process user input after submission
 */
fun createEmailDialog(context: Context, required: Boolean = false, onEmail: (GithubLogin) -> Unit) {
    InputSheet().show(context) {
        val text = if (required) R.string.air_label_email else R.string.air_label_email_optional
        style(SheetStyle.DIALOG)
        title(if (required) R.string.air_label_use_email else R.string.air_label_use_guest)
        withIconButton(IconButton(R.drawable.ic_github))
        with(InputSeparator())
        with(
            InputEditText(EMAIL) {
                required(required = required)
                label(text)
                hint(text)
                validationListener {
                    ValidationResult(
                        if (required) it.isNotEmpty() else true,
                        context.getString(R.string.air_error_no_email))
                }
            })
        onPositive { onEmail(GithubLogin(username = it.getString(EMAIL, ""))) }
    }
}
