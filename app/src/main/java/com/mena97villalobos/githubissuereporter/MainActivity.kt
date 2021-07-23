package com.mena97villalobos.githubissuereporter

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.mena97villalobos.issue_reporter.AuthenticationType
import com.mena97villalobos.issue_reporter.model.DeviceInfo
import com.mena97villalobos.issue_reporter.ui.DialogLauncher

class MainActivity : AppCompatActivity() {

    private lateinit var selector: Button
    private lateinit var github: Button
    private lateinit var email: Button
    private lateinit var emailOptional: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        selector = findViewById(R.id.selector)
        github = findViewById(R.id.github)
        email = findViewById(R.id.email)
        emailOptional = findViewById(R.id.email_optional)

        setupOnClickListeners()
    }

    private fun setupOnClickListeners() {
        selector.setOnClickListener { openSelector() }
        github.setOnClickListener { openGithub() }
        email.setOnClickListener { openEmail() }
        emailOptional.setOnClickListener { openEmailOptional() }
    }

    private fun openSelector() {
        DialogLauncher.forTarget("mena97villalobos", "GithubIssueReporter", false, DeviceInfo(this))
            .setToken("YOUR-TOKEN")
            .setAuthenticationType(AuthenticationType.SELECTOR)
            .setMinimumDescriptionLenght(10)
            .launch(this)
    }

    private fun openGithub() {
        DialogLauncher.forTarget("mena97villalobos", "GithubIssueReporter", false, DeviceInfo(this))
            .setToken("YOUR-TOKEN")
            .setAuthenticationType(AuthenticationType.GITHUB)
            .setMinimumDescriptionLenght(10)
            .launch(this)
    }

    private fun openEmail() {
        DialogLauncher.forTarget("mena97villalobos", "GithubIssueReporter", false, DeviceInfo(this))
            .setToken("YOUR-TOKEN")
            .setAuthenticationType(AuthenticationType.EMAIL_REQUIRED)
            .setMinimumDescriptionLenght(10)
            .launch(this)
    }

    private fun openEmailOptional() {
        DialogLauncher.forTarget("mena97villalobos", "GithubIssueReporter", false, DeviceInfo(this))
            .setToken("YOUR-TOKEN")
            .setAuthenticationType(AuthenticationType.EMAIL_OPTIONAL)
            .setMinimumDescriptionLenght(10)
            .launch(this)
    }
}