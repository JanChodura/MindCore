package com.mindjourney.core.viewmodel

import kotlinx.coroutines.flow.StateFlow

interface IPagerViewModel {

    fun onPageChangedByView(pageId: Int)
}
