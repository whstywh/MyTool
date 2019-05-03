package com.example.mytool.room

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Created by whstywh on 2019/4/19 .
 * description： 添加或者读取时操作List<Dir>集合，但实际是将List<Dir>数据以json字符串形式保存在本地数据库中
 */
class TypeConverter {

    /*
    * list<Dir> -> json
    * */
    @TypeConverter
    fun listToJsonDir(t: List<Dir>): String = Gson().toJson(t)


    /*
    * json -> list<Dir>
    * */
    @TypeConverter
    fun jsonToListDir(j: String): List<Dir> =
        Gson().fromJson<List<Dir>>(j, object : TypeToken<List<Dir>>() {}.type)

}