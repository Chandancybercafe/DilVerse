package com.twogether.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Ink = Color(0xFF160E25)
private val Card = Color(0xFF241733)
private val Pink = Color(0xFFFF72B6)
private val Lavender = Color(0xFFBCA3FF)
private val Muted = Color(0xFFB9A9CA)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { TwoTogetherApp() }
    }
}

@Composable
fun TwoTogetherApp() {
    var page by remember { mutableStateOf("login") }
    var loggedIn by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var mood by remember { mutableStateOf("🥰 Loved") }
    var partnerCode by remember { mutableStateOf("") }
    var tab by remember { mutableStateOf("Home") }
    MaterialTheme(colorScheme = darkColorScheme(primary=Pink, secondary=Lavender, background=Ink, surface=Card)) {
        Surface(color=Ink, modifier=Modifier.fillMaxSize()) {
            if (!loggedIn) {
                AuthScreen(page, { page=it }, name, {name=it}, email, {email=it}, password, {password=it}) {
                    loggedIn = true
                }
            } else {
                HomeScreen(tab, {tab=it}, mood, {mood=it}, partnerCode, {partnerCode=it}) { loggedIn=false; page="login" }
            }
        }
    }
}

@Composable
fun AuthScreen(page:String, setPage:(String)->Unit, name:String, setName:(String)->Unit, email:String, setEmail:(String)->Unit, password:String, setPassword:(String)->Unit, onContinue:()->Unit) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(24.dp), horizontalAlignment=Alignment.CenterHorizontally, verticalArrangement=Arrangement.Center) {
        Text("♡", fontSize=64.sp, color=Pink, fontWeight=FontWeight.Black)
        Text("TwoTogether", fontSize=32.sp, fontWeight=FontWeight.Bold, color=Color.White)
        Text("two hearts, one little universe", color=Muted)
        Spacer(Modifier.height(28.dp))
        Card(colors=CardDefaults.cardColors(containerColor=Card), shape=RoundedCornerShape(28.dp), modifier=Modifier.fillMaxWidth()) {
            Column(Modifier.padding(22.dp), verticalArrangement=Arrangement.spacedBy(14.dp)) {
                Text(if(page=="login") "Welcome back 💗" else "Create your account ✨", color=Color.White, fontSize=23.sp, fontWeight=FontWeight.Bold)
                Text(if(page=="login") "Sign in to your shared universe." else "Start your own little universe.", color=Muted)
                if(page=="signup") Field("Your name", name, setName)
                Field("Email address", email, setEmail)
                OutlinedTextField(password, setPassword, label={Text("Password")}, visualTransformation=PasswordVisualTransformation(), modifier=Modifier.fillMaxWidth(), shape=RoundedCornerShape(16.dp), colors=authColors())
                Button(onClick=onContinue, modifier=Modifier.fillMaxWidth().height(52.dp), shape=RoundedCornerShape(16.dp)) { Text(if(page=="login") "Sign in ♡" else "Create account ♡") }
                TextButton(onClick={setPage(if(page=="login") "signup" else "login")}, modifier=Modifier.align(Alignment.CenterHorizontally)) {
                    Text(if(page=="login") "New here? Create account" else "Already have an account? Sign in", color=Lavender)
                }
                Text("Demo mode • Authentication will connect to Firebase in the next setup step", color=Muted, fontSize=11.sp)
            }
        }
        Spacer(Modifier.height(18.dp))
        Text("Made by Yuvie ♡", color=Lavender, fontSize=12.sp, fontWeight=FontWeight.Medium)
    }
}

@Composable
fun authColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor=Pink, unfocusedBorderColor=Muted, focusedLabelColor=Pink,
    unfocusedLabelColor=Muted, focusedTextColor=Color.White, unfocusedTextColor=Color.White,
    cursorColor=Pink
)

@Composable
fun Field(label:String, value:String, change:(String)->Unit) {
    OutlinedTextField(value, change, label={Text(label)}, modifier=Modifier.fillMaxWidth(), shape=RoundedCornerShape(16.dp), colors=authColors())
}

