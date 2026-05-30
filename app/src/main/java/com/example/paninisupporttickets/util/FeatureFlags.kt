package com.example.paninisupporttickets.util

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object FeatureFlags {
    private val _enableTicketCreation = MutableStateFlow(true)
    val enableTicketCreation: StateFlow<Boolean> = _enableTicketCreation.asStateFlow()

    private val _enablePriorityUpdate = MutableStateFlow(true)
    val enablePriorityUpdate: StateFlow<Boolean> = _enablePriorityUpdate.asStateFlow()

    private val _enableCategoryFilter = MutableStateFlow(false)
    val enableCategoryFilter: StateFlow<Boolean> = _enableCategoryFilter.asStateFlow()

    fun setTicketCreationEnabled(enabled: Boolean) {
        _enableTicketCreation.value = enabled
    }

    fun setPriorityUpdateEnabled(enabled: Boolean) {
        _enablePriorityUpdate.value = enabled
    }

    fun setCategoryFilterEnabled(enabled: Boolean) {
        _enableCategoryFilter.value = enabled
    }
}
