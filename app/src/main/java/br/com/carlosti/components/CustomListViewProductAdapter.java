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

public class CustomListViewProductAdapter extends ArrayAdapter<CustomRowProduct> {

    Context context;

    public CustomListViewProductAdapter(Context context, int resourceId,
                                        List<CustomRowProduct> items) {
        super(context, resourceId, items);
        this.context = context;
    }

    public class ViewHolder {
        TextView textViewProduct;
        TextView textViewDescription;
        TextView textViewNickName;
        TextView textViewProductGroup;
        TextView textViewProductTypeComplement;
        ImageView imageView;
        public String productCode;

        public String getProductCode() {
            return productCode;
        }

        public void setProductCode(String productCode) {
            this.productCode = productCode;
        }
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        CustomRowProduct rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_list_product, null);
            holder = new ViewHolder();

            holder.textViewProduct = convertView.findViewById(R.id.product_listproduct);
            holder.textViewDescription = convertView.findViewById(R.id.description_listproduct);
            holder.textViewNickName = convertView.findViewById(R.id.nickname_listproduct);
            holder.textViewProductGroup = convertView.findViewById(R.id.product_group_listproduct);
            holder.textViewProductTypeComplement = convertView.findViewById(R.id.type_listproduct);
            holder.imageView = convertView.findViewById(R.id.icon_listproduct);

            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.textViewProduct.setText(rowItem.getProduct());
        holder.textViewDescription.setText(rowItem.getDescription());
        holder.textViewNickName.setText(rowItem.getNickname());
        holder.textViewProductGroup.setText(rowItem.getProductGroup());
        holder.textViewProductTypeComplement.setText(rowItem.getProductTypeComplement());

        if (rowItem.getImage() != null)
            holder.imageView = rowItem.getImage();
        holder.setProductCode(rowItem.getProduct());

        return convertView;
    }
}
