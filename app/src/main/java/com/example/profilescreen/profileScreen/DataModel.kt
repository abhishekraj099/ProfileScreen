package com.example.profilescreen.profileScreen

import java.util.UUID

data class UserProfile(
    val name: String = "",
    val username: String = "",
    val email: String = "",
    val bio: String = "",
    val college: String = "",
    val profileImageUrl: String = "",
    val hackathons: List<Hackathon> = emptyList(),
    val projects: List<Project> = emptyList(),
    val isPremium: Boolean = false
)

data class Hackathon(
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val description: String = "",
    val date: String = ""
)

data class Project(
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val description: String = "",
    val githubLink: String = ""
)