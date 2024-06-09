@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.keyring

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.keyring.ui.theme.KeyRingTheme
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import kotlinx.coroutines.delay
import com.google.gson.Gson


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KeyRingTheme {
                KeyRingApp()
            }
        }
    }
}

@Composable
fun KeyRingApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomePage(navController) }
        composable("welcome") { WelcomePage(navController) }
        composable("chooseMode") { ChooseModePage(navController) }
        composable("keyringMode") { KeyringMode(navController) }
        composable("keyringEditor") { KeyringEditor() }
        composable("randomPasswordGeneratorMode") { RandomPasswordGeneratorMode(navController) }
        composable(
            "generated/{password}",
            arguments = listOf(navArgument("password") { type = NavType.StringType })
        ) { backStackEntry ->
            val password = backStackEntry.arguments?.getString("password") ?: ""
            GeneratedPage(navController = navController, generatedPassword = password)
        }
    }
}

@Composable
fun HomePage(navController: NavHostController, modifier: Modifier = Modifier) {
    LaunchedEffect(Unit) {
        delay(3000)
        navController.navigate("welcome")
    }
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.home_page),
            contentDescription = null,
            modifier = Modifier.size(200.dp) // 根据需要调整大小
        )
    }
}

@Composable
fun WelcomePage(navController: NavHostController, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Welcome",
            fontSize = 70.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 120.dp)
        )
        Text(
            text = "KeyRing",
            fontSize = 70.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.height(50.dp))

        Image(
            painter = painterResource(id = R.drawable.home_page),
            contentDescription = null,
            modifier = Modifier.size(200.dp)
        )
        Spacer(modifier = Modifier.height(80.dp))

        Button(
            onClick = { navController.navigate("chooseMode") },
            modifier = Modifier
                .padding(bottom = 100.dp)
                .size(width = 200.dp, height = 80.dp), // 設置按鈕大小
            shape = RoundedCornerShape(30.dp), // 設置圓角半徑，值越小角越方
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black, // 設置按鈕背景顏色為黑色
                contentColor = Color.White // 設置按鈕文字顏色為白色
            )
        ) {
            Text(
                text = "Start",
                fontSize = 40.sp // 設置按鈕上文字的大小
            )
        }

    }
}

@Composable
fun ChooseModePage(navController: NavHostController, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Choose Mode",
            fontSize = 55.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 120.dp)
        )

        Spacer(modifier = Modifier.height(50.dp))

        Image(
            painter = painterResource(id = R.drawable.big_k),
            contentDescription = null,
            modifier = Modifier
                .size(140.dp)
                .offset(x = (-60).dp)
        )

        Button(
            onClick = { navController.navigate("keyringMode") },
            modifier = Modifier
                .padding(bottom = 20.dp)
                .size(width = 330.dp, height = 120.dp), // 設置按鈕大小
            shape = RoundedCornerShape(30.dp), // 設置圓角半徑，值越小角越方
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black, // 設置按鈕背景顏色為黑色
                contentColor = Color.White // 設置按鈕文字顏色為白色
            )
        ) {
            Text(
                text = "Key Ring",
                fontSize = 30.sp // 設置按鈕上文字的大小
            )
        }

        Image(
            painter = painterResource(id = R.drawable.big_r),
            contentDescription = null,
            modifier = Modifier
                .size(140.dp)
                .offset(x = (60).dp)
        )

        Button(
            onClick = { navController.navigate("randomPasswordGeneratorMode") },
            modifier = Modifier
                .padding(bottom = 80.dp)
                .size(width = 330.dp, height = 120.dp), // 設置按鈕大小
            shape = RoundedCornerShape(30.dp), // 設置圓角半徑，值越小角越方
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black, // 設置按鈕背景顏色為黑色
                contentColor = Color.White // 設置按鈕文字顏色為白色
            )
        ) {
            Text(
                text = "Random Password Generator",
                fontSize = 30.sp, // 設置文字的大小
                textAlign = TextAlign.Center, // 將文字置中對齊
                lineHeight = 40.sp,
                modifier = Modifier.fillMaxWidth() // 確保文字填滿寬度以實現居中對齊
            )
        }
    }
}

@Composable
fun KeyringMode(navController: NavHostController, modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .offset(y = (-50).dp)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier.size(500.dp) // 根据需要调整大小
        ) {
            Image(
                painter = painterResource(id = R.drawable.keyrings),
                contentDescription = null,
                modifier = Modifier.matchParentSize()
            )

            val lightGray = Color(0xFFD3D3D3)
            FloatingActionButton(
                onClick = { navController.navigate("keyringEditor") },
                shape = CircleShape,
                containerColor = lightGray,
                contentColor = Color.White,
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(160.dp)
                    .offset(x = (-3).dp, y = (-110).dp),
            ) {
                Text(
                    text = "+",
                    fontSize = 150.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.offset(y = (-25).dp),
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.BottomCenter),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "Click the plus sign to create your key ring.",
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                lineHeight = 40.sp,
                modifier = Modifier
                    .offset(y = 50.dp)
                    .padding(bottom = 150.dp)
                    .fillMaxWidth()
            )
        }
    }
}

