package br.com.carlosti.components;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.carlosti.enterprise.R;

public class CustomListViewEnterpriseAdapter extends ArrayAdapter<CustomRowEnterprise> {

    Context context;

    public CustomListViewEnterpriseAdapter(Context context, int resourceId,
                                           List<CustomRowEnterprise> items) {
        super(context, resourceId, items);
        this.context = context;
    }

    public class ViewHolder {
        TextView txtName;
        TextView txtCity;
        ImageView imageView;
        public Integer empresaCode;

        public Integer getEmpresaCode() {
            return empresaCode;
        }

        public void setEmpresaCode(Integer empresaCode) {
            this.empresaCode = empresaCode;
        }
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        CustomRowEnterprise rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_list_enterprise, null);
            holder = new ViewHolder();
            holder.txtName = convertView.findViewById(R.id.txt_name);
            holder.txtCity = convertView.findViewById(R.id.txt_city);
            holder.imageView = convertView.findViewById(R.id.icon);
            //holder.radioSelected = convertView.findViewById(R.id.radio_selected);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.txtName.setText(rowItem.getName());
        holder.txtCity.setText(rowItem.getCity());
        if (rowItem.getImage() != null)
            holder.imageView = rowItem.getImage();
        holder.setEmpresaCode(rowItem.getEmpresa());

        return convertView;
    }
}
