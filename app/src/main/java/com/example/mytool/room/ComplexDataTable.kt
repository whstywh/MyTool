package com.example.mytool.room

import androidx.room.*

/**
 * Created by whstywh on 2019/5/20.
 * description：
 */
@Entity
data class ComplexDataTable(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 1,
    @Ignore
    var datas: List<CData>? = null
)

@Entity
data class CData(
    @PrimaryKey(autoGenerate = true)
    var id: Long,
    var data: String,
    var t_id: Long
)


data class TableWithCData(
    @Embedded
    var table: ComplexDataTable,
    @Relation(parentColumn = "id", entityColumn = "t_id")
    var datas: List<CData>
)