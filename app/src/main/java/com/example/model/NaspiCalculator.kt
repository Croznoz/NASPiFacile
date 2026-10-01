package com.example.model

import java.util.Locale
import kotlin.math.min
import kotlin.math.roundToInt

object NaspiCalculator {

    /**
     * Calcola durata e importi secondo la circolare INPS n. 94/2015 e n. 25/2024
     */
    fun calculate(
        weeksWorked: Int,
        totalTaxableSalary4Years: Double,
        isOver55: Boolean
    ): SimulationResult {
        // Durata: settimane / 2 (fino a max 104 settimane = 24 mesi)
        val validWeeks = min(weeksWorked, InpsConstants.SETTIMANE_MASSIME_CALCOLO)
        val durationWeeks = min(validWeeks / 2, InpsConstants.SETTIMANE_MASSIME_DURATA)
        val durationMonths = durationWeeks / 4.33

        // Retribuzione media mensile: (totale retribuzioni imponibili / settimane) * 4.33
        val averageMonthlySalary = if (validWeeks > 0) {
            (totalTaxableSalary4Years / validWeeks) * InpsConstants.COEFFICIENTE_MESE
        } else {
            0.0
        }

        // Calcolo importo base mensile iniziale
        val calculatedBase = if (averageMonthlySalary <= InpsConstants.SOGLIA_RETRIBUZIONE_RIFERIMENTO) {
            averageMonthlySalary * InpsConstants.PERCENTUALE_BASE
        } else {
            val basePart = InpsConstants.SOGLIA_RETRIBUZIONE_RIFERIMENTO * InpsConstants.PERCENTUALE_BASE
            val excessPart = (averageMonthlySalary - InpsConstants.SOGLIA_RETRIBUZIONE_RIFERIMENTO) * InpsConstants.PERCENTUALE_ECCEDENZA
            basePart + excessPart
        }

        // Applicazione massimale mensile lordo
        val initialGrossMonthly = min(calculatedBase, InpsConstants.MASSIMALE_MENSILE_LORDO)

        // Décalage: mese di inizio riduzione del 3%
        val decalageStartMonth = if (isOver55) 8 else 6

        // Proiezione mese per mese
        val totalMonthsCount = min(24, durationMonths.roundToInt().coerceAtLeast(1))
        val schedule = mutableListOf<MonthlyPaymentProjection>()
        var currentAmount = initialGrossMonthly
        var totalGross = 0.0

        for (month in 1..totalMonthsCount) {
            if (month >= decalageStartMonth) {
                // Riduzione progressiva del 3% ogni mese
                currentAmount *= (1.0 - InpsConstants.PERCENTUALE_DECALAGE)
            }
            val note = when {
                month < decalageStartMonth -> "Importo pieno (100%)"
                month == decalageStartMonth -> if (isOver55) "Inizio Décalage -3% (Agevolazione Over 55)" else "Inizio Décalage -3% (6° mese)"
                else -> "Riduzione -3% progressiva"
            }
            val noteEn = when {
                month < decalageStartMonth -> "Full amount (100%)"
                month == decalageStartMonth -> if (isOver55) "Decalage Start -3% (Over 55 Relief)" else "Decalage Start -3% (6th month)"
                else -> "Progressive -3% reduction"
            }
            schedule.add(
                MonthlyPaymentProjection(
                    monthNumber = month,
                    grossAmount = currentAmount,
                    note = note,
                    noteEn = noteEn
                )
            )
            totalGross += currentAmount
        }

        return SimulationResult(
            weeksWorked = validWeeks,
            durationWeeks = durationWeeks,
            durationMonths = durationMonths,
            averageMonthlySalary = averageMonthlySalary,
            initialGrossMonthly = initialGrossMonthly,
            isOver55 = isOver55,
            decalageStartMonth = decalageStartMonth,
            monthlySchedule = schedule,
            totalEstimatedGross = totalGross
        )
    }

    fun formatCurrency(amount: Double): String {
        return String.format(Locale.ITALY, "€ %.2f", amount)
    }
}
