package com.example.gymtracker.data.repository

import com.example.gymtracker.data.dao.BodyWeightEntryDao
import com.example.gymtracker.data.entity.BodyWeightEntry
import kotlinx.coroutines.flow.Flow

class BodyWeightRepository(
    private val bodyWeightEntryDao: BodyWeightEntryDao
) {
    fun getAllEntries(): Flow<List<BodyWeightEntry>> = bodyWeightEntryDao.getAllEntries()
    fun getLatestEntry(): Flow<BodyWeightEntry?> = bodyWeightEntryDao.getLatestEntry()
    fun getEntriesBetween(startDate: String, endDate: String): Flow<List<BodyWeightEntry>> =
        bodyWeightEntryDao.getEntriesBetween(startDate, endDate)
    suspend fun getEntryForDate(date: String): BodyWeightEntry? = bodyWeightEntryDao.getEntryForDate(date)
    suspend fun insertEntry(entry: BodyWeightEntry): Long = bodyWeightEntryDao.insert(entry)
    suspend fun updateEntry(entry: BodyWeightEntry) = bodyWeightEntryDao.update(entry)
    suspend fun deleteEntry(entry: BodyWeightEntry) = bodyWeightEntryDao.delete(entry)
    suspend fun deleteEntryById(id: Long) = bodyWeightEntryDao.deleteById(id)
}
