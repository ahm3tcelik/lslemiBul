package com.ahmetc.islemibul;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class Calculate {

    public Calculate() { }

    public int Sonuc(String sayi_islem) {
        double sonuc = 0;
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("rhino");
        try {
            Object result = engine.eval(sayi_islem);
            sonuc = (double)result;
        } catch (ScriptException e) {
            e.printStackTrace();
        }
        return (int) sonuc;
    }
}

