package com.hero.recipespace.view

sealed class LoadingState {
    object Loading : LoadingState()

    data class Progress(val value: Int) : LoadingState()

    object Hidden : LoadingState()

    object Idle : LoadingState()
}
