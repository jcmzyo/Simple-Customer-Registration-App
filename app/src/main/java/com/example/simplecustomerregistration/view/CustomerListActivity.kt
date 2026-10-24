package com.example.simplecustomerregistration.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.example.simplecustomerregistration.data.Customer

import com.example.simplecustomerregistration.ui.theme.SimpleCustomerRegistrationTheme
import com.example.simplecustomerregistration.viewmodel.CustomerListViewModel

class CustomerListActivity : ComponentActivity() {

    private val customerListViewModel: CustomerListViewModel by viewModels { CustomerListViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SimpleCustomerRegistrationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize().background(color = Color.Green)
                ){
                    CustomerListScreen(
                        customerListViewModel.customerList.value,
                        {customerListViewModel.addCustomer() }
                    )
                }
            }
        }
    }

}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerListScreen(
    customerList: List<Customer>?,
    addUser: () -> Boolean,
){
    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("Simple Customer Registration")
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { addUser },
                Modifier.wrapContentSize()
            ) {
                Text("Add New Contact")
            }
        }
    ) { innerPadding ->
        if (customerList.isNullOrEmpty()){
            Text(
                text = "No registered customers"
            )
        }
        else{
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                val names = customerList;
                items(names.size){
                        registered -> CustomerField("$names[registered].firstName $names[registered].lastName ")
                }
        }
    }
}}

@Composable
fun CustomerField(s: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(4f)
            .padding(10.dp)
            .background(Color.Blue)
    ){
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .wrapContentWidth()
        ){
            Box(
                modifier = Modifier
                    .aspectRatio(1f)
                    .fillMaxSize()
                    .padding(15.dp)
            ){
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .border(3.dp, Color.Red, CircleShape)
                        .aspectRatio(1f)
                        .fillMaxSize()
                        .background(Color.Green)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .padding(20.dp)
                    .background(Color.Cyan),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = s,
                    autoSize = TextAutoSize.StepBased(),
                    maxLines = 1
                )
            }

        }
    }
}