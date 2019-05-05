
package com.example.mytool.room

import androidx.room.*
import androidx.room.ForeignKey.CASCADE

/**
 * Created by whstywh on 2019/4/19.
 * description：
 */
@Entity(
    //表名，默认值为类名
    tableName = "book_table",
    /*
     * 添加索引以加快查询速度，
     * 可以使用 @Entity 注解的 indices 属性创建索引，
     * 可以使用 @Index 注解的 unique 属性设置为 true 来强制这个字段唯一。
     */
    indices = [Index(value = ["book_name"], unique = true),
        Index(value =["book_user_id"],unique = true)],
    /*
    * ForeignKey中的 onDelete()和 onUpdate(), 通过这两个属性的值来设置当User对象被删除／更新时，Book对象作出的响应。
    *
    * 这两个属性的可选值如下：
    * CASCADE：User删除时对应Book一同删除； 更新时，关联的字段一同更新
    * NO_ACTION：User删除时不做任何响应
    * RESTRICT：禁止User的删除／更新。当User删除或更新时，Sqlite会立马报错。
    * SET_NULL：当User删除时， Book中的userId会设为NULL
    * SET_DEFAULT：与SET_NULL类似，当User删除时，Book中的userId会设为默认值
    * */
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = arrayOf("user_id"),
        childColumns = arrayOf("book_user_id"),
        onDelete = CASCADE,
        onUpdate = CASCADE
    )]
)
data class BookTable(

    @PrimaryKey(autoGenerate = true)//主键自增长
    @ColumnInfo(name = "_id")
    var id: Long,

    @ColumnInfo(name = "book_name")//字段名
    var name: String ,

    @Ignore//忽略持久化，该字段将不会保存在数据库中
    var author: String,

    //TypeConverter::class 将对List<Dir>类型进行转换保存
    var dir: List<Dir>?,

    @ColumnInfo(name = "book_user_id")
    var userid: Long,

    /*
    * 注解嵌套的对象
    * book_table表中字段:id,name,author,dir,userid,price,unit
    * */
    @Embedded
    var price: Price?

){
    constructor():this(1,"","",null,0,null)
}



data class Dir(
    var name: String
)

@Entity(tableName = "user_table")
data class User(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_id")
    var userid: Long
)

data class Price(
    @Ignore
    var price: Int,
    @Ignore
    var unit: String
){
    constructor():this(1,"")
}