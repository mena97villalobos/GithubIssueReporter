package com.mena97villalobos.issue_reporter.model.github

import android.text.TextUtils

/**
 * Stores credentials and token for authentication on Github
 *
 * @param username GitHub username
 *
 * @param password GitHub password
 *
 * @param apiToken GitHub token. For more information see
 * [Creating a personal access token](https://docs.github.com/en/github/authenticating-to-github/keeping-your-account-and-data-secure/creating-a-personal-access-token)
 */
data class GithubLogin(
    val username: String = "",
    val password: String = "",
    var apiToken: String = ""
) {

    /**
     * @return Authentication process should be done using the token or user and password provided
     */
    fun shouldUseApiToken(): Boolean {
        return TextUtils.isEmpty(username) || TextUtils.isEmpty(password)
    }
}
