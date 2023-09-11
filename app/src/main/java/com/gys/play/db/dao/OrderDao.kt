package com.gys.play.db.dao

import androidx.room.*
import com.gys.play.db.entity.Order
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {

    @Query("select * from `order` ")
    fun getAll(): Flow<List<Order>>

    @Query("select * from `order` where status =:status")
    fun getAllForStatus(status: Int): List<Order>

    /**
     * REPLACE 直接替换旧的
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(order: Order)

    @Query("update `order` set status =:status where purchaseToken =:purchaseToken")
    fun updateOrderStatus(status: Int, purchaseToken: String)

    @Update
    fun update(order: Order)

}