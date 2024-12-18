package com.example.profilescreen.profileScreen

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val database: FirebaseDatabase,
    private val storage: FirebaseStorage
) : ViewModel() {
    private val _userProfile = MutableStateFlow(UserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    private val _isEditMode = MutableStateFlow(false)
    val isEditMode: StateFlow<Boolean> = _isEditMode.asStateFlow()

    private val _profileImageUri = MutableStateFlow<Uri?>(null)
    val profileImageUri: StateFlow<Uri?> = _profileImageUri.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        fetchUserProfile()
    }

    fun toggleEditMode() {
        _isEditMode.value = !_isEditMode.value
    }

    fun updateProfileImage(uri: Uri) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val userId = auth.currentUser?.uid
                    ?: throw IllegalStateException("User not authenticated")

                // Upload image to Firebase Storage
                val storageRef = storage.reference.child("profile_images/$userId")
                val uploadTask = storageRef.putFile(uri).await()

                // Get download URL
                val downloadUrl = uploadTask.storage.downloadUrl.await().toString()

                // Update profile in Realtime Database
                val userRef = database.reference.child("users").child(userId)
                userRef.child("profileImageUrl").setValue(downloadUrl).await()

                // Update local state
                _userProfile.update { it.copy(profileImageUrl = downloadUrl) }
                _profileImageUri.value = uri
            } catch (e: Exception) {
                // Handle error
                Log.e("ProfileViewModel", "Error uploading image", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateUserProfile(
        name: String,
        username: String,
        bio: String,
        college: String
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val userId = auth.currentUser?.uid
                    ?: throw IllegalStateException("User not authenticated")

                // Prepare updated profile data
                val updatedProfile = _userProfile.value.copy(
                    name = name,
                    username = username,
                    bio = bio,
                    college = college
                )

                // Update in Realtime Database
                val userRef = database.reference.child("users").child(userId)
                userRef.setValue(updatedProfile).await()

                // Update local state
                _userProfile.value = updatedProfile
                _isEditMode.value = false
            } catch (e: Exception) {
                // Handle error
                Log.e("ProfileViewModel", "Error updating profile", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun fetchUserProfile() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val userId = auth.currentUser?.uid
                    ?: throw IllegalStateException("User not authenticated")

                val snapshot = database.reference.child("users").child(userId).get().await()

                val profile = snapshot.getValue(UserProfile::class.java)
                    ?: UserProfile(email = auth.currentUser?.email ?: "")

                _userProfile.value = profile
            } catch (e: Exception) {
                // Handle error
                Log.e("ProfileViewModel", "Error fetching profile", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addHackathon(hackathon: Hackathon) {
        viewModelScope.launch {
            val userId = auth.currentUser?.uid ?: return@launch
            val currentHackathons = _userProfile.value.hackathons.toMutableList()
            currentHackathons.add(hackathon)

            database.reference.child("users").child(userId)
                .child("hackathons")
                .setValue(currentHackathons)
        }
    }

    fun addProject(project: Project) {
        viewModelScope.launch {
            val userId = auth.currentUser?.uid ?: return@launch
            val currentProjects = _userProfile.value.projects.toMutableList()
            currentProjects.add(project)

            database.reference.child("users").child(userId)
                .child("projects")
                .setValue(currentProjects)
        }
    }

    fun upgradeToPremium() {
        viewModelScope.launch {
            val userId = auth.currentUser?.uid ?: return@launch

            database.reference.child("users").child(userId)
                .child("isPremium")
                .setValue(true)
        }
    }
}