package com.ashe.essenceidle.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ashe.essenceidle.R
import com.ashe.essenceidle.model.MainActivityViewModel
import com.ashe.essenceidle.ui.components.StatDisplay

@Composable
fun HomeScreen(viewModel: MainActivityViewModel){
    val characterState by viewModel.characterState.collectAsState()
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState()),
    ) {
        //TODO Implement Generic status bar
        Surface(
            color = MaterialTheme.colorScheme.surfaceContainer,
            modifier = Modifier
                .padding(vertical = 5.dp, horizontal = 3.dp)
                .fillMaxWidth()
        ) {
            Column {
                Row(modifier = Modifier.align(Alignment.End)) {
                    Text(
                        MainActivityViewModel.Flags.getUserName(viewModel.contacts),
                        fontSize = 18.sp)
                    Spacer(modifier = Modifier.weight(1f))
                    val painter = painterResource(R.drawable.mixedrainandsunweather)
                    Icon(painter, modifier = Modifier.size(25.dp), contentDescription = "Warning")
                    Text("99°", fontSize = 18.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("Ithaca, New York", fontSize = 18.sp)
                }
            }
        }

        //Stat Display
        //As I understand this is probably the best way to do this right now?
        //By passing these as all primitives it lets Compose to more intelligently
        //skip recomposition
        StatDisplay(
            essenceStrength = characterState.essenceStrength,
            speed = characterState.speed,
            power = characterState.power,
            spirit = characterState.spirit,
            endurance = characterState.endurance,
            speedUnlocked = characterState.speedUnlocked,
            powerUnlocked = characterState.powerUnlocked,
            spiritUnlocked = characterState.spiritUnlocked,
            enduranceUnlocked = characterState.enduranceUnlocked
        )

        //TODO Implement news stuff for reals
        Surface(
            color = MaterialTheme.colorScheme.surfaceContainer,
            modifier = Modifier
                .padding(vertical = 5.dp, horizontal = 3.dp)
                .fillMaxWidth()
        ) {
            LazyRow {
                item {
                    NewsCard(R.drawable.rescueplaceholderimage, "Rescue Efforts begin in Altenhöring","June 13, 2024")
                }
                item {
                    NewsCard(R.drawable.convoyplaceholder, "Commission forces arrive in Limlosta Exclusion Zone","June 11, 2024")
                }
                item {
                    NewsCard(R.drawable.fractalcityplaceholder, "Exploration efforts continue in the FPU","June 6, 2024")
                }
            }
        }
    }
}

@Composable
fun NewsCard(image: Int, title: String, date: String) {
    val painter = painterResource(image)
    ElevatedCard(
        modifier = Modifier
            .width(330.dp)
            .padding(5.dp),
        colors = CardDefaults.cardColors()
    ) {
        Column(
            modifier = Modifier.padding(10.dp)) {
            Image(
                painter = painter,
                contentDescription = "Essence Icon",
                modifier = Modifier.clip(RoundedCornerShape(10.dp))
            )

            Text(title, fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top=10.dp))
            Text(date, modifier = Modifier.padding(top=6.dp))
        }
    }
}