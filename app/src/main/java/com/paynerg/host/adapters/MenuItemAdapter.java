package com.paynerg.host.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.paynerg.host.R;
import com.paynerg.host.models.MenuItem;

import java.util.List;

public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.ViewHolder> {

    private List<MenuItem> menu;
    Context context;



    public MenuItemAdapter(Context context, List<MenuItem> menu) {
        this.context = context;
        this.menu = menu;
    }

    public Context getContext() {
        return context;
    }

    public void deleteItem(int position) {
        menu.remove(position);
        notifyItemRemoved(position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layout = R.layout.menu_item;
        View v = LayoutInflater.from(parent.getContext())
                .inflate(layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MenuItem item = menu.get(position);
        holder.setItemName(item.getItemName());
        holder.setDescription(item.getDescription());
    }

    @Override
    public int getItemCount() {
        return menu.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewItemName;
        private TextView textViewDescription;

        public ViewHolder(View menuItemView) {
            super(menuItemView);

            textViewItemName = menuItemView.findViewById(R.id.item_name);
            textViewDescription = menuItemView.findViewById(R.id.description);
        }

        public void setItemName(String itemName) {
            if (textViewItemName == null) return;
            textViewItemName.setText(itemName);
        }

        public void setDescription(String description) {
            if (textViewDescription == null) return;
            textViewDescription.setText(description);
        }
    }
}