@Composable
fun HomeScreen(tab:String, setTab:(String)->Unit, mood:String, setMood:(String)->Unit, code:String, setCode:(String)->Unit, logout:()->Unit) {
    Column(Modifier.fillMaxSize()) {
        Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(20.dp), verticalArrangement=Arrangement.spacedBy(16.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement=Arrangement.SpaceBetween, verticalAlignment=Alignment.CenterVertically) {
                Column { Text("YOUR LITTLE UNIVERSE", color=Lavender, fontSize=11.sp); Text("Hey, lovebirds ♡", color=Color.White, fontSize=26.sp, fontWeight=FontWeight.Bold) }
                IconButton(onClick=logout) { Icon(Icons.Default.Logout, "Log out", tint=Muted) }
            }
            Card(colors=CardDefaults.cardColors(containerColor=Card), shape=RoundedCornerShape(24.dp), modifier=Modifier.fillMaxWidth()) {
                Column(Modifier.padding(18.dp), verticalArrangement=Arrangement.spacedBy(8.dp)) {
                    Text("♡", color=Pink, fontSize=34.sp)
                    Text("Your story, together.", color=Color.White, fontSize=20.sp, fontWeight=FontWeight.Bold)
                    Text("Every little moment deserves a place.", color=Muted)
                    Text("💗 0 days together", color=Lavender)
                }
            }
            SectionTitle("How are you feeling?")
            Row(horizontalArrangement=Arrangement.spacedBy(8.dp)) { listOf("🥰 Loved","😊 Happy","😔 Low","😴 Tired").forEach { m -> FilterChip(selected=mood==m, onClick={setMood(m)}, label={Text(m)}, colors=FilterChipDefaults.filterChipColors(selectedContainerColor=Pink, selectedLabelColor=Ink)) } }
            FeatureCard("💬","Private Chat","Messages, photos, voice notes")
            FeatureCard("📸","Stories","Share today's little moments")
            FeatureCard("🎮","Play Together","Mini games & couple quizzes")
            FeatureCard("🌍","Dream Destinations","Places you want to visit together")
            FeatureCard("🎞️","Memories","Your shared timeline & albums")
            FeatureCard("💌","Love Letters","Notes for now or the future")
            FeatureCard("📞","Calls","Voice and video calling setup")
            SectionTitle("Connect your partner")
            OutlinedTextField(code,setCode,label={Text("Partner invite code")},modifier=Modifier.fillMaxWidth(),colors=authColors())
            Button(onClick={},modifier=Modifier.fillMaxWidth()) { Text("Connect (demo)") }
            Text("Prototype preview: these cards are UI placeholders. Cloud sync, calls, chat, uploads and invite pairing need backend credentials and services.", color=Muted, fontSize=12.sp)
        }
        Text("Made by Yuvie ♡", color=Lavender, fontSize=12.sp, modifier=Modifier.fillMaxWidth().padding(vertical=8.dp), textAlign=androidx.compose.ui.text.style.TextAlign.Center)
        NavigationBar(containerColor=Card) {
            listOf("Home","Chat","Play","Moments","Us").forEach { item ->
                NavigationBarItem(selected=tab==item, onClick={setTab(item)}, icon={Icon(when(item) { "Home"->Icons.Default.Home; "Chat"->Icons.Default.Chat; "Play"->Icons.Default.SportsEsports; "Moments"->Icons.Default.PhotoCamera; else->Icons.Default.Favorite }, item)}, label={Text(item)})
            }
        }
    }
}

@Composable
fun SectionTitle(title:String) { Text(title, color=Color.White, fontSize=18.sp, fontWeight=FontWeight.Bold) }

@Composable
fun FeatureCard(icon:String, title:String, subtitle:String) {
    Card(colors=CardDefaults.cardColors(containerColor=Card), shape=RoundedCornerShape(20.dp), modifier=Modifier.fillMaxWidth()) {
        Row(Modifier.padding(16.dp), verticalAlignment=Alignment.CenterVertically, horizontalArrangement=Arrangement.spacedBy(14.dp)) {
            Text(icon, fontSize=28.sp)
            Column { Text(title, color=Color.White, fontWeight=FontWeight.SemiBold); Text(subtitle, color=Muted, fontSize=12.sp) }
            Spacer(Modifier.weight(1f))
            Icon(Icons.Default.ChevronRight, null, tint=Lavender)
        }
    }
}