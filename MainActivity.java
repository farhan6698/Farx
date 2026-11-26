package com.farx.studio;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.*;

public class MainActivity extends Activity {
    private EditText editor;
    private TextView output;
    private static final String SAMPLE =
        "MODE CODE\n" +
        "  PRINT \"Welcome to FARX GOD\"\n" +
        "  LET x = 20\n" +
        "  LET y = 22\n" +
        "  ADD x y\n" +
        "  PRINT \"FARX is running!\"\n" +
        "END MODE\n";

    private int dp(int n) { return (int)(n * getResources().getDisplayMetrics().density); }

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        getWindow().setStatusBarColor(Color.rgb(9, 14, 27));
        getWindow().setNavigationBarColor(Color.rgb(9, 14, 27));

        LinearLayout page = new LinearLayout(this);
        page.setOrientation(LinearLayout.VERTICAL);
        page.setPadding(dp(16), dp(12), dp(16), dp(12));
        page.setBackgroundColor(Color.rgb(9, 14, 27));

        TextView brand = new TextView(this);
        brand.setText("FARX  /  STUDIO");
        brand.setTextColor(Color.rgb(105, 220, 255));
        brand.setTextSize(23);
        brand.setTypeface(Typeface.DEFAULT_BOLD);
        page.addView(brand);

        TextView sub = new TextView(this);
        sub.setText("GOD EDITION  •  Android development preview");
        sub.setTextColor(Color.LTGRAY);
        sub.setTextSize(12);
        sub.setPadding(0, dp(4), 0, dp(12));
        page.addView(sub);

        LinearLayout actions = new LinearLayout(this);
        actions.setOrientation(LinearLayout.HORIZONTAL);
        Button run = button("▶ RUN");
        Button sample = button("LOAD SAMPLE");
        actions.addView(run, new LinearLayout.LayoutParams(0, dp(46), 1));
        LinearLayout.LayoutParams sp = new LinearLayout.LayoutParams(0, dp(46), 1);
        sp.leftMargin = dp(8);
        actions.addView(sample, sp);
        page.addView(actions);

        editor = new EditText(this);
        editor.setGravity(Gravity.TOP | Gravity.START);
        editor.setTypeface(Typeface.MONOSPACE);
        editor.setTextSize(14);
        editor.setTextColor(Color.rgb(230, 239, 255));
        editor.setHintTextColor(Color.GRAY);
        editor.setHint("Write FARX code here...");
        editor.setPadding(dp(12), dp(12), dp(12), dp(12));
        editor.setBackgroundColor(Color.rgb(19, 29, 48));
        editor.setMinLines(12);
        editor.setText(SAMPLE);
        LinearLayout.LayoutParams ep = new LinearLayout.LayoutParams(-1, 0, 1.2f);
        ep.topMargin = dp(12);
        page.addView(editor, ep);

        TextView label = new TextView(this);
        label.setText("CONSOLE OUTPUT");
        label.setTextColor(Color.rgb(105, 220, 255));
        label.setTypeface(Typeface.DEFAULT_BOLD);
        label.setPadding(0, dp(12), 0, dp(6));
        page.addView(label);

        ScrollView scroll = new ScrollView(this);
        output = new TextView(this);
        output.setTypeface(Typeface.MONOSPACE);
        output.setTextSize(13);
        output.setTextColor(Color.rgb(166, 245, 190));
        output.setPadding(dp(12), dp(12), dp(12), dp(12));
        output.setBackgroundColor(Color.rgb(16, 25, 39));
        output.setText("Ready. Press RUN to execute the sample.\n");
        scroll.addView(output);
        page.addView(scroll, new LinearLayout.LayoutParams(-1, 0, 0.7f));

        run.setOnClickListener(v -> execute());
        sample.setOnClickListener(v -> editor.setText(SAMPLE));
        setContentView(page);
    }

    private Button button(String text) {
        Button b = new Button(this);
        b.setText(text);
        b.setTextSize(12);
        b.setTextColor(Color.WHITE);
        b.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.rgb(28, 105, 150)));
        return b;
    }

    private void execute() {
        try {
            FarxEngine.Result result = FarxEngine.run(editor.getText().toString());
            output.setText(result.output + "\n" + result.summary);
        } catch (FarxEngine.FarxException ex) {
            output.setText("FARX ERROR\n" + ex.getMessage());
        } catch (Exception ex) {
            output.setText("INTERNAL ERROR\n" + ex.getClass().getSimpleName() + ": " + ex.getMessage());
        }
    }
}
