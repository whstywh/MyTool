package com.example.mytool.room


import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Created by whstywh on 2019/4/19 .
 * description：数据库客户端
 */
@Database(
    entities = [BookTable::class,User::class],
    version = 1
)
@TypeConverters(TypeConverter::class)
abstract class RoomClient : RoomDatabase() {

    abstract fun roomDao(): RoomDao

    companion object {
        @Volatile
        var INSTANCE: RoomClient? = null

        private const val DB_NAME = "whstywh.db"

        fun getInstance(context: Context): RoomClient =
            INSTANCE ?: synchronized(RoomClient::class) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                RoomClient::class.java, DB_NAME
            )
                //启用fallback to destructive migration — 增加版本后，数据库将被清空
//                .fallbackToDestructiveMigration()
//                .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)
                .build()


        /*
        * 因为Room使用的也是SQLite， 所以支持原有Sqlite数据库迁移到Room。
        * 假设原有一个版本号为1的数据库有一张表User, 现在要迁移到Room， 我们需要定义好Entity, DAO, Database,
        * 然后创建Database时添加一个空实现的Migraton即可。
        *
        * 需要注意的是，即使对数据库没有任何升级操作，也需要升级版本，否则会抛异常 IllegalStateException.
        * */
        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
            }
        }

        /*数据库更新*/

        //向book_table表中添加一个新的字段:last_update
        val MIGRATION_2_3: Migration = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE book_table " + " ADD COLUMN last_update INTEGER")
            }
        }

        //把_id从Long类型修改成String类型
        val MIGRATION_3_4: Migration = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Create the new table
                database.execSQL(
                    "CREATE TABLE book_table_new (_id TEXT, name TEXT,dir TEXT, last_update INTEGER, PRIMARY KEY(_id))"
                )

                // Copy the data
                database.execSQL(
                    "INSERT INTO users_new (_id, name,dir, last_update) SELECT _id, name,dir, last_update FROM book_table"
                )

                // Remove the old table
                database.execSQL("DROP TABLE book_table")

                // Change the table name to the correct one
                database.execSQL("ALTER TABLE book_table_new RENAME TO book_table")
            }
        }
    }
}