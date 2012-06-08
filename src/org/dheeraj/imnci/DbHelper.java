package org.dheeraj.imnci;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper {
	static final String dbName = "mch_android.db";
	static final int version = 1;
	static final String Tag = "DbHelper";
	Context context;

	public DbHelper(Context context) {
		super(context, dbName, null, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sqls[] = {
				"CREATE TABLE abortions(ID integer, place_of_abortion text, date_of_abortion text , exported integer);",
				"CREATE TABLE anc_02(ID integer primary key,anc_date text, anaemia text, complication text,rti_sti text , exported integer);",
				"CREATE TABLE anc_03(ID integer primary key,anc_date text, anaemia text, complication text,rti_sti text , exported integer);",
				"CREATE TABLE anc_04(ID integer primary key,anc_date text, anaemia text, complication text,rti_sti text , exported integer);",
				"CREATE TABLE default_mid(def_mid integer, exported integer);",
				"CREATE TABLE ifa(ID integer ,ifa_date text, anaemia text, complication text,rti_sti text , exported integer);",
				"CREATE TABLE mother_reg(mid integer primary key, mother text, husband text,date_of_birth text, age integer, category text, phone_no integer, blood_group text,LMP text,JSY_Benefits text,abortions integer, exported integer);",
				"CREATE TABLE pictures(mid integer,uri text, exported integer);",
				"CREATE TABLE pnc(ID integer,home_visit text,complication text,PPCM text,checkup text, exported integer);",
				"CREATE TABLE po(ID integer, place_of_delivery text, delivery_type text,complication text, date_of_delivery text, date_of_discharge text,JSY_Benefits text , exported integer);",
				"CREATE TABLE tt1(ID integer primary key,tt_date text, anaemia text, complication text,rti_sti text , exported integer);",
				"CREATE TABLE tt2(ID integer primary key,tt_date text, anaemia text, complication text,rti_sti text , exported integer);",
				"CREATE TABLE ttb(ID integer primary key,tt_date text, anaemia text, complication text,rti_sti text , exported integer);",
				"CREATE TABLE updates(created_at int,ID text,priority integer,title text,message text, exported integer);"
		};
		
		for(String sql: sqls){
			db.execSQL(sql);
			Log.d(Tag, sql);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


	}
}
