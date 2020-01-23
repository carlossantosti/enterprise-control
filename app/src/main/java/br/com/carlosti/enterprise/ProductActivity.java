package br.com.carlosti.enterprise;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import br.com.carlosti.constant.Constants;
import br.com.carlosti.data.dao.ProductDAO;
import br.com.carlosti.data.dao.ProductGroupDAO;
import br.com.carlosti.data.models.Product;
import br.com.carlosti.data.models.ProductGroup;

import static java.lang.Integer.parseInt;
import static java.util.Objects.requireNonNull;

public class ProductActivity extends AppCompatActivity {

    private ViewHolder mViewHolder = new ViewHolder();
    private String productCode;
    private Integer enterpriseCode;
    private List<String> productGroupDescList;
    private List<ProductGroup> productGroupList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        this.mViewHolder.editTextProduto = findViewById(R.id.produto_crud_product);
        this.mViewHolder.editTextDescricao = findViewById(R.id.descricao_crud_product);
        this.mViewHolder.editTextApelido = findViewById(R.id.apelido_crud_product);
        this.mViewHolder.autoCompleteTextViewGrupo = findViewById(R.id.grupo_crud_product);
        this.mViewHolder.editTextSubGrupo = findViewById(R.id.sub_grupo_crud_product);
        this.mViewHolder.editTextSituacao = findViewById(R.id.situacao_crud_product);
        this.mViewHolder.editTextPeso = findViewById(R.id.peso_crud_product);
        this.mViewHolder.editTextClassificacao = findViewById(R.id.classificacao_crud_product);
        this.mViewHolder.editTextCodigoBarras = findViewById(R.id.codigo_barras_crud_product);
        this.mViewHolder.editTextColecao = findViewById(R.id.colecao_crud_product);

        this.loadDataFromActivity();
        if (this.productCode != null) {
            this.mViewHolder.editTextProduto.setText(this.productCode);
            this.mViewHolder.editTextProduto.setEnabled(false);
        }
        this.loadDataFromDb();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.custom_menu_edit_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                if (this.enterpriseCode == null) {
                    Toast.makeText(getApplicationContext(), getString(R.string.error_getenterprise), Toast.LENGTH_LONG).show();
                    startActivity(new Intent(this, ChooseEnterpriseActivity.class));
                    finish();
                    break;
                }
                Product product = isReadyToSave();
                if (product == null)
                    break;
                this.saveProduct(product);
                Intent intent = new Intent(this, ListProductsActivity.class);
                intent.putExtra(Constants.ENTERPRISE_CODE, this.enterpriseCode);
                startActivity(intent);
                finish();
                break;
            case R.id.action_cancel:
                Intent intentCancel = new Intent(this, ListProductsActivity.class);
                intentCancel.putExtra(Constants.ENTERPRISE_CODE, this.enterpriseCode);
                startActivity(intentCancel);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private Product isReadyToSave() {
        Product p = new Product();

        if (this.productCode == null)
            p.setProduto(requireNonNull(this.mViewHolder.editTextProduto.getText()).toString());
        else
            p.setProduto(this.productCode);

        ProductGroup pg = this.productGroupList.stream()
                .filter(pg1 -> {
                    if (pg1.getDescricaoGrupoProduto().equals(this.mViewHolder.autoCompleteTextViewGrupo.getText().toString()))
                        return true;
                    return false;
                }).findAny()
                .orElse(null);


        p.setEmpresa(this.enterpriseCode);
        p.setDescricaoProduto(requireNonNull(this.mViewHolder.editTextDescricao.getText()).toString());
        p.setApelidoProduto(requireNonNull(this.mViewHolder.editTextApelido.getText()).toString());
        if (pg != null && pg.getGrupoProduto() != null)
            p.setGrupoProduto(pg.getGrupoProduto());
        if (!requireNonNull(this.mViewHolder.editTextSubGrupo.getText()).toString().equals(""))
            p.setSubGrupoProduto(requireNonNull(parseInt(requireNonNull(this.mViewHolder.editTextSubGrupo.getText()).toString())));
        p.setSituacao(requireNonNull(this.mViewHolder.editTextSituacao.getText()).toString());
        if (!requireNonNull(this.mViewHolder.editTextPeso.getText()).toString().equals(""))
            p.setPesoLiquido(Objects.<Double>requireNonNull(Double.parseDouble(requireNonNull(this.mViewHolder.editTextPeso.getText()).toString())));
        p.setClassificacaoFiscal(requireNonNull(this.mViewHolder.editTextClassificacao.getText()).toString());
        p.setCodigoBarras(requireNonNull(this.mViewHolder.editTextCodigoBarras.getText()).toString());
        p.setColecao(requireNonNull(this.mViewHolder.editTextColecao.getText()).toString());

        String toastMessage = null;
        if (this.productCode == null && (p.getProduto() == null || p.getProduto().equals("")))
            toastMessage = getString(R.string.product);
        else if (p.getDescricaoProduto() == null || p.getDescricaoProduto().equals(""))
            toastMessage = getString(R.string.product_description);
        else if (p.getGrupoProduto() == null)
            toastMessage = getString(R.string.product_group);
        else if (p.getSituacao() == null || p.getSituacao().equals(""))
            toastMessage = getString(R.string.situation);

        if (toastMessage != null) {
            Toast.makeText(getApplicationContext(), toastMessage + " " + getString(R.string.must_be_filled_up), Toast.LENGTH_LONG).show();
            return null;
        }

        return p;
    }

