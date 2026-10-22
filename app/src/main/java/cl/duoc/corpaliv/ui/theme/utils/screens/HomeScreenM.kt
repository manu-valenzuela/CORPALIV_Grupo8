@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenMedium() {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi App Kotlin") }
            )
        }
    ) { innerPadding ->

        Row(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(24.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            // Panel izquierdo
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {

                Text(
                    text = "¡Bienvenido!",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { /* acción futura */ }
                ) {
                    Text("Presióname")
                }
            }

            // Panel derecho
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo App",
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                contentScale = ContentScale.Fit
            )
        }
    }
}