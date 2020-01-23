package br.com.carlosti.data.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import br.com.carlosti.constant.Constants;
import br.com.carlosti.data.models.Enterprise;

import static java.util.Objects.requireNonNull;

public class EnterpriseDAO extends SQLiteOpenHelper {

    public EnterpriseDAO(Context context) {
        super(context, Constants.NAME_DB, null, Constants.VERSION_DB);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS EMPRESA (\n" +
                "    EMPRESA                         INTEGER         NOT NULL,\n" +
                "    NOME_FANTASIA                   VARCHAR (30),\n" +
                "    RAZAO_SOCIAL                    VARCHAR (50),\n" +
                "    ENDERECO                        VARCHAR (50),\n" +
                "    BAIRRO                          VARCHAR (30),\n" +
                "    CEP                             VARCHAR (10),\n" +
                "    CIDADE                          VARCHAR (50),\n" +
                "    PAIS                            VARCHAR (50),\n" +
                "    TELEFONE                        VARCHAR (15),\n" +
                "    FAX                             VARCHAR (15),\n" +
                "    CNPJ                            VARCHAR (18),\n" +
                "    IE                              VARCHAR (18),\n" +
                "    CONSTRAINT PK_EMPRESA PRIMARY KEY (\n" +
                "        EMPRESA\n" +
                "    )\n" +
                ");";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public ArrayList<Enterprise> selectAll() {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM EMPRESA";

        ArrayList<Enterprise> listEnterprise = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery(sql, null);
        } catch (SQLiteException e) {
            if (requireNonNull(e.getLocalizedMessage()).contains("no such table: EMPRESA")) {
                this.onCreate(db);
                cursor = db.rawQuery(sql, null);
            }
        }

        if (cursor != null) {
            while (cursor.moveToNext()) {
                listEnterprise.add(getEnterprise(cursor));
            }
            cursor.close();
        }

        return listEnterprise;
    }

    public Enterprise getById(Integer id) {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM EMPRESA WHERE EMPRESA.EMPRESA=?;";

        String[] args = {String.valueOf(id)};

        Cursor cursor = null;
        try {
            cursor = db.rawQuery(sql, args);
        } catch (SQLiteException e) {
            if (requireNonNull(e.getLocalizedMessage()).contains("no such table: EMPRESA")) {
                this.onCreate(db);
                cursor = db.rawQuery(sql, args);
            }
        }

        Enterprise enterprise = null;
        if (cursor != null) {
            if (cursor.moveToNext())
                enterprise = getEnterprise(cursor);
            cursor.close();

            return enterprise;
        }

        return null;
    }

    public void insert(Enterprise enterprise) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = getContentValues(enterprise);

        try {
            long empresa_code = db.insert("EMPRESA", null, cv);
            enterprise.setEmpresa((int) empresa_code);
        } catch (SQLiteException e) {
            if (requireNonNull(e.getLocalizedMessage()).contains("no such table: EMPRESA"))
                this.onCreate(db);
        }
    }

    public void update(Enterprise enterprise) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = getContentValues(enterprise);
        String sql = "EMPRESA = ?";

        String[] args = {String.valueOf(enterprise.getEmpresa())};
        db.update("EMPRESA", cv, sql, args);
    }

    public void delete(Integer empresa) {
        SQLiteDatabase db = getWritableDatabase();

        String sql = "EMPRESA = ?";

        String[] args = {String.valueOf(empresa)};
        db.delete("EMPRESA", sql, args);
    }

    @NotNull
    private ContentValues getContentValues(Enterprise enterprise) {
        ContentValues cv = new ContentValues();
        cv.put("NOME_FANTASIA", enterprise.getNomeFantasia());
        cv.put("RAZAO_SOCIAL", enterprise.getRazaoSocial());
        cv.put("ENDERECO", enterprise.getEndereco());
        cv.put("BAIRRO", enterprise.getBairro());
        cv.put("CEP", enterprise.getCep());
        cv.put("CIDADE", enterprise.getCidade());
        cv.put("PAIS", enterprise.getPais());
        cv.put("TELEFONE", enterprise.getTelefone());
        cv.put("FAX", enterprise.getFax());
        cv.put("CNPJ", enterprise.getCnpj());
        cv.put("IE", enterprise.getIe());
        return cv;
    }

    @NotNull
    private Enterprise getEnterprise(Cursor cursor) {
        Enterprise enterprise = new Enterprise();
        enterprise.setEmpresa(cursor.getInt(cursor.getColumnIndexOrThrow("EMPRESA")));
        enterprise.setNomeFantasia(cursor.getString(cursor.getColumnIndexOrThrow("NOME_FANTASIA")));
        enterprise.setRazaoSocial(cursor.getString(cursor.getColumnIndexOrThrow("RAZAO_SOCIAL")));
        enterprise.setEndereco(cursor.getString(cursor.getColumnIndexOrThrow("ENDERECO")));
        enterprise.setBairro(cursor.getString(cursor.getColumnIndexOrThrow("BAIRRO")));
        enterprise.setCep(cursor.getString(cursor.getColumnIndexOrThrow("CEP")));
        enterprise.setCidade(cursor.getString(cursor.getColumnIndexOrThrow("CIDADE")));
        enterprise.setPais(cursor.getString(cursor.getColumnIndexOrThrow("PAIS")));
        enterprise.setTelefone(cursor.getString(cursor.getColumnIndexOrThrow("TELEFONE")));
        enterprise.setFax(cursor.getString(cursor.getColumnIndexOrThrow("FAX")));
        enterprise.setCnpj(cursor.getString(cursor.getColumnIndexOrThrow("CNPJ")));
        enterprise.setIe(cursor.getString(cursor.getColumnIndexOrThrow("IE")));
        return enterprise;
    }
}
