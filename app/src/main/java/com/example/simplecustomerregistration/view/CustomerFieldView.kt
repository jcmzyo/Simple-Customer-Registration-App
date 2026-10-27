package com.example.simplecustomerregistration.view

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.example.simplecustomerregistration.R
import com.example.simplecustomerregistration.constants.CONSTANTS
import com.example.simplecustomerregistration.data.Customer
import com.example.simplecustomerregistration.viewmodel.CustomerListViewModel

@GlideModule
class CustomerFieldView {
    companion object {

        enum class TypeFieldSettings(
            val tag: String, val ratio: Float, val keyboardOption: KeyboardOptions, val regex: Regex
        ) {
            FIRSTNAME(
                CONSTANTS.TEXT_STRING_FIRST_NAME,
                4f,
                KeyboardOptions.Default,
                CustomerListViewModel.CustomerDataState.FIRSTNAME_REGEX
            ),
            LASTNAME(
                CONSTANTS.TEXT_STRING_LAST_NAME,
                4f,
                KeyboardOptions.Default,
                CustomerListViewModel.CustomerDataState.LASTNAME_REGEX
            ),
            MOBILE_NUMBER(
                CONSTANTS.TEXT_STRING_MOBILE_NUMBER,
                7f,
                KeyboardOptions(keyboardType = KeyboardType.Text),
                CustomerListViewModel.CustomerDataState.MOBILE_NUMBER_REGEX
            ),
            ADDRESS(
                CONSTANTS.TEXT_STRING_ADDRESS,
                7f,
                KeyboardOptions.Default,
                CustomerListViewModel.CustomerDataState.ADDRESS_REGEX
            )
        }

        private lateinit var currentCustomerDataState: CustomerListViewModel.CustomerDataState


        @Composable
        fun Spacing() {
            Spacer(modifier = Modifier.height(CONSTANTS.DP_SMALL_SPACING))
        }

        @Composable
        fun ColumnScope.FieldPanel(
            textFieldState: TextFieldState, isEditMode: Boolean, settings: TypeFieldSettings
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(vertical = CONSTANTS.DP_BIG_SPACING)
                    .weight(1f)
            ) {
                OutlinedTextField(
                    state = textFieldState,
                    label = { Text(text = settings.tag) },
                    keyboardOptions = settings.keyboardOption,
                    isError = !settings.regex.matches(textFieldState.text),
                    enabled = isEditMode,
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledBorderColor = Color.Transparent,
                        disabledTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    contentPadding = PaddingValues.Zero,
                    lineLimits = TextFieldLineLimits.SingleLine,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(settings.ratio)
                )
            }
        }

