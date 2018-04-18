package com.android.freelance.sample.util

import android.util.Log
import com.android.freelance.sample.model.Article
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.*

/**
 * Created by Kyaw Khine on 12/05/2017.
 */
object QueryUtils {

    private val LOG_TAG = QueryUtils::class.java!!.getName()
    private val Log_Tag = QueryUtils::class.java.simpleName

    fun fetchArticleData(requestUrl: String): List<Article>? {
        Log.i(LOG_TAG, "TEST: fetchArticleData() called...")

        val url = createUrl(requestUrl)

        var jsonResponse: String? = null
        try {

            jsonResponse = getResponseFromHttpUrl(url)
        } catch (e: IOException) {

            Log.e(Log_Tag, "Problem making the HTTP request.", e)
        }

        return extractFeatureFromJson(jsonResponse)
    }

    private fun createUrl(requestUrl: String): URL? {
        Log.i(LOG_TAG, "TEST: createUrl() called...")

        var url: URL? = null  // <---> It's used to import java.net.URL

        try {
            url = URL(requestUrl)
        } catch (e: MalformedURLException) {
            Log.e(Log_Tag, "Problem building the URL.", e)
        }

        return url
    }

    @Throws(IOException::class)
    private fun getResponseFromHttpUrl(url: URL?): String? {
        Log.i(LOG_TAG, "TEST: getResponseFromHttpUrl() called...")

        // All the Question Mark is interested the null pointer exception throw the null
        val urlConnection = url?.openConnection() as HttpURLConnection?
        // url = it's object, ? = that isn't null, .openConnection() as HttpURLConnection = check & execute the url

        try {
            if (urlConnection?.responseCode == HttpURLConnection.HTTP_OK) {
                val inputStream = urlConnection.inputStream

                val scanner = Scanner(inputStream)
                scanner.useDelimiter("\\A")

                if (scanner.hasNext()) {
                    return scanner.next()
                }
            } else {
                Log.e(Log_Tag, "Error response code: " + urlConnection?.responseCode)
            }
        } finally {
            urlConnection?.disconnect()
        }

        return null
    }

    private fun extractFeatureFromJson(jsonResponse: String?): List<Article>? {
        Log.i(LOG_TAG, "TEST: extractFeatureFromJson() called...")

        if (jsonResponse == null || jsonResponse.isEmpty()) {
            return null
        }

        val articles = mutableListOf<Article>()

        try {
            val baseJsonResponse = JSONObject(jsonResponse)
            val articleArray = baseJsonResponse.getJSONArray("articles")

            for (i in 0..articleArray.length() - 1) {
                val currentArticle = articleArray.getJSONObject(i)

                val title = if (currentArticle.has("title")) {
                    currentArticle.getString("title")
                } else {
                    "null"
                }

                val url = if (currentArticle.has("url")) {
                    currentArticle.getString("url")
                } else {
                    "null"
                }

                articles.add(Article(title, url))
            }
        } catch (e: JSONException) {
            Log.e(Log_Tag, "Problem parsing the article JSON results.", e)
        }
        return articles
    }
}