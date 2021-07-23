package com.mena97villalobos.issue_reporter.ui

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.maxkeppeler.sheets.core.IconButton
import com.maxkeppeler.sheets.core.SheetStyle
import com.maxkeppeler.sheets.input.InputSheet
import com.maxkeppeler.sheets.input.ValidationResult
import com.maxkeppeler.sheets.input.type.InputEditText
import com.maxkeppeler.sheets.options.Option
import com.maxkeppeler.sheets.options.OptionsSheet
import com.mena97villalobos.issue_reporter.AuthenticationType
import com.mena97villalobos.issue_reporter.R
import com.mena97villalobos.issue_reporter.model.DeviceInfo
import com.mena97villalobos.issue_reporter.model.Report
import com.mena97villalobos.issue_reporter.model.github.GithubLogin
import com.mena97villalobos.issue_reporter.model.github.GithubTarget
import com.mena97villalobos.issue_reporter.ui.dialogs.createEmailDialog
import com.mena97villalobos.issue_reporter.ui.dialogs.createGithubLoginDialog
import java.io.IOException
import org.eclipse.egit.github.core.Issue
import org.eclipse.egit.github.core.client.GitHubClient
import org.eclipse.egit.github.core.client.RequestException
import org.eclipse.egit.github.core.service.IssueService

/**
 * Helper class to create an issue reporter dialog
 * @param targetUsername username of the target repository owner
 * @param targetRepository target repository to create the issue
 * @param isRepoPrivate Whether a repository is private or not
 * @param deviceInfo Device information to upload it
 */
