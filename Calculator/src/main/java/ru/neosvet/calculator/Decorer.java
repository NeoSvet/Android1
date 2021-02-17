package ru.neosvet.calculator;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

public class Decorer {
    public enum Themes {
        SYSTEM(0), LIGHT(1), DARK(2);
        private int value;

        Themes(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    private final String THEME = "theme";
    private Themes theme;
    private SharedPreferences pref;

    public Decorer(Context context) {
        pref = context.getSharedPreferences("decor", Context.MODE_PRIVATE);
        switch (pref.getInt(THEME, -1)) {
            case 0:
                theme = Themes.SYSTEM;
                break;
            case 1:
                theme = Themes.LIGHT;
                break;
            case 2:
                theme = Themes.DARK;
                break;
            default:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)  //Android 10+
                    theme = Themes.SYSTEM;
                else
                    theme = Themes.LIGHT;
                break;
        }
    }

    public void applyTheme(Activity act) {
        switch (theme) {
            case SYSTEM:
                act.setTheme(R.style.Theme_Android1);
                break;
            case LIGHT:
                act.setTheme(R.style.Theme_Light);
                break;
            case DARK:
                act.setTheme(R.style.Theme_Dark);
                break;
        }
    }

    public Themes getTheme() {
        return theme;
    }

    public void setTheme(Themes theme) {
        this.theme = theme;
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(THEME, theme.getValue());
        editor.apply();
    }
}
