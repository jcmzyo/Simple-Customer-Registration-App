package com.example.simplecustomerregistration.view

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.simplecustomerregistration.constants.CONSTANTS
import com.example.simplecustomerregistration.data.Customer
import com.example.simplecustomerregistration.ui.theme.SimpleCustomerRegistrationTheme
import com.example.simplecustomerregistration.view.CustomerFieldView.Companion.CustomerField
import com.example.simplecustomerregistration.viewmodel.CustomerListViewModel
import kotlinx.coroutines.launch
import java.io.File
import java.util.UUID


class CustomerListActivity : ComponentActivity() {

    private val customerListViewModel: CustomerListViewModel by viewModels { CustomerListViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val pickMedia =
            this.registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    val result = saveImageToInternalStorage(applicationContext, uri)
                    customerListViewModel.editCurrentCustomerImage(result)
                }
                customerListViewModel.togglePhotoPicker(false)
            }
        enableEdgeToEdge()
        setContent {
            val customerList by customerListViewModel.customerList.observeAsState(emptyList())
            SimpleCustomerRegistrationTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color.Green)
                ) {

                    if (customerListViewModel.isPhotoPickerMode.collectAsStateWithLifecycle().value) {
                        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    }

                    CustomerListScreen(
                        customerList,
                        customerListViewModel.isEditMode.collectAsStateWithLifecycle().value,
                        customerListViewModel.isAddMode.collectAsStateWithLifecycle().value,
                        customerListViewModel.currentFocusedCustomerDataState.collectAsStateWithLifecycle().value,
                        startAdd = { customerListViewModel.toggleAdd() },
                        addCustomerToDb = { customerListViewModel.addCustomerToDb() },
                        editCustomer = { customerListViewModel.editCurrentCustomerImage(it) },
                        removeCustomerImage = { customerListViewModel.removeCurrentCustomerImage() },
                        toggleEdit = { customerListViewModel.toggleEdit() },
                        updateCustomer = { customerListViewModel.updateCustomerToDb() },
                        deleteCustomer = { customerListViewModel.deleteCustomerToDb() },
                        togglePhotoPicker = { customerListViewModel.togglePhotoPicker(it) })
                }
            }
        }
    }

}

fun saveImageToInternalStorage(context: Context, uri: Uri): Uri {
    val inputStream = context.contentResolver.openInputStream(uri)

    val type = context.contentResolver.getType(uri) ?: ""
    val index: Int = type.indexOf('/')
    val filename = "${UUID.randomUUID()}-icon." + type.substring(index + 1)
    val outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE)
    inputStream?.use { input ->
        outputStream.use { output ->
            input.copyTo(output)
        }
    }

    val f = File(context.filesDir, filename)

    return Uri.fromFile(f) ?: Uri.EMPTY
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerListScreen(
    customerList: List<Customer>,
    isEditMode: Boolean,
    isAddMode: Boolean,
    currentCustomerState: CustomerListViewModel.CustomerDataState,
    startAdd: () -> Unit,
    addCustomerToDb: () -> Unit,
    editCustomer: (CustomerListViewModel.CustomerDataState) -> Unit,
    removeCustomerImage: () -> Unit,
    toggleEdit: () -> Unit,
    updateCustomer: () -> Unit,
    deleteCustomer: () -> Unit,
    togglePhotoPicker: (Boolean) -> Unit
) {
    Scaffold(topBar = {
        TopAppBar(
            colors = topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.primary,
            ), title = {
                Text(CONSTANTS.TITLE_STRING_APP_NAME)
            })
    }, floatingActionButton = {
        if (!isAddMode && !isEditMode) ExtendedFloatingActionButton(
            onClick = { startAdd() }, Modifier.wrapContentSize()
        ) {
            Text(CONSTANTS.TEXT_STRING_ADD_NEW_CUSTOMER)
        }
    }) { innerPadding ->
        if (customerList.isEmpty() && !isAddMode) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = CONSTANTS.TEXT_STRING_NO_CUSTOMERS
                )
            }

        } else {
            val lazyColumnListState = rememberLazyListState()
            val coroutineScope = rememberCoroutineScope()

            LazyColumn(
                state = lazyColumnListState,
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(horizontal = CONSTANTS.DP_PADDING)
                    .fillMaxSize()
            ) {

                coroutineScope.launch {
                    if (isAddMode) {
                        lazyColumnListState.scrollToItem(0)
                    }
                }
                if (isAddMode) {
                    item {
                        CustomerField(
                            Customer(),
                            isEditMode,
                            isAddMode,
                            currentCustomerState,
                            editCustomer,
                            toggleEdit,
                            addCustomerToDb,
                            updateCustomer,
                            deleteCustomer,
                            removeCustomerImage,
                            togglePhotoPicker
                        )
                    }
                }


                items(customerList.size) { index ->
                    CustomerField(
                        customerList[index],
                        isEditMode,
                        isAddMode,
                        currentCustomerState,
                        editCustomer,
                        toggleEdit,
                        addCustomerToDb,
                        updateCustomer,
                        deleteCustomer,
                        removeCustomerImage,
                        togglePhotoPicker
                    )
                }
            }
        }
    }
}


