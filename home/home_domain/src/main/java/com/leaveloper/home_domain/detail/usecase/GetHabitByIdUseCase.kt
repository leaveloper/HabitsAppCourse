package com.leaveloper.home_domain.detail.usecase

import com.leaveloper.home_domain.models.Habit
import com.leaveloper.home_domain.repository.HomeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetHabitByIdUseCase(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(id: String): Habit {
        // Dispatchers.IO es para llamadas que tengan input u outputs
        // Por ejemplo hacia API, BD

        // Si no se utiliza withContext o se utiliza Dispatchers.MAIN
        // ejecuta en el hilo principal y puede llegar a bloquear
        // ya que, en este caso, la función que llama a este método
        // actualiza el estado en el hilo principal
        return withContext(Dispatchers.IO) {
            repository.getHabitById(id)
        }
    }
}