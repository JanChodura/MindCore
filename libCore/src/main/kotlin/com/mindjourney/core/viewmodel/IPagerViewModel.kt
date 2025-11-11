package com.mindjourney.core.viewmodel

import kotlinx.coroutines.flow.StateFlow

interface IPagerViewModel {

    val currentPageId: StateFlow<Int>
    fun onPageChangedByView(pageId: Int)
}
