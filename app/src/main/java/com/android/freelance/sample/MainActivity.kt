package com.android.freelance.sample

import android.app.LoaderManager
import android.content.AsyncTaskLoader
import android.content.Context
import android.content.Intent
import android.content.Loader
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.Toast
import com.android.freelance.sample.adapter.ArticleAdapter
import com.android.freelance.sample.model.Article
import com.android.freelance.sample.util.QueryUtils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<List<Article>>,
        ArticleAdapter.ListItemClickListener {

    private val LOG_TAG = MainActivity::class.java!!.getName()

    private val mArticleLoaderId = 1
    private val mNewsEndPointUrl = "http://newsapi.org/v1/articles"
    /**
     * private val mNewsEndPointUrl = "https://newsapi.org/v1/articles"
     * Got the Error "javax.net.ssl.SSLHandshakeException: java.security.cert.CertPathValidatorException: Trust anchor for certification path not found."
     **/

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(LOG_TAG, "TEST: onCreate() called...")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rv_articles.layoutManager = LinearLayoutManager(this)
        rv_articles.setHasFixedSize(true)

        runLoaders()
    }

    private fun runLoaders() {
        Log.i(LOG_TAG, "TEST: runLoaders() called...")

        pb_articles.visibility = View.VISIBLE

        val connManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connManager.activeNetworkInfo

        if (networkInfo != null && networkInfo.isConnected) {
            loaderManager.initLoader(mArticleLoaderId, null, this)
        } else {
            pb_articles.visibility = View.INVISIBLE
            Toast.makeText(this, "No Internet Connection.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<List<Article>> {
        Log.i(LOG_TAG, "TEST: onCreateLoader() called...")

        val baseUri: Uri = Uri.parse(mNewsEndPointUrl)
        val uriBuilder: Uri.Builder = baseUri.buildUpon()

        uriBuilder.appendQueryParameter("source", "ars-technica")
        uriBuilder.appendQueryParameter("apiKey", getString(R.string.news_api_key))

        return object : AsyncTaskLoader<List<Article>>(this) {
            override fun onStartLoading() {
                forceLoad()
            }

            override fun loadInBackground(): List<Article>? {
                return QueryUtils.fetchArticleData(uriBuilder.toString())
            }
        }
    }

    override fun onLoadFinished(loader: Loader<List<Article>>?, data: List<Article>) {
        Log.i(LOG_TAG, "TEST: onLoadFinished() called...")

        pb_articles.visibility = View.INVISIBLE
        if (!data.isEmpty() && data != null) {
            rv_articles.adapter = ArticleAdapter(this, data)
        }
    }

    override fun onLoaderReset(loader: Loader<List<Article>>?) {
        Log.i(LOG_TAG, "TEST: onLoaderReset() called...")

    }

    override fun onListItemClick(article: Article) {
        Log.i(LOG_TAG, "TEST: onListItemClick() called...")

        val articleUrl = Uri.parse(article.mUrl)
        val browserIntent = Intent(Intent.ACTION_VIEW, articleUrl)
        startActivity(browserIntent)
    }
}