    private void saveProduct(Product product) {
        ProductDAO productDAO = new ProductDAO(this);
        if (this.productCode == null)
            productDAO.insert(product);
        else {
            product.setProduto(this.productCode);
            productDAO.update(product);
        }
    }

    @SuppressLint("DefaultLocale")
    private void loadDataFromDb() {
        ProductGroupDAO productGroupDAO = new ProductGroupDAO(this);
        if (this.enterpriseCode != null) {
            this.productGroupList = productGroupDAO.selectAllByEnterprise(this.enterpriseCode);
            this.loadProductGroup(this.productGroupList);
        }
        if (this.productCode == null) return;

        ProductDAO productDAO = new ProductDAO(this);
        Product product = productDAO.getById(this.productCode);

        if (product == null) return;

        Integer selection = this.getPositionGroup(product.getGrupoProduto(), productGroupDAO);
        if (selection != null) {
            this.mViewHolder.autoCompleteTextViewGrupo.setText(this.mViewHolder.autoCompleteTextViewGrupo.getAdapter().getItem(selection).toString(), false);
        }

        this.mViewHolder.editTextProduto.setText(product.getProduto());
        this.mViewHolder.editTextDescricao.setText(product.getDescricaoProduto());
        this.mViewHolder.editTextApelido.setText(product.getApelidoProduto());
        this.mViewHolder.editTextSubGrupo.setText(String.valueOf(product.getSubGrupoProduto()));
        this.mViewHolder.editTextSituacao.setText(product.getSituacao());
        this.mViewHolder.editTextPeso.setText(String.format("%.2f", product.getPesoLiquido()));
        this.mViewHolder.editTextClassificacao.setText(product.getClassificacaoFiscal());
        this.mViewHolder.editTextCodigoBarras.setText(product.getCodigoBarras());
        this.mViewHolder.editTextColecao.setText(product.getColecao());

        productGroupDAO.close();
    }

    private void loadProductGroup(List<ProductGroup> productGroupList) {
        this.productGroupDescList = new ArrayList<>();
        for (ProductGroup pg : productGroupList) {
            this.productGroupDescList.add(pg.getDescricaoGrupoProduto());
        }

        if (this.productGroupDescList.isEmpty())
            return;
        String[] OPTIONS = new String[this.productGroupDescList.size()];
        this.productGroupDescList.toArray(OPTIONS);

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        R.layout.dropdown_sort_list_products,
                        OPTIONS);

        this.mViewHolder.autoCompleteTextViewGrupo.setAdapter(adapter);
    }

    private Integer getPositionGroup(Integer groupProduct, ProductGroupDAO productGroupDAO) {
        ProductGroup productGroup = productGroupDAO.getByPrimaryKeys(groupProduct, this.enterpriseCode);
        if (productGroup != null)
            return this.productGroupDescList.indexOf(productGroup.getDescricaoGrupoProduto());
        return null;
    }

    private void loadDataFromActivity() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (getIntent().hasExtra(Constants.PRODUCT_CODE))
                this.productCode = extras.getString(Constants.PRODUCT_CODE);

            if (getIntent().hasExtra(Constants.ENTERPRISE_CODE))
                this.enterpriseCode = extras.getInt(Constants.ENTERPRISE_CODE);
        }
    }

    private static class ViewHolder {
        TextInputEditText editTextProduto;
        TextInputEditText editTextDescricao;
        TextInputEditText editTextApelido;
        AutoCompleteTextView autoCompleteTextViewGrupo;
        TextInputEditText editTextSubGrupo;
        TextInputEditText editTextSituacao;
        TextInputEditText editTextPeso;
        TextInputEditText editTextClassificacao;
        TextInputEditText editTextCodigoBarras;
        TextInputEditText editTextColecao;
    }
}