class DialogLauncher(
    private val targetUsername: String,
    private val targetRepository: String,
    private val isRepoPrivate: Boolean,
    private val deviceInfo: DeviceInfo
) {

    companion object {
        private const val ISSUE_TITLE = "ISSUE_TITLE"
        private const val ISSUE_DESCRIPTION = "ISSUE_DESCRIPTION"

        /**
         * Create an instance of [DialogLauncher] whit all necesary data
         *
         * @param username username of the target repository owner
         * @param repository target repository to create the issue
         * @param isRepoPrivate Whether a repository is private or not
         * @param deviceInfo Device information to upload it
         */
        fun forTarget(
            username: String,
            repository: String,
            isRepoPrivate: Boolean,
            deviceInfo: DeviceInfo
        ): DialogLauncher {
            return DialogLauncher(username, repository, isRepoPrivate, deviceInfo)
        }
    }

    private var token = ""
    private var minDescriptionLength = 0
    private var authenticationType: AuthenticationType = AuthenticationType.GITHUB
    private var success = MutableLiveData<Boolean?>(null)

    /** Sets the github token for authentication in case no GitHub user is provided */
    fun setToken(token: String): DialogLauncher {
        this.token = token
        return this
    }

    /**
     * Sets the minimum length for the issue description
     */
    fun setMinimumDescriptionLenght(minLength: Int): DialogLauncher {
        this.minDescriptionLength
        return this
    }

    /**
     * Configures authentication type to show the right dialogs Checks if a token as been set if the
     * user selects [AuthenticationType.EMAIL_REQUIRED] or [AuthenticationType.EMAIL_OPTIONAL]. Also
     * for private repositories a token must be provided otherwise the creation of the report will
     * fail
     *
     * @param type [AuthenticationType] selected by the user
     */
    fun setAuthenticationType(type: AuthenticationType): DialogLauncher {
        val privateRepoCondition =
            (isRepoPrivate && type == AuthenticationType.GITHUB) && token.isBlank()
        val requiredTokenCondition =
            (type == AuthenticationType.EMAIL_OPTIONAL ||
                type == AuthenticationType.EMAIL_REQUIRED) && token.isBlank()

        if (privateRepoCondition || requiredTokenCondition) {
            throw IllegalStateException("GitHub Token must be define")
        }
        this.authenticationType = type
        return this
    }

    /**
     * Shows the dialogs to authenticate and create an issue after configuring everything
     *
     * @param context Context to show the dialog
     */
    fun launch(context: Context): LiveData<Boolean?> {
        when (authenticationType) {
            AuthenticationType.GITHUB ->
                createGithubLoginDialog(context) { showIssueCreationDialog(context, it) }
            AuthenticationType.EMAIL_OPTIONAL ->
                createEmailDialog(context, required = false) {
                    showIssueCreationDialog(context, it)
                }
            AuthenticationType.EMAIL_REQUIRED ->
                createEmailDialog(context, required = true) { showIssueCreationDialog(context, it) }
            AuthenticationType.SELECTOR -> {
                showSelectorDialog(context)
            }
        }
        return success
    }

    private fun stringValidationListener(context: Context, value: String): ValidationResult =
        ValidationResult(
            value.isNotEmpty() && value.isNotBlank(),
            errorText = context.getString(R.string.valid_value_error_msj))

    private fun showSelectorDialog(context: Context) {
        val options = hashMapOf<Option, () -> Unit>()
        if (!isRepoPrivate) {
            options[Option(R.drawable.ic_github, R.string.air_label_github)] =
                {
                    createGithubLoginDialog(context) { showIssueCreationDialog(context, it) }
                }
        }
        if (token.isNotEmpty()) {
            options[Option(R.drawable.ic_email, R.string.air_label_email)] =
                {
                    createEmailDialog(context, true) { showIssueCreationDialog(context, it) }
                }
            options[Option(R.drawable.ic_anonymous, R.string.air_label_email_optional)] =
                {
                    createEmailDialog(context, false) { showIssueCreationDialog(context, it) }
                }
        }
        OptionsSheet().show(context) {
            title(R.string.select_auth_type)
            with(options.keys.toMutableList())
            onPositive { _: Int, option: Option -> options[option]?.invoke() }
        }
    }

    private fun showIssueCreationDialog(context: Context, authentication: GithubLogin) {
        authentication.apiToken = token
        InputSheet().show(context) {
            style(SheetStyle.DIALOG)
            title(R.string.air_title_report_issue)
            withIconButton(IconButton(R.drawable.ic_issue))
            with(
                InputEditText(ISSUE_TITLE) {
                    required(required = true)
                    label(R.string.air_label_issue_title)
                    hint(R.string.air_label_issue_title)
                    validationListener { stringValidationListener(context, it) }
                })
            with(
                InputEditText(ISSUE_DESCRIPTION) {
                    required(required = true)
                    label(R.string.air_label_issue_description)
                    hint(R.string.air_label_issue_description)
                    validationListener {
                        ValidationResult(
                            it.isNotEmpty() && it.isNotBlank() && it.length >= minDescriptionLength,
                            errorText = context.getString(R.string.valid_value_error_msj))
                    }
                })
            onPositive { reportIssue(it, authentication) }
        }
    }

    private fun reportIssue(result: Bundle, authentication: GithubLogin) {
        val report =
            Report(
                result.getString(ISSUE_TITLE, "Issue"),
                result.getString(ISSUE_DESCRIPTION, "Description problem"),
                deviceInfo,
                authentication.username)

        createIssue(
            IssueService(
                if (authentication.shouldUseApiToken()) {
                    GitHubClient().setOAuth2Token(authentication.apiToken)
                } else {
                    GitHubClient().setCredentials(authentication.username, authentication.password)
                }),
            Issue().setTitle(report.title).setBody(report.getDescription()),
            GithubTarget(targetUsername, targetRepository, isRepoPrivate))
    }

    private fun createIssue(service: IssueService, issue: Issue, target: GithubTarget) {
        Thread {
                Runnable {
                    try {
                        service.createIssue(target.username, target.repository, issue)
                        success.postValue(true)
                    } catch (e: RequestException) {
                        success.postValue(false)
                    } catch (e: IOException) {
                        success.postValue(false)
                    }
                }
            }
            .start()
    }
}
