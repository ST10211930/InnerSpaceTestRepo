package student.projects.innerspace.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkModule {
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://type.fit/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val quoteApi: QuoteApi = retrofit.create(QuoteApi::class.java)
}
