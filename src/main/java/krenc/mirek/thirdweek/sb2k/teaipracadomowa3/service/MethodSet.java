package krenc.mirek.thirdweek.sb2k.teaipracadomowa3.service;

import java.lang.reflect.Type;

class MethodSet {
    private String name;
    private Type type;

    public MethodSet(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    public MethodSet() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
