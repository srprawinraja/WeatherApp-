package com.example.weatherapp

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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
                        weatherViewModel.getData(city.trim())

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
                    focusedContainerColor = Color(blackColor),
                    unfocusedContainerColor = Color(blackColor),
                    focusedIndicatorColor = Color(blackColor),
                    unfocusedIndicatorColor = Color(blackColor),
                    focusedLabelColor = Color(blackColor),
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
                                tint = Color.White,
                                contentDescription = stringResource(id = R.string.search_icon)
                            )
                            Text(
                                text = stringResource(id = R.string.search),
                                color = Color.White,
                                modifier = Modifier.padding(start = 10.dp, top = 2.dp)
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


            when (val result = weatherResult.value) {
                is NetworkResponse.Success -> {
                    WeatherDetail(headingColor, result.data)
                }

                is NetworkResponse.Error -> {
                    Log.e("hello there",city)
                    Text(text = result.message)
                }

                NetworkResponse.Loading -> {
                    CircularProgressIndicator()
                }

                null -> {
                    // nothing to display
                }
            }
        }

    }
}
@Composable
fun WeatherDetail(headingColor:Int, data:WeatherModel){
    val todayForecastScroll = rememberScrollState()

    Text(
        data.city,
        fontSize = 25.sp,
        color = Color.White,
        fontStyle = FontStyle.Normal,
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
    )
    Spacer(modifier = Modifier.height(1.dp))
    Text(
        data.weather,
        fontSize = 12.sp,
        color = Color(color=headingColor),
        fontStyle = FontStyle.Normal,
        fontFamily = FontFamily.Default,

        )
    Spacer(modifier = Modifier.height(20.dp))
    AsyncImage(
        modifier = Modifier.size(160.dp),
        model = data.img,
        contentDescription = stringResource(id = R.string.icon),
        error = painterResource(id = R.drawable._cb99d46_bd7d_4eb7_9526_5b7c4fe7fc9d)
    )
    Spacer(modifier = Modifier.height(20.dp))

    Row{
        Text(
            data.temp,
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
                modifier = Modifier.horizontalScroll(todayForecastScroll).padding(5.dp)
            ){
                for(todayForecastDetail in data.todayForecast){
                    todayForecast(todayForecastDetail.time, todayForecastDetail.img, todayForecastDetail.temp, headingColor)

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
                    text = "${data.dayForecast.size}-DAY FORECAST",
                    color = Color(headingColor),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.height(15.dp))
                for(dayForecast in data.dayForecast) {
                    daysForecast(
                        day = dayForecast.day,
                        img = dayForecast.img,
                        weather = dayForecast.weather,
                        tempMax = dayForecast.tempMax,
                        tempMin = dayForecast.tempMin,
                        headingColor = headingColor
                    )
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
                modifier = Modifier.padding(start = 20.dp, top = 15.dp, bottom = 10.dp)
            )
            for(airConditionDetail in data.airCondition){
                airCondition(headingColor, airConditionDetail.label1, airConditionDetail.value1, airConditionDetail.label2, airConditionDetail.value2)
            }
            Spacer(modifier = Modifier.height(10.dp))

        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}
@Composable
fun todayForecast(time:String, img:String, temp: String, headingColor: Int){
    Spacer(modifier = Modifier.width(15.dp))

    Column (
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(text = time, color = Color(headingColor), fontSize = 13.sp)
        AsyncImage(
            model = img,
            contentDescription = stringResource(id = R.string.icon),
            error = painterResource(id = R.drawable._cb99d46_bd7d_4eb7_9526_5b7c4fe7fc9d),
            alignment = Alignment.TopCenter,
            modifier = Modifier.padding(end = 5.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ){
            Text(
                temp,
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

@Composable
fun daysForecast(day:String, img: String, weather:String, tempMax:String, tempMin:String, headingColor:Int){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround

    ) {
        Text(
            text = day,
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
            model = img,
            contentDescription = stringResource(id = R.string.icon),
            error = painterResource(id = R.drawable._cb99d46_bd7d_4eb7_9526_5b7c4fe7fc9d),
            alignment = Alignment.TopCenter
        )
        Text(
            text = weather,
            color = colorResource(id = R.color.text_color), fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f),

            )
        Row (
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.End
        ){
            Text(
                text = tempMax,
                color = colorResource(id = R.color.text_color),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.End
            )
            Text(
                text = "/${tempMin}",
                color = Color(headingColor),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.End
            )


        }
    }
    Spacer(Modifier.height(5.dp))

}

@Composable
fun airCondition(headingColor: Int, label1:String, value1:String, label2:String, value2:String){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Column (
            modifier = Modifier
                .weight(1f)
                .padding(start = 10.dp) ,
            horizontalAlignment = Alignment.Start
        ){
            Text(
                text = label1,
                color = Color(headingColor),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold)
            Text(
                text = value1,
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
                text = label2,
                color = Color(headingColor),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,)
            Text(
                text = value2,
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,)
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
