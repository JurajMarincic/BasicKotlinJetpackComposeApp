package com.example.jurajappjetpack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.jurajappjetpack.ui.theme.JurajAppJetpackTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.material3.Slider
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.findNavController
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment
import kotlin.math.pow
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import kotlinx.coroutines.delay
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.PaintingStyle.Companion.Fill
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.*
import com.google.android.gms.location.LocationServices
import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.lifecycle.ViewModel
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.appcompat.app.AlertDialog
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.material3.Divider
import androidx.navigation.NavType
import androidx.navigation.navArgument
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.room.Room

class MainActivity: ComponentActivity() {



            override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                val database = Room.databaseBuilder(
                    applicationContext,
                    AppDatabase::class.java,
                    "my_database"
                ).build()


                val userRepository = UserRepository(database.userDao())
                setContent {
                    var navController = rememberNavController()

                    NavHost(navController = navController, startDestination = "mainScreen") {
                        composable("mainScreen") { MyApp(navController) }
                        composable(
                            "Screen0/{sliderValue}",
                            arguments = listOf(navArgument("sliderValue") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val sliderValue = backStackEntry.arguments?.getInt("sliderValue") ?: 0
                            UIScreen(navController, sliderValue)
                        }
                        composable(
                            "Screen1/{sliderValue}",
                            arguments = listOf(navArgument("sliderValue") { type = NavType.IntType })
                        ) {
                                backStackEntry ->
                            val sliderValue = backStackEntry.arguments?.getInt("sliderValue") ?: 0
                            AnimationScreen(navController, sliderValue)
                        }
                        composable("Screen2/{sliderValue}",
                            arguments = listOf(navArgument("sliderValue") { type = NavType.IntType })
                        ) {
                                backStackEntry ->
                            val sliderValue = backStackEntry.arguments?.getInt("sliderValue") ?: 0
                            ArithmeticLogicalScreen(navController, sliderValue)
                        }
                        composable("Screen3/{sliderValue}",
                            arguments = listOf(navArgument("sliderValue") { type = NavType.IntType })
                        ) {
                                backStackEntry ->
                            val sliderValue = backStackEntry.arguments?.getInt("sliderValue") ?: 0
                            DatabaseScreen(navController, sliderValue,userRepository)
                        }
                        composable("Screen4") {
                            FeaturesScreen(navController)
                        }
                    }
                }
            }

    @Composable
    fun MyApp(navController: NavController) {
        MainLazyColumn(navController)
    }


