package br.com.carlosti.enterprise;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.List;

import br.com.carlosti.components.CustomListViewEnterpriseAdapter;
import br.com.carlosti.components.CustomRowEnterprise;
import br.com.carlosti.constant.Constants;
import br.com.carlosti.data.dao.EnterpriseDAO;
import br.com.carlosti.data.models.Enterprise;

import static android.view.View.*;


public class ChooseEnterpriseActivity extends AppCompatActivity
        implements ListView.OnItemClickListener, OnClickListener {

    private List<CustomRowEnterprise> mRowItems;
    private Integer enterpriseCode;
    private ViewHolder mViewHolder = new ViewHolder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_enterprise);

        this.mViewHolder.listViewEnterprises = findViewById(R.id.listv_enterprises);
        this.mViewHolder.buttonEnter = findViewById(R.id.button_enter);
        this.mViewHolder.textViewNoData = findViewById(R.id.no_data_choose_enterprise);

        this.mViewHolder.listViewEnterprises.setOnItemClickListener(this);
        this.mViewHolder.buttonEnter.setOnClickListener(this);
        this.mViewHolder.textViewNoData.setOnClickListener(this);

        this.loadDataFromDb();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_enter) {
            Toast toast = Toast.makeText(getApplicationContext(), R.string.select_a_enterprise, Toast.LENGTH_SHORT);
            if (this.mViewHolder.radioSelected == null) {
                toast.show();
                return;
            }

            toast.setText(R.string.entering);
            toast.show();

            Intent intent = new Intent(this, ListProductsActivity.class);
            intent.putExtra(Constants.ENTERPRISE_CODE, this.enterpriseCode);
            startActivity(intent);
        } else if (v.getId() == R.id.no_data_choose_enterprise) {
            Intent intent = new Intent(this, EnterpriseActivity.class);
            intent.putExtra(Constants.FLAG_FIRST_COMPANY, true);
            startActivity(intent);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        CustomListViewEnterpriseAdapter.ViewHolder holder = (CustomListViewEnterpriseAdapter.ViewHolder) view.getTag();
        this.enterpriseCode = holder.empresaCode;

        this.mViewHolder.radioSelected = setRadioSelected((RadioButton) view.findViewById(R.id.radio_selected));
        if (this.mViewHolder.radioSelected != null)
            this.mViewHolder.radioSelected.setChecked(true);
    }

    private RadioButton setRadioSelected(RadioButton rbSelected) {
        if (this.mViewHolder.radioSelected != null)
            this.mViewHolder.radioSelected.setChecked(false);

        return rbSelected;
    }

    private void loadDataFromDb() {
        List<Enterprise> listEnterprises = new ArrayList<>();

        EnterpriseDAO enterpriseDAO = new EnterpriseDAO(this);
        listEnterprises = enterpriseDAO.selectAll();
        enterpriseDAO.close();

        if (listEnterprises == null || listEnterprises != null && listEnterprises.isEmpty()) {
            this.mViewHolder.textViewNoData.setVisibility(VISIBLE);
            this.mViewHolder.listViewEnterprises.setVisibility(GONE);
            return;
        }

        this.mViewHolder.textViewNoData.setVisibility(GONE);
        this.mViewHolder.listViewEnterprises.setVisibility(VISIBLE);

        this.mRowItems = new ArrayList<>();
        for (Enterprise e : listEnterprises)
            this.mRowItems.add(new CustomRowEnterprise(e.getEmpresa(), e.getRazaoSocial(), e.getCidade(), null));

        CustomListViewEnterpriseAdapter adapter = new CustomListViewEnterpriseAdapter(this, R.layout.item_list_enterprise, mRowItems);
        this.mViewHolder.listViewEnterprises.setAdapter(adapter);
    }

    private static class ViewHolder {
        ListView listViewEnterprises;
        MaterialButton buttonEnter;
        MaterialTextView textViewNoData;
        RadioButton radioSelected;
    }

}
