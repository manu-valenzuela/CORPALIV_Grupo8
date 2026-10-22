@Composable
fun HomeScreen2() {

    val windowSizeClass = obtenerWindowSizeClass()

    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Small -> HomeScreenS()
        WindowWidthSizeClass.Medium -> HomeScreenM()
        WindowWidthSizeClass.Large -> HomeScreenL()
    }
}