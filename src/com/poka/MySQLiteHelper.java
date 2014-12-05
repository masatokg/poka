package com.poka;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class MySQLiteHelper extends SQLiteOpenHelper  {

	public MySQLiteHelper(Context context){
		super(context,"1201755.sqlite3",null,5);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO 自動生成されたメソッド・スタブ
		db.execSQL("CREATE TABLE IF NOT EXISTS"+
		" Serifu( _id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,phrase TEXT )");


	}



	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO 自動生成されたメソッド・スタブ
		db.execSQL("drop table Serifu;");
		onCreate(db);
	}



	public void insertSerifu (SQLiteDatabase db,String inputMsg){
	String sqlstr = "insert into Serifu (phrase)values(' "+ inputMsg +" ');";
	try{
		db.beginTransaction();
		db.execSQL(sqlstr);

		db.setTransactionSuccessful();
	}catch (SQLException e){
		Log.e("ERROR",e.toString());
		}finally{
			db.endTransaction();
		}
	return;

	}

	public SQLiteCursor selectSerifuList(SQLiteDatabase db){
		SQLiteCursor cursor = null;

		String sqlstr = "SELECT _id,phrase FROM Serifu ORDER BY _id;";
		try{
			cursor = (SQLiteCursor)db.rawQuery(sqlstr,null);
			if(cursor.getCount()!=0){

				cursor.moveToFirst();
			}
		} catch (SQLException e){
			Log.e("ERROR",e.toString());
		} finally{

		}
		return cursor;
	}
	public void deleteSerifu(SQLiteDatabase db,int id){

		String sqlstr = "DELETE FROM Serifu where _id = " + id +";";
		try{
			db.beginTransaction();
			db.execSQL(sqlstr);

			db.setTransactionSuccessful();
		}catch (SQLException e){
			Log.e("ERROR",e.toString());
		}finally {
			db.endTransaction();

		}
	}
}