data class KeyringData(
    val id: Int,
    var isOpen: Boolean,
    var accountNumber: String,
    var password: String,
    var isLocked: Boolean = false,
    var isImportant: Boolean = false // 新增一個屬性來表示是否重要
)

@Composable
fun KeyringEditor(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val activity = (context as? Activity)
    val sharedPreferences = context.getSharedPreferences("keyring_prefs", Context.MODE_PRIVATE)
    var rectangles by remember { mutableStateOf(loadRectangles(sharedPreferences)) }
    val maxRectangles = 20
    val gson = Gson()

    fun saveRectangles() {
        val editor = sharedPreferences.edit()
        val json = gson.toJson(rectangles)
        editor.putString("rectangles", json)
        editor.apply()
    }

    fun addRectangle() {
        if (rectangles.size < maxRectangles) {
            rectangles = rectangles + listOf(KeyringData(rectangles.size, false, "", ""))
            saveRectangles()
        }
    }

    fun removeRectangle(index: Int) {
        rectangles = rectangles.toMutableList().apply {
            removeAt(index)
        }
        saveRectangles()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Button(
            onClick = { addRectangle() },
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(y = 25.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            )
        ) {
            Text("Add")
        }

        Button(
            onClick = { activity?.finish() },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(y = 25.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            )
        ) {
            Text("Leave")
        }

        Text(
            text = "Your Key Ring",
            fontSize = 35.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .offset(y = 20.dp)
                .align(Alignment.TopCenter)
                .padding(top = 60.dp)
        )

        LazyColumn(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 150.dp)
        ) {
            items(rectangles.size) { index ->
                val keyring = rectangles[index]
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 165.dp, max = if (keyring.isOpen) 300.dp else 165.dp)
                        .background(Color(0xFFD3D3D3), shape = RoundedCornerShape(30.dp))
                        .padding(vertical = 20.dp)
                        .clickable {
                            rectangles = rectangles.toMutableList().apply {
                                set(index, keyring.copy(isOpen = !keyring.isOpen))
                            }
                            saveRectangles()
                        }
                ) {
                    if (keyring.isOpen) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            TextField(
                                value = keyring.accountNumber,
                                onValueChange = {
                                    rectangles = rectangles.toMutableList().apply {
                                        set(index, keyring.copy(accountNumber = it))
                                    }
                                    saveRectangles()
                                },
                                label = { Text("Account Number") },
                                modifier = Modifier.offset(x = 35.dp) // 往右移動
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            TextField(
                                value = keyring.password,
                                onValueChange = {
                                    rectangles = rectangles.toMutableList().apply {
                                        set(index, keyring.copy(password = it))
                                    }
                                    saveRectangles()
                                },
                                label = { Text("Password") },
                                modifier = Modifier.offset(x = 35.dp) // 往右移動
                            )
                            Spacer(modifier = Modifier.height(32.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .offset(x = 16.dp) // 將整行內容往右移動
                            ) {
                                IconButton(
                                    onClick = { removeRectangle(index) }, // 刪除按鈕
                                    modifier = Modifier
                                        .offset(x = (-40).dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = null,
                                        tint = Color.Red
                                    )
                                }
                                Spacer(modifier = Modifier.width(16.dp))
                                Button(
                                    onClick = {
                                        // Save data logic here
                                        Toast.makeText(context, "Data saved!", Toast.LENGTH_SHORT).show()
                                        // 將盒子設置為鎖定並關閉
                                        rectangles = rectangles.toMutableList().apply {
                                            set(index, keyring.copy(isOpen = false, isLocked = true))
                                        }
                                        saveRectangles()
                                    },
                                    modifier = Modifier
                                        .height(50.dp)
                                        .width(120.dp)
                                        .offset(x = (-10).dp),
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color.Black,
                                        contentColor = Color.White
                                    )
                                ) {
                                    Text(
                                        text = "Save",
                                        fontSize = 20.sp  // 根據需要調整字體大小
                                    )
                                }
                                Spacer(modifier = Modifier.width(16.dp))
                                IconButton(
                                    onClick = {
                                        // 切換星星狀態
                                        rectangles = rectangles.toMutableList().apply {
                                            set(index, keyring.copy(isImportant = !keyring.isImportant))
                                        }
                                        saveRectangles()
                                    },
                                    modifier = Modifier
                                        .offset(x = 16.dp)
                                ) {
                                    Icon(
                                        imageVector = if (keyring.isImportant) Icons.Default.Star else Icons.Default.Star,
                                        contentDescription = null,
                                        tint = if (keyring.isImportant) Color.Red else Color.White
                                    )
                                }
                            }
                        }
                    } else {
                        Image(
                            painter = painterResource(
                                id = if (keyring.isLocked) R.drawable.lock else R.drawable.key_1
                            ),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.size(250.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

fun loadRectangles(sharedPreferences: SharedPreferences): List<KeyringData> {
    val gson = Gson()
    val json = sharedPreferences.getString("rectangles", null)
    val type = object : TypeToken<List<KeyringData>>() {}.type
    return if (json != null) gson.fromJson(json, type) else emptyList()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RandomPasswordGeneratorMode(navController: NavHostController, modifier: Modifier = Modifier) {
    var inputText by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var generatedPassword by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Click the plus sign to create your key ring. Please enter the password length you need below (maximum length 20)",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            lineHeight = 40.sp,
            modifier = Modifier
                .padding(top = 100.dp)
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Box(
            modifier = Modifier.size(370.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.screen),
                contentDescription = null,
                modifier = Modifier.matchParentSize()
            )
            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 60.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = inputText,
                        onValueChange = { newText ->
                            inputText = newText
                            if (newText.isNotEmpty()) {
                                showError = false
                            }
                        },
                        modifier = Modifier
                            .width(240.dp)
                            .height(50.dp),
                        placeholder = { Text("Password length") },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number
                        ),
                        colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            val length = inputText.toIntOrNull()
                            if (length == null || length !in 1..20) {
                                showError = true
                            } else {
                                generatedPassword = generateRandomPassword(length)
                                navController.navigate("generated/$generatedPassword")
                            }
                        },
                        modifier = Modifier
                            .height(50.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Black,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Submit")
                    }
                }
                if (showError) {
                    Text(
                        text = "Please enter a number between 1 and 20",
                        color = Color.Red,
                        modifier = Modifier.padding(top = 7.dp)
                    )
                }
            }
        }

        Image(
            painter = painterResource(id = R.drawable.keyboard),
            contentDescription = null,
            modifier = Modifier.size(240.dp)
        )
    }
}

