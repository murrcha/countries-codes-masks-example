package com.kkaysheva.countrycodeexample.adapter;

import java.util.Objects;

import androidx.annotation.NonNull;

public final class Country implements Comparable<Country> {
    @NonNull
    private final String name;
    @NonNull
    private final String iso;
    @NonNull
    private final String dialCode;
    @NonNull
    private final String phoneMask;

    public Country(@NonNull String name,
                   @NonNull String iso,
                   @NonNull String dialCode,
                   @NonNull String mask) {
        this.name = name;
        this.iso = iso;
        this.dialCode = dialCode;
        this.phoneMask = mask;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public String getDialCode() {
        return dialCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Country country = (Country) o;
        return Objects.equals(name, country.name) &&
                Objects.equals(iso, country.iso) &&
                Objects.equals(dialCode, country.dialCode) &&
                Objects.equals(phoneMask, country.phoneMask);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, iso, dialCode, phoneMask);
    }

    @Override
    public String toString() {
        return String.format("%s; %s; %s; %s",
                name, iso, dialCode, phoneMask);
    }

    @Override
    public int compareTo(@NonNull Country other) {
        return this.getName().compareTo(other.getName());
    }
}
