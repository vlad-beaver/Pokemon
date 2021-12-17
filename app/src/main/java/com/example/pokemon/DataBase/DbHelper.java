package com.example.pokemon.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ProjectDb.db";
    private static final int DATABASE_VERSION = 1;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS TypePokemon ( "
                + "ID_Pokemon      INTEGER  NOT NULL , "
                + "TypePokemon TEXT PRIMARY KEY    NOT NULL);"
        );
        db.execSQL("CREATE TABLE IF NOT EXISTS NamePokemon ("
                + "ID_Pokemon INTEGER NOT NULL, "
                + "Name TEXT PRIMARY KEY   NOT NULL, "
                + "Type  Text  NOT NULL, "
                + "Date DATE    NOT NULL, "
                + "FOREIGN KEY(Type) REFERENCES TypePokemon(TypePokemon) "
                + "ON DELETE CASCADE ON UPDATE CASCADE);"
        );
        db.execSQL("CREATE TABLE IF NOT EXISTS VersionPokemon ("
                + "LastVersion   TEXT NOT NULL, "
                + "Name TEXT  NOT NULL, "
                + "NextVersion  TEXT NOT NULL, "
                + "FOREIGN KEY(Name) REFERENCES NamePokemon(Name) "
                + "ON DELETE CASCADE ON UPDATE CASCADE);"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE TypePokemon;");
        db.execSQL("DROP TABLE NamePokemon");
        db.execSQL("DROP TABLE VersionPokemon;");
        onCreate(db);
    }


    public void addType(SQLiteDatabase db, int ID_Pokemon, String TypePokemon ) {
        ContentValues cv = new ContentValues();
        cv.put("ID_Pokemon", ID_Pokemon);
        cv.put("TypePokemon", TypePokemon);
        db.insert("TypePokemon", null, cv);
        cv.clear();
    }

    public void addNamePokemon(SQLiteDatabase db, int ID_Pokemon, String Name, String Type,  String Date) {
        ContentValues cv = new ContentValues();
        cv.put("ID_Pokemon", ID_Pokemon);
        cv.put("Name", Name);
        cv.put("Type", Type);
        cv.put("Date", Date);
        db.insert("NamePokemon", null, cv);
        cv.clear();
    }

    public void addVersionPokemon(SQLiteDatabase db, String LastVersion, String Name, String NextVersion) {
        ContentValues cv = new ContentValues();
        cv.put("LastVersion", LastVersion);
        cv.put("Name", Name);
        cv.put("NextVersion", NextVersion);
        db.insert("VersionPokemon", null, cv);
        cv.clear();
    }


    public void initDatabase(SQLiteDatabase db) {
        addType(db,1,"вода");
        addType(db,2,"огонь");
        addType(db,3,"земля");
        addType(db,4,"электричество");
        addNamePokemon(db,1,"Пикачу","электричество","2012-11-01");
        addNamePokemon(db,2,"Монферно","огонь","2015-03-12");
        addNamePokemon(db,3,"Вартортл","вода","2002-06-11");
        addNamePokemon(db,4,"Гротл","земля","2005-09-10");
        addNamePokemon(db,5,"Бульбазавр","земля","2004-10-10");
        addNamePokemon(db,6,"Чармелеон","огонь","2009-01-09");
        addNamePokemon(db,7,"Глум","земля","2003-05-03");
        addNamePokemon(db,8,"Поливирл","вода","2007-07-02");
        addNamePokemon(db,9,"Кадабра","электричество","2002-02-08");
        addVersionPokemon(db,"Пичу","Пикачу","Райчу");
        addVersionPokemon(db,"Чимчар","Монферно","Инфернейп");
        addVersionPokemon(db,"Сквиртл","Вартортл","Бластойз");
        addVersionPokemon(db,"Тортвиг","Гротл","Тортерра");
        addVersionPokemon(db,"Чармандер","Чармелеон","Чаризард");
        addVersionPokemon(db,"Одиш","Глум","Вилеплум");
        addVersionPokemon(db,"Поливаг","Поливирл","Политоед");
        addVersionPokemon(db,"Абра","Кадабра","Алаказам");
    }

    public void createViews(SQLiteDatabase db) {
        db.execSQL("drop view if exists PokemonView; ");
        db.execSQL("create view if not exists PokemonView as " +
                "select NamePokemon.Date, NamePokemon.Type, VersionPokemon.LastVersion,VersionPokemon.Name, VersionPokemon.NextVersion" +
                " from NamePokemon join VersionPokemon where NamePokemon.Name=VersionPokemon.Name ;"
        );
    }
}
