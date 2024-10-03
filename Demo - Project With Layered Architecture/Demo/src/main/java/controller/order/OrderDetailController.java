package controller.order;

import dto.OrderDetail;
import util.CrudUtil;

import java.sql.SQLException;
import java.util.List;

public class OrderDetailController {
    public boolean addOrderDetail(List<OrderDetail> orderDetails) {
        for(OrderDetail orderDetail: orderDetails) {
            boolean isOrderDetailAdd = addOrderDetail(orderDetail);
            if (!isOrderDetailAdd) {
                return false;
            }
        }
        return true;
    }

    public boolean addOrderDetail(OrderDetail orderDetails){
        String SQL ="INSERT INTO orderdetail VALUES(?,?,?,?)";
        try {
            return CrudUtil.execute(SQL,
                    orderDetails.getOrderId(),
                    orderDetails.getItemCode(),
                    orderDetails.getQty(),
                    orderDetails.getDiscount()
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}