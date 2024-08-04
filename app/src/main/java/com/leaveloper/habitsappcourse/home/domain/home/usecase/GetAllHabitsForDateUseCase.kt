package com.leaveloper.habitsappcourse.home.domain.home.usecase

import com.leaveloper.habitsappcourse.home.domain.models.Habit
import com.leaveloper.habitsappcourse.home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import java.time.ZonedDateTime

class GetAllHabitsForDateUseCase(private val repository: HomeRepository) {
    operator fun invoke(date: ZonedDateTime): Flow<List<Habit>> {
        return repository.getAllHabitsForSelectedDate(date)
    }
}