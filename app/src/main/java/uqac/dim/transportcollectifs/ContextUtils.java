package uqac.dim.transportcollectifs;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import java.util.Locale;

public class ContextUtils extends ContextWrapper {
    public ContextUtils(Context base) {
        super(base);
    }

    public static ContextWrapper updateLocale(Context context, Locale locale) {
        Configuration config = context.getResources().getConfiguration();
        config.setLocale(locale);

        return new ContextUtils(context.createConfigurationContext(config));
    }
}
