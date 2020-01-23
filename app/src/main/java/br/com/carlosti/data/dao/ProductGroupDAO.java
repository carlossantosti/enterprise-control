package br.com.carlosti.data.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import br.com.carlosti.constant.Constants;
import br.com.carlosti.data.models.ProductGroup;

import static java.util.Objects.requireNonNull;

public class ProductGroupDAO extends SQLiteOpenHelper {

    public ProductGroupDAO(Context context) {
        super(context, Constants.NAME_DB, null, Constants.VERSION_DB);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS GRUPO_PRODUTO (\n" +
                "    EMPRESA                       INTEGER         NOT NULL,\n" +
                "    GRUPO_PRODUTO                 INTEGER         NOT NULL,\n" +
                "    DESCRICAO_GRUPO_PRODUTO       VARCHAR (50),\n" +
                "    PERC_DESCONTO                 NUMERIC (5, 2),\n" +
                "    TIPO_COMPLEMENTO              VARCHAR (3), \n" +
                "    CONSTRAINT PK_GRUPO_PRODUTO PRIMARY KEY (\n" +
                "        EMPRESA,\n" +
                "        GRUPO_PRODUTO\n" +
                "    )\n" +
                ");";

        db.execSQL(sql);
        this.insertData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public ArrayList<ProductGroup> selectAllByEnterprise(Integer empresaCode) {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM GRUPO_PRODUTO WHERE EMPRESA=?;";

        ArrayList<ProductGroup> listProductGroup = new ArrayList<>();

        String[] args = {String.valueOf(empresaCode)};
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(sql, args);
        } catch (SQLiteException e) {
            if (requireNonNull(e.getLocalizedMessage()).contains("no such table: GRUPO_PRODUTO")) {
                this.onCreate(db);
                cursor = db.rawQuery(sql, args);
            }
        }

        if (cursor != null) {
            while (cursor.moveToNext()) {
                listProductGroup.add(getProductGroup(cursor));
            }
            cursor.close();
        }

        return listProductGroup;
    }

    public ProductGroup getByPrimaryKeys(Integer groupProduct, Integer empresaCode) {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM GRUPO_PRODUTO WHERE GRUPO_PRODUTO=? AND EMPRESA=?;";

        String[] args = {String.valueOf(groupProduct), String.valueOf(empresaCode)};

        Cursor cursor = null;
        try {
            cursor = db.rawQuery(sql, args);
        } catch (SQLiteException e) {
            if (requireNonNull(e.getLocalizedMessage()).contains("no such table: GRUPO_PRODUTO")) {
                this.onCreate(db);
                cursor = db.rawQuery(sql, args);
            }
        }

        ProductGroup productGroup = null;
        if (cursor != null) {
            if (cursor.moveToNext())
                productGroup = getProductGroup(cursor);
            cursor.close();

            return productGroup;
        }

        return null;
    }

    @NotNull
    private ProductGroup getProductGroup(Cursor cursor) {
        ProductGroup pg = new ProductGroup();
        pg.setGrupoProduto(cursor.getInt(cursor.getColumnIndexOrThrow("GRUPO_PRODUTO")));
        pg.setEmpresa(cursor.getInt(cursor.getColumnIndexOrThrow("EMPRESA")));
        pg.setDescricaoGrupoProduto(cursor.getString(cursor.getColumnIndexOrThrow("DESCRICAO_GRUPO_PRODUTO")));
        pg.setPercDesconto(cursor.getDouble(cursor.getColumnIndexOrThrow("PERC_DESCONTO")));
        pg.setTipoComplemento(cursor.getString(cursor.getColumnIndexOrThrow("TIPO_COMPLEMENTO")));
        return pg;
    }

    private void insertData(SQLiteDatabase db) {
        db.execSQL("INSERT INTO GRUPO_PRODUTO (EMPRESA, GRUPO_PRODUTO, DESCRICAO_GRUPO_PRODUTO, PERC_DESCONTO, TIPO_COMPLEMENTO) VALUES (1, 2, 'TECIDO', NULL, '1');");
        db.execSQL("INSERT INTO GRUPO_PRODUTO (EMPRESA, GRUPO_PRODUTO, DESCRICAO_GRUPO_PRODUTO, PERC_DESCONTO, TIPO_COMPLEMENTO) VALUES (1, 3, 'EMBALAGENS', NULL, '4');");
        db.execSQL("INSERT INTO GRUPO_PRODUTO (EMPRESA, GRUPO_PRODUTO, DESCRICAO_GRUPO_PRODUTO, PERC_DESCONTO, TIPO_COMPLEMENTO) VALUES (1, 4, 'FIBRA', NULL, '3');");
        db.execSQL("INSERT INTO GRUPO_PRODUTO (EMPRESA, GRUPO_PRODUTO, DESCRICAO_GRUPO_PRODUTO, PERC_DESCONTO, TIPO_COMPLEMENTO) VALUES (1, 51, 'FRONHA', NULL, '1');");
        db.execSQL("INSERT INTO GRUPO_PRODUTO (EMPRESA, GRUPO_PRODUTO, DESCRICAO_GRUPO_PRODUTO, PERC_DESCONTO, TIPO_COMPLEMENTO) VALUES (1, 355, 'ALMOFADA', NULL, '3');");
        db.execSQL("INSERT INTO GRUPO_PRODUTO (EMPRESA, GRUPO_PRODUTO, DESCRICAO_GRUPO_PRODUTO, PERC_DESCONTO, TIPO_COMPLEMENTO) VALUES (1, 359, 'DECORAÇAO', NULL, '4');");
        db.execSQL("INSERT INTO GRUPO_PRODUTO (EMPRESA, GRUPO_PRODUTO, DESCRICAO_GRUPO_PRODUTO, PERC_DESCONTO, TIPO_COMPLEMENTO) VALUES (1, 361, 'ELETRONICOS', NULL, '4');");
        db.execSQL("INSERT INTO GRUPO_PRODUTO (EMPRESA, GRUPO_PRODUTO, DESCRICAO_GRUPO_PRODUTO, PERC_DESCONTO, TIPO_COMPLEMENTO) VALUES (1, 363, 'PESSOAL', NULL, '4');");
        db.execSQL("INSERT INTO GRUPO_PRODUTO (EMPRESA, GRUPO_PRODUTO, DESCRICAO_GRUPO_PRODUTO, PERC_DESCONTO, TIPO_COMPLEMENTO) VALUES (1, 383, 'JOGOS', NULL, '3');");
        db.execSQL("INSERT INTO GRUPO_PRODUTO (EMPRESA, GRUPO_PRODUTO, DESCRICAO_GRUPO_PRODUTO, PERC_DESCONTO, TIPO_COMPLEMENTO) VALUES (1, 389, 'TRAVESSEIROS', NULL, '2');");
        db.execSQL("INSERT INTO GRUPO_PRODUTO (EMPRESA, GRUPO_PRODUTO, DESCRICAO_GRUPO_PRODUTO, PERC_DESCONTO, TIPO_COMPLEMENTO) VALUES (2, 40130843, 'ACESSÓRIOS PARA CELULAR', NULL, '12');");
        db.execSQL("INSERT INTO GRUPO_PRODUTO (EMPRESA, GRUPO_PRODUTO, DESCRICAO_GRUPO_PRODUTO, PERC_DESCONTO, TIPO_COMPLEMENTO) VALUES (1, 40130849, 'CARTÃO CD', NULL,'12');");
        db.execSQL("INSERT INTO GRUPO_PRODUTO (EMPRESA, GRUPO_PRODUTO, DESCRICAO_GRUPO_PRODUTO, PERC_DESCONTO, TIPO_COMPLEMENTO) VALUES (2, 40130859, 'MP DIVERSAS', NULL, '2');");
        db.execSQL("INSERT INTO GRUPO_PRODUTO (EMPRESA, GRUPO_PRODUTO, DESCRICAO_GRUPO_PRODUTO, PERC_DESCONTO, TIPO_COMPLEMENTO) VALUES (1, 90294395, 'VESTE  CRIATIVA', NULL,'2');");
        db.execSQL("INSERT INTO GRUPO_PRODUTO (EMPRESA, GRUPO_PRODUTO, DESCRICAO_GRUPO_PRODUTO, PERC_DESCONTO, TIPO_COMPLEMENTO) VALUES (1, 90294397, 'ESCRITORIO', NULL,'1');");
        db.execSQL("INSERT INTO GRUPO_PRODUTO (EMPRESA, GRUPO_PRODUTO, DESCRICAO_GRUPO_PRODUTO, PERC_DESCONTO, TIPO_COMPLEMENTO) VALUES (1, 110359819, 'MASTER COMFORT', NULL, '3');");
        db.execSQL("INSERT INTO GRUPO_PRODUTO (EMPRESA, GRUPO_PRODUTO, DESCRICAO_GRUPO_PRODUTO, PERC_DESCONTO, TIPO_COMPLEMENTO) VALUES (1, 120392521, 'PRODUTOS DE 2ª QUALIDADE', NULL, '4');");
        db.execSQL("INSERT INTO GRUPO_PRODUTO (EMPRESA, GRUPO_PRODUTO, DESCRICAO_GRUPO_PRODUTO, PERC_DESCONTO, TIPO_COMPLEMENTO) VALUES (1, 150490651, 'KIT VIAGEM', NULL,'3');");
        db.execSQL("INSERT INTO GRUPO_PRODUTO (EMPRESA, GRUPO_PRODUTO, DESCRICAO_GRUPO_PRODUTO, PERC_DESCONTO, TIPO_COMPLEMENTO) VALUES (1, 150490653, 'KIT', NULL,'1');");
        db.execSQL("INSERT INTO GRUPO_PRODUTO (EMPRESA, GRUPO_PRODUTO, DESCRICAO_GRUPO_PRODUTO, PERC_DESCONTO, TIPO_COMPLEMENTO) VALUES (1, 150490655, 'COLCHA', NULL,'4');");
        db.execSQL("INSERT INTO GRUPO_PRODUTO (EMPRESA, GRUPO_PRODUTO, DESCRICAO_GRUPO_PRODUTO, PERC_DESCONTO, TIPO_COMPLEMENTO) VALUES (1, 150490657, 'JG DE CAMA SOLTEIRO', NULL,'2');");
        db.execSQL("INSERT INTO GRUPO_PRODUTO (EMPRESA, GRUPO_PRODUTO, DESCRICAO_GRUPO_PRODUTO, PERC_DESCONTO, TIPO_COMPLEMENTO) VALUES (1, 150490661, 'DOCES', NULL,'3');");
        db.execSQL("INSERT INTO GRUPO_PRODUTO (EMPRESA, GRUPO_PRODUTO, DESCRICAO_GRUPO_PRODUTO, PERC_DESCONTO, TIPO_COMPLEMENTO) VALUES (1, 511190670, 'KIT PRESENTE', NULL,'4');");
        db.execSQL("INSERT INTO GRUPO_PRODUTO (EMPRESA, GRUPO_PRODUTO, DESCRICAO_GRUPO_PRODUTO, PERC_DESCONTO, TIPO_COMPLEMENTO) VALUES (1, 511190672, 'CORTE MESA', NULL,'2');");
        db.execSQL("INSERT INTO GRUPO_PRODUTO (EMPRESA, GRUPO_PRODUTO, DESCRICAO_GRUPO_PRODUTO, PERC_DESCONTO, TIPO_COMPLEMENTO) VALUES (1, 511190690, 'REFIL', NULL,'1');");
        db.execSQL("INSERT INTO GRUPO_PRODUTO (EMPRESA, GRUPO_PRODUTO, DESCRICAO_GRUPO_PRODUTO, PERC_DESCONTO, TIPO_COMPLEMENTO) VALUES (1, 404, 'CANECAS PERSONALIZADAS', NULL,'2');");
        db.execSQL("INSERT INTO GRUPO_PRODUTO (EMPRESA, GRUPO_PRODUTO, DESCRICAO_GRUPO_PRODUTO, PERC_DESCONTO, TIPO_COMPLEMENTO) VALUES (2, 357, 'CASA', NULL,'3');");
    }
}
