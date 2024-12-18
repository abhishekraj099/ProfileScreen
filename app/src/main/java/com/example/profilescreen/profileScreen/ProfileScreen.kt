package com.example.profilescreen.profileScreen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import coil.compose.AsyncImage


import androidx.compose.runtime.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = androidx.hilt.navigation.compose.hiltViewModel()
) {
    // Collect state values
    val userProfile by viewModel.userProfile.collectAsState()
    val isEditMode by viewModel.isEditMode.collectAsState()
    val profileImageUri by viewModel.profileImageUri.collectAsState()

    // State for editing
    var editedName by remember { mutableStateOf(userProfile.name) }
    var editedUsername by remember { mutableStateOf(userProfile.username) }
    var editedBio by remember { mutableStateOf(userProfile.bio) }
    var editedCollege by remember { mutableStateOf(userProfile.college) }

    // Image Picker
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewModel.updateProfileImage(it) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile") },
                actions = {
                    IconButton(onClick = {
                        if (isEditMode) {
                            // Save changes
                            viewModel.updateUserProfile(
                                name = editedName,
                                username = editedUsername,
                                bio = editedBio,
                                college = editedCollege
                            )
                        }
                        viewModel.toggleEditMode()
                    }) {
                        Icon(
                            imageVector = if (isEditMode) Icons.Filled.Save else Icons.Filled.Edit,
                            contentDescription = if (isEditMode) "Save" else "Edit Profile"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Image
            Box(modifier = Modifier.size(120.dp)) {
                if (profileImageUri != null) {
                    AsyncImage(
                        model = profileImageUri,
                        contentDescription = "Profile Image",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                    )
                } else if (userProfile.profileImageUrl.isNotEmpty()) {
                    AsyncImage(
                        model = userProfile.profileImageUrl,
                        contentDescription = "Profile Image",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = "Profile Placeholder",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer)
                    )
                }

                if (isEditMode) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .background(MaterialTheme.colorScheme.primary, CircleShape)
                            .clickable { launcher.launch("image/*") }
                            .padding(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "Edit Profile Picture",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Profile Details
            if (isEditMode) {
                // Edit Mode
                OutlinedTextField(
                    value = editedName,
                    onValueChange = { editedName = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = editedUsername,
                    onValueChange = { editedUsername = it },
                    label = { Text("Username") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = editedBio,
                    onValueChange = { editedBio = it },
                    label = { Text("Bio") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = editedCollege,
                    onValueChange = { editedCollege = it },
                    label = { Text("College") },
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                // View Mode
                Text(
                    text = userProfile.name,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "@${userProfile.username}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = userProfile.bio,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = userProfile.college,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { /* Navigate to Hackathons */ },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("My Hackathons")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = { /* Navigate to Projects */ },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("My Projects")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { /* Upgrade to Premium */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Upgrade to Premium")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { /* Logout */ },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Logout")
            }
        }
    }
}