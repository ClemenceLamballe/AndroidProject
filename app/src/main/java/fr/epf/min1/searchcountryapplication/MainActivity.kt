package fr.epf.min1.searchcountryapplication

import Country
import CountryService
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
//import fr.epf.min1.searchcountryapplication.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Response
import retrofit2.Call
import retrofit2.Callback


class MainActivity : ComponentActivity() {

    private lateinit var searchField: EditText
    private lateinit var searchByCapitalButton: Button
    private lateinit var searchByCountryButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var countryService: CountryService
    private val countryNames = mutableStateOf(emptyList<String>())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                SearchCountryContent()
            }
        }

        searchField = findViewById(R.id.searchField)
        searchByCapitalButton = findViewById(R.id.searchByCapitalButton)
        searchByCountryButton = findViewById(R.id.searchByCountryButton)
        recyclerView = findViewById(R.id.recyclerView)


        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialisation de Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://restcountries.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // Créer une instance de CountryService
        countryService = retrofit.create(CountryService::class.java)

        searchByCapitalButton.setOnClickListener {
            val query = searchField.text.toString()
            searchByCapital(query)
        }

        searchByCountryButton.setOnClickListener {
            val query = searchField.text.toString()
            //searchByCountry(query)
        }
    }

    private fun searchByCapital(capital: String) {
        val call = countryService.searchByCapital(capital)
        call.enqueue(object : Callback<List<Country>> {
            override fun onResponse(call: Call<List<Country>>, response: Response<List<Country>>) {
                if (response.isSuccessful) {
                    val countries = response.body()
                    countries?.let {
                        val countryNames = it.map { country -> country.name }
                        this@MainActivity.countryNames.value = countryNames
                    }
                } else {
                    // Gérer les erreurs de réponse
                    Toast.makeText(this@MainActivity, "Failed to fetch data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Country>>, t: Throwable) {
                // Gérer les erreurs de connexion réseau
                Toast.makeText(this@MainActivity, "Network error", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun searchByCountry(name: String) {
        val call = countryService.searchByCountry(name)
        call.enqueue(object : Callback<List<Country>> {
            override fun onResponse(call: Call<List<Country>>, response: Response<List<Country>>) {
                if (response.isSuccessful) {
                    val countries = response.body()
                    // Mettre à jour l'UI avec les données récupérées
                } else {
                    // Gérer les erreurs de réponse
                    Toast.makeText(this@MainActivity, "Failed to fetch data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Country>>, t: Throwable) {
                // Gérer les erreurs de connexion réseau
                Toast.makeText(this@MainActivity, "Network error", Toast.LENGTH_SHORT).show()
            }
        })
    }





}





@Composable
fun SearchCountryContent() {
    Text(text = "Search Country Application")
}

@Composable
fun CountryList(countryNames: MutableState<List<String>>) {
    LazyColumn {
        items(countryNames.value) { countryName ->
            Text(
                text = countryName,
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            SearchCountryContent()
        }
    }
}
