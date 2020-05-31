import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.lang.annotation.Annotation;

public class FieldInspector implements FieldInspectorInterface{
    List<FieldInfo> privateFields = new ArrayList<>();

    @Override
    public Collection<FieldInfo> inspect(String className) {
        try {
            List<FieldInfo> pola = new ArrayList<FieldInfo>();
            Field[] allFields = Class.forName(className).getDeclaredFields();
            for (Field field: allFields){
                if (Modifier.isPublic(field.getModifiers())){
                    Type type;

                    if(field.getType().toString().equals("int") || field.getType().toString().equals("class java.lang.Integer"))
                        type = Type.INT;

                    else if(field.getType().toString().equals("long") || field.getType().toString().equals("class java.lang.Long"))
                        type = Type.LONG;

                    else if(field.getType().toString().equals("float") || field.getType().toString().equals("class java.lang.Float"))
                        type = Type.FLOAT;

                    else if(field.getType().toString().equals("double") || field.getType().toString().equals("class java.lang.Double"))
                        type = Type.DOUBLE;

                    else
                        type = Type.OTHER;

                    if (0 < field.getAnnotations().length){
                        int wersja = 0;
                        for (Annotation obecne : field.getAnnotations()){
                            if (obecne instanceof FieldVersion){
                                wersja = ((FieldVersion) obecne).version();
                            }
                        }
                        privateFields.add(new FieldInfo(type, field.getName(), wersja));
                    }else
                    privateFields.add(new FieldInfo(type, field.getName()));
                }
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return privateFields;
    }

}