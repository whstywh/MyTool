package com.example.mytool.room

import androidx.room.*
import io.reactivex.Maybe
import io.reactivex.Observable


/**
 * Created by whstywh on 2019/4/19 .
 * description：定义访问数据（增删改查）的接口
 */
@Dao
interface RoomDao {


    /*TODO:增*/
    /**
     * 返回 long、long[] 或 List<Long>类型的 rowId，
     *
     * OnConflictStrategy.REPLACE:发生冲突时替换原有数据
     */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(b: BookTable): Maybe<Long>

    @Insert
    fun insert(b: List<BookTable>): Maybe<List<Long>>


    /*TODO:改*/

    /*返回 int 型的数据库更新行数*/
    @Update
    fun update(l: BookTable): Maybe<Int>

    /*TODO:删*/

    /*返回 int 型的数据库删除行数*/
    @Delete
    fun delete(l: BookTable): Maybe<Int>

    /*TODO:查*/

    /**
     * Room 会在编译时验证这个方法，所以如果查询有问题编译时就会报错。
     * Room 还会验证查询的返回值，如果查询响应的字段名和返回对象的字段名不匹配，
     * 如果有些字段不匹配，你会看到警告，如果所有字段都不匹配，你会看到 error
     */
    @Query("SELECT * FROM book_table")
    fun querybook(): Observable<List<BookTable>>

    /*条件查询， : 后加 参数名 的方式获取参数值*/
    @Query("SELECT * FROM book_table WHERE  book_name = :name")
    fun querywhere(name: String): Maybe<BookTable>

    @Query("SELECT * FROM book_table WHERE  book_name IN (:name)")
    fun querywherein(name: List<String>): Maybe<List<BookTable>>

    /*模糊查询*/
    @Query("SELECT * FROM book_table WHERE book_name LIKE '%' || :name || '%' ")
    fun queryLike(name: String): Maybe<List<BookTable>>



    /*TODO: 复杂数据-查*/

    @Transaction
    @Query("SELECT * FROM ComplexDataTable")
    fun queryComplex(): Maybe<TableWithCData>

    /*TODO: 复杂数据
    *
    * 添加数据要先添加ComplexDataTable，然后根据ComplexDataTable 的id添加CData，
    * 删除数据，要先根据ComplexDataTable的id删除CData表中的数据。
    * */



}