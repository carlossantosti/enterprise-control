package br.com.carlosti.enterprise;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import br.com.carlosti.components.CustomAlertDialog;
import br.com.carlosti.components.CustomListViewProductAdapter;
import br.com.carlosti.components.CustomRowProduct;
import br.com.carlosti.constant.Constants;
import br.com.carlosti.data.dao.EnterpriseDAO;
import br.com.carlosti.data.dao.ProductDAO;
import br.com.carlosti.data.dao.ProductGroupDAO;
import br.com.carlosti.data.dao.TypeComplementDAO;
import br.com.carlosti.data.models.Enterprise;
import br.com.carlosti.data.models.Product;
import br.com.carlosti.data.models.ProductGroup;
import br.com.carlosti.data.models.TypeComplement;

public class ListProductsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private String productCode;
    private Integer enterpriseCode;
    private ViewHolder mViewHolder = new ViewHolder();
    private List<CustomRowProduct> mRowItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_products);

        this.mViewHolder.listViewProducts = findViewById(R.id.listv_products);
        this.mViewHolder.autoCompleteTextDropdownSort = findViewById(R.id.filled_exposed_dropdown);
        this.mViewHolder.textViewNoData = findViewById(R.id.no_data_products);
        this.mViewHolder.textViewEnterpriseName = findViewById(R.id.txt_enterprise_name);
        this.mViewHolder.textViewEnterpriseName = findViewById(R.id.txt_enterprise_name);
        this.mViewHolder.editTextSearch = findViewById(R.id.search_product);

        this.mViewHolder.autoCompleteTextDropdownSort.setOnItemClickListener(this);
        this.mViewHolder.listViewProducts.setOnItemClickListener(this);
        this.mViewHolder.editTextSearch.addTextChangedListener(this.textWatcherSearch);

        this.loadSortDropDown();
        this.loadDataFromActivity();
        System.out.println("Código da empresa: " + this.enterpriseCode);
        if (this.enterpriseCode != null)
            this.loadDataFromDb();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {

        System.out.println(parent.getParent().getParent());
        if (parent.getId() == R.id.listv_products) {
            CustomListViewProductAdapter.ViewHolder holder = (CustomListViewProductAdapter.ViewHolder) view.getTag();
            this.productCode = holder.getProductCode();

            this.mViewHolder.radioSelected = setRadioSelected(view.findViewById(R.id.radio_selected_product));
            if (this.mViewHolder.radioSelected != null)
                this.mViewHolder.radioSelected.setChecked(true);
        } else if (view.getId() == -1) {
            this.sortList(position);
        }
    }

    private void sortList(int option) {
        if(this.mRowItems == null || this.mRowItems.isEmpty())
            return;
        if (option == 0) {
            this.mRowItems = this.mRowItems.stream()
                    .sorted(Comparator.comparing(CustomRowProduct::getProduct))
                    .collect(Collectors.toList());
        } else if (option == 1) {
            this.mRowItems = this.mRowItems.stream()
                    .sorted(Comparator.comparing(CustomRowProduct::getDescription))
                    .collect(Collectors.toList());
        }

        CustomListViewProductAdapter adapter = new CustomListViewProductAdapter(this, R.layout.item_list_product, mRowItems);
        this.mViewHolder.listViewProducts.setAdapter(adapter);
    }

    private void filterList(String s) {
        if(this.mRowItems == null || this.mRowItems.isEmpty())
            return;

        List<CustomRowProduct> mRowItemsSearch = this.mRowItems;
        mRowItemsSearch = mRowItemsSearch.stream()
                .filter(p -> {
                    String desc = p.getDescription().toLowerCase();
                    String nick = p.getNickname().toLowerCase();
                    String prod = p.getProduct().toLowerCase();
                    String pg = p.getProductGroup().toLowerCase();
                    String ptc = p.getProductTypeComplement().toLowerCase();
                    System.out.println(p.getProduct()+" - "+p.getDescription()+" - "+p.getProductGroup()+" - "+p.getNickname()+" - "+p.getProductTypeComplement());
                    if (desc.contains(s) || nick.contains(s) || prod.contains(s)
                            || pg.contains(s) || ptc.contains(s)) {
                        return true;
                    }
                    return false;
                }).collect(Collectors.toList());

        if (mRowItemsSearch == null)
            mRowItemsSearch = new ArrayList();

        CustomListViewProductAdapter adapter = new CustomListViewProductAdapter(this, R.layout.item_list_product, mRowItemsSearch);
        this.mViewHolder.listViewProducts.setAdapter(adapter);
    }

    private RadioButton setRadioSelected(RadioButton rbSelected) {
        if (this.mViewHolder.radioSelected != null)
            this.mViewHolder.radioSelected.setChecked(false);

        return rbSelected;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.custom_menu_products, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_product:
                Intent intentSave = new Intent(this, ProductActivity.class);
                intentSave.putExtra(Constants.ENTERPRISE_CODE, this.enterpriseCode);
                startActivity(intentSave);
                finish();
                break;
            case R.id.action_edit_product:
                if (this.productCode == null) {
                    Toast.makeText(getApplicationContext(), R.string.select_item_to_edit, Toast.LENGTH_SHORT).show();
                    break;
                }
                Intent intentEdit = new Intent(this, ProductActivity.class);
                intentEdit.putExtra(Constants.PRODUCT_CODE, this.productCode);
                intentEdit.putExtra(Constants.ENTERPRISE_CODE, this.enterpriseCode);
                startActivity(intentEdit);
                finish();
                break;
            case R.id.action_delete_product:
                if (this.productCode == null) {
                    Toast.makeText(getApplicationContext(), R.string.select_item_to_delete, Toast.LENGTH_SHORT).show();
                    break;
                }

                CustomAlertDialog.confirmDialogConstructor(this, getString(R.string.delete_product), getString(R.string.message_delete), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ProductDAO productDAO = new ProductDAO(ListProductsActivity.this);
                        productDAO.delete(productCode);
                        Toast.makeText(getApplicationContext(), R.string.deleted_success, Toast.LENGTH_SHORT).show();
                        loadDataFromDb();
                    }
                }, null).show();
                break;
            case R.id.action_list_enterprise:
                Intent intentNavigate = new Intent(this, ListEnterpriseActivity.class);
                intentNavigate.putExtra(Constants.ENTERPRISE_CODE, this.enterpriseCode);
                startActivity(intentNavigate);
                finish();
                break;
            case R.id.action_change_enterprise_products:
                startActivity(new Intent(this, ChooseEnterpriseActivity.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    TextWatcher textWatcherSearch = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override
        public void afterTextChanged(Editable s) {}
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            filterList(s.toString().toLowerCase());
        }
    };

    private void loadDataFromDb() {
        List<Product> listProducts;

        ProductDAO productDAO = new ProductDAO(this);
        listProducts = productDAO.selectAllByEnterprise(this.enterpriseCode);
        productDAO.close();

        if (listProducts == null || listProducts.isEmpty()) {
            this.mViewHolder.textViewNoData.setVisibility(View.VISIBLE);
            this.mViewHolder.listViewProducts.setVisibility(View.GONE);
            return;
        }

        this.mViewHolder.textViewNoData.setVisibility(View.GONE);
        this.mViewHolder.listViewProducts.setVisibility(View.VISIBLE);

        ProductGroupDAO productGroupDAO = new ProductGroupDAO(this);
        TypeComplementDAO typeComplementDAO = new TypeComplementDAO(this);

        mRowItems = new ArrayList<>();
        for (Product p : listProducts) {
            ProductGroup productGroup = productGroupDAO.getByPrimaryKeys(p.getGrupoProduto(), p.getEmpresa());

            String descGroupProduto = "";
            String descTypeComplement = "";
            if (productGroup != null && productGroup.getDescricaoGrupoProduto() != null) {
                descGroupProduto = productGroup.getDescricaoGrupoProduto();
                TypeComplement typeComplement = typeComplementDAO.getByPrimaryKeys(productGroup.getEmpresa(), productGroup.getTipoComplemento());
                if (typeComplement != null && typeComplement.getDescricaoTipoComplemento() != null)
                    descTypeComplement = typeComplement.getDescricaoTipoComplemento();
            }

            mRowItems.add(new CustomRowProduct(p.getProduto(), p.getDescricaoProduto(), p.getApelidoProduto(),
                    descGroupProduto, descTypeComplement, null));
        }

        productDAO.close();
        typeComplementDAO.close();

        CustomListViewProductAdapter adapter = new CustomListViewProductAdapter(this, R.layout.item_list_product, mRowItems);
        this.mViewHolder.listViewProducts.setAdapter(adapter);

        EnterpriseDAO enterpriseDAO = new EnterpriseDAO(this);
        Enterprise enterprise = enterpriseDAO.getById(this.enterpriseCode);
        enterpriseDAO.close();

        if (enterprise != null)
            this.mViewHolder.textViewEnterpriseName.setText(enterprise.getNomeFantasia());
    }


    private void loadDataFromActivity() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (getIntent().hasExtra(Constants.ENTERPRISE_CODE))
                this.enterpriseCode = extras.getInt(Constants.ENTERPRISE_CODE);
        }
    }

    private void loadSortDropDown() {
        String[] OPTIONS = new String[]{"Produto", "Descrição"};

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        R.layout.dropdown_sort_list_products,
                        OPTIONS);

        this.mViewHolder.autoCompleteTextDropdownSort.setAdapter(adapter);
    }

    private static class ViewHolder {
        ListView listViewProducts;
        MaterialTextView textViewNoData;
        TextInputEditText editTextSearch;
        MaterialTextView textViewEnterpriseName;
        RadioButton radioSelected;
        AutoCompleteTextView autoCompleteTextDropdownSort;
    }
}
