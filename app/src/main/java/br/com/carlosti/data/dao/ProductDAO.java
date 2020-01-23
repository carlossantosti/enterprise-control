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
import br.com.carlosti.data.models.Product;

import static java.util.Objects.requireNonNull;

public class ProductDAO extends SQLiteOpenHelper {

    public ProductDAO(Context context) {
        super(context, Constants.NAME_DB, null, Constants.VERSION_DB);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS PRODUTO (\n" +
                "    EMPRESA                      INTEGER         NOT NULL,\n" +
                "    PRODUTO                      VARCHAR (15),\n" +
                "    DESCRICAO_PRODUTO            VARCHAR (250),\n" +
                "    APELIDO_PRODUTO              VARCHAR (30),\n" +
                "    GRUPO_PRODUTO                INTEGER,\n" +
                "    SUBGRUPO_PRODUTO             INTEGER,\n" +
                "    SITUACAO                     VARCHAR (1),\n" +
                "    PESO_LIQUIDO                 NUMERIC (11, 3),\n" +
                "    CLASSIFICACAO_FISCAL         VARCHAR (10),\n" +
                "    CODIGO_BARRAS                VARCHAR (50),\n" +
                "    COLECAO                      VARCHAR (100),\n" +
                "    CONSTRAINT PK_PRODUTO PRIMARY KEY (\n" +
                "        EMPRESA,\n" +
                "        PRODUTO\n" +
                "    )\n" +
                ");";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public ArrayList<Product> selectAllByEnterprise(Integer empresaCode) {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM PRODUTO WHERE EMPRESA=?;";

        ArrayList<Product> listProduct = new ArrayList<>();

        String[] args = {String.valueOf(empresaCode)};
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(sql, args);
        } catch (SQLiteException e) {
            if (requireNonNull(e.getLocalizedMessage()).contains("no such table: PRODUTO")) {
                this.onCreate(db);
                cursor = db.rawQuery(sql, args);
            }
        }

        if (cursor != null) {
            while (cursor.moveToNext()) {
                listProduct.add(getProduct(cursor));
            }
            cursor.close();
        }

        return listProduct;
    }

    public Product getById(String id) {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM PRODUTO WHERE PRODUTO.PRODUTO=?;";

        String[] args = {String.valueOf(id)};

        Cursor cursor = null;
        try {
            cursor = db.rawQuery(sql, args);
        } catch (SQLiteException e) {
            if (requireNonNull(e.getLocalizedMessage()).contains("no such table: PRODUTO")) {
                this.onCreate(db);
                cursor = db.rawQuery(sql, args);
            }
        }

        Product product = null;
        if (cursor != null) {
            if (cursor.moveToNext())
                product = getProduct(cursor);
            cursor.close();

            return product;
        }

        return null;
    }

    public void insert(Product product) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = getContentValues(product);

        try {
            long product_code = db.insert("PRODUTO", null, cv);
            product.setProduto(String.valueOf(product_code));
        } catch (SQLiteException e) {
            if (requireNonNull(e.getLocalizedMessage()).contains("no such table: PRODUTO")) {
                this.onCreate(db);
            }
        }
    }

    public void update(Product product) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = getContentValues(product);
        String sql = "PRODUTO = ?";

        String[] args = {String.valueOf(product.getProduto())};
        db.update("PRODUTO", cv, sql, args);
    }

    public void delete(String product) {
        SQLiteDatabase db = getWritableDatabase();

        String sql = "PRODUTO = ?";

        String[] args = {String.valueOf(product)};
        db.delete("PRODUTO", sql, args);
    }

    @NotNull
    private ContentValues getContentValues(Product product) {
        ContentValues cv = new ContentValues();
        cv.put("EMPRESA", product.getEmpresa());
        cv.put("PRODUTO", product.getProduto());
        cv.put("DESCRICAO_PRODUTO", product.getDescricaoProduto());
        cv.put("APELIDO_PRODUTO", product.getApelidoProduto());
        cv.put("GRUPO_PRODUTO", product.getGrupoProduto());
        cv.put("SUBGRUPO_PRODUTO", product.getSubGrupoProduto());
        cv.put("SITUACAO", product.getSituacao());
        cv.put("PESO_LIQUIDO", product.getPesoLiquido());
        cv.put("CLASSIFICACAO_FISCAL", product.getClassificacaoFiscal());
        cv.put("CODIGO_BARRAS", product.getCodigoBarras());
        cv.put("COLECAO", product.getColecao());
        return cv;
    }

    @NotNull
    private Product getProduct(Cursor cursor) {
        Product product = new Product();
        product.setProduto(cursor.getString(cursor.getColumnIndexOrThrow("PRODUTO")));
        product.setEmpresa(cursor.getInt(cursor.getColumnIndexOrThrow("EMPRESA")));
        product.setDescricaoProduto(cursor.getString(cursor.getColumnIndexOrThrow("DESCRICAO_PRODUTO")));
        product.setApelidoProduto(cursor.getString(cursor.getColumnIndexOrThrow("APELIDO_PRODUTO")));
        product.setGrupoProduto(cursor.getInt(cursor.getColumnIndexOrThrow("GRUPO_PRODUTO")));
        product.setSubGrupoProduto(cursor.getInt(cursor.getColumnIndexOrThrow("SUBGRUPO_PRODUTO")));
        product.setSituacao(cursor.getString(cursor.getColumnIndexOrThrow("SITUACAO")));
        product.setPesoLiquido(cursor.getDouble(cursor.getColumnIndexOrThrow("PESO_LIQUIDO")));
        product.setClassificacaoFiscal(cursor.getString(cursor.getColumnIndexOrThrow("CLASSIFICACAO_FISCAL")));
        product.setCodigoBarras(cursor.getString(cursor.getColumnIndexOrThrow("CODIGO_BARRAS")));
        product.setColecao(cursor.getString(cursor.getColumnIndexOrThrow("COLECAO")));
        return product;
    }
}