    @Composable
    fun MainScreenButton(name: String, navController: NavController, destination: String, sliderValue: Int) {
        Button(
            onClick = {
                navController.navigate("Screen$destination/$sliderValue")
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Run $name test")
        }
    }

    @Composable
    fun MainScreenFeaturesButton(name: String, navController: NavController, destination: String) {
        Button(
            onClick = {
                navController.navigate("Screen$destination")
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Run $name test")
        }
    }


    @Composable
    fun MainLazyColumn(navController: NavController) {
        val categoriesList =
            listOf(
                "UI Scalability",
                "Animation",
                "Arithmetic and Logical Operations",
                "SQLite Database",
                "Native Features"
            )

        val sliderValues = remember { mutableStateMapOf<Int, Float>() }

        fun roundToNearestInt(value: Float): Int {
            return when {
                value < 1.5f -> 1
                value < 2.5f -> 2
                value < 3.5f -> 3
                value < 4.5f -> 4
                else -> 5
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xFF89CFF0)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(categoriesList.size) { index ->
                val initialValue = sliderValues.getOrPut(index) { 3f }

                Text(
                    text = categoriesList[index],
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(50.dp)
                )

                if (index < 4) {
                    Text(
                        text = roundToNearestInt(initialValue).toString(),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Slider(
                        modifier = Modifier.padding(20.dp),
                        value = initialValue,
                        onValueChange = { newValue ->
                            sliderValues[index] = newValue
                        },
                        valueRange = 1f..5f,
                        steps = 3
                    )
                }
                if (categoriesList[index] == "Native Features") {
                    MainScreenFeaturesButton(categoriesList[index], navController, index.toString())
                }
                else {
                    MainScreenButton(
                        categoriesList[index],
                        navController,
                        index.toString(),
                        roundToNearestInt(initialValue)
                    )
                }
                Divider(
                    color = Color.Black,
                    thickness = 3.dp,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }







    @OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
    @Composable
    fun UIScreen(navController: NavController, sliderValue: Int) {

        val maxIterations = 2.0.pow(sliderValue.toDouble()).toInt()

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            for (i in 0 until maxIterations) {
                item {
                    var textValue by remember { mutableStateOf("User $i") } // Initialize textValue

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Button(
                            onClick = {
                            }
                        ) {
                            Text(text = "Button $i")
                        }

                        Text(
                            text = "+",
                            fontSize = 24.sp,
                            modifier = Modifier.clickable {
                            }
                        )

                        Text(
                            text = "Text $i",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )

                        OutlinedTextField(
                            value = textValue,
                            onValueChange = {
                                textValue = it

                            },
                            modifier = Modifier
                                .fillMaxWidth(),

                        )

                        Text(
                            text = "User $i",
                            color = Color.Gray.copy(alpha = 0.5f)
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun AnimationScreen(navController: NavController, sliderValue: Int) {
        val maxRows = 2 * sliderValue
        val circleSize = 38.dp
        val circleSpacing = 8.dp
        val rowSpacing = 40.dp

        val colors = listOf(
            Color.Blue,
            Color.Yellow,
            Color.Red,
            Color.Green,
            Color.Magenta
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xFFF5F5DC)),
            verticalArrangement = Arrangement.spacedBy(rowSpacing),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            repeat(maxRows) { rowIndex ->
                val colorIndex = rowIndex / 2

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    repeat(8) { columnIndex ->
                        val isFlickering = remember { mutableStateOf(false) }
                        val alpha by animateFloatAsState(
                            if (isFlickering.value) 1f else 0f,
                            animationSpec = keyframes {
                                durationMillis = 1000
                                0f at 0
                                1f at 500
                                0f at 1000
                            }
                        )

                        Canvas(
                            modifier = Modifier
                                .size(circleSize)
                                .padding(
                                    start = if (columnIndex == 0) circleSpacing / 2 else 0.dp,
                                    end = if (columnIndex == 9) circleSpacing / 2 else 0.dp
                                ),
                            onDraw = {
                                drawCircle(
                                    color = colors[colorIndex],
                                    radius = circleSize.toPx() / 2,
                                    center = Offset(
                                        x = circleSize.toPx() / 2,
                                        y = circleSize.toPx() / 2
                                    ),
                                    alpha = alpha
                                )
                            }
                        )
                        LaunchedEffect(isFlickering.value) {
                            delay(500)
                            isFlickering.value = !isFlickering.value
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun ArithmeticLogicalScreen(navController: NavController, sliderValue: Int) {
        var value = 0
        val numberOfOperations = 10.0.pow(sliderValue.toDouble()).toInt() * 10000
        val startTime = System.nanoTime()
        for(i in 0 until numberOfOperations){
            value += 2;
            value /= 4;
            value *= 2;
        }
        val endTime = System.nanoTime()
        val executionTime = (endTime - startTime)/1000000

        Column(


            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xFFF5F5DC))
                .padding(horizontal = 90.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "A&L Operations Test",
                color = Color.Black,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .paddingFromBaseline(bottom = 110.dp)
            )

            Text(
                 text = "$numberOfOperations operations of addition, divison and multiplication have been executed!",
                 fontSize = 20.sp,
                 modifier = Modifier
                     .paddingFromBaseline(bottom = 50.dp)
            )

            Text(
                text = "Execution time lasted $executionTime miliseconds.",
                fontSize = 15.sp,
                modifier = Modifier
                    .paddingFromBaseline(bottom = 100.dp)
            )
        }
    }

    @Composable
    fun DatabaseScreen(
        navController: NavController,
        sliderValue: Int,
        userRepository: UserRepository
    ) {
        var resultText by remember { mutableStateOf("") }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xFFF5F5DC)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Database Test",
                color = Color.Black,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(50.dp)
            )

            Text(
                text = resultText,
                modifier = Modifier.padding(24.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    CoroutineScope(Dispatchers.IO).launch {

                        val userSize = 10.0.pow(sliderValue.toDouble()).toInt()

                        val startTime = System.nanoTime()

                        for (i in 0 until userSize) {
                            userRepository.insertUser(User(age = i % 100))
                        }

                        val endTime = System.nanoTime()
                        val executionTime = (endTime - startTime)/1000000

                        withContext(Dispatchers.Main) {
                            resultText = "Added $userSize users in $executionTime miliseconds!"
                        }
                    }
                }
            ) {
                Text(text = "Save and load users")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                    val count = userRepository.getAllUsers().size
                    resultText = "Current user count: $count"
                }
                }
            ) {
                Text(text = "Show user count")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                    userRepository.clearDatabase()
                    resultText = "Database cleared!"
                }
                }
            ) {
                Text(text = "Clear database")
            }
        }
    }


    @Entity(tableName = "user")
    data class User(
        @PrimaryKey(autoGenerate = true) val id: Long = 0,
        val age: Int
    )

    @Database(entities = [User::class], version = 1)
    abstract class AppDatabase : RoomDatabase() {
        abstract fun userDao(): UserDao
    }

    @Dao
    interface UserDao {
        @Insert
        suspend fun insertUser(user: User)

        @Query("SELECT * FROM user")
        suspend fun getAllUsers(): List<User>

        @Query("DELETE FROM user")
        suspend fun clearDatabase()
    }

    class UserRepository(private val userDao: UserDao) {
        suspend fun insertUser(user: User) {
            userDao.insertUser(user)
        }

        suspend fun getAllUsers(): List<User> {
            return userDao.getAllUsers()
        }

        suspend fun clearDatabase() {
            userDao.clearDatabase()
        }
    }

    @Composable
    fun FeaturesScreen(navController: NavController) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xFFF5F5DC))
        )
        {
            Text(
                text = "Features Test",
                color = Color.Black,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(50.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(40.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StartGPSButton()
                StartCameraButton()
            }
        }
    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun StartGPSButton() {
        val activity = LocalContext.current as ComponentActivity

        val requestPermissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                startGPSFunctionality(activity)
            } else {
                Toast.makeText(activity, "Permission denied.", Toast.LENGTH_SHORT).show()
            }
        }

        Button(
            onClick = {
                if (hasLocationPermission(activity)) {
                    startGPSFunctionality(activity)
                } else {
                    requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }
            },
            modifier = Modifier.wrapContentWidth()
        ) {
            Text(text = "Start GPS")
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun StartCameraButton() {
        val activity = LocalContext.current as ComponentActivity

        val requestPermissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                startCameraFunctionality(activity)
            } else {
                Toast.makeText(activity, "Permission denied.", Toast.LENGTH_SHORT).show()
            }
        }

        Button(
            onClick = {
                if (hasCameraPermission(activity)) {
                    startCameraFunctionality(activity)
                } else {
                    requestPermissionLauncher.launch(Manifest.permission.CAMERA)
                }
            },
            modifier = Modifier.wrapContentWidth()
        ) {
            Text(text = "Start Camera")
        }
    }

    private fun hasLocationPermission(context: ComponentActivity): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun startGPSFunctionality(context: ComponentActivity) {
        Toast.makeText(context, "GPS functionality started.", Toast.LENGTH_SHORT).show()
    }

    private fun hasCameraPermission(context: ComponentActivity): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun startCameraFunctionality(context: ComponentActivity) {
        Toast.makeText(context, "Camera functionality started.", Toast.LENGTH_SHORT).show()
    }
}
