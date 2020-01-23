package br.com.carlosti.data.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import org.jetbrains.annotations.NotNull;

import br.com.carlosti.constant.Constants;
import br.com.carlosti.data.models.TypeComplement;

import static java.util.Objects.requireNonNull;

public class TypeComplementDAO extends SQLiteOpenHelper {

    final private String table = "TIPO_COMPLEMENTO";

    public TypeComplementDAO(Context context) {
        super(context, Constants.NAME_DB, null, Constants.VERSION_DB);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS TIPO_COMPLEMENTO (\n" +
                "    EMPRESA                    INTEGER      NOT NULL,\n" +
                "    TIPO_COMPLEMENTO           VARCHAR (3),\n" +
                "    DESCRICAO_TIPO_COMPLEMENTO VARCHAR (30),\n" +
                "    CONSTRAINT PK_TIPO_COMPLEMENTO PRIMARY KEY (\n" +
                "        EMPRESA,\n" +
                "        TIPO_COMPLEMENTO\n" +
                "    )\n" +
                ");";

        db.execSQL(sql);
        this.insertData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public TypeComplement getByPrimaryKeys(Integer empresaCode, String type) {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM TIPO_COMPLEMENTO WHERE EMPRESA=? AND TIPO_COMPLEMENTO=?;";

        String[] args = {String.valueOf(empresaCode), type};

        Cursor cursor = null;
        try {
            cursor = db.rawQuery(sql, args);
        } catch (SQLiteException e) {
            if (requireNonNull(e.getLocalizedMessage()).contains("no such table: TIPO_COMPLEMENTO")) {
                this.onCreate(db);
                cursor = db.rawQuery(sql, args);
            }
        }

        TypeComplement typeComplement = null;
        if (cursor != null) {
            if (cursor.moveToNext())
                typeComplement = getTypeComplement(cursor);
            cursor.close();

            return typeComplement;
        }

        return null;
    }

    @NotNull
    private TypeComplement getTypeComplement(Cursor cursor) {
        TypeComplement type = new TypeComplement();
        type.setEmpresa(cursor.getInt(cursor.getColumnIndexOrThrow("EMPRESA")));
        type.setTipoComplemento(cursor.getString(cursor.getColumnIndexOrThrow("TIPO_COMPLEMENTO")));
        type.setDescricaoTipoComplemento(cursor.getString(cursor.getColumnIndexOrThrow("DESCRICAO_TIPO_COMPLEMENTO")));
        return type;
    }

    private void insertData(SQLiteDatabase db) {
        db.execSQL("INSERT INTO TIPO_COMPLEMENTO(EMPRESA, TIPO_COMPLEMENTO, DESCRICAO_TIPO_COMPLEMENTO) VALUES (1,1,'COR');");
        db.execSQL("INSERT INTO TIPO_COMPLEMENTO(EMPRESA, TIPO_COMPLEMENTO, DESCRICAO_TIPO_COMPLEMENTO) VALUES (1,2,'TAMANHO');");
        db.execSQL("INSERT INTO TIPO_COMPLEMENTO(EMPRESA, TIPO_COMPLEMENTO, DESCRICAO_TIPO_COMPLEMENTO) VALUES (1,3,'ESTAMPA');");
        db.execSQL("INSERT INTO TIPO_COMPLEMENTO(EMPRESA, TIPO_COMPLEMENTO, DESCRICAO_TIPO_COMPLEMENTO) VALUES (1,4,'MATERIAL');");
        db.execSQL("INSERT INTO TIPO_COMPLEMENTO(EMPRESA, TIPO_COMPLEMENTO, DESCRICAO_TIPO_COMPLEMENTO) VALUES (2,1,'COR');");
        db.execSQL("INSERT INTO TIPO_COMPLEMENTO(EMPRESA, TIPO_COMPLEMENTO, DESCRICAO_TIPO_COMPLEMENTO) VALUES (2,22,'TAMANHO');");
        db.execSQL("INSERT INTO TIPO_COMPLEMENTO(EMPRESA, TIPO_COMPLEMENTO, DESCRICAO_TIPO_COMPLEMENTO) VALUES (2,3,'ESTAMPA');");
        db.execSQL("INSERT INTO TIPO_COMPLEMENTO(EMPRESA, TIPO_COMPLEMENTO, DESCRICAO_TIPO_COMPLEMENTO) VALUES (2,4,'MATERIAL');");
    }
}
