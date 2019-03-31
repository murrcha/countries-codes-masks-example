package com.kkaysheva.countrycodeexample.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kkaysheva.countrycodeexample.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.CountryHolder> {

    private List<Country> countries = new ArrayList<>();

    @NonNull
    private final OnItemClickListener listener;

    public CountryAdapter(@NonNull OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public CountryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.country_item, parent, false);
        return new CountryHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull CountryHolder holder, int position) {
        holder.bind(countries.get(position));
    }

    @Override
    public int getItemCount() {
        return countries.size();
    }

    public void setCountries(@NonNull List<Country> countries) {
        this.countries = new ArrayList<>(countries);
        notifyDataSetChanged();
    }

    class CountryHolder extends RecyclerView.ViewHolder {

        private TextView countyName;
        private TextView countryCode;
        private View item;

        @NonNull
        private final OnItemClickListener listener;

        public CountryHolder(@NonNull View itemView,
                             @NonNull OnItemClickListener listener) {
            super(itemView);
            this.listener = listener;
            item = itemView;
            item.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Country country = countries.get(position);
                    listener.onItemClicked(country);
                }
            });
            countyName = itemView.findViewById(R.id.country_name);
            countryCode = itemView.findViewById(R.id.country_code);
        }

        public void bind(@NonNull Country country) {
            countyName.setText(country.getName());
            countryCode.setText(country.getDialCode());
        }
    }

    public interface OnItemClickListener {
        void onItemClicked(Country country);
    }
}