fun generateRandomPassword(length: Int): String {
    val chars = ('A'..'Z') + ('a'..'z') + ('0'..'9') + listOf('!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '-', '_', '=', '+', '?')
    return (1..length).map { chars.random() }.joinToString("")
}

@Composable
fun GeneratedPage(navController: NavController, modifier: Modifier = Modifier, generatedPassword: String) {
    val clipboardManager = LocalClipboardManager.current

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.iphone),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .scale(1.2f)
        )
        Column(
            modifier = Modifier.align(Alignment.Center)
        ) {
            Box(
                modifier = Modifier
                    .offset(x = 50.dp, y = 20.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .width(300.dp)
                    .height(500.dp)
                    .background(Color.White)
            ) {
                Box(
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .width(280.dp)
                        .height(200.dp)
                        .background(Color.LightGray)
                        .align(Alignment.TopCenter)
                ) {
                    Text(
                        text = generatedPassword,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                // 添加 Copy 按鈕
                Button(
                    onClick = {
                        clipboardManager.setText(AnnotatedString(generatedPassword))
                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                        .offset(y = (-200).dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
                ) {
                    Text(text = "Copy", color = Color.White)
                }

                Column(
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    Spacer(modifier = Modifier.height(50.dp))

                    Image(
                        painter = painterResource(id = R.drawable.big_k),
                        contentDescription = null,
                        modifier = Modifier
                            .size(90.dp)
                            .offset(x = 30.dp, y = 100.dp)
                    )
                    Button(
                        onClick = { navController.navigate("keyringMode") },
                        modifier = Modifier
                            .padding(bottom = 20.dp)
                            .size(width = 230.dp, height = 90.dp) // 設置按鈕大小
                            .offset(y = 100.dp),
                        shape = RoundedCornerShape(20.dp), // 設置圓角半徑，值越小角越方
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Black, // 設置按鈕背景顏色為黑色
                            contentColor = Color.White // 設置按鈕文字顏色為白色
                        )
                    ) {
                        Text(
                            text = "Key Ring",
                            fontSize = 30.sp // 設置按鈕上文字的大小
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(34.dp)) // 添加空隙使按钮和白色矩形分隔开
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Button(
                    onClick = { navController.navigate("randomPasswordGeneratorMode") },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    modifier = Modifier
                        .size(width = 140.dp, height = 70.dp)
                        .offset(x = 5.dp),
                    shape = RoundedCornerShape(14.dp),

                    ) {
                    Text(
                        text = "New",
                        fontSize = 20.sp,
                        color = Color.Black
                    )
                }
                Button(
                    onClick = { navController.navigate("chooseModePage") },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    modifier = Modifier
                        .size(width = 140.dp, height = 70.dp)
                        .offset(x = (-16).dp),
                    shape = RoundedCornerShape(14.dp),
                ) {
                    Text(
                        text = "Leave",
                        fontSize = 20.sp,
                        color = Color.Black
                    )
                }
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun HomePagePreview() {
    KeyRingTheme {
        HomePage(rememberNavController())
    }
}

@Preview(showBackground = true)
@Composable
fun WelcomePagePreview() {
    KeyRingTheme {
        WelcomePage(rememberNavController())
    }
}
