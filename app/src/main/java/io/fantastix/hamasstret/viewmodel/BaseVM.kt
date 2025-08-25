package io.fantastix.hamasstret.viewmodel

import androidx.lifecycle.ViewModel
import javax.inject.Inject

interface AnalyticsService {
    fun logEvent(event: String, params: Map<String, Any>)
}

abstract class BaseVM(): ViewModel() {
    private var analyticsService: AnalyticsService = TODO()
        get() {
            TODO()
        }

    @Inject
    fun initDependencies(analyticsService: AnalyticsService) {
        this.analyticsService = analyticsService
    }
}