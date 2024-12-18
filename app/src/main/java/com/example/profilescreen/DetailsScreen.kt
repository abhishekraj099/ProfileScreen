package com.example.profilescreen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val database: FirebaseDatabase
) : ViewModel() {
    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name.asStateFlow()

    private val _age = MutableStateFlow("")
    val age: StateFlow<String> = _age.asStateFlow()

    private val _phoneNumber = MutableStateFlow("")
    val phoneNumber: StateFlow<String> = _phoneNumber.asStateFlow()

    private val _saveState = MutableStateFlow<SaveState>(SaveState.Idle)
    val saveState: StateFlow<SaveState> = _saveState.asStateFlow()

    fun updateName(newName: String) {
        _name.value = newName
    }

    fun updateAge(newAge: String) {
        _age.value = newAge
    }

    fun updatePhoneNumber(newPhoneNumber: String) {
        _phoneNumber.value = newPhoneNumber
    }

    fun saveUserDetails() {
        viewModelScope.launch {
            val currentUser = auth.currentUser
            if (currentUser == null) {
                _saveState.value = SaveState.Error("User not authenticated")
                return@launch
            }

            // Basic validation
            if (name.value.isBlank()) {
                _saveState.value = SaveState.Error("Name cannot be empty")
                return@launch
            }

            if (age.value.isBlank() || !age.value.all { it.isDigit() }) {
                _saveState.value = SaveState.Error("Please enter a valid age")
                return@launch
            }

            if (phoneNumber.value.isBlank() || !phoneNumber.value.matches(Regex("^[0-9]{10}$"))) {
                _saveState.value = SaveState.Error("Please enter a valid 10-digit phone number")
                return@launch
            }

            _saveState.value = SaveState.Loading

            val userMap = mapOf(
                "name" to name.value,
                "age" to age.value,
                "phone" to phoneNumber.value
            )

            try {
                database.reference.child("users").child(currentUser.uid)
                    .setValue(userMap)
                    .addOnSuccessListener {
                        _saveState.value = SaveState.Success
                    }
                    .addOnFailureListener { exception ->
                        _saveState.value = SaveState.Error(exception.message ?: "Failed to save details")
                    }
            } catch (e: Exception) {
                _saveState.value = SaveState.Error(e.message ?: "An unexpected error occurred")
            }
        }
    }
}

// Sealed class for managing save state
sealed class SaveState {
    object Idle : SaveState()
    object Loading : SaveState()
    object Success : SaveState()
    data class Error(val message: String) : SaveState()
}

@Composable
fun DetailsScreen(
    navController: NavController,
    viewModel: DetailsViewModel = hiltViewModel()
) {
    val name by viewModel.name.collectAsState()
    val age by viewModel.age.collectAsState()
    val phoneNumber by viewModel.phoneNumber.collectAsState()
    val saveState by viewModel.saveState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(saveState) {
        when (saveState) {
            is SaveState.Success -> {
                navController.navigate(AppRoute.Home.route) {
                    popUpTo(AppRoute.Details.route) { inclusive = true }
                }
            }
            is SaveState.Error -> {
                val errorMessage = (saveState as SaveState.Error).message
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = name,
            onValueChange = { viewModel.updateName(it) },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = age,
            onValueChange = { viewModel.updateAge(it) },
            label = { Text("Age") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = phoneNumber,
            onValueChange = { viewModel.updatePhoneNumber(it) },
            label = { Text("Phone Number") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { viewModel.saveUserDetails() },
            modifier = Modifier.fillMaxWidth(),
            enabled = saveState != SaveState.Loading
        ) {
            if (saveState == SaveState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("Save and Continue")
            }
        }
    }
}