package com.example.weatherapp

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField

import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import com.example.weatherapp.API.NetworkResponse
import com.example.weatherapp.API.WeatherModel
import com.example.weatherapp.ui.theme.WeatherAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherPage(weatherViewModel: WeatherViewModel){


    var city by remember { mutableStateOf("") }
    val text_color = ContextCompat.getColor(LocalContext.current, R.color.text_color)
    val heading_color = ContextCompat.getColor(LocalContext.current, R.color.heading)
    val black_color  = ContextCompat.getColor(LocalContext.current, R.color.black)
    var isFocused by remember { mutableStateOf(false) }
    val weatherResult =weatherViewModel.weatherResult.observeAsState()
    val keyboardController = LocalSoftwareKeyboardController.current

    Column (
        modifier = Modifier
            .background(colorResource(id = R.color.background)) // Background first
            .fillMaxSize()
    ){
        Row (

        ){
            OutlinedTextField(
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done), // Set IME action to "Done"
                keyboardActions = KeyboardActions(
                    onDone = {
                        // Action to perform when "Done" is pressed
                        // You can add other actions here, like processing the text
                        keyboardController?.hide()
                        weatherViewModel.getData(city)

                    }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .onFocusChanged {
                        isFocused = it.isFocused
                    },
                value = city,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(text_color),
                    unfocusedContainerColor = Color(text_color),
                    focusedIndicatorColor = Color(text_color),
                    unfocusedIndicatorColor = Color(text_color),
                    focusedLabelColor = Color(text_color),
                    cursorColor = Color(black_color)
                ),

                onValueChange ={
                    city=it
                },
                label = {
                    Row() {
                        if(!isFocused) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "search icon"
                            )
                            Text(
                                text = "Search for City",
                            )
                        }

                    }
                },
                shape = RoundedCornerShape(
                    topStart = 20.dp,
                    topEnd = 20.dp,
                    bottomStart = 20.dp,
                    bottomEnd = 25.dp
                ),

                )

        }
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            //WeatherDetailPreview()
            when (val result = weatherResult.value) {
                is NetworkResponse.Success -> {
                    WeatherDetail(heading_color, result.data)
                }

                is NetworkResponse.Error -> {
                    Text(text = result.message)
                }

                NetworkResponse.Loading -> {
                    CircularProgressIndicator()
                }

                null -> {
                    Text(text = "error occured")
                }
            }
        }

    }
}

@Composable
fun WeatherDetailPreview() {
    val headingColor = ContextCompat.getColor(LocalContext.current, R.color.heading)
    Text(
        "Chennai",
        fontSize = 25.sp,
        color = Color.White,
        fontStyle = FontStyle.Normal,
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
    )
    Spacer(modifier = Modifier.height(1.dp))
    Text(
        "Partly Cloudy",
        fontSize = 12.sp,
        color = Color(color=headingColor),
        fontStyle = FontStyle.Normal,
        fontFamily = FontFamily.Default,

        )
    Spacer(modifier = Modifier.height(20.dp))
    Image(imageVector = Icons.Default.ShoppingCart, contentDescription = "", modifier = Modifier.size(150.dp))
    Spacer(modifier = Modifier.height(20.dp))

    Row{
        Text(
            "45",
            fontSize = 40.sp,
            color = Color.White,
            fontStyle = FontStyle.Normal,
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.ExtraBold,
            )

        Image(
            painter = painterResource(id = R.drawable.baseline_arrow_circle_up_24),
            contentDescription = "weather image",
            modifier = Modifier.size(10.dp)
        )
    }
    Spacer(modifier = Modifier.height(50.dp))
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 30.dp, end = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,

        ){
    Column (
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(color = colorResource(id = R.color.body_background_color))
            .fillMaxWidth()

    ) {
        Text(
            text = "AIR CONDITIONS",
            color = Color(headingColor),
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 15.dp, top = 15.dp, bottom = 15.dp)
        )

        LazyColumn (
            modifier = Modifier.wrapContentSize()
        ){
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 10.dp) // This makes the first column take all available space

                    ) {
                        Text(
                            text = "Wind",
                            color = Color(headingColor),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            text = "12 Km/h",
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                    Column (
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 10.dp),
                        horizontalAlignment = Alignment.End
                    ){
                        Text(
                            text = "Dew Point",
                            color = Color(headingColor),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            text = "45 °C",
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
                Spacer(modifier =Modifier.height(10.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, bottom = 25.dp, end = 10.dp)
                ) {
                    Column (
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 10.dp)

                        ){
                        Text(
                            text = "Humidity",
                            color = Color(headingColor),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            text = "12%",
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                    Column (
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 10.dp),
                        horizontalAlignment = Alignment.End
                    ){
                        Text(
                            text = "Cloud Cover",
                            color = Color(headingColor),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            text = "34%",
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            }
        }
    }
    }
}

@Composable
fun WeatherDetail(headingColor:Int, data:WeatherModel){

    Text(
        data.location.name,
        fontSize = 25.sp,
        color = Color.White,
        fontStyle = FontStyle.Normal,
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
    )
    Spacer(modifier = Modifier.height(1.dp))
    Text(
        data.current.condition.text,
        fontSize = 12.sp,
        color = Color(color=headingColor),
        fontStyle = FontStyle.Normal,
        fontFamily = FontFamily.Default,

        )
    Spacer(modifier = Modifier.height(20.dp))
    AsyncImage(
        modifier = Modifier.size(160.dp),
        model = "https:${data.current.condition.icon}".replace("64x64","128x128"),
        contentDescription = "icon",
    )
    Spacer(modifier = Modifier.height(20.dp))

    Row{
        Text(
            data.current.temp_c.toString(),
            fontSize = 40.sp,
            color = Color.White,
            fontStyle = FontStyle.Normal,
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.ExtraBold,
            )

        Image(
            painter = painterResource(id = R.drawable.baseline_arrow_circle_up_24),
            contentDescription = "weather image",
            modifier = Modifier.size(10.dp)
        )
    }
    Spacer(modifier = Modifier.height(50.dp))

    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 30.dp, end = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(color = colorResource(id = R.color.body_background_color))
                .fillMaxWidth()

        ) {
            Text(
                text = "AIR CONDITIONS",
                color = Color(headingColor),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 15.dp, top = 15.dp, bottom = 15.dp)
            )
            LazyColumn (
                modifier = Modifier.wrapContentSize()
            ) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    ) {
                        Column (
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 10.dp) // This makes the first column take all available space
                        ){
                            Text(
                                text = "Real Feel",
                                color = Color(headingColor),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                            )
                            Text(
                                text = "${data.current.feelslike_c} °C",
                                color = Color.White,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                        Column (
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 10.dp),
                            horizontalAlignment = Alignment.End
                        ){
                            Text(
                                text = "Wind",
                                color = Color(headingColor),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                            )
                            Text(
                                text = "${data.current.wind_kph} Km/h",
                                color = Color.White,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    }
                    Spacer(modifier =Modifier.height(10.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp, bottom = 25.dp, end = 10.dp)

                    ) {
                        Column (
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 10.dp),                            ){
                            Text(
                                text = "Humidity",
                                color = Color(headingColor),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                            )
                            Text(
                                text = "${data.current.humidity}%",
                                color = Color.White,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 10.dp),
                            horizontalAlignment = Alignment.End

                        ) {
                            Text(
                                text = "UV Index",
                                color = Color(headingColor),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                            )
                            Text(
                                text = "${data.current.uv}",
                                color = Color.White,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun WeatherPagePreview() {
    WeatherAppTheme {
        WeatherPage(WeatherViewModel())
    }
}
