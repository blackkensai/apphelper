package com.lakesidestudio.apphelper.domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by blackkensai on 16-9-28.
 */

public class SearchCondition implements Parcelable {
    public Boolean system;
    public Boolean game;
    public String name;

    public SearchCondition() {
    }

    public SearchCondition(String name, Boolean system, Boolean game) {
        this.name = name;
        this.system = system;
        this.game = game;
    }


    protected SearchCondition(Parcel in) {
        Object[] objects = in.readArray(this.getClass().getClassLoader());
        name = (String) objects[0];
        system = (Boolean) objects[1];
        game = (Boolean) objects[2];
    }

    public static final Creator<SearchCondition> CREATOR = new Creator<SearchCondition>() {
        @Override
        public SearchCondition createFromParcel(Parcel in) {
            return new SearchCondition(in);
        }

        @Override
        public SearchCondition[] newArray(int size) {
            return new SearchCondition[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeArray(new Object[]{name, system, game});
    }
}
