package com.example.profilescreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Sports
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn

import androidx.compose.foundation.lazy.items

import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen() {
    // Sample data for explore screen
    val exploreItems = listOf(
        ExploreItem("Technology", Icons.Filled.Computer),
        ExploreItem("Travel", Icons.Filled.Flight),
        ExploreItem("Food", Icons.Filled.Restaurant),
        ExploreItem("Music", Icons.Filled.LibraryMusic),
        ExploreItem("Sports", Icons.Filled.Sports),
        ExploreItem("Art", Icons.Filled.Palette),
        ExploreItem("Science", Icons.Filled.Science),
        ExploreItem("Fashion", Icons.Filled.Palette)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Explore") },
                navigationIcon = {
                    IconButton(onClick = { /* Handle navigation */ }) {
                        Icon(Icons.Filled.Menu, contentDescription = "Menu")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = paddingValues,
            modifier = Modifier.padding(8.dp)
        ) {
            items(exploreItems) { item ->
                ExploreItemCard(item)
            }
        }
    }
}

@Composable
fun ExploreItemCard(item: ExploreItem) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = item.title,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = item.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

data class ExploreItem(val title: String, val icon: ImageVector)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityScreen() {
    // Sample community chat rooms or groups
    val communityRooms = listOf(
        CommunityRoom("General Discussion", 250),
        CommunityRoom("Tech Talk", 150),
        CommunityRoom("Music Lovers", 100),
        CommunityRoom("Book Club", 75),
        CommunityRoom("Fitness Group", 200),
        CommunityRoom("Travel Enthusiasts", 120)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Community") },
                actions = {
                    IconButton(onClick = { /* Handle search */ }) {
                        Icon(Icons.Filled.Search, contentDescription = "Search")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { /* Handle new chat */ }) {
                Icon(Icons.Filled.Add, contentDescription = "New Chat")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier.padding(8.dp)
        ) {
            items(communityRooms) { room ->
                CommunityRoomItem(room)
            }
        }
    }
}

@Composable
fun CommunityRoomItem(room: CommunityRoom) {
    Card(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = room.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${room.memberCount} members",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            IconButton(onClick = { /* Handle join room */ }) {
                Icon(Icons.Filled.ChatBubble, contentDescription = "Join Room")
            }
        }
    }
}

data class CommunityRoom(val name: String, val memberCount: Int)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen() {
    var isEditMode by remember { mutableStateOf(false) }

    val userProfile = remember {
        mutableStateOf(
            UserProfile(
                name = "John Doe",
                email = "johndoe@example.com",
                bio = "Tech enthusiast and traveler",
                followers = 1234,
                following = 567
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile") },
                actions = {
                    IconButton(onClick = { isEditMode = !isEditMode }) {
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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Picture
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = "Profile Picture",
                modifier = Modifier.size(100.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // User Details
            if (isEditMode) {
                // Edit Mode
                OutlinedTextField(
                    value = userProfile.value.name,
                    onValueChange = {
                        userProfile.value = userProfile.value.copy(name = it)
                    },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = userProfile.value.bio,
                    onValueChange = {
                        userProfile.value = userProfile.value.copy(bio = it)
                    },
                    label = { Text("Bio") },
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                // View Mode
                Text(
                    text = userProfile.value.name,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = userProfile.value.bio,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Social Stats
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${userProfile.value.followers}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text("Followers")
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${userProfile.value.following}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text("Following")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action Buttons
            Button(
                onClick = { /* Handle logout */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Logout")
            }
        }
    }
}

data class UserProfile(
    val name: String,
    val email: String,
    val bio: String,
    val followers: Int,
    val following: Int
)
