package sep.dgesui.cmiapp;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class SeccionValoresFragment {

    @Test
    public void probarPorcentajeFormat() {
        String valor = porcentajeFormat("89.47368421052632");
        Assert.assertEquals(valor,"89.47");
    }

    private String porcentajeFormat(String number){
        double doubleNumber = Double.parseDouble(number);
        return String.format("%.2f",doubleNumber);
    }
}
