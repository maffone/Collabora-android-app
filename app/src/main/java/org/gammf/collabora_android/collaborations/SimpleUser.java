package org.gammf.collabora_android.collaborations;

import org.joda.time.DateTime;

/**
 * @author Manuel Peruzzi
 * Simple class representing a standard user.
 */
public class SimpleUser implements User {

    private final String username;
    private final String email;
    private final String name;
    private final String surname;
    private final DateTime birthday;

    private SimpleUser(final String username, final String email, final String name,
                         final String surname, final DateTime birthday) {
        this.username = username;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.birthday = birthday;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getSurname() {
        return surname;
    }

    @Override
    public DateTime getBirthday() {
        return birthday;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimpleUser that = (SimpleUser) o;

        if (!username.equals(that.username)) return false;
        if (!email.equals(that.email)) return false;
        if (!name.equals(that.name)) return false;
        if (!surname.equals(that.surname)) return false;
        return birthday.equals(that.birthday);

    }

    @Override
    public int hashCode() {
        int result = username.hashCode();
        result = 31 * result + email.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + surname.hashCode();
        result = 31 * result + birthday.hashCode();
        return result;
    }

    /**
     * @author Manuel Peruzzi
     * This nested class is used as a builder to create an object of type SimpleUser
     */
    public static class Builder {

        private String username;
        private String email;
        private String name;
        private String surname;
        private DateTime birthday;

        public Builder username(final String username) {
            this.username = username;
            return this;
        }

        public Builder email(final String email) {
            this.email = email;
            return this;
        }

        public Builder name(final String name) {
            this.name = name;
            return this;
        }

        public Builder surname(final String surname) {
            this.surname = surname;
            return this;
        }

        public Builder birthday(final DateTime birthday) {
            this.birthday = birthday;
            return this;
        }

        public SimpleUser build() {
            return new SimpleUser(username, email, name, surname, birthday);
        }

    }

}