        @Composable
        fun FocusedPanel(isEditMode: Boolean) {
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = CONSTANTS.DP_FIELD_PADDING, vertical = 0.dp)
            ) {
                FieldPanel(
                    currentCustomerDataState.firstNameState, isEditMode, TypeFieldSettings.FIRSTNAME
                )
                FieldPanel(
                    currentCustomerDataState.lastNameState, isEditMode, TypeFieldSettings.LASTNAME
                )
            }

        }

        @Composable
        fun UnfocusedNamePanel(customer: Customer) {
            Text(
                text = "${customer.firstName} ${customer.lastName}",
                maxLines = 1,
                autoSize = TextAutoSize.StepBased(),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(CONSTANTS.DP_FIELD_PADDING)
            )
        }

        @Composable
        fun ImageFloatingActionButton(
            id: Int,
            description: String,
            onClick: () -> Unit
        ) {
            SmallFloatingActionButton(
                onClick = onClick,
                shape = MaterialTheme.shapes.extraLarge,

                ) {
                Icon(
                    painter = painterResource(id),
                    contentDescription = description
                )
            }
        }

        @OptIn(ExperimentalGlideComposeApi::class)
        @Composable
        fun MainDetailsView(
            customer: Customer,
            isEditMode: Boolean,
            isFocused: Boolean,
            currentCustomerDataState: CustomerListViewModel.CustomerDataState,
            removeCustomerImage: () -> Unit,
            togglePhotoPicker: (Boolean) -> Unit
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(if (isFocused) CONSTANTS.ASPECT_RATIO_MAIN_FIELD_FOCUSED else CONSTANTS.ASPECT_RATIO_MAIN_FIELD_UNFOCUSED)
                    .padding(CONSTANTS.DP_FIELD_PADDING)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxHeight()
                        .wrapContentWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .aspectRatio(CONSTANTS.ASPECT_RATIO_PHOTO)
                            .fillMaxSize(), contentAlignment = Alignment.CenterEnd
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .aspectRatio(CONSTANTS.ASPECT_RATIO_PHOTO)
                                .fillMaxSize()
                                .background(Color.Transparent)
                        ) {
                            GlideImage(
                                model = if (isEditMode && isFocused) currentCustomerDataState.photoUriState else customer.photo,
                                contentDescription = CONSTANTS.IMAGE_DESCRIPTION_STRING_CUSTOMER,
                                failure = placeholder(R.drawable.empty_icon),
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .fillMaxSize()
                                    .background(MaterialTheme.colorScheme.secondaryContainer)
                            )
                        }
                        Column(
                            verticalArrangement = Arrangement.SpaceEvenly,
                            horizontalAlignment = Alignment.End,
                            modifier = Modifier.matchParentSize()
                        ) {
                            Box(
                                Modifier.weight(CONSTANTS.WEIGHT_EQUAL)
                            ) {
                                if (isFocused && isEditMode && currentCustomerDataState.photoUriState.toUri() != Uri.EMPTY) {
                                    ImageFloatingActionButton(
                                        R.drawable.delete_icon,
                                        CONSTANTS.IMAGE_DESCRIPTION_STRING_DELETE
                                    ) { removeCustomerImage() }
                                }
                            }

                            Box(
                                Modifier.weight(1f)
                            ) {
                                if (isFocused && isEditMode) {
                                    ImageFloatingActionButton(
                                        R.drawable.edit_icon,
                                        CONSTANTS.IMAGE_DESCRIPTION_STRING_EDIT
                                    ) { togglePhotoPicker(true) }
                                }
                            }

                        }
                    }

                    if (isFocused) {
                        FocusedPanel(isEditMode)
                    } else {
                        UnfocusedNamePanel(customer)
                    }
                }
            }
        }

        @Composable
        fun OtherDetailsView(isEditMode: Boolean) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(CONSTANTS.ASPECT_RATIO_OTHER_FIELD)
                    .padding(horizontal = CONSTANTS.DP_FIELD_PADDING)
            ) {
                Column(
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(horizontal = CONSTANTS.DP_FIELD_PADDING, vertical = 0.dp)
                ) {
                    FieldPanel(
                        currentCustomerDataState.mobileNumberState,
                        isEditMode,
                        TypeFieldSettings.MOBILE_NUMBER
                    )
                    FieldPanel(
                        currentCustomerDataState.addressState, isEditMode, TypeFieldSettings.ADDRESS
                    )
                }
            }
        }

        @Composable
        fun ButtonsView(
            isEditMode: Boolean,
            isAddMode: Boolean,
            toggleEdit: () -> Unit,
            addCustomer: () -> Unit,
            updateCustomer: () -> Unit,
            deleteCustomer: () -> Unit
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(CONSTANTS.ASPECT_RATIO_BUTTON_FIELD)
                    .padding(horizontal = CONSTANTS.DP_FIELD_PADDING)
            ) {
                Button(
                    onClick = {
                        if (isEditMode) toggleEdit() else deleteCustomer()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(CONSTANTS.DP_BUTTON_PADDING)
                        .weight(1f)
                ) {
                    Text(if (isEditMode) CONSTANTS.BUTTON_TEXT_STRING_CANCEL else CONSTANTS.BUTTON_TEXT_STRING_DELETE)
                }

                Button(
                    enabled = !(isAddMode || isEditMode) || currentCustomerDataState.checkValidity(),
                    onClick = {
                        if (isAddMode) addCustomer() else if (isEditMode) updateCustomer() else toggleEdit()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(CONSTANTS.DP_BUTTON_PADDING)
                        .weight(CONSTANTS.WEIGHT_EQUAL)
                ) {
                    Text(if (isAddMode) CONSTANTS.BUTTON_TEXT_STRING_ADD else if (isEditMode) CONSTANTS.BUTTON_TEXT_STRING_UPDATE else CONSTANTS.BUTTON_TEXT_STRING_EDIT)
                }

            }
        }

        @Composable
        fun CustomerField(
            customer: Customer,
            isEditMode: Boolean,
            isAddMode: Boolean,
            currentCustomerDataState: CustomerListViewModel.CustomerDataState,
            editCurrentCustomer: (CustomerListViewModel.CustomerDataState) -> Unit,
            toggleEdit: () -> Unit,
            addCustomer: () -> Unit,
            updateCustomer: () -> Unit,
            deleteCustomer: () -> Unit,
            removeCustomerImage: () -> Unit,
            togglePhotoPicker: (Boolean) -> Unit
        ) {
            this.currentCustomerDataState = currentCustomerDataState
            val isFocused = customer.id == currentCustomerDataState.idState

            Spacing()
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clip(MaterialTheme.shapes.medium)
                    .border(
                        CONSTANTS.DP_BORDER_PADDING,
                        MaterialTheme.colorScheme.surfaceVariant,
                        MaterialTheme.shapes.medium
                    )
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(CONSTANTS.DP_FIELD_PADDING)
                    .clickable(onClick = {
                        if (!isAddMode && !isEditMode) {
                            editCurrentCustomer(CustomerListViewModel.CustomerDataState(customer))
                        }
                    })
            ) {
                MainDetailsView(
                    customer,
                    isEditMode,
                    isFocused,
                    currentCustomerDataState,
                    removeCustomerImage,
                    togglePhotoPicker
                )

                if (isFocused) {

                    OtherDetailsView(isEditMode)

                    ButtonsView(
                        isEditMode,
                        isAddMode,
                        toggleEdit,
                        addCustomer,
                        updateCustomer,
                        deleteCustomer
                    )
                }
            }
            Spacing()
        }
    }
}