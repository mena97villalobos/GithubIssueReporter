package com.mena97villalobos.issue_reporter.model.github

/**
 * Sets the target repository to create issues If using a private repository only way to create
 * issues is with a token Using a public record allows to use email and optional email
 *
 * @param username Owner of target repository
 *
 * @param repository Target repository to create the issue
 *
 * @param isPrivate Whether a repository is private or not
 */
data class GithubTarget(
    val username: String = "",
    val repository: String = "",
    val isPrivate: Boolean
)
