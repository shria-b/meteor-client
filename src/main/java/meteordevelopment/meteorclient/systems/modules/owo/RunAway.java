/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package meteordevelopment.meteorclient.systems.modules.owo;

import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;

public class RunAway extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    private final Setting<Exception> exception = sgGeneral.add(new EnumSetting.Builder<Exception>()
        .name("exception")
        .description("the exception to crash game")
        .defaultValue(Exception.NullPoint)
        .build()
    );
    private Object a,b;
    public RunAway() {
        super(Categories.Owo, "RunAway", "crash game");
    }
    @Override
    public void onActivate() {
        switch (exception.get()) {
            case Exception.NullPoint:
                a = null;
                b = a.toString();
                break;
            case Exception.ArithmeticException:
                int division = 1/0;
                break;
            case Exception.IndexOutOfBoundsException:
                int[] array = new int[10];
                array[10] = 1;
                break;
            case Exception.ArrayStoreException:
                Object[] objects = new String[5];
                objects[0] = 1;
                break;
            case Exception.IllegalArgumentException:
                throw new IllegalArgumentException("What Happened There");
        }
    }
    public enum Exception {
        NullPoint,
        ArithmeticException,
        IndexOutOfBoundsException,
        ArrayStoreException,
        IllegalArgumentException
    }
}