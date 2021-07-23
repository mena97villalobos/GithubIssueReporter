package com.mena97villalobos.issue_reporter

/**
 * Which type of authentication method should be presented to the user
 *
 * - [SELECTOR] shows a dialog to select which of the available options to show
 *
 * - [GITHUB] shows a dialog to input an username and password to authenticate via GitHub
 *
 * - [EMAIL_OPTIONAL] attaches an optional email to the issue
 *
 * - [EMAIL_REQUIRED] forces the user to provide an email and attach it to the issue report
 */
enum class AuthenticationType {
    SELECTOR,
    GITHUB,
    EMAIL_OPTIONAL,
    EMAIL_REQUIRED
}
