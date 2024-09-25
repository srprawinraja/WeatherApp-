package com.example.weatherapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import com.example.weatherapp.API.NetworkResponse
import com.example.weatherapp.API.WeatherModel
import com.example.weatherapp.ui.theme.WeatherAppTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale
// fasdsa
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun WeatherPage(weatherViewModel: WeatherViewModel){


    var city by remember { mutableStateOf("") }
    val textColor = ContextCompat.getColor(LocalContext.current, R.color.text_color)
    val headingColor = ContextCompat.getColor(LocalContext.current, R.color.heading)
    val blackColor  = ContextCompat.getColor(LocalContext.current, R.color.black)
    var isFocused by remember { mutableStateOf(false) }
    val weatherResult =weatherViewModel.weatherResult.observeAsState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val scroll = rememberScrollState()
    Column (
        modifier = Modifier
            .background(colorResource(id = R.color.background)) // Background first
            .fillMaxSize()
    ){
        Row {
            OutlinedTextField(
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done), // Set IME action to "Done"
                keyboardActions = KeyboardActions(
                    onDone = {

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
                    focusedContainerColor = Color(textColor),
                    unfocusedContainerColor = Color(textColor),
                    focusedIndicatorColor = Color(textColor),
                    unfocusedIndicatorColor = Color(textColor),
                    focusedLabelColor = Color(textColor),
                    cursorColor = Color(blackColor)
                ),

                onValueChange ={
                    city=it
                },
                label = {
                    Row {
                        if(!isFocused) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = stringResource(id = R.string.search_icon)
                            )
                            Text(
                                text = stringResource(id = R.string.search),
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
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scroll)
        ) {
          //WeatherDetailPreview(headingColor)
            when (val result = weatherResult.value) {
                is NetworkResponse.Success -> {
                  WeatherDetail(headingColor, result.data)
                }

                is NetworkResponse.Error -> {
                    Text(text = result.message)
                }

                NetworkResponse.Loading -> {
                    CircularProgressIndicator()
                }

                null -> {
                    Text(text = stringResource(id = R.string.error))
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
        model = stringResource(id = R.string.https)+ data.current.condition.icon.replace("64x64","128x128"),
        contentDescription = stringResource(id = R.string.icon),
        error = painterResource(id = R.drawable._cb99d46_bd7d_4eb7_9526_5b7c4fe7fc9d)
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
            contentDescription = stringResource(id = R.string.weatherImage),
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
        Column (
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(color = colorResource(id = R.color.body_background_color))
                .fillMaxWidth()
        ){
            Text(
                text = stringResource(id = R.string.todayForecast),
                color = Color(headingColor),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 20.dp, top = 15.dp, bottom = 15.dp)
            )
            Row (
            ){
                Column (
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text(text = stringResource(id = R.string.nine), color = Color(headingColor), fontSize = 13.sp)
                    AsyncImage(
                        model = stringResource(id = R.string.https)+ data.forecast.forecastday.get(0).hour.get(9).condition.icon.replace("64x64","128x128"),
                        contentDescription = stringResource(id = R.string.icon),
                        error = painterResource(id = R.drawable._cb99d46_bd7d_4eb7_9526_5b7c4fe7fc9d),
                        alignment = Alignment.TopCenter
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ){
                        Text(
                            data.forecast.forecastday.get(0).hour.get(9).temp_c.toString(),
                            fontSize = 13.sp,
                            color = Color.White,
                            fontStyle = FontStyle.Normal,
                            fontFamily = FontFamily.Default,
                            fontWeight = FontWeight.ExtraBold,
                            )

                        Image(
                            painter = painterResource(id = R.drawable.baseline_arrow_circle_up_24),
                            contentDescription = stringResource(id = R.string.weatherImage),
                            modifier = Modifier
                                .size(10.dp)
                                .padding(start = 1.dp)
                        )
                    }
                }
                Column (
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text(text = stringResource(id = R.string.twelve), color = Color(headingColor),  fontSize = 13.sp)
                    AsyncImage(
                        model = stringResource(id = R.string.https)+ data.forecast.forecastday.get(0).hour.get(12).condition.icon.replace("64x64","128x128"),
                        contentDescription = stringResource(id = R.string.icon),
                        error = painterResource(id = R.drawable._cb99d46_bd7d_4eb7_9526_5b7c4fe7fc9d),
                        alignment = Alignment.TopCenter
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ){
                        Text(
                            data.forecast.forecastday.get(0).hour.get(12).temp_c.toString(),
                            fontSize = 13.sp,
                            color = Color.White,
                            fontStyle = FontStyle.Normal,
                            fontFamily = FontFamily.Default,
                            fontWeight = FontWeight.ExtraBold,

                            )

                        Image(
                            painter = painterResource(id = R.drawable.baseline_arrow_circle_up_24),
                            contentDescription = stringResource(id = R.string.weatherImage),
                            modifier = Modifier
                                .size(10.dp)
                                .padding(start = 1.dp)
                        )
                    }
                }
                Column (
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text(text = stringResource(id = R.string.three), color = Color(headingColor), fontSize = 13.sp)
                    AsyncImage(
                        model = stringResource(id = R.string.https)+ data.forecast.forecastday.get(0).hour.get(15).condition.icon.replace("64x64","128x128"),
                        contentDescription = stringResource(id = R.string.icon),
                        error = painterResource(id = R.drawable._cb99d46_bd7d_4eb7_9526_5b7c4fe7fc9d),
                        alignment = Alignment.TopCenter
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ){
                        Text(
                            data.forecast.forecastday.get(0).hour.get(15).temp_c.toString(),
                            fontSize = 13.sp,
                            color = Color.White,
                            fontStyle = FontStyle.Normal,
                            fontFamily = FontFamily.Default,
                            fontWeight = FontWeight.ExtraBold,
                            )

                        Image(
                            painter = painterResource(id = R.drawable.baseline_arrow_circle_up_24),
                            contentDescription = stringResource(id = R.string.weatherImage),
                            modifier = Modifier
                                .size(10.dp)
                                .padding(start = 1.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
        Spacer(modifier = Modifier.height(20.dp))

        Column (
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(color = colorResource(id = R.color.body_background_color))
                .fillMaxWidth()
        ){
            Column (
                modifier = Modifier
                    .wrapContentSize()
                    .padding(20.dp),
            ){
                Text(
                    text = stringResource(id = R.string.forecast),
                    color = Color(headingColor),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.height(15.dp))
                Column (
                    verticalArrangement = Arrangement.spacedBy(15.dp)
                ){
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Text(
                            text = "Today",
                            color = Color(headingColor),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Left,
                            modifier = Modifier.weight(1f)
                        )
                        AsyncImage(
                            modifier = Modifier
                                .weight(1f)
                                .offset(0.dp, -5.dp),
                            model = stringResource(id = R.string.https)+ data.forecast.forecastday[0].day.condition.icon,
                            contentDescription = stringResource(id = R.string.icon),
                            error = painterResource(id = R.drawable._cb99d46_bd7d_4eb7_9526_5b7c4fe7fc9d),
                            alignment = Alignment.TopCenter
                        )
                        Text(
                            text = data.forecast.forecastday.get(0).day.condition.text,
                            color = colorResource(id = R.color.text_color), fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f),

                        )
                        Row (
                            modifier = Modifier.weight(1f),
                            horizontalArrangement = Arrangement.End
                            ){
                            Text(
                                text = "${data.forecast.forecastday.get(0).day.maxtemp_c.toInt()}",
                                color = colorResource(id = R.color.text_color),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.End
                            )
                            Text(
                                text = "/${data.forecast.forecastday.get(0).day.mintemp_c.toInt()}",
                                color = Color(headingColor),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.End
                            )

                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = dateToDay(data, 1),
                            color = Color(headingColor),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f),

                            )
                        AsyncImage(
                            modifier = Modifier
                                .weight(1f)
                                .offset(0.dp, -5.dp),
                            model = stringResource(id = R.string.https)+ data.forecast.forecastday.get(1).day.condition.icon,
                            contentDescription = stringResource(id = R.string.icon),
                            error = painterResource(id = R.drawable._cb99d46_bd7d_4eb7_9526_5b7c4fe7fc9d)
                        )
                        Text(
                            text = data.forecast.forecastday.get(1).day.condition.text,
                            color = colorResource(id = R.color.text_color), fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f)

                        )
                        Row (
                            modifier = Modifier.weight(1f),
                            horizontalArrangement = Arrangement.End
                        ){
                            Text(
                                text = "${data.forecast.forecastday.get(1).day.maxtemp_c.toInt()}",
                                color = colorResource(id = R.color.text_color),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.End
                            )
                            Text(
                                text = "/${data.forecast.forecastday.get(1).day.mintemp_c.toInt()}",
                                color = Color(headingColor),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.End
                            )

                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = dateToDay(data, 2),
                            color = Color(headingColor),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f)
                        )
                        AsyncImage(
                            modifier = Modifier
                                .weight(1f)
                                .offset(0.dp, -5.dp),
                            model = stringResource(id = R.string.https)+ data.forecast.forecastday.get(2).day.condition.icon,
                            contentDescription = stringResource(id = R.string.icon),
                            error = painterResource(id = R.drawable._cb99d46_bd7d_4eb7_9526_5b7c4fe7fc9d)
                        )
                        Text(
                            text = data.forecast.forecastday.get(2).day.condition.text,
                            color = colorResource(id = R.color.text_color), fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f)

                        )
                        Row (
                            modifier = Modifier.weight(1f),
                            horizontalArrangement = Arrangement.End
                        ){
                            Text(
                                text = "${data.forecast.forecastday.get(2).day.maxtemp_c.toInt()}",
                                color = colorResource(id = R.color.text_color),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.End
                            )
                            Text(
                                text = "/${data.forecast.forecastday.get(2).day.mintemp_c.toInt()}",
                                color = Color(headingColor),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.End
                            )

                        }
                    }
                }
            }

        }
        Spacer(modifier = Modifier.height(20.dp))
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(color = colorResource(id = R.color.body_background_color))
                .fillMaxWidth()

        ) {
            Text(
                text = stringResource(id = R.string.air),
                color = Color(headingColor),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 20.dp, top = 15.dp, bottom = 15.dp)
            )

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
                        text = stringResource(id = R.string.feel),
                        color = Color(headingColor),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold)
                    Text(
                        text = "${data.current.feelslike_c} "+ stringResource(id = R.string.C),
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold)
                }
                Column (
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 10.dp),
                    horizontalAlignment = Alignment.End
                ){
                    Text(
                        text = stringResource(id = R.string.wind),
                        color = Color(headingColor),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,)
                    Text(
                        text = "${data.current.wind_kph}"+ stringResource(id = R.string.km),
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,)
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
                        .padding(start = 10.dp),
                ){
                    Text(
                        text = stringResource(id = R.string.humidity),
                        color = Color(headingColor),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = "${data.current.humidity}"+ stringResource(id = R.string.percent),
                        color = Color.White,
                        fontSize = 13.sp,
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
                        text = stringResource(id = R.string.uv),
                        color = Color(headingColor),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = "${data.current.uv}",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}
data class Raindrop(val x: Float, var y: Float, val speed: Float, val length: Float, val opacity: Float)




fun dateToDay(data:WeatherModel, index:Int):String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val date = LocalDate.parse(data.forecast.forecastday.get(index).date, formatter)
    return date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
}



@Preview(showBackground = true, device = Devices.DESKTOP)
@Composable
fun WeatherPagePreview() {
    WeatherAppTheme {
        WeatherPage(WeatherViewModel())
    }
}
