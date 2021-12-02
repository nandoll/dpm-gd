package pe.com.mipredio.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import pe.com.mipredio.model.TaskModel;
import pe.com.mipredio.utils.Consts;

public class TaskDBHelper extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "tbl_task";

    public static final String ID = "id";


    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" +
            " id INTEGER PRIMARY KEY AUTOINCREMENT , " +
            " nroMedidor text ," +
            " ubigeo text ," +
            " contacto  text ," +
            " nroDocumento  text ," +
            " direccion  text ," +
            " estado text ," +
            " medicion text ," +
            " observacion text ," +
            " foto text ," +
            " fecha text ," +
            " latitud text ," +
            " longitud text ," +
            " situacion text ," +
            " horaRegistro text " +
            ")";

    public TaskDBHelper(@Nullable Context context) {
        super(context, Consts.DB_NAME, null, Consts.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.e("CREAR_TABLA", "Creando la tabla" + CREATE_TABLE);
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long insertRecordSQLite(
            String nroMedidor, String ubigeo, String contacto, String nroDocumento,
            String direccion, String medicion, String observacion, String foto,
            String fecha, String latitud, String longitud
    ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nroMedidor", nroMedidor);
        values.put("ubigeo", ubigeo);
        values.put("contacto", contacto);
        values.put("nroDocumento", nroDocumento);
        values.put("direccion", direccion);
        values.put("medicion", medicion);
        values.put("observacion", observacion);
        values.put("foto", foto);
        values.put("fecha", fecha);
        values.put("latitud", latitud);
        values.put("longitud", longitud);
        values.put("estado", "registrado");
        long id = db.insert(TABLE_NAME, null, values);
        db.close();
        return id;
    }

    public int getRecordCount(String fecha) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE fecha = '" + fecha + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public TaskModel getTaskById(String id) {
        Cursor cursor = null;
        TaskModel taskModel = null;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE id = '" + id + "'", null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                taskModel = new TaskModel(
                        id,
                        cursor.getString(cursor.getColumnIndex("direccion")),
                        cursor.getString(cursor.getColumnIndex("contacto")),
                        cursor.getString(cursor.getColumnIndex("latitud")),
                        cursor.getString(cursor.getColumnIndex("longitud")),
                        cursor.getString(cursor.getColumnIndex("nroDocumento")),
                        cursor.getString(cursor.getColumnIndex("medicion")),
                        cursor.getString(cursor.getColumnIndex("observacion")),
                        null,
                        null,
                        cursor.getString(cursor.getColumnIndex("ubigeo")),
                        cursor.getString(cursor.getColumnIndex("estado")),
                        null,
                        cursor.getString(cursor.getColumnIndex("nroMedidor")),
                        null,
                        cursor.getString(cursor.getColumnIndex("foto"))
                );
            }
            return taskModel;
        } finally {
            cursor.close();
        }
    }

    public List<TaskModel> getListTaskSQLite(String fecha) {
        ArrayList<TaskModel> lista = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE  fecha = '" + fecha + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {

                TaskModel modelRecord = new TaskModel(
                        cursor.getString(cursor.getColumnIndex("id")),
                        cursor.getString(cursor.getColumnIndex("direccion")),
                        cursor.getString(cursor.getColumnIndex("horaRegistro")),
                        cursor.getString(cursor.getColumnIndex("ubigeo")),
                        cursor.getString(cursor.getColumnIndex("estado")),
                        cursor.getString(cursor.getColumnIndex("nroMedidor"))
                );
                lista.add(modelRecord);
            } while (cursor.moveToNext());
        }
        return lista;
    }

    /*
    public ArrayList<TaskModel> getListTaskSQLite(String fecha) {
        ArrayList<TaskModel> lista = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE  fecha = '" + fecha + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                TaskModel modelRecord = new TaskModel(
                        cursor.getString(cursor.getColumnIndex("id")),
                        cursor.getString(cursor.getColumnIndex("direccion")),
                        cursor.getString(cursor.getColumnIndex("horaRegistro")),
                        cursor.getString(cursor.getColumnIndex("ubigeo")),
                        cursor.getString(cursor.getColumnIndex("estado")),
                        cursor.getString(cursor.getColumnIndex("nroMedidor"))
                );
                lista.add(modelRecord);
            } while (cursor.moveToNext());
        }
        return lista;
    }
    */


}
