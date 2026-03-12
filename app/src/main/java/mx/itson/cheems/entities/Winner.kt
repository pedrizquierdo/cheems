package mx.itson.cheems.entities

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import mx.itson.cheems.persistence.CheemsDB

class Winner {

    var id = 0
    var name : String = ""
    var nickname : String = ""

    constructor()

    constructor(id: Int, name: String, nickname: String){
        this.id = id
        this.name = name
        this.nickname = nickname
    }

    fun save(context: Context, name: String, nickname: String): Boolean {
        return try {
            val cheemsDB = CheemsDB(context, "cheems.db", null, 1)
            val db : SQLiteDatabase = cheemsDB.writableDatabase

            val values = ContentValues()
            values.put("name", name)
            values.put("nickname", nickname)

            val result = db.insert("Winner", null, values)
            db.close()
            result != -1L
        }catch(ex: Exception){
            Log.e("Error saving winner", ex.message.toString())
            false
        }
    }

    fun getAll(context: Context): List<Winner> {
        var winners : MutableList<Winner> = ArrayList()
        try {
            val cheemsDB = CheemsDB(context, "cheems.db", null, 1)
            val db : SQLiteDatabase = cheemsDB.readableDatabase
            val resultSet = db.rawQuery("SELECT id, name, nickname FROM Winner", null)

            while(resultSet.moveToNext()){
                val winner = Winner(resultSet.getInt(0), resultSet.getString(1), resultSet.getString(2))
                winners.add(winner)
            }

        } catch (ex : Exception){
            Log.e("Error getting winners", ex.message.toString())
        }
        return winners
    }

    }

