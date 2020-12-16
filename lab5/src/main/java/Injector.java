import java.io.IOException;
import java.util.Properties;

public class Injector {

    public static <T> T inject(T obj) {

        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        final String PROPS_FILE_NAME = "inject.properties";

        Properties prop = new Properties();
        try {
            prop.load(classloader.getResourceAsStream(PROPS_FILE_NAME));
        } catch (IOException e) {
            e.printStackTrace();
        }

        var fields = obj.getClass().getDeclaredFields();
        for (var field : fields) {
            String className = field.getAnnotatedType().getType().getTypeName();
            if (field.isAnnotationPresent(AutoInjectable.class) && prop.containsKey(className)) {
                try {
                    Object val = Class.forName(String.valueOf(prop.get(className))).getConstructor().newInstance();
                    boolean isAccessible = field.canAccess(obj);
                    field.setAccessible(true);
                    field.set(obj, val);
                    field.setAccessible(isAccessible);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

        return obj;
    }
}
