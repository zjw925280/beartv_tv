package com.gys.play.db

import androidx.lifecycle.*
import com.dm.cartoon.sql.Search
import com.dm.cartoon.sql.SearchRepository
import kotlinx.coroutines.launch

class SqlSearchViewModel(private val repository: SearchRepository) : ViewModel() {
    val allData: LiveData<List<Search>> = repository.allDatas.asLiveData()

    fun insert(data: Search) = viewModelScope.launch { repository.insert(data) }
    fun deletall() = viewModelScope.launch { repository.deletall() }
}

class SqlSearchFactory(private val repository: SearchRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SqlSearchViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SqlSearchViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}