package br.com.carlosti.enterprise;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import br.com.carlosti.cep.model.CEP;
import br.com.carlosti.cep.model.SimpleCallback;
import br.com.carlosti.cep.service.CEPService;
import br.com.carlosti.constant.Constants;
import br.com.carlosti.data.dao.EnterpriseDAO;
import br.com.carlosti.data.models.Enterprise;
import br.com.carlosti.utils.CNPJValidation;

public class EnterpriseActivity extends AppCompatActivity implements View.OnFocusChangeListener {

    private ViewHolder mViewHolder = new ViewHolder();
    private Integer empresaCode;
    private boolean firstCompany = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enterprise);

        this.mViewHolder.editTextNomeFantasia = findViewById(R.id.nome_fantasia_crud_enterprise);
        this.mViewHolder.editTextRazaoSocial = findViewById(R.id.razao_social_crud_enterprise);
        this.mViewHolder.editTextEndereco = findViewById(R.id.endereco_crud_enterprise);
        this.mViewHolder.editTextBairro = findViewById(R.id.bairro_crud_enterprise);
        this.mViewHolder.editTextCep = findViewById(R.id.cep_crud_enterprise);
        this.mViewHolder.editTextCidade = findViewById(R.id.cidade_crud_enterprise);
        this.mViewHolder.editTextPais = findViewById(R.id.pais_crud_enterprise);
        this.mViewHolder.editTextTelefone = findViewById(R.id.telefone_crud_enterprise);
        this.mViewHolder.editTextFax = findViewById(R.id.fax_crud_enterprise);
        this.mViewHolder.editTextCnpj = findViewById(R.id.cnpj_crud_enterprise);
        this.mViewHolder.editTextIe = findViewById(R.id.ie_crud_enterprise);

        this.mViewHolder.editTextCep.setOnFocusChangeListener(this);

        this.loadDataFromActivity();
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
                Enterprise enterprise = isReadyToSave();
                if (enterprise == null)
                    break;
                this.saveEnterprise(enterprise);
                if (firstCompany) {
                    startActivity(new Intent(this, ChooseEnterpriseActivity.class));
                    finish();
                    break;
                }
                startActivity(new Intent(this, ListEnterpriseActivity.class));
                finish();
                break;
            case R.id.action_cancel:
                if (firstCompany) {
                    startActivity(new Intent(this, ChooseEnterpriseActivity.class));
                    finish();
                    break;
                }
                startActivity(new Intent(this, ListEnterpriseActivity.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v.getId() == R.id.cep_crud_enterprise && !hasFocus) {
            CEPService service = new CEPService(EnterpriseActivity.this);
            service.getCEP(Objects.requireNonNull(this.mViewHolder.editTextCep.getText()).toString(), new SimpleCallback<CEP>() {
                @Override
                public void onResponse(CEP response) {
                    mViewHolder.editTextEndereco.setText(response.getLogradouro());
                    mViewHolder.editTextBairro.setText(response.getBairro());
                    mViewHolder.editTextCidade.setText(response.getLocalidade());
                    System.out.println(response);
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(getBaseContext(), "erro onError: " + error, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private Enterprise isReadyToSave() {
        Enterprise e = new Enterprise();
        e.setNomeFantasia(Objects.requireNonNull(this.mViewHolder.editTextNomeFantasia.getText()).toString());
        e.setRazaoSocial(Objects.requireNonNull(this.mViewHolder.editTextRazaoSocial.getText()).toString());
        e.setEndereco(Objects.requireNonNull(this.mViewHolder.editTextEndereco.getText()).toString());
        e.setBairro(Objects.requireNonNull(this.mViewHolder.editTextBairro.getText()).toString());
        e.setCep(Objects.requireNonNull(this.mViewHolder.editTextCep.getText()).toString());
        e.setCidade(Objects.requireNonNull(this.mViewHolder.editTextCidade.getText()).toString());
        e.setPais(Objects.requireNonNull(this.mViewHolder.editTextPais.getText()).toString());
        e.setTelefone(Objects.requireNonNull(this.mViewHolder.editTextTelefone.getText()).toString());
        e.setFax(Objects.requireNonNull(this.mViewHolder.editTextFax.getText()).toString());
        e.setCnpj(Objects.requireNonNull(this.mViewHolder.editTextCnpj.getText()).toString());
        e.setIe(Objects.requireNonNull(this.mViewHolder.editTextIe.getText()).toString());

        if (!e.getCnpj().equals("") && !CNPJValidation.isCNPJ(e.getCnpj())){
            Toast.makeText(getApplicationContext(), R.string.invalid_cnpj, Toast.LENGTH_LONG).show();
            return null;
        }

        String toastMessage = null;
        if (e.getNomeFantasia().equals(""))
            toastMessage = "Nome Fantasia";
        else if (e.getRazaoSocial().equals(""))
            toastMessage = "Razão Social";
        else if (e.getCnpj().equals(""))
            toastMessage = "CNPJ";
        else if (e.getIe().equals(""))
            toastMessage = "Inscrição Estadual (IE)";
        else if (e.getCep().equals(""))
            toastMessage = "Cep";
        else if (e.getEndereco().equals(""))
            toastMessage = "Endereço";
        else if (e.getBairro().equals(""))
            toastMessage = "Bairro";
        else if (e.getCidade().equals(""))
            toastMessage = "Cidade";
        else if (e.getPais().equals(""))
            toastMessage = "País";
        else if (e.getTelefone().equals(""))
            toastMessage = "Telefone";

        if (toastMessage != null) {
            Toast.makeText(getApplicationContext(), toastMessage+" "+getString(R.string.must_be_filled_up), Toast.LENGTH_LONG).show();
            return null;
        }

        return e;
    }

    private void saveEnterprise(Enterprise enterprise) {
        EnterpriseDAO enterpriseDAO = new EnterpriseDAO(this);
        if (this.empresaCode == null)
            enterpriseDAO.insert(enterprise);
        else {
            enterprise.setEmpresa(this.empresaCode);
            enterpriseDAO.update(enterprise);
        }

    }

    private void loadDataFromDb() {
        if (this.empresaCode == null) return;

        EnterpriseDAO enterpriseDAO = new EnterpriseDAO(this);
        Enterprise enterprise = enterpriseDAO.getById(this.empresaCode);

        if (enterprise == null) return;

        this.mViewHolder.editTextNomeFantasia.setText(enterprise.getNomeFantasia());
        this.mViewHolder.editTextRazaoSocial.setText(enterprise.getRazaoSocial());
        this.mViewHolder.editTextEndereco.setText(enterprise.getEndereco());
        this.mViewHolder.editTextBairro.setText(enterprise.getBairro());
        this.mViewHolder.editTextCep.setText(enterprise.getCep());
        this.mViewHolder.editTextCidade.setText(enterprise.getCidade());
        this.mViewHolder.editTextPais.setText(enterprise.getPais());
        this.mViewHolder.editTextTelefone.setText(enterprise.getTelefone());
        this.mViewHolder.editTextFax.setText(enterprise.getFax());
        this.mViewHolder.editTextCnpj.setText(enterprise.getCnpj());
        this.mViewHolder.editTextIe.setText(enterprise.getIe());
    }

    private void loadDataFromActivity() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (getIntent().hasExtra(Constants.ENTERPRISE_CODE))
                this.empresaCode = extras.getInt(Constants.ENTERPRISE_CODE);

            if (getIntent().hasExtra(Constants.FLAG_FIRST_COMPANY))
                this.firstCompany = extras.getBoolean(Constants.FLAG_FIRST_COMPANY);
        }
    }

    private static class ViewHolder {
        TextInputEditText editTextNomeFantasia;
        TextInputEditText editTextRazaoSocial;
        TextInputEditText editTextEndereco;
        TextInputEditText editTextBairro;
        TextInputEditText editTextCep;
        TextInputEditText editTextCidade;
        TextInputEditText editTextPais;
        TextInputEditText editTextTelefone;
        TextInputEditText editTextFax;
        TextInputEditText editTextCnpj;
        TextInputEditText editTextIe;
    }
}
