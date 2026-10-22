@OptIn(ExperimentalMaterial3Api::class)
@Composable

//Esta versión tiene 3 columnas.
fun HomeScreenL() {

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
        ) {

            // Menú lateral
            NavigationRail {
                NavigationRailItem(
                    selected = true,
                    onClick = { },
                    icon = { Icon(Icons.Default.Home, null) },
                    label = { Text("Inicio") }
                )

                NavigationRailItem(
                    selected = false,
                    onClick = { },
                    icon = { Icon(Icons.Default.Settings, null) },
                    label = { Text("Config.") }
                )
            }

            // Contenido principal
            Column(
                modifier = Modifier
                    .weight(2f)
                    .fillMaxHeight()
                    .padding(32.dp),
                verticalArrangement = Arrangement.Center
            ) {

                Text(
                    text = "¡Bienvenido!",
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Esta es una versión optimizada para pantallas grandes.",
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = { /* acción futura */ }
                ) {
                    Text("Presióname")
                }
            }

            // Panel derecho
            Card(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(24.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Logo App",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                }
            }
        }
    }
}