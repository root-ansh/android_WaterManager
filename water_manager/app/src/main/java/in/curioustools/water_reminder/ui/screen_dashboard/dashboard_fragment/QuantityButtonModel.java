package in.curioustools.water_reminder.ui.screen_dashboard.dashboard_fragment;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;

import in.curioustools.water_reminder.R;

public class QuantityButtonModel {

    static final QuantityButtonModel QUANTITY_CUP
            = new QuantityButtonModel(R.drawable.ic_container_cup_colored, 150);

    static final QuantityButtonModel QUANTITY_GLASS
            = new QuantityButtonModel(R.drawable.ic_container_glass_colored, 250);

    static final QuantityButtonModel QUANTITY_BOTTLE
            = new QuantityButtonModel(R.drawable.ic_container_bottle_colored, 500);

    static final QuantityButtonModel QUANTITY_THERMOS
            = new QuantityButtonModel(R.drawable.ic_container_thermos_colored, 1000);

    static final QuantityButtonModel QUANTITY_BIG_BOTTLE
            = new QuantityButtonModel(R.drawable.ic_container_big_bottle_colored, 1500);

    static ArrayList<QuantityButtonModel> getDefaultButtonList() {
        return new ArrayList<>(Arrays.asList(
                QUANTITY_BIG_BOTTLE, QUANTITY_BOTTLE, QUANTITY_CUP,
                QUANTITY_GLASS, QUANTITY_THERMOS, QUANTITY_ADD_NEW
        ));
    }


    static final int QUANTITY_MIN = 50;
    static final int QUANTITY_MAX = 2000;

    static final QuantityButtonModel QUANTITY_ADD_NEW
            = new QuantityButtonModel(R.drawable.ic_add_color_prim, -1);


    private int qtyImage, qty;


    QuantityButtonModel(int qtyImage, int qty) {
        this.qtyImage = qtyImage;
        this.qty = qty;
    }

    static int getResForQty(int qty) {
        int resID;
        if (qty <= QUANTITY_CUP.getQty()) resID = QUANTITY_CUP.getQtyImage();
        else if (qty <= QUANTITY_GLASS.getQty()) resID = QUANTITY_GLASS.getQtyImage();
        else if (qty <= QUANTITY_BOTTLE.getQty()) resID = QUANTITY_BOTTLE.getQtyImage();
        else if (qty <= QUANTITY_THERMOS.getQty()) resID = QUANTITY_THERMOS.getQtyImage();
        else resID = QUANTITY_BIG_BOTTLE.getQtyImage();
        return resID;
    }

    int getQtyImage() {
        return qtyImage;
    }

    int getQty() {
        return qty;
    }

//     void setQtyImage(int qtyImage) {
//        this.qtyImage = qtyImage;
//    }
//     void setQty(int qty) {
//        this.qty = qty;
//    }

    @NonNull
    @Override
    public String toString() {
        return "QuantityButtonModel{" +
                "qtyImage=" + qtyImage +
                ", qty=" + qty +
                '}';
    }
}


