package com.example.mytool

import android.app.Instrumentation
import android.content.pm.InstrumentationInfo
import androidx.room.testing.MigrationTestHelper
import org.junit.Rule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Test


/**
 * Created by whstywh on 2019/5/6.
 * description：
 */
class MigrationTest {

//    companion object {
//        private const val TEST_DB = "migration-test"
//    }
//
//    @Rule
//    lateinit var helper: MigrationTestHelper
//
//    init {
//        val helper = MigrationTestHelper(
//            InstrumentationRegistry.getInstrumentation(),
//            MigrationDb::class.java!!.getCanonicalName(),
//            FrameworkSQLiteOpenHelperFactory()
//        )
//
//    }
//
//    @Test
//    fun migrate1To2() {
//        var db: SupportSQLiteDatabase = helper.createDatabase(TEST_DB, 1)
//
//        // db has schema version 1. insert some data using SQL queries.
//        // You cannot use DAO classes because they expect the latest schema.
//        db.execSQL("")
//
//        // Prepare for the next version.
//        db.close()
//
//        // Re-open the database with version 2 and provide
//        // MIGRATION_1_2 as the migration process.
//        db = helper.runMigrationsAndValidate(TEST_DB, 2, true, MIGRATION_1_2)
//
//
//        // MigrationTestHelper automatically verifies the schema changes,
//        // but you need to validate that the data was migrated properly.
//    }

}