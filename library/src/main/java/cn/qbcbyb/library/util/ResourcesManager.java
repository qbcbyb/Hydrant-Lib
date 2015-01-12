package cn.qbcbyb.library.util;

import cn.qbcbyb.library.app.BaseApplication;

public class ResourcesManager {
    public static enum IdentifierType {
        id, layout, color, drawable, dimen, anim, menu, attr, style, string, integer
    }

    private static int getIdentifier(String name, IdentifierType type, String packageName) {
        BaseApplication application = BaseApplication.getInstance(BaseApplication.class);
        return application.getResources().getIdentifier(name, type.name(), packageName);
    }

    public static int getId(String name, String packageName) {
        return getIdentifier(name, IdentifierType.id, packageName);
    }

    public static int getLayout(String name, String packageName) {
        return getIdentifier(name, IdentifierType.layout, packageName);
    }

    public static int getColor(String name, String packageName) {
        return getIdentifier(name, IdentifierType.color, packageName);
    }

    public static int getDrawable(String name, String packageName) {
        return getIdentifier(name, IdentifierType.drawable, packageName);
    }

    public static int getDimen(String name, String packageName) {
        return getIdentifier(name, IdentifierType.dimen, packageName);
    }

    public static int getAnim(String name, String packageName) {
        return getIdentifier(name, IdentifierType.anim, packageName);
    }

    public static int getMenu(String name, String packageName) {
        return getIdentifier(name, IdentifierType.menu, packageName);
    }

    public static int getAttr(String name, String packageName) {
        return getIdentifier(name, IdentifierType.attr, packageName);
    }

    public static int getString(String name, String packageName) {
        return getIdentifier(name, IdentifierType.string, packageName);
    }

    public static int getStyle(String name, String packageName) {
        return getIdentifier(name, IdentifierType.style, packageName);
    }

    public static int getInteger(String name, String packageName) {
        return getIdentifier(name, IdentifierType.integer, packageName);
    }
}
