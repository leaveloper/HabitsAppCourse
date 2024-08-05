package com.leaveloper.habitsappcourse.home.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.leaveloper.habitsappcourse.home.data.local.entity.HabitEntity
import com.leaveloper.habitsappcourse.home.data.local.entity.HabitSyncEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HomeDao {
    /*fun getAllHabitsForSelectedDate(date: ZonedDateTime): Flow<List<Habit>>
    suspend fun insertHabit(habit: Habit)
    suspend fun getHabitById(id: String): Habit*/

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habitEntity: HabitEntity)

    @Query("SELECT * FROM HabitEntity WHERE id = :id")
    suspend fun getHabitById(id: String): HabitEntity

    @Query("SELECT * FROM HabitEntity WHERE startDate <= :date ORDER BY id ASC")
    fun getAllHabitsForSelectedDate(date: Long) : Flow<List<HabitEntity>>

    @Query("SELECT * FROM HabitEntity")
    fun getAllHabits() : List<HabitEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabitSync(habitSyncEntity: HabitSyncEntity)

    @Query("SELECT * FROM HabitSyncEntity")
    fun getAllHabitsSync() : List<HabitSyncEntity>

    @Delete
    suspend fun deleteHabitSync(habitSyncEntity: HabitSyncEntity)
}