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

private const val GITHUB_USERNAME = "USER_NAME"
private const val GITHUB_PASSWORD = "PASSWORD"

/**
 * Show a dialog with inputs for github login
 *
 * @param context Context to show the dialog
 * @param onCredentialsReady Process user input after submission
 */
fun createGithubLoginDialog(context: Context, onCredentialsReady: (GithubLogin) -> Unit) {
    InputSheet().show(context) {
        style(SheetStyle.DIALOG)
        title(R.string.air_label_use_account)
        withIconButton(IconButton(R.drawable.ic_github))
        with(InputSeparator())
        with(
            InputEditText(GITHUB_USERNAME) {
                required(required = true)
                label(R.string.air_label_username)
                hint(R.string.air_label_username)
                validationListener {
                    ValidationResult(
                        it.isNotEmpty(), context.getString(R.string.air_error_no_username))
                }
            })
        with(
            InputEditText(GITHUB_PASSWORD) {
                required(required = true)
                label(R.string.air_label_password)
                hint(R.string.air_label_password)
                validationListener {
                    ValidationResult(
                        it.isNotEmpty(), context.getString(R.string.air_error_no_password))
                }
            })
        onPositive {
            onCredentialsReady(
                GithubLogin(it.getString(GITHUB_USERNAME, ""), it.getString(GITHUB_PASSWORD, "")))
        }
    }
}
