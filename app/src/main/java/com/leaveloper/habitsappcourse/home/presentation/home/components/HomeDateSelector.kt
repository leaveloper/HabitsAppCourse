package com.leaveloper.habitsappcourse.home.presentation.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.ZonedDateTime

/*
* ZonedDateTime:
*
* Inependientemente de la zona horaria,
* siempre se utiliza el mismo horario
*
* ------------
* LocalDateTime
*
* La hora depende de la zona horaria
*
* */

@Composable
fun HomeDateSelector(
    selectedDate: ZonedDateTime,
    mainDate: ZonedDateTime,
    onDateClick: (ZonedDateTime) -> Unit,
    modifier: Modifier = Modifier,
    datesToShow: Int = 4 // Cantidad - 1
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        for (i in datesToShow downTo 0) {
            val date = mainDate.minusDays(i.toLong())
            HomeDateItem(
                date = date,
                isSelected = selectedDate.toLocalDate() == date.toLocalDate()
            ) {
                onDateClick(date)
            }
        }
    }
}