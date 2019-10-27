package com.demo.sp19.screen.stock;


import com.demo.architect.data.model.offline.NoteStockModel;
import com.demo.architect.data.model.offline.ProductModel;
import com.demo.architect.data.model.offline.StockModel;
import com.demo.sp19.app.base.BasePresenter;
import com.demo.sp19.app.base.BaseView;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by MSI on 26/11/2017.
 */

public interface StockContract {
    interface View extends BaseView<Presenter> {
        //show list ds stock theo  product
        void showListStock(LinkedHashMap<ProductModel, StockModel> list);

        //show ghi chú của stock
        void showNoteStock(NoteStockModel noteStockModel);
    }

    interface Presenter extends BasePresenter {
        //lấy dánh sách stock theo product by date
        void getListStock();

        //lưu danh sách stock và ghi chú
        void saveListStock(List<StockModel> list, int type);

        //lấy ghi chú của stock
        void getNoteStock();

        //lưu ghi cú của stock
        void saveNoteStock(int type, boolean saveStock);
    }
}
