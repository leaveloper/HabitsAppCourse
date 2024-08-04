package com.leaveloper.habitsappcourse.home.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.leaveloper.habitsappcourse.home.data.local.entity.HabitEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HomeDao {
    /*fun getAllHabitsForSelectedDate(date: ZonedDateTime): Flow<List<Habit>>
    suspend fun insertHabit(habit: Habit)
    suspend fun getHabitById(id: String): Habit*/

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habitEntity: HabitEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabits(habitsEntities: List<HabitEntity>)

    @Query("SELECT * FROM HabitEntity WHERE id = :id")
    fun getHabitById(id: String): HabitEntity

    @Query("SELECT * FROM HabitEntity WHERE startDate <= :date")
    fun getAllHabitsForSelectedDate(date: Long) : Flow<List<HabitEntity>>
}