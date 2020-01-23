package br.com.carlosti.enterprise;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.List;

import br.com.carlosti.components.CustomAlertDialog;
import br.com.carlosti.components.CustomListViewEnterpriseAdapter;
import br.com.carlosti.components.CustomRowEnterprise;
import br.com.carlosti.constant.Constants;
import br.com.carlosti.data.dao.EnterpriseDAO;
import br.com.carlosti.data.models.Enterprise;

public class ListEnterpriseActivity extends AppCompatActivity
        implements ListView.OnItemClickListener {

    private Integer empresaCode;
    private Integer enterpriseCode;
    private ViewHolder mViewHolder = new ViewHolder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_enterprise);

        this.mViewHolder.listViewEnterprises = findViewById(R.id.listv_enterprises_list);
        this.mViewHolder.textViewNoData = findViewById(R.id.no_data_enterprise);

        this.mViewHolder.listViewEnterprises.setOnItemClickListener(this);

        this.loadDataFromActivity();
        this.loadDataFromDb();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.loadDataFromDb();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        CustomListViewEnterpriseAdapter.ViewHolder holder = (CustomListViewEnterpriseAdapter.ViewHolder) view.getTag();
        empresaCode = holder.getEmpresaCode();

        this.mViewHolder.radioSelected = setRadioSelected((RadioButton) view.findViewById(R.id.radio_selected));
        if (this.mViewHolder.radioSelected != null)
            this.mViewHolder.radioSelected.setChecked(true);
    }

    private RadioButton setRadioSelected(RadioButton rbSelected) {
        if (this.mViewHolder.radioSelected != null)
            this.mViewHolder.radioSelected.setChecked(false);

        return rbSelected;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.custom_menu_enterprises, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_enterprise:
                startActivity(new Intent(this, EnterpriseActivity.class));
                break;
            case R.id.action_edit_enterprise:
                if (this.empresaCode == null) {
                    Toast.makeText(getApplicationContext(), R.string.select_item_to_edit, Toast.LENGTH_SHORT).show();
                    break;
                }
                Intent intent = new Intent(this, EnterpriseActivity.class);
                intent.putExtra(Constants.ENTERPRISE_CODE, this.empresaCode);
                startActivity(intent);
                break;
            case R.id.action_delete_enterprise:
                if (this.empresaCode == null) {
                    Toast.makeText(getApplicationContext(), R.string.select_item_to_delete, Toast.LENGTH_SHORT).show();
                    break;
                }

                CustomAlertDialog.confirmDialogConstructor(this, getString(R.string.delete_enterprise), getString(R.string.message_delete), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EnterpriseDAO enterpriseDAO = new EnterpriseDAO(ListEnterpriseActivity.this);
                        enterpriseDAO.delete(empresaCode);
                        Toast.makeText(getApplicationContext(), R.string.deleted_success, Toast.LENGTH_SHORT).show();
                        loadDataFromDb();
                    }
                }, null).show();
                break;
            case R.id.action_list_products:
                Intent intentNavigate = new Intent(this, ListProductsActivity.class);
                intentNavigate.putExtra(Constants.ENTERPRISE_CODE, this.enterpriseCode);
                startActivity(intentNavigate);
                break;
            case R.id.action_change_enterprise_enterprises:
                startActivity(new Intent(this, ChooseEnterpriseActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadDataFromActivity() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (getIntent().hasExtra(Constants.ENTERPRISE_CODE))
                this.enterpriseCode = extras.getInt(Constants.ENTERPRISE_CODE);
        }
    }

    private void loadDataFromDb() {
        List<Enterprise> listEnterprises;

        EnterpriseDAO enterpriseDAO = new EnterpriseDAO(this);
        listEnterprises = enterpriseDAO.selectAll();
        enterpriseDAO.close();

        if (listEnterprises == null || listEnterprises.isEmpty()) {
            this.mViewHolder.textViewNoData.setVisibility(View.VISIBLE);
            this.mViewHolder.listViewEnterprises.setVisibility(View.GONE);
            return;
        }

        this.mViewHolder.textViewNoData.setVisibility(View.GONE);
        this.mViewHolder.listViewEnterprises.setVisibility(View.VISIBLE);

        List<CustomRowEnterprise> mRowItems = new ArrayList<>();
        for (Enterprise e : listEnterprises)
            mRowItems.add(new CustomRowEnterprise(e.getEmpresa(), e.getRazaoSocial(), e.getCidade(), null));

        CustomListViewEnterpriseAdapter adapter = new CustomListViewEnterpriseAdapter(this, R.layout.item_list_enterprise, mRowItems);
        this.mViewHolder.listViewEnterprises.setAdapter(adapter);
    }

    private static class ViewHolder {
        ListView listViewEnterprises;
        RadioButton radioSelected;
        MaterialTextView textViewNoData;
    }
}
