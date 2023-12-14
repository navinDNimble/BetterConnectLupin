package com.nimble.lupin.pu_manager.utils

import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class PaginationScrollListener(
    private var layoutManager: LinearLayoutManager
) : RecyclerView.OnScrollListener() {

    abstract fun isLastPage(): Boolean
    abstract fun isLoading(): Boolean

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

//        val visibleItemCount = layoutManager.childCount
//        val totalItemCount = layoutManager.itemCount
//        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
//
//        if (isLoading().not() && isLastPage().not()) {
//            if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
//                loadMoreItems()
//            }
//        }
    }
    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

        if (isLoading().not() && isLastPage().not()) {
            if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
                loadMoreItems()
            }
        }else{
            Log.e("sachin", "Not Able to load")
        }
    }
    abstract fun loadMoreItems()
}