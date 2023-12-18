package com.example.myapplication.details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import com.example.myapplication.R
import com.example.myapplication.UiState
import com.example.myapplication.repository.model.Game
import com.example.myapplication.ui.theme.MyApplicationTheme

class DetailsActivity : ComponentActivity() {

    private val viewModel: DetailsViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val id = intent.getStringExtra("CUSTOM_ID") ?: "error"
        Toast.makeText(this, "$id", Toast.LENGTH_SHORT).show()
        Log.d("DetailsActivity", "Received ID: $id")
        viewModel.loadDetailsData(id)

        setContent {
            MyApplicationTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    DetailsView(viewModel = viewModel)
                }
            }
        }

    }
}

@Composable
fun DetailsView(viewModel: DetailsViewModel) {
    val uiState by viewModel.immutableGameData.observeAsState(UiState())
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
            uiState.data?.let { game ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    item {
                        GameView(game = game)
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
fun GameView(game: Game) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)
            .fillMaxHeight()

    ) {
        AsyncImage(
            model = game.thumbnail,
            contentDescription = game.title,
            placeholder = painterResource(R.drawable.placeholder),
            modifier = Modifier
                .size(300.dp)
                .clip(MaterialTheme.shapes.medium)
                .align(Alignment.CenterHorizontally)
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
                text = game.description,
                fontSize = 15.sp,
                fontStyle = FontStyle.Italic
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Platform: " + game.platform,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "GameUrl: " + game.gameUrl,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
    Spacer(modifier = Modifier.height(12.dp))
}
