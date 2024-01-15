package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.myapplication.details.DetailsActivity
import com.example.myapplication.repository.model.Game
import com.example.myapplication.ui.theme.MyApplicationTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getData()
        setContent {
            MyApplicationTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Scaffold (
                        topBar = {MyTopView(viewModel = viewModel)}
                    ){ scaffoldPaddings ->
                    MainView(
                        modifier = Modifier.padding(scaffoldPaddings),
                        viewModel = viewModel,
                        onClick = {id -> navigateToDetails(id)})
                    }
                }
            }
        }
    }

    private fun navigateToDetails(id: Int){
        val intent = Intent(this, DetailsActivity::class.java)
        intent.putExtra("CUSTOM_ID", "$id")
        startActivity(intent)
    }
}

@Composable
fun MainView(modifier: Modifier, viewModel: MainViewModel, onClick: (Int) -> Unit) {
    val uiState by viewModel.immutableGamesData.observeAsState(UiState())
    val query by viewModel.filterQuery.observeAsState("")

    when{
       uiState.isLoading  -> {
            CircularProgressIndicator(
                modifier = Modifier.width(24.dp)
            )
        }
        uiState.error != null -> {
            Toast.makeText(LocalContext.current, "${uiState.error}", Toast.LENGTH_SHORT ).show()
        }
        uiState.data != null -> {
            uiState.data?.let { restGames ->
                restGames.filter { it.title.contains(query, true) }.let { games ->
                    LazyColumn(
                        modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .padding(top = 70.dp)
            )  {
                items(games) { game ->
                    GameView(game = game, onClick = {id -> onClick.invoke(id)})
                }
            }
            }
            }
        }
        else ->{
            Log.e("Main View", "żaden stan widoku nie zostal zdefiniowany $uiState")
        }

    }
}

@Composable
fun GameView(game: Game, onClick: (Int) -> Unit) {
    Row(
        modifier = Modifier
            .clickable { onClick.invoke(game.id) }
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)

    ) {
        AsyncImage(
            model = game.thumbnail,
            contentDescription = game.title,
            placeholder = painterResource(R.drawable.placeholder),
            modifier = Modifier
                .size(160.dp)
                .clip(MaterialTheme.shapes.medium)
                .align(Alignment.CenterVertically)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = game.title,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = game.shortDescription,
                fontSize = 15.sp,
                fontStyle = FontStyle.Italic
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Platform: " + game.platform,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
    Spacer(modifier = Modifier.height(12.dp))
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopView(viewModel: MainViewModel) {
    var searchText by remember { mutableStateOf("") }

    SearchBar(
        modifier = Modifier.fillMaxWidth()
            .padding(16.dp),
        query = searchText,
        onQueryChange = { wpisywanyTekst -> searchText = wpisywanyTekst },
        onSearch = { viewModel.updateFilterQuery(it) },
        placeholder = { Text(text = "Wyszukaj...") },
        active = false,
        onActiveChange = { },
        leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
        },
        trailingIcon = {
            Image(
                modifier = Modifier.clickable {
                    searchText = ""
                    viewModel.updateFilterQuery("")
                },
                imageVector = Icons.Default.Clear,
                contentDescription = "Clear"
                )
            }

        ) {

        }
}


//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    val sampleGame = Game(
//        title = "Przykładowa Gra",
//        thumbnail = "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3f/Placeholder_view_vector.svg/310px-Placeholder_view_vector.svg.png",
//        shortDescription = "To jest krótki opis przykładowej gry.",
//        platform = "PC"
//    )
//    MyApplicationTheme {
//        GameView(game = sampleGame)
//    }
//}