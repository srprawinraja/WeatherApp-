package com.example.weatherapp

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField

import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.ui.theme.WeatherAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherPage(weatherViewModel: WeatherViewModel){
    var city by remember { mutableStateOf("") }
    val text_color = ContextCompat.getColor(LocalContext.current, R.color.text_color)
    val black_color  = ContextCompat.getColor(LocalContext.current, R.color.black)
    var isFocused by remember { mutableStateOf(false) }
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
                       weatherViewModel.getData(city)
                    }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp).onFocusChanged {
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
    }
}




@Preview(showBackground = true)
@Composable
fun WeatherPagePreview() {
    WeatherAppTheme {
        WeatherPage(WeatherViewModel())
    }
}
