package com.invictus.img.downloader.ui.screen.auth.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.invictus.img.downloader.ui.component.button.LoadingButton
import com.invictus.img.downloader.ui.component.text.ErrorText
import com.invictus.img.downloader.ui.component.textfield.InputField
import com.invictus.img.downloader.ui.navigation.NavigationAction
import com.invictus.img.downloader.ui.navigation.OnScreenAction
import com.invictus.img.downloader.ui.navigation.screen.ScreenRoute
import imagedownloader.composeapp.generated.resources.Res
import imagedownloader.composeapp.generated.resources.app_name
import imagedownloader.composeapp.generated.resources.compose_multiplatform
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource


@Composable
fun LoginScreen(
    onScreenAction: OnScreenAction,
    viewModel: LoginViewModel,
) {

    LaunchedEffect(Unit) {
        viewModel.logout()
    }
    val state = viewModel.state.collectAsStateWithLifecycle().value
    LoginScreenContent(
        onScreenAction = onScreenAction,
        state = state,
        inputState = viewModel.inputState,
        onLoginClick = {
            viewModel.login {
                onScreenAction(
                    NavigationAction.ChangeGraph(ScreenRoute.Login, ScreenRoute.Home)
                )
            }
        }
    )


}

@OptIn(ExperimentalTextApi::class)
@Composable
fun LoginScreenContent(
    onLoginClick: () -> Unit,
    onScreenAction: OnScreenAction,
    state: LoginScreenState,
    inputState: LoginInputState,
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .systemBarsPadding(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .imePadding()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LogoText()
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Login",
                style = MaterialTheme.typography.headlineSmall
            )
            InputField(
                textFieldState = inputState.usernameState,
                placeholder = "Enter Username",
                label = "Username*",
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Username"
                    )
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next,
                    capitalization = KeyboardCapitalization.Characters
                ),
            )
            val showPassword = remember {
                mutableStateOf(false)
            }
            InputField(
                textFieldState = inputState.passwordState,
                placeholder = "Enter Password",
                label = "Password*",
                visualTransformation = if (showPassword.value) VisualTransformation.None
                else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Password
                ),
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    if (showPassword.value) {
                        IconButton(onClick = { showPassword.value = false }) {
                            Icon(
                                imageVector = Icons.Filled.Visibility,
                                contentDescription = "hide password"
                            )
                        }
                    } else {
                        IconButton(onClick = { showPassword.value = true }) {
                            Icon(
                                imageVector = Icons.Filled.VisibilityOff,
                                contentDescription = "show password"
                            )
                        }
                    }
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Password,
                        contentDescription = "Password"
                    )
                },
            )

            val uriHandler = LocalUriHandler.current
            val textColor = LocalContentColor.current
            val textStyle = MaterialTheme.typography.titleMedium.copy(color = textColor)
            val text = remember {
                buildAnnotatedString {
                    withStyle(
                        textStyle.toSpanStyle()
                    ) {
                        append("By clicking, I accept the ")
                        withAnnotation(urlAnnotation = UrlAnnotation("https://www.quivertech.in/terms-of-service")) {
                            withStyle(
                                SpanStyle(
                                    fontWeight = FontWeight.Bold,
                                    textDecoration = TextDecoration.Underline
                                )
                            ) {
                                append("terms of service")
                            }
                        }
                        append(" and ")
                        withAnnotation(urlAnnotation = UrlAnnotation("https://www.quivertech.in/privacy-policy")) {
                            withStyle(
                                SpanStyle(
                                    fontWeight = FontWeight.Bold,
                                    textDecoration = TextDecoration.Underline
                                )
                            ) {
                                append("privacy policy")
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
            ) {
                Checkbox(
                    checked = inputState.forAccepted,
                    onCheckedChange = inputState::onAccepted
                )
                Text(
                    text = text,
                    modifier = Modifier.padding(end = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            ErrorText(errorMessage = state.error)
            Spacer(modifier = Modifier.height(8.dp))

            LoadingButton(
                fillWidth = true,
                enabled = remember(
                    state,
                    inputState.usernameState.isValid,
                    inputState.passwordState.isValid,
                    inputState.forAccepted
                ) {
                    derivedStateOf {
                        state.loading.not()
                                && inputState.usernameState.isValid
                                && inputState.passwordState.isValid
                                && inputState.forAccepted

                    }
                }.value,
                loading = state.loading,
                text = "Login",
                onClick = onLoginClick
            )

            /*     InTextButton(
                     onClick = {
                         onScreenAction(
                             NavigationAction.Navigate(AuthRoute.ForgetPassword)
                         )
                     }
                 ) {
                     Text(
                         text = "Forgot Password?"
                     )
                 }*/

            /*          Row(verticalAlignment = Alignment.CenterVertically) {
                          Text(
                              text = "Don't have an account",
                              style = MaterialTheme.typography.titleSmall
                          )
                          TextButton(
                              onClick = {
                                  onScreenAction(
                                      NavigationAction.Navigate(AuthRoute.Signup)
                                  )
                              }
                          ) {
                              Text(text = "Create one")
                          }
                      }*/
        }

        PoweredByInvictusItem(modifier = Modifier.fillMaxWidth())

    }
}

@Composable
fun LogoText() {
    Column {
        Text(
            text = buildAnnotatedString {
                withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                    append("Quiver")
                }
                append(" ")
                withStyle(SpanStyle(color = MaterialTheme.colorScheme.secondary)) {
                    append("Tech")
                }
            },
            style = MaterialTheme.typography.displayMedium
        )
        Text(
            text = "A unit of Invictus Digisoft Pvt. Ltd.",
            style = MaterialTheme.typography.titleSmall.copy(fontSize = 13.4.sp),
            modifier = Modifier.offset(y = -(10.dp), x = (32.dp))
        )
    }
}

@Composable
fun PoweredByInvictusItem(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Powered by",
            style = MaterialTheme.typography.titleSmall
        )
        Spacer(modifier = Modifier.width(4.dp))
        Icon(
            painter = painterResource(Res.drawable.compose_multiplatform),
            contentDescription = stringResource(Res.string.app_name),
            modifier = Modifier.size(32.dp),
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "Invictus Digisoft Pvt. Ltd.",
            fontWeight = FontWeight.Bold
        )

    }
}
