package com.android.freelance.sample.adapters

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.android.freelance.sample.R
import com.android.freelance.sample.models.Article

/**
 * Created by Kyaw Khine on 12/06/2017.
 */
class ArticleAdapter(val mListItemClickListener: ListItemClickListener,
                     val mArticleData: List<Article>) : RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {

    private val LOG_TAG = ArticleAdapter::class.java!!.getName()

    interface ListItemClickListener {
        fun onListItemClick(article: Article)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ArticleViewHolder {
        Log.i(LOG_TAG, "TEST: onCreateViewHolder() called...")

        val inflater = LayoutInflater.from(parent?.context)
        return ArticleViewHolder(inflater.inflate(R.layout.row_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: ArticleViewHolder?, position: Int) {
        Log.i(LOG_TAG, "TEST: onBindViewHolder() called...")

        holder?.mNewsTitleTV?.text = mArticleData[position].mTitle
    }

    override fun getItemCount(): Int {
        Log.i(LOG_TAG, "TEST: getItemCount() called...")

        return mArticleData.size
    }

    inner class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            Log.i(LOG_TAG, "TEST: onClick() in ArticleViewHolder called...")

            mListItemClickListener.onListItemClick(mArticleData[adapterPosition])
        }

        val mNewsTitleTV = itemView.findViewById<TextView>(R.id.txtTitle)
    }

    /*inner class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mNewsTitleTV: TextView = itemView.findViewById(R.id.txtTitle) as TextView
    }
    Type inference failed: Not enough information to infer parameter T in fun <T : View!> findViewById(p0: Int): T!
    Please specify it explicitly.*/
}