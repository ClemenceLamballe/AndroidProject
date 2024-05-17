import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CountryService {
    @GET("v2/name/{name}")
    fun searchByCountry(@Query("name") name: String): Call<List<Country>>

    @GET("v2/capital/{capital}")
    fun searchByCapital(@Query("capital") capital: String): Call<List<Country>>
}
