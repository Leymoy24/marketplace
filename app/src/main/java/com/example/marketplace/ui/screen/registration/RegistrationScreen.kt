package com.example.marketplace.ui.screen.registration

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.marketplace.R
import com.example.marketplace.ui.composables.CommonButton
import com.example.marketplace.ui.composables.CommonOutlinedTextField
import com.example.marketplace.ui.composables.EyeIconButton
import com.example.marketplace.ui.screen.CommonUiState
import com.example.marketplace.ui.theme.Blue

@Composable
fun RegistrationScreen(
    onEnterButtonPressed: () -> Unit,
    viewModel: RegistrationViewModel,
) {
    val uiState by viewModel.uiScreenState.collectAsState()
    val nameFieldState by viewModel.nameFieldState.collectAsState()
    val emailFieldState by viewModel.emailFieldState.collectAsState()
    val passwordFieldState by viewModel.passwordFieldState.collectAsState()
    val repeatPasswordFieldState by viewModel.repeatPasswordFieldState.collectAsState()

    var nameTextState by rememberSaveable { mutableStateOf("") }
    var emailTextState by rememberSaveable { mutableStateOf("") }
    var passwordTextState by rememberSaveable { mutableStateOf("") }
    var repeatPasswordTextState by rememberSaveable { mutableStateOf("") }
    var isVisiblePassword by rememberSaveable { mutableStateOf(false) }
    var isVisibleRepeatPassword by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(uiState) {
        if (uiState is RegistrationUiState.Success) {
            onEnterButtonPressed()
        }
    }

    Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 26.dp)
        ) {
            Text(
                modifier = Modifier.padding(top = 80.dp, bottom = 70.dp),
                text = stringResource(R.string.registration),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium
            )

            CommonOutlinedTextField(
                value = nameTextState,
                onValueChange = { value: String ->
                    nameTextState = value
                    viewModel.changeUiScreenState(RegistrationUiState.Initial)
                    viewModel.changeNameFieldState(CommonUiState.Initial)
                },
                textPlaceholder = stringResource(id = R.string.name),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.None,
                    autoCorrectEnabled = false,
                    keyboardType = KeyboardType.Text
                ),
                isError = nameFieldState is CommonUiState.Error
            )

            CommonOutlinedTextField(
                value = emailTextState,
                onValueChange = { value: String ->
                    emailTextState = value
                    viewModel.changeUiScreenState(RegistrationUiState.Initial)
                    viewModel.changeEmailFieldState(CommonUiState.Initial)
                },
                modifier = Modifier
                    .padding(top = 22.dp),
                textPlaceholder = stringResource(id = R.string.email),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                isError = emailFieldState is CommonUiState.Error
            )

            CommonOutlinedTextField(
                value = passwordTextState,
                onValueChange = { value: String ->
                    passwordTextState = value
                    if (passwordTextState != repeatPasswordTextState) {
                        viewModel.changePasswordFieldState(CommonUiState.Error)
                        viewModel.changeRepeatPasswordFieldState(CommonUiState.Error)
                        viewModel.changeUiScreenState(RegistrationUiState.PasswordsNotEquals)
                    } else {
                        viewModel.changePasswordFieldState(CommonUiState.Initial)
                        viewModel.changeRepeatPasswordFieldState(CommonUiState.Initial)
                        viewModel.changeUiScreenState(RegistrationUiState.Initial)
                    }
                },
                modifier = Modifier
                    .padding(top = 22.dp),
                textPlaceholder = stringResource(id = R.string.password),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                maxLength = 24,
                isError = passwordFieldState is CommonUiState.Error,
                visualTransformation = if (isVisiblePassword) VisualTransformation.None
                else PasswordVisualTransformation(),
                trailingIcon = {
                    EyeIconButton(
                        isVisibleEye = isVisiblePassword,
                        onCLick = {
                            isVisiblePassword = !isVisiblePassword
                        }
                    )
                }
            )

            CommonOutlinedTextField(
                value = repeatPasswordTextState,
                onValueChange = { value: String ->
                    repeatPasswordTextState = value
                    if (passwordTextState != repeatPasswordTextState) {
                        viewModel.changePasswordFieldState(CommonUiState.Error)
                        viewModel.changeRepeatPasswordFieldState(CommonUiState.Error)
                        viewModel.changeUiScreenState(RegistrationUiState.PasswordsNotEquals)
                    } else {
                        viewModel.changePasswordFieldState(CommonUiState.Initial)
                        viewModel.changeRepeatPasswordFieldState(CommonUiState.Initial)
                        viewModel.changeUiScreenState(RegistrationUiState.Initial)
                    }
                },
                modifier = Modifier
                    .padding(top = 22.dp),
                textPlaceholder = stringResource(id = R.string.confirm_the_password),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                maxLength = 24,
                isError = repeatPasswordFieldState is CommonUiState.Error,
                visualTransformation = if (isVisibleRepeatPassword) VisualTransformation.None
                else PasswordVisualTransformation(),
                trailingIcon = {
                    EyeIconButton(
                        isVisibleEye = isVisibleRepeatPassword,
                        onCLick = {
                            isVisibleRepeatPassword = !isVisibleRepeatPassword
                        }
                    )
                }
            )

            when (uiState) {
                is RegistrationUiState.EmailNotCorrect,
                is RegistrationUiState.PasswordNotCorrect,
                is RegistrationUiState.PasswordsNotEquals,
                is RegistrationUiState.EmptyFields,
                is RegistrationUiState.AlreadyRegisteredEmail-> {
                    val errorText = when (uiState) {
                        is RegistrationUiState.EmailNotCorrect -> {
                            stringResource(id = R.string.email_not_correct)
                        }

                        is RegistrationUiState.PasswordNotCorrect -> {
                            stringResource(id = R.string.password_not_requirements)
                        }

                        is RegistrationUiState.PasswordsNotEquals -> {
                            stringResource(id = R.string.mismatch_password)
                        }

                        is RegistrationUiState.AlreadyRegisteredEmail -> {
                            stringResource(R.string.email_already_exists)
                        }

                        else -> {
                            stringResource(id = R.string.fields_are_empty)
                        }
                    }

                    Text(
                        text = errorText,
                        color = MaterialTheme.colorScheme.inversePrimary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 70.dp)
                            .padding(top = 15.dp)
                    )
                }
                else -> {}
            }

            Spacer(modifier = Modifier.weight(1f))

            CommonButton(
                text = stringResource(R.string.enter),
                isLoading = uiState is RegistrationUiState.Loading,
                onClick = {
                    if (nameTextState.isEmpty()) {
                        viewModel.changeNameFieldState(CommonUiState.Error)
                    }
                    if (emailTextState.isEmpty()) {
                        viewModel.changeEmailFieldState(CommonUiState.Error)
                    }
                    if (passwordTextState.isEmpty()) {
                        viewModel.changePasswordFieldState(CommonUiState.Error)
                    }
                    if (repeatPasswordTextState.isEmpty()) {
                        viewModel.changeRepeatPasswordFieldState(CommonUiState.Error)
                    }

                    if (nameTextState.isEmpty()
                        || emailTextState.isEmpty()
                        || passwordTextState.isEmpty()
                        || repeatPasswordTextState.isEmpty()
                    ) {
                        viewModel.changeUiScreenState(RegistrationUiState.EmptyFields)
                        return@CommonButton
                    }

                    viewModel.register(
                        name = nameTextState,
                        email = emailTextState,
                        password = passwordTextState,
                    )
                },
                modifier = Modifier.padding(bottom = 80.dp),
                color = Blue
            )

        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun RegistrationScreenPreview() {
//    RegistrationScreen({}, RegistrationViewModel())
//}